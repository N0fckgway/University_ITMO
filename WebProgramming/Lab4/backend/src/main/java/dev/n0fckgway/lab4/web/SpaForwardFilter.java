package dev.n0fckgway.lab4.web;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class SpaForwardFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest req) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        String method = req.getMethod();
        if (!"GET".equalsIgnoreCase(method) && !"HEAD".equalsIgnoreCase(method)) {
            chain.doFilter(request, response);
            return;
        }

        String contextPath = req.getContextPath();
        String requestUri = req.getRequestURI();
        String path = requestUri.substring(contextPath.length());

        if (isApiOrStatic(path)) {
            chain.doFilter(request, response);
            return;
        }

        req.getRequestDispatcher("/index.html").forward(request, response);
    }

    private boolean isApiOrStatic(String path) {
        if (path == null || path.isBlank() || "/".equals(path) || "/index.html".equals(path)) {
            return true;
        }
        if (path.startsWith("/api/") || "/api".equals(path)) {
            return true;
        }
        return path.contains(".");
    }
}
