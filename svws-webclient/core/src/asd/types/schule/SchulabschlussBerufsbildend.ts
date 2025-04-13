import { JavaEnum } from '../../../java/lang/JavaEnum';
import { SchulabschlussBerufsbildendKatalogEintrag } from '../../../asd/data/schule/SchulabschlussBerufsbildendKatalogEintrag';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class SchulabschlussBerufsbildend extends JavaEnum<SchulabschlussBerufsbildend> implements CoreType<SchulabschlussBerufsbildendKatalogEintrag, SchulabschlussBerufsbildend> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<SchulabschlussBerufsbildend> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, SchulabschlussBerufsbildend> = new Map<string, SchulabschlussBerufsbildend>();

	/**
	 * Es liegt kein Abschluss vor
	 */
	public static readonly OA : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("OA", 0, );

	/**
	 * Abschluss der Ausbildungsvorbereitung
	 */
	public static readonly VORB : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("VORB", 1, );

	/**
	 * Versetzungszeugnis
	 */
	public static readonly VERS : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("VERS", 2, );

	/**
	 * Abschlusszeugnis in Aufbaubildungsgängen
	 */
	public static readonly AUFB : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("AUFB", 3, );

	/**
	 * Abschluss der Berufschulvorbereitung
	 */
	public static readonly BV : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("BV", 4, );

	/**
	 * Vorpraktikum
	 */
	public static readonly VP : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("VP", 5, );

	/**
	 * Vorpraktikum
	 */
	public static readonly BP : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("BP", 6, );

	/**
	 * Abschluss der Berufschulgrundjahres
	 */
	public static readonly BG : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("BG", 7, );

	/**
	 * Abschlusszeugnis berufliche Kenntnisse
	 */
	public static readonly ASZBK : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("ASZBK", 8, );

	/**
	 * Berufschulabschluss
	 */
	public static readonly BS : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("BS", 9, );

	/**
	 * Berufliche Kenntnisse, Fähigkeiten und Fertigkeiten
	 */
	public static readonly BK : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("BK", 10, );

	/**
	 * Berufsabschluss
	 */
	public static readonly BAB : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("BAB", 11, );

	/**
	 * Fachschulabschluss (berufliche Weiterbildung)
	 */
	public static readonly BW : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("BW", 12, );

	/**
	 * Erweiterte berufliche Kenntnisse, Fähigkeiten und Fertigkeiten
	 */
	public static readonly EBK : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("EBK", 13, );

	/**
	 * Vertiefte berufliche Kenntnisse, Fähigkeiten und Fertigkeiten
	 */
	public static readonly VBK : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("VBK", 14, );

	/**
	 * Pseudoabschluss: Schulwechsler, die im selben Bildungsgang verbleiben
	 */
	public static readonly WECHSEL : SchulabschlussBerufsbildend = new SchulabschlussBerufsbildend("WECHSEL", 15, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		SchulabschlussBerufsbildend.all_values_by_ordinal.push(this);
		SchulabschlussBerufsbildend.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<SchulabschlussBerufsbildendKatalogEintrag, SchulabschlussBerufsbildend>) : void {
		CoreTypeDataManager.putManager(SchulabschlussBerufsbildend.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<SchulabschlussBerufsbildendKatalogEintrag, SchulabschlussBerufsbildend> {
		return CoreTypeDataManager.getManager(SchulabschlussBerufsbildend.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<SchulabschlussBerufsbildend> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : SchulabschlussBerufsbildend | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<SchulabschlussBerufsbildendKatalogEintrag, SchulabschlussBerufsbildend> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : SchulabschlussBerufsbildendKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : string | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<SchulabschlussBerufsbildendKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.SchulabschlussBerufsbildend';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.schule.SchulabschlussBerufsbildend', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<SchulabschlussBerufsbildend>('de.svws_nrw.asd.types.schule.SchulabschlussBerufsbildend');

}

export function cast_de_svws_nrw_asd_types_schule_SchulabschlussBerufsbildend(obj : unknown) : SchulabschlussBerufsbildend {
	return obj as SchulabschlussBerufsbildend;
}
