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
@Table(name = "areatypes")
public class AreaType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Long id;
    @Expose
    private String name;
    @Expose
    @ManyToMany
    @JoinTable(name = "campsites_areatypes",
            joinColumns = @JoinColumn(name = "areatype_id"), inverseJoinColumns = @JoinColumn(name = "campsite_id"))
    private List<Campsite> campsites = new ArrayList<>();
    @ManyToMany(mappedBy = "areaTypes")
    private List<Area> areas = new ArrayList<>();

    public AreaType() {
    }
    
    public AreaType(String name) {
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

    public List<Campsite> getCampsites() {
        return campsites;
    }

    public void setCampsites(List<Campsite> campsites) {
        this.campsites = campsites;
    }

    public void addCampsite(Campsite campsite) {
        this.campsites.add(campsite);
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
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AreaType)) {
            return false;
        }
        AreaType other = (AreaType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.campleta.models.SpotType[ id=" + id + " ]";
    }
    
}
