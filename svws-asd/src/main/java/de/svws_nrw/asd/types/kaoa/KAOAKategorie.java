package de.svws_nrw.asd.types.kaoa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.jahrgang.JahrgaengeKatalogEintrag;
import de.svws_nrw.asd.data.kaoa.KAOAKategorieKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die KAoA-Kategorien.
 */
public enum KAOAKategorie implements CoreType<KAOAKategorieKatalogEintrag, KAOAKategorie> {

	/** KAoA-Kategorie: Formen der Orientierung und Beratung */
	SBO_2,

	/** KAoA-Kategorie: Strukturen an Schulen */
	SBO_3,

	/** KAoA-Kategorie: Potenziale entdecken und den eigenen Standort bestimmen */
	SBO_4,

	/** KAoA-Kategorie: Berufsfelder erkunden und Informationen sammeln */
	SBO_5,

	/** KAoA-Kategorie: Praxis der Arbeitswelt kennenlernen und erproben */
	SBO_6,

	/** KAoA-Kategorie: Nachholung der Erstberufsorientierung */
	SBO_7,

	/** KAoA-Kategorie: Sekundarstufe II - Individuelle Voraussetzungen für eine Ausbildung oder ein Studium überprüfen */
	SBO_8,

	/** KAoA-Kategorie: Sekundarstufe II - Praxis vertiefen - Ausbildungs- und Studienwahl konkretisieren */
	SBO_9,

	/** KAoA-Kategorie: Gestaltung und Koordination der Übergänge in der Sek. I und Sek. II */
	SBO_10;


	/* ----- Die nachfolgenden Attribute werden nicht initialisiert und werden als Cache verwendet, um z.B. den Schuljahres-bezogenen Zugriff zu cachen ----- */

	/** (Integer) -> Schuljahr */
	private static final @NotNull Map<Integer, Map<KAOAKategorie, List<Jahrgaenge>>> _mapBySchuljahrAndKategorie = new HashMap<>();

	/** (Integer, Long) -> Schuljahr, idJahrgang */
	private static final @NotNull Map<Integer, Map<Long, List<KAOAKategorieKatalogEintrag>>> _mapEintraegeBySchuljahrAndJahrgang = new HashMap<>();

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<KAOAKategorieKatalogEintrag, KAOAKategorie> manager) {
		CoreTypeDataManager.putManager(KAOAKategorie.class, manager);
		_mapBySchuljahrAndKategorie.clear();
		_mapEintraegeBySchuljahrAndJahrgang.clear();
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<KAOAKategorieKatalogEintrag, KAOAKategorie> data() {
		return CoreTypeDataManager.getManager(KAOAKategorie.class);
	}


	/**
	 * Prüft, ob der angegebene Jahrgang in dem angegebenen Schuljahr für diese Kategorie zulässig ist.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param jahrgang    der Jahrgang
	 *
	 * @return true, falls der Jahrgang in dem Schuljahr zulässig ist - ansonsten false.
	 */
	public boolean hatJahrgang(final int schuljahr, final Jahrgaenge jahrgang) {
		return getListJahrgaengeBySchuljahrAndKategorie(schuljahr, this).contains(jahrgang);
	}


	/**
	 * Liefert alle zulässigen Jahrgänge für die angegebene Kategorie in dem angegebenen Schuljahr.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param kategorie   die Kategorie
	 *
	 * @return die bei der Kategorie in dem angegebenen Schuljahr zulässigen Jahrgänge
	 */
	public static @NotNull List<Jahrgaenge> getListJahrgaengeBySchuljahrAndKategorie(final int schuljahr, final @NotNull KAOAKategorie kategorie) {
		// Überprüfen, ob der Cache für das Schuljahr existiert; falls nicht, Cache für alle Kategorien aufbauen
		Map<KAOAKategorie, List<Jahrgaenge>> mapByKategorie = _mapBySchuljahrAndKategorie.get(schuljahr);
		if (mapByKategorie == null) {
			mapByKategorie = cacheListJahrgaengeBySchuljahrAndKategorie(schuljahr);
			_mapBySchuljahrAndKategorie.put(schuljahr, mapByKategorie);
		}
		// Rückgabe der Liste von Jahrgängen aus dem Cache oder eine leere Liste, falls Kategorie nicht vorhanden
		final List<Jahrgaenge> jahrgaenge = mapByKategorie.get(kategorie);
		return (jahrgaenge != null) ? jahrgaenge : new ArrayList<>();
	}

	/**
	 * Liefert einen Cache der zulässigen Jahrgänge je Kategorie in dem angegebenen Schuljahr zurück.
	 *
	 * @param schuljahr   das Schuljahr
	 * @return einen Cache der zulässigen Jahrgänge je Kategorie in dem angegebenen Schuljahr.
	 */
	private static @NotNull Map<KAOAKategorie, List<Jahrgaenge>> cacheListJahrgaengeBySchuljahrAndKategorie(final int schuljahr) {
		final Map<KAOAKategorie, List<Jahrgaenge>> cache = new HashMap<>();
		// Erzeuge für jede Kategorie die gültigen Jahrgänge und füge sie zum Cache hinzu
		for (final KAOAKategorie kategorie : KAOAKategorie.data().getWerte()) {
			final KAOAKategorieKatalogEintrag kategorieHistorienEintrag = kategorie.daten(schuljahr);
			final List<Jahrgaenge> jahrgaenge = new ArrayList<>();
			if (kategorieHistorienEintrag != null) {
				for (final String jgBezeichner : kategorieHistorienEintrag.jahrgaenge) {
					final Jahrgaenge jahrgang = Jahrgaenge.data().getWertByBezeichner(jgBezeichner);
					jahrgaenge.add(jahrgang);
				}
			}
			cache.put(kategorie, jahrgaenge);
		}
		return cache;
	}

	/**
	 * Liefert alle zulässigen KAoA-Kategorie-Historien-Einträge für den angegebenen Jahrgang in dem angegebenen Schuljahr zurück.
	 * Dabei wird intern für das Schuljahr ein Cache aufgebaut, dass nachfolgende Zugriffe auf das gleiche Schuljahr direkt aus
	 * dem Cache bedient werden können.
	 *
	 * @param schuljahr    das Schuljahr
	 * @param idJahrgang   der Jahrgang
	 *
	 * @return alle zulässigen KAoA-Kategorie-Historien-Einträge für den angegebenen Jahrgang in dem angegebenen Schuljahr.
	 */
	public static @NotNull List<KAOAKategorieKatalogEintrag> getEintraegeBySchuljahrAndIdJahrgang(final int schuljahr, final long idJahrgang) {
		// Bestimme die Schuljahres-spezifische Map aus dem Cache. Ist diese nicht vorhanden, so muss der Cache später neu aufgebaut werden.
		Map<Long, List<KAOAKategorieKatalogEintrag>> mapEintraegeByJahrgaenge = _mapEintraegeBySchuljahrAndJahrgang.get(schuljahr);
		// Die Map ist vorhanden, weshalb der Zugriff aus dem Cache möglich ist.
		if (mapEintraegeByJahrgaenge != null) {
			final List<KAOAKategorieKatalogEintrag> result = mapEintraegeByJahrgaenge.get(idJahrgang);
			return (result != null) ? result : new ArrayList<>();
		}
		// Die Map ist nicht vorhanden, erstelle daher den Cache für das Schuljahr.
		mapEintraegeByJahrgaenge = cacheEintraegeBySchuljahrAndIdJahrgang(schuljahr);
		_mapEintraegeBySchuljahrAndJahrgang.put(schuljahr, mapEintraegeByJahrgaenge);
		// Rückgabe des Ergebnisses nach dem Aufbau des Caches.
		final List<KAOAKategorieKatalogEintrag> result = mapEintraegeByJahrgaenge.get(idJahrgang);
		return (result != null) ? result : new ArrayList<>();
	}

	/**
	 * Liefert einen Cache der zulässigen KAoA-Kategorie-Historien-Einträge je Jahrgang in dem angegebenen Schuljahr zurück.
	 *
	 * @param schuljahr   das Schuljahr
	 * @return einen Cache der zulässigen KAoA-Anschlussoption-Historien-Einträge je Jahrgang in dem angegebenen Schuljahr.
	 */
	private static @NotNull Map<Long, List<KAOAKategorieKatalogEintrag>> cacheEintraegeBySchuljahrAndIdJahrgang(final int schuljahr) {
		final Map<Long, List<KAOAKategorieKatalogEintrag>> cache = new HashMap<>();

		// Erstelle für jede ID der Einträge in den Jahrgängen für das angegebene Schuljahr einen Map-Eintrag mit der Liste der möglichen KAoA-Katalog-Einträge
		for (final JahrgaengeKatalogEintrag jahrgangHistorienEintrag : Jahrgaenge.data().getEintraegeBySchuljahr(schuljahr)) {
			final List<KAOAKategorieKatalogEintrag> result = new ArrayList<>();
			for (final KAOAKategorieKatalogEintrag kategorieHistorienEintrag : KAOAKategorie.data().getEintraegeBySchuljahr(schuljahr)) {
				// Prüfe für den den KAOA-Kategorie-Eintrag-ob dieser in dem Jahrgang zuläessig ist
				for (final String bezeichner : kategorieHistorienEintrag.jahrgaenge) {
					final JahrgaengeKatalogEintrag jahrgangEintragDerKategorie = Jahrgaenge.data().getWertByBezeichner(bezeichner).daten(schuljahr);
					if ((jahrgangEintragDerKategorie != null) && (jahrgangEintragDerKategorie.id == jahrgangHistorienEintrag.id)) {
						result.add(kategorieHistorienEintrag);
						break;
					}
				}
			}
			cache.put(jahrgangHistorienEintrag.id, result);
		}
		return cache;
	}

}
