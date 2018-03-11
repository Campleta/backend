/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.repo.impl;

import com.campleta.models.Stay;
import com.campleta.repo.interfaces.IStayRepo;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;
import javax.ws.rs.BadRequestException;

/**
 *
 * @author Vixo
 */
public class StayRepo implements IStayRepo {

    private EntityManagerFactory emf;
    private EntityManager em;
    
    public StayRepo() {
    }
    
    public StayRepo(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    @Override
    public Stay createStay(Stay stay) {
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(stay);
            em.getTransaction().commit();
            
            return stay;
        } catch (RollbackException e) {
            throw new BadRequestException(e.getMessage());
        } finally {
            em.close();
        }
    }
    
}
