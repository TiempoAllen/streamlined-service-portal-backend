package com.example.streamlined.backend.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.streamlined.backend.Entity.UserEntity;

public interface UserRepository extends JpaRepository <UserEntity, Integer>{
	Optional<UserEntity> findByUserId(Long user_id);
	UserEntity findByEmail(String email);
	boolean existsByEmail(String email);
	List<UserEntity> findByIsadmin(Boolean isadmin);

	Optional<UserEntity> findByUsername(String username);
}
