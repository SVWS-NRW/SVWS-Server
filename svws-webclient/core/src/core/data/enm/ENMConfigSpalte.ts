import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class ENMConfigSpalte extends JavaObject {

	/**
	 * Die ID dieser Teilleistung in der SVWS-DB, sofern es sich um eine Teilleistung handelt.
	 */
	public idTeilleistung: number | null = null;

	/**
	 * Der Name der Spalte
	 */
	public name: string = "";

	/**
	 * Gibt an, ob die Spalte angezeigt werden soll oder nicht.
	 */
	public anzeigen: boolean = true;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMConfigSpalte';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMConfigSpalte'].includes(name);
	}

	public static readonly class = new Class<ENMConfigSpalte>('de.svws_nrw.core.data.enm.ENMConfigSpalte');

	public static transpilerFromJSON(json: string): ENMConfigSpalte {
		const obj = JSON.parse(json) as Partial<ENMConfigSpalte>;
		const result = new ENMConfigSpalte();
		result.idTeilleistung = (obj.idTeilleistung === undefined) ? null : obj.idTeilleistung === null ? null : obj.idTeilleistung;
		if (obj.name === undefined)
			throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (obj.anzeigen === undefined)
			throw new Error('invalid json format, missing attribute anzeigen');
		result.anzeigen = obj.anzeigen;
		return result;
	}

	public static transpilerToJSON(obj: ENMConfigSpalte): string {
		let result = '{';
		result += '"idTeilleistung" : ' + ((obj.idTeilleistung === null) ? 'null' : obj.idTeilleistung.toString()) + ',';
		result += '"name" : ' + JSON.stringify(obj.name) + ',';
		result += '"anzeigen" : ' + obj.anzeigen.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ENMConfigSpalte>): string {
		let result = '{';
		if (obj.idTeilleistung !== undefined) {
			result += '"idTeilleistung" : ' + ((obj.idTeilleistung === null) ? 'null' : obj.idTeilleistung.toString()) + ',';
		}
		if (obj.name !== undefined) {
			result += '"name" : ' + JSON.stringify(obj.name) + ',';
		}
		if (obj.anzeigen !== undefined) {
			result += '"anzeigen" : ' + obj.anzeigen.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMConfigSpalte(obj: unknown): ENMConfigSpalte {
	return obj as ENMConfigSpalte;
}
