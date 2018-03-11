/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.repo.impl;

import com.campleta.models.Campsite;
import com.campleta.repo.interfaces.ICampsiteRepo;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.NotFoundException;

/**
 *
 * @author Vixo
 */
public class CampsiteRepo implements ICampsiteRepo {

    private EntityManagerFactory emf;
    private EntityManager em;
    
    public CampsiteRepo(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    @Override
    public Campsite getById(int id) {
        em = emf.createEntityManager();
        try {
            Campsite campsite = (Campsite) em.find(Campsite.class, Long.parseLong(String.valueOf(id)));
            if(campsite == null) throw new NotFoundException();
            
            return campsite;
        } catch (Exception e) {
            throw new NotFoundException();
        } finally {
            em.close();
        }
    }
    
}
