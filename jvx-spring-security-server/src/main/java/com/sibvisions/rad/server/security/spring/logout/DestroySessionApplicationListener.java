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
 * 24.04.2015 - [TK] - creation
 */
package com.sibvisions.rad.server.security.spring.logout;

import java.util.List;

import org.springframework.context.ApplicationListener;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;

/**
 * Handles destroy of the server session.
 * 
 * Ensure that a HttpSessionEventPublisher is registered in your web.xml.
 * 
 * e.g.:
 * 
 * <listener>
 *    <listener-class>org.springframework.security.web.session.HttpSessionEventPublisher</listener-class>
 * </listener>
 * 
 * @author Thomas Krautinger
 */
public class DestroySessionApplicationListener extends DestroySessionHandler
										  	   implements ApplicationListener<SessionDestroyedEvent>
{

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onApplicationEvent(SessionDestroyedEvent pEvent)
	{
		List<SecurityContext> securityContexts = pEvent.getSecurityContexts();
		
		if (securityContexts != null)
		{
			SecurityContext securityContext = null;
			
			for (int i = 0, ic = securityContexts.size(); i < ic; i++)
			{
				securityContext = securityContexts.get(i);
				
				if (securityContext != null)
				{
					doLogout(securityContext.getAuthentication());
				}
			}
		}
	}

} // DestroySessionApplicationListener
