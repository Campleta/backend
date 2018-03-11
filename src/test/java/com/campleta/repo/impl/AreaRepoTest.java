/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.repo.impl;

import com.campleta.config.DatabaseCfg;
import com.campleta.models.Area;
import com.campleta.models.AreaType;
import com.campleta.models.Campsite;
import com.campleta.repo.impl.AreaRepoTest.FindAmountAvailableAreasByAreaTypeTest;
import com.campleta.repo.impl.AreaRepoTest.GetAreaTypeTest;
import com.campleta.repo.impl.AreaRepoTest.GetAreaTypesFromCampsiteTest;
import com.campleta.repo.impl.AreaRepoTest.GetAreasFromCampsiteTest;
import com.campleta.repo.interfaces.IAreaRepo;
import com.campleta.repo.interfaces.ICampsiteRepo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.NotFoundException;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasProperty;

import com.google.gson.JsonObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Suite;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 *
 * @author Vixo
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ GetAreaTypeTest.class, FindAmountAvailableAreasByAreaTypeTest.class, 
    GetAreaTypesFromCampsiteTest.class, GetAreasFromCampsiteTest.class })
public class AreaRepoTest {

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @RunWith(value = Parameterized.class)
    public static class FindAmountAvailableAreasByAreaTypeTest {

        private EntityManagerFactory emf;
        private EntityManager em;
        private AreaRepo areaRepo;
        private int input;
        private int expected;

        public FindAmountAvailableAreasByAreaTypeTest(int input, int expected) {
            this.input = input;
            this.expected = expected;
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
            //emf = Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_DEV);
            //em = emf.createEntityManager();
            areaRepo = new AreaRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_DEV));
        }

        @After
        public void tearDown() {
        }

        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                {1, 10},
                {2, 5},
                {3, 5},
                {0, 0},
                {4, 0},
                {-1, 0}
            });
        }

        @Test
        public void findAmountAvailableAreasByAreaTypeTest() {
            int res = areaRepo.findAmountAvailableAreasByAreaType(input);
            assertEquals(expected, res);
        }
    }

    public static class GetAreaTypeTest {

        private IAreaRepo areaRepo;
        private EntityManagerFactory emf;
        private EntityManager em;
        private AreaType result;

        @BeforeClass
        public static void setUpClass() {
        }

        @AfterClass
        public static void tearDownClass() {
        }

        @Before
        public void setUp() {
            initMocks(this);
            //emf = Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_DEV);
            //em = emf.createEntityManager();
            areaRepo = new AreaRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_DEV));
        }

        @After
        public void tearDown() {
        }

        @Test
        public void getAreaTypeByNameSuccessTest() {
            String name = "Tent";

            result = areaRepo.getAreaTypeByName(name);
            assertThat(result, is(notNullValue()));
            assertThat(result, hasProperty("name", is(name)));
        }
        
        @Test (expected = NotFoundException.class)
        public void getAreaTypeByNameNotExistTest() {
            String name = "test name";
            
            result = areaRepo.getAreaTypeByName(name);
            assertThat(result, is(nullValue()));
        }
        
        @Test
        public void getAreaTypeByIdSuccessTest() {
            int id = 1;
            
            result = areaRepo.getAreaTypeById(id);
            assertThat(result, is(notNullValue()));
            assertThat(result, hasProperty("name", is("Tent")));
        }
        
        @Test
        public void getAreaTypeByIdNotExistTest() {
            int id = 0;
            
            result = areaRepo.getAreaTypeById(id);
            assertThat(result, is(nullValue()));
        }
    }
    
    public static class GetAreaTypesFromCampsiteTest {
        
        private IAreaRepo areaRepo;
        private ICampsiteRepo campsiteRepo;
        private EntityManagerFactory emf;
        private EntityManager em;
        private AreaType result;

        @BeforeClass
        public static void setUpClass() {
        }

        @AfterClass
        public static void tearDownClass() {
        }

        @Before
        public void setUp() {
            initMocks(this);
            //emf = Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_DEV);
            //em = emf.createEntityManager();
            campsiteRepo = new CampsiteRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_DEV));
            areaRepo = new AreaRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_DEV));
        }

        @After
        public void tearDown() {
        }
        
        @Test
        public void getAreaTypesByCampsiteSuccessTest() {
            Campsite c = campsiteRepo.getById(1);
            List<AreaType> result = areaRepo.getAreaTypesByCampsite(c);
            
            assertThat(result, is(notNullValue()));
            assertThat(result, hasItems(hasProperty("name", is("Tent"))));
            assertThat(result, hasItems(hasProperty("name", is("Caravan"))));
            assertThat(result, hasItems(hasProperty("name", is("House"))));
            assertThat(result, hasItems(hasProperty("name", is("Big House"))));
        }
    }
    
    public static class GetAreasFromCampsiteTest {
        
        private IAreaRepo areaRepo;
        private ICampsiteRepo campsiteRepo;

        @BeforeClass
        public static void setUpClass() {
        }

        @AfterClass
        public static void tearDownClass() {
        }

        @Before
        public void setUp() {
            initMocks(this);
            campsiteRepo = new CampsiteRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_DEV));
            areaRepo = new AreaRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_DEV));
        }

        @After
        public void tearDown() {
        }
        
        @Test
        public void getCampsiteAreasWithFilteredReservationsBetweenDatesSuccessTest() throws ParseException {
            Campsite c = campsiteRepo.getById(1);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate = format.parse("2018-08-16 12:00:00");
            Date endDate = format.parse("2018-08-27 11:59:00");
            List<JsonObject> result = areaRepo.getCampsiteAreasWithFilteredReservationsBetweenDates(c, startDate, endDate);
            int notAvailableAreas = 0;
            for (JsonObject r : result) {
                if(!r.get("Available").getAsBoolean()) notAvailableAreas++;
            }

            assertThat(result, is(notNullValue()));
            assertThat(result.size(), is(15));
            assertThat(notAvailableAreas, is(4));
        }
        
        @Test
        public void getCampsiteAreasWithFilteredReservationsBetweenDatesNoAreasTest() throws ParseException {
            Campsite c = campsiteRepo.getById(2);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
            Date startDate = format.parse("2018-08-16 12:00:00");
            Date endDate = format.parse("2018-08-27 11:59:00");
            List<JsonObject> result = areaRepo.getCampsiteAreasWithFilteredReservationsBetweenDates(c, startDate, endDate);
            assertThat(result, is(empty()));
        }

        @Test
        public void getAreaWithFilteredReservationsBetweenDatesTest() throws ParseException {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate = format.parse("2018-08-16 12:00:00");
            Date endDate = format.parse("2018-08-27 11:59:00");
            JsonObject result = areaRepo.getAreaWithFilteredReservationsBetweenDates(5, startDate, endDate);
            System.out.println(result.toString());
            assertThat(result, is(notNullValue()));
            assertThat(result.get("Id").getAsInt(), is(5));
        }

        @Test
        public void getAreaByIdTestSuccess() {
            Area result = areaRepo.getAreaById(1);

            assertThat(result, is(notNullValue()));
            assertThat(result.getId(), is(1L));
        }

        @Test
        public void getAreaByIdNotExistTest() {
            Area result = areaRepo.getAreaById(500);

            assertNull(result);
        }
    }
}
