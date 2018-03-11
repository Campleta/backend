/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.services.interfaces;

import com.campleta.models.Area;
import com.campleta.models.AreaType;
import com.google.gson.JsonObject;

import java.util.Date;
import java.util.List;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.NoContentException;

/**
 *
 * @author Vixo
 */
public interface IArea {
    /**
     * Find all area types with relation to specific Campsite.
     * @param campsiteId
     * @return List of AreaType.
     */
    List<AreaType> getAreaTypesFromCampsite(int campsiteId);
    
    /**
     * Find all areas related to campsite
     * @param campsiteId Id of campsite
     * @return List of areas
     * @throws NoContentException
     */
    List<JsonObject> getCampsiteAreas(int campsiteId, String fromDate, String toDate) throws NoContentException;

    /**
     * Find AreaType by name.
     * @param name Name of AreaType.
     * @return AreaType object.
     * @throws NotFoundException If AreaType does not exist.
     */
    AreaType getAreaTypeByName(String name) throws NotFoundException;
}
