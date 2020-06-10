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
package at.freebim.db.webservice.endpoints;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import at.freebim.db.domain.base.UuidIdentifyable;
import at.freebim.db.webservice.FreebimWebservice;
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
import at.freebim.db.webservice.dto.ValueListEntry;
import at.freebim.db.webservice.dto.rel.OrderedRel;
import at.freebim.db.webservice.dto.rel.ParameterRel;
import at.freebim.db.webservice.dto.rel.QualifiedRel;
import at.freebim.db.webservice.dto.rel.Rel;
import at.freebim.db.webservice.dto.rel.ValueListRel;

/**
 * WebService end point for {@linkplain http://db.freebim.at}.<br>
 * <br>
 * Typically you call {@link #getAllLibraries(String, String)} first to get all
 * {@link Library} instances. Afterwards you can call
 * {@link #getChildsOf(String, String, String)} recursively to build the
 * hierarchy of all {@link Component}s.
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@Service("freebimWebserviceEndpoint")
@WebService(name = "FreebimWebservice", targetNamespace = FreebimWebserviceEndpoint.TNS)
public class FreebimWebserviceEndpoint extends SpringBeanAutowiringSupport {

	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(FreebimWebserviceEndpoint.class);

	/**
	 * The target name space. <code>http://db.freebim.at/</code>
	 */
	public static final String TNS = "http://db.freebim.at/";

	@Autowired
	private FreebimWebservice freebimWebservice;

	/**
	 * Get all valid {@link Unit} instances from database. <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getAllUnits xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:password>myPassword</gs:password>
	 * </gs:getAllUnits>
	 * }
	 * </pre>
	 * 
	 * @param username
	 * @param password
	 * @return All valid {@link Unit} objects.<br>
	 *         <br>
	 *         Example response:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getAllUnitsResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<Unit>
	 * 		<freebimId>553b86e7-4868-48fd-8c73-710d39580f4e</freebimId>
	 * 		<code>m³</code>
	 * 		<conversions>
	 * 			<freebimId>9f76878e-9aec-48e8-877b-3805c1476aaa</freebimId>
	 * 			<q>1000.0</q>
	 * 		</conversions>
	 * 		<desc>Maßeinheit für das Volumen</desc>
	 * 		<descEn>SI derived unit of volume</descEn>
	 * 		<name>Kubikmeter</name>
	 * 		<nameEn>cubic metre</nameEn>
	 * 	</Unit>
	 * 	<Unit>
	 * 		<freebimId>746f727c-0770-4532-9126-bb0996025fa4</freebimId>
	 * 		<code>dB</code>
	 * 		<desc>Logarithmische Hilfsmaßeinheit zur Kennzeichnung von Pegeln und
	 * 			Maßen</desc>
	 * 		<descEn>logarithmic unit used to express the ratio between two values
	 * 			of a physical quantity, often power or intensity</descEn>
	 * 		<name>Dezibel</name>
	 * 		<nameEn>decibel</nameEn>
	 * 	</Unit>
	 * ...
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "Unit")
	public List<Unit> getAllUnits(@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password) {
		logger.info("getUnits ...");
		return this.freebimWebservice.getAllUnits(username, password);
	}

	/**
	 * Get all valid {@link Phase} instances from database. <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getAllPhases xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:password>myPassword</gs:password>
	 * </gs:getAllPhases>
	 * }
	 * </pre>
	 * 
	 * @param username
	 * @param password
	 * @return All valid {@link Phase} objects.<br>
	 *         <br>
	 *         Example response:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getAllPhasesResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<Phase>
	 * 		<freebimId>53dbc64f-11b4-41bd-b16c-8bd68788825e</freebimId>
	 * 		<code>ND</code>
	 * 		<desc>Nicht definiert, oder nicht bekannt</desc>
	 * 		<descEn>not defined or not known</descEn>
	 * 		<hexColor>ff00ff</hexColor>
	 * 		<name>Nicht definiert</name>
	 * 		<nameEn>not defined</nameEn>
	 * 	</Phase>
	 * 	<Phase>
	 * 		<freebimId>4530c920-8c7b-4738-be6a-3f1a899bc25e</freebimId>
	 * 		<code>0</code>
	 * 		<hexColor>f49e24</hexColor>
	 * 		<name>Projektiniiative</name>
	 * 		<nameEn>Strategic Definition</nameEn>
	 * 	</Phase>
	 * ...
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "Phase")
	public List<Phase> getAllPhases(@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password) {
		logger.info("getPhases ...");
		return this.freebimWebservice.getAllPhases(username, password);
	}

	/**
	 * Get all valid {@link Component} instances from database. <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getAllComponents xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:password>myPassword</gs:password>
	 * </gs:getAllComponents>
	 * }
	 * </pre>
	 * 
	 * @param username
	 * @param password
	 * @return All valid {@link Component} objects.<br>
	 *         <br>
	 *         Example response:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getAllComponentsResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<Component>
	 * 		<freebimId>08ce9a8a-e5e5-44b6-8506-f3cbbb86ecbd</freebimId>
	 * 		<bsddGuid>25bK1y_Mr1ZfqKe732iIHc</bsddGuid>
	 * 		<code>14153560</code>
	 * 		<desc>Gips-Spachtelputz</desc>
	 * 		<libraryId>ba3b60f7-56ee-4ff6-ba03-ead377db9984</libraryId>
	 * 		<name>Endbeschichtung Gipsputz</name>
	 * 		<nameEn>gypsum plaster finery</nameEn>
	 * 	</Component>
	 * 	<Component>
	 * 		<freebimId>d051d41d-1ed8-404d-aac4-1daef2876643</freebimId>
	 * 		<code>14153550</code>
	 * 		<desc>Grundierung für Kunstharzputz, Silikatputz, Silikonharzputz,
	 * 			mineralische Endbeschichtung (Voranstrich)</desc>
	 * 		<libraryId>ba3b60f7-56ee-4ff6-ba03-ead377db9984</libraryId>
	 * 		<name>Grundierung Putze</name>
	 * 		<nameEn>plaster primer</nameEn>
	 * 	</Component>
	 * ...
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "Component")
	public List<Component> getAllComponents(
			@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password) {
		logger.info("getComponents ...");
		return this.freebimWebservice.getAllComponents(username, password);
	}

	/**
	 * Get all valid {@link Parameter} instances from database. <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getAllParameters xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:password>myPassword</gs:password>
	 * </gs:getAllParameters>
	 * }
	 * </pre>
	 * 
	 * @param username
	 * @param password
	 * @return All valid {@link Parameter} objects.<br>
	 *         <br>
	 *         Example response:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getAllParametersResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<Parameter>
	 * 		<freebimId>d552c545-33bd-4ca5-a49e-84a6ae3393bb</freebimId>
	 * 		<bsddGuid>1atDtr6bTB4h6afCIM6v_F</bsddGuid>
	 * 		<measureRelations>
	 * 			<freebimId>f8168728-7bfd-450d-9c24-ee49532d18ca</freebimId>
	 * 			<order>0</order>
	 * 		</measureRelations>
	 * 		<name>Beschreibung Montage</name>
	 * 		<nameEn>Description of installation</nameEn>
	 * 		<ptype>UNDEFINED</ptype>
	 * 	</Parameter>
	 * 	<Parameter>
	 * 		<freebimId>f0047d7a-b418-4543-a000-239aee4ffddf</freebimId>
	 * 		<bsddGuid>3sUD6zttf0pgnj_WmPh0wZ</bsddGuid>
	 * 		<defaultString>1</defaultString>
	 * 		<desc>Angabe zum Türblattaußenmaß in der Höhe</desc>
	 * 		<descEn>Specification of the external dimension of the door leaf
	 * 			height</descEn>
	 * 		<disciplineRelations>
	 * 			<freebimId>e13e95c2-dd3b-4dae-9080-14ece47c86d7</freebimId>
	 * 		</disciplineRelations>
	 * 		<measureRelations>
	 * 			<freebimId>c0eb7e3f-2481-4bb0-b09b-b107615e8441</freebimId>
	 * 			<order>0</order>
	 * 		</measureRelations>
	 * 		<name>Höhe Türblatt außen</name>
	 * 		<nameEn>Door leaf height</nameEn>
	 * 		<ptype>TYPE</ptype>
	 * 	</Parameter>
	 * ...
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "Parameter")
	public List<Parameter> getAllParameters(
			@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password) {
		logger.info("getAllParameters ...");
		return this.freebimWebservice.getAllParameters(username, password);
	}

	/**
	 * Get all valid {@link Discipline} instances from database. <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getAllDisciplines xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:password>myPassword</gs:password>
	 * </gs:getAllDisciplines>
	 * }
	 * </pre>
	 * 
	 * @param username
	 * @param password
	 * @return All valid {@link Discipline} objects.<br>
	 *         <br>
	 *         Example response:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getAllDisciplinesResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<Discipline>
	 * 		<freebimId>e13e95c2-dd3b-4dae-9080-14ece47c86d7</freebimId>
	 * 		<code>010</code>
	 * 		<name>AR - Architektur</name>
	 * 	</Discipline>
	 * 	<Discipline>
	 * 		<freebimId>8e94729d-2882-447f-8f9d-f94a36e6b63b</freebimId>
	 * 		<code>011</code>
	 * 		<name>AA - Außenanlagen</name>
	 * 	</Discipline>
	 * 	<Discipline>
	 * 		<freebimId>74f88f96-d490-44d6-9012-ea754e5f5d8c</freebimId>
	 * 		<code>000</code>
	 * 		<name>AG - Auftraggeber, Bauherr</name>
	 * 	</Discipline>
	 * ...
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "Discipline")
	public List<Discipline> getAllDisciplines(
			@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password) {
		logger.info("getDisciplines ...");
		return this.freebimWebservice.getAllDisciplines(username, password);
	}

	/**
	 * Get all valid {@link DataType} instances from database. <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getAllDataTypes xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:password>myPassword</gs:password>
	 * </gs:getAllDataTypes>
	 * }
	 * </pre>
	 * 
	 * @param username
	 * @param password
	 * @return All valid {@link DataType} objects.<br>
	 *         <br>
	 *         Example response:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getAllDataTypesResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<DataType>
	 * 		<freebimId>295709cc-eeb3-4457-91ab-469572095260</freebimId>
	 * 		<name>Freier Text</name>
	 * 		<nameEn>Free Text</nameEn>
	 * 	</DataType>
	 * 	<DataType>
	 * 		<freebimId>86b5c938-8b9b-4f37-a67e-bce41ec6faa3</freebimId>
	 * 		<desc>z.B. 12345</desc>
	 * 		<descEn>e.g. 12345</descEn>
	 * 		<name>positive Ganzzahl</name>
	 * 		<nameEn>positive integer</nameEn>
	 * 		<regExp>^[+]?[0-9]+$</regExp>
	 * 	</DataType>
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "DataType")
	public List<DataType> getAllDataTypes(
			@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password) {
		logger.info("getDataTypes ...");
		return this.freebimWebservice.getAllDataTypes(username, password);
	}

	/**
	 * Get all valid {@link ParameterSet} instances from database. <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getAllParameterSets xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:password>myPassword</gs:password>
	 * </gs:getAllParameterSets>
	 * }
	 * </pre>
	 * 
	 * @param username
	 * @param password
	 * @return All valid {@link ParameterSet} objects.<br>
	 *         <br>
	 *         Example response:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getAllParameterSetsResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<ParameterSet>
	 * 		<freebimId>257d8eab-6d7d-4382-ae13-a86e76412c4f</freebimId>
	 * 		<bsddGuid>2VWFE0qXKHuO00025QrE$V</bsddGuid>
	 * 		<ifcPropertySet>true</ifcPropertySet>
	 * 		<name>Pset_WallCommon</name>
	 * 		<nameEn>wall common</nameEn>
	 * 		<parameters>
	 * 			<freebimId>a6a35355-c3a1-4f9a-bffd-ae9c4501ff5b</freebimId>
	 * 			<order>2</order>
	 * 		</parameters>
	 * 		<parameters>
	 * 			<freebimId>a7074d2c-4e07-4e72-889f-aa387fa9f97e</freebimId>
	 * 			<order>3</order>
	 * 		</parameters>
	 * 		...
	 * 		<type>PROPERTYSET</type>
	 * 	</ParameterSet>
	 * 	...
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "ParameterSet")
	public List<ParameterSet> getAllParameterSets(
			@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password) {
		logger.info("getAllParameterSets ...");
		return this.freebimWebservice.getAllParameterSets(username, password);
	}

	/**
	 * Get a specified {@link Component} from database. <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getComponent xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:password>myPassword</gs:password>
	 *     <gs:freebimId>69d359c3-bd1a-4484-9012-9f96f75e5a22</gs:freebimId>
	 * </gs:getComponent>
	 * }
	 * </pre>
	 * 
	 * @param username
	 * @param password
	 * @param freebimId Unique freeBIM id (guid) of Component to get.
	 * @return The specified {@link Component}.<br>
	 *         <br>
	 *         Example response:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getComponentResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<Component>
	 * 		<freebimId>69d359c3-bd1a-4484-9012-9f96f75e5a22</freebimId>
	 * 		<bsddGuid>2AFYKXkEn7RxYuvbduGfUV</bsddGuid>
	 * 		<code>ABS_011</code>
	 * 		<desc>Obergruppe aller Gebäude Elemente wie Wand, Tür, Stütze etc.
	 * 		</desc>
	 * 		<libraryId>87cccb4c-53ae-4df9-8461-78fb01f22f0c</libraryId>
	 * 		<name>Gebäude-Element</name>
	 * 	</Component>
	 * </ns2:getComponentResponse>
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "Component")
	public Component getComponent(@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password,
			@WebParam(name = "freebimId", mode = Mode.IN, targetNamespace = TNS) String freebimId) {
		logger.info("getComponent freebimId=[{}] ...", freebimId);
		return this.freebimWebservice.getComponent(username, password, freebimId);
	}

	/**
	 * Get a specified {@link Parameter} from database. <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getParameter xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:username>myPassword</gs:username>
	 *     <gs:freebimId>383f5bdd-8224-4bcc-b59f-0bd85767c781</gs:freebimId>
	 *     <gs:fetch>true</gs:fetch>
	 * </gs:getParameter>
	 * }
	 * </pre>
	 * 
	 * @param username
	 * @param password
	 * @param freebimId Unique freeBIM id (guid) of Parameter to get.
	 * @param fetch     If set to <code>true</code>, related Nodes will be fetched
	 *                  too. If set to <code>false</code> only relations will be
	 *                  returned.
	 * @return The specified {@link Parameter}.<br>
	 *         <br>
	 *         Example response if <code>fetch</code> is set to <code>false</code>:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getParameterResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<Parameter>
	 * 		<freebimId>383f5bdd-8224-4bcc-b59f-0bd85767c781</freebimId>
	 * 		<bsddGuid>3vHkSGoT0Hsm00051Mm008</bsddGuid>
	 * 		<defaultString>ND</defaultString>
	 * 		<desc>auch U-Wert; gibt die Energiemenge an, die pro Sekunde durch
	 * 			eine Fläche von 1 m² fließt, wenn sich die beidseitig anliegenden
	 * 			Lufttemperaturen stationär um 1 K unterscheiden; Kehrwert des
	 * 			Wärmedurchgangswiderstands.</desc>
	 * 		<descEn>also known as U-value; is the rate of transfer of heat per
	 * 			second through one square metre of a structure divided by the
	 * 			difference in temperature across the structure; is the reciprocal of
	 * 			the total thermal resistance.</descEn>
	 * 		<disciplineRelations>
	 * 			<freebimId>e13e95c2-dd3b-4dae-9080-14ece47c86d7</freebimId>
	 * 		</disciplineRelations>
	 * 		<measureRelations>
	 * 			<freebimId>a1dee01a-75db-481f-a0ac-0f766b90005b</freebimId>
	 * 			<order>0</order>
	 * 		</measureRelations>
	 * 		<name>Wärmedurchgangskoeffizient</name>
	 * 		<nameEn>Thermal transmittance</nameEn>
	 * 		<ptype>TYPE</ptype>
	 * 	</Parameter>
	 * </ns2:getParameterResponse>
	 * }
	 * </pre>
	 * 
	 * <br>
	 * Example response if <code>fetch</code> is set to <code>true</code>:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getParameterResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<Parameter>
	 * 		<freebimId>383f5bdd-8224-4bcc-b59f-0bd85767c781</freebimId>
	 * 		<bsddGuid>3vHkSGoT0Hsm00051Mm008</bsddGuid>
	 * 		<defaultString>ND</defaultString>
	 * 		<desc>auch U-Wert; gibt die Energiemenge an, die pro Sekunde durch
	 * 			eine Fläche von 1 m² fließt, wenn sich die beidseitig anliegenden
	 * 			Lufttemperaturen stationär um 1 K unterscheiden; Kehrwert des
	 * 			Wärmedurchgangswiderstands.</desc>
	 * 		<descEn>also known as U-value; is the rate of transfer of heat per
	 * 			second through one square metre of a structure divided by the
	 * 			difference in temperature across the structure; is the reciprocal of
	 * 			the total thermal resistance.</descEn>
	 * 		<disciplines>
	 * 			<freebimId>e13e95c2-dd3b-4dae-9080-14ece47c86d7</freebimId>
	 * 			<code>010</code>
	 * 			<name>AR - Architektur</name>
	 * 		</disciplines>
	 * 		<measures>
	 * 			<freebimId>a1dee01a-75db-481f-a0ac-0f766b90005b</freebimId>
	 * 			<dataType>
	 * 				<freebimId>d50b8c9d-ee0b-4dc9-9d4b-729dfeb1fdce</freebimId>
	 * 				<desc>z.B. +/- 123.45</desc>
	 * 				<descEn>e.g. +/- 123.45</descEn>
	 * 				<name>Reelle Zahl</name>
	 * 				<nameEn>Real number</nameEn>
	 * 			</dataType>
	 * 			<name>Reelle Zahl [W/m²K]</name>
	 * 			<nameEn>real number [W/m²K]</nameEn>
	 * 			<unit>
	 * 				<freebimId>99fa0315-496f-4c33-812c-5b6816c70f28</freebimId>
	 * 				<code>W/(m²K)</code>
	 * 				<desc>Einheit des Wärmedurchgangskoeffizienten</desc>
	 * 				<descEn>unit for the thermal transmittance</descEn>
	 * 				<name>Watt pro Quadratmeter Kelvin</name>
	 * 				<nameEn>Watt per squaremeter kelvin</nameEn>
	 * 			</unit>
	 * 		</measures>
	 * 		<name>Wärmedurchgangskoeffizient</name>
	 * 		<nameEn>Thermal transmittance</nameEn>
	 * 		<ptype>TYPE</ptype>
	 * 	</Parameter>
	 * </ns2:getParameterResponse>
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "Parameter")
	public Parameter getParameter(@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password,
			@WebParam(name = "freebimId", mode = Mode.IN, targetNamespace = TNS) String freebimId,
			@WebParam(name = "fetch", mode = Mode.IN, targetNamespace = TNS) boolean fetch) {
		logger.info("getParameter freebimId=[{}] ...", freebimId);
		return this.freebimWebservice.getParameter(username, password, freebimId, fetch);
	}

	/**
	 * Get a specified <code>ValueList</code> from database. <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getValueList xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:username>myPassword</gs:username>
	 *     <gs:freebimId>8b7aec5b-3098-4b23-91d5-aa7746bce4f9</gs:freebimId>
	 *     <gs:fetch>false</gs:fetch>
	 * </gs:getValueList>
	 * }
	 * </pre>
	 * 
	 * @param username
	 * @param password
	 * @param freebimId Unique freeBIM id (guid) of ValueList to get.
	 * @param fetch     If set to <code>true</code>, related ValueListEntries will
	 *                  be fetched too, the entries have to be loaded with a
	 *                  separate call to {@link #getValuesOf} otherwise.
	 * @return The specified ValueList.<br>
	 *         <br>
	 *         Example response if <code>fetch</code> is set to <code>false</code>:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getValueListResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<ValueList>
	 * 		<freebimId>8b7aec5b-3098-4b23-91d5-aa7746bce4f9</freebimId>
	 * 		<name>Allgemein:Himmelsrichtung</name>
	 * 		<valueListEntries>
	 * 			<freebimId>11f6a558-7603-45b5-b918-8fb739656bd6</freebimId>
	 * 			<order>0</order>
	 * 		</valueListEntries>
	 * 		<valueListEntries>
	 * 			<freebimId>a5ac862e-0b29-4966-99d1-1657c7fb3390</freebimId>
	 * 			<order>16</order>
	 * 		</valueListEntries>
	 * 		<valueListEntries>
	 * 			<freebimId>f8ce5cc3-d2ac-4336-b173-fe4d37d44a26</freebimId>
	 * 			<order>15</order>
	 * 		</valueListEntries>
	 * 		...
	 * 	</ValueList>
	 * </ns2:getValueListResponse>
	 * }
	 * </pre>
	 * 
	 * <br>
	 * Example response if <code>fetch</code> is set to <code>true</code>:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getValueListResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<ValueList>
	 * 		<freebimId>8b7aec5b-3098-4b23-91d5-aa7746bce4f9</freebimId>
	 * 		<entries>
	 * 			<freebimId>11f6a558-7603-45b5-b918-8fb739656bd6</freebimId>
	 * 			<desc>nicht festgelegt oder nicht bekannt</desc>
	 * 			<descEn>not defined</descEn>
	 * 			<name>ND</name>
	 * 			<nameEn>ND</nameEn>
	 * 		</entries>
	 * 		<entries>
	 * 			<freebimId>77f76a42-fd76-4b41-bf7d-10d2bf849c5b</freebimId>
	 * 			<desc>Norden</desc>
	 * 			<name>N</name>
	 * 		</entries>
	 * 		<entries>
	 * 			<freebimId>ab46a210-1b6d-45af-bd7a-12de1fa16517</freebimId>
	 * 			<desc>Nord - Nordost</desc>
	 * 			<name>NNO</name>
	 * 		</entries>
	 * 		...
	 * 		<name>Allgemein:Himmelsrichtung</name>
	 * 	</ValueList>
	 * </ns2:getValueListResponse>
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "ValueList")
	public ValueList getValueList(@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password,
			@WebParam(name = "freebimId", mode = Mode.IN, targetNamespace = TNS) String freebimId,
			@WebParam(name = "fetch", mode = Mode.IN, targetNamespace = TNS) boolean fetch) {
		logger.info("getValueList freebimId=[{}] ...", freebimId);
		return this.freebimWebservice.getValueList(username, password, freebimId, fetch);
	}

	/**
	 * Get all {@link Library} instances from database. <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getAllLibraries xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:password>myPassword</gs:password>
	 * </gs:getAllLibraries>
	 * }
	 * </pre>
	 * 
	 * @param username
	 * @param password
	 * @return All valid {@link Library} objects.<br>
	 *         <br>
	 *         Example response:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getAllLibrariesResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<Library>
	 * 		<freebimId>aaf83605-ee98-47ce-ac39-4819906d6c42</freebimId>
	 * 		<desc>Ifc4 library</desc>
	 * 		<languageCode>de-AT</languageCode>
	 * 		<lastUpdate>2014-10-26T13:47:46Z</lastUpdate>
	 * 		<name>Ifc4</name>
	 * 		<URL>http://www.buildingsmart.org</URL>
	 * 	</Library>
	 * 	<Library>
	 * 		<freebimId>87cccb4c-53ae-4df9-8461-78fb01f22f0c</freebimId>
	 * 		<desc>freeBIM Tirol</desc>
	 * 		<languageCode>de-AT</languageCode>
	 * 		<lastUpdate>2014-10-26T13:47:48Z</lastUpdate>
	 * 		<name>freeBIM</name>
	 * 		<URL>http://www.freebim.at</URL>
	 * 	</Library>
	 * ...
	 *	}
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "Library")
	public List<Library> getAllLibraries(
			@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password) {
		logger.info("getAllLibraries ...");
		List<Library> res = this.freebimWebservice.getAllLibraries(username, password);
		for (Library lib : res) {
			logger.debug("library [{}]", lib.getFreebimId());
		}
		logger.info("getAllLibraries: returning [{}] libraries.", res.size());
		return res;
	}

	/**
	 * Get related children of a specified {@link Component} or {@link Library}.
	 * <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getChildsOf xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:password>myPassword</gs:password>
	 *     <gs:freebimId>87cccb4c-53ae-4df9-8461-78fb01f22f0c</gs:freebimId>
	 * </gs:getChildsOf>
	 * }
	 * </pre>
	 * 
	 * @param username
	 * @param password
	 * @param freebimId Unique freeBIM-ID (guid) of parent to get related children
	 *                  for.
	 * @return freeBIM-ID and order of child {@link Component} instances.<br>
	 *         <br>
	 *         Example response:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getChildsOfResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<ChildRelation>
	 * 		<freebimId>47535869-2c2a-4907-ad7b-5ef13f51d7b7</freebimId>
	 * 		<order>0</order>
	 * 	</ChildRelation>
	 * 	<ChildRelation>
	 * 		<freebimId>b11c7d45-c14a-420d-9340-d125e612aa56</freebimId>
	 * 		<order>1</order>
	 * 	</ChildRelation>
	 * </ns2:getChildsOfResponse>
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "ChildRelation")
	public List<OrderedRel> getChildsOf(
			@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password,
			@WebParam(name = "freebimId", mode = Mode.IN, targetNamespace = TNS) String freebimId) {
		logger.info("getChildsOf freebimId=[{}] ...", freebimId);
		return this.freebimWebservice.getChildsOf(username, password, freebimId);
	}

	/**
	 * Get related {@link Parameter}'s of a specified <code>Component</code>. <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getParameterOf xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:username>myPassword</gs:username>
	 *     <gs:freebimId>69d359c3-bd1a-4484-9012-9f96f75e5a22</gs:freebimId>
	 * </gs:getParameterOf>
	 * }
	 * </pre>
	 * 
	 * @param username
	 * @param password
	 * @param freebimId Unique freeBIM-ID (guid) of component to get related
	 *                  {@link Parameter} for.
	 * @return freeBIM-ID, freeBIM-ID of Phase and order of parameters.<br>
	 *         <br>
	 *         Example response:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getParameterOfResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<ParameterRelation>
	 * 		<freebimId>60f5a6ab-eba4-4e8b-a51a-226a72a251a8</freebimId>
	 * 		<order>0</order>
	 * 		<phase>92621f37-8fbc-4979-a1f7-91c048ebb1c2</phase>
	 * 	</ParameterRelation>
	 * 	<ParameterRelation>
	 * 		<freebimId>aa3c26dc-c966-4fcc-9f24-9fe1a26c3c70</freebimId>
	 * 		<order>1</order>
	 * 		<phase>92621f37-8fbc-4979-a1f7-91c048ebb1c2</phase>
	 * 	</ParameterRelation>
	 * 	<ParameterRelation>
	 * 		<freebimId>344bbdb3-7608-4ec9-af4b-7e1416db8ccd</freebimId>
	 * 		<order>2</order>
	 * 		<phase>92621f37-8fbc-4979-a1f7-91c048ebb1c2</phase>
	 * 	</ParameterRelation>
	 * ...
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "ParameterRelation")
	public List<ParameterRel> getParameterOf(
			@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password,
			@WebParam(name = "freebimId", mode = Mode.IN, targetNamespace = TNS) String freebimId) {
		logger.info("getParameterOf freebimId=[{}] ...", freebimId);
		return this.freebimWebservice.getParameterOf(username, password, freebimId);
	}

	/**
	 * Get related {@link Parameter}'s of a specified <code>ParameterSet</code>.
	 * <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getParameterOfPSet xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:username>myPassword</gs:username>
	 *     <gs:freebimId>257d8eab-6d7d-4382-ae13-a86e76412c4f</gs:freebimId>
	 * </gs:getParameterOfPSet>
	 * }
	 * </pre>
	 * 
	 * @param username
	 * @param password
	 * @param freebimId Unique freeBIM-ID (guid) of parameter-set to get related
	 *                  {@link Parameter} for.
	 * @return freeBIM-ID and order of parameters.<br>
	 *         <br>
	 *         Example response:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getParameterOfPSetResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<ParameterRelation>
	 * 		<freebimId>68b32196-e9ad-4c79-9005-221decaa3916</freebimId>
	 * 		<order>0</order>
	 * 	</ParameterRelation>
	 * 	<ParameterRelation>
	 * 		<freebimId>6f2f93b8-615e-40d8-91c2-2ddcd67fc2f7</freebimId>
	 * 		<order>0</order>
	 * 	</ParameterRelation>
	 * ...
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "ParameterRelation")
	public List<OrderedRel> getParameterOfPSet(
			@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password,
			@WebParam(name = "freebimId", mode = Mode.IN, targetNamespace = TNS) String freebimId) {
		logger.info("getParameterOfPSet freebimId=[{}] ...", freebimId);
		return this.freebimWebservice.getParameterOfPSet(username, password, freebimId);
	}

	/**
	 * Get related <code>ParameterSet</code>'s of a specified
	 * <code>Component</code>. <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getParameterSetOf xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:username>myPassword</gs:username>
	 *     <gs:freebimId>d3de6f45-2c74-4e24-9bef-7ba828428714</gs:freebimId>
	 * </gs:getParameterSetOf>
	 * }
	 * </pre>
	 * 
	 * @param username
	 * @param password
	 * @param freebimId Unique freeBIM-ID (guid) of component to get related
	 *                  <code>ParameterSet</code>'s for.
	 * @return freeBIM-ID of parameter sets.<br>
	 *         <br>
	 *         Example response:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getParameterSetOfResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<ParameterSetRelation>
	 * 		<freebimId>257d8eab-6d7d-4382-ae13-a86e76412c4f</freebimId>
	 * 	</ParameterSetRelation>
	 * </ns2:getParameterSetOfResponse>
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "ParameterSetRelation")
	public List<Rel> getParameterSetOf(
			@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password,
			@WebParam(name = "freebimId", mode = Mode.IN, targetNamespace = TNS) String freebimId) {
		logger.info("getParameterSetOf freebimId=[{}] ...", freebimId);
		return this.freebimWebservice.getParameterSetOf(username, password, freebimId);
	}

	/**
	 * Get equal objects of a specified element. <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getEqualsOf xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:password>myPassword</gs:password>
	 *     <gs:freebimId>69d359c3-bd1a-4484-9012-9f96f75e5a22</gs:freebimId>
	 * </gs:getEqualsOf>
	 * }
	 * </pre>
	 * 
	 * @param username
	 * @param password
	 * @param freebimId Unique freeBIM-ID (guid) of object to get equal objects for.
	 * @return List of freeBIM-ID and equality (from 0. to 1.) of equal objects.<br>
	 *         <br>
	 *         Example response:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getEqualsOfResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<EqualRelation>
	 * 		<freebimId>81f1a129-53ed-44ff-9bc6-604021cd0209</freebimId>
	 * 		<q>1.0</q>
	 * 	</EqualRelation>
	 * </ns2:getEqualsOfResponse>
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "EqualRelation")
	public List<QualifiedRel> getEqualsOf(
			@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password,
			@WebParam(name = "freebimId", mode = Mode.IN, targetNamespace = TNS) String freebimId) {
		logger.info("getEqualsOf freebimId=[{}] ...", freebimId);
		return this.freebimWebservice.getEqualsOf(username, password, freebimId);
	}

	/**
	 * Get related parts of a specified <code>Component</code>. <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getPartsOf xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:username>myPassword</gs:username>
	 *     <gs:freebimId>b567ab5b-3ecc-4633-b6ba-8f968dac73a8</gs:freebimId>
	 * </gs:getPartsOf>
	 * }
	 * </pre>
	 * 
	 * @param username
	 * @param password
	 * @param freebimId Unique freeBIM-ID (guid) of component to get parts for.
	 * @return List of freeBIM-ID's of parts.<br>
	 *         <br>
	 *         Example response:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getPartsOfResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<PartsRelation>
	 * 		<freebimId>d2d8c44c-df94-4d25-ab51-817f5d53db21</freebimId>
	 * 	</PartsRelation>
	 * </ns2:getPartsOfResponse>
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "PartsRelation")
	public List<Rel> getPartsOf(@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password,
			@WebParam(name = "freebimId", mode = Mode.IN, targetNamespace = TNS) String freebimId) {
		logger.info("getPartsOf freebimId=[{}] ...", freebimId);
		return this.freebimWebservice.getPartsOf(username, password, freebimId);
	}

	/**
	 * Get related material of a specified <code>Component</code>. <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getMaterialOf xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:password>myPassword</gs:password>
	 *     <gs:freebimId>f29f7831-d89c-4e14-bff7-b2f0b9c3fa70</gs:freebimId>
	 * </gs:getMaterialOf>
	 * }
	 * </pre>
	 * 
	 * @param username
	 * @param password
	 * @param freebimId Unique freeBIM-ID (guid) of component to get materials for.
	 * @return List of freeBIM-ID's of materials.<br>
	 *         <br>
	 *         Example response:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getMaterialOfResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<MaterialRelation>
	 * 		<freebimId>148678c9-25ba-44c4-ac45-d122ccf2312b</freebimId>
	 * 	</MaterialRelation>
	 * </ns2:getMaterialOfResponse>
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "MaterialRelation")
	public List<Rel> getMaterialOf(@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password,
			@WebParam(name = "freebimId", mode = Mode.IN, targetNamespace = TNS) String freebimId) {
		logger.info("getMaterialOf freebimId=[{}] ...", freebimId);
		return this.freebimWebservice.getMaterialOf(username, password, freebimId);
	}

	/**
	 * Get <code>Measure</code>'s for a specified <code>Parameter</code>. <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getMeasuresOf xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:username>myPassword</gs:username>
	 *     <gs:freebimId>383f5bdd-8224-4bcc-b59f-0bd85767c781</gs:freebimId>
	 * </gs:getMeasuresOf>
	 * }
	 * </pre>
	 * 
	 * @param username
	 * @param password
	 * @param freebimId Unique freeBIM-ID (guid) of parameter to get measures for.
	 * @return List of freeBIM-ID's of measure and order.<br>
	 *         <br>
	 *         Example response:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getMeasuresOfResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<MeasureRelation>
	 * 		<freebimId>a1dee01a-75db-481f-a0ac-0f766b90005b</freebimId>
	 * 		<order>0</order>
	 * 	</MeasureRelation>
	 * </ns2:getMeasuresOfResponse>
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "MeasureRelation")
	public List<OrderedRel> getMeasuresOf(
			@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password,
			@WebParam(name = "freebimId", mode = Mode.IN, targetNamespace = TNS) String freebimId) {
		logger.info("getMeasuresOf freebimId=[{}] ...", freebimId);
		return this.freebimWebservice.getMeasuresOf(username, password, freebimId);
	}

	/**
	 * Get a specified <code>Measure</code>. <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getMeasure xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:password>myPassword</gs:password>
	 *     <gs:freebimId>a1dee01a-75db-481f-a0ac-0f766b90005b</gs:freebimId>
	 *     <gs:fetch>false</gs:fetch>
	 * </gs:getMeasure>
	 * }
	 * </pre>
	 * 
	 * @param username
	 * @param password
	 * @param freebimId Unique freeBIM-ID (guid) of measure to get.
	 * @param fetch     If set to <code>true</code>, Unit, DataType and ValueList
	 *                  will be fetched too.
	 * @return The specified Measure.<br>
	 *         <br>
	 *         Example response if <code>fetch</code> is set to <code>false</code>:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getMeasureResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<Measure>
	 * 		<freebimId>a1dee01a-75db-481f-a0ac-0f766b90005b</freebimId>
	 * 		<dataTypeRelation>
	 * 			<freebimId>d50b8c9d-ee0b-4dc9-9d4b-729dfeb1fdce</freebimId>
	 * 		</dataTypeRelation>
	 * 		<name>Reelle Zahl [W/m²K]</name>
	 * 		<nameEn>real number [W/m²K]</nameEn>
	 * 		<unitRelation>
	 * 			<freebimId>99fa0315-496f-4c33-812c-5b6816c70f28</freebimId>
	 * 		</unitRelation>
	 * 	</Measure>
	 * </ns2:getMeasureResponse>
	 * }
	 * </pre>
	 * 
	 * <br>
	 * Example response if <code>fetch</code> is set to <code>true</code>:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getMeasureResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<Measure>
	 * 		<freebimId>a1dee01a-75db-481f-a0ac-0f766b90005b</freebimId>
	 * 		<dataType>
	 * 			<freebimId>d50b8c9d-ee0b-4dc9-9d4b-729dfeb1fdce</freebimId>
	 * 			<desc>z.B. +/- 123.45</desc>
	 * 			<descEn>e.g. +/- 123.45</descEn>
	 * 			<name>Reelle Zahl</name>
	 * 			<nameEn>Real number</nameEn>
	 * 		</dataType>
	 * 		<name>Reelle Zahl [W/m²K]</name>
	 * 		<nameEn>real number [W/m²K]</nameEn>
	 * 		<unit>
	 * 			<freebimId>99fa0315-496f-4c33-812c-5b6816c70f28</freebimId>
	 * 			<code>W/(m²K)</code>
	 * 			<desc>Einheit des Wärmedurchgangskoeffizienten</desc>
	 * 			<descEn>unit for the thermal transmittance</descEn>
	 * 			<name>Watt pro Quadratmeter Kelvin</name>
	 * 			<nameEn>Watt per squaremeter kelvin</nameEn>
	 * 		</unit>
	 * 	</Measure>
	 * </ns2:getMeasureResponse>
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "Measure")
	public Measure getMeasure(@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password,
			@WebParam(name = "freebimId", mode = Mode.IN, targetNamespace = TNS) String freebimId,
			@WebParam(name = "fetch", mode = Mode.IN, targetNamespace = TNS) boolean fetch) {
		logger.info("getMeasuresOf freebimId=[{}] ...", freebimId);
		return this.freebimWebservice.getMeasure(username, password, freebimId, fetch);
	}

	/**
	 * Get relation to {@link Unit} of a specified {@link Measure}. Returned
	 * <code>freeBIM-ID</code> can be passed to
	 * {@link #getByFreebimId(String, String, String)} or use
	 * {@link #getAllUnits(String, String)} to fetch all {@link Unit} instances at
	 * once. <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getUnitOf xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:username>myPassword</gs:username>
	 *     <gs:freebimId>a1dee01a-75db-481f-a0ac-0f766b90005b</gs:freebimId>
	 * </gs:getUnitOf>
	 * }
	 * </pre>
	 * 
	 * @param username
	 * @param password
	 * @param freebimId Unique freeBIM-ID (guid) of measure to get unit for.
	 * @return freeBIM-ID of unit.<br>
	 *         <br>
	 *         Example response:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getUnitOfResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<UnitRelation>
	 * 		<freebimId>99fa0315-496f-4c33-812c-5b6816c70f28</freebimId>
	 * 	</UnitRelation>
	 * </ns2:getUnitOfResponse>
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "UnitRelation")
	public Rel getUnitOf(@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password,
			@WebParam(name = "freebimId", mode = Mode.IN, targetNamespace = TNS) String freebimId) {
		logger.info("getUnitOf freebimId=[{}] ...", freebimId);
		return this.freebimWebservice.getUnitOf(username, password, freebimId);
	}

	/**
	 * Get relation to {@link DataType} of a specified {@link Measure}. <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getDataTypeOf xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:password>myPassword</gs:password>
	 *     <gs:freebimId>a1dee01a-75db-481f-a0ac-0f766b90005b</gs:freebimId>
	 * </gs:getDataTypeOf>
	 * }
	 * </pre>
	 * 
	 * @param username
	 * @param password
	 * @param freebimId Unique freeBIM-ID (guid) of measure to get DataType relation
	 *                  for.
	 * @return freeBIM-ID of DataType.<br>
	 *         <br>
	 *         Example response:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getDataTypeOfResponse xmlns:ns2="http://db.freebim.at/">
	 * <DataTypeRelation>
	 *     <freebimId>d50b8c9d-ee0b-4dc9-9d4b-729dfeb1fdce</freebimId>
	 * </DataTypeRelation>
	 * </ns2:getDataTypeOfResponse>
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "DataTypeRelation")
	public Rel getDataTypeOf(@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password,
			@WebParam(name = "freebimId", mode = Mode.IN, targetNamespace = TNS) String freebimId) {
		logger.info("getDataTypeOf freebimId=[{}] ...", freebimId);
		return this.freebimWebservice.getDataTypeOf(username, password, freebimId);
	}

	/**
	 * Get all {@link ValueList} relations of a specified {@link Measure}. <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getValueListsOf xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:username>myPassword</gs:username>
	 *     <gs:freebimId>90fb2e54-71b0-48b6-a5d2-d22f09575588</gs:freebimId>
	 * </gs:getValueListsOf>
	 * }
	 * </pre>
	 * 
	 * @param username
	 * @param password
	 * @param freebimId Unique freeBIM-ID (guid) of measure to get value list for.
	 * @return All value list relations.<br>
	 *         <br>
	 *         Example response:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getValueListsOfResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<ValueListRelation>
	 * 		<freebimId>40d7d1d5-fe1a-49cd-a756-57cb35735ae6</freebimId>
	 * 		<component>10591a70-8b3e-40a7-ab62-b797ca6ee05b</component>
	 * 	</ValueListRelation>
	 * 	<ValueListRelation>
	 * 		<freebimId>7dcacecf-2dc7-4f8b-97ea-68934d0e2010</freebimId>
	 * 		<component>de7b0a84-102b-4dad-ab09-6d2a7e6f25f3</component>
	 * 	</ValueListRelation>
	 * 	<ValueListRelation>
	 * 		<freebimId>c9a81368-98b8-47c7-a6a4-169d8c87d343</freebimId>
	 * 		<component>91b00397-e510-491c-a219-0e0cd57901bc</component>
	 * 	</ValueListRelation>
	 * 	...
	 * </ns2:getValueListsOfResponse>
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "ValueListRelation")
	public List<ValueListRel> getValueListsOf(
			@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password,
			@WebParam(name = "freebimId", mode = Mode.IN, targetNamespace = TNS) String freebimId) {
		logger.info("getValueListOf freebimId=[{}] ...", freebimId);
		return this.freebimWebservice.getValueListsOf(username, password, freebimId);
	}

	/**
	 * Get {@link ValueList} of a specified {@link Measure} for a specified
	 * {@link Component}. <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getValueListOf xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:username>myPassword</gs:username>
	 *     <gs:freebimId>840e5e8e-9bd9-446e-b385-1a6d44d84f35</gs:freebimId>
	 *     <gs:component>69d359c3-bd1a-4484-9012-9f96f75e5a22</gs:component>
	 *     <gs:fetch>false</gs:fetch>
	 * </gs:getValueListOf>
	 * }
	 * </pre>
	 * 
	 * @param username
	 * @param password
	 * @param measure   Unique freeBIM-ID (guid) of measure to get value list for.
	 * @param component Unique freeBIM-ID (guid) of component to get value list for.
	 * @param fetch     If set to <code>true</code>, related Nodes will be fetched
	 *                  too. If set to <code>false</code> only relations will be
	 *                  returned.
	 * @return The value list.<br>
	 *         <br>
	 *         Example response:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getValueListOfResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<ValueList>
	 * 		<freebimId>8b7aec5b-3098-4b23-91d5-aa7746bce4f9</freebimId>
	 * 		<name>Allgemein:Himmelsrichtung</name>
	 * 		<valueListEntries>
	 * 			<freebimId>11f6a558-7603-45b5-b918-8fb739656bd6</freebimId>
	 * 			<order>0</order>
	 * 		</valueListEntries>
	 * 		<valueListEntries>
	 * 			<freebimId>a5ac862e-0b29-4966-99d1-1657c7fb3390</freebimId>
	 * 			<order>16</order>
	 * 		</valueListEntries>
	 * 		<valueListEntries>
	 * 			<freebimId>f8ce5cc3-d2ac-4336-b173-fe4d37d44a26</freebimId>
	 * 			<order>15</order>
	 * 		</valueListEntries>
	 * 		...
	 * 	</ValueList>
	 * </ns2:getValueListOfResponse>
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "ValueList")
	public ValueList getValueListOf(@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password,
			@WebParam(name = "freebimId", mode = Mode.IN, targetNamespace = TNS) String measure,
			@WebParam(name = "component", mode = Mode.IN, targetNamespace = TNS) String component,
			@WebParam(name = "fetch", mode = Mode.IN, targetNamespace = TNS) boolean fetch) {
		logger.info("getValueListOf freebimId=[{}] ...", measure);
		return this.freebimWebservice.getValueListOf(username, password, measure, component, fetch);
	}

	/**
	 * Get {@link ValueListEntry} instances of a specified {@link ValueList}. <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getValuesOf xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:username>myPassword</gs:username>
	 *     <gs:freebimId>c9a81368-98b8-47c7-a6a4-169d8c87d343</gs:freebimId>
	 * </gs:getValuesOf>
	 * }
	 * </pre>
	 * 
	 * @param username
	 * @param password
	 * @param freebimId Unique freeBIM-ID (guid) of value list to get entries for.
	 * @return List of freeBIM-ID and order of entries.<br>
	 *         <br>
	 *         Example response:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getValuesOfResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<ValueRelation>
	 * 		<freebimId>1b37d75d-6d0b-49a8-a913-c491ffe75eee</freebimId>
	 * 		<order>0</order>
	 * 	</ValueRelation>
	 * 	<ValueRelation>
	 * 		<freebimId>97ef2416-00fc-47ac-bafa-543e5698b59b</freebimId>
	 * 		<order>1</order>
	 * 	</ValueRelation>
	 * </ns2:getValuesOfResponse>
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "ValueRelation")
	public List<OrderedRel> getValuesOf(
			@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password,
			@WebParam(name = "freebimId", mode = Mode.IN, targetNamespace = TNS) String freebimId) {
		logger.info("getValuesOf freebimId=[{}] ...", freebimId);
		return this.freebimWebservice.getValuesOf(username, password, freebimId);
	}

	/**
	 * Get all refIdName's for a specified {@link Library} from database.<br>
	 * <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getRefIdNames xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:password>myPassword</gs:password>
	 *     <gs:libraryName>freeBIM</gs:libraryName>
	 * </gs:getRefIdNames>
	 * }
	 * </pre>
	 * 
	 * @see #getLibraryReferences(String, String, String, String)
	 * 
	 * @param username
	 * @param password
	 * @param libraryName
	 * @return All refIdName's for the specified {@link Library}.<br>
	 *         <br>
	 *         Example response:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getRefIdNamesResponse xmlns:ns2="http://db.freebim.at/">
	 * 	   <refIdName>parameters.ID</refIdName>
	 * 	   <refIdName>components.ID</refIdName>
	 * 	   <refIdName>value_lists.ID</refIdName>
	 * 	   <refIdName>value_list_defs.ID</refIdName>
	 * 	   <refIdName>disciplines.ID</refIdName>
	 * 	   <refIdName>unit_types.ID</refIdName>
	 * 	   <refIdName>material.ID</refIdName>
	 * 	   <refIdName>data_types.ID</refIdName>
	 * 	   <refIdName>phases.ID</refIdName>
	 * </ns2:getRefIdNamesResponse>
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "refIdName")
	public List<String> getRefIdNames(
			@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password,
			@WebParam(name = "libraryName", mode = Mode.IN, targetNamespace = TNS) String libraryName) {
		logger.info("get refIdNames for Library {} ...", libraryName);
		return this.freebimWebservice.getRefIdNames(username, password, libraryName);
	}

	/**
	 * Get all references for a specified {@link Library} and refIdName.<br>
	 * If a {@link Library} has been imported from an external database, a reference
	 * to the external entity is stored as a combination of <code>refIdName</code>
	 * and <code>refId</code>.<br>
	 * I.e. the table name of the external database is stored in
	 * <code>refIdName</code> and the primary key of the entity is stored in
	 * <code>refId</code>.<br>
	 * <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getLibraryReferences xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:password>myPassword</gs:password>
	 *     <gs:libraryName>freeBIM</gs:libraryName>
	 *     <gs:refIdName>parameters.ID</gs:refIdName>
	 * </gs:getLibraryReferences>
	 * }
	 * </pre>
	 * 
	 * @see #getRefIdNames(String, String, String)
	 * @see #getLibraryReference(String, String, String, String, String)
	 * 
	 * @param username
	 * @param password
	 * @param libraryName Name of {@link Library}.
	 * @param refIdName   I.e. a table name of an external database.
	 * @return All references.<br>
	 *         <br>
	 *         Example response:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getLibraryReferencesResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<LibraryReference>
	 * 		<freebimId>851c5f77-5f0e-4bf8-8d4a-4cd7eeca084a</freebimId>
	 * 		<refId>322</refId>
	 * 	</LibraryReference>
	 * 	<LibraryReference>
	 * 		<freebimId>08857fb8-665e-4b75-850e-961f8ddd6f38</freebimId>
	 * 		<refId>1141</refId>
	 * 	</LibraryReference>
	 * 	<LibraryReference>
	 * 		<freebimId>35fc1219-7524-44e9-95ff-dd5f58e2ccfb</freebimId>
	 * 		<refId>1140</refId>
	 * 	</LibraryReference>
	 * ...
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "LibraryReference")
	public List<LibraryReference> getLibraryReferences(
			@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password,
			@WebParam(name = "libraryName", mode = Mode.IN, targetNamespace = TNS) String libraryName,
			@WebParam(name = "refIdName", mode = Mode.IN, targetNamespace = TNS) String refIdName) {
		logger.info("getLibraryReferences libraryName=[{}], refIdName=[{}] ...", libraryName, refIdName);
		return this.freebimWebservice.getLibraryReferences(username, password, libraryName, refIdName);
	}

	/**
	 * Get all nodes that are referencing a specified {@link Library} with a
	 * specified <code>refIdName</code> and a specified <code>refId</code>. <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getLibraryReference xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:password>myPassword</gs:password>
	 *     <gs:libraryName>freeBIM</gs:libraryName>
	 *     <gs:refIdName>parameters.ID</gs:refIdName>
	 *     <gs:refId>17</gs:refId>
	 * </gs:getLibraryReference>
	 * }
	 * </pre>
	 * 
	 * @see #getRefIdNames(String, String, String)
	 * 
	 * @param username
	 * @param password
	 * @param libraryName Name of {@link Library}.
	 * @param refIdName   I.e. a table name of an external database.
	 * @param refId       I.e. a primary key in an external database.
	 * @return The referencing objects.<br>
	 *         <br>
	 *         Example response:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getLibraryReferenceResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<Referenced xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type=
	"ns2:parameter">
	 * 		<freebimId>98e63dc6-c9ad-4dd3-a2c7-db58e8a51dac</freebimId>
	 * 		<code>P05</code>
	 * 		<defaultString>0000</defaultString>
	 * 		<desc>Mauerlichte (A) bzw. Wandöffnung (D)</desc>
	 * 		<disciplineRelations>
	 * 			<freebimId>e13e95c2-dd3b-4dae-9080-14ece47c86d7</freebimId>
	 * 		</disciplineRelations>
	 * 		<measureRelations>
	 * 			<freebimId>3c355d49-68f5-4075-b831-71f854662050</freebimId>
	 * 			<order>0</order>
	 * 		</measureRelations>
	 * 		<name>Breite Öffnung</name>
	 * 		<ptype>TYPE</ptype>
	 * 	</Referenced>
	 * </ns2:getLibraryReferenceResponse>
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "Referenced")
	public List<Base<? extends UuidIdentifyable>> getLibraryReference(
			@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password,
			@WebParam(name = "libraryName", mode = Mode.IN, targetNamespace = TNS) String libraryName,
			@WebParam(name = "refIdName", mode = Mode.IN, targetNamespace = TNS) String refIdName,
			@WebParam(name = "refId", mode = Mode.IN, targetNamespace = TNS) String refId) {
		logger.info("getLibraryReferences libraryName=[{}], refIdName=[{}] ...", libraryName, refIdName);
		return this.freebimWebservice.getLibraryReference(username, password, libraryName, refIdName, refId);
	}

	/**
	 * Get an element specified by its freeBIM-ID. <br>
	 * <br>
	 * Example request:
	 * 
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <gs:getByFreebimId xmlns:gs="http://db.freebim.at/">
	 *     <gs:username>myUsername</gs:username>
	 *     <gs:username>myPassword</gs:username>
	 *     <gs:freebimId>7eab2a34-85fe-4f82-891f-b0e9a9c13019</gs:freebimId>
	 * </gs:getByFreebimId>
	 * }
	 * </pre>
	 * 
	 * @param username
	 * @param password
	 * @param freebimId
	 * @return The found element. Actual type depends on stored data class. <br>
	 *         Example response:
	 * 
	 *         <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <ns2:getByFreebimIdResponse xmlns:ns2="http://db.freebim.at/">
	 * 	<Node xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type=
	"ns2:unit">
	 * 		<freebimId>7eab2a34-85fe-4f82-891f-b0e9a9c13019</freebimId>
	 * 		<bsddGuid>7 geladene bsDD-Guids</bsddGuid>
	 * 		<code>m</code>
	 * 		<conversions>
	 * 			<freebimId>138a2d9f-3255-4277-b393-81d352b11eb7</freebimId>
	 * 			<q>100.0</q>
	 * 		</conversions>
	 * 		<conversions>
	 * 			<freebimId>7d962c2d-b80b-48f1-a094-c39820ab83d3</freebimId>
	 * 			<q>1000.0</q>
	 * 		</conversions>
	 * 		<desc>Basiseinheit der Länge</desc>
	 * 		<descEn>fundamental unit of length</descEn>
	 * 		<name>Meter</name>
	 * 		<nameEn>metre</nameEn>
	 * 	</Node>
	 * </ns2:getByFreebimIdResponse>
	 * }
	 * </pre>
	 */
	@WebMethod
	@WebResult(name = "Node")
	public Base<? extends UuidIdentifyable> getByFreebimId(
			@WebParam(name = "username", mode = Mode.IN, targetNamespace = TNS) String username,
			@WebParam(name = "password", mode = Mode.IN, targetNamespace = TNS) String password,
			@WebParam(name = "freebimId", mode = Mode.IN, targetNamespace = TNS) String freebimId) {
		logger.info("getByFreebimId freebimId=[{}] ...", freebimId);
		return this.freebimWebservice.getByFreebimId(username, password, freebimId);
	}
}
