import { JavaObject } from '../../../java/lang/JavaObject';

export class StundenplanPausenzeit extends JavaObject {

	/**
	 * Die ID der Pausenzeit.
	 */
	public id : number = -1;

	/**
	 * Der {@link Wochentag} für die Pausenzeit (1=Montag, 2=Dienstag, ..., 7=Sonntag)
	 */
	public wochentag : number = 1;

	/**
	 * Die Uhrzeit in Minuten seit 0 Uhr, wann die Pause beginnt. NULL bedeutet "noch nicht definiert".
	 */
	public beginn : number | null = null;

	/**
	 * Die Uhrzeit in Minuten seit 0 Uhr, wann die Pause endet. NULL bedeutet "noch nicht definiert".
	 */
	public ende : number | null = null;

	/**
	 * Die Bezeichnung der Pausenzeit, welche die Art der Pausenzeit genauer beschreibt (z.B. Mittagspause).
	 */
	public bezeichnung : string = "Pause";


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
		result.beginn = typeof obj.beginn === "undefined" ? null : obj.beginn === null ? null : obj.beginn;
		result.ende = typeof obj.ende === "undefined" ? null : obj.ende === null ? null : obj.ende;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanPausenzeit) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"wochentag" : ' + obj.wochentag + ',';
		result += '"beginn" : ' + ((!obj.beginn) ? 'null' : obj.beginn) + ',';
		result += '"ende" : ' + ((!obj.ende) ? 'null' : obj.ende) + ',';
		result += '"bezeichnung" : ' + '"' + obj.bezeichnung! + '"' + ',';
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
			result += '"beginn" : ' + ((!obj.beginn) ? 'null' : obj.beginn) + ',';
		}
		if (typeof obj.ende !== "undefined") {
			result += '"ende" : ' + ((!obj.ende) ? 'null' : obj.ende) + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + '"' + obj.bezeichnung + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanPausenzeit(obj : unknown) : StundenplanPausenzeit {
	return obj as StundenplanPausenzeit;
}
