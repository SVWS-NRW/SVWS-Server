import { JavaObject } from '../../../java/lang/JavaObject';

export class GEAbschlussFach extends JavaObject {

	/**
	 * Das Kürzel des Faches
	 */
	public kuerzel : string = "";

	/**
	 * Die Bezeichnung des Faches
	 */
	public bezeichnung : string | null = null;

	/**
	 * Die Note, die in dem Fach erteilt wurde
	 */
	public note : number = -1;

	/**
	 * Gibt an, ob das Fach eine Fremdsprache ist oder nicht
	 */
	public istFremdsprache : boolean | null = false;

	/**
	 * Gibt die Art der Leistungsdifferenzierung bei dem Fach an: E-Kurs, G-Kurs oder sonstiger Kurs
	 */
	public kursart : string | null = "";

	/**
	 * Gibt an, ob das Fach als Ausgleich genutzt wurde oder nicht.
	 */
	public ausgleich : boolean | null = false;

	/**
	 * Gibt an, ob in diesem Fach ein Defizit ausgeglichen wurde.
	 */
	public ausgeglichen : boolean | null = false;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.abschluss.GEAbschlussFach'].includes(name);
	}

	public static transpilerFromJSON(json : string): GEAbschlussFach {
		const obj = JSON.parse(json);
		const result = new GEAbschlussFach();
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		result.bezeichnung = typeof obj.bezeichnung === "undefined" ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		if (typeof obj.note === "undefined")
			 throw new Error('invalid json format, missing attribute note');
		result.note = obj.note;
		result.istFremdsprache = typeof obj.istFremdsprache === "undefined" ? null : obj.istFremdsprache === null ? null : obj.istFremdsprache;
		result.kursart = typeof obj.kursart === "undefined" ? null : obj.kursart === null ? null : obj.kursart;
		result.ausgleich = typeof obj.ausgleich === "undefined" ? null : obj.ausgleich === null ? null : obj.ausgleich;
		result.ausgeglichen = typeof obj.ausgeglichen === "undefined" ? null : obj.ausgeglichen === null ? null : obj.ausgeglichen;
		return result;
	}

	public static transpilerToJSON(obj : GEAbschlussFach) : string {
		let result = '{';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel!) + ',';
		result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result += '"note" : ' + obj.note + ',';
		result += '"istFremdsprache" : ' + ((!obj.istFremdsprache) ? 'null' : obj.istFremdsprache) + ',';
		result += '"kursart" : ' + ((!obj.kursart) ? 'null' : JSON.stringify(obj.kursart)) + ',';
		result += '"ausgleich" : ' + ((!obj.ausgleich) ? 'null' : obj.ausgleich) + ',';
		result += '"ausgeglichen" : ' + ((!obj.ausgeglichen) ? 'null' : obj.ausgeglichen) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GEAbschlussFach>) : string {
		let result = '{';
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel!) + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		if (typeof obj.note !== "undefined") {
			result += '"note" : ' + obj.note + ',';
		}
		if (typeof obj.istFremdsprache !== "undefined") {
			result += '"istFremdsprache" : ' + ((!obj.istFremdsprache) ? 'null' : obj.istFremdsprache) + ',';
		}
		if (typeof obj.kursart !== "undefined") {
			result += '"kursart" : ' + ((!obj.kursart) ? 'null' : JSON.stringify(obj.kursart)) + ',';
		}
		if (typeof obj.ausgleich !== "undefined") {
			result += '"ausgleich" : ' + ((!obj.ausgleich) ? 'null' : obj.ausgleich) + ',';
		}
		if (typeof obj.ausgeglichen !== "undefined") {
			result += '"ausgeglichen" : ' + ((!obj.ausgeglichen) ? 'null' : obj.ausgeglichen) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_abschluss_GEAbschlussFach(obj : unknown) : GEAbschlussFach {
	return obj as GEAbschlussFach;
}
