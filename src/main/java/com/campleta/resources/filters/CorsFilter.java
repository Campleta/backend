/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.resources.filters;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Vixo
 */
@Provider
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext response) {
        response.getHeaders().putSingle("Access-Control-Allow-Origin", "http://localhost:4200");
        response.getHeaders().putSingle("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PUT, DELETE");
        response.getHeaders().putSingle("Access-Control-Allow-Headers", "Content-Type,x-requested-with,Authorization");
        response.getHeaders().putSingle("Access-Control-Expose-Headers", "Content-Type,x-requested-with,Campleta");
    }
    
}
