import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvSchuelerImportOptions extends JavaObject {

	/**
	 * Die ID des Schuljahresabschnitts, aus dem importiert werden soll.
	 */
	public idSchuljahresabschnitt: number = -1;

	/**
	 *  Gibt an, wie mit Jahrgängen umgegangen werden soll.
	 *  Gibt an, ob der Folgejahrgang des Quell-Jahrgangs verwendet werden soll.
	 */
	public folgejahrgang: boolean = false;

	/**
	 *  Gibt an, wie mit Klassenzuweisungen umgegangen werden soll.
	 *  Gibt an, ob Klassenzuweisungen übernommen werden sollen.
	 */
	public klassenzuweisungenUebernehmen: boolean = true;

	/**
	 *  Legt fest, ob beim Import in den Folgejahrgang die Versetzungsvermerke
	 *  und die individuelle Folgeklasse eines Schülers berücksichtigt werden.
	 */
	public versetzungsvermerkeBeruecksichtigen: boolean = true;

	/**
	 * Gibt an, ob fehlende UV-Klassen im Ziel-Planungsabschnitt automatisch angelegt werden sollen.
	 */
	public createMissingKlassen: boolean = false;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvSchuelerImportOptions';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvSchuelerImportOptions'].includes(name);
	}

	public static readonly class = new Class<UvSchuelerImportOptions>('de.svws_nrw.core.data.uv.UvSchuelerImportOptions');

	public static transpilerFromJSON(json: string): UvSchuelerImportOptions {
		const obj = JSON.parse(json) as Partial<UvSchuelerImportOptions>;
		const result = new UvSchuelerImportOptions();
		if (obj.idSchuljahresabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		if (obj.folgejahrgang === undefined)
			throw new Error('invalid json format, missing attribute folgejahrgang');
		result.folgejahrgang = obj.folgejahrgang;
		if (obj.klassenzuweisungenUebernehmen === undefined)
			throw new Error('invalid json format, missing attribute klassenzuweisungenUebernehmen');
		result.klassenzuweisungenUebernehmen = obj.klassenzuweisungenUebernehmen;
		if (obj.versetzungsvermerkeBeruecksichtigen === undefined)
			throw new Error('invalid json format, missing attribute versetzungsvermerkeBeruecksichtigen');
		result.versetzungsvermerkeBeruecksichtigen = obj.versetzungsvermerkeBeruecksichtigen;
		if (obj.createMissingKlassen === undefined)
			throw new Error('invalid json format, missing attribute createMissingKlassen');
		result.createMissingKlassen = obj.createMissingKlassen;
		return result;
	}

	public static transpilerToJSON(obj: UvSchuelerImportOptions): string {
		let result = '{';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		result += '"folgejahrgang" : ' + obj.folgejahrgang.toString() + ',';
		result += '"klassenzuweisungenUebernehmen" : ' + obj.klassenzuweisungenUebernehmen.toString() + ',';
		result += '"versetzungsvermerkeBeruecksichtigen" : ' + obj.versetzungsvermerkeBeruecksichtigen.toString() + ',';
		result += '"createMissingKlassen" : ' + obj.createMissingKlassen.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvSchuelerImportOptions>): string {
		let result = '{';
		if (obj.idSchuljahresabschnitt !== undefined) {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		}
		if (obj.folgejahrgang !== undefined) {
			result += '"folgejahrgang" : ' + obj.folgejahrgang.toString() + ',';
		}
		if (obj.klassenzuweisungenUebernehmen !== undefined) {
			result += '"klassenzuweisungenUebernehmen" : ' + obj.klassenzuweisungenUebernehmen.toString() + ',';
		}
		if (obj.versetzungsvermerkeBeruecksichtigen !== undefined) {
			result += '"versetzungsvermerkeBeruecksichtigen" : ' + obj.versetzungsvermerkeBeruecksichtigen.toString() + ',';
		}
		if (obj.createMissingKlassen !== undefined) {
			result += '"createMissingKlassen" : ' + obj.createMissingKlassen.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvSchuelerImportOptions(obj: unknown): UvSchuelerImportOptions {
	return obj as UvSchuelerImportOptions;
}
