import { JavaEnum } from '../../../java/lang/JavaEnum';
import { BKGymBelegungsfehlerArt } from '../../../core/types/bk/BKGymBelegungsfehlerArt';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';

export class BKGymBelegungsfehlerTyp extends JavaEnum<BKGymBelegungsfehlerTyp> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<BKGymBelegungsfehlerTyp> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, BKGymBelegungsfehlerTyp> = new Map<string, BKGymBelegungsfehlerTyp>();

	/**
	 * BelegungsfehlerArt LK_1 | Parameter: Fachbezeichnung
	 */
	public static readonly LK_1: BKGymBelegungsfehlerTyp = new BKGymBelegungsfehlerTyp("LK_1", 0, "LK_1", BKGymBelegungsfehlerArt.BELEGUNG, 10, "Das Fach %s ist nicht der erste Leistungskurs.");

	/**
	 * BelegungsfehlerArt LK_2 | Parameter: Fachbezeichnung
	 */
	public static readonly LK_2: BKGymBelegungsfehlerTyp = new BKGymBelegungsfehlerTyp("LK_2", 1, "LK_2", BKGymBelegungsfehlerArt.BELEGUNG, 10, "Das Fach %s ist nicht der zweite Leistungskurs.");

	/**
	 * BelegungsfehlerArt LK_3 | Parameter: Fachbezeichnung
	 */
	public static readonly LK_3: BKGymBelegungsfehlerTyp = new BKGymBelegungsfehlerTyp("LK_3", 2, "LK_3", BKGymBelegungsfehlerArt.BELEGUNG, 10, "Das Grundkursfach %s ist als Leistungskurs markiert.");

	/**
	 * BelegungsfehlerArt AB_3
	 */
	public static readonly AB_3: BKGymBelegungsfehlerTyp = new BKGymBelegungsfehlerTyp("AB_3", 3, "AB_3", BKGymBelegungsfehlerArt.BELEGUNG, 10, "Es muss ein drittes Abiturfach gewählt werden.");

	/**
	 * BelegungsfehlerArt AB_4
	 */
	public static readonly AB_4: BKGymBelegungsfehlerTyp = new BKGymBelegungsfehlerTyp("AB_4", 4, "AB_4", BKGymBelegungsfehlerArt.BELEGUNG, 10, "Es muss ein viertes Abiturfach gewählt werden.");

	/**
	 * BelegungsfehlerArt AB_5 | Parameter: Fachkürzel von AB3, Fachkürzel von AB4, Gliederung, Fachklassenschlüssel
	 */
	public static readonly AB_5: BKGymBelegungsfehlerTyp = new BKGymBelegungsfehlerTyp("AB_5", 5, "AB_5", BKGymBelegungsfehlerArt.BELEGUNG, 5, "Die gewählte Kombination aus 3. Abiturfach (%s) und 4. Abiturfach (%s) ist in dem Bildungsgang %s%s nicht zulässig.");

	/**
	 * BelegungsfehlerArt ST_1 | Parameter: Fachbezeichnung
	 */
	public static readonly ST_1: BKGymBelegungsfehlerTyp = new BKGymBelegungsfehlerTyp("ST_1", 6, "ST_1", BKGymBelegungsfehlerArt.BELEGUNG, 5, "Das Fach %s der Stundentafel wurde mehrfach belegt.");

	/**
	 * BelegungsfehlerArt ST_2 | Parameter: Fachbezeichnung, Halbjahr
	 */
	public static readonly ST_2: BKGymBelegungsfehlerTyp = new BKGymBelegungsfehlerTyp("ST_2", 7, "ST_2", BKGymBelegungsfehlerArt.BELEGUNG, 5, "Das Fach %s weist im Halbjahr %s keine Note auf, obwohl es belegt werden muss.");

	/**
	 * BelegungsfehlerArt ST_3 | Parameter: Fachbezeichnung
	 */
	public static readonly ST_3: BKGymBelegungsfehlerTyp = new BKGymBelegungsfehlerTyp("ST_3", 8, "ST_3", BKGymBelegungsfehlerArt.BELEGUNG, 5, "Das Fach %s der Stundentafel wurde nicht im nötigen Stundenumfang belegt.");

	/**
	 * BelegungsfehlerArt ST_4 | Parameter: Fachbezeichnung
	 */
	public static readonly ST_4: BKGymBelegungsfehlerTyp = new BKGymBelegungsfehlerTyp("ST_4", 9, "ST_4", BKGymBelegungsfehlerArt.BELEGUNG, 10, "Das Fach %s der Stundentafel wurde überhaupt nicht belegt.");

	/**
	 * BelegungsfehlerArt ST_5_INFO | Parameter: Fachbezeichnung
	 */
	public static readonly ST_5_INFO: BKGymBelegungsfehlerTyp = new BKGymBelegungsfehlerTyp("ST_5_INFO", 10, "ST_5_INFO", BKGymBelegungsfehlerArt.HINWEIS, 0, "Der Stundenumfang des Fachs %s ist in der Summe erfüllt, aber nicht in allen Halbjahren.");

	/**
	 * BelegungsfehlerArt ST_6 | Parameter: Fachbezeichnung, Halbjahr
	 */
	public static readonly ST_6: BKGymBelegungsfehlerTyp = new BKGymBelegungsfehlerTyp("ST_6", 11, "ST_6", BKGymBelegungsfehlerArt.BELEGUNG, 5, "Das Fach %s der Stundentafel wurde im Halbjahr %s nicht belegt.");

	/**
	 * BelegungsfehlerArt KL_1 Klausur | Parameter: Fachbezeichnung, Halbjahr
	 */
	public static readonly KL_1: BKGymBelegungsfehlerTyp = new BKGymBelegungsfehlerTyp("KL_1", 12, "KL_1", BKGymBelegungsfehlerArt.SCHRIFTLICHKEIT, 2, "Im Fach %s fehlt die Klausurbelegung im Halbjahr %s.");

	/**
	 * BelegungsfehlerArt KL_2 Klausur | Parameter: Fachbezeichnung, Halbjahr
	 */
	public static readonly KL_2: BKGymBelegungsfehlerTyp = new BKGymBelegungsfehlerTyp("KL_2", 13, "KL_2", BKGymBelegungsfehlerArt.SCHRIFTLICHKEIT, 2, "Im LK-Fach %s fehlt die Klausurbelegung im Halbjahr %s.");

	/**
	 * BelegungsfehlerArt KL_3 Klausur | Parameter: Fachbezeichnung, Halbjahr
	 */
	public static readonly KL_3: BKGymBelegungsfehlerTyp = new BKGymBelegungsfehlerTyp("KL_3", 14, "KL_3", BKGymBelegungsfehlerArt.SCHRIFTLICHKEIT, 2, "In der Fremdsprache %s fehlt die Klausurbelegung im Halbjahr %s.");

	/**
	 * BelegungsfehlerArt KL_4 Klausur | Parameter: Fachbezeichnung, Halbjahr
	 */
	public static readonly KL_4: BKGymBelegungsfehlerTyp = new BKGymBelegungsfehlerTyp("KL_4", 15, "KL_4", BKGymBelegungsfehlerArt.SCHRIFTLICHKEIT, 2, "Im Abiturfach %s fehlt die Klausurbelegung im Halbjahr %s.");

	/**
	 * BelegungsfehlerArt KL_1_INFO Klausur | Parameter: Fachbezeichnung, Halbjahr
	 */
	public static readonly KL_1_INFO: BKGymBelegungsfehlerTyp = new BKGymBelegungsfehlerTyp("KL_1_INFO", 16, "KL_1_INFO", BKGymBelegungsfehlerArt.SCHRIFTLICHKEIT, 0, "Hinweis: Im Fach %s fehlt die Klausurbelegung im Halbjahr %s.");

	/**
	 * BelegungsfehlerArt KL_3_INFO Klausur | Parameter: Fachbezeichnung, Halbjahr
	 */
	public static readonly KL_3_INFO: BKGymBelegungsfehlerTyp = new BKGymBelegungsfehlerTyp("KL_3_INFO", 17, "KL_3_INFO", BKGymBelegungsfehlerArt.SCHRIFTLICHKEIT, 0, "Hinweis: In der Fremdsprache %s fehlt die Klausurbelegung im Halbjahr %s.");

	/**
	 * BelegungsfehlerArt HJ_1_INFO Belegung | Parameter: Halbjahr
	 */
	public static readonly HJ_1_INFO: BKGymBelegungsfehlerTyp = new BKGymBelegungsfehlerTyp("HJ_1_INFO", 18, "KL_3_INFO", BKGymBelegungsfehlerArt.BELEGUNG, 0, "Hinweis: Das Halbjahr %s ist nicht bewertet. Bedingungen müssen manuell geprüft werden.");

	/**
	 * Der eindeutige Code des Belegungsfehlers
	 */
	public readonly code: string;

	/**
	 * Die Art des Fehlers
	 */
	public readonly art: BKGymBelegungsfehlerArt;

	/**
	 * Der Wert des Fehlers höhere Werte gleich schwerwiegenderer Fehler
	 */
	public readonly wert: number;

	/**
	 * Der Text des Fehlers, der ausgegeben wird
	 */
	public readonly text: string;

	/**
	 * Erstellt einen neuen Belegungsfehler für die Aufzählung (s.o.). Dabei wird ein
	 * Text für die Prüfung angegeben.
	 *
	 * @param code   der eindeutige Code des Belegungsfehlers
	 * @param art    die Fehlerart (Belegungsfehler, Schriftlichkeit oder Information)
	 * @param wert   die Härte des Fehlers zur Beurteilung der Fehlerschwere
	 * @param text   der zugeordnete Text für die Gesamtbelegprüfung oder null
	 */
	private constructor(name: string, ordinal: number, code: string, art: BKGymBelegungsfehlerArt, wert: number, text: string) {
		super(name, ordinal);
		BKGymBelegungsfehlerTyp.all_values_by_ordinal.push(this);
		BKGymBelegungsfehlerTyp.all_values_by_name.set(name, this);
		this.code = code;
		this.art = art;
		this.wert = wert;
		this.text = text;
	}

	/**
	 * Gibt zurück, ob es sich bei dem Belegungsfehler nur um eine Information
	 * und nicht um einen "echten" Fehler handelt.
	 *
	 * @return true, falls es sich nur um eine Information handelt, sonst false
	 */
	public istInfo(): boolean {
		return (this.art as unknown === BKGymBelegungsfehlerArt.HINWEIS as unknown);
	}

	/**
	 * Gibt zurück, ob es sich bei dem Belegungsfehler um einen "echten" Fehler handelt
	 * und nicht nur um eine Information.
	 *
	 * @return true, falls es sich um einen "echten" Fehler handelt, sonst false
	 */
	public istFehler(): boolean {
		return (this.art as unknown !== BKGymBelegungsfehlerArt.HINWEIS as unknown);
	}

	/**
	 * Gibt die Art des Belegungsfehlers zurück.
	 *
	 * @return die Art des Belegungsfehlers
	 */
	public getArt(): BKGymBelegungsfehlerArt {
		return this.art;
	}

	/**
	 * Gibt den zugehörigen Wert für den Belegungsfehler zurück.
	 *
	 * @return der zugehörige Wert des Belegungsfehlers
	 */
	public getWert(): number {
		return this.wert;
	}

	/**
	 * Gibt den zugehörigen Text für den Belegungsfehler zurück.
	 *
	 * @return der zugehörige Text des Belegungsfehlers
	 */
	public getText(): string {
		return this.text;
	}

	public toString(): string {
		return this.code;
	}

	/**
	 * Gibt den formatierten Text mit den angegebenen Parametern zurück.
	 *
	 * @param args   die Parameter für den String
	 *
	 * @return der Fehlertext mit den eingesetzten Parametern
	 */
	public format(...args: Array<unknown>): string {
		return JavaString.format(this.text, ...args);
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<BKGymBelegungsfehlerTyp> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): BKGymBelegungsfehlerTyp | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.bk.BKGymBelegungsfehlerTyp';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.bk.BKGymBelegungsfehlerTyp', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<BKGymBelegungsfehlerTyp>('de.svws_nrw.core.types.bk.BKGymBelegungsfehlerTyp');

}

export function cast_de_svws_nrw_core_types_bk_BKGymBelegungsfehlerTyp(obj: unknown): BKGymBelegungsfehlerTyp {
	return obj as BKGymBelegungsfehlerTyp;
}
