package de.svws_nrw.server.jetty;

import java.security.Principal;

import javax.security.auth.Subject;

import org.eclipse.jetty.server.UserIdentity;

/**
 * Diese Klasse stellt eine Benutzeridentität im Anmeldeprozess des Jetty-Servers
 * dar und implementiert dafür die Schnittstelle {@link UserIdentity}.
 */
public final class SVWSUserIdentity implements UserIdentity {

	/** Dieses Attribut enthält die Information zum Benutzer entsprechend der Dokumentation der Klasse {@link Subject} */
    private final Subject subject;

    /** Diese Klasse repräsentiert den Benutzer-Principal, d.h. die Entität, die sich authentifizierz (siehe {@link Principal}) */
    private final Principal userPrincipal;

    /**
     * Erzeugt eine neue {@link SVWSUserIdentity}, durch Angabe der Benutzerinformationen der {@link Subject}-Klassen
     * und der Indetität in Form des {@link Principal}.
     *
     * @param subject         die Benutzerinformationen
     * @param userPrincipal   die Identität des Benutzers
     */
    public SVWSUserIdentity(final Subject subject, final Principal userPrincipal) {
        this.subject = subject;
        this.userPrincipal = userPrincipal;
    }

	@Override
	public Subject getSubject() {
		return subject;
	}

	@Override
	public Principal getUserPrincipal() {
		return userPrincipal;
	}

	@Override
	public boolean isUserInRole(final String role, final Scope scope) {
		// we accept every role...
		return true;
	}


    @Override
    public String toString() {
        return SVWSUserIdentity.class.getSimpleName() + "('" + userPrincipal.toString() + "')";
    }

}
