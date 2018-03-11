/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.services;

import com.campleta.models.*;
import com.campleta.repo.interfaces.IAreaRepo;
import com.campleta.repo.interfaces.IBookingRepo;
import com.campleta.repo.interfaces.ICampsiteRepo;
import com.campleta.repo.interfaces.IRoleRepo;
import com.campleta.repo.interfaces.IStayRepo;
import com.campleta.repo.interfaces.IUserRepo;
import com.campleta.services.interfaces.IBooking;
import com.google.gson.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.reflect.TypeToken;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 *
 * @author Vixo
 */
public class BookingService implements IBooking {

    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").setPrettyPrinting().create();
    private DateTimeFormatter formatter = ISODateTimeFormat.dateTimeParser().withZoneUTC();
    private IAreaRepo areaRepo;
    private IBookingRepo bookingRepo;
    private IUserRepo userRepo;
    private IStayRepo stayRepo;
    private IRoleRepo roleRepo;
    private ICampsiteRepo campsiteRepo;

    private Reservation reservation;
    private Area area;

    public BookingService(
            IAreaRepo areaRepo, 
            IBookingRepo bookingRepo, 
            IUserRepo userRepo, 
            IStayRepo stayRepo, 
            IRoleRepo roleRepo,
            ICampsiteRepo campsiteRepo) {
        this.areaRepo = areaRepo;
        this.bookingRepo = bookingRepo;
        this.userRepo = userRepo;
        this.stayRepo = stayRepo;
        this.roleRepo = roleRepo;
        this.campsiteRepo = campsiteRepo;
    }

    /**
     *
     * @param content Json String containing information for creating a reservation.
     * @return Reservation object if successfully created.
     */
    @Override
    public Reservation book(String content) {
        JsonObject data = new JsonParser().parse(content).getAsJsonObject();

        if (!data.has("campsite") || !data.has("areaType") || !data.has("stays")
                || data.get("stays").getAsJsonArray().size() < 1 || !data.has("startDate") || !data.has("endDate")) {

            throw new BadRequestException("Incorrect information.");
        }

        DateTime startDateFormat = formatter.parseDateTime(data.get("startDate").getAsString());
        DateTime endDateFormat = formatter.parseDateTime(data.get("endDate").getAsString());
        startDateFormat.withHourOfDay(12).withMinuteOfHour(0).withSecondOfMinute(0);
        endDateFormat.withHourOfDay(11).withMinuteOfHour(59).withSecondOfMinute(0);
        Calendar startDate = startDateFormat.toCalendar(Locale.GERMAN);
        Calendar endDate = endDateFormat.toCalendar(Locale.GERMAN);

        return validate(data.get("campsite").getAsInt(), data.get("areaType").getAsInt(), data.get("stays").getAsJsonArray(), startDate, endDate);
    }

    @Override
    public List<Reservation> getNotPlacedReservations(int campsiteId) {
        if(campsiteId < 0) return bookingRepo.getNotPlacedReservations();
        return bookingRepo.getNotPlacedReservationsFromCampsite(campsiteId);
    }

    @Override
    public Reservation addArea(int reservationId, int areaId) {
        reservation = bookingRepo.getReservationById(reservationId);
        area = areaRepo.getAreaById(areaId);
        if(reservation == null) throw new BadRequestException();
        if(area == null) throw new BadRequestException();
        JsonObject areaJson = areaRepo.getAreaWithFilteredReservationsBetweenDates(areaId, reservation.getStartDate(), reservation.getEndDate());
        if(areaJson == null) throw new BadRequestException();
        if(!areaJson.has("Available") || !areaJson.get("Available").getAsBoolean()) throw new BadRequestException("Area is occupied.");

        reservation.setArea(area);
        area.addReservation(reservation);
        return bookingRepo.addAreaToReservation(reservation, area);
    }

    @Override
    public List<Reservation> getAreaReservations(int areaId, Date fromDate) {
        Area area = areaRepo.getAreaById(areaId);
        if(area == null) throw new BadRequestException();
        return bookingRepo.getReservations(area, fromDate);
    }

    @Override
    public Reservation getReservation(int id) {
        Reservation reservation = bookingRepo.getReservationById(id);
        if(reservation == null) throw new NotFoundException("Reservation could not be found.");

        return reservation;
    }

    @Override
    public Reservation updateReservation(Reservation reservation) {
        if(reservation.getId() == null) throw new BadRequestException("Reservation has no Id");
        Calendar startDate = Calendar.getInstance(Locale.GERMAN);
        Calendar endDate = Calendar.getInstance(Locale.GERMAN);
        startDate.setTime(reservation.getStartDate());
        endDate.setTime(reservation.getEndDate());
        startDate.set(Calendar.HOUR_OF_DAY, 12);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        endDate.set(Calendar.HOUR_OF_DAY, 11);
        endDate.set(Calendar.MINUTE, 59);
        endDate.set(Calendar.SECOND, 0);

        JsonElement element = gson.toJsonTree(reservation.getStays(), new TypeToken<List<Stay>>(){}.getType());
        if(!element.isJsonArray()) throw new BadRequestException("Could not convert requested data.");

        if (!validateBookingDates(startDate, endDate)) throw new BadRequestException("Incorrect dates.");
        if (!validateStayGuests(element.getAsJsonArray())) throw new BadRequestException("No guests set");
        if (!validateAreas(Integer.parseInt(reservation.getAreaType().getId().toString()))) throw new NotFoundException("Area does not exist.");

        // Find guests and create.
        for (Stay stay : reservation.getStays()) {
            element = gson.toJsonTree(stay.getGuests(), new TypeToken<List<User>>(){}.getType());
            stay.setGuests(createGuests(element.getAsJsonArray()));
        }

        Reservation updateReservation = bookingRepo.updateReservation(reservation);
        if(updateReservation == null) throw new NotFoundException("Reservation not found.");

        return updateReservation;
    }

    protected boolean validateBookingDates(Calendar startDate, Calendar endDate) {
        Calendar now = Calendar.getInstance();
        if (startDate.before(now) || endDate.before(now)) {
            return false; // Check if both startDate and endDate is in the future.
        }
        if (!endDate.after(startDate)) {
            return false; // Check if endDate is after startDate
        }
        long MAX_DURATION = MILLISECONDS.convert(23, TimeUnit.HOURS);
        long time = endDate.getTime().getTime() - startDate.getTime().getTime();
        if (time < MAX_DURATION) {
            return false;
        }

        return true;
    }

    private Reservation validate(int campsite, int areaType, JsonArray staysArr, Calendar startDate, Calendar endDate) {
        if (!validateBookingDates(startDate, endDate)) throw new BadRequestException("Incorrect dates.");
        if (!validateStayGuests(staysArr)) throw new BadRequestException("No guests set.");
        if (!validateAreas(areaType)) throw new NotFoundException("Area does not exist.");

        return createReservation(campsite, areaType, staysArr, startDate, endDate);
    }

    private Reservation createReservation(int campsite, int areaType, JsonArray staysArr, Calendar startDate, Calendar endDate) {
        Campsite campsiteObj = campsiteRepo.getById(campsite);
        Reservation reservation = new Reservation();
        reservation.setAreaType(areaRepo.getAreaTypeById(areaType));
        reservation.setStartDate(startDate.getTime());
        reservation.setEndDate(endDate.getTime());
        reservation.setStays(createStays(reservation, staysArr, startDate, endDate));
        reservation.setCampsite(campsiteObj);
        campsiteObj.addReservation(reservation);

        return bookingRepo.createReservation(reservation);
    }

    private List<Stay> createStays(Reservation reservation, JsonArray staysArr, Calendar reservationStart, Calendar reservationEnd) {
        List<Stay> staysList = new ArrayList<>();
        for (int i = 0; i < staysArr.size(); i++) {
            JsonObject jsonStay = staysArr.get(i).getAsJsonObject();
            Calendar startStay = DatatypeConverter.parseDateTime(jsonStay.get("startDate").getAsString());
            Calendar endStay = DatatypeConverter.parseDateTime(jsonStay.get("endDate").getAsString());
            if (!validateBookingDates(startStay, endStay)) {
                throw new BadRequestException("Invalid dates for Stay.");
            }
            if (startStay.before(reservationStart) || endStay.after(reservationEnd)) {
                throw new BadRequestException("Stay dates cannot exceed the reservation dates.");
            }

            Stay stay = new Stay();
            stay.setStartDate(startStay.getTime());
            stay.setEndDate(endStay.getTime());
            stay.setGuests(createGuests(jsonStay.get("guests").getAsJsonArray()));
            stay.setReservation(reservation);

            staysList.add(stay);
        }

        return staysList;
    }
    
    private boolean validateAreas(int areaType) {
        AreaType areaTypeObj = areaRepo.getAreaTypeById(areaType);
        if (areaTypeObj == null) {
            throw new NotFoundException("AreaType does not exist.");
        }
        return checkAvailableAreas(areaType);
    }

    private boolean validateStayGuests(JsonArray staysArr) {
        for (int i = 0; i < staysArr.size(); i++) {
            if (!staysArr.get(i).getAsJsonObject().has("guests") || staysArr.get(i).getAsJsonObject().get("guests").getAsJsonArray().size() <= 0) {
                return false;
            }
        }

        return true;
    }

    private boolean checkAvailableAreas(int type) {
        return areaRepo.findAmountAvailableAreasByAreaType(type) > 0;
    }

    private List<User> createGuests(JsonArray guests) {
        List<User> guestList = new ArrayList<>();
        for (int i = 0; i < guests.size(); i++) {
            JsonObject jsonGuest = guests.get(i).getAsJsonObject();
            User guest = null;
            try {
                if(!jsonGuest.has("passport") || jsonGuest.get("passport").getAsString().equals("")) throw new NotFoundException("No passport");
                guest = userRepo.getUserByPassport(jsonGuest.get("passport").getAsString());
            } catch (NotFoundException e) {
                User newUser = new User();
                if(!jsonGuest.has("passport") || jsonGuest.get("passport").getAsString().equals("")) {
                    newUser.setPassport(null);
                } else {
                    newUser.setPassport(jsonGuest.get("passport").getAsString());
                }
                if(jsonGuest.has("firstname")) newUser.setFirstname(jsonGuest.get("firstname").getAsString());
                if(jsonGuest.has("lastname")) newUser.setLastname(jsonGuest.get("lastname").getAsString());
                Role role = roleRepo.getRoleByName("Guest");
                newUser.addRole(role);
                guest = userRepo.createUser(newUser);
            } finally {
                guestList.add(guest);
            }

        }

        return guestList;
    }

}
