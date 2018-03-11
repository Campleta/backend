/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.services;

import com.campleta.models.Role;
import com.campleta.models.User;
import com.campleta.services.interfaces.IToken;
import com.nimbusds.jose.JOSEException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Matchers.notNull;

/**
 *
 * @author Vixo
 */
public class TokenServiceTest {
    
    private IToken tokenCtrl;

    public TokenServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        this.tokenCtrl = new TokenService();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testCreateTokenSuccess() throws JOSEException {
        User user = new User("test@campleta.com");
        user.addRole(new Role("User"));
        String token = tokenCtrl.createToken(user);
        
        assertThat(token, notNullValue());
    }
}
