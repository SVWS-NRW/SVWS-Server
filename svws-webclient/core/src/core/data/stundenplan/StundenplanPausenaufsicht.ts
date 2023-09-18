import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';

export class StundenplanPausenaufsicht extends JavaObject {

	/**
	 * Die ID der Pausenaufsicht
	 */
	public id : number = -1;

	/**
	 * Die ID der {@link StundenplanPausenzeit}.
	 */
	public idPausenzeit : number = -1;

	/**
	 * Die ID des {@link StundenplanLehrer} der Aufsicht führt.
	 */
	public idLehrer : number = -1;

	/**
	 * Der Wochen-Typ bei der Unterscheidung von (A,B,... -Wochen -> 1, 2, ...) oder 0
	 */
	public wochentyp : number = -1;

	/**
	 * Die IDs der {@link StundenplanAufsichtsbereich}, in denen in dieser Pausenzeit von dem {@link StundenplanLehrer} Aufsicht geführt wird.
	 */
	public bereiche : List<number> = new ArrayList();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsicht'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanPausenaufsicht {
		const obj = JSON.parse(json);
		const result = new StundenplanPausenaufsicht();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.idPausenzeit === "undefined")
			 throw new Error('invalid json format, missing attribute idPausenzeit');
		result.idPausenzeit = obj.idPausenzeit;
		if (typeof obj.idLehrer === "undefined")
			 throw new Error('invalid json format, missing attribute idLehrer');
		result.idLehrer = obj.idLehrer;
		if (typeof obj.wochentyp === "undefined")
			 throw new Error('invalid json format, missing attribute wochentyp');
		result.wochentyp = obj.wochentyp;
		if ((obj.bereiche !== undefined) && (obj.bereiche !== null)) {
			for (const elem of obj.bereiche) {
				result.bereiche?.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : StundenplanPausenaufsicht) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idPausenzeit" : ' + obj.idPausenzeit + ',';
		result += '"idLehrer" : ' + obj.idLehrer + ',';
		result += '"wochentyp" : ' + obj.wochentyp + ',';
		if (!obj.bereiche) {
			result += '"bereiche" : []';
		} else {
			result += '"bereiche" : [ ';
			for (let i = 0; i < obj.bereiche.size(); i++) {
				const elem = obj.bereiche.get(i);
				result += elem;
				if (i < obj.bereiche.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanPausenaufsicht>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.idPausenzeit !== "undefined") {
			result += '"idPausenzeit" : ' + obj.idPausenzeit + ',';
		}
		if (typeof obj.idLehrer !== "undefined") {
			result += '"idLehrer" : ' + obj.idLehrer + ',';
		}
		if (typeof obj.wochentyp !== "undefined") {
			result += '"wochentyp" : ' + obj.wochentyp + ',';
		}
		if (typeof obj.bereiche !== "undefined") {
			if (!obj.bereiche) {
				result += '"bereiche" : []';
			} else {
				result += '"bereiche" : [ ';
				for (let i = 0; i < obj.bereiche.size(); i++) {
					const elem = obj.bereiche.get(i);
					result += elem;
					if (i < obj.bereiche.size() - 1)
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

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanPausenaufsicht(obj : unknown) : StundenplanPausenaufsicht {
	return obj as StundenplanPausenaufsicht;
}
