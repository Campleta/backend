/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.servlets.filters;

import com.campleta.repo.impl.UserRepo;
import com.campleta.services.AuthenticationService;
import com.campleta.services.interfaces.IAuthentication;
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
import javax.servlet.http.HttpSession;

/**
 *
 * @author Vixo
 */
@WebFilter(filterName = "CampsiteRelationFilter", urlPatterns = {"/Portal"})
public class CampsiteRelationFilter implements Filter {
    
    private ServletContext context;
    private HttpServletRequest req;
    private HttpServletResponse res;
    private HttpSession session;
    private IAuthentication authService;
    private static final String cookieName = "campleta";
    
    public CampsiteRelationFilter() {
        this.authService = new AuthenticationService(new UserRepo());
    }    
    
    @Inject
    public CampsiteRelationFilter(IAuthentication authService) {
        this.authService = authService;
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
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        try {
            req = (HttpServletRequest) request;
            res = (HttpServletResponse) response;

            Cookie cookie = null;
            for(Cookie c : req.getCookies()) {
                if(c.getName().equals(cookieName)) {
                    cookie = c;
                }
            }

            if(cookie != null) {
                session = req.getSession(false);
                if(session != null) {
                    Long campsite = Long.parseLong(session.getAttribute("campsite").toString());
                    if(campsite != null) {
                        if(authService.validateCampsiteRelation(cookie.getValue(), campsite)) {
                            chain.doFilter(request, response);
                        } else {
                            denieUser(res);
                        }
                    }
                } else {
                    denieUser(res);
                }
            } else {
                denieUser(res);
            }
        } catch (NullPointerException nullEx) {
            denieUser(res);
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
    
    public void denieUser(HttpServletResponse res) throws IOException {
        String redirectUrl = res.encodeRedirectURL("index.jsp");
        res.sendRedirect(redirectUrl);
    }
    
}
