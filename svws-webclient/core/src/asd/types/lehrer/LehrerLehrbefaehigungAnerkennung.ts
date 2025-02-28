import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { LehrerLehrbefaehigungAnerkennungKatalogEintrag } from '../../../asd/data/lehrer/LehrerLehrbefaehigungAnerkennungKatalogEintrag';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class LehrerLehrbefaehigungAnerkennung extends JavaEnum<LehrerLehrbefaehigungAnerkennung> implements CoreType<LehrerLehrbefaehigungAnerkennungKatalogEintrag, LehrerLehrbefaehigungAnerkennung> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<LehrerLehrbefaehigungAnerkennung> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, LehrerLehrbefaehigungAnerkennung> = new Map<string, LehrerLehrbefaehigungAnerkennung>();

	/**
	 * Anerkennung der Lehrbefähigung 'erworben durch LABG/OVP bzw. Laufbahnverordnung'
	 */
	public static readonly ID_1 : LehrerLehrbefaehigungAnerkennung = new LehrerLehrbefaehigungAnerkennung("ID_1", 0, );

	/**
	 * Anerkennung der Lehrbefähigung 'Unterrichtserlaubnis (z. B. Zertifikatskurs)'
	 */
	public static readonly ID_2 : LehrerLehrbefaehigungAnerkennung = new LehrerLehrbefaehigungAnerkennung("ID_2", 1, );

	/**
	 * Anerkennung der Lehrbefähigung 'mehrjähriger Unterricht ohne Lehramtsprüfung oder Unterrichtserlaubnis'
	 */
	public static readonly ID_3 : LehrerLehrbefaehigungAnerkennung = new LehrerLehrbefaehigungAnerkennung("ID_3", 2, );

	/**
	 * Anerkennung der Lehrbefähigung 'sonstige'
	 */
	public static readonly ID_9 : LehrerLehrbefaehigungAnerkennung = new LehrerLehrbefaehigungAnerkennung("ID_9", 3, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		LehrerLehrbefaehigungAnerkennung.all_values_by_ordinal.push(this);
		LehrerLehrbefaehigungAnerkennung.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<LehrerLehrbefaehigungAnerkennungKatalogEintrag, LehrerLehrbefaehigungAnerkennung>) : void {
		CoreTypeDataManager.putManager(LehrerLehrbefaehigungAnerkennung.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<LehrerLehrbefaehigungAnerkennungKatalogEintrag, LehrerLehrbefaehigungAnerkennung> {
		return CoreTypeDataManager.getManager(LehrerLehrbefaehigungAnerkennung.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LehrerLehrbefaehigungAnerkennung> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : LehrerLehrbefaehigungAnerkennung | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<LehrerLehrbefaehigungAnerkennungKatalogEintrag, LehrerLehrbefaehigungAnerkennung> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : LehrerLehrbefaehigungAnerkennungKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : String | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<LehrerLehrbefaehigungAnerkennungKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.lehrer.LehrerLehrbefaehigungAnerkennung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.lehrer.LehrerLehrbefaehigungAnerkennung', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<LehrerLehrbefaehigungAnerkennung>('de.svws_nrw.asd.types.lehrer.LehrerLehrbefaehigungAnerkennung');

}

export function cast_de_svws_nrw_asd_types_lehrer_LehrerLehrbefaehigungAnerkennung(obj : unknown) : LehrerLehrbefaehigungAnerkennung {
	return obj as LehrerLehrbefaehigungAnerkennung;
}
