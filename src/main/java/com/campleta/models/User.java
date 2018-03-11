/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.models;

import com.campleta.services.security.Sha3;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 *
 * @author Vixo
 */
@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose 
    private Long id;
    @Expose 
    @Column(unique = true)
    private String email;
    @Column(unique = true, nullable = true)
    @Expose
    private String passport;
    @Expose 
    private String firstname;
    @Expose 
    private String lastname;
    private String password;
    @ManyToMany
    @Expose 
    private List<Role> roles = new ArrayList<>();
    @ManyToMany(mappedBy = "employees")
    @JoinColumn(nullable = true)
    @Expose
    private List<Campsite> campsites = new ArrayList<>();

    public User() {}
    
    public User(String email) {
        this.email = email;
    }
    
    public User(String email, String firstname, String lastname) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
    }
    
    public User(String email, String firstname, String lastname, String password, List<Role> roles) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = Sha3.encode(password);
        this.roles = roles;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = Sha3.encode(password);
    }

    public List<Role> getRoles() {
        return roles;
    }
    
    public List<String> getRolesAsString() {
        List<String> userRoles = new ArrayList();
        
        for (Role role : roles) {
            userRoles.add(role.getName());
        }
        return userRoles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
    
    public void addRole(Role role) {
        this.roles.add(role);
    }

    public List<Campsite> getCampsites() {
        return campsites;
    }

    public void setCampsite(List<Campsite> campsites) {
        this.campsites = campsites;
    }
    
    public void addCampsite(Campsite campsite) {
        this.campsites.add(campsite);
    }
    
    public JsonObject toJsonObject() {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", this.id);
        obj.addProperty("email", this.email);
        obj.addProperty("firstname", this.firstname);
        obj.addProperty("lastname", this.lastname);
        JsonArray roles = new JsonArray();
        for(Role role : this.roles) {
            roles.add(role.toJsonObject());
        }
        obj.add("roles", roles);
        if(this.campsites != null && this.campsites.size() > 0) {
            JsonArray jsonCampsites = new JsonArray();
            for(Campsite c : this.campsites) {
                JsonObject cObj = c.toJsonObject();
                jsonCampsites.add(cObj);
            }
            obj.add("campsites", jsonCampsites);
        }
        
        return obj;
    }
    
}
