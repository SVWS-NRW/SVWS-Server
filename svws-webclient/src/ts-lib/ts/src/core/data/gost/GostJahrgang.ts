import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class GostJahrgang extends JavaObject {

	public abiturjahr : number = -1;

	public jahrgang : String | null = null;

	public bezeichnung : String | null = "Allgemein / Vorlage";

	public istAbgeschlossen : boolean = false;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostJahrgang'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostJahrgang {
		const obj = JSON.parse(json);
		const result = new GostJahrgang();
		if (typeof obj.abiturjahr === "undefined")
			 throw new Error('invalid json format, missing attribute abiturjahr');
		result.abiturjahr = obj.abiturjahr;
		result.jahrgang = typeof obj.jahrgang === "undefined" ? null : obj.jahrgang === null ? null : String(obj.jahrgang);
		result.bezeichnung = typeof obj.bezeichnung === "undefined" ? null : obj.bezeichnung === null ? null : String(obj.bezeichnung);
		if (typeof obj.istAbgeschlossen === "undefined")
			 throw new Error('invalid json format, missing attribute istAbgeschlossen');
		result.istAbgeschlossen = obj.istAbgeschlossen;
		return result;
	}

	public static transpilerToJSON(obj : GostJahrgang) : string {
		let result = '{';
		result += '"abiturjahr" : ' + obj.abiturjahr + ',';
		result += '"jahrgang" : ' + ((!obj.jahrgang) ? 'null' : '"' + obj.jahrgang.valueOf() + '"') + ',';
		result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung.valueOf() + '"') + ',';
		result += '"istAbgeschlossen" : ' + obj.istAbgeschlossen + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostJahrgang>) : string {
		let result = '{';
		if (typeof obj.abiturjahr !== "undefined") {
			result += '"abiturjahr" : ' + obj.abiturjahr + ',';
		}
		if (typeof obj.jahrgang !== "undefined") {
			result += '"jahrgang" : ' + ((!obj.jahrgang) ? 'null' : '"' + obj.jahrgang.valueOf() + '"') + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung.valueOf() + '"') + ',';
		}
		if (typeof obj.istAbgeschlossen !== "undefined") {
			result += '"istAbgeschlossen" : ' + obj.istAbgeschlossen + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_gost_GostJahrgang(obj : unknown) : GostJahrgang {
	return obj as GostJahrgang;
}
