package com.myproject.springbootoauth2.config.security.userdetail;

import com.myproject.springbootoauth2.entity.User;
import com.myproject.springbootoauth2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author nguyenle
 * @since 11:10 AM Wed 3/12/2025
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new MyUserDetails(user);
	}

	public UserDetails loadUserById(Long id) {
		User user = userRepository.findById(id).orElse(null);
		if (user == null) {
			throw new UsernameNotFoundException(String.valueOf(id));
		}
		return new MyUserDetails(user);
	}
}
