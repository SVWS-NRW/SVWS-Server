package de.svws_nrw.module.reporting.types.lerngruppen;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.data.jahrgaenge.DataJahrgangsdaten;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ProxyReportingSchuelerLeistungsdatenMatrix;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLeistungsdatenMatrix;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import de.svws_nrw.module.reporting.types.jahrgang.ProxyReportingJahrgang;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Klasse und erweitert die Klasse {@link ReportingKlasse}.
 */
public class ProxyReportingKlasse extends ReportingKlasse {

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
		super(klassenDaten.id,
				null,
				ersetzeNullBlankTrim(klassenDaten.kuerzel),
				new ArrayList<>(),
				klassenDaten.sortierung,
				klassenDaten.beginnSommersemester,
				ersetzeNullBlankTrim(klassenDaten.beschreibung),
				null,
				klassenDaten.idAllgemeinbildendOrganisationsform,
				klassenDaten.idBerufsbildendOrganisationsform,
				klassenDaten.idFachklasse,
				klassenDaten.idFolgeklasse,
				klassenDaten.idJahrgang,
				klassenDaten.idKlassenart,
				new ArrayList<>(reportingRepository.lehrer(klassenDaten.klassenLeitungen.stream().filter(Objects::nonNull).toList(), false)),
				new ArrayList<>(),
				klassenDaten.idSchulgliederung,
				klassenDaten.idVorgaengerklasse,
				klassenDaten.idWeiterbildungOrganisationsform,
				null,
				ersetzeNullBlankTrim(klassenDaten.kuerzelFolgeklasse),
				ersetzeNullBlankTrim(klassenDaten.kuerzelVorgaengerklasse),
				ersetzeNullBlankTrim(klassenDaten.parallelitaet),
				ersetzeNullBlankTrim(klassenDaten.pruefungsordnung),
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
	 * @return    True, falls es das gleiche Objekt ist, andernfalls false.
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
				// Die ID der Folgeklasse ist bekannt, aber sie wurde noch nicht aus der DB geladen. Lade deren Daten und lade dann alle Klassen des
				// Lernabschnitts.
				final KlassenDaten klassenDaten;
				try {
					klassenDaten = new DataKlassendaten(reportingRepository.conn()).getByIdOhneSchueler(super.idFolgeklasse());
				} catch (final ApiOperationException e) {
					ReportingExceptionUtils.logException(
							"FEHLER: Fehler bei der Ermittlung der Daten für die Folgeklasse der Klasse %s in %s."
									.formatted(super.kuerzel, super.schuljahresabschnitt.textSchuljahresabschnittKurz()),
							e, reportingRepository.logger(), LogLevel.ERROR, 0);
					return super.folgeklasse();
				}
				super.folgeklasse = this.reportingRepository.schuljahresabschnitt(klassenDaten.idSchuljahresabschnitt).klasse(super.idFolgeklasse());
			} else {
				// Die ID der Folgeklasse ist bekannt und die Klasse wurde in einem Lernabschnitt bereits erzeugt, sie kann aus dem Lernabschnitt geholt werden.
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
									final DataJahrgangsdaten dataJahrgangsdaten = new DataJahrgangsdaten(this.reportingRepository.conn());
									final Long idJahrgang = super.idJahrgang();
									if (idJahrgang == null)
										throw new ApiOperationException(Response.Status.BAD_REQUEST, "Keine ID für den Jahrgang übergeben.");
									return dataJahrgangsdaten.getById(idJahrgang);
								} catch (final ApiOperationException e) {
									ReportingExceptionUtils.logException(
											"INFO: Fehler mit definiertem Rückgabewert abgefangen bei der Bestimmung der Daten eines Jahrgangs.", e,
											reportingRepository.logger(), LogLevel.INFO, 0);
									return new JahrgangsDaten();
								}
							}), this.schuljahresabschnitt);
		}
		return super.jahrgang();
	}

	/**
	 * Erstellt eine Leistungsdaten-Matrix für die Schüler dieser Gruppe basierend auf dem Schuljahresabschnitt der Gruppe.
	 *
	 * @return Die Leistungsdaten-Matrix für diese Schülergruppe.
	 */
	@Override
	public ReportingSchuelerLeistungsdatenMatrix schuelerLeistungsdatenMatrix() {
		return new ProxyReportingSchuelerLeistungsdatenMatrix(this.reportingRepository, this.schueler(), this.schuljahresabschnitt());
	}

	/**
	 * Stellt eine Liste mit Schülern der Klasse zur Verfügung.
	 *
	 * @return	Liste mit Schülern
	 */
	@Override
	public List<ReportingSchueler> schueler() {
		if (super.schueler().isEmpty()) {
			super.schueler = this.reportingRepository.schueler(idsSchueler);
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
				// Die ID der Vorgängerklasse ist bekannt, aber sie wurde noch nicht aus der DB geladen. Lade deren Daten und lade dann alle Klassen des
				// Lernabschnitts.
				final KlassenDaten klassenDaten;
				try {
					klassenDaten = new DataKlassendaten(reportingRepository.conn()).getByIdOhneSchueler(super.idVorgaengerklasse());
				} catch (final ApiOperationException e) {
					ReportingExceptionUtils.logException(
							"FEHLER: Fehler bei der Ermittlung der Daten für die Vorgängerklasse der Klasse %s in %s."
									.formatted(super.kuerzel, super.schuljahresabschnitt.textSchuljahresabschnittKurz()),
							e, reportingRepository.logger(), LogLevel.ERROR, 0);
					return super.vorgaengerklasse();
				}
				super.vorgaengerklasse = this.reportingRepository.schuljahresabschnitt(klassenDaten.idSchuljahresabschnitt).klasse(super.idVorgaengerklasse());
			} else {
				// Die ID der Vorgängerklasse ist bekannt und die Klasse wurde in einem Lernabschnitt bereits erzeugt, sie kann aus dem Lernabschnitt geholt
				// werden.
				super.vorgaengerklasse =
						this.reportingRepository.mapKlassen().get(super.idVorgaengerklasse()).schuljahresabschnitt().klasse(super.idVorgaengerklasse());
			}
		}
		return super.vorgaengerklasse();
	}
}
