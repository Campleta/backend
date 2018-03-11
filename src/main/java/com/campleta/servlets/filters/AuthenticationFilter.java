/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.servlets.filters;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Vixo
 */
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"/Logout", "/Portal", "/Portal/Dashboard", "/Profile"})
public class AuthenticationFilter implements Filter {
    
    private ServletContext context;
    private HttpServletRequest req;
    private HttpServletResponse res;
    private static final String cookieName = "campleta";
    
    public AuthenticationFilter() {
    }    
    
    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        req = (HttpServletRequest) request;
        res = (HttpServletResponse) response;
        
        boolean permission = false;
        Cookie[] cookies = req.getCookies();
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(cookieName)) {
                permission = true;
            }
        }
        
        if(permission) {
            chain.doFilter(req, res);
        } else {
            String redirectUrl = res.encodeRedirectURL("login.jsp");
            res.sendRedirect(redirectUrl);
        }
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {        
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {        
        this.context = filterConfig.getServletContext();
        this.context.log("AuthenticationFilter initialized");
    }
    
}
