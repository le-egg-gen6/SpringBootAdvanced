package com.myproject.springbootoauth2.repository;

import com.myproject.springbootoauth2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author nguyenle
 * @since 10:41 AM Wed 3/12/2025
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
}
