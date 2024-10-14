package com.example.streamlined.backend.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.streamlined.backend.Entity.RequestEntity;
import com.example.streamlined.backend.Entity.TechnicianEntity;
import com.example.streamlined.backend.Entity.UserEntity;

public interface TechnicianRepository extends JpaRepository <TechnicianEntity, Long>{

    Optional<TechnicianEntity> findById(Long tid);

    

   
    

}
