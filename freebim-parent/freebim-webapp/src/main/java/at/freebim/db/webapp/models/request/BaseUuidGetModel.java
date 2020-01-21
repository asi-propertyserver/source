package at.freebim.db.webapp.models.request;

import javax.validation.constraints.NotBlank;

/**
 * This class represents the model for a request to a controller.
 * 
 * @see at.freebim.db.webapp.controller.BaseUuidController#get(BaseUuidGetModel)
 * 
 * @author Patrick.Lanzinger@student.uibk.ac.at
 *
 */
public class BaseUuidGetModel {

	/**
	 * The uuid.
	 * 
	 * @see at.freebim.db.domain.base.UuidIdentifyable
	 */
	@NotBlank
	private String u;

	/**
	 * Get the uuid;
	 * 
	 * @see at.freebim.db.domain.base.UuidIdentifyable
	 * 
	 * @return the uuid
	 */
	public String getU() {
		return u;
	}
}
