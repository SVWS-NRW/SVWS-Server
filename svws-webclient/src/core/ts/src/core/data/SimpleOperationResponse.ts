import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { List, cast_java_util_List } from '../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../java/util/Vector';

export class SimpleOperationResponse extends JavaObject {

	/**
	 * Gibt an, ob die Operation erfolgreich war. 
	 */
	public success : boolean = false;

	/**
	 * Das Log der Operation. 
	 */
	public log : List<string | null> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.SimpleOperationResponse'].includes(name);
	}

	public static transpilerFromJSON(json : string): SimpleOperationResponse {
		const obj = JSON.parse(json);
		const result = new SimpleOperationResponse();
		if (typeof obj.success === "undefined")
			 throw new Error('invalid json format, missing attribute success');
		result.success = obj.success;
		if (!!obj.log) {
			for (let elem of obj.log) {
				result.log?.add(elem === null ? null : elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SimpleOperationResponse) : string {
		let result = '{';
		result += '"success" : ' + obj.success + ',';
		if (!obj.log) {
			result += '"log" : []';
		} else {
			result += '"log" : [ ';
			for (let i : number = 0; i < obj.log.size(); i++) {
				let elem = obj.log.get(i);
				result += (elem == null) ? null : '"' + elem + '"';
				if (i < obj.log.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SimpleOperationResponse>) : string {
		let result = '{';
		if (typeof obj.success !== "undefined") {
			result += '"success" : ' + obj.success + ',';
		}
		if (typeof obj.log !== "undefined") {
			if (!obj.log) {
				result += '"log" : []';
			} else {
				result += '"log" : [ ';
				for (let i : number = 0; i < obj.log.size(); i++) {
					let elem = obj.log.get(i);
					result += (elem == null) ? null : '"' + elem + '"';
					if (i < obj.log.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_SimpleOperationResponse(obj : unknown) : SimpleOperationResponse {
	return obj as SimpleOperationResponse;
}
