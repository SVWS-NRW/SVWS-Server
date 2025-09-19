import { JavaEnum } from '../../../../java/lang/JavaEnum';
import { BKGymBelegungsfehlerArt } from '../../../../core/abschluss/bk/d/BKGymBelegungsfehlerArt';
import { Class } from '../../../../java/lang/Class';

export class BKGymBelegungsfehler extends JavaEnum<BKGymBelegungsfehler> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<BKGymBelegungsfehler> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, BKGymBelegungsfehler> = new Map<string, BKGymBelegungsfehler>();

	/**
	 * BelegungsfehlerArt LK_1
	 */
	public static readonly LK_1 : BKGymBelegungsfehler = new BKGymBelegungsfehler("LK_1", 0, "LK_1", BKGymBelegungsfehlerArt.BELEGUNG, "Es muss ein erster Leistungskurs gewählt werden.");

	/**
	 * BelegungsfehlerArt LK_2
	 */
	public static readonly LK_2 : BKGymBelegungsfehler = new BKGymBelegungsfehler("LK_2", 1, "LK_2", BKGymBelegungsfehlerArt.BELEGUNG, "Es muss ein zweiter Leistungskurs gewählt werden.");

	/**
	 * BelegungsfehlerArt LK_3
	 */
	public static readonly LK_3 : BKGymBelegungsfehler = new BKGymBelegungsfehler("LK_3", 2, "LK_3", BKGymBelegungsfehlerArt.BELEGUNG, "Die Kombination aus erstem und zweiten Leistungskurs ist für den Bildungsgang nicht zulässig.");

	/**
	 * BelegungsfehlerArt AB_3
	 */
	public static readonly AB_3 : BKGymBelegungsfehler = new BKGymBelegungsfehler("AB_3", 3, "AB_3", BKGymBelegungsfehlerArt.BELEGUNG, "Es muss ein drittes Abiturfach gewählt werden.");

	/**
	 * BelegungsfehlerArt AB_4
	 */
	public static readonly AB_4 : BKGymBelegungsfehler = new BKGymBelegungsfehler("AB_4", 4, "AB_4", BKGymBelegungsfehlerArt.BELEGUNG, "Es muss ein viertes Abiturfach gewählt werden.");

	/**
	 * BelegungsfehlerArt AB_5
	 */
	public static readonly AB_5 : BKGymBelegungsfehler = new BKGymBelegungsfehler("AB_5", 5, "AB_5", BKGymBelegungsfehlerArt.BELEGUNG, "Die gewählte Kombination aus drittem und viertem Abiturfach is in dem Bildungsgang nicht zulässig.");

	/**
	 * BelegungsfehlerArt ABI_11
	 */
	public static readonly ABI_11 : BKGymBelegungsfehler = new BKGymBelegungsfehler("ABI_11", 6, "ABI_11", BKGymBelegungsfehlerArt.BELEGUNG, "Religionslehre und Sport dürfen nicht gleichzeitig Abiturfächer sein.");

	/**
	 * BelegungsfehlerArt ABI_12
	 */
	public static readonly ABI_12 : BKGymBelegungsfehler = new BKGymBelegungsfehler("ABI_12", 7, "ABI_12", BKGymBelegungsfehlerArt.SCHRIFTLICHKEIT, "In Q2.2 muss das 3. Abiturfach schriftlich belegt sein.");

	/**
	 * BelegungsfehlerArt 	ABI_13
	 */
	public static readonly ABI_13 : BKGymBelegungsfehler = new BKGymBelegungsfehler("ABI_13", 8, "ABI_13", BKGymBelegungsfehlerArt.SCHRIFTLICHKEIT, "In Q2.2 muss das 4. Abiturfach mündlich belegt sein.");

	/**
	 * Der eindeutige Code des Belegungsfehlers
	 */
	public readonly code : string;

	/**
	 * Die Art des Fehlers
	 */
	public readonly art : BKGymBelegungsfehlerArt;

	/**
	 * Der Text des Fehlers, der ausgegeben wird
	 */
	public readonly text : string;

	/**
	 * Erstellt einen neuen Belegungsfehler für die Aufzählung (s.o.). Dabei wird ein
	 * Text für die Prüfung angegeben.
	 *
	 * @param code   der eindeutige Code des Belegungsfehlers
	 * @param art    die Fehlerart (Belegungsfehler, Schriftlichkeit oder Information)
	 * @param text   der zugeordnete Text für die Gesamtbelegprüfung oder null
	 */
	private constructor(name : string, ordinal : number, code : string, art : BKGymBelegungsfehlerArt, text : string) {
		super(name, ordinal);
		BKGymBelegungsfehler.all_values_by_ordinal.push(this);
		BKGymBelegungsfehler.all_values_by_name.set(name, this);
		this.code = code;
		this.art = art;
		this.text = text;
	}

	/**
	 * Gibt zurück, ob es sich bei dem Belegungsfehler nur um eine Information
	 * und nicht um einen "echten" Fehler handelt.
	 *
	 * @return true, falls es sich nur um eine Information handelt, sonst false
	 */
	public istInfo() : boolean {
		return (this.art as unknown === BKGymBelegungsfehlerArt.HINWEIS as unknown);
	}

	/**
	 * Gibt zurück, ob es sich bei dem Belegungsfehler um einen "echten" Fehler handelt
	 * und nicht nur um eine Information.
	 *
	 * @return true, falls es sich um einen "echten" Fehler handelt, sonst false
	 */
	public istFehler() : boolean {
		return (this.art as unknown !== BKGymBelegungsfehlerArt.HINWEIS as unknown);
	}

	/**
	 * Gibt die Art des Belegungsfehlers zurück.
	 *
	 * @return die Art des Belegungsfehlers
	 */
	public getArt() : BKGymBelegungsfehlerArt {
		return this.art;
	}

	/**
	 * Gibt den zugehörigen Text für den Belegungsfehler zurück.
	 *
	 * @return der zugehörige Text des Belegungsfehlers
	 */
	public getText() : string {
		return this.text;
	}

	public toString() : string {
		return this.code;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<BKGymBelegungsfehler> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : BKGymBelegungsfehler | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.BKGymBelegungsfehler';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.BKGymBelegungsfehler', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<BKGymBelegungsfehler>('de.svws_nrw.core.abschluss.bk.d.BKGymBelegungsfehler');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_BKGymBelegungsfehler(obj : unknown) : BKGymBelegungsfehler {
	return obj as BKGymBelegungsfehler;
}
