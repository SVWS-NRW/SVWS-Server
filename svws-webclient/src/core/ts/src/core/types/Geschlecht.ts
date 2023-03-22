import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../java/lang/JavaString';

export class Geschlecht extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<Geschlecht> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, Geschlecht> = new Map<string, Geschlecht>();

	/**
	 * Männlich mit Statistikcode 3
	 */
	public static readonly M : Geschlecht = new Geschlecht("M", 0, 3, "m", "männlich", "männlich");

	/**
	 * weiblich mit Statistikcode 4
	 */
	public static readonly W : Geschlecht = new Geschlecht("W", 1, 4, "w", "weiblich", "weiblich");

	/**
	 * divers mit Statistikcode 5
	 */
	public static readonly D : Geschlecht = new Geschlecht("D", 2, 5, "d", "divers", "divers");

	/**
	 * ohne Angabe mit Statistikcode 6
	 */
	public static readonly X : Geschlecht = new Geschlecht("X", 3, 6, "x", "ohne Angabe", "ohne Angabe im Geburtenregister");

	/**
	 * Die ID des Geschlechtes, welche im Rahmen der amtlichen Schulstatistik verwendet wird.
	 */
	public readonly id : number;

	/**
	 * Das Geschlecht als einstelliges Kürzel
	 */
	public readonly kuerzel : string;

	/**
	 * Die Kurz-Bezeichnung des Geschlechtes
	 */
	public readonly text : string;

	/**
	 * Die ausführliche Bezeichnung des Geschlechtes
	 */
	public readonly textLang : string;

	/**
	 * Erzeugt ein neues Geschlecht für die Aufzählung der Geschlechter.
	 *
	 * @param id          die ID des Geschlechtes, welche im Rahmen der amtlichen Schulstatistik verwendet wird
	 * @param kuerzel     das Geschlecht als einstelliges Kürzel
	 * @param text        die textuelle Kurz-Bezeichnung des Geschlechtes
	 * @param textLang    die ausführliche textuelle Bezeichnung des Geschlechtes
	 */
	private constructor(name : string, ordinal : number, id : number, kuerzel : string, text : string, textLang : string) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		Geschlecht.all_values_by_ordinal.push(this);
		Geschlecht.all_values_by_name.set(name, this);
		this.id = id;
		this.kuerzel = kuerzel;
		this.text = text;
		this.textLang = textLang;
	}

	/**
	 *
	 * Bestimmt das Geschlecht anhand der ID.
	 *
	 * @param value   die ID des Geschlechtes
	 *
	 * @return das Geschlecht oder null, falls die ID fehlerhaft ist
	 */
	public static fromValue(value : number | null) : Geschlecht | null {
		if (value === null)
			return null;
		switch (value) {
			case 3: 
				return Geschlecht.M;
			case 4: 
				return Geschlecht.W;
			case 5: 
				return Geschlecht.D;
			case 6: 
				return Geschlecht.X;
		}
		return null;
	}

	/**
	 *
	 * Bestimmt das Geschlecht anhand des übergebenen Strings.
	 * Enthält der übegebene String einen ungültigen Wert,
	 * so wird als Geschlecht "x", d.h. ohne Angabe im Geburtenregister
	 * zurückgegebeben.
	 *
	 * @param text         die textuelle Beschreibung des Geschlechts
	 *
	 * @return das Geschlecht als Type
	 */
	public static fromStringValue(text : string | null) : Geschlecht {
		if ((text === null) || JavaObject.equalsTranspiler("", (text)))
			return Geschlecht.X;
		let upperValue : string | null = text.toUpperCase();
		switch (upperValue) {
			case "MÄNNLICH":
			case "MAENNLICH":
			case "M": 
				return Geschlecht.M;
			case "WEIBLICH":
			case "W": 
				return Geschlecht.W;
			case "DIVERS":
			case "D": 
				return Geschlecht.D;
			case "-":
			case "X":
			case "OHNE ANGABE":
			case "OHNE_ANGABE":
			case "OHNE ANGABE IM GEBURTENREGISTER": 
				return Geschlecht.X;
		}
		return Geschlecht.X;
	}

	/**
	 * Gibt die Anrede für eine Person dieses Geschlechts in Abhängigkeit vom Alter zurück.
	 *
	 * @param alter   das Alter der Person
	 *
	 * @return die Anrede
	 */
	public getAnrede(alter : number) : string | null {
		switch (this.id) {
			case 3: 
				return (alter < 18) ? "Lieber" : "Sehr geehrter Herr";
			case 4: 
				return (alter < 18) ? "Liebe" : "Sehr geehrte Frau";
			case 5: 
				return "Guten Tag";
			case 6: 
				return "Guten Tag";
		}
		return "Guten Tag";
	}

	public toString() : string {
		return this.kuerzel;
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
	 * Returns true if this and the other enumeration values are equal.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns true if they are equal and false otherwise
	 */
	public equals(other : JavaObject) : boolean {
		if (!(other instanceof Geschlecht))
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
	public compareTo(other : Geschlecht) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Geschlecht> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Geschlecht | null {
		let tmp : Geschlecht | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.Geschlecht'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_Geschlecht(obj : unknown) : Geschlecht {
	return obj as Geschlecht;
}
