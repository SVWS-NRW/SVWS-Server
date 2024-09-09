import { JavaEnum } from '../../../java/lang/JavaEnum';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_historienId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';
import { SprachreferenzniveauKatalogEintrag } from '../../../asd/data/fach/SprachreferenzniveauKatalogEintrag';

export class Sprachreferenzniveau extends JavaEnum<Sprachreferenzniveau> implements CoreType<SprachreferenzniveauKatalogEintrag, Sprachreferenzniveau> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<Sprachreferenzniveau> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, Sprachreferenzniveau> = new Map<string, Sprachreferenzniveau>();

	/**
	 * Referenzniveau nach GeR A1.
	 */
	public static readonly A1 : Sprachreferenzniveau = new Sprachreferenzniveau("A1", 0, );

	/**
	 * Referenzniveau nach GeR A1 Plus
	 */
	public static readonly A1P : Sprachreferenzniveau = new Sprachreferenzniveau("A1P", 1, );

	/**
	 * Referenzniveau nach GeR A1A2
	 */
	public static readonly A1A2 : Sprachreferenzniveau = new Sprachreferenzniveau("A1A2", 2, );

	/**
	 * Referenzniveau nach GeR A2
	 */
	public static readonly A2 : Sprachreferenzniveau = new Sprachreferenzniveau("A2", 3, );

	/**
	 * Referenzniveau nach GeR A2 Plus
	 */
	public static readonly A2P : Sprachreferenzniveau = new Sprachreferenzniveau("A2P", 4, );

	/**
	 * Referenzniveau nach GeR A2B1.
	 */
	public static readonly A2B1 : Sprachreferenzniveau = new Sprachreferenzniveau("A2B1", 5, );

	/**
	 * Referenzniveau nach GeR B1.
	 */
	public static readonly B1 : Sprachreferenzniveau = new Sprachreferenzniveau("B1", 6, );

	/**
	 * Referenzniveau nach GeR B1 Plus.
	 */
	public static readonly B1P : Sprachreferenzniveau = new Sprachreferenzniveau("B1P", 7, );

	/**
	 * Referenzniveau nach GeR B1B2.
	 */
	public static readonly B1B2 : Sprachreferenzniveau = new Sprachreferenzniveau("B1B2", 8, );

	/**
	 * Referenzniveau nach GeR B2.
	 */
	public static readonly B2 : Sprachreferenzniveau = new Sprachreferenzniveau("B2", 9, );

	/**
	 * Referenzniveau nach GeR B2C1.
	 */
	public static readonly B2C1 : Sprachreferenzniveau = new Sprachreferenzniveau("B2C1", 10, );

	/**
	 * Referenzniveau nach GeR C1.
	 */
	public static readonly C1 : Sprachreferenzniveau = new Sprachreferenzniveau("C1", 11, );

	/**
	 * Referenzniveau nach GeR C2.
	 */
	public static readonly C2 : Sprachreferenzniveau = new Sprachreferenzniveau("C2", 12, );

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		Sprachreferenzniveau.all_values_by_ordinal.push(this);
		Sprachreferenzniveau.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<SprachreferenzniveauKatalogEintrag, Sprachreferenzniveau>) : void {
		CoreTypeDataManager.putManager(Sprachreferenzniveau.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<SprachreferenzniveauKatalogEintrag, Sprachreferenzniveau> {
		return CoreTypeDataManager.getManager(Sprachreferenzniveau.class);
	}

	/**
	 * Vergleicht dieses Sprachreferenzniveau mit dem übergebenen Niveau.
	 *
	 * @param other   das andere Sprachreferenzniveau
	 *
	 * @return -1 (kleiner), 0 (gleich) oder 1 (größer)
	 */
	public compare(other : Sprachreferenzniveau | null) : number {
		if (other === null)
			return 1;
		return this.compareTo(other);
	}

	/**
	 * Vergleicht dieses Sprachreferenzniveau mit dem Niveau des übergebenen Kürzels.
	 *
	 * @param kuerzel   das Kürzel des anderen Sprachreferenzniveaus
	 *
	 * @return -1 (kleiner), 0 (gleich) oder 1 (größer)
	 */
	public compareByKuerzel(kuerzel : string | null) : number {
		if (kuerzel === null)
			return 1;
		return this.compare(Sprachreferenzniveau.data().getWertByKuerzel(kuerzel));
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Sprachreferenzniveau> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Sprachreferenzniveau | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<SprachreferenzniveauKatalogEintrag, Sprachreferenzniveau> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : SprachreferenzniveauKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public historienId() : number {
		return de_svws_nrw_asd_types_CoreType_historienId(this);
	}

	public historie() : List<SprachreferenzniveauKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.fach.Sprachreferenzniveau';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.fach.Sprachreferenzniveau', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<Sprachreferenzniveau>('de.svws_nrw.asd.types.fach.Sprachreferenzniveau');

}

export function cast_de_svws_nrw_asd_types_fach_Sprachreferenzniveau(obj : unknown) : Sprachreferenzniveau {
	return obj as Sprachreferenzniveau;
}
