import { JavaEnum } from '../../java/lang/JavaEnum';
import { HashMap } from '../../java/util/HashMap';

export class PersonalTyp extends JavaEnum<PersonalTyp> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<PersonalTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, PersonalTyp> = new Map<string, PersonalTyp>();

	/**
	 * Lehrkraft fest der Schule zugeordnet hat eine Identnummer
	 */
	public static readonly LEHRKRAFT : PersonalTyp = new PersonalTyp("LEHRKRAFT", 0, 1, "LEHRKRAFT", "Lehrkraft", null, null);

	/**
	 * Verwaltungskraft ohne Identnummer
	 */
	public static readonly SEKRETARIAT : PersonalTyp = new PersonalTyp("SEKRETARIAT", 1, 2, "SEKRETARIAT", "Sekretär/Sekretärin", null, null);

	/**
	 * angestelltes Personal (z.B. Sozialarbeiter*in ohne Identnummer
	 */
	public static readonly PERSONAL : PersonalTyp = new PersonalTyp("PERSONAL", 2, 3, "PERSONAL", "Angestelltes Personal ohne Identnummer", null, null);

	/**
	 * externe Lehrkräfte mit Identnummer von anderen Schulen abgeordnet
	 */
	public static readonly EXTERN : PersonalTyp = new PersonalTyp("EXTERN", 3, 4, "EXTERN", "Externe Lehrkraft, z.B. abgeordnet oder im Rahmen einer Kooperation", null, null);

	/**
	 * Sonstige Personaltypen
	 */
	public static readonly SONSTIGE : PersonalTyp = new PersonalTyp("SONSTIGE", 4, 5, "SONSTIGE", "Sonstiges Personal", null, null);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static readonly VERSION : number = 1;

	/**
	 * Eine HashMap für den schnellen Zugriff auf Personal-Typen anhand der Bezeichnung des PersonalTyps
	 */
	private static readonly _mapKuerzel : HashMap<string, PersonalTyp> = new HashMap<string, PersonalTyp>();

	/**
	 * Eine HashMap für den schnellen Zugriff auf Personal-Typen anhand der ID des PersonalTyps
	 */
	private static readonly _mapID : HashMap<number, PersonalTyp> = new HashMap<number, PersonalTyp>();

	/**
	 * Die ID des Personal-Typs als Integer
	 */
	public readonly id : number;

	/**
	 * Das Kürzel des Personal-Typs als String
	 */
	public readonly kuerzel : string;

	/**
	 * Die Bezeichnung des Personal-Typs als String
	 */
	public readonly bezeichnung : string;

	/**
	 * Gibt an, in welchem Schuljahr der Personaltyp eingeführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public readonly gueltigVon : number | null;

	/**
	 * Gibt an, bis zu welchem Schuljahr der Personaltyp gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public readonly gueltigBis : number | null;

	/**
	 * Erzeugt einen neuen PersonalTyp für die Aufzählung.
	 *
	 * @param id           die ID des Personal-Typs
	 * @param kuerzel      das Kürzel des Personal-Typs
	 * @param bezeichnung  die Bezeichnung des Personal-Typs
	 * @param gueltigVon   gibt an, in welchem Schuljahr der Personaltyp eingeführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 * @param gueltigBis   gibt an, bis zu welchem Schuljahr der Personaltyp gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	private constructor(name : string, ordinal : number, id : number, kuerzel : string, bezeichnung : string, gueltigVon : number | null, gueltigBis : number | null) {
		super(name, ordinal);
		PersonalTyp.all_values_by_ordinal.push(this);
		PersonalTyp.all_values_by_name.set(name, this);
		this.id = id;
		this.kuerzel = kuerzel;
		this.bezeichnung = bezeichnung;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

	/**
	 * Gibt eine Map von den IDs der Personal-Typen auf die zugehörigen Personal-Typen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der Personal-Typen auf die zugehörigen Personal-Typen
	 */
	private static getMapID() : HashMap<number, PersonalTyp> {
		if (PersonalTyp._mapID.size() === 0)
			for (const p of PersonalTyp.values())
				PersonalTyp._mapID.put(p.id, p);
		return PersonalTyp._mapID;
	}

	/**
	 * Gibt eine Map von den Kürzeln der Personal-Typen auf die zugehörigen Personal-Typen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Personal-Typen auf die zugehörigen Personal-Typen
	 */
	private static getMapKuerzel() : HashMap<string, PersonalTyp> {
		if (PersonalTyp._mapKuerzel.size() === 0)
			for (const p of PersonalTyp.values())
				PersonalTyp._mapKuerzel.put(p.kuerzel, p);
		return PersonalTyp._mapKuerzel;
	}

	/**
	 * Gibt den PersonalTyp anhand der Bezeichnung zurück.
	 *
	 * @param kuerzel   die Bezeichnung des Personal-Typs
	 *
	 * @return der Personal-Typ oder null, falls die Bezeichnung ungültig ist
	 */
	public static fromKuerzel(kuerzel : string | null) : PersonalTyp | null {
		return PersonalTyp.getMapKuerzel().get(kuerzel);
	}

	/**
	 * Gibt den PersonalTyp anhand der ID zurück.
	 *
	 * @param id   die ID des Personal-Typs
	 *
	 * @return der Personal-Typ oder null, falls die ID ungültig ist
	 */
	public static fromID(id : number | null) : PersonalTyp | null {
		return PersonalTyp.getMapID().get(id);
	}

	public toString() : string {
		return this.kuerzel;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<PersonalTyp> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : PersonalTyp | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.PersonalTyp';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.PersonalTyp', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_PersonalTyp(obj : unknown) : PersonalTyp {
	return obj as PersonalTyp;
}
