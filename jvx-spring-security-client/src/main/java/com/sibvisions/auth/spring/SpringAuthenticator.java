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
package com.sibvisions.auth.spring;

import java.util.Hashtable;

import javax.rad.application.ILauncher;
import javax.rad.remote.AbstractConnection;

import com.sibvisions.apps.auth.IAuthenticator;

/**
 * The <code>SpringAuthenticator</code> will be used for spring security pre authentication.
 * 
 * @author Thomas Krautinger
 */
public class SpringAuthenticator implements IAuthenticator
{
 	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public Hashtable<String, Object> getCredentials(ILauncher pLauncher)
	{
		Hashtable<String, Object> htCredentials = new Hashtable<String, Object>();
		
		String sValue = pLauncher.getParameter(ILauncher.PARAM_APPLICATIONNAME);
		
		if (sValue != null)
		{
			htCredentials.put(APPLICATION, sValue);
		}

		return htCredentials;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAuthenticated(ILauncher pLauncher, AbstractConnection pConnection)
	{
		// Do nothing
	}

} // SpringAuthenticator
