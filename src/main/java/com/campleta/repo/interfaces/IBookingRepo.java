/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.repo.interfaces;

import com.campleta.models.Area;
import com.campleta.models.Reservation;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Vixo
 */
public interface IBookingRepo {
    /**
     * Finds a reservation by passed id
     * @param id Id of reservation
     * @return Reservation
     */
    Reservation getReservationById(int id);

    /**
     * Stores the Reservation object in the database.
     * @param reservation
     * @return Reservation object if succesfully stored.
     */
    Reservation createReservation(Reservation reservation);

    /**
     * Attaches an Area to a Reservation
     * @param reservation
     * @param area
     * @return Reservation with attached Area.
     */
    Reservation addAreaToReservation(Reservation reservation, Area area);

    /**
     * Gets all Reservations with no Area attached.
     * @return List of Reservations
     */
    List<Reservation> getNotPlacedReservations();

    /**
     * Gets all Reservations from specific Campsite, with no Area attached.
     * @param campsiteId
     * @return List of Reservations
     */
    List<Reservation> getNotPlacedReservationsFromCampsite(int campsiteId);

    /**
     * Returns reservations for specific area, from date.
     * @param fromDate
     * @return
     */
    List getReservations(Area area, Date fromDate);

    /**
     * Updates a Reservation.
     * @param reservation
     * @return Newly updated Reservation.
     */
    Reservation updateReservation(Reservation reservation);
}
