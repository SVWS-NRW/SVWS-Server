import { JavaEnum } from '../../../java/lang/JavaEnum';
import { Class } from '../../../java/lang/Class';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';

export class BKGymPruefungsArt extends JavaEnum<BKGymPruefungsArt> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<BKGymPruefungsArt> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, BKGymPruefungsArt> = new Map<string, BKGymPruefungsArt>();

	/**
	 * Prüfung der Belegung von Kursen
	 */
	public static readonly BELEGUNG: BKGymPruefungsArt = new BKGymPruefungsArt("BELEGUNG", 0, "BELEGUNG");

	/**
	 * Markierung von Kursen
	 */
	public static readonly MARKIERUNG: BKGymPruefungsArt = new BKGymPruefungsArt("MARKIERUNG", 1, "MARKIERUNG");

	/**
	 * Prüfung der Zulassung zur Abiturprüfung - entspricht Markierungsbedingungen
	 */
	public static readonly ZULASSUNG: BKGymPruefungsArt = new BKGymPruefungsArt("ZULASSUNG", 2, "ZULASSUNG");

	/**
	 * Prüfung des Abiturergebnisses auf notwendige Nachprüfungen
	 */
	public static readonly NACHPRUEFUNG: BKGymPruefungsArt = new BKGymPruefungsArt("NACHPRUEFUNG", 3, "NACHPRUEFUNG");

	/**
	 * Prüfung ob das Abitur nach Ablegen aller Prüfungsbestandteile bestanden wurde.
	 */
	public static readonly BESTEHEN: BKGymPruefungsArt = new BKGymPruefungsArt("BESTEHEN", 4, "BESTEHEN");

	/**
	 * Das Kürzel für die Prüfungsart
	 */
	public readonly kuerzel: string;

	/**
	 * Erzeugt ein neues Abitur-Pruefungsart-Objekt
	 *
	 * @param kuerzel        das Kürzel der Prüfungsart
	 */
	private constructor(name: string, ordinal: number, kuerzel: string) {
		super(name, ordinal);
		BKGymPruefungsArt.all_values_by_ordinal.push(this);
		BKGymPruefungsArt.all_values_by_name.set(name, this);
		this.kuerzel = kuerzel;
	}

	/**
	 * Gibt die Prüfungsart anhand des übergebenen Kürzels zurück.
	 *
	 * @param kuerzel    das Kürzel der Prüfungsart
	 *
	 * @return die Prüfungsart
	 */
	public static fromKuerzel(kuerzel: string): BKGymPruefungsArt | null {
		let _sevar_1586417265 : any;
		const _seexpr_1586417265 = (kuerzel);
		if (_seexpr_1586417265 === "BELEGUNG") {
			_sevar_1586417265 = BKGymPruefungsArt.BELEGUNG;
		} else if (_seexpr_1586417265 === "ZULASSUNG") {
			_sevar_1586417265 = BKGymPruefungsArt.ZULASSUNG;
		} else if (_seexpr_1586417265 === "NACHPRUEFUNG") {
			_sevar_1586417265 = BKGymPruefungsArt.NACHPRUEFUNG;
		} else if (_seexpr_1586417265 === "BESTEHEN") {
			_sevar_1586417265 = BKGymPruefungsArt.BESTEHEN;
		} else {
			throw new DeveloperNotificationException("Die Prüfungsart " + kuerzel + " gibt es nicht.");
		}
		return _sevar_1586417265;
	}

	public toString(): string {
		return this.kuerzel;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<BKGymPruefungsArt> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): BKGymPruefungsArt | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.bk.BKGymPruefungsArt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.bk.BKGymPruefungsArt', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<BKGymPruefungsArt>('de.svws_nrw.core.types.bk.BKGymPruefungsArt');

}

export function cast_de_svws_nrw_core_types_bk_BKGymPruefungsArt(obj: unknown): BKGymPruefungsArt {
	return obj as BKGymPruefungsArt;
}
