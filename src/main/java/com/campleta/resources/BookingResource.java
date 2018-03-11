/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.resources;

import com.campleta.config.DatabaseCfg;
import com.campleta.models.Reservation;
import com.campleta.models.Stay;
import com.campleta.models.User;
import com.campleta.repo.impl.AreaRepo;
import com.campleta.repo.impl.BookingRepo;
import com.campleta.repo.impl.CampsiteRepo;
import com.campleta.repo.impl.RoleRepo;
import com.campleta.repo.impl.StayRepo;
import com.campleta.repo.impl.UserRepo;
import com.campleta.services.AreaService;
import com.campleta.services.AuthenticationService;
import com.campleta.services.BookingService;
import com.campleta.services.interfaces.IArea;
import com.campleta.services.interfaces.IAuthentication;
import com.google.gson.*;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.campleta.services.interfaces.IBooking;
import com.google.gson.reflect.TypeToken;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.security.Principal;
import java.util.List;
import java.util.Locale;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.core.SecurityContext;

/**
 * REST Web Service
 *
 * @author Vixo
 */
@Path("reservations")
@RolesAllowed("Employee")
public class BookingResource {

    @Context
    private UriInfo context;
    
    private IBooking reservationService;
    private IAuthentication authService;
    private IArea areaService;
    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").setPrettyPrinting().create();
    private EntityManagerFactory emf;
    private EntityManager em;
    private DateTimeFormatter formatter = ISODateTimeFormat.dateTimeParser().withZoneUTC();

    /**
     * Creates a new instance of ReservationResource
     */
    public BookingResource() {
        String PU_NAME = DatabaseCfg.PU_NAME;
        this.reservationService = new BookingService(
                new AreaRepo(Persistence.createEntityManagerFactory(PU_NAME)), 
                new BookingRepo(Persistence.createEntityManagerFactory(PU_NAME)), 
                new UserRepo(Persistence.createEntityManagerFactory(PU_NAME)), 
                new StayRepo(Persistence.createEntityManagerFactory(PU_NAME)),
                new RoleRepo(Persistence.createEntityManagerFactory(PU_NAME)),
                new CampsiteRepo(Persistence.createEntityManagerFactory(PU_NAME)));
        this.authService = new AuthenticationService(new UserRepo(Persistence.createEntityManagerFactory(PU_NAME)));
        this.areaService = new AreaService(new AreaRepo(Persistence.createEntityManagerFactory(PU_NAME)),
                new CampsiteRepo(Persistence.createEntityManagerFactory(PU_NAME)));
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response book(@Context SecurityContext securityContext, String content) {
        JsonObject input = new JsonParser().parse(content).getAsJsonObject();
        if(!input.has("campsite")) throw new BadRequestException();
        if(!authService.validateCampsiteRelation(securityContext.getUserPrincipal().getName(), input.get("campsite").getAsLong())) throw new NotAuthorizedException("Not authorized to create a reservation for this campsite");
        return Response.status(Response.Status.CREATED).entity(gson.toJson(reservationService.book(content))).type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response getReservation(@Context SecurityContext securityContext, @PathParam("id") int id) {
        return Response.status(Response.Status.OK).entity(gson.toJson(reservationService.getReservation(id))).type(MediaType.APPLICATION_JSON).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{campsiteId}/notApproved")
    public Response getAllReservationsWithNoArea(@Context SecurityContext securityContext, @PathParam("campsiteId") int campsiteId) {
        if(campsiteId <= 0) throw new BadRequestException();
        if(!authService.validateCampsiteRelation(securityContext.getUserPrincipal().getName(), Long.parseLong(String.valueOf(campsiteId)))) throw new NotAuthorizedException("Not authorized to get reservations for this campsite");
        return Response.status(Response.Status.OK).entity(gson.toJson(reservationService.getNotPlacedReservations(campsiteId))).type(MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{reservationId}/area")
    public Response addAreaToReservation(@PathParam("reservationId") int reservationId, String content) {
        JsonObject input = new JsonParser().parse(content).getAsJsonObject();
        if(!input.has("areaId")) throw new BadRequestException();
        return Response
                .status(Response.Status.OK)
                .entity(gson.toJson(reservationService.addArea(reservationId, input.get("areaId").getAsInt())))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{reservationId}")
    public Response editReservation(@PathParam("reservationId") int reservationId, String content) {
        JsonObject response;
        try {
            JsonObject inputJson = new JsonParser().parse(content).getAsJsonObject();
            Reservation reservation = reservationService.getReservation(reservationId);
            if(inputJson.has("areaType")) reservation.setAreaType(areaService.getAreaTypeByName(inputJson.get("areaType").getAsString()));
            if(inputJson.has("startDate")) {
                DateTime startDate = formatter.parseDateTime(inputJson.get("startDate").getAsString());
                startDate.withHourOfDay(12).withMinuteOfHour(0).withSecondOfMinute(0);
                reservation.setStartDate(startDate.toCalendar(Locale.GERMAN).getTime());
            }
            if(inputJson.has("endDate")) {
                DateTime endDate = formatter.parseDateTime(inputJson.get("endDate").getAsString());
                endDate.withHourOfDay(12).withMinuteOfHour(0).withSecondOfMinute(0);
                reservation.setEndDate(endDate.toCalendar(Locale.GERMAN).getTime());
            }
            if(inputJson.has("stays")) {
                List<Stay> stays = gson.fromJson(inputJson.get("stays"), new TypeToken<List<Stay>>(){}.getType());
                reservation.setStays(stays);
                for(Stay stay : stays) {
                    stay.setReservation(reservation);
                }
            }

            reservation = reservationService.updateReservation(reservation);

            return Response.status(Response.Status.OK).entity(gson.toJson(reservation)).type(MediaType.APPLICATION_JSON).build();
        } catch (NotFoundException notFound) {
            response = new JsonObject();
            response.addProperty("error_code", 404);
            response.addProperty("error_message", notFound.getMessage());

            return Response.status(Response.Status.NOT_FOUND).entity(gson.toJson(response)).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            response = new JsonObject();
            response.addProperty("error_code", 500);
            response.addProperty("error_message", "Unknown server error.");

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(gson.toJson(response)).type(MediaType.APPLICATION_JSON).build();
        }
    }
}
