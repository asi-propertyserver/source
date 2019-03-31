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
package at.freebim.db.webapp.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.FreebimUser;
import at.freebim.db.repository.FreebimUserRepository;


/**
 * A custom authentication manager that allows access if the user details
 * exist in the database and if the username and password are not the same.
 * Otherwise, throw a {@link BadCredentialsException}.
 * The class extends {@link AuthenticationManager}.
 * 
 * @see org.springframework.security.authentication.AuthenticationManager
 * @see org.springframework.security.authentication.BadCredentialsException
 * 
 * @author rainer.breuss@uibk.ac.at
 * 
 */
@Service ("authenticationManager")
public class CustomAuthenticationManager implements AuthenticationManager {

	/**
	 * The logger.
	 */
	protected static Logger logger = LoggerFactory.getLogger(CustomAuthenticationManager.class);

	// We need an Md5 encoder since our passwords in the database are Md5 encoded. 
	/**
	 * The md5 password encoder.
	 */
	//TODO: deprecated: should be updated
	@Autowired
	private Md5PasswordEncoder passwordEncoder;
	
	/**
	 * The salt for the hash of the password.
	 */
	@Value("${props.salt}")
	private final String salt = "";
	
	/**
	 * The repository for the {@link FreebimUser}.
	 */
	@Autowired
	private FreebimUserRepository userRepository;

	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.AuthenticationManager#authenticate(org.springframework.security.core.Authentication)
	 */
	@Transactional(readOnly=true)
	public Authentication authenticate(Authentication auth)
			throws AuthenticationException {

		logger.debug("Performing custom authentication: User name={}", auth.getName());
		
		// Init a database user object
		FreebimUser user = null;
		
		try {
			// Retrieve user details from database
			user = userRepository.findByUsername(auth.getName());
			
			logger.debug("User [{}] found.", user.getUsername());
			
		} catch (Exception e) {
			logger.error("User [" + auth.getName() + "] does not exists!");
			throw new BadCredentialsException("User does not exists!");
		}
		
		logger.debug("checking password for user '{}' ...", user.getUsername());
		
		// Compare passwords
		// Make sure to encode the password first before comparing
		if (passwordEncoder.isPasswordValid(user.getPassword(), (String) auth.getCredentials(), this.salt) == false ) {
			logger.error("Wrong password for user [" + auth.getName() + "] !");
			throw new BadCredentialsException("Wrong password!");
		}
		
		logger.debug("password for User [{}] checked successfully.", user.getUsername());
		
		UsernamePasswordAuthenticationToken authenticationToken;
		authenticationToken = new UsernamePasswordAuthenticationToken(
				auth.getName(), 
				auth.getCredentials(), 
				user.getRoles());

		SecurityContextHolder.getContext().setAuthentication(authenticationToken);

		logger.info("User [{}] successfully authenticated.", auth.getName());
		
		return authenticationToken;
	}
	

}
