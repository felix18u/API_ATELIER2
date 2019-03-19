package com.backoffice.bo.boundary;

import com.backoffice.bo.entity.Photo;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.Path;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoRepository extends CrudRepository<Photo, String> {

    Page<Photo> findAll(Pageable pegeable);

    List<Photo> findAll();

    Optional<Photo> findById(String id);
    
    void init();

    void store(MultipartFile file);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

}