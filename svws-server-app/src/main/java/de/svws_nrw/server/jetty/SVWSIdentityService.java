package de.svws_nrw.server.jetty;

import java.security.Principal;

import javax.security.auth.Subject;

import org.eclipse.jetty.security.IdentityService;
import org.eclipse.jetty.security.RoleRunAsToken;
import org.eclipse.jetty.security.RunAsToken;
import org.eclipse.jetty.server.UserIdentity;

import de.svws_nrw.data.benutzer.BenutzerApiPrincipal;


/**
 * Dies Klasse implmentiert die Schnittstelle {@link IdentityService}
 * des Jetty-Server und wird für die Klasse {@link HttpServer}
 * und dem Umgang mit einer {@link SVWSUserIdentity} benötigt.
 */
public final class SVWSIdentityService implements IdentityService {

	@Override
	public Object associate(final UserIdentity user) {
		return null;
	}

	@Override
	public void disassociate(final Object previous) {
		//
	}

	@Override
	public Object setRunAs(final UserIdentity user, final RunAsToken token) {
		return token;
	}

	@Override
	public void unsetRunAs(final Object token) {
		//
	}


	@Override
	public UserIdentity newUserIdentity(final Subject subject, final Principal userPrincipal, final String[] roles) {
		if (userPrincipal instanceof BenutzerApiPrincipal)
			return new SVWSUserIdentity(subject, userPrincipal);
		return UserIdentity.UNAUTHENTICATED_IDENTITY;
	}

	@Override
	public RunAsToken newRunAsToken(final String runAsName) {
		return new RoleRunAsToken(runAsName);
	}

	@Override
	public UserIdentity getSystemUserIdentity() {
		return null;
	}

}
