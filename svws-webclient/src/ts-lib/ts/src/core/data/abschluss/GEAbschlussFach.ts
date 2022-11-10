import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { JavaBoolean, cast_java_lang_Boolean } from '../../../java/lang/JavaBoolean';

export class GEAbschlussFach extends JavaObject {

	public kuerzel : String = "";

	public bezeichnung : String | null = null;

	public note : number = -1;

	public istFremdsprache : Boolean | null = false;

	public kursart : String | null = "";

	public ausgleich : Boolean | null = false;

	public ausgeglichen : Boolean | null = false;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.abschluss.GEAbschlussFach'].includes(name);
	}

	public static transpilerFromJSON(json : string): GEAbschlussFach {
		const obj = JSON.parse(json);
		const result = new GEAbschlussFach();
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = String(obj.kuerzel);
		result.bezeichnung = typeof obj.bezeichnung === "undefined" ? null : obj.bezeichnung === null ? null : String(obj.bezeichnung);
		if (typeof obj.note === "undefined")
			 throw new Error('invalid json format, missing attribute note');
		result.note = obj.note;
		result.istFremdsprache = typeof obj.istFremdsprache === "undefined" ? null : obj.istFremdsprache === null ? null : Boolean(obj.istFremdsprache);
		result.kursart = typeof obj.kursart === "undefined" ? null : obj.kursart === null ? null : String(obj.kursart);
		result.ausgleich = typeof obj.ausgleich === "undefined" ? null : obj.ausgleich === null ? null : Boolean(obj.ausgleich);
		result.ausgeglichen = typeof obj.ausgeglichen === "undefined" ? null : obj.ausgeglichen === null ? null : Boolean(obj.ausgeglichen);
		return result;
	}

	public static transpilerToJSON(obj : GEAbschlussFach) : string {
		let result = '{';
		result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung.valueOf() + '"') + ',';
		result += '"note" : ' + obj.note + ',';
		result += '"istFremdsprache" : ' + ((!obj.istFremdsprache) ? 'null' : obj.istFremdsprache.valueOf()) + ',';
		result += '"kursart" : ' + ((!obj.kursart) ? 'null' : '"' + obj.kursart.valueOf() + '"') + ',';
		result += '"ausgleich" : ' + ((!obj.ausgleich) ? 'null' : obj.ausgleich.valueOf()) + ',';
		result += '"ausgeglichen" : ' + ((!obj.ausgeglichen) ? 'null' : obj.ausgeglichen.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GEAbschlussFach>) : string {
		let result = '{';
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung.valueOf() + '"') + ',';
		}
		if (typeof obj.note !== "undefined") {
			result += '"note" : ' + obj.note + ',';
		}
		if (typeof obj.istFremdsprache !== "undefined") {
			result += '"istFremdsprache" : ' + ((!obj.istFremdsprache) ? 'null' : obj.istFremdsprache.valueOf()) + ',';
		}
		if (typeof obj.kursart !== "undefined") {
			result += '"kursart" : ' + ((!obj.kursart) ? 'null' : '"' + obj.kursart.valueOf() + '"') + ',';
		}
		if (typeof obj.ausgleich !== "undefined") {
			result += '"ausgleich" : ' + ((!obj.ausgleich) ? 'null' : obj.ausgleich.valueOf()) + ',';
		}
		if (typeof obj.ausgeglichen !== "undefined") {
			result += '"ausgeglichen" : ' + ((!obj.ausgeglichen) ? 'null' : obj.ausgeglichen.valueOf()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_abschluss_GEAbschlussFach(obj : unknown) : GEAbschlussFach {
	return obj as GEAbschlussFach;
}
