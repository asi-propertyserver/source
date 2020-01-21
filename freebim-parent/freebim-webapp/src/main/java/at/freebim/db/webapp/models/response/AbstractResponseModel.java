package at.freebim.db.webapp.models.response;

/**
 * The basic response model.
 * 
 * @author Patrick.Lanzinger@student.uibk.ac.at
 *
 */
public abstract class AbstractResponseModel {

	protected String errorMessage;
	
	
	/**
	 * Create new instance.
	 */
	public AbstractResponseModel() {
	}

	/**
	 * Create new instance.
	 * 
	 * @param errorMessage the error message.
	 */
	public AbstractResponseModel(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * Get the error message.
	 * 
	 * @return the error message.
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Set the error message.
	 * 
	 * @param errorMessage the error message.
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
