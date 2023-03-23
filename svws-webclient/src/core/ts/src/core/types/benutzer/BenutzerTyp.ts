import { JavaObject } from '../../../java/lang/JavaObject';

export class BenutzerTyp extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<BenutzerTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, BenutzerTyp> = new Map<string, BenutzerTyp>();

	/**
	 * Ein allgemeiner Benutzertyp
	 */
	public static readonly ALLGEMEIN : BenutzerTyp = new BenutzerTyp("ALLGEMEIN", 0, 0, "Allgemein");

	/**
	 * Ein Benutzertyp für Lehrer und weiteres Personal
	 */
	public static readonly LEHRER : BenutzerTyp = new BenutzerTyp("LEHRER", 1, 1, "Lehrer/Personal");

	/**
	 * Ein Benutzertyp für Schüler
	 */
	public static readonly SCHUELER : BenutzerTyp = new BenutzerTyp("SCHUELER", 2, 2, "Schüler");

	/**
	 * Ein Benutzertyp für Erzieher
	 */
	public static readonly ERZIEHER : BenutzerTyp = new BenutzerTyp("ERZIEHER", 3, 3, "Erzieher");

	/**
	 * Die ID des Benutzertyps
	 */
	public readonly id : number;

	/**
	 * Die textuelle Bezeichnung des Benutzertyps.
	 */
	public readonly bezeichnung : string;

	/**
	 * Erzeugt einen neuen Benutzertyp für die Aufzählung.
	 *
	 * @param id                  die ID des Benutzertyps
	 * @param bezeichnung         die Bezeichnung des Benutzertyps
	 */
	private constructor(name : string, ordinal : number, id : number, bezeichnung : string) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		BenutzerTyp.all_values_by_ordinal.push(this);
		BenutzerTyp.all_values_by_name.set(name, this);
		this.id = id;
		this.bezeichnung = bezeichnung;
	}

	/**
	 *
	 * Gibt den Benutzertyp die Benutzerkompetenz anhand der übergebenen ID zurück.
	 *
	 * @param id    die ID der Benutzerkompetenz
	 *
	 * @return die Benutzerkompetenz oder null, falls die ID fehlerhaft ist
	 */
	public static getByID(id : number) : BenutzerTyp | null {
		switch (id) {
			case 0: {
				return BenutzerTyp.ALLGEMEIN;
			}
			case 1: {
				return BenutzerTyp.LEHRER;
			}
			case 2: {
				return BenutzerTyp.SCHUELER;
			}
			case 3: {
				return BenutzerTyp.ERZIEHER;
			}
		}
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
		if (!(other instanceof BenutzerTyp))
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
	public compareTo(other : BenutzerTyp) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<BenutzerTyp> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : BenutzerTyp | null {
		const tmp : BenutzerTyp | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.benutzer.BenutzerTyp'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_benutzer_BenutzerTyp(obj : unknown) : BenutzerTyp {
	return obj as BenutzerTyp;
}
