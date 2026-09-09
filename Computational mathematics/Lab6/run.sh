#!/usr/bin/env bash
# Build the complete WAR and start a dedicated WildFly instance. Ctrl+C stops it.
set -euo pipefail
cd "$(dirname "$0")"
PROJECT_DIR="$PWD"

if [[ -z "${WILDFLY_HOME:-}" ]]; then
    for candidate in /opt/homebrew/opt/wildfly-as/libexec /usr/local/opt/wildfly-as/libexec /opt/wildfly; do
        if [[ -f "$candidate/bin/standalone.sh" ]]; then
            WILDFLY_HOME="$candidate"
            break
        fi
    done
fi
if [[ -z "${WILDFLY_HOME:-}" || ! -f "$WILDFLY_HOME/bin/standalone.sh" ]]; then
    echo 'WildFly не найден. Укажите путь: WILDFLY_HOME=/путь/wildfly bash run.sh' >&2
    exit 1
fi
WILDFLY_HOME="$(cd "$WILDFLY_HOME" && pwd)"
if [[ ! -d "$WILDFLY_HOME/modules/system/layers/base/jakarta/servlet/api" ]]; then
    echo 'Нужен WildFly 27+ с Jakarta Servlet 6.0 (например, WildFly 39). WildFly 26 с javax.servlet не подходит.' >&2
    exit 1
fi
for tool in java javac npm curl; do
    if ! command -v "$tool" >/dev/null 2>&1; then
        echo "Не найдена команда $tool. Установите JDK 17+ и Node.js 22.12+." >&2
        exit 1
    fi
done

# Port offset isolates this lab from other local WildFly applications.
PORT_OFFSET="${PORT_OFFSET:-100}"
if [[ ! "$PORT_OFFSET" =~ ^[0-9]{1,5}$ ]]; then
    echo 'PORT_OFFSET должен быть целым неотрицательным числом.' >&2
    exit 1
fi
PORT_OFFSET=$((10#$PORT_OFFSET))
HTTP_PORT=$((8080 + PORT_OFFSET))
MANAGEMENT_PORT=$((9990 + PORT_OFFSET))
if (( MANAGEMENT_PORT > 65535 )); then
    echo 'PORT_OFFSET слишком велик: порт превышает 65535.' >&2
    exit 1
fi
for port in "$HTTP_PORT" "$MANAGEMENT_PORT"; do
    if command -v lsof >/dev/null 2>&1 && lsof -nP -iTCP:"$port" -sTCP:LISTEN >/dev/null 2>&1; then
        echo "Порт $port занят. Задайте другой сдвиг, например PORT_OFFSET=200 bash run.sh" >&2
        exit 1
    fi
done

echo 'Сборка React, проверка Java и создание app/build/libs/lab6.war…'
bash ./gradlew --console=plain :app:check :app:war

# All mutable server state belongs to the project, not the shared installation.
RUNTIME_DIR="$PROJECT_DIR/.wildfly"
mkdir -p "$RUNTIME_DIR"
if ! mkdir "$RUNTIME_DIR/run.lock" 2>/dev/null; then
    echo 'Этот экземпляр уже запущен или остался .wildfly/run.lock после аварийного завершения. Остановите его перед повторным запуском.' >&2
    exit 1
fi
SERVER_PID=''
cleanup() {
    if [[ -n "$SERVER_PID" ]] && kill -0 "$SERVER_PID" 2>/dev/null; then
        kill -TERM "$SERVER_PID" 2>/dev/null || true
        wait "$SERVER_PID" 2>/dev/null || true
    fi
    rmdir "$RUNTIME_DIR/run.lock" 2>/dev/null || true
}
trap cleanup EXIT
trap 'exit 130' INT
trap 'exit 143' TERM
mkdir -p "$RUNTIME_DIR/configuration" "$RUNTIME_DIR/deployments" "$RUNTIME_DIR/log"
if [[ ! -f "$RUNTIME_DIR/configuration/lab6.xml" ]]; then
    # Use the bundled profile, not standalone.xml with other labs' deployments.
    cp "$WILDFLY_HOME/standalone/configuration/standalone-full.xml" "$RUNTIME_DIR/configuration/lab6.xml"
    if [[ -f "$WILDFLY_HOME/standalone/configuration/logging.properties" ]]; then
        cp "$WILDFLY_HOME/standalone/configuration/logging.properties" "$RUNTIME_DIR/configuration/logging.properties"
    fi
fi
for realmFile in application-users.properties application-roles.properties mgmt-users.properties mgmt-groups.properties; do
    if [[ ! -f "$RUNTIME_DIR/configuration/$realmFile" ]]; then
        touch "$RUNTIME_DIR/configuration/$realmFile"
    fi
done
cp app/build/libs/lab6.war "$RUNTIME_DIR/deployments/lab6.war"
rm -f "$RUNTIME_DIR/deployments/lab6.war.failed"
touch "$RUNTIME_DIR/deployments/lab6.war.dodeploy"
echo "Запуск WildFly. Журнал: $RUNTIME_DIR/log/console.log"
JBOSS_BASE_DIR="$RUNTIME_DIR" LAUNCH_JBOSS_IN_BACKGROUND=1 \
    bash "$WILDFLY_HOME/bin/standalone.sh" -c lab6.xml -b 127.0.0.1 -bmanagement 127.0.0.1 \
    "-Djboss.socket.binding.port-offset=$PORT_OFFSET" > "$RUNTIME_DIR/log/console.log" 2>&1 &
SERVER_PID=$!
for ((attempt=0; attempt<120; attempt++)); do
    if ! kill -0 "$SERVER_PID" 2>/dev/null; then
        echo 'WildFly завершился с ошибкой:' >&2
        tail -n 35 "$RUNTIME_DIR/log/console.log" >&2
        exit 1
    fi
    if [[ -f "$RUNTIME_DIR/deployments/lab6.war.failed" ]]; then
        cat "$RUNTIME_DIR/deployments/lab6.war.failed" >&2
        exit 1
    fi
    if curl --noproxy '*' --silent --fail --max-time 1 "http://127.0.0.1:$HTTP_PORT/lab6/api/health" >/dev/null; then
        echo "Готово: http://localhost:$HTTP_PORT/lab6/"
        echo 'Ctrl+C — остановить WildFly.'
        wait "$SERVER_PID"
        exit $?
    fi
    sleep 1
done
echo 'WildFly не стал доступен за 120 секунд. Последние сообщения:' >&2
tail -n 35 "$RUNTIME_DIR/log/console.log" >&2
exit 1
