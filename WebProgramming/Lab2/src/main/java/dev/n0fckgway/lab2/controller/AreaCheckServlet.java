package dev.n0fckgway.lab2.controller;

import dev.n0fckgway.lab2.model.AreaChecker;
import dev.n0fckgway.lab2.model.HitResult;
import dev.n0fckgway.lab2.model.Point;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AreaCheckServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if ("clear".equals(action)) {
            ServletContext context = getServletContext();
            response.setContentType("text/plain;charset=UTF-8");
            context.removeAttribute("results");
            return;
        }

        String[] xValues = request.getParameterValues("x");
        String strY = request.getParameter("y");
        String strR = request.getParameter("r");

        if (xValues == null || xValues.length == 0) {
            request.setAttribute("xError", "Нужно выбрать хотя бы одно значение X");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        double y;
        double r;

        try {
            y = Double.parseDouble(strY);
            r = Double.parseDouble(strR);

        } catch (NumberFormatException e) {
            request.setAttribute("yError", "Некорректно введенные параметры!");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        List<HitResult> newHits = new ArrayList<>();

        for (String xValue : xValues) {
            double x;
            try {
                x = Double.parseDouble(xValue);
            } catch (NumberFormatException e) {
                request.setAttribute("xError", "Некорректно введенные параметры!");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                return;
            }

            try {
                if (!Point.validation(x, y, r)) {
                    request.setAttribute("error", "Параметры не проходят валидацию!");
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                    return;
                }
            } catch (IllegalArgumentException e) {
                request.setAttribute("error", "Параметры не проходят валидацию!");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                return;
            }

            long start = System.nanoTime();
            boolean hit = AreaChecker.checkHit(x, y, r);
            long end = System.nanoTime();

            newHits.add(new HitResult(x, y, r, hit, LocalDateTime.now(), end - start));
        }



        ServletContext servletContext = request.getServletContext();

        @SuppressWarnings("unchecked")
        List<HitResult> results = (List<HitResult>) servletContext.getAttribute("results");

        if (results == null) {
            results = new ArrayList<>();
            servletContext.setAttribute("results", results);
        }

        results.addAll(newHits);
        servletContext.setAttribute("results", results);
        servletContext.setAttribute("lastResult", newHits.get(newHits.size() - 1));

        String acceptHeader = request.getHeader("Accept");
        String requestedWith = request.getHeader("X-Requested-With");
        boolean wantsJson = (acceptHeader != null && acceptHeader.toLowerCase().contains("application/json"))
                || (requestedWith != null && "xmlhttprequest".equalsIgnoreCase(requestedWith));

        if (wantsJson) {
            response.setContentType("application/json");
            StringBuilder json = new StringBuilder();
            json.append("{\"hits\":[");
            for (int i = 0; i < newHits.size(); i++) {
                HitResult hr = newHits.get(i);
                json.append("{")
                        .append("\"x\":").append(hr.getX()).append(",")
                        .append("\"y\":").append(hr.getY()).append(",")
                        .append("\"r\":").append(hr.getR()).append(",")
                        .append("\"hit\":").append(hr.isHit()).append(",")
                        .append("\"nowTime\":\"").append(hr.getNowTime()).append("\",")
                        .append("\"execTime\":").append(hr.getExecTime())
                        .append("}");
                if (i < newHits.size() - 1) {
                    json.append(",");
                }
            }
            json.append("]}");
            response.getWriter().write(json.toString());
            return;
        }

        response.setContentType("text/html;charset=UTF-8");
        request.getRequestDispatcher("/result.jsp").forward(request, response);


    }



}
