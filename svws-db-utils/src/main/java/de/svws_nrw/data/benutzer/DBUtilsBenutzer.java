package de.svws_nrw.data.benutzer;

import de.nrw.schule.svws.ext.jbcrypt.BCrypt;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.types.benutzer.BenutzerTyp;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.views.benutzer.DTOViewBenutzer;
import de.svws_nrw.db.dto.current.views.benutzer.DTOViewBenutzerdetails;
import de.svws_nrw.db.dto.current.views.benutzer.DTOViewBenutzerKompetenz;


/**
 * Diese Klasse stellt allgemeine Methoden bezüglich eines Datenbank-Benutzer zur Verfügung.
 */
public final class DBUtilsBenutzer {

	private DBUtilsBenutzer() {
		throw new IllegalStateException("Instantiation of " + DBUtilsBenutzer.class.getName() + " not allowed");
	}

	/**
	 * Diese Methode liest die Kompetenzen des Benutzers ein und speichert diese Information
	 * bei dem übergebenen Benutzer-Objekt.
	 * Anmerkung: Diese Methode benutzt dabei die DTO-Klasse DTOUsers, weshalb sie nicht in
	 * die Klasse Benutzer integriert werden kann.
	 *
	 * @param user   der Benutzer dessen Kompetenzen eingelesen werden sollen
	 */
	public static void leseKompetenzen(final Benutzer user) {
		user.getKompetenzen().clear();
		try (DBEntityManager conn = user.getEntityManager()) {
			final DTOViewBenutzer dbBenutzer = conn.queryNamed("DTOViewBenutzer.benutzername", user.getUsername(), DTOViewBenutzer.class).stream().findFirst().orElse(null);
			if (dbBenutzer == null)
				return;
			if (dbBenutzer.IstAdmin)
				user.getKompetenzen().add(BenutzerKompetenz.ADMIN);
			conn.queryNamed("DTOViewBenutzerKompetenz.benutzer_id", dbBenutzer.ID, DTOViewBenutzerKompetenz.class).stream()
				.map(komp -> BenutzerKompetenz.getByID((int) (long) komp.Kompetenz_ID))
				.filter(komp -> (komp != null) && (komp != BenutzerKompetenz.KEINE))
				.forEach(komp -> user.getKompetenzen().add(komp));
		}
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
	public static boolean pruefePasswort(final Benutzer user, final String password) {
		if (user.getUsername() == null)
			return false;
		try (DBEntityManager conn = user.getEntityManager()) {
			if (conn.useDBLogin())
				return true;
			final DTOViewBenutzerdetails dbBenutzer = conn
					.queryNamed("DTOViewBenutzerdetails.benutzername", user.getUsername(), DTOViewBenutzerdetails.class).stream()
					.findFirst().orElse(null);
			if (dbBenutzer == null)
				return false;
			final String pwHash = dbBenutzer.PasswordHash;
			user.setId(dbBenutzer.ID);
			user.setIdLehrer(dbBenutzer.Typ == BenutzerTyp.LEHRER ? dbBenutzer.TypID : null);
			if ((password == null) || ("".equals(password))) {
				return (pwHash == null) || ("".equals(pwHash));
			}
			if ((pwHash == null) || ("".equals(pwHash))) {
				return false;
			}
			return BCrypt.checkpw(password, pwHash);
		}
	}


}
