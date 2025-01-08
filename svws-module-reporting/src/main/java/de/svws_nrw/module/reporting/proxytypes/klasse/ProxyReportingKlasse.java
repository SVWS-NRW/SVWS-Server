package de.svws_nrw.module.reporting.proxytypes.klasse;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.klassen.KlassenDaten;
import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
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

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Klasse und erweitert die Klasse {@link ReportingKlasse}.
 */
public class ProxyReportingKlasse extends ReportingKlasse {

	/** Collator für die deutsche Sortierung von Einträgen */
	@JsonIgnore
	private final Collator colGerman = java.text.Collator.getInstance(Locale.GERMAN);

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingKlasse}.
	 *
	 * @param reportingRepository Repository für das Reporting.
	 * @param klassenDaten Stammdaten-Objekt aus der DB.
	 */
	public ProxyReportingKlasse(final ReportingRepository reportingRepository, final KlassenDaten klassenDaten) {
		super(klassenDaten.beginnSommersemester,
				ersetzeNullBlankTrim(klassenDaten.beschreibung),
				null,
				klassenDaten.id,
				klassenDaten.idAllgemeinbildendOrganisationsform,
				klassenDaten.idBerufsbildendOrganisationsform,
				klassenDaten.idFachklasse,
				klassenDaten.idFolgeklasse,
				klassenDaten.idJahrgang,
				klassenDaten.idKlassenart,
				klassenDaten.klassenLeitungen,
				new ArrayList<>(),
				klassenDaten.idSchulgliederung,
				klassenDaten.idVorgaengerklasse,
				klassenDaten.idWeiterbildungOrganisationsform,
				klassenDaten.istSichtbar,
				null,
				new ArrayList<>(),
				ersetzeNullBlankTrim(klassenDaten.kuerzel),
				ersetzeNullBlankTrim(klassenDaten.kuerzelFolgeklasse),
				ersetzeNullBlankTrim(klassenDaten.kuerzelVorgaengerklasse),
				ersetzeNullBlankTrim(klassenDaten.parallelitaet),
				ersetzeNullBlankTrim(klassenDaten.pruefungsordnung),
				new ArrayList<>(),
				null,
				klassenDaten.sortierung,
				ersetzeNullBlankTrim(klassenDaten.teilstandort),
				klassenDaten.verwendungAnkreuzkompetenzen,
				null);

		this.reportingRepository = reportingRepository;
		this.schuljahresabschnitt = this.reportingRepository.schuljahresabschnitt(klassenDaten.idSchuljahresabschnitt);
		// Schüler setzen. Fülle nur die Liste der IDs. Die ReportingSchueler-Liste wird per lazy-Loading gefüllt, da nicht immer die Klassenschüler benötigt
		// werden.
		if ((klassenDaten.schueler != null) && !klassenDaten.schueler.isEmpty())
			idsSchueler.addAll(klassenDaten.schueler.stream().map(s -> s.id).toList());
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
	 * Stellt die Daten der Folgeklasse der Klasse zur Verfügung, wenn diese bereits existiert.
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
							"FEHLER: Fehler bei der Ermittlung der Daten für die Folgeklasse der Klasse %s in %s."
									.formatted(super.kuerzel, super.schuljahresabschnitt.textSchuljahresabschnittKurz()),
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
	 * Stellt die Jahrgangsdaten der Klasse zur Verfügung.
	 *
	 * @return	Die Jahrgangsdaten der Klasse.
	 */
	@Override
	public ReportingJahrgang jahrgang() {
		if ((super.jahrgang() == null) && (super.idJahrgang() != null) && (super.idJahrgang() >= 0)) {
			super.jahrgang =
					new ProxyReportingJahrgang(
							this.reportingRepository,
							this.reportingRepository.mapJahrgaenge().computeIfAbsent(super.idJahrgang(), j -> {
								try {
									return new DataJahrgangsdaten(this.reportingRepository.conn()).getFromID(super.idJahrgang());
								} catch (final ApiOperationException e) {
									ReportingExceptionUtils.putStacktraceInLog(
											"INFO: Fehler mit definiertem Rückgabewert abgefangen bei der Bestimmung der Daten eines Jahrgangs.", e,
											reportingRepository.logger(), LogLevel.INFO, 0);
									return new JahrgangsDaten();
								}
							}), this.schuljahresabschnitt);
		}
		return super.jahrgang();
	}

	/**
	 * Stellt eine Liste mit Lehrern in der Funktion der Klassenleitung der Klasse zur Verfügung.
	 *
	 * @return	Liste von Lehrern als Klassenleitungen
	 */
	@Override
	public List<ReportingLehrer> klassenleitungen() {
		if (super.klassenleitungen().isEmpty() && !super.idsKlassenleitungen().isEmpty()) {
			super.klassenleitungen =
					super.idsKlassenleitungen().stream()
							.map(klId -> this.reportingRepository.mapLehrerStammdaten().computeIfAbsent(klId, l -> {
								try {
									return new DataLehrerStammdaten(this.reportingRepository.conn()).getById(klId);
								} catch (final ApiOperationException e) {
									ReportingExceptionUtils.putStacktraceInLog(
											"INFO: Fehler mit definiertem Rückgabewert abgefangen bei der Bestimmung der Stammdaten eines Lehrers.", e,
											reportingRepository.logger(), LogLevel.INFO, 0);
									return new LehrerStammdaten();
								}
							}))
							.map(l -> (ReportingLehrer) new ProxyReportingLehrer(
									this.reportingRepository,
									l))
							.toList();
		}
		return super.klassenleitungen();
	}

	/**
	 * Stellt eine Liste mit Schülern der Klasse zur Verfügung.
	 *
	 * @return	Liste mit Schülern
	 */
	@Override
	public List<ReportingSchueler> schueler() {
		if (super.schueler().isEmpty()) {
			final KlassenDaten klassenDaten;
			if (super.idsSchueler().isEmpty()) {
				try {
					klassenDaten = new DataKlassendaten(reportingRepository.conn()).getById(super.id());
					if ((klassenDaten.schueler != null) && !klassenDaten.schueler.isEmpty())
						idsSchueler.addAll(klassenDaten.schueler.stream().map(s -> s.id).toList());
				} catch (final ApiOperationException e) {
					ReportingExceptionUtils.putStacktraceInLog(
							"FEHLER: Fehler bei der Ermittlung der Schülerdaten der Klasse %s in %s."
									.formatted(super.kuerzel, super.schuljahresabschnitt.textSchuljahresabschnittKurz()),
							e, reportingRepository.logger(), LogLevel.ERROR, 0);
					return super.schueler();
				}
			}
			if (!super.idsSchueler.isEmpty()) {
				super.schueler = DataSchuelerStammdaten.getListStammdaten(this.reportingRepository.conn(), idsSchueler).stream()
						.map(s -> this.reportingRepository.mapSchuelerStammdaten().computeIfAbsent(s.id, k -> s))
						.map(s -> (ReportingSchueler) new ProxyReportingSchueler(
								this.reportingRepository,
								s))
						.sorted(Comparator
								.comparing(ReportingSchueler::nachname, colGerman)
								.thenComparing(ReportingSchueler::vorname, colGerman)
								.thenComparing(ReportingSchueler::vornamen, colGerman)
								.thenComparing(ReportingSchueler::geburtsdatum, colGerman)
								.thenComparing(ReportingSchueler::id, colGerman))
						.toList();
				this.reportingRepository.mapSchueler().putAll(super.schueler.stream().collect(Collectors.toMap(ReportingSchueler::id, s -> s)));
			}
		}
		return super.schueler();
	}

	/**
	 * Stellt die Daten der Vorgängerklasse der Klasse zur Verfügung.
	 *
	 * @return Daten der Vorgängerklasse
	 */
	@Override
	public ReportingKlasse vorgaengerklasse() {
		if ((super.vorgaengerklasse() == null) && (super.idVorgaengerklasse() != null) && (super.idVorgaengerklasse() >= 0)) {
			if (!this.reportingRepository.mapKlassen().containsKey(super.idVorgaengerklasse())) {
				// ID der Vorgängerklasse ist bekannt, aber sie wurde noch nicht aus der DB geladen. Lade deren Daten und lade dann alle Klassen des Lernabschnitts.
				final KlassenDaten klassenDaten;
				try {
					klassenDaten = new DataKlassendaten(reportingRepository.conn()).getByIdOhneSchueler(super.idVorgaengerklasse());
				} catch (final ApiOperationException e) {
					ReportingExceptionUtils.putStacktraceInLog(
							"FEHLER: Fehler bei der Ermittlung der Daten für die Vorgängerklasse der Klasse %s in %s."
									.formatted(super.kuerzel, super.schuljahresabschnitt.textSchuljahresabschnittKurz()),
							e, reportingRepository.logger(), LogLevel.ERROR, 0);
					return super.vorgaengerklasse();
				}
				super.vorgaengerklasse = this.reportingRepository.schuljahresabschnitt(klassenDaten.idSchuljahresabschnitt).klasse(super.idVorgaengerklasse());
			} else {
				// ID der Vorgängerklasse ist bekannt und die Klasse wurde in einem Lernabschnitt bereits erzeugt, hole sie aus Lernabschnitt.
				super.vorgaengerklasse =
						this.reportingRepository.mapKlassen().get(super.idVorgaengerklasse()).schuljahresabschnitt().klasse(super.idVorgaengerklasse());
			}
		}
		return super.vorgaengerklasse();
	}
}
