package at.freebim.db.webapp.security.jwt;

import org.springframework.http.HttpStatus;

/**
 * This exception should be thrown when something went wrong in the parsing or
 * validating of the jwt-token.
 * 
 * @author Patrick.Lanzinger@student.uibk.ac.at
 *
 */
public class JwtValidationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * The error message.
	 */
	private final String message;

	/**
	 * The http status.
	 */
	private final HttpStatus httpStatus;

	/**
	 * Creates a new instance.
	 * 
	 * @param message    the error message.
	 * @param httpStatus the http status.
	 */
	public JwtValidationException(String message, HttpStatus httpStatus) {
		this.message = message;
		this.httpStatus = httpStatus;
	}

	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * Get the http status.
	 * 
	 * @return the http status.
	 */
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
