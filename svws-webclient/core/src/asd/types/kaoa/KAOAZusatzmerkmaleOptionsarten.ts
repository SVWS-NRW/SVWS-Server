import { JavaEnum } from '../../../java/lang/JavaEnum';
import { KAOAZusatzmerkmaleOptionsartenKatalogEintrag } from '../../../asd/data/kaoa/KAOAZusatzmerkmaleOptionsartenKatalogEintrag';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class KAOAZusatzmerkmaleOptionsarten extends JavaEnum<KAOAZusatzmerkmaleOptionsarten> implements CoreType<KAOAZusatzmerkmaleOptionsartenKatalogEintrag, KAOAZusatzmerkmaleOptionsarten> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<KAOAZusatzmerkmaleOptionsarten> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, KAOAZusatzmerkmaleOptionsarten> = new Map<string, KAOAZusatzmerkmaleOptionsarten>();

	/**
	 * Keine Option für das KAoA-Zusatzmerkmal
	 */
	public static readonly KEINE : KAOAZusatzmerkmaleOptionsarten = new KAOAZusatzmerkmaleOptionsarten("KEINE", 0, );

	/**
	 * Anschlussoptionen laut SBO 10.7
	 */
	public static readonly ANSCHLUSSOPTION : KAOAZusatzmerkmaleOptionsarten = new KAOAZusatzmerkmaleOptionsarten("ANSCHLUSSOPTION", 1, );

	/**
	 * Berufsfelder
	 */
	public static readonly BERUFSFELD : KAOAZusatzmerkmaleOptionsarten = new KAOAZusatzmerkmaleOptionsarten("BERUFSFELD", 2, );

	/**
	 * Freitext
	 */
	public static readonly FREITEXT : KAOAZusatzmerkmaleOptionsarten = new KAOAZusatzmerkmaleOptionsarten("FREITEXT", 3, );

	/**
	 * Freitext Beruf
	 */
	public static readonly FREITEXT_BERUF : KAOAZusatzmerkmaleOptionsarten = new KAOAZusatzmerkmaleOptionsarten("FREITEXT_BERUF", 4, );

	/**
	 * SBO der Ebene 4 (SBO x.x.x.y)
	 */
	public static readonly SBO_EBENE_4 : KAOAZusatzmerkmaleOptionsarten = new KAOAZusatzmerkmaleOptionsarten("SBO_EBENE_4", 5, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		KAOAZusatzmerkmaleOptionsarten.all_values_by_ordinal.push(this);
		KAOAZusatzmerkmaleOptionsarten.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<KAOAZusatzmerkmaleOptionsartenKatalogEintrag, KAOAZusatzmerkmaleOptionsarten>) : void {
		CoreTypeDataManager.putManager(KAOAZusatzmerkmaleOptionsarten.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<KAOAZusatzmerkmaleOptionsartenKatalogEintrag, KAOAZusatzmerkmaleOptionsarten> {
		return CoreTypeDataManager.getManager(KAOAZusatzmerkmaleOptionsarten.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<KAOAZusatzmerkmaleOptionsarten> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : KAOAZusatzmerkmaleOptionsarten | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<KAOAZusatzmerkmaleOptionsartenKatalogEintrag, KAOAZusatzmerkmaleOptionsarten> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : KAOAZusatzmerkmaleOptionsartenKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : string | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<KAOAZusatzmerkmaleOptionsartenKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.kaoa.KAOAZusatzmerkmaleOptionsarten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.kaoa.KAOAZusatzmerkmaleOptionsarten', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<KAOAZusatzmerkmaleOptionsarten>('de.svws_nrw.asd.types.kaoa.KAOAZusatzmerkmaleOptionsarten');

}

export function cast_de_svws_nrw_asd_types_kaoa_KAOAZusatzmerkmaleOptionsarten(obj : unknown) : KAOAZusatzmerkmaleOptionsarten {
	return obj as KAOAZusatzmerkmaleOptionsarten;
}
