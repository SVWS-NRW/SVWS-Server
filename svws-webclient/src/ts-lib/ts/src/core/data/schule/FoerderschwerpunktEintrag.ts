import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class FoerderschwerpunktEintrag extends JavaObject {

	public id : number = 0;

	public kuerzel : String = "";

	public text : String = "";

	public kuerzelStatistik : String = "";

	public istSichtbar : boolean = false;

	public istAenderbar : boolean = false;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schule.FoerderschwerpunktEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): FoerderschwerpunktEintrag {
		const obj = JSON.parse(json);
		const result = new FoerderschwerpunktEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = String(obj.kuerzel);
		if (typeof obj.text === "undefined")
			 throw new Error('invalid json format, missing attribute text');
		result.text = String(obj.text);
		if (typeof obj.kuerzelStatistik === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzelStatistik');
		result.kuerzelStatistik = String(obj.kuerzelStatistik);
		if (typeof obj.istSichtbar === "undefined")
			 throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		if (typeof obj.istAenderbar === "undefined")
			 throw new Error('invalid json format, missing attribute istAenderbar');
		result.istAenderbar = obj.istAenderbar;
		return result;
	}

	public static transpilerToJSON(obj : FoerderschwerpunktEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		result += '"text" : ' + '"' + obj.text.valueOf() + '"' + ',';
		result += '"kuerzelStatistik" : ' + '"' + obj.kuerzelStatistik.valueOf() + '"' + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		result += '"istAenderbar" : ' + obj.istAenderbar + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<FoerderschwerpunktEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		}
		if (typeof obj.text !== "undefined") {
			result += '"text" : ' + '"' + obj.text.valueOf() + '"' + ',';
		}
		if (typeof obj.kuerzelStatistik !== "undefined") {
			result += '"kuerzelStatistik" : ' + '"' + obj.kuerzelStatistik.valueOf() + '"' + ',';
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

export function cast_de_nrw_schule_svws_core_data_schule_FoerderschwerpunktEintrag(obj : unknown) : FoerderschwerpunktEintrag {
	return obj as FoerderschwerpunktEintrag;
}
