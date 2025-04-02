import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { BildungsgangTypKatalogEintrag } from '../../../asd/data/schule/BildungsgangTypKatalogEintrag';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class BerufskollegBildungsgangTyp extends JavaEnum<BerufskollegBildungsgangTyp> implements CoreType<BildungsgangTypKatalogEintrag, BerufskollegBildungsgangTyp> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<BerufskollegBildungsgangTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, BerufskollegBildungsgangTyp> = new Map<string, BerufskollegBildungsgangTyp>();

	/**
	 * Berufsfachschulen
	 */
	public static readonly BERUFSFACHSCHULE : BerufskollegBildungsgangTyp = new BerufskollegBildungsgangTyp("BERUFSFACHSCHULE", 0, );

	/**
	 * Berufsfachschulen
	 */
	public static readonly BERUFSSCHULE : BerufskollegBildungsgangTyp = new BerufskollegBildungsgangTyp("BERUFSSCHULE", 1, );

	/**
	 * Berufliches Gymnasium
	 */
	public static readonly BERUFLICHES_GYMNASIUM : BerufskollegBildungsgangTyp = new BerufskollegBildungsgangTyp("BERUFLICHES_GYMNASIUM", 2, );

	/**
	 * Fachoberschule
	 */
	public static readonly FACHOBERSCHULE : BerufskollegBildungsgangTyp = new BerufskollegBildungsgangTyp("FACHOBERSCHULE", 3, );

	/**
	 * Fachschule
	 */
	public static readonly FACHSCHULE : BerufskollegBildungsgangTyp = new BerufskollegBildungsgangTyp("FACHSCHULE", 4, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		BerufskollegBildungsgangTyp.all_values_by_ordinal.push(this);
		BerufskollegBildungsgangTyp.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<BildungsgangTypKatalogEintrag, BerufskollegBildungsgangTyp>) : void {
		CoreTypeDataManager.putManager(BerufskollegBildungsgangTyp.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<BildungsgangTypKatalogEintrag, BerufskollegBildungsgangTyp> {
		return CoreTypeDataManager.getManager(BerufskollegBildungsgangTyp.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<BerufskollegBildungsgangTyp> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : BerufskollegBildungsgangTyp | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<BildungsgangTypKatalogEintrag, BerufskollegBildungsgangTyp> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : BildungsgangTypKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : string | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<BildungsgangTypKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schule.BerufskollegBildungsgangTyp';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.schule.BerufskollegBildungsgangTyp', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<BerufskollegBildungsgangTyp>('de.svws_nrw.asd.types.schule.BerufskollegBildungsgangTyp');

}

export function cast_de_svws_nrw_asd_types_schule_BerufskollegBildungsgangTyp(obj : unknown) : BerufskollegBildungsgangTyp {
	return obj as BerufskollegBildungsgangTyp;
}
