package com.clothes.services;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.clothes.models.User;

public class UserMapper implements RowMapper<User> {
	
		@Override
		public User mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			User todo = new User();

			todo.setId(rs.getInt("ID"));
			todo.setName(rs.getString("NAME"));
			todo.setEmail(rs.getString("EMAIL"));
			todo.setPassword(rs.getString("PASSWORD"));
			
			return todo;
		}
}
