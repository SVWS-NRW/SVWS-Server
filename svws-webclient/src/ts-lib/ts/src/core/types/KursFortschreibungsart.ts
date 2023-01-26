import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../java/lang/JavaString';

export class KursFortschreibungsart extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<KursFortschreibungsart> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, KursFortschreibungsart> = new Map<string, KursFortschreibungsart>();

	/**
	 * Keine Fortschreibung Kurs wird bei der Verstzung gelöscht. 
	 */
	public static readonly KEINE : KursFortschreibungsart = new KursFortschreibungsart("KEINE", 0, 0, "N", "Keine", null, null);

	/**
	 * Nur Definition, alle Schüler werden aus dem Kurs gelöscht, der Jahrgang wird aber erhöht. 
	 */
	public static readonly NUR_DEFINITION_JAHRGANG_HOCHSCHREIBEN : KursFortschreibungsart = new KursFortschreibungsart("NUR_DEFINITION_JAHRGANG_HOCHSCHREIBEN", 1, 1, "D", "Nur Definition, Jahrgang hochschreiben", null, null);

	/**
	 * Nur Definition, alle Schüler werden aus dem Kurs gelöscht, der Jahrgang wird nicht erhöht. 
	 */
	public static readonly NUR_DEFINITION_JAHRGANG_BEIBEHALTEN : KursFortschreibungsart = new KursFortschreibungsart("NUR_DEFINITION_JAHRGANG_BEIBEHALTEN", 2, 2, "B", "Nur Definition, Jahrgang beibehalten", null, null);

	/**
	 * Komplett, der Kurs wird mit Schülern hochgeschrieben und der Jahrgang erhöht. 
	 */
	public static readonly KOMPLETT : KursFortschreibungsart = new KursFortschreibungsart("KOMPLETT", 3, 3, "K", "Komplett", null, null);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. 
	 */
	public static VERSION : number = 1;

	/**
	 * Die ID der Kurs-Fortschreibungsart als Integer 
	 */
	public readonly id : number;

	/**
	 * Das eindeutige einstelleige Kürzel der Kurs-Fortschreibungsart. 
	 */
	public readonly kuerzel : string;

	/**
	 * Die Beschreibung der Kurs-Fortschreibungsart 
	 */
	public readonly beschreibung : string;

	/**
	 * Gibt an, in welchem Schuljahr die Fortschreibungsart einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. 
	 */
	public readonly gueltigVon : number | null;

	/**
	 * Gibt an, bis zu welchem Schuljahr die Fortschreibungsart gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. 
	 */
	public readonly gueltigBis : number | null;

	/**
	 * Erstellt eine neue Kurs-Fortschreibungsart in der Aufzählung.
	 * 
	 * @param id             die ID der Fortchreibungsart
	 * @param kuerzel        das eindeutige einstelleige Kürzel der Kurs-Fortschreibungsart
	 * @param beschreibung   die Beschreibung der Kurs-Fortschreibungsart
	 * @param gueltigVon     gibt an, in welchem Schuljahr die Fortschreibungsart einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 * @param gueltigBis     gibt an, bis zu welchem Schuljahr die Fortschreibungsart gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	private constructor(name : string, ordinal : number, id : number, kuerzel : string, beschreibung : string, gueltigVon : number | null, gueltigBis : number | null) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		KursFortschreibungsart.all_values_by_ordinal.push(this);
		KursFortschreibungsart.all_values_by_name.set(name, this);
		this.id = id;
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

	/**
	 * Gibt die Kurs-Fortschreibungsart anhand der ID zurück.
	 * Eine ungültige ID wird als Fortschreibungsart KEINE interpretiert. 
	 * 
	 * @param id    die ID
	 * 
	 * @return die Kurs-Fortschreibungsart 
	 */
	public static fromID(id : number | null) : KursFortschreibungsart | null {
		if (id === null) 
			return KursFortschreibungsart.KEINE;
		switch (id) {
			case 0: 
				return KursFortschreibungsart.KEINE;
			case 1: 
				return KursFortschreibungsart.NUR_DEFINITION_JAHRGANG_HOCHSCHREIBEN;
			case 2: 
				return KursFortschreibungsart.NUR_DEFINITION_JAHRGANG_BEIBEHALTEN;
			case 3: 
				return KursFortschreibungsart.KOMPLETT;
		}
		return KursFortschreibungsart.KEINE;
	}

	/**
	 * Gibt die Kurs-Fortschreibungsart anhand des Kürzels zurück. 
	 * Eine ungültiges Kürzel wird als Fortschreibungsart KEINE interpretiert. 
	 * 
	 * @param kuerzel    das Kürzel
	 * 
	 * @return die Kurs-Fortschreibungsart 
	 */
	public static fromKuerzel(kuerzel : string | null) : KursFortschreibungsart | null {
		if (kuerzel === null) 
			return KursFortschreibungsart.KEINE;
		switch (kuerzel) {
			case "N": 
				return KursFortschreibungsart.KEINE;
			case "D": 
				return KursFortschreibungsart.NUR_DEFINITION_JAHRGANG_HOCHSCHREIBEN;
			case "B": 
				return KursFortschreibungsart.NUR_DEFINITION_JAHRGANG_BEIBEHALTEN;
			case "K": 
				return KursFortschreibungsart.KOMPLETT;
		}
		return KursFortschreibungsart.KEINE;
	}

	/**
	 * Prüft, ob das übergebene Kürzel für eine gültige Kurs-Fortschreibungsart
	 * steht oder nicht
	 * 
	 * @param kuerzel   das zu prüfende Kürzel
	 * 
	 * @return true, falls das kürzel gültig ist.
	 */
	public static isValidKuerzel(kuerzel : string | null) : boolean {
		for (let art of KursFortschreibungsart.values()) 
			if (JavaObject.equalsTranspiler(art.kuerzel, (kuerzel))) 
				return true;
		return false;
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
		if (!(other instanceof KursFortschreibungsart))
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
	public compareTo(other : KursFortschreibungsart) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<KursFortschreibungsart> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : KursFortschreibungsart | null {
		let tmp : KursFortschreibungsart | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.KursFortschreibungsart'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_KursFortschreibungsart(obj : unknown) : KursFortschreibungsart {
	return obj as KursFortschreibungsart;
}
