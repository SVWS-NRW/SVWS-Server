import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class StatusSchulwechselErfolgreicheBewerbung extends JavaEnum<StatusSchulwechselErfolgreicheBewerbung> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<StatusSchulwechselErfolgreicheBewerbung> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, StatusSchulwechselErfolgreicheBewerbung> = new Map<string, StatusSchulwechselErfolgreicheBewerbung>();

	/**
	 * Initialer Status, nachdem ein Schüler von schulbewerbung.de importiert wurde
	 */
	public static readonly NEU: StatusSchulwechselErfolgreicheBewerbung = new StatusSchulwechselErfolgreicheBewerbung("NEU", 0, 1, "neu");

	/**
	 * Wird gesetzt, sobald ein ordentlicher Datensatz für den Schüler angelegt wurde
	 */
	public static readonly AUFGENOMMEN: StatusSchulwechselErfolgreicheBewerbung = new StatusSchulwechselErfolgreicheBewerbung("AUFGENOMMEN", 1, 2, "aufgenommen");

	private readonly id: number;

	private readonly bezeichnung: string;

	private constructor(name: string, ordinal: number, id: number, bezeichnung: string) {
		super(name, ordinal);
		StatusSchulwechselErfolgreicheBewerbung.all_values_by_ordinal.push(this);
		StatusSchulwechselErfolgreicheBewerbung.all_values_by_name.set(name, this);
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
	public static getByIdOrNull(id: number): StatusSchulwechselErfolgreicheBewerbung | null {
		for (const status of StatusSchulwechselErfolgreicheBewerbung.values()) {
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
	public static getByBezeichnungOrNull(bezeichnung: string): StatusSchulwechselErfolgreicheBewerbung | null {
		for (const status of StatusSchulwechselErfolgreicheBewerbung.values()) {
			if (JavaObject.equalsTranspiler(status.bezeichnung, (bezeichnung))) {
				return status;
			}
		}
		return null;
	}

	/**
	 * Gibt die Bezeichnung des Status zurück.
	 *
	 * @return die Bezeichnung des Status
	 */
	public getBezeichnung(): string {
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
	 * Gibt den Statustext zurück.
	 *
	 * @return den Text des Status
	 */
	public toString(): string {
		return this.getBezeichnung();
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<StatusSchulwechselErfolgreicheBewerbung> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): StatusSchulwechselErfolgreicheBewerbung | null {
		const tmp = this.all_values_by_name.get(name);
		return tmp ?? null;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.schule.StatusSchulwechselErfolgreicheBewerbung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.schule.StatusSchulwechselErfolgreicheBewerbung', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<StatusSchulwechselErfolgreicheBewerbung>('de.svws_nrw.core.types.schule.StatusSchulwechselErfolgreicheBewerbung');

}

export function cast_de_svws_nrw_core_types_schule_StatusSchulwechselErfolgreicheBewerbung(obj: unknown): StatusSchulwechselErfolgreicheBewerbung {
	return obj as StatusSchulwechselErfolgreicheBewerbung;
}
