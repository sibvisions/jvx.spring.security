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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

/**
 * Handles the destroy of the server session and delegates the call to the parameterized 
 * logout success handler.
 * 
 * @author Thomas Krautinger
 */
public class DestroySessionLogoutSuccessHandler extends DestroySessionHandler 
                                                implements LogoutSuccessHandler
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The delegate logout success handler. */
	private LogoutSuccessHandler delegateLogoutSuccessHandler;
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Creates a new instance of <code>DestroySessionLogoutSuccessHandler</code>.
     */
	public DestroySessionLogoutSuccessHandler()
	{
		delegateLogoutSuccessHandler = new SimpleUrlLogoutSuccessHandler();
	}
	
	/**
	 * Creates a new instance of <code>DestroySessionLogoutSuccessHandler</code>.
	 * 
	 * @param pDelegateLogoutSuccessHandler
	 */
	public DestroySessionLogoutSuccessHandler(LogoutSuccessHandler pDelegateLogoutSuccessHandler)
	{
		delegateLogoutSuccessHandler = pDelegateLogoutSuccessHandler;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public void onLogoutSuccess(HttpServletRequest pRequest, HttpServletResponse pResponse, 
                                Authentication pAuthentication) throws IOException, ServletException
	{
		doLogout(pAuthentication);
		
		if (delegateLogoutSuccessHandler != null)
		{
			delegateLogoutSuccessHandler.onLogoutSuccess(pRequest, pResponse, pAuthentication);
		}
	}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the delegate logout success handler.
	 * 
	 * @return the delegate logout success handler
	 */
	public LogoutSuccessHandler getDelegateLogoutSuccessHandler()
	{
		return delegateLogoutSuccessHandler;
	}

	/**
	 * Sets the delegate logout success handler.
	 * 
	 * @param pDelegateLogoutSuccessHandler the delegate logout success handler
	 */
	public void setDelegateLogoutSuccessHandler(LogoutSuccessHandler pDelegateLogoutSuccessHandler)
	{
		delegateLogoutSuccessHandler = pDelegateLogoutSuccessHandler;
	}
	
} // DestroySessionLogoutSuccessHandler
