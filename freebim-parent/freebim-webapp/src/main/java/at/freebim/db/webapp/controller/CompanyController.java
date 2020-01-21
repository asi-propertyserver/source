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
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import at.freebim.db.domain.Company;
import at.freebim.db.domain.Contributor;
import at.freebim.db.domain.base.LifetimeBaseNode;
import at.freebim.db.domain.rel.CompanyCompany;
import at.freebim.db.domain.rel.WorksFor;
import at.freebim.db.service.BaseNodeService;
import at.freebim.db.service.CompanyService;
import at.freebim.db.service.DateService;
import at.freebim.db.service.FileService;
import at.freebim.db.service.GraphService.Graph;
import io.swagger.annotations.ApiOperation;

/**
 * The controller that handles the node/entity {@link Company}. It extends
 * {@link BaseController}.
 * 
 * @see at.freebim.db.domain.Company
 * @see at.freebim.db.webapp.controller.BaseController
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@RestController
@RequestMapping("/company")
public class CompanyController extends BaseController<Company> {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);

	/**
	 * This class extends {@link at.freebim.db.service.GraphService.Node} simply by
	 * the field href.
	 * 
	 * @see at.freebim.db.service.GraphService.Node
	 * 
	 * @author rainer.breuss@uibk.ac.at
	 *
	 */
	public class Node extends at.freebim.db.service.GraphService.Node {
		private static final long serialVersionUID = 1L;

		/**
		 * A hypertext reference.
		 */
		public String href;

		/**
		 * Create new instance.
		 * 
		 * @param id   the id of the node
		 * @param name the name of the node
		 * @param type the type of the node
		 */
		public Node(Long id, String name, String type) {
			super(id, name, type);
		}
	}

	/**
	 * The service that handles operations on the node/entity {@link Company}.
	 */
	@Autowired
	private CompanyService companyService;

	/**
	 * The service that handles file uploads.
	 */
	@Autowired
	private FileService fileUploadService;

	/**
	 * The service that handles dates.
	 */
	@Autowired
	private DateService dateService;

	/**
	 * Creates a new instance.
	 */
	public CompanyController() {
		super(Company.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.webapp.controller.BaseController#getService()
	 */
	@Override
	protected BaseNodeService<Company> getService() {
		return this.companyService;
	}

	/**
	 * Get an image that has the provided file-name.
	 * 
	 * @param file  the name of the file. This is provided as path variable.
	 * @param model the model
	 * @return a {@link FileSystemResource} to get the file
	 */
	@ApiOperation(value = "Get an image", notes = "Load an image from the server that has the provided filename")
	@GetMapping(value = "/logo/{file:.+}", headers = "Accept=image/jpeg, image/jpg, image/png, image/gif, image/png")
	public @ResponseBody FileSystemResource getImage(@PathVariable String file, Model model) {
		logger.debug("getImage  [{}]", file);

		super.setUserInfo(model);

		try {
			return this.fileUploadService.getFile(file);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Upload a file to the server. The file is provided as {@link MultipartFile}
	 * from the request.
	 * 
	 * @param file  the file that will be uploaded
	 * @param model the model
	 * @return the {@link AjaxResponse} that includes the filename of the uploaded
	 *         file
	 */
	@ApiOperation(value = "Upload a file", notes = "Uplaod a file to the server")
	@PostMapping(value = "/upload")
	public @ResponseBody AjaxResponse handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {

		AjaxResponse response = null;

		try {
			String filename = this.fileUploadService.uploadFile(file);

			response = new AjaxResponse(filename);

			savedNodesNotifications(response);

		} catch (AccessDeniedException e) {
			response = new AjaxResponse(null);
			response.setAccessDenied(true);
		} catch (Exception e) {
			logger.info(e.toString());
			response = new AjaxResponse(null);
			response.setError(e.toString());
		}

		return response;
	}

	/**
	 * Load a {@link Graph} of the company to company relations with the
	 * contributors that are part of that company.
	 * 
	 * @param parentId
	 * @param recursive
	 * @param withParams
	 * @param withEquals
	 * @param model      the model
	 * @return the {@link AjaxResponse} that includes the created {@link Graph}
	 */
	@ApiOperation(value = "Load a graph", notes = "Load a {@link Graph} of the company to company relations with the contributors that are part of that company")
	@GetMapping(value = "/graph")
	public @ResponseBody AjaxResponse getChildsOfNode(Long parentId, boolean recursive, boolean withParams,
			boolean withEquals) {
		logger.debug("Received request: childs_of_node parentId=[{}]", parentId);

		AjaxResponse response = null;
		Long now = this.dateService.getMillis();

		Graph graph = new Graph();

		ArrayList<Company> companies = this.companyService.getAll(true);
		for (Company comp : companies) {
			Node node = new Node(comp.getNodeId(), comp.getName(), "Company");
			node.href = comp.getLogo();
			graph.nodes.put(comp.getNodeId(), node);

			// add Contributors to Company
			Iterable<WorksFor> iterable = comp.getContributor();

			if (iterable != null) {
				Iterator<WorksFor> iter = iterable.iterator();
				while (iter.hasNext()) {
					WorksFor rel = iter.next();
					Contributor bn = rel.getN1();
					if (LifetimeBaseNode.class.isAssignableFrom(bn.getClass())) {
						LifetimeBaseNode l = (LifetimeBaseNode) bn;
						if (l.getValidFrom() > now || (l.getValidTo() != null && l.getValidTo() <= now)) {
							continue;
						}
					}
					at.freebim.db.service.GraphService.Node cnode = new at.freebim.db.service.GraphService.Node(
							bn.getNodeId(), bn.getName(), "Contributor");
					graph.nodes.put(bn.getNodeId(), cnode);
					graph.links.add(rel);
				}
			}

			// build Company structure
			Iterable<CompanyCompany> iterable2 = comp.getCompany();
			if (iterable2 != null) {
				Iterator<CompanyCompany> iter2 = iterable2.iterator();
				while (iter2.hasNext()) {
					CompanyCompany rel = iter2.next();
					Company bn = rel.getN1();
					if (bn.getNodeId().equals(comp.getNodeId())) {
						bn = rel.getN2();
					}

					if (LifetimeBaseNode.class.isAssignableFrom(bn.getClass())) {
						LifetimeBaseNode l = (LifetimeBaseNode) bn;
						if (l.getValidFrom() > now || (l.getValidTo() != null && l.getValidTo() <= now)) {
							continue;
						}
					}
					graph.links.add(rel);
				}
			}
		}

		response = new AjaxResponse(graph);

		return response;
	}

}
