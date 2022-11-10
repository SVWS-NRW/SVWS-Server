import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class FaecherListeEintrag extends JavaObject {

	public id : number = 0;

	public kuerzel : String | null = null;

	public kuerzelStatistik : String | null = null;

	public bezeichnung : String | null = null;

	public sortierung : number = 0;

	public istOberstufenFach : boolean = false;

	public istSichtbar : boolean = false;

	public istAenderbar : boolean = false;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.fach.FaecherListeEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): FaecherListeEintrag {
		const obj = JSON.parse(json);
		const result = new FaecherListeEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = typeof obj.kuerzel === "undefined" ? null : obj.kuerzel === null ? null : String(obj.kuerzel);
		result.kuerzelStatistik = typeof obj.kuerzelStatistik === "undefined" ? null : obj.kuerzelStatistik === null ? null : String(obj.kuerzelStatistik);
		result.bezeichnung = typeof obj.bezeichnung === "undefined" ? null : obj.bezeichnung === null ? null : String(obj.bezeichnung);
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (typeof obj.istOberstufenFach === "undefined")
			 throw new Error('invalid json format, missing attribute istOberstufenFach');
		result.istOberstufenFach = obj.istOberstufenFach;
		if (typeof obj.istSichtbar === "undefined")
			 throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		if (typeof obj.istAenderbar === "undefined")
			 throw new Error('invalid json format, missing attribute istAenderbar');
		result.istAenderbar = obj.istAenderbar;
		return result;
	}

	public static transpilerToJSON(obj : FaecherListeEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel.valueOf() + '"') + ',';
		result += '"kuerzelStatistik" : ' + ((!obj.kuerzelStatistik) ? 'null' : '"' + obj.kuerzelStatistik.valueOf() + '"') + ',';
		result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung.valueOf() + '"') + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		result += '"istOberstufenFach" : ' + obj.istOberstufenFach + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		result += '"istAenderbar" : ' + obj.istAenderbar + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<FaecherListeEintrag>) : string {
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
		if (typeof obj.istOberstufenFach !== "undefined") {
			result += '"istOberstufenFach" : ' + obj.istOberstufenFach + ',';
		}
		if (typeof obj.istSichtbar !== "undefined") {
			result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		}
		if (typeof obj.istAenderbar !== "undefined") {
			result += '"istAenderbar" : ' + obj.istAenderbar + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_fach_FaecherListeEintrag(obj : unknown) : FaecherListeEintrag {
	return obj as FaecherListeEintrag;
}
