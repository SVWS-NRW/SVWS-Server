import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { LehrerKatalogAbgangsgrundEintrag, cast_de_nrw_schule_svws_core_data_lehrer_LehrerKatalogAbgangsgrundEintrag } from '../../../core/data/lehrer/LehrerKatalogAbgangsgrundEintrag';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class LehrerAbgangsgrund extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : String;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<LehrerAbgangsgrund> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<String, LehrerAbgangsgrund> = new Map<String, LehrerAbgangsgrund>();

	public static readonly RUHEST : LehrerAbgangsgrund = new LehrerAbgangsgrund("RUHEST", 0, [new LehrerKatalogAbgangsgrundEintrag(1, "RUHEST", "Eintritt in den Ruhestand", "11", null, null)]);

	public static readonly UNFAEHIGK : LehrerAbgangsgrund = new LehrerAbgangsgrund("UNFAEHIGK", 1, [new LehrerKatalogAbgangsgrundEintrag(2, "UNFÄHIGK", "Dienst-, Erwerbs-, Berufsunfähigkeit", "12", null, null)]);

	public static readonly TOD : LehrerAbgangsgrund = new LehrerAbgangsgrund("TOD", 2, [new LehrerKatalogAbgangsgrundEintrag(3, "TOD", "Tod", "13", null, null)]);

	public static readonly AndBuLand : LehrerAbgangsgrund = new LehrerAbgangsgrund("AndBuLand", 3, [new LehrerKatalogAbgangsgrundEintrag(4, "AndBuLand", "Übertritt in den Schuldienst eines anderen Bundeslandes", "14", null, null)]);

	public static readonly WECHSEL : LehrerAbgangsgrund = new LehrerAbgangsgrund("WECHSEL", 4, [new LehrerKatalogAbgangsgrundEintrag(5, "WECHSEL", "Wechsel innerhalb des Landes von der berichtenden Schule an eine andere Schule", "15", null, null)]);

	public static readonly BEFRIST : LehrerAbgangsgrund = new LehrerAbgangsgrund("BEFRIST", 5, [new LehrerKatalogAbgangsgrundEintrag(6, "BEFRIST", "Befristete Abgänge", "16", null, null)]);

	public static readonly SONSTIG : LehrerAbgangsgrund = new LehrerAbgangsgrund("SONSTIG", 6, [new LehrerKatalogAbgangsgrundEintrag(7, "SONSTIG", "Sonstige Abgänge", "17", null, null)]);

	public static VERSION : number = 1;

	public readonly daten : LehrerKatalogAbgangsgrundEintrag;

	public readonly historie : Array<LehrerKatalogAbgangsgrundEintrag>;

	/**
	 * Erzeugt einen neuen Grund in der Aufzählung.
	 * 
	 * @param historie   die Historie des Abgangsgrundes, welches ein Array von {@link LehrerKatalogAbgangsgrundEintrag} ist  
	 */
	private constructor(name : string, ordinal : number, historie : Array<LehrerKatalogAbgangsgrundEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		LehrerAbgangsgrund.all_values_by_ordinal.push(this);
		LehrerAbgangsgrund.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt den Grund anhand der angegebenen ID zurück.
	 * 
	 * @param id   die ID des Grundes
	 * 
	 * @return der Grund für das Verlassen der Schule durch den Lehrer oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number) : LehrerAbgangsgrund | null {
		for (let grund of LehrerAbgangsgrund.values()) 
			if (grund.daten.id === id) 
				return grund;
		return null;
	}

	/**
	 * Gibt den Grund anhand des angegebenen Kürzels zurück.
	 * 
	 * @param kuerzel   das Kürzel des Grundes
	 * 
	 * @return der Grund für das Verlassen der Schule durch den Lehrer oder null, falls das kuerzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : String | null) : LehrerAbgangsgrund | null {
		for (let grund of LehrerAbgangsgrund.values()) 
			if (JavaObject.equalsTranspiler(grund.daten.kuerzel, (kuerzel))) 
				return grund;
		return null;
	}

	/**
	 * Gibt den Grund anhand des angegebenen Schlüssels der
	 * amtlichen Schulstatistik zurück.
	 * 
	 * @param schluessel   der Schlüssel der amtlichen Schulstatistik 
	 *                     für den Grundes
	 * 
	 * @return der Grund für das Verlassen der Schule durch den Lehrer oder null, falls der Schlüssel ungültig ist
	 */
	public static getByASDSchluessel(schluessel : String | null) : LehrerAbgangsgrund | null {
		for (let grund of LehrerAbgangsgrund.values()) 
			if (JavaObject.equalsTranspiler(grund.daten.schluessel, (schluessel))) 
				return grund;
		return null;
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
		if (!(other instanceof LehrerAbgangsgrund))
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
	public compareTo(other : LehrerAbgangsgrund) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LehrerAbgangsgrund> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : String) : LehrerAbgangsgrund | null {
		let tmp : LehrerAbgangsgrund | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.statkue.LehrerAbgangsgrund'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_statkue_LehrerAbgangsgrund(obj : unknown) : LehrerAbgangsgrund {
	return obj as LehrerAbgangsgrund;
}
