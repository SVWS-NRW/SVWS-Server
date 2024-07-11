import { JavaEnum } from '../../../java/lang/JavaEnum';

export class PersonTyp extends JavaEnum<PersonTyp> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<PersonTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, PersonTyp> = new Map<string, PersonTyp>();

	/**
	 * Ein Personentyp für Lehrer und weiteres Personal
	 */
	public static readonly LEHRER : PersonTyp = new PersonTyp("LEHRER", 0, 1, "L", "Lehrer/Personal");

	/**
	 * Ein Personentyp für Schüler
	 */
	public static readonly SCHUELER : PersonTyp = new PersonTyp("SCHUELER", 1, 2, "S", "Schüler");

	/**
	 * Ein Personentyp für Erzieher
	 */
	public static readonly ERZIEHER : PersonTyp = new PersonTyp("ERZIEHER", 2, 3, "E", "Erzieher");

	/**
	 * Die ID des Personentyps
	 */
	public readonly id : number;

	/**
	 * Das Kürzel des Personentyps.
	 */
	public readonly kuerzel : string;

	/**
	 * Die textuelle Bezeichnung des Personentyps.
	 */
	public readonly bezeichnung : string;

	/**
	 * Erzeugt einen neuen Personentyp für die Aufzählung.
	 *
	 * @param id                  die ID des Personentyps
	 * @param kuerzel             das Kürzel des Personentyps
	 * @param bezeichnung         die Bezeichnung des Personentyps
	 */
	private constructor(name : string, ordinal : number, id : number, kuerzel : string, bezeichnung : string) {
		super(name, ordinal);
		PersonTyp.all_values_by_ordinal.push(this);
		PersonTyp.all_values_by_name.set(name, this);
		this.id = id;
		this.kuerzel = kuerzel;
		this.bezeichnung = bezeichnung;
	}

	/**
	 * Gibt den Personentyp anhand der übergebenen ID zurück.
	 *
	 * @param id    die ID des Personentyps
	 *
	 * @return den Personentyp oder null, falls die ID fehlerhaft ist
	 */
	public static getByID(id : number) : PersonTyp | null {
		switch (id) {
			case 1: {
				return PersonTyp.LEHRER;
			}
			case 2: {
				return PersonTyp.SCHUELER;
			}
			case 3: {
				return PersonTyp.ERZIEHER;
			}
			default: {
				return null;
			}
		}
	}

	/**
	 * Gibt den Personentyp anhand des übergebenen Kürzels zurück.
	 *
	 * @param kuerzel    das Kürzel des Personentyps
	 *
	 * @return den Personentyp oder null, falls das Kürzel fehlerhaft ist
	 */
	public static getByKuerzel(kuerzel : string | null) : PersonTyp | null {
		switch (kuerzel) {
			case "L": {
				return PersonTyp.LEHRER;
			}
			case "S": {
				return PersonTyp.SCHUELER;
			}
			case "E": {
				return PersonTyp.ERZIEHER;
			}
			default: {
				return null;
			}
		}
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<PersonTyp> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : PersonTyp | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.schule.PersonTyp';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.schule.PersonTyp', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_schule_PersonTyp(obj : unknown) : PersonTyp {
	return obj as PersonTyp;
}
