package com.datn.motchill.repository;

import com.datn.motchill.entity.User;
import com.datn.motchill.enums.UserStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	User findOneByUsernameAndStatus(String name, UserStatusEnum status);
	User findOneByEmailAndStatus(String email, UserStatusEnum status);
	boolean existsByEmail(String email);
	boolean existsByUsername(String username);

	// Các phương thức cần bổ sung
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
}
