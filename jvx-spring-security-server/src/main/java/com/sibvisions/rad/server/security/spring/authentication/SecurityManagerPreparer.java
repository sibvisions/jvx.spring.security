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

import javax.rad.remote.IConnectionConstants;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.PortResolver;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.security.web.util.RedirectUrlBuilder;
import org.springframework.security.web.util.UrlUtils;

import com.sibvisions.rad.server.security.spring.SpringSecurityManager;
import com.sibvisions.rad.server.security.spring.WrappedAuthentication;

/**
 * The <code>SecurityManagerPreparer</code> sets additional parameters
 * into the session and authentication object which are needed by the security manager.
 * 
 * @author Thomas Krautinger
 */
public class SecurityManagerPreparer implements InitializingBean
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The port resolver. */
	private PortResolver portResolver = new PortResolverImpl();
	
	/** The logout process URL. */
	private String logoutProcessUrl;
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>SecurityManagerPreparer</code>.
	 * 
	 * @param pLogoutProcessUrl the logout process URL.
	 */
	public SecurityManagerPreparer(String pLogoutProcessUrl)
	{
		logoutProcessUrl = pLogoutProcessUrl;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public void afterPropertiesSet() throws Exception
	{
		if (logoutProcessUrl == null)
		{
			throw new IllegalStateException("'logoutProcessUrl' is required");
		}
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Sets additional parameters to the session and authentication object.
	 * 
	 * @param pRequest the request which is used to get the current session
	 */
	public void doPrepareParameters(HttpServletRequest pRequest)
	{
		String absoluteLogoutProcessUrl = buildAbsoluteLogoutProcessUrl(pRequest);
		
		HttpSession session = pRequest.getSession(false);
		
		if (session != null)
		{
			session.setAttribute(SpringSecurityManager.LOGOUT_PROCESS_URL, absoluteLogoutProcessUrl);
		}
		
		SecurityContext context = SecurityContextHolder.getContext();
		
		if (context != null)
		{
			Authentication authentication = context.getAuthentication();
			
			if (authentication != null)
			{
				if (!(authentication instanceof WrappedAuthentication))
				{
					authentication = new WrappedAuthentication(authentication);
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
				
				((WrappedAuthentication) authentication).setProperty(SpringSecurityManager.LOGOUT_PROCESS_URL, absoluteLogoutProcessUrl);
			}
		}
	}
	
	/**
	 * Gets the logout process URL.
	 * 
	 * @return the logout process URL
	 */
	public String getLogoutProcessUrl()
	{
		return logoutProcessUrl;
	}

	/**
	 * Sets the logout process URL.
	 * 
	 * @param pLogoutProcessUrl the logout process URL
	 */
	public void setLogoutProcessUrl(String pLogoutProcessUrl)
	{
		logoutProcessUrl = pLogoutProcessUrl;
	}
	
	/**
	 * Builds the absolute logout process URL based on the request instance <code>pRequest</code> and <code>logoutProcessUrl</code>.
	 * 
	 * @param pRequest the request to build the logout process URL
	 * 
	 * @return the absolute logout process URL
	 */
	protected String buildAbsoluteLogoutProcessUrl(HttpServletRequest pRequest)
	{

        if (UrlUtils.isAbsoluteUrl(logoutProcessUrl))
        {
            return logoutProcessUrl;
        }
        
        RedirectUrlBuilder urlBuilder = new RedirectUrlBuilder();

        urlBuilder.setScheme(pRequest.getScheme());
        urlBuilder.setServerName(pRequest.getServerName());
        urlBuilder.setPort(portResolver.getServerPort(pRequest));
        urlBuilder.setContextPath(pRequest.getContextPath());
        urlBuilder.setPathInfo(logoutProcessUrl);

        return urlBuilder.getUrl();
    }
	
} // SecurityManagerPreparer
