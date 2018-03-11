/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.repo.impl;

import com.campleta.models.Role;
import com.campleta.repo.interfaces.IRoleRepo;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.ws.rs.NotFoundException;

/**
 *
 * @author Vixo
 */
public class RoleRepo  implements IRoleRepo {

    private EntityManagerFactory emf;
    private EntityManager em;
    
    public RoleRepo(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    @Override
    public Role getRoleByName(String name) {
        em = emf.createEntityManager();
        try {
            return (Role) em.createQuery("SELECT r FROM Role r WHERE r.name = :roleName")
                    .setParameter("roleName", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException(e.getMessage());
        } finally {
            em.close();
        }
    }
    
}
