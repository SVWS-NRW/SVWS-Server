import { JavaObject } from '../../../java/lang/JavaObject';

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

	/**
	 * Gibt an, ob die Fachwahl als ein Abiturfach geplant ist oder nicht
	 */
	public abiturfach : number | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostFachwahl';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostFachwahl'].includes(name);
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
		result.abiturfach = typeof obj.abiturfach === "undefined" ? null : obj.abiturfach === null ? null : obj.abiturfach;
		return result;
	}

	public static transpilerToJSON(obj : GostFachwahl) : string {
		let result = '{';
		result += '"fachID" : ' + obj.fachID + ',';
		result += '"schuelerID" : ' + obj.schuelerID + ',';
		result += '"kursartID" : ' + obj.kursartID + ',';
		result += '"istSchriftlich" : ' + obj.istSchriftlich + ',';
		result += '"abiturfach" : ' + ((!obj.abiturfach) ? 'null' : obj.abiturfach) + ',';
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
		if (typeof obj.abiturfach !== "undefined") {
			result += '"abiturfach" : ' + ((!obj.abiturfach) ? 'null' : obj.abiturfach) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostFachwahl(obj : unknown) : GostFachwahl {
	return obj as GostFachwahl;
}
