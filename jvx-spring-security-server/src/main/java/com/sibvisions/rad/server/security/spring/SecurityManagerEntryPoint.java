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
package com.sibvisions.rad.server.security.spring;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

/**
 * The <code>SecurityManagerEntryPoint</code> is the entry point for spring security configurations.
 * 
 * @author Thomas Krautinger
 */
public class SecurityManagerEntryPoint implements AuthenticationEntryPoint
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The delegate entry point. */
	private AuthenticationEntryPoint delegateEntryPoint;
	
	/** The forbidden entry point. */
	private AuthenticationEntryPoint delegateForbiddenEntryPoint;
	
	/** The secured paths. */
	private String[] securedPaths = new String[]{"/services/Server",
												 "/services/Download",
												 "/services/Upload",
												 "/services/mobile/*",
											     "/services/rest/*"};

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>SecurityManagerEntryPoint</code>.
	 * 
	 * @param pDelegateEntryPoint the delegate entry point
	 */
	public SecurityManagerEntryPoint(AuthenticationEntryPoint pDelegateEntryPoint)
	{
		delegateEntryPoint = pDelegateEntryPoint;
		delegateForbiddenEntryPoint = new Http403ForbiddenEntryPoint();
	}
	
	/**
	 * Creates a new instance of <code>SecurityManagerEntryPoint</code>.
	 * 
	 * @param pDelegateEntryPoint the delegate entry point
	 * @param pDelegateForbiddenEntryPoint the delegate forbidden entry point
	 */
	public SecurityManagerEntryPoint(AuthenticationEntryPoint pDelegateEntryPoint, AuthenticationEntryPoint pDelegateForbiddenEntryPoint)
	{
		delegateEntryPoint = pDelegateEntryPoint;
		delegateForbiddenEntryPoint = pDelegateForbiddenEntryPoint;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public void commence(HttpServletRequest pRequest, HttpServletResponse pResponse, AuthenticationException pAuthException) throws IOException, ServletException
	{
		if (pAuthException != null)
		{
			HttpSession session = pRequest.getSession(false);
			
			if (session != null)
			{
				String path = pRequest.getServletPath();
				
				if (path != null
					&& securedPaths != null)
				{
					for (int i = 0; i < securedPaths.length; i++)
					{
						if (path.equals(securedPaths[i])
							|| (securedPaths[i].endsWith("*")
							&& path.startsWith(securedPaths[i].substring(0, securedPaths[i].length() - 1))))
						{
							if (delegateForbiddenEntryPoint != null)
							{
								delegateForbiddenEntryPoint.commence(pRequest, pResponse, pAuthException);
							}
							
							return;
						}
					}
				}
			}
		}

		if (delegateEntryPoint != null)
		{
			delegateEntryPoint.commence(pRequest, pResponse, pAuthException);
		}
	}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the delegate entry point.
	 * 
	 * @return the delegate entry point
	 */
	public AuthenticationEntryPoint getDelegateEntryPoint()
	{
		return delegateEntryPoint;
	}

	/**
	 * Sets the delegate entry point.
	 * 
	 * @param pDelegateEntryPoint the delegate entry point
	 */
	public void setDelegateEntryPoint(AuthenticationEntryPoint pDelegateEntryPoint)
	{
		delegateEntryPoint = pDelegateEntryPoint;
	}

	/**
	 * Gets the delegate forbidden entry point.
	 * 
	 * @return the delegate forbidden entry point 
	 */
	public AuthenticationEntryPoint getForbiddenEntryPoint()
	{
		return delegateForbiddenEntryPoint;
	}

	/**
	 * Sets the delegate forbidden entry point.
	 * 
	 * @param pDeleageteForbiddenEntryPoint the delegate forbidden entry point
	 */
	public void setForbiddenEntryPoint(AuthenticationEntryPoint pDeleageteForbiddenEntryPoint)
	{
		delegateForbiddenEntryPoint = pDeleageteForbiddenEntryPoint;
	}

	/**
	 * Gets the secured paths.
	 * 
	 * @return the secured paths
	 */
	public String[] getSecuredPaths()
	{
		return securedPaths;
	}

	/**
	 * Sets the secured paths.
	 * 
	 * @param pSecuredPaths the secured paths
	 */
	public void setSecuredPaths(String[] pSecuredPaths)
	{
		securedPaths = pSecuredPaths;
	}
	
} // SecurityManagerEntryPoint
