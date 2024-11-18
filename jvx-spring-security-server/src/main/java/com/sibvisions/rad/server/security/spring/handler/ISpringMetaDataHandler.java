/*
 * Copyright 2015 SIB Visions GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *
 * History
 * 
 * 04.02.2015 - [TK] - creation
 */
package com.sibvisions.rad.server.security.spring.handler;

import java.util.Hashtable;

import jvx.rad.persist.MetaData;

/**
 * The <code>ISpringMetaDataHandler</code> encapsulate the access to a spring security authentication object.
 * 
 * @author Thomas Krautinger
 */
public interface ISpringMetaDataHandler
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the username.
	 * 
	 * @return the username.
	 */
	public String getUsername();
	
	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public String getPassword();
	
	/**
	 * Gets the validity of the user.
	 * 
	 * @return <code>true</code> if the user is valid, <code>false</code> otherwise
	 */
	public boolean isValid();
	
	/**
	 * Gets a list with all available rolenames.
	 * 
	 * @return all available rolenames
	 */
	public String[] getRoles();
	
	/**
	 * Gets a list with all available work-screens.
	 * 
	 * @return all available work-screens
	 */
	public String[][] getWorkScreens();
	
	/**
	 * Gets all available column meta data for available work-screens.
	 * 
	 * @return the column meta data for the work-screens
	 */
	public Hashtable<String, MetaData> getMetaData();
	
} // ISpringMetaDataHandler
