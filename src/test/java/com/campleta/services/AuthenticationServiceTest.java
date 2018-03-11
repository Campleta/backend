/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.services;

import com.campleta.models.User;
import com.campleta.repo.impl.StubUserRepo;
import com.campleta.repo.impl.UserRepo;
import com.campleta.repo.interfaces.IUserRepo;
import com.campleta.services.interfaces.IAuthentication;
import java.security.Principal;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.SecurityContext;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Vixo
 */
public class AuthenticationServiceTest {
    
    private IAuthentication authService;
    private IUserRepo authRepo;
    private SecurityContext securityContext;
    private Principal principal;
    
    public AuthenticationServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        authService = new AuthenticationService(new StubUserRepo());
    }
    
    @After
    public void tearDown() {
    }

    @Test (expected = NotFoundException.class)
    public void loginUserNotExistTest() {
        String email = "null@null.dk";
        String pass = "1234";
        
        User user = authService.login(email, pass);
        
        assertThat(user, is(nullValue()));
    }
    
    @Test
    public void loginSuccessTest() {
        String email = "martin@vixo.dk";
        String pass = "1234";
        
        User user = authService.login(email, pass);
        
        assertThat(user, is(notNullValue()));
        assertThat(user.getEmail(), is(email));
        assertThat(user.getRoles(), contains(hasProperty("name", is("Admin"))));
    }
    
    @Test (expected = BadRequestException.class)
    public void loginUserWithNullEmailTest() {
        String email = null;
        String pass = "1234";
        
        authService.login(email, pass);
    }
    
    @Test (expected = BadRequestException.class)
    public void loginUserWithNullPasswordTest() {
        String email = "martin@vixo.dk";
        String pass = null;
        
        authService.login(email, pass);
    }
    
    @Test (expected = BadRequestException.class)
    public void loginUserWithWrongPasswordTest() {
        String email = "martin@vixo.dk";
        String pass = "null";
        
        authService.login(email, pass);
    }
    
    @Test (expected = BadRequestException.class)
    public void loginUserIllegalEmailTest() {
        String email = "hej";
        String pass = "1234";
        
        authService.login(email, pass);
    }
    
    @Test
    public void reAuthenticateTest() {
        securityContext = mock(SecurityContext.class);
        principal = mock(Principal.class);
        authRepo = mock(IUserRepo.class);
        IAuthentication auth = new AuthenticationService(authRepo);
        
        User expectedUser = new User();
        expectedUser.setEmail("martin@vixo.dk");
       
        when(securityContext.getUserPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn("martin@vixo.dk");
        when(authRepo.getUserByEmail(anyString())).thenReturn(expectedUser);
        
        User user = auth.reAuthenticate(securityContext);
        assertThat(user, is(notNullValue()));
        assertThat(user.getEmail(), is("martin@vixo.dk"));
    }
    
    @Test (expected = NotAuthorizedException.class)
    public void reAuthenticateUserNotExistTest() {
        securityContext = mock(SecurityContext.class);
        principal = mock(Principal.class);
        authRepo = mock(IUserRepo.class);
        IAuthentication auth = new AuthenticationService(authRepo);
        
        when(securityContext.getUserPrincipal()).thenReturn(null);
        
        auth.reAuthenticate(securityContext);
    }
    
    @Test (expected = NotFoundException.class)
    public void reAuthenticateUserAuthenticatedButDoesNotExistTest() {
        securityContext = mock(SecurityContext.class);
        principal = mock(Principal.class);
        authRepo = mock(IUserRepo.class);
        IAuthentication auth = new AuthenticationService(authRepo);
        
        when(securityContext.getUserPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn("martin@vixo.dk");
        when(authRepo.getUserByEmail(anyString())).thenThrow(NotFoundException.class);
        
        auth.reAuthenticate(securityContext);
    }
}
