import { JavaEnum } from '../../../java/lang/JavaEnum';

export class AbiturBelegungsart extends JavaEnum<AbiturBelegungsart> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<AbiturBelegungsart> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, AbiturBelegungsart> = new Map<string, AbiturBelegungsart>();

	/**
	 * AbiturBelegungsart "nicht belegt"
	 */
	public static readonly NICHT_BELEGT : AbiturBelegungsart = new AbiturBelegungsart("NICHT_BELEGT", 0, "-", "nicht belegt");

	/**
	 * AbiturBelegungsart "mündlich"
	 */
	public static readonly MUENDLICH : AbiturBelegungsart = new AbiturBelegungsart("MUENDLICH", 1, "M", "mündlich");

	/**
	 * AbiturBelegungsart "schriftlich"
	 */
	public static readonly SCHRIFTLICH : AbiturBelegungsart = new AbiturBelegungsart("SCHRIFTLICH", 2, "S", "schriftlich");

	/**
	 * Das Kürzel für die Kurs-Belegungsart (-,M oder S)
	 */
	public kuerzel : string;

	/**
	 * Die textuelle Beschreibung der Kurs-Belegungsart (nicht belegt, mündlich oder schriftlich)
	 */
	public beschreibung : string;

	/**
	 * Erzeugt ein neues Abitur-Belegungsart-Objekt
	 *
	 * @param kuerzel        das der Kurs-Belegungsart
	 * @param beschreibung   die textuelle Beschreibung der Kurs-Belegungsart
	 */
	private constructor(name : string, ordinal : number, kuerzel : string, beschreibung : string) {
		super(name, ordinal);
		AbiturBelegungsart.all_values_by_ordinal.push(this);
		AbiturBelegungsart.all_values_by_name.set(name, this);
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
	}

	/**
	 * Gibt die Kurs-Belegungsart anhand des übergebenen Kürzels zurück.
	 *
	 * @param kuerzel    das Kürzel der Kurs-Belegungsart
	 *
	 * @return die Kurs-Belegungsart oder null, falls das kuerzel fehlerhaft ist
	 */
	public static fromKuerzel(kuerzel : string | null) : AbiturBelegungsart | null {
		if (kuerzel === null)
			return AbiturBelegungsart.NICHT_BELEGT;
		switch (kuerzel) {
			case "-": {
				return AbiturBelegungsart.NICHT_BELEGT;
			}
			case "M": {
				return AbiturBelegungsart.MUENDLICH;
			}
			case "S": {
				return AbiturBelegungsart.SCHRIFTLICH;
			}
			default: {
				return null;
			}
		}
	}

	public toString() : string {
		return this.kuerzel;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<AbiturBelegungsart> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : AbiturBelegungsart | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.gost.AbiturBelegungsart', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_gost_AbiturBelegungsart(obj : unknown) : AbiturBelegungsart {
	return obj as AbiturBelegungsart;
}
