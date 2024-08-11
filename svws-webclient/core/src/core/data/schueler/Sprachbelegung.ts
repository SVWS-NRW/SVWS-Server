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
		const obj = JSON.parse(json) as Partial<Sprachbelegung>;
		const result = new Sprachbelegung();
		if (obj.sprache === undefined)
			throw new Error('invalid json format, missing attribute sprache');
		result.sprache = obj.sprache;
		result.reihenfolge = (obj.reihenfolge === undefined) ? null : obj.reihenfolge === null ? null : obj.reihenfolge;
		result.belegungVonJahrgang = (obj.belegungVonJahrgang === undefined) ? null : obj.belegungVonJahrgang === null ? null : obj.belegungVonJahrgang;
		result.belegungVonAbschnitt = (obj.belegungVonAbschnitt === undefined) ? null : obj.belegungVonAbschnitt === null ? null : obj.belegungVonAbschnitt;
		result.belegungBisJahrgang = (obj.belegungBisJahrgang === undefined) ? null : obj.belegungBisJahrgang === null ? null : obj.belegungBisJahrgang;
		result.belegungBisAbschnitt = (obj.belegungBisAbschnitt === undefined) ? null : obj.belegungBisAbschnitt === null ? null : obj.belegungBisAbschnitt;
		result.referenzniveau = (obj.referenzniveau === undefined) ? null : obj.referenzniveau === null ? null : obj.referenzniveau;
		if (obj.hatKleinesLatinum === undefined)
			throw new Error('invalid json format, missing attribute hatKleinesLatinum');
		result.hatKleinesLatinum = obj.hatKleinesLatinum;
		if (obj.hatLatinum === undefined)
			throw new Error('invalid json format, missing attribute hatLatinum');
		result.hatLatinum = obj.hatLatinum;
		if (obj.hatGraecum === undefined)
			throw new Error('invalid json format, missing attribute hatGraecum');
		result.hatGraecum = obj.hatGraecum;
		if (obj.hatHebraicum === undefined)
			throw new Error('invalid json format, missing attribute hatHebraicum');
		result.hatHebraicum = obj.hatHebraicum;
		return result;
	}

	public static transpilerToJSON(obj : Sprachbelegung) : string {
		let result = '{';
		result += '"sprache" : ' + JSON.stringify(obj.sprache) + ',';
		result += '"reihenfolge" : ' + ((!obj.reihenfolge) ? 'null' : obj.reihenfolge.toString()) + ',';
		result += '"belegungVonJahrgang" : ' + ((!obj.belegungVonJahrgang) ? 'null' : JSON.stringify(obj.belegungVonJahrgang)) + ',';
		result += '"belegungVonAbschnitt" : ' + ((!obj.belegungVonAbschnitt) ? 'null' : obj.belegungVonAbschnitt.toString()) + ',';
		result += '"belegungBisJahrgang" : ' + ((!obj.belegungBisJahrgang) ? 'null' : JSON.stringify(obj.belegungBisJahrgang)) + ',';
		result += '"belegungBisAbschnitt" : ' + ((!obj.belegungBisAbschnitt) ? 'null' : obj.belegungBisAbschnitt.toString()) + ',';
		result += '"referenzniveau" : ' + ((!obj.referenzniveau) ? 'null' : JSON.stringify(obj.referenzniveau)) + ',';
		result += '"hatKleinesLatinum" : ' + obj.hatKleinesLatinum.toString() + ',';
		result += '"hatLatinum" : ' + obj.hatLatinum.toString() + ',';
		result += '"hatGraecum" : ' + obj.hatGraecum.toString() + ',';
		result += '"hatHebraicum" : ' + obj.hatHebraicum.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Sprachbelegung>) : string {
		let result = '{';
		if (obj.sprache !== undefined) {
			result += '"sprache" : ' + JSON.stringify(obj.sprache) + ',';
		}
		if (obj.reihenfolge !== undefined) {
			result += '"reihenfolge" : ' + ((!obj.reihenfolge) ? 'null' : obj.reihenfolge.toString()) + ',';
		}
		if (obj.belegungVonJahrgang !== undefined) {
			result += '"belegungVonJahrgang" : ' + ((!obj.belegungVonJahrgang) ? 'null' : JSON.stringify(obj.belegungVonJahrgang)) + ',';
		}
		if (obj.belegungVonAbschnitt !== undefined) {
			result += '"belegungVonAbschnitt" : ' + ((!obj.belegungVonAbschnitt) ? 'null' : obj.belegungVonAbschnitt.toString()) + ',';
		}
		if (obj.belegungBisJahrgang !== undefined) {
			result += '"belegungBisJahrgang" : ' + ((!obj.belegungBisJahrgang) ? 'null' : JSON.stringify(obj.belegungBisJahrgang)) + ',';
		}
		if (obj.belegungBisAbschnitt !== undefined) {
			result += '"belegungBisAbschnitt" : ' + ((!obj.belegungBisAbschnitt) ? 'null' : obj.belegungBisAbschnitt.toString()) + ',';
		}
		if (obj.referenzniveau !== undefined) {
			result += '"referenzniveau" : ' + ((!obj.referenzniveau) ? 'null' : JSON.stringify(obj.referenzniveau)) + ',';
		}
		if (obj.hatKleinesLatinum !== undefined) {
			result += '"hatKleinesLatinum" : ' + obj.hatKleinesLatinum.toString() + ',';
		}
		if (obj.hatLatinum !== undefined) {
			result += '"hatLatinum" : ' + obj.hatLatinum.toString() + ',';
		}
		if (obj.hatGraecum !== undefined) {
			result += '"hatGraecum" : ' + obj.hatGraecum.toString() + ',';
		}
		if (obj.hatHebraicum !== undefined) {
			result += '"hatHebraicum" : ' + obj.hatHebraicum.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schueler_Sprachbelegung(obj : unknown) : Sprachbelegung {
	return obj as Sprachbelegung;
}
