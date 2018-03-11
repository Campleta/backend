/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.services;

import com.campleta.models.Area;
import com.campleta.models.AreaType;
import com.campleta.models.Campsite;
import com.campleta.repo.interfaces.IAreaRepo;
import com.campleta.repo.interfaces.ICampsiteRepo;
import com.campleta.services.interfaces.IArea;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.NoContentException;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import com.google.gson.JsonObject;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Vixo
 */
public class AreaServiceTest {
    
    private IArea areaService;
    private IAreaRepo areaRepo;
    private ICampsiteRepo campsiteRepo;
    
    public AreaServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        areaRepo = mock(IAreaRepo.class);
        campsiteRepo = mock(ICampsiteRepo.class);
        areaService = new AreaService(areaRepo, campsiteRepo);
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void getAreaTypesFromCampsiteSuccessTest() {
        Campsite c = new Campsite();
        List<AreaType> expected = new ArrayList<>();
        expected.add(new AreaType());
        expected.add(new AreaType());
        expected.add(new AreaType());
        when(campsiteRepo.getById(1)).thenReturn(c);
        when(areaRepo.getAreaTypesByCampsite(c)).thenReturn(expected);
        
        List<AreaType> result = areaService.getAreaTypesFromCampsite(1);
        
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(3));
    }
    
    @Test (expected = NotFoundException.class)
    public void getAreaTypesFromCampsiteCampsiteNotExistTest() {
        when(campsiteRepo.getById(500)).thenThrow(NotFoundException.class);
        
        areaService.getAreaTypesFromCampsite(500);
    }
    
    @Test (expected = NotFoundException.class)
    public void getAreaTypesFromCampsiteNoAreaTypes() {
        Campsite c = new Campsite();
        when(campsiteRepo.getById(2)).thenReturn(c);
        when(areaRepo.getAreaTypesByCampsite(c)).thenThrow(NotFoundException.class);
        
        areaService.getAreaTypesFromCampsite(2);
    }

    @Test
    public void getCampsiteAreasSuccessTest() throws NoContentException {
        Campsite c = new Campsite();
        c.setId(1L);
        List<JsonObject> expected = new ArrayList<>();
        expected.add(new JsonObject());
        expected.add(new JsonObject());

        DateTimeFormatter formatter = ISODateTimeFormat.dateTimeParser().withZoneUTC();
        Date startMockDate = formatter.parseDateTime("2017-08-09T12:00:00Z").toCalendar(Locale.GERMAN).getTime();
        Date endMockDate = formatter.parseDateTime("2017-08-15T12:00:00Z").toCalendar(Locale.GERMAN).getTime();

        when(campsiteRepo.getById(1)).thenReturn(c);
        when(areaRepo.getCampsiteAreasWithFilteredReservationsBetweenDates(c, startMockDate, endMockDate)).thenReturn(expected);
        
        List<JsonObject> result = areaService.getCampsiteAreas(1, "2017-08-09T12:00:00Z", "2017-08-15T12:00:00Z");
        assertThat(result.size(), is(2));
    }
    
    @Test (expected = NoContentException.class)
    public void getCampsiteAreasNoAreasTest() throws NoContentException {
        Campsite c = new Campsite();
        c.setId(1L);
        List<JsonObject> expected = new ArrayList<>();
        Date mockDate = new Date();
        when(areaRepo.getCampsiteAreasWithFilteredReservationsBetweenDates(c, mockDate, mockDate)).thenReturn(expected);
        
        areaService.getCampsiteAreas(1, "2017-08-09T12:00:00Z", "2017-08-15T12:00:00Z");
    }

    @Test
    public void getAreaTypeByNameSuccessTest() {
        AreaType at = new AreaType();
        at.setName("Tent");
        when(areaRepo.getAreaTypeByName("Tent")).thenReturn(at);

        AreaType expected = areaService.getAreaTypeByName("Tent");
        verify(areaRepo).getAreaTypeByName("Tent");
        assertThat(expected, is(notNullValue()));
        assertThat(expected.getName(), is("Tent"));
    }

    @Test (expected = NotFoundException.class)
    public void getAreaTypeByNameNotFoundTest() {
        when(areaRepo.getAreaTypeByName(anyString())).thenThrow(NotFoundException.class);

        areaService.getAreaTypeByName("Hej");

    }
}
