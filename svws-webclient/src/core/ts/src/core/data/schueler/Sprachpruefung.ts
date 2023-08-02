import { JavaObject } from '../../../java/lang/JavaObject';

export class Sprachpruefung extends JavaObject {

	/**
	 * Das einstellige Sprachkürzel des geprüften Faches
	 */
	public sprache : string | null = null;

	/**
	 * Gibt an, in welchem ASD-Jahrgang die Prüfung abgelegt wurde
	 */
	public jahrgang : string | null = null;

	/**
	 * ID der Bezeichnung des am Schulabschluss orientierte Anspruchsniveau der Sprachprüfung
	 */
	public anspruchsniveauId : number | null = null;

	/**
	 * Sprache, die durch die Prüfung ersetzt wird
	 */
	public ersetzteSprache : string | null = null;

	/**
	 * Prüfung ist eine Prüfung im herkunftssprachlichen Unterricht
	 */
	public istHSUPruefung : boolean = false;

	/**
	 * Prüfung ist eine Sprachfeststellungsprüfung
	 */
	public istFeststellungspruefung : boolean = false;

	/**
	 * Durch die Prüfung kann die erste Pflichtfremdsprache ersetzt werden
	 */
	public kannErstePflichtfremdspracheErsetzen : boolean = false;

	/**
	 * Durch die Prüfung kann die zweite Pflichtfremdsprache ersetzt werden
	 */
	public kannZweitePflichtfremdspracheErsetzen : boolean = false;

	/**
	 * Durch die Prüfung kann die Wahlpflichtfremdsprache ersetzt werden
	 */
	public kannWahlpflichtfremdspracheErsetzen : boolean = false;

	/**
	 * Durch die Prüfung kann die Sprache als fortgeführte Fremdsprache in der GOSt belegt werden.
	 */
	public kannBelegungAlsFortgefuehrteSpracheErlauben : boolean = false;

	/**
	 * Das Kürzel des GeR-Referenzniveaus, welches durch die Prüfung erreicht wurde
	 */
	public referenzniveau : string | null = null;

	/**
	 * Die Note, die in der Sprachprüfung erreicht wurde (1,2,3,4,5,6 oder null, wenn keine Note angegeben ist)
	 */
	public note : number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.Sprachpruefung'].includes(name);
	}

	public static transpilerFromJSON(json : string): Sprachpruefung {
		const obj = JSON.parse(json);
		const result = new Sprachpruefung();
		result.sprache = typeof obj.sprache === "undefined" ? null : obj.sprache === null ? null : obj.sprache;
		result.jahrgang = typeof obj.jahrgang === "undefined" ? null : obj.jahrgang === null ? null : obj.jahrgang;
		result.anspruchsniveauId = typeof obj.anspruchsniveauId === "undefined" ? null : obj.anspruchsniveauId === null ? null : obj.anspruchsniveauId;
		result.ersetzteSprache = typeof obj.ersetzteSprache === "undefined" ? null : obj.ersetzteSprache === null ? null : obj.ersetzteSprache;
		if (typeof obj.istHSUPruefung === "undefined")
			 throw new Error('invalid json format, missing attribute istHSUPruefung');
		result.istHSUPruefung = obj.istHSUPruefung;
		if (typeof obj.istFeststellungspruefung === "undefined")
			 throw new Error('invalid json format, missing attribute istFeststellungspruefung');
		result.istFeststellungspruefung = obj.istFeststellungspruefung;
		if (typeof obj.kannErstePflichtfremdspracheErsetzen === "undefined")
			 throw new Error('invalid json format, missing attribute kannErstePflichtfremdspracheErsetzen');
		result.kannErstePflichtfremdspracheErsetzen = obj.kannErstePflichtfremdspracheErsetzen;
		if (typeof obj.kannZweitePflichtfremdspracheErsetzen === "undefined")
			 throw new Error('invalid json format, missing attribute kannZweitePflichtfremdspracheErsetzen');
		result.kannZweitePflichtfremdspracheErsetzen = obj.kannZweitePflichtfremdspracheErsetzen;
		if (typeof obj.kannWahlpflichtfremdspracheErsetzen === "undefined")
			 throw new Error('invalid json format, missing attribute kannWahlpflichtfremdspracheErsetzen');
		result.kannWahlpflichtfremdspracheErsetzen = obj.kannWahlpflichtfremdspracheErsetzen;
		if (typeof obj.kannBelegungAlsFortgefuehrteSpracheErlauben === "undefined")
			 throw new Error('invalid json format, missing attribute kannBelegungAlsFortgefuehrteSpracheErlauben');
		result.kannBelegungAlsFortgefuehrteSpracheErlauben = obj.kannBelegungAlsFortgefuehrteSpracheErlauben;
		result.referenzniveau = typeof obj.referenzniveau === "undefined" ? null : obj.referenzniveau === null ? null : obj.referenzniveau;
		result.note = typeof obj.note === "undefined" ? null : obj.note === null ? null : obj.note;
		return result;
	}

	public static transpilerToJSON(obj : Sprachpruefung) : string {
		let result = '{';
		result += '"sprache" : ' + ((!obj.sprache) ? 'null' : JSON.stringify(obj.sprache)) + ',';
		result += '"jahrgang" : ' + ((!obj.jahrgang) ? 'null' : JSON.stringify(obj.jahrgang)) + ',';
		result += '"anspruchsniveauId" : ' + ((!obj.anspruchsniveauId) ? 'null' : obj.anspruchsniveauId) + ',';
		result += '"ersetzteSprache" : ' + ((!obj.ersetzteSprache) ? 'null' : JSON.stringify(obj.ersetzteSprache)) + ',';
		result += '"istHSUPruefung" : ' + obj.istHSUPruefung + ',';
		result += '"istFeststellungspruefung" : ' + obj.istFeststellungspruefung + ',';
		result += '"kannErstePflichtfremdspracheErsetzen" : ' + obj.kannErstePflichtfremdspracheErsetzen + ',';
		result += '"kannZweitePflichtfremdspracheErsetzen" : ' + obj.kannZweitePflichtfremdspracheErsetzen + ',';
		result += '"kannWahlpflichtfremdspracheErsetzen" : ' + obj.kannWahlpflichtfremdspracheErsetzen + ',';
		result += '"kannBelegungAlsFortgefuehrteSpracheErlauben" : ' + obj.kannBelegungAlsFortgefuehrteSpracheErlauben + ',';
		result += '"referenzniveau" : ' + ((!obj.referenzniveau) ? 'null' : JSON.stringify(obj.referenzniveau)) + ',';
		result += '"note" : ' + ((!obj.note) ? 'null' : obj.note) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Sprachpruefung>) : string {
		let result = '{';
		if (typeof obj.sprache !== "undefined") {
			result += '"sprache" : ' + ((!obj.sprache) ? 'null' : JSON.stringify(obj.sprache)) + ',';
		}
		if (typeof obj.jahrgang !== "undefined") {
			result += '"jahrgang" : ' + ((!obj.jahrgang) ? 'null' : JSON.stringify(obj.jahrgang)) + ',';
		}
		if (typeof obj.anspruchsniveauId !== "undefined") {
			result += '"anspruchsniveauId" : ' + ((!obj.anspruchsniveauId) ? 'null' : obj.anspruchsniveauId) + ',';
		}
		if (typeof obj.ersetzteSprache !== "undefined") {
			result += '"ersetzteSprache" : ' + ((!obj.ersetzteSprache) ? 'null' : JSON.stringify(obj.ersetzteSprache)) + ',';
		}
		if (typeof obj.istHSUPruefung !== "undefined") {
			result += '"istHSUPruefung" : ' + obj.istHSUPruefung + ',';
		}
		if (typeof obj.istFeststellungspruefung !== "undefined") {
			result += '"istFeststellungspruefung" : ' + obj.istFeststellungspruefung + ',';
		}
		if (typeof obj.kannErstePflichtfremdspracheErsetzen !== "undefined") {
			result += '"kannErstePflichtfremdspracheErsetzen" : ' + obj.kannErstePflichtfremdspracheErsetzen + ',';
		}
		if (typeof obj.kannZweitePflichtfremdspracheErsetzen !== "undefined") {
			result += '"kannZweitePflichtfremdspracheErsetzen" : ' + obj.kannZweitePflichtfremdspracheErsetzen + ',';
		}
		if (typeof obj.kannWahlpflichtfremdspracheErsetzen !== "undefined") {
			result += '"kannWahlpflichtfremdspracheErsetzen" : ' + obj.kannWahlpflichtfremdspracheErsetzen + ',';
		}
		if (typeof obj.kannBelegungAlsFortgefuehrteSpracheErlauben !== "undefined") {
			result += '"kannBelegungAlsFortgefuehrteSpracheErlauben" : ' + obj.kannBelegungAlsFortgefuehrteSpracheErlauben + ',';
		}
		if (typeof obj.referenzniveau !== "undefined") {
			result += '"referenzniveau" : ' + ((!obj.referenzniveau) ? 'null' : JSON.stringify(obj.referenzniveau)) + ',';
		}
		if (typeof obj.note !== "undefined") {
			result += '"note" : ' + ((!obj.note) ? 'null' : obj.note) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schueler_Sprachpruefung(obj : unknown) : Sprachpruefung {
	return obj as Sprachpruefung;
}
