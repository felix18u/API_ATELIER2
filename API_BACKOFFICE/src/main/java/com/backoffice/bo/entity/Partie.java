package com.backoffice.bo.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "player")
public class Partie {

    @Id
    private String id;
    private String token;
    private String nb_photos;
    private String status;
    private String score;
    private String joueur;

    @ManyToOne
    @JoinColumn(name = "serie_id", nullable = false)
    private Series serie;

    @ManyToMany(cascade = {
        CascadeType.PERSIST,
        CascadeType.MERGE
    })

    @JoinTable(name = "partie_photo",
            joinColumns = @JoinColumn(name = "partie_id"),
            inverseJoinColumns = @JoinColumn(name = "photo_id"))
    private Set<Photo> photo = new HashSet<>();

    Partie() {
        //pour JPA	
    }

    public Partie(String token, String nb_photos, String status, String score, String joueur) {
        this.token = token;
        this.nb_photos = nb_photos;
        this.status = status;
        this.score = score;
        this.joueur = joueur;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNb_photos() {
        return this.nb_photos;
    }

    public void setNb_photos(String nb_photos) {
        this.nb_photos = nb_photos;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getScore() {
        return this.score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getJoueur() {
        return this.joueur;
    }

    public void setJoueur(String joueur) {
        this.joueur = joueur;
    }
    
    public void addPhoto(Photo photo) {
        this.photo.add(photo);
        photo.getPartie().add(this);
    }

    public Series getSerie() {
        return this.serie;
    }

    public void setSerie(Series serie) {
        this.serie = serie;
    }

    public Set<Photo> getPhoto() {
        return this.photo;
    }

    public void setPhoto(Set<Photo> photo) {
        this.photo = photo;
    }

}
