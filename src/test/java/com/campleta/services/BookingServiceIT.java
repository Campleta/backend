package com.campleta.services;

import com.campleta.config.DatabaseCfg;
import com.campleta.models.*;
import com.campleta.repo.impl.*;
import com.campleta.services.interfaces.IBooking;
import org.junit.*;

import javax.persistence.Persistence;
import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class BookingServiceIT {

    private IBooking bookingService;

    @BeforeClass
    public static void setupClass() {}

    @Before
    public void setup() {
        bookingService = new BookingService(new AreaRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_DEV)),
                new BookingRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_DEV)),
                new UserRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_DEV)),
                new StayRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_DEV)),
                new RoleRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_DEV)),
                new CampsiteRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_DEV)));
    }

    @After
    public void teardown() {}

    @AfterClass
    public static void teardownClass() {}

    @Test
    public void editReservationSuccessTest() {
        Reservation r = new Reservation();
        r.setId(2L);
        Campsite c = new Campsite();
        c.setId(500L);
        AreaType at = new AreaType();
        at.setId(2L);
        r.setCampsite(c);
        r.setAreaType(at);
        Date d = new Date();
        Date end = new Date();
        d.setDate(d.getDate() + 1);
        end.setDate(end.getDate() + 2);
        r.setStartDate(d);
        r.setEndDate(end);
        Reservation result = bookingService.updateReservation(r);

        assertThat(result, is(notNullValue()));
        assertThat(result.getStartDate(), is(d));
    }

    @Test (expected = NotFoundException.class)
    public void editReservationNotExistTest() {
        Reservation r = new Reservation();
        r.setId(404L);
        Campsite c = new Campsite();
        c.setId(500L);
        AreaType at = new AreaType();
        at.setId(500L);
        r.setCampsite(c);
        r.setAreaType(at);
        Date start = new Date();
        Date end = new Date();
        start.setDate(start.getDate() + 1);
        end.setDate(end.getDate() + 3);
        r.setStartDate(start);
        r.setEndDate(end);
        bookingService.updateReservation(r);
    }

    @Test
    public void editReservationWithNewStayAndNewGuestTest() {
        Reservation reservation = bookingService.getReservation(2);
        Stay stay = new Stay();
        User guest = new User();
        guest.setPassport("88888888");
        guest.setFirstname("Martin");
        guest.setLastname("Karlsen");
        stay.addGuest(guest);
        stay.setReservation(reservation);
        stay.setStartDate(reservation.getStartDate());
        stay.setEndDate(reservation.getEndDate());
        reservation.addStays(stay);

        Reservation result = bookingService.updateReservation(reservation);

        assertThat(result.getId(), is(reservation.getId()));
        assertThat(result.getStays(), is(not(empty())));
        assertThat(result.getStays().get(0).getGuests().get(0).getPassport(), is("88888888"));
    }

    @Test
    public void editReservationAddAnonymousGuestToStayTest() {
        Reservation reservation = bookingService.getReservation(3);
        Stay stay = new Stay();
        stay.setStartDate(reservation.getStartDate());
        stay.setEndDate(reservation.getEndDate());
        User anonymousGuest = new User();
        stay.addGuest(anonymousGuest);
        stay.setReservation(reservation);
        reservation.addStays(stay);

        Reservation result = bookingService.updateReservation(reservation);

        assertThat(result.getId(), is(reservation.getId()));
        assertThat(result.getStays(), is(not(empty())));
        assertThat(result.getStays().get(0).getGuests().get(0).getPassport(), is(nullValue()));
    }

    @Test
    public void editReservationReplaceStayWithNewStayTest() {
        Reservation reservation = bookingService.getReservation(5);
        Stay stay = new  Stay();
        stay.setReservation(reservation);
        stay.setStartDate(reservation.getStays().get(0).getStartDate());
        stay.setEndDate(reservation.getStays().get(0).getEndDate());
        User user = new User();
        user.setPassport("12345678");
        user.setFirstname("Test");
        user.setLastname("Lastname");
        stay.addGuest(user);
        Stay newStay = new Stay();
        Date startDate = new Date();
        startDate.setDate(3);
        startDate.setMonth(8);
        startDate.setYear(2018);
        newStay.setStartDate(startDate);
        newStay.setEndDate(reservation.getEndDate());
        newStay.setReservation(reservation);
        User guest = new User();
        newStay.addGuest(guest);
        List<Stay> stayList = new ArrayList<Stay>();
        stayList.add(stay);
        stayList.add(newStay);
        reservation.setStays(stayList);

        Reservation result = bookingService.updateReservation(reservation);

        assertThat(result.getId(), is(reservation.getId()));
        assertThat(result.getStays(), is(not(empty())));
        assertThat(result.getStays().size(), is(2));
        assertThat(result.getStays().get(1).getStartDate(), is(startDate));
        assertThat(result.getStays().get(1).getId(), is(notNullValue()));
    }
}
