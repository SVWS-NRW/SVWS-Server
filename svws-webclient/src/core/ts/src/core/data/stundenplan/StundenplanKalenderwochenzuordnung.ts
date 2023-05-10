import { JavaObject } from '../../../java/lang/JavaObject';

export class StundenplanKalenderwochenzuordnung extends JavaObject {

	/**
	 * Die ID der Kalenderwochenzuordnung.
	 */
	public id : number = -1;

	/**
	 * Das Kalenderjahr der Zuordnung
	 */
	public jahr : number = -1;

	/**
	 * Die Kalenderwoche in dem Jahr.
	 */
	public kw : number = -1;

	/**
	 * Der Wochentyp, der der Kalenderwoche zugeordnet ist (z.B. eine A- bzw. B-Wochen, d.h. 1 bzw. 2).
	 */
	public wochentyp : number = 0;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanKalenderwochenzuordnung'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanKalenderwochenzuordnung {
		const obj = JSON.parse(json);
		const result = new StundenplanKalenderwochenzuordnung();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.jahr === "undefined")
			 throw new Error('invalid json format, missing attribute jahr');
		result.jahr = obj.jahr;
		if (typeof obj.kw === "undefined")
			 throw new Error('invalid json format, missing attribute kw');
		result.kw = obj.kw;
		if (typeof obj.wochentyp === "undefined")
			 throw new Error('invalid json format, missing attribute wochentyp');
		result.wochentyp = obj.wochentyp;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanKalenderwochenzuordnung) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"jahr" : ' + obj.jahr + ',';
		result += '"kw" : ' + obj.kw + ',';
		result += '"wochentyp" : ' + obj.wochentyp + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanKalenderwochenzuordnung>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.jahr !== "undefined") {
			result += '"jahr" : ' + obj.jahr + ',';
		}
		if (typeof obj.kw !== "undefined") {
			result += '"kw" : ' + obj.kw + ',';
		}
		if (typeof obj.wochentyp !== "undefined") {
			result += '"wochentyp" : ' + obj.wochentyp + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanKalenderwochenzuordnung(obj : unknown) : StundenplanKalenderwochenzuordnung {
	return obj as StundenplanKalenderwochenzuordnung;
}
