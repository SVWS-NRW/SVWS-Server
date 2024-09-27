package de.svws_nrw.module.reporting.proxytypes.fach;

import de.svws_nrw.core.data.fach.FachDaten;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Fach und erweitert die Klasse {@link ReportingFach}.
 */
public class ProxyReportingFach extends ReportingFach {

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingFach}.
	 * @param fachDaten 	Die allgemeinen Daten des Faches
	 * @param fachGostDaten Die GOSt-Daten des Faches
	 * @param schuljahr		Das Schuljahr, aus dem die Statistikdaten des Faches gelesen werden.
	 */
	public ProxyReportingFach(final FachDaten fachDaten, final GostFach fachGostDaten, final int schuljahr) {
		super(fachDaten.aufgabenfeld,
				fachDaten.aufZeugnis,
				fachDaten.bezeichnung,
				fachDaten.bezeichnungUeberweisungszeugnis,
				fachDaten.bezeichnungZeugnis,
				fachDaten.bilingualeSprache,
				null,
				fachDaten.holeAusAltenLernabschnitten,
				fachDaten.id,
				fachDaten.istFHRFach,
				false,
				false,
				fachDaten.istOberstufenFach,
				fachDaten.istNachpruefungErlaubt,
				fachDaten.istPruefungsordnungsRelevant,
				fachDaten.istSchriftlichBA,
				fachDaten.istSchriftlichZK,
				fachDaten.istSichtbar,
				fachDaten.kuerzel,
				fachDaten.maxZeichenInFachbemerkungen,
				fachDaten.sortierung,
				null);

		if ((fachDaten.kuerzelStatistik != null) && !fachDaten.kuerzelStatistik.isEmpty()) {
			super.statistikfach = new ProxyReportingStatistikFach(Fach.getBySchluesselOrDefault(fachDaten.kuerzelStatistik), schuljahr, true);
			super.fachgruppe = super.statistikfach().fachgruppe();
		}

		if (fachGostDaten != null) {
			this.istFremdsprache = fachGostDaten.istFremdsprache;
			this.istFremdSpracheNeuEinsetzend = fachGostDaten.istFremdSpracheNeuEinsetzend;
		}
	}
}
