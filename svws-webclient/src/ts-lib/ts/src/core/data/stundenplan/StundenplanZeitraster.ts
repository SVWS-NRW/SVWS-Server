import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class StundenplanZeitraster extends JavaObject {

	public id : number = -1;

	public wochentag : number = -1;

	public unterrichtstunde : number = -1;

	public stundenbeginn : String = "";

	public stundenende : String = "";


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 * 
	 * @param id                 die ID
	 * @param wochentag          der Wochentag an dem der Unterricht stattfindet (1=Montag, 2=Dienstag, ..., 7=Sonntag)
	 * @param unterrichtstunde   die Nummer der Unterrichtsstunde an dem Wochentag
	 * @param stundenbeginn      die Uhrzeit, wann die Unterrichtsstunde beginnt
	 * @param stundenende        die Uhrzeit, wann die Unterrichtsstunde endet
	 */
	public constructor(id : number, wochentag : number, unterrichtstunde : number, stundenbeginn : String, stundenende : String);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : number, __param2? : number, __param3? : String, __param4? : String) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined")) {
			} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && typeof __param1 === "number") && ((typeof __param2 !== "undefined") && typeof __param2 === "number") && ((typeof __param3 !== "undefined") && ((__param3 instanceof String) || (typeof __param3 === "string"))) && ((typeof __param4 !== "undefined") && ((__param4 instanceof String) || (typeof __param4 === "string")))) {
			let id : number = __param0 as number;
			let wochentag : number = __param1 as number;
			let unterrichtstunde : number = __param2 as number;
			let stundenbeginn : String = __param3;
			let stundenende : String = __param4;
			this.id = id;
			this.wochentag = wochentag;
			this.unterrichtstunde = unterrichtstunde;
			this.stundenbeginn = stundenbeginn;
			this.stundenende = stundenende;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.stundenplan.StundenplanZeitraster'].includes(name);
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
		if (typeof obj.stundenbeginn === "undefined")
			 throw new Error('invalid json format, missing attribute stundenbeginn');
		result.stundenbeginn = String(obj.stundenbeginn);
		if (typeof obj.stundenende === "undefined")
			 throw new Error('invalid json format, missing attribute stundenende');
		result.stundenende = String(obj.stundenende);
		return result;
	}

	public static transpilerToJSON(obj : StundenplanZeitraster) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"wochentag" : ' + obj.wochentag + ',';
		result += '"unterrichtstunde" : ' + obj.unterrichtstunde + ',';
		result += '"stundenbeginn" : ' + '"' + obj.stundenbeginn.valueOf() + '"' + ',';
		result += '"stundenende" : ' + '"' + obj.stundenende.valueOf() + '"' + ',';
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
			result += '"stundenbeginn" : ' + '"' + obj.stundenbeginn.valueOf() + '"' + ',';
		}
		if (typeof obj.stundenende !== "undefined") {
			result += '"stundenende" : ' + '"' + obj.stundenende.valueOf() + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_stundenplan_StundenplanZeitraster(obj : unknown) : StundenplanZeitraster {
	return obj as StundenplanZeitraster;
}
