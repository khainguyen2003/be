package com.datn.motchill.service.impl;

import com.datn.motchill.common.exceptions.NotFoundException;
import com.datn.motchill.dto.user.UserDTO;
import com.datn.motchill.dto.user.UserFilterDTO;
import com.datn.motchill.entity.User;
import com.datn.motchill.enums.UserRoleEnum;
import com.datn.motchill.enums.UserStatusEnum;
import com.datn.motchill.repository.UserRepository;
import com.datn.motchill.service.UserService;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final ModelMapper modelMapper;
	private UserRepository userRepository;

	private ModelMapper mapper;

	private PasswordEncoder passwordEncoder;

	public Page<UserDTO> search(UserFilterDTO filterDTO) {
		Specification<User> specification = getSearchSpecification(filterDTO);
		Pageable pageable = PageRequest.of(filterDTO.getPage(), filterDTO.getLimit());
		return userRepository.findAll(specification, pageable).map((element) -> mapper.map(element, UserDTO.class));
	}

	@Override
	@Transactional(readOnly = true)
	public UserDTO findByUsername(String username) {
		return userRepository.findByUsername(username)
				.map(user -> mapper.map(user, UserDTO.class))
				.orElseThrow(() -> new NotFoundException("User not found with username: " + username));
	}

	@Override
	@Transactional(readOnly = true)
	public UserDTO findByEmail(String email) {
		return userRepository.findByEmail(email)
				.map(user -> mapper.map(user, UserDTO.class))
				.orElseThrow(() -> new NotFoundException("User not found with email: " + email));
	}

	@Override
	public UserDTO save(UserDTO dto) {
		
		User userEntity = new User();
		// update
		if(dto.getId() != null) {
			Optional<User> oldUser = userRepository.findById(dto.getId());
			if(oldUser.isPresent()) {
				if(!dto.getPassword().equals(oldUser.get().getPassword())){ // update new password
					oldUser.get().setPassword(passwordEncoder.encode(dto.getPassword()));
				}
				if(dto.getAvatar() == null || dto.getAvatar().isEmpty())
					dto.setAvatar(oldUser.get().getAvatar()); // set default url from old
			}
		}
		// create
		else {
			userEntity = mapper.map(dto, User.class);
			userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
			userEntity.setStatus(UserStatusEnum.ACTIVE);
		}
		// set roles
		if(dto.getRole() == null) {
			userEntity.setRole(UserRoleEnum.USER);
		}
		return mapper.map(userRepository.save(userEntity), UserDTO.class);
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	public UserDTO findOneByEmailAndStatus(String email, UserStatusEnum status) {
		User entity = userRepository.findOneByEmailAndStatus(email, status);
		if(entity != null) {
			return mapper.map(entity, UserDTO.class);
		}
		return null;
	}

	@Override
	public long getTotalItem() {
		return userRepository.count();
	}

	@Override
	public UserDTO findById(Long id) {
		Optional<User> userEntity = userRepository.findById(id);
		if(userEntity.isPresent()) {
			UserDTO dto = mapper.map(userEntity.get(), UserDTO.class);
			return dto;
		}
		return null;
	}

	private Specification<User> getSearchSpecification(final UserFilterDTO request) {
		return new Specification<>() {

			private static final long serialVersionUID = 6345534328548406667L;

			@Override
			@Nullable
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query,
										 CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();

				if (request.getSearch() != null && !request.getSearch().isEmpty()) {
					String search = "%" + request.getSearch().toLowerCase() + "%";

					Predicate fullNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), search);
					Predicate emailPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), search);
					Predicate phonePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), search);

					predicates.add(criteriaBuilder.or(fullNamePredicate, emailPredicate, phonePredicate));
				}

				if (request.getRole() != null && !request.getRole().isEmpty()) {
					Path<UserRoleEnum> rolePath = root.get("role");
					predicates.add(rolePath.in(request.getRole()));
				}

				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}

		};
	}
}
