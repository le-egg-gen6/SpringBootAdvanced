package com.myproject.springbootoauth2.service;

import com.myproject.springbootoauth2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author nguyenle
 * @since 10:58 AM Wed 3/12/2025
 */
@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
}
