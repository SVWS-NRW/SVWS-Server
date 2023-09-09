package de.svws_nrw.db.utils.schema;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.data.fach.FachKatalogEintrag;
import de.svws_nrw.core.data.schule.HerkunftKatalogEintrag;
import de.svws_nrw.core.data.schule.HerkunftsartKatalogEintrag;
import de.svws_nrw.core.data.schule.HerkunftsartKatalogEintragBezeichnung;
import de.svws_nrw.core.data.schule.SchulformKatalogEintrag;
import de.svws_nrw.core.data.schule.SchulformSchulgliederung;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.KursFortschreibungsart;
import de.svws_nrw.core.types.Note;
import de.svws_nrw.core.types.PersonalTyp;
import de.svws_nrw.core.types.fach.Fachgruppe;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import de.svws_nrw.core.types.jahrgang.Jahrgaenge;
import de.svws_nrw.core.types.klassen.Klassenart;
import de.svws_nrw.core.types.kurse.ZulaessigeKursart;
import de.svws_nrw.core.types.lehrer.LehrerLeitungsfunktion;
import de.svws_nrw.core.types.schueler.Einschulungsart;
import de.svws_nrw.core.types.schueler.Herkunft;
import de.svws_nrw.core.types.schueler.Herkunftsarten;
import de.svws_nrw.core.types.schule.AllgemeinbildendOrganisationsformen;
import de.svws_nrw.core.types.schule.AllgemeineMerkmale;
import de.svws_nrw.core.types.schule.BerufskollegOrganisationsformen;
import de.svws_nrw.core.types.schule.Nationalitaeten;
import de.svws_nrw.core.types.schule.Religion;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.types.schule.Schulgliederung;
import de.svws_nrw.core.types.schule.WeiterbildungskollegOrganisationsformen;
import de.svws_nrw.db.Benutzer;
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
	private record CoreTypeTable(String name, long version, BiConsumer<DBEntityManager, Logger> updater) { /**/ }

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
		tables.add(new CoreTypeTable("Fachgruppen", Fachgruppe.VERSION, updateFachgruppen));
		tables.add(new CoreTypeTable("KursFortschreibungsarten", KursFortschreibungsart.VERSION, updateKursFortschreibungsarten));
		tables.add(new CoreTypeTable("Schulformen", Schulform.VERSION, updateSchulformen));
		tables.add(new CoreTypeTable("PersonalTypen", PersonalTyp.VERSION, updatePersonalTypen));
		tables.add(new CoreTypeTable("Nationalitaeten_Keys", Nationalitaeten.VERSION, updateNationalitaeten_Keys));
		tables.add(new CoreTypeTable("Jahrgaenge_Keys", Jahrgaenge.VERSION, updateJahrgaengeKeys));
		tables.add(new CoreTypeTable("Noten", Note.VERSION, updateNoten));
		tables.add(new CoreTypeTable("Herkunft", Herkunft.VERSION, updateHerkuenfte));
		tables.add(new CoreTypeTable("Herkunft_Keys", Herkunft.VERSION, updateHerkuenfteKeys));
		tables.add(new CoreTypeTable("Herkunft_Schulformen", Herkunft.VERSION, updateHerkuenfteSchulformen));
		tables.add(new CoreTypeTable("Herkunftsart", Herkunftsarten.VERSION, updateHerkunftsarten));
		tables.add(new CoreTypeTable("Herkunftsart_Keys", Herkunftsarten.VERSION, updateHerkunftsartenKeys));
		tables.add(new CoreTypeTable("Herkunftsart_Schulformen", Herkunftsarten.VERSION, updateHerkunftsartenSchulformen));
		tables.add(new CoreTypeTable("KlassenartenKatalog_Keys", Klassenart.VERSION, updateKlassenartenKeys));
		tables.add(new CoreTypeTable("KursartenKatalog_Keys", ZulaessigeKursart.VERSION, updateKursartenKeys));
		tables.add(new CoreTypeTable("FachKatalog", ZulaessigesFach.VERSION, updateZulaessigeFaecher));
		tables.add(new CoreTypeTable("FachKatalog_Keys", ZulaessigesFach.VERSION, updateZulaessigeFaecherKeys));
		tables.add(new CoreTypeTable("FachKatalog_Schulformen", ZulaessigesFach.VERSION, updateZulaessigeFaecherSchulformen));
		tables.add(new CoreTypeTable("EinschulungsartKatalog_Keys", Einschulungsart.VERSION, updateEinschulungsartenKeys));
		tables.add(new CoreTypeTable("Religionen_Keys", Religion.VERSION, updateReligionenKeys));
		tables.add(new CoreTypeTable("AllgemeineMerkmaleKatalog_Keys", AllgemeineMerkmale.VERSION, updateAllgemeineMerkmaleKeys));
		tables.add(new CoreTypeTable("OrganisationsformenKatalog_Keys", BerufskollegOrganisationsformen.VERSION
				+ WeiterbildungskollegOrganisationsformen.VERSION + AllgemeinbildendOrganisationsformen.VERSION,
				updateOrganisationsformenKeys));
		tables.add(new CoreTypeTable("LehrerLeitungsfunktion_Keys", LehrerLeitungsfunktion.VERSION, updateLehrerLeitungsfunktionenKeys));
	}


	/**
	 * Prüft, ob die Core-Types aktuell sind, d.h. die Version in den Core-Types mit der
	 * Version in der Datenbank übereinstimmt.
	 *
	 * @return true, falls die Core-Types in der DB aktuell sind, sonst false
	 */
	public boolean isUptodate() {
		try (DBEntityManager conn = _schemaManager.getUser().getEntityManager()) {
			_status.update(conn);
			for (final CoreTypeTable entry : tables)
				if ((entry.name == null) || (!pruefeVersion(conn, entry.name, entry.version)))
					return false;
			return true;
		}
	}


	/**
	 * Prüft, ob die Core-Types aktuell sind, d.h. die Version in den Core-Types mit der
	 * Version in der Datenbank übereinstimmt.
	 *
	 * @param conn   die Datenbankverbindung
	 *
	 * @return true, falls die Core-Types in der DB aktuell sind, sonst false
	 */
	public boolean isUptodate(final DBEntityManager conn) {
		_status.update(conn);
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
			if  (Long.compare(tab.getCoreType().getCoreTypeVersion(), v.Version) < 0)
				return false;  // Die Version des Core-Types ist kleiner als die Version in der DB
		}
		// TODO unten deprecated, oben aktuell
		for (final CoreTypeTable entry : tables) {
			final DTOSchemaCoreTypeVersion v = _status.getCoreTypeVersion(conn, entry.name);
			if (v == null)
				continue; // Bisher keine Version gespeichert - Update also möglich
			if  (Long.compare(entry.version, v.Version) < 0)
				return false;  // Die Version des Core-Types ist kleiner als die Version in der DB
		}
		return true;
	}


	/**
	 * Aktualisiert die Core-Types im Schema schrittweise auf die angegebene Revision.
	 *
	 * @param user         der Datenbank-Benutzer für den Verbindungsaufbau zur Datenbank
	 * @param lockSchema   gibt an, on das Schema für den Update-Prozess gesperrt werden soll. Dies ist z.B. nicht
	 *                     notwendig, wenn der Update-Prozess im Rahmen einer Migration gestartet wird.
	 * @param rev          die Datenbank-Revision auf welche aktualisiert wird
	 *
	 * @return true im Erfolgsfall, sonst false
	 */
	public boolean update(final Benutzer user, final boolean lockSchema, final long rev) {
		try (DBEntityManager conn = user.getEntityManager()) {
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
			final long status_revision = _status.version == null ? 0 : _status.version.getRevisionOrDefault(0);
			for (final SchemaTabelle tab : Schema.getTabellen(status_revision)) {
				if (!tab.hasCoreType())
					continue;
				final SchemaTabelleCoreType ct = tab.getCoreType();
				if (pruefeVersion(conn, tab.name(), ct.getCoreTypeVersion()))
					continue;
				_logger.logLn(strAktualisiereTabelle + tab.name());
				updateCoreTypeTabelle(conn, tab.name(), ct.getCoreTypeName(), ct.getCoreTypeVersion(), ct.getSQLInsert(status_revision));
			}
			// TODO unten deprecated: Aktualisiere ggf. die Daten der einzelnen Core-Types
			for (final CoreTypeTable entry : tables)
				if (!pruefeVersion(conn, entry.name, entry.version))
					entry.updater.accept(conn, _logger);
			return true;
		} catch (@SuppressWarnings("unused") final Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			// Entsperre ggf. das Datenbankschema
			if ((lockSchema) && (!SVWSKonfiguration.get().unlockSchema(_schemaManager.getSchemaStatus().schemaName)))
				_logger.logLn("-> Update evtl. fehlgeschlagen! (Fehler beim Freigeben des Datenbank-Schemas. Schema ist nicht gesperrt - dies wird an dieser Stelle nicht erwartet!)");
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
	private static void updateCoreTypeTabelle(final DBEntityManager conn, final String tabname, final String typename, final long version, final String sqlInsert) {
		// Lösche alle Daten
		conn.transactionNativeDelete("DELETE FROM " + tabname);
		conn.transactionFlush();
		// Füge die aktuellen Daten des Core-Types ein
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
		sql.append("(ID, Fachbereich, SchildFgID, FG_Bezeichnung, FG_Kuerzel, Schulformen, FarbeR, FarbeG, FarbeB, Sortierung, FuerZeugnis, gueltigVon, gueltigBis) ");
		final Fachgruppe[] values = Fachgruppe.values();
		for (int i = 0; i < values.length; i++) {
			final Fachgruppe f = values[i];
			sql.append(i == 0 ? strValues : ", (");
			sql.append(f.daten.id).append(",");
			sql.append(f.daten.nummer).append(",");
			sql.append(f.daten.idSchild).append(",");
			sql.append("'").append(f.daten.bezeichnung).append("'").append(",");
			sql.append("'").append(f.daten.kuerzel).append("'").append(",");
			sql.append("'").append(f.getSchulformen().stream().map(s -> s.daten.kuerzel).collect(Collectors.joining(","))).append("'").append(",");
			sql.append(f.daten.farbe.red).append(",");
			sql.append(f.daten.farbe.green).append(",");
			sql.append(f.daten.farbe.blue).append(",");
			sql.append(f.daten.sortierung).append(",");
			sql.append(f.daten.fuerZeugnis ? 1 : 0).append(",");
			sql.append(f.daten.gueltigVon).append(",");
			sql.append(f.daten.gueltigBis).append(")");
		}
		updateCoreTypeTabelle(conn, tabname, KursFortschreibungsart.class.getCanonicalName(), KursFortschreibungsart.VERSION, sql.toString());
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
		final String[] jg_kuerzel = Arrays.stream(values).flatMap(jg -> Arrays.stream(jg.historie)).map(jg -> jg.kuerzel).collect(Collectors.toSet()).toArray(new String[0]);
		boolean isFirst = true;
		for (int i = 0; i < jg_kuerzel.length; i++) {
			sql.append(isFirst ? strValues : ", (");
			isFirst = false;
			sql.append("'").append(jg_kuerzel[i]).append("'").append(")");
		}
		updateCoreTypeTabelle(conn, tabname, Jahrgaenge.class.getCanonicalName(), Jahrgaenge.VERSION, sql.toString());
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
			sql.append(i == 0 ? strValues : ", (");
			sql.append(p.id).append(",");
			sql.append("'").append(p.kuerzel).append("'").append(",");
			sql.append("'").append(p.beschreibung).append("'").append(",");
			sql.append(p.gueltigVon).append(",");
			sql.append(p.gueltigBis).append(")");
		}
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
		final List<String> codes = Arrays.stream(Nationalitaeten.values()).map(nat -> nat.daten.codeDEStatis).distinct().toList();
		boolean isFirst = true;
		for (int i = 0; i < codes.size(); i++) {
			final String code = codes.get(i);
			sql.append(isFirst ? strValues : ", (");
			isFirst = false;
			sql.append("'").append(code).append("')");
		}
		updateCoreTypeTabelle(conn, tabname, Nationalitaeten.class.getCanonicalName(), Nationalitaeten.VERSION, sql.toString());
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
		final Note[] values = Note.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			final Note n = values[i];
			if (n == Note.KEINE)
				continue;
			sql.append(isFirst ? strValues : ", (");
			isFirst = false;
			sql.append(n.id).append(",");
			sql.append("'").append(n.kuerzel).append("'").append(",");
			sql.append(n.hatTendenz()).append(",");
			sql.append("'").append(n.text).append("'").append(",");
			sql.append("--------------------".equals(n.textZeugnis)).append(",");
			sql.append(n.notenpunkte).append(",");
			if (n.notenpunkte != null)
				sql.append("'").append(String.format("%02d", n.notenpunkte)).append("'").append(",");
			else if ((n == Note.E3_TEILGENOMMEN) || (n == Note.E2_MIT_ERFOLG_TEILGENOMMEN) || n == (Note.E3_TEILGENOMMEN) || (n == Note.ATTEST))
				sql.append("'").append(n.kuerzel).append("'").append(",");
			else
				sql.append(strNullValue);
			sql.append((n.notenpunkte != null) || (n == Note.E3_TEILGENOMMEN) || (n == Note.E2_MIT_ERFOLG_TEILGENOMMEN)
					|| (n == (Note.E3_TEILGENOMMEN)) || (n == Note.ATTEST)).append(",");
			sql.append(n.sortierung).append(",");
			sql.append(n.gueltigVon).append(",");
			sql.append(n.gueltigBis).append(")");
		}
		updateCoreTypeTabelle(conn, tabname, Note.class.getCanonicalName(), Note.VERSION, sql.toString());
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
			sql.append(i == 0 ? strValues : ", (");
			sql.append(p.id).append(",");
			sql.append("'").append(p.kuerzel).append("'").append(",");
			sql.append("'").append(p.bezeichnung).append("'").append(",");
			sql.append(p.gueltigVon).append(",");
			sql.append(p.gueltigBis).append(")");
		}
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
		for (int i = 0; i < values.length; i++) {
			final Schulform schulform = values[i];
			for (final SchulformKatalogEintrag sf : schulform.historie) {
				sql.append(isFirst ? strValues : ", (");
				isFirst = false;
				sql.append(sf.id).append(",");
				sql.append("'").append(sf.kuerzel).append("'").append(",");
				if (sf.nummer == null)
					sql.append(strNullValue);
				else
					sql.append("'").append(sf.nummer).append("'").append(",");
				sql.append("'").append(sf.bezeichnung).append("'").append(",");
				sql.append(sf.hatGymOb).append(",");
				sql.append(sf.gueltigVon).append(",");
				sql.append(sf.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(conn, tabname, Schulform.class.getCanonicalName(), Schulform.VERSION, sql.toString());
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
		final Herkunft[] values = Herkunft.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			final Herkunft herkunft = values[i];
			for (final HerkunftKatalogEintrag h : herkunft.historie) {
				sql.append(isFirst ? strValues : ", (");
				isFirst = false;
				sql.append(h.id).append(",");
				sql.append("'").append(h.kuerzel).append("'").append(",");
				sql.append("'").append(h.beschreibung.replace("'", "''")).append("'").append(",");
				sql.append(h.gueltigVon).append(",");
				sql.append(h.gueltigBis).append(")");
			}
		}
		updateCoreTypeTabelle(conn, tabname, Herkunft.class.getCanonicalName(), Herkunft.VERSION, sql.toString());
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
		final List<String> kuerzel = Arrays.stream(Herkunft.values()).map(h -> h.daten.kuerzel).distinct().toList();
		boolean isFirst = true;
		for (int i = 0; i < kuerzel.size(); i++) {
			final String k = kuerzel.get(i);
			sql.append(isFirst ? strValues : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
		updateCoreTypeTabelle(conn, tabname, Herkunft.class.getCanonicalName(), Herkunft.VERSION, sql.toString());
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
		final Herkunft[] values = Herkunft.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			final Herkunft herkunft = values[i];
			for (final HerkunftKatalogEintrag h : herkunft.historie) {
				final List<Schulform> schulformen = h.schulformen.stream().map(Schulform::getByKuerzel).toList();
				for (final Schulform sf : schulformen) {
					sql.append(isFirst ? strValues : ", (");
					isFirst = false;
					sql.append(h.id).append(",");
					sql.append("'").append(sf.daten.kuerzel).append("'").append(")");
				}
			}
		}
		updateCoreTypeTabelle(conn, tabname, Herkunft.class.getCanonicalName(), Herkunft.VERSION, sql.toString());
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
		for (int i = 0; i < values.length; i++) {
			final Herkunftsarten herkunft = values[i];
			for (final HerkunftsartKatalogEintrag h : herkunft.historie) {
				sql.append(isFirst ? strValues : ", (");
				isFirst = false;
				sql.append(h.id).append(",");
				sql.append("'").append(h.kuerzel).append("'").append(",");
				sql.append(h.gueltigVon).append(",");
				sql.append(h.gueltigBis).append(")");
			}
		}
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
		for (int i = 0; i < kuerzel.size(); i++) {
			final String k = kuerzel.get(i);
			sql.append(isFirst ? strValues : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
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
		for (int i = 0; i < values.length; i++) {
			final Herkunftsarten herkunftsart = values[i];
			for (final HerkunftsartKatalogEintrag h : herkunftsart.historie) {
				for (final HerkunftsartKatalogEintragBezeichnung hb : h.bezeichnungen) {
					sql.append(isFirst ? strValues : ", (");
					isFirst = false;
					sql.append(h.id).append(",");
					sql.append("'").append(Schulform.getByKuerzel(hb.schulform).daten.kuerzel).append("'").append(",");
					sql.append("'").append(hb.kurzBezeichnung.replace("'", "''")).append("'").append(",");
					sql.append("'").append(hb.bezeichnung.replace("'", "''")).append("'").append(")");
				}
			}
		}
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
		final List<String> kuerzel = Arrays.stream(Klassenart.values()).map(h -> h.daten.kuerzel).distinct().toList();
		boolean isFirst = true;
		for (int i = 0; i < kuerzel.size(); i++) {
			final String k = kuerzel.get(i);
			sql.append(isFirst ? strValues : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
		updateCoreTypeTabelle(conn, tabname, Klassenart.class.getCanonicalName(), Klassenart.VERSION, sql.toString());
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
		final List<String> kuerzel = Arrays.stream(ZulaessigeKursart.values()).map(h -> h.daten.kuerzel).distinct().toList();
		boolean isFirst = true;
		for (int i = 0; i < kuerzel.size(); i++) {
			final String k = kuerzel.get(i);
			sql.append(isFirst ? strValues : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
		updateCoreTypeTabelle(conn, tabname, ZulaessigeKursart.class.getCanonicalName(), ZulaessigeKursart.VERSION, sql.toString());
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
		sql.append("(ID, KuerzelASD, Bezeichnung, Kuerzel, Aufgabenfeld, Fachgruppe, JahrgangAb, IstFremdsprache, IstHKFS, IstAusRegUFach, IstErsatzPflichtFS, IstKonfKoop, NurSII, ExportASD, gueltigVon, gueltigBis) ");
		final ZulaessigesFach[] values = ZulaessigesFach.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			final ZulaessigesFach fach = values[i];
			for (final FachKatalogEintrag f : fach.historie) {
				sql.append(isFirst ? strValues : ", (");
				isFirst = false;
				sql.append(f.id).append(",");
				sql.append("'").append(f.kuerzelASD).append("'").append(",");
				sql.append("'").append(f.bezeichnung.replace("'", "''")).append("'").append(",");
				sql.append("'").append(f.kuerzel).append("'").append(",");
				if (f.aufgabenfeld == null)
					sql.append(strNullValue);
				else
					sql.append(f.aufgabenfeld).append(",");
				if (f.fachgruppe == null)
					sql.append(strNullValue);
				else
					sql.append("'").append(f.fachgruppe).append("'").append(",");
				if (f.abJahrgang == null)
					sql.append(strNullValue);
				else
					sql.append("'").append(f.abJahrgang).append("'").append(",");
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
		updateCoreTypeTabelle(conn, tabname, ZulaessigesFach.class.getCanonicalName(), ZulaessigesFach.VERSION, sql.toString());
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
		final List<String> kuerzel = Arrays.stream(ZulaessigesFach.values()).map(h -> h.daten.kuerzelASD).distinct().toList();
		boolean isFirst = true;
		for (int i = 0; i < kuerzel.size(); i++) {
			final String k = kuerzel.get(i);
			sql.append(isFirst ? strValues : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
		updateCoreTypeTabelle(conn, tabname, ZulaessigesFach.class.getCanonicalName(), ZulaessigesFach.VERSION, sql.toString());
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
		final ZulaessigesFach[] values = ZulaessigesFach.values();
		boolean isFirst = true;
		for (int i = 0; i < values.length; i++) {
			final ZulaessigesFach fach = values[i];
			for (final FachKatalogEintrag f : fach.historie) {
				for (final SchulformSchulgliederung sfsg : f.zulaessig) {
					sql.append(isFirst ? strValues : ", (");
					isFirst = false;
					sql.append(f.id).append(",");
					if (sfsg.schulform == null)
						sql.append(strNullValue);
					else
						sql.append("'").append(Schulform.getByKuerzel(sfsg.schulform).daten.kuerzel).append("'").append(",");
					if (sfsg.gliederung == null)
						sql.append("'')");
					else
						sql.append("'").append(Schulgliederung.getByKuerzel(sfsg.gliederung)).append("'").append(")");
				}
			}
		}
		updateCoreTypeTabelle(conn, tabname, ZulaessigesFach.class.getCanonicalName(), ZulaessigesFach.VERSION, sql.toString());
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
		for (int i = 0; i < kuerzel.size(); i++) {
			final String k = kuerzel.get(i);
			sql.append(isFirst ? strValues : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
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
		final List<String> kuerzel = Arrays.stream(Religion.values()).map(h -> h.daten.kuerzel).distinct().toList();
		boolean isFirst = true;
		for (int i = 0; i < kuerzel.size(); i++) {
			final String k = kuerzel.get(i);
			sql.append(isFirst ? strValues : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
		updateCoreTypeTabelle(conn, tabname, Religion.class.getCanonicalName(), Religion.VERSION, sql.toString());
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
		for (int i = 0; i < kuerzel.size(); i++) {
			final String k = kuerzel.get(i);
			sql.append(isFirst ? strValues : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
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
				Stream.of(BerufskollegOrganisationsformen.values()).map(h -> h.daten.kuerzel),
				Stream.of(WeiterbildungskollegOrganisationsformen.values()).map(h -> h.daten.kuerzel),
				Stream.of(AllgemeinbildendOrganisationsformen.values()).map(h -> h.daten.kuerzel)
				).flatMap(o -> o).distinct().toList();
		boolean isFirst = true;
		for (int i = 0; i < kuerzel.size(); i++) {
			final String k = kuerzel.get(i);
			sql.append(isFirst ? strValues : ", (");
			isFirst = false;
			sql.append("'").append(k).append("')");
		}
		updateCoreTypeTabelle(conn, tabname,
				BerufskollegOrganisationsformen.class.getCanonicalName() + ", " + WeiterbildungskollegOrganisationsformen.class.getCanonicalName() + ", " + AllgemeinbildendOrganisationsformen.class.getCanonicalName(),
				BerufskollegOrganisationsformen.VERSION + WeiterbildungskollegOrganisationsformen.VERSION + AllgemeinbildendOrganisationsformen.VERSION,
				sql.toString()
				);
	};


	/**
	 * Aktualisiert die Tabelle für den Core-Type LehrerLeitungsfunktion.
	 */
	private final BiConsumer<DBEntityManager, Logger> updateLehrerLeitungsfunktionenKeys = (final DBEntityManager conn, final Logger logger) -> {
		final String tabname = "LehrerLeitungsfunktion_Keys";
		logger.logLn(strAktualisiereTabelle + tabname);
		updateCoreTypeTabelle(conn, tabname, LehrerLeitungsfunktion.class.getCanonicalName(), LehrerLeitungsfunktion.VERSION,
				Arrays.stream(LehrerLeitungsfunktion.values()).map(h -> "" + h.daten.id).distinct()
				.collect(Collectors.joining("), (", strInsertInto + tabname + "(ID) VALUES (", ")"))
				);
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
