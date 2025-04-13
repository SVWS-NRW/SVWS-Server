import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';
import { ReligionKatalogEintrag } from '../../../asd/data/schule/ReligionKatalogEintrag';

export class Religion extends JavaEnum<Religion> implements CoreType<ReligionKatalogEintrag, Religion> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<Religion> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, Religion> = new Map<string, Religion>();

	/**
	 * Religion: alevitisch
	 */
	public static readonly AR : Religion = new Religion("AR", 0, );

	/**
	 * Religion: evangelisch
	 */
	public static readonly ER : Religion = new Religion("ER", 1, );

	/**
	 * Religion: jüdisch
	 */
	public static readonly HR : Religion = new Religion("HR", 2, );

	/**
	 * Religion: islamisch
	 */
	public static readonly IR : Religion = new Religion("IR", 3, );

	/**
	 * Religion: katholisch
	 */
	public static readonly KR : Religion = new Religion("KR", 4, );

	/**
	 * Religion: mennonitische BG NRW
	 */
	public static readonly ME : Religion = new Religion("ME", 5, );

	/**
	 * Religion: ohne Bekenntnis
	 */
	public static readonly OH : Religion = new Religion("OH", 6, );

	/**
	 * Religion: griechisch-orthodox
	 */
	public static readonly OR : Religion = new Religion("OR", 7, );

	/**
	 * Religion: syrisch-orthodox
	 */
	public static readonly SO : Religion = new Religion("SO", 8, );

	/**
	 * Religion: sonstige orthodoxe
	 */
	public static readonly XO : Religion = new Religion("XO", 9, );

	/**
	 * Religion: andere Religionen
	 */
	public static readonly XR : Religion = new Religion("XR", 10, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		Religion.all_values_by_ordinal.push(this);
		Religion.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<ReligionKatalogEintrag, Religion>) : void {
		CoreTypeDataManager.putManager(Religion.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<ReligionKatalogEintrag, Religion> {
		return CoreTypeDataManager.getManager(Religion.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Religion> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Religion | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<ReligionKatalogEintrag, Religion> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : ReligionKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : string | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<ReligionKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.Religion';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.schule.Religion', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<Religion>('de.svws_nrw.asd.types.schule.Religion');

}

export function cast_de_svws_nrw_asd_types_schule_Religion(obj : unknown) : Religion {
	return obj as Religion;
}
