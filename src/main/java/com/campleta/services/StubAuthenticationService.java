/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.services;

import com.campleta.models.User;
import com.campleta.services.interfaces.IAuthentication;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author Vixo
 */
public class StubAuthenticationService implements IAuthentication {

    @Override
    public User login(String email, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void register(String email, String password, String repeat_password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean validateCampsiteRelation(String email, Long campsiteId) {
        return false;
    }

    @Override
    public User reAuthenticate(SecurityContext securityContext) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
