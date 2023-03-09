import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { ENMFloskel, cast_de_nrw_schule_svws_core_data_enm_ENMFloskel } from '../../../core/data/enm/ENMFloskel';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class ENMFloskelgruppe extends JavaObject {

	/**
	 * Das Kürzel der Floskelgruppe, z. B. AL1, AL2 oder ASV. 
	 */
	public kuerzel : string | null = null;

	/**
	 * Die textuelle Bezeichnung der Floskelgruppe, z. B. Allgemeine Floskeln oder Floskeln zum Arbeits- und Sozialverhalten. 
	 */
	public bezeichnung : string | null = null;

	/**
	 * Die Hauptgruppe für Floskeln. Diese kann bei mehreren Floskelgruppen auftreten und fasst diese ggf. nochmals zusammen (z.B. ALLG) 
	 */
	public hauptgruppe : string | null = null;

	/**
	 * Die Liste der Floskeln, die dieser Floskelgruppe zugeordnet sind. 
	 */
	public readonly floskeln : Vector<ENMFloskel> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.enm.ENMFloskelgruppe'].includes(name);
	}

	public static transpilerFromJSON(json : string): ENMFloskelgruppe {
		const obj = JSON.parse(json);
		const result = new ENMFloskelgruppe();
		result.kuerzel = typeof obj.kuerzel === "undefined" ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.bezeichnung = typeof obj.bezeichnung === "undefined" ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		result.hauptgruppe = typeof obj.hauptgruppe === "undefined" ? null : obj.hauptgruppe === null ? null : obj.hauptgruppe;
		if (!!obj.floskeln) {
			for (let elem of obj.floskeln) {
				result.floskeln?.add(ENMFloskel.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : ENMFloskelgruppe) : string {
		let result = '{';
		result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel + '"') + ',';
		result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung + '"') + ',';
		result += '"hauptgruppe" : ' + ((!obj.hauptgruppe) ? 'null' : '"' + obj.hauptgruppe + '"') + ',';
		if (!obj.floskeln) {
			result += '"floskeln" : []';
		} else {
			result += '"floskeln" : [ ';
			for (let i : number = 0; i < obj.floskeln.size(); i++) {
				let elem = obj.floskeln.get(i);
				result += ENMFloskel.transpilerToJSON(elem);
				if (i < obj.floskeln.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMFloskelgruppe>) : string {
		let result = '{';
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel + '"') + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung + '"') + ',';
		}
		if (typeof obj.hauptgruppe !== "undefined") {
			result += '"hauptgruppe" : ' + ((!obj.hauptgruppe) ? 'null' : '"' + obj.hauptgruppe + '"') + ',';
		}
		if (typeof obj.floskeln !== "undefined") {
			if (!obj.floskeln) {
				result += '"floskeln" : []';
			} else {
				result += '"floskeln" : [ ';
				for (let i : number = 0; i < obj.floskeln.size(); i++) {
					let elem = obj.floskeln.get(i);
					result += ENMFloskel.transpilerToJSON(elem);
					if (i < obj.floskeln.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_enm_ENMFloskelgruppe(obj : unknown) : ENMFloskelgruppe {
	return obj as ENMFloskelgruppe;
}
