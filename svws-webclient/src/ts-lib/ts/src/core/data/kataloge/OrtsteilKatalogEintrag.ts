import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class OrtsteilKatalogEintrag extends JavaObject {

	public id : number = 0;

	public ort_id : Number | null = null;

	public ortsteil : String | null = null;

	public sortierung : number = 0;

	public istSichtbar : boolean = false;

	public istAenderbar : boolean = false;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kataloge.OrtsteilKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): OrtsteilKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new OrtsteilKatalogEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.ort_id = typeof obj.ort_id === "undefined" ? null : obj.ort_id;
		result.ortsteil = typeof obj.ortsteil === "undefined" ? null : obj.ortsteil;
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

	public static transpilerToJSON(obj : OrtsteilKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"ort_id" : ' + ((!obj.ort_id) ? 'null' : obj.ort_id.valueOf()) + ',';
		result += '"ortsteil" : ' + ((!obj.ortsteil) ? 'null' : '"' + obj.ortsteil.valueOf() + '"') + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		result += '"istAenderbar" : ' + obj.istAenderbar + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<OrtsteilKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.ort_id !== "undefined") {
			result += '"ort_id" : ' + ((!obj.ort_id) ? 'null' : obj.ort_id.valueOf()) + ',';
		}
		if (typeof obj.ortsteil !== "undefined") {
			result += '"ortsteil" : ' + ((!obj.ortsteil) ? 'null' : '"' + obj.ortsteil.valueOf() + '"') + ',';
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

export function cast_de_nrw_schule_svws_core_data_kataloge_OrtsteilKatalogEintrag(obj : unknown) : OrtsteilKatalogEintrag {
	return obj as OrtsteilKatalogEintrag;
}
