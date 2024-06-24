package de.svws_nrw.server.jetty;

import javax.security.auth.Subject;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.WebApplicationException;

import org.eclipse.jetty.security.IdentityService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.server.UserIdentity;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.security.Password;

import de.svws_nrw.data.benutzer.BenutzerApiPrincipal;
import de.svws_nrw.db.utils.ApiOperationException;



/**
 * Diese Klasse implementiert den {@link LoginService} des Jetty-Services und stellt
 * die wesentlichen Teile des Authentifizierungs-Prozesses im SVWS-Server zur Verfügung. <br>
 * Hierbei ist insbesondere die Implementierung der Methode
 * {@link SVWSLoginService#login(String, Object, ServletRequest)} von Beudeutung.
 */
public final class SVWSLoginService extends AbstractLifeCycle implements LoginService {

	/** der Identity-Service, welcher in diesem LoginService genútzt wird. */
	protected IdentityService userIdentityService = new SVWSIdentityService();

	/** Der Name dieses Authentifizierungs-Dienstes */
	protected String serviceName;



	/**
	 * Erzeugt einen neuen Authentifizierungs-Dienst mit dem angegebenen Dienst-Namen.
	 *
	 * @param serviceName   der Name des Authentifizierungs-Dienstes
	 */
	public SVWSLoginService(final String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * @see org.eclipse.jetty.security.LoginService#getName()
	 */
	@Override
	public String getName() {
		return serviceName;
	}




	/**
	 * @see org.eclipse.jetty.security.LoginService#login(java.lang.String,
	 *      java.lang.Object, jakarta.servlet.ServletRequest)
	 */
	@Override
	public UserIdentity login(final String username, final Object credentials, final ServletRequest request) {
		// Akzeptiere nur HTTP-Anfragen
		if (!(request instanceof HttpServletRequest))
			return null;
		final HttpServletRequest req = (HttpServletRequest) request;

		// Wandle die Crendentials in einen Passwort-String um
		final String password;
		if (credentials instanceof char[])
			password = new String((char[]) credentials);
		else if ((credentials instanceof String) || (credentials instanceof Password))
			password = credentials.toString();
		else {
			System.err.println("Fehler beim Prüfen des Kennwortes! " + credentials.getClass());
			return null;
		}

		// Prüfe, ob ein Login bei der OpenAPI-Applikation erfolgreich ist
		final BenutzerApiPrincipal principal;
		try {
			principal = BenutzerApiPrincipal.login(username, password, req);
			if (principal == null)
				return null;
		} catch (final ApiOperationException e) {
			throw new WebApplicationException(e.getMessage(), e, e.getStatus());
		}

		// Erzeuge UserIdentity zur weiteren Handhabung im Rahmen des Jetty-LoginService, diese erlaubt auch den Zugriff auf den
		// UserPrincpial mit den SchILD-BenutzerInformationen
		final Subject subject = new Subject();
		subject.getPrincipals().add(principal);
		subject.setReadOnly();
		return userIdentityService.newUserIdentity(subject, principal, null);
	}


	/**
	 * @see org.eclipse.jetty.security.LoginService#validate(org.eclipse.jetty.server.UserIdentity)
	 */
	@Override
	public boolean validate(final UserIdentity user) {
		// TODO prüfe, ob der angemeldete Benutzer immer noch gültig angemeldet ist
		return true;
	}


	/**
	 * @see org.eclipse.jetty.security.LoginService#logout(org.eclipse.jetty.server.UserIdentity)
	 */
	@Override
	public void logout(final UserIdentity user) {
		// TODO invalidate UserIdentity
	}




	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + serviceName + "]";
	}




	/**
	 * @see org.eclipse.jetty.security.LoginService#getIdentityService()
	 */
	@Override
	public IdentityService getIdentityService() {
		return userIdentityService;
	}


	/**
	 * Set the identityService.
	 *
	 * @param identityService the identityService to set
	 */
	@Override
	public void setIdentityService(final IdentityService identityService) {
		if (isRunning())
			throw new IllegalStateException("Running");
		userIdentityService = identityService;
	}


}
