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

        String action = request.getParameter("action");
        if ("clear".equals(action)) {
            request.getRequestDispatcher("/areaCheck?action=clear").forward(request, response);
            return;
        }


        String x = request.getParameter("x");
        String y = request.getParameter("y");
        String r = request.getParameter("r");

        if (x == null || y == null || r == null) {
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        } else request.getRequestDispatcher("/areaCheck").forward(request, response);


    }


}
