package com.backoffice.bo.boundary;

import com.backoffice.bo.entity.Partie;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;


public interface PartieRepository extends CrudRepository<Partie,String>{
	
    Page<Partie> findAll(Pageable pegeable);

    List<Partie> findAll();

    Optional<Partie> findById(String id);
}
