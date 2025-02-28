import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { LehrerZugangsgrundKatalogEintrag } from '../../../asd/data/lehrer/LehrerZugangsgrundKatalogEintrag';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class LehrerZugangsgrund extends JavaEnum<LehrerZugangsgrund> implements CoreType<LehrerZugangsgrundKatalogEintrag, LehrerZugangsgrund> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<LehrerZugangsgrund> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, LehrerZugangsgrund> = new Map<string, LehrerZugangsgrund>();

	/**
	 * Grund 'Neueintritt in den Schuldienst mit abgelegter 2. Staatsprüfung oder anderweitig erfüllter Eingangsvoraussetzung' für das Kommen des Lehrers an die Schule
	 */
	public static readonly NEU : LehrerZugangsgrund = new LehrerZugangsgrund("NEU", 0, );

	/**
	 * Grund 'Übertritt aus dem Schuldienst eines anderen Bundeslandes' für das Kommen des Lehrers an die Schule
	 */
	public static readonly AndBuLand : LehrerZugangsgrund = new LehrerZugangsgrund("AndBuLand", 1, );

	/**
	 * Grund 'Wechsel innerhalb des Landes von einer anderen Schule an die berichtende Schule' für das Kommen des Lehrers an die Schule
	 */
	public static readonly WECHSEL : LehrerZugangsgrund = new LehrerZugangsgrund("WECHSEL", 2, );

	/**
	 * Grund 'Wiedereintritt in den Schuldienst' für das Kommen des Lehrers an die Schule
	 */
	public static readonly WIEDER : LehrerZugangsgrund = new LehrerZugangsgrund("WIEDER", 3, );

	/**
	 * Grund 'Sonstige Zugänge' für das Kommen des Lehrers an die Schule
	 */
	public static readonly SONSTIG : LehrerZugangsgrund = new LehrerZugangsgrund("SONSTIG", 4, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		LehrerZugangsgrund.all_values_by_ordinal.push(this);
		LehrerZugangsgrund.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<LehrerZugangsgrundKatalogEintrag, LehrerZugangsgrund>) : void {
		CoreTypeDataManager.putManager(LehrerZugangsgrund.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<LehrerZugangsgrundKatalogEintrag, LehrerZugangsgrund> {
		return CoreTypeDataManager.getManager(LehrerZugangsgrund.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LehrerZugangsgrund> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : LehrerZugangsgrund | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<LehrerZugangsgrundKatalogEintrag, LehrerZugangsgrund> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : LehrerZugangsgrundKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : String | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<LehrerZugangsgrundKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.lehrer.LehrerZugangsgrund';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.lehrer.LehrerZugangsgrund', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<LehrerZugangsgrund>('de.svws_nrw.asd.types.lehrer.LehrerZugangsgrund');

}

export function cast_de_svws_nrw_asd_types_lehrer_LehrerZugangsgrund(obj : unknown) : LehrerZugangsgrund {
	return obj as LehrerZugangsgrund;
}
