import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class OrtKatalogEintrag extends JavaObject {

	public id : number = 0;

	public plz : String | null = null;

	public ortsname : String | null = null;

	public kreis : String | null = null;

	public kuerzelBundesland : String | null = null;

	public sortierung : number = 0;

	public istSichtbar : boolean = false;

	public istAenderbar : boolean = false;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kataloge.OrtKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): OrtKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new OrtKatalogEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.plz = typeof obj.plz === "undefined" ? null : obj.plz;
		result.ortsname = typeof obj.ortsname === "undefined" ? null : obj.ortsname;
		result.kreis = typeof obj.kreis === "undefined" ? null : obj.kreis;
		result.kuerzelBundesland = typeof obj.kuerzelBundesland === "undefined" ? null : obj.kuerzelBundesland;
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

	public static transpilerToJSON(obj : OrtKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"plz" : ' + ((!obj.plz) ? 'null' : '"' + obj.plz.valueOf() + '"') + ',';
		result += '"ortsname" : ' + ((!obj.ortsname) ? 'null' : '"' + obj.ortsname.valueOf() + '"') + ',';
		result += '"kreis" : ' + ((!obj.kreis) ? 'null' : '"' + obj.kreis.valueOf() + '"') + ',';
		result += '"kuerzelBundesland" : ' + ((!obj.kuerzelBundesland) ? 'null' : '"' + obj.kuerzelBundesland.valueOf() + '"') + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		result += '"istAenderbar" : ' + obj.istAenderbar + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<OrtKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.plz !== "undefined") {
			result += '"plz" : ' + ((!obj.plz) ? 'null' : '"' + obj.plz.valueOf() + '"') + ',';
		}
		if (typeof obj.ortsname !== "undefined") {
			result += '"ortsname" : ' + ((!obj.ortsname) ? 'null' : '"' + obj.ortsname.valueOf() + '"') + ',';
		}
		if (typeof obj.kreis !== "undefined") {
			result += '"kreis" : ' + ((!obj.kreis) ? 'null' : '"' + obj.kreis.valueOf() + '"') + ',';
		}
		if (typeof obj.kuerzelBundesland !== "undefined") {
			result += '"kuerzelBundesland" : ' + ((!obj.kuerzelBundesland) ? 'null' : '"' + obj.kuerzelBundesland.valueOf() + '"') + ',';
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

export function cast_de_nrw_schule_svws_core_data_kataloge_OrtKatalogEintrag(obj : unknown) : OrtKatalogEintrag {
	return obj as OrtKatalogEintrag;
}
