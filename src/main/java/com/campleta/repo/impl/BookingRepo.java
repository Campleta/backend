/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.repo.impl;

import com.campleta.models.Area;
import com.campleta.models.Reservation;
import com.campleta.models.Stay;
import com.campleta.models.User;
import com.campleta.repo.interfaces.IBookingRepo;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.ws.rs.NotFoundException;

/**
 *
 * @author Vixo
 */
public class BookingRepo implements IBookingRepo {

    private EntityManagerFactory emf;
    private EntityManager em;
    
    public BookingRepo(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Reservation getReservationById(int id) {
        em = emf.createEntityManager();
        try {
            return (Reservation) em.find(Reservation.class, Long.parseLong(String.valueOf(id)));
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public Reservation createReservation(Reservation reservation) {
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            for(Stay stay : reservation.getStays()) {
                em.persist(stay);
            }
            em.persist(reservation);
            em.getTransaction().commit();
            
            return reservation;
        } catch (RollbackException e) {
            throw new RollbackException(e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public Reservation addAreaToReservation(Reservation reservation, Area area) {
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(reservation);
            em.merge(area);
            em.getTransaction().commit();

            return reservation;
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Reservation> getNotPlacedReservations() {
        em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT r FROM Reservation r WHERE r.area IS NULL")
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Reservation> getNotPlacedReservationsFromCampsite(int campsiteId) {
        em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT r FROM Reservation r WHERE r.area IS NULL AND r.campsite.id = :campsiteId")
                    .setParameter("campsiteId", campsiteId)
                    .getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Reservation> getReservations(Area area, Date fromDate) {
        em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT r FROM Reservation r WHERE r.area = :area AND r.endDate > :startDate")
                    .setParameter("area", area)
                    .setParameter("startDate", fromDate, TemporalType.TIMESTAMP)
                    .getResultList();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public Reservation updateReservation(Reservation reservation) {
        em = emf.createEntityManager();
        try {
            Reservation res = em.find(Reservation.class, reservation.getId());
            if(res == null) return null;

            em.getTransaction().begin();
            Reservation result = (Reservation) em.merge(reservation);
            em.getTransaction().commit();

            return result;
        } catch (IllegalArgumentException argument) {
            return null;
        } finally {
            em.close();
        }
    }
}
