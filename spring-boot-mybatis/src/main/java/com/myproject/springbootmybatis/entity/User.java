package com.myproject.springbootmybatis.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author nguyenle
 * @since 1:44 PM Mon 3/3/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

	private Integer id;

	private String name;

	private Integer age;

	private String email;

}
