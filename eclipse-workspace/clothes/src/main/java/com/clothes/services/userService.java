package com.clothes.services;

import org.springframework.jdbc.core.JdbcTemplate;

import com.clothes.models.User;

public class userService {

	String validateUserInfo;
	
	String DBName;
	String DBPassword;
	JdbcTemplate jdbcTemplate;
	
	
	public void validate(User u) {
		
	}
	
	public User retrieveUser(int id) {

		return jdbcTemplate.queryForObject(
				"SELECT * FROM User where id=?",
				new Object[] { id }, new UserMapper());

	}
}
