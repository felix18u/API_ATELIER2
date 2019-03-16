package com.backoffice.bo.boundary;

import com.backoffice.bo.entity.Photo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface PhotoRepository extends CrudRepository<Photo, String> {

    Page<Photo> findAll(Pageable pegeable);

    List<Photo> findAll();

    Optional<Photo> findById(String id);

}