/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.servlets.filters;

import com.campleta.services.AuthenticationService;
import com.campleta.services.interfaces.IAuthentication;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 *
 * @author Vixo
 */
public class CampsiteRelationFilterTest {
    
    private CampsiteRelationFilter campsiteFilter;
    private HttpServletResponse response;
    private HttpServletRequest request;
    private FilterChain chain;
    private Cookie cookie;
    private IAuthentication authService;
    private HttpSession session;
    
    public CampsiteRelationFilterTest() {
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
        authService = mock(AuthenticationService.class);
        session = mock(HttpSession.class);
        campsiteFilter = new CampsiteRelationFilter(authService);
        
        when(request.getSession(false)).thenReturn(session);
        when(cookie.getName()).thenReturn("campleta");
        when(cookie.getValue()).thenReturn("1");
        when(session.getAttribute("campsite")).thenReturn("1");
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void relationSuccessTest() throws IOException, ServletException {
        when(request.getCookies()).thenReturn(new Cookie[] { cookie });
        when(authService.validateCampsiteRelation(anyString(), anyLong())).thenReturn(Boolean.TRUE);
        
        campsiteFilter.doFilter(request, response, chain);
        
        verify(chain).doFilter(request, response);
    }
    
    @Test
    public void relationCampsiteNotExistingTest() throws IOException, ServletException {
        when(request.getCookies()).thenReturn(new Cookie[] { });
        when(authService.validateCampsiteRelation(anyString(), anyLong())).thenReturn(Boolean.FALSE);
        
        campsiteFilter.doFilter(request, response, chain);
        
        verify(response).sendRedirect(anyString());
    }
    
    @Test
    public void relationCampsiteNoSessionTest() throws IOException, ServletException {
        when(request.getCookies()).thenReturn(new Cookie[] { cookie });
        when(request.getSession(false)).thenReturn(null);
        
        campsiteFilter.doFilter(request, response, chain);
        
        verify(response).sendRedirect(anyString());
    }
    
    @Test
    public void relationCampsiteHasNotCampsiteSessionTest() throws IOException, ServletException {
        when(request.getCookies()).thenReturn(new Cookie[] {cookie});
        when(request.getSession(false)).thenThrow(NullPointerException.class);
        
        campsiteFilter.doFilter(request, response, chain);
        
        verify(response).sendRedirect(anyString());
    }
}
