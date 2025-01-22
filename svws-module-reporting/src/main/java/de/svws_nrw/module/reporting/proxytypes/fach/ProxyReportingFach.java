package de.svws_nrw.module.reporting.proxytypes.fach;

import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Fach und erweitert die Klasse {@link ReportingFach}.
 */
public class ProxyReportingFach extends ReportingFach {

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingFach}.
	 *
	 * @param fach 	Die allgemeinen Daten des Faches
	 * @param schuljahr		Das Schuljahr, aus dem die Statistikdaten des Faches gelesen werden.
	 */
	public ProxyReportingFach(final DTOFach fach, final int schuljahr) {
		super(ersetzeNullBlankTrim(fach.Aufgabenfeld),
				fach.AufZeugnis,
				ersetzeNullBlankTrim(fach.Bezeichnung),
				ersetzeNullBlankTrim(fach.BezeichnungUeberweisungsZeugnis),
				ersetzeNullBlankTrim(fach.BezeichnungZeugnis),
				((fach.Unterrichtssprache != null) && (!(fach.Unterrichtssprache.isEmpty())) && (!"D".equals(fach.Unterrichtssprache)))
						? fach.Unterrichtssprache.substring(0, 1) : "",
				null,
				fach.AbgeschlFaecherHolen,
				fach.ID,
				((fach.GewichtungFHR != null) && (fach.GewichtungFHR != 0)),
				fach.IstFremdsprache,
				fach.IstMoeglichAlsNeueFremdspracheInSekII,
				fach.IstOberstufenFach,
				fach.IstNachpruefungErlaubt,
				fach.IstPruefungsordnungsRelevant,
				fach.IstSchriftlichBA,
				fach.IstSchriftlichZK,
				fach.Sichtbar,
				ersetzeNullBlankTrim(fach.Kuerzel),
				((fach.MaxBemZeichen == null) ? Integer.MAX_VALUE : fach.MaxBemZeichen),
				fach.SortierungAllg,
				null);

		if ((fach.StatistikKuerzel != null) && !fach.StatistikKuerzel.isEmpty()) {
			super.statistikfach = new ProxyReportingStatistikFach(Fach.getBySchluesselOrDefault(fach.StatistikKuerzel), schuljahr, true);
			super.fachgruppe = super.statistikfach().fachgruppe();
		}
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


}
