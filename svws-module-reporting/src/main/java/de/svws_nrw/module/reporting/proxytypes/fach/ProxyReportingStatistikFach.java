package de.svws_nrw.module.reporting.proxytypes.fach;

import de.svws_nrw.asd.data.fach.FachKatalogEintrag;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.fach.ReportingStatistikFach;

/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ StatistikFach und erweitert die Klasse {@link ReportingStatistikFach}.</p>
 *
 *  <p>In diesem Kontext besitzt die Proxy-Klasse ausschließlich die gleichen Methoden wie die zugehörige Reporting-Super-Klasse.
 *  Während die Super-Klasse aber als reiner Datentyp konzipiert ist, d. h. ohne Anbindung an die Datenbank,
 *  greift die Proxy-Klassen an verschiedenen Stellen auf die Datenbank zu.</p>
 *
 *  <ul>
 *      <li>Die Proxy-Klasse stellt in der Regel einen zusätzlichen Constructor zur Verfügung, um Reporting-Objekte
 *  		aus Stammdatenobjekten (aus dem Package core.data) erstellen zu können. Darin werden Felder, die Reporting-Objekte
 *  		zurückgegeben und nicht im Stammdatenobjekt enthalten sind, mit null initialisiert.</li>
 * 		<li>Die Proxy-Klasse überschreibt einzelne Getter der Super-Klasse (beispielsweise bei Felder, die mit null initialisiert wurden)
 *  		und lädt dort dann aus der Datenbank die Daten bei Bedarf nach (lazy-loading), um den Umfang der Datenstrukturen gering zu
 *  		halten.</li>
 * 		<li>Die Proxy-Klasse können zudem auf das Repository {@link ReportingRepository} zugreifen. Dieses
 * 			enthält neben den Stammdaten der Schule einige Maps, in der zur jeweiligen ID bereits ausgelesene Stammdaten anderer Objekte
 * 			wie Kataloge, Jahrgänge, Klassen, Lehrer, Schüler usw. gespeichert werden. So sollen Datenbankzugriffe minimiert werden. Werden in der
 * 			Proxy-Klasse Daten nachgeladen, so werden sie dabei auch in der entsprechenden Map des Repository ergänzt.</li>
 *  </ul>
 */
public class ProxyReportingStatistikFach extends ReportingStatistikFach {


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis der Daten eines Statistik-Faches.
	 * Wenn das Statistikfach im Schuljahresabschnitt nicht gültig war, können keine Daten des Statistikfaches ergänzt werden. Es ist dann null.
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
	 * Erstellt ein neues Reporting-Objekt auf Basis der Daten eines Statistik-Fachkürzels (ASD-Kürzel). Dadurch werden die Fachdaten des Statistik-Faches angelegt.
	 * Wenn das Statistikfach im Schuljahresabschnitt nicht gültig war, können keine Daten des Statistikfaches ergänzt werden. Es ist dann null.
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
			super.abJahrgang = statistikfachDaten.abJahrgang;
			super.aufgabenfeld = statistikfachDaten.aufgabenfeld;
			super.bezeichnung = statistikfachDaten.text;
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
			super.kuerzel = statistikfachDaten.kuerzel;
			super.kuerzelASD = statistikfachDaten.schluessel;
			super.nurSII = statistikfachDaten.nurSII;
		}
	}
}
