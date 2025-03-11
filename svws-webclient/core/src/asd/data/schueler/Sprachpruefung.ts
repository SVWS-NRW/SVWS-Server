import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class Sprachpruefung extends JavaObject {

	/**
	 * Das einstellige Sprachkürzel des geprüften Faches
	 */
	public sprache : string = "";

	/**
	 * Gibt an, in welchem ASD-Jahrgang die Prüfung abgelegt wurde
	 */
	public jahrgang : string | null = null;

	/**
	 * ID der Bezeichnung des am Schulabschluss orientierten Anspruchsniveau der Sprachprüfung
	 */
	public anspruchsniveauId : number | null = null;

	/**
	 * Gibt das Datum an, an dem die Prüfung abgelegt wurde
	 */
	public pruefungsdatum : string | null = null;

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
	 * Durch die Prüfung kann die Sprache als fortgeführte Fremdsprache in der GOSt belegt werden
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


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schueler.Sprachpruefung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.schueler.Sprachpruefung'].includes(name);
	}

	public static class = new Class<Sprachpruefung>('de.svws_nrw.asd.data.schueler.Sprachpruefung');

	public static transpilerFromJSON(json : string): Sprachpruefung {
		const obj = JSON.parse(json) as Partial<Sprachpruefung>;
		const result = new Sprachpruefung();
		if (obj.sprache === undefined)
			throw new Error('invalid json format, missing attribute sprache');
		result.sprache = obj.sprache;
		result.jahrgang = (obj.jahrgang === undefined) ? null : obj.jahrgang === null ? null : obj.jahrgang;
		result.anspruchsniveauId = (obj.anspruchsniveauId === undefined) ? null : obj.anspruchsniveauId === null ? null : obj.anspruchsniveauId;
		result.pruefungsdatum = (obj.pruefungsdatum === undefined) ? null : obj.pruefungsdatum === null ? null : obj.pruefungsdatum;
		result.ersetzteSprache = (obj.ersetzteSprache === undefined) ? null : obj.ersetzteSprache === null ? null : obj.ersetzteSprache;
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
		result.referenzniveau = (obj.referenzniveau === undefined) ? null : obj.referenzniveau === null ? null : obj.referenzniveau;
		result.note = (obj.note === undefined) ? null : obj.note === null ? null : obj.note;
		return result;
	}

	public static transpilerToJSON(obj : Sprachpruefung) : string {
		let result = '{';
		result += '"sprache" : ' + JSON.stringify(obj.sprache) + ',';
		result += '"jahrgang" : ' + ((obj.jahrgang === null) ? 'null' : JSON.stringify(obj.jahrgang)) + ',';
		result += '"anspruchsniveauId" : ' + ((obj.anspruchsniveauId === null) ? 'null' : obj.anspruchsniveauId.toString()) + ',';
		result += '"pruefungsdatum" : ' + ((obj.pruefungsdatum === null) ? 'null' : JSON.stringify(obj.pruefungsdatum)) + ',';
		result += '"ersetzteSprache" : ' + ((obj.ersetzteSprache === null) ? 'null' : JSON.stringify(obj.ersetzteSprache)) + ',';
		result += '"istHSUPruefung" : ' + obj.istHSUPruefung.toString() + ',';
		result += '"istFeststellungspruefung" : ' + obj.istFeststellungspruefung.toString() + ',';
		result += '"kannErstePflichtfremdspracheErsetzen" : ' + obj.kannErstePflichtfremdspracheErsetzen.toString() + ',';
		result += '"kannZweitePflichtfremdspracheErsetzen" : ' + obj.kannZweitePflichtfremdspracheErsetzen.toString() + ',';
		result += '"kannWahlpflichtfremdspracheErsetzen" : ' + obj.kannWahlpflichtfremdspracheErsetzen.toString() + ',';
		result += '"kannBelegungAlsFortgefuehrteSpracheErlauben" : ' + obj.kannBelegungAlsFortgefuehrteSpracheErlauben.toString() + ',';
		result += '"referenzniveau" : ' + ((obj.referenzniveau === null) ? 'null' : JSON.stringify(obj.referenzniveau)) + ',';
		result += '"note" : ' + ((obj.note === null) ? 'null' : obj.note.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Sprachpruefung>) : string {
		let result = '{';
		if (obj.sprache !== undefined) {
			result += '"sprache" : ' + JSON.stringify(obj.sprache) + ',';
		}
		if (obj.jahrgang !== undefined) {
			result += '"jahrgang" : ' + ((obj.jahrgang === null) ? 'null' : JSON.stringify(obj.jahrgang)) + ',';
		}
		if (obj.anspruchsniveauId !== undefined) {
			result += '"anspruchsniveauId" : ' + ((obj.anspruchsniveauId === null) ? 'null' : obj.anspruchsniveauId.toString()) + ',';
		}
		if (obj.pruefungsdatum !== undefined) {
			result += '"pruefungsdatum" : ' + ((obj.pruefungsdatum === null) ? 'null' : JSON.stringify(obj.pruefungsdatum)) + ',';
		}
		if (obj.ersetzteSprache !== undefined) {
			result += '"ersetzteSprache" : ' + ((obj.ersetzteSprache === null) ? 'null' : JSON.stringify(obj.ersetzteSprache)) + ',';
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
		if (obj.referenzniveau !== undefined) {
			result += '"referenzniveau" : ' + ((obj.referenzniveau === null) ? 'null' : JSON.stringify(obj.referenzniveau)) + ',';
		}
		if (obj.note !== undefined) {
			result += '"note" : ' + ((obj.note === null) ? 'null' : obj.note.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schueler_Sprachpruefung(obj : unknown) : Sprachpruefung {
	return obj as Sprachpruefung;
}
