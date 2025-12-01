package dev.n0fckgway.lab2.controller;

import dev.n0fckgway.lab2.exceptions.InvalidDataException;
import dev.n0fckgway.lab2.model.AreaChecker;
import dev.n0fckgway.lab2.model.HitResult;
import dev.n0fckgway.lab2.model.Point;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AreaCheckServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        String strX = request.getParameter("x");
        String strY = request.getParameter("y");
        String strR = request.getParameter("r");

        double x, y, r;

        try {
            x = Double.parseDouble(strX);
            y = Double.parseDouble(strY);
            r = Double.parseDouble(strR);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Некорректно введенные параметры!");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        if (!Point.validation(x, y, r)) {
            request.setAttribute("error", "Параметры не проходят валидацию!");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }

        long start = System.nanoTime();
        boolean hit = AreaChecker.checkHit(x, y, r);
        long end = System.nanoTime();

        HitResult hitResult = new HitResult(x, y, r, hit, LocalDateTime.now(), end - start);

        HttpSession httpSession = request.getSession();
        List<HitResult> results = (List<HitResult>) httpSession.getAttribute("results");

        if (results == null) {
            results = new ArrayList<>();
            httpSession.setAttribute("results", results);
        }
        results.add(hitResult);
        httpSession.setAttribute("results", results);
        request.setAttribute("Current Result", hitResult);
        request.setAttribute("results", results);
        request.getRequestDispatcher("/index.jsp").forward(request, response);

    }

}
