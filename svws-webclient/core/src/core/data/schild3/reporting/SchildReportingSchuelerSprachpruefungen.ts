import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class SchildReportingSchuelerSprachpruefungen extends JavaObject {

	/**
	 * Die ID des Schülers, zu dem die Laufbahnplanungsdaten gehören.
	 */
	public schuelerID : number = 0;

	/**
	 * Das Sprachkürzel des geprüften Faches
	 */
	public sprache : string = "";

	/**
	 * Gibt an, in welchem ASD-Jahrgang die Prüfung abgelegt wurde
	 */
	public jahrgang : string = "";

	/**
	 * Bezeichnung des am Schulabschluss orientierte Anspruchsniveau der Sprachprüfung
	 */
	public anspruchsniveau : string = "";

	/**
	 * Sprache, die durch die Prüfung ersetzt wird
	 */
	public ersetzteSprache : string = "";

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
	 * Datum der Sprachprüfung.
	 */
	public pruefungsdatum : string = "";

	/**
	 * Das Kürzel des GeR-Referenzniveaus, welches durch die Prüfung erreicht wurde
	 */
	public referenzniveau : string = "";

	/**
	 * Die Note, die in der Sprachprüfung erreicht wurde
	 */
	public note : string = "";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schild3.reporting.SchildReportingSchuelerSprachpruefungen';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schild3.reporting.SchildReportingSchuelerSprachpruefungen'].includes(name);
	}

	public static class = new Class<SchildReportingSchuelerSprachpruefungen>('de.svws_nrw.core.data.schild3.reporting.SchildReportingSchuelerSprachpruefungen');

	public static transpilerFromJSON(json : string): SchildReportingSchuelerSprachpruefungen {
		const obj = JSON.parse(json) as Partial<SchildReportingSchuelerSprachpruefungen>;
		const result = new SchildReportingSchuelerSprachpruefungen();
		if (obj.schuelerID === undefined)
			throw new Error('invalid json format, missing attribute schuelerID');
		result.schuelerID = obj.schuelerID;
		if (obj.sprache === undefined)
			throw new Error('invalid json format, missing attribute sprache');
		result.sprache = obj.sprache;
		if (obj.jahrgang === undefined)
			throw new Error('invalid json format, missing attribute jahrgang');
		result.jahrgang = obj.jahrgang;
		if (obj.anspruchsniveau === undefined)
			throw new Error('invalid json format, missing attribute anspruchsniveau');
		result.anspruchsniveau = obj.anspruchsniveau;
		if (obj.ersetzteSprache === undefined)
			throw new Error('invalid json format, missing attribute ersetzteSprache');
		result.ersetzteSprache = obj.ersetzteSprache;
		if (obj.istHSUPruefung === undefined)
			throw new Error('invalid json format, missing attribute istHSUPruefung');
		result.istHSUPruefung = obj.istHSUPruefung;
		if (obj.istFeststellungspruefung === undefined)
			throw new Error('invalid json format, missing attribute istFeststellungspruefung');
		result.istFeststellungspruefung = obj.istFeststellungspruefung;
		if (obj.kannErstePflichtfremdspracheErsetzen === undefined)
			throw new Error('invalid json format, missing attribute kannErstePflichtfremdspracheErsetzen');
		result.kannErstePflichtfremdspracheErsetzen = obj.kannErstePflichtfremdspracheErsetzen;
		if (obj.kannZweitePflichtfremdspracheErsetzen === undefined)
			throw new Error('invalid json format, missing attribute kannZweitePflichtfremdspracheErsetzen');
		result.kannZweitePflichtfremdspracheErsetzen = obj.kannZweitePflichtfremdspracheErsetzen;
		if (obj.kannWahlpflichtfremdspracheErsetzen === undefined)
			throw new Error('invalid json format, missing attribute kannWahlpflichtfremdspracheErsetzen');
		result.kannWahlpflichtfremdspracheErsetzen = obj.kannWahlpflichtfremdspracheErsetzen;
		if (obj.kannBelegungAlsFortgefuehrteSpracheErlauben === undefined)
			throw new Error('invalid json format, missing attribute kannBelegungAlsFortgefuehrteSpracheErlauben');
		result.kannBelegungAlsFortgefuehrteSpracheErlauben = obj.kannBelegungAlsFortgefuehrteSpracheErlauben;
		if (obj.pruefungsdatum === undefined)
			throw new Error('invalid json format, missing attribute pruefungsdatum');
		result.pruefungsdatum = obj.pruefungsdatum;
		if (obj.referenzniveau === undefined)
			throw new Error('invalid json format, missing attribute referenzniveau');
		result.referenzniveau = obj.referenzniveau;
		if (obj.note === undefined)
			throw new Error('invalid json format, missing attribute note');
		result.note = obj.note;
		return result;
	}

	public static transpilerToJSON(obj : SchildReportingSchuelerSprachpruefungen) : string {
		let result = '{';
		result += '"schuelerID" : ' + obj.schuelerID.toString() + ',';
		result += '"sprache" : ' + JSON.stringify(obj.sprache) + ',';
		result += '"jahrgang" : ' + JSON.stringify(obj.jahrgang) + ',';
		result += '"anspruchsniveau" : ' + JSON.stringify(obj.anspruchsniveau) + ',';
		result += '"ersetzteSprache" : ' + JSON.stringify(obj.ersetzteSprache) + ',';
		result += '"istHSUPruefung" : ' + obj.istHSUPruefung.toString() + ',';
		result += '"istFeststellungspruefung" : ' + obj.istFeststellungspruefung.toString() + ',';
		result += '"kannErstePflichtfremdspracheErsetzen" : ' + obj.kannErstePflichtfremdspracheErsetzen.toString() + ',';
		result += '"kannZweitePflichtfremdspracheErsetzen" : ' + obj.kannZweitePflichtfremdspracheErsetzen.toString() + ',';
		result += '"kannWahlpflichtfremdspracheErsetzen" : ' + obj.kannWahlpflichtfremdspracheErsetzen.toString() + ',';
		result += '"kannBelegungAlsFortgefuehrteSpracheErlauben" : ' + obj.kannBelegungAlsFortgefuehrteSpracheErlauben.toString() + ',';
		result += '"pruefungsdatum" : ' + JSON.stringify(obj.pruefungsdatum) + ',';
		result += '"referenzniveau" : ' + JSON.stringify(obj.referenzniveau) + ',';
		result += '"note" : ' + JSON.stringify(obj.note) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchildReportingSchuelerSprachpruefungen>) : string {
		let result = '{';
		if (obj.schuelerID !== undefined) {
			result += '"schuelerID" : ' + obj.schuelerID.toString() + ',';
		}
		if (obj.sprache !== undefined) {
			result += '"sprache" : ' + JSON.stringify(obj.sprache) + ',';
		}
		if (obj.jahrgang !== undefined) {
			result += '"jahrgang" : ' + JSON.stringify(obj.jahrgang) + ',';
		}
		if (obj.anspruchsniveau !== undefined) {
			result += '"anspruchsniveau" : ' + JSON.stringify(obj.anspruchsniveau) + ',';
		}
		if (obj.ersetzteSprache !== undefined) {
			result += '"ersetzteSprache" : ' + JSON.stringify(obj.ersetzteSprache) + ',';
		}
		if (obj.istHSUPruefung !== undefined) {
			result += '"istHSUPruefung" : ' + obj.istHSUPruefung.toString() + ',';
		}
		if (obj.istFeststellungspruefung !== undefined) {
			result += '"istFeststellungspruefung" : ' + obj.istFeststellungspruefung.toString() + ',';
		}
		if (obj.kannErstePflichtfremdspracheErsetzen !== undefined) {
			result += '"kannErstePflichtfremdspracheErsetzen" : ' + obj.kannErstePflichtfremdspracheErsetzen.toString() + ',';
		}
		if (obj.kannZweitePflichtfremdspracheErsetzen !== undefined) {
			result += '"kannZweitePflichtfremdspracheErsetzen" : ' + obj.kannZweitePflichtfremdspracheErsetzen.toString() + ',';
		}
		if (obj.kannWahlpflichtfremdspracheErsetzen !== undefined) {
			result += '"kannWahlpflichtfremdspracheErsetzen" : ' + obj.kannWahlpflichtfremdspracheErsetzen.toString() + ',';
		}
		if (obj.kannBelegungAlsFortgefuehrteSpracheErlauben !== undefined) {
			result += '"kannBelegungAlsFortgefuehrteSpracheErlauben" : ' + obj.kannBelegungAlsFortgefuehrteSpracheErlauben.toString() + ',';
		}
		if (obj.pruefungsdatum !== undefined) {
			result += '"pruefungsdatum" : ' + JSON.stringify(obj.pruefungsdatum) + ',';
		}
		if (obj.referenzniveau !== undefined) {
			result += '"referenzniveau" : ' + JSON.stringify(obj.referenzniveau) + ',';
		}
		if (obj.note !== undefined) {
			result += '"note" : ' + JSON.stringify(obj.note) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schild3_reporting_SchildReportingSchuelerSprachpruefungen(obj : unknown) : SchildReportingSchuelerSprachpruefungen {
	return obj as SchildReportingSchuelerSprachpruefungen;
}
