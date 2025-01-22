import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ENMServerConfig } from '../../../core/data/enm/ENMServerConfig';

export class ENMConfigResponse extends JavaObject {

	/**
	 * ID des zugehörigen Objektes.
	 */
	public config : ENMServerConfig | null = null;

	/**
	 * Gibt an, ob die Operation erfolgreich war.
	 */
	public success : boolean = false;

	/**
	 * Das Log der Operation.
	 */
	public log : List<string> = new ArrayList<string>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMConfigResponse';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMConfigResponse'].includes(name);
	}

	public static class = new Class<ENMConfigResponse>('de.svws_nrw.core.data.enm.ENMConfigResponse');

	public static transpilerFromJSON(json : string): ENMConfigResponse {
		const obj = JSON.parse(json) as Partial<ENMConfigResponse>;
		const result = new ENMConfigResponse();
		result.config = ((obj.config === undefined) || (obj.config === null)) ? null : ENMServerConfig.transpilerFromJSON(JSON.stringify(obj.config));
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

	public static transpilerToJSON(obj : ENMConfigResponse) : string {
		let result = '{';
		result += '"config" : ' + ((obj.config === null) ? 'null' : ENMServerConfig.transpilerToJSON(obj.config)) + ',';
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

	public static transpilerToJSONPatch(obj : Partial<ENMConfigResponse>) : string {
		let result = '{';
		if (obj.config !== undefined) {
			result += '"config" : ' + ((obj.config === null) ? 'null' : ENMServerConfig.transpilerToJSON(obj.config)) + ',';
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

export function cast_de_svws_nrw_core_data_enm_ENMConfigResponse(obj : unknown) : ENMConfigResponse {
	return obj as ENMConfigResponse;
}
