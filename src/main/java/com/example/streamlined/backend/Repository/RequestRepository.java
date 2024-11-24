package com.example.streamlined.backend.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.streamlined.backend.Entity.RequestEntity;

public interface RequestRepository extends JpaRepository<RequestEntity, Integer> {
}

