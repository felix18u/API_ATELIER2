package com.backoffice.bo.boundary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.bo.entity.Series;

public interface SeriesRepository extends JpaRepository<Series,String>{
	
	
}
