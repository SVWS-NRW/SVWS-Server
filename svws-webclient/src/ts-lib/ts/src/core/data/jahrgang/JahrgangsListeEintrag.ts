import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class JahrgangsListeEintrag extends JavaObject {

	public id : number = 0;

	public kuerzel : String | null = null;

	public kuerzelStatistik : String | null = null;

	public bezeichnung : String | null = null;

	public sortierung : number = 0;

	public idSchulgliederung : String | null = null;

	public idFolgejahrgang : Number | null = null;

	public istSichtbar : boolean = false;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.jahrgang.JahrgangsListeEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): JahrgangsListeEintrag {
		const obj = JSON.parse(json);
		const result = new JahrgangsListeEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = typeof obj.kuerzel === "undefined" ? null : obj.kuerzel;
		result.kuerzelStatistik = typeof obj.kuerzelStatistik === "undefined" ? null : obj.kuerzelStatistik;
		result.bezeichnung = typeof obj.bezeichnung === "undefined" ? null : obj.bezeichnung;
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		result.idSchulgliederung = typeof obj.idSchulgliederung === "undefined" ? null : obj.idSchulgliederung;
		result.idFolgejahrgang = typeof obj.idFolgejahrgang === "undefined" ? null : obj.idFolgejahrgang;
		if (typeof obj.istSichtbar === "undefined")
			 throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		return result;
	}

	public static transpilerToJSON(obj : JahrgangsListeEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel.valueOf() + '"') + ',';
		result += '"kuerzelStatistik" : ' + ((!obj.kuerzelStatistik) ? 'null' : '"' + obj.kuerzelStatistik.valueOf() + '"') + ',';
		result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung.valueOf() + '"') + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		result += '"idSchulgliederung" : ' + ((!obj.idSchulgliederung) ? 'null' : '"' + obj.idSchulgliederung.valueOf() + '"') + ',';
		result += '"idFolgejahrgang" : ' + ((!obj.idFolgejahrgang) ? 'null' : obj.idFolgejahrgang.valueOf()) + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<JahrgangsListeEintrag>) : string {
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
		if (typeof obj.idSchulgliederung !== "undefined") {
			result += '"idSchulgliederung" : ' + ((!obj.idSchulgliederung) ? 'null' : '"' + obj.idSchulgliederung.valueOf() + '"') + ',';
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

export function cast_de_nrw_schule_svws_core_data_jahrgang_JahrgangsListeEintrag(obj : unknown) : JahrgangsListeEintrag {
	return obj as JahrgangsListeEintrag;
}
