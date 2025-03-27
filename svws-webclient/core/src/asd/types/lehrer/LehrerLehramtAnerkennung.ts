import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';
import { LehrerLehramtAnerkennungKatalogEintrag } from '../../../asd/data/lehrer/LehrerLehramtAnerkennungKatalogEintrag';

export class LehrerLehramtAnerkennung extends JavaEnum<LehrerLehramtAnerkennung> implements CoreType<LehrerLehramtAnerkennungKatalogEintrag, LehrerLehramtAnerkennung> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<LehrerLehramtAnerkennung> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, LehrerLehramtAnerkennung> = new Map<string, LehrerLehramtAnerkennung>();

	/**
	 * Lehramtsanerkennung 'Zweite Staatsprüfung für ein Lehramt'
	 */
	public static readonly ST : LehrerLehramtAnerkennung = new LehrerLehramtAnerkennung("ST", 0, );

	/**
	 * Lehramtsanerkennung 'Anerkennung Lehramt'
	 */
	public static readonly AL : LehrerLehramtAnerkennung = new LehrerLehramtAnerkennung("AL", 1, );

	/**
	 * Lehramtsanerkennung 'Anerkennung geeignete Prüfung'
	 */
	public static readonly AP : LehrerLehramtAnerkennung = new LehrerLehramtAnerkennung("AP", 2, );

	/**
	 * Lehramtsanerkennung 'Förderliche Berufstätigkeit'
	 */
	public static readonly BT : LehrerLehramtAnerkennung = new LehrerLehramtAnerkennung("BT", 3, );

	/**
	 * Lehramtsanerkennung 'ohne'
	 */
	public static readonly OH : LehrerLehramtAnerkennung = new LehrerLehramtAnerkennung("OH", 4, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		LehrerLehramtAnerkennung.all_values_by_ordinal.push(this);
		LehrerLehramtAnerkennung.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<LehrerLehramtAnerkennungKatalogEintrag, LehrerLehramtAnerkennung>) : void {
		CoreTypeDataManager.putManager(LehrerLehramtAnerkennung.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<LehrerLehramtAnerkennungKatalogEintrag, LehrerLehramtAnerkennung> {
		return CoreTypeDataManager.getManager(LehrerLehramtAnerkennung.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LehrerLehramtAnerkennung> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : LehrerLehramtAnerkennung | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<LehrerLehramtAnerkennungKatalogEintrag, LehrerLehramtAnerkennung> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : LehrerLehramtAnerkennungKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : String | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<LehrerLehramtAnerkennungKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.lehrer.LehrerLehramtAnerkennung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.lehrer.LehrerLehramtAnerkennung', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<LehrerLehramtAnerkennung>('de.svws_nrw.asd.types.lehrer.LehrerLehramtAnerkennung');

}

export function cast_de_svws_nrw_asd_types_lehrer_LehrerLehramtAnerkennung(obj : unknown) : LehrerLehramtAnerkennung {
	return obj as LehrerLehramtAnerkennung;
}
