import { JavaObject } from '../../../java/lang/JavaObject';

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

	/**
	 * Gibt an, ob das kleine Latinum erreicht wurde oder nicht.
	 */
	public hatKleinesLatinum : boolean = false;

	/**
	 * Gibt an, ob das Latinum erreicht wurde oder nicht.
	 */
	public hatLatinum : boolean = false;

	/**
	 * Gibt an, ob das Graecum erreicht wurde oder nicht.
	 */
	public hatGraecum : boolean = false;

	/**
	 * Gibt an, ob das Hebraicum erreicht wurde oder nicht.
	 */
	public hatHebraicum : boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schueler.Sprachbelegung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.Sprachbelegung'].includes(name);
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
		if (typeof obj.hatKleinesLatinum === "undefined")
			 throw new Error('invalid json format, missing attribute hatKleinesLatinum');
		result.hatKleinesLatinum = obj.hatKleinesLatinum;
		if (typeof obj.hatLatinum === "undefined")
			 throw new Error('invalid json format, missing attribute hatLatinum');
		result.hatLatinum = obj.hatLatinum;
		if (typeof obj.hatGraecum === "undefined")
			 throw new Error('invalid json format, missing attribute hatGraecum');
		result.hatGraecum = obj.hatGraecum;
		if (typeof obj.hatHebraicum === "undefined")
			 throw new Error('invalid json format, missing attribute hatHebraicum');
		result.hatHebraicum = obj.hatHebraicum;
		return result;
	}

	public static transpilerToJSON(obj : Sprachbelegung) : string {
		let result = '{';
		result += '"sprache" : ' + JSON.stringify(obj.sprache!) + ',';
		result += '"reihenfolge" : ' + ((!obj.reihenfolge) ? 'null' : obj.reihenfolge) + ',';
		result += '"belegungVonJahrgang" : ' + ((!obj.belegungVonJahrgang) ? 'null' : JSON.stringify(obj.belegungVonJahrgang)) + ',';
		result += '"belegungVonAbschnitt" : ' + ((!obj.belegungVonAbschnitt) ? 'null' : obj.belegungVonAbschnitt) + ',';
		result += '"belegungBisJahrgang" : ' + ((!obj.belegungBisJahrgang) ? 'null' : JSON.stringify(obj.belegungBisJahrgang)) + ',';
		result += '"belegungBisAbschnitt" : ' + ((!obj.belegungBisAbschnitt) ? 'null' : obj.belegungBisAbschnitt) + ',';
		result += '"referenzniveau" : ' + ((!obj.referenzniveau) ? 'null' : JSON.stringify(obj.referenzniveau)) + ',';
		result += '"hatKleinesLatinum" : ' + obj.hatKleinesLatinum + ',';
		result += '"hatLatinum" : ' + obj.hatLatinum + ',';
		result += '"hatGraecum" : ' + obj.hatGraecum + ',';
		result += '"hatHebraicum" : ' + obj.hatHebraicum + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Sprachbelegung>) : string {
		let result = '{';
		if (typeof obj.sprache !== "undefined") {
			result += '"sprache" : ' + JSON.stringify(obj.sprache!) + ',';
		}
		if (typeof obj.reihenfolge !== "undefined") {
			result += '"reihenfolge" : ' + ((!obj.reihenfolge) ? 'null' : obj.reihenfolge) + ',';
		}
		if (typeof obj.belegungVonJahrgang !== "undefined") {
			result += '"belegungVonJahrgang" : ' + ((!obj.belegungVonJahrgang) ? 'null' : JSON.stringify(obj.belegungVonJahrgang)) + ',';
		}
		if (typeof obj.belegungVonAbschnitt !== "undefined") {
			result += '"belegungVonAbschnitt" : ' + ((!obj.belegungVonAbschnitt) ? 'null' : obj.belegungVonAbschnitt) + ',';
		}
		if (typeof obj.belegungBisJahrgang !== "undefined") {
			result += '"belegungBisJahrgang" : ' + ((!obj.belegungBisJahrgang) ? 'null' : JSON.stringify(obj.belegungBisJahrgang)) + ',';
		}
		if (typeof obj.belegungBisAbschnitt !== "undefined") {
			result += '"belegungBisAbschnitt" : ' + ((!obj.belegungBisAbschnitt) ? 'null' : obj.belegungBisAbschnitt) + ',';
		}
		if (typeof obj.referenzniveau !== "undefined") {
			result += '"referenzniveau" : ' + ((!obj.referenzniveau) ? 'null' : JSON.stringify(obj.referenzniveau)) + ',';
		}
		if (typeof obj.hatKleinesLatinum !== "undefined") {
			result += '"hatKleinesLatinum" : ' + obj.hatKleinesLatinum + ',';
		}
		if (typeof obj.hatLatinum !== "undefined") {
			result += '"hatLatinum" : ' + obj.hatLatinum + ',';
		}
		if (typeof obj.hatGraecum !== "undefined") {
			result += '"hatGraecum" : ' + obj.hatGraecum + ',';
		}
		if (typeof obj.hatHebraicum !== "undefined") {
			result += '"hatHebraicum" : ' + obj.hatHebraicum + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schueler_Sprachbelegung(obj : unknown) : Sprachbelegung {
	return obj as Sprachbelegung;
}
