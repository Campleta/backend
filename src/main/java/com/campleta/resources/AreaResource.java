/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.resources;

import com.campleta.config.DatabaseCfg;
import com.campleta.models.*;
import com.campleta.repo.impl.*;
import com.campleta.repo.interfaces.IAreaRepo;
import com.campleta.repo.interfaces.ICampsiteRepo;
import com.campleta.services.AreaService;
import com.campleta.services.AuthenticationService;
import com.campleta.services.BookingService;
import com.campleta.services.interfaces.IArea;
import com.campleta.services.interfaces.IAuthentication;
import com.campleta.services.interfaces.IBooking;
import com.google.gson.*;

import javax.annotation.security.RolesAllowed;
import javax.persistence.Persistence;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * REST Web Service
 *
 * @author Vixo
 */
@Path("areas")
public class AreaResource {

    @Context
    private UriInfo context;
    
    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").setPrettyPrinting().create();
    private DateTimeFormatter formatter = ISODateTimeFormat.dateTimeParser().withZoneUTC();
    private IArea areaService;
    private IBooking bookingService;

    private IAreaRepo areaRepo;
    private ICampsiteRepo campsiteRepo;

    public AreaResource() {
        this.areaRepo = new AreaRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME));
        this.campsiteRepo = new CampsiteRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME));
        this.areaService = new AreaService(this.areaRepo, this.campsiteRepo);
        this.bookingService = new BookingService(this.areaRepo, new BookingRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)),
                new UserRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)),
                new StayRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)),
                new RoleRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)),
                this.campsiteRepo);
    }

    @GET
    @Path("/campsite/{campsiteId}/{fromDate}/{toDate}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCampsiteAreas(@Context SecurityContext securityContext,
                                     @PathParam("campsiteId") int campsiteId,
                                     @PathParam("fromDate") String fromDate,
                                     @PathParam("toDate") String toDate) throws NoContentException, ParseException {
        List<JsonObject> result = areaService.getCampsiteAreas(campsiteId, fromDate, toDate);
        JsonArray resultArr = new JsonArray();
        for(JsonObject obj : result) {
            resultArr.add(obj);
        }
        return Response.status(Response.Status.OK).entity(gson.toJson(resultArr)).build();
    }

    @GET
    @Path("/{areaId}/reservations/now")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNowReservationForArea(@PathParam("areaId") int areaId) {
        Date date = new Date();
        List<Reservation> reservations = bookingService.getAreaReservations(areaId, date);

        /*JsonArray response = new JsonArray();
        for(Reservation r : reservations) {
            JsonObject json = new JsonObject();
            json.addProperty("Id", r.getId());
            json.addProperty("StartDate", gson.toJson(r.getStartDate()));
            json.addProperty("EndDate", gson.toJson(r.getEndDate()));
            JsonArray stays = new JsonArray();
            for(Stay s : r.getStays()) {
                JsonObject stayObj = new JsonObject();
                stayObj.addProperty("Id", s.getId());
                stayObj.addProperty("StartDate", gson.toJson(s.getStartDate()));
                stayObj.addProperty("EndDate", gson.toJson(s.getEndDate()));
                for(User u : s.getGuests()) {

                }
                stays.add(gson.toJson(s));
            }
            json.add("Stays", stays);
            response.add(json);
        }*/

        return Response.status(Response.Status.OK).entity(gson.toJson(reservations)).type(MediaType.APPLICATION_JSON).build();
    }
}
