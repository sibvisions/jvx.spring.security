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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

/**
 * Handles the destroy of the server session.
 * 
 * @author Thomas Krautinger
 */
public class DestroySessionLogoutHandler extends DestroySessionHandler 
                                         implements LogoutHandler
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The delegate logout success handler. */
	private LogoutHandler delegateLogoutHandler;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Creates a new instance of <code>DestroySessionLogoutHandler</code>.
     */
	public DestroySessionLogoutHandler()
	{
		delegateLogoutHandler = new SecurityContextLogoutHandler();
	}
	
	/**
	 * Creates a new instance of <code>DestroySessionLogoutHandler</code>.
	 * 
	 * @param pDelegateLogoutHandler the delegate logout handler
	 */
	public DestroySessionLogoutHandler(LogoutHandler pDelegateLogoutHandler)
	{
		delegateLogoutHandler = pDelegateLogoutHandler;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public void logout(HttpServletRequest pRequest, HttpServletResponse pResponse, Authentication pAuthentication)
	{
		doLogout(pAuthentication);
		
		if (delegateLogoutHandler != null)
		{
			delegateLogoutHandler.logout(pRequest, pResponse, pAuthentication);
		}
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the delegate logout handler.
	 * 
	 * @return the delegate logout handler
	 */
	public LogoutHandler getDelegateLogoutHandler()
	{
		return delegateLogoutHandler;
	}

	/**
	 * Sets the delegate logout handler.
	 * 
	 * @param pDelegateLogoutHandler the delegate logout handler
	 */
	public void setDelegateLogoutHandler(LogoutHandler pDelegateLogoutHandler)
	{
		delegateLogoutHandler = pDelegateLogoutHandler;
	}
	
} // DestroySessionLogoutHandler
