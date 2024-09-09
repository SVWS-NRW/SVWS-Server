import { JavaEnum } from '../../../java/lang/JavaEnum';
import type { JavaSet } from '../../../java/util/JavaSet';
import { BilingualeSpracheKatalogEintrag } from '../../../asd/data/fach/BilingualeSpracheKatalogEintrag';
import { HashMap } from '../../../java/util/HashMap';
import { CoreTypeDataManager } from '../../../asd/utils/CoreTypeDataManager';
import { Schulform } from '../../../asd/types/schule/Schulform';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import type { CoreType } from '../../../asd/types/CoreType';
import { de_svws_nrw_asd_types_CoreType_getManager, de_svws_nrw_asd_types_CoreType_daten, de_svws_nrw_asd_types_CoreType_historienId, de_svws_nrw_asd_types_CoreType_historie } from '../../../asd/types/CoreType';
import { JavaString } from '../../../java/lang/JavaString';
import { CoreTypeException } from '../../../asd/data/CoreTypeException';

export class BilingualeSprache extends JavaEnum<BilingualeSprache> implements CoreType<BilingualeSpracheKatalogEintrag, BilingualeSprache> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<BilingualeSprache> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, BilingualeSprache> = new Map<string, BilingualeSprache>();

	/**
	 * Bilinguale Sprache Englisch
	 */
	public static readonly ENGLISCH : BilingualeSprache = new BilingualeSprache("ENGLISCH", 0, );

	/**
	 * Bilinguale Sprache Französisch
	 */
	public static readonly FRANZOESISCH : BilingualeSprache = new BilingualeSprache("FRANZOESISCH", 1, );

	/**
	 * Bilinguale Sprache Italienisch
	 */
	public static readonly ITALIENISCH : BilingualeSprache = new BilingualeSprache("ITALIENISCH", 2, );

	/**
	 * Bilinguale Sprache Niederländisch
	 */
	public static readonly NIEDERLAENDISCH : BilingualeSprache = new BilingualeSprache("NIEDERLAENDISCH", 3, );

	/**
	 * Bilinguale Sprache Spanisch
	 */
	public static readonly SPANISCH : BilingualeSprache = new BilingualeSprache("SPANISCH", 4, );

	/**
	 * Bilinguale Sprache Türkisch
	 */
	public static readonly TUERKISCH : BilingualeSprache = new BilingualeSprache("TUERKISCH", 5, );

	/**
	 * Bilinguale Sprache Neugriechisch
	 */
	public static readonly NEUGRIECHIESCH : BilingualeSprache = new BilingualeSprache("NEUGRIECHIESCH", 6, );

	/**
	 * Die Menge der Schulformen. Diese ist nach der Initialisierung nicht leer.
	 */
	private static readonly _mapSchulformenByID : HashMap<number, JavaSet<Schulform>> = new HashMap<number, JavaSet<Schulform>>();

	private constructor(name : string, ordinal : number) {
		super(name, ordinal);
		BilingualeSprache.all_values_by_ordinal.push(this);
		BilingualeSprache.all_values_by_name.set(name, this);
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static init(manager : CoreTypeDataManager<BilingualeSpracheKatalogEintrag, BilingualeSprache>) : void {
		CoreTypeDataManager.putManager(BilingualeSprache.class, manager);
		for (const ct of BilingualeSprache.data().getWerte())
			for (const e of ct.historie())
				BilingualeSprache._mapSchulformenByID.put(e.id, Schulform.data().getWerteByBezeichnerAsNonEmptySet(e.schulformen));
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static data() : CoreTypeDataManager<BilingualeSpracheKatalogEintrag, BilingualeSprache> {
		return CoreTypeDataManager.getManager(BilingualeSprache.class);
	}

	/**
	 * Prüft, ob die Schulform bei diesem Core-Type-Wert in dem angegeben Schuljahr zulässig ist oder nicht.
	 *
	 * @param schuljahr   das zu prüfende Schuljahr
	 * @param sf          die Schulform, auf die geprüft wird
	 *
	 * @return true, falls die Schulform zulässig ist, und ansonsten false
	 */
	public hatSchulform(schuljahr : number, sf : Schulform) : boolean {
		const bske : BilingualeSpracheKatalogEintrag | null = this.daten(schuljahr);
		if (bske !== null) {
			const result : JavaSet<Schulform> | null = BilingualeSprache._mapSchulformenByID.get(bske.id);
			if (result === null)
				throw new CoreTypeException(JavaString.format("Fehler beim prüfen der Schulform. Der Core-Type %s ist nicht korrekt initialisiert.", this.getClass().getSimpleName()))
			return result.contains(sf);
		}
		return false;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<BilingualeSprache> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : BilingualeSprache | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	public getManager() : CoreTypeDataManager<BilingualeSpracheKatalogEintrag, BilingualeSprache> {
		return de_svws_nrw_asd_types_CoreType_getManager(this);
	}

	public daten(schuljahr : number) : BilingualeSpracheKatalogEintrag | null {
		return de_svws_nrw_asd_types_CoreType_daten(this, schuljahr);
	}

	public historienId() : number {
		return de_svws_nrw_asd_types_CoreType_historienId(this);
	}

	public historie() : List<BilingualeSpracheKatalogEintrag> {
		return de_svws_nrw_asd_types_CoreType_historie(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.types.fach.BilingualeSprache';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.types.CoreType', 'java.lang.Comparable', 'de.svws_nrw.asd.types.fach.BilingualeSprache', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<BilingualeSprache>('de.svws_nrw.asd.types.fach.BilingualeSprache');

}

export function cast_de_svws_nrw_asd_types_fach_BilingualeSprache(obj : unknown) : BilingualeSprache {
	return obj as BilingualeSprache;
}
