import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class StatusSchulwechselAbgang extends JavaEnum<StatusSchulwechselAbgang> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<StatusSchulwechselAbgang> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, StatusSchulwechselAbgang> = new Map<string, StatusSchulwechselAbgang>();

	/**
	 * Initialer Status, wenn ein Schüler für einen Wechsel markiert wurde
	 */
	public static readonly BEVORSTEHEND: StatusSchulwechselAbgang = new StatusSchulwechselAbgang("BEVORSTEHEND", 0, 1, "bevorstehend");

	/**
	 * Wird gesetzt, sobald der Versand des Wechseldokuments eines Schülers angestossen wurde
	 */
	public static readonly GEPLANT: StatusSchulwechselAbgang = new StatusSchulwechselAbgang("GEPLANT", 1, 2, "geplant");

	/**
	 * Wird gesetzt, sobald ein Wechseldokument für den Wechselvorgang versendet wurde
	 */
	public static readonly GESENDET: StatusSchulwechselAbgang = new StatusSchulwechselAbgang("GESENDET", 2, 3, "gesendet");

	/**
	 * Wird gesetzt, sobald die Bestätigung für den erfolgreichen Wechselvorgang eingegangen ist
	 */
	public static readonly BESTAETIGT: StatusSchulwechselAbgang = new StatusSchulwechselAbgang("BESTAETIGT", 3, 4, "bestätigt");

	/**
	 * Wird gesetzt, wenn ein Wechsel nicht erfolgreich war
	 */
	public static readonly NICHT_VERSORGT: StatusSchulwechselAbgang = new StatusSchulwechselAbgang("NICHT_VERSORGT", 4, 5, "nicht versorgt");

	private readonly id: number;

	private readonly bezeichnung: string;

	private constructor(name: string, ordinal: number, id: number, bezeichnung: string) {
		super(name, ordinal);
		StatusSchulwechselAbgang.all_values_by_ordinal.push(this);
		StatusSchulwechselAbgang.all_values_by_name.set(name, this);
		this.id = id;
		this.bezeichnung = bezeichnung;
	}

	/**
	 * Bestimmt den Status anhand der Id.
	 *
	 * @param id   die Id des Status
	 *
	 * @return den Status oder null, falls kein Status mit dieser Id existiert
	 */
	public static getByIdOrNull(id: number): StatusSchulwechselAbgang | null {
		for (const status of StatusSchulwechselAbgang.values()) {
			if (status.id === id) {
				return status;
			}
		}
		return null;
	}

	/**
	 * Bestimmt den Status anhand der Bezeichnung.
	 *
	 * @param bezeichnung   die Bezeichnung des Status
	 *
	 * @return den Status oder null, falls kein Status mit dieser Bezeichnung existiert
	 */
	public static getByBezeichnungOrNull(bezeichnung: string): StatusSchulwechselAbgang | null {
		for (const status of StatusSchulwechselAbgang.values()) {
			if (JavaObject.equalsTranspiler(status.bezeichnung, (bezeichnung))) {
				return status;
			}
		}
		return null;
	}

	/**
	 * Gibt den Statustext zurück.
	 *
	 * @return den Text des Status
	 */
	public toString(): string {
		return this.bezeichnung;
	}

	/**
	 * Gibt die Id des Status zurück.
	 *
	 * @return die Id des Status
	 */
	public getId(): number {
		return this.id;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<StatusSchulwechselAbgang> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): StatusSchulwechselAbgang | null {
		const tmp = this.all_values_by_name.get(name);
		return tmp ?? null;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.schule.StatusSchulwechselAbgang';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.schule.StatusSchulwechselAbgang', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<StatusSchulwechselAbgang>('de.svws_nrw.core.types.schule.StatusSchulwechselAbgang');

}

export function cast_de_svws_nrw_core_types_schule_StatusSchulwechselAbgang(obj: unknown): StatusSchulwechselAbgang {
	return obj as StatusSchulwechselAbgang;
}
