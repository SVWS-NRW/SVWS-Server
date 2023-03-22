import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { LehrerKatalogZugangsgrundEintrag, cast_de_nrw_schule_svws_core_data_lehrer_LehrerKatalogZugangsgrundEintrag } from '../../../core/data/lehrer/LehrerKatalogZugangsgrundEintrag';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class LehrerZugangsgrund extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<LehrerZugangsgrund> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, LehrerZugangsgrund> = new Map<string, LehrerZugangsgrund>();

	/**
	 * Grund 'Neueintritt in den Schuldienst mit abgelegter 2. Staatsprüfung oder anderweitig erfüllter Eingangsvoraussetzung' für das Kommen des Lehrers an die Schule
	 */
	public static readonly NEU : LehrerZugangsgrund = new LehrerZugangsgrund("NEU", 0, [new LehrerKatalogZugangsgrundEintrag(1, "NEU", "Neueintritt in den Schuldienst mit abgelegter 2. Staatsprüfung oder anderweitig erfüllter Eingangsvoraussetzung", "1", null, null)]);

	/**
	 * Grund 'Übertritt aus dem Schuldienst eines anderen Bundeslandes' für das Kommen des Lehrers an die Schule
	 */
	public static readonly AndBuLand : LehrerZugangsgrund = new LehrerZugangsgrund("AndBuLand", 1, [new LehrerKatalogZugangsgrundEintrag(2, "AndBuLand", "Übertritt aus dem Schuldienst eines anderen Bundeslandes", "2", null, null)]);

	/**
	 * Grund 'Wechsel innerhalb des Landes von einer anderen Schule an die berichtende Schule' für das Kommen des Lehrers an die Schule
	 */
	public static readonly WECHSEL : LehrerZugangsgrund = new LehrerZugangsgrund("WECHSEL", 2, [new LehrerKatalogZugangsgrundEintrag(3, "WECHSEL", "Wechsel innerhalb des Landes von einer anderen Schule an die berichtende Schule", "3", null, null)]);

	/**
	 * Grund 'Wiedereintritt in den Schuldienst' für das Kommen des Lehrers an die Schule
	 */
	public static readonly WIEDER : LehrerZugangsgrund = new LehrerZugangsgrund("WIEDER", 3, [new LehrerKatalogZugangsgrundEintrag(4, "WIEDER", "Wiedereintritt in den Schuldienst", "4", null, null)]);

	/**
	 * Grund 'Sonstige Zugänge' für das Kommen des Lehrers an die Schule
	 */
	public static readonly SONSTIG : LehrerZugangsgrund = new LehrerZugangsgrund("SONSTIG", 4, [new LehrerKatalogZugangsgrundEintrag(5, "SONSTIG", "Sonstige Zugänge", "5", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static VERSION : number = 1;

	/**
	 * Der aktuellen Daten des Zugangsgrundes, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : LehrerKatalogZugangsgrundEintrag;

	/**
	 * Die Historie mit den Einträgen des Zugangsgrundes
	 */
	public readonly historie : Array<LehrerKatalogZugangsgrundEintrag>;

	/**
	 * Erzeugt einen neuen Grund in der Aufzählung.
	 * 
	 * @param historie   die Historie des Zugangsgrundes, welches ein Array von {@link LehrerKatalogZugangsgrundEintrag} ist  
	 */
	private constructor(name : string, ordinal : number, historie : Array<LehrerKatalogZugangsgrundEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		LehrerZugangsgrund.all_values_by_ordinal.push(this);
		LehrerZugangsgrund.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt den Grund anhand der angegebenen ID zurück.
	 * 
	 * @param id   die ID des Grundes
	 * 
	 * @return der Grund für das Kommen des Lehrers an die Schule oder null, falls die ID ungültig ist
	 */
	public static getByID(id : number) : LehrerZugangsgrund | null {
		for (let grund of LehrerZugangsgrund.values()) 
			if (grund.daten.id === id) 
				return grund;
		return null;
	}

	/**
	 * Gibt den Grund anhand des angegebenen Kürzels zurück.
	 * 
	 * @param kuerzel   das Kürzel des Grundes
	 * 
	 * @return der Grund für das Kommen des Lehrers an die Schule oder null, falls das Kürzel ungültig ist
	 */
	public static getByKuerzel(kuerzel : string | null) : LehrerZugangsgrund | null {
		for (let grund of LehrerZugangsgrund.values()) 
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
	 * @return der Grund für das Kommen des Lehrers an die Schule oder null, falls der Schlüssel ungültig ist
	 */
	public static getByASDSchluessel(schluessel : string | null) : LehrerZugangsgrund | null {
		for (let grund of LehrerZugangsgrund.values()) 
			if (JavaObject.equalsTranspiler(grund.daten.schluessel, (schluessel))) 
				return grund;
		return null;
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
		if (!(other instanceof LehrerZugangsgrund))
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
	public compareTo(other : LehrerZugangsgrund) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LehrerZugangsgrund> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : LehrerZugangsgrund | null {
		let tmp : LehrerZugangsgrund | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.lehrer.LehrerZugangsgrund'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_lehrer_LehrerZugangsgrund(obj : unknown) : LehrerZugangsgrund {
	return obj as LehrerZugangsgrund;
}
