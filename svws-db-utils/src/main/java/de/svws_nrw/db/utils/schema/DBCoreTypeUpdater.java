package de.svws_nrw.db.utils.schema;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.asd.data.NoteKatalogEintrag;
import de.svws_nrw.asd.data.fach.FachKatalogEintrag;
import de.svws_nrw.asd.data.fach.FachgruppeKatalogEintrag;
import de.svws_nrw.asd.data.schueler.HerkunftBildungsgangKatalogEintrag;
import de.svws_nrw.asd.data.schueler.HerkunftBildungsgangTypKatalogEintrag;
import de.svws_nrw.core.data.schule.HerkunftSchulformKatalogEintrag;
import de.svws_nrw.core.data.schule.HerkunftSonstigeKatalogEintrag;
import de.svws_nrw.core.data.schule.HerkunftsartKatalogEintrag;
import de.svws_nrw.core.data.schule.HerkunftsartKatalogEintragBezeichnung;
import de.svws_nrw.asd.data.schule.SchulformKatalogEintrag;
import de.svws_nrw.asd.data.schule.SchulformSchulgliederung;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.KursFortschreibungsart;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.core.types.PersonalTyp;
import de.svws_nrw.asd.types.fach.Fachgruppe;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.klassen.Klassenart;
import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.asd.types.lehrer.LehrerLeitungsfunktion;
import de.svws_nrw.asd.types.schueler.HerkunftBildungsgang;
import de.svws_nrw.asd.types.schueler.HerkunftBildungsgangTyp;
import de.svws_nrw.core.types.schueler.Einschulungsart;
import de.svws_nrw.core.types.schueler.HerkunftSchulform;
import de.svws_nrw.core.types.schueler.HerkunftSonstige;
import de.svws_nrw.core.types.schueler.Herkunftsarten;
import de.svws_nrw.asd.types.schule.AllgemeinbildendOrganisationsformen;
import de.svws_nrw.core.types.schule.AllgemeineMerkmale;
import de.svws_nrw.asd.types.schule.BerufskollegOrganisationsformen;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.types.schule.Religion;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.types.schule.WeiterbildungskollegOrganisationsformen;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.DBException;
import de.svws_nrw.db.dto.current.schema.DTOSchemaCoreTypeVersion;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleCoreType;

/**
 * Diese Klasse dient der Aktualisierung von Core-Type-Daten in der
 * Datenbank. Diese Updates werden von Prozessen, wie z.B. dem Erstellen,
 * Migrieren, Aktualisieren, Importieren und Exportieren von DB-Schemata
 * durchgeführt. Eine Prüfung beim Serverstart im Rahmen des dortigen
 * Update-Prozesses ist wünschenswert, da die Core-Typs nicht zwingend
 * an neue Datenbank-Revisionen geknüpft sind.
 */
public class DBCoreTypeUpdater {

	/** Der Schema-Manager, welcher für die Updates verwendet wird. */
	private final DBSchemaManager _schemaManager;

	/** Ein Logger, um die Abläufe bei dem Update-Prozess zu loggen. */
	private final Logger _logger;

	/** Der Status des Datenbank-Schema. */
	private final DBSchemaStatus _status;

	/**
	 * Ein Record mit dem Tabellen-Namen, der Version des Core-Types und einem Lambda für die
	 * Aktualisierung der Tabelle mit den Core-Type-Daten.
	 *
	 * @param name     der Name der Core-Type-Tabelle
	 * @param version  die Version des Core-Types
	 * @param updater  der Lambda-Ausdruck zum Aktualisieren der DB-Tabellen mit Daten des Core-Types
	 */
	private record CoreTypeTable(String name, long version, BiConsumer<DBEntityManager, Logger> updater) {
		/**/
	}

	/** Eine Liste von Records mit den zu aktualisierenden Tabellen - siehe auch {@link CoreTypeTable}. */
	private final ArrayList<CoreTypeTable> tables = new ArrayList<>();


	private static final String strInsertInto = "INSERT INTO ";
	private static final String strValues = "VALUES (";
	private static final String strAktualisiereTabelle = "Aktualisiere Core-Type in Tabelle ";
	private static final String strNullValue = "null,";
	private static final String strSpaltenNurKuerzel = "(Kuerzel) ";


	/**
	 * Erzeugt einen neuen {@link DBCoreTypeUpdater}.
	 *
	 * @param schemaManager   der Schema-Manager, welcher verwendet wird
	 * @param returnOnError   gibt an, ob Operatioen bei Einzelfehlern abgebrochen werden sollen
	 */
	DBCoreTypeUpdater(final DBSchemaManager schemaManager, final boolean returnOnError) {
		this._schemaManager = schemaManager;
		this._logger = schemaManager.getLogger();
		this._status = schemaManager.getSchemaStatus();
		tables.add(new CoreTypeTable("Fachgruppen", Fachgruppe.data().getVersion(), updateFachgruppen));
		tables.add(new CoreTypeTable("KursFortschreibungsarten", KursFortschreibungsart.VERSION, updateKursFortschreibungsarten));
		tables.add(new CoreTypeTable("Schulformen", Schulform.data().getVersion(), updateSchulformen));
		tables.add(new CoreTypeTable("PersonalTypen", PersonalTyp.VERSION, updatePersonalTypen));
		tables.add(new CoreTypeTable("Nationalitaeten_Keys", Nationalitaeten.data().getVersion(), updateNationalitaeten_Keys));
		tables.add(new CoreTypeTable("Jahrgaenge_Keys", Jahrgaenge.data().getVersion(), updateJahrgaengeKeys));
		tables.add(new CoreTypeTable("Noten", Note.data().getVersion(), updateNoten));
		final long versionHerkuenfte = HerkunftSonstige.VERSION + HerkunftBildungsgang.data().getVersion() + HerkunftBildungsgangTyp.data().getVersion()
				+ HerkunftSchulform.VERSION;
		tables.add(new CoreTypeTable("Herkunft", versionHerkuenfte, updateHerkuenfte));
		tables.add(new CoreTypeTable("Herkunft_Keys", versionHerkuenfte, updateHerkuenfteKeys));
		tables.add(new CoreTypeTable("Herkunft_Schulformen", versionHerkuenfte, updateHerkuenfteSchulformen));
		tables.add(new CoreTypeTable("Herkunftsart", Herkunftsarten.VERSION, updateHerkunftsarten));
		tables.add(new CoreTypeTable("Herkunftsart_Keys", Herkunftsarten.VERSION, updateHerkunftsartenKeys));
		tables.add(new CoreTypeTable("Herkunftsart_Schulformen", Herkunftsarten.VERSION, updateHerkunftsartenSchulformen));
		tables.add(new CoreTypeTable("KlassenartenKatalog_Keys", Klassenart.data().getVersion(), updateKlassenartenKeys));
		tables.add(new CoreTypeTable("KursartenKatalog_Keys", ZulaessigeKursart.data().getVersion(), updateKursartenKeys));
		tables.add(new CoreTypeTable("FachKatalog", Fach.data().getVersion(), updateZulaessigeFaecher));
		tables.add(new CoreTypeTable("FachKatalog_Keys", Fach.data().getVersion(), updateZulaessigeFaecherKeys));
		tables.add(new CoreTypeTable("FachKatalog_Schulformen", Fach.data().getVersion(), updateZulaessigeFaecherSchulformen));
		tables.add(new CoreTypeTable("EinschulungsartKatalog_Keys", Einschulungsart.VERSION, updateEinschulungsartenKeys));
		tables.add(new CoreTypeTable("Religionen_Keys", Religion.data().getVersion(), updateReligionenKeys));
		tables.add(new CoreTypeTable("AllgemeineMerkmaleKatalog_Keys", AllgemeineMerkmale.VERSION, updateAllgemeineMerkmaleKeys));
		tables.add(new CoreTypeTable("OrganisationsformenKatalog_Keys", BerufskollegOrganisationsformen.data().getVersion()
				+ WeiterbildungskollegOrganisationsformen.data().getVersion() + AllgemeinbildendOrganisationsformen.data().getVersion(),
				updateOrganisationsformenKeys));
		tables.add(new CoreTypeTable("LehrerLeitungsfunktion_Keys", LehrerLeitungsfunktion.data().getVersion(), updateLehrerLeitungsfunktionenKeys));
	}


	/**
	 * Prüft, ob die Core-Types aktuell sind, d.h. die Version in den Core-Types mit der
	 * Version in der Datenbank übereinstimmt.
	 *
	 * @return true, falls die Core-Types in der DB aktuell sind, sonst false
	 *
	 * @throws DBException   wenn ein Verbindungsfehler auftritt
	 */
	public boolean isUptodate() throws DBException {
		final DBEntityManager conn = _schemaManager.getConnection();
		_status.update(conn);
		long status_revision;
		try {
			status_revision = _status.version.getRevision();
		} catch (@SuppressWarnings("unused") final Exception e) {
			status_revision = 0;
		}
		for (final SchemaTabelle tab : Schema.getTabellen(status_revision)) {
			if (!tab.hasCoreType())
				continue;
			final DTOSchemaCoreTypeVersion v = _status.getCoreTypeVersion(conn, tab.name());
			if (v == null)
				return false; // Bisher keine Version gespeichert - Update also nötig
			if (Long.compare(tab.getCoreType().getCoreTypeVersion(), v.Version) > 0)
				return false;  // Die Version des Core-Types ist größer als die Version in der DB -> Update also nötig
		}
		// TODO unten deprecated, oben aktuell
		for (final CoreTypeTable entry : tables)
			if ((entry.name == null) || (!pruefeVersion(conn, entry.name, entry.version)))
				return false;
		return true;
	}


	/**
	 * Prüft, ob eine Aktualisierung der Core-Types möglich ist,
	 * d.h. die Java-Core-Types gleich alt oder neuer sind als die in
	 * der Datenbank vorhandenen Core-Types. Ist dies nicht der
	 * Fall, so wird i.A. eine veraltete Server-Version mit einem neueren Schema
	 * verwendet.
	 *
	 * @param conn   die Datenbankverbindung
	 *
	 * @return true, falls eine Aktualisierung möglich ist, sonst false
	 */
	private boolean isUpdatable(final DBEntityManager conn) {
		// Prüfe zunächst, ob ein Update möglich ist
		_status.update(conn);
		long status_revision;
		try {
			status_revision = _status.version.getRevision();
		} catch (@SuppressWarnings("unused") final Exception e) {
			status_revision = 0;
		}
		for (final SchemaTabelle tab : Schema.getTabellen(status_revision)) {
			if (!tab.hasCoreType())
				continue;
			final DTOSchemaCoreTypeVersion v = _status.getCoreTypeVersion(conn, tab.name());
			if (v == null)
				continue; // Bisher keine Version gespeichert - Update also möglich
			if (Long.compare(tab.getCoreType().getCoreTypeVersion(), v.Version) < 0)
				return false;  // Die Version des Core-Types ist kleiner als die Version in der DB
		}
		// TODO unten deprecated, oben aktuell
		for (final CoreTypeTable entry : tables) {
			final DTOSchemaCoreTypeVersion v = _status.getCoreTypeVersion(conn, entry.name);
			if (v == null)
				continue; // Bisher keine Version gespeichert - Update also möglich
			if (Long.compare(entry.version, v.Version) < 0)
				return false;  // Die Version des Core-Types ist kleiner als die Version in der DB
		}
		return true;
	}


	/**
	 * Aktualisiert die Core-Types im Schema schrittweise auf die angegebene Revision.
	 *
	 * @param conn         die Datenbankverbindung ohne aktive Transaktion
	 * @param lockSchema   gibt an, on das Schema für den Update-Prozess gesperrt werden soll. Dies ist z.B. nicht
	 *                     notwendig, wenn der Update-Prozess im Rahmen einer Migration gestartet wird.
	 * @param rev          die Datenbank-Revision auf welche aktualisiert wird
	 *
	 * @return true im Erfolgsfall, sonst false
	 */
	public boolean updateNewTransaction(final DBEntityManager conn, final boolean lockSchema, final long rev) {
		try {
			conn.transactionBegin();
			boolean result = update(conn, lockSchema, rev);
			if (result && (!conn.transactionCommit()))
				result = false;
			return result;
		} catch (final Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			conn.transactionRollback();
		}
	}


	/**
	 * Aktualisiert die Core-Types im Schema schrittweise auf die angegebene Revision.
	 *
	 * @param conn         die Datenbankverbindung mit aktiver Transaktion
	 * @param lockSchema   gibt an, on das Schema für den Update-Prozess gesperrt werden soll. Dies ist z.B. nicht
	 *                     notwendig, wenn der Update-Prozess im Rahmen einer Migration gestartet wird.
	 * @param rev          die Datenbank-Revision auf welche aktualisiert wird
	 *
	 * @return true im Erfolgsfall, sonst false
	 */
	public boolean update(final DBEntityManager conn, final boolean lockSchema, final long rev) {
		// Sperre ggf. das Datenbankschema
		if ((lockSchema) && (!SVWSKonfiguration.get().lockSchema(_schemaManager.getSchemaStatus().schemaName))) {
			_logger.logLn("-> Update fehlgeschlagen! (Schema ist aktuell gesperrt und kann daher nicht aktualisiert werden)");
			return false;
		}

		try {
			long revision = rev;
			// Prüfe zunächst, ob ein Update möglich ist
			if (!isUpdatable(conn))
				throw new DBException("Core-Types können nicht aktualisiert werden, da die Core-Type-Version in der Datenbank neuer sind als die des Servers.");
			// Bestimme ggf. die aktuelle Datenbank-Revision
			if (revision < 0)
				revision = _status.getVersion().getRevisionOrDefault(-1);
			if (revision < 0)
				throw new DBException("Core-Types können nicht aktualisiert werden, da die Revision der Datenbank nicht bestimmt werden kann.");
			// Aktualisiere ggf. die Daten der einzelnen Core-Types
			final long status_revision = (_status.version == null) ? 0 : _status.version.getRevisionOrDefault(0);
			for (final SchemaTabelle tab : Schema.getTabellen(status_revision)) {
				if (!tab.hasCoreType())
					continue;
				final SchemaTabelleCoreType ct = tab.getCoreType();
				if (pruefeVersion(conn, tab.name(), ct.getCoreTypeVersion()))
					continue;
				_logger.logLn(strAktualisiereTabelle + tab.name());
				updateCoreTypeTabelle(conn, tab.name(), ct.getCoreTypeName(), ct.getCoreTypeVersion(),
						ct.getSQLInsert(status_revision, conn.getDBDriver() == DBDriver.SQLITE));
			}
			// TODO unten deprecated: Aktualisiere ggf. die Daten der einzelnen Core-Types
			for (final CoreTypeTable entry : tables)
				if (!pruefeVersion(conn, entry.name, entry.version))
					entry.updater.accept(conn, _logger);
			return true;
		} catch (final Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			// Entsperre ggf. das Datenbankschema
			if ((lockSchema) && (!SVWSKonfiguration.get().unlockSchema(_schemaManager.getSchemaStatus().schemaName)))
				_logger.logLn("-> Update evtl. fehlgeschlagen! (Fehler beim Freigeben des Datenbank-Schemas. Schema ist nicht gesperrt"
						+ " - dies wird an dieser Stelle nicht erwartet!)");
		}
	}

	/**
	 * Aktualisiert die Datenbank in Bezug auf den Core-Type in der angegebenen
	 * Tabelle mithilfe des übergebenen SQL-Befehls.
	 * Dabei wird in der aktiven Datenbank-Transaktion zunächst eine
	 * Lösch-Operation für alle Daten der Tabelle durchgeführt, dann ein flush und
	 * anschließend die Insert-Operation gefolgt von einem weiteren flush, bevor
	 * die Version in der Versions-Tabelle für die Core-Types aktualisiert wird
	 * und abschließend ein flush erfolgt.
	 * Die Operationen müssen sicherstellen, dass die referentielle
	 * Integrität nicht zerstört wird. Ist dies nicht der Fall, so wird
	 * die Transaktion nicht ausgeführt.
	 *
	 * @param conn        die Datenbankverbindung
	 * @param tabname     der Name der Tabelle
	 * @param typename    der Simple-Name des Core-Types
	 * @param version     die neu zu setzende Version des Core-Type
	 * @param sqlInsert   der Befehl zum Einfügen der Core-type-Daten
	 */
	private static void updateCoreTypeTabelle(final DBEntityManager conn, final String tabname, final String typename, final long version,
			final String sqlInsert) {
		// Aktualisiere die Daten des Core-Types
		conn.transactionNativeUpdate(sqlInsert);
		conn.transactionFlush();
		// Aktualsiere die Core-Type-Version in der entsprechenden Tabelle
		DTOSchemaCoreTypeVersion v = conn.queryByKey(DTOSchemaCoreTypeVersion.class, tabname);
		if (v == null) {
			v = new DTOSchemaCoreTypeVersion(tabname, typename, version);
		} else {
			v.Version = version;
		}
		conn.transactionPersist(v);
		conn.transactionFlush();
	}


	/**
	 * Aktualisiert die Tabelle für den Core-Type Fachgruppe.
	 */
	private final BiConsumer<DBEntityManager, Logger> updateFachgruppen = (final DBEntityManager conn, final Logger logger) -> {
		final String tabname = "Fachgruppen";
		logger.logLn(strAktualisiereTabelle + tabname);
		final StringBuilder sql = new StringBuilder();
		sql.append(strInsertInto);
		sql.append(tabname);
		sql.append(
				"(ID, Fachbereich, SchildFgID, FG_Bezeichnung, FG_Kuerzel, Schulformen, FarbeR, FarbeG, FarbeB, Sortierung, FuerZeugnis, gueltigVon, gueltigBis) ");
		boolean isFirst = true;
		for (final Fachgruppe fg : Fachgruppe.values()) {
			for (final FachgruppeKatalogEintrag fgke : fg.historie()) {
				if (isFirst) {
					sql.append(strValues);
					isFirst = false;
				} else
					sql.append(", (");
				sql.append(fgke.id).append(",");
				sql.append(fgke.nummer).append(",");
				sql.append(fgke.schluessel).append(",");
				sql.append("'").append(fgke.text).append("'").append(",");
				sql.append("'").append(fgke.kuerzel).append("'").append(",");
				sql.append("'").append(fgke.schulformen.stream().collect(Collectors.joining(","))).append("'").append(",");
				sql.append(fgke.farbe.red).append(",");
				sql.append(fgke.farbe.green).append(",");
				sql.append(fgke.farbe.blue).append(",");
				sql.append(fgke.sortierung).append(",");
				sql.append(fgke.fuerZeugnis ? 1 : 0).append(",");
				sql.append(fgke.gueltigVon).append(",");
				sql.append(fgke.gueltigBis).append(")");
			}
		}
		if (conn.getDBDriver() != DBDriver.SQLITE)
			sql.append(
					" ON DUPLICATE KEY UPDATE ID=VALUES(ID), Fachbereich=VALUES(Fachbereich), SchildFgID=VALUES(SchildFgID), FG_Bezeichnung=VALUES(FG_Bezeichnung), FG_Kuerzel=VALUES(FG_Kuerzel), Schulformen=VALUES(Schulformen), FarbeR=VALUES(FarbeR), FarbeG=VALUES(FarbeG), FarbeB=VALUES(FarbeB), Sortierung=VALUES(Sortierung), FuerZeugnis=VALUES(FuerZeugnis), gueltigVon=VALUES(gueltigVon), gueltigBis=VALUES(gueltigBis)");
		updateCoreTypeTabelle(conn, tabname, Fachgruppe.class.getCanonicalName(), Fachgruppe.data().getVersion(), sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type Jahrgaenge.
	 */
	private final BiConsumer<DBEntityManager, Logger> updateJahrgaengeKeys = (final DBEntityManager conn, final Logger logger) -> {
		final String tabname = "Jahrgaenge_Keys";
		logger.logLn(strAktualisiereTabelle + tabname);
		final StringBuilder sql = new StringBuilder();
		sql.append(strInsertInto);
		sql.append(tabname);
		sql.append(strSpaltenNurKuerzel);
		final Jahrgaenge[] values = Jahrgaenge.values();
		final String[] jg_kuerzel = Arrays.stream(values).flatMap(jg -> jg.historie().stream()).map(jg -> jg.kuerzel).collect(Collectors.toSet())
				.toArray(new String[0]);
		boolean isFirst = true;
		for (final String kuerzel : jg_kuerzel) {
			sql.append(isFirst ? strValues : ", (");
			isFirst = false;
			sql.append("'").append(kuerzel).append("'").append(")");
		}
		if (conn.getDBDriver() != DBDriver.SQLITE)
			sql.append(" ON DUPLICATE KEY UPDATE Kuerzel=VALUES(Kuerzel)");
		updateCoreTypeTabelle(conn, tabname, Jahrgaenge.class.getCanonicalName(), Jahrgaenge.data().getVersion(), sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type KursFortschreibungsart.
	 */
	private final BiConsumer<DBEntityManager, Logger> updateKursFortschreibungsarten = (final DBEntityManager conn, final Logger logger) -> {
		final String tabname = "KursFortschreibungsarten";
		logger.logLn(strAktualisiereTabelle + tabname);
		final StringBuilder sql = new StringBuilder();
		sql.append(strInsertInto);
		sql.append(tabname);
		sql.append("(ID, Kuerzel, Bezeichnung, gueltigVon, gueltigBis) ");
		final KursFortschreibungsart[] values = KursFortschreibungsart.values();
		for (int i = 0; i < values.length; i++) {
			final KursFortschreibungsart p = values[i];
			sql.append((i == 0) ? strValues : ", (");
			sql.append(p.id).append(",");
			sql.append("'").append(p.kuerzel).append("'").append(",");
			sql.append("'").append(p.beschreibung).append("'").append(",");
			sql.append(p.gueltigVon).append(",");
			sql.append(p.gueltigBis).append(")");
		}
		if (conn.getDBDriver() != DBDriver.SQLITE)
			sql.append(
					" ON DUPLICATE KEY UPDATE ID=VALUES(ID), Kuerzel=VALUES(Kuerzel), Bezeichnung=VALUES(Bezeichnung), gueltigVon=VALUES(gueltigVon), gueltigBis=VALUES(gueltigBis)");
		updateCoreTypeTabelle(conn, tabname, KursFortschreibungsart.class.getCanonicalName(), KursFortschreibungsart.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type Nationalitaeten.
	 */
	private final BiConsumer<DBEntityManager, Logger> updateNationalitaeten_Keys = (final DBEntityManager conn, final Logger logger) -> {
		final String tabname = "Nationalitaeten_Keys";
		logger.logLn(strAktualisiereTabelle + tabname);
		final StringBuilder sql = new StringBuilder();
		sql.append(strInsertInto);
		sql.append(tabname);
		sql.append("(DEStatisCode) ");
		final List<String> codes = Arrays.stream(Nationalitaeten.values()).flatMap(nat -> nat.historie().stream()).map(kat -> kat.codeDEStatis).distinct().toList();
		boolean isFirst = true;
		for (final String code : codes) {
			sql.append(isFirst ? strValues : ", (");
			isFirst = false;
			sql.append("'").append(code).append("')");
		}
		if (conn.getDBDriver() != DBDriver.SQLITE)
			sql.append(" ON DUPLICATE KEY UPDATE DEStatisCode=VALUES(DEStatisCode)");
		updateCoreTypeTabelle(conn, tabname, Nationalitaeten.class.getCanonicalName(), Nationalitaeten.data().getVersion(), sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type Note.
	 */
	private final BiConsumer<DBEntityManager, Logger> updateNoten = (final DBEntityManager conn, final Logger logger) -> {
		final String tabname = "Noten";
		logger.logLn(strAktualisiereTabelle + tabname);
		final StringBuilder sql = new StringBuilder();
		sql.append(strInsertInto);
		sql.append(tabname);
		sql.append("(ID, Kuerzel, IstTendenznote, Text, AufZeugnis, Notenpunkte, TextLaufbahnSII, AufLaufbahnSII, Sortierung, gueltigVon, gueltigBis) ");
		boolean isFirst = true;
		for (final Note n : Note.values()) {
			if (n == Note.KEINE)
				continue;
			for (final NoteKatalogEintrag nke : n.historie()) {
				sql.append(isFirst ? strValues : ", (");
				isFirst = false;
				sql.append(nke.id).append(",");
				sql.append("'").append(nke.kuerzel).append("'").append(",");
				sql.append(Note.eintragHatTendenz(nke)).append(",");
				sql.append("'").append(nke.text).append("'").append(",");
				sql.append("--------------------".equals(nke.textZeugnis)).append(",");
				sql.append(nke.notenpunkte).append(",");
				if (nke.notenpunkte != null)
					sql.append("'").append(String.format("%02d", nke.notenpunkte)).append("'").append(",");
				else if ((n == Note.E3_TEILGENOMMEN) || (n == Note.E2_MIT_ERFOLG_TEILGENOMMEN) || (n == (Note.E3_TEILGENOMMEN)) || (n == Note.ATTEST))
					sql.append("'").append(nke.kuerzel).append("'").append(",");
				else
					sql.append(strNullValue);
				sql.append((nke.notenpunkte != null) || (n == Note.E3_TEILGENOMMEN) || (n == Note.E2_MIT_ERFOLG_TEILGENOMMEN)
						|| (n == (Note.E3_TEILGENOMMEN)) || (n == Note.ATTEST)).append(",");
				sql.append(nke.sortierung).append(",");
				sql.append(nke.gueltigVon).append(",");
				sql.append(nke.gueltigBis).append(")");
			}
		}
		if (conn.getDBDriver() != DBDriver.SQLITE)
			sql.append(
					" ON DUPLICATE KEY UPDATE ID=VALUES(ID), Kuerzel=VALUES(Kuerzel), IstTendenznote=VALUES(IstTendenznote), Text=VALUES(Text), AufZeugnis=VALUES(AufZeugnis), Notenpunkte=VALUES(Notenpunkte), TextLaufbahnSII=VALUES(TextLaufbahnSII), AufLaufbahnSII=VALUES(AufLaufbahnSII), Sortierung=VALUES(Sortierung), gueltigVon=VALUES(gueltigVon), gueltigBis=VALUES(gueltigBis)");
		updateCoreTypeTabelle(conn, tabname, Note.class.getCanonicalName(), Note.data().getVersion(), sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type PersonalTyp.
	 */
	private final BiConsumer<DBEntityManager, Logger> updatePersonalTypen = (final DBEntityManager conn, final Logger logger) -> {
		final String tabname = "PersonalTypen";
		logger.logLn(strAktualisiereTabelle + tabname);
		final StringBuilder sql = new StringBuilder();
		sql.append(strInsertInto);
		sql.append(tabname);
		sql.append("(ID, Kuerzel, Bezeichnung, gueltigVon, gueltigBis) ");
		final PersonalTyp[] values = PersonalTyp.values();
		for (int i = 0; i < values.length; i++) {
			final PersonalTyp p = values[i];
			sql.append((i == 0) ? strValues : ", (");
			sql.append(p.id).append(",");
			sql.append("'").append(p.kuerzel).append("'").append(",");
			sql.append("'").append(p.bezeichnung).append("'").append(",");
			sql.append(p.gueltigVon).append(",");
			sql.append(p.gueltigBis).append(")");
		}
		if (conn.getDBDriver() != DBDriver.SQLITE)
			sql.append(
					" ON DUPLICATE KEY UPDATE ID=VALUES(ID), Kuerzel=VALUES(Kuerzel), Bezeichnung=VALUES(Bezeichnung), gueltigVon=VALUES(gueltigVon), gueltigBis=VALUES(gueltigBis)");
		updateCoreTypeTabelle(conn, tabname, PersonalTyp.class.getCanonicalName(), PersonalTyp.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type Schulform.
	 */
	private final BiConsumer<DBEntityManager, Logger> updateSchulformen = (final DBEntityManager conn, final Logger logger) -> {
		final String tabname = "Schulformen";
		logger.logLn(strAktualisiereTabelle + tabname);
		final StringBuilder sql = new StringBuilder();
		sql.append(strInsertInto);
		sql.append(tabname);
		sql.append("(ID, Kuerzel, Nummer, Bezeichnung, HatGymOb, gueltigVon, gueltigBis) ");
		final Schulform[] values = Schulform.values();
		boolean isFirst = true;
		for (final Schulform schulform : values) {
			for (final SchulformKatalogEintrag sf : schulform.historie()) {
				sql.append(isFirst ? strValues : ", (");
				isFirst = false;
				sql.append(sf.id).append(",");
				sql.append("'").append(sf.kuerzel).append("'").append(",");
				if (sf.schluessel == null)
					sql.append(strNullValue);
				else
					sql.append("'").append(sf.schluessel).append("'").append(",");
				sql.append("'").append(sf.text).append("'").append(",");
				sql.append(sf.hatGymOb).append(",");
				sql.append(sf.gueltigVon).append(",");
				sql.append(sf.gueltigBis).append(")");
			}
		}
		if (conn.getDBDriver() != DBDriver.SQLITE)
			sql.append(
					" ON DUPLICATE KEY UPDATE ID=VALUES(ID), Kuerzel=VALUES(Kuerzel), Nummer=VALUES(Nummer), Bezeichnung=VALUES(Bezeichnung), HatGymOb=VALUES(HatGymOb), gueltigVon=VALUES(gueltigVon), gueltigBis=VALUES(gueltigBis)");
		updateCoreTypeTabelle(conn, tabname, Schulform.class.getCanonicalName(), Schulform.data().getVersion(), sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type Herkunft.
	 */
	private final BiConsumer<DBEntityManager, Logger> updateHerkuenfte = (final DBEntityManager conn, final Logger logger) -> {
		final String tabname = "Herkunft";
		logger.logLn(strAktualisiereTabelle + tabname);
		final StringBuilder sql = new StringBuilder();
		sql.append(strInsertInto);
		sql.append(tabname);
		sql.append("(ID, Kuerzel, Beschreibung, gueltigVon, gueltigBis) ");
		boolean isFirst = true;
		for (final HerkunftSonstige herkunft : HerkunftSonstige.values()) {
			for (final HerkunftSonstigeKatalogEintrag h : herkunft.historie) {
				sql.append(isFirst ? strValues : ", (");
				isFirst = false;
				sql.append(h.id + 1000000000L).append(",");
				sql.append("'").append(h.kuerzel).append("'").append(",");
				sql.append("'").append(h.beschreibung.replace("'", "''")).append("'").append(",");
				sql.append(h.gueltigVon).append(",");
				sql.append(h.gueltigBis).append(")");
			}
		}
		for (final HerkunftBildungsgang herkunft : HerkunftBildungsgang.values()) {
			for (final HerkunftBildungsgangKatalogEintrag h : herkunft.historie()) {
				sql.append(isFirst ? strValues : ", (");
				isFirst = false;
				sql.append(h.id + 2000000000L).append(",");
				sql.append("'").append(h.kuerzel).append("'").append(",");
				sql.append("'").append(h.text.replace("'", "''")).append("'").append(",");
				sql.append(h.gueltigVon).append(",");
				sql.append(h.gueltigBis).append(")");
			}
		}
		for (final HerkunftBildungsgangTyp herkunft : HerkunftBildungsgangTyp.values()) {
			for (final HerkunftBildungsgangTypKatalogEintrag h : herkunft.historie()) {
				sql.append(isFirst ? strValues : ", (");
				isFirst = false;
				sql.append(h.id + 3000000000L).append(",");
				sql.append("'").append(h.kuerzel).append("'").append(",");
				sql.append("'").append(h.text.replace("'", "''")).append("'").append(",");
				sql.append(h.gueltigVon).append(",");
				sql.append(h.gueltigBis).append(")");
			}
		}
		for (final HerkunftSchulform herkunft : HerkunftSchulform.values()) {
			for (final HerkunftSchulformKatalogEintrag h : herkunft.historie) {
				sql.append(isFirst ? strValues : ", (");
				isFirst = false;
				sql.append(h.id + 4000000000L).append(",");
				sql.append("'").append(h.kuerzel).append("'").append(",");
				sql.append("'").append(h.beschreibung.replace("'", "''")).append("'").append(",");
				sql.append(h.gueltigVon).append(",");
				sql.append(h.gueltigBis).append(")");
			}
		}
		if (conn.getDBDriver() != DBDriver.SQLITE)
			sql.append(
					" ON DUPLICATE KEY UPDATE ID=VALUES(ID), Kuerzel=VALUES(Kuerzel), Beschreibung=VALUES(Beschreibung), gueltigVon=VALUES(gueltigVon), gueltigBis=VALUES(gueltigBis)");
		final long version = HerkunftSonstige.VERSION + HerkunftBildungsgang.data().getVersion() + HerkunftBildungsgangTyp.data().getVersion()
				+ HerkunftSchulform.VERSION;
		updateCoreTypeTabelle(conn, tabname, "de.svws_nrw.core.types.schueler.Herkunft", version, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type Herkunft.
	 */
	private final BiConsumer<DBEntityManager, Logger> updateHerkuenfteKeys = (final DBEntityManager conn, final Logger logger) -> {
		final String tabname = "Herkunft_Keys";
		logger.logLn(strAktualisiereTabelle + tabname);
		final StringBuilder sql = new StringBuilder();
		sql.append(strInsertInto);
		sql.append(tabname);
		sql.append(strSpaltenNurKuerzel);
		final List<String> kuerzel = new ArrayList<>(
				Stream.concat(Arrays.stream(HerkunftSonstige.values()).map(h -> h.daten.kuerzel).distinct(),
						Stream.concat(Arrays.stream(HerkunftBildungsgang.values()).flatMap(hb -> hb.historie().stream()).map(h -> h.kuerzel).distinct(),
								Stream.concat(
										Arrays.stream(HerkunftBildungsgangTyp.values()).flatMap(hb -> hb.historie().stream()).map(h -> h.kuerzel).distinct(),
										Arrays.stream(HerkunftSchulform.values()).map(h -> h.daten.kuerzel).distinct()))
				).distinct().toList());
		boolean isFirst = true;
		for (final String k : kuerzel) {
			sql.append(isFirst ? strValues : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
		final long version = HerkunftSonstige.VERSION + HerkunftBildungsgang.data().getVersion() + HerkunftBildungsgangTyp.data().getVersion()
				+ HerkunftSchulform.VERSION;
		if (conn.getDBDriver() != DBDriver.SQLITE)
			sql.append(" ON DUPLICATE KEY UPDATE Kuerzel=VALUES(Kuerzel)");
		updateCoreTypeTabelle(conn, tabname, "de.svws_nrw.core.types.schueler.Herkunft", version, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type Herkunft.
	 */
	private final BiConsumer<DBEntityManager, Logger> updateHerkuenfteSchulformen = (final DBEntityManager conn, final Logger logger) -> {
		final String tabname = "Herkunft_Schulformen";
		logger.logLn(strAktualisiereTabelle + tabname);
		final StringBuilder sql = new StringBuilder();
		sql.append(strInsertInto);
		sql.append(tabname);
		sql.append("(Herkunft_ID, Schulform_Kuerzel) ");
		boolean isFirst = true;
		for (final HerkunftSonstige herkunft : HerkunftSonstige.values()) {
			for (final HerkunftSonstigeKatalogEintrag h : herkunft.historie) {
				for (final String sf : h.schulformen) {
					sql.append(isFirst ? strValues : ", (");
					isFirst = false;
					sql.append(h.id + 1000000000L).append(",");
					sql.append("'").append(sf).append("'").append(")");
				}
			}
		}
		for (final HerkunftBildungsgang herkunft : HerkunftBildungsgang.values()) {
			for (final HerkunftBildungsgangKatalogEintrag h : herkunft.historie()) {
				for (final SchulformSchulgliederung sf : h.zulaessig) {
					sql.append(isFirst ? strValues : ", (");
					isFirst = false;
					sql.append(h.id + 2000000000L).append(",");
					sql.append("'").append(sf.schulform).append("'").append(")");
				}
			}
		}
		for (final HerkunftBildungsgangTyp herkunft : HerkunftBildungsgangTyp.values()) {
			for (final HerkunftBildungsgangTypKatalogEintrag h : herkunft.historie()) {
				for (final SchulformSchulgliederung sf : h.zulaessig) {
					sql.append(isFirst ? strValues : ", (");
					isFirst = false;
					sql.append(h.id + 3000000000L).append(",");
					sql.append("'").append(sf.schulform).append("'").append(")");
				}
			}
		}
		for (final HerkunftSchulform herkunft : HerkunftSchulform.values()) {
			for (final HerkunftSchulformKatalogEintrag h : herkunft.historie) {
				for (final String sf : h.schulformen) {
					sql.append(isFirst ? strValues : ", (");
					isFirst = false;
					sql.append(h.id + 4000000000L).append(",");
					sql.append("'").append(sf).append("'").append(")");
				}
			}
		}
		if (conn.getDBDriver() != DBDriver.SQLITE)
			sql.append(" ON DUPLICATE KEY UPDATE Herkunft_ID=VALUES(Herkunft_ID), Schulform_Kuerzel=VALUES(Schulform_Kuerzel)");
		final long version = HerkunftSonstige.VERSION + HerkunftBildungsgang.data().getVersion() + HerkunftBildungsgangTyp.data().getVersion()
				+ HerkunftSchulform.VERSION;
		updateCoreTypeTabelle(conn, tabname, "de.svws_nrw.core.types.schueler.Herkunft", version, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type Herkunftsarten.
	 */
	private final BiConsumer<DBEntityManager, Logger> updateHerkunftsarten = (final DBEntityManager conn, final Logger logger) -> {
		final String tabname = "Herkunftsart";
		logger.logLn(strAktualisiereTabelle + tabname);
		final StringBuilder sql = new StringBuilder();
		sql.append(strInsertInto);
		sql.append(tabname);
		sql.append("(ID, Kuerzel, gueltigVon, gueltigBis) ");
		final Herkunftsarten[] values = Herkunftsarten.values();
		boolean isFirst = true;
		for (final Herkunftsarten herkunft : values) {
			for (final HerkunftsartKatalogEintrag h : herkunft.historie) {
				sql.append(isFirst ? strValues : ", (");
				isFirst = false;
				sql.append(h.id).append(",");
				sql.append("'").append(h.kuerzel).append("'").append(",");
				sql.append(h.gueltigVon).append(",");
				sql.append(h.gueltigBis).append(")");
			}
		}
		if (conn.getDBDriver() != DBDriver.SQLITE)
			sql.append(" ON DUPLICATE KEY UPDATE ID=VALUES(ID), Kuerzel=VALUES(Kuerzel), gueltigVon=VALUES(gueltigVon), gueltigBis=VALUES(gueltigBis)");
		updateCoreTypeTabelle(conn, tabname, Herkunftsarten.class.getCanonicalName(), Herkunftsarten.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type Herkunftsarten.
	 */
	private final BiConsumer<DBEntityManager, Logger> updateHerkunftsartenKeys = (final DBEntityManager conn, final Logger logger) -> {
		final String tabname = "Herkunftsart_Keys";
		logger.logLn(strAktualisiereTabelle + tabname);
		final StringBuilder sql = new StringBuilder();
		sql.append(strInsertInto);
		sql.append(tabname);
		sql.append(strSpaltenNurKuerzel);
		final List<String> kuerzel = Arrays.stream(Herkunftsarten.values()).map(h -> h.daten.kuerzel).distinct().toList();
		boolean isFirst = true;
		for (final String k : kuerzel) {
			sql.append(isFirst ? strValues : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
		if (conn.getDBDriver() != DBDriver.SQLITE)
			sql.append(" ON DUPLICATE KEY UPDATE Kuerzel=VALUES(Kuerzel)");
		updateCoreTypeTabelle(conn, tabname, Herkunftsarten.class.getCanonicalName(), Herkunftsarten.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type Herkunftsarten.
	 */
	private final BiConsumer<DBEntityManager, Logger> updateHerkunftsartenSchulformen = (final DBEntityManager conn, final Logger logger) -> {
		final String tabname = "Herkunftsart_Schulformen";
		logger.logLn(strAktualisiereTabelle + tabname);
		final StringBuilder sql = new StringBuilder();
		sql.append(strInsertInto);
		sql.append(tabname);
		sql.append("(Herkunftsart_ID, Schulform_Kuerzel, KurzBezeichnung, Bezeichnung) ");
		final Herkunftsarten[] values = Herkunftsarten.values();
		boolean isFirst = true;
		for (final Herkunftsarten herkunftsart : values) {
			for (final HerkunftsartKatalogEintrag h : herkunftsart.historie) {
				for (final HerkunftsartKatalogEintragBezeichnung hb : h.bezeichnungen) {
					sql.append(isFirst ? strValues : ", (");
					isFirst = false;
					sql.append(h.id).append(",");
					sql.append("'").append(hb.schulform).append("'").append(",");
					sql.append("'").append(hb.kurzBezeichnung.replace("'", "''")).append("'").append(",");
					sql.append("'").append(hb.bezeichnung.replace("'", "''")).append("'").append(")");
				}
			}
		}
		if (conn.getDBDriver() != DBDriver.SQLITE)
			sql.append(
					" ON DUPLICATE KEY UPDATE Herkunftsart_ID=VALUES(Herkunftsart_ID), Schulform_Kuerzel=VALUES(Schulform_Kuerzel), KurzBezeichnung=VALUES(KurzBezeichnung), Bezeichnung=VALUES(Bezeichnung)");
		updateCoreTypeTabelle(conn, tabname, Herkunftsarten.class.getCanonicalName(), Herkunftsarten.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type Klassenart.
	 */
	private final BiConsumer<DBEntityManager, Logger> updateKlassenartenKeys = (final DBEntityManager conn, final Logger logger) -> {
		final String tabname = "KlassenartenKatalog_Keys";
		logger.logLn(strAktualisiereTabelle + tabname);
		final StringBuilder sql = new StringBuilder();
		sql.append(strInsertInto);
		sql.append(tabname);
		sql.append(strSpaltenNurKuerzel);
		final List<String> kuerzel = Arrays.stream(Klassenart.values()).flatMap(k -> k.historie().stream()).map(h -> h.kuerzel).distinct().toList();
		boolean isFirst = true;
		for (final String k : kuerzel) {
			sql.append(isFirst ? strValues : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
		if (conn.getDBDriver() != DBDriver.SQLITE)
			sql.append(" ON DUPLICATE KEY UPDATE Kuerzel=VALUES(Kuerzel)");
		updateCoreTypeTabelle(conn, tabname, Klassenart.class.getCanonicalName(), Klassenart.data().getVersion(), sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type ZulaessigeKursart.
	 */
	private final BiConsumer<DBEntityManager, Logger> updateKursartenKeys = (final DBEntityManager conn, final Logger logger) -> {
		final String tabname = "KursartenKatalog_Keys";
		logger.logLn(strAktualisiereTabelle + tabname);
		final StringBuilder sql = new StringBuilder();
		sql.append(strInsertInto);
		sql.append(tabname);
		sql.append(strSpaltenNurKuerzel);
		final List<String> kuerzel = Arrays.stream(ZulaessigeKursart.values()).flatMap(k -> k.historie().stream()).map(h -> h.kuerzel).distinct().toList();
		boolean isFirst = true;
		for (final String k : kuerzel) {
			sql.append(isFirst ? strValues : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
		if (conn.getDBDriver() != DBDriver.SQLITE)
			sql.append(" ON DUPLICATE KEY UPDATE Kuerzel=VALUES(Kuerzel)");
		updateCoreTypeTabelle(conn, tabname, ZulaessigeKursart.class.getCanonicalName(), ZulaessigeKursart.data().getVersion(), sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type ZulaessigesFach.
	 */
	private final BiConsumer<DBEntityManager, Logger> updateZulaessigeFaecher = (final DBEntityManager conn, final Logger logger) -> {
		final String tabname = "FachKatalog";
		logger.logLn(strAktualisiereTabelle + tabname);
		final StringBuilder sql = new StringBuilder();
		sql.append(strInsertInto);
		sql.append(tabname);
		sql.append(
				"(ID, KuerzelASD, Bezeichnung, Kuerzel, Aufgabenfeld, Fachgruppe, JahrgangAb, IstFremdsprache, IstHKFS, IstAusRegUFach, IstErsatzPflichtFS, IstKonfKoop, NurSII, ExportASD, gueltigVon, gueltigBis) ");
		boolean isFirst = true;
		for (final Fach fach : Fach.values()) {
			for (final FachKatalogEintrag f : fach.historie()) {
				sql.append(isFirst ? strValues : ", (");
				isFirst = false;
				sql.append(f.id).append(",");
				sql.append("'").append(f.schluessel).append("'").append(",");
				sql.append("'").append(f.text.replace("'", "''")).append("'").append(",");
				sql.append("'").append(f.kuerzel).append("'").append(",");
				if (f.aufgabenfeld == null)
					sql.append(strNullValue);
				else
					sql.append(f.aufgabenfeld).append(",");
				if (f.fachgruppe == null)
					sql.append(strNullValue);
				else
					sql.append("'").append(f.fachgruppe).append("'").append(",");
				if (f.abJahrgang == null) {
					sql.append(strNullValue);
				} else {
					final Jahrgaenge tmpJahrgang = Jahrgaenge.data().getWertByBezeichner(f.abJahrgang);
					sql.append("'").append(tmpJahrgang.historie().getLast().kuerzel).append("'").append(",");
				}
				sql.append(f.istFremdsprache ? 1 : 0).append(",");
				sql.append(f.istHKFS ? 1 : 0).append(",");
				sql.append(f.istAusRegUFach ? 1 : 0).append(",");
				sql.append(f.istErsatzPflichtFS ? 1 : 0).append(",");
				sql.append(f.istKonfKoop ? 1 : 0).append(",");
				sql.append(f.nurSII ? 1 : 0).append(",");
				sql.append(f.exportASD ? 1 : 0).append(",");
				sql.append(f.gueltigVon).append(",");
				sql.append(f.gueltigBis).append(")");
			}
		}
		if (conn.getDBDriver() != DBDriver.SQLITE)
			sql.append(
					" ON DUPLICATE KEY UPDATE ID=VALUES(ID), KuerzelASD=VALUES(KuerzelASD), Bezeichnung=VALUES(Bezeichnung), Kuerzel=VALUES(Kuerzel), Aufgabenfeld=VALUES(Aufgabenfeld), Fachgruppe=VALUES(Fachgruppe), JahrgangAb=VALUES(JahrgangAb), IstFremdsprache=VALUES(IstFremdsprache), IstHKFS=VALUES(IstHKFS), IstAusRegUFach=VALUES(IstAusRegUFach), IstErsatzPflichtFS=VALUES(IstErsatzPflichtFS), IstKonfKoop=VALUES(IstKonfKoop), NurSII=VALUES(NurSII), ExportASD=VALUES(ExportASD), gueltigVon=VALUES(gueltigVon), gueltigBis=VALUES(gueltigBis)");
		updateCoreTypeTabelle(conn, tabname, Fach.class.getCanonicalName(), Fach.data().getVersion(), sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type ZulaessigesFach.
	 */
	private final BiConsumer<DBEntityManager, Logger> updateZulaessigeFaecherKeys = (final DBEntityManager conn, final Logger logger) -> {
		final String tabname = "FachKatalog_Keys";
		logger.logLn(strAktualisiereTabelle + tabname);
		final StringBuilder sql = new StringBuilder();
		sql.append(strInsertInto);
		sql.append(tabname);
		sql.append(strSpaltenNurKuerzel);
		final List<String> kuerzel = Arrays.stream(Fach.values()).flatMap(f -> f.historie().stream()).map(h -> h.schluessel).distinct().toList();
		boolean isFirst = true;
		for (final String k : kuerzel) {
			sql.append(isFirst ? strValues : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
		if (conn.getDBDriver() != DBDriver.SQLITE)
			sql.append(" ON DUPLICATE KEY UPDATE Kuerzel=VALUES(Kuerzel)");
		updateCoreTypeTabelle(conn, tabname, Fach.class.getCanonicalName(), Fach.data().getVersion(), sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type ZulaessigesFach.
	 */
	private final BiConsumer<DBEntityManager, Logger> updateZulaessigeFaecherSchulformen = (final DBEntityManager conn, final Logger logger) -> {
		final String tabname = "FachKatalog_Schulformen";
		logger.logLn(strAktualisiereTabelle + tabname);
		final StringBuilder sql = new StringBuilder();
		sql.append(strInsertInto);
		sql.append(tabname);
		sql.append("(Fach_ID, Schulform_Kuerzel, Schulgliederung_Kuerzel) ");
		final Fach[] values = Fach.values();
		boolean isFirst = true;
		for (final Fach fach : values) {
			for (final FachKatalogEintrag f : fach.historie()) {
				for (final SchulformSchulgliederung sfsg : f.zulaessig) {
					sql.append(isFirst ? strValues : ", (");
					isFirst = false;
					sql.append(f.id).append(",");
					if (sfsg.schulform == null)
						sql.append(strNullValue);
					else
						sql.append("'").append(sfsg.schulform).append("'").append(",");
					if (sfsg.gliederung == null)
						sql.append("'')");
					else
						sql.append("'").append(Schulgliederung.data().getWertByKuerzel(sfsg.gliederung)).append("'").append(")");
				}
			}
		}
		if (conn.getDBDriver() != DBDriver.SQLITE)
			sql.append(
					" ON DUPLICATE KEY UPDATE Fach_ID=VALUES(Fach_ID), Schulform_Kuerzel=VALUES(Schulform_Kuerzel), Schulgliederung_Kuerzel=VALUES(Schulgliederung_Kuerzel)");
		updateCoreTypeTabelle(conn, tabname, Fach.class.getCanonicalName(), Fach.data().getVersion(), sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type Einschulungsart.
	 */
	private final BiConsumer<DBEntityManager, Logger> updateEinschulungsartenKeys = (final DBEntityManager conn, final Logger logger) -> {
		final String tabname = "EinschulungsartKatalog_Keys";
		logger.logLn(strAktualisiereTabelle + tabname);
		final StringBuilder sql = new StringBuilder();
		sql.append(strInsertInto);
		sql.append(tabname);
		sql.append(strSpaltenNurKuerzel);
		final List<String> kuerzel = Arrays.stream(Einschulungsart.values()).map(h -> h.daten.kuerzel).distinct().toList();
		boolean isFirst = true;
		for (final String k : kuerzel) {
			sql.append(isFirst ? strValues : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
		if (conn.getDBDriver() != DBDriver.SQLITE)
			sql.append(" ON DUPLICATE KEY UPDATE Kuerzel=VALUES(Kuerzel)");
		updateCoreTypeTabelle(conn, tabname, Einschulungsart.class.getCanonicalName(), Einschulungsart.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type Religion.
	 */
	private final BiConsumer<DBEntityManager, Logger> updateReligionenKeys = (final DBEntityManager conn, final Logger logger) -> {
		final String tabname = "Religionen_Keys";
		logger.logLn(strAktualisiereTabelle + tabname);
		final StringBuilder sql = new StringBuilder();
		sql.append(strInsertInto);
		sql.append(tabname);
		sql.append(strSpaltenNurKuerzel);
		final List<String> kuerzel = Arrays.stream(Religion.values()).flatMap(r -> r.historie().stream()).map(h -> h.kuerzel).distinct().toList();
		boolean isFirst = true;
		for (final String k : kuerzel) {
			sql.append(isFirst ? strValues : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
		if (conn.getDBDriver() != DBDriver.SQLITE)
			sql.append(" ON DUPLICATE KEY UPDATE Kuerzel=VALUES(Kuerzel)");
		updateCoreTypeTabelle(conn, tabname, Religion.class.getCanonicalName(), Religion.data().getVersion(), sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type AllgemeineMerkmale.
	 */
	private final BiConsumer<DBEntityManager, Logger> updateAllgemeineMerkmaleKeys = (final DBEntityManager conn, final Logger logger) -> {
		final String tabname = "AllgemeineMerkmaleKatalog_Keys";
		logger.logLn(strAktualisiereTabelle + tabname);
		final StringBuilder sql = new StringBuilder();
		sql.append(strInsertInto);
		sql.append(tabname);
		sql.append(strSpaltenNurKuerzel);
		final List<String> kuerzel = Arrays.stream(AllgemeineMerkmale.values()).map(h -> h.daten.kuerzel).distinct().toList();
		boolean isFirst = true;
		for (final String k : kuerzel) {
			sql.append(isFirst ? strValues : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
		if (conn.getDBDriver() != DBDriver.SQLITE)
			sql.append(" ON DUPLICATE KEY UPDATE Kuerzel=VALUES(Kuerzel)");
		updateCoreTypeTabelle(conn, tabname, AllgemeineMerkmale.class.getCanonicalName(), AllgemeineMerkmale.VERSION, sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für die Core-Types BerufskollegOrganisationsformen,
	 * WeiterbildungskollegOrganisationsformen und AllgemeinbildendOrganisationsformen.
	 */
	private final BiConsumer<DBEntityManager, Logger> updateOrganisationsformenKeys = (final DBEntityManager conn, final Logger logger) -> {
		final String tabname = "OrganisationsformenKatalog_Keys";
		logger.logLn(strAktualisiereTabelle + tabname);
		final StringBuilder sql = new StringBuilder();
		sql.append(strInsertInto);
		sql.append(tabname);
		sql.append(strSpaltenNurKuerzel);
		final List<String> kuerzel = Stream.of(
				Stream.of(BerufskollegOrganisationsformen.values()).flatMap(o -> o.historie().stream()).map(h -> h.kuerzel),
				Stream.of(WeiterbildungskollegOrganisationsformen.values()).flatMap(o -> o.historie().stream()).map(h -> h.kuerzel),
				Stream.of(AllgemeinbildendOrganisationsformen.values()).flatMap(o -> o.historie().stream()).map(h -> h.kuerzel)
		).flatMap(o -> o).distinct().toList();
		boolean isFirst = true;
		for (final String k : kuerzel) {
			sql.append(isFirst ? strValues : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
		if (conn.getDBDriver() != DBDriver.SQLITE)
			sql.append(" ON DUPLICATE KEY UPDATE Kuerzel=VALUES(Kuerzel)");
		updateCoreTypeTabelle(conn, tabname,
				BerufskollegOrganisationsformen.class.getCanonicalName() + ", " + WeiterbildungskollegOrganisationsformen.class.getCanonicalName() + ", "
						+ AllgemeinbildendOrganisationsformen.class.getCanonicalName(),
				BerufskollegOrganisationsformen.data().getVersion() + WeiterbildungskollegOrganisationsformen.data().getVersion()
						+ AllgemeinbildendOrganisationsformen.data().getVersion(),
				sql.toString());
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type LehrerLeitungsfunktion.
	 */
	private final BiConsumer<DBEntityManager, Logger> updateLehrerLeitungsfunktionenKeys = (final DBEntityManager conn, final Logger logger) -> {
		final String tabname = "LehrerLeitungsfunktion_Keys";
		logger.logLn(strAktualisiereTabelle + tabname);

		updateCoreTypeTabelle(conn, tabname, LehrerLeitungsfunktion.class.getCanonicalName(), LehrerLeitungsfunktion.data().getVersion(),
				Arrays.stream(LehrerLeitungsfunktion.values()).flatMap(l -> l.historie().stream()).map(h -> "" + h.id).distinct()
						.collect(Collectors.joining("), (", strInsertInto + tabname + "(ID) VALUES (",
								") " + ((conn.getDBDriver() != DBDriver.SQLITE) ? " ON DUPLICATE KEY UPDATE ID=VALUES(ID)" : ""))));
	};


	/**
	 * Prüft, ob die übergebene Version mit der Version des in der
	 * Datenbank gespeicherten Core-Types übereinstimmt oder nicht.
	 *
	 * @param conn      die Datenbankverbindung
	 * @param tabname   der Name der Tabelle, wo der Core-Type gespechert ist
	 * @param version   die Version, auf welche geprüft wird
	 *
	 * @return true, falls die Versionen übereinstimmen, und ansonsten false
	 */
	private boolean pruefeVersion(final DBEntityManager conn, final String tabname, final long version) {
		final DTOSchemaCoreTypeVersion v = _status.getCoreTypeVersion(conn, tabname);
		if (v == null)
			return false;
		return v.Version == version;
	}

}
