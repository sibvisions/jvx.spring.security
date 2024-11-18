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
package com.sibvisions.rad.server.security.spring.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

/**
 * The <code>SecurityManagerPreparerAuthenticationSuccessHandler</code> sets additional parameters
 * into the session and authentication object which are needed by the security manager.
 * 
 * @author Thomas Krautinger
 */
public class SecurityManagerPreparerAuthenticationSuccessHandler extends SecurityManagerPreparer 
                                                                 implements AuthenticationSuccessHandler
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The delegate authentication success handler. */
	private AuthenticationSuccessHandler delegateAuthenticationSuccessHandler;
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>PrepareSessionAuthenticationSuccessHandler</code>.
	 * As delegate authentication success handler will be an <code>SimpleUrlAuthenticationSuccessHandler</code> 
	 * with the default target URL <code>/</code> created.
	 * 
	 * @param pLogoutProcessUrl the logout process URL
	 */
	public SecurityManagerPreparerAuthenticationSuccessHandler(String pLogoutProcessUrl)
	{
		this(new SimpleUrlAuthenticationSuccessHandler("/"), pLogoutProcessUrl);
	}
	
	/**
	 * Creates a new instance of <code>PrepareSessionAuthenticationSuccessHandler</code>.
	 * As delegate authentication success handler will be an <code>SimpleUrlAuthenticationSuccessHandler</code> 
	 * with the default target URL <code>pDefaultTargetUrl</code> created.
	 * 
	 * @param pDefaultTargetUrl the default target URL
	 * @param pLogoutProcessUrl the logout process URL
	 */
	public SecurityManagerPreparerAuthenticationSuccessHandler(String pDefaultTargetUrl, String pLogoutProcessUrl)
	{
		this(new SimpleUrlAuthenticationSuccessHandler(pDefaultTargetUrl), pLogoutProcessUrl);
	}
	
	/**
	 * Creates a new instance of <code>PrepareSessionAuthenticationSuccessHandler</code>.
	 * 
	 * @param pDelegateAuthenticationSuccessHandler the delegate authentication success handler 
	 * @param pLogoutProcessUrl the logout process URL 
	 */
	public SecurityManagerPreparerAuthenticationSuccessHandler(AuthenticationSuccessHandler pDelegateAuthenticationSuccessHandler, 
                                                               String pLogoutProcessUrl)
	{
		super(pLogoutProcessUrl);
		
		delegateAuthenticationSuccessHandler = pDelegateAuthenticationSuccessHandler;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public void onAuthenticationSuccess(HttpServletRequest pRequest, HttpServletResponse pResponse, 
                                        Authentication pAuthentication) throws IOException, ServletException
	{
		if (delegateAuthenticationSuccessHandler != null)
		{
			delegateAuthenticationSuccessHandler.onAuthenticationSuccess(pRequest, pResponse, pAuthentication);
		}
		
		doPrepareParameters(pRequest);
	}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the delegate authentication success handler.
	 * 
	 * @return the delegate authentication success handler
	 */
	public AuthenticationSuccessHandler getDelegateAuthenticationSuccessHandler()
	{
		return delegateAuthenticationSuccessHandler;
	}

	/**
	 * Sets the delegate authentication success handler.
	 * 
	 * @param pDelegateAuthenticationSuccessHandler the delegate authentication success handler
	 */
	public void setDelegateAuthenticationSuccessHandler(AuthenticationSuccessHandler pDelegateAuthenticationSuccessHandler)
	{
		delegateAuthenticationSuccessHandler = pDelegateAuthenticationSuccessHandler;
	}

} // SecurityManagerPreparerAuthenticationSuccessHandler
