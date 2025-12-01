<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
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
                </table>
            </div>
        </section>

        <section class="card input-card">
            <p class="eyebrow">Расчёт</p>
            <h2>Выбор координат</h2>
            <div class="input-block">
                <p class="label">X:</p>
                <div class="pill-group">
                    <input class="pill" type="checkbox">-4</input>
                    <button class="pill">-3</button>
                    <button class="pill">-2</button>
                    <button class="pill">-1</button>
                    <button class="pill is-active">0</button>
                    <button class="pill">1</button>
                    <button class="pill">2</button>
                    <button class="pill">3</button>
                    <button class="pill">4</button>
                </div>
            </div>
            <div class="input-block">
                <label for="y-input" class="label">Y:</label>
                <input type="text" id="y-input" placeholder="-5 - 3" value="1">
            </div>
            <div class="input-block slider-block">
                <label for="r-select" class="label">R:</label>
                <select name="select" id="r-select">
                    <option value="1" selected>1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                </select>
            </div>
            <button class="btn btn-primary full-width">Отправить запрос</button>
        </section>

        <section class="card pagination-card">
            <div class="pager">
                <button class="circle-btn">←</button>
                <div>
                    <p class="label">Страница</p>
                    <strong>1</strong>
                </div>
                <button class="circle-btn">→</button>
            </div>
            <label class="label" for="page-size">Размер таблицы</label>
            <div class="select-row">
                <select id="page-size">
                    <option>10</option>
                    <option>20</option>
                    <option>50</option>
                </select>
                <button class="btn btn-ghost">Очистить таблицу</button>
            </div>
        </section>
    </main>
</div>
<script src="static/js/canvas.js"></script>
</body>
</html>
