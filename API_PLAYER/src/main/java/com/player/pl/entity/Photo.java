package com.player.pl.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
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
    private String position;
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

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}