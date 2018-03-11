/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.services.interfaces;

import com.campleta.models.User;
import com.nimbusds.jose.JOSEException;

/**
 *
 * @author Vixo
 */
public interface IToken {
    String createToken(User user) throws JOSEException;
}
