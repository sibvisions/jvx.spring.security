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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * The <code>SecurityManagerPreparerAuthenticationFilter</code> sets additional parameters
 * into the session and authentication object which are needed by the security manager.
 * 
 * @author Thomas Krautinger
 */
public class SecurityManagerPreparerAuthenticationFilter extends SecurityManagerPreparer
                                                         implements Filter
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>SecurityManagerPreparerAuthenticationFilter</code>.
	 * 
	 * @param pLogoutProcessUrl the logout process URL.
	 */
	public SecurityManagerPreparerAuthenticationFilter(String pLogoutProcessUrl)
	{
		super(pLogoutProcessUrl); 
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public void init(FilterConfig pFilterConfig) throws ServletException
	{
		// Do nothing
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void doFilter(ServletRequest pRequest, ServletResponse pResponse, FilterChain pChain) throws IOException, ServletException
	{
		pChain.doFilter(pRequest, pResponse);
		
		if (pRequest instanceof HttpServletRequest)
		{
			doPrepareParameters((HttpServletRequest) pRequest);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void destroy()
	{
		// Do nothing
	}
	
} // SecurityManagerPreparerAuthenticationFilter
