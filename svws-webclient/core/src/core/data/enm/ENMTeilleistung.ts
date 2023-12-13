import { JavaObject } from '../../../java/lang/JavaObject';

export class ENMTeilleistung extends JavaObject {

	/**
	 * Die ID dieser Teilleistung in der SVWS-DB (z.B. 307956)
	 */
	public id : number = 0;

	/**
	 * Die ID der Teilleistungsart (z.B. 42)
	 */
	public artID : number = 0;

	/**
	 * Das Datum an dem die Teilleistung erbracht bzw. festgelegt wurde. (z.B. "2020-10-10")
	 */
	public datum : string | null = null;

	/**
	 * Eine Bemerkung zu der Teilleistung (z.B. "Nachgeschrieben")
	 */
	public bemerkung : string | null = null;

	/**
	 * Das Noten-Kürzel für die Teilleistung (z.B. "1+")
	 */
	public notenKuerzel : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMTeilleistung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMTeilleistung'].includes(name);
	}

	public static transpilerFromJSON(json : string): ENMTeilleistung {
		const obj = JSON.parse(json);
		const result = new ENMTeilleistung();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.artID === "undefined")
			 throw new Error('invalid json format, missing attribute artID');
		result.artID = obj.artID;
		result.datum = typeof obj.datum === "undefined" ? null : obj.datum === null ? null : obj.datum;
		result.bemerkung = typeof obj.bemerkung === "undefined" ? null : obj.bemerkung === null ? null : obj.bemerkung;
		result.notenKuerzel = typeof obj.notenKuerzel === "undefined" ? null : obj.notenKuerzel === null ? null : obj.notenKuerzel;
		return result;
	}

	public static transpilerToJSON(obj : ENMTeilleistung) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"artID" : ' + obj.artID + ',';
		result += '"datum" : ' + ((!obj.datum) ? 'null' : JSON.stringify(obj.datum)) + ',';
		result += '"bemerkung" : ' + ((!obj.bemerkung) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		result += '"notenKuerzel" : ' + ((!obj.notenKuerzel) ? 'null' : JSON.stringify(obj.notenKuerzel)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMTeilleistung>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.artID !== "undefined") {
			result += '"artID" : ' + obj.artID + ',';
		}
		if (typeof obj.datum !== "undefined") {
			result += '"datum" : ' + ((!obj.datum) ? 'null' : JSON.stringify(obj.datum)) + ',';
		}
		if (typeof obj.bemerkung !== "undefined") {
			result += '"bemerkung" : ' + ((!obj.bemerkung) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		}
		if (typeof obj.notenKuerzel !== "undefined") {
			result += '"notenKuerzel" : ' + ((!obj.notenKuerzel) ? 'null' : JSON.stringify(obj.notenKuerzel)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMTeilleistung(obj : unknown) : ENMTeilleistung {
	return obj as ENMTeilleistung;
}
