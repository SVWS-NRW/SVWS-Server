import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class ENMSprachenfolge extends JavaObject {

	/**
	 * Das Kürzel der Sprache, bereinigt von dem Jahrgang, in dem die Sprache eingesetzt hat
	 */
	public sprache : string | null = null;

	/**
	 * Die ID des Faches
	 */
	public fachID : number = 0;

	/**
	 * Das Kürzel des Faches
	 */
	public fachKuerzel : string | null = null;

	/**
	 * Die Reihenfolge des Faches in der Sprachenfolge (Beispiel 1)
	 */
	public reihenfolge : number = 0;

	/**
	 * Die Information, ab welchem Jahrgang die Sprache belegt wurde (Beispiel 5)
	 */
	public belegungVonJahrgang : number = 0;

	/**
	 * Die Information, ab welchem Abschnitt in dem Jahrgang die Sprache belegt wurde (Beispiel 1)
	 */
	public belegungVonAbschnitt : number = 0;

	/**
	 * Die Information, bis zu welchem Jahrgang die Sprache belegt wurde (Beispiel 12), sofern die Sprache bereits abgeschlossen ist
	 */
	public belegungBisJahrgang : number | null = null;

	/**
	 * Die Information, bis zu welchem Abschnitt in dem Jahrgang die Sprache belegt wurde (Beispiel 2), sofern die Sprache bereits abgeschlossen ist
	 */
	public belegungBisAbschnitt : number | null = null;

	/**
	 * Die Bezeichnung des Sprachreferenzniveaus, welches bisher erreicht wurde (z.B. B2/C1)
	 */
	public referenzniveau : string | null = null;

	/**
	 * Die Mindest-Dauer der Belegung in der Sekundarstufe I gemäß den Stufen im Core-Type SprachBelegungSekI (z.B. "0, 2, 4, 6")
	 */
	public belegungSekI : number | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMSprachenfolge';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMSprachenfolge'].includes(name);
	}

	public static class = new Class<ENMSprachenfolge>('de.svws_nrw.core.data.enm.ENMSprachenfolge');

	public static transpilerFromJSON(json : string): ENMSprachenfolge {
		const obj = JSON.parse(json) as Partial<ENMSprachenfolge>;
		const result = new ENMSprachenfolge();
		result.sprache = (obj.sprache === undefined) ? null : obj.sprache === null ? null : obj.sprache;
		if (obj.fachID === undefined)
			throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		result.fachKuerzel = (obj.fachKuerzel === undefined) ? null : obj.fachKuerzel === null ? null : obj.fachKuerzel;
		if (obj.reihenfolge === undefined)
			throw new Error('invalid json format, missing attribute reihenfolge');
		result.reihenfolge = obj.reihenfolge;
		if (obj.belegungVonJahrgang === undefined)
			throw new Error('invalid json format, missing attribute belegungVonJahrgang');
		result.belegungVonJahrgang = obj.belegungVonJahrgang;
		if (obj.belegungVonAbschnitt === undefined)
			throw new Error('invalid json format, missing attribute belegungVonAbschnitt');
		result.belegungVonAbschnitt = obj.belegungVonAbschnitt;
		result.belegungBisJahrgang = (obj.belegungBisJahrgang === undefined) ? null : obj.belegungBisJahrgang === null ? null : obj.belegungBisJahrgang;
		result.belegungBisAbschnitt = (obj.belegungBisAbschnitt === undefined) ? null : obj.belegungBisAbschnitt === null ? null : obj.belegungBisAbschnitt;
		result.referenzniveau = (obj.referenzniveau === undefined) ? null : obj.referenzniveau === null ? null : obj.referenzniveau;
		result.belegungSekI = (obj.belegungSekI === undefined) ? null : obj.belegungSekI === null ? null : obj.belegungSekI;
		return result;
	}

	public static transpilerToJSON(obj : ENMSprachenfolge) : string {
		let result = '{';
		result += '"sprache" : ' + ((obj.sprache === null) ? 'null' : JSON.stringify(obj.sprache)) + ',';
		result += '"fachID" : ' + obj.fachID.toString() + ',';
		result += '"fachKuerzel" : ' + ((obj.fachKuerzel === null) ? 'null' : JSON.stringify(obj.fachKuerzel)) + ',';
		result += '"reihenfolge" : ' + obj.reihenfolge.toString() + ',';
		result += '"belegungVonJahrgang" : ' + obj.belegungVonJahrgang.toString() + ',';
		result += '"belegungVonAbschnitt" : ' + obj.belegungVonAbschnitt.toString() + ',';
		result += '"belegungBisJahrgang" : ' + ((obj.belegungBisJahrgang === null) ? 'null' : obj.belegungBisJahrgang.toString()) + ',';
		result += '"belegungBisAbschnitt" : ' + ((obj.belegungBisAbschnitt === null) ? 'null' : obj.belegungBisAbschnitt.toString()) + ',';
		result += '"referenzniveau" : ' + ((obj.referenzniveau === null) ? 'null' : JSON.stringify(obj.referenzniveau)) + ',';
		result += '"belegungSekI" : ' + ((obj.belegungSekI === null) ? 'null' : obj.belegungSekI.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMSprachenfolge>) : string {
		let result = '{';
		if (obj.sprache !== undefined) {
			result += '"sprache" : ' + ((obj.sprache === null) ? 'null' : JSON.stringify(obj.sprache)) + ',';
		}
		if (obj.fachID !== undefined) {
			result += '"fachID" : ' + obj.fachID.toString() + ',';
		}
		if (obj.fachKuerzel !== undefined) {
			result += '"fachKuerzel" : ' + ((obj.fachKuerzel === null) ? 'null' : JSON.stringify(obj.fachKuerzel)) + ',';
		}
		if (obj.reihenfolge !== undefined) {
			result += '"reihenfolge" : ' + obj.reihenfolge.toString() + ',';
		}
		if (obj.belegungVonJahrgang !== undefined) {
			result += '"belegungVonJahrgang" : ' + obj.belegungVonJahrgang.toString() + ',';
		}
		if (obj.belegungVonAbschnitt !== undefined) {
			result += '"belegungVonAbschnitt" : ' + obj.belegungVonAbschnitt.toString() + ',';
		}
		if (obj.belegungBisJahrgang !== undefined) {
			result += '"belegungBisJahrgang" : ' + ((obj.belegungBisJahrgang === null) ? 'null' : obj.belegungBisJahrgang.toString()) + ',';
		}
		if (obj.belegungBisAbschnitt !== undefined) {
			result += '"belegungBisAbschnitt" : ' + ((obj.belegungBisAbschnitt === null) ? 'null' : obj.belegungBisAbschnitt.toString()) + ',';
		}
		if (obj.referenzniveau !== undefined) {
			result += '"referenzniveau" : ' + ((obj.referenzniveau === null) ? 'null' : JSON.stringify(obj.referenzniveau)) + ',';
		}
		if (obj.belegungSekI !== undefined) {
			result += '"belegungSekI" : ' + ((obj.belegungSekI === null) ? 'null' : obj.belegungSekI.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMSprachenfolge(obj : unknown) : ENMSprachenfolge {
	return obj as ENMSprachenfolge;
}
