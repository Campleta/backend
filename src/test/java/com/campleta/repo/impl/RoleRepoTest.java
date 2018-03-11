/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.repo.impl;

import com.campleta.config.DatabaseCfg;
import com.campleta.models.Role;
import com.campleta.repo.interfaces.IRoleRepo;
import javax.persistence.Persistence;
import javax.ws.rs.NotFoundException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasProperty;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Vixo
 */
public class RoleRepoTest {
    
    private IRoleRepo roleRepo;
    private Role role;
    
    public RoleRepoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        roleRepo = new RoleRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_DEV));
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void getRoleByNameSuccessTest() {
        role = roleRepo.getRoleByName("User");
        
        assertThat(role, is(notNullValue()));
        assertThat(role, hasProperty("name", is("User")));
    }
    
    @Test (expected = NotFoundException.class)
    public void getRoleByNameNotExistTest() {
        role = roleRepo.getRoleByName("HAHAH");
    }
}
