import { JavaObject } from '../../../java/lang/JavaObject';

export class StundenplanZeitraster extends JavaObject {

	/**
	 * Die ID des Zeitraster-Eintrages.
	 */
	public id : number = -1;

	/**
	 * Der {@link Wochentag} an dem der Unterricht stattfindet (1=Montag, 2=Dienstag, ..., 7=Sonntag)
	 */
	public wochentag : number = -1;

	/**
	 * Die Nummer der Unterrichtsstunde an dem Wochentag
	 */
	public unterrichtstunde : number = -1;

	/**
	 * Die Uhrzeit, wann die Unterrichtsstunde beginnt.
	 */
	public stundenbeginn : string = "";

	/**
	 * Die Uhrzeit, wann die Unterrichtsstunde endet.
	 */
	public stundenende : string = "";


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
	public constructor(id : number, wochentag : number, unterrichtstunde : number, stundenbeginn : string, stundenende : string);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : number, __param2? : number, __param3? : string, __param4? : string) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined")) {
			// empty block
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && typeof __param1 === "number") && ((typeof __param2 !== "undefined") && typeof __param2 === "number") && ((typeof __param3 !== "undefined") && (typeof __param3 === "string")) && ((typeof __param4 !== "undefined") && (typeof __param4 === "string"))) {
			const id : number = __param0 as number;
			const wochentag : number = __param1 as number;
			const unterrichtstunde : number = __param2 as number;
			const stundenbeginn : string = __param3;
			const stundenende : string = __param4;
			this.id = id;
			this.wochentag = wochentag;
			this.unterrichtstunde = unterrichtstunde;
			this.stundenbeginn = stundenbeginn;
			this.stundenende = stundenende;
		} else throw new Error('invalid method overload');
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
		if (typeof obj.stundenbeginn === "undefined")
			 throw new Error('invalid json format, missing attribute stundenbeginn');
		result.stundenbeginn = obj.stundenbeginn;
		if (typeof obj.stundenende === "undefined")
			 throw new Error('invalid json format, missing attribute stundenende');
		result.stundenende = obj.stundenende;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanZeitraster) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"wochentag" : ' + obj.wochentag + ',';
		result += '"unterrichtstunde" : ' + obj.unterrichtstunde + ',';
		result += '"stundenbeginn" : ' + '"' + obj.stundenbeginn! + '"' + ',';
		result += '"stundenende" : ' + '"' + obj.stundenende! + '"' + ',';
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
			result += '"stundenbeginn" : ' + '"' + obj.stundenbeginn + '"' + ',';
		}
		if (typeof obj.stundenende !== "undefined") {
			result += '"stundenende" : ' + '"' + obj.stundenende + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanZeitraster(obj : unknown) : StundenplanZeitraster {
	return obj as StundenplanZeitraster;
}
