package de.svws_nrw.data.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.data.BenutzerKennwort;
import de.svws_nrw.core.data.benutzer.BenutzerListeEintrag;
import de.svws_nrw.core.data.db.SchemaListeEintrag;
import de.svws_nrw.core.data.schule.SchuleInfo;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.DBException;
import de.svws_nrw.db.dto.current.views.benutzer.DTOViewBenutzerdetails;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.dto.DTOInformationSchema;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.db.utils.schema.DBSchemaManager;
import de.svws_nrw.db.utils.schema.DBSchemaStatus;
import de.svws_nrw.db.utils.schema.DBSchemaVersion;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse stellt Methoden für den Zugriff auf ein Schema für die
 * SVWS-DB zur Verfügung.
 */
public final class DBUtilsSchema {

	private DBUtilsSchema() {
		throw new IllegalStateException("Instantiation of " + DBUtilsSchema.class.getName() + " not allowed");
	}

	/**
	 * Prüft, ob die übergebenen Credentials den Zugriff auf die Schema-Informationen
	 * der Datenbank zulassen.
	 *
	 * @param credentials   die Benutzer-Credentials
	 *
	 * @return true, falls die Credentials den Zugriff zulassen
	 */
	public static boolean checkDBPassword(final BenutzerKennwort credentials) {
		if (credentials == null)
			return false;
		DBConfig dbconfig = SVWSKonfiguration.get().getRootDBConfig(credentials.user, credentials.password);
		switch (dbconfig.getDBDriver()) {
			case MYSQL, MARIA_DB:
				dbconfig = dbconfig.switchSchema("information_schema");
				break;
			case MDB:
				dbconfig = dbconfig.switchSchema("PUBLIC");
				break;
			case MSSQL:
				dbconfig = dbconfig.switchSchema("master");
				break;
			case SQLITE:
				dbconfig = dbconfig.switchSchema("");
				break;
		}
		final Benutzer user;
		try {
			user = Benutzer.create(dbconfig);
		} catch (@SuppressWarnings("unused") final DBException db) {
			return false;
		}
		try (DBEntityManager em = user.getEntityManager()) {
			return (em != null);
		} catch (@SuppressWarnings("unused") final Exception pe) {
			return false;
		}
	}



	/**
	 * Aktualisiert das Schema, mit dem der angebenene Benutzer angemeldet ist auf die angegebene Revision.
	 *
	 * @param user       der angemeldete Benutzer mit der zugehörigen Datenbank-Verbindung
	 * @param revision   die Datenbank-Revision, auf die aktualisiert werden soll
	 *
	 * @return der Log der Update-Operation
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static LogConsumerList updateSchema(final Benutzer user, final long revision) throws ApiOperationException {
		// Ermittle die Revision, auf die aktualisiert werden soll. Hier wird ggf. eine negative Revision als neueste Revision interpretiert
		final long max_revision = (SVWSKonfiguration.get().getServerMode() == ServerMode.STABLE)
				? SchemaRevisionen.maxRevision.revision
				: SchemaRevisionen.maxDeveloperRevision.revision;
		long rev = revision;
		if (rev < 0)
			rev = max_revision;
		if (rev > max_revision)
			throw new ApiOperationException(Status.BAD_REQUEST);

		// Erzeuge einen Logger für das Update
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);

		// Erzeuge einen Schema-Manager, der die Aktualisierung des DB-Schema durchführt
		final DBSchemaManager manager = DBSchemaManager.create(user, true, logger);
		if (manager == null)
			throw new ApiOperationException(Status.FORBIDDEN);

		// Prüfe, ob das Schema aktuell ist
		if (!manager.updater.isUptodate(rev, false)) {
			// Prüfe, ob die angegebene Revision überhaupt ein Update erlaubt -> wenn nicht, dann liegt ein BAD_REQUEST (400) vor
			if (!manager.updater.isUpdatable(rev, false))
				throw new ApiOperationException(Status.BAD_REQUEST);

			// Führe die Aktualisierung durch
			final boolean success = manager.updater.update(user, rev, false, true);
			if (!success)
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
		}

		// return log from logger
		return log;
	}


	/**
	 * Bestimmt die Liste der über die Datenbankverbindung verfügbaren SVWS-Schemata.
	 *
	 * @param conn   die Datenbankverbindung
	 * @param nurSVWSSchemas    gibt an, ob nur SVWS-Schema zurückgegeben werden sollen oder alle
	 *
	 * @return die Liste der SVWS-Schema-Einträge
	 */
	public static List<SchemaListeEintrag> getSVWSSchemaListe(final DBEntityManager conn, final boolean nurSVWSSchemas) {
		// Lese zunächst alle Schemata in der DB ein. Dies können auch Schemata sein, die keine SVWS-Server-Schemata sind!
		final List<String> all = DTOInformationSchema.queryNames(conn);
		final ArrayList<SchemaListeEintrag> result = new ArrayList<>();
		final SVWSKonfiguration config = SVWSKonfiguration.get();
		// Filtere alle Schemata heraus, die gültige SVWS-Schemata sind.
		for (final String schemaname : all) {
			final DBSchemaStatus status = DBSchemaStatus.read(conn.getUser(), schemaname);
			final DBSchemaVersion version = status.getVersion();
			if ((version != null) && (version.getRevisionOrDefault(Integer.MIN_VALUE) != Integer.MIN_VALUE)) {
				final String schemanameConfig = config.getSchemanameCaseConfig(schemaname);
				final DBConfig dbconfig = config.getDBConfig(schemanameConfig);
				final SchemaListeEintrag schemainfo = new SchemaListeEintrag();
				schemainfo.name = (schemanameConfig == null) ? schemaname : schemanameConfig;
				schemainfo.username = (dbconfig == null) ? "" : dbconfig.getUsername();
				schemainfo.isSVWS = true;
				schemainfo.revision = version.getRevisionOrDefault(-1);
				schemainfo.isTainted = version.isTainted();
				schemainfo.isInConfig = (schemanameConfig != null);
				schemainfo.isDeactivated = config.isDeactivatedSchema(schemaname);
				result.add(schemainfo);
			} else if (!nurSVWSSchemas) { // Kein gültiges SVWS-Schema, prüfe das nächste Schema...
				final String schemanameConfig = config.getSchemanameCaseConfig(schemaname);
				final DBConfig dbconfig = config.getDBConfig(schemanameConfig);
				final SchemaListeEintrag schemainfo = new SchemaListeEintrag();
				schemainfo.name = (schemanameConfig == null) ? schemaname : schemanameConfig;
				schemainfo.username = (dbconfig == null) ? "" : dbconfig.getUsername();
				schemainfo.isSVWS = false;
				schemainfo.revision = -1;
				schemainfo.isTainted = true;
				schemainfo.isInConfig = (schemanameConfig != null);
				schemainfo.isDeactivated = config.isDeactivatedSchema(schemaname);
				result.add(schemainfo);
			}
		}
		return result;
	}


	/**
	 * Prüft, ob es sich bei dem übergebenen Schema-Namen um einen Namen für ein
	 * SVWS-Schema handelt und gibt bei Erfolg ein Objekt für den Zugriff auf den
	 * Schema-Status zurück..
	 *
	 * @param conn         die Datenbankverbindung
	 * @param schemaname   der Name des Schemas
	 *
	 * @return ein Objekt für den Zugriff auf den Schema-Status
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static DBSchemaStatus getSchemaStatus(final DBEntityManager conn, final String schemaname) throws ApiOperationException {
		final List<String> schemata = DTOInformationSchema.queryNames(conn);
		final Set<String> setSchemata = schemata.stream().map(String::toLowerCase).collect(Collectors.toSet());
		if (!setSchemata.contains(schemaname.toLowerCase()))
			throw new ApiOperationException(Status.FORBIDDEN, "Der Datenbankbenutzer hat keine Zugriffsrechte auf das Schema %s.".formatted(schemaname));
		final DBSchemaStatus status = DBSchemaStatus.read(conn.getUser(), schemaname);
		final DBSchemaVersion version = status.getVersion();
		if (version == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Schema %s ist kein gültiges SVWS-Schema".formatted(schemaname));
		return status;
	}


	/**
	 * Ermittelt die Informationen zu der Schule aus einem SVWS-Schema
	 *
	 * @param conn         die Datenbankverbindung
	 * @param schemaname   der Name des Schemas
	 *
	 * @return die Informationen zu der Schule in dem SVWS-Schema
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static SchuleInfo getSchuleInfo(final DBEntityManager conn, final String schemaname) throws ApiOperationException {
		final DBSchemaStatus status = getSchemaStatus(conn, schemaname);
		final SchuleInfo schuleInfo = status.getSchuleInfo();
		if (schuleInfo == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Das Schema %s ist noch nicht mit den Informationen einer Schule initialisiert".formatted(schemaname));
		return status.getSchuleInfo();
	}


	/**
	 * Prüfe, ob das Schema ein aktuelles SVWS-Schema ist und gibt den zugehörigen Datenbank-Benutzer
	 * für den Zugriff auf das SVWS-Schea zurück.
	 *
	 * @param conn         die usprüngliche Datenbankverbindung zu dem Information-Schema
	 * @param schemaname   der Name des Schemas
	 *
	 * @return der Datenbank-Benutzer für die neue Verbindung
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static Benutzer getBenutzerFuerSVWSSchema(final DBEntityManager conn, final String schemaname) throws ApiOperationException {
		final List<SchemaListeEintrag> schemata = getSVWSSchemaListe(conn, true);
		SchemaListeEintrag schema = null;
		for (final SchemaListeEintrag s : schemata) {
			if (s.name.equalsIgnoreCase(schemaname)) {
				schema = s;
				break;
			}
		}
		if (schema == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Es existiert kein SVWS-Schema mit dem Namen %s.".formatted(schemaname));
		final SchemaRevisionen rev = (SVWSKonfiguration.get().getServerMode() == ServerMode.DEV)
				? SchemaRevisionen.maxDeveloperRevision : SchemaRevisionen.maxRevision;
		if (schema.revision < rev.revision)
			throw new ApiOperationException(Status.BAD_REQUEST, "Das SVWS-Schema %s ist nicht aktuell (%d).".formatted(schemaname, schema.revision));
		if (schema.revision > rev.revision)
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Das SVWS-Schema %s ist neuer (%d) als die vom Server unterstützte Version (%d).".formatted(schemaname, schema.revision, rev.revision));
		try {
			return conn.getUser().connectTo(schemaname);
		} catch (@SuppressWarnings("unused") final DBException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Fehler beim Zugriff auf das SVWS-Schema %s.".formatted(schemaname));
		}
	}


	/**
	 * Ermittelt die Informationen zu den administrativen Benutzern in einem aktuellen SVWS-Schema
	 *
	 * @param conn         die Datenbankverbindung
	 * @param schemaname   der Name des Schemas
	 *
	 * @return die Informationen zu den administrativen Benutzern in dem SVWS-Schema
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static List<BenutzerListeEintrag> getAdmins(final DBEntityManager conn, final String schemaname) throws ApiOperationException {
		final Benutzer neu = getBenutzerFuerSVWSSchema(conn, schemaname);
		try (DBEntityManager schemaConn = neu.getEntityManager()) {
			final List<DTOViewBenutzerdetails> admins = schemaConn.queryList(DTOViewBenutzerdetails.QUERY_BY_ISTADMIN, DTOViewBenutzerdetails.class, true);
			final List<BenutzerListeEintrag> result = new ArrayList<>();
			for (final DTOViewBenutzerdetails admin : admins) {
				final BenutzerListeEintrag b = new BenutzerListeEintrag();
				b.id = admin.ID;
				b.typ = admin.Typ.id;
				b.typID = admin.TypID;
				b.anzeigename = admin.AnzeigeName;
				b.name = admin.Benutzername;
				b.istAdmin = admin.IstAdmin;
				b.idCredentials = admin.credentialID;
				result.add(b);
			}
			return result;
		}
	}

}
