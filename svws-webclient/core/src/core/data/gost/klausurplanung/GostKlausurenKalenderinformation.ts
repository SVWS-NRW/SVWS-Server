import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class GostKlausurenKalenderinformation extends JavaObject {

	/**
	 * Die ID der Kalenderinformation.
	 */
	public id : number = -1;

	/**
	 * Die Bezeichnung der Kalenderinformation.
	 */
	public bezeichnung : string = "";

	/**
	 * Das Startdatum der Kalenderinformation.
	 */
	public startdatum : string | null = null;

	/**
	 * Die Startzeit der Kalenderinformation in Minuten seit 0 Uhr.
	 */
	public startzeit : number | null = null;

	/**
	 * Das Enddatum der Kalenderinformation.
	 */
	public enddatum : string | null = null;

	/**
	 * Die Endzeit der Kalenderinformation in Minuten seit 0 Uhr.
	 */
	public endzeit : number | null = null;

	/**
	 * Die textuelle Bemerkung zur Kalenderinformation, sofern vorhanden.
	 */
	public bemerkung : string | null = null;

	/**
	 * Die Information, ob es sich um einen Sperrtermin handelt.
	 */
	public istSperrtermin : boolean = false;


	/**
	 * Default-Konstruktor
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenKalenderinformation';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenKalenderinformation'].includes(name);
	}

	public static class = new Class<GostKlausurenKalenderinformation>('de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenKalenderinformation');

	public static transpilerFromJSON(json : string): GostKlausurenKalenderinformation {
		const obj = JSON.parse(json) as Partial<GostKlausurenKalenderinformation>;
		const result = new GostKlausurenKalenderinformation();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		result.startdatum = (obj.startdatum === undefined) ? null : obj.startdatum === null ? null : obj.startdatum;
		result.startzeit = (obj.startzeit === undefined) ? null : obj.startzeit === null ? null : obj.startzeit;
		result.enddatum = (obj.enddatum === undefined) ? null : obj.enddatum === null ? null : obj.enddatum;
		result.endzeit = (obj.endzeit === undefined) ? null : obj.endzeit === null ? null : obj.endzeit;
		result.bemerkung = (obj.bemerkung === undefined) ? null : obj.bemerkung === null ? null : obj.bemerkung;
		if (obj.istSperrtermin === undefined)
			throw new Error('invalid json format, missing attribute istSperrtermin');
		result.istSperrtermin = obj.istSperrtermin;
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurenKalenderinformation) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"startdatum" : ' + ((obj.startdatum === null) ? 'null' : JSON.stringify(obj.startdatum)) + ',';
		result += '"startzeit" : ' + ((obj.startzeit === null) ? 'null' : obj.startzeit.toString()) + ',';
		result += '"enddatum" : ' + ((obj.enddatum === null) ? 'null' : JSON.stringify(obj.enddatum)) + ',';
		result += '"endzeit" : ' + ((obj.endzeit === null) ? 'null' : obj.endzeit.toString()) + ',';
		result += '"bemerkung" : ' + ((obj.bemerkung === null) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		result += '"istSperrtermin" : ' + obj.istSperrtermin.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurenKalenderinformation>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.startdatum !== undefined) {
			result += '"startdatum" : ' + ((obj.startdatum === null) ? 'null' : JSON.stringify(obj.startdatum)) + ',';
		}
		if (obj.startzeit !== undefined) {
			result += '"startzeit" : ' + ((obj.startzeit === null) ? 'null' : obj.startzeit.toString()) + ',';
		}
		if (obj.enddatum !== undefined) {
			result += '"enddatum" : ' + ((obj.enddatum === null) ? 'null' : JSON.stringify(obj.enddatum)) + ',';
		}
		if (obj.endzeit !== undefined) {
			result += '"endzeit" : ' + ((obj.endzeit === null) ? 'null' : obj.endzeit.toString()) + ',';
		}
		if (obj.bemerkung !== undefined) {
			result += '"bemerkung" : ' + ((obj.bemerkung === null) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		}
		if (obj.istSperrtermin !== undefined) {
			result += '"istSperrtermin" : ' + obj.istSperrtermin.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurenKalenderinformation(obj : unknown) : GostKlausurenKalenderinformation {
	return obj as GostKlausurenKalenderinformation;
}
