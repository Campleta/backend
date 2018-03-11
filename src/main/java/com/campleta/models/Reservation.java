/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.models;

import com.google.gson.annotations.Expose;
import org.eclipse.persistence.annotations.CascadeOnDelete;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author Vixo
 */
@Entity
@Table(name = "reservations")
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = true)
    @Expose
    private Campsite campsite;

    @Expose
    private AreaType areaType;

    @Expose
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date startDate;

    @Expose
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date endDate;

    @Expose
    @OneToMany (mappedBy = "reservation", orphanRemoval = true)
    private List<Stay> stays = new ArrayList<>();

    @Expose
    @ManyToOne
    private Area area;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Campsite getCampsite() {
        return campsite;
    }

    public void setCampsite(Campsite campsite) {
        this.campsite = campsite;
    }

    public AreaType getAreaType() {
        return areaType;
    }

    public void setAreaType(AreaType areaType) {
        this.areaType = areaType;
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

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public List<Stay> getStays() {
        return stays;
    }

    public void setStays(List<Stay> stays) {
        this.stays = stays;
    }
    
    public void addStays(Stay stay) {
        this.stays.add(stay);
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
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.campleta.models.Reservation[ id=" + id + " ]";
    }
    
}
