import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag } from '../../../asd/data/jahrgang/PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class PrimarstufeSchuleingangsphaseBesuchsjahre extends JavaEnum<PrimarstufeSchuleingangsphaseBesuchsjahre> implements CoreType<PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag, PrimarstufeSchuleingangsphaseBesuchsjahre> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<PrimarstufeSchuleingangsphaseBesuchsjahre> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, PrimarstufeSchuleingangsphaseBesuchsjahre> = new Map<string, PrimarstufeSchuleingangsphaseBesuchsjahre>();

	/**
	 * E1: Das erste Besuchsjahr in der Schuleingangsphase
	 */
	public static readonly E1 : PrimarstufeSchuleingangsphaseBesuchsjahre = new PrimarstufeSchuleingangsphaseBesuchsjahre("E1", 0, );

	/**
	 * E2: Das zweite Besuchsjahr in der Schuleingangsphase
	 */
	public static readonly E2 : PrimarstufeSchuleingangsphaseBesuchsjahre = new PrimarstufeSchuleingangsphaseBesuchsjahre("E2", 1, );

	/**
	 * E3: Das dritte Besuchsjahr in der Schuleingangsphase
	 */
	public static readonly E3 : PrimarstufeSchuleingangsphaseBesuchsjahre = new PrimarstufeSchuleingangsphaseBesuchsjahre("E3", 2, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		PrimarstufeSchuleingangsphaseBesuchsjahre.all_values_by_ordinal.push(this);
		PrimarstufeSchuleingangsphaseBesuchsjahre.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag, PrimarstufeSchuleingangsphaseBesuchsjahre>) : void {
		CoreTypeDataManager.putManager(PrimarstufeSchuleingangsphaseBesuchsjahre.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag, PrimarstufeSchuleingangsphaseBesuchsjahre> {
		return CoreTypeDataManager.getManager(PrimarstufeSchuleingangsphaseBesuchsjahre.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<PrimarstufeSchuleingangsphaseBesuchsjahre> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : PrimarstufeSchuleingangsphaseBesuchsjahre | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag, PrimarstufeSchuleingangsphaseBesuchsjahre> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : string | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahre';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahre', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<PrimarstufeSchuleingangsphaseBesuchsjahre>('de.svws_nrw.asd.types.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahre');

}

export function cast_de_svws_nrw_asd_types_jahrgang_PrimarstufeSchuleingangsphaseBesuchsjahre(obj : unknown) : PrimarstufeSchuleingangsphaseBesuchsjahre {
	return obj as PrimarstufeSchuleingangsphaseBesuchsjahre;
}
