/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.repo.interfaces;

import com.campleta.models.Role;

/**
 *
 * @author Vixo
 */
public interface IRoleRepo {
    Role getRoleByName(String name);
}
