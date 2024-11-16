package de.svws_nrw.module.reporting.proxytypes.schueler.lernabschnitte;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.klassen.KlassenDaten;
import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.core.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.data.jahrgaenge.DataJahrgangsdaten;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.data.lehrer.DataLehrerStammdaten;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import de.svws_nrw.module.reporting.proxytypes.jahrgang.ProxyReportingJahrgang;
import de.svws_nrw.module.reporting.proxytypes.lehrer.ProxyReportingLehrer;
import de.svws_nrw.module.reporting.proxytypes.schueler.ProxyReportingSchueler;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.klasse.ReportingKlasse;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLernabschnitt;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Lernabschnitt und erweitert die Klasse {@link ReportingSchuelerLernabschnitt}.
 */
public class ProxyReportingSchuelerLernabschnitt extends ReportingSchuelerLernabschnitt {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingSchuelerLernabschnitt}.
	 *
	 * @param reportingRepository Repository für das Reporting.
	 * @param schuelerLernabschnittsdaten Stammdaten-Objekt aus der DB.
	 */
	public ProxyReportingSchuelerLernabschnitt(final ReportingRepository reportingRepository, final SchuelerLernabschnittsdaten schuelerLernabschnittsdaten) {
		super(schuelerLernabschnittsdaten.abschluss,
				schuelerLernabschnittsdaten.abschlussart,
				schuelerLernabschnittsdaten.abschlussBerufsbildend,
				schuelerLernabschnittsdaten.anzahlSchulbesuchsjahre,
				schuelerLernabschnittsdaten.bilingualerZweig,
				schuelerLernabschnittsdaten.datumAnfang,
				schuelerLernabschnittsdaten.datumEnde,
				schuelerLernabschnittsdaten.datumKonferenz,
				schuelerLernabschnittsdaten.datumZeugnis,
				schuelerLernabschnittsdaten.fehlstundenGesamt,
				schuelerLernabschnittsdaten.fehlstundenGrenzwert,
				schuelerLernabschnittsdaten.fehlstundenUnentschuldigt,
				null,
				null,
				schuelerLernabschnittsdaten.bemerkungen.foerderschwerpunkt,
				null,
				schuelerLernabschnittsdaten.hatAOSF,
				schuelerLernabschnittsdaten.hatAutismus,
				schuelerLernabschnittsdaten.hatSchwerbehinderungsNachweis,
				schuelerLernabschnittsdaten.hatZieldifferentenUnterricht,
				null,
				schuelerLernabschnittsdaten.id,
				schuelerLernabschnittsdaten.fachklasseID,
				schuelerLernabschnittsdaten.foerderschwerpunkt1ID,
				schuelerLernabschnittsdaten.foerderschwerpunkt2ID,
				schuelerLernabschnittsdaten.folgeklassenID,
				schuelerLernabschnittsdaten.jahrgangID,
				schuelerLernabschnittsdaten.klassenID,
				schuelerLernabschnittsdaten.schuelerID,
				schuelerLernabschnittsdaten.schuljahresabschnitt,
				schuelerLernabschnittsdaten.schwerpunktID,
				schuelerLernabschnittsdaten.sonderpaedagogeID,
				schuelerLernabschnittsdaten.tutorID,
				schuelerLernabschnittsdaten.istAbschlussPrognose,
				schuelerLernabschnittsdaten.istFachpraktischerAnteilAusreichend,
				schuelerLernabschnittsdaten.istGewertet,
				schuelerLernabschnittsdaten.istWiederholung,
				null,
				schuelerLernabschnittsdaten.Klassenart,
				null,
				null,
				schuelerLernabschnittsdaten.noteDurchschnitt,
				schuelerLernabschnittsdaten.noteLernbereichGSbzwAL,
				schuelerLernabschnittsdaten.noteLernbereichNW,
				schuelerLernabschnittsdaten.organisationsform,
				schuelerLernabschnittsdaten.pruefungsOrdnung,
				null,
				schuelerLernabschnittsdaten.schulgliederung,
				null,
				null,
				schuelerLernabschnittsdaten.textErgebnisPruefungsalgorithmus,
				null,
				schuelerLernabschnittsdaten.bemerkungen.uebergangESF,
				schuelerLernabschnittsdaten.bemerkungen.versetzungsentscheidung,
				schuelerLernabschnittsdaten.versetzungsvermerk,
				schuelerLernabschnittsdaten.wechselNr,
				schuelerLernabschnittsdaten.zeugnisart,
				schuelerLernabschnittsdaten.bemerkungen.zeugnisASV,
				schuelerLernabschnittsdaten.bemerkungen.zeugnisAUE,
				schuelerLernabschnittsdaten.bemerkungen.zeugnisAllgemein,
				schuelerLernabschnittsdaten.bemerkungen.zeugnisLELS);

		this.reportingRepository = reportingRepository;
		super.foerderschwerpunkt1 = this.reportingRepository.katalogFoerderschwerpunkte().get(schuelerLernabschnittsdaten.foerderschwerpunkt1ID);
		super.foerderschwerpunkt2 = this.reportingRepository.katalogFoerderschwerpunkte().get(schuelerLernabschnittsdaten.foerderschwerpunkt2ID);

		ersetzeStringNullDurchEmpty(this, false);
	}


	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	public ReportingRepository reportingRepository() {
		return reportingRepository;
	}


	/**
	 * Stellt die Daten der Folgeklasse des Schülers zur Verfügung, dem dieser Lernabschnitt gehört.
	 *
	 * @return Daten der Folgeklasse
	 */
	@Override
	public ReportingKlasse folgeklasse() {
		if ((super.folgeklasse() == null) && (super.idFolgeklasse() != null) && (super.idFolgeklasse() >= 0)) {
			if (!this.reportingRepository.mapKlassen().containsKey(super.idFolgeklasse())) {
				// ID der Folgeklasse ist bekannt, aber sie wurde noch nicht aus der DB geladen. Lade deren Daten und lade dann alle Klassen des Lernabschnitts.
				final KlassenDaten klassenDaten;
				try {
					klassenDaten = new DataKlassendaten(reportingRepository.conn()).getByIdOhneSchueler(super.idFolgeklasse());
				} catch (final ApiOperationException e) {
					ReportingExceptionUtils.putStacktraceInLog(
							"FEHLER: Fehler bei der Ermittlung der Daten für die Folgeklasse des Schülers %s in %s."
									.formatted(super.schueler().id(), super.schuljahresabschnitt.textSchuljahresabschnittKurz()),
							e, reportingRepository.logger(), LogLevel.ERROR, 0);
					return super.folgeklasse();
				}
				super.folgeklasse = this.reportingRepository.schuljahresabschnitt(klassenDaten.idSchuljahresabschnitt).klasse(super.idFolgeklasse());
			} else {
				// ID der Folgeklasse ist bekannt und die Klasse wurde in einem Lernabschnitt bereits erzeugt, hole sie aus Lernabschnitt.
				super.folgeklasse = this.reportingRepository.mapKlassen().get(super.idFolgeklasse()).schuljahresabschnitt().klasse(super.idFolgeklasse());
			}
		}
		return super.folgeklasse();
	}

	/**
	 * Stellt die Daten des Jahrgangs des Schülers zur Verfügung, dem dieser Lernabschnitt gehört.
	 *
	 * @return Daten des Jahrgangs
	 */
	@Override
	public ReportingJahrgang jahrgang() {
		if ((super.jahrgang() == null) && (super.idJahrgang() != null) && (super.idJahrgang() >= 0)) {
			super.jahrgang =
					new ProxyReportingJahrgang(
							reportingRepository,
							reportingRepository.mapJahrgaenge().computeIfAbsent(super.idJahrgang(), k -> {
								try {
									return new DataJahrgangsdaten(reportingRepository.conn()).getFromID(super.idJahrgang());
								} catch (final ApiOperationException e) {
									ReportingExceptionUtils.putStacktraceInLog(
											"INFO: Fehler mit definiertem Rückgabewert abgefangen bei der Bestimmung der Daten eines Jahrgangs.", e,
											reportingRepository.logger(), LogLevel.INFO, 0);
									return new JahrgangsDaten();
								}
							}), this.schuljahresabschnitt());
		}
		return super.jahrgang();
	}

	/**
	 * Stellt die Daten der Klasse des Schülers zur Verfügung, dem dieser Lernabschnitt gehört.
	 *
	 * @return Daten der Klasse
	 */
	@Override
	public ReportingKlasse klasse() {
		if ((super.klasse() == null) && (super.idKlasse() != null) && (super.idKlasse() >= 0)) {
			if (!this.reportingRepository.mapKlassen().containsKey(super.idKlasse())) {
				// ID der Klasse ist bekannt, aber sie wurde noch nicht aus der DB geladen. Lade deren Daten und lade dann alle Klassen des Lernabschnitts.
				final KlassenDaten klassenDaten;
				try {
					klassenDaten = new DataKlassendaten(reportingRepository.conn()).getByIdOhneSchueler(super.idKlasse());
				} catch (final ApiOperationException e) {
					ReportingExceptionUtils.putStacktraceInLog(
							"FEHLER: Fehler bei der Ermittlung der Daten für die Klasse des Schülers %s in %s."
									.formatted(super.schueler().id(), super.schuljahresabschnitt.textSchuljahresabschnittKurz()),
							e, reportingRepository.logger(), LogLevel.ERROR, 0);
					return super.klasse();
				}
				super.klasse = this.reportingRepository.schuljahresabschnitt(klassenDaten.idSchuljahresabschnitt).klasse(super.idKlasse());
			} else {
				// ID der Klasse ist bekannt und die Klasse wurde in einem Lernabschnitt bereits erzeugt, hole sie aus Lernabschnitt.
				super.klasse = this.reportingRepository.mapKlassen().get(super.idKlasse()).schuljahresabschnitt().klasse(super.idKlasse());
			}
		}
		return super.klasse();
	}

	// TODO Klasse für die Leistungsdaten für die Reporting erzeugen und dann die Daten im überschriebenen Getter hier dynamisch nachladen.

	// TODO Klasse für die Nachprüfungen für die Reporting erzeugen und dann die Daten im überschriebenen Getter hier dynamisch nachladen.

	/**
	 * Stellt die Daten des Schülers zur Verfügung, dem dieser Lernabschnitt gehört.
	 *
	 * @return Daten des Schülers
	 */
	@Override
	public ReportingSchueler schueler() {
		if ((super.schueler() == null) && (super.idSchueler() >= 0)) {
			super.schueler =
					new ProxyReportingSchueler(
							reportingRepository,
							reportingRepository.mapSchuelerStammdaten().computeIfAbsent(super.idSchueler(), l -> {
								try {
									return new DataSchuelerStammdaten(reportingRepository.conn()).getStammdaten(reportingRepository.conn(), super.idSchueler());
								} catch (final ApiOperationException e) {
									ReportingExceptionUtils.putStacktraceInLog(
											"INFO: Fehler mit definiertem Rückgabewert abgefangen bei der Bestimmung der Stammdaten eines Schülers.", e,
											reportingRepository.logger(), LogLevel.INFO, 0);
									return new SchuelerStammdaten();
								}
							}));
		}
		return super.schueler();
	}

	/**
	 * Stellt die Daten des Schuljahresabschnitts zur Verfügung, in dem dieser Lernabschnitt liegt.
	 *
	 * @return Daten des Schuljahresabschnitts
	 */
	@Override
	public ReportingSchuljahresabschnitt schuljahresabschnitt() {
		if ((super.schuljahresabschnitt() == null) && (super.idSchuljahresabschnitt() >= 0)) {
			super.schuljahresabschnitt = this.reportingRepository.schuljahresabschnitt(super.idSchuljahresabschnitt());
		}
		return super.schuljahresabschnitt();
	}


	/**
	 * Stellt die Daten des Sonderpädagogen zur Verfügung, der in diesem Lernabschnitt dem Schüler zugeordnet ist.
	 *
	 * @return Daten des Sonderpädagogen
	 */
	@Override
	public ReportingLehrer sonderpaedagoge() {
		if ((super.sonderpaedagoge() == null) && (super.idSonderpaedagoge() != null) && (super.idSonderpaedagoge() >= 0)) {
			super.sonderpaedagoge =
					new ProxyReportingLehrer(
							reportingRepository,
							reportingRepository.mapLehrerStammdaten().computeIfAbsent(super.idSonderpaedagoge(), l -> {
								try {
									return new DataLehrerStammdaten(reportingRepository.conn()).getFromID(super.idSonderpaedagoge());
								} catch (final ApiOperationException e) {
									ReportingExceptionUtils.putStacktraceInLog(
											"INFO: Fehler mit definiertem Rückgabewert abgefangen bei der Bestimmung der Stammdaten eines Lehrers.", e,
											reportingRepository.logger(), LogLevel.INFO, 0);
									return new LehrerStammdaten();
								}
							}));
		}
		return super.sonderpaedagoge();
	}

	/**
	 * Stellt die Daten des Tutors zur Verfügung, der in diesem Lernabschnitt dem Schüler zugeordnet ist.
	 *
	 * @return Daten des Tutors
	 */
	@Override
	public ReportingLehrer tutor() {
		if ((super.tutor() == null) && (super.idTutor() >= 0)) {
			super.tutor =
					new ProxyReportingLehrer(
							reportingRepository,
							reportingRepository.mapLehrerStammdaten().computeIfAbsent(super.idTutor(), l -> {
								try {
									return new DataLehrerStammdaten(reportingRepository.conn()).getFromID(super.idTutor());
								} catch (final ApiOperationException e) {
									ReportingExceptionUtils.putStacktraceInLog(
											"INFO: Fehler mit definiertem Rückgabewert abgefangen bei der Bestimmung der Stammdaten eines Lehrers.", e,
											reportingRepository.logger(), LogLevel.INFO, 0);
									return new LehrerStammdaten();
								}
							}));
		}
		return super.tutor();
	}
}
