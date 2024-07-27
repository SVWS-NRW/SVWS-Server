package de.svws_nrw.module.reporting.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.fach.FachDaten;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostJahrgangsdaten;
import de.svws_nrw.core.data.gost.GostLaufbahnplanungBeratungsdaten;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.core.data.klassen.KlassenDaten;
import de.svws_nrw.core.data.lehrer.LehrerStammdaten;
import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.core.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.data.schule.FoerderschwerpunktEintrag;
import de.svws_nrw.core.data.schule.ReligionEintrag;
import de.svws_nrw.core.data.schule.SchuleStammdaten;
import de.svws_nrw.core.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.stundenplan.Stundenplan;
import de.svws_nrw.core.data.stundenplan.StundenplanListeEintrag;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.data.faecher.DataFachdaten;
import de.svws_nrw.data.jahrgaenge.DataJahrgangsdaten;
import de.svws_nrw.data.kataloge.DataOrte;
import de.svws_nrw.data.kataloge.DataOrtsteile;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.data.lehrer.DataLehrerStammdaten;
import de.svws_nrw.data.schueler.DataKatalogSchuelerFoerderschwerpunkte;
import de.svws_nrw.data.schule.DataReligionen;
import de.svws_nrw.data.schule.DataSchuleStammdaten;
import de.svws_nrw.data.schule.DataSchuljahresabschnitte;
import de.svws_nrw.data.stundenplan.DataStundenplan;
import de.svws_nrw.data.stundenplan.DataStundenplanListe;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.proxytypes.fach.ProxyReportingFach;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import jakarta.ws.rs.core.Response.Status;

/**
 * <p>Dieses Repository enthält neben den Stammdaten der Schule einige Maps, in der zur jeweiligen ID bereits ausgelesene Stammdaten anderer
 * Objekte wie Kataloge, Jahrgänge, Klassen, Lehrer, Schüler usw. gespeichert werden. So sollen Datenbankzugriffe minimiert werden.</p>
 *
 * <p>Werden in anderen Klasse Daten nachgeladen, so werden diese in der Regel auch in der entsprechenden Map des Repository ergänzt.</p>
 *
 * <p>Des Weiteren kann diese Klasse genutzt werden, um die Maps bereits vor der Erstellung er eigentlichen Reporting-Objekte zu füllen,
 * beispielsweise mit Daten aus im Vorfeld durchgeführten Prüfungen bei API-Abfragen. So müssen diese Daten nicht erneut aus der Datenbank
 * geladen werden.</p>
 */
public class ReportingRepository {

	/** Die Verbindung zu Datenbank, um Daten abrufen zu können. */
	private final DBEntityManager conn;

	/** Die Reporting-Parameter, mit IDs und weiteren Informationen, die für den Druck verwendet werden sollen. */
	private final ReportingParameter reportingParameter;

	/** Liste, die Einträge aus dem Logger sammelt. */
	private final LogConsumerList log;

	/** Logger, der den Ablauf protokolliert und Fehlerdaten sammelt */
	private final Logger logger;


	/** Die Stammdaten der Schule zur Datenbankanbindung. */
	private final SchuleStammdaten schulstammdaten;

	/** Der aktuelle Schuljahresabschnitt der Schule aus der Datenbankverbindung */
	private final Schuljahresabschnitt aktuellerSchuljahresabschnitt;

	/** Der ausgewählte Schuljahresabschnitt, der für die Ausgabe der Reports ausgewählt wurde */
	private final Schuljahresabschnitt auswahlSchuljahresabschnitt;


	/** Stellt den Katalog der Förderschwerpunkte über eine Map zur Förderschwerpunkt-ID zur Verfügung */
	private Map<Long, FoerderschwerpunktEintrag> katalogFoerderschwerpunkte;

	/** Stellt den Katalog der Orte über eine Map zur Ort-ID zur Verfügung */
	private Map<Long, OrtKatalogEintrag> katalogOrte;

	/** Stellt den Katalog der Ortsteile über eine Map zur Ortsteil-ID zur Verfügung */
	private Map<Long, OrtsteilKatalogEintrag> katalogOrtsteile;

	/** Stellt die Religionen aus dem Katalog der Schule zur Religion-ID zur Verfügung */
	private Map<Long, ReligionEintrag> katalogReligionen;

	/** Stellt die Daten von bereits abgerufenen aktuellen Lernabschnitten zur Schüler-ID zur Verfügung. */
	private final Map<Long, SchuelerLernabschnittsdaten> mapAktuelleLernabschnittsdaten = new HashMap<>();

	/** Stellt die Daten von bereits abgerufenen ausgewählten Lernabschnitten zur Schüler-ID zur Verfügung. */
	private final Map<Long, SchuelerLernabschnittsdaten> mapAuswahlLernabschnittsdaten = new HashMap<>();

	/** Stellt die Daten der Abiturjahrgänge über eine Map zum Abiturjahr Verfügung. */
	private final Map<Integer, GostJahrgangsdaten> mapGostAbiturjahrgangDaten = new HashMap<>();

	/** Stellt die Fächer der Abiturjahrgänge über eine Map zum Abiturjahr Verfügung. */
	private final Map<Integer, GostFaecherManager> mapGostAbiturjahrgangFaecher = new HashMap<>();

	/** Stellt die Beratungsdaten zur GOSt von bereits abgerufenen Schülern über eine Map zur Schüler-ID zur Verfügung. */
	private final Map<Long, GostLaufbahnplanungBeratungsdaten> mapGostBeratungsdaten = new HashMap<>();

	/** Stellt die Abiturdaten in der GOSt von bereits abgerufenen Schülern über eine Map zur Schüler-ID zur Verfügung. */
	private final Map<Long, Abiturdaten> mapGostSchuelerAbiturdaten = new HashMap<>();

	/** Stellt die Stammdaten der Jahrgänge über eine Map zur Jahrgang-ID zur Verfügung */
	private Map<Long, JahrgangsDaten> mapJahrgaenge;

	/** Stellt die Stammdaten der Klassen über eine Map zur Klassen-ID zur Verfügung */
	private Map<Long, KlassenDaten> mapKlassen;

	/** Stellt die Stammdaten von bereits abgerufenen Lehrkräften über eine Map zur Lehrer-ID zur Verfügung */
	private Map<Long, LehrerStammdaten> mapLehrerStammdaten;

	/** Stellt die Stammdaten von bereits abgerufenen Schülern über eine Map zur Schüler-ID zur Verfügung */
	private final Map<Long, SchuelerStammdaten> mapSchuelerStammdaten = new HashMap<>();

	/** Stellt die Liste aller Schuljahresabschnitte über eine Map zur Schulabschnitts-ID zur Verfügung */
	private final Map<Long, Schuljahresabschnitt> mapSchuljahresabschnitte;

	/** Stelle eine Liste aller Stundenplandefinitionen der Schule zur Verfügung, sortiert nach Schuljahresabschnitt und Gültigkeitsbeginn. */
	private List<StundenplanListeEintrag> stundenplandefinitionen;

	/** Stelle die bereits eingelesenen Stundepläne al Map zu ihrer ID zur Verfügung. */
	private final Map<Long, Stundenplan> mapStundenplaene = new HashMap<>();


	/** Stellt alle Fächer der Schule als Reporting-Objekte zur Fach-ID zur Verfügung */
	private final Map<Long, ReportingFach> mapReportingFaecher = new HashMap<>();



	/**
	 * <p>Erstellt das Repository für häufig genutzte Daten aus der Schuldatenbank, um die Zugriffe darauf zu minimieren.
	 * Im Konstruktor werden folgende Elemente direkt mit Daten initialisiert:</p>
	 *
	 * <ul>
	 *     <li>Stammdaten der Schule</li>
	 *     <li>Schuljahresabschnitte und aktueller sowie zu druckender Schuljahresabschnitt</li>
	 *     <li>Kataloge: Förderschwerpunkte, Orte, Ortsteile</li>
	 *     <li>Maps: Jahrgänge, Klassen des aktuellen und zu druckenden Schuljahresabschnitts</li>
	 *     <li>Map: Lernabschnittsdaten der Schüler</li>
	 * </ul>
	 *
	 * @param conn					Die Verbindung zur Datenbank der Schule.
	 * @param reportingParameter 	Die Informationen, die für die Generierung eines Reports verwendet werden sollen.
	 * @param logger 				Logger, der die Erstellung der Reports protokolliert.
	 * @param log 					Log, das die Erstellung des Reports protokolliert.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public ReportingRepository(final DBEntityManager conn, final ReportingParameter reportingParameter, final Logger logger, final LogConsumerList log)
			throws ApiOperationException {

		// Initialisiere den Logger und das Log, sofern noch nicht erfolgt.
		if (logger == null)
			this.logger = new Logger();
		else
			this.logger = logger;

		if ((logger == null) || (log == null)) {
			this.log = new LogConsumerList();
			this.logger.addConsumer(this.log);
		} else
			this.log = log;

		this.logger.logLn(LogLevel.DEBUG, 4, ">>> Beginn der Erzeugung des Reporting-Repository");

		// Validiere Datenbankverbindung
		if (conn == null) {
			this.logger.logLn(LogLevel.ERROR, 8, "FEHLER: Es wurde keine Verbindung zur Datenbank für die Initialisierung des Reporting-Repository übergeben.");
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurde keine Verbindung zur Datenbank für die Initialisierung des Reporting-Repository übergeben.");
		}
		this.conn = conn;

		// Validiere Reporting-Parameter
		if (reportingParameter == null) {
			this.logger.logLn(LogLevel.ERROR, 8, "FEHLER: Es wurden keine Daten Ausgabe im Report für die Initialisierung des Reporting-Repository übergeben.");
			throw new ApiOperationException(Status.NOT_FOUND,
					"FEHLER: Es wurden keine Daten Ausgabe im Report für die Initialisierung des Reporting-Repository übergeben.");
		}
		this.reportingParameter = reportingParameter;

		// Ermittle die Daten der Schule. Wenn diese nicht gefunden wird oder sie keinen aktuellen Schuljahresabschnitt besitzt, dann wird ein Fehler geworfen.
		this.logger.logLn(LogLevel.DEBUG, 8, "Ermittle Stammdaten und Abschnitte der Schule.");
		try {
			this.schulstammdaten = DataSchuleStammdaten.getStammdaten(this.conn);

			// Ermittle grundlegende Daten zur Schule
			mapSchuljahresabschnitte = new DataSchuljahresabschnitte(this.conn).getAbschnitte().stream().collect(Collectors.toMap(a -> a.id, a -> a));
			aktuellerSchuljahresabschnitt = this.mapSchuljahresabschnitte.values().stream().filter(a -> a.id == this.schulstammdaten.idSchuljahresabschnitt)
					.toList().getFirst();

			if ((this.reportingParameter.idSchuljahresabschnitt == this.aktuellerSchuljahresabschnitt.id)
					|| (this.reportingParameter.idSchuljahresabschnitt < 0)
					|| this.mapSchuljahresabschnitte.values().stream().filter(a -> a.id == this.reportingParameter.idSchuljahresabschnitt).toList().isEmpty())
				auswahlSchuljahresabschnitt = aktuellerSchuljahresabschnitt;
			else
				auswahlSchuljahresabschnitt =
						this.mapSchuljahresabschnitte.values().stream().filter(a -> a.id == this.reportingParameter.idSchuljahresabschnitt)
								.toList().getFirst();
		} catch (final Exception e) {
			this.logger.logLn(LogLevel.ERROR, 8, "FEHLER: Die Stammdaten der Schule oder ihre Abschnittsdaten konnten nicht ermittelt werden.");
			throw new ApiOperationException(Status.NOT_FOUND,
					"FEHLER: Die Stammdaten der Schule oder ihre Abschnittsdaten konnten nicht ermittelt werden.");
		}

		// Ermittle grundlegende Kataloge.
		initKataloge();

		// Ermittle die Stammdaten der Lehrer
		initLehrerStammdaten();

		// Ermittle die Klassen- und Jahrgangsdaten und Stammdaten
		initJahrgaengeKlassen();

		// Ermittle Fächerdaten und schreibe sie in die zentrale Map.
		initFachdaten();

		// Ermittle die Stundenpläne zum aktuellen und zum ausgewählten Schuljahresabschnitt.
		initStundeplaene();

		this.logger.logLn(LogLevel.DEBUG, 4, "<<< Ende der Erzeugung des Reporting-Repository");
	}


	/**
	 * Initialisiert die Stammdaten der Lehrer.
	 * @throws ApiOperationException	Im Fehlerfall
	 */
	private void initLehrerStammdaten() throws ApiOperationException {
		try {
			this.logger.logLn(LogLevel.DEBUG, 8, "Ermittle die Lehrerstammdaten.");
			this.mapLehrerStammdaten = new DataLehrerStammdaten(this.conn).getSichtbareLehrerStammdaten(this.conn).stream()
					.collect(Collectors.toMap(l -> l.id, l -> l));
		} catch (final Exception e) {
			this.mapLehrerStammdaten = new HashMap<>();
			this.logger.logLn(LogLevel.ERROR, 4, "FEHLER: Die Lehrerstammdaten konnten nicht ermittelt werden.");
			throw new ApiOperationException(Status.NOT_FOUND,
					"FEHLER: Die Lehrerstammdaten konnten nicht ermittelt werden.");
		}
	}

	/**
	 * Initialisiert die Daten der Jahrgänge und Klassen.
	 * @throws ApiOperationException	Im Fehlerfall
	 */
	private void initJahrgaengeKlassen() throws ApiOperationException {
		try {
			this.logger.logLn(LogLevel.DEBUG, 8, "Ermittle die Jahrgangsdaten.");
			this.mapJahrgaenge = new DataJahrgangsdaten(this.conn).getJahrgaenge().stream().collect(Collectors.toMap(j -> j.id, j -> j));
		} catch (final Exception e) {
			this.logger.logLn(LogLevel.ERROR, 4, "FEHLER: Die Jahrgangsdaten konnten nicht ermittelt werden.");
			throw new ApiOperationException(Status.NOT_FOUND,
					"FEHLER: Die Jahrgangsdaten. konnten nicht ermittelt werden.");
		}
		try {
			this.logger.logLn(LogLevel.DEBUG, 8, "Ermittle die Klassendaten.");
			this.mapKlassen = new DataKlassendaten(this.conn).getListBySchuljahresabschnittID(aktuellerSchuljahresabschnitt.id, false).stream()
					.collect(Collectors.toMap(k -> k.id, k -> k));
			if (auswahlSchuljahresabschnitt.id != aktuellerSchuljahresabschnitt.id)
				this.mapKlassen.putAll(new DataKlassendaten(this.conn).getListBySchuljahresabschnittID(auswahlSchuljahresabschnitt.id, false).stream()
						.collect(Collectors.toMap(k -> k.id, k -> k)));
		} catch (final Exception e) {
			this.logger.logLn(LogLevel.ERROR, 4, "FEHLER: Die Klassendaten konnten nicht ermittelt werden.");
			throw new ApiOperationException(Status.NOT_FOUND,
					"FEHLER: Die Klassendaten. konnten nicht ermittelt werden.");
		}
	}

	/**
	 * Initialisiert die Daten der Kataloge.
	 * @throws ApiOperationException	Im Fehlerfall
	 */
	private void initKataloge() throws ApiOperationException {
		try {
			this.logger.logLn(LogLevel.DEBUG, 8, "Ermittle Katalogdaten.");
			this.katalogFoerderschwerpunkte =
					new DataKatalogSchuelerFoerderschwerpunkte(this.conn).getAllFromDB().stream().collect(Collectors.toMap(f -> f.id, f -> f));
			this.katalogOrte = new DataOrte(this.conn).getOrte().stream().collect(Collectors.toMap(o -> o.id, o -> o));
			this.katalogOrtsteile = new DataOrtsteile(this.conn).getOrtsteile().stream().collect(Collectors.toMap(o -> o.id, o -> o));
			this.katalogReligionen = new DataReligionen(this.conn).getListReligionen().stream().collect(Collectors.toMap(r -> r.id, r -> r));
		} catch (final Exception e) {
			this.logger.logLn(LogLevel.ERROR, 8, "FEHLER: Die Kataloge der Schule konnten nicht ermittelt werden.");
			throw new ApiOperationException(Status.NOT_FOUND,
					"FEHLER: Die Kataloge der Schule konnten nicht ermittelt werden.");
		}
	}

	/**
	 * Initialisiert die Daten der Fächer.
	 * @throws ApiOperationException	Im Fehlerfall
	 */
	private void initFachdaten() throws ApiOperationException {
		try {
			this.logger.logLn(LogLevel.DEBUG, 8, "Ermittle Fächerdaten.");
			final Map<Long, FachDaten> mapFaecherDaten = new DataFachdaten(this.conn).getFaecherdaten();
			final Map<Long, GostFach> mapFaecherGostDaten = new DataFachdaten(this.conn).getFaecherGostdaten();
			for (final FachDaten fach : mapFaecherDaten.values()) {
				mapReportingFaecher.put(fach.id, new ProxyReportingFach(this, fach, mapFaecherGostDaten.get(fach.id)));
			}
		} catch (final Exception e) {
			this.logger.logLn(LogLevel.ERROR, 8, "FEHLER: Die Daten der Fächer konnten nicht ermittelt werden.");
			throw new ApiOperationException(Status.NOT_FOUND,
					"FEHLER: Die Daten der Fächer konnten nicht ermittelt werden.");
		}
	}

	/**
	 * Initialisiert die Daten der Stundenpläne.
	 * @throws ApiOperationException	Im Fehlerfall
	 */
	private void initStundeplaene() throws ApiOperationException {
		try {
			this.logger.logLn(LogLevel.DEBUG, 8, "Ermittle alle Stundenplandefintionen der Schule.");
			// Ermittle alle Stundenpläne zum aktuellen Schuljahresabschnitt.
			this.stundenplandefinitionen = DataStundenplanListe.getStundenplaene(this.conn, null);
			this.logger.logLn(LogLevel.DEBUG, 8, "Ermittle die Stundenpläne für den aktuellen und ausgewählten Schuljahresabschnitt.");
			final List<StundenplanListeEintrag> filterStundenplandefinitionen =
					stundenplandefinitionen.stream().filter(d -> ((d.idSchuljahresabschnitt == aktuellerSchuljahresabschnitt.id)
							|| (d.idSchuljahresabschnitt == auswahlSchuljahresabschnitt.id))).toList();
			if (!filterStundenplandefinitionen.isEmpty()) {
				for (final StundenplanListeEintrag stundenplandefinition : filterStundenplandefinitionen) {
					final Stundenplan stundenplan = DataStundenplan.getStundenplan(this.conn, stundenplandefinition.id);
					if (stundenplan != null)
						this.mapStundenplaene.put(stundenplandefinition.id, stundenplan);
				}
			}
		} catch (final Exception e) {
			this.logger.logLn(LogLevel.ERROR, 8, "Die Daten der Stundenpläne konnten nicht ermittelt werden.");
			throw new ApiOperationException(Status.NOT_FOUND,
					"FEHLER: Die Daten der Stundenpläne konnten nicht ermittelt werden.");
		}
	}


	/**
	 * Gibt die Datenbankverbindung zurück
	 * @return Datenbankverbindung
	 */
	public DBEntityManager conn() {
		return conn;
	}

	/**
	 * Die Reporting-Parameter, mit IDs und weiteren Informationen, die für den Druck verwendet werden sollen.
	 * @return Die übergebenen Reporting-Parameter
	 * */
	public ReportingParameter reportingParameter() {
		return reportingParameter;
	}

	/**
	 * Logger, der die Erstellung der Reports protokolliert.
	 * @return Der Logger zum Sammeln der Fehler
	 */
	public Logger logger() {
		return logger;
	}

	/**
	 * Log, das die Erstellung des Reports protokolliert.
	 * @return Das Log, in dem Fehlerinformationen gesammelt werden.
	 * */
	public LogConsumerList log() {
		return log;
	}


	/**
	 * Stellt die Stammdaten der Schule aus der Datenbankverbindung zur Verfügung
	 * @return Stammdaten der Schule
	 */
	public SchuleStammdaten schulstammdaten() {
		return schulstammdaten;
	}


	/**
	 * Stellt den aktuellen Schuljahresabschnitt der Schule aus der Datenbankverbindung zur Verfügung
	 * @return Aktueller Schuljahresabschnitt der Schule
	 */
	public Schuljahresabschnitt aktuellerSchuljahresabschnitt() {
		return aktuellerSchuljahresabschnitt;
	}

	/**
	 * Der ausgewählte Schuljahresabschnitt, der für die Ausgabe der Reports ausgewählt wurde
	 * @return Schuljahresabschnitt der Auswahl für den Druck
	 */
	public Schuljahresabschnitt auswahlSchuljahresabschnitt() {
		return auswahlSchuljahresabschnitt;
	}



	/**
	 * Stellt die eine Map der Förderschwerpunkt-Katalog-Einträge der Schule aus der Datenbankverbindung zu deren IDs zur Verfügung
	 * @return Map der Förderschwerpunkt-Katalog-Einträge
	 */
	public Map<Long, FoerderschwerpunktEintrag> katalogFoerderschwerpunkte() {
		return katalogFoerderschwerpunkte;
	}

	/**
	 * Stellt die eine Map der Ort-Katalog-Einträge der Schule aus der Datenbankverbindung zu deren IDs zur Verfügung
	 * @return Map der Ort-Katalog-Einträge
	 */
	public Map<Long, OrtKatalogEintrag> katalogOrte() {
		return katalogOrte;
	}

	/**
	 * Stellt die eine Map der Ortsteil-Katalog-Einträge der Schule aus der Datenbankverbindung zu deren IDs zur Verfügung
	 * @return Map der Ortsteil-Katalog-Einträge
	 */
	public Map<Long, OrtsteilKatalogEintrag> katalogOrtsteile() {
		return katalogOrtsteile;
	}

	/**
	 * Stellt die Religionen aus dem Katalog der Schule zur Religion-ID zur Verfügung
	 * @return Map der Religionen-Katalog-Einträge
	 */
	public Map<Long, ReligionEintrag> katalogReligionen() {
		return katalogReligionen;
	}


	/**
	 * Stellt die Daten von bereits abgerufenen aktuellen Lernabschnitten zur Schüler-ID zur Verfügung.
	 * @return Map der Daten von bereits abgerufenen aktuellen Lernabschnitten.
	 */
	public Map<Long, SchuelerLernabschnittsdaten> mapAktuelleLernabschnittsdaten() {
		return mapAktuelleLernabschnittsdaten;
	}

	/**
	 * Stellt die Daten von bereits abgerufenen ausgewählten Lernabschnitten zur Schüler-ID zur Verfügung.
	 * @return Map der Daten von bereits abgerufenen ausgewählten Lernabschnitten.
	 */
	public Map<Long, SchuelerLernabschnittsdaten> mapAuswahlLernabschnittsdaten() {
		return mapAuswahlLernabschnittsdaten;
	}

	/**
	 * Stellt die Daten der Abiturjahrgänge über eine Map zum Abiturjahr Verfügung.
	 * @return Map der Daten zu den Abiturjahrgängen
	 */
	public Map<Integer, GostJahrgangsdaten> mapGostAbiturjahrgangDaten() {
		return mapGostAbiturjahrgangDaten;
	}

	/**
	 * Stellt die Fächer der Abiturjahrgänge über eine Map zum Abiturjahr Verfügung.
	 * @return Map der Fächer zu den Abiturjahrgängen
	 */
	public Map<Integer, GostFaecherManager> mapGostAbiturjahrgangFaecher() {
		return mapGostAbiturjahrgangFaecher;
	}

	/**
	 * Stellt die Beratungsdaten zur GOSt von bereits abgerufenen Schülern über eine Map zur Schüler-ID zur Verfügung.
	 * @return Map mit GOSt-Beratungsdaten der Schüler
	 */
	public Map<Long, GostLaufbahnplanungBeratungsdaten> mapGostBeratungsdaten() {
		return mapGostBeratungsdaten;
	}

	/**
	 * Stellt die Abiturdaten in der GOSt von bereits abgerufenen Schülern über eine Map zur Schüler-ID zur Verfügung.
	 * @return Map mit GOSt-Abiturdaten der Schüler
	 */
	public Map<Long, Abiturdaten> mapGostSchuelerAbiturdaten() {
		return mapGostSchuelerAbiturdaten;
	}

	/**
	 * Stellt die Stammdaten der Jahrgänge über eine Map zur Jahrgangs-ID zur Verfügung
	 * @return Map der Stammdaten der Jahrgänge.
	 */
	public Map<Long, JahrgangsDaten> mapJahrgaenge() {
		return mapJahrgaenge;
	}

	/**
	 * Stellt die Stammdaten der Klassen über eine Map zur Klassen-ID zur Verfügung
	 * @return Map der Stammdaten der Klassen.
	 */
	public Map<Long, KlassenDaten> mapKlassen() {
		return mapKlassen;
	}

	/**
	 * Stellt die Stammdaten von bereits abgerufenen Lehrkräften über eine Map zur Lehrer-ID zur Verfügung.
	 * @return Map der Stammdaten von bereits abgerufenen Lehrkräften
	 */
	public Map<Long, LehrerStammdaten> mapLehrerStammdaten() {
		return mapLehrerStammdaten;
	}

	/**
	 * Stellt die Stammdaten von bereits abgerufenen Schülern über eine Map zur Schüler-ID zur Verfügung
	 * @return Map der Stammdaten von bereits abgerufenen Schülern
	 */
	public Map<Long, SchuelerStammdaten> mapSchuelerStammdaten() {
		return mapSchuelerStammdaten;
	}

	/**
	 * Stellt die Map der Schuljahresabschnitte der Schule aus der Datenbankverbindung zu deren IDs zur Verfügung
	 * @return Map der Schuljahresabschnitte
	 */
	public Map<Long, Schuljahresabschnitt> mapSchuljahresabschnitte() {
		return mapSchuljahresabschnitte;
	}

	/**
	 * Stelle eine Liste aller Stundenplandefinitionen der Schule zur Verfügung, sortiert nach Schuljahresabschnitt und Gültigkeitsbeginn.
	 * @return Inhalt des Feldes stundenplandefinitionen
	 */
	public List<StundenplanListeEintrag> stundenplandefinitionen() {
		return stundenplandefinitionen;
	}

	/**
	 * Stelle die bereits eingelesenen Stundepläne als Map zu ihrer ID zur Verfügung.
	 * @return Inhalt des Feldes mapStundenplaene
	 */
	public Map<Long, Stundenplan> mapStundenplaene() {
		return mapStundenplaene;
	}

	/**
	 * Ermittelt den zum übergebenen Datum gehörigen Stundenplan und gibt ihn zurück.
	 * @param datum Das Datum im Format yyyy-mm-dd, dessen Stundenplan bestimmt werden soll.
	 * @return Der Stundenplan zum Datum, wenn er gefunden wird, sonst null.
	 */
	public Stundenplan stundenplan(final String datum) {
		if ((datum == null) || (datum.length() != 10) || (stundenplandefinitionen == null) || (stundenplandefinitionen.isEmpty()))
			return null;
		final StundenplanListeEintrag stundenplandefinitionZuDatum = stundenplandefinitionen.stream()
				.filter(d -> ((d.gueltigAb != null) && (d.gueltigBis != null) && (datum.compareTo(d.gueltigAb) >= 0) && (datum.compareTo(d.gueltigBis) <= 0)))
				.findFirst().orElse(null);
		if (stundenplandefinitionZuDatum != null) {
			if (mapStundenplaene.containsKey(stundenplandefinitionZuDatum.id))
				return mapStundenplaene.get(stundenplandefinitionZuDatum.id);
			else {
				try {
					final Stundenplan stundenplan = DataStundenplan.getStundenplan(this.conn, stundenplandefinitionZuDatum.id);
					if (stundenplan != null) {
						this.mapStundenplaene.put(stundenplandefinitionZuDatum.id, stundenplan);
						return mapStundenplaene.get(stundenplandefinitionZuDatum.id);
					}
				} catch (final Exception ignore) {
					// Stundenplan konnte nicht aus der Datenbak ermittelt werden. Sollte nicht eintreten, wenn Definition vorhanden ist.
				}
			}
		}
		// Kein Plan gefunden oder Fehler.
		return null;
	}


	/**
	 * Stellt alle Fächer der Schule als Reporting-Objekte zur Fach-ID zur Verfügung
	 * @return Map der Reporting-Fächer
	 */
	public Map<Long, ReportingFach> mapReportingFaecher() {
		return mapReportingFaecher;
	}

}
