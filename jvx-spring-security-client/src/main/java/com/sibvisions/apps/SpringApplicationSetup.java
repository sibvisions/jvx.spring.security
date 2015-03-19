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
package com.sibvisions.apps;

import java.net.URL;

import javax.rad.application.IApplication;
import javax.rad.application.genui.IApplicationSetup;
import javax.rad.application.genui.RemoteApplication;
import javax.rad.application.genui.UILauncher;
import javax.rad.remote.AbstractConnection;
import javax.rad.remote.CommunicationException;
import javax.rad.remote.IConnectionConstants;
import javax.rad.remote.MasterConnection;
import javax.rad.remote.UnauthorizedException;

import com.sibvisions.apps.projx.ProjX;
import com.sibvisions.auth.spring.SpringAuthenticator;

/**
 * The <code>SpringApplicationSetup</code> sets default parameters
 * and appends additional listeners for login, logout and exception handling.
 *  
 * @author Thomas Krautinger
 */
public class SpringApplicationSetup implements IApplicationSetup
{	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the application. */
	private ProjX projx;
	
	/** the flag indicates whether logout was forced by the user. */
	private boolean userLogout;
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public void apply(IApplication pApplication)
	{
		projx = (ProjX) pApplication;
		
		UILauncher launcher = projx.getLauncher();
		
		launcher.setParameter(ProjX.PARAM_AUTHENTICATOR, SpringAuthenticator.class.getName());
		launcher.setParameter(ProjX.PARAM_MENU_CHANGE_PASSWORD_VISIBLE, "false");
		launcher.setParameter(ProjX.PARAM_MENU_LOGIN_VISIBLE, "false");
		
		// login immediate
		projx.setUseLoginThread(false);
		
		// add listeners
		projx.eventBeforeLogout().addListener(this, "doBeforeLogout");
		projx.eventAfterLogout().addListener(this, "doAfterLogout");
		projx.eventAfterLogin().addListener(this, "doAfterLogin");
		projx.eventAutoLoginException().addListener(this, "doAutoLoginException");
		projx.eventAfterCommunicationException().addListener(this, "doAfterCommunicationException");
		
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Register additional parameters (logout.process.url and logout.process.target) to the launcher.
	 * 
	 * @param pApplication the application
	 */
	public void doAfterLogin(RemoteApplication pApplication)
	{
		UILauncher launcher = projx.getLauncher();
		
		try
		{
			launcher.setRegistryKey("logout.process.url", (String) projx.getConnection().getProperty(IConnectionConstants.PREFIX_CLIENT + "logout.process.url"));
		}
		catch (Throwable th)
		{
			projx.debug(th);
		}
		
		try
		{
			launcher.setRegistryKey("logout.process.target", (String) projx.getConnection().getProperty(IConnectionConstants.PREFIX_CLIENT + "logout.process.target"));
		}
		catch (Throwable th)
		{
			projx.debug(th);
		}
	}
	
	/**
	 * Validates if the logout was executed by the user. 
	 * 
	 * @param pApplication the application
	 */
	public void doBeforeLogout(RemoteApplication pApplication)
	{
		userLogout = false;
		
		try
		{
			AbstractConnection connection = pApplication.getConnection();
			
			if (connection != null)
			{
				String sUserLogout = (String) connection.getProperty("userlogout");
				
				userLogout = sUserLogout != null
							 && Boolean.valueOf(sUserLogout).booleanValue();
			}
		}
		catch (Throwable th)
		{
			projx.handleException(th);
		}
	}
	
	/**
	 * Redirects to the logout process URL if the logout was executed by the user.
	 * 
	 * @param pApplication the application
	 */
	public void doAfterLogout(RemoteApplication pApplication)
	{
		try
		{
			if (userLogout)
			{
				redirect();
				
				projx.showInformation(projx, projx.translate("You have successfully logged out!"));
			}
		}
		catch (Throwable th)
		{
			projx.handleException(th);
		}
		finally
		{
			projx.getLogin().setVisible(false);
		}
	}
	

	/**
	 * Shows error message if authentication failed.
	 * 
	 * @param pApplication the application
	 * @param pConnection the used connection
	 * @param pThrowable the exception
	 * @throws Throwable if show error fails
	 */
	public void doAutoLoginException(ProjX pApplication, MasterConnection pConnection, Throwable pThrowable) throws Throwable
	{
		projx.handleException(new SecurityException("Spring security pre-authentication failed!"));
		projx.error(pThrowable);
	}
	
	/**
	 * Shows error message after communication exception.
	 * 
	 * @param pApplication the application
	 * @param pException the communication exception
	 */
	public void doAfterCommunicationException(ProjX pApplication, CommunicationException pException)
	{
		if (pException instanceof UnauthorizedException)
		{
			try
			{
				projx.showInformation(projx, projx.translate("You were logged off by the pre-authentication system!"));
			}
			catch (Throwable thr)
			{
				projx.handleException(thr);
			}
		}
		else
		{
			projx.handleException(pException);
		}
	}

	
	/**
	 * Redirects to the url defined as registry key logout.process.url.
	 * 
	 * @return <code>true</code> if redirecting was successful, <code>false</code> otherwise.
	 */
	private boolean redirect()
	{
		UILauncher launcher = projx.getLauncher();
		
		String sProcessUrl = launcher.getRegistryKey("logout.process.url");
		String sProcessTarget = launcher.getRegistryKey("logout.process.target");
		
		if (sProcessUrl != null
			&& sProcessUrl.trim().length() > 0)
		{
			try
			{
				if (!(sProcessUrl.startsWith("/")
					|| sProcessUrl.startsWith("./")))
				{
					// we must validate the url, because windows rundll32 url.dll,fileprotocolhandler don't do it
					URL uProcessUrl = new URL(sProcessUrl);
					
					if (!(uProcessUrl.getProtocol().toLowerCase().equals("http")
						|| uProcessUrl.getProtocol().toLowerCase().equals("https")))
					{
						throw new IllegalArgumentException("Invalid logout process URL (" + sProcessUrl + ")");
					}
				}
				
				if (sProcessTarget == null
					|| sProcessTarget.trim().length() == 0)
				{
					sProcessTarget = "_self";
				}
				
				launcher.showDocument(sProcessUrl, null, sProcessTarget);
				
				return true;
			}
			catch (Throwable thr)
			{
				projx.error(thr);
			}
		}
		
		return false;
	}
	
} // SpringApplicationSetup
