/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.repo.impl;

import com.campleta.config.DatabaseCfg;
import com.campleta.models.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.NotFoundException;
import com.campleta.repo.interfaces.IUserRepo;
import javax.persistence.EntityExistsException;
import javax.persistence.RollbackException;

/**
 *
 * @author Vixo
 */
public class UserRepo implements IUserRepo{

    private EntityManagerFactory emf;
    private EntityManager em;
    
    public UserRepo() {
    }
    
    public UserRepo(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    @Override
    public User getUserByEmail(String email) {
        em = emf.createEntityManager();
        try {
            return (User) em.createQuery("SELECT u FROM User u WHERE u.email = :email")
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new NotFoundException(ex.getMessage());
        } finally {
            em.close();
        }
    }
    
    @Override
    public User getUserById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User getUserByPassport(String passport) {
        em = emf.createEntityManager();
        try {
            return (User) em.createQuery("SELECT u FROM User u WHERE u.passport = :passport")
                    .setParameter("passport", passport)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException(e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public User createUser(User user) {
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            
            return user;
        } catch (EntityExistsException e) {
            throw new EntityExistsException(e.getMessage());
        } finally {
            em.close();
        }
    }
}
