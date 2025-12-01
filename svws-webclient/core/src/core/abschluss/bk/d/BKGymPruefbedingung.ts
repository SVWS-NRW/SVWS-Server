import { JavaEnum } from '../../../../java/lang/JavaEnum';
import { BKGymPruefbedingungArt } from '../../../../core/abschluss/bk/d/BKGymPruefbedingungArt';
import { Class } from '../../../../java/lang/Class';
import { BKGymPruefungsArt } from '../../../../core/abschluss/bk/d/BKGymPruefungsArt';

export class BKGymPruefbedingung extends JavaEnum<BKGymPruefbedingung> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<BKGymPruefbedingung> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, BKGymPruefbedingung> = new Map<string, BKGymPruefbedingung>();

	/**
	 * Prüfe, dass vier erste Leistungskurse eingebracht sind
	 */
	public static readonly KA_1: BKGymPruefbedingung = new BKGymPruefbedingung("KA_1", 0, "KA_1", BKGymPruefungsArt.ZULASSUNG, BKGymPruefbedingungArt.KURSART, "LK1", 4, "", null, "APO-BK AnlageD § 15 Abs. 2 Nr. 1", "Es sind weniger als acht Leistungskurse eingebracht worden.");

	/**
	 * Prüfe, dass vier zweite Leistungskurse eingebracht sind
	 */
	public static readonly KA_2: BKGymPruefbedingung = new BKGymPruefbedingung("KA_2", 1, "KA_2", BKGymPruefungsArt.ZULASSUNG, BKGymPruefbedingungArt.KURSART, "LK2", 4, "", null, "APO-BK AnlageD § 15 Abs. 2 Nr. 1 ", "Es sind weniger als acht Leistungskurse eingebracht worden.");

	/**
	 * Prüfe, dass vier Grundkurse des 3. Abifachs eingebracht sind
	 */
	public static readonly KA_3: BKGymPruefbedingung = new BKGymPruefbedingung("KA_3", 2, "KA_3", BKGymPruefungsArt.ZULASSUNG, BKGymPruefbedingungArt.KURSART, "AB3", 4, "", null, "APO-BK AnlageD § 15 Abs. 3 Nr. 1", "Es sind weniger als vier Kurse des dritten Abiturfachs eingebracht worden.");

	/**
	 * Prüfe, dass vier Grundkurse des 4. Abifachs eingebracht sind
	 */
	public static readonly KA_4: BKGymPruefbedingung = new BKGymPruefbedingung("KA_4", 3, "KA_4", BKGymPruefungsArt.ZULASSUNG, BKGymPruefbedingungArt.KURSART, "AB4", 4, "", null, "APO-BK AnlageD § 15 Abs. 3 Nr. 1", "Es sind weniger als vier Kurse des vierten Abiturfachs eingebracht worden.");

	/**
	 * Prüfe, dass vier Kurse des Fachs Deutsch eingebracht sind
	 */
	public static readonly FA_D: BKGymPruefbedingung = new BKGymPruefbedingung("FA_D", 4, "FA_D", BKGymPruefungsArt.ZULASSUNG, BKGymPruefbedingungArt.FACH, "Deutsch", 4, "", null, "APO-BK AnlageD § 15 Abs. 3 Nr. 2 a)", "Es sind weniger als vier Kurse Deutsch eingebracht worden.");

	/**
	 * Prüfe, dass vier Kurse des Fachs Mathematik eingebracht sind
	 */
	public static readonly FA_M: BKGymPruefbedingung = new BKGymPruefbedingung("FA_M", 5, "FA_M", BKGymPruefungsArt.ZULASSUNG, BKGymPruefbedingungArt.FACH, "Mathematik", 4, "", null, "APO-BK AnlageD § 15 Abs. 3 Nr. 2 c)", "Es sind weniger als vier Kurse Mathematik eingbracht worden.");

	/**
	 * Prüfe, dass zwei Kurse Gesellschaftslehre mit Geschichte eingebracht sind
	 */
	public static readonly FA_GMG: BKGymPruefbedingung = new BKGymPruefbedingung("FA_GMG", 6, "FA_GMG", BKGymPruefungsArt.ZULASSUNG, BKGymPruefbedingungArt.FACH, "Gesellschaftslehre mit Geschichte", 2, "", null, "APO-BK AnlageD § 15 Abs. 3 Nr. 2 e)", "Es sind weniger als zwei Kurse Gesellschaftslehre mit Geschichte eingbracht worden.");

	/**
	 * Prüfe, dass vier Kurse aus Aufgabenfeld II eingebracht sind
	 */
	public static readonly AF_II: BKGymPruefbedingung = new BKGymPruefbedingung("AF_II", 7, "AF_II", BKGymPruefungsArt.ZULASSUNG, BKGymPruefbedingungArt.AUFGABENFELD, "II", 4, "", null, "APO-BK AnlageD § 15 Abs. 3 Nr. 2 e)", "Es sind weniger als vier Kurse aus dem gesellschaftswissenschaftlichen Aufgabenbereich eingbracht worden.");

	/**
	 * Prüfe, dass vier Kurse der Fachgruppe Naturwissenschaft eingebracht sind
	 */
	public static readonly FG_NW: BKGymPruefbedingung = new BKGymPruefbedingung("FG_NW", 8, "FG_NW", BKGymPruefungsArt.ZULASSUNG, BKGymPruefbedingungArt.FACHGRUPPE, "Naturwissenschaft", 4, "EF.1", null, "APO-BK AnlageD § 15 Abs. 3 Nr. 2 d)", "Es sind weniger als vier Kurse Mathematik eingbracht worden.");

	/**
	 * Prüfe, dass zweite Fremdsprache belegt wurde seit 11.1
	 */
	public static readonly FS_1: BKGymPruefbedingung = new BKGymPruefbedingung("FS_1", 9, "FS_1", BKGymPruefungsArt.ZULASSUNG, BKGymPruefbedingungArt.FREMDSPRACHE, "FORTGEFUEHRT_NEU", 4, "", null, "APO-BK AnlageD § 15 Abs. 3 Nr. 2 b)", "Es sind weniger als vier Kurse einer Fremdsprache eingbracht worden.");

	/**
	 * Prüfe, dass mindestens zwei Kurse einer neu einsetzenden Fremdsprache eingebracht sind
	 */
	public static readonly FS_2: BKGymPruefbedingung = new BKGymPruefbedingung("FS_2", 10, "FS_2", BKGymPruefungsArt.ZULASSUNG, BKGymPruefbedingungArt.FREMDSPRACHE, "NEU_NICHT SI", 2, "", null, "APO-BK AnlageD § 15 Abs. 3", "Es sind weniger als zwei Kurse der neu einsetzenden Fremdsprache eingebracht worden, obwohl kein vier Jahre durchgängier Unterricht in der SI belegt wurde.");

	/**
	 * Prüfe, dass mindestens 4 Kurse der neu einsetzenden Fremdsprache belegt wurden, wenn eine zweite Fremdsprache nicht durchgängig vier Jahre in der SI belegt wurde.
	 */
	public static readonly FS_3: BKGymPruefbedingung = new BKGymPruefbedingung("FS_3", 11, "FS_3", BKGymPruefungsArt.ZULASSUNG, BKGymPruefbedingungArt.FREMDSPRACHE, "MINIMUM", 4, "keine zweite Fremdsprache in SI", null, "APO-BK AnlageD § 15 Abs. 2 Nr. 4", "Es sind weniger als vier Kurse der neu einsetzenden Fremdsprache belegt worden, obwohl kein vier Jahre durchgängiger Unterricht in der SI belegt wurde.");

	/**
	 * Prüfe, dass mindestens 32 Kurse eingebracht wurden inklusive Leistungskurse.
	 */
	public static readonly AK_1: BKGymPruefbedingung = new BKGymPruefbedingung("AK_1", 12, "AK_1", BKGymPruefungsArt.ZULASSUNG, BKGymPruefbedingungArt.ANZAHLKURSE, "MININUM", 32, "ohne Facharbeit", null, "APO-BK AnlageD § 15 Abs. 2 Nr. 1", "Es sind weniger als 32 Kurse in den Block I eingebracht worden.");

	/**
	 * Prüfe, dass höchsten 39 Kurse eingebracht sind, wenn die Facharbeit eingebracht wurde
	 */
	public static readonly AK_2: BKGymPruefbedingung = new BKGymPruefbedingung("AK_2", 13, "AK_2", BKGymPruefungsArt.ZULASSUNG, BKGymPruefbedingungArt.ANZAHLKURSE, "MAXIMUM", 39, "mit Facharbeit", null, "APO-BK AnlageD § 15 Abs. 2 Nr. 1 in V. Abs. 4 Nr. 3", "Bei eingebrachter Facharbeit sind mehr als 39 Kurse eingebracht worden.");

	/**
	 * Prüfe, dass höchsten 40 Kurse eingebracht sind, wenn keine Facharbeit eingebracht wurde
	 */
	public static readonly AK_3: BKGymPruefbedingung = new BKGymPruefbedingung("AK_3", 14, "AK_3", BKGymPruefungsArt.ZULASSUNG, BKGymPruefbedingungArt.ANZAHLKURSE, "MAXIMUM", 40, "ohne Facharbeit", null, "APO-BK AnlageD § 15 Abs. 2 Nr. 1 in V. Abs. 4 Nr. 3", "Es sind mehr als 40 Kurse in den Block I eingebracht worden.");

	/**
	 * Prüfe, dass mindestens 200 Punkte erreicht wurden
	 */
	public static readonly EP_1: BKGymPruefbedingung = new BKGymPruefbedingung("EP_1", 15, "EP_1", BKGymPruefungsArt.ZULASSUNG, BKGymPruefbedingungArt.PUNKTE, "MINIMUM", 200, "Block I", null, "APO-BK AnlageD § 15 Abs. 2 Nr. 2 in V. § 25 Abs. 3", "Es sind weniger als 200 Punkte im Block I erreicht worden.");

	/**
	 * Prüfe, dass nicht mehr als 3 Leistungskurs-Defizite aufweisen
	 */
	public static readonly AD_1: BKGymPruefbedingung = new BKGymPruefbedingung("AD_1", 16, "AD_1", BKGymPruefungsArt.ZULASSUNG, BKGymPruefbedingungArt.DEFIZITE_LK, "Leistungskurse", 3, "", null, "APO-BK AnlageD § 15 Abs. 2 Nr. 3", "Mehr als drei Leistungskurse weisen ein Defizit auf.");

	/**
	 * Prüfe, dass nicht mehr als 6 Defizite bei bis zu 32 eingebrachten Kursen vorliegen
	 */
	public static readonly AD_2: BKGymPruefbedingung = new BKGymPruefbedingung("AD_2", 17, "AD_2", BKGymPruefungsArt.ZULASSUNG, BKGymPruefbedingungArt.DEFIZITE, "", 6, "", 32, "APO-BK AnlageD § 15 Abs. 2 Nr. 3 a)", "Es wurden 32 Kurse eingebracht und mehr als sechs davon weisen Defizite auf.");

	/**
	 * Prüfe, dass nicht mehr als 7 Defizite bei bis zu 37 eingebrachten Kursen vorliegen
	 */
	public static readonly AD_3: BKGymPruefbedingung = new BKGymPruefbedingung("AD_3", 18, "AD_3", BKGymPruefungsArt.ZULASSUNG, BKGymPruefbedingungArt.DEFIZITE, "", 7, "", 37, "APO-BK AnlageD § 15 Abs. 2 Nr. 3 b)", "Es wurden 33 bis 37 Kurse eingebracht und mehr als sieben davon weisen Defizite auf.");

	/**
	 * Prüfe, dass nicht mehr als 8 Defizite bei bis zu 40 eingebrachten Kursen vorliegen
	 */
	public static readonly AD_4: BKGymPruefbedingung = new BKGymPruefbedingung("AD_4", 19, "AD_4", BKGymPruefungsArt.ZULASSUNG, BKGymPruefbedingungArt.DEFIZITE, "", 8, "", 40, "APO-BK AnlageD § 15 Abs. 2 Nr. 3 c)", "Es wurden mehr als 37 Kurse eingebracht und mehr als acht davon weisen Defizite auf.");

	/**
	 * Der eindeutige Code der Bedingung
	 */
	public readonly code: string;

	/**
	 * Die Art der Prüfung, zu der die Bedingung gehört
	 */
	public readonly pruefArt: BKGymPruefungsArt;

	/**
	 * Die Art der Bedingung
	 */
	public readonly bbArt: BKGymPruefbedingungArt;

	/**
	 * Der erste Textwert-Parameter
	 */
	public readonly text1: string;

	/**
	 * Der erste Zahlwert-Parameter
	 */
	public readonly zahl1: number;

	/**
	 * Der zweite Textwert-Parameter
	 */
	public readonly text2: string;

	/**
	 * Der zweite Zahlwert-Parameter
	 */
	public readonly zahl2: number | null;

	/**
	 * Der Text des Fehlers, der ausgegeben wird
	 */
	public readonly bezugAPOBK: string;

	/**
	 * Der Text des Fehlers, der ausgegeben wird
	 */
	public readonly hinweis: string;

	/**
	 * Erstellt einen neue Prüfbedingung für die Aufzählung (s.o.).
	 *
	 * @param code         der eindeutige Code der Prüfbedingung
	 * @param pruefArt     die Prüfungsart
	 * @param bbArt        die PrüfbedingungsArt
	 * @param text1        der erste Textwert-Parameter
	 * @param zahl1        der erste Zahlenwert-Parameter
	 * @param text2        der zweite Textwert-Parameter
	 * @param zahl2        der zweite Zahlenwert-Parameter
	 * @param bezugAPOBK   die Stelle in der APOBK, an der die Bedingung steht.
	 * @param hinweis      der Hinweis, der bei Auftreten des Fehlers ausgegeben wird.
	 */
	private constructor(name: string, ordinal: number, code: string, pruefArt: BKGymPruefungsArt, bbArt: BKGymPruefbedingungArt, text1: string, zahl1: number, text2: string, zahl2: number | null, bezugAPOBK: string, hinweis: string) {
		super(name, ordinal);
		BKGymPruefbedingung.all_values_by_ordinal.push(this);
		BKGymPruefbedingung.all_values_by_name.set(name, this);
		this.code = code;
		this.pruefArt = pruefArt;
		this.bbArt = bbArt;
		this.text1 = text1;
		this.zahl1 = zahl1;
		this.text2 = text2;
		this.zahl2 = zahl2;
		this.bezugAPOBK = bezugAPOBK;
		this.hinweis = hinweis;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<BKGymPruefbedingung> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): BKGymPruefbedingung | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.BKGymPruefbedingung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.BKGymPruefbedingung', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<BKGymPruefbedingung>('de.svws_nrw.core.abschluss.bk.d.BKGymPruefbedingung');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_BKGymPruefbedingung(obj: unknown): BKGymPruefbedingung {
	return obj as BKGymPruefbedingung;
}
