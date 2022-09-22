package de.nrw.schule.svws.db.utils;

import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.db.Benutzer;
import de.nrw.schule.svws.db.dto.current.views.benutzer.DTOViewBenutzer;
import de.nrw.schule.svws.db.dto.current.views.benutzer.DTOViewBenutzerKompetenz;
import de.nrw.schule.svws.ext.jbcrypt.BCrypt;


/**
 * Diese Klasse stellt allgemeine Methoden bezüglich eines Datenbank-Benutzer zur Verfügung. 
 */
public class BenutzerUtils {
	
	
	/**
	 * Diese Methode liest die Kompetenzen des Benutzers ein und speichert diese Information
	 * bei dem übergebenen Benutzer-Objekt. 
	 * Anmerkung: Diese Methode benutzt dabei die DTO-Klasse DTOUsers, weshalb sie nicht in 
	 * die Klasse Benutzer integriert werden kann. 
	 * 
	 * @param user   der Benutzer dessen Kompetenzen eingelesen werden sollen
	 */
	public static void leseKompetenzen(Benutzer user) {
		user.getKompetenzen().clear();
		if (user.getEntityManager() == null)
			return;
		DTOViewBenutzer dbBenutzer = user.getEntityManager().queryNamed("DTOViewBenutzer.benutzername", user.getUsername(), DTOViewBenutzer.class).stream().findFirst().orElse(null);
		if (dbBenutzer == null)
			return;
		if (dbBenutzer.IstAdmin)
			user.getKompetenzen().add(BenutzerKompetenz.ADMIN);
		user.getEntityManager().queryNamed("DTOViewBenutzerKompetenz.benutzer_id", dbBenutzer.ID, DTOViewBenutzerKompetenz.class).stream()
			.map(komp -> BenutzerKompetenz.getByID((int)(long)komp.Kompetenz_ID))
			.filter(komp -> (komp != null) && (komp != BenutzerKompetenz.KEINE))
			.forEach(komp -> user.getKompetenzen().add(komp));
	}
	
	
	/**
	 * Prüft, ob das übergebene Passwort bei dem übergebenen Benutzer gültig ist.  
	 *  
	 * Anmerkung: Diese Methode benutzt dabei die DTO-Klasse DTOUsers, weshalb sie nicht in 
	 * die Klasse Benutzer integriert werden kann.
	 *  
	 * @param user        der Benutzer, bei dem das Kennwort geprüft werden soll
	 * @param password    das zu prüfende Kennwort
	 * 
	 * @return true, falls das Kennwort gültig ist, und ansonsten false 
	 */
	public static boolean pruefePasswort(Benutzer user, String password) {
		if (user.getUsername() == null)
			return false;
		if (user.getEntityManager() == null)
			return false;
		if (user.getEntityManager().useDBLogin())
			return true;
		DTOViewBenutzer dbBenutzer = user.getEntityManager().queryNamed("DTOViewBenutzer.benutzername", user.getUsername(), DTOViewBenutzer.class).stream().findFirst().orElse(null);
		if (dbBenutzer == null)
			return false;
		String PasswordHash = dbBenutzer.PasswordHash;
		user.setId(dbBenutzer.ID);
		if ((password == null) || ("".equals(password))) {
			return (PasswordHash == null) || ("".equals(PasswordHash));
		}
		if ((PasswordHash == null) || ("".equals(PasswordHash))) {
			return false;
		}
		return BCrypt.checkpw(password, PasswordHash);
	}
	

}
