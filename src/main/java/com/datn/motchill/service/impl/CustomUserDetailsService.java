package com.datn.motchill.service.impl;

import com.datn.motchill.dto.MyUser;
import com.datn.motchill.entity.User;
import com.datn.motchill.enums.UserRoleEnum;
import com.datn.motchill.enums.UserStatusEnum;
import com.datn.motchill.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		final User user = userRepository.findOneByUsernameAndStatus(username, UserStatusEnum.ACTIVE);
		if(user == null) { 
			throw new UsernameNotFoundException("User not found!");
		}
		List<SimpleGrantedAuthority> authorities = Collections.singletonList(
				new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
		);
		MyUser myUser = new MyUser(user.getUsername(), user.getPassword(),
									true, true, true, true, authorities);
		// custom contains data
		myUser.setFullName(user.getFullName());
		myUser.setId(user.getId());
		return myUser;
	}

}
