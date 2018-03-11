/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.services.interfaces;

import com.campleta.models.Reservation;
import com.campleta.models.User;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Vixo
 */
public interface IBooking {

    /**
     * Creates a new Reservation.
     * @param content
     * @return Newly created Reservation.
     */
    Reservation book(String content);

    /**
     * Finds all Reservations from specific Campsite, with no Area attached.
     * @param campsiteId
     * @return List of Reservations.
     */
    List<Reservation> getNotPlacedReservations(int campsiteId);

    /**
     * Adds an Area to a given Reservation.
     * @param reservationId
     * @param areaId
     * @return Reservation with attached Area
     */
    Reservation addArea(int reservationId, int areaId);

    /**
     * Finds all reservation from specific date and forward in time.
     * @param areaId
     * @param fromDate
     * @return List of Reservation.
     */
    List<Reservation> getAreaReservations(int areaId, Date fromDate);

    /**
     * Finds a Reservation .
     * @param id
     * @return Reservation object.
     */
    Reservation getReservation(int id);

    /**
     * Updates a Reservation object.
     * @param reservation
     * @return Newly updated Reservation.
     */
    Reservation updateReservation(Reservation reservation);
}
