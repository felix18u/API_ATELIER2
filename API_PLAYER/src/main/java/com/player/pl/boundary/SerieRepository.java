package com.player.pl.boundary;

import com.player.pl.entity.Series;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface SerieRepository extends CrudRepository<Series, String> {

    Page<Series> findAll(Pageable pegeable);

    List<Series> findAll();

    Optional<Series> findById(String id);

}