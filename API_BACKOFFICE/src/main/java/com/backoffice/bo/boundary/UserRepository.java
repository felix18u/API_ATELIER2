package com.backoffice.bo.boundary;

import com.backoffice.bo.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User,String>{
	
    Page<User> findAll(Pageable pegeable);

    List<User> findAll();

    Optional<User> findById(String id);
    
    Optional<User> findByUsername(String id);
}
