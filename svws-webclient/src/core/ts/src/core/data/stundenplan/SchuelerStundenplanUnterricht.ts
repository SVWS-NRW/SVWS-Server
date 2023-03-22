import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class SchuelerStundenplanUnterricht extends JavaObject {

	/**
	 * Die ID der Leistungsdaten, um zusammen gehörige Unterrichtseinheiten zu erkennen.
	 */
	public idLeistungen : number = -1;

	/**
	 * Die ID der Unterrichtseinheit
	 */
	public idUnterricht : number = -1;

	/**
	 * Die ID im Zeitraster des Stundenplans
	 */
	public idZeitraster : number = -1;

	/**
	 * Die Kursart der Unterrichtseinheit.
	 */
	public kursart : string = "";

	/**
	 * Der Wochen-Typ bei der Unterscheidung von (A,B,... -Wochen -> 1, 2, ...) oder 0
	 */
	public wochentyp : number = -1;

	/**
	 * Die ID des Faches
	 */
	public idFach : number = -1;

	/**
	 * Das Kürzel des Unterrichtsfaches.
	 */
	public fachKuerzel : string = "";

	/**
	 * Die Bezeichnung des Unterrichtsfaches.
	 */
	public fachBezeichnung : string = "";

	/**
	 * Das Kürzel des Unterrichtsfaches in Bezug auf die amtliche Schulstatistik.
	 */
	public fachKuerzelStatistik : string = "";

	/**
	 * Die ID des Lehrers.
	 */
	public idLehrer : number = -1;

	/**
	 * Der Nachname des Schülers.
	 */
	public lehrerKuerzel : string = "";

	/**
	 * Der Nachname des Schülers.
	 */
	public lehrerNachname : string = "";

	/**
	 * Der Vorname des Schülers.
	 */
	public lehrerVorname : string = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.stundenplan.SchuelerStundenplanUnterricht'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerStundenplanUnterricht {
		const obj = JSON.parse(json);
		const result = new SchuelerStundenplanUnterricht();
		if (typeof obj.idLeistungen === "undefined")
			 throw new Error('invalid json format, missing attribute idLeistungen');
		result.idLeistungen = obj.idLeistungen;
		if (typeof obj.idUnterricht === "undefined")
			 throw new Error('invalid json format, missing attribute idUnterricht');
		result.idUnterricht = obj.idUnterricht;
		if (typeof obj.idZeitraster === "undefined")
			 throw new Error('invalid json format, missing attribute idZeitraster');
		result.idZeitraster = obj.idZeitraster;
		if (typeof obj.kursart === "undefined")
			 throw new Error('invalid json format, missing attribute kursart');
		result.kursart = obj.kursart;
		if (typeof obj.wochentyp === "undefined")
			 throw new Error('invalid json format, missing attribute wochentyp');
		result.wochentyp = obj.wochentyp;
		if (typeof obj.idFach === "undefined")
			 throw new Error('invalid json format, missing attribute idFach');
		result.idFach = obj.idFach;
		if (typeof obj.fachKuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute fachKuerzel');
		result.fachKuerzel = obj.fachKuerzel;
		if (typeof obj.fachBezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute fachBezeichnung');
		result.fachBezeichnung = obj.fachBezeichnung;
		if (typeof obj.fachKuerzelStatistik === "undefined")
			 throw new Error('invalid json format, missing attribute fachKuerzelStatistik');
		result.fachKuerzelStatistik = obj.fachKuerzelStatistik;
		if (typeof obj.idLehrer === "undefined")
			 throw new Error('invalid json format, missing attribute idLehrer');
		result.idLehrer = obj.idLehrer;
		if (typeof obj.lehrerKuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute lehrerKuerzel');
		result.lehrerKuerzel = obj.lehrerKuerzel;
		if (typeof obj.lehrerNachname === "undefined")
			 throw new Error('invalid json format, missing attribute lehrerNachname');
		result.lehrerNachname = obj.lehrerNachname;
		if (typeof obj.lehrerVorname === "undefined")
			 throw new Error('invalid json format, missing attribute lehrerVorname');
		result.lehrerVorname = obj.lehrerVorname;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerStundenplanUnterricht) : string {
		let result = '{';
		result += '"idLeistungen" : ' + obj.idLeistungen + ',';
		result += '"idUnterricht" : ' + obj.idUnterricht + ',';
		result += '"idZeitraster" : ' + obj.idZeitraster + ',';
		result += '"kursart" : ' + '"' + obj.kursart! + '"' + ',';
		result += '"wochentyp" : ' + obj.wochentyp + ',';
		result += '"idFach" : ' + obj.idFach + ',';
		result += '"fachKuerzel" : ' + '"' + obj.fachKuerzel! + '"' + ',';
		result += '"fachBezeichnung" : ' + '"' + obj.fachBezeichnung! + '"' + ',';
		result += '"fachKuerzelStatistik" : ' + '"' + obj.fachKuerzelStatistik! + '"' + ',';
		result += '"idLehrer" : ' + obj.idLehrer + ',';
		result += '"lehrerKuerzel" : ' + '"' + obj.lehrerKuerzel! + '"' + ',';
		result += '"lehrerNachname" : ' + '"' + obj.lehrerNachname! + '"' + ',';
		result += '"lehrerVorname" : ' + '"' + obj.lehrerVorname! + '"' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerStundenplanUnterricht>) : string {
		let result = '{';
		if (typeof obj.idLeistungen !== "undefined") {
			result += '"idLeistungen" : ' + obj.idLeistungen + ',';
		}
		if (typeof obj.idUnterricht !== "undefined") {
			result += '"idUnterricht" : ' + obj.idUnterricht + ',';
		}
		if (typeof obj.idZeitraster !== "undefined") {
			result += '"idZeitraster" : ' + obj.idZeitraster + ',';
		}
		if (typeof obj.kursart !== "undefined") {
			result += '"kursart" : ' + '"' + obj.kursart + '"' + ',';
		}
		if (typeof obj.wochentyp !== "undefined") {
			result += '"wochentyp" : ' + obj.wochentyp + ',';
		}
		if (typeof obj.idFach !== "undefined") {
			result += '"idFach" : ' + obj.idFach + ',';
		}
		if (typeof obj.fachKuerzel !== "undefined") {
			result += '"fachKuerzel" : ' + '"' + obj.fachKuerzel + '"' + ',';
		}
		if (typeof obj.fachBezeichnung !== "undefined") {
			result += '"fachBezeichnung" : ' + '"' + obj.fachBezeichnung + '"' + ',';
		}
		if (typeof obj.fachKuerzelStatistik !== "undefined") {
			result += '"fachKuerzelStatistik" : ' + '"' + obj.fachKuerzelStatistik + '"' + ',';
		}
		if (typeof obj.idLehrer !== "undefined") {
			result += '"idLehrer" : ' + obj.idLehrer + ',';
		}
		if (typeof obj.lehrerKuerzel !== "undefined") {
			result += '"lehrerKuerzel" : ' + '"' + obj.lehrerKuerzel + '"' + ',';
		}
		if (typeof obj.lehrerNachname !== "undefined") {
			result += '"lehrerNachname" : ' + '"' + obj.lehrerNachname + '"' + ',';
		}
		if (typeof obj.lehrerVorname !== "undefined") {
			result += '"lehrerVorname" : ' + '"' + obj.lehrerVorname + '"' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_stundenplan_SchuelerStundenplanUnterricht(obj : unknown) : SchuelerStundenplanUnterricht {
	return obj as SchuelerStundenplanUnterricht;
}
