import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Sprachbelegung extends JavaObject {

	/**
	 * Das einstellige Sprachkürzel des belegten Faches
	 */
	public sprache : string = "";

	/**
	 * Gibt an, an welcher Stelle in der Sprachenfolge die Sprache begonnen wurde
	 */
	public reihenfolge : number | null = null;

	/**
	 * Der Jahrgang, in dem die Sprache zum ersten mal belegt wurde
	 */
	public belegungVonJahrgang : string | null = null;

	/**
	 * Der Abschnitt des Jahrganges, in welchem die Sprache zum ersten mal belegt wurde
	 */
	public belegungVonAbschnitt : number | null = null;

	/**
	 * Der Jahrgang, in dem die Belegung der Sprache beendet wurde - sofern sie schon beendet wurde
	 */
	public belegungBisJahrgang : string | null = null;

	/**
	 * Der Abschnitt des Jahrgangs, in dem die Belegung der Sprache beendet wurde - sofern sie schon beendet wurde
	 */
	public belegungBisAbschnitt : number | null = null;

	/**
	 * Das Referenzniveau, welches bisher erreicht wurde
	 */
	public referenzniveau : string | null = null;


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
		result.sprache = obj.sprache;
		result.reihenfolge = typeof obj.reihenfolge === "undefined" ? null : obj.reihenfolge === null ? null : obj.reihenfolge;
		result.belegungVonJahrgang = typeof obj.belegungVonJahrgang === "undefined" ? null : obj.belegungVonJahrgang === null ? null : obj.belegungVonJahrgang;
		result.belegungVonAbschnitt = typeof obj.belegungVonAbschnitt === "undefined" ? null : obj.belegungVonAbschnitt === null ? null : obj.belegungVonAbschnitt;
		result.belegungBisJahrgang = typeof obj.belegungBisJahrgang === "undefined" ? null : obj.belegungBisJahrgang === null ? null : obj.belegungBisJahrgang;
		result.belegungBisAbschnitt = typeof obj.belegungBisAbschnitt === "undefined" ? null : obj.belegungBisAbschnitt === null ? null : obj.belegungBisAbschnitt;
		result.referenzniveau = typeof obj.referenzniveau === "undefined" ? null : obj.referenzniveau === null ? null : obj.referenzniveau;
		return result;
	}

	public static transpilerToJSON(obj : Sprachbelegung) : string {
		let result = '{';
		result += '"sprache" : ' + '"' + obj.sprache! + '"' + ',';
		result += '"reihenfolge" : ' + ((!obj.reihenfolge) ? 'null' : obj.reihenfolge) + ',';
		result += '"belegungVonJahrgang" : ' + ((!obj.belegungVonJahrgang) ? 'null' : '"' + obj.belegungVonJahrgang + '"') + ',';
		result += '"belegungVonAbschnitt" : ' + ((!obj.belegungVonAbschnitt) ? 'null' : obj.belegungVonAbschnitt) + ',';
		result += '"belegungBisJahrgang" : ' + ((!obj.belegungBisJahrgang) ? 'null' : '"' + obj.belegungBisJahrgang + '"') + ',';
		result += '"belegungBisAbschnitt" : ' + ((!obj.belegungBisAbschnitt) ? 'null' : obj.belegungBisAbschnitt) + ',';
		result += '"referenzniveau" : ' + ((!obj.referenzniveau) ? 'null' : '"' + obj.referenzniveau + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Sprachbelegung>) : string {
		let result = '{';
		if (typeof obj.sprache !== "undefined") {
			result += '"sprache" : ' + '"' + obj.sprache + '"' + ',';
		}
		if (typeof obj.reihenfolge !== "undefined") {
			result += '"reihenfolge" : ' + ((!obj.reihenfolge) ? 'null' : obj.reihenfolge) + ',';
		}
		if (typeof obj.belegungVonJahrgang !== "undefined") {
			result += '"belegungVonJahrgang" : ' + ((!obj.belegungVonJahrgang) ? 'null' : '"' + obj.belegungVonJahrgang + '"') + ',';
		}
		if (typeof obj.belegungVonAbschnitt !== "undefined") {
			result += '"belegungVonAbschnitt" : ' + ((!obj.belegungVonAbschnitt) ? 'null' : obj.belegungVonAbschnitt) + ',';
		}
		if (typeof obj.belegungBisJahrgang !== "undefined") {
			result += '"belegungBisJahrgang" : ' + ((!obj.belegungBisJahrgang) ? 'null' : '"' + obj.belegungBisJahrgang + '"') + ',';
		}
		if (typeof obj.belegungBisAbschnitt !== "undefined") {
			result += '"belegungBisAbschnitt" : ' + ((!obj.belegungBisAbschnitt) ? 'null' : obj.belegungBisAbschnitt) + ',';
		}
		if (typeof obj.referenzniveau !== "undefined") {
			result += '"referenzniveau" : ' + ((!obj.referenzniveau) ? 'null' : '"' + obj.referenzniveau + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schueler_Sprachbelegung(obj : unknown) : Sprachbelegung {
	return obj as Sprachbelegung;
}
