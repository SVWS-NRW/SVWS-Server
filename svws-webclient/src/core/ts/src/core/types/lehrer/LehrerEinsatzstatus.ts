import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { LehrerKatalogEinsatzstatusEintrag, cast_de_nrw_schule_svws_core_data_lehrer_LehrerKatalogEinsatzstatusEintrag } from '../../../core/data/lehrer/LehrerKatalogEinsatzstatusEintrag';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class LehrerEinsatzstatus extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<LehrerEinsatzstatus> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, LehrerEinsatzstatus> = new Map<string, LehrerEinsatzstatus>();

	/**
	 * Einsatzstatus: 'Stammschule, ganz oder teilweise auch an anderen Schulen tätig'
	 */
	public static readonly A : LehrerEinsatzstatus = new LehrerEinsatzstatus("A", 0, [new LehrerKatalogEinsatzstatusEintrag(1, "A", "Stammschule, ganz oder teilweise auch an anderen Schulen tätig", null, null)]);

	/**
	 * Einsatzstatus: 'nicht Stammschule, aber auch hier tätig'
	 */
	public static readonly B : LehrerEinsatzstatus = new LehrerEinsatzstatus("B", 1, [new LehrerKatalogEinsatzstatusEintrag(2, "B", "nicht Stammschule, aber auch hier tätig", null, null)]);

	/**
	 * Einsatzstatus: 'Stammschule, nur hier tätig'
	 */
	public static readonly DEFAULT : LehrerEinsatzstatus = new LehrerEinsatzstatus("DEFAULT", 2, [new LehrerKatalogEinsatzstatusEintrag(3, "*", "Stammschule, nur hier tätig", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static VERSION : number = 1;

	/**
	 * Der aktuellen Daten des Einsatzstatus, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : LehrerKatalogEinsatzstatusEintrag;

	/**
	 * Die Historie mit den Einträgen des Einsatzstatus
	 */
	public readonly historie : Array<LehrerKatalogEinsatzstatusEintrag>;

	/**
	 * Eine Hashmap mit allen Einsatzstatus, welche ihrer ID zugeordnet sind.
	 */
	private static readonly _statusByID : HashMap<number, LehrerEinsatzstatus | null> = new HashMap();

	/**
	 * Eine Hashmap mit allen Einsatzstatus, welche dem Kürzel bzw. ASD-Schlüssel zugeordnet sind.
	 */
	private static readonly _statusByKuerzel : HashMap<string, LehrerEinsatzstatus | null> = new HashMap();

	/**
	 * Erzeugt einen neuen Einsatzstatus in der Aufzählung.
	 * 
	 * @param historie   die Historie des Einsatzstatus, welches ein Array von {@link LehrerKatalogEinsatzstatusEintrag} ist  
	 */
	private constructor(name : string, ordinal : number, historie : Array<LehrerKatalogEinsatzstatusEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		LehrerEinsatzstatus.all_values_by_ordinal.push(this);
		LehrerEinsatzstatus.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von der ID des Einsatzstatus auf den zugehörigen Einsatzstatus
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von der ID des Einsatzstatus auf den zugehörigen Einsatzstatus
	 */
	private static getMapStatusByID() : HashMap<number, LehrerEinsatzstatus | null> {
		if (LehrerEinsatzstatus._statusByID.size() === 0)
			for (let g of LehrerEinsatzstatus.values()) 
				LehrerEinsatzstatus._statusByID.put(g.daten.id, g);
		return LehrerEinsatzstatus._statusByID;
	}

	/**
	 * Gibt eine Map von de Kürzel des Einsatzstatus auf den zugehörigen Einsatzstatus
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von de Kürzel des Einsatzstatus auf den zugehörigen Einsatzstatus
	 */
	private static getMapStatusByKuerzel() : HashMap<string, LehrerEinsatzstatus | null> {
		if (LehrerEinsatzstatus._statusByKuerzel.size() === 0)
			for (let g of LehrerEinsatzstatus.values()) 
				LehrerEinsatzstatus._statusByKuerzel.put(g.daten.kuerzel, g);
		return LehrerEinsatzstatus._statusByKuerzel;
	}

	/**
	 * Gibt den Einsatzstatus anhand der angegebenen ID zurück.
	 * 
	 * @param id   die ID des Einsatzstatus
	 * 
	 * @return der Einsatzstatus oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number) : LehrerEinsatzstatus | null {
		return LehrerEinsatzstatus.getMapStatusByID().get(id);
	}

	/**
	 * Gibt den Einsatzstatus anhand des angegebenen Kürzels zurück.
	 * 
	 * @param kuerzel   das Kürzel des Einsatzstatus
	 * 
	 * @return der Einsatzstatus oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : LehrerEinsatzstatus | null {
		return LehrerEinsatzstatus.getMapStatusByKuerzel().get(kuerzel);
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
		if (!(other instanceof LehrerEinsatzstatus))
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
	public compareTo(other : LehrerEinsatzstatus) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LehrerEinsatzstatus> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : LehrerEinsatzstatus | null {
		let tmp : LehrerEinsatzstatus | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.lehrer.LehrerEinsatzstatus'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_lehrer_LehrerEinsatzstatus(obj : unknown) : LehrerEinsatzstatus {
	return obj as LehrerEinsatzstatus;
}
