package de.svws_nrw.server.jetty;

import java.security.Principal;

import javax.security.auth.Subject;

import org.eclipse.jetty.security.IdentityService;
import org.eclipse.jetty.security.UserIdentity;
import de.svws_nrw.data.benutzer.BenutzerApiPrincipal;


/**
 * Dies Klasse implmentiert die Schnittstelle {@link IdentityService}
 * des Jetty-Server und wird für die Klasse {@link SvwsServer}
 * und dem Umgang mit einer {@link UserIdentity} benötigt.
 */
public final class SVWSIdentityService implements IdentityService {

	@Override
	public Association associate(final UserIdentity user, final RunAsToken runAsToken) {
		return null;
	}

	@Override
	public void onLogout(final UserIdentity user) {
		// nichts zu tun
	}

	@Override
	public UserIdentity newUserIdentity(final Subject subject, final Principal userPrincipal, final String[] roles) {
		if (userPrincipal instanceof BenutzerApiPrincipal)
			return UserIdentity.from(subject, userPrincipal, roles);
		return null;
	}

	@Override
	public RunAsToken newRunAsToken(final String runAsName) {
		return new RunAsToken() {
			/* empty */
		};
	}

	@Override
	public UserIdentity getSystemUserIdentity() {
		return null;
	}

}
