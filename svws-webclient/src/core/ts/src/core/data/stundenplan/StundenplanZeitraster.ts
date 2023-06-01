import { JavaObject } from '../../../java/lang/JavaObject';

export class StundenplanZeitraster extends JavaObject {

	/**
	 * Die ID des Zeitraster-Eintrages.
	 */
	public id : number = -1;

	/**
	 * Der {@link Wochentag} an dem der Unterricht stattfindet (1=Montag, 2=Dienstag, ..., 7=Sonntag)
	 */
	public wochentag : number = 1;

	/**
	 * Die Nummer der Unterrichtsstunde an dem Wochentag
	 */
	public unterrichtstunde : number = 1;

	/**
	 * Die Uhrzeit in Minuten seit 0 Uhr, wann die Unterrichtsstunde beginnt. NULL bedeutet "noch nicht definiert".
	 */
	public stundenbeginn : number | null = null;

	/**
	 * Die Uhrzeit in Minuten seit 0 Uhr, wann die Unterrichtsstunde endet. NULL bedeutet "noch nicht definiert".
	 */
	public stundenende : number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanZeitraster'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanZeitraster {
		const obj = JSON.parse(json);
		const result = new StundenplanZeitraster();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.wochentag === "undefined")
			 throw new Error('invalid json format, missing attribute wochentag');
		result.wochentag = obj.wochentag;
		if (typeof obj.unterrichtstunde === "undefined")
			 throw new Error('invalid json format, missing attribute unterrichtstunde');
		result.unterrichtstunde = obj.unterrichtstunde;
		result.stundenbeginn = typeof obj.stundenbeginn === "undefined" ? null : obj.stundenbeginn === null ? null : obj.stundenbeginn;
		result.stundenende = typeof obj.stundenende === "undefined" ? null : obj.stundenende === null ? null : obj.stundenende;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanZeitraster) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"wochentag" : ' + obj.wochentag + ',';
		result += '"unterrichtstunde" : ' + obj.unterrichtstunde + ',';
		result += '"stundenbeginn" : ' + ((!obj.stundenbeginn) ? 'null' : obj.stundenbeginn) + ',';
		result += '"stundenende" : ' + ((!obj.stundenende) ? 'null' : obj.stundenende) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanZeitraster>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.wochentag !== "undefined") {
			result += '"wochentag" : ' + obj.wochentag + ',';
		}
		if (typeof obj.unterrichtstunde !== "undefined") {
			result += '"unterrichtstunde" : ' + obj.unterrichtstunde + ',';
		}
		if (typeof obj.stundenbeginn !== "undefined") {
			result += '"stundenbeginn" : ' + ((!obj.stundenbeginn) ? 'null' : obj.stundenbeginn) + ',';
		}
		if (typeof obj.stundenende !== "undefined") {
			result += '"stundenende" : ' + ((!obj.stundenende) ? 'null' : obj.stundenende) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanZeitraster(obj : unknown) : StundenplanZeitraster {
	return obj as StundenplanZeitraster;
}
