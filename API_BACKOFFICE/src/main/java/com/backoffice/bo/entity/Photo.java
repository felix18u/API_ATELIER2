package com.backoffice.bo.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Photo {

    @Id
    private String id;
    private String descr;
    private String lon;
    private String lat;
    private String url;

    @ManyToOne
    @JoinColumn(name = "serie_id", nullable = false)
    @JsonIgnore
    private Series serie;

    @ManyToMany(mappedBy = "photo")
    @JsonIgnore
    private Set<Partie> partie = new HashSet<>();
    
    Photo() {
        // necessaire pour JPA !
    }

    public Photo(String descr, String lon, String lat, String url, Series serie) {
        this.descr = descr;
        this.lon = lon;
        this.lat = lat;
        this.url = url;
        this.serie = serie;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public Series getSerie() {
        return this.serie;
    }

    public void setSerie(Series serie) {
        this.serie = serie;
    }
    public Set<Partie> getPartie() {
        return this.partie;
    }

    public void setPartie(Set<Partie> partie ) {
        this.partie = partie;
    }
    public String getDesc() {
        return this.descr;
    }

    public void setDesc(String descr) {
        this.descr = descr;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    
}