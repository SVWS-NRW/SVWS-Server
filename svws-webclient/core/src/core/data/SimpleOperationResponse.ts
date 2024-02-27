import { JavaObject } from '../../java/lang/JavaObject';
import { ArrayList } from '../../java/util/ArrayList';
import type { List } from '../../java/util/List';

export class SimpleOperationResponse extends JavaObject {

	/**
	 * Gibt an, ob die Operation erfolgreich war.
	 */
	public success : boolean = false;

	/**
	 * Das Log der Operation.
	 */
	public log : List<string | null> = new ArrayList();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.SimpleOperationResponse';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.SimpleOperationResponse'].includes(name);
	}

	public static transpilerFromJSON(json : string): SimpleOperationResponse {
		const obj = JSON.parse(json);
		const result = new SimpleOperationResponse();
		if (typeof obj.success === "undefined")
			 throw new Error('invalid json format, missing attribute success');
		result.success = obj.success;
		if ((obj.log !== undefined) && (obj.log !== null)) {
			for (const elem of obj.log) {
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
			for (let i = 0; i < obj.log.size(); i++) {
				const elem = obj.log.get(i);
				result += (elem === null) ? null : '"' + elem + '"';
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
				for (let i = 0; i < obj.log.size(); i++) {
					const elem = obj.log.get(i);
					result += (elem === null) ? null : '"' + elem + '"';
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

export function cast_de_svws_nrw_core_data_SimpleOperationResponse(obj : unknown) : SimpleOperationResponse {
	return obj as SimpleOperationResponse;
}
