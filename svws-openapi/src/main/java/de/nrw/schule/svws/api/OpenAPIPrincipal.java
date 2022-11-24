package de.nrw.schule.svws.api;

import java.io.Serializable;
import java.security.Principal;

import jakarta.servlet.http.HttpServletRequest;

import de.nrw.schule.svws.config.SVWSKonfiguration;
import de.nrw.schule.svws.db.Benutzer;
import de.nrw.schule.svws.db.DBConfig;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.utils.BenutzerUtils;
import de.nrw.schule.svws.db.utils.OperationError;


/**
 * Diese Klasse beinhaltet die {@link Principal}-Objekte für den Authentifizierungsprozess
 * über die OpenAPI-Schnittstelle. Sie ist zunächst unabhängig gehalten von der konkret
 * gewählten HTTP-Server-Implementierung. Eine solche HTTP-Server-Implementierung wird ggf.
 * für den internen Gebrauch weitere {@link Principal}-Objekte anlegen.
 */
public class OpenAPIPrincipal implements Principal, Serializable {
	
	private static final long serialVersionUID = -6227920753748399662L;

	/** Das zu dem Principal gehörende {@link Benutzer}-Objekt für den Datenbankzugriff */
	private Benutzer user;

	
	/**
	 * Erzeugt einen neuen Principal ohne zugeordnetem {@link Benutzer} für den Datenbankzugriff.
	 */
	private OpenAPIPrincipal() {
		user = null;
	}
	
	
	/**
	 * Erzeugt einen neuen Principal mit dem übergebenen {@link Benutzer} für den Datenbankzugriff.
	 * 
	 * @param user   der {@link Benutzer} für den Datenbankzugriff
	 */
	private OpenAPIPrincipal(Benutzer user) {
		this.user = user;
	}

	
	/**
	 * Gibt das {@link Benutzer}-Objekt für den Datenbankzugriff zurück, welches diesem
	 * Principal zugeordnet ist.
	 *  
	 * @return das {@link Benutzer}-Objekt für den Datenbankzugriff 
	 */
	public Benutzer getUser() {
		return user;
	}	
	
	
	/**
	 * Gibt zurück, ob dieser Principal bei der SVWS-Datenbank authentifiziert ist.
	 * 
	 * @return true, falls dieser Principal bei der SVWS-Datenbank authentifiziert ist.
	 */
	public boolean isAuthenticated() {
		return user != null;
	}
		

	@Override
	public String toString() {
		return user == null ? "Benutzer nicht angemeldet" : user.getUsername();
	}

	@Override
	public String getName() {		
		return user == null ? null : user.getUsername();
	}


	/**
	 * Prüft, ob der Login mit dem angegebenen Benutzername und dem angegebenen Kennwort bei dem HTTP-Request gültig ist.  
	 * 
	 * @param username   der Benutzername
	 * @param password   das Kennwort
	 * @param request    der HTTP-Request
	 * 
	 * @return der Benutzerprincipal, falls der Login gültig ist, sonst null
	 */
	public static OpenAPIPrincipal login(String username, String password, HttpServletRequest request) {
		// Prüfe, ob die Pfade "/debug/" oder "/openapi.json" angefragt werden. Hier erfolgt immer ein anonymer Zugriff und keine Überprüfung über die DB
		String path = request.getPathInfo();
		if (path == null)
			return null;
		
		// Prüfe auf illegale Zugriffe die mit "/index.php" beginnen und lehne den Zugriff ab
		if (path.startsWith("/index.php"))
			return null;
		
		boolean isAnonymous = (path.matches("/debug/.*") || "/openapi.json".equals(path));
		// Prüfe, ob aufgrund der Konfiguration ein anonymer Zugriff auf den SVWS-Client ermöglicht werden soll
		if ((!SVWSKonfiguration.get().isEnableClientProtection()) && 
				(path.matches("/") || path.matches("/.*\\.html") || 
				 path.matches("/.*\\.js") || path.matches("/.*\\.js.map") || path.matches("/js/.*") ||
				 path.matches("/.*\\.css") || path.matches("/.*\\.css.map") || path.matches("/css/.*") ||
				 path.matches("/fonts/.*") ||
				 path.matches("/.*\\.ico") || path.matches("/.*\\.png") ||
				 path.matches("/assets/.*"))) {
			isAnonymous = true;
		}

		// Spezieller DB-Zugriff für "/api/schema/root/" - Hier muss eine Anmeldung mit einem DB-Passwort erfolgen, da Operationen direkt das Schema manipulieren
		boolean isDBMSRootAuthentication = path.matches("/api/schema/root/.*");
		
		// Bestimme das Schema, auf welches zugegriffen wird anhand des aktuellen Pfades - gehe zunächst davon aus, dass kein Schema gewählt ist
		String schema = "";
		if ((!isAnonymous) && (!isDBMSRootAuthentication)) {
			var pathelements = path.split("/");
			if ((pathelements.length > 2) && ("".equals(pathelements[0])) && ("db".equals(pathelements[1])))
				schema = pathelements[2];
		}
		DBConfig config = SVWSKonfiguration.get().getDBConfig(schema);
		if (config == null)
			isAnonymous = true;

		// Erzeuge ggf. einen anonymen Principal
		if (isAnonymous && (!isDBMSRootAuthentication))
			return new OpenAPIPrincipal();

		// Prüfe, ob ein Zugriff als Root auf das DBMS nötig ist
		if (isDBMSRootAuthentication) {
			// Prüfe, ob der root-Zugriff per Konfiguration deaktiviert wurde. Dann darf aus Sicherheitgründen auch keine Kennwort-Prüfung stattfinden!
			if (SVWSKonfiguration.get().isDBRootAccessDisabled())
				return null;
			// Erstelle eine DB-Konfiguration für den Datenbank-Root-Zugriff mit den angegebenen Benutzerdaten 
			// An dieser Stelle kann nicht vorausgesetzt werden, dass ein anderes SVWS-Schema bereits generiert wurde.
			DBConfig rootConfig = SVWSKonfiguration.get().getRootDBConfig(username, password);
			Benutzer benutzer = Benutzer.create(rootConfig);
			benutzer.setUsername(username);
			benutzer.setPassword(password);
			try (DBEntityManager conn = benutzer.getEntityManager()) {
				if (conn == null)  // Prüfe, ob eine Verbindung zu DB aufgebaut werden konnte 
					return null; // wenn nicht, dann liegt ein Verbindungs- oder Authentifizierungsfehler vor und die Authentifizierung ist fehlgeschlagen
			}
			return new OpenAPIPrincipal(benutzer);
		}

		if (config == null)
			return null;

		// Prüfe, ob das Datenbankschema ggf. gesperrt ist.
		if ((config.getDBSchema() != null) && SVWSKonfiguration.get().isLockedSchema(config.getDBSchema()))
			throw OperationError.SERVICE_UNAVAILABLE.exception("Datenbank-Schema ist zur Zeit aufgrund von internen Operationen gesperrt. Der Zugriff kann später nochmals versucht werden.");

		if (config.useDBLogin()) {
			// Setze den übergebene Benutzername und das Kennwort auch für die Datenbankverbindung, falls die DB-Konfiguration eine Anmeldung per SVWS-Benutzer vorsieht  
			config = config.switchUser(username, password);
		}
		Benutzer user = Benutzer.create(config);
		user.setUsername(username);
		user.setPassword(password);

		// Prüfe den Passwort-Hash des Benutzer aus der DB
		if (!BenutzerUtils.pruefePasswort(user, password))
			return null;
		
		// Lese die Benutzerkompetenzen aus der Datenbank
		BenutzerUtils.leseKompetenzen(user);

		return new OpenAPIPrincipal(user);
	}	

}
