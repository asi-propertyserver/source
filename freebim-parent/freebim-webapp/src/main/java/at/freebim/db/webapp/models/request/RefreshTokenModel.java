package at.freebim.db.webapp.models.request;

import javax.validation.constraints.NotBlank;

/**
 * This class represents the model for a request to a controller.
 * 
 * @see at.freebim.db.webapp.controller.AuthorizationController#refreshToken(RefreshTokenModel)
 * 
 * @author Patrick.Lanzinger@student.uibk.ac.at
 *
 */
public class RefreshTokenModel {

	/**
	 * The refresh-token.
	 */
	@NotBlank
	private String refreshToken;

	/**
	 * Get the refresh-token.
	 * 
	 * @return the refresh-token.
	 */
	public String getRefreshToken() {
		return refreshToken;
	}

}
