/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.repo.interfaces;

import com.campleta.models.User;

/**
 *
 * @author Vixo
 */
public interface IUserRepo {
    /**
     * Gets user from database by searching for email.
     * @param email
     * @return User
     * @throws NotFoundException when there's no user with the input email.
     */
    User getUserByEmail(String email);
    User getUserById(Long id);
    User getUserByPassport(String passport);
    User createUser(User user);
}
