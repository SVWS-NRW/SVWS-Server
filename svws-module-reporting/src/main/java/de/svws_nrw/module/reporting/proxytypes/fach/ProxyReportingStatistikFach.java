package de.svws_nrw.module.reporting.proxytypes.fach;

import de.svws_nrw.asd.data.fach.FachKatalogEintrag;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.module.reporting.types.fach.ReportingStatistikFach;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ StatistikFach und erweitert die Klasse {@link ReportingStatistikFach}.
 */
public class ProxyReportingStatistikFach extends ReportingStatistikFach {

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingStatistikFach}.
	 * Wenn das Statistikfach im Schuljahresabschnitt nicht gültig war, können keine Daten des Statistikfaches ergänzt werden. Es ist dann null.
	 *
	 * @param statistikfach 			Fach aus dem Katalog der Statistikfächer
	 * @param schuljahr					Das Schuljahr, aus dem die Statistikdaten des Faches gelesen werden.
	 * @param exaktePruefungSchuljahr	Wenn true, dann werden nur Statistikdaten aus dem angegebenen Schuljahr zugelassen. Wenn false, dann wird der letzte
	 * 									gültige Eintrag in der Historie des Statistikfaches verwendet.
	 */
	public ProxyReportingStatistikFach(final Fach statistikfach, final int schuljahr, final boolean exaktePruefungSchuljahr) {
		super(null,
				-1,
				null,
				false,
				null,
				null,
				null,
				"",
				-1,
				false,
				false,
				false,
				false,
				false,
				null,
				null,
				false);

		initReportingStatistikFach(statistikfach, schuljahr, exaktePruefungSchuljahr);
	}

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für ein {@link ReportingStatistikFach}.
	 * Wenn das Statistikfach im Schuljahresabschnitt nicht gültig war, können keine Daten des Statistikfaches ergänzt werden. Es ist dann null.
	 *
	 * @param statistikfachKuerzel 		ASD-Kürzel des zugehörigen Statistik-Faches
	 * @param schuljahr					Das Schuljahr, aus dem die Statistikdaten des Faches gelesen werden.
	 * @param exaktePruefungSchuljahr	Wenn true, dann werden nur Statistikdaten aus dem angegebenen Schuljahr zugelassen. Wenn false, dann wird der letzte
	 * 									gültige Eintrag in der Historie des Statistikfaches verwendet.
	 */
	public ProxyReportingStatistikFach(final String statistikfachKuerzel, final int schuljahr, final boolean exaktePruefungSchuljahr) {
		super(null,
				-1,
				null,
				false,
				null,
				null,
				null,
				"",
				-1,
				false,
				false,
				false,
				false,
				false,
				null,
				null,
				false);

		if ((statistikfachKuerzel != null) && !statistikfachKuerzel.isEmpty())
			initReportingStatistikFach(Fach.getBySchluesselOrDefault(statistikfachKuerzel), schuljahr, exaktePruefungSchuljahr);
	}


	/**
	 * Initialisiert die Daten des Statistikfaches, wenn es für das angegebene Schuljahr ein solches gültiges Fach gibt.
	 *
	 * @param statistikfach				Fach aus dem Katalog der Statistikfächer
	 * @param schuljahr					Das Schuljahr, aus dem die Statistikdaten des Faches gelesen werden.
	 * @param exaktePruefungSchuljahr	Wenn true, dann werden nur Statistikdaten aus dem angegebenen Schuljahr zugelassen. Wenn false, dann wird der letzte
	 * 									gültige Eintrag in der Historie des Statistikfaches verwendet.
	 */
	private void initReportingStatistikFach(final Fach statistikfach, final int schuljahr, final boolean exaktePruefungSchuljahr) {

		final FachKatalogEintrag statistikfachDaten;
		if (exaktePruefungSchuljahr)
			statistikfachDaten = statistikfach.daten(schuljahr);
		else
			statistikfachDaten = statistikfach.getEintragOrLast(schuljahr);

		// Wenn die statistikfachDaten null sind, dann war das Statistikfach wahrscheinlich im angegebenen Schuljahr nicht gültig.
		if (statistikfachDaten != null) {
			super.abJahrgang = ersetzeNullDurchEmpty(statistikfachDaten.abJahrgang);
			super.aufgabenfeld = statistikfachDaten.aufgabenfeld;
			super.bezeichnung = ersetzeNullDurchEmpty(statistikfachDaten.text);
			super.exportASD = statistikfachDaten.exportASD;
			super.fachgruppe = statistikfach.getFachgruppe(schuljahr);
			super.gueltigBis = statistikfachDaten.gueltigBis;
			super.gueltigVon = statistikfachDaten.gueltigVon;
			super.htmlFarbeRGB = statistikfach.getHMTLFarbeRGB(schuljahr);
			super.idFachkatalog = statistikfachDaten.id;
			super.istAusRegUFach = statistikfachDaten.istAusRegUFach;
			super.istErsatzPflichtFS = statistikfachDaten.istErsatzPflichtFS;
			super.istFremdsprache = statistikfachDaten.istFremdsprache;
			super.istHKFS = statistikfachDaten.istHKFS;
			super.istKonfKoop = statistikfachDaten.istKonfKoop;
			super.kuerzel = ersetzeNullDurchEmpty(statistikfachDaten.kuerzel);
			super.kuerzelASD = ersetzeNullDurchEmpty(statistikfachDaten.schluessel);
			super.nurSII = statistikfachDaten.nurSII;
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
