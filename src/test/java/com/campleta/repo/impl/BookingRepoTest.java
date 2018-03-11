/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.repo.impl;

import com.campleta.config.DatabaseCfg;
import com.campleta.models.Area;
import com.campleta.models.AreaType;
import com.campleta.models.Reservation;
import com.campleta.repo.interfaces.IBookingRepo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasProperty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hamcrest.collection.IsEmptyCollection;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 *
 * @author Vixo
 */
@Transactional
public class BookingRepoTest {

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").setPrettyPrinting().create();
    private DateTimeFormatter formatter = ISODateTimeFormat.dateTimeParser().withZoneUTC();

    private IBookingRepo bookingRepo;
    private Reservation reservationResult;
    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction transaction;
    private Query query;
    
    public BookingRepoTest() {
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
        /*emf = mock(EntityManagerFactory.class);
        em = mock(EntityManager.class);
        transaction = mock(EntityTransaction.class);
        query = mock(Query.class);*/
        //em = Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_DEV).createEntityManager();
        bookingRepo = new BookingRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_DEV));
        //when(emf.createEntityManager()).thenReturn(em);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void createReservationSuccessTest() {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        Reservation expected = new Reservation();
        expected.setStartDate(startDate.getTime());
        expected.setEndDate(endDate.getTime());
        
        reservationResult = bookingRepo.createReservation(expected);
        assertThat(reservationResult, is(notNullValue()));
        assertEquals(startDate.getTime().getTime(), reservationResult.getStartDate().getTime());
        assertEquals(endDate.getTime().getTime(), reservationResult.getEndDate().getTime());
    }
    
    @Test
    public void getNotPlacedReservationsTest() {
        List<Reservation> result = bookingRepo.getNotPlacedReservations();
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(3));
    }
    
    @Test
    public void getNotPlacedReservationsFromCampsiteSuccessTest() {
        List<Reservation> result = bookingRepo.getNotPlacedReservationsFromCampsite(1);
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(2));
    }
    
    @Test
    public void getNotPlacedReservationsFromCampsiteNoReservationsTest() {
        List<Reservation> result = bookingRepo.getNotPlacedReservationsFromCampsite(2);
        assertThat(result, IsEmptyCollection.empty());
    }
    
    @Test
    public void getNotPlacedReservationsFromCampsiteRealTestDbNoReservationsTest() {
        EntityManagerFactory newEmf = Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_DEV);
        BookingRepo newBookingRepo = new BookingRepo(newEmf);
        List<Reservation> result = newBookingRepo.getNotPlacedReservationsFromCampsite(500);
        
        assertThat(result, IsEmptyCollection.empty());
    }

    @Test
    public void getReservationByIdSuccessTest() {
        Reservation reservation = bookingRepo.getReservationById(1);
        assertThat(reservation, is(notNullValue()));
        assertThat(reservation.getId(), is(1L));
    }

    @Test
    public void getReservationByIdNotReservationTest() {
        Reservation reservation = bookingRepo.getReservationById(500);
        assertThat(reservation, is(nullValue()));
    }

    @Test
    public void addAreaToReservationSuccessTest() {
        Reservation res = new Reservation();
        res.setId(1L);
        Area area = new Area();
        area.setId(1L);
        res.setArea(area);
        area.addReservation(res);

        Reservation result = bookingRepo.addAreaToReservation(res, area);
        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is(1L));
        assertThat(result.getArea(), is(notNullValue()));
        assertThat(result.getArea(), hasProperty("id", is(1L)));
    }

    @Test
    public void getReservationsTest() throws ParseException {
        Area area = new Area();
        area.setId(1L);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowDate = format.parse("2018-08-07 12:00:00");

        List<Reservation> result = bookingRepo.getReservations(area, nowDate);
        assertThat(result, is(notNullValue()));
        assertEquals(2, result.size());
        assertThat(result.get(0).getId(), is(1L));
        assertEquals(nowDate, result.get(0).getStartDate());
    }

    @Test
    public void getReservationsNoReservationsTest() {

    }

    @Test
    public void updateReservatioSuccessTest() {
        Reservation reservation = new Reservation();
        reservation.setStartDate(new Date());
        reservation = bookingRepo.createReservation(reservation);
        Date d = new Date();
        d.setDate(d.getDay() + 1);
        reservation.setStartDate(d);
        Reservation expected = bookingRepo.updateReservation(reservation);

        assertThat(expected, is(notNullValue()));
        assertThat(expected.getStartDate(), is(d));
    }
}
