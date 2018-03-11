/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.repo.impl;

import com.campleta.models.Role;
import com.campleta.models.User;
import java.util.ArrayList;
import java.util.List;
import com.campleta.repo.interfaces.IUserRepo;

/**
 *
 * @author Vixo
 */
public class StubUserRepo implements IUserRepo{

    @Override
    public User getUserByEmail(String email) {
        
        if(email.equals("null@null.dk")) {
            return null;
        }
        List<Role> roles = new ArrayList<>();
        roles.add(new Role("Admin"));
        User user = new User(email, "Admin", "User", "1234", roles);
        
        return user;
    }

    @Override
    public User getUserById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User getUserByPassport(String passport) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User createUser(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
