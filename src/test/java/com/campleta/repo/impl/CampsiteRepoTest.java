/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.repo.impl;

import com.campleta.config.DatabaseCfg;
import com.campleta.models.Campsite;
import com.campleta.repo.interfaces.ICampsiteRepo;
import javax.persistence.Persistence;
import javax.ws.rs.NotFoundException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
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
public class CampsiteRepoTest {
    
    private ICampsiteRepo campsiteRepo;
    
    public CampsiteRepoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        campsiteRepo = new CampsiteRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_DEV));
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void getCampsiteByIdSuccessTest() {
        Campsite result = campsiteRepo.getById(1);
        
        assertThat(result, is(notNullValue()));
        assertThat(result.getName(), is("Marina di Venezia"));
    }
    
    @Test (expected = NotFoundException.class)
    public void getCampsiteByIdNotExistTest() {
        campsiteRepo.getById(800);
    }
    
}
