import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuleInfo extends JavaObject {

	/**
	 * Die eindeutige Schulnummer der Schule
	 */
	public schulNr : number = 0;

	/**
	 * Die Schulform der Schule
	 */
	public schulform : string = "";

	/**
	 * Die Bezeichnung der Schule
	 */
	public bezeichnung : string = "";

	/**
	 * Der Straßenname der Straße in der die Schule liegt.
	 */
	public strassenname : string | null = null;

	/**
	 * Die Hausnummer zur Straße in der die Schule liegt.
	 */
	public hausnummer : string | null = null;

	/**
	 * Ggf. der Hausnummerzusatz zur Straße in der die Schule liegt.
	 */
	public hausnummerZusatz : string | null = null;

	/**
	 * Die Postleitzahl des Gebietes in dem die Schule liegt.
	 */
	public plz : string | null = null;

	/**
	 * Der Ort in dem die Schule liegt.
	 */
	public ort : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.SchuleInfo';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.SchuleInfo'].includes(name);
	}

	public static class = new Class<SchuleInfo>('de.svws_nrw.core.data.schule.SchuleInfo');

	public static transpilerFromJSON(json : string): SchuleInfo {
		const obj = JSON.parse(json) as Partial<SchuleInfo>;
		const result = new SchuleInfo();
		if (obj.schulNr === undefined)
			throw new Error('invalid json format, missing attribute schulNr');
		result.schulNr = obj.schulNr;
		if (obj.schulform === undefined)
			throw new Error('invalid json format, missing attribute schulform');
		result.schulform = obj.schulform;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		result.strassenname = (obj.strassenname === undefined) ? null : obj.strassenname === null ? null : obj.strassenname;
		result.hausnummer = (obj.hausnummer === undefined) ? null : obj.hausnummer === null ? null : obj.hausnummer;
		result.hausnummerZusatz = (obj.hausnummerZusatz === undefined) ? null : obj.hausnummerZusatz === null ? null : obj.hausnummerZusatz;
		result.plz = (obj.plz === undefined) ? null : obj.plz === null ? null : obj.plz;
		result.ort = (obj.ort === undefined) ? null : obj.ort === null ? null : obj.ort;
		return result;
	}

	public static transpilerToJSON(obj : SchuleInfo) : string {
		let result = '{';
		result += '"schulNr" : ' + obj.schulNr.toString() + ',';
		result += '"schulform" : ' + JSON.stringify(obj.schulform) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"strassenname" : ' + ((obj.strassenname === null) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		result += '"hausnummer" : ' + ((obj.hausnummer === null) ? 'null' : JSON.stringify(obj.hausnummer)) + ',';
		result += '"hausnummerZusatz" : ' + ((obj.hausnummerZusatz === null) ? 'null' : JSON.stringify(obj.hausnummerZusatz)) + ',';
		result += '"plz" : ' + ((obj.plz === null) ? 'null' : JSON.stringify(obj.plz)) + ',';
		result += '"ort" : ' + ((obj.ort === null) ? 'null' : JSON.stringify(obj.ort)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuleInfo>) : string {
		let result = '{';
		if (obj.schulNr !== undefined) {
			result += '"schulNr" : ' + obj.schulNr.toString() + ',';
		}
		if (obj.schulform !== undefined) {
			result += '"schulform" : ' + JSON.stringify(obj.schulform) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.strassenname !== undefined) {
			result += '"strassenname" : ' + ((obj.strassenname === null) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		}
		if (obj.hausnummer !== undefined) {
			result += '"hausnummer" : ' + ((obj.hausnummer === null) ? 'null' : JSON.stringify(obj.hausnummer)) + ',';
		}
		if (obj.hausnummerZusatz !== undefined) {
			result += '"hausnummerZusatz" : ' + ((obj.hausnummerZusatz === null) ? 'null' : JSON.stringify(obj.hausnummerZusatz)) + ',';
		}
		if (obj.plz !== undefined) {
			result += '"plz" : ' + ((obj.plz === null) ? 'null' : JSON.stringify(obj.plz)) + ',';
		}
		if (obj.ort !== undefined) {
			result += '"ort" : ' + ((obj.ort === null) ? 'null' : JSON.stringify(obj.ort)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_SchuleInfo(obj : unknown) : SchuleInfo {
	return obj as SchuleInfo;
}
