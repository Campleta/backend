/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.repo.impl;

import com.campleta.models.Area;
import com.campleta.models.AreaType;
import com.campleta.models.Campsite;
import com.campleta.models.Reservation;
import com.campleta.repo.interfaces.IAreaRepo;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.NoContentException;

/**
 *
 * @author Vixo
 */
public class AreaRepo implements IAreaRepo {
    
    private EntityManagerFactory emf;
    private EntityManager em;
    
    public AreaRepo(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Area getAreaById(int id) {
        em = emf.createEntityManager();
        try {
            return em.find(Area.class, Long.parseLong(String.valueOf(id)));
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public int findAmountAvailableAreasByAreaType(int areaType) {
        em = emf.createEntityManager();
        try {
            Object object = em.createQuery("SELECT COUNT(a) FROM Area a INNER JOIN a.areaTypes at WHERE at.id = :areaType")
                    .setParameter("areaType", areaType)
                    .getSingleResult();
            return Integer.parseInt(object.toString());
        } catch(NoResultException e) {
            throw new NotFoundException(e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public AreaType getAreaTypeByName(String name) {
        em = emf.createEntityManager();
        try {
            return (AreaType) em.createQuery("SELECT at FROM AreaType at WHERE at.name = :areaTypeName")
                    .setParameter("areaTypeName", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException(e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public AreaType getAreaTypeById(int id) {
        em = emf.createEntityManager();
        try {
            AreaType a = em.find(AreaType.class, Long.parseLong(String.valueOf(id)));
            return a;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotFoundException(e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public List<AreaType> getAreaTypesByCampsite(Campsite campsite) {
        em = emf.createEntityManager();
        try {
            List<AreaType> areatypes = em.createQuery("SELECT at FROM AreaType at WHERE at.campsites = :campsite")
                    .setParameter("campsite", campsite)
                    .getResultList();
            if(areatypes.isEmpty()) throw new NoContentException("No areatypes for this campsite.");
            return areatypes;
        } catch (NoContentException e) {
            throw new NotFoundException(e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public List<JsonObject> getCampsiteAreasWithFilteredReservationsBetweenDates(Campsite campsite, Date startDate, Date endDate) {
        em = emf.createEntityManager();
        try {
            // Returning all areas with all reservations (should filter reservations when creating json object
            List<Object[]> objects = em.createNativeQuery("SELECT DISTINCT a.ID, a.NAME, " +
                    "(CASE WHEN EXISTS " +
                    "(SELECT * FROM reservations re WHERE re.STARTDATE < ?endDate AND re.ENDDATE > ?startDate AND r.ID = re.ID) " +
                    "THEN 0 ELSE 1 END ) " +
                    "AS AVAILABLE FROM areas a " +
                    "LEFT JOIN reservations r ON r.area_id = a.ID WHERE a.CAMPSITE_ID = ?campsite " +
                    "GROUP BY a.ID, a.NAME")
                    .setParameter("endDate", endDate, TemporalType.TIMESTAMP)
                    .setParameter("startDate", startDate, TemporalType.TIMESTAMP)
                    .setParameter("campsite", campsite.getId())
                    .getResultList();

            List<JsonObject> areas = new ArrayList<>();
            for(Object[] obj : objects) {
                JsonObject json = new JsonObject();
                json.addProperty("Id", obj[0].toString());
                json.addProperty("Name", obj[1].toString());
                boolean available = true;
                if(Integer.parseInt(obj[2].toString()) == 0) available = false;
                json.addProperty("Available", available);

                areas.add(json);
            }

            return areas;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotFoundException(e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public JsonObject getAreaWithFilteredReservationsBetweenDates(int areaId, Date startDate, Date endDate) {
        em = emf.createEntityManager();
        try {
            JsonObject json = new JsonObject();
            List<Object[]> obj = em.createNativeQuery("SELECT DISTINCT a.ID, a.NAME, " +
                    "(CASE WHEN EXISTS " +
                    "(SELECT * FROM reservations re WHERE re.STARTDATE < ?endDate AND re.ENDDATE > ?startDate AND r.ID = re.ID) " +
                    "THEN 0 ELSE 1 END ) " +
                    "AS AVAILABLE FROM areas a " +
                    "LEFT JOIN reservations r ON r.area_id = a.ID WHERE a.ID = ?areaId")
                    .setParameter("endDate", endDate, TemporalType.TIMESTAMP)
                    .setParameter("startDate", startDate, TemporalType.TIMESTAMP)
                    .setParameter("areaId", areaId)
                    .getResultList();

            json.addProperty("Id", obj.get(0)[0].toString());
            json.addProperty("Name", obj.get(0)[1].toString());
            boolean available = true;
            if(Integer.parseInt(obj.get(0)[2].toString()) == 0) available = false;
            json.addProperty("Available", available);

            return json;
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

}
