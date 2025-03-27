import { JavaEnum } from '../../../java/lang/JavaEnum';
import { KAOAMerkmaleOptionsartenKatalogEintrag } from '../../../asd/data/kaoa/KAOAMerkmaleOptionsartenKatalogEintrag';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class KAOAMerkmaleOptionsarten extends JavaEnum<KAOAMerkmaleOptionsarten> implements CoreType<KAOAMerkmaleOptionsartenKatalogEintrag, KAOAMerkmaleOptionsarten> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<KAOAMerkmaleOptionsarten> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, KAOAMerkmaleOptionsarten> = new Map<string, KAOAMerkmaleOptionsarten>();

	/**
	 * Keine Option für das KAoA-Merkmal
	 */
	public static readonly KEINE : KAOAMerkmaleOptionsarten = new KAOAMerkmaleOptionsarten("KEINE", 0, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		KAOAMerkmaleOptionsarten.all_values_by_ordinal.push(this);
		KAOAMerkmaleOptionsarten.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<KAOAMerkmaleOptionsartenKatalogEintrag, KAOAMerkmaleOptionsarten>) : void {
		CoreTypeDataManager.putManager(KAOAMerkmaleOptionsarten.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<KAOAMerkmaleOptionsartenKatalogEintrag, KAOAMerkmaleOptionsarten> {
		return CoreTypeDataManager.getManager(KAOAMerkmaleOptionsarten.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<KAOAMerkmaleOptionsarten> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : KAOAMerkmaleOptionsarten | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<KAOAMerkmaleOptionsartenKatalogEintrag, KAOAMerkmaleOptionsarten> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : KAOAMerkmaleOptionsartenKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : String | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<KAOAMerkmaleOptionsartenKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.kaoa.KAOAMerkmaleOptionsarten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.kaoa.KAOAMerkmaleOptionsarten', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<KAOAMerkmaleOptionsarten>('de.svws_nrw.asd.types.kaoa.KAOAMerkmaleOptionsarten');

}

export function cast_de_svws_nrw_asd_types_kaoa_KAOAMerkmaleOptionsarten(obj : unknown) : KAOAMerkmaleOptionsarten {
	return obj as KAOAMerkmaleOptionsarten;
}
