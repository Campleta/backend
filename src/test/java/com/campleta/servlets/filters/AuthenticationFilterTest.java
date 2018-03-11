/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.servlets.filters;

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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 *
 * @author Vixo
 */
public class AuthenticationFilterTest {
    
    private AuthenticationFilter authFilter;
    private HttpServletResponse response;
    private HttpServletRequest request;
    private FilterChain chain;
    private Cookie cookie;
    
    public AuthenticationFilterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        initMocks(this);
        request = mock(HttpServletRequest.class);
        response = mock (HttpServletResponse.class);
        chain = mock(FilterChain.class);
        cookie = mock(Cookie.class);
        authFilter = new AuthenticationFilter();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void authenticateSuccessTest() throws IOException, ServletException {
        when(request.getCookies()).thenReturn(new Cookie[] { cookie });
        when(cookie.getName()).thenReturn("campleta");
        
        authFilter.doFilter(request, response, chain);
        
        verify(chain).doFilter(request, response);
    }
    
    @Test
    public void authenticateErrorNoCookieTest() throws IOException, ServletException {
        when(request.getCookies()).thenReturn(new Cookie[] {});
        
        authFilter.doFilter(request, response, chain);
        
        verify(response).sendRedirect(anyString());
    }
}
