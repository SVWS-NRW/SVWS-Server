package de.svws_nrw.module.reporting.proxytypes.jahrgang;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.klasse.ReportingKlasse;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;

import java.util.Comparator;
import java.util.List;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Jahrgang und erweitert die Klasse {@link ReportingJahrgang}.
 */
public class ProxyReportingJahrgang extends ReportingJahrgang {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;



	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingJahrgang}.
	 * @param reportingRepository Repository für die Reporting.
	 * @param jahrgangsDaten Stammdaten-Objekt aus der DB.
	 * @param schuljahresabschnitt Der Schuljahresabschnitt zu diesem Jahrgang.
	 */
	public ProxyReportingJahrgang(final ReportingRepository reportingRepository, final JahrgangsDaten jahrgangsDaten,
			final ReportingSchuljahresabschnitt schuljahresabschnitt) {
		super(jahrgangsDaten.bezeichnung,
				jahrgangsDaten.gueltigBis,
				jahrgangsDaten.gueltigVon,
				null,
				jahrgangsDaten.id,
				jahrgangsDaten.idFolgejahrgang,
				null,
				jahrgangsDaten.kuerzel,
				jahrgangsDaten.kuerzelSchulgliederung,
				jahrgangsDaten.kuerzelStatistik,
				jahrgangsDaten.istSichtbar,
				null,
				schuljahresabschnitt,
				jahrgangsDaten.sortierung);
		this.reportingRepository = reportingRepository;
	}



	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 * @return Repository für die Reporting
	 */
	public ReportingRepository reportingRepository() {
		return reportingRepository;
	}

	/**
	 * Stellt die Daten des Folgejahrgangs zur Verfügung.
	 * @return Daten des Folgejahrgangs
	 */
	@Override
	public ReportingJahrgang folgejahrgang() {
		if ((super.folgejahrgang() == null) && (super.idFolgejahrgang() != null) && (super.idFolgejahrgang() >= 0)) {
			if (!this.reportingRepository.mapJahrgaenge().containsKey(super.idFolgejahrgang())) {
				// TODO: Wenn die Jahrgänge eine Gültigkeit erhalten, dann ist diese hier auch zu implementieren. Aktuell werden in alle Schuljahresabschnitte alle
				//  Jahrgänge übernommen und der Folgejahrgang innerhalb des gleichen Lernabschnitts ermittelt, da keine Regelung zum Folgejahrgang und einem
				//  Folgeabschnitt im System implementiert ist. Daher wird eine direkt Rückgabe erzeugt, die aber nie auftreten dürfte.
				return super.folgejahrgang();
			}
			// ID des FolgeJahrgangs ist bekannt und der Jahrgang wurde in einem Lernabschnitt bereits erzeugt, hole ihn aus Lernabschnitt.
			super.folgejahrgang = super.schuljahresabschnitt().jahrgang(super.idFolgejahrgang());
		}
		return super.folgejahrgang();
	}

	/**
	 * Stellt eine Liste mit Klassen des Jahrgangs im übergebenen Schuljahresabschnitt zur Verfügung.
	 * @return	Liste mit Klassen
	 */
	@Override
	public List<ReportingKlasse> klassen() {
		if (super.klassen().isEmpty()) {
			super.klassen =
					super.schuljahresabschnitt.klassen().stream()
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
	 * @return	Liste mit Schülern
	 */
	@Override
	public List<ReportingSchueler> schueler() {
		if (super.schueler().isEmpty()) {
			super.schueler =
					klassen().stream()
							.flatMap(k -> k.schueler().stream())
							.sorted(Comparator
									.comparing(ReportingSchueler::nachname)
									.thenComparing(ReportingSchueler::vorname)
									.thenComparing(ReportingSchueler::vornamen)
									.thenComparing(ReportingSchueler::geburtsdatum)
									.thenComparing(ReportingSchueler::id))
							.toList();
		}
		return super.schueler();
	}
}
