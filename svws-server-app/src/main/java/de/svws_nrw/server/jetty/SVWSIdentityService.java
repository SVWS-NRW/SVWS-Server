package de.svws_nrw.server.jetty;

import java.security.Principal;

import javax.security.auth.Subject;

import org.eclipse.jetty.security.IdentityService;
import org.eclipse.jetty.security.RoleRunAsToken;
import org.eclipse.jetty.security.RunAsToken;
import org.eclipse.jetty.server.UserIdentity;

import de.svws_nrw.api.OpenAPIPrincipal;


/**
 * Dies Klasse implmentiert die Schnittstelle {@link IdentityService}
 * des Jetty-Server und wird für die Klasse {@link HttpServer} 
 * und dem Umgang mit einer {@link SVWSUserIdentity} benötigt. 
 */
public class SVWSIdentityService implements IdentityService {
	
	@Override
	public Object associate(UserIdentity user) {
		return null;
	}

	@Override
	public void disassociate(Object previous) {
		//
	}

	@Override
	public Object setRunAs(UserIdentity user, RunAsToken token) {
		return token;
	}

	@Override
	public void unsetRunAs(Object token) {
		//
	}

	
	@Override
	public UserIdentity newUserIdentity(Subject subject, Principal userPrincipal, String[] roles) {
		if (userPrincipal instanceof OpenAPIPrincipal)
			return new SVWSUserIdentity(subject, userPrincipal);
		return UserIdentity.UNAUTHENTICATED_IDENTITY;
	}

	@Override
	public RunAsToken newRunAsToken(String runAsName) {
		return new RoleRunAsToken(runAsName);
	}

	@Override
	public UserIdentity getSystemUserIdentity() {
		return null;
	}

}
