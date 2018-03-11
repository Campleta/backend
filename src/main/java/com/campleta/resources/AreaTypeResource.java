/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.resources;

import com.campleta.config.DatabaseCfg;
import com.campleta.repo.impl.AreaRepo;
import com.campleta.repo.impl.CampsiteRepo;
import com.campleta.repo.impl.UserRepo;
import com.campleta.services.AreaService;
import com.campleta.services.AuthenticationService;
import com.campleta.services.interfaces.IArea;
import com.campleta.services.interfaces.IAuthentication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.annotation.security.RolesAllowed;
import javax.persistence.Persistence;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * REST Web Service
 *
 * @author Vixo
 */
@Path("areaTypes")
@RolesAllowed("Employee")
public class AreaTypeResource {

    private IAuthentication authService;
    private IArea areaService;
    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").setPrettyPrinting().create();
    
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of AreaTypeResource
     */
    public AreaTypeResource() {
        this.authService = new AuthenticationService(new UserRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
        this.areaService = new AreaService(
                new AreaRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)),
                new CampsiteRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{campsiteId}")
    public Response getAreaTypesForCampsite(@Context SecurityContext securityContext, @PathParam("campsiteId") int campsiteId) {
        if(campsiteId <= 0) throw new BadRequestException();
        if(!authService.validateCampsiteRelation(securityContext.getUserPrincipal().getName(), Long.parseLong(String.valueOf(campsiteId)))) throw new NotAuthorizedException("Not authorized to get reservations for this campsite");
        return Response.status(Response.Status.OK).entity(gson.toJson(areaService.getAreaTypesFromCampsite(campsiteId))).type(MediaType.APPLICATION_JSON).build();
    }
}
