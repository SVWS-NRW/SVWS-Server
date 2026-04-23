package de.svws_nrw.module.reporting.repositories;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import de.svws_nrw.asd.data.schueler.SchuelerLeistungsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.asd.data.schueler.Sprachbelegung;
import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.data.schule.SchulformKatalogEintrag;
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
import de.svws_nrw.core.data.schule.FoerderschwerpunktEintrag;
import de.svws_nrw.core.data.schule.ReligionEintrag;
import de.svws_nrw.core.data.schule.Telefonart;
import de.svws_nrw.core.data.stundenplan.StundenplanListeEintrag;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.core.utils.stundenplan.StundenplanManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.filterung.ReportingFilterService;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierungService;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungKurs;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlasse;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.erzieher.ReportingErzieherArt;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerZuweisung;
import de.svws_nrw.module.reporting.types.schueler.telefon.ReportingSchuelerTelefonkontakt;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;
import jakarta.ws.rs.core.Response.Status;

/**
 * Zentrales Repository des Reporting-Moduls, welches aber nur an domänenspezifische Repositories delegiert.
 */
public class ReportingRepository {

	/** Die Verbindung zur Datenbank. */
	private final DBEntityManager conn;

	/** Einstellungen und Daten zum Steuern der Report-Generierung. */
	private final ReportingParameterTypisiert reportingParameterTypisiert;

	/** Logger, der den Ablauf protokolliert und Fehlerdaten sammelt. */
	private final Logger logger;

	/** Die Liste, die Einträge aus dem Logger sammelt. */
	private final LogConsumerList log;

	/** Service für die Ermittlung von Sortierungsattributen. */
	private final ReportingSortierungService sortierungService;

	/** Service für die Erstellung von Filtern. */
	private final ReportingFilterService filterService;

	// Domänen-Repositories
	private final ReportingRepositorySchule repositorySchule;
	private final ReportingRepositoryKataloge repositoryKataloge;
	private final ReportingRepositoryLehrer repositoryLehrer;
	private final ReportingRepositorySchueler repositorySchueler;
	private final ReportingRepositoryLerngruppen repositoryLerngruppen;
	private final ReportingRepositoryStundenplan repositoryStundenplan;
	private final ReportingRepositoryGost repositoryGost;


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
	@SuppressWarnings("java:S3366") // Die Warnung "'this' should not be exposed from constructors" kann aus folgendem Grund hier unterdrückt werden.
	// Die Weitergabe von 'this innerhalb des Konstruktors birgt normalerweise die Gefahr, dass die Sub-Klassen eine Referenz auf ein noch nicht vollständig
	// initialisiertes Objekt erhalten. Dennoch ist der Ansatz hier gewünscht und sicher, da in diesem synchronen Setup-Prozess die Sub-Repositories während
	// ihrer Erstellung keine Methoden aus diesem Repository aufrufen, sondern die Referenz lediglich in einem final-Feld abspeichern. Dies vermeidet
	// zustandsbehaftete (und damit fehleranfällige) init()-Methoden und ermöglicht komplett unveränderliche Repository-Objekte.
	public ReportingRepository(final DBEntityManager conn, final ReportingParameter reportingParameter, final Logger logger, final LogConsumerList log)
			throws ApiOperationException {

		// Initialisiere den Logger und das Log, sofern noch nicht erfolgt.
		if (logger == null) {
			this.logger = new Logger();
		} else {
			this.logger = logger;
		}

		if ((logger == null) || (log == null)) {
			this.log = new LogConsumerList();
			this.logger.addConsumer(this.log);
		} else {
			this.log = log;
		}

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
		this.reportingParameterTypisiert = new ReportingParameterTypisiert(this, reportingParameter);

		this.logger.logLn(LogLevel.DEBUG, 8, "Validierung der Datenbankverbindung und der Reporting-Parameter erfolgreich abgeschlossen.");

		// Erzeuge die Services für Sortierung und Filterung und der Domänen-Repositories.
		this.sortierungService = new ReportingSortierungService(this.reportingParameterTypisiert, this.logger);
		this.filterService = new ReportingFilterService(this.reportingParameterTypisiert, this.logger);
		this.logger.logLn(LogLevel.DEBUG, 8, "Services für Sortierung und Filterung erfolgreich erzeugt.");
		// WICHTIG: Während ihrer folgenden Initialisierung dürfen die Domänen-Repositories keine Methoden aus dieser Klasse aufrufen,
		// da dieses aufgrund seiner gerade erfolgenden Initialisierung noch null ist.
		this.repositorySchule = new ReportingRepositorySchule(this, this.conn, this.logger, reportingParameter.idSchuljahresabschnitt);
		this.logger.logLn(LogLevel.DEBUG, 8, "Schul-Repository erfolgreich erzeugt.");
		this.repositoryKataloge = new ReportingRepositoryKataloge(this.conn, this.logger);
		this.logger.logLn(LogLevel.DEBUG, 8, "Katalog-Repository erfolgreich erzeugt.");
		this.repositoryLehrer = new ReportingRepositoryLehrer(this, this.conn, this.logger, this.sortierungService);
		this.logger.logLn(LogLevel.DEBUG, 8, "Lehrer-Repository erfolgreich erzeugt.");
		this.repositorySchueler = new ReportingRepositorySchueler(this, this.conn, this.logger, this.sortierungService);
		this.logger.logLn(LogLevel.DEBUG, 8, "Schüler-Repository erfolgreich erzeugt.");
		this.repositoryLerngruppen = new ReportingRepositoryLerngruppen(this.repositorySchule, this.conn, this.logger);
		this.logger.logLn(LogLevel.DEBUG, 8, "Lerngruppen-Repository erfolgreich erzeugt.");
		this.repositoryStundenplan = new ReportingRepositoryStundenplan(this, this.conn, this.logger);
		this.logger.logLn(LogLevel.DEBUG, 8, "Stundenplan-Repository erfolgreich erzeugt.");
		this.repositoryGost = new ReportingRepositoryGost();
		this.logger.logLn(LogLevel.DEBUG, 8, "GOST-Repository erfolgreich erzeugt.");

		this.logger.logLn(LogLevel.DEBUG, 4, "<<< Ende der Erzeugung des Reporting-Repository");
	}


	// ##### Infrastruktur-Getter #####

	/**
	 * Gibt die Datenbankverbindung zurück.
	 *
	 * @return Die Datenbankverbindung.
	 */
	public DBEntityManager conn() {
		return conn;
	}

	/**
	 * Gibt die Log-Eintrags-Liste zurück, in der die Logger-Einträge gesammelt werden.
	 *
	 * @return Die Liste der gesammelten Log-Einträge.
	 */
	public LogConsumerList log() {
		return log;
	}

	/**
	 * Gibt den Logger zurück, der den Ablauf protokolliert und Fehlerdaten sammelt.
	 *
	 * @return Der Logger.
	 */
	public Logger logger() {
		return logger;
	}

	/**
	 * Gibt die typisierten Reporting-Parameter zurück, die die Report-Generierung steuern.
	 *
	 * @return Die typisierten Reporting-Parameter.
	 */
	public ReportingParameterTypisiert reportingParameter() {
		return reportingParameterTypisiert;
	}

	/**
	 * Gibt den Service für die Ermittlung von Sortierungsattributen zurück.
	 *
	 * @return Der ReportingSortierungService.
	 */
	public ReportingSortierungService sortierungService() {
		return sortierungService;
	}

	/**
	 * Gibt den Service für die Erstellung von Filtern zurück.
	 *
	 * @return Der ReportingFilterService.
	 */
	public ReportingFilterService filterService() {
		return filterService;
	}


	// ##### Domänen-Repository-Getter #####

	/**
	 * Gibt das Domänen-Repository für Schuldaten und Schuljahresabschnitte zurück.
	 *
	 * @return Das ReportingRepositorySchule.
	 */
	public ReportingRepositorySchule repositorySchule() {
		return repositorySchule;
	}

	/**
	 * Gibt das Domänen-Repository für Kataloge, Fächer, Jahrgänge und Erzieherarten zurück.
	 *
	 * @return Das ReportingRepositoryKataloge.
	 */
	public ReportingRepositoryKataloge repositoryKataloge() {
		return repositoryKataloge;
	}

	/**
	 * Gibt das Domänen-Repository für Lehrkräfte zurück.
	 *
	 * @return Das ReportingRepositoryLehrer.
	 */
	public ReportingRepositoryLehrer repositoryLehrer() {
		return repositoryLehrer;
	}

	/**
	 * Gibt das Domänen-Repository für Schülerdaten zurück.
	 *
	 * @return Das ReportingRepositorySchueler.
	 */
	public ReportingRepositorySchueler repositorySchueler() {
		return repositorySchueler;
	}

	/**
	 * Gibt das Domänen-Repository für Klassen und Kurse zurück.
	 *
	 * @return Das ReportingRepositoryLerngruppen.
	 */
	public ReportingRepositoryLerngruppen repositoryLerngruppen() {
		return repositoryLerngruppen;
	}

	/**
	 * Gibt das Domänen-Repository für Stundenpläne zurück.
	 *
	 * @return Das ReportingRepositoryStundenplan.
	 */
	public ReportingRepositoryStundenplan repositoryStundenplan() {
		return repositoryStundenplan;
	}

	/**
	 * Gibt das Domänen-Repository für GOSt-Daten zurück.
	 *
	 * @return Das ReportingRepositoryGost.
	 */
	public ReportingRepositoryGost repositoryGost() {
		return repositoryGost;
	}


	// ##### Delegationen an ReportingSchuleRepository #####

	/**
	 * Gibt die Stammdaten der Schule zurück.
	 *
	 * @return Die Stammdaten der Schule.
	 */
	public SchuleStammdaten schulstammdaten() {
		return repositorySchule.schulstammdaten();
	}

	/**
	 * Gibt alle Schuljahresabschnitte der Schule als sortierte Liste zurück.
	 *
	 * @return Alle Schuljahresabschnitte der Schule.
	 */
	public List<ReportingSchuljahresabschnitt> schuljahresabschnitte() {
		return repositorySchule.schuljahresabschnitte();
	}

	/**
	 * Gibt den Schuljahresabschnitt zur übergebenen ID zurück.
	 *
	 * @param id Die ID des angeforderten Schuljahresabschnitts.
	 *
	 * @return Der Schuljahresabschnitt zur ID.
	 */
	public ReportingSchuljahresabschnitt schuljahresabschnitt(final long id) {
		return repositorySchule.schuljahresabschnitt(id);
	}

	/**
	 * Gibt den Schuljahresabschnitt zum angegebenen Schuljahr und Abschnitt zurück.
	 *
	 * @param schuljahr Das Schuljahr.
	 * @param abschnitt Der Abschnitt.
	 *
	 * @return Der Schuljahresabschnitt zu den Angaben oder null, falls keiner existiert.
	 */
	public ReportingSchuljahresabschnitt schuljahresabschnitt(final int schuljahr, final int abschnitt) {
		return repositorySchule.schuljahresabschnitt(schuljahr, abschnitt);
	}

	/**
	 * Gibt den aktuellen Schuljahresabschnitt der Schule zurück.
	 *
	 * @return Der aktuelle Schuljahresabschnitt der Schule.
	 */
	public ReportingSchuljahresabschnitt aktuellerSchuljahresabschnitt() {
		return repositorySchule.aktuellerSchuljahresabschnitt();
	}

	/**
	 * Gibt den für den Druck ausgewählten Schuljahresabschnitt zurück.
	 *
	 * @return Der Schuljahresabschnitt der Auswahl für den Druck.
	 */
	public ReportingSchuljahresabschnitt auswahlSchuljahresabschnitt() {
		return repositorySchule.auswahlSchuljahresabschnitt();
	}

	/**
	 * Gibt die Map der Schuljahresabschnitte zurück, indiziert nach ID.
	 *
	 * @return Map der Schuljahresabschnitte.
	 */
	public Map<Long, ReportingSchuljahresabschnitt> mapSchuljahresabschnitte() {
		return repositorySchule.mapSchuljahresabschnitte();
	}


	// ##### Delegationen an ReportingKatalogRepository #####

	/**
	 * Gibt die Map der Entlassgründe zurück, indiziert nach ID.
	 *
	 * @return Map der Entlassgründe.
	 */
	public Map<Long, KatalogEntlassgrund> katalogEntlassgruende() {
		return repositoryKataloge.katalogEntlassgruende();
	}

	/**
	 * Gibt die Map der Förderschwerpunkt-Katalogeinträge zurück, indiziert nach ID.
	 *
	 * @return Map der Förderschwerpunkt-Katalogeinträge.
	 */
	public Map<Long, FoerderschwerpunktEintrag> katalogFoerderschwerpunkte() {
		return repositoryKataloge.katalogFoerderschwerpunkte();
	}

	/**
	 * Gibt die Map der Ort-Katalogeinträge zurück, indiziert nach ID.
	 *
	 * @return Map der Ort-Katalogeinträge.
	 */
	public Map<Long, OrtKatalogEintrag> katalogOrte() {
		return repositoryKataloge.katalogOrte();
	}

	/**
	 * Gibt die Map der Ortsteil-Katalogeinträge zurück, indiziert nach ID.
	 *
	 * @return Map der Ortsteil-Katalogeinträge.
	 */
	public Map<Long, OrtsteilKatalogEintrag> katalogOrtsteile() {
		return repositoryKataloge.katalogOrtsteile();
	}

	/**
	 * Gibt die Map der Religions-Katalogeinträge zurück, indiziert nach ID.
	 *
	 * @return Map der Religions-Katalogeinträge.
	 */
	public Map<Long, ReligionEintrag> katalogReligionen() {
		return repositoryKataloge.katalogReligionen();
	}

	/**
	 * Gibt die Map der Schul-Katalogeinträge zurück, indiziert nach ID.
	 *
	 * @return Map der Schul-Katalogeinträge.
	 */
	public Map<Long, SchulEintrag> katalogSchulen() {
		return repositoryKataloge.katalogSchulen();
	}

	/**
	 * Gibt die Map der Schulform-Katalogeinträge zurück, indiziert nach ID.
	 *
	 * @return Map der Schulform-Katalogeinträge.
	 */
	public Map<Long, SchulformKatalogEintrag> katalogSchulformen() {
		return repositoryKataloge.katalogSchulformen();
	}

	/**
	 * Gibt die Map der Telefonnummer-Arten zurück, indiziert nach ID.
	 *
	 * @return Map der Telefonnummer-Arten.
	 */
	public Map<Long, Telefonart> katalogTelefonnummerArten() {
		return repositoryKataloge.katalogTelefonnummerArten();
	}

	/**
	 * Gibt die Map der Fächer-DTOs zurück, indiziert nach der ID des Faches.
	 *
	 * @return Map der Fächer-DTOs.
	 */
	public Map<Long, DTOFach> mapFaecher() {
		return repositoryKataloge.mapFachdaten();
	}

	/**
	 * Gibt die Map der Jahrgangsdaten zurück, indiziert nach Jahrgangs-ID.
	 *
	 * @return Map der Jahrgangsdaten.
	 */
	public Map<Long, JahrgangsDaten> mapJahrgaenge() {
		return repositoryKataloge.mapJahrgaenge();
	}

	/**
	 * Gibt die Map der Erzieherarten als Reporting-Objekte zurück, indiziert nach ID.
	 *
	 * @return Map der Erzieherarten.
	 */
	public Map<Long, ReportingErzieherArt> mapReportingErzieherarten() {
		return repositoryKataloge.mapErzieherarten();
	}


	// ##### Delegationen an ReportingLehrerRepository #####

	/**
	 * Gibt das ReportingLehrer-Objekt zur übergebenen ID zurück.
	 *
	 * @param idLehrer Die ID des Lehrers.
	 *
	 * @return Das ReportingLehrer-Objekt oder null, falls die Lehrkraft nicht existiert.
	 */
	public ReportingLehrer lehrer(final long idLehrer) {
		return repositoryLehrer.lehrer(idLehrer);
	}

	/**
	 * Gibt eine sortierte Liste von ReportingLehrer-Objekten zu den übergebenen IDs zurück.
	 *
	 * @param idsLehrer Liste der Lehrer-IDs.
	 *
	 * @return Sortierte Liste von ReportingLehrer-Objekten.
	 */
	public List<ReportingLehrer> lehrer(final List<Long> idsLehrer) {
		return repositoryLehrer.lehrer(idsLehrer);
	}

	/**
	 * Gibt eine Liste von ReportingLehrer-Objekten zu den übergebenen IDs zurück, optional sortiert.
	 *
	 * @param idsLehrer     Liste der Lehrer-IDs.
	 * @param sortiereListe Gibt an, ob die definierte Sortierung angewendet werden soll.
	 *
	 * @return Liste von ReportingLehrer-Objekten.
	 */
	public List<ReportingLehrer> lehrer(final List<Long> idsLehrer, final boolean sortiereListe) {
		return repositoryLehrer.lehrer(idsLehrer, sortiereListe);
	}

	/**
	 * Gibt die Map der Lehrerstammdaten zurück, indiziert nach der ID des Lehrers.
	 *
	 * @return Map der Lehrerstammdaten.
	 */
	public Map<Long, LehrerStammdaten> mapLehrerStammdaten() {
		return repositoryLehrer.mapLehrerStammdaten();
	}


	// ##### Delegationen an ReportingSchuelerRepository #####

	/**
	 * Gibt das ReportingSchueler-Objekt zur übergebenen ID zurück.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Das ReportingSchueler-Objekt oder null, falls der Schüler nicht existiert.
	 */
	public ReportingSchueler schueler(final long idSchueler) {
		return repositorySchueler.schueler(idSchueler);
	}

	/**
	 * Gibt eine sortierte Liste von ReportingSchueler-Objekten zu den übergebenen IDs zurück.
	 *
	 * @param idsSchueler Liste der Schüler-IDs.
	 *
	 * @return Sortierte Liste von ReportingSchueler-Objekten.
	 */
	public List<ReportingSchueler> schueler(final List<Long> idsSchueler) {
		return repositorySchueler.schueler(idsSchueler);
	}

	/**
	 * Gibt eine Liste von ReportingSchueler-Objekten zu den übergebenen IDs zurück, optional sortiert.
	 *
	 * @param idsSchueler   Liste der Schüler-IDs.
	 * @param sortiereListe Gibt an, ob die definierte Sortierung angewendet werden soll.
	 *
	 * @return Liste von ReportingSchueler-Objekten.
	 */
	public List<ReportingSchueler> schueler(final List<Long> idsSchueler, final boolean sortiereListe) {
		return repositorySchueler.schueler(idsSchueler, sortiereListe);
	}

	/**
	 * Gibt die Map der Schülerstammdaten zurück, indiziert nach der ID des Schülers.
	 *
	 * @return Map der Schülerstammdaten.
	 */
	public Map<Long, SchuelerStammdaten> mapSchuelerStammdaten() {
		return repositorySchueler.mapSchuelerStammdaten();
	}

	/**
	 * Gibt die Map der Erzieherstammdaten zurück, indiziert nach Schüler-ID.
	 *
	 * @return Map der Erzieherstammdaten.
	 */
	public Map<Long, List<ErzieherStammdaten>> mapErzieherStammdaten() {
		return repositorySchueler.mapErzieherStammdaten();
	}

	/**
	 * Gibt die Map der Sprachbelegungen der Schüler zurück, indiziert nach Schüler-ID.
	 *
	 * @return Map der Sprachbelegungen.
	 */
	public Map<Long, List<Sprachbelegung>> mapSchuelerSprachbelegungen() {
		return repositorySchueler.mapSchuelerSprachbelegungen();
	}

	/**
	 * Gibt die Map der Schulbesuchsdaten der Schüler zurück, indiziert nach Schüler-ID.
	 *
	 * @return Map der Schulbesuchsdaten.
	 */
	public Map<Long, SchuelerSchulbesuchsdaten> mapSchuelerSchulbesuchsdaten() {
		return repositorySchueler.mapSchuelerSchulbesuchsdaten();
	}

	/**
	 * Gibt die dreidimensionale Map aller Leistungsdaten der Schüler zurück.
	 *
	 * @return Map der Leistungsdaten.
	 */
	public ListMap3DLongKeys<SchuelerLeistungsdaten> mapAlleLeistungsdaten() {
		return repositorySchueler.mapAlleLeistungsdaten();
	}

	/**
	 * Gibt die vierdimensionale Map aller Lernabschnittsdaten der Schüler zurück.
	 *
	 * @return Map der Lernabschnittsdaten.
	 */
	public ListMap4DLongKeys<SchuelerLernabschnittsdaten> mapAlleLernabschnittsdaten() {
		return repositorySchueler.mapAlleLernabschnittsdaten();
	}

	/**
	 * Gibt die Map der bereits erzeugten ReportingSchueler-Objekte zurück, indiziert nach Schüler-ID.
	 *
	 * @return Map der ReportingSchueler-Objekte.
	 */
	public Map<Long, ReportingSchueler> mapSchueler() {
		return repositorySchueler.mapSchueler();
	}

	/**
	 * Gibt die Map der Telefonkontakte der Schüler zurück, indiziert nach Schüler-ID.
	 *
	 * @return Map der Telefonkontakte.
	 */
	public Map<Long, List<ReportingSchuelerTelefonkontakt>> mapSchuelerTelefonkontakte() {
		return repositorySchueler.mapSchuelerTelefonkontakte();
	}

	/**
	 * Gibt die Map der Zuweisungen der Schüler zurück, indiziert nach Schüler-ID.
	 *
	 * @return Map der Zuweisungen.
	 */
	public Map<Long, List<ReportingSchuelerZuweisung>> mapSchuelerZuweisungen() {
		return repositorySchueler.mapSchuelerZuweisungen();
	}


	// ##### Delegationen an ReportingLerngruppenRepository #####

	/**
	 * Gibt das ReportingKlasse-Objekt zur übergebenen ID zurück.
	 *
	 * @param idKlasse Die ID der Klasse.
	 *
	 * @return Das ReportingKlasse-Objekt oder null, falls die Klasse nicht existiert.
	 */
	public ReportingKlasse klasse(final long idKlasse) {
		return repositoryLerngruppen.klasse(idKlasse);
	}

	/**
	 * Gibt eine sortierte Liste von ReportingKlasse-Objekten zu den übergebenen IDs zurück.
	 *
	 * @param idsKlassen Liste der Klassen-IDs.
	 *
	 * @return Sortierte Liste von ReportingKlasse-Objekten.
	 */
	public List<ReportingKlasse> klassen(final List<Long> idsKlassen) {
		return repositoryLerngruppen.klassen(idsKlassen);
	}

	/**
	 * Gibt die Map der bereits erzeugten ReportingKlasse-Objekte zurück, indiziert nach Klassen-ID.
	 *
	 * @return Map der Klassen.
	 */
	public Map<Long, ReportingKlasse> mapKlassen() {
		return repositoryLerngruppen.mapKlassen();
	}

	/**
	 * Gibt die Map der bereits erzeugten ReportingKurs-Objekte zurück, indiziert nach Kurs-ID.
	 *
	 * @return Map der Kurse.
	 */
	public Map<Long, ReportingKurs> mapKurse() {
		return repositoryLerngruppen.mapKurse();
	}


	// ##### Delegationen an ReportingStundenplanRepository #####

	/**
	 * Gibt den Stundenplan zurück, der am übergebenen Datum gültig ist.
	 *
	 * @param datum Das Datum im Format yyyy-mm-dd.
	 *
	 * @return Der Stundenplan zum Datum oder null, falls keiner existiert.
	 */
	public ReportingStundenplanungStundenplan stundenplan(final String datum) {
		return repositoryStundenplan.stundenplan(datum);
	}

	/**
	 * Gibt den Stundenplan zur übergebenen ID zurück.
	 *
	 * @param idStundenplan Die ID des Stundenplans.
	 *
	 * @return Der Stundenplan zur ID oder null, falls keiner existiert.
	 */
	public ReportingStundenplanungStundenplan stundenplan(final long idStundenplan) {
		return repositoryStundenplan.stundenplan(idStundenplan);
	}

	/**
	 * Gibt den StundenplanManager zur übergebenen ID zurück. Fehlt der Eintrag im Cache, wird er aus der Datenbank erzeugt.
	 *
	 * @param idStundenplan Die ID des Stundenplans.
	 *
	 * @return Der StundenplanManager zur ID oder null, falls der Stundenplan nicht existiert.
	 */
	public StundenplanManager stundenplanManager(final long idStundenplan) {
		return repositoryStundenplan.stundenplanManager(idStundenplan);
	}

	/**
	 * Gibt die Liste aller aktiven Stundenplandefinitionen zurück, absteigend nach Gültigkeitsbeginn sortiert.
	 *
	 * @return Liste aller Stundenplandefinitionen.
	 */
	public List<StundenplanListeEintrag> stundenplandefinitionen() {
		return repositoryStundenplan.stundenplandefinitionen();
	}


	// ##### Delegationen an ReportingGostRepository #####

	/**
	 * Gibt die Map der Jahrgangsdaten zu den Abiturjahrgängen zurück, indiziert nach dem Abiturjahrgang.
	 *
	 * @return Map der Daten zu den Abiturjahrgängen.
	 */
	public Map<Integer, GostJahrgangsdaten> mapGostAbiturjahrgangDaten() {
		return repositoryGost.mapGostAbiturjahrgangDaten();
	}

	/**
	 * Gibt die Map der Fächermanager zu den Abiturjahrgängen zurück, indiziert nach dem Abiturjahrgang.
	 *
	 * @return Map der Fächermanager zu den Abiturjahrgängen.
	 */
	public Map<Integer, GostFaecherManager> mapGostAbiturjahrgangFaecher() {
		return repositoryGost.mapGostAbiturjahrgangFaecher();
	}

	/**
	 * Gibt die Map der GOSt-Laufbahnberatungsdaten zurück, indiziert nach Schüler-ID.
	 *
	 * @return Map mit GOSt-Beratungsdaten der Schüler.
	 */
	public Map<Long, GostLaufbahnplanungBeratungsdaten> mapGostBeratungsdaten() {
		return repositoryGost.mapGostBeratungsdaten();
	}

	/**
	 * Gibt die Map der Abiturdaten zurück, die im Rahmen der Laufbahnberatung ermittelt wurden, indiziert nach Schüler-ID.
	 *
	 * @return Map mit GOSt-Beratungsdaten-Abiturdaten.
	 */
	public Map<Long, Abiturdaten> mapGostBeratungsdatenAbiturdaten() {
		return repositoryGost.mapGostBeratungsdatenAbiturdaten();
	}

	/**
	 * Gibt die Map der GOSt-Abiturdaten der Schüler zurück, indiziert nach Schüler-ID.
	 *
	 * @return Map mit GOSt-Abiturdaten der Schüler.
	 */
	public Map<Long, Abiturdaten> mapGostSchuelerAbiturdaten() {
		return repositoryGost.mapGostSchuelerAbiturdaten();
	}

	/**
	 * Gibt die Map der aktuell geladenen Kursplanungs-Kurse zurück, indiziert nach Kurs-ID.
	 *
	 * @return Map der aktuell geladenen Kursplanung-Kurse.
	 */
	public Map<Long, ReportingGostKursplanungKurs> mapGostKursplanungKurse() {
		return repositoryGost.mapGostKursplanungKurse();
	}


	// ##### Delegationen an die ausgelagerten Services #####

	/**
	 * Delegiert an {@link ReportingSortierungService#getSortierungsAttribute(String, boolean)}.
	 *
	 * @param typ                                Der Name des Typs (z. B. "ReportingSchueler").
	 * @param nutzeStandardsortierungAlsFallback Gibt an, ob die Standardsortierung als Fallback geladen werden soll.
	 *
	 * @return Eine Liste der bereinigten Attributnamen.
	 */
	public List<String> getSortierungsAttribute(final String typ, final boolean nutzeStandardsortierungAlsFallback) {
		return this.sortierungService.getSortierungsAttribute(typ, nutzeStandardsortierungAlsFallback);
	}

	/**
	 * Delegiert an {@link ReportingFilterService#getFilter(String, List)}.
	 *
	 * @param <T>                Der Typ der zu filternden Objekte.
	 * @param typ                Der Name des Typs (z. B. "ReportingFach").
	 * @param validierungsfehler Eine Liste, in der unbekannte Attribute gesammelt werden (darf null sein).
	 *
	 * @return Ein {@link Predicate}, das die Filterkriterien anwendet.
	 */
	public <T> Predicate<T> getFilter(final String typ, final List<String> validierungsfehler) {
		return this.filterService.getFilter(typ, validierungsfehler);
	}
}
