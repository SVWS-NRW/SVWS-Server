import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class ENMConfigKlasseSpalte extends JavaObject {

	/**
	 * Die ID dieser Teilleistung in der SVWS-DB, sofern es sich um eine Teilleistung handelt.
	 */
	public idTeilleistung: number | null = null;

	/**
	 * Der Name der Spalte
	 */
	public name: string = "";

	/**
	 * Gibt an, ob die Spalte gesperrt werden soll oder nicht.
	 */
	public gesperrt: boolean = true;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMConfigKlasseSpalte';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMConfigKlasseSpalte'].includes(name);
	}

	public static readonly class = new Class<ENMConfigKlasseSpalte>('de.svws_nrw.core.data.enm.ENMConfigKlasseSpalte');

	public static transpilerFromJSON(json: string): ENMConfigKlasseSpalte {
		const obj = JSON.parse(json) as Partial<ENMConfigKlasseSpalte>;
		const result = new ENMConfigKlasseSpalte();
		result.idTeilleistung = (obj.idTeilleistung === undefined) ? null : obj.idTeilleistung === null ? null : obj.idTeilleistung;
		if (obj.name === undefined)
			throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (obj.gesperrt === undefined)
			throw new Error('invalid json format, missing attribute gesperrt');
		result.gesperrt = obj.gesperrt;
		return result;
	}

	public static transpilerToJSON(obj: ENMConfigKlasseSpalte): string {
		let result = '{';
		result += '"idTeilleistung" : ' + ((obj.idTeilleistung === null) ? 'null' : obj.idTeilleistung.toString()) + ',';
		result += '"name" : ' + JSON.stringify(obj.name) + ',';
		result += '"gesperrt" : ' + obj.gesperrt.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ENMConfigKlasseSpalte>): string {
		let result = '{';
		if (obj.idTeilleistung !== undefined) {
			result += '"idTeilleistung" : ' + ((obj.idTeilleistung === null) ? 'null' : obj.idTeilleistung.toString()) + ',';
		}
		if (obj.name !== undefined) {
			result += '"name" : ' + JSON.stringify(obj.name) + ',';
		}
		if (obj.gesperrt !== undefined) {
			result += '"gesperrt" : ' + obj.gesperrt.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMConfigKlasseSpalte(obj: unknown): ENMConfigKlasseSpalte {
	return obj as ENMConfigKlasseSpalte;
}
