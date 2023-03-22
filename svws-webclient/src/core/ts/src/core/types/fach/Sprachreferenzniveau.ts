import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { SprachreferenzniveauKatalogEintrag, cast_de_nrw_schule_svws_core_data_fach_SprachreferenzniveauKatalogEintrag } from '../../../core/data/fach/SprachreferenzniveauKatalogEintrag';

export class Sprachreferenzniveau extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<Sprachreferenzniveau> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, Sprachreferenzniveau> = new Map<string, Sprachreferenzniveau>();

	/**
	 * Referenzniveau nach GeR A1.
	 */
	public static readonly A1 : Sprachreferenzniveau = new Sprachreferenzniveau("A1", 0, [new SprachreferenzniveauKatalogEintrag(1, "A1", null, null)]);

	/**
	 * Referenzniveau nach GeR A1 Plus
	 */
	public static readonly A1P : Sprachreferenzniveau = new Sprachreferenzniveau("A1P", 1, [new SprachreferenzniveauKatalogEintrag(2, "A1+", null, null)]);

	/**
	 * Referenzniveau nach GeR A1A2
	 */
	public static readonly A1A2 : Sprachreferenzniveau = new Sprachreferenzniveau("A1A2", 2, [new SprachreferenzniveauKatalogEintrag(3, "A1/A2", null, null)]);

	/**
	 * Referenzniveau nach GeR A2
	 */
	public static readonly A2 : Sprachreferenzniveau = new Sprachreferenzniveau("A2", 3, [new SprachreferenzniveauKatalogEintrag(4, "A2", null, null)]);

	/**
	 * Referenzniveau nach GeR A2 Plus
	 */
	public static readonly A2P : Sprachreferenzniveau = new Sprachreferenzniveau("A2P", 4, [new SprachreferenzniveauKatalogEintrag(5, "A2+", null, null)]);

	/**
	 * Referenzniveau nach GeR A2B1.
	 */
	public static readonly A2B1 : Sprachreferenzniveau = new Sprachreferenzniveau("A2B1", 5, [new SprachreferenzniveauKatalogEintrag(6, "A2/B1", null, null)]);

	/**
	 * Referenzniveau nach GeR B1.
	 */
	public static readonly B1 : Sprachreferenzniveau = new Sprachreferenzniveau("B1", 6, [new SprachreferenzniveauKatalogEintrag(7, "B1", null, null)]);

	/**
	 * Referenzniveau nach GeR B1 Plus.
	 */
	public static readonly B1P : Sprachreferenzniveau = new Sprachreferenzniveau("B1P", 7, [new SprachreferenzniveauKatalogEintrag(8, "B1+", null, null)]);

	/**
	 * Referenzniveau nach GeR B1B2.
	 */
	public static readonly B1B2 : Sprachreferenzniveau = new Sprachreferenzniveau("B1B2", 8, [new SprachreferenzniveauKatalogEintrag(9, "B1/B2", null, null)]);

	/**
	 * Referenzniveau nach GeR B2.
	 */
	public static readonly B2 : Sprachreferenzniveau = new Sprachreferenzniveau("B2", 9, [new SprachreferenzniveauKatalogEintrag(10, "B2", null, null)]);

	/**
	 * Referenzniveau nach GeR B2C1.
	 */
	public static readonly B2C1 : Sprachreferenzniveau = new Sprachreferenzniveau("B2C1", 10, [new SprachreferenzniveauKatalogEintrag(11, "B2/C1", null, null)]);

	/**
	 * Referenzniveau nach GeR C1.
	 */
	public static readonly C1 : Sprachreferenzniveau = new Sprachreferenzniveau("C1", 11, [new SprachreferenzniveauKatalogEintrag(12, "C1", null, null)]);

	/**
	 * Referenzniveau nach GeR C2.
	 */
	public static readonly C2 : Sprachreferenzniveau = new Sprachreferenzniveau("C2", 12, [new SprachreferenzniveauKatalogEintrag(13, "C2", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static VERSION : number = 1;

	/**
	 * Der aktuellen Daten des Sprachprüfungsniveaus
	 */
	public readonly daten : SprachreferenzniveauKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen des Sprachprüfungsniveaus
	 */
	public readonly historie : Array<SprachreferenzniveauKatalogEintrag>;

	/**
	 * Die Zuordnung der Sprachreferenzniveaus zu ihren IDs
	 */
	private static readonly _mapID : HashMap<number, Sprachreferenzniveau> = new HashMap();

	/**
	 * Die Zuordnung der Sprachreferenzniveaus zu ihren Bezeichnungen
	 */
	private static readonly _mapKuerzel : HashMap<string, Sprachreferenzniveau> = new HashMap();

	/**
	 *
	 * Erstellt ein neues Sprachreferenzniveau dieser Aufzählung.
	 *
	 * @param historie   die Historie des Sprachreferenzniveaus, welche ein Array von 
	 *                   {@link SprachreferenzniveauKatalogEintrag} ist  
	 */
	private constructor(name : string, ordinal : number, historie : Array<SprachreferenzniveauKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		Sprachreferenzniveau.all_values_by_ordinal.push(this);
		Sprachreferenzniveau.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den IDs der Sprachreferenzniveaus auf die zugehörigen Sprachreferenzniveaus
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den IDs der Sprachreferenzniveaus auf die zugehörigen Sprachreferenzniveaus
	 */
	private static getMapByID() : HashMap<number, Sprachreferenzniveau> {
		if (Sprachreferenzniveau._mapID.size() === 0)
			for (let l of Sprachreferenzniveau.values()) 
				Sprachreferenzniveau._mapID.put(l.daten.id, l);
		return Sprachreferenzniveau._mapID;
	}

	/**
	 * Gibt eine Map von den Bezeichnungen der Sprachreferenzniveaus auf die zugehörigen Sprachreferenzniveaus
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Bezeichnungen der Sprachreferenzniveaus auf die zugehörigen Sprachreferenzniveaus
	 */
	private static getMapByKuerzel() : HashMap<string, Sprachreferenzniveau> {
		if (Sprachreferenzniveau._mapKuerzel.size() === 0)
			for (let l of Sprachreferenzniveau.values()) 
				Sprachreferenzniveau._mapKuerzel.put(l.daten.kuerzel, l);
		return Sprachreferenzniveau._mapKuerzel;
	}

	/**
	 * Gibt das Sprachreferenzniveau für die übergebene ID zurück.
	 *
	 * @param id   die ID des Sprachreferenzniveaus
	 *
	 * @return das Sprachreferenzniveau oder null, wenn die ID ungültig ist
	 */
	public static getByID(id : number | null) : Sprachreferenzniveau | null {
		return Sprachreferenzniveau.getMapByID().get(id);
	}

	/**
	 * Gibt das Sprachreferenzniveau für das übergebene Kürzel zurück.
	 * 
	 * @param kuerzel   das Kürzel des Sprachreferenzniveaus
	 * 
	 * @return das Sprachreferenzniveau oder null, wenn das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : Sprachreferenzniveau | null {
		return Sprachreferenzniveau.getMapByKuerzel().get(kuerzel);
	}

	/**
	 * Vergleicht dieses Sprachreferenzniveau mit dem Niveau des übergebenen Kürzels.
	 * 
	 * @param kuerzel   das Kürzel des anderen Sprachreferenzniveaus
	 * 
	 * @return -1 (kleiner), 0 (gleich) oder 1 (größer)
	 */
	public vergleiche(kuerzel : string | null) : number {
		let other : Sprachreferenzniveau | null = Sprachreferenzniveau.getByKuerzel(kuerzel);
		if (other === null)
			return 1;
		return this.compareTo(other);
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	private name() : string {
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
	public toString() : string {
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
		if (!(other instanceof Sprachreferenzniveau))
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
	public compareTo(other : Sprachreferenzniveau) : number {
		return this.__ordinal - other.__ordinal;
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
		let tmp : Sprachreferenzniveau | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.fach.Sprachreferenzniveau'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_fach_Sprachreferenzniveau(obj : unknown) : Sprachreferenzniveau {
	return obj as Sprachreferenzniveau;
}
