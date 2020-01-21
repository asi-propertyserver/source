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

import at.freebim.db.domain.base.Parameterized;
import at.freebim.db.service.ParameterizedService;

/**
 * This service defines the basics for all services that define functionality
 * for a class <b>T</b> that extends {@link Parameterized}. This service extends
 * {@link StatedBaseNodeServiceImpl} and implements
 * {@link ParameterizedService}.
 *
 * @author rainer.breuss@uibk.ac.at
 * @see at.freebim.db.domain.base.Parameterized
 * @see at.freebim.db.service.ParameterizedService
 * @see at.freebim.db.service.impl.StatedBaseNodeServiceImpl
 */
public abstract class ParameterizedServiceImpl<T extends Parameterized> extends StatedBaseNodeServiceImpl<T>
		implements ParameterizedService<T> {

}
