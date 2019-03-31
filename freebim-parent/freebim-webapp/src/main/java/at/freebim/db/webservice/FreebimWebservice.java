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
package at.freebim.db.webservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.neo4j.graphdb.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.freebim.db.domain.FreebimUser;
import at.freebim.db.domain.base.BaseNode;
import at.freebim.db.domain.base.HierarchicalBaseNode;
import at.freebim.db.domain.base.Role;
import at.freebim.db.domain.base.UuidIdentifyable;
import at.freebim.db.domain.rel.ComponentComponent;
import at.freebim.db.domain.rel.ContainsParameter;
import at.freebim.db.domain.rel.Equals;
import at.freebim.db.domain.rel.HasEntry;
import at.freebim.db.domain.rel.HasMeasure;
import at.freebim.db.domain.rel.HasParameter;
import at.freebim.db.domain.rel.HasParameterSet;
import at.freebim.db.domain.rel.HasValue;
import at.freebim.db.domain.rel.OfDataType;
import at.freebim.db.domain.rel.OfMaterial;
import at.freebim.db.domain.rel.OfUnit;
import at.freebim.db.domain.rel.ParentOf;
import at.freebim.db.service.ComponentService;
import at.freebim.db.service.DataTypeService;
import at.freebim.db.service.DisciplineService;
import at.freebim.db.service.FreebimUserService;
import at.freebim.db.service.LibraryService;
import at.freebim.db.service.ParameterService;
import at.freebim.db.service.ParameterSetService;
import at.freebim.db.service.PhaseService;
import at.freebim.db.service.RelationService;
import at.freebim.db.service.UnitService;
import at.freebim.db.webservice.dto.Base;
import at.freebim.db.webservice.dto.Component;
import at.freebim.db.webservice.dto.DataType;
import at.freebim.db.webservice.dto.Discipline;
import at.freebim.db.webservice.dto.Library;
import at.freebim.db.webservice.dto.LibraryReference;
import at.freebim.db.webservice.dto.Measure;
import at.freebim.db.webservice.dto.Parameter;
import at.freebim.db.webservice.dto.ParameterSet;
import at.freebim.db.webservice.dto.Phase;
import at.freebim.db.webservice.dto.Unit;
import at.freebim.db.webservice.dto.ValueList;
import at.freebim.db.webservice.dto.rel.OrderedRel;
import at.freebim.db.webservice.dto.rel.ParameterRel;
import at.freebim.db.webservice.dto.rel.QualifiedRel;
import at.freebim.db.webservice.dto.rel.Rel;
import at.freebim.db.webservice.dto.rel.ValueListRel;

/**
 * This service provides basic functionality for a bunch of classes/nodes.
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@Service("freebimWebservice")
public class FreebimWebservice {
	
	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(FreebimWebservice.class);

	@Autowired
	private DtoHelper dtoHelper;
	
	/**
	 * The service that handles the {@link at.freebim.db.domain.Phase}.
	 */
	@Autowired
	private PhaseService phaseService;

	/**
	 * The service that handles the {@link at.freebim.db.domain.Unit}.
	 */
	@Autowired
	private UnitService unitService;
	
	/**
	 * The service that handles the {@link at.freebim.db.domain.Component}.
	 */
	@Autowired
	private ComponentService componentService;
	
	/**
	 * The service that handles the {@link at.freebim.db.domain.Discipline}.
	 */
	@Autowired
	private DisciplineService disciplineService;

	/**
	 * The service that handles the {@link at.freebim.db.domain.DataType}.
	 */
	@Autowired
	private DataTypeService dataTypeService;
	
	/**
	 * The service that handles the {@link at.freebim.db.domain.Library}.
	 */
	@Autowired
	private LibraryService libraryService;
	
	/**
	 * The service that handles the {@link at.freebim.db.domain.FreebimUser}.
	 */
	@Autowired
	private FreebimUserService freebimUserService;
	
	/**
	 * The service that handles relations.
	 */
	@Autowired
	private RelationService relationService;
	
	/**
	 * The service that handles the {@link at.freebim.db.domain.ParameterSet}.
	 */
	@Autowired
	private ParameterSetService parameterSetService;

	/**
	 * The service that handles {@link at.freebim.db.domain.Parameter}s.
	 */
	@Autowired
	private ParameterService parameterService;
	

	/**
	 * Get all relevant nodes of the type {@link at.freebim.db.domain.Unit}.
	 * Provide the user name and password to test if {@link FreebimUser} is allowed to do this.
	 * If he is not allowed an empty {@link List} is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @return the {@link List} of all found {@link Unit} objects
	 */
	public List<Unit> getAllUnits(String username, String password) {
		logger.info("getUnits ...");
		List<Unit> delivered = new ArrayList<Unit>();
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			List<at.freebim.db.domain.Unit> stored = this.unitService.getAll(true);
			for (at.freebim.db.domain.Unit unit : stored) {
				if (this.dtoHelper.validNode(unit)) {
					delivered.add(new Unit(unit, this.dtoHelper));
				}
			}
		}
		logger.info("getUnits finished.");
		return delivered;
	}

	/**
	 * Get all relevant nodes of the type {@link at.freebim.db.domain.Phase}.
	 * Provide the user name and password to test if {@link FreebimUser} is allowed to do this.
	 * If he is not allowed an empty {@link List} is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @return the {@link List} of all found {@link Phase} objects
	 */
	public List<Phase> getAllPhases(String username, String password) {
		logger.info("getAllPhases ...");
		List<Phase> delivered = new ArrayList<Phase>();
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			ArrayList<at.freebim.db.domain.Phase> stored = this.phaseService.getAll(true);
			for (at.freebim.db.domain.Phase phase : stored) {
				if (this.dtoHelper.validNode(phase)) {
					delivered.add(new Phase(phase, this.dtoHelper));
				}
			}
		}
		logger.info("getAllPhases finished.");
		return delivered;
	}
	
	/**
	 * Get all relevant nodes of the type {@link at.freebim.db.domain.Component}.
	 * Provide the user name and password to test if {@link FreebimUser} is allowed to do this.
	 * If he is not allowed an empty {@link List} is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @return the {@link List} of all found {@link Component} objects
	 */
	public List<Component> getAllComponents(String username, String password) {
		logger.info("getAllComponents ...");
		List<Component> delivered = new ArrayList<Component>();
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			ArrayList<at.freebim.db.domain.Component> stored = this.componentService.getAll(true);
			logger.info("getAllComponents: {} found.", (stored == null) ? "null" : stored.size());
			for (at.freebim.db.domain.Component comp : stored) {
				if (this.dtoHelper.validNode(comp)) {
					delivered.add(new Component(comp, this.dtoHelper.getLibraryId(comp), this.dtoHelper));
				}
			}
		}
		logger.info("getAllComponents finished.");
		return delivered;
	}
	
	/**
	 * Get all relevant nodes of the type {@link at.freebim.db.domain.Parameter}.
	 * Provide the user name and password to test if {@link FreebimUser} is allowed to do this.
	 * If he is not allowed an empty {@link List} is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @return the {@link List} of all found {@link Parameter} objects
	 */
	public List<Parameter> getAllParameters(String username, String password) {
		logger.info("getAllParameters ...");
		List<Parameter> delivered = new ArrayList<Parameter>();
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			ArrayList<at.freebim.db.domain.Parameter> stored = this.parameterService.getAll(true);
			logger.info("getAllParameters: {} found.", (stored == null) ? "null" : stored.size());
			for (at.freebim.db.domain.Parameter param : stored) {
				if (this.dtoHelper.validNode(param)) {
					delivered.add(new Parameter(param, dtoHelper, false));
				}
			}
		}
		logger.info("getAllParameters finished, [{}] elements returned.", delivered.size());
		return delivered;
	}
	
	/**
	 * Get all relevant nodes of the type {@link at.freebim.db.domain.ParameterSet}.
	 * Provide the user name and password to test if {@link FreebimUser} is allowed to do this.
	 * If he is not allowed an empty {@link List} is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @return the {@link List} of all found {@link ParameterSet} objects
	 */
	public List<ParameterSet> getAllParameterSets(String username, String password) {
		logger.info("getAllParameterSets ...");
		List<ParameterSet> delivered = new ArrayList<ParameterSet>();
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			at.freebim.db.domain.Library ifcLibrary = this.libraryService.get(LibraryService.LIBRARY_NAME_IFC4);
			ArrayList<at.freebim.db.domain.ParameterSet> stored = this.parameterSetService.getAll(true);
			for (at.freebim.db.domain.ParameterSet pset : stored) {
				if (this.dtoHelper.validNode(pset)) {
					ParameterSet ps = new ParameterSet(pset, dtoHelper);
					delivered.add(ps);
					
					// traverse owners up to Library to determine whether this is a standard IFC property set
					Iterable<HasParameterSet> owners = pset.getOwners();
					boolean isIfc = false;
					if (owners != null) {
						Iterator<HasParameterSet> iter = owners.iterator();
						while (iter.hasNext()) {
							HasParameterSet rel = iter.next();
							HierarchicalBaseNode owner = (HierarchicalBaseNode) this.dtoHelper.getRelatedNode(pset, rel, Direction.INCOMING); 
							isIfc = this.dtoHelper.belongsToLibrary(owner, ifcLibrary);
							if (isIfc) {
								break;
							}
						}
					}
				}
			}
		}
		logger.info("getAllParameterSets finished.");
		return delivered;
	}



	/**
	 * Get all relevant nodes of the type {@link at.freebim.db.domain.Discipline}.
	 * Provide the user name and password to test if {@link FreebimUser} is allowed to do this.
	 * If he is not allowed an empty {@link List} is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @return the {@link List} of all found {@link Discipline} objects
	 */
	public List<Discipline> getAllDisciplines(String username, String password) {
		logger.info("getDisciplines ...");
		List<Discipline> delivered = new ArrayList<Discipline>();
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			ArrayList<at.freebim.db.domain.Discipline> stored = this.disciplineService.getAll(true);
			for (at.freebim.db.domain.Discipline dis : stored) {
				if (this.dtoHelper.validNode(dis)) {
					delivered.add(new Discipline(dis, this.dtoHelper));
				}
			}
		}
		logger.info("getDisciplines finished.");
		return delivered;
	}

	/**
	 * Get all relevant nodes of the type {@link at.freebim.db.domain.DataType}.
	 * Provide the user name and password to test if {@link FreebimUser} is allowed to do this.
	 * If he is not allowed an empty {@link List} is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @return the {@link List} of all found {@link DataType} objects
	 */
	public List<DataType> getAllDataTypes(String username, String password) {
		logger.info("getDataTypes ...");
		List<DataType> delivered = new ArrayList<DataType>();
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			ArrayList<at.freebim.db.domain.DataType> stored = this.dataTypeService.getAll(true);
			for (at.freebim.db.domain.DataType dt : stored) {
				if (this.dtoHelper.validNode(dt)) {
					delivered.add(new DataType(dt, this.dtoHelper));
				}
			}
		}
		logger.info("getDataTypes finished.");
		return delivered;
	}

	/**
	 * Get the {@link at.freebim.db.domain.Component} that has the provided uuid.
	 * If no {@link at.freebim.db.domain.Component} is found <code>null</code> is returned.
	 * Provide the user name and password to test if {@link FreebimUser} is allowed to do this.
	 * If he is not allowed an <code>null</code> is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @param uuid the uuid of the {@link at.freebim.db.domain.Component}
	 * @return the {@link Component} that has the matching uuid
	 */
	public Component getComponent(String username, String password, String uuid) {
		logger.info("getComponent uuid=[{}]", uuid);
		Component delivered = null;
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			UuidIdentifyable stored = this.relationService.getByUuid(uuid);
			if (this.dtoHelper.validNode(stored) && at.freebim.db.domain.Component.class.isAssignableFrom(stored.getClass())) {
				delivered = new Component((at.freebim.db.domain.Component) stored, this.dtoHelper.getLibraryId(stored), this.dtoHelper);
			}
			logger.info("getComponent id=[{}] finished.", uuid);
		}
		return delivered;
	}
	
	/**
	 * Get the {@link at.freebim.db.domain.Parameter} that has the provided uuid.
	 * If no {@link at.freebim.db.domain.Parameter} is found <code>null</code> is returned.
	 * Provide the user name and password to test if {@link FreebimUser} is allowed to do this.
	 * If he is not allowed an <code>null</code> is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @param uuid the uuid of the {@link at.freebim.db.domain.Parameter}
	 * @param fetch if set to <code>true</code> all referenced nodes will be fetched too. 
	 * 		  If set to <code>false</code> only relations to referenced nodes will be returned.
	 * @return the {@link Parameter} that has the matching uuid
	 */
	public Parameter getParameter(String username, String password, String uuid, boolean fetch) {
		logger.info("getParameter uuid=[{}]", uuid);
		Parameter delivered = null;
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			UuidIdentifyable stored = this.relationService.getByUuid(uuid);
			if (this.dtoHelper.validNode(stored) && at.freebim.db.domain.Parameter.class.isAssignableFrom(stored.getClass())) {
				delivered = new Parameter((at.freebim.db.domain.Parameter) stored, this.dtoHelper, fetch);
			}
			logger.info("getParameter id=[{}] finished.", uuid);
		}
		return delivered;
	}

	/**
	 * Get the {@link at.freebim.db.domain.ValueList} that has the provided uuid.
	 * If no {@link at.freebim.db.domain.ValueList} is found <code>null</code> is returned.
	 * Provide the user name and password to test if {@link FreebimUser} is allowed to do this.
	 * If he is not allowed an <code>null</code> is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @param uuid the uuid of the {@link at.freebim.db.domain.ValueList}
	 * @param fetch if set to <code>true</code> all referenced nodes will be fetched too. 
	 * 		  If set to <code>false</code> only relations to referenced nodes will be returned.
	 * @return the {@link Parameter} that has the matching uuid
	 */
	public ValueList getValueList(String username, String password, String uuid, boolean fetch) {
		logger.info("getValueList uuid=[{}]", uuid);
		ValueList delivered = null;
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			UuidIdentifyable stored = this.relationService.getByUuid(uuid);
			if (this.dtoHelper.validNode(stored) && at.freebim.db.domain.ValueList.class.isAssignableFrom(stored.getClass())) {
				at.freebim.db.domain.ValueList vl = (at.freebim.db.domain.ValueList) stored; 
				delivered = new ValueList(vl, this.dtoHelper, fetch);
			}
			logger.info("getValueList id=[{}] finished.", uuid);
		}
		return delivered;
	}

	
	/**
	 * Get the {@link at.freebim.db.service.LibraryService.LibraryReference}.
	 * Those are defined by the name of the reference and the id of the library itself..
	 * If nothing is found or the user has not the access rights an empty {@link List} is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @param libraryName the name of the {@link at.freebim.db.domain.Library} 
	 * @param refIdName the name of the reference
	 * @return the {@link List} of found {@link LibraryReference} that match the parameters
	 */
	public List<LibraryReference> getLibraryReferences(String username, String password, String libraryName, String refIdName) {
		logger.info("getLibraryReferences libraryName=[{}], refIdName=[{}] ...", libraryName, refIdName);
		List<LibraryReference> res = new ArrayList<LibraryReference>();
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			logger.debug("username=[{}] test ok.", username);
			at.freebim.db.domain.Library library = this.libraryService.get(libraryName);
			if (library != null) {
				logger.debug("library=[{}] found.", library.getName());
				List<at.freebim.db.service.LibraryService.LibraryReference> refs = this.libraryService.getReferencedNodes(library.getNodeId(), refIdName);
				logger.debug("LibraryReference count=[{}].", refs == null ? null : refs.size());
				for (at.freebim.db.service.LibraryService.LibraryReference ref : refs) {
					res.add(new LibraryReference(ref, this.dtoHelper));
				}
			}
			else
				logger.error("getLibraryReferences: no Library for username=[{}].", username);
			
		} else
			logger.error("getLibraryReferences: test failed for username=[{}].", username);
		logger.info("getLibraryReferences finished.");
		return res;
	}

	/**
	 * Get the {@link List} of nodes ({@link Base}) that extend {@link UuidIdentifyable} 
	 * and reference a {@link at.freebim.db.domain.Library}.
	 * Those are defined by the name of the reference and the id of the library itself..
	 * If nothing is found or the user has not the access rights an empty {@link List} is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @param libraryName the name of the {@link at.freebim.db.domain.Library} 
	 * @param refIdName the name of the reference
	 * @param refId the id/code of the reference
	 * @return the {@link List} of found {@link LibraryReference}s that match the parameters
	 */
	public List<Base<? extends UuidIdentifyable>> getLibraryReference(String username, String password, String libraryName, String refIdName, String refId) {
		logger.info("getLibraryReference libraryName=[{}], refIdName=[{}] ...", libraryName, refIdName);
		List<Base<? extends UuidIdentifyable>> res = new ArrayList<Base<? extends UuidIdentifyable>>();
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			logger.debug("username=[{}] test ok.", username);
			at.freebim.db.domain.Library library = this.libraryService.get(libraryName);
			if (library != null) {
				logger.debug("library=[{}] found.", library.getName());
				List<? extends UuidIdentifyable> nodes = this.libraryService.getReferencedNodesForRefId(library.getNodeId(), refIdName, refId);
				logger.debug("LibraryReference count=[{}].", nodes == null ? null : nodes.size());
				
				DtoInstantiatorVisitor instantiator = new DtoInstantiatorVisitor(this.dtoHelper);
				for (UuidIdentifyable node : nodes) {
					if (node == null)
						continue;
					node.accept(instantiator);
					Base<? extends UuidIdentifyable> base = instantiator.getInstance();
					res.add(base);
				}
			}
			else
				logger.error("getLibraryReferences: no Library for username=[{}].", username);
			
		} else
			logger.error("getLibraryReferences: test failed for username=[{}].", username);
		logger.info("getLibraryReference finished.");
		return res;
	}
	
	/**
	 * Get a node ({@link Base}) that extends {@link UuidIdentifyable} that has the
	 * provided freebimId. Provide the user name and password to test if {@link FreebimUser} 
	 * is allowed to do this. If he has no permission, <code>null</code> is returned.
	 * 
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @param freebimId the unique freebimId
	 * @return the node ({@link Base}) that extends {@link UuidIdentifyable} that has the matching freebimId
	 */
	public Base<? extends UuidIdentifyable> getByFreebimId(String username, String password, String freebimId) {
		logger.info("getByFreebimId freebimId=[{}] ...", freebimId);
		Base<? extends UuidIdentifyable> res = null;
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			logger.debug("username=[{}] test ok.", username);
			UuidIdentifyable node = this.relationService.findByFreebimId(freebimId);
			if (node != null) {
				DtoInstantiatorVisitor instantiator = new DtoInstantiatorVisitor(this.dtoHelper);
				node.accept(instantiator);
				res = instantiator.getInstance();
			}
		}		
		return res;
	}

	/**
	 * Get all child's of the node that has the provided freebimId.
	 * Provide the user name and password to test if {@link FreebimUser} 
	 * is allowed to do this. If he has no permission an empty {@link List} is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @param freebimId the unique freebimId
	 * @return the {@link List} of {@link OrderedRel} that have a child relation to the node with
	 * the provided freebimId
	 */
	public List<OrderedRel> getChildsOf(String username, String password, String freebimId) {
		logger.info("getChildsOf freebimId=[{}] ...", freebimId);
		List<OrderedRel> delivered = new ArrayList<OrderedRel>();
		
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			logger.debug("getChildsOf {}", freebimId);
			try {
				UuidIdentifyable parent = this.relationService.getByUuid(freebimId);
				logger.debug("getChildsOf found {}", parent == null ? "null" : parent.getNodeId());
				if (parent != null && HierarchicalBaseNode.class.isAssignableFrom(parent.getClass())) {
					Iterable<ParentOf> rels = ((HierarchicalBaseNode) parent).getChilds();
					Iterator<ParentOf> iter = rels.iterator();
					if (iter != null) {
						while (iter.hasNext()) {
							ParentOf rel = iter.next();
							UuidIdentifyable node = (UuidIdentifyable) this.dtoHelper.getRelatedNode(parent, rel, Direction.OUTGOING); 
							if (this.dtoHelper.validNode(node)) {
								OrderedRel r = new OrderedRel(node.getUuid(), rel.getOrdering(), rel.getInfo(), this.dtoHelper);
								delivered.add(r);
							}						
						}
						Collections.sort(delivered, new Comparator<OrderedRel>() {
							@Override
							public int compare(OrderedRel a, OrderedRel b) {
								return (a.getOrder() - b.getOrder());
							}
						});
					}
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		logger.info("getChildsOf freebimId=[{}] finished.", freebimId);
		return delivered;
	}
	
	/**
	 * Get all parameter nodes of the node that has the provided freebimId.
	 * Provide the user name and password to test if {@link FreebimUser} 
	 * is allowed to do this. If he has no permission an empty {@link List} is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @param freebimId the unique freebimId
	 * @return the {@link List} of {@link ParameterRel} 
	 */
	public List<ParameterRel> getParameterOf(String username, String password, String freebimId) {
		logger.info("getParameterOf freebimId=[{}] ...", freebimId);
		List<ParameterRel> delivered = new ArrayList<ParameterRel>();
		
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			logger.debug("getParameterOf {}", freebimId);
			try {
				UuidIdentifyable parent = this.relationService.getByUuid(freebimId);
				if (parent != null) {
					if (at.freebim.db.domain.Component.class.isAssignableFrom(parent.getClass())) {
						// parameters of Component
						logger.debug("getParameterOf found component {}", parent == null ? "null" : parent.getNodeId());
						Iterable<HasParameter> rels = ((at.freebim.db.domain.Component) parent).getParameter();
						if (rels != null) {
							Iterator<HasParameter> iter = rels.iterator();
							if (iter != null) {
								while (iter.hasNext()) {
									HasParameter rel = iter.next();
									UuidIdentifyable node = (UuidIdentifyable) this.dtoHelper.getRelatedNode(parent, rel, Direction.OUTGOING); 
									if (this.dtoHelper.validNode(node)) {
										ParameterRel r = new ParameterRel(node.getUuid(), rel.getOrdering(), rel.getPhaseUuid(), rel.getInfo(), this.dtoHelper);
										delivered.add(r);
									}
								}
								Collections.sort(delivered, new Comparator<OrderedRel>() {
									@Override
									public int compare(OrderedRel a, OrderedRel b) {
										return (a.getOrder() - b.getOrder());
									}
								});
							}
						}
					}
					
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		logger.info("getParameterOf freebimId=[{}] finished.", freebimId);
		return delivered;
	}
	
	
	/**
	 * Get all {@link at.freebim.db.domain.ParameterSet} of the node that has the provided freebimId.
	 * The relation of those nodes is {@link ContainsParameter}.
	 * Provide the user name and password to test if {@link FreebimUser} 
	 * is allowed to do this. If he has no permission an empty {@link List} is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @param freebimId the unique freebimId
	 * @return the {@link List} of {@link OrderedRel}
	 * 
	 */
	public List<OrderedRel> getParameterOfPSet(String username, String password, String freebimId) {
		logger.info("getParameterOfPSet freebimId=[{}] ...", freebimId);
		List<OrderedRel> delivered = new ArrayList<OrderedRel>();
		
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			logger.debug("getParameterOfPSet {}", freebimId);
			try {
				UuidIdentifyable parent = this.relationService.getByUuid(freebimId);
				if (parent != null) {
					if (at.freebim.db.domain.ParameterSet.class.isAssignableFrom(parent.getClass())) {
						// parameters of ParameterSet
						logger.debug("getParameterOfPSet found ParameterSet {}", parent == null ? "null" : parent.getNodeId());
						Iterable<ContainsParameter> rels = ((at.freebim.db.domain.ParameterSet) parent).getParameters();
						if (rels != null) {
							Iterator<ContainsParameter> iter = rels.iterator();
							if (iter != null) {
								while (iter.hasNext()) {
									ContainsParameter rel = iter.next();
									UuidIdentifyable node = (UuidIdentifyable) this.dtoHelper.getRelatedNode(parent, rel, Direction.OUTGOING); 
									if (this.dtoHelper.validNode(node)) {
										OrderedRel r = new OrderedRel(node.getUuid(), rel.getOrdering(), rel.getInfo(), this.dtoHelper);
										delivered.add(r);
									}
								}
								Collections.sort(delivered, new Comparator<OrderedRel>() {
									@Override
									public int compare(OrderedRel a, OrderedRel b) {
										return (a.getOrder() - b.getOrder());
									}
								});
							}
						}
						
					}
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		logger.info("getParameterOfPSet freebimId=[{}] finished.", freebimId);
		return delivered;
	}

	/**
	 * Get all {@link at.freebim.db.domain.ParameterSet} of the node that has the provided freebimId.
	 * The relation of those nodes is {@link HasParameterSet}.
	 * Provide the user name and password to test if {@link FreebimUser} 
	 * is allowed to do this. If he has no permission an empty {@link List} is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @param freebimId the unique freebimId
	 * @return the {@link List} of {@link Rel} 
	 *
	 */
	public List<Rel> getParameterSetOf(String username, String password, String freebimId) {

		logger.info("getParameterSetOf freebimId=[{}] ...", freebimId);

		List<Rel> delivered = new ArrayList<Rel>();
		
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			logger.debug("getParameterSetOf {}", freebimId);
			try {
				UuidIdentifyable parent = this.relationService.getByUuid(freebimId);
				if (parent != null) {
					Iterable<HasParameterSet> rels = null;
					if (HierarchicalBaseNode.class.isAssignableFrom(parent.getClass())) {
						logger.debug("getParameterSet of Library found Library {}", parent == null ? "null" : parent.getNodeId());
						rels = ((HierarchicalBaseNode) parent).getParameterSets();
						if (rels != null) {
							Iterator<HasParameterSet> iter = rels.iterator();
							if (iter != null) {
								while (iter.hasNext()) {
									HasParameterSet rel = iter.next();
									UuidIdentifyable node = (UuidIdentifyable) this.dtoHelper.getRelatedNode(parent, rel, Direction.OUTGOING); 
									if (this.dtoHelper.validNode(node)) {
										Rel r = new Rel(node.getUuid(), rel.getInfo(), this.dtoHelper);
										delivered.add(r);
									}
								}
							}
						}
					} 
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		logger.info("getParameterSetOf freebimId=[{}] finished.", freebimId);
		return delivered;
	}

	
	/**
	 * Get all nodes that are in a {@link Equals} relation.
	 * Provide the user name and password to test if {@link FreebimUser} 
	 * is allowed to do this. If he has no permission an empty {@link List} is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @param freebimId the unique freebimId
	 * @return the {@link List} of {@link QualifiedRel} 
	 */
	public List<QualifiedRel> getEqualsOf(String username, String password, String freebimId) {
		logger.info("getEqualsOf freebimId=[{}] ...", freebimId);
		List<QualifiedRel> delivered = new ArrayList<QualifiedRel>();
		
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			logger.debug("getEqualsOf {}", freebimId);
			UuidIdentifyable src = this.relationService.getByUuid(freebimId);
			if (src != null) {
				Iterable<Equals> iterable = src.getEq();
				if (iterable != null) {
					Iterator<Equals> iter = iterable.iterator();
					if (iter != null) {
						while (iter.hasNext()) {
							Equals rel = iter.next();
							BaseNode node = this.dtoHelper.getRelatedNode(src, rel, Direction.BOTH);
							if (this.dtoHelper.validNode(node) && UuidIdentifyable.class.isAssignableFrom(node.getClass())) {
								QualifiedRel r = new QualifiedRel(((UuidIdentifyable) node).getUuid(), rel.getQ(), rel.getInfo(), this.dtoHelper);
								delivered.add(r);
							}
						}
						Collections.sort(delivered, new Comparator<QualifiedRel>() {
							@Override
							public int compare(QualifiedRel a, QualifiedRel b) {
								return (int) Math.floor(a.getQ() - b.getQ());
							}
						});
					}
				}
			}
		}
		logger.info("getEqualsOf freebimId=[{}] finished.", freebimId);
		return delivered;
	}

	/**
	 * Get the different parts of a {@link at.freebim.db.domain.Component} 
	 * out which it is constructed. Provide the user name and password 
	 * to test if {@link FreebimUser} is allowed to do this. If he has no permission 
	 * an empty {@link List} is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @param freebimId the unique freebimId
	 * @return the {@link List} of {@link Rel} that include the components 
	 * out of which the component is build
	 */
	public List<Rel> getPartsOf(String username, String password, String freebimId) {
		logger.info("getPartsOf freebimId=[{}] ...", freebimId);
		List<Rel> delivered = new ArrayList<Rel>();
		
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			logger.debug("getPartsOf {}", freebimId);
			UuidIdentifyable src = this.relationService.getByUuid(freebimId);
			if (src != null && at.freebim.db.domain.Component.class.isAssignableFrom(src.getClass())) {
				Iterable<ComponentComponent> iterable = ((at.freebim.db.domain.Component) src).getParts();
				if (iterable != null) {
					Iterator<ComponentComponent> iter = iterable.iterator();
					if (iter != null) {
						while (iter.hasNext()) {
							ComponentComponent rel = iter.next();
							BaseNode node = this.dtoHelper.getRelatedNode(src, rel, Direction.OUTGOING);
							if (this.dtoHelper.validNode(node) && UuidIdentifyable.class.isAssignableFrom(node.getClass())) {
								Rel r = new Rel(((UuidIdentifyable) node).getUuid(), rel.getInfo(), this.dtoHelper);
								delivered.add(r);
							}
						}
					}
				}
			}
		}
		logger.info("getPartsOf freebimId=[{}] finished.", freebimId);
		return delivered;
	}

	/**
	 * Get the material of a {@link at.freebim.db.domain.Component} 
	 * out which it is constructed. Provide the user name and password 
	 * to test if {@link FreebimUser} is allowed to do this. If he has no permission 
	 * an empty {@link List} is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @param freebimId the unique freebimId
	 * @return the {@link List} of {@link Rel} that inlcude the material
	 * out of which the component is build
	 */
	public List<Rel> getMaterialOf(String username, String password, String freebimId) {
		logger.info("getMaterialOf freebimId=[{}] ...", freebimId);
		List<Rel> delivered = new ArrayList<Rel>();
		
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			logger.debug("getMaterialOf {}", freebimId);
			UuidIdentifyable src = this.relationService.getByUuid(freebimId);
			if (src != null && at.freebim.db.domain.Component.class.isAssignableFrom(src.getClass())) {
				Iterable<OfMaterial> iterable = ((at.freebim.db.domain.Component) src).getMaterial();
				if (iterable != null) {
					Iterator<OfMaterial> iter = iterable.iterator();
					if (iter != null) {
						while (iter.hasNext()) {
							OfMaterial rel = iter.next();
							BaseNode node = this.dtoHelper.getRelatedNode(src, rel, Direction.OUTGOING);
							if (this.dtoHelper.validNode(node) && UuidIdentifyable.class.isAssignableFrom(node.getClass())) {
								Rel r = new Rel(((UuidIdentifyable) node).getUuid(), rel.getInfo(), this.dtoHelper);
								delivered.add(r);
							}
						}
					}
				}
			}
		}
		logger.info("getMaterialOf freebimId=[{}] finished.", freebimId);
		return delivered;
	}

	/**
	 * Get all nodes that are {@link at.freebim.db.domain.Measure}s of a 
	 * {@link at.freebim.db.domain.Parameter}.
	 * Provide the user name and password to test if {@link FreebimUser} 
	 * is allowed to do this. If he has no permission an empty {@link List} is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @param freebimId the unique freebimId
	 * @return the {@link List} of {@link OrderedRel} in which the measures are saved
	 */
	public List<OrderedRel> getMeasuresOf(String username, String password, String freebimId) {
		logger.info("getMeasuresOf freebimId=[{}] ...", freebimId);
		List<OrderedRel> delivered = new ArrayList<OrderedRel>();
		
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			logger.debug("getMeasuresOf {}", freebimId);
			UuidIdentifyable src = this.relationService.getByUuid(freebimId);
			if (src != null && at.freebim.db.domain.Parameter.class.isAssignableFrom(src.getClass())) {
				Iterable<HasMeasure> iterable = ((at.freebim.db.domain.Parameter) src).getMeasures();
				if (iterable != null) {
					Iterator<HasMeasure> iter = iterable.iterator();
					if (iter != null) {
						while (iter.hasNext()) {
							HasMeasure rel = iter.next();
							BaseNode node = this.dtoHelper.getRelatedNode(src, rel, Direction.OUTGOING);
							if (this.dtoHelper.validNode(node) && UuidIdentifyable.class.isAssignableFrom(node.getClass())) {
								OrderedRel r = new OrderedRel(((UuidIdentifyable) node).getUuid(), rel.getOrdering(), rel.getInfo(), this.dtoHelper);
								delivered.add(r);
							}
						}
						Collections.sort(delivered, new Comparator<OrderedRel>() {
							@Override
							public int compare(OrderedRel a, OrderedRel b) {
								return (a.getOrder() - b.getOrder());
							}
						});
					}
				}
			}
		}
		logger.info("getMeasuresOf freebimId=[{}] finished.", freebimId);
		return delivered;
	}
	
	/**
	 * Get the node {@link at.freebim.db.domain.Measure} that has the provided freebimId.
	 * Provide the user name and password to test if {@link FreebimUser} 
	 * is allowed to do this. If he has no permission <code>null</code> is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @param freebimId the unique freebimId
	 * @param fetch if set to <code>true</code> all referenced nodes will be fetched too. 
	 * 		  If set to <code>false</code> only relations to referenced nodes will be returned.
	 * @return the {@link Measure} that has the provided freebimId
	 */
	public Measure getMeasure(String username, String password, String freebimId, boolean fetch) {
		logger.info("getMeasure freebimId=[{}] ...", freebimId);
		Measure delivered = null;
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			logger.debug("getMeasure {}", freebimId);
			UuidIdentifyable src = this.relationService.getByUuid(freebimId);
			if (src != null && at.freebim.db.domain.Measure.class.isAssignableFrom(src.getClass())) {
				at.freebim.db.domain.Measure m = (at.freebim.db.domain.Measure) src;
				delivered = new Measure(m, dtoHelper, fetch);
			}
		}
		logger.info("getMeasure freebimId=[{}] finished.", freebimId);
		return delivered;
	}

	
	/**
	 * Get the {@link at.freebim.db.domain.Unit}s of the {@link at.freebim.db.domain.Measure} 
	 * node that has the provided freebimId.
	 * Provide the user name and password to test if {@link FreebimUser} 
	 * is allowed to do this. If he has no permission <code>null</code> is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @param freebimId the unique freebimId
	 * @return the {@link Rel} with the {@link Unit} that is connected to 
	 * {@link at.freebim.db.domain.Measure} with the provided freebimId
	 */
	public Rel getUnitOf(String username, String password, String freebimId) {
		logger.info("getUnitOf freebimId=[{}] ...", freebimId);
		Rel delivered = null;
		
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			logger.debug("getUnitOf {}", freebimId);
			UuidIdentifyable src = this.relationService.getByUuid(freebimId);
			if (src != null && at.freebim.db.domain.Measure.class.isAssignableFrom(src.getClass())) {
				Iterable<OfUnit> iterable = ((at.freebim.db.domain.Measure) src).getUnit();
				if (iterable != null) {
					Iterator<OfUnit> iter = iterable.iterator();
					if (iter != null) {
						while (iter.hasNext()) {
							OfUnit rel = iter.next();
							BaseNode node = this.dtoHelper.getRelatedNode(src, rel, Direction.OUTGOING);
							if (this.dtoHelper.validNode(node) && UuidIdentifyable.class.isAssignableFrom(node.getClass())) {
								delivered = new Rel(((UuidIdentifyable) node).getUuid(), rel.getInfo(), this.dtoHelper);
							}
						}
					}
				}
			}
		}
		logger.info("getUnitOf freebimId=[{}] finished.", freebimId);
		return delivered;
	}
	
	/**
	 * Get the {@link at.freebim.db.domain.DataType}s of the {@link at.freebim.db.domain.Measure} 
	 * node that has the provided freebimId.
	 * Provide the user name and password to test if {@link FreebimUser} 
	 * is allowed to do this. If he has no permission <code>null</code> is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @param freebimId the unique freebimId
	 * @return the {@link Rel} with the {@link DataType} that is connected to 
	 * {@link at.freebim.db.domain.Measure} with the provided freebimId
	 */
	public Rel getDataTypeOf(String username, String password, String freebimId) {
		logger.info("getDataTypeOf freebimId=[{}] ...", freebimId);
		Rel delivered = null;
		
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			logger.debug("getDataTypeOf {}", freebimId);
			UuidIdentifyable src = this.relationService.getByUuid(freebimId);
			if (src != null && at.freebim.db.domain.Measure.class.isAssignableFrom(src.getClass())) {
				Iterable<OfDataType> iterable = ((at.freebim.db.domain.Measure) src).getDataType();
				if (iterable != null) {
					Iterator<OfDataType> iter = iterable.iterator();
					if (iter != null) {
						while (iter.hasNext()) {
							OfDataType rel = iter.next();
							BaseNode node = this.dtoHelper.getRelatedNode(src, rel, Direction.OUTGOING);
							if (this.dtoHelper.validNode(node) && UuidIdentifyable.class.isAssignableFrom(node.getClass())) {
								delivered = new Rel(((UuidIdentifyable) node).getUuid(), rel.getInfo(), this.dtoHelper);
							}
						}
					}
				}
			}
		}
		logger.info("getDataTypeOf freebimId=[{}] finished.", freebimId);
		return delivered;
	}

	/**
	 * Get the {@link at.freebim.db.domain.ValueList}s of the {@link at.freebim.db.domain.Measure} 
	 * node that has the provided freebimId.
	 * Provide the user name and password to test if {@link FreebimUser} 
	 * is allowed to do this. If he has no permission an empty {@link List} is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @param freebimId the unique freebimId
	 * @return the {@link List} of {@link ValueListRel} that are connected to 
	 * {@link at.freebim.db.domain.Measure} with the provided freebimId
	 */
	public List<ValueListRel> getValueListsOf(String username, String password, String freebimId) {
		logger.info("getValueListsOf freebimId=[{}] ...", freebimId);
		List<ValueListRel> delivered = new ArrayList<ValueListRel>();
		
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			logger.debug("getValueListsOf {}", freebimId);
			UuidIdentifyable src = this.relationService.getByUuid(freebimId);
			if (src != null && at.freebim.db.domain.Measure.class.isAssignableFrom(src.getClass())) {
				Iterable<HasValue> iterable = ((at.freebim.db.domain.Measure) src).getValue();
				if (iterable != null) {
					Iterator<HasValue> iter = iterable.iterator();
					if (iter != null) {
						while (iter.hasNext()) {
							HasValue rel = iter.next();
							BaseNode node = this.dtoHelper.getRelatedNode(src, rel, Direction.OUTGOING);
							if (this.dtoHelper.validNode(node) && UuidIdentifyable.class.isAssignableFrom(node.getClass())) {
								delivered.add(new ValueListRel(((UuidIdentifyable) node).getUuid(), rel.getComponentUuid(), rel.getInfo(), this.dtoHelper));
							}
						}
					}
				}
			}
		}
		logger.info("getValueListsOf freebimId=[{}] finished.", freebimId);
		return delivered;
	}
	
	/**
	 * Get the {@link at.freebim.db.domain.ValueList} of the {@link at.freebim.db.domain.Measure} 
	 * node that has the provided freebimId and is 
	 * connected to the {@link at.freebim.db.domain.Component} that has the provided name.
	 * Provide the user name and password to test if {@link FreebimUser} 
	 * is allowed to do this. If he has no permission <code>null</code> is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @param freebimId the unique freebimId
	 * @param component the name of the {@link at.freebim.db.domain.Component}
	 * @param fetch if set to <code>true</code> all referenced nodes will be fetched too. 
	 * 		  If set to <code>false</code> only relations to referenced nodes will be returned.
	 * @return the {@link ValueList} that fulfills the requirements that give the parameters
	 */
	public ValueList getValueListOf(String username, String password, String freebimId, String component, boolean fetch) {
		logger.info("getValueListOf freebimId=[{}], component=[{}] ...", freebimId, component);
		
		ValueList res = null;
		
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			logger.debug("getValueListOf {}", freebimId);
			UuidIdentifyable src = this.relationService.getByUuid(freebimId);
			if (src != null && at.freebim.db.domain.Measure.class.isAssignableFrom(src.getClass())) {
				Iterable<HasValue> iterable = ((at.freebim.db.domain.Measure) src).getValue();
				if (iterable != null) {
					Iterator<HasValue> iter = iterable.iterator();
					if (iter != null) {
						while (iter.hasNext()) {
							HasValue rel = iter.next();
							if (component.equals(rel.getComponentUuid())) {
								BaseNode node = this.dtoHelper.getRelatedNode(src, rel, Direction.OUTGOING);
								if (this.dtoHelper.validNode(node) && UuidIdentifyable.class.isAssignableFrom(node.getClass())) {
									res = new ValueList((at.freebim.db.domain.ValueList) node, this.dtoHelper, fetch);
									break;
								}
							}
						}
					}
				}
			}
		}
		logger.info("getValueListOf freebimId=[{}] finished.", freebimId);
		return res;
	}


	/**
	 * Get the entries in a {@link at.freebim.db.domain.ValueList} that has the
	 * provided freebimId. Provide the user name and password to test if {@link FreebimUser} 
	 * is allowed to do this. If he has no permission an empty {@link List} is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @param freebimId the unique freebimId
	 * @return the {@link List} of {@link OrderedRel} that contain the entries of the valuelist
	 */
	public List<OrderedRel> getValuesOf(String username, String password, String freebimId) {
		logger.info("getValuesOf freebimId=[{}] ...", freebimId);
		ArrayList<OrderedRel> delivered = new ArrayList<OrderedRel>();
		
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			logger.debug("getValuesOf {}", freebimId);
			UuidIdentifyable src = this.relationService.getByUuid(freebimId);
			if (src != null && at.freebim.db.domain.ValueList.class.isAssignableFrom(src.getClass())) {
				Iterable<HasEntry> iterable = ((at.freebim.db.domain.ValueList) src).getEntries();
				if (iterable != null) {
					Iterator<HasEntry> iter = iterable.iterator();
					if (iter != null) {
						while (iter.hasNext()) {
							HasEntry rel = iter.next();
							BaseNode node = this.dtoHelper.getRelatedNode(src, rel, Direction.OUTGOING);
							if (this.dtoHelper.validNode(node) && UuidIdentifyable.class.isAssignableFrom(node.getClass())) {
								OrderedRel r = new OrderedRel(((UuidIdentifyable) node).getUuid(), rel.getOrdering(), rel.getInfo(), this.dtoHelper);
								delivered.add(r);
							}
						}
						Collections.sort(delivered, new Comparator<OrderedRel>() {
							@Override
							public int compare(OrderedRel a, OrderedRel b) {
								return (a.getOrder() - b.getOrder());
							}
						});
					}
				}
			}
		}
		logger.info("getValuesOf freebimId=[{}] finished.", freebimId);
		return delivered;
	}

	/**
	 * Get all relevant nodes of the type {@link at.freebim.db.domain.Library}.
	 * Provide the user name and password to test if {@link FreebimUser} is allowed to do this.
	 * If he is not allowed an empty {@link List} is returned.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @return the {@link List} of all found {@link Library} objects
	 */
	public List<Library> getAllLibraries(String username, String password) {
		logger.info("getAllLibraries ...");
		List<Library> delivered = new ArrayList<Library>();
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			ArrayList<at.freebim.db.domain.Library> stored = this.libraryService.getAll(false);
			for (at.freebim.db.domain.Library node : stored) {
				if (this.dtoHelper.validNode(node)) {
					Library lib = new Library(node, this.dtoHelper);
					logger.debug("getAllLibraries library name=[{}], freebim-ID=[{}] added.", lib.getName(), lib.getFreebimId());
					delivered.add(lib);
				}
			}
		}
		logger.info("getAllLibraries finished, n=[{}].", delivered.size());
		return delivered;
	}

	/**
	 * Get all reference names for a {@link at.freebim.db.domain.Library} that has the
	 * provided name.
	 * 
	 * @param username the user name of the {@link FreebimUser}
	 * @param password the password of the {@link FreebimUser}
	 * @param libraryName the name of the {@link at.freebim.db.domain.Library}
	 * @return a {@link List} of names of the references
	 */
	public List<String> getRefIdNames(String username, String password, String libraryName) {
		List<String>  refIdNames = null;
		logger.info("getRefIdNames for Libraray {} ...", libraryName);
		if (this.freebimUserService.test(username, password, new Role[] {Role.ROLE_WEBSERVICE_READ})) {
			at.freebim.db.domain.Library library = this.libraryService.get(libraryName);
			if (library != null) {
				logger.debug("library=[{}] found.", library.getName());
				refIdNames = this.libraryService.getRefIdNames(library.getNodeId());	
			}
		}
		logger.info("getRefIdNames for Libraray {} finished.", libraryName);
		return refIdNames;
	}



}
