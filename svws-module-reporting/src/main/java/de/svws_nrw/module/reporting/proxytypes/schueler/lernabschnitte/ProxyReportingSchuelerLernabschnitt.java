package de.svws_nrw.module.reporting.proxytypes.schueler.lernabschnitte;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.data.schueler.SchuelerLeistungsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.data.lehrer.DataLehrerStammdaten;

import de.svws_nrw.data.schueler.DataSchuelerLeistungsdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLeistungsdaten;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import de.svws_nrw.module.reporting.proxytypes.lehrer.ProxyReportingLehrer;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.klasse.ReportingKlasse;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLernabschnitt;

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
		super(ersetzeNullBlankTrim(schuelerLernabschnittsdaten.abschluss),
				schuelerLernabschnittsdaten.abschlussart,
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.abschlussBerufsbildend),
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.bilingualerZweig),
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.datumAnfang),
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.datumEnde),
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.datumKonferenz),
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.datumZeugnis),
				schuelerLernabschnittsdaten.fehlstundenGesamt,
				schuelerLernabschnittsdaten.fehlstundenGrenzwert,
				schuelerLernabschnittsdaten.fehlstundenUnentschuldigt,
				null,
				null,
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.bemerkungen.foerderschwerpunkt),
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
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.Klassenart),
				new ArrayList<>(),
				null,
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.noteDurchschnitt),
				schuelerLernabschnittsdaten.noteLernbereichGSbzwAL,
				schuelerLernabschnittsdaten.noteLernbereichNW,
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.organisationsform),
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.pruefungsOrdnung),
				null,
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.schulgliederung),
				null,
				null,
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.textErgebnisPruefungsalgorithmus),
				null,
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.bemerkungen.uebergangESF),
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.bemerkungen.versetzungsentscheidung),
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.versetzungsvermerk),
				schuelerLernabschnittsdaten.wechselNr,
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.zeugnisart),
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.bemerkungen.zeugnisASV),
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.bemerkungen.zeugnisAUE),
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.bemerkungen.zeugnisAllgemein),
				ersetzeNullBlankTrim(schuelerLernabschnittsdaten.bemerkungen.zeugnisLELS));

		this.reportingRepository = reportingRepository;

		super.foerderschwerpunkt1 = this.reportingRepository.katalogFoerderschwerpunkte().get(schuelerLernabschnittsdaten.foerderschwerpunkt1ID);
		super.foerderschwerpunkt2 = this.reportingRepository.katalogFoerderschwerpunkte().get(schuelerLernabschnittsdaten.foerderschwerpunkt2ID);

		super.schuljahresabschnitt = this.reportingRepository.schuljahresabschnitt(super.idSchuljahresabschnitt());

		super.schueler = this.reportingRepository.mapSchueler().get(schuelerLernabschnittsdaten.schuelerID);

		schuelerLernabschnittsdaten.leistungsdaten.forEach(
				l -> this.reportingRepository.mapAlleLeistungsdaten().add(this.schueler().id(), this.id(), l.id, l));
	}


	// ##### Hash und Equals Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return    true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj);
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
		if ((super.jahrgang == null) && (super.idJahrgang != null) && (super.idJahrgang >= 0))
			super.jahrgang = super.schuljahresabschnitt.jahrgang(super.idJahrgang);
		return super.jahrgang();
	}

	/**
	 * Stellt die Daten der Klasse des Schülers zur Verfügung, dem dieser Lernabschnitt gehört.
	 *
	 * @return Daten der Klasse
	 */
	@Override
	public ReportingKlasse klasse() {
		if ((super.klasse() == null) && (super.idKlasse() != null) && (super.idKlasse() >= 0))
			super.klasse = super.schuljahresabschnitt.klasse(super.idKlasse);
		return super.klasse();
	}

	/**
	 * Stellt die Leistungsdaten zur Verfügung, die in diesem Lernabschnitt dem Schüler zugeordnet sind.
	 *
	 * @return Die Leistungsdaten des Lernabschnitts.
	 */
	@Override
	public List<ReportingSchuelerLeistungsdaten> leistungsdaten() {
		if (!this.reportingRepository.mapAlleLeistungsdaten().containsKey1(this.schueler.id())) {
			final List<SchuelerLeistungsdaten> listLeistungsdaten = new ArrayList<>();
			if (new DataSchuelerLeistungsdaten(this.reportingRepository.conn()).getByLernabschnitt(this.id, listLeistungsdaten)) {
				listLeistungsdaten.forEach(
						l -> this.reportingRepository.mapAlleLeistungsdaten().add(this.schueler().id(), this.id(), l.id, l));
			}
		}
		if (super.leistungsdaten().isEmpty()) {
			super.setLeistungsdaten(this.reportingRepository.mapAlleLeistungsdaten()
					.get12(this.schueler().id(), this.id()).stream()
					.map(l -> (ReportingSchuelerLeistungsdaten) new ProxyReportingSchuelerLeistungsdaten(reportingRepository, this, l))
					.toList());
		}
		return super.leistungsdaten();
	}

	// TODO Klasse für die Nachprüfungen für die Reporting erzeugen und dann die Daten im überschriebenen Getter hier dynamisch nachladen.

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
									return new DataLehrerStammdaten(reportingRepository.conn()).getById(super.idSonderpaedagoge());
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
									return new DataLehrerStammdaten(reportingRepository.conn()).getById(super.idTutor());
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
