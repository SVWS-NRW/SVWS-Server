import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Sprachbelegung extends JavaObject {

	/**
	 * Das einstellige Sprachkürzel des belegten Faches 
	 */
	public sprache : String = "";

	/**
	 * Gibt an, an welcher Stelle in der Sprachenfolge die Sprache begonnen wurde 
	 */
	public reihenfolge : Number | null = null;

	/**
	 * Der Jahrgang, in dem die Sprache zum ersten mal belegt wurde 
	 */
	public belegungVonJahrgang : String | null = null;

	/**
	 * Der Abschnitt des Jahrganges, in welchem die Sprache zum ersten mal belegt wurde 
	 */
	public belegungVonAbschnitt : Number | null = null;

	/**
	 * Der Jahrgang, in dem die Belegung der Sprache beendet wurde - sofern sie schon beendet wurde 
	 */
	public belegungBisJahrgang : String | null = null;

	/**
	 * Der Abschnitt des Jahrgangs, in dem die Belegung der Sprache beendet wurde - sofern sie schon beendet wurde 
	 */
	public belegungBisAbschnitt : Number | null = null;

	/**
	 * Das Referenzniveau, welches bisher erreicht wurde 
	 */
	public referenzniveau : String | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schueler.Sprachbelegung'].includes(name);
	}

	public static transpilerFromJSON(json : string): Sprachbelegung {
		const obj = JSON.parse(json);
		const result = new Sprachbelegung();
		if (typeof obj.sprache === "undefined")
			 throw new Error('invalid json format, missing attribute sprache');
		result.sprache = String(obj.sprache);
		result.reihenfolge = typeof obj.reihenfolge === "undefined" ? null : obj.reihenfolge === null ? null : Number(obj.reihenfolge);
		result.belegungVonJahrgang = typeof obj.belegungVonJahrgang === "undefined" ? null : obj.belegungVonJahrgang === null ? null : String(obj.belegungVonJahrgang);
		result.belegungVonAbschnitt = typeof obj.belegungVonAbschnitt === "undefined" ? null : obj.belegungVonAbschnitt === null ? null : Number(obj.belegungVonAbschnitt);
		result.belegungBisJahrgang = typeof obj.belegungBisJahrgang === "undefined" ? null : obj.belegungBisJahrgang === null ? null : String(obj.belegungBisJahrgang);
		result.belegungBisAbschnitt = typeof obj.belegungBisAbschnitt === "undefined" ? null : obj.belegungBisAbschnitt === null ? null : Number(obj.belegungBisAbschnitt);
		result.referenzniveau = typeof obj.referenzniveau === "undefined" ? null : obj.referenzniveau === null ? null : String(obj.referenzniveau);
		return result;
	}

	public static transpilerToJSON(obj : Sprachbelegung) : string {
		let result = '{';
		result += '"sprache" : ' + '"' + obj.sprache.valueOf() + '"' + ',';
		result += '"reihenfolge" : ' + ((!obj.reihenfolge) ? 'null' : obj.reihenfolge.valueOf()) + ',';
		result += '"belegungVonJahrgang" : ' + ((!obj.belegungVonJahrgang) ? 'null' : '"' + obj.belegungVonJahrgang.valueOf() + '"') + ',';
		result += '"belegungVonAbschnitt" : ' + ((!obj.belegungVonAbschnitt) ? 'null' : obj.belegungVonAbschnitt.valueOf()) + ',';
		result += '"belegungBisJahrgang" : ' + ((!obj.belegungBisJahrgang) ? 'null' : '"' + obj.belegungBisJahrgang.valueOf() + '"') + ',';
		result += '"belegungBisAbschnitt" : ' + ((!obj.belegungBisAbschnitt) ? 'null' : obj.belegungBisAbschnitt.valueOf()) + ',';
		result += '"referenzniveau" : ' + ((!obj.referenzniveau) ? 'null' : '"' + obj.referenzniveau.valueOf() + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Sprachbelegung>) : string {
		let result = '{';
		if (typeof obj.sprache !== "undefined") {
			result += '"sprache" : ' + '"' + obj.sprache.valueOf() + '"' + ',';
		}
		if (typeof obj.reihenfolge !== "undefined") {
			result += '"reihenfolge" : ' + ((!obj.reihenfolge) ? 'null' : obj.reihenfolge.valueOf()) + ',';
		}
		if (typeof obj.belegungVonJahrgang !== "undefined") {
			result += '"belegungVonJahrgang" : ' + ((!obj.belegungVonJahrgang) ? 'null' : '"' + obj.belegungVonJahrgang.valueOf() + '"') + ',';
		}
		if (typeof obj.belegungVonAbschnitt !== "undefined") {
			result += '"belegungVonAbschnitt" : ' + ((!obj.belegungVonAbschnitt) ? 'null' : obj.belegungVonAbschnitt.valueOf()) + ',';
		}
		if (typeof obj.belegungBisJahrgang !== "undefined") {
			result += '"belegungBisJahrgang" : ' + ((!obj.belegungBisJahrgang) ? 'null' : '"' + obj.belegungBisJahrgang.valueOf() + '"') + ',';
		}
		if (typeof obj.belegungBisAbschnitt !== "undefined") {
			result += '"belegungBisAbschnitt" : ' + ((!obj.belegungBisAbschnitt) ? 'null' : obj.belegungBisAbschnitt.valueOf()) + ',';
		}
		if (typeof obj.referenzniveau !== "undefined") {
			result += '"referenzniveau" : ' + ((!obj.referenzniveau) ? 'null' : '"' + obj.referenzniveau.valueOf() + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schueler_Sprachbelegung(obj : unknown) : Sprachbelegung {
	return obj as Sprachbelegung;
}
