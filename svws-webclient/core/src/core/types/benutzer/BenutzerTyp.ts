import { JavaEnum } from '../../../java/lang/JavaEnum';

export class BenutzerTyp extends JavaEnum<BenutzerTyp> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<BenutzerTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, BenutzerTyp> = new Map<string, BenutzerTyp>();

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
		super(name, ordinal);
		BenutzerTyp.all_values_by_ordinal.push(this);
		BenutzerTyp.all_values_by_name.set(name, this);
		this.id = id;
		this.bezeichnung = bezeichnung;
	}

	/**
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
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.benutzer.BenutzerTyp', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_benutzer_BenutzerTyp(obj : unknown) : BenutzerTyp {
	return obj as BenutzerTyp;
}
