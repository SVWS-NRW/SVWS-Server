import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { KindergartenbesuchKatalogEintrag, cast_de_nrw_schule_svws_core_data_schule_KindergartenbesuchKatalogEintrag } from '../../../core/data/schule/KindergartenbesuchKatalogEintrag';

export class Kindergartenbesuch extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : String;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<Kindergartenbesuch> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<String, Kindergartenbesuch> = new Map<String, Kindergartenbesuch>();

	public static readonly KEINER : Kindergartenbesuch = new Kindergartenbesuch("KEINER", 0, [new KindergartenbesuchKatalogEintrag(1, 0, "kein Kindergarten", null, null)]);

	public static readonly MAX_1_JAHR : Kindergartenbesuch = new Kindergartenbesuch("MAX_1_JAHR", 1, [new KindergartenbesuchKatalogEintrag(2, 1, "unter 1 Jahr", null, null)]);

	public static readonly MAX_2_JAHRE : Kindergartenbesuch = new Kindergartenbesuch("MAX_2_JAHRE", 2, [new KindergartenbesuchKatalogEintrag(3, 2, "1 bis unter 2 Jahre", null, null)]);

	public static readonly MAX_3_JAHRE : Kindergartenbesuch = new Kindergartenbesuch("MAX_3_JAHRE", 3, [new KindergartenbesuchKatalogEintrag(4, 3, "2 bis unter 3 Jahre", null, null)]);

	public static readonly MIN_3_JAHRE : Kindergartenbesuch = new Kindergartenbesuch("MIN_3_JAHRE", 4, [new KindergartenbesuchKatalogEintrag(5, 4, "3 Jahre und mehr Jahre", null, null)]);

	public static VERSION : number = 1;

	public readonly daten : KindergartenbesuchKatalogEintrag;

	public readonly historie : Array<KindergartenbesuchKatalogEintrag>;

	private static readonly _mapKuerzel : HashMap<Number, Kindergartenbesuch | null> = new HashMap();

	/**
	 * Erzeugt einen neuen Eintrag in der Aufzählung.
	 * 
	 * @param historie   die Historie der Eintrags, welche ein Array von 
	 *                   {@link KindergartenbesuchKatalogEintrag} ist  
	 */
	private constructor(name : string, ordinal : number, historie : Array<KindergartenbesuchKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		Kindergartenbesuch.all_values_by_ordinal.push(this);
		Kindergartenbesuch.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzeln auf den zugehörigen Core-Type-Wert. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln auf den zugehörigen Core-Type-Wert
	 */
	private static getMapByKuerzel() : HashMap<Number, Kindergartenbesuch | null> {
		if (Kindergartenbesuch._mapKuerzel.size() === 0) {
			for (let s of Kindergartenbesuch.values()) {
				if (s.daten !== null) 
					Kindergartenbesuch._mapKuerzel.put(s.daten.kuerzel, s);
			}
		}
		return Kindergartenbesuch._mapKuerzel;
	}

	/**
	 * Gibt den Core-Type-Wert für das angegebe Kürzel der Dauer des Kindergartenbesuchs zurück.
	 * 
	 * @param kuerzel   das Kürzel der Dauer
	 * 
	 * @return der Core-Type-Wert oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : number) : Kindergartenbesuch | null {
		return Kindergartenbesuch.getMapByKuerzel().get(kuerzel);
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
		if (!(other instanceof Kindergartenbesuch))
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
	public compareTo(other : Kindergartenbesuch) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Kindergartenbesuch> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : String) : Kindergartenbesuch | null {
		let tmp : Kindergartenbesuch | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.schule.Kindergartenbesuch'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_schule_Kindergartenbesuch(obj : unknown) : Kindergartenbesuch {
	return obj as Kindergartenbesuch;
}
