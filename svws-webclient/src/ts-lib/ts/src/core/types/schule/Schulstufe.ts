import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { SchulstufeKatalogEintrag, cast_de_nrw_schule_svws_core_data_schule_SchulstufeKatalogEintrag } from '../../../core/data/schule/SchulstufeKatalogEintrag';

export class Schulstufe extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : String;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<Schulstufe> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<String, Schulstufe> = new Map<String, Schulstufe>();

	public static readonly VORSCHULSTUFE : Schulstufe = new Schulstufe("VORSCHULSTUFE", 0, [new SchulstufeKatalogEintrag(0, "V", "Vorschulstufe", null, null)]);

	public static readonly PRIMARSTUFE : Schulstufe = new Schulstufe("PRIMARSTUFE", 1, [new SchulstufeKatalogEintrag(1000, "P", "Primarstufe", null, null)]);

	public static readonly SEKUNDARSTUFE_I : Schulstufe = new Schulstufe("SEKUNDARSTUFE_I", 2, [new SchulstufeKatalogEintrag(2000, "SI", "Sekundarstufe I", null, null)]);

	public static readonly SEKUNDARSTUFE_II : Schulstufe = new Schulstufe("SEKUNDARSTUFE_II", 3, [new SchulstufeKatalogEintrag(3000, "SII", "Sekundarstufe II", null, null)]);

	public static readonly TERTIAERSTUFE : Schulstufe = new Schulstufe("TERTIAERSTUFE", 4, [new SchulstufeKatalogEintrag(4000, "T", "Tertiärstufe", null, null)]);

	public static VERSION : number = 1;

	public readonly daten : SchulstufeKatalogEintrag;

	public readonly historie : Array<SchulstufeKatalogEintrag>;

	private static readonly _mapByKuerzel : HashMap<String, Schulstufe> = new HashMap();

	/**
	 * Erzeugt eine neue Schulstufe in der Aufzählung.
	 * 
	 * @param historie   die Historie der Schulstufe, welches ein Array von {@link SchulstufeKatalogEintrag} ist  
	 */
	private constructor(name : string, ordinal : number, historie : Array<SchulstufeKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		Schulstufe.all_values_by_ordinal.push(this);
		Schulstufe.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzeln der Schulstufen auf die zugehörigen Schulstufen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln der Schulstufen auf die zugehörigen Schulstufen
	 */
	private static getMapByKuerzel() : HashMap<String, Schulstufe> {
		if (Schulstufe._mapByKuerzel.size() === 0) {
			for (let s of Schulstufe.values()) {
				if (s.daten !== null) 
					Schulstufe._mapByKuerzel.put(s.daten.kuerzel, s);
			}
		}
		return Schulstufe._mapByKuerzel;
	}

	/**
	 * Gibt die Schulstufe für das angegebene Kürzel zurück.
	 * 
	 * @param kuerzel   das Kürzel der Schulstufe
	 * 
	 * @return die Schulstufe oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : String | null) : Schulstufe | null {
		return Schulstufe.getMapByKuerzel().get(kuerzel);
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
		if (!(other instanceof Schulstufe))
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
	public compareTo(other : Schulstufe) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Schulstufe> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : String) : Schulstufe | null {
		let tmp : Schulstufe | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.schule.Schulstufe'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_schule_Schulstufe(obj : unknown) : Schulstufe {
	return obj as Schulstufe;
}
