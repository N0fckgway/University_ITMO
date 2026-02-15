import React, { useEffect, useMemo, useRef, useState} from "react";
import {useNavigate} from "react-router-dom";
import BasicButton from "../atoms/Button";
import Input from "../atoms/Input";
import Label from "../atoms/Label";
import styles from "./MainPage.module.scss";
import Logo from "../atoms/Logo";

const AXIS_VALUES = [-5, -4, -3, -2, -1, 0, 1, 2, 3];
const GRAPH_SIZE = 540;
const GRAPH_CENTER = GRAPH_SIZE / 2;
const GRAPH_MARGIN = 26;
const MAX_ABS_X = 5;

const clamp = (value, min, max) => Math.max(min, Math.min(max, value));


const MainPage = () => {
    const svgRef = useRef(null);
    const navigation = useNavigate()

    const [xValue, setXValue] = useState(null);
    const [yValue, setYValue] = useState("");
    const [rValue, setRValue] = useState(2);
    const [graphR, setGraphR] = useState(2);
    const [results, setResults] = useState([]);
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const [clearing, setClearing] = useState(false);

    const yNumber = Number(yValue);
    const isYValid = yValue !== "" && Number.isFinite(yNumber) && yNumber >= -3 && yNumber <= 3;
    const isRValid = Number.isFinite(rValue) && rValue > 0;
    const canSubmit = xValue !== null && isYValid && isRValid;

    const scale = useMemo(() => (GRAPH_CENTER - GRAPH_MARGIN) / MAX_ABS_X, []);
    const gridStep = useMemo(() => Math.max(24, scale), [scale]);

    useEffect(() => {
        fetchHistory();
    }, []);

    const toPixelX = (x) => GRAPH_CENTER + x * scale;
    const toPixelY = (y) => GRAPH_CENTER - y * scale;

    const normalizeResult = (item) => ({
        ...item,
        x: Number(item.x),
        y: Number(item.y),
        r: Number(item.r),
    });

    const authFetch = async (path, options = {}) => {
        const token = localStorage.getItem("token");
        if (!token) {
            navigation("/Lab4/login");
            throw new Error("Unauthorized");
        }

        const mergedHeaders = {
            ...(options.headers || {}),
            Authorization: `Bearer ${token}`
        };

        const response = await fetch(`/Lab4/api${path}`, {
            ...options,
            headers: mergedHeaders
        });

        if (response.status === 401 || response.status === 403) {
            localStorage.removeItem("token");
            navigation("/Lab4/login");
            throw new Error("Unauthorized");
        }

        return response;
    };

    const fetchHistory = async () => {
        setLoading(true);
        setError("");
        try {
            const response = await authFetch("/points/history");
            if (!response.ok) {
                const text = await response.text();
                throw new Error(text || "Не удалось загрузить историю");
            }
            const history = await response.json();
            setResults(Array.isArray(history) ? history.map(normalizeResult) : []);
        } catch (e) {
            setError(e.message || "Ошибка загрузки истории");
        } finally {
            setLoading(false);
        }
    };

    const submitPoint = async (x, y, fromGraph) => {
        setError("");

        if (!isRValid) {
            setError("R должен быть больше 0");
            return;
        }
        if (!fromGraph && !AXIS_VALUES.includes(x)) {
            setError("X выбирается только кнопками");
            return;
        }
        if (!Number.isFinite(y) || y < -3 || y > 3) {
            setError("Y должен быть в диапазоне [-3; 3]");
            return;
        }

        try {
            const response = await authFetch("/points/check", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({
                    x,
                    y,
                    r: rValue,
                    fromGraph
                })
            });

            if (!response.ok) {
                const text = await response.text();
                throw new Error(text || "Ошибка проверки точки");
            }

            const created = await response.json();
            setResults(prev => [normalizeResult(created), ...prev]);
        } catch (e) {
            setError(e.message || "Ошибка проверки точки");
        }
    };

    const handleFormSubmit = async (e) => {
        e.preventDefault();
        if (!canSubmit) {
            setError("Проверьте X, Y и R");
            return;
        }
        await submitPoint(xValue, yNumber, false);
    };

    const handleGraphClick = async (e) => {
        if (!isRValid) {
            setError("При отрицательном R отправка с графика отключена");
            return;
        }

        const rect = svgRef.current?.getBoundingClientRect();
        if (!rect) {
            return;
        }

        const pixelX = e.clientX - rect.left;
        const pixelY = e.clientY - rect.top;

        const svgX = (pixelX / rect.width) * GRAPH_SIZE;
        const svgY = (pixelY / rect.height) * GRAPH_SIZE;

        const rawX = (svgX - GRAPH_CENTER) / scale;
        const rawY = (GRAPH_CENTER - svgY) / scale;

        const x = Number(clamp(rawX, -5, 3).toFixed(3));
        const y = Number(clamp(rawY, -3, 3).toFixed(3));

        setYValue(String(y));
        await submitPoint(x, y, true);
    };

    const handleRSelect = (value) => {
        setRValue(value);
        if (value > 0) {
            setGraphR(value);
        }
    };

    const handleLogout = async () => {
        try {
            await authFetch("/auth/logout", {method: "POST"});
        } catch (e) {
        } finally {
            localStorage.removeItem("token");
            navigation("/Lab4");
        }
    };

    const handleClearHistory = async () => {
        setError("");
        setClearing(true);
        try {
            const response = await authFetch("/points/history", {method: "DELETE"});
            if (!response.ok && response.status !== 204) {
                const text = await response.text();
                throw new Error(text || "Не удалось очистить историю");
            }
            setResults([]);
        } catch (e) {
            setError(e.message || "Не удалось очистить историю");
        } finally {
            setClearing(false);
        }
    };

    const quarterClipId = "q3-quarter-clip";

    return (
        <main className={styles.main}>
            <header className={styles.header}>
                <h1 className={styles.title}>Проверка попадания точки</h1>
                <BasicButton className={styles.logoutButton} type="button" onClick={handleLogout}>
                    Выйти
                </BasicButton>
            </header>
            <Logo aria-label="InstaMax" onClick={() => navigation('/Lab4')}/>

            <section className={styles.layout}>
                <article className={styles.panel}>
                    <h2 className={styles.panelTitle}>Параметры</h2>

                    <form className={styles.form} onSubmit={handleFormSubmit}>
                        <div className={styles.field}>
                            <Label>X</Label>
                            <div className={styles.buttonGrid}>
                                {AXIS_VALUES.map(value => (
                                    <BasicButton
                                        key={`x-${value}`}
                                        type="button"
                                        className={`${styles.pickButton} ${xValue === value ? styles.pickButtonActive : ""}`}
                                        onClick={() => setXValue(value)}
                                    >
                                        {value}
                                    </BasicButton>
                                ))}
                            </div>
                        </div>

                        <div className={styles.field}>
                            <Label htmlFor="y-input">Y (-3..3)</Label>
                            <Input
                                id="y-input"
                                name="y"
                                value={yValue}
                                onChange={(e) => setYValue(e.target.value)}
                                placeholder="Введите Y"
                            />
                            {!isYValid && yValue !== "" && (
                                <p className={styles.errorText}>Y должен быть числом в диапазоне [-3; 3]</p>
                            )}
                        </div>

                        <div className={styles.field}>
                            <Label>R</Label>
                            <div className={styles.buttonGrid}>
                                {AXIS_VALUES.map(value => (
                                    <BasicButton
                                        key={`r-${value}`}
                                        type="button"
                                        className={`${styles.pickButton} ${rValue === value ? styles.pickButtonActive : ""}`}
                                        onClick={() => handleRSelect(value)}
                                    >
                                        {value}
                                    </BasicButton>
                                ))}
                            </div>
                        </div>

                        <BasicButton className={styles.submitButton} type="submit" disabled={!canSubmit}>
                            Проверить точку
                        </BasicButton>
                    </form>

                    {error && <p className={styles.errorText}>{error}</p>}
                </article>

                <article className={styles.panel}>
                    <h2 className={styles.panelTitle}>График</h2>

                    <svg
                        ref={svgRef}
                        viewBox={`0 0 ${GRAPH_SIZE} ${GRAPH_SIZE}`}
                        className={styles.graph}
                        onClick={handleGraphClick}
                        role="img"
                        aria-label="Координатная плоскость с зоной попадания"
                    >
                        <defs>
                            <pattern id="main-grid" width={gridStep} height={gridStep} patternUnits="userSpaceOnUse">
                                <path d={`M ${gridStep} 0 L 0 0 0 ${gridStep}`} className={styles.gridLine}/>
                            </pattern>
                            <filter id="pointGlowGood" x="-150%" y="-150%" width="400%" height="400%">
                                <feGaussianBlur stdDeviation="2.4" result="blur"/>
                                <feMerge>
                                    <feMergeNode in="blur"/>
                                    <feMergeNode in="SourceGraphic"/>
                                </feMerge>
                            </filter>
                            <filter id="pointGlowBad" x="-150%" y="-150%" width="400%" height="400%">
                                <feGaussianBlur stdDeviation="2.2" result="blur"/>
                                <feMerge>
                                    <feMergeNode in="blur"/>
                                    <feMergeNode in="SourceGraphic"/>
                                </feMerge>
                            </filter>
                            <marker id="arrow" markerWidth="10" markerHeight="10" refX="8" refY="3" orient="auto">
                                <path d="M0,0 L0,6 L9,3 z" className={styles.axisArrow}/>
                            </marker>
                            <clipPath id={quarterClipId}>
                                <rect x="0" y={GRAPH_CENTER} width={GRAPH_CENTER} height={GRAPH_CENTER}/>
                            </clipPath>
                        </defs>

                        <rect x="0" y="0" width={GRAPH_SIZE} height={GRAPH_SIZE} rx="28" className={styles.graphBase}/>
                        <rect x="0" y="0" width={GRAPH_SIZE} height={GRAPH_SIZE} rx="28" fill="url(#main-grid)"/>

                        <g className={styles.area}>
                            <rect
                                x={toPixelX(0)}
                                y={toPixelY(graphR)}
                                width={graphR * scale}
                                height={graphR * scale}
                            />

                            <polygon
                                points={`
                                  ${toPixelX(0)},${toPixelY(0)}
                                  ${toPixelX(graphR)},${toPixelY(0)}
                                  ${toPixelX(0)},${toPixelY(-graphR / 2)}
                                `}
                            />

                            <circle
                                cx={GRAPH_CENTER}
                                cy={GRAPH_CENTER}
                                r={graphR * scale}
                                clipPath={`url(#${quarterClipId})`}
                            />
                        </g>

                        <line
                            x1="18"
                            y1={GRAPH_CENTER}
                            x2={GRAPH_SIZE - 8}
                            y2={GRAPH_CENTER}
                            className={styles.axis}
                            markerEnd="url(#arrow)"
                        />
                        <line
                            x1={GRAPH_CENTER}
                            y1={GRAPH_SIZE - 8}
                            x2={GRAPH_CENTER}
                            y2="10"
                            className={styles.axis}
                            markerEnd="url(#arrow)"
                        />

                        <text x={GRAPH_SIZE - 28} y={GRAPH_CENTER - 10} className={styles.axisLabel}>X</text>
                        <text x={GRAPH_CENTER + 14} y={22} className={styles.axisLabel}>Y</text>

                        {[graphR, graphR / 2, -graphR / 2, -graphR].map((value, idx) => (
                            <g key={`tick-y-${idx}`}>
                                <line
                                    x1={GRAPH_CENTER - 7}
                                    y1={toPixelY(value)}
                                    x2={GRAPH_CENTER + 7}
                                    y2={toPixelY(value)}
                                    className={styles.tick}
                                />
                            </g>
                        ))}

                        {[graphR, graphR / 2, -graphR / 2, -graphR].map((value, idx) => (
                            <g key={`tick-x-${idx}`}>
                                <line
                                    x1={toPixelX(value)}
                                    y1={GRAPH_CENTER - 7}
                                    x2={toPixelX(value)}
                                    y2={GRAPH_CENTER + 7}
                                    className={styles.tick}
                                />
                            </g>
                        ))}

                        <text x={toPixelX(graphR) - 8} y={GRAPH_CENTER - 10} className={styles.tickLabel}>{graphR}</text>
                        <text x={toPixelX(graphR / 2) - 10} y={GRAPH_CENTER - 10} className={styles.tickLabel}>{Number((graphR / 2).toFixed(2))}</text>
                        <text x={toPixelX(-graphR / 2) - 20} y={GRAPH_CENTER - 10} className={styles.tickLabel}>{Number((-graphR / 2).toFixed(2))}</text>
                        <text x={toPixelX(-graphR) - 16} y={GRAPH_CENTER - 10} className={styles.tickLabel}>{-graphR}</text>

                        <text x={GRAPH_CENTER + 10} y={toPixelY(graphR) + 4} className={styles.tickLabel}>{graphR}</text>
                        <text x={GRAPH_CENTER + 10} y={toPixelY(graphR / 2) + 4} className={styles.tickLabel}>{Number((graphR / 2).toFixed(2))}</text>
                        <text x={GRAPH_CENTER + 10} y={toPixelY(-graphR / 2) + 4} className={styles.tickLabel}>{Number((-graphR / 2).toFixed(2))}</text>
                        <text x={GRAPH_CENTER + 10} y={toPixelY(-graphR) + 4} className={styles.tickLabel}>{-graphR}</text>

                        {results.map((item, idx) => (
                            <circle
                                key={`point-${item.id ?? `${item.checkedAt}-${idx}`}`}
                                cx={toPixelX(item.x)}
                                cy={toPixelY(item.y)}
                                r="4.5"
                                className={item.hit ? styles.hitPoint : styles.missPoint}
                                filter={item.hit ? "url(#pointGlowGood)" : "url(#pointGlowBad)"}
                            />
                        ))}
                    </svg>
                </article>
            </section>

            <section className={styles.panel}>
                <div className={styles.historyHeader}>
                    <h2 className={styles.panelTitle}>История проверок</h2>
                    <BasicButton
                        type="button"
                        className={styles.clearButton}
                        onClick={handleClearHistory}
                        disabled={clearing || loading || results.length === 0}
                    >
                        {clearing ? "Очистка..." : "Очистить список"}
                    </BasicButton>
                </div>
                {loading ? (
                    <p>Загрузка...</p>
                ) : (
                    <div className={styles.tableWrap}>
                        <table className={styles.table}>
                            <thead>
                            <tr>
                                <th>X</th>
                                <th>Y</th>
                                <th>R</th>
                                <th>Результат</th>
                                <th>Время</th>
                            </tr>
                            </thead>
                            <tbody>
                            {results.length === 0 && (
                                <tr>
                                    <td colSpan="5">Нет результатов</td>
                                </tr>
                            )}
                            {results.map((item, idx) => (
                                <tr key={`row-${item.id ?? `${item.checkedAt}-${idx}`}`}>
                                    <td>{item.x}</td>
                                    <td>{item.y}</td>
                                    <td>{item.r}</td>
                                    <td>{item.hit ? "Попадание" : "Промах"}</td>
                                    <td>{new Date(item.checkedAt).toLocaleString()}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </section>
        </main>
    );
};

export default MainPage;
