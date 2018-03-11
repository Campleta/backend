/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.models;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author Vixo
 */
@Entity
@Table(name = "areas")
public class Area implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Long id;

    @Expose
    private String name;

    @ManyToMany
    @JoinTable(name = "areas_areatypes", joinColumns = @JoinColumn(name = "area_id"), inverseJoinColumns = @JoinColumn(name = "areatype_id"))
    private List<AreaType> areaTypes = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "area_id", referencedColumnName = "id")
    private List<Reservation> reservations = new ArrayList<>();

    @ManyToOne
    @Expose
    private Campsite campsite;

    public Area() {}
    
    public Area(String name) {
        this.name = name;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AreaType> getAreaTypes() {
        return areaTypes;
    }

    public void setAreaTypes(List<AreaType> areaTypes) {
        this.areaTypes = areaTypes;
    }
    
    public void addAreaType(AreaType areaType) {
        this.areaTypes.add(areaType);
    }

    public Campsite getCampsite() {
        return campsite;
    }

    public void setCampsite(Campsite campsite) {
        this.campsite = campsite;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
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
        if (!(object instanceof Area)) {
            return false;
        }
        Area other = (Area) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.campleta.models.Area[ id=" + id + " ]";
    }
    
}
