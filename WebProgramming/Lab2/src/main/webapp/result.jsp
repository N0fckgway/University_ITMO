<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="static/css/style.css">
    <title>Результаты</title>
</head>
<body>
<div class="app-shell">
    <header class="lab-header">
        <div>
            <p class="eyebrow">Результат проверки</p>
            <h1>AreaCheckServlet</h1>
        </div>
        <a class="btn btn-ghost" href="index.jsp">Новый запрос</a>
    </header>

    <main class="layout-grid">
        <section class="card table-card" style="grid-column: span 12;">
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
                    <tbody>
                    <c:forEach var="res" items="${applicationScope.results}">
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
            <div style="margin-top: 1rem;">
                <p class="eyebrow">Последний результат:</p>
                <c:choose>
                    <c:when test="${not empty applicationScope.lastResult}">
                        <p>Точка (${applicationScope.lastResult.x}, ${applicationScope.lastResult.y}) при R=${applicationScope.lastResult.r}: 
                            <span class="hit ${applicationScope.lastResult.hit ? 'yes' : 'no'}">
                                ${applicationScope.lastResult.hit ? 'попадание' : 'мимо'}
                            </span>
                        </p>
                    </c:when>
                    <c:otherwise>
                        <p>Результатов пока нет.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>
    </main>
</div>
</body>
</html>
