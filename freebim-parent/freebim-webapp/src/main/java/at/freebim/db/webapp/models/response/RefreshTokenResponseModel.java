package at.freebim.db.webapp.models.response;

/**
 * This class represents the response of the request to get a new token.
 * 
 * @see at.freebim.db.webapp.controller.AuthorizationController#refreshToken(at.freebim.db.webapp.models.request.RefreshTokenModel)
 * 
 * @author Patrick.Lanzinger@student.uibk.ac.at
 *
 */
public class RefreshTokenResponseModel extends AbstractResponseModel {

	/**
	 * The jwt-token.
	 */
	private String token;

	/**
	 * Creates a new instance.
	 * 
	 * @param token the jwt-token
	 * @param errorMessage the error message
	 */
	public RefreshTokenResponseModel(String token, String errorMessage) {
		super(errorMessage);
		this.token = token;	
	}

	/**
	 * Get the jwt-token.
	 * 
	 * @return the jwt-token.
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Set the jwt-token.
	 * 
	 * @param token the jwt-token.
	 */
	public void setToken(String token) {
		this.token = token;
	}
}
