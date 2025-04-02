import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class LehrerEinwilligung extends JavaObject {

	/**
	 * Die ID des zugehörigen Lehrers.
	 */
	public idLehrer : number = 0;

	/**
	 * Die ID der Einwilligungsart.
	 */
	public idEinwilligungsart : number = 0;

	/**
	 * Der Status der Einwilligung (erteilt/nicht erteilt).
	 */
	public istZugestimmt : boolean = false;

	/**
	 * Der Status der Abfrage der Einwilligung (abgefragt/nicht abgefragt).
	 */
	public istAbgefragt : boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.lehrer.LehrerEinwilligung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.lehrer.LehrerEinwilligung'].includes(name);
	}

	public static class = new Class<LehrerEinwilligung>('de.svws_nrw.core.data.lehrer.LehrerEinwilligung');

	public static transpilerFromJSON(json : string): LehrerEinwilligung {
		const obj = JSON.parse(json) as Partial<LehrerEinwilligung>;
		const result = new LehrerEinwilligung();
		if (obj.idLehrer === undefined)
			throw new Error('invalid json format, missing attribute idLehrer');
		result.idLehrer = obj.idLehrer;
		if (obj.idEinwilligungsart === undefined)
			throw new Error('invalid json format, missing attribute idEinwilligungsart');
		result.idEinwilligungsart = obj.idEinwilligungsart;
		if (obj.istZugestimmt === undefined)
			throw new Error('invalid json format, missing attribute istZugestimmt');
		result.istZugestimmt = obj.istZugestimmt;
		if (obj.istAbgefragt === undefined)
			throw new Error('invalid json format, missing attribute istAbgefragt');
		result.istAbgefragt = obj.istAbgefragt;
		return result;
	}

	public static transpilerToJSON(obj : LehrerEinwilligung) : string {
		let result = '{';
		result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		result += '"idEinwilligungsart" : ' + obj.idEinwilligungsart.toString() + ',';
		result += '"istZugestimmt" : ' + obj.istZugestimmt.toString() + ',';
		result += '"istAbgefragt" : ' + obj.istAbgefragt.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LehrerEinwilligung>) : string {
		let result = '{';
		if (obj.idLehrer !== undefined) {
			result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		}
		if (obj.idEinwilligungsart !== undefined) {
			result += '"idEinwilligungsart" : ' + obj.idEinwilligungsart.toString() + ',';
		}
		if (obj.istZugestimmt !== undefined) {
			result += '"istZugestimmt" : ' + obj.istZugestimmt.toString() + ',';
		}
		if (obj.istAbgefragt !== undefined) {
			result += '"istAbgefragt" : ' + obj.istAbgefragt.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_lehrer_LehrerEinwilligung(obj : unknown) : LehrerEinwilligung {
	return obj as LehrerEinwilligung;
}
