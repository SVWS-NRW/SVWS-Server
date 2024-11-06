import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { LehrerRechtsverhaeltnisKatalogEintrag } from '../../../asd/data/lehrer/LehrerRechtsverhaeltnisKatalogEintrag';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_historienId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class LehrerRechtsverhaeltnis extends JavaEnum<LehrerRechtsverhaeltnis> implements CoreType<LehrerRechtsverhaeltnisKatalogEintrag, LehrerRechtsverhaeltnis> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<LehrerRechtsverhaeltnis> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, LehrerRechtsverhaeltnis> = new Map<string, LehrerRechtsverhaeltnis>();

	/**
	 * Rechtsverhältnis 'Beamter auf Lebenszeit'
	 */
	public static readonly L : LehrerRechtsverhaeltnis = new LehrerRechtsverhaeltnis("L", 0, );

	/**
	 * Rechtsverhältnis 'Beamter auf Probe'
	 */
	public static readonly P : LehrerRechtsverhaeltnis = new LehrerRechtsverhaeltnis("P", 1, );

	/**
	 * Rechtsverhältnis 'Beamter auf Probe zur Anstellung'
	 */
	public static readonly A : LehrerRechtsverhaeltnis = new LehrerRechtsverhaeltnis("A", 2, );

	/**
	 * Rechtsverhältnis 'Beamter, nebenamtlich (nicht hauptamtlich im Schuldienst)'
	 */
	public static readonly N : LehrerRechtsverhaeltnis = new LehrerRechtsverhaeltnis("N", 3, );

	/**
	 * Rechtsverhältnis 'Beamter auf Widerruf (LAA)'
	 */
	public static readonly W : LehrerRechtsverhaeltnis = new LehrerRechtsverhaeltnis("W", 4, );

	/**
	 * Rechtsverhältnis 'Angestellte, unbefristet (BAT-Vertrag)'
	 */
	public static readonly U : LehrerRechtsverhaeltnis = new LehrerRechtsverhaeltnis("U", 5, );

	/**
	 * Rechtsverhältnis 'Angestellte, befristet (BAT-Vertrag)'
	 */
	public static readonly B : LehrerRechtsverhaeltnis = new LehrerRechtsverhaeltnis("B", 6, );

	/**
	 * Rechtsverhältnis 'Angestellte, nicht BAT-Vertrag'
	 */
	public static readonly J : LehrerRechtsverhaeltnis = new LehrerRechtsverhaeltnis("J", 7, );

	/**
	 * Rechtsverhältnis 'Gestellungsvertrag'
	 */
	public static readonly S : LehrerRechtsverhaeltnis = new LehrerRechtsverhaeltnis("S", 8, );

	/**
	 * Rechtsverhältnis 'Unentgeltlich Beschäftigte'
	 */
	public static readonly X : LehrerRechtsverhaeltnis = new LehrerRechtsverhaeltnis("X", 9, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		LehrerRechtsverhaeltnis.all_values_by_ordinal.push(this);
		LehrerRechtsverhaeltnis.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<LehrerRechtsverhaeltnisKatalogEintrag, LehrerRechtsverhaeltnis>) : void {
		CoreTypeDataManager.putManager(LehrerRechtsverhaeltnis.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<LehrerRechtsverhaeltnisKatalogEintrag, LehrerRechtsverhaeltnis> {
		return CoreTypeDataManager.getManager(LehrerRechtsverhaeltnis.class);
	}

	/**
	 * Gibt das Rechtsverhältnis zu dem übergebenen Schlüssel zurück. Bei einem ungültigen Schlüssel
	 * wird null zurückgegeben,
	 *
	 * @param schluessel   der Schlüsselwert
	 *
	 * @return das zugehörige Rechtsverhältnis oder null
	 */
	public static getBySchluessel(schluessel : string | null) : LehrerRechtsverhaeltnis | null {
		if (schluessel === null)
			return null;
		return LehrerRechtsverhaeltnis.data().getWertBySchluessel(schluessel);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LehrerRechtsverhaeltnis> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : LehrerRechtsverhaeltnis | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<LehrerRechtsverhaeltnisKatalogEintrag, LehrerRechtsverhaeltnis> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : LehrerRechtsverhaeltnisKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public historienId() : number {
		return de_svws_nrw_asd_types_CoreType_historienId(this);
	}

	public historie() : List<LehrerRechtsverhaeltnisKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<LehrerRechtsverhaeltnis>('de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis');

}

export function cast_de_svws_nrw_asd_types_lehrer_LehrerRechtsverhaeltnis(obj : unknown) : LehrerRechtsverhaeltnis {
	return obj as LehrerRechtsverhaeltnis;
}
