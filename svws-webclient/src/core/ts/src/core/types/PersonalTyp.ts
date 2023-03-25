import { JavaObject } from '../../java/lang/JavaObject';
import { HashMap } from '../../java/util/HashMap';

export class PersonalTyp extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<PersonalTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, PersonalTyp> = new Map<string, PersonalTyp>();

	/**
	 * Lehrkraft fest der Schule zugeodnet hat eine Identnummer
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
	public static VERSION : number = 1;

	/**
	 * Eine HashMap für den schnellen Zugriff auf Personal-Typen anhand der Bezeichnung des PersonalTyps
	 */
	private static readonly _mapBezeichnungen : HashMap<string, PersonalTyp> = new HashMap();

	/**
	 * Eine HashMap für den schnellen Zugriff auf Personal-Typen anhand der ID des PersonalTyps
	 */
	private static readonly _mapID : HashMap<number, PersonalTyp> = new HashMap();

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
	 * Gibt an, in welchem Schuljahr der Personaltyp einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
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
	 * @param gueltigVon   gibt an, in welchem Schuljahr der Personaltyp einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 * @param gueltigBis   gibt an, bis zu welchem Schuljahr der Personaltyp gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	private constructor(name : string, ordinal : number, id : number, kuerzel : string, bezeichnung : string, gueltigVon : number | null, gueltigBis : number | null) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
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
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
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
	 * Gibt eine Map von den Bezeichnungen der Personal-Typen auf die zugehörigen Personal-Typen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *
	 * @return die Map von den Bezeichnungen der Personal-Typen auf die zugehörigen Personal-Typen
	 */
	private static getMapBezeichnungen() : HashMap<string, PersonalTyp> {
		if (PersonalTyp._mapBezeichnungen.size() === 0)
			for (const p of PersonalTyp.values())
				PersonalTyp._mapBezeichnungen.put(p.kuerzel, p);
		return PersonalTyp._mapBezeichnungen;
	}

	/**
	 *
	 * Gibt den PersonalTyp anhand der Bezeichnung zurück.
	 *
	 * @param bezeichnung   die Bezeichnung des Personal-Typs
	 *
	 * @return der Personal-Typ oder null, falls die Bezeichnung ungültig ist
	 *
	 */
	public static fromBezeichnung(bezeichnung : string | null) : PersonalTyp | null {
		return PersonalTyp.getMapBezeichnungen().get(bezeichnung);
	}

	/**
	 *
	 * Gibt den PersonalTyp anhand der ID zurück.
	 *
	 * @param id   die ID des Personal-Typs
	 *
	 * @return der Personal-Typ oder null, falls die ID ungültig ist
	 *
	 */
	public static fromID(id : number | null) : PersonalTyp | null {
		return PersonalTyp.getMapID().get(id);
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
		if (!(other instanceof PersonalTyp))
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
	public compareTo(other : PersonalTyp) : number {
		return this.__ordinal - other.__ordinal;
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
		const tmp : PersonalTyp | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.PersonalTyp'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_PersonalTyp(obj : unknown) : PersonalTyp {
	return obj as PersonalTyp;
}
