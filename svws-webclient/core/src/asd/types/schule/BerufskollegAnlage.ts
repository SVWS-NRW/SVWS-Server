import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_historienId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';
import { BerufskollegAnlageKatalogEintrag } from '../../../asd/data/schule/BerufskollegAnlageKatalogEintrag';

export class BerufskollegAnlage extends JavaEnum<BerufskollegAnlage> implements CoreType<BerufskollegAnlageKatalogEintrag, BerufskollegAnlage> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<BerufskollegAnlage> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, BerufskollegAnlage> = new Map<string, BerufskollegAnlage>();

	/**
	 * Anlage A: Fachklassen duales System und Ausbildungsvorbereitung
	 */
	public static readonly A : BerufskollegAnlage = new BerufskollegAnlage("A", 0, );

	/**
	 * Anlage B: Berufsfachschule
	 */
	public static readonly B : BerufskollegAnlage = new BerufskollegAnlage("B", 1, );

	/**
	 * Anlage C: Berufsfachschule und Fachoberschule
	 */
	public static readonly C : BerufskollegAnlage = new BerufskollegAnlage("C", 2, );

	/**
	 * Anlage D: Berufliches Gymnasium und Fachoberschule
	 */
	public static readonly D : BerufskollegAnlage = new BerufskollegAnlage("D", 3, );

	/**
	 * Anlage E: Fachschule
	 */
	public static readonly E : BerufskollegAnlage = new BerufskollegAnlage("E", 4, );

	/**
	 * Anlage H: Berufsgrundbildung und Berufsausbildung an einer freien Waldorfschule / Hiberniakolleg
	 */
	public static readonly H : BerufskollegAnlage = new BerufskollegAnlage("H", 5, );

	/**
	 * Anlage X: Ehemalige Kollegschule
	 */
	public static readonly X : BerufskollegAnlage = new BerufskollegAnlage("X", 6, );

	/**
	 * Anlage Z: TODO
	 */
	public static readonly Z : BerufskollegAnlage = new BerufskollegAnlage("Z", 7, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		BerufskollegAnlage.all_values_by_ordinal.push(this);
		BerufskollegAnlage.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<BerufskollegAnlageKatalogEintrag, BerufskollegAnlage>) : void {
		CoreTypeDataManager.putManager(BerufskollegAnlage.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<BerufskollegAnlageKatalogEintrag, BerufskollegAnlage> {
		return CoreTypeDataManager.getManager(BerufskollegAnlage.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<BerufskollegAnlage> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : BerufskollegAnlage | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<BerufskollegAnlageKatalogEintrag, BerufskollegAnlage> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : BerufskollegAnlageKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public historienId() : number {
		return de_svws_nrw_asd_types_CoreType_historienId(this);
	}

	public historie() : List<BerufskollegAnlageKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.BerufskollegAnlage';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.schule.BerufskollegAnlage', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<BerufskollegAnlage>('de.svws_nrw.asd.types.schule.BerufskollegAnlage');

}

export function cast_de_svws_nrw_asd_types_schule_BerufskollegAnlage(obj : unknown) : BerufskollegAnlage {
	return obj as BerufskollegAnlage;
}
