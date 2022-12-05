import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class BenutzergruppeDaten extends JavaObject {

	public id : number = -1;

	public bezeichnung : String = "";

	public istAdmin : boolean = false;

	public kompetenzen : Vector<Number> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.benutzer.BenutzergruppeDaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): BenutzergruppeDaten {
		const obj = JSON.parse(json);
		const result = new BenutzergruppeDaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = String(obj.bezeichnung);
		if (typeof obj.istAdmin === "undefined")
			 throw new Error('invalid json format, missing attribute istAdmin');
		result.istAdmin = obj.istAdmin;
		if (!!obj.kompetenzen) {
			for (let elem of obj.kompetenzen) {
				result.kompetenzen?.add(Number(elem));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : BenutzergruppeDaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"bezeichnung" : ' + '"' + obj.bezeichnung.valueOf() + '"' + ',';
		result += '"istAdmin" : ' + obj.istAdmin + ',';
		if (!obj.kompetenzen) {
			result += '"kompetenzen" : []';
		} else {
			result += '"kompetenzen" : [ ';
			for (let i : number = 0; i < obj.kompetenzen.size(); i++) {
				let elem = obj.kompetenzen.get(i);
				result += elem;
				if (i < obj.kompetenzen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BenutzergruppeDaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + '"' + obj.bezeichnung.valueOf() + '"' + ',';
		}
		if (typeof obj.istAdmin !== "undefined") {
			result += '"istAdmin" : ' + obj.istAdmin + ',';
		}
		if (typeof obj.kompetenzen !== "undefined") {
			if (!obj.kompetenzen) {
				result += '"kompetenzen" : []';
			} else {
				result += '"kompetenzen" : [ ';
				for (let i : number = 0; i < obj.kompetenzen.size(); i++) {
					let elem = obj.kompetenzen.get(i);
					result += elem;
					if (i < obj.kompetenzen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_benutzer_BenutzergruppeDaten(obj : unknown) : BenutzergruppeDaten {
	return obj as BenutzergruppeDaten;
}
