/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.servlets;

import com.campleta.config.DatabaseCfg;
import com.campleta.repo.impl.UserRepo;
import java.io.IOException;
import javax.persistence.Persistence;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import org.mockito.Spy;

/**
 *
 * @author Vixo
 */
public class LoginServletTest {
    
    private LoginServlet loginServlet;
    private LoginServlet spy;
    
    private HttpServletRequest request;
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private RequestDispatcher dispatcher;
    
    public LoginServletTest() {
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
        response = mock(HttpServletResponse.class);
        loginServlet = new LoginServlet(new UserRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_DEV)));
        spy = spy(loginServlet);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void loginSuccessTest() throws ServletException, IOException {
        String email = "test@campleta.com";
        String password = "1234";
        
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(response.encodeRedirectURL(anyString())).thenReturn(anyString());
        
        loginServlet.doPost(request, response);
        
        verify(request).getParameter("email");
        verify(request).getParameter("password");
        verify(response).addCookie(anyObject());
        verify(response).sendRedirect(anyString());
    }
    
    @Test
    public void loginErrorTest() throws ServletException, IOException {
        String email = "test@campleta.com";
        String password = "hhkb";
        
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        
        spy.doPost(request, response);
        
        verify(request).getParameter("email");
        verify(request).getParameter("password");
        verify(request).setAttribute("loginFailed", true);
        verify(spy).doGet(request, response);
    }
}
