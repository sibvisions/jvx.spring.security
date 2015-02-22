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
 * 04.02.2013 - [TK] - creation
 */
package com.sibvisions.rad.server.security.spring;

import java.util.Collection;
import java.util.Hashtable;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * The <code>WrappedAuthentication</code> extends the authentication object to append additional properties.
 * 
 * @author Thomas Krautinger
 */
public class WrappedAuthentication implements Authentication
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The original authentication. */
	private Authentication authentication;
	
	/** The properties. */
	private Hashtable<String, Object> properties = new Hashtable<String, Object>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>WrappedAuthentication</code>.
	 * 
	 * @param pAuthentication the authentication.
	 */
	public WrappedAuthentication(Authentication pAuthentication)
	{
		authentication = pAuthentication;
	}
	
 	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName()
	{
		return authentication.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		return authentication.getAuthorities();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getCredentials()
	{
		return authentication.getCredentials();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getDetails()
	{
		return authentication.getDetails();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getPrincipal()
	{
		return authentication.getPrincipal();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAuthenticated()
	{
		return authentication.isAuthenticated();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAuthenticated(boolean pIsAuthenticated) throws IllegalArgumentException
	{
		authentication.setAuthenticated(pIsAuthenticated);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the value of a property.
	 * 
	 * @param pName the property name
	 * @return the value of the property or <code>null</code> if the property is not available
	 */
	public Object getProperty(String pName)
	{
		return properties.get(pName);
	}
	
	/**
	 * Gets all properties.
	 * 
	 * @return a {@link Hashtable} with property names and values
	 */
	public Hashtable<String, Object> getProperties()
	{
		return properties;
	}
	
	/**
	 * Sets the value of a property.
	 * 
	 * @param pName the property name
	 * @param pValue the value for the property or <code>null</code> to delete the property
	 */
	public void setProperty(String pName, Object pValue)
	{
		properties.put(pName, pValue);
	}
	
} // WrappedAuthentication
