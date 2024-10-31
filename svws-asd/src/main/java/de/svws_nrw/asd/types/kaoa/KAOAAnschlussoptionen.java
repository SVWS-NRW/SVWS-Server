package de.svws_nrw.asd.types.kaoa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.kaoa.KAOAAnschlussoptionenKatalogEintrag;
import de.svws_nrw.asd.data.kaoa.KAOAZusatzmerkmalKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die KAoA-Anschlussoptionen für Anschlussvereinbarungen nach SBO 10.7.
 */
public enum KAOAAnschlussoptionen implements CoreType<KAOAAnschlussoptionenKatalogEintrag, KAOAAnschlussoptionen> {

	/** KAoA-Anschlussoption: Berufspraxisstufe einer Förderschule für Geistige Entwicklung */
	BE,

	/** KAoA-Anschlussoption: Gymnasiale Oberstufe der Gesamtschule oder des Gymnasiums */
	GY,

	/** KAoA-Anschlussoption: Berufskolleg - Einjährige Ausbildungsvorbereitung in Vollzeit (AV) - Anlage A */
	AV,

	/** KAoA-Anschlussoption: Berufskolleg - Einjährige Berufsfachschule Typ I (BFS I) - Anlage B */
	BFSI,

	/** KAoA-Anschlussoption: Berufskolleg - Einjährige Berufsfachschule Typ II (BFS II) - Anlage B */
	BFSII,

	/** KAoA-Anschlussoption: Berufskolleg - Zweijährige [Höhere] Berufsfachschule - Anlage C */
	HOEBFS,

	/** KAoA-Anschlussoption: Berufskolleg - Zweijährige Fachoberschule (FOS 11/12) - Anlage C */
	FOS,

	/** KAoA-Anschlussoption: Berufskolleg - Berufliches Gymnasium - Anlage D */
	BERUFGY,

	/** KAoA-Anschlussoption: Duale Berufsausbildung (inkl. Beamtenlaufbahn im mittleren Dienst) */
	DUAL,

	/** KAoA-Anschlussoption: Berufskolleg - schulische Ausbildung zwei- oder dreijährig in der Berufsfachschule Anlage B oder C oder am Beruflichen Gymnasium */
	SCHUAUS,

	/** KAoA-Anschlussoption: Schulische Ausbildung in Berufen des Gesundheitswesens und der Altenpflege */
	SCHUGA,

	/** KAoA-Anschlussoption: Berufsausbildung in einer außerbetrieblichen Einrichtung (BaE kooperativ/integrativ) */
	BAE,

	/** KAoA-Anschlussoption: Betriebliche Ausbildung in gesondert geregelten Berufen (Fachpraktikerausbildung/Werkerausbildung) für Jugendliche mit Behinderung */
	FACHAUS,

	/** KAoA-Anschlussoption: Berufsvorbereitende Bildungsmaßnahme der Agentur für Arbeit (BvB), auch rehaspezifisch */
	BVB,

	/** KAoA-Anschlussoption: Einstiegsqualifizierung (EQ und EQ plus) */
	EQ,

	/** KAoA-Anschlussoption: Werkstattjahr.NRW */
	WERK,

	/** KAoA-Anschlussoption: Jugendwerkstatt */
	JUWERK,

	/** KAoA-Anschlussoption: weitere Maßnahmen gemäß SGB II/III/VIII (z. B. Aktivierungshilfen, Projekte der Jugendberufshilfe)  */
	MASS,

	/** KAoA-Anschlussoption: Abendrealschule oder VHS zum Nachholen des Schulabschlusses */
	VHS,

	/** KAoA-Anschlussoption: betriebliche Langzeitpraktika (ohne EQ) (z.B. zum Erwerb der vollen FHR) */
	PRAKT,

	/** KAoA-Anschlussoption: Freiwilligendienste, Freiwilliger Wehrdienst/Laufbahn Bundeswehr und ähnliche Anschlussoptionen */
	FREI,

	/** KAoA-Anschlussoption: Sozialversicherungspflichtige Beschäftigung als ungelernte Kraft */
	SVP,

	/** KAoA-Anschlussoption: Außerbetriebliche Ausbildung für Menschen mit Behinderung im Berufsbildungswerk (BBW) */
	BBW,

	/** KAoA-Anschlussoption: Begleitete betriebliche Ausbildung (bbA) für Menschen mit Behinderung */
	BBA,

	/** KAoA-Anschlussoption: Unterstützte Beschäftigung (UB) für Menschen mit Behinderung */
	UB,

	/** KAoA-Anschlussoption: Diagnose der Arbeitsmarktfähigkeit besonders betroffener behinderter Menschen (DIA-AM) */
	DIAAM,

	/** KAoA-Anschlussoption: Maßnahmen im Eingangsverfahren und im Berufsbildungsbereich in Werkstätten für behinderte Menschen (WfbM) */
	WFBM,

	/** KAoA-Anschlussoption: Betreuung in Tagesförderstätten für schwerst- und schwermehrfachbehinderte Menschen */
	TAG,

	/** KAoA-Anschlussoption: Verbleib zu Hause bei einer Schwerst- und Schwermehrfachbehinderung */
	HAUSE,

	/** KAoA-Anschlussoption: weitere rehaspezifische Maßnahmen für Menschen mit Behinderung */
	REHA,

	/** KAoA-Anschlussoption: Minijob */
	MINI,

	/** KAoA-Anschlussoption: Schwangerschaft/Elternzeit */
	SCHWAN,

	/** KAoA-Anschlussoption: Schulabsenz, daher Verbleib unbekannt */
	ABSENZ,

	/** KAoA-Anschlussoption: Verbleib unbekannt - andere Gründe */
	UNBE,

	/** KAoA-Anschlussoption: Noch kein Anschluss */
	NOKEAN,

	/** KAoA-Anschlussoption: Duales Studium (inkl. Beamtenausbildung im gehobenen Dienst) */
	DUASTUD,

	/** KAoA-Anschlussoption: Hochschulstudium */
	STUD;

	/* ----- Die nachfolgenden Attribute werden nicht initialisiert und werden als Cache verwendet, um z.B. den Schuljahres-bezogenen Zugriff zu cachen ----- */

	/** (Integer, Long) -> Schuljahr, idZusatzmerkmal */
	private static final @NotNull Map<Integer, Map<Long, List<KAOAAnschlussoptionenKatalogEintrag>>> _mapEintraegeBySchuljahrAndZusatzmerkmal = new HashMap<>();


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<KAOAAnschlussoptionenKatalogEintrag, KAOAAnschlussoptionen> manager) {
		CoreTypeDataManager.putManager(KAOAAnschlussoptionen.class, manager);
		_mapEintraegeBySchuljahrAndZusatzmerkmal.clear();
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<KAOAAnschlussoptionenKatalogEintrag, KAOAAnschlussoptionen> data() {
		return CoreTypeDataManager.getManager(KAOAAnschlussoptionen.class);
	}

	/**
	 * Liefert alle zulässigen KAoA-Anschlussoption-Historien-Einträge für das angegebene Zusatzmerkmal in dem angegebenen Schuljahr zurück.
	 * Dabei wird intern für das Schuljahr ein Cache aufgebaut, dass nachfolgende Zugriffe auf das gleiche Schuljahr direkt aus
	 * dem Cache bedient werden können.
	 *
	 * @param schuljahr         das Schuljahr
	 * @param idZusatzmerkmal   die id des KAoA-Zusatzmerkmal-Historien-Eintrags
	 *
	 * @return alle zulässigen KAoA-Anschlussoption-Historien-Einträge für das angegebene Zusatzmerkmal in dem angegebenen Schuljahr.
	 */
	public static @NotNull List<KAOAAnschlussoptionenKatalogEintrag> getEintraegeBySchuljahrAndIdZusatzmerkmal(final int schuljahr,
			final long idZusatzmerkmal) {
		// Bestimme die Schuljahres-spezifische Map aus dem Cache. Ist diese nicht vorhanden, so muss der Cache später neu aufgebaut werden.
		Map<Long, List<KAOAAnschlussoptionenKatalogEintrag>> mapEintraegeByZusatzmerkmal = _mapEintraegeBySchuljahrAndZusatzmerkmal.get(schuljahr);
		// Die Map ist vorhanden, weshalb der Zugriff aus dem Cache möglich ist.
		if (mapEintraegeByZusatzmerkmal != null)
			return getAnschlussoptionHistorienEintraegeFromCache(mapEintraegeByZusatzmerkmal, idZusatzmerkmal);
		// Falls der Cache nicht vorhanden ist, wird er erstellt.
		mapEintraegeByZusatzmerkmal = cacheEintraegeBySchuljahrAndIdZusatzmerkmal(schuljahr);
		_mapEintraegeBySchuljahrAndZusatzmerkmal.put(schuljahr, mapEintraegeByZusatzmerkmal);
		// Rückgabe des Ergebnisses nach dem Aufbau des Caches.
		return getAnschlussoptionHistorienEintraegeFromCache(mapEintraegeByZusatzmerkmal, idZusatzmerkmal);
	}

	/**
	 * Liefert einen Cache der zulässigen KAoA-Anschlussoption-Historien-Einträge je Zusatzmerkmal in dem angegebenen Schuljahr zurück.
	 *
	 * @param schuljahr   das Schuljahr
	 * @return einen Cache der zulässigen KAoA-Anschlussoption-Historien-Einträge je Zusatzmerkmal in dem angegebenen Schuljahr.
	 */
	private static @NotNull Map<Long, List<KAOAAnschlussoptionenKatalogEintrag>> cacheEintraegeBySchuljahrAndIdZusatzmerkmal(final int schuljahr) {
		final Map<Long, List<KAOAAnschlussoptionenKatalogEintrag>> cache = new HashMap<>();
		final List<KAOAZusatzmerkmal> zusatzmerkmale = KAOAZusatzmerkmal.data().getWerte();
		final List<KAOAAnschlussoptionen> anschlussoptionen = KAOAAnschlussoptionen.data().getWerte();

		// Füge die Einträge zur Cache-Map hinzu, sofern sie im gegebenen Schuljahr gültig sind.
		for (final KAOAZusatzmerkmal zusatzmerkmal : zusatzmerkmale) {
			final KAOAZusatzmerkmalKatalogEintrag zusatzmerkmalHistorienEintrag = zusatzmerkmal.daten(schuljahr);
			if (zusatzmerkmalHistorienEintrag == null)
				continue;
			final List<KAOAAnschlussoptionenKatalogEintrag> result = new ArrayList<>();
			// Iteriere durch die Anschlussoptionen und füge die zulässigen zur Ergebnisliste hinzu.
			for (final KAOAAnschlussoptionen anschlussoption : anschlussoptionen) {
				final KAOAAnschlussoptionenKatalogEintrag anschlussoptionHistorienEintrag = anschlussoption.daten(schuljahr);
				if ((anschlussoptionHistorienEintrag != null) && anschlussoptionHistorienEintrag.anzeigeZusatzmerkmal.contains(zusatzmerkmal.name()))
					result.add(anschlussoptionHistorienEintrag);
			}
			cache.put(zusatzmerkmalHistorienEintrag.id, result);
		}
		return cache;
	}

	private static @NotNull List<KAOAAnschlussoptionenKatalogEintrag> getAnschlussoptionHistorienEintraegeFromCache(final @NotNull Map<Long, List<KAOAAnschlussoptionenKatalogEintrag>> cache,
			final long idZusatzmerkmal) {
		final List<KAOAAnschlussoptionenKatalogEintrag> result = cache.get(idZusatzmerkmal);
		return (result != null) ? result : new ArrayList<>();
	}
}
