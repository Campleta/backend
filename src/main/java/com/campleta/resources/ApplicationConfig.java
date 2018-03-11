package com.campleta.resources;

import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.campleta.resources.AreaResource.class);
        resources.add(com.campleta.resources.AreaTypeResource.class);
        resources.add(com.campleta.resources.AuthResource.class);
        resources.add(com.campleta.resources.BookingResource.class);
        resources.add(com.campleta.resources.filters.CorsFilter.class);
        resources.add(com.campleta.resources.filters.JWTAuthenticationFilter.class);
        resources.add(com.campleta.resources.filters.RolesAllowedFilter.class);
    }
    
}
