package at.freebim.db.webapp.models.response;

/**
 * This class represents the response model of a login request.
 * 
 * @see at.freebim.db.webapp.controller.AuthorizationController#login(at.freebim.db.webapp.models.request.LoginRequestModel)
 * 
 * @author Patrick.Lanzinger@student.uibk.ac.at
 *
 */
public class LoginResponseModel extends AbstractResponseModel {
	/**
	 * The jwt-token.
	 */
	private String token;

	/**
	 * The jwt-refresh-token.
	 */
	private String refreshToken;

	/**
	 * Create a new instance.
	 */
	public LoginResponseModel() {
	}
	
	/**
	 * Creates new instance.
	 * 
	 * @param erroMessage the error message.
	 */
	public LoginResponseModel(String erroMessage) {
		super(erroMessage);
	}

	/**
	 * Creates a new instance.
	 * 
	 * @param token        the jwt-token
	 * @param refreshToken the jwt-refresh-token
	 */
	public LoginResponseModel(String token, String refreshToken) {
		this.token = token;
		this.refreshToken = refreshToken;
	}

	/**
	 * Set the jwt-token.
	 * 
	 * @param token the jwt-token.
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Set the refresh-token.
	 * 
	 * @param refreshToken the refresh-token.
	 */
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
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
	 * Get the refresh-token.
	 * 
	 * @return the refresh-token.
	 */
	public String getRefreshToken() {
		return refreshToken;
	}
}
