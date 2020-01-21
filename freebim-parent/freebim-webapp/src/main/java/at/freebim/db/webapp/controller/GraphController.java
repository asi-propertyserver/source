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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import at.freebim.db.domain.BigBangNode;
import at.freebim.db.domain.Component;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.Parameter;
import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.domain.base.HierarchicalBaseNode;
import at.freebim.db.domain.base.LifetimeBaseNode;
import at.freebim.db.domain.base.Named;
import at.freebim.db.domain.base.UuidIdentifyable;
import at.freebim.db.domain.rel.Equals;
import at.freebim.db.domain.rel.HasParameter;
import at.freebim.db.domain.rel.ParentOf;
import at.freebim.db.domain.rel.References;
import at.freebim.db.service.BaseNodeService;
import at.freebim.db.service.BigBangNodeService;
import at.freebim.db.service.DateService;
import at.freebim.db.service.GraphService;
import at.freebim.db.service.GraphService.Graph;
import at.freebim.db.service.GraphService.Node;
import at.freebim.db.service.RelationService;

/**
 * The controller of the root node {@link BigBangNode}. This controller is used
 * to create graphs. It extends {@link BaseController}.
 * 
 * @see at.freebim.db.domain.BigBangNode
 * @see at.freebim.db.webapp.controller.BaseController
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@RestController
@RequestMapping("/graph")
public class GraphController extends BaseController<BigBangNode> {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(GraphController.class);

	/**
	 * The service that can handle a {@link Graph}.
	 */
	@Autowired
	private GraphService graphService;

	/**
	 * The service that handles the root node {@link BigBangNode}.
	 */
	@Autowired
	private BigBangNodeService bigBangNodeService;

	/**
	 * The service that handles relations.
	 */
	@Autowired
	private RelationService relationService;

	/**
	 * The service that handles dates.
	 */
	@Autowired
	private DateService dateService;

	/**
	 * A map of libraries where the key is the id of the library.
	 */
	private Map<Long, Library> libraries;

	/**
	 * Create a new instance.
	 */
	public GraphController() {
		super(BigBangNode.class);
		this.libraries = new HashMap<Long, Library>();
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
	 * Add the {@link Equals}, the {@link Parameter} and the child nodes
	 * ({@link ParentOf}) to the graph. Check if the added child's are still valid
	 * with the time stamp. It is determined by the parameters if the equals or
	 * parameters are added. One parameter decides also if the child's child's are
	 * added recursively.
	 * 
	 * @param bn         the node from which is all added to the graph
	 * @param graph      the graph
	 * @param now        the time stamp
	 * @param recursive  determines if the child's child's should be loaded
	 *                   recursively
	 * @param withParams determines if the {@link Parameter}s should be loaded and
	 *                   added to the graph
	 * @param equals     if the {@link Equals} should be loaded and added to the
	 *                   graph
	 */
	private void addChilds(BaseNode bn, Graph graph, Long now, boolean recursive, boolean withParams, boolean equals) {

		if (equals) {
			addEquals(bn, graph, now);
		}
		if (withParams) {
			addParameter(bn, graph, now);
		}

		if (HierarchicalBaseNode.class.isAssignableFrom(bn.getClass())) {
			HierarchicalBaseNode parentNode = (HierarchicalBaseNode) bn;
			Iterable<ParentOf> iterable = parentNode.getChilds();
			if (iterable != null) {
				Iterator<ParentOf> iter = iterable.iterator();
				while (iter.hasNext()) {
					ParentOf r = iter.next();
					BaseNode childNode = r.getN2();
					childNode = this.relationService.fetch(childNode, childNode.getClass());
					if (LifetimeBaseNode.class.isAssignableFrom(childNode.getClass())) {
						LifetimeBaseNode l = (LifetimeBaseNode) childNode;
						if (l.getValidFrom() > now || (l.getValidTo() != null && l.getValidTo() <= now)) {
							continue;
						}
					}
					addNode(childNode, graph, now);
					graph.links.add(r);

					if (recursive) {
						addChilds(childNode, graph, now, recursive, withParams, equals);
					}
				}
			}
		}
	}

	/**
	 * Add all nodes that are {@link Equals} to the graph.
	 * 
	 * @param bn    the node from which the {@link Equals} are added
	 * @param graph the graph
	 * @param now   the time stamp to check if the {@link Equals} are still valid
	 */
	private void addEquals(BaseNode bn, Graph graph, Long now) {
		Iterable<Equals> iterable = bn.getEq();
		if (iterable != null) {
			Iterator<Equals> iter = iterable.iterator();
			while (iter.hasNext()) {
				Equals r = iter.next();
				BaseNode eqNode = r.getN2();
				if (eqNode.getNodeId().equals(bn.getNodeId())) {
					eqNode = r.getN1();
				}
				eqNode = this.relationService.fetch(eqNode, eqNode.getClass());
				if (LifetimeBaseNode.class.isAssignableFrom(eqNode.getClass())) {
					LifetimeBaseNode l = (LifetimeBaseNode) eqNode;
					if (l.getValidFrom() > now || (l.getValidTo() != null && l.getValidTo() <= now)) {
						continue;
					}
				}
				addNode(eqNode, graph, now);
				graph.links.add(r);
			}
		}
	}

	/**
	 * Add the nodes ({@link Parameter}) that are connected to a {@link Component}
	 * to a graph. There will be checked if the nodes are still valid.
	 * 
	 * @param bn    the node ({@link Component}) from which the {@link Parameter}s
	 *              are loaded
	 * @param graph the graph
	 * @param now   the time stamp to check the validity of the node
	 */
	private void addParameter(BaseNode bn, Graph graph, Long now) {
		if (Component.class.isAssignableFrom(bn.getClass())) {
			Component component = (Component) bn;
			Iterable<HasParameter> iterable = component.getParameter();
			if (iterable != null) {
				Iterator<HasParameter> iter = iterable.iterator();
				while (iter.hasNext()) {
					HasParameter r = iter.next();
					BaseNode parameter = r.getN2();
					parameter = this.relationService.fetch(parameter, parameter.getClass());
					if (LifetimeBaseNode.class.isAssignableFrom(parameter.getClass())) {
						LifetimeBaseNode l = (LifetimeBaseNode) parameter;
						if (l.getValidFrom() > now || (l.getValidTo() != null && l.getValidTo() <= now)) {
							continue;
						}
					}
					addNode(parameter, graph, now);
					graph.links.add(r);
				}
			}
		}
	}

	/**
	 * Add a node to a graph and check if the node is still valid. If the node is
	 * not valid nothing is done else the references libraries will be loaded and
	 * added to the node.
	 * 
	 * @param bn    the node that will be added to the graph
	 * @param graph the graph
	 * @param now   the time stamp to check if the node is still valid
	 */
	private void addNode(BaseNode bn, Graph graph, Long now) {
		if (LifetimeBaseNode.class.isAssignableFrom(bn.getClass())) {
			LifetimeBaseNode l = (LifetimeBaseNode) bn;
			if (l.getValidFrom() > now || (l.getValidTo() != null && l.getValidTo() <= now)) {
				return;
			}
		}
		String name = "*";
		if (BigBangNode.class.isAssignableFrom(bn.getClass())) {
			name = "*";
		} else {
			if (Library.class.isAssignableFrom(bn.getClass())) {
				name = ((Library) bn).getName();
			} else if (Named.class.isAssignableFrom(bn.getClass())) {
				name = ((Named) bn).getName();
			}
		}
		Node n = new Node(bn.getNodeId(), name, bn.getClass().getSimpleName());

		// get referenced libraries for that node
		if (UuidIdentifyable.class.isAssignableFrom(bn.getClass())) {
			UuidIdentifyable u = (UuidIdentifyable) bn;
			Iterable<References> refIter = u.getRef();
			if (refIter != null) {
				Iterator<References> iter = refIter.iterator();
				while (iter.hasNext()) {
					References r = iter.next();
					r.getN2();
					Library lib = this.libraries.get(r.getN2().getNodeId());
					if (lib == null) {
						lib = (Library) this.relationService.fetch(r.getN2(), r.getN2().getClass());
						this.libraries.put(lib.getNodeId(), lib);
					}
					if (n.libs.contains(lib.getNodeId())) {
						continue;
					}
					n.libs.add(lib.getNodeId());
				}
			}
		}

		graph.nodes.put(bn.getNodeId(), n);
	}

	/**
	 * Get the JSP-page that has the name graph.
	 * 
	 * @param model the model
	 * @return the name of the JSP-page
	 */
	@GetMapping(value = "")
	public String getRoot(Model model) {
		logger.debug("Received request to graph with root");

		super.setUserInfo(model);

		return "graph";
	}

	/**
	 * Create a {@link Graph} starting from the node that has the provided id. If no
	 * node is found for the id the {@link BigBangNode} is used. Add the
	 * {@link Equals}, the {@link Parameter} and the child nodes ({@link ParentOf})
	 * to the graph. Check if the added child's are still valid with the time stamp.
	 * It is determined by the parameters if the equals or parameters are added. One
	 * parameter decides also if the child's child's are added recursively.
	 * 
	 * @param parentId   the id of the root node from which the graph will be
	 *                   created
	 * @param recursive  determines if the child's child's should be loaded
	 *                   recursively
	 * @param withParams determines if the {@link Parameter}s should be loaded and
	 *                   added to the graph
	 * @param equals     if the {@link Equals} should be loaded and added to the
	 *                   graph
	 * @return the {@link AjaxResponse} that includes the loaded {@link Graph}
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/childs_of")
	public @ResponseBody AjaxResponse getRoot(Long parentId, String clazz, boolean recursive, boolean withParams,
			boolean equals) {
		logger.debug("Received request to graph with parentId=[{}], recursive: [{}]", parentId, recursive);

		Long now = this.dateService.getMillis();

		AjaxResponse response = null;
		Graph graph = new Graph();

		Class<? extends BaseNode> parsedClass = null;
		try {
			parsedClass = (Class<? extends BaseNode>) Class.forName("at.freebim.db.domain." + clazz);
		} catch (ClassNotFoundException e) {
			return new AjaxResponse(null);
		}

		BaseNode root = this.relationService.getNodeById(parentId, parsedClass);
		// root = this.relationService.getNodeById(parentId, root.getClass());

		if (root == null) {
			root = this.bigBangNodeService.getBigBangNode();
		}
		addChilds(root, graph, now, recursive, withParams, equals);

		response = new AjaxResponse(graph);
		return response;
	}

	/**
	 * Create a {@link Graph} starting from the node that has the provided id. Add
	 * the {@link Equals}, the {@link Parameter} and the child nodes
	 * ({@link ParentOf}) to the graph. It is determined by the parameters if the
	 * equals or parameters are added. One parameter decides also if the child's
	 * child's are added recursively.
	 * 
	 * @param parentId   the id of the root node from which the graph will be
	 *                   created
	 * @param recursive  determines if the child's child's should be loaded
	 *                   recursively
	 * @param withParams determines if the {@link Parameter}s should be loaded and
	 *                   added to the graph
	 * @param equals     if the {@link Equals} should be loaded and added to the
	 *                   graph
	 * @return the {@link AjaxResponse} that includes the loaded {@link Graph}
	 */
	@GetMapping(value = "/childs_of_node")
	public @ResponseBody AjaxResponse getChildsOfNode(Long parentId, boolean recursive, boolean withParams,
			boolean equals) {
		logger.debug("Received request: childs_of_node parentId=[{}]", parentId);

		AjaxResponse response = null;
		Graph graph = this.graphService.getGraphFor(parentId, recursive, withParams, equals);

		response = new AjaxResponse(graph);

		return response;
	}

}
