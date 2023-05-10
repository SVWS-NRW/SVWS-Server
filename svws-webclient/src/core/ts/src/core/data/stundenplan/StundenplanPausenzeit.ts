import { JavaObject } from '../../../java/lang/JavaObject';

export class StundenplanPausenzeit extends JavaObject {

	/**
	 * Die ID der Pausenzeit.
	 */
	public id : number = -1;

	/**
	 * Der Wochentag für die Pausenzeit (1=Montag, 2=Dienstag, ..., 7=Sonntag)
	 */
	public wochentag : number = -1;

	/**
	 * Die Uhrzeit, wann die Pause beginnt.
	 */
	public beginn : string = "";

	/**
	 * Die Uhrzeit, wann die Pause endet.
	 */
	public ende : string = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanPausenzeit'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanPausenzeit {
		const obj = JSON.parse(json);
		const result = new StundenplanPausenzeit();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.wochentag === "undefined")
			 throw new Error('invalid json format, missing attribute wochentag');
		result.wochentag = obj.wochentag;
		if (typeof obj.beginn === "undefined")
			 throw new Error('invalid json format, missing attribute beginn');
		result.beginn = obj.beginn;
		if (typeof obj.ende === "undefined")
			 throw new Error('invalid json format, missing attribute ende');
		result.ende = obj.ende;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanPausenzeit) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"wochentag" : ' + obj.wochentag + ',';
		result += '"beginn" : ' + '"' + obj.beginn! + '"' + ',';
		result += '"ende" : ' + '"' + obj.ende! + '"' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanPausenzeit>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.wochentag !== "undefined") {
			result += '"wochentag" : ' + obj.wochentag + ',';
		}
		if (typeof obj.beginn !== "undefined") {
			result += '"beginn" : ' + '"' + obj.beginn + '"' + ',';
		}
		if (typeof obj.ende !== "undefined") {
			result += '"ende" : ' + '"' + obj.ende + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanPausenzeit(obj : unknown) : StundenplanPausenzeit {
	return obj as StundenplanPausenzeit;
}
