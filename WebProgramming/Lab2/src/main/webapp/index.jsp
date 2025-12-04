<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="static/css/style.css">
    <title>Web-2</title>
</head>
<body>
<div class="app-shell">
    <div class="system-bar">
        <span class="dot"></span>
        <span class="dot"></span>
        <span class="dot"></span>
    </div>

    <header class="lab-header">
        <div>
            <p class="eyebrow">Веб-программирование</p>
            <h1>Вариант 74382</h1>
        </div>
        <h2>Выполнил Суворов Станислав Денисович P3215 <a href="https://github.com/N0fckgway/University_ITMO"
                                                         target="_blank">github</a></h2>
    </header>

    <main class="layout-grid">
        <section class="card graph-card">
            <div class="card-heading">
                <p class="eyebrow">Область попадания</p>
            </div>
            <canvas id="holst" width="400" height="400"></canvas>
        </section>

        <section class="card table-card">
            <div class="card-heading">
                <p class="eyebrow">История запросов</p>
            </div>
            <div class="table-wrapper">
                <table>
                    <thead>
                    <tr>
                        <th>X</th>
                        <th>Y</th>
                        <th>R</th>
                        <th>Hit</th>
                        <th>Request time</th>
                        <th>Exec. time</th>
                    </tr>
                    </thead>
                    <tbody id="table-results">
                    <c:forEach var="res" items="${results}">
                        <tr>
                            <td>${res.x}</td>
                            <td>${res.y}</td>
                            <td>${res.r}</td>
                            <td class="hit ${res.hit ? 'yes' : 'no'}">${res.hit}</td>
                            <td>${res.nowTime}</td>
                            <td>${res.execTime}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="table-footer">
                    <div class="page-block">
                        <p class="label">Страница</p>
                        <div class="pager-inline">
                            <button class="circle-btn ghost-btn" id="prev-page">←</button>
                            <div class="page-indicator">
                                <span class="page-number" id="page-number">1</span>
                            </div>
                            <button class="circle-btn ghost-btn" id="next-page">→</button>
                        </div>
                    </div>
                <div class="select-row">
                    <div class="page-size">
                        <label class="label" for="page-size">Размер таблицы</label>
                        <select id="page-size">
                            <option>5</option>
                            <option selected>10</option>
                            <option>15</option>
                        </select>
                    </div>
                    <button class="btn btn-ghost" id="clear-button" name="action" value="clear">Очистить таблицу</button>
                </div>
            </div>
        </section>

        <section class="card input-card">
            <p class="eyebrow">Расчёт</p>
            <h2 id="header-coordinates" >Выбор координат</h2>
            <form action="${pageContext.request.contextPath}/validationParam" method="get">
            <div class="input-block">
                <p class="label">X:</p>
                <div class="pill-group">
                    <input class="pill-input" type="checkbox" name="x" id="x-4" value="-4">
                    <label class="pill" for="x-4">-4</label>

                    <input class="pill-input" type="checkbox" name="x" id="x-3" value="-3">
                    <label class="pill" for="x-3">-3</label>

                    <input class="pill-input" type="checkbox" name="x" id="x-2" value="-2">
                    <label class="pill" for="x-2">-2</label>

                    <input class="pill-input" type="checkbox" name="x" id="x-1" value="-1">
                    <label class="pill" for="x-1">-1</label>

                    <input class="pill-input" type="checkbox" name="x" id="x0" value="0" checked>
                    <label class="pill" for="x0">0</label>

                    <input class="pill-input" type="checkbox" name="x" id="x1" value="1">
                    <label class="pill" for="x1">1</label>

                    <input class="pill-input" type="checkbox" name="x" id="x2" value="2">
                    <label class="pill" for="x2">2</label>

                    <input class="pill-input" type="checkbox" name="x" id="x3" value="3">
                    <label class="pill" for="x3">3</label>

                    <input class="pill-input" type="checkbox" name="x" id="x4" value="4">
                    <label class="pill" for="x4">4</label>
                    <p class="err" id="x-err"></p>
                </div>
            </div>
            <div class="input-block">
                <label for="y-input" class="label">Y:</label>
                <input name="y" type="text" id="y-input" class="text-input" placeholder="Введите значение: " value="1">
                <p class="err" id="y-err"></p>
            </div>
            <div class="input-block slider-block">
                <label for="r-select" class="label">R:</label>
                <select name="r" id="r-select">
                    <option value="1">1</option>
                    <option value="2" selected>2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                </select>
            </div>
            <button class="btn btn-primary full-width">Отправить запрос</button>
            </form>
        </section>
    </main>
</div>
<script src="static/js/canvas.js"></script>
<script src="static/js/validation.js"></script>
<script src="static/js/technicalButtons.js"></script>
</body>
</html>
