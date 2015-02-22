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

import java.util.Hashtable;
import java.util.List;

import javax.rad.remote.IConnectionConstants;
import javax.rad.server.ISession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.sibvisions.rad.server.AbstractSession;
import com.sibvisions.rad.server.http.HttpContext;
import com.sibvisions.rad.server.security.IAccessController;
import com.sibvisions.rad.server.security.ISecurityManager;
import com.sibvisions.rad.server.security.spring.handler.DefaultAuthenticationMetaDataHandler;
import com.sibvisions.rad.server.security.spring.handler.ISpringMetaDataHandler;
import com.sibvisions.util.Reflective;
import com.sibvisions.util.type.ResourceUtil;
import com.sibvisions.util.xml.XmlNode;

/**
 * The <code>SpringPreAuthenticationSecurityManager</code> is an {@link ISecurityManager}  that allows
 * to use Spring Security as security manager.
 * 
 * @author Thomas Krautinger
 */
public class SpringSecurityManager implements ISecurityManager
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public void validateAuthentication(ISession pSession)
	{
		SecurityContext securityContext = SecurityContextHolder.getContext();
		
		if (securityContext != null)
		{
			Authentication authentication = securityContext.getAuthentication();
			
			if (authentication != null
				&& authentication.isAuthenticated())
			{
				Hashtable<String, Object> metadataProperties = new Hashtable<String, Object>();
				metadataProperties.put("authentication", authentication);
				
				ISpringMetaDataHandler metaDataHandler = getAuthenticationMetaDataHandler(metadataProperties, pSession);
				
				if (pSession instanceof AbstractSession)
				{
					((AbstractSession) pSession).setUserName(metaDataHandler.getUsername());
					((AbstractSession) pSession).setPassword(metaDataHandler.getPassword());
				}
				
				pSession.setProperty(IConnectionConstants.PREFIX_SERVER + "preauthentication.metadatahandler", metaDataHandler);
				
				if (!(authentication instanceof WrappedAuthentication))
				{
					authentication = new WrappedAuthentication(authentication);
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
				
				// set the jvx session id into the authentication object for the logout (success) handler
				((WrappedAuthentication) authentication).setProperty(IConnectionConstants.PREFIX_CLIENT + "sessionid", pSession.getId());
				
				// set the logout process url
				Object logoutProcessUrl = ((WrappedAuthentication) authentication).getProperty(IConnectionConstants.PREFIX_CLIENT + "logout.process.url");
				
				if (logoutProcessUrl == null)
				{
					HttpContext context = HttpContext.getCurrentInstance();
					
					if (context != null)
					{
						HttpSession session = ((HttpServletRequest) context.getRequest()).getSession(false);
						
						if (session != null)
						{
							pSession.setProperty(IConnectionConstants.PREFIX_CLIENT + "logout.process.url",
												 session.getAttribute(IConnectionConstants.PREFIX_CLIENT + "logout.process.url"));
						}
						
					}
				}
				
				pSession.setProperty(IConnectionConstants.PREFIX_CLIENT + "logout.process.url", logoutProcessUrl);
			}
			else
			{
				throw new SecurityException("Access denied! The authentication could not be established.");
			}
		}
		else
		{
			throw new SecurityException("Access denied! The security context could not be established.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void changePassword(ISession pSession) throws Exception
	{
		throw new UnsupportedOperationException("Change password is not supported!");
	}

	/**
	 * {@inheritDoc}
	 */
	public void logout(ISession pSession)
	{
		if (Boolean.valueOf((String)pSession.getProperty("userlogout")).booleanValue()
			&& pSession.getProperty(IConnectionConstants.PREFIX_CLIENT + "logout.process.url") == null)
		{
		     SecurityContextHolder.getContext().setAuthentication(null);
		     SecurityContextHolder.clearContext();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IAccessController getAccessController(ISession pSession) throws Exception
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void release()
	{
		// Do nothing
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates and return the authentication meta data handler.
	 * 
	 * @param pProperties properties for the authentication meta data handler
	 * @param pSession the session
	 * 
	 * @return the authentication meta data handler
	 */
	protected ISpringMetaDataHandler getAuthenticationMetaDataHandler(Hashtable<String, Object> pProperties, ISession pSession)
	{
		Hashtable<String, Object> properties = pProperties;
		
		if (pProperties == null)
		{
			pProperties = new Hashtable<String, Object>();
		}
		
		// append additional configuration properties
		List<XmlNode> propertiesNode = pSession.getConfig().getNodes("/application/securitymanager/preauhtentication/metadtahandler/properties/property");
		
		if (propertiesNode != null)
		{
			for (int i = 0, ic = propertiesNode.size(); i < ic; i++)
			{
				XmlNode property = propertiesNode.get(i);
				
				XmlNode propertyName = property.getNode("/name");
				XmlNode propertyValue = property.getNode("/value");
				
				if (propertyName != null
					&& propertyValue != null)
				{
					properties.put(propertyName.getValue(), propertyValue.getValue());
				}
			}
		}
		
		// create metadata handler class instance
		String className = pSession.getConfig().getProperty("/application/securitymanager/preauhtentication/metadtahandler/class");
		
		if (className == null)
		{
			return new DefaultAuthenticationMetaDataHandler(properties);
		}
		else
		{
			try
			{
				return (ISpringMetaDataHandler) Reflective.construct(ResourceUtil.getResourceClassLoader(this), className, properties);
			}
			catch (Throwable thr)
			{
				throw new SecurityException("Access denied! Cannot create spring metadata handler.", thr);
			}
		}
	}

} // SpringSecurityManager
