package de.svws_nrw.server.jetty;

import javax.security.auth.Subject;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;

import org.eclipse.jetty.security.IdentityService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.server.UserIdentity;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.security.Password;

import de.svws_nrw.api.OpenAPIPrincipal;



/**
 * Diese Klasse implementiert den {@link LoginService} des Jetty-Services und stellt
 * die wesentlichen Teile des Authentifizierungs-Prozesses im SVWS-Server zur Verfügung. <br>
 * Hierbei ist insbesondere die Implementierung der Methode 
 * {@link SVWSLoginService#login(String, Object, ServletRequest)} von Beudeutung.
 */
public class SVWSLoginService extends AbstractLifeCycle implements LoginService {

	/** der Identity-Service, welcher in diesem LoginService genútzt wird. */
	protected IdentityService userIdentityService = new SVWSIdentityService();
	
	/** Der Name dieses Authentifizierungs-Dienstes */
	protected String serviceName;
	


	/**
	 * Erzeugt einen neuen Authentifizierungs-Dienst mit dem angegebenen Dienst-Namen.
	 *  
	 * @param serviceName   der Name des Authentifizierungs-Dienstes
	 */
	public SVWSLoginService(String serviceName) {
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
	public UserIdentity login(String username, Object credentials, ServletRequest request) {
		// Akzeptiere nur HTTP-Anfragen
		if (!(request instanceof HttpServletRequest))
			return null;
		HttpServletRequest req = (HttpServletRequest) request;

		// Wandle die Crendentials in einen Passwort-String um
		String password;
		if (credentials instanceof char[]) 
			password = new String((char[]) credentials);
		else if ((credentials instanceof String) || (credentials instanceof Password)) 
			password = credentials.toString();
		else {
			System.err.println("Fehler beim Prüfen des Kennwortes! " + credentials.getClass());
			return null;
		}

		// Prüfe, ob ein Login bei der OpenAPI-Applikation erfolgreich ist
		OpenAPIPrincipal principal = OpenAPIPrincipal.login(username, password, req);
		if (principal == null)
			return null;
		
		// Erzeuge UserIdentity zur weiteren Handhabung im Rahmen des Jetty-LoginService, diese erlaubt auch den Zugriff auf den 
		// UserPrincpial mit den SchILD-BenutzerInformationen
		Subject subject = new Subject();
		subject.getPrincipals().add(principal);
		subject.setReadOnly();
		return userIdentityService.newUserIdentity(subject, principal, null);
	}

	
	/**
	 * @see org.eclipse.jetty.security.LoginService#validate(org.eclipse.jetty.server.UserIdentity)
	 */
	@Override
	public boolean validate(UserIdentity user) {
		// TODO prüfe, ob der angemeldete Benutzer immer noch gültig angemeldet ist
		return true;		
	}


	/**
	 * @see org.eclipse.jetty.security.LoginService#logout(org.eclipse.jetty.server.UserIdentity)
	 */
	@Override
	public void logout(UserIdentity user) {
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
	public void setIdentityService(IdentityService identityService) {
		if (isRunning())
			throw new IllegalStateException("Running");
		userIdentityService = identityService;
	}
	
	
}
