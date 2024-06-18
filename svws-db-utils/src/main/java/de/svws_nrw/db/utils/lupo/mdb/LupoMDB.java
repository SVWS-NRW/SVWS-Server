package de.svws_nrw.db.utils.lupo.mdb;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.healthmarketscience.jackcess.DataType;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.DateTimeType;

import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostLeistungen;
import de.svws_nrw.core.data.schueler.Sprachbelegung;
import de.svws_nrw.core.data.schueler.Sprachendaten;
import de.svws_nrw.core.exceptions.UserNotificationException;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.Note;
import de.svws_nrw.core.types.SchuelerStatus;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import de.svws_nrw.core.types.gost.GostBesondereLernleistung;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.gost.GostLaufbahnplanungFachkombinationTyp;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.data.gost.DBUtilsGost;
import de.svws_nrw.data.schule.SchulUtils;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangBeratungslehrer;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFachkombinationen;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFaecher;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerFachbelegungen;
import de.svws_nrw.db.dto.current.schema.DTOSchemaAutoInkremente;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.gost.DTOFaecherNichtMoeglicheKombination;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.persistence.TypedQuery;
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse bietet Methoden zum Zugriff auf LuPO-Dateien im
 * Access-MDB-Format.
 */
public class LupoMDB {

	/** Der Logger für die Zugriff auf diese LuPO-Datei */
	public final Logger logger = new Logger();

	/** der Dateiname der LuPO-MDB-Datei */
	private final String filename;

	private List<ABPVersion> versionen = null;
	private List<ABPSchuldaten> schuldaten = null;
	private Map<String, ABPFachgruppen> fachgruppen = null;
	private Map<String, ABPFaecher> faecher = null;
	private List<ABPKursarten> kursarten = null;
	private List<ABPLehrer> lupoBenutzer = null;
	private List<ABPNichtMoeglAbiFachKombi> nichtMoeglicheKombinationen = null;
	private List<ABPSchueler> schueler = null;
	private List<ABPSchuelerFaecher> schuelerFaecher = null;
	private List<ABPSchuelerFaecherBasisSicherung> schuelerFaecherBasisSicherungen = null;
	private List<ABPSchuelerFaecherSicherung> schuelerFaecherSicherungen = null;
	private List<ABPSchuelerFehlermeldungen> fehlermeldungen = null;
	private List<ABPSchuelerSprachenfolge> schuelerSprachenfolge = null;

	/**
	 * Erzeugt eine neue Instanz für eine LuPO-MDB-Datenbankdatei.
	 * Es findet zunächst kein lesender oder schreibender Zugriff auf die Datei statt.
	 *
	 * @param filename   der Dateiname der LuPO-MDB-Datenbankdatei
	 */
	public LupoMDB(final String filename) {
		if (filename == null)
			throw new NullPointerException("LuPO-MDB-Dateiname muss angegeben werden.");
		this.filename = filename;
	}


	/**
	 * Kopiert alle Daten von dem anderen LupoMDB-Objekt.
	 *
	 * @param other   das andere LupoMDB-Objekt.
	 */
	public void copyFrom(final LupoMDB other) {
		versionen = other.versionen;
		schuldaten = other.schuldaten;
		fachgruppen = other.fachgruppen;
		faecher = other.faecher;
		kursarten = other.kursarten;
		lupoBenutzer = other.lupoBenutzer;
		nichtMoeglicheKombinationen = other.nichtMoeglicheKombinationen;
		schueler = other.schueler;
		schuelerFaecher = other.schuelerFaecher;
		schuelerFaecherBasisSicherungen = other.schuelerFaecherBasisSicherungen;
		schuelerFaecherSicherungen = other.schuelerFaecherSicherungen;
		fehlermeldungen = other.fehlermeldungen;
		schuelerSprachenfolge = other.schuelerSprachenfolge;
	}


	/**
	 * Importiert die Daten aus einer bestehenden LuPO-Datei
	 *
	 * @throws IOException   wird im Falle eines Fehlers beim Datenbank-Zugriff
	 *                       erzeugt.
	 */
	public void importFrom() throws IOException {
		try (Database db = DatabaseBuilder.open(new File(filename))) {
			db.setDateTimeType(DateTimeType.LOCAL_DATE_TIME);
			versionen = ABPVersion.read(db);
			schuldaten = ABPSchuldaten.read(db);
			fachgruppen = ABPFachgruppen.read(db);
			faecher = ABPFaecher.read(db);
			kursarten = ABPKursarten.read(db);
			lupoBenutzer = ABPLehrer.read(db);
			nichtMoeglicheKombinationen = ABPNichtMoeglAbiFachKombi.read(db);
			schueler = ABPSchueler.read(db);
			schuelerFaecher = ABPSchuelerFaecher.read(db);
			schuelerFaecherBasisSicherungen = ABPSchuelerFaecherBasisSicherung.read(db);
			schuelerFaecherSicherungen = ABPSchuelerFaecherSicherung.read(db);
			fehlermeldungen = ABPSchuelerFehlermeldungen.read(db);
			schuelerSprachenfolge = ABPSchuelerSprachenfolge.read(db);
		}
		// Prüfe, die eingelesen Daten, ob diese weiter verarbeitet werden können.
		for (final ABPSchueler s : schueler) {
			if (s.Schild_ID == null)
				throw new IOException("Fehler beim Einlesen der Schüler-Daten. Alle Schüler der LuPO-Datei müssen gültige IDs zugewiesen haben.");
		}
	}


	/**
	 * Exportiert die Daten in die angegebene LuPO-Datei
	 *
	 * @throws IOException   wird im Falle eines Fehlers beim Datenbank-Zugriff
	 *                       erzeugt.
	 */
	public void exportTo() throws IOException {
		logger.logLn("Schreibe LuPO-Daten in die Datei " + filename);
		logger.modifyIndent(2);
		try (Database db = new DatabaseBuilder(new File(filename))
				.setFileFormat(Database.FileFormat.V2000)
				.putSummaryProperty("Title", DataType.TEXT, "db1")
				.putSummaryProperty("Author", DataType.TEXT, "SVWS-Server-Team")
				.putSummaryProperty("Company", DataType.TEXT, "NRW")
				.create()) {
			db.setDateTimeType(DateTimeType.LOCAL_DATE_TIME);
			ABPVersion.write(db, versionen);
			ABPSchuldaten.write(db, schuldaten);
			ABPFachgruppen.write(db, fachgruppen);
			ABPFaecher.write(db, faecher);
			ABPKursarten.write(db, kursarten);
			ABPLehrer.write(db, lupoBenutzer);
			ABPNichtMoeglAbiFachKombi.write(db, nichtMoeglicheKombinationen);
			ABPSchueler.write(db, schueler);
			ABPSchuelerFaecher.write(db, schuelerFaecher);
			ABPSchuelerFaecherBasisSicherung.write(db, schuelerFaecherBasisSicherungen);
			ABPSchuelerFaecherSicherung.write(db, schuelerFaecherSicherungen);
			ABPSchuelerFehlermeldungen.write(db, fehlermeldungen);
			ABPSchuelerSprachenfolge.write(db, schuelerSprachenfolge);
		}
		logger.logLn("Fertig!");
		logger.modifyIndent(-2);
	}


	/**
	 * Schreibt die Daten für eine leere LuPO-MDB in dieses Objekt
	 */
	public void getEmpty() {
		versionen = ABPVersion.getDefault();
		schuldaten = ABPSchuldaten.getDefault();
		fachgruppen = ABPFachgruppen.getDefault();
		faecher = ABPFaecher.getDefault();
		kursarten = ABPKursarten.getDefault();
		lupoBenutzer = ABPLehrer.getDefault();
		nichtMoeglicheKombinationen = ABPNichtMoeglAbiFachKombi.getDefault();
		schueler = ABPSchueler.getDefault();
		schuelerFaecher = ABPSchuelerFaecher.getDefault();
		schuelerFaecherBasisSicherungen = ABPSchuelerFaecherBasisSicherung.getDefault();
		schuelerFaecherSicherungen = ABPSchuelerFaecherSicherung.getDefault();
		fehlermeldungen = ABPSchuelerFehlermeldungen.getDefault();
		schuelerSprachenfolge = ABPSchuelerSprachenfolge.getDefault();
	}


	/**
	 * Schreibt die Daten für den übergebenen Jahrgang der Sekundarstufe II in dieses Objekt
	 *
	 * @param user       der Datenbank-Benutzer für den SVWS-Datenbankzugriff (siehe {@link Benutzer})
	 * @param jahrgang   der Jahrgang, der in diese LuPO-MDB geschrieben werden soll ("EF", "Q1" oder "Q2").
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public void getFromLeistungsdaten(final Benutzer user, final String jahrgang) throws ApiOperationException {
		try (DBEntityManager conn = user.getEntityManager()) {
			final @NotNull DTOEigeneSchule schule = SchulUtils.getDTOSchule(conn);
			final @NotNull DTOSchuljahresabschnitte schuljahresabschnitt = SchulUtils.getSchuljahreabschnitt(conn, schule.Schuljahresabschnitts_ID);
			if ((jahrgang == null) || ((!"EF".equalsIgnoreCase(jahrgang)) && (!"Q1".equalsIgnoreCase(jahrgang)) && (!"Q2".equalsIgnoreCase(jahrgang)))) {
				logger.logLn("Ungültiger Jahrgang! Erzeuge Daten für eine leere LuPO-Datei...");
				logger.modifyIndent(2);
				getEmpty();
				logger.modifyIndent(-2);
				return;
			}
			logger.logLn("Lese Daten für den Jahrgang " + jahrgang + " aus der SVWS-Datenbank...");
			logger.modifyIndent(2);
			final List<DTOFach> dtofaecher = conn.queryAll(DTOFach.class).stream().sorted((f1, f2) -> {
				if (f1.SortierungAllg == null)
					return -1;
				return f2.SortierungAllg == null ? 1 : f2.SortierungAllg - f1.SortierungAllg;
			}).toList();
			final Map<Long, DTOFach> dtoFaecherMap = dtofaecher.stream().collect(Collectors.toMap(f -> f.ID, f -> f));
			final List<DTOFaecherNichtMoeglicheKombination> dtoFaecherNichtMoeglicheKombination = conn.queryAll(DTOFaecherNichtMoeglicheKombination.class);
			final TypedQuery<DTOSchueler> queryDtoSchueler = conn.query(
					"SELECT s FROM DTOSchueler s JOIN DTOSchuelerLernabschnittsdaten l ON "
							+ "s.ID IS NOT NULL AND s.ID = l.Schueler_ID AND s.Schuljahresabschnitts_ID = l.Schuljahresabschnitts_ID "
							+ "AND (s.Geloescht = null OR s.Geloescht = false) AND s.Status = :status AND l.ASDJahrgang = :jahrgang "
							+ "ORDER BY s.Nachname, s.Vorname",
					DTOSchueler.class);
			final List<DTOSchueler> dtoSchueler = queryDtoSchueler
					.setParameter("status", SchuelerStatus.AKTIV)
					.setParameter("jahrgang", jahrgang)
					.getResultList();
			final List<Long> schuelerIDs = dtoSchueler.stream().map(s -> s.ID).toList();
			final Map<Long, GostLeistungen> gostLeistungen = DBUtilsGost.getLeistungsdaten(conn, schuelerIDs);
			final Map<Long, DTOGostSchueler> dtoLupoSchueler = conn.queryAll(DTOGostSchueler.class).stream()
					.collect(Collectors.toMap(s -> s.Schueler_ID, s -> s));
			final Map<Long, DTOLehrer> mapLehrer = conn.queryAll(DTOLehrer.class).stream().collect(Collectors.toMap(l -> l.ID, l -> l));
			final Map<Long, DTOKlassen> mapKlassen = conn.queryAll(DTOKlassen.class).stream().collect(Collectors.toMap(k -> k.ID, k -> k));
			final TypedQuery<DTOSchuelerLernabschnittsdaten> queryDtoSchuelerLernabschnitte = conn.query(
					"SELECT l FROM DTOSchueler s JOIN DTOSchuelerLernabschnittsdaten l ON s.ID IN :value AND s.ID = l.Schueler_ID AND"
							+ " s.Schuljahresabschnitts_ID = l.Schuljahresabschnitts_ID",
					DTOSchuelerLernabschnittsdaten.class);
			final Map<Long, DTOSchuelerLernabschnittsdaten> mapAktAbschnitte = queryDtoSchuelerLernabschnitte
					.setParameter("value", schuelerIDs)
					.getResultList().stream().collect(Collectors.toMap(l -> l.Schueler_ID, l -> l));
			versionen = ABPVersion.getDefault();
			schuldaten = ABPSchuldaten.get(schule, jahrgang, schuljahresabschnitt.Abschnitt);
			fachgruppen = ABPFachgruppen.getDefault();
			faecher = ABPFaecher.get(fachgruppen, dtofaecher, dtoFaecherMap);
			kursarten = ABPKursarten.getDefault();
			lupoBenutzer = ABPLehrer.getDefault();
			nichtMoeglicheKombinationen = ABPNichtMoeglAbiFachKombi.get(dtoFaecherNichtMoeglicheKombination, dtofaecher, dtoFaecherMap);
			schueler = ABPSchueler.get(dtoSchueler, mapAktAbschnitte, mapKlassen, mapLehrer, dtoLupoSchueler, gostLeistungen);
			schuelerFaecher = ABPSchuelerFaecher.get(faecher, fachgruppen, dtoSchueler, dtoLupoSchueler, gostLeistungen);
			schuelerFaecherBasisSicherungen = ABPSchuelerFaecherBasisSicherung.getDefault();
			schuelerFaecherSicherungen = ABPSchuelerFaecherSicherung.getDefault();
			fehlermeldungen = ABPSchuelerFehlermeldungen.getDefault();
			schuelerSprachenfolge = ABPSchuelerSprachenfolge.getDefault();
			logger.logLn("Fertig!");
			logger.modifyIndent(-2);
		}
	}



	/**
	 * Schreibt die Jahrgangs-bezogenen Daten aus diesem Objekt in die zugehörigen LUPO-Tabellen
	 * für den angegeben Jahrgang.
	 *
	 * @param conn          die Datenbank-Verbindung
	 * @param abschnitt     der aktuelle Schuljahresabschnitt
	 * @param abiJahrgang   der Abitur-Jahrgang, für den die Informationen gesetzt werden sollen.
	 * @param klasse        die zugehörige Klasse des Jahrgangs, aus der ggf. Informationen zu den
	 *                      Beratungslehrern entnommen werden können (für Default-Einträge).
	 * @param dtoFaecher    die in der SVWS-DB definierten Fächer
	 * @param replace       gibt an, ob alte Daten für den Jahrgang der LuPO-Datei ersetzt werden
	 *                      sollen, sofern sie bereits vorhanden sind.
	 */
	private void setLUPOJahrgang(final DBEntityManager conn, final DTOSchuljahresabschnitte abschnitt, final int abiJahrgang, final String klasse,
			final Map<String, DTOFach> dtoFaecher, final boolean replace) {
		DTOGostJahrgangsdaten lupoJahrgangsdaten = conn.queryByKey(DTOGostJahrgangsdaten.class, abiJahrgang);
		if (lupoJahrgangsdaten != null) {
			logger.log("* Abiturjahrgang ist bereits angelegt");
			if (!replace) {
				logger.logLn(0, " - nichts zu tun...");
				return;
			}
			logger.logLn(0, " - ersetze die Daten...");
		}
		// Entferne zunächst alte jahrgangsbezogene Daten aus den LuPO-Tabellen
		conn.transactionNativeDelete("DELETE FROM Gost_Jahrgang_Beratungslehrer WHERE Abi_Jahrgang = " + abiJahrgang);
		conn.transactionFlush();
		conn.transactionNativeDelete("DELETE FROM Gost_Jahrgang_Faecher WHERE Abi_Jahrgang = " + abiJahrgang);
		conn.transactionFlush();
		conn.transactionNativeDelete("DELETE FROM Gost_Jahrgang_Fachkombinationen WHERE Abi_Jahrgang = " + abiJahrgang);
		conn.transactionFlush();

		// Schreibe jahrgangsbezogene Daten in die LuPO-Tabelle
		logger.logLn("* Schreibe Jahrgangebezogene LuPO-Daten in die DB...");
		logger.modifyIndent(2);
		logger.logLn("- allgemeine Daten für den Jahrgang...");
		final ABPSchuldaten abpSchule = schuldaten.get(0);
		if (lupoJahrgangsdaten == null)
			lupoJahrgangsdaten = new DTOGostJahrgangsdaten(abiJahrgang);
		lupoJahrgangsdaten.TextBeratungsbogen = abpSchule.BeratungsText;
		lupoJahrgangsdaten.TextMailversand = abpSchule.MailText;
		lupoJahrgangsdaten.ZusatzkursGEVorhanden = abpSchule.ZusatzkursGeschichteVorhanden;
		lupoJahrgangsdaten.ZusatzkursGEErstesHalbjahr = abpSchule.ZusatzkursGeschichteBeginn.kuerzel;
		lupoJahrgangsdaten.ZusatzkursSWVorhanden = abpSchule.ZusatzkursSoWiVorhanden;
		lupoJahrgangsdaten.ZusatzkursSWErstesHalbjahr = abpSchule.ZusatzkursSoWiBeginn.kuerzel;
		conn.transactionPersist(lupoJahrgangsdaten);
		conn.transactionFlush();

		logger.logLn("- die Beratungslehrer anhand der Klassenlehrerinformationen...");
		final List<DTOKlassen> klassen = conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, abschnitt.ID)
				.stream().filter(kl -> kl.Klasse.equals(klasse)).toList();
		final List<Long> klassenIDs = klassen.stream().map(kl -> kl.ID).toList();
		final Map<Long, List<DTOKlassenLeitung>> mapKlassenLeitung = conn.queryList(DTOKlassenLeitung.QUERY_LIST_BY_KLASSEN_ID,
				DTOKlassenLeitung.class, klassenIDs).stream().collect(Collectors.groupingBy(kl -> kl.Klassen_ID));

		final Map<Long, DTOLehrer> lehrer = conn.queryAll(DTOLehrer.class).stream().collect(Collectors.toMap(l -> l.ID, l -> l));
		logger.modifyIndent(2);
		for (final DTOKlassen kl : klassen) {
			final List<DTOKlassenLeitung> klassenleitungen = mapKlassenLeitung.get(kl.ID);
			if ((klassenleitungen != null) && !klassenleitungen.isEmpty()) {
				for (final DTOKlassenLeitung kll : klassenleitungen) {
					logger.logLn("- Klassenlehrer der Klasse " + kl.Klasse + "...");
					final DTOLehrer l = lehrer.get(kll.Lehrer_ID);
					if (l != null) {
						conn.transactionPersist(new DTOGostJahrgangBeratungslehrer(abiJahrgang, l.ID));
						conn.transactionFlush();
					}
				}
			}
		}
		logger.modifyIndent(-2);

		logger.logLn("- die Fachinformationen für den Jahrgang");
		logger.modifyIndent(2);
		for (final ABPFaecher abpFach : faecher.values()) {
			logger.log("- Fach " + abpFach.FachKrz + ": ");
			final DTOFach dtoFach = dtoFaecher.get(abpFach.FachKrz);
			if (dtoFach == null) {
				logger.logLn(0, "FEHLER - Fach in der DB nicht definiert!");
				continue;
			}
			final DTOGostJahrgangFaecher lupoFach = new DTOGostJahrgangFaecher(abiJahrgang, dtoFach.ID,
					abpFach.E1, abpFach.E2, abpFach.Q1, abpFach.Q2, abpFach.Q3, abpFach.Q4, abpFach.Abi_Moegl,
					abpFach.LK_Moegl);
			lupoFach.WochenstundenQPhase = dtoFach.WochenstundenQualifikationsphase;
			conn.transactionPersist(lupoFach);
			conn.transactionFlush();
			logger.logLn(0, "OK");
		}
		logger.modifyIndent(-2);

		logger.logLn("- die nicht möglichen Abitur-Fachkombinationen für den Jahrgang");
		logger.modifyIndent(2);
		if (!nichtMoeglicheKombinationen.isEmpty()) {
			// Bestimme die ID, für welche der Datensatz eingefügt wird
			final DTOSchemaAutoInkremente dbNmkID = conn.queryByKey(DTOSchemaAutoInkremente.class, "Gost_Jahrgang_Fachkombinationen");
			long idNMK = dbNmkID == null ? 1 : dbNmkID.MaxID + 1;
			for (final ABPNichtMoeglAbiFachKombi nmk : nichtMoeglicheKombinationen) {
				logger.log("- Fachkombination " + nmk.Fach1_Krz + " (" + nmk.Kursart1 + ") <-> " + nmk.Fach2_Krz + " (" + nmk.Kursart2 + "): ");
				final DTOFach dtoFach1 = dtoFaecher.get(nmk.Fach1_Krz);
				if (dtoFach1 == null) {
					logger.logLn(0, "FEHLER - Fach 1 der Kombination in der DB nicht definiert!");
					continue;
				}
				if (Boolean.FALSE.equals(dtoFach1.IstOberstufenFach)) {
					logger.logLn(0, "FEHLER - Fach 1 der Kombination ist nicht als Fach der Oberstufe gekennzeichnet!");
					continue;
				}
				final DTOFach dtoFach2 = dtoFaecher.get(nmk.Fach2_Krz);
				if (dtoFach2 == null) {
					logger.logLn(0, "FEHLER - Fach 2 der Kombination in der DB nicht definiert!");
					continue;
				}
				if (Boolean.FALSE.equals(dtoFach2.IstOberstufenFach)) {
					logger.logLn(0, "FEHLER - Fach 2 der Kombination ist nicht als Fach der Oberstufe gekennzeichnet!");
					continue;
				}
				GostLaufbahnplanungFachkombinationTyp typ = GostLaufbahnplanungFachkombinationTyp.VERBOTEN;
				if (nmk.Typ != null)
					typ = ("+".equals(nmk.Typ) ? GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH : GostLaufbahnplanungFachkombinationTyp.VERBOTEN);
				final DTOGostJahrgangFachkombinationen lupoNMK = new DTOGostJahrgangFachkombinationen(idNMK++, abiJahrgang,
						dtoFach1.ID, dtoFach2.ID, !"Q1Q4".equals(nmk.Phase), !"Q1Q4".equals(nmk.Phase), true, true, true, true,
						typ, "");
				lupoNMK.Abi_Jahrgang = abiJahrgang;
				lupoNMK.Kursart1 = nmk.Kursart1;
				lupoNMK.Kursart2 = nmk.Kursart2;
				conn.transactionPersist(lupoNMK);
				conn.transactionFlush();
				final DTOGostJahrgangFachkombinationen lupoNMK2 = new DTOGostJahrgangFachkombinationen(idNMK++, abiJahrgang,
						dtoFach2.ID, dtoFach1.ID, !"Q1Q4".equals(nmk.Phase), !"Q1Q4".equals(nmk.Phase), true, true, true, true,
						typ, "");
				lupoNMK2.Abi_Jahrgang = abiJahrgang;
				lupoNMK2.Kursart1 = nmk.Kursart2;
				lupoNMK2.Kursart2 = nmk.Kursart1;
				conn.transactionPersist(lupoNMK2);
				conn.transactionFlush();
				logger.logLn(0, "OK");
			}
		}
		logger.modifyIndent(-2);

		logger.modifyIndent(-2);
	}


	/**
	 * Schreibt die Schüler-bezogenen Daten aus diesem Objekt in die zugehörigen LUPO-Tabellen
	 * für den angegeben Schüler. Vorhande Fachwahlen und Laufbahninformationen des Schülers
	 * werden ggf. ersetzt.
	 *
	 * @param conn                 die Datenbank-Verbindung mit aktiver Transaktion
	 * @param mapFaecher           eine Map mit den Fächern aus der SVWS-DB
	 * @param dtoSchueler          das Schüler-DTO aus der SVWS-DTO
	 * @param abpSchueler          das zu schreibende Schüler-Objekt aus der LuPO-Datei
	 * @param mapSchuelerFaecher   die zu schreibenden Fachwahlen des Schülers
	 */
	private void setLUPOSchueler(final DBEntityManager conn, final Map<String, DTOFach> mapFaecher, final DTOSchueler dtoSchueler,
			final ABPSchueler abpSchueler, final HashMap<Integer, ArrayList<ABPSchuelerFaecher>> mapSchuelerFaecher) {
		logger.logLn("- Schreibe Allgemeine Schüler-Daten in die DB... ");
		DTOGostSchueler lupoSchueler = conn.queryByKey(DTOGostSchueler.class, dtoSchueler.ID);
		if (lupoSchueler == null) {
			lupoSchueler = new DTOGostSchueler(dtoSchueler.ID, (abpSchueler.Sportattest != null) || ("J".equals(abpSchueler.Sportattest)));
		} else {
			lupoSchueler.HatSportattest = (abpSchueler.Sportattest != null) || ("J".equals(abpSchueler.Sportattest));
		}
		lupoSchueler.DatumBeratung = abpSchueler.DatumBeratung == null ? null : abpSchueler.DatumBeratung.toLocalDate().toString();
		lupoSchueler.DatumRuecklauf = abpSchueler.DatumRuecklauf == null ? null : abpSchueler.DatumRuecklauf.toLocalDate().toString();
		lupoSchueler.Beratungslehrer_ID = null;  // TODO LehrerID aus abpSchueler.Beratungslehrer herausfinden?
		lupoSchueler.Kommentar = abpSchueler.Kommentar;
		lupoSchueler.PruefPhase = abpSchueler.PruefPhase;
		lupoSchueler.BesondereLernleistung_Art = abpSchueler.BLL_Art;
		lupoSchueler.BesondereLernleistung_Punkte = abpSchueler.BLL_Punkte;
		conn.transactionPersist(lupoSchueler);
		conn.transactionFlush();

		// Entferne vorab ggf. bereits vorhandene Fachwahlen des Schülers
		conn.transactionExecuteDelete("DELETE FROM DTOGostSchuelerFachbelegungen e WHERE e.Schueler_ID = %d".formatted(dtoSchueler.ID));

		logger.logLn("- Schreibe Fachbezogene Schüler-Daten in die DB... ");
		final ArrayList<ABPSchuelerFaecher> abpSFaecher = mapSchuelerFaecher.get(abpSchueler.ID);
		if (abpSFaecher != null) {
			logger.modifyIndent(2);
			for (final ABPSchuelerFaecher abpSFach : abpSFaecher) {
				logger.log("- Fach " + abpSFach.FachKrz + ": ");
				final DTOFach dtoFach = mapFaecher.get(abpSFach.FachKrz);
				if (dtoFach == null) {
					logger.logLn(0, "FEHLER! Ignoriere das Fach beim Einlesen...");
					continue;
				}
				final DTOGostSchuelerFachbelegungen lupoSFach = new DTOGostSchuelerFachbelegungen(dtoSchueler.ID, dtoFach.ID);
				lupoSFach.EF1_Kursart = convertBlankToNull(abpSFach.Kursart_E1);
				lupoSFach.EF1_Punkte = convertBlankToNull(abpSFach.Punkte_E1);
				lupoSFach.EF2_Kursart = convertBlankToNull(abpSFach.Kursart_E2);
				lupoSFach.EF2_Punkte = convertBlankToNull(abpSFach.Punkte_E2);
				lupoSFach.Q11_Kursart = convertBlankToNull(abpSFach.Kursart_Q1);
				lupoSFach.Q11_Punkte = convertBlankToNull(abpSFach.Punkte_Q1);
				lupoSFach.Q12_Kursart = convertBlankToNull(abpSFach.Kursart_Q2);
				lupoSFach.Q12_Punkte = convertBlankToNull(abpSFach.Punkte_Q2);
				lupoSFach.Q21_Kursart = convertBlankToNull(abpSFach.Kursart_Q3);
				lupoSFach.Q21_Punkte = convertBlankToNull(abpSFach.Punkte_Q3);
				lupoSFach.Q22_Kursart = convertBlankToNull(abpSFach.Kursart_Q4);
				lupoSFach.Q22_Punkte = convertBlankToNull(abpSFach.Punkte_Q4);
				lupoSFach.AbiturFach = abpSFach.AbiturFach;
				lupoSFach.Bemerkungen = convertBlankToNull(abpSFach.Bemerkungen);
				lupoSFach.Markiert_Q1 = (abpSFach.Markiert_Q1 != null) && "J".equals(abpSFach.Markiert_Q1);
				lupoSFach.Markiert_Q2 = (abpSFach.Markiert_Q2 != null) && "J".equals(abpSFach.Markiert_Q2);
				lupoSFach.Markiert_Q3 = (abpSFach.Markiert_Q3 != null) && "J".equals(abpSFach.Markiert_Q3);
				lupoSFach.Markiert_Q4 = (abpSFach.Markiert_Q4 != null) && "J".equals(abpSFach.Markiert_Q4);
				lupoSFach.ergebnisAbiturpruefung = abpSFach.AbiPruefErgebnis;
				lupoSFach.hatMuendlichePflichtpruefung = abpSFach.MdlPflichtPruefung == null ? null : "J".equals(abpSFach.MdlPflichtPruefung);
				lupoSFach.ergebnisMuendlichePruefung = abpSFach.MdlPruefErgebnis;
				conn.transactionPersist(lupoSFach);
				conn.transactionFlush();
				logger.logLn(0, "OK");
			}
			logger.modifyIndent(-2);
		}
		logger.modifyIndent(-2);
	}


	/**
	 * Schreibt die Daten aus diesem Objekt in die zugehörigen LUPO-Tabellen.
	 *
	 * @param user               der Datenbank-Benutzer für den SVWS-Datenbankzugriff (siehe {@link Benutzer})
	 * @param replaceJahrgang    gibt an, ob alte Daten für den Jahrgang der LuPO-Datei ersetzt werden
	 *                           sollen, sofern sie bereits vorhanden sind.
	 * @param replaceSchueler    gibt an, ob alte Daten für die Schüler der LuPO-Datei ersetzt werden
	 *                           sollen, sofern sie bereits vorhanden sind.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public void setLUPOTables(final Benutzer user, final boolean replaceJahrgang, final boolean replaceSchueler) throws ApiOperationException {
		try (DBEntityManager conn = user.getEntityManager()) {
			try {
				conn.transactionBegin();

				final @NotNull DTOEigeneSchule schule = SchulUtils.getDTOSchule(conn);
				final Map<Long, DTOSchuljahresabschnitte> mapSchuljahresabschnitte = conn.queryAll(DTOSchuljahresabschnitte.class).stream()
						.collect(Collectors.toMap(a -> a.ID, a -> a));
				final Map<Long, DTOKlassen> mapKlassen = conn.queryAll(DTOKlassen.class).stream().collect(Collectors.toMap(k -> k.ID, k -> k));
				final DTOSchuljahresabschnitte schuleAbschnitt = mapSchuljahresabschnitte.get(schule.Schuljahresabschnitts_ID);

				logger.logLn("Informationen zu der LuPO-Datei...");
				logger.modifyIndent(2);
				logger.logLn("- Schulhalbjahr der SVWS-DB: " + schuleAbschnitt.Jahr + "." + schuleAbschnitt.Abschnitt);
				if (schuldaten.isEmpty()) {
					logger.logLn("- FEHLER: Fehlender Eintrag für die aktuelle Schule in den LuPO-Daten!");
					logger.modifyIndent(-2);
					throw new UserNotificationException("Die LuPO-Datei ist fehlerhaft und hat keine Einträge für die aktuelle Schule.");
				}
				final ABPSchuldaten abpSchule = schuldaten.get(0);
				logger.logLn("- Beratungshalbjahr der LuPO-Datei: " + abpSchule.Beratungshalbjahr);
				logger.modifyIndent(-2);

				logger.logLn("Vorbereitung...");
				logger.modifyIndent(2);
				logger.logLn("  - Erzeuge HashMap mit der Zuordnung der Fächer zum Schüler aus der LuPO-Tabelle...");
				final HashMap<Integer, ArrayList<ABPSchuelerFaecher>> mapSchuelerFaecher = new HashMap<>();
				for (final ABPSchuelerFaecher abpSchuelerFaecher : schuelerFaecher)
					mapSchuelerFaecher.computeIfAbsent(abpSchuelerFaecher.Schueler_ID, k -> new ArrayList<>()).add(abpSchuelerFaecher);
				logger.logLn("  - Bestimme die zu bearbeitende Schüler-Menge aus der LuPO-Datei...");
				final List<Long> schuelerIDs = schueler.stream().filter(s -> s.Schild_ID != null).map(s -> (long) s.Schild_ID).toList();
				logger.logLn("  - Lese Schüler aus der DB ein, um diese mit den Daten der LuPO-Datei abzugleichen...");
				final Map<Long, DTOSchueler> dtoSchuelerMap = conn.queryByKeyList(DTOSchueler.class, schuelerIDs)
						.stream().collect(Collectors.toMap(s -> s.ID, s -> s));
				logger.logLn("  - Lese die aktuellen Lernabschnitte der Schüler aus der DB ein, um davon Daten mit den Daten aus der LuPO-Datei"
						+ " abzugleichen...");
				final Map<Long, DTOSchuelerLernabschnittsdaten> mapSchuelerAktLernabschnitt = conn.query(
						"SELECT l FROM DTOSchueler s JOIN DTOSchuelerLernabschnittsdaten l ON l.Schueler_ID = s.ID AND"
								+ " l.Schuljahresabschnitts_ID = s.Schuljahresabschnitts_ID AND l.Schueler_ID IN :value",
						DTOSchuelerLernabschnittsdaten.class).setParameter("value", schuelerIDs).getResultList().stream()
						.collect(Collectors.toMap(l -> l.Schueler_ID, l -> l));
				logger.logLn("  - Lese Jahrgänge aus der DB ein, um diese beim Abgleich mit den Daten der LuPO-Datei zu verwenden...");
				final Map<Long, DTOJahrgang> mapJahrgaenge = conn.queryAll(DTOJahrgang.class).stream().collect(Collectors.toMap(j -> j.ID, j -> j));
				logger.logLn("  - Lese Fächer aus der DB ein, um diese beim Abgleich mit den Daten der LuPO-Datei zu verwenden...");
				final Map<String, DTOFach> mapFaecher = conn.queryAll(DTOFach.class).stream().collect(Collectors.toMap(f -> f.Kuerzel, f -> f));
				logger.modifyIndent(-2);

				// zum Überprüfen, dass nicht mehr als ein Abiturjahrgang in der LuPO-Datei enthalten ist
				Integer lupoAbijahrgang = null;

				logger.logLn("Prüfe Schülerdaten...");
				logger.modifyIndent(2);
				for (final ABPSchueler abpSchueler : schueler) {
					logger.logLn("- Lese LuPO-Schüler " + abpSchueler.ID + " mit der DB-ID " + abpSchueler.Schild_ID + " ein...");
					logger.modifyIndent(2);
					final DTOSchueler dtoSchueler = dtoSchuelerMap.get(abpSchueler.Schild_ID == null ? null : (long) abpSchueler.Schild_ID);
					if (dtoSchueler == null) {
						logger.logLn("- FEHLER: Der Schüler konnte nicht in der DB gefunden werden. Überspringe Schüler!");
						logger.modifyIndent(-2);
						continue;
					}

					final DTOSchuljahresabschnitte dtoAbschnittSchueler = mapSchuljahresabschnitte.get(dtoSchueler.Schuljahresabschnitts_ID);
					if (dtoAbschnittSchueler == null) {
						logger.logLn("- FEHLER: Der Schuljahresabschnitt des Schülers konnte nicht in der DB gefunden werden. Überspringe Schüler!");
						logger.modifyIndent(-2);
						continue;
					}
					final DTOSchuelerLernabschnittsdaten dtoAktAbschnitt = mapSchuelerAktLernabschnitt.get(dtoSchueler.ID);
					if (dtoAktAbschnitt == null) {
						logger.logLn("- FEHLER: Der Lernabschnitt des Schülers konnte nicht in der DB gefunden werden. Überspringe Schüler!");
						logger.modifyIndent(-2);
						continue;
					}

					logger.log("- Ermittle Abiturjahrgang: ");
					final DTOJahrgang dtoJahrgang = mapJahrgaenge.get(dtoAktAbschnitt.Jahrgang_ID);
					if (dtoJahrgang == null) {
						logger.logLn("FEHLER! Überspringe diesen Schüler!");
						logger.modifyIndent(-2);
						continue;
					}
					final int restjahre = dtoJahrgang.AnzahlRestabschnitte / schule.AnzahlAbschnitte;
					final int abiJahrgang = dtoAbschnittSchueler.Jahr + restjahre;
					logger.logLn(0, "" + abiJahrgang);
					logger.log("- Prüfe, ob der aktuelle Abiturjahrgang oder ein anderer Abiturjahrgang bereits vorgekommen ist: ");
					if (lupoAbijahrgang == null) {
						logger.logLn(0, "Nein");
						lupoAbijahrgang = abiJahrgang;
						logger.modifyIndent(2);
						final DTOKlassen klasse = mapKlassen.get(dtoAktAbschnitt.Klassen_ID);
						setLUPOJahrgang(conn, schuleAbschnitt, abiJahrgang, klasse == null ? null : klasse.Klasse, mapFaecher, replaceJahrgang);
						logger.modifyIndent(-2);
					} else if (Integer.compare(lupoAbijahrgang, abiJahrgang) == 0) {
						logger.logLn(0, "Ja");
					} else {
						throw new UserNotificationException("Fehler: Es dürfen für den Import nicht Schüler aus mehreren Abiturjahrgänge in einer LuPO-Datei"
								+ " vorkommen. Bitte passen Sie die LuPO-Datei für den Import an.");
					}

					// Prüfe, ob dem Schüler bereits Laufbahnplanungsdaten zugewiesen wurden
					logger.logLn("- Prüfe, ob für den Schüler bereits Laufbahnplanungsdaten vorhanden sind... ");
					final DTOGostSchueler lupoSchuelerVorhanden = conn.queryByKey(DTOGostSchueler.class, dtoSchueler.ID);
					final boolean hatLaufbahnplanungdaten = (lupoSchuelerVorhanden != null);
					if (hatLaufbahnplanungdaten) {
						if (!replaceSchueler) {
							logger.logLn("  - HINWEIS: Für den Schüler liegen bereits Laufbahnplanungsdaten vor. Überspringe Schüler!");
							logger.modifyIndent(-2);
							continue;
						}
						logger.logLn("  - HINWEIS: Für den Schüler liegen bereits Laufbahnplanungsdaten vor. Diese werden ersetzt...");
					}
					setLUPOSchueler(conn, mapFaecher, dtoSchueler, abpSchueler, mapSchuelerFaecher);
				}
				if (!conn.transactionCommit())
					throw new UserNotificationException("Fehler beim Erstellen des Schemas - Datenbank-Transaktion konnte nicht abgeschlossen werden.");
			} catch (final Exception e) {
				logger.logLn("Fehler: " + e.getMessage());
				if (e instanceof UserNotificationException)
					throw e;
				throw new UserNotificationException("Unerwarteter Fehler beim Importieren der Daten: " + e.getMessage());
			} finally {
				logger.modifyIndent(-2);
				conn.transactionRollback();
			}
		}
	}


	private static String convertBlankToNull(final String input) {
		if ((input == null) || (input.isBlank()))
			return null;
		return input;
	}


	/**
	 * Ermittelt die Schulform, für welche die LuPO-Datei generiert wurde.
	 *
	 * @return die Schulform
	 */
	public Schulform retrieveSchulform() {
		if (schuldaten.size() != 1)
			return null;
		return Schulform.getByKuerzel(schuldaten.get(0).SchulformKrz);
	}


	/**
	 * Ermittelt die Liste der Fächer der gymnasialen Oberstufe aus der LuPO-Datei.
	 * Die Informationen können dabei gegegenüber Daten aus einer SVWS-DB
	 * unvollständig sein!
	 *
	 * @return die Liste der Fächer der gymnasialen Oberstufe
	 */
	public List<GostFach> retrieveGostFaecher() {
		final ArrayList<GostFach> result = new ArrayList<>();
		for (final ABPFaecher lupoFach : faecher.values()) {
			final GostFach gostFach = new GostFach();
			gostFach.id = lupoFach.ID;
			gostFach.kuerzel = lupoFach.StatistikKrz;
			gostFach.kuerzelAnzeige = lupoFach.FachKrz;
			gostFach.bezeichnung = lupoFach.Bezeichnung;
			gostFach.sortierung = lupoFach.Sortierung;
			gostFach.istFremdsprache = lupoFach.IstSprache;
			gostFach.istFremdSpracheNeuEinsetzend = lupoFach.AlsNeueFSInSII;
			gostFach.biliSprache = lupoFach.Unterichtssprache;

			gostFach.istMoeglichAbiLK = lupoFach.LK_Moegl;
			gostFach.istMoeglichAbiGK = lupoFach.Abi_Moegl;
			gostFach.istMoeglichEF1 = lupoFach.E1;
			gostFach.istMoeglichEF2 = lupoFach.E2;
			gostFach.istMoeglichQ11 = lupoFach.Q1;
			gostFach.istMoeglichQ12 = lupoFach.Q2;
			gostFach.istMoeglichQ21 = lupoFach.Q3;
			gostFach.istMoeglichQ22 = lupoFach.Q4;

			if (lupoFach.Q_WStd != null)
				gostFach.wochenstundenQualifikationsphase = lupoFach.Q_WStd;
			else
				gostFach.wochenstundenQualifikationsphase = ("VX".equals(lupoFach.StatistikKrz) ? 2 : 3);

			final ABPFaecher leitfach1 = faecher.get(lupoFach.Leitfach);
			final ABPFaecher leitfach2 = faecher.get(lupoFach.Leitfach2);
			if (leitfach1 == null) {
				gostFach.projektKursLeitfach1ID = null;
				gostFach.projektKursLeitfach1Kuerzel = null;
			} else {
				gostFach.projektKursLeitfach1ID = (long) leitfach1.ID;
				gostFach.projektKursLeitfach1Kuerzel = leitfach1.FachKrz;
			}
			if (leitfach2 == null) {
				gostFach.projektKursLeitfach2ID = null;
				gostFach.projektKursLeitfach2Kuerzel = null;
			} else {
				gostFach.projektKursLeitfach2ID = (long) leitfach2.ID;
				gostFach.projektKursLeitfach2Kuerzel = leitfach2.FachKrz;
			}
			result.add(gostFach);
		}
		return result;
	}


	/**
	 * Ermittelt die Abiturdaten der Schüler aus der LuPO-MDB-Datei. Die
	 * Informationen können dabei gegegenüber Daten aus einer SVWS-DB
	 * unvollständig sein!
	 *
	 * @return die Liste der Abiturdaten der Schüler
	 */
	public List<Abiturdaten> retrieveAbiturdaten() {
		final HashMap<Long, Abiturdaten> alleAbiturdaten = new HashMap<>();
		final ArrayList<Abiturdaten> result = new ArrayList<>();
		for (final ABPSchueler lupoSchueler : schueler) {
			final Abiturdaten abidaten = new Abiturdaten();
			abidaten.schuelerID = lupoSchueler.ID;
			abidaten.abiturjahr = 2020; // fiktives Schuljahr (in der Lupo-MDB nicht vorhanden
			abidaten.schuljahrAbitur = 2021; // s.o.

			abidaten.bilingualeSprache = lupoSchueler.Bilingual;

			abidaten.besondereLernleistung = GostBesondereLernleistung.fromKuerzel(lupoSchueler.BLL_Art).kuerzel;
			abidaten.besondereLernleistungNotenKuerzel = Note.fromNotenpunkte(lupoSchueler.BLL_Punkte).kuerzel;
			try {
				abidaten.block1AnzahlKurse = Integer.parseInt(lupoSchueler.AnzK_Summe);
			} catch (@SuppressWarnings("unused") final NumberFormatException e) {
				abidaten.block1AnzahlKurse = null;
			}
			abidaten.block1Zulassung = lupoSchueler.Zulassung;
			abidaten.sprachendaten = new Sprachendaten();
			abidaten.sprachendaten.schuelerID = abidaten.schuelerID;
			result.add(abidaten);
			alleAbiturdaten.put(abidaten.schuelerID, abidaten);
		}

		for (final ABPSchuelerFaecher lupoSchuelerFach : schuelerFaecher) {
			final long schueler_id = lupoSchuelerFach.Schueler_ID;
			final Abiturdaten abidaten = alleAbiturdaten.get(schueler_id);
			if (abidaten == null)
				continue;

			final ABPFaecher lupoFach = faecher.get(lupoSchuelerFach.FachKrz);
			if (lupoFach == null)
				continue; // ignoriere Belegungen, wo das Fach nicht korrekt definiert ist

			final ZulaessigesFach zulFach = ZulaessigesFach.getByKuerzelASD(lupoFach.StatistikKrz);
			if (zulFach == null)
				continue; // ignoriere unzulässige Fächer

			final AbiturFachbelegung fachbelegung = new AbiturFachbelegung();
			fachbelegung.fachID = lupoSchuelerFach.Fach_ID;
			fachbelegung.abiturFach = lupoSchuelerFach.AbiturFach;
			fachbelegung.istFSNeu = lupoFach.AlsNeueFSInSII;
			GostKursart fachKursart = GostKursart.GK;
			if ("PX".equals(lupoFach.StatistikKrz))
				fachKursart = GostKursart.PJK;
			if ("VX".equals(lupoFach.StatistikKrz))
				fachKursart = GostKursart.VTF;
			if (lupoSchuelerFach.Kursart_E1 != null) {
				fachbelegung.belegungen[0] = new AbiturFachbelegungHalbjahr();
				fachbelegung.belegungen[0].halbjahrKuerzel = GostHalbjahr.EF1.kuerzel;
				setFachbelegung(fachbelegung.belegungen[0], lupoSchuelerFach.Kursart_E1, fachKursart, lupoFach.Q_WStd, false);
				fachbelegung.letzteKursart = fachbelegung.belegungen[0].kursartKuerzel;
				fachbelegung.belegungen[0].biliSprache = lupoFach.Unterichtssprache;
				fachbelegung.belegungen[0].notenkuerzel = getNotenkuerzelFromLupoNotenpunkte(lupoSchuelerFach.Punkte_E1);
			}
			if (lupoSchuelerFach.Kursart_E2 != null) {
				fachbelegung.belegungen[1] = new AbiturFachbelegungHalbjahr();
				fachbelegung.belegungen[1].halbjahrKuerzel = GostHalbjahr.EF2.kuerzel;
				setFachbelegung(fachbelegung.belegungen[1], lupoSchuelerFach.Kursart_E2, fachKursart, lupoFach.Q_WStd, false);
				fachbelegung.letzteKursart = fachbelegung.belegungen[1].kursartKuerzel;
				fachbelegung.belegungen[1].biliSprache = lupoFach.Unterichtssprache;
				fachbelegung.belegungen[1].notenkuerzel = getNotenkuerzelFromLupoNotenpunkte(lupoSchuelerFach.Punkte_E2);
			}
			if (lupoSchuelerFach.Kursart_Q1 != null) {
				fachbelegung.belegungen[2] = new AbiturFachbelegungHalbjahr();
				fachbelegung.belegungen[2].halbjahrKuerzel = GostHalbjahr.Q11.kuerzel;
				setFachbelegung(fachbelegung.belegungen[2], lupoSchuelerFach.Kursart_Q1, fachKursart, lupoFach.Q_WStd,
						"J".equals(lupoSchuelerFach.Markiert_Q1));
				fachbelegung.letzteKursart = fachbelegung.belegungen[2].kursartKuerzel;
				fachbelegung.belegungen[2].biliSprache = lupoFach.Unterichtssprache;
				fachbelegung.belegungen[2].notenkuerzel = getNotenkuerzelFromLupoNotenpunkte(lupoSchuelerFach.Punkte_Q1);
			}
			if (lupoSchuelerFach.Kursart_Q2 != null) {
				fachbelegung.belegungen[3] = new AbiturFachbelegungHalbjahr();
				fachbelegung.belegungen[3].halbjahrKuerzel = GostHalbjahr.Q12.kuerzel;
				setFachbelegung(fachbelegung.belegungen[3], lupoSchuelerFach.Kursart_Q2, fachKursart, lupoFach.Q_WStd,
						"J".equals(lupoSchuelerFach.Markiert_Q2));
				fachbelegung.letzteKursart = fachbelegung.belegungen[3].kursartKuerzel;
				fachbelegung.belegungen[3].biliSprache = lupoFach.Unterichtssprache;
				fachbelegung.belegungen[3].notenkuerzel = getNotenkuerzelFromLupoNotenpunkte(lupoSchuelerFach.Punkte_Q2);
			}
			if (lupoSchuelerFach.Kursart_Q3 != null) {
				fachbelegung.belegungen[4] = new AbiturFachbelegungHalbjahr();
				fachbelegung.belegungen[4].halbjahrKuerzel = GostHalbjahr.Q21.kuerzel;
				setFachbelegung(fachbelegung.belegungen[4], lupoSchuelerFach.Kursart_Q3, fachKursart, lupoFach.Q_WStd,
						"J".equals(lupoSchuelerFach.Markiert_Q3));
				fachbelegung.letzteKursart = fachbelegung.belegungen[4].kursartKuerzel;
				fachbelegung.belegungen[4].biliSprache = lupoFach.Unterichtssprache;
				fachbelegung.belegungen[4].notenkuerzel = getNotenkuerzelFromLupoNotenpunkte(lupoSchuelerFach.Punkte_Q3);
			}
			if (lupoSchuelerFach.Kursart_Q4 != null) {
				fachbelegung.belegungen[5] = new AbiturFachbelegungHalbjahr();
				fachbelegung.belegungen[5].halbjahrKuerzel = GostHalbjahr.Q22.kuerzel;
				setFachbelegung(fachbelegung.belegungen[5], lupoSchuelerFach.Kursart_Q4, fachKursart, lupoFach.Q_WStd,
						"J".equals(lupoSchuelerFach.Markiert_Q4));
				fachbelegung.letzteKursart = fachbelegung.belegungen[5].kursartKuerzel;
				fachbelegung.belegungen[5].biliSprache = lupoFach.Unterichtssprache;
				fachbelegung.belegungen[5].notenkuerzel = getNotenkuerzelFromLupoNotenpunkte(lupoSchuelerFach.Punkte_Q4);
			}
			if (fachKursart == GostKursart.PJK) {
				abidaten.projektkursLeitfach1Kuerzel = lupoFach.Leitfach;
				abidaten.projektkursLeitfach2Kuerzel = lupoFach.Leitfach2;
			}
			if ((lupoSchuelerFach.FS_BeginnJg != null) && (lupoSchuelerFach.Sprachenfolge != null) && zulFach.daten.istFremdsprache) {
				final Sprachbelegung sprachbelegung = new Sprachbelegung();
				sprachbelegung.sprache = zulFach.daten.kuerzel;
				try {
					sprachbelegung.reihenfolge = Integer.parseInt(lupoSchuelerFach.Sprachenfolge);
				} catch (@SuppressWarnings("unused") final NumberFormatException e) {
					sprachbelegung.reihenfolge = -1;
				}
				sprachbelegung.belegungVonJahrgang = lupoSchuelerFach.FS_BeginnJg;
				if (sprachbelegung.belegungVonJahrgang.length() == 1)
					sprachbelegung.belegungVonJahrgang = "0" + sprachbelegung.belegungVonJahrgang;
				if ("10".equals(sprachbelegung.belegungVonJahrgang))
					sprachbelegung.belegungVonJahrgang = "EF";
				sprachbelegung.belegungVonAbschnitt = 1;
				abidaten.sprachendaten.belegungen.add(sprachbelegung);
			}
			if ((lupoSchuelerFach.Kursart_E1 != null) || (lupoSchuelerFach.Kursart_E2 != null)
					|| (lupoSchuelerFach.Kursart_Q1 != null) || (lupoSchuelerFach.Kursart_Q2 != null)
					|| (lupoSchuelerFach.Kursart_Q3 != null) || (lupoSchuelerFach.Kursart_Q4 != null))
				abidaten.fachbelegungen.add(fachbelegung);
		}
		for (final ABPSchuelerSprachenfolge lupoSchuelerSprachenfolge : schuelerSprachenfolge) {
			final long schueler_id = lupoSchuelerSprachenfolge.Schueler_ID;
			final Abiturdaten abidaten = alleAbiturdaten.get(schueler_id);
			if (abidaten == null)
				continue;

			final ABPFaecher lupoFach = faecher.get(lupoSchuelerSprachenfolge.FachKrz);
			if (lupoFach == null)
				continue; // ignoriere Belegungen, wo das Fach nicht korrekt definiert ist

			final ZulaessigesFach zulFach = ZulaessigesFach.getByKuerzelASD(lupoFach.StatistikKrz);
			if (zulFach == null)
				continue; // ignoriere unzulässige Fächer

			if ((lupoSchuelerSprachenfolge.JahrgangVon != null) && (lupoSchuelerSprachenfolge.Reihenfolge != null) && zulFach.daten.istFremdsprache) {
				final Sprachbelegung sprachbelegung = new Sprachbelegung();
				sprachbelegung.sprache = zulFach.daten.kuerzel;
				try {
					sprachbelegung.reihenfolge = Integer.parseInt(lupoSchuelerSprachenfolge.Reihenfolge);
				} catch (@SuppressWarnings("unused") final NumberFormatException e) {
					sprachbelegung.reihenfolge = -1;
				}
				sprachbelegung.belegungVonJahrgang = "" + lupoSchuelerSprachenfolge.JahrgangVon;
				if (sprachbelegung.belegungVonJahrgang.length() == 1)
					sprachbelegung.belegungVonJahrgang = "0" + sprachbelegung.belegungVonJahrgang;
				if ("10".equals(sprachbelegung.belegungVonJahrgang))
					sprachbelegung.belegungVonJahrgang = "EF";
				sprachbelegung.belegungVonAbschnitt = lupoSchuelerSprachenfolge.AbschnittVon == null ? 1 : ((int) lupoSchuelerSprachenfolge.AbschnittVon);
				abidaten.sprachendaten.belegungen.add(sprachbelegung);
			}
		}
		return result;
	}


	private static String getNotenkuerzelFromLupoNotenpunkte(final String lupoNotenpunkte) {
		if (lupoNotenpunkte == null)
			return null;
		return switch (lupoNotenpunkte) {
			case "E1" -> Note.E1_MIT_BESONDEREM_ERFOLG_TEILGENOMMEN.kuerzel;
			case "E2" -> Note.E2_MIT_ERFOLG_TEILGENOMMEN.kuerzel;
			case "E3" -> Note.E3_TEILGENOMMEN.kuerzel;
			case "00", "0" -> Note.UNGENUEGEND.kuerzel;
			case "01", "1" -> Note.MANGELHAFT_MINUS.kuerzel;
			case "02", "2" -> Note.MANGELHAFT.kuerzel;
			case "03", "3" -> Note.MANGELHAFT_PLUS.kuerzel;
			case "04", "4" -> Note.AUSREICHEND_MINUS.kuerzel;
			case "05", "5" -> Note.AUSREICHEND.kuerzel;
			case "06", "6" -> Note.AUSREICHEND_PLUS.kuerzel;
			case "07", "7" -> Note.BEFRIEDIGEND_MINUS.kuerzel;
			case "08", "8" -> Note.BEFRIEDIGEND.kuerzel;
			case "09", "9" -> Note.BEFRIEDIGEND_PLUS.kuerzel;
			case "10" -> Note.GUT_MINUS.kuerzel;
			case "11" -> Note.GUT.kuerzel;
			case "12" -> Note.GUT_PLUS.kuerzel;
			case "13" -> Note.SEHR_GUT_MINUS.kuerzel;
			case "14" -> Note.SEHR_GUT.kuerzel;
			case "15" -> Note.SEHR_GUT_PLUS.kuerzel;
			default -> null;
		};
	}


	private static void setFachbelegung(final AbiturFachbelegungHalbjahr belegung, final String belegungPlanungKursart,
			final GostKursart fachKursart, final Integer wochenstunden, final boolean istInAbiwertung) {
		belegung.kursartKuerzel = fachKursart.toString();
		if ("AT".equals(belegungPlanungKursart))
			belegung.kursartKuerzel = "AT";
		else if ("LK".equals(belegungPlanungKursart))
			belegung.kursartKuerzel = "LK";
		else if ("ZK".equals(belegungPlanungKursart))
			belegung.kursartKuerzel = "ZK";
		belegung.schriftlich = belegungPlanungKursart == null ? null
				: "LK".equals(belegungPlanungKursart) || "S".equals(belegungPlanungKursart);
		if ("LK".equals(belegungPlanungKursart))
			belegung.wochenstunden = 5;
		else if (wochenstunden == null)
			belegung.wochenstunden = (fachKursart == GostKursart.VTF ? 2 : 3);
		else
			belegung.wochenstunden = wochenstunden;
		belegung.block1gewertet = istInAbiwertung;
	}

}
