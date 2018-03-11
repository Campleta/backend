/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.models;

import com.google.gson.JsonObject;
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
@Table(name = "campsites")
public class Campsite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Long id;
    @Expose
    private String name;
    @Expose
    private String mapUrl;
    @ManyToMany
    @JoinTable(name = "campsites_users",
            joinColumns = @JoinColumn(name = "campsite_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> employees = new ArrayList<>();
    @OneToMany
    @JoinColumn(name = "campsite_id", referencedColumnName = "id")
    private List<Area> areas = new ArrayList<>();
    @OneToMany
    @JoinColumn(name = "campsite_id", referencedColumnName = "id")
    private List<Reservation> reservations = new ArrayList<>();

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

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public List<User> getEmployees() {
        return employees;
    }

    public void setEmployees(List<User> employees) {
        this.employees = employees;
    }
    
    public void addEmployee(User user) {
        employees.add(user);
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }
    
    public void addArea(Area area) {
        this.areas.add(area);
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
    
    public JsonObject toJsonObject() {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", this.id);
        obj.addProperty("name", this.name);
        
        return obj;
    }
}
