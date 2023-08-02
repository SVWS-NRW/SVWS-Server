import { JavaObject } from '../../../java/lang/JavaObject';
import { ENMFloskel, cast_de_svws_nrw_core_data_enm_ENMFloskel } from '../../../core/data/enm/ENMFloskel';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';

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
	public readonly floskeln : List<ENMFloskel> = new ArrayList();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMFloskelgruppe'].includes(name);
	}

	public static transpilerFromJSON(json : string): ENMFloskelgruppe {
		const obj = JSON.parse(json);
		const result = new ENMFloskelgruppe();
		result.kuerzel = typeof obj.kuerzel === "undefined" ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.bezeichnung = typeof obj.bezeichnung === "undefined" ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		result.hauptgruppe = typeof obj.hauptgruppe === "undefined" ? null : obj.hauptgruppe === null ? null : obj.hauptgruppe;
		if ((obj.floskeln !== undefined) && (obj.floskeln !== null)) {
			for (const elem of obj.floskeln) {
				result.floskeln?.add(ENMFloskel.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : ENMFloskelgruppe) : string {
		let result = '{';
		result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result += '"hauptgruppe" : ' + ((!obj.hauptgruppe) ? 'null' : JSON.stringify(obj.hauptgruppe)) + ',';
		if (!obj.floskeln) {
			result += '"floskeln" : []';
		} else {
			result += '"floskeln" : [ ';
			for (let i = 0; i < obj.floskeln.size(); i++) {
				const elem = obj.floskeln.get(i);
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
			result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		if (typeof obj.hauptgruppe !== "undefined") {
			result += '"hauptgruppe" : ' + ((!obj.hauptgruppe) ? 'null' : JSON.stringify(obj.hauptgruppe)) + ',';
		}
		if (typeof obj.floskeln !== "undefined") {
			if (!obj.floskeln) {
				result += '"floskeln" : []';
			} else {
				result += '"floskeln" : [ ';
				for (let i = 0; i < obj.floskeln.size(); i++) {
					const elem = obj.floskeln.get(i);
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

export function cast_de_svws_nrw_core_data_enm_ENMFloskelgruppe(obj : unknown) : ENMFloskelgruppe {
	return obj as ENMFloskelgruppe;
}
