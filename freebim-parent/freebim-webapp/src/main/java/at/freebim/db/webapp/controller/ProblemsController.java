/******************************************************************************
 * Copyright (C) 2009-2019  ASI-Propertyserver
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see {@literal<http://www.gnu.org/licenses/>}.
 *****************************************************************************/
package at.freebim.db.webapp.controller;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import at.freebim.db.domain.BigBangNode;
import at.freebim.db.domain.Component;
import at.freebim.db.domain.DataType;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.Measure;
import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.ValueList;
import at.freebim.db.domain.rel.HasParameter;
import at.freebim.db.service.BaseNodeService;
import at.freebim.db.service.BigBangNodeService;
import at.freebim.db.service.ProblemService;
import at.freebim.db.service.ProblemService.IdPair;
import at.freebim.db.service.ProblemService.IdTriple;

/**
 * The controller that is used to report problems. It extends
 * {@link BaseController}.
 * 
 * @see at.freebim.db.domain.BigBangNode
 * @see at.freebim.db.webapp.controller.BaseController
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@RestController
@RequestMapping("/problems")
public class ProblemsController extends BaseController<BigBangNode> {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ProblemsController.class);

	/**
	 * The service that handles the root node/entity {@link BigBangNode}.
	 */
	@Autowired
	private BigBangNodeService bigBangNodeService;

	/**
	 * The service that can extract problems (missing data, ...) from the database.
	 */
	@Autowired
	private ProblemService problemService;

	/**
	 * Creates a new instance.
	 */
	public ProblemsController() {
		super(BigBangNode.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.webapp.controller.BaseController#getService()
	 */
	@Override
	protected BaseNodeService<BigBangNode> getService() {
		return this.bigBangNodeService;
	}

	/**
	 * Get all missing node id's of the node {@link Parameter} that do not have a
	 * relation to a {@link Measure} node
	 * 
	 * @return the {@link AjaxResponse} that includes the ids of the nodes or the
	 *         error message
	 */
	@GetMapping(value = "/missingMeasure")
	public @ResponseBody AjaxResponse missingMeasure() {
		logger.debug("missingMeasure called ...");

		AjaxResponse response = null;
		try {
			ArrayList<Long> ids = this.problemService.getMissingMeasure();
			response = new AjaxResponse(ids);
		} catch (Exception e) {
			logger.error("Error in 'missingMeasure':", e);
		}
		return response;
	}

	/**
	 * Get all node-id's of the nodes {@link Measure} that do not have a
	 * {@link ValueList} or a {@link DataType}.
	 * 
	 * @return the {@link AjaxResponse} that includes the list of id's of the nodes
	 *         or the error message
	 */
	@GetMapping(value = "/emptyMeasure")
	public @ResponseBody AjaxResponse emptyMeasure() {
		logger.debug("emptyMeasure called ...");

		AjaxResponse response = null;
		try {
			ArrayList<Long> ids = this.problemService.getEmptyMeasure();
			response = new AjaxResponse(ids);
		} catch (Exception e) {
			logger.error("Error in 'emptyMeasure':", e);
		}

		return response;
	}

	/**
	 * Get all node-id's of {@link Component}s without any {@link Parameter}s on
	 * their path to {@link BigBangNode}.
	 * 
	 * @return the {@link AjaxResponse} that includes the list of id's of the nodes
	 *         or the error message
	 */
	@GetMapping(value = "/emptyComponents")
	public @ResponseBody AjaxResponse emptyComponents() {
		logger.debug("emptyComponents called ...");

		AjaxResponse response = null;
		try {
			ArrayList<Long> ids = this.problemService.getComponentWithoutParameters();
			response = new AjaxResponse(ids);
		} catch (Exception e) {
			logger.error("Error in 'emptyComponents':", e);
		}

		return response;
	}

	/**
	 * Get all node-id's of {@link Parameter}s referenced by a {@link HasParameter}
	 * relations which is referencing a deleted phase.
	 * 
	 * @return the {@link AjaxResponse} that includes the list of id's of the nodes
	 *         or the error message
	 */
	@GetMapping(value = "/deletedPhase")
	public @ResponseBody AjaxResponse deletedPhase() {
		logger.debug("deletedPhase called ...");

		AjaxResponse response = null;
		try {
			ArrayList<Long> ids = this.problemService.deletedPhase();
			response = new AjaxResponse(ids);
		} catch (Exception e) {
			logger.error("Error in 'deletedPhase':", e);
		}

		return response;
	}

	/**
	 * Get all {@link Parameter}-ID's of parameters that could be assigned to parent
	 * node.
	 * 
	 * @param libid the id of the {@link Library}
	 * @return the {@link AjaxResponse} that includes the list of parameter-ID's
	 */
	@GetMapping(value = "/paramsMoveUp")
	public @ResponseBody AjaxResponse paramsMoveUp(Long libid) {
		logger.debug("paramsMoveUp called, libid={} ...", libid);

		AjaxResponse response = null;

		ArrayList<IdPair> pairs;
		try {
			pairs = this.problemService.specializableParameters(libid);
			response = new AjaxResponse(pairs);
		} catch (Exception e) {
			logger.error("Error in 'paramsMoveUp':", e);
		}

		return response;
	}

	/**
	 * Get all ID's of {@link Parameter}s that are assigned multiple times along a
	 * single path.
	 * 
	 * @return the {@link AjaxResponse} that includes a list of
	 *         {@link Parameter}-ID's
	 */
	@GetMapping(value = "/multipleParameterAssignment")
	public @ResponseBody AjaxResponse multipleParameterAssignment() {
		logger.debug("multipleParameterAssignment called ...");

		AjaxResponse response = null;

		ArrayList<IdTriple> res;
		try {
			res = this.problemService.multipleParameterAssignment();
			response = new AjaxResponse(res);
		} catch (Exception e) {
			logger.error("Error in 'multipleParameterAssignment':", e);
		}

		return response;
	}
}
