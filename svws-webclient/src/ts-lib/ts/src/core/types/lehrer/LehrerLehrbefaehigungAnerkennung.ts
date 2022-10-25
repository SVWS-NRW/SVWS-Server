import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { LehrerKatalogLehrbefaehigungAnerkennungEintrag, cast_de_nrw_schule_svws_core_data_lehrer_LehrerKatalogLehrbefaehigungAnerkennungEintrag } from '../../../core/data/lehrer/LehrerKatalogLehrbefaehigungAnerkennungEintrag';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class LehrerLehrbefaehigungAnerkennung extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : String;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<LehrerLehrbefaehigungAnerkennung> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<String, LehrerLehrbefaehigungAnerkennung> = new Map<String, LehrerLehrbefaehigungAnerkennung>();

	public static readonly ID_1 : LehrerLehrbefaehigungAnerkennung = new LehrerLehrbefaehigungAnerkennung("ID_1", 0, [new LehrerKatalogLehrbefaehigungAnerkennungEintrag(1, "1", "erworben durch LABG/OVP bzw. Laufbahnverordnung", null, null)]);

	public static readonly ID_2 : LehrerLehrbefaehigungAnerkennung = new LehrerLehrbefaehigungAnerkennung("ID_2", 1, [new LehrerKatalogLehrbefaehigungAnerkennungEintrag(2, "2", "Unterrichtserlaubnis (z. B. Zertifikatskurs)", null, null)]);

	public static readonly ID_3 : LehrerLehrbefaehigungAnerkennung = new LehrerLehrbefaehigungAnerkennung("ID_3", 2, [new LehrerKatalogLehrbefaehigungAnerkennungEintrag(3, "3", "mehrjähriger Unterricht ohne Lehramtsprüfung oder Unterrichtserlaubnis", null, null)]);

	public static readonly ID_9 : LehrerLehrbefaehigungAnerkennung = new LehrerLehrbefaehigungAnerkennung("ID_9", 3, [new LehrerKatalogLehrbefaehigungAnerkennungEintrag(4, "9", "sonstige", null, null)]);

	public static VERSION : number = 1;

	public readonly daten : LehrerKatalogLehrbefaehigungAnerkennungEintrag;

	public readonly historie : Array<LehrerKatalogLehrbefaehigungAnerkennungEintrag>;

	private static readonly _anerkennungenByID : HashMap<Number, LehrerLehrbefaehigungAnerkennung | null> = new HashMap();

	private static readonly _anerkennungenByKuerzel : HashMap<String, LehrerLehrbefaehigungAnerkennung | null> = new HashMap();

	/**
	 * Erzeugt einen neuen Anerkennungsgrund für Lehrbefähigungen in der Aufzählung.
	 * 
	 * @param historie   die Historie der Lehrbefähigung, welches ein Array von {@link LehrerKatalogLehrbefaehigungAnerkennungEintrag} ist  
	 */
	private constructor(name : string, ordinal : number, historie : Array<LehrerKatalogLehrbefaehigungAnerkennungEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		LehrerLehrbefaehigungAnerkennung.all_values_by_ordinal.push(this);
		LehrerLehrbefaehigungAnerkennung.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den IDs der Lehrbefaehigungssanerkennungen auf die zugehörigen Lehrbefaehigungssanerkennungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den IDs der Lehrbefaehigungssanerkennungen auf die zugehörigen Lehrbefaehigungssanerkennungen
	 */
	private static getMapAnerkennungenByID() : HashMap<Number, LehrerLehrbefaehigungAnerkennung | null> {
		if (LehrerLehrbefaehigungAnerkennung._anerkennungenByID.size() === 0) 
			for (let l of LehrerLehrbefaehigungAnerkennung.values()) 
				LehrerLehrbefaehigungAnerkennung._anerkennungenByID.put(l.daten.id, l);
		return LehrerLehrbefaehigungAnerkennung._anerkennungenByID;
	}

	/**
	 * Gibt eine Map von den Kürzeln der Lehrbefaehigungssanerkennungen auf die zugehörigen Lehrbefaehigungssanerkennungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den Kürzeln der Lehrbefaehigungssanerkennungen auf die zugehörigen Lehrbefaehigungssanerkennungen
	 */
	private static getMapAnerkennungenByKuerzel() : HashMap<String, LehrerLehrbefaehigungAnerkennung | null> {
		if (LehrerLehrbefaehigungAnerkennung._anerkennungenByKuerzel.size() === 0) 
			for (let l of LehrerLehrbefaehigungAnerkennung.values()) 
				LehrerLehrbefaehigungAnerkennung._anerkennungenByKuerzel.put(l.daten.kuerzel, l);
		return LehrerLehrbefaehigungAnerkennung._anerkennungenByKuerzel;
	}

	/**
	 * Gibt den Anerkennungsgrund für Lehrbefähigungen anhand der angegebenen ID zurück.
	 * 
	 * @param id   die ID des Anerkennungsgrunded für Lehrbefähigungen
	 * 
	 * @return der Anerkennungsgrund für Lehrbefähigungen oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number) : LehrerLehrbefaehigungAnerkennung | null {
		return LehrerLehrbefaehigungAnerkennung.getMapAnerkennungenByID().get(id);
	}

	/**
	 * Gibt den Anerkennungsgrund für Lehrbefähigungen anhand des angegebenen Kürzels zurück.
	 * 
	 * @param kuerzel   das Kürzel des Anerkennungsgrunded für Lehrbefähigungen
	 * 
	 * @return der Anerkennungsgrund für Lehrbefähigungen oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : String | null) : LehrerLehrbefaehigungAnerkennung | null {
		return LehrerLehrbefaehigungAnerkennung.getMapAnerkennungenByKuerzel().get(kuerzel);
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
		if (!(other instanceof LehrerLehrbefaehigungAnerkennung))
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
	public compareTo(other : LehrerLehrbefaehigungAnerkennung) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LehrerLehrbefaehigungAnerkennung> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : String) : LehrerLehrbefaehigungAnerkennung | null {
		let tmp : LehrerLehrbefaehigungAnerkennung | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.lehrer.LehrerLehrbefaehigungAnerkennung'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_lehrer_LehrerLehrbefaehigungAnerkennung(obj : unknown) : LehrerLehrbefaehigungAnerkennung {
	return obj as LehrerLehrbefaehigungAnerkennung;
}
