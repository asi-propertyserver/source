package at.freebim.db.webapp.models.request;

/**
 * This class represents the model for a request to a controller.
 * 
 * @see at.freebim.db.webapp.controller.BaseController#delete(DeleteModel)
 * 
 * @author Patrick.Lanzinger@student.uibk.ac.at
 *
 */
public class DeleteModel {
	/**
	 * The id of a node.
	 * 
	 * @see at.freebim.db.domain.base.BaseNode#getNodeId()
	 */
	private Long nodeId;

	/**
	 * Get the id of a node.
	 * 
	 * @return the id of the node.
	 */
	public Long getNodeId() {
		return nodeId;
	}
}
