package com.backoffice.bo.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "series")
public class Series { 
	
    @Id
    private String id;
    private String ville;
    private String map_ref;
    private String dist;

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
