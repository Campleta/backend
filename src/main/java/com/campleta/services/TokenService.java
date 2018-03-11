/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.services;

import com.campleta.models.Role;
import com.campleta.models.User;
import com.campleta.services.interfaces.IToken;
import com.campleta.services.security.Secrets;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author Vixo
 */
public class TokenService implements IToken {

    private static final String ISSUER = "Campleta-usettoken";

    private JWSSigner signer;
    private JWTClaimsSet claim;
    private SignedJWT signedJWT;

    public TokenService() {
    }

    @Override
    public String createToken(User user) throws JOSEException {
        String rolesString = getRolesAsString(user.getRoles());
        List<String> roles = new ArrayList<>();
        for(Role role : user.getRoles()) {
            roles.add(role.getName());
        }

        signer = new MACSigner(Secrets.TOKEN_SECRET.getBytes());
        Calendar date = Calendar.getInstance();
        Calendar expDate = date;
        expDate.add(Calendar.HOUR_OF_DAY, 24);
        claim = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .claim("email", user.getEmail())
                .claim("roles", roles)
                .issueTime(date.getTime())
                .expirationTime(expDate.getTime())
                .issuer(ISSUER)
                .build();

        signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claim);
        signedJWT.sign(signer);
        
        return signedJWT.serialize();
    }

    private String getRolesAsString(List<Role> roles) {
        StringBuilder strBuilder = new StringBuilder();
        for (Role role : roles) {
            strBuilder.append(role.getName());
            strBuilder.append(",");
        }

        String rolesAsString = strBuilder.substring(0, strBuilder.length() - 1);

        if (strBuilder.length() <= 0) {
            throw new IndexOutOfBoundsException();
        }

        return rolesAsString;
    }

}
