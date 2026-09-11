import { JavaEnum } from '../../../../java/lang/JavaEnum';
import { Class } from '../../../../java/lang/Class';

export class UvBlockungRegelPrioritaet extends JavaEnum<UvBlockungRegelPrioritaet> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<UvBlockungRegelPrioritaet> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, UvBlockungRegelPrioritaet> = new Map<string, UvBlockungRegelPrioritaet>();

	/**
	 * Sehr hohe Priorität.
	 */
	public static readonly SEHR_HOCH: UvBlockungRegelPrioritaet = new UvBlockungRegelPrioritaet("SEHR_HOCH", 0, 0, "sehr hoch");

	/**
	 * Hohe Priorität.
	 */
	public static readonly HOCH: UvBlockungRegelPrioritaet = new UvBlockungRegelPrioritaet("HOCH", 1, 1, "hoch");

	/**
	 * Mittlere Hohe Priorität.
	 */
	public static readonly MITTEL: UvBlockungRegelPrioritaet = new UvBlockungRegelPrioritaet("MITTEL", 2, 2, "mittel");

	/**
	 * Geringe Priorität.
	 */
	public static readonly GERING: UvBlockungRegelPrioritaet = new UvBlockungRegelPrioritaet("GERING", 3, 3, "gering");

	/**
	 * Sehr geringe Priorität.
	 */
	public static readonly SEHR_GERING: UvBlockungRegelPrioritaet = new UvBlockungRegelPrioritaet("SEHR_GERING", 4, 4, "sehr gering");

	/**
	 * Eine deaktivierte Regel. Muss den letzten Index haben.
	 */
	public static readonly DEAKTIVIERT: UvBlockungRegelPrioritaet = new UvBlockungRegelPrioritaet("DEAKTIVIERT", 5, 5, "deaktiviert");

	/**
	 * Die Nummer (der Index) der Priorität.
	 */
	public readonly nr: number;

	/**
	 * Die Bezeichnung der Priorität.
	 */
	public readonly bezeichnung: string | null;

	/**
	 * Erstellt einen neuen Regel-Typ mit der angegeben ID.
	 *
	 * @param nr            die Nummer der Priorität.
	 * @param bezeichnung   die textuelle Bezeichnung der Priorität.
	 */
	private constructor(name: string, ordinal: number, nr: number, bezeichnung: string) {
		super(name, ordinal);
		UvBlockungRegelPrioritaet.all_values_by_ordinal.push(this);
		UvBlockungRegelPrioritaet.all_values_by_name.set(name, this);
		this.nr = nr;
		this.bezeichnung = bezeichnung;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<UvBlockungRegelPrioritaet> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): UvBlockungRegelPrioritaet | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.regel.UvBlockungRegelPrioritaet';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.regel.UvBlockungRegelPrioritaet', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<UvBlockungRegelPrioritaet>('de.svws_nrw.core.data.uv.regel.UvBlockungRegelPrioritaet');

}

export function cast_de_svws_nrw_core_data_uv_regel_UvBlockungRegelPrioritaet(obj: unknown): UvBlockungRegelPrioritaet {
	return obj as UvBlockungRegelPrioritaet;
}
