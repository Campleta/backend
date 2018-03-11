/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.services;

import com.campleta.models.Campsite;
import com.campleta.models.User;
import com.campleta.services.interfaces.IAuthentication;
import com.campleta.services.security.Sha3;
import javax.persistence.NoResultException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import com.campleta.repo.interfaces.IUserRepo;
import java.security.Principal;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author Vixo
 */
public class AuthenticationService implements IAuthentication {

    private IUserRepo authRepo;
    private Principal principal;

    public AuthenticationService(IUserRepo authRepo) {
        this.authRepo = authRepo;
    }

    /**
     * Tries to login the user.
     *
     * @param email Email of user.
     * @param password Password of user.
     * @return User if validation is correct.
     * @throws BadRequestException if either email or password is null or empty. Also throws BadRequestException if email does not contain "@".
     */
    @Override
    public User login(String email, String password) {
        if (!checkNotNull(email) || !checkNotNull(password)) {
            throw new BadRequestException();
        }
        if (!email.contains("@")) {
            throw new BadRequestException();
        }

        return validate(email, password);
    }

    @Override
    public User reAuthenticate(@Context SecurityContext securityContext) {
        principal = securityContext.getUserPrincipal();
        if(principal == null) throw new NotAuthorizedException("Not authorized.");
        
        return authRepo.getUserByEmail(principal.getName());
    }

    @Override
    public void register(String email, String password, String repeat_password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Checks if user exist and if it does, it check if password matches.
     *
     * @param email
     * @param password
     * @return User if user exist and password matches.
     * @throws NotFoundException if user does not exist in the database. BadRequestException if user does exist but password does not match.
     */
    private User validate(String email, String password) {
        try {
            User user = authRepo.getUserByEmail(email);
            if (user == null) {
                throw new NotFoundException();
            }

            if (!user.getPassword().equals(Sha3.encode(password))) {
                throw new BadRequestException();
            }
            return user;
        } catch (BadRequestException badReq) {
            throw new BadRequestException(badReq.getMessage());
        } catch (NotFoundException notFound) {
            throw new NotFoundException(notFound.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotFoundException(e.getMessage());
        }
    }

    /**
     * Checks if String is null or is empty String.
     *
     * @param param String to check.
     * @return true if String is not null or empty, or false if String is null or empty String.
     */
    private boolean checkNotNull(String param) {
        return (param != null) && (!param.isEmpty());
    }

    @Override
    public boolean validateCampsiteRelation(String email, Long campsiteId) {
        try {
            User user = authRepo.getUserByEmail(email);
            if (user == null) {
                throw new NotFoundException("User not found");
            }

            List<Campsite> campsites = user.getCampsites();
            for(Campsite campsite : campsites) {
                if (campsite != null || campsite.getId().equals(campsiteId)) {
                    return true;
                }
            }

            return false;
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

}
