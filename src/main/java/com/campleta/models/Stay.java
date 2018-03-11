/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.models;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author Vixo
 */
@Entity
public class Stay implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Long id;

    @ManyToOne
    private Reservation reservation;

    @OneToMany
    @Expose
    private List<User> guests = new ArrayList<>();

    @Expose
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Expose
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public List<User> getGuests() {
        return guests;
    }

    public void setGuests(List<User> guests) {
        this.guests = guests;
    }
    
    public void addGuest(User guest) {
        this.guests.add(guest);
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Stay)) {
            return false;
        }
        Stay other = (Stay) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.campleta.models.Stay[ id=" + id + " ]";
    }
    
}
