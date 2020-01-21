package at.freebim.db.webapp.models.request;

import javax.validation.constraints.NotBlank;

import at.freebim.db.domain.FreebimUser;

/**
 * This class represents the model for a request to a controller.
 * 
 * @see at.freebim.db.webapp.controller.AuthorizationController#login(LoginRequestModel)
 * 
 * @author Patrick.Lanzinger@student.uibk.ac.at
 *
 */
public class LoginRequestModel {

	/**
	 * The username of a {@link FreebimUser}.
	 */
	@NotBlank
	private String username;

	/**
	 * The password of a {@link FreebimUser}.
	 */
	@NotBlank
	private String password;

	/**
	 * Get the username.
	 * 
	 * @return the username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Set the username.
	 * 
	 * @param username the username.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Get the password.
	 * 
	 * @return the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set the password.
	 * 
	 * @param password the password.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
