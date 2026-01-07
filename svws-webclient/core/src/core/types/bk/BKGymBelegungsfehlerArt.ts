import { JavaEnum } from '../../../java/lang/JavaEnum';
import { Class } from '../../../java/lang/Class';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';

export class BKGymBelegungsfehlerArt extends JavaEnum<BKGymBelegungsfehlerArt> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<BKGymBelegungsfehlerArt> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, BKGymBelegungsfehlerArt> = new Map<string, BKGymBelegungsfehlerArt>();

	/**
	 * Belegungsfehler
	 */
	public static readonly BELEGUNG: BKGymBelegungsfehlerArt = new BKGymBelegungsfehlerArt("BELEGUNG", 0, "BELEGUNG");

	/**
	 * Fehler bei der Schriftlichkeit
	 */
	public static readonly SCHRIFTLICHKEIT: BKGymBelegungsfehlerArt = new BKGymBelegungsfehlerArt("SCHRIFTLICHKEIT", 1, "SCHRIFTLICHKEIT");

	/**
	 * Fehler in Bezug auf schulische Rahmenbedingungen - Zusatzkursbeginn oder nicht erlaubte oder geforderte Fachkombinationen
	 */
	public static readonly SCHULSPEZIFISCH: BKGymBelegungsfehlerArt = new BKGymBelegungsfehlerArt("SCHULSPEZIFISCH", 2, "SCHULSPEZIFISCH");

	/**
	 * Information, aber kein Fehler
	 */
	public static readonly HINWEIS: BKGymBelegungsfehlerArt = new BKGymBelegungsfehlerArt("HINWEIS", 3, "HINWEIS");

	/**
	 * Das Kürzel für die Belegungsfehlerart
	 */
	public readonly kuerzel: string;

	/**
	 * Erzeugt ein neues Abitur-Belegungsfehler-Objekt
	 *
	 * @param kuerzel        das Kürzel der Fehler-Art
	 */
	private constructor(name: string, ordinal: number, kuerzel: string) {
		super(name, ordinal);
		BKGymBelegungsfehlerArt.all_values_by_ordinal.push(this);
		BKGymBelegungsfehlerArt.all_values_by_name.set(name, this);
		this.kuerzel = kuerzel;
	}

	/**
	 * Gibt die Belegungsfehler-Art anhand des übergebenen Kürzels zurück.
	 *
	 * @param kuerzel    das Kürzel der Belegungsfehler-Art
	 *
	 * @return die Belegungsfehler-Art
	 */
	public static fromKuerzel(kuerzel: string): BKGymBelegungsfehlerArt {
		let _sevar_2055730872 : any;
		const _seexpr_2055730872 = (kuerzel);
		if (_seexpr_2055730872 === "BELEGUNG") {
			_sevar_2055730872 = BKGymBelegungsfehlerArt.BELEGUNG;
		} else if (_seexpr_2055730872 === "SCHRIFTLICHKEIT") {
			_sevar_2055730872 = BKGymBelegungsfehlerArt.SCHRIFTLICHKEIT;
		} else if (_seexpr_2055730872 === "SCHULSPEZIFISCH") {
			_sevar_2055730872 = BKGymBelegungsfehlerArt.SCHULSPEZIFISCH;
		} else if (_seexpr_2055730872 === "HINWEIS") {
			_sevar_2055730872 = BKGymBelegungsfehlerArt.HINWEIS;
		} else {
			throw new DeveloperNotificationException("Die Belegungsfehlerart " + kuerzel + " gibt es nicht.");
		}
		return _sevar_2055730872;
	}

	public toString(): string {
		return this.kuerzel;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<BKGymBelegungsfehlerArt> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): BKGymBelegungsfehlerArt | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.bk.BKGymBelegungsfehlerArt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.bk.BKGymBelegungsfehlerArt', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<BKGymBelegungsfehlerArt>('de.svws_nrw.core.types.bk.BKGymBelegungsfehlerArt');

}

export function cast_de_svws_nrw_core_types_bk_BKGymBelegungsfehlerArt(obj: unknown): BKGymBelegungsfehlerArt {
	return obj as BKGymBelegungsfehlerArt;
}
