package de.svws_nrw.asd.types.kaoa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.kaoa.KAOAKategorieKatalogEintrag;
import de.svws_nrw.asd.data.kaoa.KAOAMerkmalKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die KAOA-Merkmale.
 */
public enum KAOAMerkmal implements CoreType<KAOAMerkmalKatalogEintrag, KAOAMerkmal> {

	/** KAoA-Merkmal: Schulische prozessorientierte Begleitung und Beratung */
	SBO_2_1,

	/** KAoA-Merkmal: Berufsorientierende Angebote der Berufsberatung der Bundesagentur für Arbeit (BA) */
	SBO_2_2,

	/** KAoA-Merkmal: Individuelle Beratungsangebote außerschulischer Partner */
	SBO_2_3,

	/** KAoA-Merkmal: STAR - Berufswegekonferenz */
	SBO_2_4,

	/** KAoA-Merkmal: Einbindung von Eltern bzw. Erziehungsberechtigten */
	SBO_2_5,

	/** KAoA-Merkmal: STAR - Einbindung von Eltern bzw. Erziehungsberechtigten */
	SBO_2_6,

	/** KAoA-Merkmal: Portfolioinstrument */
	SBO_3_4,

	/** KAoA-Merkmal: Potenzialanalyse 1-tägig */
	SBO_4_1,

	/** KAoA-Merkmal: Potenzialanalyse für Schülerinnen und Schüler mit den Förderschwerpunkten Lernen und Emotionale soziale Entwicklung– 2-tägig */
	SBO_4_2,

	/** KAoA-Merkmal: STAR – Potenzialanalyse für Schülerinnen und Schüler mit den Förderschwerpunkten Geistige Entwicklung, Körperliche und motorische Entwicklung, Hören und Kommunikation und Sprache - 2-tägig */
	SBO_4_3,

	/** KAoA-Merkmal: STAR – Feststellung des funktionalen Sehvermögens im Förderschwerpunkt Sehen */
	SBO_4_4,

	/** KAoA-Merkmal: STAR – Potenzialanalyse im Förderschwerpunkt Sehen – 2-tägig */
	SBO_4_5,

	/** KAoA-Merkmal: Berufsfelderkundungen */
	SBO_5_1,

	/** KAoA-Merkmal: STAR – Berufsfelderkundungen */
	SBO_5_2,

	/** KAoA-Merkmal: STAR – Arbeitsplatzbezogenes Kommunikationstraining I im Förderschwerpunkt Hören und Kommunikation */
	SBO_5_3,

	/** KAoA-Merkmal: STAR - Berufsorientierungsseminar */
	SBO_5_4,

	/** KAoA-Merkmal: Betriebspraktika in der Sekundarstufe I (ggf. 1 Woche verlagert aus der Oberstufe) */
	SBO_6_1,

	/** KAoA-Merkmal: STAR – Intensivtraining arbeitsrelevanter sozialer Kompetenzen (TASK) */
	SBO_6_2,

	/** KAoA-Merkmal: STAR – Betriebspraktikum im Block */
	SBO_6_3,

	/** KAoA-Merkmal: Praxiskurse */
	SBO_6_4,

	/** KAoA-Merkmal: Langzeitpraktikum */
	SBO_6_5,

	/** KAoA-Merkmal: STAR – Betriebspraktikum in Langzeit */
	SBO_6_6,

	/** KAoA-Merkmal: KAoA-kompakt */
	SBO_7_1,

	/** KAoA-Merkmal: Standortbestimmung - Reflexionsworkshop Sek. II */
	SBO_8_1,

	/** KAoA-Merkmal: Stärkung der Entscheidungskompetenz I – Sek. II */
	SBO_8_2,

	/** KAoA-Merkmal: Praxiselemente in Betrieben, Hochschulen, Institutionen */
	SBO_9_1,

	/** KAoA-Merkmal: Studienorientierung */
	SBO_9_2,

	/** KAoA-Merkmal: Stärkung der Entscheidungskompetenz II - Sek. II */
	SBO_9_3,

	/** KAoA-Merkmal: Bewerbungsphase */
	SBO_10_1,

	/** KAoA-Merkmal: STAR – Arbeitsplatzbezogenes Kommunikationstraining II im Förderschwerpunkt Hören und Kommunikation */
	SBO_10_2,

	/** KAoA-Merkmal: STAR – Betriebsnahes Bewerbungstraining/Umgang mit Dolmetschenden und Technik im Förderschwerpunkt Hören und Kommunikation */
	SBO_10_3,

	/** KAoA-Merkmal: Übergangsbegleitung */
	SBO_10_4,

	/** KAoA-Merkmal: STAR - Übergangsbegleitung */
	SBO_10_5,

	/** KAoA-Merkmal: Anschlussvereinbarung */
	SBO_10_6,

	/** KAoA-Merkmal: Koordinierte Übergangsgestaltung */
	SBO_10_7;

	/* ----- Die nachfolgenden Attribute werden nicht initialisiert und werden als Cache verwendet, um z.B. den Schuljahres-bezogenen Zugriff zu cachen ----- */

	/** (Integer, Long) -> Schuljahr, idKategorie */
	private static final @NotNull Map<Integer, Map<Long, List<KAOAMerkmalKatalogEintrag>>> _mapEintraegeBySchuljahrAndKategorie = new HashMap<>();

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<KAOAMerkmalKatalogEintrag, KAOAMerkmal> manager) {
		CoreTypeDataManager.putManager(KAOAMerkmal.class, manager);
		_mapEintraegeBySchuljahrAndKategorie.clear();
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<KAOAMerkmalKatalogEintrag, KAOAMerkmal> data() {
		return CoreTypeDataManager.getManager(KAOAMerkmal.class);
	}


	/**
	 * Liefert alle zulässigen KAoA-Merkmal-Historien-Einträge für die angegebene Kategorie in dem angegebenen Schuljahr zurück.
	 * Dabei wird intern für das Schuljahr ein Cache aufgebaut, dass nachfolgende Zugriffe auf das gleiche Schuljahr direkt aus
	 * dem Cache bedient werden können.
	 *
	 * @param schuljahr     das Schuljahr
	 * @param idKategorie   die id des KAoA-Kategorie-Historien-Eintrags
	 *
	 * @return alle zulässigen KAoA-Merkmal-Historien-Einträge für die angegebene Kategorie in dem angegebenen Schuljahr.
	 */
	public static @NotNull List<KAOAMerkmalKatalogEintrag> getEintraegeBySchuljahrAndIdKategorie(final int schuljahr, final long idKategorie) {
		// Bestimme die Schuljahres-spezifische Map aus dem Cache. Ist diese nicht vorhanden, so muss der Cache später neu aufgebaut werden.
		Map<Long, List<KAOAMerkmalKatalogEintrag>> mapEintraegeByKategorie = _mapEintraegeBySchuljahrAndKategorie.get(schuljahr);
		// Die Map ist vorhanden, weshalb der Zugriff aus dem Cache möglich ist.
		if (mapEintraegeByKategorie != null)
			return getMerkmalHistorienEintraegeFromCache(mapEintraegeByKategorie, idKategorie);
		// Falls der Cache nicht vorhanden ist, wird er erstellt.
		mapEintraegeByKategorie = cacheEintraegeBySchuljahrAndIdKategorie(schuljahr);
		_mapEintraegeBySchuljahrAndKategorie.put(schuljahr, mapEintraegeByKategorie);
		// Rückgabe des Ergebnisses nach dem Aufbau des Caches.
		return getMerkmalHistorienEintraegeFromCache(mapEintraegeByKategorie, idKategorie);
	}

	/**
	 * Liefert einen Cache der zulässigen KAoA-Merkmal-Historien-Einträge je Kategorie in dem angegebenen Schuljahr zurück.
	 *
	 * @param schuljahr   das Schuljahr
	 * @return einen Cache der zulässigen KAoA-Merkmal-Historien-Einträge je Kategorie in dem angegebenen Schuljahr.
	 */
	private static @NotNull Map<Long, List<KAOAMerkmalKatalogEintrag>> cacheEintraegeBySchuljahrAndIdKategorie(final int schuljahr) {
		final Map<Long, List<KAOAMerkmalKatalogEintrag>> cache = new HashMap<>();
		final List<KAOAKategorie> kategorien = KAOAKategorie.data().getWerte();
		final List<KAOAMerkmal> merkmale = KAOAMerkmal.data().getWerte();

		// Füge die Einträge zur Cache-Map hinzu, sofern sie im gegebenen Schuljahr gültig sind.
		for (final KAOAKategorie kategorie : kategorien) {
			final KAOAKategorieKatalogEintrag kategorieHistorienEintrag = kategorie.daten(schuljahr);
			if (kategorieHistorienEintrag == null)
				continue;
			// Iteriere durch die Merkmale und füge die zulässigen zur Ergebnisliste hinzu.
			final List<KAOAMerkmalKatalogEintrag> result = new ArrayList<>();
			for (final KAOAMerkmal merkmal : merkmale) {
				final KAOAMerkmalKatalogEintrag merkmalHistorienEintrag = merkmal.daten(schuljahr);
				if ((merkmalHistorienEintrag != null) && (merkmalHistorienEintrag.kategorie.equals(kategorie.name())))
					result.add(merkmalHistorienEintrag);
			}
			cache.put(kategorieHistorienEintrag.id, result);
		}
		return cache;
	}

	private static @NotNull List<KAOAMerkmalKatalogEintrag> getMerkmalHistorienEintraegeFromCache(final @NotNull Map<Long, List<KAOAMerkmalKatalogEintrag>> cache,
			final long idKategorie) {
		final List<KAOAMerkmalKatalogEintrag> result = cache.get(idKategorie);
		return (result != null) ? result : new ArrayList<>();
	}
}
