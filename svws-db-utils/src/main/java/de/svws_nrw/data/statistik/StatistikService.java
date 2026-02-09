package de.svws_nrw.data.statistik;

import static de.svws_nrw.data.TransactionSupport.transactional;

import de.svws_nrw.asd.data.statistik.StatistikGesamt;

/**
 * Ein Service für den Zugriff auf die Statistikdaten
 */
public final class StatistikService {

	/** Der Service für die Schuldaten für die Statistik */
	private final SchuleStatistikService schuleStatistikService;

	/** Der Service für den Zugriff auf die Lehrer-Statistikdaten */
	private final LehrerStatistikService lehrerStatistikService;

	/** Der Service für den Zugriff auf die Klassen-Statistikdaten */
	private final KlassenStatistikService klassenStatistikService;

	/** Der Service für den Zugriff auf die Schueler-Statistikdaten */
	private final SchuelerStatistikService schuelerStatistikService;

	/** Der Service für den Zugriff auf die Jahrgänge-Statistikdaten */
	private final JahrgaengeStatistikService jahrgaengeStatistikService;

	/** Der Service für den Zugriff auf die Orts-Statistikdaten */
	private final OrteStatistikService orteStatistikService;

	/** Der Service für den Zugriff auf die Förderschwerpunkt-Statistikdaten */
	private final FoerderschwerpunkteStatistikService foerderschwerpunktStatistikService;

	/** Der Service für den Zugriff auf die Statistikdaten zu den Religionen */
	private final ReligionStatistikService religionStatistikService;


	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param schuleStatistikService               der Service für die Schuldaten für die Statistik
	 * @param lehrerStatistikService               der Service für den Datenbank-Zugriff auf die Lehrer
	 * @param klassenStatistikService              der Service für den Datenbank-Zugriff auf die Klassen
	 * @param schuelerStatistikService             der Service für den Zugriff auf die Schueler-Statistikdaten
	 * @param jahrgaengeStatistikService           der Service für den Datenbank-Zugriff auf die Jahrgänge
	 * @param orteStatistikService                 der Service für den Datenbank-Zugriff auf die Orte
	 * @param foerderschwerpunktStatistikService   der Service für den Datenbank-Zugriff auf die Förderschwerpunkte
	 * @param religionStatistikService             der Service für den Datenbank-Zugriff auf die Religionen
	 */
	public StatistikService(final SchuleStatistikService schuleStatistikService,
			final LehrerStatistikService lehrerStatistikService,
			final KlassenStatistikService klassenStatistikService,
			final SchuelerStatistikService schuelerStatistikService,
			final JahrgaengeStatistikService jahrgaengeStatistikService,
			final OrteStatistikService orteStatistikService,
			final FoerderschwerpunkteStatistikService foerderschwerpunktStatistikService,
			final ReligionStatistikService religionStatistikService) {
		this.schuleStatistikService = schuleStatistikService;
		this.lehrerStatistikService = lehrerStatistikService;
		this.klassenStatistikService = klassenStatistikService;
		this.schuelerStatistikService = schuelerStatistikService;
		this.jahrgaengeStatistikService = jahrgaengeStatistikService;
		this.orteStatistikService = orteStatistikService;
		this.foerderschwerpunktStatistikService = foerderschwerpunktStatistikService;
		this.religionStatistikService = religionStatistikService;
	}


	/**
	 * Bestimmt die Statistikdaten.
	 *
	 * @return die Statistikdaten
	 */
	public StatistikGesamt get() {
		return transactional(() -> {
			final StatistikGesamt daten = new StatistikGesamt();
			daten.schule = schuleStatistikService.get();
			daten.lehrer = lehrerStatistikService.getList();
			daten.klassen = klassenStatistikService.getList();
			daten.schueler = schuelerStatistikService.getList();
			daten.jahrgaenge = jahrgaengeStatistikService.getList();
			daten.orte = orteStatistikService.getList();
			daten.foederschwerpunkte = foerderschwerpunktStatistikService.getList();
			daten.religionen = religionStatistikService.getList();
			return daten;
		});
	}

}
