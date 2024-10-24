import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JahrgaengeKatalogEintrag } from '../../../asd/data/jahrgang/JahrgaengeKatalogEintrag';
import { HashMap } from '../../../java/util/HashMap';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { ArrayList } from '../../../java/util/ArrayList';
import { Jahrgaenge } from '../../../asd/types/jahrgang/Jahrgaenge';
import { KAOAKategorieKatalogEintrag } from '../../../asd/data/kaoa/KAOAKategorieKatalogEintrag';
import { NullPointerException } from '../../../java/lang/NullPointerException';
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

	private static readonly _mapBySchuljahrAndKategorie : JavaMap<number, JavaMap<KAOAKategorie, List<Jahrgaenge>>> = new HashMap<number, JavaMap<KAOAKategorie, List<Jahrgaenge>>>();

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
		return KAOAKategorie.getListBySchuljahrAndKategorie(schuljahr, this).contains(jahrgang);
	}

	/**
	 * Liefert alle zulässigen Jahrgänge für die angegebene Kategorie in dem angegebenen Schuljahr.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param kategorie   die Kategorie
	 *
	 * @return die bei der Kategorie in dem angegebenen Schuljahr zulässigen Jahrgänge
	 */
	public static getListBySchuljahrAndKategorie(schuljahr : number, kategorie : KAOAKategorie) : List<Jahrgaenge> {
		const mapByKategorie : JavaMap<KAOAKategorie, List<Jahrgaenge>> | null = KAOAKategorie._mapBySchuljahrAndKategorie.computeIfAbsent(schuljahr, { apply : (k: number | null) => new HashMap<KAOAKategorie, List<Jahrgaenge>>() });
		if (mapByKategorie === null)
			throw new NullPointerException("computeIfAbsent darf nicht null liefern")
		let result : List<Jahrgaenge> | null = mapByKategorie.get(kategorie);
		if (result === null) {
			result = new ArrayList();
			const eintrag : KAOAKategorieKatalogEintrag | null = kategorie.daten(schuljahr);
			if (eintrag !== null)
				for (const jgBezeichner of eintrag.jahrgaenge)
					result.add(Jahrgaenge.data().getWertByBezeichner(jgBezeichner));
			mapByKategorie.put(kategorie, result);
		}
		return result;
	}

	/**
	 * Liefert alle zulässigen KAoA-Kategorie-Historien-Einträge für den angegebenen Jahrgang in dem angegebenen Schuljahr zurück.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param idJahrgang    der Jahrgang
	 *
	 * @return alle zulässigen KAoA-Kategorie-Historien-Einträge für den angegebenen Jahrgang in dem angegebenen Schuljahr.
	 */
	public static getListBySchuljahrAndJahrgang(schuljahr : number, idJahrgang : number) : List<KAOAKategorieKatalogEintrag> {
		const mapEintraegeByJahrgaenge : JavaMap<number, List<KAOAKategorieKatalogEintrag>> | null = KAOAKategorie._mapEintraegeBySchuljahrAndJahrgang.computeIfAbsent(schuljahr, { apply : (j: number | null) => new HashMap<number, List<KAOAKategorieKatalogEintrag>>() });
		if (mapEintraegeByJahrgaenge === null)
			throw new NullPointerException("computeIfAbsent darf nicht null liefern")
		let result : List<KAOAKategorieKatalogEintrag> | null = mapEintraegeByJahrgaenge.get(idJahrgang);
		if (result === null) {
			result = new ArrayList();
			const werte : List<KAOAKategorie> | null = KAOAKategorie.data().getWerte();
			for (const kategorie of werte) {
				const eintrag : KAOAKategorieKatalogEintrag | null = kategorie.daten(schuljahr);
				if (eintrag !== null) {
					for (const bezeichner of eintrag.jahrgaenge) {
						const eintragJahrgang : JahrgaengeKatalogEintrag | null = Jahrgaenge.data().getWertByBezeichner(bezeichner).daten(schuljahr);
						if ((eintragJahrgang !== null) && (eintragJahrgang.id === idJahrgang)) {
							result.add(eintrag);
							break;
						}
					}
				}
			}
			mapEintraegeByJahrgaenge.put(idJahrgang, result);
		}
		return result;
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
