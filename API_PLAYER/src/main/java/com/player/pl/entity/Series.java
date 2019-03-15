package com.player.pl.entity;

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
    private String map_ref;
    private String dist;

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Photo> photo = new HashSet<>();

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Partie> partie = new HashSet<>();
    Series(){
        
     //pour JPA	
    }

    public Series(String ville, String map_ref, String dist) {
        this.ville = ville;
        this.map_ref = map_ref;
        this.dist = dist;
    }

    public String getId()
	{
		return this.id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getVille()
	{
		return this.ville;
	}

	public void setVille(String ville)
	{
		this.ville = ville;
	}

	public String getMap_ref()
	{
		return this.map_ref;
	}

	public void setMap_ref(String map_ref)
	{
		this.map_ref = map_ref;
	}

	public String getDist()
	{
		return this.dist;
	}

	public void setDist(String dist)
	{
		this.dist = dist;
	}
    
}
