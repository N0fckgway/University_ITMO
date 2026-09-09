package ru.itmo.ode;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/** Lifecycle, HTTP threads and static React assets are managed by WildFly. */
@WebServlet(urlPatterns = {"/api/solve", "/api/health"})
public final class ApiServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getServletPath().equals("/api/health")) {
            send(resp, 200, Map.of("status", "ok"));
        } else {
            resp.setHeader("Allow", "POST");
            send(resp, 405, Map.of("error", "Для расчёта используйте POST."));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!req.getServletPath().equals("/api/solve")) {
            resp.setHeader("Allow", "GET, HEAD");
            send(resp, 405, Map.of("error", "Для проверки сервера используйте GET."));
            return;
        }
        try {
            String type = req.getContentType();
            if (type == null || !type.toLowerCase(Locale.ROOT).split(";", 2)[0].strip().equals("application/x-www-form-urlencoded")) {
                send(resp, 415, Map.of("error", "Ожидаются данные формы application/x-www-form-urlencoded."));
                return;
            }
            byte[] body = req.getInputStream().readNBytes(16385);
            if (body.length > 16384) {
                send(resp, 413, Map.of("error", "Слишком большой запрос. Допустимо 16 КиБ."));
                return;
            }
            Map<String, String> fields = new HashMap<>();
            for (String item : new String(body, StandardCharsets.UTF_8).split("&")) {
                String[] pair = item.split("=", 2);
                if (pair.length != 2) throw new IllegalArgumentException("Некорректный формат формы.");
                String key = URLDecoder.decode(pair[0], StandardCharsets.UTF_8);
                if (fields.put(key, URLDecoder.decode(pair[1], StandardCharsets.UTF_8)) != null)
                    throw new IllegalArgumentException("Поле передано дважды: " + key);
            }
            send(resp, 200, Solver.response(Input.from(fields)));
        } catch (IllegalArgumentException ex) {
            send(resp, 400, Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            getServletContext().log("Ошибка вычисления ОДУ", ex);
            send(resp, 500, Map.of("error", "Внутренняя ошибка. Проверьте журнал WildFly."));
        }
    }

    private static void send(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Cache-Control", "no-store");
        resp.setHeader("X-Content-Type-Options", "nosniff");
        resp.getWriter().write(Json.encode(body));
    }
}
