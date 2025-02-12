package de.svws_nrw.module.reporting.repositories;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.adt.map.ListMap3DLongKeys;
import de.svws_nrw.core.data.erzieher.ErzieherStammdaten;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostJahrgangsdaten;
import de.svws_nrw.core.data.gost.GostLaufbahnplanungBeratungsdaten;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.data.schueler.SchuelerLeistungsdaten;
import de.svws_nrw.core.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.data.schueler.Sprachbelegung;
import de.svws_nrw.core.data.schule.FoerderschwerpunktEintrag;
import de.svws_nrw.core.data.schule.ReligionEintrag;
import de.svws_nrw.core.data.stundenplan.Stundenplan;
import de.svws_nrw.core.data.stundenplan.StundenplanListeEintrag;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.data.erzieher.DataErzieherarten;
import de.svws_nrw.data.jahrgaenge.DataJahrgangsdaten;
import de.svws_nrw.data.kataloge.DataOrte;
import de.svws_nrw.data.kataloge.DataOrtsteile;
import de.svws_nrw.data.lehrer.DataLehrerStammdaten;
import de.svws_nrw.data.schueler.DataKatalogSchuelerFoerderschwerpunkte;
import de.svws_nrw.data.schule.DataReligionen;
import de.svws_nrw.data.schule.DataSchuleStammdaten;
import de.svws_nrw.data.stundenplan.DataStundenplan;
import de.svws_nrw.data.stundenplan.DataStundenplanListe;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.proxytypes.schueler.erzieher.ProxyReportingErzieherArt;
import de.svws_nrw.module.reporting.proxytypes.schule.ProxyReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.types.klasse.ReportingKlasse;
import de.svws_nrw.module.reporting.types.kurs.ReportingKurs;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.erzieher.ReportingErzieherArt;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import jakarta.ws.rs.core.Response.Status;

/**
 * <p>Dieses Repository enthält neben den Stammdaten der Schule Maps, in denen Objekte wie Kataloge, Jahrgänge, Klassen, Lehrer, Schüler usw. gespeichert
 * werden. So sollen Datenbankzugriffe minimiert werden.</p>
 * <p>Des Weiteren kann diese Klasse genutzt werden, um die Maps bereits vor der Erstellung er eigentlichen Reporting-Objekte zu füllen,
 * beispielsweise mit Daten aus im Vorfeld durchgeführten Prüfungen bei API-Abfragen. So müssen diese Daten nicht erneut aus der Datenbank
 * geladen werden.</p>
 * <p>Werden in anderen Klasse Daten nachgeladen, so werden diese in der Regel auch in der entsprechenden Map des Repository ergänzt.</p>
 * <p>Ebenso werden einige bereits erzeugte Reporting-Objekt hier zwischengespeichert.</p>
 */
public class ReportingRepository {

	/** Die Verbindung zu Datenbank. */
	private final DBEntityManager conn;

	/** Einstellungen und Daten zum Steuern der Report-Generierung. */
	private final ReportingParameter reportingParameter;

	/** Logger, der den Ablauf protokolliert und Fehlerdaten sammelt. */
	private final Logger logger;

	/** Liste, die Einträge aus dem Logger sammelt. */
	private final LogConsumerList log;

	/** Die ID des aktuellen Schuljahresabschnitts der Schule. */
	private final Long idAktuellerSchuljahresbaschnitt;

	/** Die ID des in den ReportingParamtern ausgewählten Schuljahresabschnitts. */
	private final Long idAuswahlSchuljahresbaschnitt;


	// #########  Ab hier folgen Objekte aus dem Core. #########

	/** Die Stammdaten der Schule zur Datenbankanbindung. */
	private final SchuleStammdaten schulstammdaten;

	/** Stellt den Katalog der Förderschwerpunkte über eine Map zur Förderschwerpunkt-ID zur Verfügung */
	private Map<Long, FoerderschwerpunktEintrag> katalogFoerderschwerpunkte;

	/** Stellt den Katalog der Orte über eine Map zur Ort-ID zur Verfügung */
	private Map<Long, OrtKatalogEintrag> katalogOrte;

	/** Stellt den Katalog der Ortsteile über eine Map zur Ortsteil-ID zur Verfügung */
	private Map<Long, OrtsteilKatalogEintrag> katalogOrtsteile;

	/** Stellt die Religionen aus dem Katalog der Schule zur Religion-ID zur Verfügung */
	private Map<Long, ReligionEintrag> katalogReligionen;

	/** Stellt die Daten aller bereits abgerufenen Leistungsdaten zur Schüler-, Lernabschnitts- und Leistungsdaten-ID zur Verfügung. */
	private final ListMap3DLongKeys<SchuelerLeistungsdaten> mapAlleLeistungsdaten = new ListMap3DLongKeys<>();

	/** Stellt die Daten aller bereits abgerufenen Leistungsdaten zur Schüler-, Schuljahresabschnitt- und Lernabschnitts-ID zur Verfügung. */
	private final ListMap3DLongKeys<SchuelerLernabschnittsdaten> mapAlleLernabschnittsdaten = new ListMap3DLongKeys<>();

	/** Stellt die Stammdaten von bereits abgerufenen Erziehern über eine Map zur Schueler-ID zur Verfügung. */
	private final Map<Long, List<ErzieherStammdaten>> mapErzieherStammdaten = new HashMap<>();

	/** Stellt alle Fächer der Schule als DTOs zur Fach-ID zur Verfügung. Die Reporting-Fächer -Objekte sind in den Schuljahresabschnitten abrufbar. */
	private final Map<Long, DTOFach> mapFaecher = new HashMap<>();

	/** Stellt die Daten der Abiturjahrgänge über eine Map zum Abiturjahr Verfügung. */
	private final Map<Integer, GostJahrgangsdaten> mapGostAbiturjahrgangDaten = new HashMap<>();

	/** Stellt die Fächer der Abiturjahrgänge über eine Map zum Abiturjahr Verfügung. */
	private final Map<Integer, GostFaecherManager> mapGostAbiturjahrgangFaecher = new HashMap<>();

	/** Stellt die Beratungsdaten zur GOSt von bereits abgerufenen Schülern über eine Map zur Schüler-ID zur Verfügung. */
	private final Map<Long, GostLaufbahnplanungBeratungsdaten> mapGostBeratungsdaten = new HashMap<>();

	/** Stellt die für die Beratungsdaten zur GOSt relevanten Abiturdaten von bereits abgerufenen Schülern über eine Map zur Schüler-ID zur Verfügung. */
	private final Map<Long, Abiturdaten> mapGostBeratungsdatenAbiturdaten = new HashMap<>();

	/** Stellt die Abiturdaten in der GOSt von bereits abgerufenen Schülern über eine Map zur Schüler-ID zur Verfügung. */
	private final Map<Long, Abiturdaten> mapGostSchuelerAbiturdaten = new HashMap<>();

	/** Stellt die Stammdaten der Jahrgänge über eine Map zur Jahrgang-ID zur Verfügung */
	private Map<Long, JahrgangsDaten> mapJahrgaenge;

	/** Stellt die Stammdaten von bereits abgerufenen Lehrkräften über eine Map zur Lehrer-ID zur Verfügung */
	private Map<Long, LehrerStammdaten> mapLehrerStammdaten;

	/** Stellt die Sprachbelegungen von bereits abgerufenen Schülern über eine Map zur Schüler-ID zur Verfügung */
	private final Map<Long, List<Sprachbelegung>> mapSchuelerSprachbelegungen = new HashMap<>();

	/** Stellt die Stammdaten von bereits abgerufenen Schülern über eine Map zur Schüler-ID zur Verfügung */
	private final Map<Long, SchuelerStammdaten> mapSchuelerStammdaten = new HashMap<>();

	/** Stelle eine Liste aller Stundenplandefinitionen der Schule zur Verfügung, sortiert nach Schuljahresabschnitt und Gültigkeitsbeginn. */
	private List<StundenplanListeEintrag> stundenplandefinitionen;

	/** Stelle die bereits eingelesenen Stundenpläne als Map zu ihrer ID zur Verfügung. */
	private final Map<Long, Stundenplan> mapStundenplaene = new HashMap<>();


	// #########  Ab hier folgen Reporting-Objekte. #########

	/** Stellt alle Erzieherarten über eine Map zur Erzieherart-ID zur Verfügung */
	private Map<Long, ReportingErzieherArt> mapReportingErzieherarten;

	/** Stellt alle Klassen in den Schuljahresabschnitten über eine Map zur Klassen-ID zur Verfügung. */
	private final Map<Long, ReportingKlasse> mapKlassen = new HashMap<>();

	/** Stellt alle Kurse in den Schuljahresabschnitten über eine Map zur Kurs-ID zur Verfügung. */
	private final Map<Long, ReportingKurs> mapKurse = new HashMap<>();

	/** Stellt die Liste aller Schüler über eine Map zur Schüler-ID zur Verfügung */
	private final Map<Long, ReportingSchueler> mapSchueler = new HashMap<>();

	/** Stellt die Liste aller Schuljahresabschnitte über eine Map zur Schulabschnitts-ID zur Verfügung */
	private final Map<Long, ReportingSchuljahresabschnitt> mapSchuljahresabschnitte = new HashMap<>();


	/**
	 * Erstellt das Repository für häufig genutzte Daten aus der Schuldatenbank, um die Zugriffe darauf zu minimieren. Ebenso werden einzelne
	 * Reporting-Objekte hier zwischengespeichert.
	 *
	 * @param conn						Die Verbindung zur Datenbank.
	 * @param reportingParameter 		Einstellungen und Daten zum Steuern der Report-Generierung.
	 * @param logger 					Logger, der den Ablauf protokolliert und Fehlerdaten sammelt.
	 * @param log 						Liste, die Einträge aus dem Logger sammelt.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
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

			// Ermittle die Schuljahresabschnitte der Schule und erstelle damit eine Map der ReportingSchuljahresabschnitte
			final List<Schuljahresabschnitt> datenSchuljahresabschnitte = this.conn.getUser().schuleGetStammdaten().abschnitte;
			for (final Schuljahresabschnitt datenSchuljahresabschnitt : datenSchuljahresabschnitte) {
				mapSchuljahresabschnitte.putIfAbsent(datenSchuljahresabschnitt.id,
						new ProxyReportingSchuljahresabschnitt(this, datenSchuljahresabschnitt));
			}

			idAktuellerSchuljahresbaschnitt = this.schulstammdaten.idSchuljahresabschnitt;
			idAuswahlSchuljahresbaschnitt = this.reportingParameter.idSchuljahresabschnitt;
		} catch (final Exception e) {
			ReportingExceptionUtils.putStacktraceInLog(
					"FEHLER: Die Stamm- oder Abschnittsdaten der Schule konnten nicht ermittelt werden oder der Schuljahresabschnitt ist ungültig.",
					e, this.logger(), LogLevel.ERROR, 8);
			throw new ApiOperationException(Status.NOT_FOUND,
					"FEHLER: Die Stamm- oder Abschnittsdaten der Schule konnten nicht ermittelt werden oder der übergebene Schuljahresabschnitt ist ungültig.");
		}

		// Ermittle grundlegende Kataloge.
		initKataloge();

		// Ermittle die Fächerdaten und schreibe sie in die zentrale Map. Diese dient als Basis für die Fachlisten der Schuljahresabschnitte.
		initFachdaten();

		// Ermittle die Stammdaten der Lehrer
		initLehrerStammdaten();

		// Ermittle die Jahrgangsdaten und schreibe sie in die zentrale Map. Diese dient als Basis für die Jahrgangslisten der Schuljahresabschnitte.
		initJahrgaenge();

		// Ermittle die Stundenpläne zum aktuellen und zum ausgewählten Schuljahresabschnitt.
		initStundenplaene();

		this.logger.logLn(LogLevel.DEBUG, 4, "<<< Ende der Erzeugung des Reporting-Repository");
	}


	/**
	 * Initialisiert die Daten der Kataloge.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private void initKataloge() throws ApiOperationException {
		try {
			this.logger.logLn(LogLevel.DEBUG, 8, "Ermittle Katalogdaten.");

			this.katalogFoerderschwerpunkte =
					new DataKatalogSchuelerFoerderschwerpunkte(this.conn).getAllFromDB().stream().collect(Collectors.toMap(f -> f.id, f -> f));
			this.logger.logLn(LogLevel.DEBUG, 8, "Katalog Förderschwerpunkte geladen.");

			this.katalogOrte = new DataOrte(this.conn).getOrte().stream().collect(Collectors.toMap(o -> o.id, o -> o));
			this.logger.logLn(LogLevel.DEBUG, 8, "Katalog Orte geladen.");

			this.katalogOrtsteile = new DataOrtsteile(this.conn).getOrtsteile().stream().collect(Collectors.toMap(o -> o.id, o -> o));
			this.logger.logLn(LogLevel.DEBUG, 8, "Katalog Ortsteile geladen.");

			this.katalogReligionen = new DataReligionen(this.conn).getAll().stream().collect(Collectors.toMap(r -> r.id, r -> r));
			this.logger.logLn(LogLevel.DEBUG, 8, "Katalog Religionen geladen.");

			this.mapReportingErzieherarten = new DataErzieherarten(this.conn).getListErzieherarten().stream().collect(Collectors.toMap(a -> a.id,
					a -> new ProxyReportingErzieherArt(this, a)));
			this.logger.logLn(LogLevel.DEBUG, 8, "Liste der Erzieherarten geladen.");
		} catch (final Exception e) {
			this.logger.logLn(LogLevel.ERROR, 8, "FEHLER: Die Kataloge der Schule konnten nicht vollständig ermittelt werden.");
			throw new ApiOperationException(Status.NOT_FOUND, e,
					"FEHLER: Die Kataloge der Schule konnten nicht vollständig ermittelt werden.");
		}
	}

	/**
	 * Initialisiert die Daten der Fächer.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private void initFachdaten() throws ApiOperationException {
		try {
			this.logger.logLn(LogLevel.DEBUG, 8, "Ermittle Fächer.");
			mapFaecher.putAll(conn.queryAll(DTOFach.class).stream().collect(Collectors.toMap(f -> f.ID, f -> f)));
		} catch (final Exception e) {
			this.logger.logLn(LogLevel.ERROR, 8, "FEHLER: Die Fächer konnten nicht ermittelt werden.");
			throw new ApiOperationException(Status.NOT_FOUND, e,
					"FEHLER: Die Daten der Fächer konnten nicht ermittelt werden.");
		}
	}

	/**
	 * Initialisiert die Stammdaten der Lehrer.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private void initLehrerStammdaten() throws ApiOperationException {
		try {
			this.logger.logLn(LogLevel.DEBUG, 8, "Ermittle die Lehrerstammdaten.");
			this.mapLehrerStammdaten = new DataLehrerStammdaten(this.conn).getAll().stream()
					.collect(Collectors.toMap(l -> l.id, l -> l));
		} catch (final Exception e) {
			this.mapLehrerStammdaten = new HashMap<>();
			this.logger.logLn(LogLevel.ERROR, 4, "FEHLER: Die Lehrerstammdaten konnten nicht ermittelt werden.");
			throw new ApiOperationException(Status.NOT_FOUND, e,
					"FEHLER: Die Lehrerstammdaten konnten nicht ermittelt werden.");
		}
	}

	/**
	 * Initialisiert die Daten der Jahrgänge.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private void initJahrgaenge() throws ApiOperationException {
		try {
			this.logger.logLn(LogLevel.DEBUG, 8, "Ermittle die Jahrgangsdaten.");
			this.mapJahrgaenge = new DataJahrgangsdaten(this.conn).getAll().stream().collect(Collectors.toMap(j -> j.id, j -> j));
		} catch (final Exception e) {
			this.logger.logLn(LogLevel.ERROR, 4, "FEHLER: Die Jahrgangsdaten konnten nicht ermittelt werden.");
			throw new ApiOperationException(Status.NOT_FOUND, e,
					"FEHLER: Die Jahrgangsdaten. konnten nicht ermittelt werden.");
		}
	}

	/**
	 * Initialisiert die Daten der Stundenpläne.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private void initStundenplaene() throws ApiOperationException {
		try {
			this.logger.logLn(LogLevel.DEBUG, 8, "Ermittle alle Stundenplandefinitionen der Schule.");
			// Ermittle alle Stundenpläne zum aktuellen Schuljahresabschnitt.
			this.stundenplandefinitionen = DataStundenplanListe.getStundenplaene(this.conn, null);
			this.logger.logLn(LogLevel.DEBUG, 8, "Ermittle die Stundenpläne für den aktuellen und ausgewählten Schuljahresabschnitt.");
			final List<StundenplanListeEintrag> filterStundenplandefinitionen =
					stundenplandefinitionen.stream().filter(d -> ((d.idSchuljahresabschnitt == idAktuellerSchuljahresbaschnitt)
							|| (d.idSchuljahresabschnitt == idAuswahlSchuljahresbaschnitt))).toList();
			if (!filterStundenplandefinitionen.isEmpty()) {
				for (final StundenplanListeEintrag stundenplandefinition : filterStundenplandefinitionen) {
					final Stundenplan stundenplan = DataStundenplan.getStundenplan(this.conn, stundenplandefinition.id);
					if (stundenplan != null)
						this.mapStundenplaene.put(stundenplandefinition.id, stundenplan);
				}
			}
		} catch (final Exception e) {
			this.logger.logLn(LogLevel.ERROR, 8, "Die Daten der Stundenpläne konnten nicht ermittelt werden.");
			throw new ApiOperationException(Status.NOT_FOUND, e,
					"FEHLER: Die Daten der Stundenpläne konnten nicht ermittelt werden.");
		}
	}


	/**
	 * Die Verbindung zu Datenbank.
	 *
	 * @return Inhalt des Feldes conn
	 */
	public DBEntityManager conn() {
		return conn;
	}

	/**
	 * Liste, die Einträge aus dem Logger sammelt.
	 *
	 * @return Inhalt des Feldes log
	 */
	public LogConsumerList log() {
		return log;
	}

	/**
	 * Logger, der den Ablauf protokolliert und Fehlerdaten sammelt.
	 *
	 * @return Inhalt des Feldes logger
	 */
	public Logger logger() {
		return logger;
	}

	/**
	 * Einstellungen und Daten zum Steuern der Report-Generierung.
	 *
	 * @return Inhalt des Feldes reportingParameter
	 */
	public ReportingParameter reportingParameter() {
		return reportingParameter;
	}


	/**
	 * Stellt die Stammdaten der Schule aus der Datenbankverbindung zur Verfügung
	 *
	 * @return Stammdaten der Schule
	 */
	public SchuleStammdaten schulstammdaten() {
		return schulstammdaten;
	}


	/**
	 * Stellt eine sortierte Liste aller Schuljahresabschnitte der Schule aus der Datenbankverbindung zur Verfügung
	 *
	 * @return Alle Schuljahresabschnitte der Schule
	 */
	public List<ReportingSchuljahresabschnitt> schuljahresabschnitte() {
		return mapSchuljahresabschnitte.values().stream().sorted(
				Comparator.comparing(ReportingSchuljahresabschnitt::schuljahr)
						.thenComparing(ReportingSchuljahresabschnitt::abschnitt))
				.toList();
	}

	/**
	 * Stellt den angeforderten Schuljahresabschnitt der Schule aus der Datenbankverbindung zur Verfügung
	 *
	 * @param id Die ID des angeforderten Schuljahresabschnitts
	 *
	 * @return Schuljahresabschnitt der Schule zur ID
	 */
	public ReportingSchuljahresabschnitt schuljahresabschnitt(final long id) {
		return mapSchuljahresabschnitte.get(id);
	}

	/**
	 * Stellt den angeforderten Schuljahresabschnitt der Schule aus der Datenbankverbindung zur Verfügung
	 *
	 * @param schuljahr Das Schuljahr des angeforderten Schuljahresabschnitts
	 * @param abschnitt Der Abschnitt des angeforderten Schuljahresabschnitts
	 *
	 * @return Schuljahresabschnitt der Schule zu den Parametern
	 */
	public ReportingSchuljahresabschnitt schuljahresabschnitt(final int schuljahr, final int abschnitt) {
		final List<ReportingSchuljahresabschnitt> reportingSchuljahresabschnitte =
				mapSchuljahresabschnitte.values().stream().filter(a -> (a.schuljahr() == schuljahr) && (a.abschnitt() == abschnitt)).toList();
		if (reportingSchuljahresabschnitte.isEmpty())
			return null;
		return reportingSchuljahresabschnitte.getFirst();
	}


	/**
	 * Stellt den aktuellen Schuljahresabschnitt der Schule aus der Datenbankverbindung zur Verfügung
	 *
	 * @return Aktueller Schuljahresabschnitt der Schule
	 */
	public ReportingSchuljahresabschnitt aktuellerSchuljahresabschnitt() {
		return this.mapSchuljahresabschnitte.get(idAktuellerSchuljahresbaschnitt);
	}

	/**
	 * Der ausgewählte Schuljahresabschnitt, der für die Ausgabe der Reports ausgewählt wurde
	 *
	 * @return Schuljahresabschnitt der Auswahl für den Druck
	 */
	public ReportingSchuljahresabschnitt auswahlSchuljahresabschnitt() {
		return this.mapSchuljahresabschnitte.get(idAuswahlSchuljahresbaschnitt);
	}




	/**
	 * Stellt die eine Map der Förderschwerpunkt-Katalog-Einträge der Schule aus der Datenbankverbindung zu deren IDs zur Verfügung
	 *
	 * @return Map der Förderschwerpunkt-Katalog-Einträge
	 */
	public Map<Long, FoerderschwerpunktEintrag> katalogFoerderschwerpunkte() {
		return katalogFoerderschwerpunkte;
	}

	/**
	 * Stellt die eine Map der Ort-Katalog-Einträge der Schule aus der Datenbankverbindung zu deren IDs zur Verfügung
	 *
	 * @return Map der Ort-Katalog-Einträge
	 */
	public Map<Long, OrtKatalogEintrag> katalogOrte() {
		return katalogOrte;
	}

	/**
	 * Stellt die eine Map der Ortsteil-Katalog-Einträge der Schule aus der Datenbankverbindung zu deren IDs zur Verfügung
	 *
	 * @return Map der Ortsteil-Katalog-Einträge
	 */
	public Map<Long, OrtsteilKatalogEintrag> katalogOrtsteile() {
		return katalogOrtsteile;
	}

	/**
	 * Stellt die Religionen aus dem Katalog der Schule zur Religion-ID zur Verfügung
	 *
	 * @return Map der Religionen-Katalog-Einträge
	 */
	public Map<Long, ReligionEintrag> katalogReligionen() {
		return katalogReligionen;
	}



	/**
	 * Stellt die Daten aller bereits abgerufenen Leistungsdaten zur Schüler-, Lernabschnitts- und Leistungsdaten-ID zur Verfügung.
	 *
	 * @return Map der Daten aller bereits abgerufenen Lernabschnitte.
	 */
	public ListMap3DLongKeys<SchuelerLeistungsdaten> mapAlleLeistungsdaten() {
		return mapAlleLeistungsdaten;
	}

	/**
	 * Stellt die Daten aller bereits abgerufenen Lernabschnitte zur Schüler-, Schuljahresabschnitt und Lernabschnitts-ID zur Verfügung.
	 *
	 * @return Map der Daten aller bereits abgerufenen Lernabschnitte.
	 */
	public ListMap3DLongKeys<SchuelerLernabschnittsdaten> mapAlleLernabschnittsdaten() {
		return mapAlleLernabschnittsdaten;
	}

	/**
	 * Stellt die Stammdaten von bereits abgerufenen Erziehern über eine Map zur Schüler-ID zur Verfügung
	 *
	 * @return Inhalt des Feldes mapErzieherStammdaten
	 */
	public Map<Long, List<ErzieherStammdaten>> mapErzieherStammdaten() {
		return mapErzieherStammdaten;
	}

	/**
	 * Stellt alle Erzieherarten über eine Map zur Erzieherart-ID zur Verfügung
	 *
	 * @return Inhalt des Feldes mapReportingErzieherarten
	 */
	public Map<Long, ReportingErzieherArt> mapReportingErzieherarten() {
		return mapReportingErzieherarten;
	}

	/**
	 * Stellt alle Fächer der Schule als DTOs zur Fach-ID zur Verfügung. Die Reporting-Fächer -Objekte sind in den Schuljahresabschnitten abrufbar.
	 *
	 * @return Map der Fächer-DTO
	 */
	public Map<Long, DTOFach> mapFaecher() {
		return mapFaecher;
	}

	/**
	 * Stellt die Daten der Abiturjahrgänge über eine Map zum Abiturjahr Verfügung.
	 *
	 * @return Map der Daten zu den Abiturjahrgängen
	 */
	public Map<Integer, GostJahrgangsdaten> mapGostAbiturjahrgangDaten() {
		return mapGostAbiturjahrgangDaten;
	}

	/**
	 * Stellt die Fächer der Abiturjahrgänge über eine Map zum Abiturjahr Verfügung.
	 *
	 * @return Map der Fächer zu den Abiturjahrgängen
	 */
	public Map<Integer, GostFaecherManager> mapGostAbiturjahrgangFaecher() {
		return mapGostAbiturjahrgangFaecher;
	}

	/**
	 * Stellt die Beratungsdaten zur GOSt von bereits abgerufenen Schülern über eine Map zur Schüler-ID zur Verfügung.
	 *
	 * @return Map mit GOSt-Beratungsdaten der Schüler
	 */
	public Map<Long, GostLaufbahnplanungBeratungsdaten> mapGostBeratungsdaten() {
		return mapGostBeratungsdaten;
	}

	/**
	 * Stellt die für die Beratungsdaten zur GOSt relevanten Abiturdaten von bereits abgerufenen Schülern über eine Map zur Schüler-ID zur Verfügung.
	 *
	 * @return Inhalt des Feldes mapGostBeratungsdatenAbiturdaten
	 */
	public Map<Long, Abiturdaten> mapGostBeratungsdatenAbiturdaten() {
		return mapGostBeratungsdatenAbiturdaten;
	}

	/**
	 * Stellt die Abiturdaten in der GOSt von bereits abgerufenen Schülern über eine Map zur Schüler-ID zur Verfügung.
	 *
	 * @return Map mit GOSt-Abiturdaten der Schüler
	 */
	public Map<Long, Abiturdaten> mapGostSchuelerAbiturdaten() {
		return mapGostSchuelerAbiturdaten;
	}

	/**
	 * Stellt die Stammdaten der Jahrgänge über eine Map zur Jahrgangs-ID zur Verfügung
	 *
	 * @return Map der Stammdaten der Jahrgänge.
	 */
	public Map<Long, JahrgangsDaten> mapJahrgaenge() {
		return mapJahrgaenge;
	}

	/**
	 * Stellt alle Klassen in den Schuljahresabschnitten über eine Map zur Klassen-ID zur Verfügung.
	 *
	 * @return Map der Stammdaten der Klassen.
	 */
	public Map<Long, ReportingKlasse> mapKlassen() {
		return mapKlassen;
	}

	/**
	 * Stellt alle Kurse in den Schuljahresabschnitten über eine Map zur Kurs-ID zur Verfügung.
	 *
	 * @return Map der Stammdaten der Kurse.
	 */
	public Map<Long, ReportingKurs> mapKurse() {
		return mapKurse;
	}

	/**
	 * Stellt die Stammdaten von bereits abgerufenen Lehrkräften über eine Map zur Lehrer-ID zur Verfügung.
	 *
	 * @return Map der Stammdaten von bereits abgerufenen Lehrkräften
	 */
	public Map<Long, LehrerStammdaten> mapLehrerStammdaten() {
		return mapLehrerStammdaten;
	}

	/**
	 * Stellt die Stammdaten von bereits abgerufenen Schülern über eine Map zur Schüler-ID zur Verfügung
	 *
	 * @return Map der Stammdaten von bereits abgerufenen Schülern
	 */
	public Map<Long, SchuelerStammdaten> mapSchuelerStammdaten() {
		return mapSchuelerStammdaten;
	}

	/**
	 * Stellt die Liste aller Schüler über eine Map zur Schüler-ID zur Verfügung
	 *
	 * @return Inhalt des Feldes mapSchueler
	 */
	public Map<Long, ReportingSchueler> mapSchueler() {
		return mapSchueler;
	}

	/**
	 * Stellt die Sprachbelegungen von bereits abgerufenen Schülern über eine Map zur Schüler-ID zur Verfügung
	 *
	 * @return Inhalt des Feldes mapSchuelerSprachbelegungen
	 */
	public Map<Long, List<Sprachbelegung>> mapSchuelerSprachbelegungen() {
		return mapSchuelerSprachbelegungen;
	}

	/**
	 * Stellt die Liste aller Schuljahresabschnitte über eine Map zur Schulabschnitts-ID zur Verfügung
	 *
	 * @return Inhalt des Feldes mapSchuljahresabschnitte
	 */
	public Map<Long, ReportingSchuljahresabschnitt> mapSchuljahresabschnitte() {
		return mapSchuljahresabschnitte;
	}

	/**
	 * Stelle eine Liste aller Stundenplandefinitionen der Schule zur Verfügung, sortiert nach Schuljahresabschnitt und Gültigkeitsbeginn.
	 *
	 * @return Inhalt des Feldes stundenplandefinitionen
	 */
	public List<StundenplanListeEintrag> stundenplandefinitionen() {
		return stundenplandefinitionen;
	}

	/**
	 * Stelle die bereits eingelesenen Stundenpläne als Map zu ihrer ID zur Verfügung.
	 *
	 * @return Inhalt des Feldes mapStundenplaene
	 */
	public Map<Long, Stundenplan> mapStundenplaene() {
		return mapStundenplaene;
	}

	/**
	 * Ermittelt den zum übergebenen Datum gehörigen Stundenplan und gibt ihn zurück.
	 *
	 * @param datum Das Datum im Format yyyy-mm-dd, dessen Stundenplan bestimmt werden soll.
	 *
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
			try {
				final Stundenplan stundenplan = DataStundenplan.getStundenplan(this.conn, stundenplandefinitionZuDatum.id);
				if (stundenplan != null) {
					this.mapStundenplaene.put(stundenplandefinitionZuDatum.id, stundenplan);
					return mapStundenplaene.get(stundenplandefinitionZuDatum.id);
				}
			} catch (@SuppressWarnings("unused") final Exception ignore) {
				// Stundenplan konnte nicht aus der Datenbank ermittelt werden. Sollte nicht eintreten, wenn Definition vorhanden ist.
			}
		}
		// Kein Plan gefunden oder Fehler.
		return null;
	}


}
