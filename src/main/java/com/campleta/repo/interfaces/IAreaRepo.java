/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.repo.interfaces;

import com.campleta.models.Area;
import com.campleta.models.AreaType;
import com.campleta.models.Campsite;
import com.campleta.models.Reservation;
import com.google.gson.JsonObject;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Vixo
 */
public interface IAreaRepo {

    /**
     * Finds specific area from id
     * @param id
     * @return Area object
     */
    Area getAreaById(int id);

    /**
     * Finds number of available areas in specific areaType.
     * @param areaType
     * @return 
     */
    int findAmountAvailableAreasByAreaType(int areaType);
    AreaType getAreaTypeByName(String name);
    AreaType getAreaTypeById(int id);
    List<AreaType> getAreaTypesByCampsite(Campsite campsite);
    
    /**
     * Finds all areas in a campsite
     * @param campsite Campsite to look for areas in.
     * @return List of Area.
     */
    List<JsonObject> getCampsiteAreasWithFilteredReservationsBetweenDates(Campsite campsite, Date startDate, Date endDate);

    /**
     * Returns single area with filtered reservations
     * @param areaId
     * @param startDate
     * @param endDate
     * @return
     */
    JsonObject getAreaWithFilteredReservationsBetweenDates(int areaId, Date startDate, Date endDate);
}
