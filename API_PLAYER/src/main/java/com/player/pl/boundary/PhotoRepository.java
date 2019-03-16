package com.player.pl.boundary;

import com.player.pl.entity.Photo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.*;

public interface PhotoRepository extends CrudRepository<Photo, String> {

    Page<Photo> findAll(Pageable pegeable);

    List<Photo> findAll();

    Optional<Photo> findById(String id);

}