import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { BildungsgangTypKatalogEintrag } from '../../../asd/data/schule/BildungsgangTypKatalogEintrag';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_historienId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class WeiterbildungskollegBildungsgangTyp extends JavaEnum<WeiterbildungskollegBildungsgangTyp> implements CoreType<BildungsgangTypKatalogEintrag, WeiterbildungskollegBildungsgangTyp> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<WeiterbildungskollegBildungsgangTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, WeiterbildungskollegBildungsgangTyp> = new Map<string, WeiterbildungskollegBildungsgangTyp>();

	/**
	 * Abendgymnasium
	 */
	public static readonly ABENDGYMNASIUM : WeiterbildungskollegBildungsgangTyp = new WeiterbildungskollegBildungsgangTyp("ABENDGYMNASIUM", 0, );

	/**
	 * Abendrealschule
	 */
	public static readonly ABENDREALSCHULE : WeiterbildungskollegBildungsgangTyp = new WeiterbildungskollegBildungsgangTyp("ABENDREALSCHULE", 1, );

	/**
	 * Kolleg
	 */
	public static readonly KOLLEG : WeiterbildungskollegBildungsgangTyp = new WeiterbildungskollegBildungsgangTyp("KOLLEG", 2, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		WeiterbildungskollegBildungsgangTyp.all_values_by_ordinal.push(this);
		WeiterbildungskollegBildungsgangTyp.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<BildungsgangTypKatalogEintrag, WeiterbildungskollegBildungsgangTyp>) : void {
		CoreTypeDataManager.putManager(WeiterbildungskollegBildungsgangTyp.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<BildungsgangTypKatalogEintrag, WeiterbildungskollegBildungsgangTyp> {
		return CoreTypeDataManager.getManager(WeiterbildungskollegBildungsgangTyp.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<WeiterbildungskollegBildungsgangTyp> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : WeiterbildungskollegBildungsgangTyp | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<BildungsgangTypKatalogEintrag, WeiterbildungskollegBildungsgangTyp> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : BildungsgangTypKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public historienId() : number {
		return de_svws_nrw_asd_types_CoreType_historienId(this);
	}

	public historie() : List<BildungsgangTypKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.WeiterbildungskollegBildungsgangTyp';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.schule.WeiterbildungskollegBildungsgangTyp', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<WeiterbildungskollegBildungsgangTyp>('de.svws_nrw.asd.types.schule.WeiterbildungskollegBildungsgangTyp');

}

export function cast_de_svws_nrw_asd_types_schule_WeiterbildungskollegBildungsgangTyp(obj : unknown) : WeiterbildungskollegBildungsgangTyp {
	return obj as WeiterbildungskollegBildungsgangTyp;
}
