/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.repo.impl;

import com.campleta.config.DatabaseCfg;
import com.campleta.models.User;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import javax.transaction.Transaction;
import javax.transaction.Transactional;
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
import org.mockito.MockitoAnnotations;

/**
 *
 * @author Vixo
 */
@Transactional
public class UserRepoTest {

    private UserRepo userRepo;
    private EntityManager em;
    private Query query;
    private EntityTransaction transaction;
    
    public UserRepoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userRepo = new UserRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_DEV));
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void getUserByEmailSuccessTest() {
        String email = "test@campleta.com";
        User user = userRepo.getUserByEmail(email);

        assertThat(user, hasProperty("email", is(email)));
    }
    
    @Test (expected = NotFoundException.class)
    public void getUserByEmailWrongEmailTest() {
        String email = "hej@hej.dk";
        userRepo.getUserByEmail(email);
    }
    
    @Test
    public void getUserByPassportSuccessTest() {
        String passport = "12345678";
        User user = userRepo.getUserByPassport(passport);
        
        assertThat(user, hasProperty("passport", is(passport)));
    }
    
    @Test (expected = NotFoundException.class)
    public void getUserByPassportNotExistingTest() {
        String passport = "44444444";
        userRepo.getUserByPassport(passport);
    }
    
    @Test (expected = NotFoundException.class)
    public void getUserByNullPassportTest() {
        String passport = null;
        userRepo.getUserByPassport(passport);
    }
    
    @Test
    public void createUserSuccessTest() {
        String passport = "11111111";
        String firstname = "MyFirstname";
        String lastname = "MyLastname";
        User expected = new User();
        expected.setPassport(passport);
        expected.setFirstname(firstname);
        expected.setLastname(lastname);
        
        User user = userRepo.createUser(expected);
        
        assertThat(user, is(notNullValue()));
        assertThat(user, hasProperty("passport", is(passport)));
        assertThat(user, hasProperty("firstname", is(firstname)));
        assertThat(user, hasProperty("lastname", is(lastname)));
    }
    
    @Test (expected = RollbackException.class)
    public void createUserAlreadyExistTest() {
        String passport = "12345678";
        String firstname = "Test";
        String lastname = "User";
        User user = new User();
        user.setPassport(passport);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        
        userRepo.createUser(user);
    }
}
