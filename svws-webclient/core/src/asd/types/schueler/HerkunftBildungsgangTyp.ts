import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { HerkunftBildungsgangTypKatalogEintrag } from '../../../asd/data/schueler/HerkunftBildungsgangTypKatalogEintrag';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_statistikId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';

export class HerkunftBildungsgangTyp extends JavaEnum<HerkunftBildungsgangTyp> implements CoreType<HerkunftBildungsgangTypKatalogEintrag, HerkunftBildungsgangTyp> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<HerkunftBildungsgangTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, HerkunftBildungsgangTyp> = new Map<string, HerkunftBildungsgangTyp>();

	/**
	 * Weiterbildungskolleg: Abendgymnasium
	 */
	public static readonly AG : HerkunftBildungsgangTyp = new HerkunftBildungsgangTyp("AG", 0, );

	/**
	 * Weiterbildungskolleg: Abendrealschule
	 */
	public static readonly AR : HerkunftBildungsgangTyp = new HerkunftBildungsgangTyp("AR", 1, );

	/**
	 * Weiterbildungskolleg: Abendrealschule
	 */
	public static readonly KL : HerkunftBildungsgangTyp = new HerkunftBildungsgangTyp("KL", 2, );

	/**
	 * Berufskolleg: Berufsfachschule
	 */
	public static readonly BF : HerkunftBildungsgangTyp = new HerkunftBildungsgangTyp("BF", 3, );

	/**
	 * Berufskolleg: Berufschule
	 */
	public static readonly BS : HerkunftBildungsgangTyp = new HerkunftBildungsgangTyp("BS", 4, );

	/**
	 * Berufskolleg: Berufliches Gymnasium
	 */
	public static readonly BY : HerkunftBildungsgangTyp = new HerkunftBildungsgangTyp("BY", 5, );

	/**
	 * Berufskolleg: Fachoberschule
	 */
	public static readonly FO : HerkunftBildungsgangTyp = new HerkunftBildungsgangTyp("FO", 6, );

	/**
	 * Berufskolleg: Fachschule
	 */
	public static readonly FS : HerkunftBildungsgangTyp = new HerkunftBildungsgangTyp("FS", 7, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		HerkunftBildungsgangTyp.all_values_by_ordinal.push(this);
		HerkunftBildungsgangTyp.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<HerkunftBildungsgangTypKatalogEintrag, HerkunftBildungsgangTyp>) : void {
		CoreTypeDataManager.putManager(HerkunftBildungsgangTyp.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<HerkunftBildungsgangTypKatalogEintrag, HerkunftBildungsgangTyp> {
		return CoreTypeDataManager.getManager(HerkunftBildungsgangTyp.class);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<HerkunftBildungsgangTyp> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : HerkunftBildungsgangTyp | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<HerkunftBildungsgangTypKatalogEintrag, HerkunftBildungsgangTyp> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : HerkunftBildungsgangTypKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public statistikId() : String | null {
		return de_svws_nrw_asd_types_CoreType_statistikId(this);
	}

	public historie() : List<HerkunftBildungsgangTypKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.schueler.HerkunftBildungsgangTyp';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.schueler.HerkunftBildungsgangTyp', 'de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<HerkunftBildungsgangTyp>('de.svws_nrw.asd.types.schueler.HerkunftBildungsgangTyp');

}

export function cast_de_svws_nrw_asd_types_schueler_HerkunftBildungsgangTyp(obj : unknown) : HerkunftBildungsgangTyp {
	return obj as HerkunftBildungsgangTyp;
}
