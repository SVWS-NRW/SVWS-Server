import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { KindergartenbesuchKatalogEintrag } from '../../../asd/data/schule/KindergartenbesuchKatalogEintrag';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class Kindergartenbesuch extends JavaEnum<Kindergartenbesuch> implements CoreType<KindergartenbesuchKatalogEintrag, Kindergartenbesuch> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<Kindergartenbesuch> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, Kindergartenbesuch> = new Map<string, Kindergartenbesuch>();

	/**
	 * Kein Kindergartenbesuch
	 */
	public static readonly KEINER : Kindergartenbesuch = new Kindergartenbesuch("KEINER", 0, );

	/**
	 * Kindergartenbesuch unter einem Jahr
	 */
	public static readonly MAX_1_JAHR : Kindergartenbesuch = new Kindergartenbesuch("MAX_1_JAHR", 1, );

	/**
	 * Kindergartenbesuch unter einem Jahr
	 */
	public static readonly MAX_2_JAHRE : Kindergartenbesuch = new Kindergartenbesuch("MAX_2_JAHRE", 2, );

	/**
	 * Kindergartenbesuch unter einem Jahr
	 */
	public static readonly MAX_3_JAHRE : Kindergartenbesuch = new Kindergartenbesuch("MAX_3_JAHRE", 3, );

	/**
	 * Kindergartenbesuch unter einem Jahr
	 */
	public static readonly MIN_3_JAHRE : Kindergartenbesuch = new Kindergartenbesuch("MIN_3_JAHRE", 4, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		Kindergartenbesuch.all_values_by_ordinal.push(this);
		Kindergartenbesuch.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<KindergartenbesuchKatalogEintrag, Kindergartenbesuch>) : void {
		CoreTypeDataManager.putManager(Kindergartenbesuch.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<KindergartenbesuchKatalogEintrag, Kindergartenbesuch> {
		return CoreTypeDataManager.getManager(Kindergartenbesuch.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Kindergartenbesuch> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Kindergartenbesuch | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<KindergartenbesuchKatalogEintrag, Kindergartenbesuch> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : KindergartenbesuchKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : String | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<KindergartenbesuchKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.Kindergartenbesuch';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.schule.Kindergartenbesuch', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<Kindergartenbesuch>('de.svws_nrw.asd.types.schule.Kindergartenbesuch');

}

export function cast_de_svws_nrw_asd_types_schule_Kindergartenbesuch(obj : unknown) : Kindergartenbesuch {
	return obj as Kindergartenbesuch;
}
