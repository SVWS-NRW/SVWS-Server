import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { LehrerEinsatzstatusKatalogEintrag } from '../../../asd/data/lehrer/LehrerEinsatzstatusKatalogEintrag';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_historienId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class LehrerEinsatzstatus extends JavaEnum<LehrerEinsatzstatus> implements CoreType<LehrerEinsatzstatusKatalogEintrag, LehrerEinsatzstatus> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<LehrerEinsatzstatus> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, LehrerEinsatzstatus> = new Map<string, LehrerEinsatzstatus>();

	/**
	 * Einsatzstatus: 'Stammschule, ganz oder teilweise auch an anderen Schulen tätig'
	 */
	public static readonly A : LehrerEinsatzstatus = new LehrerEinsatzstatus("A", 0, );

	/**
	 * Einsatzstatus: 'nicht Stammschule, aber auch hier tätig'
	 */
	public static readonly B : LehrerEinsatzstatus = new LehrerEinsatzstatus("B", 1, );

	/**
	 * Einsatzstatus: 'Stammschule, nur hier tätig'
	 */
	public static readonly DEFAULT : LehrerEinsatzstatus = new LehrerEinsatzstatus("DEFAULT", 2, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		LehrerEinsatzstatus.all_values_by_ordinal.push(this);
		LehrerEinsatzstatus.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<LehrerEinsatzstatusKatalogEintrag, LehrerEinsatzstatus>) : void {
		CoreTypeDataManager.putManager(LehrerEinsatzstatus.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<LehrerEinsatzstatusKatalogEintrag, LehrerEinsatzstatus> {
		return CoreTypeDataManager.getManager(LehrerEinsatzstatus.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LehrerEinsatzstatus> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : LehrerEinsatzstatus | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<LehrerEinsatzstatusKatalogEintrag, LehrerEinsatzstatus> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : LehrerEinsatzstatusKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public historienId() : number {
		return de_svws_nrw_asd_types_CoreType_historienId(this);
	}

	public historie() : List<LehrerEinsatzstatusKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<LehrerEinsatzstatus>('de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus');

}

export function cast_de_svws_nrw_asd_types_lehrer_LehrerEinsatzstatus(obj : unknown) : LehrerEinsatzstatus {
	return obj as LehrerEinsatzstatus;
}
