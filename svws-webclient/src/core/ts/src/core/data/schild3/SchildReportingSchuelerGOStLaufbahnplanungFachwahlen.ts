import { JavaObject } from '../../../java/lang/JavaObject';

export class SchildReportingSchuelerGOStLaufbahnplanungFachwahlen extends JavaObject {

	/**
	 * Die ID des Schülers, zu dem die Laufbahnplanungsdaten gehören.
	 */
	public schuelerID : number = 0;

	/**
	 * Die Bezeichnung des Faches
	 */
	public bezeichnung : string = "";

	/**
	 * Das Kürzel des Faches
	 */
	public kuerzel : string = "";

	/**
	 * Fach ist eine Fremdsprache in der GOSt fortführbare Fremdsprache
	 */
	public fachIstFortfuehrbareFremdspracheInGOSt : boolean | null = false;

	/**
	 * Fach ist eine Fremdsprache: Jahrgangsstufe des Beginns der Sprachbelegung
	 */
	public jahrgangFremdsprachenbeginn : string = "";

	/**
	 * Fach ist eine Fremdsprache: Position in der Fremdsprachenfolge bzw. Prüfungsvermerk
	 */
	public positionFremdsprachenfolge : string = "";

	/**
	 * Fachbelegung in der EF.1
	 */
	public belegungEF1 : string = "";

	/**
	 * Fachbelegung in der EF.2
	 */
	public belegungEF2 : string = "";

	/**
	 * Fachbelegung in der Q1.1
	 */
	public belegungQ11 : string = "";

	/**
	 * Fachbelegung in der Q1.2
	 */
	public belegungQ12 : string = "";

	/**
	 * Fachbelegung in der Q2.1
	 */
	public belegungQ21 : string = "";

	/**
	 * Fachbelegung in der Q2.2
	 */
	public belegungQ22 : string = "";

	/**
	 * Abiturfacheintrag, sofern das belegte Fach als Abiturfach gewählt wurde
	 */
	public abiturfach : string = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schild3.SchildReportingSchuelerGOStLaufbahnplanungFachwahlen'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchildReportingSchuelerGOStLaufbahnplanungFachwahlen {
		const obj = JSON.parse(json);
		const result = new SchildReportingSchuelerGOStLaufbahnplanungFachwahlen();
		if (typeof obj.schuelerID === "undefined")
			 throw new Error('invalid json format, missing attribute schuelerID');
		result.schuelerID = obj.schuelerID;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		result.fachIstFortfuehrbareFremdspracheInGOSt = typeof obj.fachIstFortfuehrbareFremdspracheInGOSt === "undefined" ? null : obj.fachIstFortfuehrbareFremdspracheInGOSt === null ? null : obj.fachIstFortfuehrbareFremdspracheInGOSt;
		if (typeof obj.jahrgangFremdsprachenbeginn === "undefined")
			 throw new Error('invalid json format, missing attribute jahrgangFremdsprachenbeginn');
		result.jahrgangFremdsprachenbeginn = obj.jahrgangFremdsprachenbeginn;
		if (typeof obj.positionFremdsprachenfolge === "undefined")
			 throw new Error('invalid json format, missing attribute positionFremdsprachenfolge');
		result.positionFremdsprachenfolge = obj.positionFremdsprachenfolge;
		if (typeof obj.belegungEF1 === "undefined")
			 throw new Error('invalid json format, missing attribute belegungEF1');
		result.belegungEF1 = obj.belegungEF1;
		if (typeof obj.belegungEF2 === "undefined")
			 throw new Error('invalid json format, missing attribute belegungEF2');
		result.belegungEF2 = obj.belegungEF2;
		if (typeof obj.belegungQ11 === "undefined")
			 throw new Error('invalid json format, missing attribute belegungQ11');
		result.belegungQ11 = obj.belegungQ11;
		if (typeof obj.belegungQ12 === "undefined")
			 throw new Error('invalid json format, missing attribute belegungQ12');
		result.belegungQ12 = obj.belegungQ12;
		if (typeof obj.belegungQ21 === "undefined")
			 throw new Error('invalid json format, missing attribute belegungQ21');
		result.belegungQ21 = obj.belegungQ21;
		if (typeof obj.belegungQ22 === "undefined")
			 throw new Error('invalid json format, missing attribute belegungQ22');
		result.belegungQ22 = obj.belegungQ22;
		if (typeof obj.abiturfach === "undefined")
			 throw new Error('invalid json format, missing attribute abiturfach');
		result.abiturfach = obj.abiturfach;
		return result;
	}

	public static transpilerToJSON(obj : SchildReportingSchuelerGOStLaufbahnplanungFachwahlen) : string {
		let result = '{';
		result += '"schuelerID" : ' + obj.schuelerID + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel!) + ',';
		result += '"fachIstFortfuehrbareFremdspracheInGOSt" : ' + ((!obj.fachIstFortfuehrbareFremdspracheInGOSt) ? 'null' : obj.fachIstFortfuehrbareFremdspracheInGOSt) + ',';
		result += '"jahrgangFremdsprachenbeginn" : ' + JSON.stringify(obj.jahrgangFremdsprachenbeginn!) + ',';
		result += '"positionFremdsprachenfolge" : ' + JSON.stringify(obj.positionFremdsprachenfolge!) + ',';
		result += '"belegungEF1" : ' + JSON.stringify(obj.belegungEF1!) + ',';
		result += '"belegungEF2" : ' + JSON.stringify(obj.belegungEF2!) + ',';
		result += '"belegungQ11" : ' + JSON.stringify(obj.belegungQ11!) + ',';
		result += '"belegungQ12" : ' + JSON.stringify(obj.belegungQ12!) + ',';
		result += '"belegungQ21" : ' + JSON.stringify(obj.belegungQ21!) + ',';
		result += '"belegungQ22" : ' + JSON.stringify(obj.belegungQ22!) + ',';
		result += '"abiturfach" : ' + JSON.stringify(obj.abiturfach!) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchildReportingSchuelerGOStLaufbahnplanungFachwahlen>) : string {
		let result = '{';
		if (typeof obj.schuelerID !== "undefined") {
			result += '"schuelerID" : ' + obj.schuelerID + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel!) + ',';
		}
		if (typeof obj.fachIstFortfuehrbareFremdspracheInGOSt !== "undefined") {
			result += '"fachIstFortfuehrbareFremdspracheInGOSt" : ' + ((!obj.fachIstFortfuehrbareFremdspracheInGOSt) ? 'null' : obj.fachIstFortfuehrbareFremdspracheInGOSt) + ',';
		}
		if (typeof obj.jahrgangFremdsprachenbeginn !== "undefined") {
			result += '"jahrgangFremdsprachenbeginn" : ' + JSON.stringify(obj.jahrgangFremdsprachenbeginn!) + ',';
		}
		if (typeof obj.positionFremdsprachenfolge !== "undefined") {
			result += '"positionFremdsprachenfolge" : ' + JSON.stringify(obj.positionFremdsprachenfolge!) + ',';
		}
		if (typeof obj.belegungEF1 !== "undefined") {
			result += '"belegungEF1" : ' + JSON.stringify(obj.belegungEF1!) + ',';
		}
		if (typeof obj.belegungEF2 !== "undefined") {
			result += '"belegungEF2" : ' + JSON.stringify(obj.belegungEF2!) + ',';
		}
		if (typeof obj.belegungQ11 !== "undefined") {
			result += '"belegungQ11" : ' + JSON.stringify(obj.belegungQ11!) + ',';
		}
		if (typeof obj.belegungQ12 !== "undefined") {
			result += '"belegungQ12" : ' + JSON.stringify(obj.belegungQ12!) + ',';
		}
		if (typeof obj.belegungQ21 !== "undefined") {
			result += '"belegungQ21" : ' + JSON.stringify(obj.belegungQ21!) + ',';
		}
		if (typeof obj.belegungQ22 !== "undefined") {
			result += '"belegungQ22" : ' + JSON.stringify(obj.belegungQ22!) + ',';
		}
		if (typeof obj.abiturfach !== "undefined") {
			result += '"abiturfach" : ' + JSON.stringify(obj.abiturfach!) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schild3_SchildReportingSchuelerGOStLaufbahnplanungFachwahlen(obj : unknown) : SchildReportingSchuelerGOStLaufbahnplanungFachwahlen {
	return obj as SchildReportingSchuelerGOStLaufbahnplanungFachwahlen;
}
