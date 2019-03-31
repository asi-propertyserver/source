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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import at.freebim.db.domain.BigBangNode;
import at.freebim.db.service.BaseNodeService;
import at.freebim.db.service.BigBangNodeService;
import at.freebim.db.service.DateService;
import at.freebim.db.service.StatisticService;
import at.freebim.db.service.StatisticService.StatPoint;

/**
 * This controller is used to return the statistics.
 * It extends {@link BaseController}.
 * 
 * @see at.freebim.db.domain.BigBangNode
 * @see at.freebim.db.webapp.controller.BaseController
 * 
 * @author rainer.breuss@uibk.ac.at
 *
 */
@Controller
@RequestMapping("/statistic")
public class StatisticController extends BaseController<BigBangNode> {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(StatisticController.class);
	

	/**
	 * The service that handles the root node/entity {@link BigBangNode}.
	 */
	@Autowired
	private BigBangNodeService bigBangNodeService;
	
	/**
	 * The service which provides as the statistical data.
	 */
	@Autowired
	private StatisticService statisticService;
	
	
	/**
	 * The service that handles the date.
	 */
	@Autowired
	private DateService dateService;
	
    /**
     * Creates a new instance.
     */
    public StatisticController() {
		super(BigBangNode.class);
	}

	/* (non-Javadoc)
	 * @see at.freebim.db.webapp.controller.BaseController#getService()
	 */
	@Override
	protected BaseNodeService<BigBangNode> getService() {
		return this.bigBangNodeService;
	}
	

	/**
	 * Show the statistic web-page.
	 * 
	 * @param model the model
	 * @return the name of the JSP page
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String getRoot(Model model) {
		logger.debug("Received request to show statistic page");

		super.setUserInfo(model);

		return "statistic";
	}

	/**
	 * Get the number of modifications over a specified time period.
	 * The period is defined by to time stamps that are passed as parameter.
	 * 
	 * @param fromTs the starting time stamp
	 * @param toTs the end time stamp
	 * @param model the model
	 * @return the {@link AjaxResponse} that includes the statistics
	 */
	@RequestMapping(value = "/added/{fromTs}/{toTs}", method = RequestMethod.GET)
	public @ResponseBody  AjaxResponse getAdded(@PathVariable Long fromTs, @PathVariable Long toTs, Model model) {
		logger.debug("Received request to statistic 'added', from=[{}], to=[{}]", fromTs, toTs);

		super.setUserInfo(model);
		
		if (toTs == 0L) {
			toTs = this.dateService.getMillis();
		}
		
		AjaxResponse response = null;
		
		ArrayList<StatPoint> points = this.statisticService.get(fromTs, toTs);
		
		response = new AjaxResponse(points);
		return response;
	}

}
