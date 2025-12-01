import { JavaEnum } from '../../../../java/lang/JavaEnum';
import { Class } from '../../../../java/lang/Class';
import { DeveloperNotificationException } from '../../../../core/exceptions/DeveloperNotificationException';

export class BKGymPruefbedingungArt extends JavaEnum<BKGymPruefbedingungArt> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<BKGymPruefbedingungArt> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, BKGymPruefbedingungArt> = new Map<string, BKGymPruefbedingungArt>();

	/**
	 * Bedingungen, die für bestimmte Kursarten gelten
	 */
	public static readonly KURSART: BKGymPruefbedingungArt = new BKGymPruefbedingungArt("KURSART", 0, "KURSART");

	/**
	 * Bedingungen, die für konkrete Fächer gelten
	 */
	public static readonly FACH: BKGymPruefbedingungArt = new BKGymPruefbedingungArt("FACH", 1, "FACH");

	/**
	 * Bedingungen, die für Fremdsprachen gelten
	 */
	public static readonly FREMDSPRACHE: BKGymPruefbedingungArt = new BKGymPruefbedingungArt("FREMDSPRACHE", 2, "FREMDSPRACHE");

	/**
	 * Bedingungen, die für die Aufgabenfelder I, II und III gelten
	 */
	public static readonly AUFGABENFELD: BKGymPruefbedingungArt = new BKGymPruefbedingungArt("AUFGABENFELD", 3, "AUFGABENFELD");

	/**
	 * Bedingungen, die für die Gesamtanzahl von eingebrachten Kursen gelten
	 */
	public static readonly ANZAHLKURSE: BKGymPruefbedingungArt = new BKGymPruefbedingungArt("ANZAHLKURSE", 4, "ANZAHLKURSE");

	/**
	 * Bedingungen, die für eine bestimmte Fachgruppe gilt
	 */
	public static readonly FACHGRUPPE: BKGymPruefbedingungArt = new BKGymPruefbedingungArt("FACHGRUPPE", 5, "FACHGRUPPE");

	/**
	 * Bedingungen, die für erreichte Punkte gilt
	 */
	public static readonly PUNKTE: BKGymPruefbedingungArt = new BKGymPruefbedingungArt("PUNKTE", 6, "PUNKTE");

	/**
	 * Bedingungen, die für Gesamtanzahl von Defiziten gilt
	 */
	public static readonly DEFIZITE: BKGymPruefbedingungArt = new BKGymPruefbedingungArt("DEFIZITE", 7, "DEFIZITE");

	/**
	 * Bedingungen, die für Anzahl von Defiziten im Leistungskursbereich gilt
	 */
	public static readonly DEFIZITE_LK: BKGymPruefbedingungArt = new BKGymPruefbedingungArt("DEFIZITE_LK", 8, "DEFIZITE_LK");

	/**
	 * Das Kürzel für die Belegungsfehlerart
	 */
	public readonly kuerzel: string;

	/**
	 * Erzeugt ein neues Objekt der PrüfbedingungsArt
	 *
	 * @param kuerzel        das Kürzel der PrüfbedingungArt
	 */
	private constructor(name: string, ordinal: number, kuerzel: string) {
		super(name, ordinal);
		BKGymPruefbedingungArt.all_values_by_ordinal.push(this);
		BKGymPruefbedingungArt.all_values_by_name.set(name, this);
		this.kuerzel = kuerzel;
	}

	/**
	 * Gibt die Prüfbedingungsart anhand des übergebenen Kürzels zurück.
	 *
	 * @param kuerzel    das Kürzel der Prüfbedingungsart
	 *
	 * @return die Prüfbedingungsart
	 */
	public static fromKuerzel(kuerzel: string): BKGymPruefbedingungArt {
		let _sevar_1692909530 : any;
		const _seexpr_1692909530 = (kuerzel);
		if (_seexpr_1692909530 === "KURSART") {
			_sevar_1692909530 = BKGymPruefbedingungArt.KURSART;
		} else if (_seexpr_1692909530 === "FACH") {
			_sevar_1692909530 = BKGymPruefbedingungArt.FACH;
		} else if (_seexpr_1692909530 === "FREMDSPRACHE") {
			_sevar_1692909530 = BKGymPruefbedingungArt.FREMDSPRACHE;
		} else if (_seexpr_1692909530 === "AUFGABENFELD") {
			_sevar_1692909530 = BKGymPruefbedingungArt.AUFGABENFELD;
		} else if (_seexpr_1692909530 === "ANZAHLKURSE") {
			_sevar_1692909530 = BKGymPruefbedingungArt.ANZAHLKURSE;
		} else if (_seexpr_1692909530 === "FACHGRUPPE") {
			_sevar_1692909530 = BKGymPruefbedingungArt.FACHGRUPPE;
		} else {
			throw new DeveloperNotificationException("Die Prüfungbedingungsart " + kuerzel + " gibt es nicht.");
		}
		return _sevar_1692909530;
	}

	public toString(): string {
		return this.kuerzel;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<BKGymPruefbedingungArt> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): BKGymPruefbedingungArt | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.BKGymPruefbedingungArt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.BKGymPruefbedingungArt', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<BKGymPruefbedingungArt>('de.svws_nrw.core.abschluss.bk.d.BKGymPruefbedingungArt');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_BKGymPruefbedingungArt(obj: unknown): BKGymPruefbedingungArt {
	return obj as BKGymPruefbedingungArt;
}
