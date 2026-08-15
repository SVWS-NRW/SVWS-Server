package de.svws_nrw.module.reporting.types.stundenplanung;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsicht;
import de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsichtBereich;
import de.svws_nrw.core.data.stundenplan.StundenplanPausenzeit;
import de.svws_nrw.core.types.Wochentag;
import de.svws_nrw.core.utils.stundenplan.StundenplanManager;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlasse;


/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Pausenzeit und erweitert die Klasse {@link de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungPausenzeit}.
 */
public class ProxyReportingStundenplanungPausenzeit extends ReportingStundenplanungPausenzeit {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingContext reportingContext;

	/** Der Stundenplan-Manager, zu dem dieses Zeitraster-Element gehört. */
	@JsonIgnore
	private StundenplanManager stundenplanManager;

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingStundenplanungPausenzeit}.
	 *
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param pausenzeit		  Die Pausenzeit aus dem Stundenplan.
	 * @param stundenplan         Der Stundenplan, zudem diese Pausenzeit gehört.
	 */
	public ProxyReportingStundenplanungPausenzeit(final ReportingContext reportingContext,
			final StundenplanPausenzeit pausenzeit, final ReportingStundenplanungStundenplan stundenplan) {
		super(pausenzeit.id,
				stundenplan,
				ersetzeNullBlankTrim(pausenzeit.bezeichnung),
				pausenzeit.beginn,
				pausenzeit.ende,
				new ArrayList<>(),
				new ArrayList<>(),
				null);

		this.reportingContext = reportingContext;
		this.stundenplanManager = this.reportingContext.repositoryStundenplan().manager(stundenplan.id());

		if (stundenplanManager == null) {
			return;
		}

		try {
			// Prüfe, ob die ID des Zeitrasters zum Stundenplan-Manager passt.
			stundenplanManager.pausenzeitGetByIdOrException(pausenzeit.id);
			// Setze den Wochentag, wenn es passt.
			super.wochentag = Wochentag.fromIDorException(pausenzeit.wochentag);
		} catch (final Exception e) {
			// Beide Prüfungen arbeiten allein auf den bereits geladenen Daten; ein Datenzugriff findet hier nicht statt. Passen Pausenzeit und Stundenplan
			// nicht zusammen oder ist der Wochentag unbekannt, sind Klassen und Aufsichten dieser Pausenzeit nicht bestimmbar: Der Konstruktor bricht ab und
			// die Pausenzeit erscheint ohne diese Angaben statt mit falschen.
			this.stundenplanManager = null;
			return;
		}

		// Klassen der Pausenzeit ergänzen. Durch User-Filter ausgeschlossene Klassen (null) werden übersprungen.
		pausenzeit.klassen.forEach(k -> {
			final ReportingKlasse klasse = stundenplan.schuljahresabschnitt().klasse(k);
			if (klasse != null) {
				super.klassen().add(klasse);
			}
		});

		// Pausenaufsichten erzeugen.
		final List<ReportingStundenplanungPausenaufsicht> reportingAufsichten = new ArrayList<>();

		for (final StundenplanPausenaufsicht aufsicht : this.stundenplanManager.pausenaufsichtGetMengeByPausenzeitId(pausenzeit.id)) {
			for (final StundenplanPausenaufsichtBereich bereich : aufsicht.bereiche) {
				reportingAufsichten.add(new ReportingStundenplanungPausenaufsicht(
						aufsicht.id,
						stundenplanManager.aufsichtsbereichGetByIdOrException(bereich.idAufsichtsbereich).beschreibung,
						stundenplanManager.aufsichtsbereichGetByIdOrException(bereich.idAufsichtsbereich).kuerzel,
						bereich.idAufsichtsbereich,
						this.reportingContext.repositoryLehrer().lehrer(aufsicht.idLehrer),
						bereich.wochentyp)
				);
			}
		}

		super.setPausenaufsichten(reportingAufsichten);
	}


	// ##### Hash und Equals ud Compare-Methoden #####

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

}
