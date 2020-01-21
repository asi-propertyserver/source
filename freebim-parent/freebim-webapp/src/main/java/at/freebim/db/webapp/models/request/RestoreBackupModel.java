package at.freebim.db.webapp.models.request;

import javax.validation.constraints.NotBlank;

/**
 * This class represents the model for a request to a controller.
 * 
 * @see at.freebim.db.webapp.controller.AdminController#restoreBackup(RestoreBackupModel)
 * 
 * @author Patrick.Lanzinger@student.uibk.ac.at
 *
 */
public class RestoreBackupModel {

	/**
	 * The name of the backup.
	 */
	@NotBlank
	private String backup;

	/**
	 * Get the name of the backup.
	 * 
	 * @return the name of the backup.
	 */
	public String getBackup() {
		return backup;
	}
}
