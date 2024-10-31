import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JahrgaengeKatalogEintrag } from '../../../asd/data/jahrgang/JahrgaengeKatalogEintrag';
import { HashMap } from '../../../java/util/HashMap';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { ArrayList } from '../../../java/util/ArrayList';
import { Jahrgaenge } from '../../../asd/types/jahrgang/Jahrgaenge';
import { KAOAKategorieKatalogEintrag } from '../../../asd/data/kaoa/KAOAKategorieKatalogEintrag';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_historienId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';
import type { JavaMap } from '../../../java/util/JavaMap';

export class KAOAKategorie extends JavaEnum<KAOAKategorie> implements CoreType<KAOAKategorieKatalogEintrag, KAOAKategorie> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<KAOAKategorie> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, KAOAKategorie> = new Map<string, KAOAKategorie>();

	/**
	 * KAoA-Kategorie: Formen der Orientierung und Beratung
	 */
	public static readonly SBO_2 : KAOAKategorie = new KAOAKategorie("SBO_2", 0, );

	/**
	 * KAoA-Kategorie: Strukturen an Schulen
	 */
	public static readonly SBO_3 : KAOAKategorie = new KAOAKategorie("SBO_3", 1, );

	/**
	 * KAoA-Kategorie: Potenziale entdecken und den eigenen Standort bestimmen
	 */
	public static readonly SBO_4 : KAOAKategorie = new KAOAKategorie("SBO_4", 2, );

	/**
	 * KAoA-Kategorie: Berufsfelder erkunden und Informationen sammeln
	 */
	public static readonly SBO_5 : KAOAKategorie = new KAOAKategorie("SBO_5", 3, );

	/**
	 * KAoA-Kategorie: Praxis der Arbeitswelt kennenlernen und erproben
	 */
	public static readonly SBO_6 : KAOAKategorie = new KAOAKategorie("SBO_6", 4, );

	/**
	 * KAoA-Kategorie: Nachholung der Erstberufsorientierung
	 */
	public static readonly SBO_7 : KAOAKategorie = new KAOAKategorie("SBO_7", 5, );

	/**
	 * KAoA-Kategorie: Sekundarstufe II - Individuelle Voraussetzungen für eine Ausbildung oder ein Studium überprüfen
	 */
	public static readonly SBO_8 : KAOAKategorie = new KAOAKategorie("SBO_8", 6, );

	/**
	 * KAoA-Kategorie: Sekundarstufe II - Praxis vertiefen - Ausbildungs- und Studienwahl konkretisieren
	 */
	public static readonly SBO_9 : KAOAKategorie = new KAOAKategorie("SBO_9", 7, );

	/**
	 * KAoA-Kategorie: Gestaltung und Koordination der Übergänge in der Sek. I und Sek. II
	 */
	public static readonly SBO_10 : KAOAKategorie = new KAOAKategorie("SBO_10", 8, );

	/**
	 * (Integer) -> Schuljahr
	 */
	private static readonly _mapBySchuljahrAndKategorie : JavaMap<number, JavaMap<KAOAKategorie, List<Jahrgaenge>>> = new HashMap<number, JavaMap<KAOAKategorie, List<Jahrgaenge>>>();

	/**
	 * (Integer, Long) -> Schuljahr, idJahrgang
	 */
	private static readonly _mapEintraegeBySchuljahrAndJahrgang : JavaMap<number, JavaMap<number, List<KAOAKategorieKatalogEintrag>>> = new HashMap<number, JavaMap<number, List<KAOAKategorieKatalogEintrag>>>();

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		KAOAKategorie.all_values_by_ordinal.push(this);
		KAOAKategorie.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<KAOAKategorieKatalogEintrag, KAOAKategorie>) : void {
		CoreTypeDataManager.putManager(KAOAKategorie.class, manager);
		KAOAKategorie._mapBySchuljahrAndKategorie.clear();
		KAOAKategorie._mapEintraegeBySchuljahrAndJahrgang.clear();
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<KAOAKategorieKatalogEintrag, KAOAKategorie> {
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
	public hatJahrgang(schuljahr : number, jahrgang : Jahrgaenge | null) : boolean {
		return KAOAKategorie.getListJahrgaengeBySchuljahrAndKategorie(schuljahr, this).contains(jahrgang);
	}

	/**
	 * Liefert alle zulässigen Jahrgänge für die angegebene Kategorie in dem angegebenen Schuljahr.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param kategorie   die Kategorie
	 *
	 * @return die bei der Kategorie in dem angegebenen Schuljahr zulässigen Jahrgänge
	 */
	public static getListJahrgaengeBySchuljahrAndKategorie(schuljahr : number, kategorie : KAOAKategorie) : List<Jahrgaenge> {
		let mapByKategorie : JavaMap<KAOAKategorie, List<Jahrgaenge>> | null = KAOAKategorie._mapBySchuljahrAndKategorie.get(schuljahr);
		if (mapByKategorie === null) {
			mapByKategorie = KAOAKategorie.cacheListJahrgaengeBySchuljahrAndKategorie(schuljahr);
			KAOAKategorie._mapBySchuljahrAndKategorie.put(schuljahr, mapByKategorie);
		}
		const jahrgaenge : List<Jahrgaenge> | null = mapByKategorie.get(kategorie);
		return (jahrgaenge !== null) ? jahrgaenge : new ArrayList();
	}

	/**
	 * Liefert einen Cache der zulässigen Jahrgänge je Kategorie in dem angegebenen Schuljahr zurück.
	 *
	 * @param schuljahr   das Schuljahr
	 * @return einen Cache der zulässigen Jahrgänge je Kategorie in dem angegebenen Schuljahr.
	 */
	private static cacheListJahrgaengeBySchuljahrAndKategorie(schuljahr : number) : JavaMap<KAOAKategorie, List<Jahrgaenge>> {
		const cache : JavaMap<KAOAKategorie, List<Jahrgaenge>> | null = new HashMap<KAOAKategorie, List<Jahrgaenge>>();
		for (const kategorie of KAOAKategorie.data().getWerte()) {
			const kategorieHistorienEintrag : KAOAKategorieKatalogEintrag | null = kategorie.daten(schuljahr);
			const jahrgaenge : List<Jahrgaenge> | null = new ArrayList<Jahrgaenge>();
			if (kategorieHistorienEintrag !== null) {
				for (const jgBezeichner of kategorieHistorienEintrag.jahrgaenge) {
					const jahrgang : Jahrgaenge | null = Jahrgaenge.data().getWertByBezeichner(jgBezeichner);
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
	public static getEintraegeBySchuljahrAndIdJahrgang(schuljahr : number, idJahrgang : number) : List<KAOAKategorieKatalogEintrag> {
		let mapEintraegeByJahrgaenge : JavaMap<number, List<KAOAKategorieKatalogEintrag>> | null = KAOAKategorie._mapEintraegeBySchuljahrAndJahrgang.get(schuljahr);
		if (mapEintraegeByJahrgaenge !== null) {
			const result : List<KAOAKategorieKatalogEintrag> | null = mapEintraegeByJahrgaenge.get(idJahrgang);
			return (result !== null) ? result : new ArrayList();
		}
		mapEintraegeByJahrgaenge = KAOAKategorie.cacheEintraegeBySchuljahrAndIdJahrgang(schuljahr);
		KAOAKategorie._mapEintraegeBySchuljahrAndJahrgang.put(schuljahr, mapEintraegeByJahrgaenge);
		const result : List<KAOAKategorieKatalogEintrag> | null = mapEintraegeByJahrgaenge.get(idJahrgang);
		return (result !== null) ? result : new ArrayList();
	}

	/**
	 * Liefert einen Cache der zulässigen KAoA-Kategorie-Historien-Einträge je Jahrgang in dem angegebenen Schuljahr zurück.
	 *
	 * @param schuljahr   das Schuljahr
	 * @return einen Cache der zulässigen KAoA-Anschlussoption-Historien-Einträge je Jahrgang in dem angegebenen Schuljahr.
	 */
	private static cacheEintraegeBySchuljahrAndIdJahrgang(schuljahr : number) : JavaMap<number, List<KAOAKategorieKatalogEintrag>> {
		const cache : JavaMap<number, List<KAOAKategorieKatalogEintrag>> | null = new HashMap<number, List<KAOAKategorieKatalogEintrag>>();
		for (const jahrgangHistorienEintrag of Jahrgaenge.data().getEintraegeBySchuljahr(schuljahr)) {
			const result : List<KAOAKategorieKatalogEintrag> | null = new ArrayList<KAOAKategorieKatalogEintrag>();
			for (const kategorieHistorienEintrag of KAOAKategorie.data().getEintraegeBySchuljahr(schuljahr)) {
				for (const bezeichner of kategorieHistorienEintrag.jahrgaenge) {
					const jahrgangEintragDerKategorie : JahrgaengeKatalogEintrag | null = Jahrgaenge.data().getWertByBezeichner(bezeichner).daten(schuljahr);
					if ((jahrgangEintragDerKategorie !== null) && (jahrgangEintragDerKategorie.id === jahrgangHistorienEintrag.id)) {
						result.add(kategorieHistorienEintrag);
						break;
					}
				}
			}
			cache.put(jahrgangHistorienEintrag.id, result);
		}
		return cache;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<KAOAKategorie> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : KAOAKategorie | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<KAOAKategorieKatalogEintrag, KAOAKategorie> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : KAOAKategorieKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public historienId() : number {
		return de_svws_nrw_asd_types_CoreType_historienId(this);
	}

	public historie() : List<KAOAKategorieKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.kaoa.KAOAKategorie';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.kaoa.KAOAKategorie', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<KAOAKategorie>('de.svws_nrw.asd.types.kaoa.KAOAKategorie');

}

export function cast_de_svws_nrw_asd_types_kaoa_KAOAKategorie(obj : unknown) : KAOAKategorie {
	return obj as KAOAKategorie;
}
