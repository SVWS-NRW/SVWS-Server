import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { BerufskollegBildungsgangTyp, cast_de_nrw_schule_svws_core_types_schule_BerufskollegBildungsgangTyp } from '../../../core/types/schule/BerufskollegBildungsgangTyp';
import { HerkunftBildungsgangTypKatalogEintrag, cast_de_nrw_schule_svws_core_data_schule_HerkunftBildungsgangTypKatalogEintrag } from '../../../core/data/schule/HerkunftBildungsgangTypKatalogEintrag';
import { WeiterbildungskollegBildungsgangTyp, cast_de_nrw_schule_svws_core_types_schule_WeiterbildungskollegBildungsgangTyp } from '../../../core/types/schule/WeiterbildungskollegBildungsgangTyp';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class HerkunftBildungsgangsTyp extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : String;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<HerkunftBildungsgangsTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<String, HerkunftBildungsgangsTyp> = new Map<String, HerkunftBildungsgangsTyp>();

	public static readonly AG : HerkunftBildungsgangsTyp = new HerkunftBildungsgangsTyp("AG", 0, [new HerkunftBildungsgangTypKatalogEintrag(1000, WeiterbildungskollegBildungsgangTyp.ABENDGYMNASIUM, null, null, null)]);

	public static readonly AR : HerkunftBildungsgangsTyp = new HerkunftBildungsgangsTyp("AR", 1, [new HerkunftBildungsgangTypKatalogEintrag(2000, WeiterbildungskollegBildungsgangTyp.ABENDREALSCHULE, null, null, null)]);

	public static readonly KL : HerkunftBildungsgangsTyp = new HerkunftBildungsgangsTyp("KL", 2, [new HerkunftBildungsgangTypKatalogEintrag(3000, WeiterbildungskollegBildungsgangTyp.KOLLEG, null, null, null)]);

	public static readonly BF : HerkunftBildungsgangsTyp = new HerkunftBildungsgangsTyp("BF", 3, [new HerkunftBildungsgangTypKatalogEintrag(10000, null, BerufskollegBildungsgangTyp.BERUFSFACHSCHULE, null, null)]);

	public static readonly BS : HerkunftBildungsgangsTyp = new HerkunftBildungsgangsTyp("BS", 4, [new HerkunftBildungsgangTypKatalogEintrag(11000, null, BerufskollegBildungsgangTyp.BERUFSSCHULE, null, null)]);

	public static readonly BY : HerkunftBildungsgangsTyp = new HerkunftBildungsgangsTyp("BY", 5, [new HerkunftBildungsgangTypKatalogEintrag(12000, null, BerufskollegBildungsgangTyp.BERUFLICHES_GYMNASIUM, null, null)]);

	public static readonly FO : HerkunftBildungsgangsTyp = new HerkunftBildungsgangsTyp("FO", 6, [new HerkunftBildungsgangTypKatalogEintrag(13000, null, BerufskollegBildungsgangTyp.FACHOBERSCHULE, null, null)]);

	public static readonly FS : HerkunftBildungsgangsTyp = new HerkunftBildungsgangsTyp("FS", 7, [new HerkunftBildungsgangTypKatalogEintrag(14000, null, BerufskollegBildungsgangTyp.FACHSCHULE, null, null)]);

	public static VERSION : number = 1;

	public readonly daten : HerkunftBildungsgangTypKatalogEintrag;

	public readonly historie : Array<HerkunftBildungsgangTypKatalogEintrag>;

	private static readonly _ebenen : HashMap<String, HerkunftBildungsgangsTyp> = new HashMap();

	/**
	 * Erzeugt eine neuen Herkunfts-Bildungsgangtyp in der Aufzählung.
	 * 
	 * @param historie   die Historie des Herkunfts-Bildungsgangtyps, welche ein Array von 
	 *                   {@link HerkunftBildungsgangTypKatalogEintrag} ist  
	 */
	private constructor(name : string, ordinal : number, historie : Array<HerkunftBildungsgangTypKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		HerkunftBildungsgangsTyp.all_values_by_ordinal.push(this);
		HerkunftBildungsgangsTyp.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzeln der Bildungsgangtypen auf die 
	 * zugehörigen Bildungsgangtypen zurück. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln auf die zugehörigen Bildungsgangtypen
	 */
	private static getMapByKuerzel() : HashMap<String, HerkunftBildungsgangsTyp> {
		if (HerkunftBildungsgangsTyp._ebenen.size() === 0) {
			for (let s of HerkunftBildungsgangsTyp.values()) {
				if (s.daten !== null) 
					HerkunftBildungsgangsTyp._ebenen.put(s.daten.kuerzel, s);
			}
		}
		return HerkunftBildungsgangsTyp._ebenen;
	}

	/**
	 * Gibt den Herkunfts-Bildungsgangtypen für das angegebene Kürzel zurück.
	 * 
	 * @param kuerzel   das Kürzel des Herkunfts-Bildungsgangtyps
	 * 
	 * @return der Herkunfts-Bildungsgangtyp oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : String | null) : HerkunftBildungsgangsTyp | null {
		return HerkunftBildungsgangsTyp.getMapByKuerzel().get(kuerzel);
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	private name() : String {
		return this.__name;
	}

	/**
	 * Returns the ordinal value of this enumeration value.
	 *
	 * @returns the ordinal value
	 */
	private ordinal() : number {
		return this.__ordinal;
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	public toString() : String {
		return this.__name;
	}

	/**
	 * Returns true if this and the other enumeration values are equal.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns true if they are equal and false otherwise
	 */
	public equals(other : JavaObject) : boolean {
		if (!(other instanceof HerkunftBildungsgangsTyp))
			return false;
		return this === other;
	}

	/**
	 * Returns the ordinal value as hashcode, since the ordinal value is unique.
	 *
	 * @returns the ordinal value as hashcode
	 */
	public hashCode() : number {
		return this.__ordinal;
	}

	/**
	 * Compares this enumeration value with the other enumeration value by their ordinal value.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns a negative, zero or postive value as this enumeration value is less than, equal to
	 *          or greater than the other enumeration value
	 */
	public compareTo(other : HerkunftBildungsgangsTyp) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<HerkunftBildungsgangsTyp> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : String) : HerkunftBildungsgangsTyp | null {
		let tmp : HerkunftBildungsgangsTyp | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.statkue.HerkunftBildungsgangsTyp'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_statkue_HerkunftBildungsgangsTyp(obj : unknown) : HerkunftBildungsgangsTyp {
	return obj as HerkunftBildungsgangsTyp;
}
