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
package com.sibvisions.rad.server.security.spring.logout;

import javax.rad.remote.SessionExpiredException;

import org.springframework.security.core.Authentication;

import com.sibvisions.rad.server.Server;
import com.sibvisions.rad.server.security.spring.WrappedAuthentication;

/**
 * Handles destroy of the server session.
 * 
 * @author Thomas Krautinger
 */
public class DestroySessionHandler
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Destroys the server session.
	 * 
	 * @param pAuthentication the authentication
	 */
	public void doLogout(Authentication pAuthentication)
	{
		if (pAuthentication != null
			&& pAuthentication instanceof WrappedAuthentication)
		{
			Object sessionId = ((WrappedAuthentication) pAuthentication).getProperty("client.sessionid");
			
			if (sessionId != null)
			{
				Server server = Server.getInstance();
				
				try
				{
					server.destroySession(sessionId);
				}
				catch (SessionExpiredException exc)
				{
					// Do nothing
				}
			}
		}
	}
	
} // DestroySessionHandler
