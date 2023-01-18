import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';

export class GostFachwahl extends JavaObject {

	/**
	 * Die ID des Faches 
	 */
	public fachID : number = -1;

	/**
	 * Die ID des Schülers 
	 */
	public schuelerID : number = -1;

	/**
	 * Die ID der Kursart 
	 */
	public kursartID : number = -1;

	/**
	 * Gibt an, ob die Fachwahl ein schriftlicher Kurs ist oder nicht 
	 */
	public istSchriftlich : boolean = false;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostFachwahl'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostFachwahl {
		const obj = JSON.parse(json);
		const result = new GostFachwahl();
		if (typeof obj.fachID === "undefined")
			 throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		if (typeof obj.schuelerID === "undefined")
			 throw new Error('invalid json format, missing attribute schuelerID');
		result.schuelerID = obj.schuelerID;
		if (typeof obj.kursartID === "undefined")
			 throw new Error('invalid json format, missing attribute kursartID');
		result.kursartID = obj.kursartID;
		if (typeof obj.istSchriftlich === "undefined")
			 throw new Error('invalid json format, missing attribute istSchriftlich');
		result.istSchriftlich = obj.istSchriftlich;
		return result;
	}

	public static transpilerToJSON(obj : GostFachwahl) : string {
		let result = '{';
		result += '"fachID" : ' + obj.fachID + ',';
		result += '"schuelerID" : ' + obj.schuelerID + ',';
		result += '"kursartID" : ' + obj.kursartID + ',';
		result += '"istSchriftlich" : ' + obj.istSchriftlich + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostFachwahl>) : string {
		let result = '{';
		if (typeof obj.fachID !== "undefined") {
			result += '"fachID" : ' + obj.fachID + ',';
		}
		if (typeof obj.schuelerID !== "undefined") {
			result += '"schuelerID" : ' + obj.schuelerID + ',';
		}
		if (typeof obj.kursartID !== "undefined") {
			result += '"kursartID" : ' + obj.kursartID + ',';
		}
		if (typeof obj.istSchriftlich !== "undefined") {
			result += '"istSchriftlich" : ' + obj.istSchriftlich + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_gost_GostFachwahl(obj : unknown) : GostFachwahl {
	return obj as GostFachwahl;
}
