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
package com.sibvisions.rad.server.security.spring.authentication;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * The <code>SecurityManagerPreparerApplicationListener</code> sets additional parameters
 * into the session and authentication object which are needed by the security manager.
 * 
 * Ensure that a RequestContextListener is registered in your web.xml.
 * 
 * e.g.:
 * 
 * <listener>
 *    <listener-class>org.springframework.web.context.request.RequestContextListener</listener-class>
 * </listener>
 * 
 * @author Thomas Krautinger
 */
public class SecurityManagerPreparerApplicationListener extends SecurityManagerPreparer
														implements ApplicationListener<InteractiveAuthenticationSuccessEvent>
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>SecurityManagerPreparerApplicationListener</code>.
	 * 
	 * @param pLogoutProcessUrl the logout process URL.
	 */
	public SecurityManagerPreparerApplicationListener(String pLogoutProcessUrl)
	{
		super(pLogoutProcessUrl);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public void onApplicationEvent(InteractiveAuthenticationSuccessEvent pEvent)
	{
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		
		if (requestAttributes != null)
		{
			Object request = requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
			
			if (request != null
				&& request instanceof HttpServletRequest)
			{
				doPrepareParameters((HttpServletRequest) request);
			}
		}
	}
	
} // SecurityManagerPreparerApplicationListener
