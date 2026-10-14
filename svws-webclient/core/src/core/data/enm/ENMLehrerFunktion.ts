import { JavaEnum } from '../../../java/lang/JavaEnum';
import { Class } from '../../../java/lang/Class';

export class ENMLehrerFunktion extends JavaEnum<ENMLehrerFunktion> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal: Array<ENMLehrerFunktion> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name: Map<string, ENMLehrerFunktion> = new Map<string, ENMLehrerFunktion>();

	/**
	 * Fachlehrer
	 */
	public static readonly FACHLEHRER: ENMLehrerFunktion = new ENMLehrerFunktion("FACHLEHRER", 0, 1, "Fachlehrer/-in");

	/**
	 * Klassenleitung
	 */
	public static readonly KLASSENLEITUNG: ENMLehrerFunktion = new ENMLehrerFunktion("KLASSENLEITUNG", 1, 2, "Klassenleitung");

	/**
	 * Abteilungsleitung
	 */
	public static readonly ABTEILUNGSLEITUNG: ENMLehrerFunktion = new ENMLehrerFunktion("ABTEILUNGSLEITUNG", 2, 3, "Abteilungsleitung");

	/**
	 * Schulleitung
	 */
	public static readonly SCHULLEITUNG: ENMLehrerFunktion = new ENMLehrerFunktion("SCHULLEITUNG", 3, 4, "Schulleitung");

	/**
	 * Notenmoduladministrator
	 */
	public static readonly NOTENMODULADMIN: ENMLehrerFunktion = new ENMLehrerFunktion("NOTENMODULADMIN", 4, 5, "Notenmoduladministrator");

	/**
	 * Eine eindeutige ID für die Lehrerfunktion
	 */
	public readonly id: number;

	/**
	 * Ein Text, welcher die Lehrerfunktion für die Visualisierung beschreibt
	 */
	public readonly text: string;

	/**
	 * Erstellt eine neue Lehrer-Funktion für das Notenmodul
	 *
	 * @param id      die eindeutige ID
	 * @param text    die textuelle Beschreibung
	 */
	private constructor(name: string, ordinal: number, id: number, text: string) {
		super(name, ordinal);
		ENMLehrerFunktion.all_values_by_ordinal.push(this);
		ENMLehrerFunktion.all_values_by_name.set(name, this);
		this.id = id;
		this.text = text;
	}

	/**
	 * Gibt die Lehrer-Funktion für die übergebene ID zurück
	 *
	 * @param id   die ID oder null
	 *
	 * @return die Lehrer-Funktion oder null, falls die ID ungültig ist
	 */
	public static fromID(id: number | null): ENMLehrerFunktion | null {
		if (id === null) {
			return null;
		}
		let _sevar_1093309407;
		const _seexpr_1093309407 = (id);
		if (_seexpr_1093309407 === 1) {
			_sevar_1093309407 = ENMLehrerFunktion.FACHLEHRER;
		} else if (_seexpr_1093309407 === 2) {
			_sevar_1093309407 = ENMLehrerFunktion.KLASSENLEITUNG;
		} else if (_seexpr_1093309407 === 3) {
			_sevar_1093309407 = ENMLehrerFunktion.ABTEILUNGSLEITUNG;
		} else if (_seexpr_1093309407 === 4) {
			_sevar_1093309407 = ENMLehrerFunktion.SCHULLEITUNG;
		} else if (_seexpr_1093309407 === 5) {
			_sevar_1093309407 = ENMLehrerFunktion.NOTENMODULADMIN;
		} else {
			_sevar_1093309407 = null;
		}
		return _sevar_1093309407;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values(): Array<ENMLehrerFunktion> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name: string): ENMLehrerFunktion | null {
		const tmp = this.all_values_by_name.get(name);
		return tmp ?? null;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMLehrerFunktion';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMLehrerFunktion', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static readonly class = new Class<ENMLehrerFunktion>('de.svws_nrw.core.data.enm.ENMLehrerFunktion');

}

export function cast_de_svws_nrw_core_data_enm_ENMLehrerFunktion(obj: unknown): ENMLehrerFunktion {
	return obj as ENMLehrerFunktion;
}
