import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { LehrerKatalogLeitungsfunktionenEintrag, cast_de_nrw_schule_svws_core_data_lehrer_LehrerKatalogLeitungsfunktionenEintrag } from '../../../core/data/lehrer/LehrerKatalogLeitungsfunktionenEintrag';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class LehrerLeitungsfunktion extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : String;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<LehrerLeitungsfunktion> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<String, LehrerLeitungsfunktion> = new Map<String, LehrerLeitungsfunktion>();

	/**
	 * Schulleitung 
	 */
	public static readonly SL : LehrerLeitungsfunktion = new LehrerLeitungsfunktion("SL", 0, [new LehrerKatalogLeitungsfunktionenEintrag(1, "SL", "Schulleitung", null, null)]);

	/**
	 * Stellvertretende Schulleitung 
	 */
	public static readonly SL_STV : LehrerLeitungsfunktion = new LehrerLeitungsfunktion("SL_STV", 1, [new LehrerKatalogLeitungsfunktionenEintrag(2, "Stv. SL", "Stellvertretende Schulleitung", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. 
	 */
	public static VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Leitungsfunktionen von Lehrern 
	 */
	public readonly daten : LehrerKatalogLeitungsfunktionenEintrag;

	/**
	 * Die Historie mit den Einträgen der Leitungsfunktionen von Lehrern 
	 */
	public readonly historie : Array<LehrerKatalogLeitungsfunktionenEintrag>;

	/**
	 * Eine Hashmap mit allen definierten Leitungsfunktion, zugeordnet zu ihren Kürzeln 
	 */
	private static readonly _mapByKuerzel : HashMap<String, LehrerLeitungsfunktion | null> = new HashMap();

	/**
	 * Eine Hashmap mit allen definierten Leitungsfunktion, zugeordnet zu ihren IDs 
	 */
	private static readonly _mapByID : HashMap<Number, LehrerLeitungsfunktion | null> = new HashMap();

	/**
	 * Erzeugt eine neue Leitungsfunktion in der Aufzählung.
	 * 
	 * @param historie   die Historie der Leitungsfunktion, welches ein Array von {@link LehrerKatalogLeitungsfunktionenEintrag} ist  
	 */
	private constructor(name : string, ordinal : number, historie : Array<LehrerKatalogLeitungsfunktionenEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		LehrerLeitungsfunktion.all_values_by_ordinal.push(this);
		LehrerLeitungsfunktion.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzeln der Leitungsfunktionen auf die zugehörigen Leitungsfunktionen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln der Leitungsfunktionen auf die zugehörigen Leitungsfunktionen
	 */
	private static getMapByKuerzel() : HashMap<String, LehrerLeitungsfunktion | null> {
		if (LehrerLeitungsfunktion._mapByKuerzel.size() === 0) {
			for (let s of LehrerLeitungsfunktion.values()) {
				if (s.daten !== null) 
					LehrerLeitungsfunktion._mapByKuerzel.put(s.daten.kuerzel, s);
			}
		}
		return LehrerLeitungsfunktion._mapByKuerzel;
	}

	/**
	 * Gibt eine Map von den IDs der Leitungsfunktionen auf die zugehörigen Leitungsfunktionen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den IDs der Leitungsfunktionen auf die zugehörigen Leitungsfunktionen
	 */
	private static getMapByID() : HashMap<Number, LehrerLeitungsfunktion | null> {
		if (LehrerLeitungsfunktion._mapByID.size() === 0) {
			for (let s of LehrerLeitungsfunktion.values()) {
				if (s.daten !== null) 
					LehrerLeitungsfunktion._mapByID.put(s.daten.id, s);
			}
		}
		return LehrerLeitungsfunktion._mapByID;
	}

	/**
	 * Gibt die Leitungsfunktion für das angegebene Kürzel zurück.
	 * 
	 * @param kuerzel   das Kürzel der Leitungsfunktion
	 * 
	 * @return die Leitungsfunktion oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : String | null) : LehrerLeitungsfunktion | null {
		return LehrerLeitungsfunktion.getMapByKuerzel().get(kuerzel);
	}

	/**
	 * Gibt die Leitungsfunktion für die angegebene ID zurück.
	 * 
	 * @param id   die ID der Leitungsfunktion
	 * 
	 * @return die Leitungsfunktion oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number) : LehrerLeitungsfunktion | null {
		return LehrerLeitungsfunktion.getMapByID().get(id);
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
		if (!(other instanceof LehrerLeitungsfunktion))
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
	public compareTo(other : LehrerLeitungsfunktion) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LehrerLeitungsfunktion> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : String) : LehrerLeitungsfunktion | null {
		let tmp : LehrerLeitungsfunktion | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.lehrer.LehrerLeitungsfunktion'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_lehrer_LehrerLeitungsfunktion(obj : unknown) : LehrerLeitungsfunktion {
	return obj as LehrerLeitungsfunktion;
}
