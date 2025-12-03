package dev.n0fckgway.lab2.controller;

import dev.n0fckgway.lab2.model.AreaChecker;
import dev.n0fckgway.lab2.model.HitResult;
import dev.n0fckgway.lab2.model.Point;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AreaCheckServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        String[] xValues = request.getParameterValues("x");
        String strY = request.getParameter("y");
        String strR = request.getParameter("r");

        if (xValues == null || xValues.length == 0) {
            request.setAttribute("error", "Нужно выбрать хотя бы одно значение X");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        double y;
        double r;

        try {
            y = Double.parseDouble(strY);
            r = Double.parseDouble(strR);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Некорректно введенные параметры!");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        List<HitResult> newHits = new ArrayList<>();

        for (String xValue : xValues) {
            double x;
            try {
                x = Double.parseDouble(xValue);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Некорректно введенные параметры!");
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

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html><html><body>");
            out.printf("<p>Параметры:" + newHits.get(0).toJson());
            out.println("</body></html>");
        }



        ServletContext servletContext = request.getServletContext();

        @SuppressWarnings("unchecked")
        List<HitResult> results = (List<HitResult>) servletContext.getAttribute("results");

        if (results == null) {
            results = new ArrayList<>();
            servletContext.setAttribute("results", results);
        }

        results.addAll(newHits);
        request.setAttribute("currentResult", newHits.isEmpty() ? null : newHits.get(newHits.size() - 1));
        request.setAttribute("results", results);
        request.getRequestDispatcher("/index.jsp").forward(request, response);

    }

}
