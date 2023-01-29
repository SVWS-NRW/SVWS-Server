import { JavaObject, cast_java_lang_Object } from '../../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../../java/lang/JavaString';

export class Klausurvorgabe extends JavaObject {

	/**
	 * Die ID des Stundenplans. 
	 */
	public idVorgabe : number = -1;

	/**
	 * Die ID des Stundenplans. 
	 */
	public abiJahrgang : number = -1;

	/**
	 * Die ID des Stundenplans. 
	 */
	public halbjahr : number = -1;

	/**
	 * Die ID des Stundenplans. 
	 */
	public kursartAllg : string = "";

	/**
	 * Die ID des Stundenplans. 
	 */
	public quartal : number = -1;

	/**
	 * Die ID des Stundenplans. 
	 */
	public dauer : number = -1;

	/**
	 * Die ID des Stundenplans. 
	 */
	public auswahlzeit : number = -1;

	/**
	 * Die ID des Stundenplans. 
	 */
	public istMdlPruefung : boolean = false;

	/**
	 * Die ID des Stundenplans. 
	 */
	public istAudioNotwendig : boolean = false;

	/**
	 * Die ID des Stundenplans. 
	 */
	public istVideoNotwendig : boolean = false;

	/**
	 * Die ID des Stundenplans. 
	 */
	public bemerkungVorgabe : string | null = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.klausuren.Klausurvorgabe'].includes(name);
	}

	public static transpilerFromJSON(json : string): Klausurvorgabe {
		const obj = JSON.parse(json);
		const result = new Klausurvorgabe();
		if (typeof obj.idVorgabe === "undefined")
			 throw new Error('invalid json format, missing attribute idVorgabe');
		result.idVorgabe = obj.idVorgabe;
		if (typeof obj.abiJahrgang === "undefined")
			 throw new Error('invalid json format, missing attribute abiJahrgang');
		result.abiJahrgang = obj.abiJahrgang;
		if (typeof obj.halbjahr === "undefined")
			 throw new Error('invalid json format, missing attribute halbjahr');
		result.halbjahr = obj.halbjahr;
		if (typeof obj.kursartAllg === "undefined")
			 throw new Error('invalid json format, missing attribute kursartAllg');
		result.kursartAllg = obj.kursartAllg;
		if (typeof obj.quartal === "undefined")
			 throw new Error('invalid json format, missing attribute quartal');
		result.quartal = obj.quartal;
		if (typeof obj.dauer === "undefined")
			 throw new Error('invalid json format, missing attribute dauer');
		result.dauer = obj.dauer;
		if (typeof obj.auswahlzeit === "undefined")
			 throw new Error('invalid json format, missing attribute auswahlzeit');
		result.auswahlzeit = obj.auswahlzeit;
		if (typeof obj.istMdlPruefung === "undefined")
			 throw new Error('invalid json format, missing attribute istMdlPruefung');
		result.istMdlPruefung = obj.istMdlPruefung;
		if (typeof obj.istAudioNotwendig === "undefined")
			 throw new Error('invalid json format, missing attribute istAudioNotwendig');
		result.istAudioNotwendig = obj.istAudioNotwendig;
		if (typeof obj.istVideoNotwendig === "undefined")
			 throw new Error('invalid json format, missing attribute istVideoNotwendig');
		result.istVideoNotwendig = obj.istVideoNotwendig;
		result.bemerkungVorgabe = typeof obj.bemerkungVorgabe === "undefined" ? null : obj.bemerkungVorgabe === null ? null : obj.bemerkungVorgabe;
		return result;
	}

	public static transpilerToJSON(obj : Klausurvorgabe) : string {
		let result = '{';
		result += '"idVorgabe" : ' + obj.idVorgabe + ',';
		result += '"abiJahrgang" : ' + obj.abiJahrgang + ',';
		result += '"halbjahr" : ' + obj.halbjahr + ',';
		result += '"kursartAllg" : ' + '"' + obj.kursartAllg! + '"' + ',';
		result += '"quartal" : ' + obj.quartal + ',';
		result += '"dauer" : ' + obj.dauer + ',';
		result += '"auswahlzeit" : ' + obj.auswahlzeit + ',';
		result += '"istMdlPruefung" : ' + obj.istMdlPruefung + ',';
		result += '"istAudioNotwendig" : ' + obj.istAudioNotwendig + ',';
		result += '"istVideoNotwendig" : ' + obj.istVideoNotwendig + ',';
		result += '"bemerkungVorgabe" : ' + ((!obj.bemerkungVorgabe) ? 'null' : '"' + obj.bemerkungVorgabe + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Klausurvorgabe>) : string {
		let result = '{';
		if (typeof obj.idVorgabe !== "undefined") {
			result += '"idVorgabe" : ' + obj.idVorgabe + ',';
		}
		if (typeof obj.abiJahrgang !== "undefined") {
			result += '"abiJahrgang" : ' + obj.abiJahrgang + ',';
		}
		if (typeof obj.halbjahr !== "undefined") {
			result += '"halbjahr" : ' + obj.halbjahr + ',';
		}
		if (typeof obj.kursartAllg !== "undefined") {
			result += '"kursartAllg" : ' + '"' + obj.kursartAllg + '"' + ',';
		}
		if (typeof obj.quartal !== "undefined") {
			result += '"quartal" : ' + obj.quartal + ',';
		}
		if (typeof obj.dauer !== "undefined") {
			result += '"dauer" : ' + obj.dauer + ',';
		}
		if (typeof obj.auswahlzeit !== "undefined") {
			result += '"auswahlzeit" : ' + obj.auswahlzeit + ',';
		}
		if (typeof obj.istMdlPruefung !== "undefined") {
			result += '"istMdlPruefung" : ' + obj.istMdlPruefung + ',';
		}
		if (typeof obj.istAudioNotwendig !== "undefined") {
			result += '"istAudioNotwendig" : ' + obj.istAudioNotwendig + ',';
		}
		if (typeof obj.istVideoNotwendig !== "undefined") {
			result += '"istVideoNotwendig" : ' + obj.istVideoNotwendig + ',';
		}
		if (typeof obj.bemerkungVorgabe !== "undefined") {
			result += '"bemerkungVorgabe" : ' + ((!obj.bemerkungVorgabe) ? 'null' : '"' + obj.bemerkungVorgabe + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_gost_klausuren_Klausurvorgabe(obj : unknown) : Klausurvorgabe {
	return obj as Klausurvorgabe;
}
