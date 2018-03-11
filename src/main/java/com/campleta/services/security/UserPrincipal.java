package com.campleta.services.security;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 REF: http://scytl.github.io/restguide/#_security_2
 */
public class UserPrincipal implements Principal {

    private String email;
    private List<String> roles = new ArrayList<>();

    public UserPrincipal(String email, List<String> roles) {
        super();
        this.email = email;
        this.roles = roles;
    }

    @Override
    public String getName() {
        return email;
    }

    public boolean isUserInRole(String role) {
        return this.roles.contains(role);
    }
}
