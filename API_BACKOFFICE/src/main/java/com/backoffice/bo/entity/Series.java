package com.backoffice.bo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "series")
public class Series {

    @Id
    private String id;
    private String ville;
    private String lon1;
    private String lat1;
    private String lon2;
    private String lat2;
    private String dist;

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Photo> photo = new HashSet<>();

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Partie> partie = new HashSet<>();

    Series() {

        //pour JPA	
    }

    public Series(String ville, String lon1, String lat1, String lon2, String lat2, String dist) {
        this.ville = ville;
        this.lon1 = lon1;
        this.lat1 = lat1;
        this.lon2 = lon2;
        this.lat2 = lat2;
        this.dist = dist;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVille() {
        return this.ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getDist() {
        return this.dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }
    
    public Set<Photo> getPhoto() {
        return this.photo;
    }

    public void setPhoto(Set<Photo> photo) {
        this.photo = photo;
    }

    public Set<Partie> getPartie() {
        return this.partie;
    }

    public void setPartie(Set<Partie> partie) {
        this.partie = partie;
    }

    public String getLon1() {
        return lon1;
    }

    public void setLon1(String lon1) {
        this.lon1 = lon1;
    }

    public String getLat1() {
        return lat1;
    }

    public void setLat1(String lat1) {
        this.lat1 = lat1;
    }

    public String getLon2() {
        return lon2;
    }

    public void setLon2(String lon2) {
        this.lon2 = lon2;
    }

    public String getLat2() {
        return lat2;
    }

    public void setLat2(String lat2) {
        this.lat2 = lat2;
    }
    
    

}
