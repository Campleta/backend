/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.repo.impl;

import com.campleta.config.DatabaseCfg;
import com.campleta.models.Reservation;
import com.campleta.models.Stay;
import com.campleta.models.User;
import com.campleta.repo.interfaces.IStayRepo;
import javax.persistence.Persistence;
import javax.transaction.Transactional;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Vixo
 */
@Transactional
public class StayRepoTest {
    
    private IStayRepo stayRepo;
    private Reservation reservation;
    private Stay stay;
    
    public StayRepoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        stayRepo = new StayRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_DEV));
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void createStaySuccessTest() {
        reservation = new Reservation();
        stay = new Stay();
        
        Stay res = stayRepo.createStay(stay);
        assertThat(res, is(notNullValue()));
    }
}
