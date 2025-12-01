package dev.n0fckgway.lab2.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;


public class ControllerServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");


        String x = request.getParameter("x");
        String y = request.getParameter("y");
        String r = request.getParameter("r");

        if (x == null || y == null || r == null) {
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        } else request.getRequestDispatcher("/check-area");


        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html><html><body>");
            out.printf("<p>Параметры: x=%s, y=%s, r=%s</p>%n", x, y, r);
            out.println("</body></html>");
        }


    }


}
