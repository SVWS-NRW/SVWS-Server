import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class ReligionEintrag extends JavaObject {

	public id : number = -1;

	public text : String | null = "";

	public textZeugnis : String | null = "";

	public kuerzel : String | null = "";

	public sortierung : number = 1;

	public istSichtbar : boolean = true;

	public istAenderbar : boolean = true;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schule.ReligionEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): ReligionEintrag {
		const obj = JSON.parse(json);
		const result = new ReligionEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.text = typeof obj.text === "undefined" ? null : obj.text === null ? null : String(obj.text);
		result.textZeugnis = typeof obj.textZeugnis === "undefined" ? null : obj.textZeugnis === null ? null : String(obj.textZeugnis);
		result.kuerzel = typeof obj.kuerzel === "undefined" ? null : obj.kuerzel === null ? null : String(obj.kuerzel);
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (typeof obj.istSichtbar === "undefined")
			 throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		if (typeof obj.istAenderbar === "undefined")
			 throw new Error('invalid json format, missing attribute istAenderbar');
		result.istAenderbar = obj.istAenderbar;
		return result;
	}

	public static transpilerToJSON(obj : ReligionEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"text" : ' + ((!obj.text) ? 'null' : '"' + obj.text.valueOf() + '"') + ',';
		result += '"textZeugnis" : ' + ((!obj.textZeugnis) ? 'null' : '"' + obj.textZeugnis.valueOf() + '"') + ',';
		result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel.valueOf() + '"') + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		result += '"istAenderbar" : ' + obj.istAenderbar + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ReligionEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.text !== "undefined") {
			result += '"text" : ' + ((!obj.text) ? 'null' : '"' + obj.text.valueOf() + '"') + ',';
		}
		if (typeof obj.textZeugnis !== "undefined") {
			result += '"textZeugnis" : ' + ((!obj.textZeugnis) ? 'null' : '"' + obj.textZeugnis.valueOf() + '"') + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel.valueOf() + '"') + ',';
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + obj.sortierung + ',';
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

export function cast_de_nrw_schule_svws_core_data_schule_ReligionEintrag(obj : unknown) : ReligionEintrag {
	return obj as ReligionEintrag;
}
