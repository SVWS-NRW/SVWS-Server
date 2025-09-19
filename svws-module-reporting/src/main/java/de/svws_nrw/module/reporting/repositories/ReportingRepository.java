package de.svws_nrw.module.reporting.repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.data.schule.SchulformKatalogEintrag;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.adt.map.ListMap3DLongKeys;
import de.svws_nrw.core.adt.map.ListMap4DLongKeys;
import de.svws_nrw.core.data.erzieher.ErzieherStammdaten;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostJahrgangsdaten;
import de.svws_nrw.core.data.gost.GostLaufbahnplanungBeratungsdaten;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.kataloge.KatalogEntlassgrund;
import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.core.data.kataloge.SchulEintrag;
import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.asd.data.schueler.SchuelerLeistungsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.asd.data.schueler.Sprachbelegung;
import de.svws_nrw.core.data.schule.FoerderschwerpunktEintrag;
import de.svws_nrw.core.data.schule.ReligionEintrag;
import de.svws_nrw.core.data.schule.TelefonArt;
import de.svws_nrw.core.data.stundenplan.Stundenplan;
import de.svws_nrw.core.data.stundenplan.StundenplanListeEintrag;
import de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsicht;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterricht;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterrichtsverteilung;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.core.utils.stundenplan.StundenplanManager;
import de.svws_nrw.data.erzieher.DataErzieherarten;
import de.svws_nrw.data.jahrgaenge.DataJahrgangsdaten;
import de.svws_nrw.data.kataloge.DataKatalogEntlassgruende;
import de.svws_nrw.data.kataloge.DataOrte;
import de.svws_nrw.data.kataloge.DataOrtsteile;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.data.lehrer.DataLehrerStammdaten;
import de.svws_nrw.data.schueler.DataKatalogSchuelerFoerderschwerpunkte;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.data.schule.DataKatalogTelefonArten;
import de.svws_nrw.data.schule.DataReligionen;
import de.svws_nrw.data.schule.DataSchuleStammdaten;
import de.svws_nrw.data.schule.DataSchulen;
import de.svws_nrw.data.stundenplan.DataStundenplan;
import de.svws_nrw.data.stundenplan.DataStundenplanListe;
import de.svws_nrw.data.stundenplan.DataStundenplanPausenaufsichten;
import de.svws_nrw.data.stundenplan.DataStundenplanUnterricht;
import de.svws_nrw.data.stundenplan.DataStundenplanUnterrichtsverteilung;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.proxytypes.lehrer.ProxyReportingLehrer;
import de.svws_nrw.module.reporting.proxytypes.schueler.ProxyReportingSchueler;
import de.svws_nrw.module.reporting.proxytypes.schueler.erzieher.ProxyReportingErzieherArt;
import de.svws_nrw.module.reporting.proxytypes.schule.ProxyReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.proxytypes.stundenplanung.ProxyReportingStundenplanungStundenplan;
import de.svws_nrw.module.reporting.sortierung.ComparatorFactory;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistryReportingLehrer;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistryReportingSchueler;
import de.svws_nrw.module.reporting.types.klasse.ReportingKlasse;
import de.svws_nrw.module.reporting.types.kurs.ReportingKurs;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.erzieher.ReportingErzieherArt;
import de.svws_nrw.module.reporting.types.schueler.telefon.ReportingSchuelerTelefonkontakt;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import jakarta.ws.rs.core.Response.Status;

/**
 * <p>Dieses Repository enthält neben den Stammdaten der Schule Maps, in denen Objekte wie Kataloge, Jahrgänge, Klassen, Lehrer, Schüler usw. gespeichert
 * werden. So sollen Datenbankzugriffe minimiert werden.</p>
 * <p>Des Weiteren kann diese Klasse genutzt werden, um die Maps bereits vor der Erstellung der eigentlichen Reporting-Objekte zu füllen,
 * beispielsweise mit Daten aus im Vorfeld durchgeführten Prüfungen bei API-Abfragen. So müssen diese Daten nicht erneut aus der Datenbank
 * geladen werden.</p>
 * <p>Werden in anderen Klassen Daten nachgeladen, so werden diese in der Regel auch in der entsprechenden Map des Repository ergänzt.</p>
 * <p>Ebenso werden einige bereits erzeugte Reporting-Objekte hier zwischengespeichert.</p>
 */
public class ReportingRepository {

	/** Die Verbindung zu Datenbank. */
	private final DBEntityManager conn;

	/** Einstellungen und Daten zum Steuern der Report-Generierung. */
	private final ReportingParameter reportingParameter;

	/** Logger, der den Ablauf protokolliert und Fehlerdaten sammelt. */
	private final Logger logger;

	/** Die Liste, die Einträge aus dem Logger sammelt. */
	private final LogConsumerList log;

	/** Die ID des aktuellen Schuljahresabschnitts der Schule. */
	private final Long idAktuellerSchuljahresabschnitt;

	/** Die ID des in den ReportingParametern ausgewählten Schuljahresabschnitts. */
	private final Long idAuswahlSchuljahresabschnitt;


	// #########  Ab hier folgen Objekte aus dem Core. #########

	/** Die Stammdaten der Schule zur Datenbankanbindung. */
	private final SchuleStammdaten schulstammdaten;

	/** Stellt den Katalog der Entlassgründe über eine Map zur Entlassgrund-ID zur Verfügung */
	private Map<Long, KatalogEntlassgrund> katalogEntlassgruende;

	/** Stellt den Katalog der Förderschwerpunkte über eine Map zur Förderschwerpunkt-ID zur Verfügung */
	private Map<Long, FoerderschwerpunktEintrag> katalogFoerderschwerpunkte;

	/** Stellt den Katalog der Orte über eine Map zur Ort-ID zur Verfügung */
	private Map<Long, OrtKatalogEintrag> katalogOrte;

	/** Stellt den Katalog der Ortsteile über eine Map zur Ortsteil-ID zur Verfügung */
	private Map<Long, OrtsteilKatalogEintrag> katalogOrtsteile;

	/** Stellt die Religionen aus dem Katalog der Schule zur Religion-ID zur Verfügung */
	private Map<Long, ReligionEintrag> katalogReligionen;

	/** Stellt die Schulen aus dem Katalog der Schulen der Schule zur Schul-ID zur Verfügung */
	private Map<Long, SchulEintrag> katalogSchulen;

	/** Stellt die Schulformen gemäß ihrer ID aus der Historie des Core-Types zur Verfügung */
	private Map<Long, SchulformKatalogEintrag> katalogSchulformen;

	/** Stelle die Telefonnummer-Arten zu ihrer ID zur Verfügung. */
	private Map<Long, TelefonArt> katalogTelefonnummerArten = new HashMap<>();


	/** Stellt die Daten aller bereits abgerufenen Leistungsdaten zur Schüler-, Lernabschnitts- und Leistungsdaten-ID zur Verfügung. */
	private final ListMap3DLongKeys<SchuelerLeistungsdaten> mapAlleLeistungsdaten = new ListMap3DLongKeys<>();

	/** Stellt die Daten aller bereits abgerufenen Lernabschnittsdaten zur Schüler-, Schuljahresabschnitt-, Wechselnummer und Lernabschnitts-ID zur Verfügung. */
	private final ListMap4DLongKeys<SchuelerLernabschnittsdaten> mapAlleLernabschnittsdaten = new ListMap4DLongKeys<>();

	/** Stellt die Stammdaten von bereits abgerufenen Erziehern über eine Map zur Schueler-ID zur Verfügung. */
	private final Map<Long, List<ErzieherStammdaten>> mapErzieherStammdaten = new HashMap<>();

	/** Stellt alle Fächer der Schule als DTOs zur Fach-ID zur Verfügung. Die Reporting-Fächer-Objekte sind in den Schuljahresabschnitten abrufbar. */
	private final Map<Long, DTOFach> mapFachdaten = new HashMap<>();

	/** Stellt die Daten der Abiturjahrgänge über eine Map zum Abiturjahr zur Verfügung. */
	private final Map<Integer, GostJahrgangsdaten> mapGostAbiturjahrgangDaten = new HashMap<>();

	/** Stellt die Fächer der Abiturjahrgänge über eine Map zum Abiturjahr zur Verfügung. */
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

	/** Stellt die Schulbesuchsdaten von bereits abgerufenen Schülern über eine Map zur Schüler-ID zur Verfügung */
	private final Map<Long, SchuelerSchulbesuchsdaten> mapSchuelerSchulbesuchsdaten = new HashMap<>();

	/** Stellt die Stammdaten von bereits abgerufenen Schülern über eine Map zur Schüler-ID zur Verfügung */
	private final Map<Long, SchuelerStammdaten> mapSchuelerStammdaten = new HashMap<>();

	/** Stelle eine Liste aller Stundenplandefinitionen der Schule zur Verfügung, sortiert nach Schuljahresabschnitt und Gültigkeitsbeginn. */
	private List<StundenplanListeEintrag> stundenplandefinitionen;

	/** Stelle die bereits erstellte Stundenmanager als Map zu ihrer ID zur Verfügung. */
	private final Map<Long, StundenplanManager> mapStundenplanManager = new HashMap<>();


	// #########  Ab hier folgen Reporting-Objekte. #########

	/** Stellt alle Erzieherarten über eine Map zur Erzieherart-ID zur Verfügung */
	private Map<Long, ReportingErzieherArt> mapErzieherarten;

	/** Stellt alle Klassen in den Schuljahresabschnitten über eine Map zur Klassen-ID zur Verfügung. */
	private final Map<Long, ReportingKlasse> mapKlassen = new HashMap<>();

	/** Stellt alle Kurse in den Schuljahresabschnitten über eine Map zur Kurs-ID zur Verfügung. */
	private final Map<Long, ReportingKurs> mapKurse = new HashMap<>();

	/** Stellt die Liste aller Lehrer über eine Map zur Lehrer-ID zur Verfügung */
	private final Map<Long, ReportingLehrer> mapLehrer = new HashMap<>();

	/** Stellt die Liste aller Schüler über eine Map zur Schüler-ID zur Verfügung */
	private final Map<Long, ReportingSchueler> mapSchueler = new HashMap<>();

	/** Stellt die Liste aller Schüler über eine Map zur Schüler-ID zur Verfügung */
	private final Map<Long, List<ReportingSchuelerTelefonkontakt>> mapSchuelerTelefonkontakte = new HashMap<>();

	/** Stellt die Liste aller Schuljahresabschnitte über eine Map zur Schulabschnitts-ID zur Verfügung */
	private final Map<Long, ReportingSchuljahresabschnitt> mapSchuljahresabschnitte = new HashMap<>();

	/** Stellt die Liste aller Stundenpläne über eine Map zur Stundenplan-ID zur Verfügung. */
	private final Map<Long, ReportingStundenplanungStundenplan> mapStundenplaene = new HashMap<>();



	/**
	 * Erstellt das Repository für häufig genutzte Daten aus der Schuldatenbank, um Zugriffe darauf zu minimieren. Ebenso werden einzelne
	 * Reporting-Objekte hier zwischengespeichert.
	 *
	 * @param conn						Die Verbindung zur Datenbank.
	 * @param reportingParameter 		Einstellungen und Daten zum Steuern der Report-Generierung.
	 * @param logger 					Logger, der den Ablauf protokolliert und Fehlerdaten sammelt.
	 * @param log 						Die Liste, die Einträge aus dem Logger sammelt.
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

			idAktuellerSchuljahresabschnitt = this.schulstammdaten.idSchuljahresabschnitt;
			idAuswahlSchuljahresabschnitt = this.reportingParameter.idSchuljahresabschnitt;
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
		initStundenplanDefinitionen();

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

			this.katalogEntlassgruende =
					new DataKatalogEntlassgruende(this.conn).getAll().stream().collect(Collectors.toMap(e -> e.id, e -> e));
			this.logger.logLn(LogLevel.DEBUG, 8, "Katalog Entlassgründe geladen.");

			this.katalogFoerderschwerpunkte =
					new DataKatalogSchuelerFoerderschwerpunkte(this.conn).getAll().stream().collect(Collectors.toMap(f -> f.id, f -> f));
			this.logger.logLn(LogLevel.DEBUG, 8, "Katalog Förderschwerpunkte geladen.");

			this.katalogOrte = new DataOrte(this.conn).getOrte().stream().collect(Collectors.toMap(o -> o.id, o -> o));
			this.logger.logLn(LogLevel.DEBUG, 8, "Katalog Orte geladen.");

			this.katalogOrtsteile = new DataOrtsteile(this.conn).getOrtsteile().stream().collect(Collectors.toMap(o -> o.id, o -> o));
			this.logger.logLn(LogLevel.DEBUG, 8, "Katalog Ortsteile geladen.");

			this.katalogReligionen = new DataReligionen(this.conn).getAll().stream().collect(Collectors.toMap(r -> r.id, r -> r));
			this.logger.logLn(LogLevel.DEBUG, 8, "Katalog Religionen geladen.");

			this.katalogSchulen = new DataSchulen(this.conn).getAll().stream().collect(Collectors.toMap(s -> s.id, s -> s));
			this.logger.logLn(LogLevel.DEBUG, 8, "Katalog Schulen geladen.");

			final ArrayList<SchulformKatalogEintrag> schulformen = new ArrayList<>();
			for (final Schulform schulform : Schulform.values())
				schulformen.addAll(schulform.historie());
			this.katalogSchulformen = schulformen.stream().collect(Collectors.toMap(sfke -> sfke.id, sfke -> sfke));
			this.logger.logLn(LogLevel.DEBUG, 8, "Katalog Schulformen geladen.");

			this.katalogTelefonnummerArten = new DataKatalogTelefonArten(this.conn).getAll().stream().collect(Collectors.toMap(ta -> ta.id, ta -> ta));
			this.logger.logLn(LogLevel.DEBUG, 8, "Katalog TelefonnummerArten geladen.");

			this.mapErzieherarten = new DataErzieherarten(this.conn).getAll().stream().collect(Collectors.toMap(a -> a.id,
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
			mapFachdaten.putAll(conn.queryAll(DTOFach.class).stream().collect(Collectors.toMap(f -> f.ID, f -> f)));
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
	 * Initialisiert die Liste der vorhandenen Daten der Stundenpläne der Schule.
	 *
	 * @throws ApiOperationException	Im Fehlerfall wird eine ApiOperationException ausgelöst und Log-Daten zusammen mit dieser zurückgegeben.
	 */
	private void initStundenplanDefinitionen() throws ApiOperationException {
		try {
			this.logger.logLn(LogLevel.DEBUG, 8, "Ermittle alle Stundenplan-Definitionen der Schule.");
			// Ermittle alle Stundenpläne zum aktuellen Schuljahresabschnitt.
			this.stundenplandefinitionen = new ArrayList<>(DataStundenplanListe.getStundenplaeneAktiv(this.conn, null));
			if (!this.stundenplandefinitionen.isEmpty())
				this.stundenplandefinitionen.sort(Comparator.comparing((StundenplanListeEintrag sle) -> sle.gueltigAb).reversed());
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
	 * Die Liste, die Einträge aus dem Logger sammelt.
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
	 * @return Schuljahresabschnitt der Schule zu den Parametern oder null, wenn der Abschnitt nicht existiert.
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
		return this.mapSchuljahresabschnitte.get(idAktuellerSchuljahresabschnitt);
	}

	/**
	 * Der ausgewählte Schuljahresabschnitt, der für die Ausgabe der Reports ausgewählt wurde
	 *
	 * @return Schuljahresabschnitt der Auswahl für den Druck
	 */
	public ReportingSchuljahresabschnitt auswahlSchuljahresabschnitt() {
		return this.mapSchuljahresabschnitte.get(idAuswahlSchuljahresabschnitt);
	}




	/**
	 * Stellt eine Map der Entlassgründe der Schule aus der Datenbankverbindung zu deren IDs zur Verfügung
	 *
	 * @return Map der Entlassgründe
	 */
	public Map<Long, KatalogEntlassgrund> katalogEntlassgruende() {
		return katalogEntlassgruende;
	}

	/**
	 * Stellt eine Map der Förderschwerpunkt-Katalog-Einträge der Schule aus der Datenbankverbindung zu deren IDs zur Verfügung
	 *
	 * @return Map der Förderschwerpunkt-Katalog-Einträge
	 */
	public Map<Long, FoerderschwerpunktEintrag> katalogFoerderschwerpunkte() {
		return katalogFoerderschwerpunkte;
	}

	/**
	 * Stellt eine Map der Ort-Katalog-Einträge der Schule aus der Datenbankverbindung zu deren IDs zur Verfügung
	 *
	 * @return Map der Ort-Katalog-Einträge
	 */
	public Map<Long, OrtKatalogEintrag> katalogOrte() {
		return katalogOrte;
	}

	/**
	 * Stellt eine Map der Ortsteil-Katalog-Einträge der Schule aus der Datenbankverbindung zu deren IDs zur Verfügung
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
	 * Stellt die Schulen aus dem Katalog der Schulen der Schule zur Schul-ID zur Verfügung
	 *
	 * @return Map der Schul-Katalog-Einträge
	 */
	public Map<Long, SchulEintrag> katalogSchulen() {
		return katalogSchulen;
	}

	/**
	 * Stellt die Schulformen gemäß ihrer ID aus der Historie des Core-Types zur Verfügung
	 *
	 * @return Map der Schulform-Einträge
	 */
	public Map<Long, SchulformKatalogEintrag> katalogSchulformen() {
		return katalogSchulformen;
	}

	/**
	 * Stellt die Telefonnummer-Arten zu ihrer ID zur Verfügung.
	 *
	 * @return Map der Telefonnummer-Arten
	 */
	public Map<Long, TelefonArt> katalogTelefonnummerArten() {
		return katalogTelefonnummerArten;
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
	 * Stellt die Daten aller bereits abgerufenen Lernabschnitte zur Schüler-, Schuljahresabschnitt, Wechselnummer und Lernabschnitts-ID zur Verfügung.
	 *
	 * @return Map der Daten aller bereits abgerufenen Lernabschnitte.
	 */
	public ListMap4DLongKeys<SchuelerLernabschnittsdaten> mapAlleLernabschnittsdaten() {
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
		return mapErzieherarten;
	}

	/**
	 * Stellt alle Fächer der Schule als DTOs zur Fach-ID zur Verfügung. Die Reporting-Fächer-Objekte sind in den Schuljahresabschnitten abrufbar.
	 *
	 * @return Map der Fächer-DTO
	 */
	public Map<Long, DTOFach> mapFaecher() {
		return mapFachdaten;
	}

	/**
	 * Stellt die Daten der Abiturjahrgänge über eine Map zum Abiturjahr zur Verfügung.
	 *
	 * @return Map der Daten zu den Abiturjahrgängen
	 */
	public Map<Integer, GostJahrgangsdaten> mapGostAbiturjahrgangDaten() {
		return mapGostAbiturjahrgangDaten;
	}

	/**
	 * Stellt die Fächer der Abiturjahrgänge über eine Map zum Abiturjahr zur Verfügung.
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
	 * Stellt die Schulbesuchsdaten von bereits abgerufenen Schülern über eine Map zur Schüler-ID zur Verfügung
	 *
	 * @return Map der Schulbesuchsdaten von bereits abgerufenen Schülern
	 */
	public Map<Long, SchuelerSchulbesuchsdaten> mapSchuelerSchulbesuchsdaten() {
		return mapSchuelerSchulbesuchsdaten;
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
	 * Stellt die Listen aller Telefonkontakte pro Schüler für alle Schüler über eine Map zur Schüler-ID zur Verfügung
	 *
	 * @return Inhalt des Feldes mapSchuelerTelefonkontakte
	 */
	public Map<Long, List<ReportingSchuelerTelefonkontakt>> mapSchuelerTelefonkontakte() {
		return mapSchuelerTelefonkontakte;
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


	// ##### Reporting Klassen erzeugen und verwalten #####

	/**
	 * Liefert ein ReportingKlasse-Objekt basierend auf der gegebenen Klassen-ID.
	 * Wenn die ID negativ ist, wird null zurückgegeben.
	 * Ansonsten wird eine ProxyReportingKlasse erstellt und in der Map gespeichert,
	 * falls für die ID noch kein Eintrag existiert.
	 *
	 * @param idKlasse Die eindeutige ID der Klasse
	 *
	 * @return Ein ReportingKlassen-Objekt für die gegebene Klassen-ID oder null, falls die ID negativ ist
	 */
	public ReportingKlasse klasse(final long idKlasse) {
		if (idKlasse < 0)
			return null;

		// Prüfe, ob die ID der Klasse in der Map der Klassen ist.
		ergaenzeKlasseInMapKlassen(idKlasse);
		return mapKlassen.get(idKlasse);
	}

	/**
	 * Erzeugt und sortiert eine Liste von ReportingKlassen-Objekten basierend auf den übergebenen Klassen-IDs.
	 * Falls eine Klasse bereits existiert, wird er aus einem internen Cache abgerufen.
	 *
	 * @param idsKlassen Eine Liste von Long-Werten, die die IDs der Klassen repräsentieren, für die ReportingKlasse-Objekte erstellt werden sollen.
	 *                    Null- oder negative Werte in der Liste werden ignoriert.
	 * @return Eine sortierte Liste von ReportingKlasse-Objekten basierend auf dem Klassenkürzel.
	 */
	public List<ReportingKlasse> klassen(final List<Long> idsKlassen) {

		final List<ReportingKlasse> resultKlassen = new ArrayList<>();

		// Sofern noch keine Reporting-Objekte der Klassen existieren, erzeuge sie und speichere sie.
		for (final Long idKlasse : idsKlassen) {
			if ((idKlasse == null) || (idKlasse < 0))
				continue;
			ergaenzeKlasseInMapKlassen(idKlasse);
			resultKlassen.add(mapKlassen.get(idKlasse));
		}

		// Sortiere die Klassenliste für die Rückgabe
		return resultKlassen.stream().sorted(Comparator.comparing(ReportingKlasse::kuerzel)).toList();
	}

	/**
	 * Ergänzt eine Klasse in der Map der Klassen, wenn diese dort noch nicht existiert. Gleichzeitig werden auch alle anderen Klassen des gleichen
	 * Schuljahresabschnitts ergänzt.
	 *
	 * @param idKlasse Die ID der Klasse, die bei Fehlen in der Map ergänzt wird.
	 */
	private void ergaenzeKlasseInMapKlassen(final long idKlasse) {
		if (!mapKlassen.containsKey(idKlasse)) {
			final KlassenDaten klassenDaten;
			try {
				// Wenn nicht, lade alle Klassen des Schuljahresabschnitts aus der DB nach.
				klassenDaten = new DataKlassendaten(this.conn()).getById(idKlasse);
				this.schuljahresabschnitt(klassenDaten.idSchuljahresabschnitt).klasse(idKlasse);
			} catch (final ApiOperationException e) {
				ReportingExceptionUtils.putStacktraceInLog(
						"FEHLER: Fehler bei der Ermittlung der Daten für des Klassen %s.".formatted(idKlasse), e, this.logger(),
						LogLevel.ERROR,
						0);
			}
		}
	}


	// ##### Reporting Lehrer erzeugen und verwalten #####

	/**
	 * Gibt ein ReportingLehrer-Objekt für die angegebene Lehrer-ID zurück.
	 * Falls die Stammdaten des Lehrers nicht im Cache vorhanden sind, werden sie aus der Datenbank geladen.
	 *
	 * @param idLehrer Die eindeutige ID des Lehrers, für den das ReportingLehrer-Objekt erstellt werden soll.
	 *                 Die ID muss größer oder gleich 0 sein.
	 * @return Das ReportingLehrer-Objekt, falls Stammdaten gefunden wurden, oder null, wenn der Lehrer nicht existiert
	 *         oder ein Fehler beim Laden der Stammdaten aufgetreten ist.
	 */
	public ReportingLehrer lehrer(final long idLehrer) {
		if (idLehrer < 0)
			return null;

		// Lade ggf. fehlende Lehrerstammdaten analog zum Vorgehen bei Schülern nach.
		if (!mapLehrerStammdaten.containsKey(idLehrer)) {
			try {
				final LehrerStammdaten fehlendeLehrerstammdaten = new DataLehrerStammdaten(this.conn).getById(idLehrer);
				mapLehrerStammdaten.put(fehlendeLehrerstammdaten.id, fehlendeLehrerstammdaten);
			} catch (final ApiOperationException e) {
				ReportingExceptionUtils.putStacktraceInLog(
						"FEHLER: Fehler bei der Ermittlung der fehlenden Lehrerstammdaten einer Lehrkraft aus der Datenbank im ReportingRepository.", e,
						this.logger(), LogLevel.ERROR, 0);
				return null;
			}
		}

		if (mapLehrerStammdaten.containsKey(idLehrer))
			return mapLehrer.computeIfAbsent(idLehrer, key -> new ProxyReportingLehrer(this, mapLehrerStammdaten.get(key)));
		else
			return null;
	}

	/**
	 * Diese Methode erstellt eine Liste von ReportingLehrer-Objekten basierend auf den übergebenen Lehrerkürzeln (IDs).
	 * Fehlende Stammdaten werden bei Bedarf nachgeladen und zur weiteren Verarbeitung genutzt.
	 *
	 * @param idsLehrer Eine Liste der IDs der gewünschten Lehrer. Nullwerte oder IDs kleiner 0 werden ignoriert.
	 * @return Eine gemäß den konfigurierten Reporting-Parametern sortierte Liste von ReportingLehrer-Objekten.
	 */
	public List<ReportingLehrer> lehrer(final List<Long> idsLehrer) {

		final List<ReportingLehrer> resultLehrer = new ArrayList<>();

		// Analog zu schueler(...): fehlende Stammdaten nachladen.
		if ((idsLehrer == null) || idsLehrer.isEmpty())
			return resultLehrer;

		final List<Long> fehlendeLehrerIds = idsLehrer.stream()
				.filter(Objects::nonNull)
				.filter(id -> id >= 0)
				.distinct()
				.filter(id -> !mapLehrerStammdaten.containsKey(id))
				.toList();

		if (!fehlendeLehrerIds.isEmpty()) {
			try {
				final List<LehrerStammdaten> fehlendeLehrerstammdaten = new DataLehrerStammdaten(this.conn).getListByIDs(fehlendeLehrerIds);
				for (final LehrerStammdaten l : fehlendeLehrerstammdaten) {
					if (l != null)
						mapLehrerStammdaten.put(l.id, l);
				}
			} catch (final ApiOperationException e) {
				ReportingExceptionUtils.putStacktraceInLog(
						"FEHLER: Fehler bei der Ermittlung der fehlenden Lehrerstammdaten einer Lehrerliste aus der Datenbank im ReportingRepository.", e,
						this.logger(), LogLevel.ERROR, 0);
				return resultLehrer;
			}
		}

		// Sofern noch keine Reporting-Objekte der Lehrer existieren, erzeuge sie und speichere sie.
		for (final Long idLehrer : idsLehrer) {
			if ((idLehrer == null) || (idLehrer < 0))
				continue;
			if (mapLehrerStammdaten.containsKey(idLehrer))
				resultLehrer.add(mapLehrer.computeIfAbsent(idLehrer, key -> new ProxyReportingLehrer(this, mapLehrerStammdaten.get(key))));
		}

		// Sortiere gemäß den Angaben in den ReportingParametern.
		final Optional<Comparator<ReportingLehrer>> optionalComparator = ComparatorFactory.buildOptionalComparator(this, ReportingLehrer.class.getSimpleName(),
				SortierungRegistryReportingLehrer.sortierungRegistry(),
				SortierungRegistryReportingLehrer.standardsortierung());

		return optionalComparator
				.map(reportingLehrerComparator -> resultLehrer.stream().sorted(reportingLehrerComparator).toList())
				.orElse(resultLehrer);
	}


	// ##### ReportingSchueler-Objekte erzeugen und verwalten #####

	/**
	 * Holt die Reporting-Daten eines Schülers basierend auf der gegebenen ID.
	 * Falls die Schülerdaten nicht im lokalen Cache enthalten sind, werden sie aus der Datenbank abgerufen
	 * und im Cache gespeichert. Tritt ein Fehler beim Abrufen auf, wird null zurückgegeben.
	 *
	 * @param idSchueler die ID des Schülers, dessen Reporting-Daten abgerufen werden sollen
	 *                   (muss positiv oder 0 sein, andernfalls wird null zurückgegeben).
	 * @return ein ReportingSchueler-Objekt für den gegebenen Schüler,
	 *         falls die Daten erfolgreich abgerufen werden konnten; sonst null.
	 */
	public ReportingSchueler schueler(final long idSchueler) {
		if (idSchueler < 0)
			return null;

		if (!mapSchuelerStammdaten.containsKey(idSchueler)) {
			try {
				final SchuelerStammdaten fehlendeSchulerstammdaten = new DataSchuelerStammdaten(this.conn).getById(idSchueler);
				mapSchuelerStammdaten.put(fehlendeSchulerstammdaten.id, fehlendeSchulerstammdaten);
			} catch (final ApiOperationException e) {
				ReportingExceptionUtils.putStacktraceInLog(
						"FEHLER: Fehler bei der Ermittlung der fehlenden Schülerstammdaten eines Schülers aus der Datenbank im ReportingRepository.", e,
						this.logger(), LogLevel.ERROR, 0);
				return null;
			}
		}

		if (mapSchuelerStammdaten.containsKey(idSchueler))
			return mapSchueler.computeIfAbsent(idSchueler, key -> new ProxyReportingSchueler(this, mapSchuelerStammdaten.get(key)));
		else
			return null;
	}

	/**
	 * Liefert eine Liste von {@link ReportingSchueler}-Objekten basierend auf einer gegebenen Liste von Schüler-IDs.
	 * Diese Methode überprüft, ob die benötigten Stammdaten für die angegebenen Schüler-IDs bereits im Speicher vorhanden sind,
	 * und lädt diese gegebenenfalls aus der Datenbank nach. Anschließend werden entsprechende {@link ReportingSchueler}-Objekte erstellt
	 * und zurückgegeben.
	 *
	 * @param idsSchueler Eine Liste von Schüler-IDs, für die {@link ReportingSchueler}-Objekte erstellt und/oder zurückgegeben werden sollen;
	 *                    null oder eine leere Liste führt zu einer Rückgabe einer leeren Liste.
	 * @return Eine Liste von {@link ReportingSchueler}-Objekten, sortiert gemäß den vordefinierten oder standardmäßigen Sortierparametern,
	 *         sofern definiert. Wenn keine Schüler gefunden werden, wird eine leere Liste zurückgegeben.
	 */
	public List<ReportingSchueler> schueler(final List<Long> idsSchueler) {

		final List<ReportingSchueler> resultSchueler = new ArrayList<>();

		// Sofern noch keine Schülerstammdaten für (einzelne) Schüler aus der DB geladen wurden, müssen diese nachgeladen werden, weil dann auch keine
		// ReportingSchueler-Objekte vorhanden sein können.
		if ((idsSchueler == null) || idsSchueler.isEmpty())
			return resultSchueler;

		// Bereinige die Liste der IDs und suche die heraus, zu denen noch keine Stammdaten vorhanden sind.
		final List<Long> fehlendeSchuelerIds = idsSchueler.stream()
				.filter(Objects::nonNull)
				.filter(id -> id >= 0)
				.distinct()
				.filter(id -> !mapSchuelerStammdaten.containsKey(id))
				.toList();

		// Lade die fehlenden Schülerstammdaten aus der Datenbank nach.
		if (!fehlendeSchuelerIds.isEmpty()) {
			try {
				final List<SchuelerStammdaten> fehlendeSchulerstammdaten = new DataSchuelerStammdaten(this.conn).getListByIds(fehlendeSchuelerIds);
				for (final SchuelerStammdaten s : fehlendeSchulerstammdaten) {
					if (s != null)
						mapSchuelerStammdaten.put(s.id, s);
				}
			} catch (final ApiOperationException e) {
				ReportingExceptionUtils.putStacktraceInLog(
						"FEHLER: Fehler bei der Ermittlung der fehlenden Schülerstammdaten einer Schülerliste aus der Datenbank im ReportingRepository.", e,
						this.logger(), LogLevel.ERROR, 0);
				return resultSchueler;
			}
		}

		// Sofern noch keine Reporting-Objekte der Schüler existieren, erzeuge sie und speichere sie.
		for (final Long idSchueler : idsSchueler) {
			if ((idSchueler == null) || (idSchueler < 0))
				continue;
			if (mapSchuelerStammdaten.containsKey(idSchueler))
				resultSchueler.add(mapSchueler.computeIfAbsent(idSchueler, key -> new ProxyReportingSchueler(this, mapSchuelerStammdaten.get(key))));
		}

		// Sortiere gemäß den Angaben in den ReportingParametern.
		final Optional<Comparator<ReportingSchueler>> optionalComparator =
				ComparatorFactory.buildOptionalComparator(this, ReportingSchueler.class.getSimpleName(),
						SortierungRegistryReportingSchueler.sortierungRegistry(), SortierungRegistryReportingSchueler.standardsortierung());

		// Rückgabe der sortierten Liste oder der unsortierten Liste.
		return optionalComparator
				.map(reportingSchuelerComparator -> resultSchueler.stream().sorted(reportingSchuelerComparator).toList())
				.orElse(resultSchueler);
	}


	// ##### Stundenpläne erzeugen und verwalten #####

	/**
	 * Ermittelt den zum übergebenen Datum gehörigen Stundenplan und gibt ihn zurück.
	 *
	 * @param datum Das Datum im Format yyyy-mm-dd, dessen Stundenplan bestimmt werden soll.
	 *
	 * @return Der Stundenplan zum Datum, wenn er gefunden wird, sonst null.
	 */
	public ReportingStundenplanungStundenplan stundenplan(final String datum) {
		if ((datum == null) || (datum.length() != 10) || (stundenplandefinitionen == null) || (stundenplandefinitionen.isEmpty()))
			return null;

		final StundenplanListeEintrag stundenplandefinitionZuDatum = stundenplandefinitionen.stream()
				.filter(d -> ((d.gueltigAb != null) && (d.gueltigBis != null) && (datum.compareTo(d.gueltigAb) >= 0) && (datum.compareTo(d.gueltigBis) <= 0)))
				.findFirst().orElse(null);

		if (stundenplandefinitionZuDatum != null)
			return stundenplan(stundenplandefinitionZuDatum.id);
		else
			return null;
	}

	/**
	 * Ermittelt den zur übergebenen ID gehörigen Stundenplan und gibt ihn zurück.
	 *
	 * @param idStundenplan Die ID des Stundenplans
	 *
	 * @return Der Stundenplan zur ID, wenn er gefunden wird, sonst null.
	 */
	public ReportingStundenplanungStundenplan stundenplan(final long idStundenplan) {
		// Prüfe zunächst, ob die Stundenplan-ID auch als Definition in der Datenbank der Schule vorhanden ist.
		if (stundenplandefinitionen.stream().noneMatch(d -> d.id == idStundenplan))
			return null;

		// Nutze vorhandene Objekte, falls möglich.
		if (mapStundenplaene.containsKey(idStundenplan))
			return mapStundenplaene.get(idStundenplan);

		if (mapStundenplanManager.containsKey(idStundenplan)) {
			mapStundenplaene.computeIfAbsent(idStundenplan, key -> new ProxyReportingStundenplanungStundenplan(this, mapStundenplanManager.get(key)));
			return mapStundenplaene.get(idStundenplan);
		}

		// Sind bisher keine Daten vorhanden, dann lade die Daten aus der DB nach.
		try {
			final StundenplanManager stundenplanManager = stundenplanManager(idStundenplan);
			if (stundenplanManager != null) {
				mapStundenplanManager.put(idStundenplan, stundenplanManager);
				mapStundenplaene.put(idStundenplan, new ProxyReportingStundenplanungStundenplan(this, stundenplanManager));
				return mapStundenplaene.get(idStundenplan);
			}
		} catch (@SuppressWarnings("unused") final Exception ignore) {
			// Stundenplan konnte nicht aus der Datenbank ermittelt werden. Sollte nicht eintreten, wenn eine Definition vorhanden ist.
		}
		return null;
	}

	/**
	 * Ermittelt den zur übergebenen ID passenden StundenplanManager zurück.
	 *
	 * @param idStundenplan Die ID des Stundenplans
	 * @return Der StundenplanManager zur ID, wenn er gefunden wird, sonst null.
	 */
	public StundenplanManager stundenplanManager(final long idStundenplan) {
		mapStundenplanManager.computeIfAbsent(idStundenplan, key -> {
			try {
				final Stundenplan stundenplan = new DataStundenplan(conn).getById(key);
				if (stundenplan == null)
					return null;
				final List<StundenplanUnterricht> unterrichte = DataStundenplanUnterricht.getUnterrichte(this.conn, key);
				final List<StundenplanPausenaufsicht> aufsichten = DataStundenplanPausenaufsichten.getAufsichten(this.conn, key);
				final StundenplanUnterrichtsverteilung unterrichtsverteilung = DataStundenplanUnterrichtsverteilung.getUnterrichtsverteilung(this.conn, key);
				return new StundenplanManager(stundenplan, unterrichte, aufsichten, unterrichtsverteilung);
			} catch (final ApiOperationException e) {
				return null;
			}
		});

		return this.mapStundenplanManager.get(idStundenplan);
	}

}
