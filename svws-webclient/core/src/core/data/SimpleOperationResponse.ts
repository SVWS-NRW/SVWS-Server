import { JavaObject } from '../../java/lang/JavaObject';
import { ArrayList } from '../../java/util/ArrayList';
import type { List } from '../../java/util/List';
import { Class } from '../../java/lang/Class';

export class SimpleOperationResponse extends JavaObject {

	/**
	 * ID des zugehörigen Objektes.
	 */
	public id : number | null = null;

	/**
	 * Gibt an, ob die Operation erfolgreich war.
	 */
	public success : boolean = false;

	/**
	 * Das Log der Operation.
	 */
	public log : List<string> = new ArrayList<string>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.SimpleOperationResponse';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.SimpleOperationResponse'].includes(name);
	}

	public static class = new Class<SimpleOperationResponse>('de.svws_nrw.core.data.SimpleOperationResponse');

	public static transpilerFromJSON(json : string): SimpleOperationResponse {
		const obj = JSON.parse(json) as Partial<SimpleOperationResponse>;
		const result = new SimpleOperationResponse();
		result.id = (obj.id === undefined) ? null : obj.id === null ? null : obj.id;
		if (obj.success === undefined)
			throw new Error('invalid json format, missing attribute success');
		result.success = obj.success;
		if (obj.log !== undefined) {
			for (const elem of obj.log) {
				result.log.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SimpleOperationResponse) : string {
		let result = '{';
		result += '"id" : ' + ((obj.id === null) ? 'null' : obj.id.toString()) + ',';
		result += '"success" : ' + obj.success.toString() + ',';
		result += '"log" : [ ';
		for (let i = 0; i < obj.log.size(); i++) {
			const elem = obj.log.get(i);
			result += '"' + elem + '"';
			if (i < obj.log.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SimpleOperationResponse>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + ((obj.id === null) ? 'null' : obj.id.toString()) + ',';
		}
		if (obj.success !== undefined) {
			result += '"success" : ' + obj.success.toString() + ',';
		}
		if (obj.log !== undefined) {
			result += '"log" : [ ';
			for (let i = 0; i < obj.log.size(); i++) {
				const elem = obj.log.get(i);
				result += '"' + elem + '"';
				if (i < obj.log.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_SimpleOperationResponse(obj : unknown) : SimpleOperationResponse {
	return obj as SimpleOperationResponse;
}
