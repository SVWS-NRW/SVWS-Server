import { JavaObject } from '../../../java/lang/JavaObject';

export class DruckGostLaufbahnplanungSchuelerSummen extends JavaObject {

	/**
	 * Die ID des Schülers, zu dem die Laufbahnplanungsdaten gehören.
	 */
	public schuelerID : number = 0;

	/**
	 * Kursanzahl in der EF.1
	 */
	public kursanzahlEF1 : number = 0;

	/**
	 * Kursanzahl in der EF.2
	 */
	public kursanzahlEF2 : number = 0;

	/**
	 * Kursanzahl in der Q1.1
	 */
	public kursanzahlQ11 : number = 0;

	/**
	 * Kursanzahl in der Q1.2
	 */
	public kursanzahlQ12 : number = 0;

	/**
	 * Kursanzahl in der Q2.1
	 */
	public kursanzahlQ21 : number = 0;

	/**
	 * Kursanzahl in der Q2.2
	 */
	public kursanzahlQ22 : number = 0;

	/**
	 * Kursanzahl in der Qualifikationsphase
	 */
	public kursanzahlQPh : number = 0;

	/**
	 * Wochenstundensumme in der EF.1
	 */
	public wochenstundenEF1 : number = 0;

	/**
	 * Wochenstundensumme in der EF.2
	 */
	public wochenstundenEF2 : number = 0;

	/**
	 * Wochenstundensumme in der Q1.1
	 */
	public wochenstundenQ11 : number = 0;

	/**
	 * Wochenstundensumme in der Q1.2
	 */
	public wochenstundenQ12 : number = 0;

	/**
	 * Wochenstundensumme in der Q2.1
	 */
	public wochenstundenQ21 : number = 0;

	/**
	 * Wochenstundensumme in der Q2.2
	 */
	public wochenstundenQ22 : number = 0;

	/**
	 * Wochenstundendurchschnitt in der EF
	 */
	public wochenstundenDurchschnittEF : number = 0;

	/**
	 * Wochenstundendurchschnitt in der EF
	 */
	public wochenstundenDurchschnittQ1 : number = 0;

	/**
	 * Wochenstundendurchschnitt in der EF
	 */
	public wochenstundenDurchschnittQ2 : number = 0;

	/**
	 * Wochenstundendurchschnitt in der Qualifikationsphase
	 */
	public wochenstundenDurchschnittQPh : number = 0;

	/**
	 * Wochenstundensumme der gesamten Laufbahn
	 */
	public wochenstundenGesamt : number = 0;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.druck.DruckGostLaufbahnplanungSchuelerSummen'].includes(name);
	}

	public static transpilerFromJSON(json : string): DruckGostLaufbahnplanungSchuelerSummen {
		const obj = JSON.parse(json);
		const result = new DruckGostLaufbahnplanungSchuelerSummen();
		if (typeof obj.schuelerID === "undefined")
			 throw new Error('invalid json format, missing attribute schuelerID');
		result.schuelerID = obj.schuelerID;
		if (typeof obj.kursanzahlEF1 === "undefined")
			 throw new Error('invalid json format, missing attribute kursanzahlEF1');
		result.kursanzahlEF1 = obj.kursanzahlEF1;
		if (typeof obj.kursanzahlEF2 === "undefined")
			 throw new Error('invalid json format, missing attribute kursanzahlEF2');
		result.kursanzahlEF2 = obj.kursanzahlEF2;
		if (typeof obj.kursanzahlQ11 === "undefined")
			 throw new Error('invalid json format, missing attribute kursanzahlQ11');
		result.kursanzahlQ11 = obj.kursanzahlQ11;
		if (typeof obj.kursanzahlQ12 === "undefined")
			 throw new Error('invalid json format, missing attribute kursanzahlQ12');
		result.kursanzahlQ12 = obj.kursanzahlQ12;
		if (typeof obj.kursanzahlQ21 === "undefined")
			 throw new Error('invalid json format, missing attribute kursanzahlQ21');
		result.kursanzahlQ21 = obj.kursanzahlQ21;
		if (typeof obj.kursanzahlQ22 === "undefined")
			 throw new Error('invalid json format, missing attribute kursanzahlQ22');
		result.kursanzahlQ22 = obj.kursanzahlQ22;
		if (typeof obj.kursanzahlQPh === "undefined")
			 throw new Error('invalid json format, missing attribute kursanzahlQPh');
		result.kursanzahlQPh = obj.kursanzahlQPh;
		if (typeof obj.wochenstundenEF1 === "undefined")
			 throw new Error('invalid json format, missing attribute wochenstundenEF1');
		result.wochenstundenEF1 = obj.wochenstundenEF1;
		if (typeof obj.wochenstundenEF2 === "undefined")
			 throw new Error('invalid json format, missing attribute wochenstundenEF2');
		result.wochenstundenEF2 = obj.wochenstundenEF2;
		if (typeof obj.wochenstundenQ11 === "undefined")
			 throw new Error('invalid json format, missing attribute wochenstundenQ11');
		result.wochenstundenQ11 = obj.wochenstundenQ11;
		if (typeof obj.wochenstundenQ12 === "undefined")
			 throw new Error('invalid json format, missing attribute wochenstundenQ12');
		result.wochenstundenQ12 = obj.wochenstundenQ12;
		if (typeof obj.wochenstundenQ21 === "undefined")
			 throw new Error('invalid json format, missing attribute wochenstundenQ21');
		result.wochenstundenQ21 = obj.wochenstundenQ21;
		if (typeof obj.wochenstundenQ22 === "undefined")
			 throw new Error('invalid json format, missing attribute wochenstundenQ22');
		result.wochenstundenQ22 = obj.wochenstundenQ22;
		if (typeof obj.wochenstundenDurchschnittEF === "undefined")
			 throw new Error('invalid json format, missing attribute wochenstundenDurchschnittEF');
		result.wochenstundenDurchschnittEF = obj.wochenstundenDurchschnittEF;
		if (typeof obj.wochenstundenDurchschnittQ1 === "undefined")
			 throw new Error('invalid json format, missing attribute wochenstundenDurchschnittQ1');
		result.wochenstundenDurchschnittQ1 = obj.wochenstundenDurchschnittQ1;
		if (typeof obj.wochenstundenDurchschnittQ2 === "undefined")
			 throw new Error('invalid json format, missing attribute wochenstundenDurchschnittQ2');
		result.wochenstundenDurchschnittQ2 = obj.wochenstundenDurchschnittQ2;
		if (typeof obj.wochenstundenDurchschnittQPh === "undefined")
			 throw new Error('invalid json format, missing attribute wochenstundenDurchschnittQPh');
		result.wochenstundenDurchschnittQPh = obj.wochenstundenDurchschnittQPh;
		if (typeof obj.wochenstundenGesamt === "undefined")
			 throw new Error('invalid json format, missing attribute wochenstundenGesamt');
		result.wochenstundenGesamt = obj.wochenstundenGesamt;
		return result;
	}

	public static transpilerToJSON(obj : DruckGostLaufbahnplanungSchuelerSummen) : string {
		let result = '{';
		result += '"schuelerID" : ' + obj.schuelerID + ',';
		result += '"kursanzahlEF1" : ' + obj.kursanzahlEF1 + ',';
		result += '"kursanzahlEF2" : ' + obj.kursanzahlEF2 + ',';
		result += '"kursanzahlQ11" : ' + obj.kursanzahlQ11 + ',';
		result += '"kursanzahlQ12" : ' + obj.kursanzahlQ12 + ',';
		result += '"kursanzahlQ21" : ' + obj.kursanzahlQ21 + ',';
		result += '"kursanzahlQ22" : ' + obj.kursanzahlQ22 + ',';
		result += '"kursanzahlQPh" : ' + obj.kursanzahlQPh + ',';
		result += '"wochenstundenEF1" : ' + obj.wochenstundenEF1 + ',';
		result += '"wochenstundenEF2" : ' + obj.wochenstundenEF2 + ',';
		result += '"wochenstundenQ11" : ' + obj.wochenstundenQ11 + ',';
		result += '"wochenstundenQ12" : ' + obj.wochenstundenQ12 + ',';
		result += '"wochenstundenQ21" : ' + obj.wochenstundenQ21 + ',';
		result += '"wochenstundenQ22" : ' + obj.wochenstundenQ22 + ',';
		result += '"wochenstundenDurchschnittEF" : ' + obj.wochenstundenDurchschnittEF + ',';
		result += '"wochenstundenDurchschnittQ1" : ' + obj.wochenstundenDurchschnittQ1 + ',';
		result += '"wochenstundenDurchschnittQ2" : ' + obj.wochenstundenDurchschnittQ2 + ',';
		result += '"wochenstundenDurchschnittQPh" : ' + obj.wochenstundenDurchschnittQPh + ',';
		result += '"wochenstundenGesamt" : ' + obj.wochenstundenGesamt + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<DruckGostLaufbahnplanungSchuelerSummen>) : string {
		let result = '{';
		if (typeof obj.schuelerID !== "undefined") {
			result += '"schuelerID" : ' + obj.schuelerID + ',';
		}
		if (typeof obj.kursanzahlEF1 !== "undefined") {
			result += '"kursanzahlEF1" : ' + obj.kursanzahlEF1 + ',';
		}
		if (typeof obj.kursanzahlEF2 !== "undefined") {
			result += '"kursanzahlEF2" : ' + obj.kursanzahlEF2 + ',';
		}
		if (typeof obj.kursanzahlQ11 !== "undefined") {
			result += '"kursanzahlQ11" : ' + obj.kursanzahlQ11 + ',';
		}
		if (typeof obj.kursanzahlQ12 !== "undefined") {
			result += '"kursanzahlQ12" : ' + obj.kursanzahlQ12 + ',';
		}
		if (typeof obj.kursanzahlQ21 !== "undefined") {
			result += '"kursanzahlQ21" : ' + obj.kursanzahlQ21 + ',';
		}
		if (typeof obj.kursanzahlQ22 !== "undefined") {
			result += '"kursanzahlQ22" : ' + obj.kursanzahlQ22 + ',';
		}
		if (typeof obj.kursanzahlQPh !== "undefined") {
			result += '"kursanzahlQPh" : ' + obj.kursanzahlQPh + ',';
		}
		if (typeof obj.wochenstundenEF1 !== "undefined") {
			result += '"wochenstundenEF1" : ' + obj.wochenstundenEF1 + ',';
		}
		if (typeof obj.wochenstundenEF2 !== "undefined") {
			result += '"wochenstundenEF2" : ' + obj.wochenstundenEF2 + ',';
		}
		if (typeof obj.wochenstundenQ11 !== "undefined") {
			result += '"wochenstundenQ11" : ' + obj.wochenstundenQ11 + ',';
		}
		if (typeof obj.wochenstundenQ12 !== "undefined") {
			result += '"wochenstundenQ12" : ' + obj.wochenstundenQ12 + ',';
		}
		if (typeof obj.wochenstundenQ21 !== "undefined") {
			result += '"wochenstundenQ21" : ' + obj.wochenstundenQ21 + ',';
		}
		if (typeof obj.wochenstundenQ22 !== "undefined") {
			result += '"wochenstundenQ22" : ' + obj.wochenstundenQ22 + ',';
		}
		if (typeof obj.wochenstundenDurchschnittEF !== "undefined") {
			result += '"wochenstundenDurchschnittEF" : ' + obj.wochenstundenDurchschnittEF + ',';
		}
		if (typeof obj.wochenstundenDurchschnittQ1 !== "undefined") {
			result += '"wochenstundenDurchschnittQ1" : ' + obj.wochenstundenDurchschnittQ1 + ',';
		}
		if (typeof obj.wochenstundenDurchschnittQ2 !== "undefined") {
			result += '"wochenstundenDurchschnittQ2" : ' + obj.wochenstundenDurchschnittQ2 + ',';
		}
		if (typeof obj.wochenstundenDurchschnittQPh !== "undefined") {
			result += '"wochenstundenDurchschnittQPh" : ' + obj.wochenstundenDurchschnittQPh + ',';
		}
		if (typeof obj.wochenstundenGesamt !== "undefined") {
			result += '"wochenstundenGesamt" : ' + obj.wochenstundenGesamt + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_druck_DruckGostLaufbahnplanungSchuelerSummen(obj : unknown) : DruckGostLaufbahnplanungSchuelerSummen {
	return obj as DruckGostLaufbahnplanungSchuelerSummen;
}
