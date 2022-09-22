import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class SchulformGliederungJahrgaenge extends JavaObject {

	public schulform : String = "";

	public gliederung : String | null = null;

	public jahrgaenge : List<String> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schule.SchulformGliederungJahrgaenge'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchulformGliederungJahrgaenge {
		const obj = JSON.parse(json);
		const result = new SchulformGliederungJahrgaenge();
		if (typeof obj.schulform === "undefined")
			 throw new Error('invalid json format, missing attribute schulform');
		result.schulform = obj.schulform;
		result.gliederung = typeof obj.gliederung === "undefined" ? null : obj.gliederung;
		if (!!obj.jahrgaenge) {
			for (let elem of obj.jahrgaenge) {
				result.jahrgaenge?.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchulformGliederungJahrgaenge) : string {
		let result = '{';
		result += '"schulform" : ' + '"' + obj.schulform.valueOf() + '"' + ',';
		result += '"gliederung" : ' + ((!obj.gliederung) ? 'null' : '"' + obj.gliederung.valueOf() + '"') + ',';
		if (!obj.jahrgaenge) {
			result += '"jahrgaenge" : []';
		} else {
			result += '"jahrgaenge" : [ ';
			for (let i : number = 0; i < obj.jahrgaenge.size(); i++) {
				let elem = obj.jahrgaenge.get(i);
				result += '"' + elem + '"';
				if (i < obj.jahrgaenge.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchulformGliederungJahrgaenge>) : string {
		let result = '{';
		if (typeof obj.schulform !== "undefined") {
			result += '"schulform" : ' + '"' + obj.schulform.valueOf() + '"' + ',';
		}
		if (typeof obj.gliederung !== "undefined") {
			result += '"gliederung" : ' + ((!obj.gliederung) ? 'null' : '"' + obj.gliederung.valueOf() + '"') + ',';
		}
		if (typeof obj.jahrgaenge !== "undefined") {
			if (!obj.jahrgaenge) {
				result += '"jahrgaenge" : []';
			} else {
				result += '"jahrgaenge" : [ ';
				for (let i : number = 0; i < obj.jahrgaenge.size(); i++) {
					let elem = obj.jahrgaenge.get(i);
					result += '"' + elem + '"';
					if (i < obj.jahrgaenge.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_schule_SchulformGliederungJahrgaenge(obj : unknown) : SchulformGliederungJahrgaenge {
	return obj as SchulformGliederungJahrgaenge;
}
