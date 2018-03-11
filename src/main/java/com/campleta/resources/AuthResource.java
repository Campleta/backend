/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.resources;

import com.campleta.config.DatabaseCfg;
import com.campleta.models.User;
import com.campleta.repo.impl.UserRepo;
import com.campleta.services.AuthenticationService;
import com.campleta.services.TokenService;
import com.campleta.services.interfaces.IAuthentication;
import com.campleta.services.interfaces.IToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nimbusds.jose.JOSEException;
import javax.annotation.security.PermitAll;
import javax.persistence.Persistence;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * REST Web Service
 *
 * @author Vixo
 */
@Path("auth")
public class AuthResource {

    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
    private JsonParser paser;
    private JsonObject input;
    private IAuthentication authService;
    private IToken tokenService;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of AuthResource
     */
    public AuthResource() {
        authService = new AuthenticationService(new UserRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME)));
        tokenService = new TokenService();
    }

    @POST
    @Path("login")
    @Consumes("application/json")
    @Produces("application/json")
    public Response login(String credentials) {
        try {
            input = new JsonParser().parse(credentials).getAsJsonObject();
            String email = input.get("email").getAsString();
            String pass = input.get("password").getAsString();

            JsonObject response = new JsonObject();

            User user = authService.login(email, pass);

            String token = tokenService.createToken(user);

            return Response
                    .status(Response.Status.OK)
                    .entity(gson.toJson(user))
                    .type(MediaType.APPLICATION_JSON)
                    .header("Campleta", token)
                    .build();
        } catch (JOSEException e) {
            JsonObject error = new JsonObject();
            error.addProperty("message", "Could not login.");

            return Response.status(Response.Status.BAD_REQUEST).entity(error).type(MediaType.APPLICATION_JSON).build();
        }
    }
    
    @GET
    @Path("reauth")
    @Produces("application/json")
    @PermitAll
    public Response reAuthentication(@Context SecurityContext securityContext) {
        try {
            User user = authService.reAuthenticate(securityContext);
            String token = tokenService.createToken(user);
            
            return Response
                    .status(Response.Status.OK)
                    .entity(gson.toJson(user))
                    .type(MediaType.APPLICATION_JSON)
                    .header("Campleta", token)
                    .build();
        } catch (JOSEException e) {
            JsonObject error = new JsonObject();
            error.addProperty("message", "Could not authenticate");
            
            return Response.status(Response.Status.UNAUTHORIZED).entity(error).type(MediaType.APPLICATION_JSON).build();
        }
        
    }
}
