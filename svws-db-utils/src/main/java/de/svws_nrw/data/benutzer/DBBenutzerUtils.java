package de.svws_nrw.data.benutzer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.types.benutzer.BenutzerTyp;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.views.benutzer.DTOViewBenutzer;
import de.svws_nrw.db.dto.current.views.benutzer.DTOViewBenutzerKompetenz;
import de.svws_nrw.db.dto.current.views.benutzer.DTOViewBenutzerdetails;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.ext.jbcrypt.BCrypt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;


/**
 * Diese Klasse stellt allgemeine Methoden bezüglich eines Datenbank-Benutzers zur Verfügung.
 */
public final class DBBenutzerUtils {

	private DBBenutzerUtils() {
		throw new IllegalStateException("Instantiation of " + DBBenutzerUtils.class.getName() + " not allowed");
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



	/**
	 * Ermittelt den aktuellen SVWS-Benutzer anhand des HTTP-Requests.
	 *
	 * @param request   das HTTP-Request-Objekt
	 * @param mode      der benötigte Server-Mode für den API-Zugriff
	 *
	 * @return der aktuelle SVWS-Benutzer
	 */
	private static Benutzer getSVWSUser(final HttpServletRequest request, final ServerMode mode) {
		if (!mode.checkServerMode(SVWSKonfiguration.get().getServerMode()))
			throw OperationError.SERVICE_UNAVAILABLE.exception("Der Dienst ist noch nicht verfügbar, da er sich zur Zeit noch in der Entwicklung befindet (Stand: %s).".formatted(mode.name()));
		if (request.getUserPrincipal() instanceof final BenutzerApiPrincipal openAPIPrincipal) {
			final Benutzer user = openAPIPrincipal.getUser();
			if (user == null)
				return null;
			final DBConfig config = user.connectionManager.getConfig();
			if ((config == null) || (config.getDBSchema() == null))
				return user;
			if (SVWSKonfiguration.get().isDeactivatedSchema(config.getDBSchema()))
				throw OperationError.SERVICE_UNAVAILABLE.exception("Datenbank-Schema ist zur Zeit deaktviert, da es fehlerhaft ist. Bitte wenden Sie sich an Ihren System-Administrator.");
			if (SVWSKonfiguration.get().isLockedSchema(config.getDBSchema()))
				throw OperationError.SERVICE_UNAVAILABLE.exception("Datenbank-Schema ist zur Zeit aufgrund von internen Operationen gesperrt. Der Zugriff kann später nochmals versucht werden.");
			return user;
		}
		return null;
	}

	/**
	 * Ermittelt den aktuellen SVWS-Benutzer anhand des HTTP-Requests und überprüft, ob der Benutzer
	 * entweder Admin-Rechte oder eine der übergebenen Kompetenzen besitzt.
	 *
	 * @param request       das HTTP-Request-Objekt
	 * @param mode          der benötigte Server-Mode für den API-Zugriff
	 * @param kompetenzen   die zu prüfenden Kompetenzen
	 *
	 * @return der aktuelle SVWS-Benutzer, falls ein Benutzer mit der Kompetenz angemeldet ist
	 *
	 * @throws WebApplicationException   Ist kein Benutzer angemeldet oder besitzt nicht die erforderliche Kompetenz,
	 *                                   so wird eine WebApplicationException mit dem HTTP Status Code FORBIDDEN (403) generiert
	 */
	public static Benutzer getSVWSUser(final HttpServletRequest request, final ServerMode mode, final BenutzerKompetenz... kompetenzen) throws WebApplicationException {
		final Benutzer user = getSVWSUser(request, mode);
		final Set<BenutzerKompetenz> setKompetenzen = new HashSet<>(Arrays.asList(kompetenzen));
		if ((user == null) || (!setKompetenzen.contains(BenutzerKompetenz.KEINE)) && (!user.pruefeKompetenz(setKompetenzen)))
			throw OperationError.FORBIDDEN.exception();
		return user;
	}

	/**
	 * Ermittelt den aktuellen SVWS-Benutzer anhand des HTTP-Requests und überprüft, ob der Benutzer
	 * entweder Admin-Rechte oder einer der übergebenen Kompetenzen besitzt. Erlaubt wird auch der Zugriff von
	 * dem Benutzer mit der übergebenen Benutzer-ID.
	 *
	 * @param request       das HTTP-Request-Objekt
	 * @param mode          der benötigte Server-Mode für den API-Zugriff
	 * @param user_id       die zu prüfende Benutzer-ID (ist dies die ID des angemeldeten Benutzers?)
	 * @param kompetenzen   die zu prüfenden Kompetenzen
	 *
	 * @return der aktuelle SVWS-Benutzer, falls ein Benutzer mit der Kompetenz angemeldet ist
	 *
	 * @throws WebApplicationException   Ist kein Benutzer angemeldet oder besitzt nicht die erforderliche Kompetenz,
	 *                                   so wird eine WebApplicationException mit dem HTTP Status Code FORBIDDEN (403) generiert
	 */
	public static Benutzer getSVWSUserAllowSelf(final HttpServletRequest request, final ServerMode mode, final long user_id, final BenutzerKompetenz... kompetenzen) throws WebApplicationException {
		final Benutzer user = getSVWSUser(request, mode);
		final Set<BenutzerKompetenz> setKompetenzen = new HashSet<>(Arrays.asList(kompetenzen));
		if ((user == null) || (!setKompetenzen.contains(BenutzerKompetenz.KEINE)) && (!user.pruefeKompetenz(setKompetenzen)) && (user.getId() != user_id))
			throw OperationError.FORBIDDEN.exception();
		return user;
	}


	/**
	 * Ermittelt den aktuellen SVWS-Benutzer anhand des HTTP-Requests und überprüft, ob der Benutzer
	 * entweder Admin-Rechte oder eine der übergebenen Kompetenzen besitzt. Anschließend wird eine
	 * {@link DBEntityManager} Instanz für den Datenbankzugriff zurückgegeben.
	 *
	 * @param request       das HTTP-Request-Objekt
	 * @param mode          der benötigte Server-Mode für den API-Zugriff
	 * @param kompetenzen   die zu prüfenden Kompetenzen
	 *
	 * @return die Datenbankverbindung für den aktuellen SVWS-Benutzer, falls ein Benutzer mit der Kompetenz angemeldet ist
	 *
	 * @throws WebApplicationException   Ist kein Benutzer angemeldet oder besitzt nicht die erforderliche Kompetenz,
	 *                                   so wird eine WebApplicationException mit dem HTTP Status Code FORBIDDEN (403) generiert
	 */
	public static DBEntityManager getDBConnection(final HttpServletRequest request, final ServerMode mode, final BenutzerKompetenz... kompetenzen) throws WebApplicationException {
		return getSVWSUser(request, mode, kompetenzen).getEntityManager();
	}


	/**
	 * Ermittelt den aktuellen SVWS-Benutzer anhand des HTTP-Requests und überprüft, ob der Benutzer
	 * entweder Admin-Rechte oder eine der übergebenen Kompetenzen besitzt. Erlaubt wird auch der Zugriff von
	 * dem Benutzer mit der übergebenen Benutzer-ID.
	 * Anschließend wird eine {@link DBEntityManager} Instanz für den Datenbankzugriff zurückgegeben.
	 *
	 * @param request       das HTTP-Request-Objekt
	 * @param mode          der benötigte Server-Mode für den API-Zugriff
	 * @param user_id       die zu prüfende Benutzer-ID (ist dies die ID des angemeldeten Benutzers?)
	 * @param kompetenzen   die zu prüfenden Kompetenzen
	 *
	 * @return die Datenbankverbindung für den aktuellen SVWS-Benutzer, falls ein Benutzer mit der Kompetenz angemeldet ist
	 *
	 * @throws WebApplicationException   Ist kein Benutzer angemeldet oder besitzt nicht die erforderliche Kompetenz,
	 *                                   so wird eine WebApplicationException mit dem HTTP Status Code FORBIDDEN (403) generiert
	 */
	public static DBEntityManager getDBConnectionAllowSelf(final HttpServletRequest request, final ServerMode mode, final long user_id, final BenutzerKompetenz... kompetenzen) throws WebApplicationException {
		return getSVWSUserAllowSelf(request, mode, user_id, kompetenzen).getEntityManager();
	}


	/**
	 * Führt die übergebene Aufgabe auf der Datenbank aus und gibt bei Erfolg die Response der Aufgabe zurück.
	 * Hierbei wird die übergebene {@link DBEntityManager}-Instanz wird für den Datenbankzugriff genutzt.
	 *
	 * Wichtig: Eine Transaktion für die Aufgabe wird erzeugt und von dieser Methode gehandhabt!
	 *
	 * @param task          die auszuführende Aufgabe
	 * @param conn          die Datenbank-Verbindung
	 *
	 * @return die Response zu der Aufgabe
	 */
	public static Response runWithTransaction(final Function<DBEntityManager, Response> task, final DBEntityManager conn) {
		try {
			conn.transactionBegin();
			final Response response = task.apply(conn);
			conn.transactionCommitOrThrow();
			return response;
		} catch (final Exception e) {
			if (e instanceof final WebApplicationException webAppException)
				return webAppException.getResponse();
			return OperationError.INTERNAL_SERVER_ERROR.exception(e).getResponse();
		} finally {
			// Perform a rollback if necessary
			conn.transactionRollbackOrThrow();
		}
	}


	/**
	 * Führt die übergebene Aufgabe auf der Datenbank aus und gibt bei Erfolg die Response der Aufgabe zurück.
	 * Hierfür wird der aktuelle SVWS-Benutzer anhand des HTTP-Requests ermittelt und überprüft, ob der
	 * Benutzer entweder Admin-Rechte oder eine der übergebenen Kompetenzen besitzt. Die
	 * dabei erstellte {@link DBEntityManager}-Instanz wird dabei für den Datenbankzugriff genutzt.
	 *
	 * Wichtig: Eine Transaktion für die Aufgabe wird erzeugt und von dieser Methode gehandhabt!
	 *
	 * @param task          die auszuführende Aufgabe
	 * @param request       das HTTP-Request-Objekt
	 * @param mode          der benötigte Server-Mode für den API-Zugriff
	 * @param kompetenzen   die zu prüfenden Kompetenzen
	 *
	 * @return die Response zu der Aufgabe
	 */
	public static Response runWithTransaction(final Function<DBEntityManager, Response> task, final HttpServletRequest request,
			final ServerMode mode, final BenutzerKompetenz... kompetenzen) {
		try (DBEntityManager conn = getDBConnection(request, mode, kompetenzen)) {
			return runWithTransaction(task, conn);
		}
	}


	/**
	 * Führt die übergebene Aufgabe auf der Datenbank aus und gibt bei Erfolg die Response der Aufgabe zurück.
	 * Hierfür wird der aktuelle SVWS-Benutzer anhand des HTTP-Requests ermittelt und überprüft, ob der
	 * Benutzer entweder Admin-Rechte oder eine der übergebenen Kompetenzen besitzt.
	 * Bei dieser Methode wird auch der Zugriff von dem Benutzer mit der übergebenen Benutzer-ID erlaubt.
	 * Die dabei erstellte {@link DBEntityManager}-Instanz wird dabei für den Datenbankzugriff genutzt.
	 *
	 * Wichtig: Eine Transaktion für die Aufgabe wird erzeugt und von dieser Methode gehandhabt!
	 *
	 * @param task          die auszuführende Aufgabe
	 * @param request       das HTTP-Request-Objekt
	 * @param mode          der benötigte Server-Mode für den API-Zugriff
	 * @param user_id       die zu prüfende Benutzer-ID (ist dies die ID des angemeldeten Benutzers?)
	 * @param kompetenzen   die zu prüfenden Kompetenzen
	 *
	 * @return die Response zu der Aufgabe
	 */
	public static Response runWithTransactionAllowSelf(final Function<DBEntityManager, Response> task, final HttpServletRequest request,
			final ServerMode mode, final long user_id, final BenutzerKompetenz... kompetenzen) {
		try (DBEntityManager conn = getDBConnectionAllowSelf(request, mode, user_id, kompetenzen)) {
			return runWithTransaction(task, conn);
		}
	}

}
