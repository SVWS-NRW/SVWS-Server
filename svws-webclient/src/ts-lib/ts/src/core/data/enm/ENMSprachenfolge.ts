import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class ENMSprachenfolge extends JavaObject {

	public sprache : String | null = null;

	public fachID : number = 0;

	public fachKuerzel : String | null = null;

	public reihenfolge : number = 0;

	public belegungVonJahrgang : number = 0;

	public belegungVonAbschnitt : number = 0;

	public belegungBisJahrgang : Number | null = null;

	public belegungBisAbschnitt : Number | null = null;

	public referenzniveau : String | null = null;

	public belegungSekI : Number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.enm.ENMSprachenfolge'].includes(name);
	}

	public static transpilerFromJSON(json : string): ENMSprachenfolge {
		const obj = JSON.parse(json);
		const result = new ENMSprachenfolge();
		result.sprache = typeof obj.sprache === "undefined" ? null : obj.sprache;
		if (typeof obj.fachID === "undefined")
			 throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		result.fachKuerzel = typeof obj.fachKuerzel === "undefined" ? null : obj.fachKuerzel;
		if (typeof obj.reihenfolge === "undefined")
			 throw new Error('invalid json format, missing attribute reihenfolge');
		result.reihenfolge = obj.reihenfolge;
		if (typeof obj.belegungVonJahrgang === "undefined")
			 throw new Error('invalid json format, missing attribute belegungVonJahrgang');
		result.belegungVonJahrgang = obj.belegungVonJahrgang;
		if (typeof obj.belegungVonAbschnitt === "undefined")
			 throw new Error('invalid json format, missing attribute belegungVonAbschnitt');
		result.belegungVonAbschnitt = obj.belegungVonAbschnitt;
		result.belegungBisJahrgang = typeof obj.belegungBisJahrgang === "undefined" ? null : obj.belegungBisJahrgang;
		result.belegungBisAbschnitt = typeof obj.belegungBisAbschnitt === "undefined" ? null : obj.belegungBisAbschnitt;
		result.referenzniveau = typeof obj.referenzniveau === "undefined" ? null : obj.referenzniveau;
		result.belegungSekI = typeof obj.belegungSekI === "undefined" ? null : obj.belegungSekI;
		return result;
	}

	public static transpilerToJSON(obj : ENMSprachenfolge) : string {
		let result = '{';
		result += '"sprache" : ' + ((!obj.sprache) ? 'null' : '"' + obj.sprache.valueOf() + '"') + ',';
		result += '"fachID" : ' + obj.fachID + ',';
		result += '"fachKuerzel" : ' + ((!obj.fachKuerzel) ? 'null' : '"' + obj.fachKuerzel.valueOf() + '"') + ',';
		result += '"reihenfolge" : ' + obj.reihenfolge + ',';
		result += '"belegungVonJahrgang" : ' + obj.belegungVonJahrgang + ',';
		result += '"belegungVonAbschnitt" : ' + obj.belegungVonAbschnitt + ',';
		result += '"belegungBisJahrgang" : ' + ((!obj.belegungBisJahrgang) ? 'null' : obj.belegungBisJahrgang.valueOf()) + ',';
		result += '"belegungBisAbschnitt" : ' + ((!obj.belegungBisAbschnitt) ? 'null' : obj.belegungBisAbschnitt.valueOf()) + ',';
		result += '"referenzniveau" : ' + ((!obj.referenzniveau) ? 'null' : '"' + obj.referenzniveau.valueOf() + '"') + ',';
		result += '"belegungSekI" : ' + ((!obj.belegungSekI) ? 'null' : obj.belegungSekI.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMSprachenfolge>) : string {
		let result = '{';
		if (typeof obj.sprache !== "undefined") {
			result += '"sprache" : ' + ((!obj.sprache) ? 'null' : '"' + obj.sprache.valueOf() + '"') + ',';
		}
		if (typeof obj.fachID !== "undefined") {
			result += '"fachID" : ' + obj.fachID + ',';
		}
		if (typeof obj.fachKuerzel !== "undefined") {
			result += '"fachKuerzel" : ' + ((!obj.fachKuerzel) ? 'null' : '"' + obj.fachKuerzel.valueOf() + '"') + ',';
		}
		if (typeof obj.reihenfolge !== "undefined") {
			result += '"reihenfolge" : ' + obj.reihenfolge + ',';
		}
		if (typeof obj.belegungVonJahrgang !== "undefined") {
			result += '"belegungVonJahrgang" : ' + obj.belegungVonJahrgang + ',';
		}
		if (typeof obj.belegungVonAbschnitt !== "undefined") {
			result += '"belegungVonAbschnitt" : ' + obj.belegungVonAbschnitt + ',';
		}
		if (typeof obj.belegungBisJahrgang !== "undefined") {
			result += '"belegungBisJahrgang" : ' + ((!obj.belegungBisJahrgang) ? 'null' : obj.belegungBisJahrgang.valueOf()) + ',';
		}
		if (typeof obj.belegungBisAbschnitt !== "undefined") {
			result += '"belegungBisAbschnitt" : ' + ((!obj.belegungBisAbschnitt) ? 'null' : obj.belegungBisAbschnitt.valueOf()) + ',';
		}
		if (typeof obj.referenzniveau !== "undefined") {
			result += '"referenzniveau" : ' + ((!obj.referenzniveau) ? 'null' : '"' + obj.referenzniveau.valueOf() + '"') + ',';
		}
		if (typeof obj.belegungSekI !== "undefined") {
			result += '"belegungSekI" : ' + ((!obj.belegungSekI) ? 'null' : obj.belegungSekI.valueOf()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_enm_ENMSprachenfolge(obj : unknown) : ENMSprachenfolge {
	return obj as ENMSprachenfolge;
}
