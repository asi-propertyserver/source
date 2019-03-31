/******************************************************************************
 * Copyright (C) 2009-2019  ASI-Propertyserver
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see {@literal<http://www.gnu.org/licenses/>}.
 *****************************************************************************/
package at.freebim.db.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.FreebimUser;
import at.freebim.db.repository.FreebimUserRepository;

/**
 * A custom service for retrieving users from our database.
 * <p>
 * This custom service must implement Spring's {@link UserDetailsService}
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	/**
	 * The logger.
	 */
	protected static Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

	/**
	 * The repository from the {@link FreebimUser}.
	 */
	@Autowired
	private FreebimUserRepository userRepository;

	
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		
		// Declare a null Spring User
		UserDetails user = null;
		
		try {
			logger.debug("loadUserByUsername: {}, {}", username, userRepository.toString());
			// Search database for a user that matches the specified username
			// You can provide a custom DAO to access your persistence layer
			// Or use JDBC to access your database
			// FreebimUser is our custom domain user. This is not the same as Spring's User
			FreebimUser freebimUser = userRepository.findByUsername(username);
			
			// Populate the Spring User object with details from the FreebimUser
			user =  new User(
					freebimUser.getUsername(), 
					freebimUser.getPassword(),
					true, // enabled
					true, // accountNonExpired
					true, // credentialsNonExpired
					true, // accountNonLocked
					freebimUser.getRoles() );

			logger.debug("got user: {}, pwd={}", user.getUsername(), user.getPassword());
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in retrieving user", e);
		}
		
		// Return user to Spring for processing.
		// Take note we're not the one evaluating whether this user is authenticated or valid
		// We just merely retrieve a user that matches the specified username
		return user;
	}
	
}
