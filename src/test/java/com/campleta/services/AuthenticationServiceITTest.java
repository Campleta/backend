/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.services;

import com.campleta.config.DatabaseCfg;
import com.campleta.models.User;
import com.campleta.repo.impl.UserRepo;
import com.campleta.services.AuthenticationService;
import com.campleta.services.interfaces.IAuthentication;
import javax.persistence.Persistence;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasProperty;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Matchers.isNull;
import static org.mockito.Matchers.notNull;

/**
 *
 * @author Vixo
 */
public class AuthenticationServiceITTest {
    
    private IAuthentication authService;
    
    public AuthenticationServiceITTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        authService = new AuthenticationService(new UserRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_DEV)));
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void loginSuccessTest() {
        String email = "test@campleta.com";
        String password = "1234";
        User user = authService.login(email, password);
        
        assertThat(user, is(notNullValue()));
        assertThat(user, hasProperty("email", is(email)));
    }
    
    @Test(expected = NotFoundException.class)
    public void loginUserNotExistTest() {
        String email = "hej@hej.dk";
        String password = "1234";
        User user = authService.login(email, password);
        
        assertThat(user, is(isNull()));
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
    
    @Test(expected = BadRequestException.class)
    public void loginUserWrongPasswordTest() {
        String email = "test@campleta.com";
        String password = "hahawuhuu";
        authService.login(email, password);
    }
    
    @Test(expected = BadRequestException.class)
    public void loginUserIllegalEmailTest() {
        String email = "hej";
        String password = "1234";
        authService.login(email, password);
    }
}
