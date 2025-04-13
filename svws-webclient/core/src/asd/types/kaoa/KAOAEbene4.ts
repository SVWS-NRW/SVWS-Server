import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { ArrayList } from '../../../java/util/ArrayList';
import { KAOAEbene4KatalogEintrag } from '../../../asd/data/kaoa/KAOAEbene4KatalogEintrag';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';
import type { JavaMap } from '../../../java/util/JavaMap';
import { KAOAZusatzmerkmal } from '../../../asd/types/kaoa/KAOAZusatzmerkmal';

export class KAOAEbene4 extends JavaEnum<KAOAEbene4> implements CoreType<KAOAEbene4KatalogEintrag, KAOAEbene4> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<KAOAEbene4> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, KAOAEbene4> = new Map<string, KAOAEbene4>();

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Langzeitpraktikum 1-tägig
	 */
	public static readonly SBO_6_5_1_1 : KAOAEbene4 = new KAOAEbene4("SBO_6_5_1_1", 0, );

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Langzeitpraktikum 2-tägig
	 */
	public static readonly SBO_6_5_1_2 : KAOAEbene4 = new KAOAEbene4("SBO_6_5_1_2", 1, );

	/**
	 * KAoA-Eintrag der SBO Ebene 4: An der KAoA-kompakt Potenzialanalyse teilgenommen
	 */
	public static readonly SBO_7_1_1_1 : KAOAEbene4 = new KAOAEbene4("SBO_7_1_1_1", 2, );

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Keine Teilnahme an der KAoA-kompakt Potenzialanalyse
	 */
	public static readonly SBO_7_1_1_2 : KAOAEbene4 = new KAOAEbene4("SBO_7_1_1_2", 3, );

	/**
	 * KAoA-Eintrag der SBO Ebene 4: An der KAoA-kompakt Berufsfelderkundung teilgenommen - 1. Tag
	 */
	public static readonly SBO_7_1_1_3 : KAOAEbene4 = new KAOAEbene4("SBO_7_1_1_3", 4, );

	/**
	 * KAoA-Eintrag der SBO Ebene 4: An der KAoA-kompakt Berufsfelderkundung teilgenommen - 2. Tag
	 */
	public static readonly SBO_7_1_1_4 : KAOAEbene4 = new KAOAEbene4("SBO_7_1_1_4", 5, );

	/**
	 * KAoA-Eintrag der SBO Ebene 4: An der KAoA-kompakt Berufsfelderkundung teilgenommen - 3. Tag
	 */
	public static readonly SBO_7_1_1_5 : KAOAEbene4 = new KAOAEbene4("SBO_7_1_1_5", 6, );

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Keine Teilnahme an den KAoA-kompakt Berufsfelderkundungen
	 */
	public static readonly SBO_7_1_1_6 : KAOAEbene4 = new KAOAEbene4("SBO_7_1_1_6", 7, );

	/**
	 * KAoA-Eintrag der SBO Ebene 4: An KAoA-kompakt Praxiskursen teilgenommen
	 */
	public static readonly SBO_7_1_1_7 : KAOAEbene4 = new KAOAEbene4("SBO_7_1_1_7", 8, );

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Keine Teilnahme an den KAoA-kompakt Praxiskursen
	 */
	public static readonly SBO_7_1_1_8 : KAOAEbene4 = new KAOAEbene4("SBO_7_1_1_8", 9, );

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Einzeltag
	 */
	public static readonly SBO_9_1_1_1 : KAOAEbene4 = new KAOAEbene4("SBO_9_1_1_1", 10, );

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Betriebspraktikum
	 */
	public static readonly SBO_9_1_1_2 : KAOAEbene4 = new KAOAEbene4("SBO_9_1_1_2", 11, );

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Auslandspraktikum
	 */
	public static readonly SBO_9_1_1_3 : KAOAEbene4 = new KAOAEbene4("SBO_9_1_1_3", 12, );

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Hochschulpraktikum/Schnupperstudium
	 */
	public static readonly SBO_9_1_1_4 : KAOAEbene4 = new KAOAEbene4("SBO_9_1_1_4", 13, );

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Duales Orientierungspraktikum
	 */
	public static readonly SBO_9_1_1_5 : KAOAEbene4 = new KAOAEbene4("SBO_9_1_1_5", 14, );

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Projektworkshop ( bei einem Bildungsträger)
	 */
	public static readonly SBO_9_1_1_6 : KAOAEbene4 = new KAOAEbene4("SBO_9_1_1_6", 15, );

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Veranstaltungen zur allgemeinen Studienorientierung an einer Hochschule
	 */
	public static readonly SBO_9_2_1_1 : KAOAEbene4 = new KAOAEbene4("SBO_9_2_1_1", 16, );

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Veranstaltungen zur allgemeinen Studienorientierung in der Schule
	 */
	public static readonly SBO_9_2_1_2 : KAOAEbene4 = new KAOAEbene4("SBO_9_2_1_2", 17, );

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Wochen der Studienorientierung
	 */
	public static readonly SBO_9_2_1_3 : KAOAEbene4 = new KAOAEbene4("SBO_9_2_1_3", 18, );

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Langer Abend der Studienberatung
	 */
	public static readonly SBO_9_2_1_4 : KAOAEbene4 = new KAOAEbene4("SBO_9_2_1_4", 19, );

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Individuelle Einzelberatung durch die Zentralen Studienberatungen der Hochschulen
	 */
	public static readonly SBO_9_2_1_5 : KAOAEbene4 = new KAOAEbene4("SBO_9_2_1_5", 20, );

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Workshops für Schülerinnen und Schüler in der Zentralen Studienberatung
	 */
	public static readonly SBO_9_2_1_6 : KAOAEbene4 = new KAOAEbene4("SBO_9_2_1_6", 21, );

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Besondere Formate für Schülerinnen und Schüler an der Hochschule (Hochschultag, Hochschulpraktikum i.S. eines „Schnupperstudiums“, allgemeine Boys‘ und Girls‘ Day Angebote)
	 */
	public static readonly SBO_9_2_1_7 : KAOAEbene4 = new KAOAEbene4("SBO_9_2_1_7", 22, );

	/**
	 * KAoA-Eintrag der SBO Ebene 4: Fachspezifische Angebote (z. B. Schülerstudium, Praktika bei Hochschullehrer*innen, Schülerlabore, zdi-Zentren, fachspezifische Boys‘ und Girls‘ Day Angebote)
	 */
	public static readonly SBO_9_2_1_8 : KAOAEbene4 = new KAOAEbene4("SBO_9_2_1_8", 23, );

	/**
	 * (Integer, Long) -> Schuljahr, idZusatzmerkmal
	 */
	private static readonly _mapEintraegeBySchuljahrAndZusatzmerkmal : JavaMap<number, JavaMap<number, List<KAOAEbene4KatalogEintrag>>> = new HashMap<number, JavaMap<number, List<KAOAEbene4KatalogEintrag>>>();

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		KAOAEbene4.all_values_by_ordinal.push(this);
		KAOAEbene4.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<KAOAEbene4KatalogEintrag, KAOAEbene4>) : void {
		CoreTypeDataManager.putManager(KAOAEbene4.class, manager);
		KAOAEbene4._mapEintraegeBySchuljahrAndZusatzmerkmal.clear();
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<KAOAEbene4KatalogEintrag, KAOAEbene4> {
		return CoreTypeDataManager.getManager(KAOAEbene4.class);
	}

	/**
	 * Liefert alle zulässigen KAoA-Ebene4-Historien-Einträge für das angegebene Zusatzmerkmal in dem angegebenen Schuljahr zurück.
	 * Dabei wird intern für das Schuljahr ein Cache aufgebaut, dass nachfolgende Zugriffe auf das gleiche Schuljahr direkt aus
	 * dem Cache bedient werden können.
	 *
	 * @param schuljahr         das Schuljahr
	 * @param idZusatzmerkmal   die id des KAoA-Zusatzmerkmal-Historien-Eintrags
	 *
	 * @return alle zulässigen KAoA-Ebene4-Historien-Einträge für das angegebene Zusatzmerkmal in dem angegebenen Schuljahr.
	 */
	public static getEintraegeBySchuljahrAndIdZusatzmerkmal(schuljahr : number, idZusatzmerkmal : number) : List<KAOAEbene4KatalogEintrag> {
		let mapEintraegeByZusatzmerkmal : JavaMap<number, List<KAOAEbene4KatalogEintrag>> | null = KAOAEbene4._mapEintraegeBySchuljahrAndZusatzmerkmal.get(schuljahr);
		if (mapEintraegeByZusatzmerkmal !== null) {
			const result : List<KAOAEbene4KatalogEintrag> | null = mapEintraegeByZusatzmerkmal.get(idZusatzmerkmal);
			return (result !== null) ? result : new ArrayList();
		}
		mapEintraegeByZusatzmerkmal = KAOAEbene4.cacheEintraegeBySchuljahrAndIdZusatzmerkmal(schuljahr);
		KAOAEbene4._mapEintraegeBySchuljahrAndZusatzmerkmal.put(schuljahr, mapEintraegeByZusatzmerkmal);
		const result : List<KAOAEbene4KatalogEintrag> | null = mapEintraegeByZusatzmerkmal.get(idZusatzmerkmal);
		return (result !== null) ? result : new ArrayList();
	}

	/**
	 * Liefert einen Cache der zulässigen KAoA-Ebene4-Historien-Einträge je Zusatzmerkmal in dem angegebenen Schuljahr zurück.
	 *
	 * @param schuljahr   das Schuljahr
	 * @return einen Cache der zulässigen KAoA-Ebene4-Historien-Einträge je Zusatzmerkmal in dem angegebenen Schuljahr.
	 */
	private static cacheEintraegeBySchuljahrAndIdZusatzmerkmal(schuljahr : number) : JavaMap<number, List<KAOAEbene4KatalogEintrag>> {
		const cache : JavaMap<number, List<KAOAEbene4KatalogEintrag>> | null = new HashMap<number, List<KAOAEbene4KatalogEintrag>>();
		for (const zusatzmerkmalHistorienEintrag of KAOAZusatzmerkmal.data().getEintraegeBySchuljahr(schuljahr)) {
			const result : List<KAOAEbene4KatalogEintrag> | null = new ArrayList<KAOAEbene4KatalogEintrag>();
			for (const ebene4HistorienEintrag of KAOAEbene4.data().getEintraegeBySchuljahr(schuljahr)) {
				if (JavaObject.equalsTranspiler(ebene4HistorienEintrag.zusatzmerkmal, (KAOAZusatzmerkmal.data().getWertByID(zusatzmerkmalHistorienEintrag.id).name())))
					result.add(ebene4HistorienEintrag);
			}
			cache.put(zusatzmerkmalHistorienEintrag.id, result);
		}
		return cache;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<KAOAEbene4> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : KAOAEbene4 | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<KAOAEbene4KatalogEintrag, KAOAEbene4> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : KAOAEbene4KatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : string | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<KAOAEbene4KatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.kaoa.KAOAEbene4';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.kaoa.KAOAEbene4', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<KAOAEbene4>('de.svws_nrw.asd.types.kaoa.KAOAEbene4');

}

export function cast_de_svws_nrw_asd_types_kaoa_KAOAEbene4(obj : unknown) : KAOAEbene4 {
	return obj as KAOAEbene4;
}
