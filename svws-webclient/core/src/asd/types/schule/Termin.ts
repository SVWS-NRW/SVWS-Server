import { JavaEnum } from '../../../java/lang/JavaEnum';
import { TerminKatalogEintrag } from '../../../asd/data/schule/TerminKatalogEintrag';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class Termin extends JavaEnum<Termin> implements CoreType<TerminKatalogEintrag, Termin> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<Termin> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, Termin> = new Map<string, Termin>();

	/**
	 * Der letzte Unterrichtstag in dem ersten Halbjahr des Schuljahres
	 */
	public static readonly HALBJAHR_LETZTER_UNTERRICHTSTAG : Termin = new Termin("HALBJAHR_LETZTER_UNTERRICHTSTAG", 0, );

	/**
	 * Der letzte Unterrichtstag in dem ersten Halbjahr des zweiten Schuljahres der Qualifikationsphase
	 */
	public static readonly GOST_HALBJAHR_LETZTER_UNTERRICHTSTAG_Q2 : Termin = new Termin("GOST_HALBJAHR_LETZTER_UNTERRICHTSTAG_Q2", 1, );

	/**
	 * Der letzte Unterrichtstag des Schuljahres
	 */
	public static readonly SCHULJAHR_LETZTER_UNTERRICHTSTAG : Termin = new Termin("SCHULJAHR_LETZTER_UNTERRICHTSTAG", 2, );

	/**
	 * Der letzte Unterrichtstag in dem zweiten Halbjahr des zweiten Schuljahres der Qualifikationsphase
	 */
	public static readonly GOST_SCHULJAHR_LETZTER_UNTERRICHTSTAG_Q2 : Termin = new Termin("GOST_SCHULJAHR_LETZTER_UNTERRICHTSTAG_Q2", 3, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		Termin.all_values_by_ordinal.push(this);
		Termin.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<TerminKatalogEintrag, Termin>) : void {
		CoreTypeDataManager.putManager(Termin.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<TerminKatalogEintrag, Termin> {
		return CoreTypeDataManager.getManager(Termin.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Termin> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Termin | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<TerminKatalogEintrag, Termin> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : TerminKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : string | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<TerminKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.Termin';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.schule.Termin', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<Termin>('de.svws_nrw.asd.types.schule.Termin');

}

export function cast_de_svws_nrw_asd_types_schule_Termin(obj : unknown) : Termin {
	return obj as Termin;
}
