package de.svws_nrw.asd.types.kaoa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.kaoa.KAOAMerkmalKatalogEintrag;
import de.svws_nrw.asd.data.kaoa.KAOAZusatzmerkmalKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die KAOA-Zusatzmerkmale.
 */
public enum KAOAZusatzmerkmal implements CoreType<KAOAZusatzmerkmalKatalogEintrag, KAOAZusatzmerkmal> {

	/** KAoA-Zusatzmerkmal: Schulisches individuelles Beratungsgespräch durchgeführt */
	SBO_2_1_1,

	/** KAoA-Zusatzmerkmal: Schulisches individuelles Beratungsgespräch nicht durchgeführt */
	SBO_2_1_2,

	/** KAoA-Zusatzmerkmal: Teilnahme an einem berufsorientierenden Angebote der Berufsberatung */
	SBO_2_2_1,

	/** KAoA-Zusatzmerkmal: Keine Teilnahme an einem berufsorientierenden Angebote der Berufsberatung */
	SBO_2_2_2,

	/** KAoA-Zusatzmerkmal: Berufsberatung der Agentur für Arbeit (BA) */
	SBO_2_3_1,

	/** KAoA-Zusatzmerkmal: Beratungsgespräch der Jugendberufsagentur */
	SBO_2_3_2,

	/** KAoA-Zusatzmerkmal: Beratungsgespräch des Jobcenters */
	SBO_2_3_3,

	/** KAoA-Zusatzmerkmal: Beratungsgespräch Jugendsozialarbeit */
	SBO_2_3_4,

	/** KAoA-Zusatzmerkmal: Beratungsgespräch eines anderen außerschulischen Partners */
	SBO_2_3_5,

	/** KAoA-Zusatzmerkmal: Kein Beratungskontakt zu einem außerschulischen Partner */
	SBO_2_3_6,

	/** KAoA-Zusatzmerkmal: STAR - Berufswegekonferenz durchgeführt */
	SBO_2_4_1,

	/** KAoA-Zusatzmerkmal: STAR - Berufswegekonferenz nicht durchgeführt */
	SBO_2_4_2,

	/** KAoA-Zusatzmerkmal: Teilnahme der Eltern bzw. Erziehungsberechtigten an Beratungs- und Informationsveranstaltungen */
	SBO_2_5_1,

	/** KAoA-Zusatzmerkmal: Keine Teilnahme der Eltern bzw. Erziehungsberechtigten an Beratungs- und Informationsveranstaltungen */
	SBO_2_5_2,

	/** KAoA-Zusatzmerkmal: Teilnahme der Eltern bzw. Erziehungsberechtigten an STAR - Beratungs- und Informationsveranstaltungen */
	SBO_2_6_1,

	/** KAoA-Zusatzmerkmal: Keine Teilnahme der Eltern bzw. Erziehungsberechtigten an STAR - Beratungs- und Informationsveranstaltungen */
	SBO_2_6_2,

	/** KAoA-Zusatzmerkmal: Berufwahlpass NRW SekI/II erhalten */
	SBO_3_4_1,

	/** KAoA-Zusatzmerkmal: Berufwahlpass NRW Leichte Sprache erhalten */
	SBO_3_4_2,

	/** KAoA-Zusatzmerkmal: Berufswahlpass NRW kompakt erhalten */
	SBO_3_4_3,

	/** KAoA-Zusatzmerkmal: anderes Portfolioinstrument  erhalten */
	SBO_3_4_4,

	/** KAoA-Zusatzmerkmal: kein Portfolioinstrument erhalten */
	SBO_3_4_5,

	/** KAoA-Zusatzmerkmal: An der Potenzialanalyse teilgenommen */
	SBO_4_1_1,

	/** KAoA-Zusatzmerkmal: Keine Teilnahme an der Potenzialanalyse */
	SBO_4_1_2,

	/** KAoA-Zusatzmerkmal: An der Potenzialanalyse teilgenommen */
	SBO_4_2_1,

	/** KAoA-Zusatzmerkmal: Keine Teilnahme an der Potenzialanalyse */
	SBO_4_2_2,

	/** KAoA-Zusatzmerkmal: An der STAR-Potenzialanalyse teilgenommen */
	SBO_4_3_1,

	/** KAoA-Zusatzmerkmal: Keine Teilnahme an der Potenzialanalyse */
	SBO_4_3_2,

	/** KAoA-Zusatzmerkmal: An der Festellung des funktionalen Sehvermögens teilgenommen */
	SBO_4_4_1,

	/** KAoA-Zusatzmerkmal: Keine Teilnahme an der Feststellung des funktionalen Sehrvermögens */
	SBO_4_4_2,

	/** KAoA-Zusatzmerkmal: An der STAR-Potenzialanalyse teilgenommen */
	SBO_4_5_1,

	/** KAoA-Zusatzmerkmal: Keine Teilnahme an der Potenzialanalyse */
	SBO_4_5_2,

	/** KAoA-Zusatzmerkmal: An der betrieblichen Berufsfelderkundung teilgenommen - 1. Tag */
	SBO_5_1_1,

	/** KAoA-Zusatzmerkmal: An der betrieblichen Berufsfelderkundung teilgenommen - 2. Tag */
	SBO_5_1_2,

	/** KAoA-Zusatzmerkmal: An der betrieblichen Berufsfelderkundung teilgenommen - 3. Tag */
	SBO_5_1_3,

	/** KAoA-Zusatzmerkmal: An der betrieblichen Berufsfelderkundung teilgenommen - mehr als drei Tage */
	SBO_5_1_4,

	/** KAoA-Zusatzmerkmal: An der trägergestützten Berufsfelderkundung teilgenommen - 1. Tag */
	SBO_5_1_5,

	/** KAoA-Zusatzmerkmal: An der trägergestützten Berufsfelderkundung teilgenommen - 2. Tag */
	SBO_5_1_6,

	/** KAoA-Zusatzmerkmal: An der trägergestützten Berufsfelderkundung teilgenommen - 3. Tag */
	SBO_5_1_7,

	/** KAoA-Zusatzmerkmal: Keine Teilnahme an den betrieblichen oder trägergestütztenBerufsfelderkundungen */
	SBO_5_1_8,

	/** KAoA-Zusatzmerkmal: An der betrieblichen STAR - Berufsfelderkundung teilgenommen - 1. Tag */
	SBO_5_2_1,

	/** KAoA-Zusatzmerkmal: An der betrieblichen STAR - Berufsfelderkundung teilgenommen - 2. Tag */
	SBO_5_2_2,

	/** KAoA-Zusatzmerkmal: An der betrieblichen STAR - Berufsfelderkundung teilgenommen - 3. Tag */
	SBO_5_2_3,

	/** KAoA-Zusatzmerkmal: An der betrieblichen STAR - Berufsfelderkundung teilgenommen - weitere Tage */
	SBO_5_2_4,

	/** KAoA-Zusatzmerkmal: An der trägergestützten STAR - Berufsfelderkundung teilgenommen - 1. Tag */
	SBO_5_2_5,

	/** KAoA-Zusatzmerkmal: An der trägergestützten STAR - Berufsfelderkundung teilgenommen - 2. Tag */
	SBO_5_2_6,

	/** KAoA-Zusatzmerkmal: An der trägergestützten STAR - Berufsfelderkundung teilgenommen - 3. Tag */
	SBO_5_2_7,

	/** KAoA-Zusatzmerkmal: Keine Teilnahme an den betrieblichen oder trägergestützten STAR - Berufsfelderkundungen */
	SBO_5_2_8,

	/** KAoA-Zusatzmerkmal: Am STAR - Arbeitsplatzbezogenes Kommunikationstraining I teilgenommen */
	SBO_5_3_1,

	/** KAoA-Zusatzmerkmal: Keine Teilnahme am STAR - Arbeitsplatzbezogenes Kommunikationstraining I */
	SBO_5_3_2,

	/** KAoA-Zusatzmerkmal: Am STAR - Berufsorientierungsseminar teilgenommen */
	SBO_5_4_1,

	/** KAoA-Zusatzmerkmal: Keine Teilnahme am STAR - Berufsorientierungsseminar */
	SBO_5_4_2,

	/** KAoA-Zusatzmerkmal: Am Betriebspraktikum teilgenommen - 1 Woche */
	SBO_6_1_1,

	/** KAoA-Zusatzmerkmal: Am Betriebspraktikum teilgenommen - 2 Wochen */
	SBO_6_1_2,

	/** KAoA-Zusatzmerkmal: Am Betriebspraktikum Sek I teilgenommen - mehr als zwei Wochen */
	SBO_6_1_3,

	/** KAoA-Zusatzmerkmal: Zusätzliche Praktika wie z. B. Schnupperpraktika */
	SBO_6_1_4,

	/** KAoA-Zusatzmerkmal: Keine Teilnahme am Betriebspraktikum */
	SBO_6_1_5,

	/** KAoA-Zusatzmerkmal: Keine Teilnahme am Intensivtraining TASK */
	SBO_6_2_2,

	/** KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum im Block - eine Woche teilgenommen */
	SBO_6_3_1,

	/** KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum im Block - zwei Wochen teilgenommen */
	SBO_6_3_2,

	/** KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum im Block - drei Wochen teilgenommen */
	SBO_6_3_3,

	/** KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum im Block - vier Wochen teilgenommen */
	SBO_6_3_4,

	/** KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum im Block - fünf Wochen teilgenommen */
	SBO_6_3_5,

	/** KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum im Block - sechs Wochen teilgenommen */
	SBO_6_3_6,

	/** KAoA-Zusatzmerkmal: Keine Teilnahme am STAR - Betriebspraktikum im Block */
	SBO_6_3_7,

	/** KAoA-Zusatzmerkmal: An trägergestützten Praxiskursen teilgenommen */
	SBO_6_4_1,

	/** KAoA-Zusatzmerkmal: An betrieblichen Praxiskursen teilgenommen */
	SBO_6_4_2,

	/** KAoA-Zusatzmerkmal: Keine Teilnahme an Praxiskursen */
	SBO_6_4_3,

	/** KAoA-Zusatzmerkmal: Am Langzeitpraktikum teilgenommen */
	SBO_6_5_1,

	/** KAoA-Zusatzmerkmal: Langzeitpraktikum abgebrochen */
	SBO_6_5_2,

	/** KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum in Langzeit 1-tägig teilgenommen */
	SBO_6_6_1,

	/** KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum in Langzeit 2-tägig teilgenommen */
	SBO_6_6_2,

	/** KAoA-Zusatzmerkmal: STAR - Betriebspraktikum in Langzeit abgebrochen */
	SBO_6_6_3,

	/** KAoA-Zusatzmerkmal: An KAoA-kompakt teilgenommen */
	SBO_7_1_1,

	/** KAoA-Zusatzmerkmal: Keine Teilnahme an KAoA-kompakt */
	SBO_7_1_2,

	/** KAoA-Zusatzmerkmal: Am Workshop "Standortbestimmung Reflexionsworkshop Sek. II" teilgenommen */
	SBO_8_1_1,

	/** KAoA-Zusatzmerkmal: Keine Teilnahme am Workshop "Standortbestimmung Reflexionsworkshop Sek. II" */
	SBO_8_1_2,

	/** KAoA-Zusatzmerkmal: Am Workshop "Stärkung der Entscheidungskompetenz I - Sek. II" teilgenommen */
	SBO_8_2_1,

	/** KAoA-Zusatzmerkmal: Keine Teilnahme am Workshop "Stärkung der Entscheidungskompetenz I - Sek. II" */
	SBO_8_2_2,

	/** KAoA-Zusatzmerkmal: An Praxiselementen in Betrieben, Hochschulen, Institutionen teilgenommen */
	SBO_9_1_1,

	/** KAoA-Zusatzmerkmal: Keine Teilnahme an den Praxiselementen */
	SBO_9_1_2,

	/** KAoA-Zusatzmerkmal: Am Betriebspraktikum teilgenommen - 1 Woche (Sek II)  */
	SBO_9_1_3,

	/** KAoA-Zusatzmerkmal: Am Betriebspraktikum teilgenommen - 2 Wochen (Sek II)  */
	SBO_9_1_4,

	/** KAoA-Zusatzmerkmal: Teilnahme an den Veranstaltungen zur Studienorientierung */
	SBO_9_2_1,

	/** KAoA-Zusatzmerkmal: Keine Teilnahme an den Veranstaltungen zur Studienorientierung */
	SBO_9_2_2,

	/** KAoA-Zusatzmerkmal: Am Workshop "Stärkung der Entscheidungskompetenz II - Sek II" teilgenommen */
	SBO_9_3_1,

	/** KAoA-Zusatzmerkmal: Keine Teilnahme am Workshop "Stärkung der Entscheidungskompetenz II - Sek II" */
	SBO_9_3_2,

	/** KAoA-Zusatzmerkmal: Bewerbungstraining wurde durchgeführt */
	SBO_10_1_1,

	/** KAoA-Zusatzmerkmal: Keine Teilnahme an einem Bewerbungstraining */
	SBO_10_1_2,

	/** KAoA-Zusatzmerkmal: Am STAR - Arbeitsplatzbezogenen Kommunikationstraining II teilgenommen */
	SBO_10_2_1,

	/** KAoA-Zusatzmerkmal: Keine Teilnahme am STAR - Arbeitsplatzbezogenen Kommunikationstraining II */
	SBO_10_2_2,

	/** KAoA-Zusatzmerkmal: Am STAR - Bewerbungstraining teilgenommen */
	SBO_10_3_1,

	/** KAoA-Zusatzmerkmal: Keine Teilnahme am STAR - Bewerbungstraining */
	SBO_10_3_2,

	/** KAoA-Zusatzmerkmal: durch die Jugendhilfe */
	SBO_10_4_1,

	/** KAoA-Zusatzmerkmal: durch die Schulsozialarbeit */
	SBO_10_4_2,

	/** KAoA-Zusatzmerkmal: durch die Berufseinstiegsbegleitung */
	SBO_10_4_3,

	/** KAoA-Zusatzmerkmal: durch die Einstiegsbegleitung über die Kommune finanziert */
	SBO_10_4_4,

	/** KAoA-Zusatzmerkmal: durch eine ehrenamtlich tätige Person */
	SBO_10_4_5,

	/** KAoA-Zusatzmerkmal: durch andere Institution */
	SBO_10_4_6,

	/** KAoA-Zusatzmerkmal: Eine STAR - Übergangsbegleitung durch den Integrationsfachdienst (IFD) findet statt */
	SBO_10_5_1,

	/** KAoA-Zusatzmerkmal: Keine STAR - Übergangsbegleitung durch den Integrationsfachdienst (IFD) */
	SBO_10_5_2,

	/** KAoA-Zusatzmerkmal: Die Anschlussvereinbarung Sek. I ist ausgefüllt worden */
	SBO_10_6_1,

	/** KAoA-Zusatzmerkmal: Die Anschlussvereinbarung Sek. I ist nicht ausgefüllt worden */
	SBO_10_6_2,

	/** KAoA-Zusatzmerkmal: Die Anschlussvereinbarung Sek. II ist ausgefüllt worden */
	SBO_10_6_3,

	/** KAoA-Zusatzmerkmal: Die Anschlussvereinbarung Sek. II ist nicht ausgefüllt worden */
	SBO_10_6_4,

	/** KAoA-Zusatzmerkmal: Anschlüsse bei einem Abgangszeugnis für Schüler/innen mit sonderpädagogischem Unterstützungsbedarf */
	SBO_10_7_1,

	/** KAoA-Zusatzmerkmal: Anschlüsse bei einem Abgangszeugnis */
	SBO_10_7_2,

	/** KAoA-Zusatzmerkmal: Anschlüsse bei einem Abschlusszeugnis im Bildungsgang Geistige Entwicklung */
	SBO_10_7_3,

	/** KAoA-Zusatzmerkmal: Anschlüsse bei einem Abschlusszeugnis im Bildungsgang Lernen */
	SBO_10_7_4,

	/** KAoA-Zusatzmerkmal: Anschlüsse bei einem Hauptschulabschluss (HA9) oder diesem gleichwertig */
	SBO_10_7_5,

	/** KAoA-Zusatzmerkmal: Anschlüsse bei einem Hauptschulabschluss nach Klasse 10 oder diesem gleichwertig */
	SBO_10_7_6,

	/** KAoA-Zusatzmerkmal: Anschlüsse bei einem Mittlerem Schulabschluss (FOR) */
	SBO_10_7_7,

	/** KAoA-Zusatzmerkmal: Anschlüsse bei einem Mittlerem Schulabschluss (FOR) mit der Qualifikation für die Oberstufe */
	SBO_10_7_8,

	/** KAoA-Zusatzmerkmal: Anschlüsse bei einem Hauptschulabschluss (HA9) mit der Qualifikation für die Oberstufe */
	SBO_10_7_9,

	/** KAoA-Zusatzmerkmal: Anschlüsse bei einem Hauptschulabschluss nach Klasse 10 mit der Qualifikation für die Oberstufe */
	SBO_10_7_10,

	/** KAoA-Zusatzmerkmal: Anschlüsse bei dem schulischen Teil der Fachhochschulreife */
	SBO_10_7_11,

	/** KAoA-Zusatzmerkmal: Anschlüsse mit der Fachhochschulreife */
	SBO_10_7_12,

	/** KAoA-Zusatzmerkmal: Anschlüsse mit der allgemeinen Hochschulreife */
	SBO_10_7_13;

	/* ----- Die nachfolgenden Attribute werden nicht initialisiert und werden als Cache verwendet, um z.B. den Schuljahres-bezogenen Zugriff zu cachen ----- */

	/** (Integer, Long) -> Schuljahr, idMerkmal */
	private static final @NotNull Map<Integer, Map<Long, List<KAOAZusatzmerkmalKatalogEintrag>>> _mapEintraegeBySchuljahrAndMerkmal = new HashMap<>();

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<KAOAZusatzmerkmalKatalogEintrag, KAOAZusatzmerkmal> manager) {
		CoreTypeDataManager.putManager(KAOAZusatzmerkmal.class, manager);
		_mapEintraegeBySchuljahrAndMerkmal.clear();
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<KAOAZusatzmerkmalKatalogEintrag, KAOAZusatzmerkmal> data() {
		return CoreTypeDataManager.getManager(KAOAZusatzmerkmal.class);
	}

	/**
	 * Liefert alle zulässigen KAoA-Zusatzmerkmal-Historien-Einträge für das angegebene Merkmal in dem angegebenen Schuljahr zurück.
	 * Dabei wird intern für das Schuljahr ein Cache aufgebaut, dass nachfolgende Zugriffe auf das gleiche Schuljahr direkt aus
	 * dem Cache bedient werden können.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param idMerkmal   die id des KAoA-Merkmal-Historien-Eintrags
	 *
	 * @return alle zulässigen KAoA-Zusatzmerkmal-Historien-Einträge für das angegebene Merkmal in dem angegebenen Schuljahr.
	 */
	public static @NotNull List<KAOAZusatzmerkmalKatalogEintrag> getEintraegeBySchuljahrAndIdMerkmal(final int schuljahr, final long idMerkmal) {
		// Bestimme die Schuljahres-spezifische Map aus dem Cache. Ist diese nicht vorhanden, so muss der Cache später neu aufgebaut werden.
		Map<Long, List<KAOAZusatzmerkmalKatalogEintrag>> mapEintraegeByMerkmal = _mapEintraegeBySchuljahrAndMerkmal.get(schuljahr);
		// Die Map ist vorhanden, weshalb der Zugriff aus dem Cache möglich ist.
		if (mapEintraegeByMerkmal != null) {
			final List<KAOAZusatzmerkmalKatalogEintrag> result = mapEintraegeByMerkmal.get(idMerkmal);
			return (result != null) ? result : new ArrayList<>();
		}
		// Falls der Cache nicht vorhanden ist, wird er erstellt.
		mapEintraegeByMerkmal = cacheEintraegeBySchuljahrAndIdMerkmal(schuljahr);
		_mapEintraegeBySchuljahrAndMerkmal.put(schuljahr, mapEintraegeByMerkmal);
		// Rückgabe des Ergebnisses nach dem Aufbau des Caches.
		final List<KAOAZusatzmerkmalKatalogEintrag> result = mapEintraegeByMerkmal.get(idMerkmal);
		return (result != null) ? result : new ArrayList<>();
	}

	/**
	 * Liefert einen Cache der zulässigen KAoA-Zusatzmerkmal-Historien-Einträge je Merkmal in dem angegebenen Schuljahr zurück.
	 *
	 * @param schuljahr   das Schuljahr
	 * @return einen Cache der zulässigen KAoA-Zusatzmerkmal-Historien-Einträge je Merkmal in dem angegebenen Schuljahr.
	 */
	private static @NotNull Map<Long, List<KAOAZusatzmerkmalKatalogEintrag>> cacheEintraegeBySchuljahrAndIdMerkmal(final int schuljahr) {
		final Map<Long, List<KAOAZusatzmerkmalKatalogEintrag>> cache = new HashMap<>();

		// Füge die Einträge zur Cache-Map hinzu, sofern sie im gegebenen Schuljahr gültig sind.
		for (final KAOAMerkmalKatalogEintrag merkmalHistorienEintrag : KAOAMerkmal.data().getEintraegeBySchuljahr(schuljahr)) {
			final List<KAOAZusatzmerkmalKatalogEintrag> result = new ArrayList<>();
			// Iteriere durch die Anschlussoptionen und füge die zulässigen zur Ergebnisliste hinzu.
			for (final KAOAZusatzmerkmalKatalogEintrag zusatzmerkmalHistorienEintrag : KAOAZusatzmerkmal.data().getEintraegeBySchuljahr(schuljahr))
				if (zusatzmerkmalHistorienEintrag.merkmal.equals(KAOAMerkmal.data().getWertByID(merkmalHistorienEintrag.id).name()))
					result.add(zusatzmerkmalHistorienEintrag);
			cache.put(merkmalHistorienEintrag.id, result);
		}
		return cache;
	}

}
