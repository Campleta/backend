/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.servlets;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 *
 * @author Vixo
 */
public class LogoutServletTest {
    
    private LogoutServlet logoutServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Cookie cookie;
    
    public LogoutServletTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        logoutServlet = new LogoutServlet();
        initMocks(this);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        cookie = mock(Cookie.class);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void logoutSuccessTest() throws ServletException, IOException {
        when(request.getCookies()).thenReturn(new Cookie[] { cookie });
        when(cookie.getName()).thenReturn("campleta");
        
        logoutServlet.doPost(request, response);
        
        verify(cookie).setMaxAge(0);
        verify(response).sendRedirect("index.jsp");
    }
    
    @Test
    public void logoutNoCookieTest() throws ServletException, IOException {
        when(request.getCookies()).thenReturn(new Cookie[] {});
        
        logoutServlet.doPost(request, response);
        
        verify(response).sendRedirect("login.jsp");
    }
}
