import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaString } from '../../../java/lang/JavaString';

export class SprachBelegungSekI extends JavaEnum<SprachBelegungSekI> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<SprachBelegungSekI> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, SprachBelegungSekI> = new Map<string, SprachBelegungSekI>();

	/**
	 * Gibt an, dass eine Sprache in der Sekundarstufe I nicht oder weniger als 2 Jahre belegt wurde
	 */
	public static readonly NICHT_BELEGT : SprachBelegungSekI = new SprachBelegungSekI("NICHT_BELEGT", 0, 0);

	/**
	 * Gibt an, dass eine Sprache in der Sekundarstufe I mindestens 2 Jahre - aber nicht 4 oder mehr Jahre - belegt wurde
	 */
	public static readonly MIND_2_JAHRE : SprachBelegungSekI = new SprachBelegungSekI("MIND_2_JAHRE", 1, 2);

	/**
	 * Gibt an, dass eine Sprache in der Sekundarstufe I mindestens 4 Jahre - aber nicht ab Klasse 5 - belegt wurde
	 */
	public static readonly MIND_4_JAHRE : SprachBelegungSekI = new SprachBelegungSekI("MIND_4_JAHRE", 2, 4);

	/**
	 * Gibt an, dass eine Sprache in der Sekundarstufe I ab Klasse 5, d.h. 5 (in G8) oder 6 Jahre belegt wurde.
	 */
	public static readonly AB_JAHRGANG_5 : SprachBelegungSekI = new SprachBelegungSekI("AB_JAHRGANG_5", 3, 6);

	/**
	 * Die Dauer der Sprachbelegung in der SekI - der Wert kann von der realen Belegung abweichen, da nur die relevante Dauer angeben ist und im Falle des Jahrgangs 5 abweichen kann, falls der G8-Bildungsgang vorliegt
	 */
	public readonly dauer : number;

	/**
	 * Erstellt einen neuen enum-Wert mit der angegebenen Dauer der Sprachbelegung.
	 *
	 * @param dauer   die Dauer der Sprachbelegung in der Sek I
	 */
	private constructor(name : string, ordinal : number, dauer : number) {
		super(name, ordinal);
		SprachBelegungSekI.all_values_by_ordinal.push(this);
		SprachBelegungSekI.all_values_by_name.set(name, this);
		this.dauer = dauer;
	}

	/**
	 * Ermittelt die Sprachbelegung in der Sek I anhand des übergebenen Jahrgangs.
	 * WICHTIG: Sollte ein Schüler sich im G8-Bildungsgang bewegen, so wird die Dauer
	 * mit 6 Jahren hier nicht korrekt zugeordnet.
	 *
	 * @param kuerzel   der Statistik-Jahrgang in welchem mit der Sprache begonnen wurde
	 *
	 * @return die Sprachbelegung in der Sek I
	 */
	public static getByASDJahrgang(kuerzel : string | null) : SprachBelegungSekI {
		if (kuerzel === null)
			return SprachBelegungSekI.NICHT_BELEGT;
		if (JavaString.compareTo(kuerzel, "05") <= 0)
			return SprachBelegungSekI.AB_JAHRGANG_5;
		if (JavaString.compareTo(kuerzel, "07") <= 0)
			return SprachBelegungSekI.MIND_4_JAHRE;
		if (JavaString.compareTo(kuerzel, "09") <= 0)
			return SprachBelegungSekI.MIND_2_JAHRE;
		return SprachBelegungSekI.NICHT_BELEGT;
	}

	/**
	 * Ermittelt die Sprachbelegung in der Sek I anhand der übergebenen Dauer der Belegung in der Sek I.
	 * WICHTIG: Sollte ein Schüler sich im G8-Bildungsgang bewegen, so wird die Dauer
	 * mit 6 Jahren hier nicht korrekt zugeordnet.
	 *
	 * @param dauer   die Dauer in vollen Jahren bei der Sprachbelegung in der Sek I
	 *
	 * @return die Sprachbelegung in der Sek I
	 */
	public static getByDauer(dauer : number) : SprachBelegungSekI {
		if (dauer <= 0)
			return SprachBelegungSekI.NICHT_BELEGT;
		if (dauer <= 3)
			return SprachBelegungSekI.MIND_2_JAHRE;
		if (dauer <= 4)
			return SprachBelegungSekI.MIND_4_JAHRE;
		return SprachBelegungSekI.AB_JAHRGANG_5;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<SprachBelegungSekI> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : SprachBelegungSekI | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.fach.SprachBelegungSekI', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_fach_SprachBelegungSekI(obj : unknown) : SprachBelegungSekI {
	return obj as SprachBelegungSekI;
}
