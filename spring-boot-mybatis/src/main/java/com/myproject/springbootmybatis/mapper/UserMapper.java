package com.myproject.springbootmybatis.mapper;

import com.myproject.springbootmybatis.entity.User;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @author nguyenle
 * @since 1:45 PM Mon 3/3/2025
 */
@Mapper
@Component
public interface UserMapper {

	List<User> findAll();

	Optional<User> findById(@Param("id") int id);

	void create(@Param("name") String name,
		@Param("age") int age,
		@Param("email") String email
	);

	void update(@Param("id") Integer id,
		@Param("name") String name,
		@Param("age") int age,
		@Param("email") String email
	);

	void delete(@Param("id") Integer id);
}
