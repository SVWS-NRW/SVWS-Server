package de.svws_nrw.module.reporting.repositories;

import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.filterung.ReportingFilterService;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierungService;
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

		// Erzeuge die Services für Sortierung und Filterung und die Domänen-Repositories.
		this.sortierungService = new ReportingSortierungService(this.reportingParameterTypisiert, this.logger);
		this.filterService = new ReportingFilterService(this.reportingParameterTypisiert, this.logger);
		this.logger.logLn(LogLevel.DEBUG, 8, "Services für Sortierung und Filterung erfolgreich erzeugt.");
		// WICHTIG: Während ihrer folgenden Initialisierung dürfen die Domänen-Repositories nur auf die
		// Infrastruktur-Getter (conn(), logger(), sortierungService(), filterService()) zugreifen,
		// da die Domänen-Repository-Felder zu diesem Zeitpunkt noch nicht gesetzt sind.
		this.repositorySchule = new ReportingRepositorySchule(this, reportingParameter.idSchuljahresabschnitt);
		this.logger.logLn(LogLevel.DEBUG, 8, "Schul-Repository erfolgreich erzeugt.");
		this.repositoryKataloge = new ReportingRepositoryKataloge(this);
		this.logger.logLn(LogLevel.DEBUG, 8, "Katalog-Repository erfolgreich erzeugt.");
		this.repositoryLehrer = new ReportingRepositoryLehrer(this);
		this.logger.logLn(LogLevel.DEBUG, 8, "Lehrer-Repository erfolgreich erzeugt.");
		this.repositorySchueler = new ReportingRepositorySchueler(this);
		this.logger.logLn(LogLevel.DEBUG, 8, "Schüler-Repository erfolgreich erzeugt.");
		this.repositoryLerngruppen = new ReportingRepositoryLerngruppen(this);
		this.logger.logLn(LogLevel.DEBUG, 8, "Lerngruppen-Repository erfolgreich erzeugt.");
		this.repositoryStundenplan = new ReportingRepositoryStundenplan(this);
		this.logger.logLn(LogLevel.DEBUG, 8, "Stundenplan-Repository erfolgreich erzeugt.");
		this.repositoryGost = new ReportingRepositoryGost(this);
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

}
