/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.services.interfaces;

import com.campleta.models.User;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author Vixo
 */
public interface IAuthentication {
    User login(String email, String password);
    void register(String email, String password, String repeat_password);
    User reAuthenticate(@Context SecurityContext securityContext);
    boolean validateCampsiteRelation(String email, Long campsiteId);
}
