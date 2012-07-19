package com.olabini.web;

import java.io.IOException;

import java.util.List;
import static java.util.Collections.list;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;


public class HttpsFilter implements Filter {
    public static class Request extends HttpServletRequestWrapper {
        private final static String HEADER_NAME = "X-Forwarded-Proto";

        private final List<String> protos;
        public Request(HttpServletRequest req) {
            super(req);
            this.protos = list(req.getHeaders(HEADER_NAME));
        }

        @Override
        public String getScheme() {
            if(protos.isEmpty()) {
                return super.getScheme();
            } else {
                return protos.get(0);
            }
        }

        @Override
        public boolean isSecure() {
            if(protos.contains("https")) {
                return true;
            } else {
                return super.isSecure();
            }
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    }

    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new Request(request), response);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            doFilter((HttpServletRequest)request, (HttpServletResponse)response, chain);
        } else {
            chain.doFilter(request, response);
        }
    }
}
