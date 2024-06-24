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
	 * Der Zeitstempel der letzten Änderung an der Teilleistungsart
	 */
	public tsArtID : string | null = null;

	/**
	 * Das Datum an dem die Teilleistung erbracht bzw. festgelegt wurde. (z.B. "2020-10-10")
	 */
	public datum : string | null = null;

	/**
	 * Der Zeitstempel der letzten Änderung an dem Datum
	 */
	public tsDatum : string | null = null;

	/**
	 * Eine Bemerkung zu der Teilleistung (z.B. "Nachgeschrieben")
	 */
	public bemerkung : string | null = null;

	/**
	 * Der Zeitstempel der letzten Änderung an der Bemerkung
	 */
	public tsBemerkung : string | null = null;

	/**
	 * Das Noten-Kürzel für die Teilleistung (z.B. "1+")
	 */
	public note : string | null = null;

	/**
	 * Der Zeitstempel der letzten Änderung an der Note
	 */
	public tsNote : string | null = null;


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
		if (obj.id === undefined)
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.artID === undefined)
			 throw new Error('invalid json format, missing attribute artID');
		result.artID = obj.artID;
		result.tsArtID = (obj.tsArtID === undefined) ? null : obj.tsArtID === null ? null : obj.tsArtID;
		result.datum = (obj.datum === undefined) ? null : obj.datum === null ? null : obj.datum;
		result.tsDatum = (obj.tsDatum === undefined) ? null : obj.tsDatum === null ? null : obj.tsDatum;
		result.bemerkung = (obj.bemerkung === undefined) ? null : obj.bemerkung === null ? null : obj.bemerkung;
		result.tsBemerkung = (obj.tsBemerkung === undefined) ? null : obj.tsBemerkung === null ? null : obj.tsBemerkung;
		result.note = (obj.note === undefined) ? null : obj.note === null ? null : obj.note;
		result.tsNote = (obj.tsNote === undefined) ? null : obj.tsNote === null ? null : obj.tsNote;
		return result;
	}

	public static transpilerToJSON(obj : ENMTeilleistung) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"artID" : ' + obj.artID + ',';
		result += '"tsArtID" : ' + ((!obj.tsArtID) ? 'null' : JSON.stringify(obj.tsArtID)) + ',';
		result += '"datum" : ' + ((!obj.datum) ? 'null' : JSON.stringify(obj.datum)) + ',';
		result += '"tsDatum" : ' + ((!obj.tsDatum) ? 'null' : JSON.stringify(obj.tsDatum)) + ',';
		result += '"bemerkung" : ' + ((!obj.bemerkung) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		result += '"tsBemerkung" : ' + ((!obj.tsBemerkung) ? 'null' : JSON.stringify(obj.tsBemerkung)) + ',';
		result += '"note" : ' + ((!obj.note) ? 'null' : JSON.stringify(obj.note)) + ',';
		result += '"tsNote" : ' + ((!obj.tsNote) ? 'null' : JSON.stringify(obj.tsNote)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMTeilleistung>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id + ',';
		}
		if (obj.artID !== undefined) {
			result += '"artID" : ' + obj.artID + ',';
		}
		if (obj.tsArtID !== undefined) {
			result += '"tsArtID" : ' + ((!obj.tsArtID) ? 'null' : JSON.stringify(obj.tsArtID)) + ',';
		}
		if (obj.datum !== undefined) {
			result += '"datum" : ' + ((!obj.datum) ? 'null' : JSON.stringify(obj.datum)) + ',';
		}
		if (obj.tsDatum !== undefined) {
			result += '"tsDatum" : ' + ((!obj.tsDatum) ? 'null' : JSON.stringify(obj.tsDatum)) + ',';
		}
		if (obj.bemerkung !== undefined) {
			result += '"bemerkung" : ' + ((!obj.bemerkung) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		}
		if (obj.tsBemerkung !== undefined) {
			result += '"tsBemerkung" : ' + ((!obj.tsBemerkung) ? 'null' : JSON.stringify(obj.tsBemerkung)) + ',';
		}
		if (obj.note !== undefined) {
			result += '"note" : ' + ((!obj.note) ? 'null' : JSON.stringify(obj.note)) + ',';
		}
		if (obj.tsNote !== undefined) {
			result += '"tsNote" : ' + ((!obj.tsNote) ? 'null' : JSON.stringify(obj.tsNote)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMTeilleistung(obj : unknown) : ENMTeilleistung {
	return obj as ENMTeilleistung;
}
