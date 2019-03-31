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
package at.freebim.db.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.neo4j.graphdb.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.domain.BigBangNode;
import at.freebim.db.domain.Component;
import at.freebim.db.domain.Contributor;
import at.freebim.db.domain.FreebimUser;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.domain.base.HierarchicalBaseNode;
import at.freebim.db.domain.base.RoleContributor;
import at.freebim.db.domain.base.UuidIdentifyable;
import at.freebim.db.domain.base.rel.RelationType;
import at.freebim.db.domain.base.rel.RelationTypeEnum;
import at.freebim.db.domain.rel.ParentOf;
import at.freebim.db.domain.rel.References;
import at.freebim.db.repository.LibraryRepository;
import at.freebim.db.service.BigBangNodeService;
import at.freebim.db.service.ComponentService;
import at.freebim.db.service.ContributorService;
import at.freebim.db.service.DateService;
import at.freebim.db.service.FreebimUserService;
import at.freebim.db.service.LibraryService;
import at.freebim.db.service.LibraryUpdateEvent;
import at.freebim.db.service.RelationService;

/**
 * The service for the node/class {@link Library}.
 * This service extends {@link HierarchicalBaseNodeServiceImpl} and
 * implements {@link LibraryService}, {@link ApplicationEventPublisherAware} and {@link ApplicationListener}.
 * 
 * @see at.freebim.db.domain.Library
 * @see at.freebim.db.service.impl.HierarchicalBaseNodeServiceImpl
 * @see at.freebim.db.service.LibraryService
 * @see org.springframework.context.ApplicationEventPublisherAware
 * @see org.springframework.context.ApplicationListener
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@Service
public class LibraryServiceImpl extends HierarchicalBaseNodeServiceImpl<Library> implements LibraryService, 
	ApplicationEventPublisherAware, ApplicationListener<LibraryUpdateEvent> {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(LibraryServiceImpl.class);
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.LifetimeBaseNodeServiceImpl#setRepository(org.springframework.data.neo4j.repository.GraphRepository)
	 */
	@Override
	@Autowired
	public void setRepository(GraphRepository<Library> r) {
		this.repository = r;
	}

	/**
	 * The service that handles {@link BigBangNode}
	 */
	@Autowired
	private BigBangNodeService bigBangNodeService;
	
	/**
	 * The service that handles relations.
	 */
	@Autowired
	private RelationService relationService;
	
	/**
	 * The service that handles {@link Component}.
	 */
	@Autowired
	private ComponentService componentService;
	
	/**
	 * The service that handles {@link Contributor}.
	 */
	@Autowired
	private ContributorService contributorService;
	
	/**
	 * The service that handles dates.
	 */
	@Autowired
	private DateService dateService;
	
	/**
	 * The service that handles {@link FreebimUser}.
	 */
	@Autowired
	private FreebimUserService freebimUserService;

	/**
	 * The user name of the admin.
	 */
	@Value("${admin.username}")
	private String username;
	
	
	/**
	 * The password of the admin.
	 */
	@Value("${admin.password}")
	private String password;
	
	/**
	 * The ifcLibraryId.
	 */
	private Long ifcLibraryId;

	private ApplicationEventPublisher applicationEventPublisher;
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationEventPublisherAware#setApplicationEventPublisher(org.springframework.context.ApplicationEventPublisher)
	 */
	@Override
	public void setApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.LibraryService#getIfcLibraryId()
	 */
	@Override
	public Long getIfcLibraryId() {
		return this.ifcLibraryId;
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.LibraryService#get(java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true)
	public Library get(String name) {
		logger.debug("get [{}]", name);
		Library node = ((LibraryRepository) this.repository).get(name);
		node = this.filterResponse(node, null);
		return node;
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.LibraryService#getForContributor(at.freebim.db.domain.Contributor)
	 */
	@Override
	@Transactional(readOnly=true)
	public ArrayList<Library> getForContributor(Contributor contributor) {
		logger.debug("getForContributor [{}]", contributor != null ? contributor.getNodeId() : null);
		ArrayList<Library> libs = null;
		if (contributor != null) {
			libs = ((LibraryRepository) this.repository).getForContributorId(contributor.getNodeId());
		}
		if (libs != null) {
			for (Library lib : libs) {
				lib = this.filterResponse(lib, null);
			}
		}
		return libs;
	}
	
	/**
	 * Initialize.
	 */
	@Transactional
	@PostConstruct
	public void init() {

		Library ifcLibrary;
		
		ifcLibrary = this.get(LIBRARY_NAME_IFC4);
		if (ifcLibrary == null) {
			FreebimUser admin = this.freebimUserService.get(this.username);

			Authentication auth = new UsernamePasswordAuthenticationToken (admin.getUsername(), this.password, admin.getRoles());
			SecurityContextHolder.getContext().setAuthentication(auth);

			try {
				ifcLibrary = new Library();
				ifcLibrary.setDesc("Ifc4 library");
				ifcLibrary.setName(LIBRARY_NAME_IFC4);
				ifcLibrary.setUrl("http://www.buildingsmart.org");
				ifcLibrary.setTs(this.dateService.getMillis());
				ifcLibrary = this.save(ifcLibrary);
				this.ifcLibraryId = ifcLibrary.getNodeId();
				logger.info("library 'Ifc4' created, nodeId={}", this.ifcLibraryId);
				
				this.createIfcStructure(ifcLibrary);

			} catch (Exception e) {
				logger.error("Error in createIfcStructure: ", e);
			}
			
			SecurityContextHolder.getContext().setAuthentication(null);

		} else {
			this.ifcLibraryId = ifcLibrary.getNodeId();
			logger.info("library 'Ifc4' exists, nodeId={}", this.ifcLibraryId);

		}
	}
	
	/**
	 * Create the ifc structure for the provided {@link Library}.
	 * 
	 * @param library the {@link Library} for which the ifc structure will be created.
	 */
	@SuppressWarnings("unused")
	private void createIfcStructure(Library library) {
		
		HierarchicalBaseNode ifcRoot = createNodeAsChildOf(library, "IfcRoot", library);
		HierarchicalBaseNode ifcObjectDef = createNodeAsChildOf(ifcRoot, "IfcObjectDefinition", library);
		HierarchicalBaseNode ifcObject = createNodeAsChildOf(ifcObjectDef, "IfcObject", library);
		HierarchicalBaseNode ifcProduct = createNodeAsChildOf(ifcObject, "IfcProduct", library);
		HierarchicalBaseNode ifcElement = createNodeAsChildOf(ifcProduct, "IfcElement", library);
		HierarchicalBaseNode ifcBuildingElement = createNodeAsChildOf(ifcElement, "IfcBuildingElement", library);
		
		HierarchicalBaseNode ifcWall = createNodeAsChildOf(ifcBuildingElement, "IfcWall", library);
		HierarchicalBaseNode ifcWallStd = createNodeAsChildOf(ifcWall, "IfcWallStandardCase", library);
		HierarchicalBaseNode ifcWallEle = createNodeAsChildOf(ifcWall, "IfcWallElementedCase", library);

		HierarchicalBaseNode ifcWindow = createNodeAsChildOf(ifcBuildingElement, "IfcWindow", library);
		HierarchicalBaseNode ifcWindowStd = createNodeAsChildOf(ifcWindow, "IfcWindowStandardCase", library);

		HierarchicalBaseNode ifcStairFight = createNodeAsChildOf(ifcBuildingElement, "IfcStairFlight", library);

		HierarchicalBaseNode ifcStair = createNodeAsChildOf(ifcBuildingElement, "IfcStair", library);

		HierarchicalBaseNode ifcSlab = createNodeAsChildOf(ifcBuildingElement, "IfcSlab", library);
		HierarchicalBaseNode ifcSlabStd = createNodeAsChildOf(ifcSlab, "IfcSlabStandardCase", library);
		HierarchicalBaseNode ifcSlabEle = createNodeAsChildOf(ifcSlab, "IfcSlabElementedCase", library);

		HierarchicalBaseNode ifcShadingDevice = createNodeAsChildOf(ifcBuildingElement, "IfcShadingDevice", library);

		HierarchicalBaseNode ifcRoof = createNodeAsChildOf(ifcBuildingElement, "IfcRoof", library);
		
		HierarchicalBaseNode ifcRampFlight = createNodeAsChildOf(ifcBuildingElement, "IfcRampFlight", library);

		HierarchicalBaseNode ifcRamp = createNodeAsChildOf(ifcBuildingElement, "IfcRamp", library);

		HierarchicalBaseNode ifcRailing = createNodeAsChildOf(ifcBuildingElement, "IfcRailing", library);

		HierarchicalBaseNode ifcPlate = createNodeAsChildOf(ifcBuildingElement, "IfcPlate", library);
		HierarchicalBaseNode ifcPlateStd = createNodeAsChildOf(ifcPlate, "IfcPlateStandardCase", library);

		HierarchicalBaseNode ifcPile = createNodeAsChildOf(ifcBuildingElement, "IfcPile", library);

		HierarchicalBaseNode ifcMember = createNodeAsChildOf(ifcBuildingElement, "IfcMember", library);
		HierarchicalBaseNode ifcMemberStd = createNodeAsChildOf(ifcMember, "IfcMemberStandardCase", library);

		HierarchicalBaseNode ifcFooting = createNodeAsChildOf(ifcBuildingElement, "IfcFooting", library);

		HierarchicalBaseNode ifcDoor = createNodeAsChildOf(ifcBuildingElement, "IfcDoor", library);
		HierarchicalBaseNode ifcDoorStd = createNodeAsChildOf(ifcDoor, "IfcDoorStandardCase", library);

		HierarchicalBaseNode ifcCurtainWall = createNodeAsChildOf(ifcBuildingElement, "IfcCurtainWall", library);

		HierarchicalBaseNode ifcCovering = createNodeAsChildOf(ifcBuildingElement, "IfcCovering", library);

		HierarchicalBaseNode ifcCol = createNodeAsChildOf(ifcBuildingElement, "IfcColumn", library);
		HierarchicalBaseNode ifcColStd = createNodeAsChildOf(ifcCol, "IfcColumnStandardCase", library);

		HierarchicalBaseNode ifcChimney = createNodeAsChildOf(ifcBuildingElement, "IfcChimney", library);

		HierarchicalBaseNode ifcBeProxy = createNodeAsChildOf(ifcBuildingElement, "IfcBuildingElementProxy", library);

		HierarchicalBaseNode ifcBeam = createNodeAsChildOf(ifcBuildingElement, "IfcBeam", library);
		HierarchicalBaseNode ifcBeamStd = createNodeAsChildOf(ifcBeam, "IfcBeamStandardCase", library);

		
		HierarchicalBaseNode ifcMaterialDef = createNodeAsChildOf(library, "IfcMaterialDefinition", library);
		HierarchicalBaseNode ifcMaterial = createNodeAsChildOf(ifcMaterialDef, "IfcMaterial", library);
		HierarchicalBaseNode ifcMaterialConst = createNodeAsChildOf(ifcMaterialDef, "IfcMaterialConstituent", library);
		HierarchicalBaseNode ifcMaterialConstSet = createNodeAsChildOf(ifcMaterialDef, "IfcMaterialConstituentSet", library);
		HierarchicalBaseNode ifcMaterialLayer = createNodeAsChildOf(ifcMaterialDef, "IfcMaterialLayer", library);
		HierarchicalBaseNode ifcMaterialLayerWO = createNodeAsChildOf(ifcMaterialLayer, "IfcMaterialLayerWithOffsets", library);
		HierarchicalBaseNode ifcMaterialLayerSet = createNodeAsChildOf(ifcMaterialDef, "IfcMaterialLayerSet", library);
		HierarchicalBaseNode ifcMaterialProfile = createNodeAsChildOf(ifcMaterialDef, "IfcMaterialProfile", library);
		HierarchicalBaseNode ifcMaterialProfileWO = createNodeAsChildOf(ifcMaterialProfile, "IfcMaterialProfileWithOffsets", library);
		HierarchicalBaseNode ifcMaterialProfileSet = createNodeAsChildOf(ifcMaterialDef, "IfcMaterialProfileSet", library);

	}

	/**
	 * The provided {@link HierarchicalBaseNode} is the {@link ParentOf} the {@link Component}
	 * that will be created from the provided name. That {@link Component} {@link References} the 
	 * provided {@link Library}.
	 * 
	 * @param parent the parent
	 * @param name the child
	 * @param library the referenced {@link Library}
	 * @return the created child/{Component}/{@link HierarchicalBaseNode}
	 */
	private HierarchicalBaseNode createNodeAsChildOf(HierarchicalBaseNode parent, String name, Library library) {
		Component child = new Component();

		child.setName(name);
		HierarchicalBaseNode bn = this.componentService.save(child);
		logger.info(name + " created, nodeId={}", bn.getNodeId());
		ParentOf rel = new ParentOf();
		rel.setN2(bn);
		rel.setN1(parent);
		this.relationService.save(rel);
		
		References ref = new References();
		ref.setN1(bn);
		ref.setN2(library);
		ref.setTs(this.dateService.getMillis());
		this.relationService.save(ref);
		
		return bn;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.LibraryService#getReferencedNodes(java.lang.Long, java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true)
	public List<LibraryReference> getReferencedNodes(Long libraryNodeId, String refIdName) {
		List<LibraryReference> res = new ArrayList<LibraryReference>();
		
		
		String query = "START lib=node({libraryNodeId}) MATCH (n)-[ref:" +
						RelationType.REFERENCES + 
						"]->(lib) WHERE ref.refIdName={refIdName} RETURN n, ref.refId AS refId";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put( "libraryNodeId", libraryNodeId );
		params.put( "refIdName", refIdName );

		logger.debug("query= [{}]", query);
		Result<Map<String, Object>> result = this.relationService.getTemplate().query(query, params);
		Iterator<Map<String, Object>> iter = result.iterator();
		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();
			Node n = (Node) map.get("n");
			if (n != null) {
				BaseNode bn = this.relationService.createTreeNode(n);
				if (bn != null && UuidIdentifyable.class.isAssignableFrom(bn.getClass())) {
					String refId = (String) map.get("refId");
					LibraryReference ref = new LibraryReference();
					ref.node = (UuidIdentifyable) bn;
					ref.refId = refId;
					res.add(ref);
				}
			}
		}
		return res;
	}
	
	/* (non-Javadoc)
	 * @see at.freebim.db.service.LibraryService#getReferencedNodesForRefId(java.lang.Long, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true)
	public List<UuidIdentifyable> getReferencedNodesForRefId(Long libraryNodeId, String refIdName, String refId) {
		List<UuidIdentifyable> res = new ArrayList<UuidIdentifyable>();
		
		StringBuilder b = new StringBuilder();
		b.append("START lib=node({libraryNodeId}) MATCH (n)-[ref:");
		b.append(RelationType.REFERENCES);
		b.append("]->(lib) WHERE ref.refIdName={refIdName} AND ref.refId={refId} RETURN n");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put( "libraryNodeId", libraryNodeId );
		params.put( "refIdName", refIdName );
		params.put( "refId", refId );

		String query = b.toString();
		
		logger.debug("query= [{}]", query);
		Result<Map<String, Object>> result = this.relationService.getTemplate().query(query, params);
		Iterator<Map<String, Object>> iter = result.iterator();
		while (iter.hasNext()) {
			Map<String, Object> map = iter.next();
			Node n = (Node) map.get("n");
			if (n != null) {
				BaseNode bn = this.relationService.createTreeNode(n);
				if (bn != null && UuidIdentifyable.class.isAssignableFrom(bn.getClass())) {
					res.add((UuidIdentifyable) bn);
				}
			}
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.ContributedBaseNodeServiceImpl#filterAfterInsert(at.freebim.db.domain.base.ContributedBaseNode)
	 */
	@Override
	public Library filterAfterInsert(Library node) {

		if (node != null) {
			BigBangNode bigBangNode = this.bigBangNodeService.getBigBangNode();
	
			ParentOf rel = new ParentOf();
			rel.setN2(node);
			rel.setN1(bigBangNode);
			this.relationService.save(rel);
			logger.info("Library nodeId={} linked to BigBangNode. ", node.getNodeId());
		}
		return super.filterAfterInsert(node);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.LibraryService#getRefIdNames(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=true)
	public List<String> getRefIdNames(Long libraryNodeId) {
		return ((LibraryRepository) this.repository).getRefIdNames(libraryNodeId);
	}
	
	/**
	 * Get the smallest and the greatest id of the nodes that 
	 * {@link References} the provided {@link Library}. 
	 * 
	 * @param libraryNodeId
	 * @return first element of the array is the min value the second is the max value
	 */
	@Transactional(readOnly=true)
	private long[] getMinMaxId(long libraryNodeId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put( "libraryNodeId", libraryNodeId );
		StringBuilder b = new StringBuilder();
		b.append("START lib=node({libraryNodeId}) MATCH (lib)<-[:");
		b.append(RelationType.REFERENCES);
		b.append("]-(x) RETURN min(ID(x)) AS min, max(ID(x)) AS max");

		String query = b.toString();
		logger.info("query= [{}]", query);
		
		long[] res = new long[] {0L, 0L};
		
		Result<Map<String, Object>> result = this.relationService.getTemplate().query(query, params);
		if (result != null) {
			Iterator<Map<String, Object>> iter = result.iterator();
			while (iter.hasNext()) {
				Map<String, Object> map = iter.next();
				Object o = map.get("min");
				if (o != null)
					res[0] = (long) o;
				o = map.get("max");
				if (o != null)
					res[1] = (long) o;
				logger.info("min: {}\t max: {}", res[0], res[1]);
			}
		}
		return res;
	}
	
	/**
	 * Delete all relations ref to nodes that {@link References} the {@link Library}.
	 * (lib)<-[r:References]-(x)-(ref)-()    
	 * 
	 * @param min the lowest possible id that can be deleted
	 * @param delta the delta over the ids
	 * @param high the highest possible id that will be deleted
	 * @param params params the {@link Library} id
	 * @return the number of nodes and relations deleted
	 */
	@Transactional
	private long clearRelations(long min, long delta, long high, Map<String, Object> params) {
		StringBuilder b = new StringBuilder();
		long count = 0L, max = min + delta;
		max = ((max > high) ? high : max);
		b.setLength(0);
		b.append("START lib=node({libraryNodeId}) MATCH (lib)<-[:");
		b.append(RelationType.REFERENCES);
		b.append("]-(x) WHERE ID(x) >= ");
		b.append(min);
		b.append(" AND ID(x) < ");
		b.append(max);
		b.append(" WITH x AS x MATCH (x)-[s]-() WHERE type(s) <> '");
		b.append(RelationType.REFERENCES);
		b.append("' DELETE s RETURN count(s) AS count");

		String query = b.toString();
		logger.info("query= [{}]", query);
		
		Result<Map<String, Object>> result = this.relationService.getTemplate().query(query, params);
		if (result != null) {
			Iterator<Map<String, Object>> iter = result.iterator();
			while (iter.hasNext()) {
				Map<String, Object> map = iter.next();
				count= (long) map.get("count");
				logger.info("deleted relations count: {}", count);
			}
		}
		return count;
	}
	
	/**
	 * Delete all nodes and relations that {@link References} a {@link Library}. 
	 * 
	 * @param min the lowest possible id that can be deleted 
	 * @param delta the delta over the ids
	 * @param high the highest possible id that will be deleted
	 * @param params the {@link Library} id
	 * @return the number of nodes and relations deleted
	 */
	@Transactional
	private long clearNodes(long min, long delta, long high, Map<String, Object> params) {
		StringBuilder b = new StringBuilder();
		long count = 0L, max = min + delta;
		max = ((max > high) ? high : max);
		b.setLength(0);
		b.append("START lib=node({libraryNodeId}) MATCH (lib)<-[r:");
		b.append(RelationType.REFERENCES);
		b.append("]-(x) WHERE ID(x) >= ");
		b.append(min);
		b.append(" AND ID(x) < ");
		b.append(max);
		b.append(" DELETE x,r RETURN count(x) AS count");

		String query = b.toString();
		logger.info("query= [{}]", query);
		
		Result<Map<String, Object>> result = this.relationService.getTemplate().query(query, params);
		if (result != null) {
			Iterator<Map<String, Object>> iter = result.iterator();
			while (iter.hasNext()) {
				Map<String, Object> map = iter.next();
				count= (long) map.get("count");
				logger.info("deleted node count: {}", count);
			}
		}
		return count;
	}


	/* (non-Javadoc)
	 * @see at.freebim.db.service.LibraryService#clear(java.lang.Long)
	 */
	@Override
	public Library clear(Long libraryNodeId) {
		Contributor c = null;
		boolean done = false;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			FreebimUser user = this.freebimUserService.get(auth.getName());
			if (user != null && user.getContributor() != null) {
				logger.info("user [{}] wants to clear Library nodeId={} ...", auth.getName(), libraryNodeId);
				c = this.contributorService.getByNodeId(user.getContributor().getNodeId());
				if (c != null) {
					if (this.contributorService.test(c, new RoleContributor [] {RoleContributor.ROLE_LIBRARY_REFERENCES})) {
						

						Map<String, Object> params = new HashMap<String, Object>();
						params.put( "libraryNodeId", libraryNodeId );

						long delta = 1000L, min;
						long[] minMax = getMinMaxId(libraryNodeId);
						long low = minMax[0];
						long high = minMax[1] + 1;
						// first: delete all relations to nodes of that library						
						try {
							for (min=low; min < high; min+=delta) {
								clearRelations(min, delta, high, params);
							}
						} catch (Exception e) {
							logger.error("ERROR in 'first: delete all relations to nodes of that library'", e);
							return null;
						}
						logger.info("\tall relations to nodes of library nodeId={} deleted.", libraryNodeId);

						// second: delete all nodes of that library						
						try {
							for (min=low; min < high; min+=delta) {
								clearNodes(min, delta, high, params);
							}
						} catch (Exception e) {
							logger.error("Error in 'second: delete all nodes of that library'", e);
							return null;
						}
						logger.info("\tall nodes of library nodeId={} deleted.", libraryNodeId);

						
						logger.info("Contributor [{}] did clear Library nodeId={}.", c.getName(), libraryNodeId);
						done = true;
					}
				}
			}
		}
		Library lib = this.getByNodeId(libraryNodeId);
		if (done && lib != null) {
			lib.setTs(this.dateService.getMillis());
			lib = this.save(lib);
		}
		return lib;
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.service.LibraryService#update(java.lang.Long)
	 */
	@Override
	public Library update(Long libraryNodeId) {
		Contributor c = null;
		boolean done = false;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			FreebimUser user = this.freebimUserService.get(auth.getName());
			if (user != null && user.getContributor() != null) {
				logger.info("user [{}] wants to update Library nodeId={} ...", auth.getName(), libraryNodeId);
				c = this.contributorService.getByNodeId(user.getContributor().getNodeId());
				if (c != null) {
					if (this.contributorService.test(c, new RoleContributor [] {RoleContributor.ROLE_LIBRARY_REFERENCES})) {
						
						LibraryUpdateEvent updateEvent = new LibraryUpdateEvent(libraryNodeId);
						this.applicationEventPublisher.publishEvent(updateEvent);
						logger.info("Contributor [{}] did trigger update Library nodeId={} event.", c.getName(), libraryNodeId);
						done = true;
					}
				}
			}
		}
		Library lib = this.getByNodeId(libraryNodeId);
		if (done && lib != null) {
			lib.setTs(this.dateService.getMillis());
			lib = this.save(lib);
		}
		return lib;
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(LibraryUpdateEvent event) {
		logger.info("onApplicationEvent(LibraryUpdateEvent) : {}", event);
		
		if (event != null && getIfcLibraryId().equals(event.getSource())) {
			// update the Ifc4 library ...
			logger.info("update the Ifc4 library ...");
			Library ifcLibrary = this.clear(getIfcLibraryId());
			this.createIfcStructure(ifcLibrary);
			logger.info("update the Ifc4 library done.");
		}
		
	}
	

	/* (non-Javadoc)
	 * @see at.freebim.db.service.impl.BaseNodeServiceImpl#getRelevantQuery(java.lang.StringBuilder, java.lang.String)
	 */
	protected void getRelevantQuery(StringBuilder b, String returnStatement) {
		
		b.append("MATCH (y:BigBangNode)");
		
		b.append(" WITH y AS x MATCH"); 
		b.append(" (x)-[:");
		b.append(RelationTypeEnum.PARENT_OF);
		b.append("]->(y)");
		b.append(" WHERE y.validFrom < {now} AND (y.validTo IS NULL OR y.validTo > {now})");
		b.append(returnStatement);
	}

}
