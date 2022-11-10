import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class SchildReportingSchuelerleistungsdaten extends JavaObject {

	public id : number = -1;

	public abschnittID : number = -1;

	public fachID : number = -1;

	public fachKuerzel : String = "";

	public fach : String = "";

	public lehrerID : number = -1;

	public lehrerKuerzel : String = "";

	public kursID : Number | null = null;

	public kurs : String = "";

	public kursart : String = "";

	public kursartAllg : String = "";

	public note : String = "";

	public noteKuerzel : String = "";

	public notePunkte : Number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schild3.SchildReportingSchuelerleistungsdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchildReportingSchuelerleistungsdaten {
		const obj = JSON.parse(json);
		const result = new SchildReportingSchuelerleistungsdaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.abschnittID === "undefined")
			 throw new Error('invalid json format, missing attribute abschnittID');
		result.abschnittID = obj.abschnittID;
		if (typeof obj.fachID === "undefined")
			 throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		if (typeof obj.fachKuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute fachKuerzel');
		result.fachKuerzel = String(obj.fachKuerzel);
		if (typeof obj.fach === "undefined")
			 throw new Error('invalid json format, missing attribute fach');
		result.fach = String(obj.fach);
		if (typeof obj.lehrerID === "undefined")
			 throw new Error('invalid json format, missing attribute lehrerID');
		result.lehrerID = obj.lehrerID;
		if (typeof obj.lehrerKuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute lehrerKuerzel');
		result.lehrerKuerzel = String(obj.lehrerKuerzel);
		result.kursID = typeof obj.kursID === "undefined" ? null : obj.kursID === null ? null : Number(obj.kursID);
		if (typeof obj.kurs === "undefined")
			 throw new Error('invalid json format, missing attribute kurs');
		result.kurs = String(obj.kurs);
		if (typeof obj.kursart === "undefined")
			 throw new Error('invalid json format, missing attribute kursart');
		result.kursart = String(obj.kursart);
		if (typeof obj.kursartAllg === "undefined")
			 throw new Error('invalid json format, missing attribute kursartAllg');
		result.kursartAllg = String(obj.kursartAllg);
		if (typeof obj.note === "undefined")
			 throw new Error('invalid json format, missing attribute note');
		result.note = String(obj.note);
		if (typeof obj.noteKuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute noteKuerzel');
		result.noteKuerzel = String(obj.noteKuerzel);
		result.notePunkte = typeof obj.notePunkte === "undefined" ? null : obj.notePunkte === null ? null : Number(obj.notePunkte);
		return result;
	}

	public static transpilerToJSON(obj : SchildReportingSchuelerleistungsdaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"abschnittID" : ' + obj.abschnittID + ',';
		result += '"fachID" : ' + obj.fachID + ',';
		result += '"fachKuerzel" : ' + '"' + obj.fachKuerzel.valueOf() + '"' + ',';
		result += '"fach" : ' + '"' + obj.fach.valueOf() + '"' + ',';
		result += '"lehrerID" : ' + obj.lehrerID + ',';
		result += '"lehrerKuerzel" : ' + '"' + obj.lehrerKuerzel.valueOf() + '"' + ',';
		result += '"kursID" : ' + ((!obj.kursID) ? 'null' : obj.kursID.valueOf()) + ',';
		result += '"kurs" : ' + '"' + obj.kurs.valueOf() + '"' + ',';
		result += '"kursart" : ' + '"' + obj.kursart.valueOf() + '"' + ',';
		result += '"kursartAllg" : ' + '"' + obj.kursartAllg.valueOf() + '"' + ',';
		result += '"note" : ' + '"' + obj.note.valueOf() + '"' + ',';
		result += '"noteKuerzel" : ' + '"' + obj.noteKuerzel.valueOf() + '"' + ',';
		result += '"notePunkte" : ' + ((!obj.notePunkte) ? 'null' : obj.notePunkte.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchildReportingSchuelerleistungsdaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.abschnittID !== "undefined") {
			result += '"abschnittID" : ' + obj.abschnittID + ',';
		}
		if (typeof obj.fachID !== "undefined") {
			result += '"fachID" : ' + obj.fachID + ',';
		}
		if (typeof obj.fachKuerzel !== "undefined") {
			result += '"fachKuerzel" : ' + '"' + obj.fachKuerzel.valueOf() + '"' + ',';
		}
		if (typeof obj.fach !== "undefined") {
			result += '"fach" : ' + '"' + obj.fach.valueOf() + '"' + ',';
		}
		if (typeof obj.lehrerID !== "undefined") {
			result += '"lehrerID" : ' + obj.lehrerID + ',';
		}
		if (typeof obj.lehrerKuerzel !== "undefined") {
			result += '"lehrerKuerzel" : ' + '"' + obj.lehrerKuerzel.valueOf() + '"' + ',';
		}
		if (typeof obj.kursID !== "undefined") {
			result += '"kursID" : ' + ((!obj.kursID) ? 'null' : obj.kursID.valueOf()) + ',';
		}
		if (typeof obj.kurs !== "undefined") {
			result += '"kurs" : ' + '"' + obj.kurs.valueOf() + '"' + ',';
		}
		if (typeof obj.kursart !== "undefined") {
			result += '"kursart" : ' + '"' + obj.kursart.valueOf() + '"' + ',';
		}
		if (typeof obj.kursartAllg !== "undefined") {
			result += '"kursartAllg" : ' + '"' + obj.kursartAllg.valueOf() + '"' + ',';
		}
		if (typeof obj.note !== "undefined") {
			result += '"note" : ' + '"' + obj.note.valueOf() + '"' + ',';
		}
		if (typeof obj.noteKuerzel !== "undefined") {
			result += '"noteKuerzel" : ' + '"' + obj.noteKuerzel.valueOf() + '"' + ',';
		}
		if (typeof obj.notePunkte !== "undefined") {
			result += '"notePunkte" : ' + ((!obj.notePunkte) ? 'null' : obj.notePunkte.valueOf()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schild3_SchildReportingSchuelerleistungsdaten(obj : unknown) : SchildReportingSchuelerleistungsdaten {
	return obj as SchildReportingSchuelerleistungsdaten;
}
