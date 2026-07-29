package de.svws_nrw.module.reporting.types.jahrgang;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.module.reporting.sortierung.ComparatorFactory;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierungService;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlasse;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;

import java.util.Comparator;
import java.util.List;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Jahrgang und erweitert die Klasse {@link ReportingJahrgang}.
 */
public class ProxyReportingJahrgang extends ReportingJahrgang {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingContext reportingContext;



	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingJahrgang}.
	 *
	 * @param reportingContext Repository für das Reporting.
	 * @param jahrgangsDaten Stammdaten-Objekt aus der DB.
	 * @param schuljahresabschnitt Der Schuljahresabschnitt zu diesem Jahrgang.
	 */
	public ProxyReportingJahrgang(final ReportingContext reportingContext, final JahrgangsDaten jahrgangsDaten,
			final ReportingSchuljahresabschnitt schuljahresabschnitt) {
		super(jahrgangsDaten.anzahlRestabschnitte,
				ersetzeNullBlankTrim(jahrgangsDaten.bezeichnung),
				jahrgangsDaten.gueltigBis,
				jahrgangsDaten.gueltigVon,
				null,
				jahrgangsDaten.id,
				jahrgangsDaten.idFolgejahrgang,
				null,
				null,
				ersetzeNullBlankTrim(jahrgangsDaten.kuerzel),
				jahrgangsDaten.idSchulgliederung,
				jahrgangsDaten.idJahrgang,
				jahrgangsDaten.istSichtbar,
				null,
				schuljahresabschnitt,
				jahrgangsDaten.sortierung);

		this.jahrgang = (jahrgangsDaten.idJahrgang == null) ? null : Jahrgaenge.data().getWertByID(jahrgangsDaten.idJahrgang);
		this.reportingContext = reportingContext;
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
	public ReportingContext reportingContext() {
		return reportingContext;
	}

	/**
	 * Stellt die Daten des Folgejahrgangs zur Verfügung.
	 *
	 * @return Daten des Folgejahrgangs
	 */
	@Override
	public ReportingJahrgang folgejahrgang() {
		if ((super.folgejahrgang() == null) && (super.idFolgejahrgang() != null) && (super.idFolgejahrgang() >= 0)) {
			if (this.reportingContext.repositoryKataloge().jahrgang(super.idFolgejahrgang()) == null) {
				return super.folgejahrgang();
			}
			// Die ID des FolgeJahrgangs ist bekannt und der Jahrgang wurde in einem Lernabschnitt bereits erzeugt, daher holt man ihn aus dem Lernabschnitt.
			super.folgejahrgang = super.schuljahresabschnitt().jahrgang(super.idFolgejahrgang());
		}
		return super.folgejahrgang();
	}

	/**
	 * Stellt eine Liste mit Klassen des Jahrgangs im übergebenen Schuljahresabschnitt zur Verfügung.
	 *
	 * @return	Liste mit Klassen
	 */
	@Override
	public List<ReportingKlasse> klassen() {
		if (super.klassen().isEmpty() && (super.schuljahresabschnitt != null)) {
			super.klassen =
					super.schuljahresabschnitt.klassen().values().stream()
							.filter(k -> k.idJahrgang() == super.id())
							.sorted(Comparator
									.comparing(ReportingKlasse::kuerzel)
									.thenComparing(ReportingKlasse::parallelitaet))
							.toList();
		}
		return super.klassen();
	}

	/**
	 * Stellt eine Liste mit Schülern des Jahrgangs zur Verfügung.
	 *
	 * @return	Liste mit Schülern
	 */
	@Override
	public List<ReportingSchueler> schueler() {
		if (super.schueler().isEmpty()) {
			// Prüfe, ob Service und Logger abrufbar sind. Andernfalls würden Standardsortierungen verwendet werden.
			final ReportingSortierungService sortierungService = (this.reportingContext != null) ? this.reportingContext.sortierungService() : null;
			final Logger logger = (this.reportingContext != null) ? this.reportingContext.logger() : null;

			final Comparator<ReportingSchueler> comparator =
					ComparatorFactory.buildComparator(sortierungService, logger, ReportingSchueler.class.getSimpleName(),
							ReportingSchueler.SORTIERUNG, true);
			super.schueler = klassen().stream().flatMap(k -> k.schueler().stream()).sorted(comparator).toList();
		}
		return super.schueler();
	}
}
