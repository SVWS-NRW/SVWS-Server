import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class JahrgangsDaten extends JavaObject {

	public id : number = 0;

	public kuerzel : String | null = null;

	public kuerzelStatistik : String | null = null;

	public bezeichnung : String | null = null;

	public sortierung : number = 0;

	public kuerzelSchulgliederung : String | null = null;

	public idFolgejahrgang : Number | null = null;

	public istSichtbar : boolean = false;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.jahrgang.JahrgangsDaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): JahrgangsDaten {
		const obj = JSON.parse(json);
		const result = new JahrgangsDaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = typeof obj.kuerzel === "undefined" ? null : obj.kuerzel === null ? null : String(obj.kuerzel);
		result.kuerzelStatistik = typeof obj.kuerzelStatistik === "undefined" ? null : obj.kuerzelStatistik === null ? null : String(obj.kuerzelStatistik);
		result.bezeichnung = typeof obj.bezeichnung === "undefined" ? null : obj.bezeichnung === null ? null : String(obj.bezeichnung);
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		result.kuerzelSchulgliederung = typeof obj.kuerzelSchulgliederung === "undefined" ? null : obj.kuerzelSchulgliederung === null ? null : String(obj.kuerzelSchulgliederung);
		result.idFolgejahrgang = typeof obj.idFolgejahrgang === "undefined" ? null : obj.idFolgejahrgang === null ? null : Number(obj.idFolgejahrgang);
		if (typeof obj.istSichtbar === "undefined")
			 throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		return result;
	}

	public static transpilerToJSON(obj : JahrgangsDaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel.valueOf() + '"') + ',';
		result += '"kuerzelStatistik" : ' + ((!obj.kuerzelStatistik) ? 'null' : '"' + obj.kuerzelStatistik.valueOf() + '"') + ',';
		result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung.valueOf() + '"') + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		result += '"kuerzelSchulgliederung" : ' + ((!obj.kuerzelSchulgliederung) ? 'null' : '"' + obj.kuerzelSchulgliederung.valueOf() + '"') + ',';
		result += '"idFolgejahrgang" : ' + ((!obj.idFolgejahrgang) ? 'null' : obj.idFolgejahrgang.valueOf()) + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<JahrgangsDaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel.valueOf() + '"') + ',';
		}
		if (typeof obj.kuerzelStatistik !== "undefined") {
			result += '"kuerzelStatistik" : ' + ((!obj.kuerzelStatistik) ? 'null' : '"' + obj.kuerzelStatistik.valueOf() + '"') + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung.valueOf() + '"') + ',';
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + obj.sortierung + ',';
		}
		if (typeof obj.kuerzelSchulgliederung !== "undefined") {
			result += '"kuerzelSchulgliederung" : ' + ((!obj.kuerzelSchulgliederung) ? 'null' : '"' + obj.kuerzelSchulgliederung.valueOf() + '"') + ',';
		}
		if (typeof obj.idFolgejahrgang !== "undefined") {
			result += '"idFolgejahrgang" : ' + ((!obj.idFolgejahrgang) ? 'null' : obj.idFolgejahrgang.valueOf()) + ',';
		}
		if (typeof obj.istSichtbar !== "undefined") {
			result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_jahrgang_JahrgangsDaten(obj : unknown) : JahrgangsDaten {
	return obj as JahrgangsDaten;
}
