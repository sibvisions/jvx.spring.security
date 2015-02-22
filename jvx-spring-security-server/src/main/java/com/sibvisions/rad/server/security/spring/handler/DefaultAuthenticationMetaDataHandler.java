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
package com.sibvisions.rad.server.security.spring.handler;

import java.util.Collection;
import java.util.Hashtable;

import javax.rad.persist.MetaData;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sibvisions.util.ArrayUtil;

/**
 * The <code>DefaultAuthenticationMetaDataHandler</code> encapsulate the access to a spring security 
 * authentication object.
 * 
 * @author Thomas Krautinger
 */
public class DefaultAuthenticationMetaDataHandler implements ISpringMetaDataHandler
{	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The properties. */
	private Hashtable<String, Object> properties;
	
	/** The authentication. */
	private Authentication authentication;
	
	/** The cached roles of the user details. */
	private String[] sRoles = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>DefaultAuthenticationMetaDataHandler</code>.
	 * 
	 * @param pProperties the properties.
	 */
	public DefaultAuthenticationMetaDataHandler(Hashtable<String, Object> pProperties)
	{
		setProperties(pProperties);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public String getUsername()
	{
		return authentication.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPassword()
	{
		if (authentication.getCredentials() instanceof String)
		{
			return (String) authentication.getCredentials();
		}
		
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isValid()
	{
		Object principal = authentication.getPrincipal();
		
		if (principal instanceof UserDetails)
		{
			return ((UserDetails) principal).isEnabled()
				   && ((UserDetails) principal).isCredentialsNonExpired()
				   && ((UserDetails) principal).isAccountNonLocked()
				   && ((UserDetails) principal).isAccountNonExpired();
		}
		
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getRoles()
	{
		if (sRoles == null)
		{
			ArrayUtil<String> auRoles = new ArrayUtil<String>();
			
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			
			for (GrantedAuthority authority : authorities)
			{
				if (!auRoles.contains(authority.getAuthority()))
				{
					String sAuthority = authority.getAuthority().trim();
					
					if (sAuthority.startsWith("[")
						&& sAuthority.endsWith("]"))
					{
						sAuthority = sAuthority.substring(1, sAuthority.length() - 1).trim();
					}
					
					auRoles.add(sAuthority);
				}
			}
			
			sRoles = new String[auRoles.size()];
			auRoles.toArray(sRoles);
		}
		
		return sRoles;
	}

	/**
	 * {@inheritDoc}
	 */
	public String[][] getWorkScreens()
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getLifecycleObjects()
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Hashtable<String, MetaData> getMetaData()
	{
		return null;
	}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the properties.
	 * 
	 * @return the properties
	 */
	public Hashtable<String, Object> getProperties()
	{
		return properties;
	}

	/**
	 * Sets the properties.
	 * 
	 * @param pProperties the properties
	 */
	public void setProperties(Hashtable<String, Object> pProperties)
	{
		if (pProperties == null
			|| !pProperties.containsKey("authentication"))
		{
			throw new IllegalArgumentException("The authentication property was not found" );
		}
		
		authentication = ((Authentication) pProperties.get("authentication"));
		properties = pProperties;
	}
	
} // DefaultAuthenticationMetaDataHandler
