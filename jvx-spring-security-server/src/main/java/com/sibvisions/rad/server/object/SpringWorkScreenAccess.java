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
package com.sibvisions.rad.server.object;

import java.util.Hashtable;

import jvx.rad.persist.MetaData;
import jvx.rad.remote.IConnectionConstants;
import jvx.rad.server.ISession;
import jvx.rad.server.SessionContext;

import com.sibvisions.apps.server.object.IWorkScreenAccess;
import com.sibvisions.rad.persist.jdbc.DBStorage;
import com.sibvisions.rad.server.security.spring.handler.ISpringMetaDataHandler;
import com.sibvisions.util.ArrayUtil;

/**
 * The <code>SpringWorkScreenAccess</code> reads the available work screens from the spring meta data handler.
 * 
 * @author Thomas Krautinger
 */
public class SpringWorkScreenAccess implements IWorkScreenAccess
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the key for the metadata handler.  */
	private static final String METADATA_HANDLER_PROPERTY = IConnectionConstants.PREFIX_SERVER + "preauthentication.metadatahandler";
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public String[] getAvailableRoles() throws Exception
	{
		ArrayUtil<String> auRoles = new ArrayUtil<String>();
	   
		ISession session = SessionContext.getCurrentSession();
		
		if (session != null)
		{
			ISpringMetaDataHandler metaDataHandler = (ISpringMetaDataHandler) session.getProperty(METADATA_HANDLER_PROPERTY);
			
			if (metaDataHandler != null)
			{
				String[] sRoles = metaDataHandler.getRoles();
				
				if (sRoles != null)
				{
					auRoles.addAll(sRoles);
				}
			}
		}
		
		String[] sResult = new String[auRoles.size()];
		auRoles.toArray(sResult);
		
		return sResult;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String[][] getAvailableWorkScreens() throws Exception
	{
		ArrayUtil<String[]> auScreens = new ArrayUtil<String[]>();
	    
		ISession session = SessionContext.getCurrentSession();
		
		if (session != null)
		{
			ISpringMetaDataHandler metaDataHandler = (ISpringMetaDataHandler) session.getProperty(METADATA_HANDLER_PROPERTY);
			
			if (metaDataHandler != null)
			{
				String[][] workScreens = metaDataHandler.getWorkScreens();
				
				if (workScreens != null)
				{
					auScreens.addAll(workScreens);
				}
			}
		}
		
		String[][] sResult = new String[auScreens.size()][];
		auScreens.toArray(sResult);
		
		return sResult;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Hashtable<String, MetaData> getAvailableMetaData() throws Exception
	{
		Hashtable<String, MetaData> htMetaData = new Hashtable<String, MetaData>();
		
		ISession session = SessionContext.getCurrentSession();
		
		if (session != null)
		{
			Hashtable<String, MetaData> htCache = DBStorage.getMetaData(session.getLifeCycleName());
	
			if (htCache != null)
			{
				htMetaData.putAll(htCache);
			}
			
			ISpringMetaDataHandler metaDataHandler = (ISpringMetaDataHandler) session.getProperty(METADATA_HANDLER_PROPERTY);
			
			if (metaDataHandler != null)
			{
				Hashtable<String, MetaData> metaData = metaDataHandler.getMetaData();
				
				if (metaData != null)
				{
					htMetaData.putAll(metaData);
				}
			}
		}
		
		return htMetaData;
	}
	
} // SpringWorkScreenAccess
