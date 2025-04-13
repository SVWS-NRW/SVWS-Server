import { JavaEnum } from '../../../java/lang/JavaEnum';
import { LehrerLeitungsfunktionKatalogEintrag } from '../../../asd/data/lehrer/LehrerLeitungsfunktionKatalogEintrag';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class LehrerLeitungsfunktion extends JavaEnum<LehrerLeitungsfunktion> implements CoreType<LehrerLeitungsfunktionKatalogEintrag, LehrerLeitungsfunktion> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<LehrerLeitungsfunktion> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, LehrerLeitungsfunktion> = new Map<string, LehrerLeitungsfunktion>();

	/**
	 * Schulleitung
	 */
	public static readonly SL : LehrerLeitungsfunktion = new LehrerLeitungsfunktion("SL", 0, );

	/**
	 * Stellvertretende Schulleitung
	 */
	public static readonly SL_STV : LehrerLeitungsfunktion = new LehrerLeitungsfunktion("SL_STV", 1, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		LehrerLeitungsfunktion.all_values_by_ordinal.push(this);
		LehrerLeitungsfunktion.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<LehrerLeitungsfunktionKatalogEintrag, LehrerLeitungsfunktion>) : void {
		CoreTypeDataManager.putManager(LehrerLeitungsfunktion.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<LehrerLeitungsfunktionKatalogEintrag, LehrerLeitungsfunktion> {
		return CoreTypeDataManager.getManager(LehrerLeitungsfunktion.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LehrerLeitungsfunktion> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : LehrerLeitungsfunktion | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<LehrerLeitungsfunktionKatalogEintrag, LehrerLeitungsfunktion> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : LehrerLeitungsfunktionKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : string | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<LehrerLeitungsfunktionKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.lehrer.LehrerLeitungsfunktion';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.lehrer.LehrerLeitungsfunktion', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<LehrerLeitungsfunktion>('de.svws_nrw.asd.types.lehrer.LehrerLeitungsfunktion');

}

export function cast_de_svws_nrw_asd_types_lehrer_LehrerLeitungsfunktion(obj : unknown) : LehrerLeitungsfunktion {
	return obj as LehrerLeitungsfunktion;
}
