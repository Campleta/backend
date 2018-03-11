/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.services;

import com.campleta.models.Area;
import com.campleta.models.AreaType;
import com.campleta.models.Campsite;
import com.campleta.repo.interfaces.IAreaRepo;
import com.campleta.repo.interfaces.ICampsiteRepo;
import com.campleta.services.interfaces.IArea;
import com.google.gson.JsonObject;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.NoContentException;

/**
 *
 * @author Vixo
 */
public class AreaService implements IArea {

    private DateTimeFormatter formatter = ISODateTimeFormat.dateTimeParser().withZoneUTC();
    private IAreaRepo areaRepo;
    private ICampsiteRepo campsiteRepo;
    
    public AreaService(IAreaRepo areaRpo, ICampsiteRepo campsiteRepo) {
        this.areaRepo = areaRpo;
        this.campsiteRepo = campsiteRepo;
    }
    
    @Override
    public List<AreaType> getAreaTypesFromCampsite(int campsiteId) {
        Campsite campsite = campsiteRepo.getById(campsiteId);
        return areaRepo.getAreaTypesByCampsite(campsite);
    }

    @Override
    public List<JsonObject> getCampsiteAreas(int campsiteId, String fromDate, String toDate) throws NoContentException {
        Campsite campsite = campsiteRepo.getById(campsiteId);

        DateTime startDateFormat = formatter.parseDateTime(fromDate);
        DateTime endDateFormat = formatter.parseDateTime(toDate);
        Calendar startDate = startDateFormat.toCalendar(Locale.GERMAN);
        Calendar endDate = endDateFormat.toCalendar(Locale.GERMAN);

        List<JsonObject> areas = areaRepo.getCampsiteAreasWithFilteredReservationsBetweenDates(campsite, startDate.getTime(), endDate.getTime());
        if(areas.size() <= 0) throw new NoContentException("No areas for this campsite.");
        return areas;
    }

    @Override
    public AreaType getAreaTypeByName(String name) throws NotFoundException {
        try {
            return areaRepo.getAreaTypeByName(name);
        } catch (NotFoundException notFound) {
            throw new NotFoundException("AreaType not found.");
        }
    }

}
