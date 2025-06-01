package com.datn.motchill.service;

import com.datn.motchill.dto.user.UserDTO;
import com.datn.motchill.dto.user.UserFilterDTO;
import com.datn.motchill.enums.UserStatusEnum;
import org.springframework.data.domain.Page;

public interface UserService {

	Page<UserDTO> search(UserFilterDTO filterDTO);
	UserDTO save(UserDTO dto);
	UserDTO findOneByEmailAndStatus(String email, UserStatusEnum status);

	UserDTO findById(Long id);
	UserDTO findByUsername(String username);
	UserDTO findByEmail(String email);
	long getTotalItem();
	boolean existsByEmail(String email);
	boolean existsByUsername(String username);
}
