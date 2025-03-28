import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

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
	 * Der Wochentyp, der der Kalenderwoche zugeordnet ist (z.B. eine A- bzw. B-Wochen, d.h. 1 bzw. 2). Muss größer als 0 sein.
	 */
	public wochentyp : number = 0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplan.StundenplanKalenderwochenzuordnung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanKalenderwochenzuordnung'].includes(name);
	}

	public static class = new Class<StundenplanKalenderwochenzuordnung>('de.svws_nrw.core.data.stundenplan.StundenplanKalenderwochenzuordnung');

	public static transpilerFromJSON(json : string): StundenplanKalenderwochenzuordnung {
		const obj = JSON.parse(json) as Partial<StundenplanKalenderwochenzuordnung>;
		const result = new StundenplanKalenderwochenzuordnung();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.jahr === undefined)
			throw new Error('invalid json format, missing attribute jahr');
		result.jahr = obj.jahr;
		if (obj.kw === undefined)
			throw new Error('invalid json format, missing attribute kw');
		result.kw = obj.kw;
		if (obj.wochentyp === undefined)
			throw new Error('invalid json format, missing attribute wochentyp');
		result.wochentyp = obj.wochentyp;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanKalenderwochenzuordnung) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"jahr" : ' + obj.jahr.toString() + ',';
		result += '"kw" : ' + obj.kw.toString() + ',';
		result += '"wochentyp" : ' + obj.wochentyp.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanKalenderwochenzuordnung>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.jahr !== undefined) {
			result += '"jahr" : ' + obj.jahr.toString() + ',';
		}
		if (obj.kw !== undefined) {
			result += '"kw" : ' + obj.kw.toString() + ',';
		}
		if (obj.wochentyp !== undefined) {
			result += '"wochentyp" : ' + obj.wochentyp.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanKalenderwochenzuordnung(obj : unknown) : StundenplanKalenderwochenzuordnung {
	return obj as StundenplanKalenderwochenzuordnung;
}
