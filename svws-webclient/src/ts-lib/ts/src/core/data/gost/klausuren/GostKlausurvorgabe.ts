import { JavaObject, cast_java_lang_Object } from '../../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../../java/lang/JavaString';

export class GostKlausurvorgabe extends JavaObject {

	/**
	 * Die ID der Klausurvorgabe. 
	 */
	public idVorgabe : number = -1;

	/**
	 * Das Jahr, in welchem der Jahrgang Abitur machen wird, -1 für die Vorlage. 
	 */
	public abiJahrgang : number = -1;

	/**
	 * Das Gost-Halbjahr, in dem die Klausurg geschrieben wird. 
	 */
	public halbjahr : number = -1;

	/**
	 * Das Quartal, in welchem die Klausur gechrieben wird. 
	 */
	public quartal : number = -1;

	/**
	 * Die ID des Faches. 
	 */
	public idFach : number = -1;

	/**
	 * Das Kürzel einer verallgemeinerten Kursart. 
	 */
	public kursartAllg : string = "";

	/**
	 * Die Dauer der Klausur in Minuten. 
	 */
	public dauer : number = -1;

	/**
	 * Die Auswahlzeit in Minuten, sofern vorhanden. 
	 */
	public auswahlzeit : number = -1;

	/**
	 * Die Information, ob es sich um eine mündliche Prüfung handelt. 
	 */
	public istMdlPruefung : boolean = false;

	/**
	 * Die Information, ob Audioequipment nötig ist, z.B. für Klasuren mit Hörverstehensanteilen. 
	 */
	public istAudioNotwendig : boolean = false;

	/**
	 * Die Information, ob Videoequipment nötig ist, z.B. für Klasuren mit Videoanalyse. 
	 */
	public istVideoNotwendig : boolean = false;

	/**
	 * Die textuelle Bemerkung zur Klausurvorgabe, sofern vorhanden. 
	 */
	public bemerkungVorgabe : string | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.klausuren.GostKlausurvorgabe'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostKlausurvorgabe {
		const obj = JSON.parse(json);
		const result = new GostKlausurvorgabe();
		if (typeof obj.idVorgabe === "undefined")
			 throw new Error('invalid json format, missing attribute idVorgabe');
		result.idVorgabe = obj.idVorgabe;
		if (typeof obj.abiJahrgang === "undefined")
			 throw new Error('invalid json format, missing attribute abiJahrgang');
		result.abiJahrgang = obj.abiJahrgang;
		if (typeof obj.halbjahr === "undefined")
			 throw new Error('invalid json format, missing attribute halbjahr');
		result.halbjahr = obj.halbjahr;
		if (typeof obj.quartal === "undefined")
			 throw new Error('invalid json format, missing attribute quartal');
		result.quartal = obj.quartal;
		if (typeof obj.idFach === "undefined")
			 throw new Error('invalid json format, missing attribute idFach');
		result.idFach = obj.idFach;
		if (typeof obj.kursartAllg === "undefined")
			 throw new Error('invalid json format, missing attribute kursartAllg');
		result.kursartAllg = obj.kursartAllg;
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

	public static transpilerToJSON(obj : GostKlausurvorgabe) : string {
		let result = '{';
		result += '"idVorgabe" : ' + obj.idVorgabe + ',';
		result += '"abiJahrgang" : ' + obj.abiJahrgang + ',';
		result += '"halbjahr" : ' + obj.halbjahr + ',';
		result += '"quartal" : ' + obj.quartal + ',';
		result += '"idFach" : ' + obj.idFach + ',';
		result += '"kursartAllg" : ' + '"' + obj.kursartAllg! + '"' + ',';
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

	public static transpilerToJSONPatch(obj : Partial<GostKlausurvorgabe>) : string {
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
		if (typeof obj.quartal !== "undefined") {
			result += '"quartal" : ' + obj.quartal + ',';
		}
		if (typeof obj.idFach !== "undefined") {
			result += '"idFach" : ' + obj.idFach + ',';
		}
		if (typeof obj.kursartAllg !== "undefined") {
			result += '"kursartAllg" : ' + '"' + obj.kursartAllg + '"' + ',';
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

export function cast_de_nrw_schule_svws_core_data_gost_klausuren_GostKlausurvorgabe(obj : unknown) : GostKlausurvorgabe {
	return obj as GostKlausurvorgabe;
}
