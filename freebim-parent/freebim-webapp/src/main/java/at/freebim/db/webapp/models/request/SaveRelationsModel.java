package at.freebim.db.webapp.models.request;

import at.freebim.db.dto.Relations;

/**
 * This class represents the model for a request to a controller.
 * 
 * @see at.freebim.db.webapp.controller.BaseController#saveRelations(SaveRelationsModel)
 * 
 * @author Patrick.Lanzinger@student.uibk.ac.at
 *
 */
public class SaveRelationsModel {

	/**
	 * The id of node from which the relations start.
	 */
	private Long nodeId;

	/**
	 * An array of relations.
	 */
	private Relations[] relArray;

	/**
	 * Get the id of the node.
	 * 
	 * @return the id of the node.
	 */
	public Long getNodeId() {
		return nodeId;
	}

	/**
	 * Get the array of relations.
	 * 
	 * @return the array of relations.
	 */
	public Relations[] getRelArray() {
		return relArray;
	}

}
