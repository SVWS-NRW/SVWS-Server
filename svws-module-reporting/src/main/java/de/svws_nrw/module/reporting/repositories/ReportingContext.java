package de.svws_nrw.module.reporting.repositories;

import java.util.List;

import de.svws_nrw.base.email.EmailJobManager;
import de.svws_nrw.base.email.EmailJobManagerFactory;
import de.svws_nrw.core.data.benutzer.BenutzerEMailDaten;
import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.data.benutzer.DataBenutzerEMailDaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.views.benutzer.DTOViewBenutzerdetails;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.filterung.ReportingFilterService;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.diagnose.ReportingProblem;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSammler;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierungService;
import de.svws_nrw.module.reporting.types.schule.ProxyReportingBenutzer;
import de.svws_nrw.module.reporting.utils.ReportingServerUtils;
import jakarta.ws.rs.core.Response.Status;

/**
 * Zentraler Kontext-Container des Reporting-Moduls, der Infrastruktur (Datenbankverbindung, Logger, Services) bereitstellt
 * und Zugriff auf die domänenspezifischen Sub-Repositories gewährt.
 */
public class ReportingContext {

	/** Die Verbindung zur Datenbank. */
	private final DBEntityManager conn;

	/** Der Modus, in dem der Server betrieben wird (STABLE, BETA, ALPHA, DEV). */
	private final ServerMode serverMode;

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

	/** Sammler der hingenommenen Ausgabeprobleme dieses Aufrufs. */
	private final ReportingProblemSammler problemSammler;

	/** Objekt zum aktuell angemeldeten Benutzer. */
	private final ProxyReportingBenutzer benutzer;


	// Domänen-Repositories
	private final ReportingRepositorySchule repositorySchule;
	private final ReportingRepositoryKataloge repositoryKataloge;
	private final ReportingRepositoryLehrer repositoryLehrer;
	private final ReportingRepositorySchueler repositorySchueler;
	private final ReportingRepositoryLerngruppen repositoryLerngruppen;
	private final ReportingRepositoryStundenplan repositoryStundenplan;
	private final ReportingRepositoryGost repositoryGost;
	private final ReportingRepositoryGostKlausurplanung repositoryGostKlausurplanung;
	private final ReportingRepositoryGostKursplanung repositoryGostKursplanung;


	/**
	 * Erstellt den Kontext für häufig genutzte Daten aus der Schuldatenbank, um Zugriffe darauf zu minimieren. Ebenso werden einzelne
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
	// ihrer Erstellung ausschließlich Methoden aufrufen, deren Felder zu diesem Zeitpunkt bereits gesetzt sind - die Infrastruktur-Getter und
	// meldeAusgabeproblem(); im Übrigen speichern sie die Referenz lediglich in einem final-Feld ab. Dies vermeidet zustandsbehaftete (und damit
	// fehleranfällige) init()-Methoden und ermöglicht komplett unveränderliche Kontext-Objekte.
	public ReportingContext(final DBEntityManager conn, final ReportingParameter reportingParameter, final Logger logger, final LogConsumerList log)
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

		this.logger.logLn(LogLevel.DEBUG, 4, ">>> Beginn der Erzeugung des Reporting-Context");

		// Die Validierung von conn und reportingParameter erfolgt in der ReportingFactory.
		this.conn = conn;
		this.serverMode = ReportingServerUtils.servermode();
		this.reportingParameterTypisiert = new ReportingParameterTypisiert(this, reportingParameter);

		// Erzeuge die Services für Sortierung und Filterung und die Domänen-Repositories.
		this.sortierungService = new ReportingSortierungService(this.reportingParameterTypisiert, this.logger);
		this.filterService = new ReportingFilterService(this.reportingParameterTypisiert, this.logger);
		this.logger.logLn(LogLevel.DEBUG, 8, "Services für Sortierung und Filterung erfolgreich erzeugt.");
		// Der Sammler entsteht vor den Domänen-Repositories: Bereits deren Initialisierung kann ein Ausgabeproblem melden.
		this.problemSammler = new ReportingProblemSammler(this.logger);
		// WICHTIG: Während ihrer folgenden Initialisierung dürfen die Domänen-Repositories nur auf die
		// Infrastruktur-Getter (conn(), logger(), sortierungService(), filterService()) und auf
		// meldeAusgabeproblem() zugreifen, da die Domänen-Repository-Felder zu diesem Zeitpunkt noch
		// nicht gesetzt sind. Der Problemsammler steht dafür bereits oben bereit.
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
		this.repositoryGostKlausurplanung = new ReportingRepositoryGostKlausurplanung(this);
		this.logger.logLn(LogLevel.DEBUG, 8, "GOSt-Klausurplanung-Repository erfolgreich erzeugt.");
		this.repositoryGostKursplanung = new ReportingRepositoryGostKursplanung(this);
		this.logger.logLn(LogLevel.DEBUG, 8, "GOSt-Kursplanung-Repository erfolgreich erzeugt.");

		// Erzeuge das Benutzer-Objekt für den aktuell angemeldeten Benutzer.
		this.benutzer = initBenutzer();

		this.logger.logLn(LogLevel.DEBUG, 4, "<<< Ende der Erzeugung des Reporting-Context");
	}


	/**
	 * Initialisiert das Benutzer-Objekt für den aktuell angemeldeten Benutzer.
	 *
	 * @return Das Benutzer-Objekt für den aktuell angemeldeten Benutzer.
	 */
	private ProxyReportingBenutzer initBenutzer() {
		this.logger.logLn(LogLevel.DEBUG, 8,
				"Ermittle die Informationen für den aktuell angemeldeten Benutzer (ID %d).".formatted(conn.getUser().getId()));
		DTOViewBenutzerdetails benutzerdetails;
		try {
			benutzerdetails = conn.queryByKey(DTOViewBenutzerdetails.class, conn.getUser().getId());
		} catch (@SuppressWarnings("unused") final Exception ignore) {
			benutzerdetails = null;
		}
		BenutzerEMailDaten benutzerEMailDaten;
		try {
			benutzerEMailDaten = new DataBenutzerEMailDaten(conn).getById(conn.getUser().getId());
		} catch (@SuppressWarnings("unused") final Exception ignore) {
			benutzerEMailDaten = null;
		}

		final EmailJobManager emailJobManagerBenutzer =
				EmailJobManagerFactory.getInstance().getManagerByUser(conn.getDBSchema(), conn.getUser().getId());

		this.logger.logLn(LogLevel.DEBUG, 8, "Ermittelte Informationen: Benutzerdetails %s, E-Mail-Daten %s, E-Mail-Manager %s."
				.formatted((benutzerdetails != null) ? "ja" : "nein", (benutzerEMailDaten != null) ? "ja" : "nein",
						(emailJobManagerBenutzer != null) ? "ja" : "nein"));

		return new ProxyReportingBenutzer(this, this.conn.getUser(), benutzerdetails, benutzerEMailDaten, emailJobManagerBenutzer);
	}


	// ##### Infrastruktur-Getter #####

	/**
	 * Gibt die Datenbankverbindung zurück.
	 * Sie ist bewusst package-private: Außerhalb des {@code repositories}-Pakets darf die Verbindung
	 * nicht direkt verwendet werden; Datenzugriffe gehören in die Domänen-Repositories.
	 *
	 * @return Die Datenbankverbindung.
	 */
	DBEntityManager conn() {
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

	/**
	 * Gibt den Modus zurück, in dem der Server betrieben wird.
	 *
	 * @return Der ServerMode.
	 */
	public ServerMode serverMode() {
		return serverMode;
	}


	// ##### Ausgabeprobleme #####

	/**
	 * Meldet ein Ausgabeproblem und ist der einzige Weg dorthin: Der Befund wird registriert und einmal je Ursache, Auswirkung und Schlüssel protokolliert,
	 * höchstens mit {@code WARNING} - ein {@code ERROR} ist dem Abbruch vorbehalten. Eine abbrechende Ursache wird dagegen nicht gesammelt, sondern
	 * als Serverfehler geworfen; den Logeintrag dazu schreibt die Abschlussgrenze.
	 *
	 * @param ursache      Woran es liegt; eine abbrechende Ursache führt zum Wurf.
	 * @param auswirkung   Was daraus in der Ausgabe folgt.
	 * @param schluessel   Welches Objekt betroffen ist, aus Objektart und ID.
	 * @param beschreibung Der Sachverhalt für das Log.
	 * @param fehler       Der auslösende Fehler oder {@code null}. Ein {@link Error} gehört nicht hierher: Ein nicht mehr arbeitsfähiges Laufzeitsystem wird
	 *                     nicht hingenommen.
	 *
	 * @throws ApiOperationException Mit dem Status 500, wenn die Ursache die Ausgabe abbricht.
	 */
	public void meldeAusgabeproblem(final ReportingProblemursache ursache, final ReportingProblemauswirkung auswirkung,
			final ReportingProblemSchluessel schluessel, final String beschreibung, final Exception fehler) {
		if (ursache.istAbbruch()) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, fehler,
					"Die Ausgabe wird abgebrochen: %s [%s]".formatted((beschreibung == null) ? "" : beschreibung, schluessel.beschreibung()));
		}
		this.problemSammler.melde(ursache, auswirkung, schluessel, beschreibung, fehler);
	}

	/**
	 * Gibt die Anzahl der bislang gemeldeten Ausgabeprobleme zurück. Mehrfach gemeldete Probleme zählen einmal. Die Zahl ist eine interne Angabe über die
	 * Kombinationen aus Ursache, Auswirkung und Schlüssel; die Zählung im öffentlichen Hinweisvertrag folgt dessen Kategorien.
	 *
	 * @return Die Anzahl.
	 */
	public int anzahlAusgabeprobleme() {
		return this.problemSammler.anzahl();
	}

	/**
	 * Gibt die bislang gemeldeten Ausgabeprobleme in der Reihenfolge ihres ersten Auftretens zurück. Die Rückgabe ist unveränderlich, denn gemeldet wird
	 * ausschließlich über {@link #meldeAusgabeproblem}.
	 *
	 * @return Die unveränderliche Liste der Probleme.
	 */
	public List<ReportingProblem> ausgabeprobleme() {
		return this.problemSammler.probleme();
	}


	// ##### Identität des Requests #####

	/**
	 * Gibt den aktuell angemeldeten Benutzer der Datenbankverbindung zurück.
	 *
	 * @return Der angemeldete Benutzer.
	 */
	public ProxyReportingBenutzer benutzer() {
		return benutzer;
	}

	/**
	 * Gibt den Namen des Datenbank-Schemas der aktuellen Verbindung zurück.
	 *
	 * @return Der Name des Datenbank-Schemas.
	 */
	public String schemaName() {
		return conn.getDBSchema();
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

	/**
	 * Gibt das Domänen-Repository für die GOSt-Klausurplanung zurück.
	 *
	 * @return Das ReportingRepositoryGostKlausurplanung.
	 */
	public ReportingRepositoryGostKlausurplanung repositoryGostKlausurplanung() {
		return repositoryGostKlausurplanung;
	}

	/**
	 * Gibt das Domänen-Repository für die GOSt-Kursplanung zurück.
	 *
	 * @return Das ReportingRepositoryGostKursplanung.
	 */
	public ReportingRepositoryGostKursplanung repositoryGostKursplanung() {
		return repositoryGostKursplanung;
	}

}
