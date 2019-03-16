package com.backoffice.bo.boundary;

import com.backoffice.bo.entity.Series;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface SeriesRepository extends CrudRepository<Series, String> {

    Page<Series> findAll(Pageable pegeable);

    List<Series> findAll();

    Optional<Series> findById(String id);

}