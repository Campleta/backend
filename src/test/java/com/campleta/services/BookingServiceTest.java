/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.services;

import com.campleta.models.Area;
import com.campleta.models.AreaType;
import com.campleta.models.Campsite;
import com.campleta.models.Reservation;
import com.campleta.models.Stay;
import com.campleta.models.User;
import com.campleta.repo.interfaces.IAreaRepo;
import com.campleta.repo.interfaces.IBookingRepo;
import com.campleta.repo.interfaces.ICampsiteRepo;
import com.campleta.repo.interfaces.IRoleRepo;
import com.campleta.repo.interfaces.IStayRepo;
import com.campleta.repo.interfaces.IUserRepo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.xml.bind.DatatypeConverter;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;

import com.google.gson.JsonObject;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Vixo
 */
public class BookingServiceTest {

    private BookingService bookingService;
    private IAreaRepo areaRepo;
    private IBookingRepo bookingRepo;
    private IUserRepo userRepo;
    private IStayRepo stayRepo;
    private IRoleRepo roleRepo;
    private ICampsiteRepo campsiteRepo;
    private Reservation reservation;

    public BookingServiceTest() {
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
        bookingRepo = mock(IBookingRepo.class);
        userRepo = mock(IUserRepo.class);
        stayRepo = mock(IStayRepo.class);
        roleRepo = mock(IRoleRepo.class);
        campsiteRepo = mock(ICampsiteRepo.class);
        bookingService = new BookingService(areaRepo, bookingRepo, userRepo, stayRepo, roleRepo, campsiteRepo);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void bookSuccessTest() {
        String data = "{ \n"
                + "     \"campsite\": 2,"
                + "	\"areaType\": 1,\n"
                + "     \"stays\": [ \n"
                + "     {\n"
                + "         \"startDate\": \"2019-08-07T12:42:00Z\","
                + "         \"endDate\": \"2019-08-14T12:42:00Z\","
                + "         \"guests\": [ \n"
                + "         {\n"
                + "             \"passport\": \"44886622\",\n"
                + "             \"firstname\": \"Martin\",\n"
                + "             \"lastname\": \"Karlsen\",\n"
                + "             \"anonymous\": false\n"
                + "         }]"
                + "     }],"
                + "    \"startDate\": \"2019-08-07T12:42:00Z\",\n"
                + "    \"endDate\": \"2019-08-14T12:42:00Z\"\n"
                + "}";
        Campsite campleta = new Campsite();
        campleta.setId(1L);
        campleta.setName("Campleta Gili");
        
        AreaType tent = new AreaType();
        tent.setId(1L);
        tent.setName("Tent");
        
        User guest = new User();
        guest.setPassport("44886622");
        guest.setFirstname("Martin");
        guest.setLastname("Karlsen");
        guest.addCampsite(campleta);
        List<User> guestsRes = new ArrayList<>();
        guestsRes.add(guest);
        campleta.addEmployee(guest);
        
        Reservation result = new Reservation();
        result.setId(1L);
        result.setAreaType(tent);
        
        Stay stay = new Stay();
        stay.setGuests(guestsRes);
        result.addStays(stay);
        
        when(campsiteRepo.getById(anyInt())).thenReturn(campleta);
        when(areaRepo.getAreaTypeById(anyInt())).thenReturn(tent);
        when(areaRepo.findAmountAvailableAreasByAreaType(anyInt())).thenReturn(5);
        when(userRepo.getUserByPassport(anyString())).thenReturn(guest);
        when(bookingRepo.createReservation(anyObject())).thenReturn(result);
        reservation = bookingService.book(data);

        assertThat(reservation, is(notNullValue()));
        assertThat(reservation.getStays(), not(IsEmptyCollection.empty()));
        assertThat(reservation.getStays(), hasSize(1));
        assertThat(reservation.getStays().get(0).getGuests(), is(notNullValue()));
        assertThat(reservation.getStays().get(0).getGuests(), hasItem(hasProperty("passport", is("44886622"))));
    }

    @Test
    public void bookWithAnonymousGuestsTest() {
        String data = "{ \n"
                + "     \"campsite\": 2,"
                + "	\"areaType\": 1,\n"
                + "     \"stays\": [ \n"
                + "     {\n"
                + "         \"startDate\": \"2019-08-07T12:44:00Z\","
                + "         \"endDate\": \"2019-08-14T12:44:00Z\","
                + "         \"guests\": [ \n"
                + "         {\n"
                + "             \"passport\": \"44886622\",\n"
                + "             \"firstname\": \"Martin\",\n"
                + "             \"lastname\": \"Karlsen\",\n"
                + "             \"anonymous\": false\n"
                + "         },"
                + "         {\n"
                + "             \"passport\": \"\","
                + "             \"firstname\": \"\","
                + "             \"lastname\": \"\","
                + "             \"anonymous\": true\n"
                + "         },"
                + "         {\n"
                + "             \"passport\": \"\","
                + "             \"firstname\": \"\","
                + "             \"lastname\": \"\","
                + "             \"anonymous\": true\n"
                + "         }]\n"
                + "     }],"
                + "    \"startDate\": \"2019-08-07T12:44:00Z\",\n"
                + "    \"endDate\": \"2019-08-14T12:44:00Z\"\n"
                + "}";
        Campsite campleta = new Campsite();
        campleta.setId(1L);
        campleta.setName("Campleta Gili");
        
        AreaType tent = new AreaType();
        tent.setId(1L);
        tent.setName("Tent");
        
        User guest = new User();
        guest.setPassport("44886622");
        guest.setFirstname("Martin");
        guest.setLastname("Karlsen");
        guest.addCampsite(campleta);
        User guest2 = new User();
        User guest3 = new User();
        List<User> guestsRes = new ArrayList<>();
        guestsRes.add(guest);
        guestsRes.add(guest2);
        guestsRes.add(guest3);
        campleta.addEmployee(guest3);
        
        Reservation result = new Reservation();
        result.setId(1L);
        result.setAreaType(tent);
        
        Stay stay = new Stay();
        stay.setGuests(guestsRes);
        result.addStays(stay);
        
        when(campsiteRepo.getById(anyInt())).thenReturn(campleta);
        when(areaRepo.getAreaTypeById(anyInt())).thenReturn(tent);
        when(areaRepo.findAmountAvailableAreasByAreaType(anyInt())).thenReturn(10);
        when(userRepo.getUserByPassport(anyString())).thenThrow(new NotFoundException());
        when(userRepo.createUser(guest)).thenReturn(guest);
        when(userRepo.createUser(guest2)).thenReturn(guest2);
        when(userRepo.createUser(guest3)).thenReturn(guest3);
        when(bookingRepo.createReservation(anyObject())).thenReturn(result);

        reservation = bookingService.book(data);

        assertThat(reservation, is(notNullValue()));
    }
    
    @Test
    public void bookWithMultipleStaysTest() {
        String data = "{ \n"
                + "     \"campsite\": 2,"
                + "	\"areaType\": 1,\n"
                + "     \"stays\": [ \n"
                + "     {\n"
                + "         \"startDate\": \"2019-08-07T12:42:00Z\","
                + "         \"endDate\": \"2019-08-21T12:42:00Z\","
                + "         \"guests\": [ \n"
                + "         {\n"
                + "             \"passport\": \"44886622\",\n"
                + "             \"firstname\": \"Martin\",\n"
                + "             \"lastname\": \"Karlsen\",\n"
                + "             \"anonymous\": false\n"
                + "         }]"
                + "     },"
                + "     {"
                + "         \"startDate\": \"2019-08-07T12:42:00Z\","
                + "         \"endDate\": \"2019-08-14T12:42:00Z\","
                + "         \"guests\": ["
                + "         {"
                + "             \"passport\": \"11223388\","
                + "             \"firstname\": \"Laura\","
                + "             \"lastname\": \"Nielsen\","
                + "             \"anonymous\": false"
                + "         }]"
                + "     }],"
                + "    \"startDate\": \"2019-08-07T12:42:00Z\",\n"
                + "    \"endDate\": \"2019-08-21T12:42:00Z\"\n"
                + "}";
        Campsite campleta = new Campsite();
        campleta.setId(1L);
        campleta.setName("Campleta Gili");
        
        AreaType tent = new AreaType();
        tent.setId(1L);
        tent.setName("Tent");
        
        User guest = new User();
        guest.setPassport("44886622");
        guest.setFirstname("Martin");
        guest.setLastname("Karlsen");
        guest.addCampsite(campleta);
        User guest2 = new User();
        guest2.setPassport("11223388");
        guest2.setFirstname("Laura");
        guest2.setLastname("Nielsen");
        campleta.addEmployee(guest);
        
        Reservation result = new Reservation();
        result.setId(1L);
        result.setAreaType(tent);
        
        Stay stay = new Stay();
        Stay stay2 = new Stay();
        stay.addGuest(guest);
        stay2.addGuest(guest2);
        result.addStays(stay);
        result.addStays(stay2);
        
        when(campsiteRepo.getById(anyInt())).thenReturn(campleta);
        when(areaRepo.getAreaTypeById(anyInt())).thenReturn(tent);
        when(areaRepo.findAmountAvailableAreasByAreaType(anyInt())).thenReturn(5);
        when(userRepo.getUserByPassport(anyString())).thenReturn(guest);
        when(userRepo.getUserByPassport(anyString())).thenReturn(guest2);
        when(bookingRepo.createReservation(anyObject())).thenReturn(result);
        reservation = bookingService.book(data);
        reservation = bookingService.book(data);
        
        assertThat(reservation, is(notNullValue()));
        assertThat(reservation.getStays(), is(notNullValue()));
        assertThat(reservation.getStays().size(), is(2));
        assertThat(reservation.getStays().get(0).getGuests(), hasItem(hasProperty("firstname", is("Martin"))));
        assertThat(reservation.getStays().get(1).getGuests(), hasItem(hasProperty("firstname", is("Laura"))));
    }
    
    @Test (expected = BadRequestException.class)
    public void bookWithMultipleStaysButNoGuestsForOneStayTest() {
        String data = "{ \n"
                + "     \"campsite\": 2,"
                + "	\"areaType\": 1,\n"
                + "     \"stays\": [ \n"
                + "     {\n"
                + "         \"startDate\": \"2019-08-07T12:42:00Z\","
                + "         \"endDate\": \"2019-08-21T12:42:00Z\","
                + "         \"guests\": [ \n"
                + "         {\n"
                + "             \"passport\": \"44886622\",\n"
                + "             \"firstname\": \"Martin\",\n"
                + "             \"lastname\": \"Karlsen\",\n"
                + "             \"anonymous\": false\n"
                + "         }]"
                + "     },"
                + "     {"
                + "         \"startDate\": \"2019-08-07T12:42:00Z\","
                + "         \"endDate\": \"2019-08-14T12:42:00Z\""
                + "     }],"
                + "    \"startDate\": \"2019-08-07T12:42:00Z\",\n"
                + "    \"endDate\": \"2019-08-21T12:42:00Z\"\n"
                + "}";
        AreaType tent = new AreaType();
        tent.setId(1L);
        tent.setName("Tent");
        
        User guest = new User();
        guest.setPassport("44886622");
        guest.setFirstname("Martin");
        guest.setLastname("Karlsen");
        
        Reservation result = new Reservation();
        result.setId(1L);
        result.setAreaType(tent);
        
        Stay stay = new Stay();
        Stay stay2 = new Stay();
        stay.addGuest(guest);
        result.addStays(stay);
        result.addStays(stay2);
        
        when(areaRepo.getAreaTypeById(anyInt())).thenReturn(tent);
        when(areaRepo.findAmountAvailableAreasByAreaType(anyInt())).thenReturn(5);
        when(userRepo.getUserByPassport(anyString())).thenReturn(guest);
        when(userRepo.getUserByPassport(anyString())).thenReturn(new User());
        when(bookingRepo.createReservation(anyObject())).thenReturn(result);
        reservation = bookingService.book(data);
        reservation = bookingService.book(data);
        
        assertThat(reservation, is(notNullValue()));
        assertThat(reservation.getStays(), is(notNullValue()));
        assertThat(reservation.getStays().size(), is(2));
        assertThat(reservation.getStays().get(0).getGuests(), hasItem(hasProperty("firstname", is("Martin"))));
        assertThat(reservation.getStays().get(1).getGuests(), hasItem(hasProperty("firstname", is("Laura"))));
    }

    @Test (expected = BadRequestException.class)
    public void bookWrongInputNoStayInfoTest() {
        User user = new User();
        String data = "{ \n"
                + "     \"campsite\": 2,"
                + "	\"areaType\": 1,\n"
                + "     \"startDate\": \"2019-08-07T12:33:44Z\",\n"
                + "     \"endDate\": \"2019-08-14T12:44:00Z\"\n"
                + "}";
        
        reservation = bookingService.book(data);
    }
    
    @Test(expected = BadRequestException.class)
    public void bookWrongInputNoGuestInfoTest() {
        User user = new User();
        String data = "{ \n"
                + "     \"campsite\": 2,"
                + "	\"areaType\": 1,"
                + "     \"stays\": ["
                + "     {"
                + "         \"startDate\": \"2019-08-07T12:33:44Z\","
                + "         \"endDate\": \"2019-08-14T12:44:00Z\""
                + "     }],"
                + "     \"startDate\": \"2019-08-07T12:33:44Z\",\n"
                + "     \"endDate\": \"2019-08-14T12:44:00Z\"\n"
                + "}";

        reservation = bookingService.book(data);
    }
    
    @Test(expected = BadRequestException.class)
    public void bookEndDateBeforeStartDateTest() {
        User user = new User();
        String data = "{ \n"
                + "     \"campsite\": 2,"
                + "	\"areaType\": 1,\n"
                + "     \"stays\": ["
                + "     {"
                + "         \"startDate\": \"2019-08-07T12:33:44Z\",\n"
                + "         \"endDate\": \"2019-08-06T12:33:44Z\","
                + "         \"guests\": [ \n"
                + "         {\n"
                + "             \"passport\": \"44886622\",\n"
                + "             \"firstname\": \"Martin\",\n"
                + "             \"lastname\": \"Karlsen\",\n"
                + "             \"anonymous\": false\n"
                + "         }]\n"
                + "     }],"
                + "    \"startDate\": \"2019-08-07T12:33:44Z\",\n"
                + "    \"endDate\": \"2019-08-06T12:33:44Z\"\n"
                + "}";

        reservation = bookingService.book(data);
    }

    @Test(expected = BadRequestException.class)
    public void bookEndDateSameAsStartDateTest() {
        User user = new User();
        String data = "{ \n"
                + "     \"campsite\": 2,"
                + "	\"areaType\": 1,\n"
                + "     \"stays\": ["
                + "     {"
                + "         \"startDate\": \"2019-08-07T12:33:44Z\","
                + "         \"endDate\": \"2019-08-07T12:33:44Z\","
                + "         \"guests\": [ \n"
                + "         {\n"
                + "             \"passport\": \"44886622\",\n"
                + "             \"firstname\": \"Martin\",\n"
                + "             \"lastname\": \"Karlsen\",\n"
                + "             \"anonymous\": false\n"
                + "         }]"
                + "     }],"
                + "    \"startDate\": \"2019-08-07T12:33:44Z\",\n"
                + "    \"endDate\": \"2019-08-07T12:33:44Z\"\n"
                + "}";

        reservation = bookingService.book(data);
    }

    @Test
    public void bookTestDifferentDatesTest() {
        Object[][] arr = new Object[][] {
            { "2019-08-14T12:44:00Z", "2019-08-14T12:44:00Z", false},
            { "2019-08-14T12:44:00Z", "2019-08-15T12:44:00Z", true},
            { "2019-08-14T12:44:00Z", "2019-08-15T02:44:00Z", false},
            { "2019-08-14T23:44:00Z", "2019-08-15T02:44:00Z", false},
            { "2017-08-03T12:44:00Z", "2017-08-15T02:44:00Z", false},
            { "2019-08-14T12:44:00Z", "2019-08-13T02:44:00Z", false},
            { "2019-08-07T12:44:00Z", "2019-08-15T02:44:00Z", true}
        };
        
        for (Object[] element : arr) {
            boolean res = bookingService.validateBookingDates(DatatypeConverter.parseDateTime(element[0].toString()), DatatypeConverter.parseDateTime(element[1].toString()));
            
            assertEquals(element[2], res);
        }
    }
    
    @Test (expected = BadRequestException.class)
    public void bookStayStartDateBeforeReservationStartDateTest() {
        User user = new User();
        String data = "{ \n"
                + "     \"campsite\": 2,"
                + "	\"areaType\": 1,\n"
                + "     \"stays\": ["
                + "     {"
                + "         \"startDate\": \"2019-08-05T12:33:44Z\","
                + "         \"endDate\": \"2019-08-07T12:33:44Z\","
                + "         \"guests\": [ \n"
                + "         {\n"
                + "             \"passport\": \"44886622\",\n"
                + "             \"firstname\": \"Martin\",\n"
                + "             \"lastname\": \"Karlsen\",\n"
                + "             \"anonymous\": false\n"
                + "         }]"
                + "     }],"
                + "    \"startDate\": \"2019-08-07T12:33:44Z\",\n"
                + "    \"endDate\": \"2019-08-07T12:33:44Z\"\n"
                + "}";

        reservation = bookingService.book(data);
    }
    
    @Test
    public void getNotPlacedReservationsTest() {
        List<Reservation> inputList = new ArrayList<Reservation>();
        inputList.add(new Reservation());
        inputList.add(new Reservation()); 
        inputList.add(new Reservation());
        when(bookingRepo.getNotPlacedReservations()).thenReturn(inputList);
        
        List<Reservation> result = bookingService.getNotPlacedReservations(-1);
        
        assertThat(result.size(), is(3));
    }
    
    @Test (expected = NotFoundException.class)
    public void getNotPlacedReservationsNoReservationsTest() {
        when(bookingRepo.getNotPlacedReservations()).thenThrow(NotFoundException.class);
        List<Reservation> result = bookingService.getNotPlacedReservations(-1);
    }
    
    @Test
    public void getNotPlacedReservationsFromCampsiteTest() {
        List<Reservation> inputList = new ArrayList<Reservation>();
        inputList.add(new Reservation());
        inputList.add(new Reservation()); 
        inputList.add(new Reservation());
        when(bookingRepo.getNotPlacedReservationsFromCampsite(1)).thenReturn(inputList);
        
        List<Reservation> result = bookingService.getNotPlacedReservations(1);
        
        assertThat(result.size(), is(3));
    }
    
    @Test (expected = NotFoundException.class)
    public void getNotPlacedReservationsNoReservationsFromCampsiteTest() {
        when(bookingRepo.getNotPlacedReservationsFromCampsite(1)).thenThrow(NotFoundException.class);
        List<Reservation> result = bookingService.getNotPlacedReservations(1);
    }

    @Test
    public void addAreaSuccessTest() {
        Date startDate = new Date();
        Date endDate = new Date();
        Reservation res = new Reservation();
        res.setId(1L);
        res.setStartDate(startDate);
        res.setEndDate(endDate);
        JsonObject json = new JsonObject();
        json.addProperty("Available", true);
        Area area = new Area("hej");
        area.setId(1L);
        res.setArea(area);
        when(bookingRepo.getReservationById(1)).thenReturn(res);
        when(areaRepo.getAreaById(1)).thenReturn(area);
        when(areaRepo.getAreaWithFilteredReservationsBetweenDates(1, startDate, endDate)).thenReturn(json);
        when(bookingRepo.addAreaToReservation(res, area)).thenReturn(res);

        Reservation result = bookingService.addArea(1, 1);
        assertThat(result, is(notNullValue()));
        assertThat(result.getArea(), is(notNullValue()));
        assertThat(result.getArea().getId(), is(1L));
    }

    @Test (expected = BadRequestException.class)
    public void addAreaNotAvailableTest() {
        Reservation res = new Reservation();
        res.setId(1L);
        Area area = new Area();
        area.setId(1L);
        JsonObject json = new JsonObject();
        json.addProperty("Available", false);

        when(bookingRepo.getReservationById(1)).thenReturn(res);
        when(areaRepo.getAreaById(1)).thenReturn(area);
        when(areaRepo.getAreaWithFilteredReservationsBetweenDates(anyInt(), anyObject(), anyObject())).thenReturn(json);

        bookingService.addArea(1, 1);
    }

    @Test (expected = BadRequestException.class)
    public void addAreaReservationNotExistTest() {
        bookingService.addArea(1, 1);
    }

    @Test (expected = BadRequestException.class)
    public void addAreaAreaNotExistTest() {
        Reservation res = new Reservation();
        res.setId(1L);

        bookingService.addArea(1, 1);
    }

    @Test
    public void getAreaReservationsFromDateTest() {
        Reservation r = new Reservation();
        r.setId(1L);
        List<Reservation> result = new ArrayList<>();
        result.add(r);
        Area a = new Area();
        a.setId(1L);

        when(areaRepo.getAreaById(anyInt())).thenReturn(a);
        when(bookingRepo.getReservations(anyObject(), anyObject())).thenReturn(result);

        Date d = new Date();
        List<Reservation> actualResult = bookingService.getAreaReservations(1, d);

        assertThat(actualResult, is(notNullValue()));
    }

    @Test (expected = BadRequestException.class)
    public void getAreaReservationsFromDateAreaDoesNotExistTest() {
        when(areaRepo.getAreaById(anyInt())).thenReturn(null);
        bookingService.getAreaReservations(500, new Date());
    }

    @Test
    public void getAreaReservationsFromDateNoReservationsTest() {
        Area a = new Area();
        a.setId(1L);

        when(areaRepo.getAreaById(anyInt())).thenReturn(a);
        when(bookingRepo.getReservations(anyObject(), anyObject())).thenReturn(null);

        List<Reservation> result = bookingService.getAreaReservations(1, new Date());
        assertThat(result, is(nullValue()));
    }

    @Test
    public void getReservationByIdSuccessTest() {
        Reservation expected = new Reservation();
        expected.setId(2L);
        when(bookingRepo.getReservationById(2)).thenReturn(expected);

        Reservation reservation = bookingService.getReservation(2);
        assertThat(reservation, is(notNullValue()));
        assertThat(reservation.getId(), is(2L));
    }

    @Test (expected = NotFoundException.class)
    public void getReservationByIdNotFoundTest() {
        when(bookingRepo.getReservationById(anyInt())).thenReturn(null);
        bookingService.getReservation(500);
    }

    @Test
    public void updateReservationSuccessTest() {
        Reservation reservation = new Reservation();
        reservation.setId(2L);
        Campsite c = new Campsite();
        c.setId(500L);
        AreaType at = new AreaType();
        at.setId(500L);
        reservation.setCampsite(c);
        reservation.setAreaType(at);
        Date d = new Date();
        Date end = new Date();
        d.setDate(d.getDate() + 1);
        reservation.setStartDate(d);
        end.setDate(end.getDate() + 3);
        reservation.setEndDate(end);

        when(areaRepo.getAreaTypeById(anyInt())).thenReturn(at);
        when(areaRepo.findAmountAvailableAreasByAreaType(anyInt())).thenReturn(1);
        when(bookingRepo.updateReservation(anyObject())).thenReturn(reservation);
        Reservation expected = bookingService.updateReservation(reservation);

        assertThat(expected, is(notNullValue()));
        assertThat(expected.getStartDate(), is(d));
    }

    @Test (expected = NotFoundException.class)
    public void updateReservationNotFoundTest() {
        Reservation r = new Reservation();
        r.setId(404L);
        Campsite c = new Campsite();
        c.setId(500L);
        AreaType at = new AreaType();
        at.setId(500L);
        r.setAreaType(at);
        r.setCampsite(c);
        Date date = new Date();
        date.setDate(date.getDate() + 1);
        r.setStartDate(date);
        Date end = new Date();
        end.setDate(end.getDate() + 3);
        r.setEndDate(end);
        Stay s = new Stay();
        s.setStartDate(r.getStartDate());
        s.setEndDate(r.getEndDate());
        User g = new User();
        s.addGuest(g);
        when(bookingRepo.updateReservation(anyObject())).thenReturn(null);
        bookingService.updateReservation(r);
    }
}
