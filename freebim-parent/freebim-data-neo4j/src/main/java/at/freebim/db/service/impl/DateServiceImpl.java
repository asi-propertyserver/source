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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import at.freebim.db.service.DateService;

/**
 * The service for handling all things regarding the time and date. It extends
 * {@link DateService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.service.DateService
 */
@Service
public class DateServiceImpl implements DateService {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(DateServiceImpl.class);

	private SimpleDateFormat simpleDateFormat;
	private SimpleDateFormat parserFormat;

	/**
	 * Initialize the private fields.
	 */
	@PostConstruct
	public void init() {
		this.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		this.parserFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.DateService#getNow()
	 */
	@Override
	public String getNow() {
		Date now = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime();
		String res = simpleDateFormat.format(now);
		logger.debug("Now is: [{}]", res);
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.DateService#getMillis()
	 */
	@Override
	public Long getMillis() {
		Long millis = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
		logger.debug("getMillis for now returning {}", millis);
		return millis;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.freebim.db.service.DateService#getMillisFrom(java.lang.String)
	 */
	@Override
	public Long getMillisFrom(String dateTime) {
		if (dateTime == null) {
			logger.warn("Can't getMillis from null string, returning 0L.");
			return 0L;
		}
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		try {
			cal.setTime(this.parserFormat.parse(dateTime));
			Long millis = cal.getTimeInMillis();

			logger.debug("getMillis from [{}] returning {}", dateTime, millis);
			return millis;
		} catch (ParseException e) {
			logger.error("Can't getMillis from [{}], returning 0L.", dateTime);
		}
		return 0L;
	}

}
