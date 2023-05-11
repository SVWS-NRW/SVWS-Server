import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import { List } from '../../../java/util/List';

export class StundenplanUnterricht extends JavaObject {

	/**
	 * Die ID der Unterrichtseinheit
	 */
	public id : number = -1;

	/**
	 * Die ID im Zeitraster des Stundenplans
	 */
	public idZeitraster : number = -1;

	/**
	 * Der Wochen-Typ bei der Unterscheidung von (A,B,... -Wochen -> 1, 2, ...) oder 0
	 */
	public wochentyp : number = -1;

	/**
	 * Die ID des Kurses
	 */
	public idKurs : number = -1;

	/**
	 * Die allgemeine Kursart der Unterrichtseinheit (siehe auch {@link ZulaessigeKursart}).
	 */
	public kursart : string = "";

	/**
	 * Die Bezeichnung des Kurses.
	 */
	public kursBezeichnung : string = "";

	/**
	 * Die IDs der Jahrgänge, denen der Kurs zugeordnet ist.
	 */
	public kursJahrgangIDs : List<number> = new ArrayList();

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
	 * Die IDs der Lehrer, die dieser Unterrichtseinheit zugeordnet sind.
	 */
	public lehrer : List<number> = new ArrayList();

	/**
	 * Die IDs der Klassen, die dieser Unterrichtseinheit zugeordnet sind.
	 */
	public klassen : List<number> = new ArrayList();

	/**
	 * Die IDs der Räume, die dieser Unterrichtseinheit zugeordnet sind.
	 */
	public raeume : List<number> = new ArrayList();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanUnterricht'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanUnterricht {
		const obj = JSON.parse(json);
		const result = new StundenplanUnterricht();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.idZeitraster === "undefined")
			 throw new Error('invalid json format, missing attribute idZeitraster');
		result.idZeitraster = obj.idZeitraster;
		if (typeof obj.wochentyp === "undefined")
			 throw new Error('invalid json format, missing attribute wochentyp');
		result.wochentyp = obj.wochentyp;
		if (typeof obj.idKurs === "undefined")
			 throw new Error('invalid json format, missing attribute idKurs');
		result.idKurs = obj.idKurs;
		if (typeof obj.kursart === "undefined")
			 throw new Error('invalid json format, missing attribute kursart');
		result.kursart = obj.kursart;
		if (typeof obj.kursBezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute kursBezeichnung');
		result.kursBezeichnung = obj.kursBezeichnung;
		if ((obj.kursJahrgangIDs !== undefined) && (obj.kursJahrgangIDs !== null)) {
			for (const elem of obj.kursJahrgangIDs) {
				result.kursJahrgangIDs?.add(elem);
			}
		}
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
		if ((obj.lehrer !== undefined) && (obj.lehrer !== null)) {
			for (const elem of obj.lehrer) {
				result.lehrer?.add(elem);
			}
		}
		if ((obj.klassen !== undefined) && (obj.klassen !== null)) {
			for (const elem of obj.klassen) {
				result.klassen?.add(elem);
			}
		}
		if ((obj.raeume !== undefined) && (obj.raeume !== null)) {
			for (const elem of obj.raeume) {
				result.raeume?.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : StundenplanUnterricht) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idZeitraster" : ' + obj.idZeitraster + ',';
		result += '"wochentyp" : ' + obj.wochentyp + ',';
		result += '"idKurs" : ' + obj.idKurs + ',';
		result += '"kursart" : ' + '"' + obj.kursart! + '"' + ',';
		result += '"kursBezeichnung" : ' + '"' + obj.kursBezeichnung! + '"' + ',';
		if (!obj.kursJahrgangIDs) {
			result += '"kursJahrgangIDs" : []';
		} else {
			result += '"kursJahrgangIDs" : [ ';
			for (let i = 0; i < obj.kursJahrgangIDs.size(); i++) {
				const elem = obj.kursJahrgangIDs.get(i);
				result += elem;
				if (i < obj.kursJahrgangIDs.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"idFach" : ' + obj.idFach + ',';
		result += '"fachKuerzel" : ' + '"' + obj.fachKuerzel! + '"' + ',';
		result += '"fachBezeichnung" : ' + '"' + obj.fachBezeichnung! + '"' + ',';
		result += '"fachKuerzelStatistik" : ' + '"' + obj.fachKuerzelStatistik! + '"' + ',';
		if (!obj.lehrer) {
			result += '"lehrer" : []';
		} else {
			result += '"lehrer" : [ ';
			for (let i = 0; i < obj.lehrer.size(); i++) {
				const elem = obj.lehrer.get(i);
				result += elem;
				if (i < obj.lehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.klassen) {
			result += '"klassen" : []';
		} else {
			result += '"klassen" : [ ';
			for (let i = 0; i < obj.klassen.size(); i++) {
				const elem = obj.klassen.get(i);
				result += elem;
				if (i < obj.klassen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.raeume) {
			result += '"raeume" : []';
		} else {
			result += '"raeume" : [ ';
			for (let i = 0; i < obj.raeume.size(); i++) {
				const elem = obj.raeume.get(i);
				result += elem;
				if (i < obj.raeume.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanUnterricht>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.idZeitraster !== "undefined") {
			result += '"idZeitraster" : ' + obj.idZeitraster + ',';
		}
		if (typeof obj.wochentyp !== "undefined") {
			result += '"wochentyp" : ' + obj.wochentyp + ',';
		}
		if (typeof obj.idKurs !== "undefined") {
			result += '"idKurs" : ' + obj.idKurs + ',';
		}
		if (typeof obj.kursart !== "undefined") {
			result += '"kursart" : ' + '"' + obj.kursart + '"' + ',';
		}
		if (typeof obj.kursBezeichnung !== "undefined") {
			result += '"kursBezeichnung" : ' + '"' + obj.kursBezeichnung + '"' + ',';
		}
		if (typeof obj.kursJahrgangIDs !== "undefined") {
			if (!obj.kursJahrgangIDs) {
				result += '"kursJahrgangIDs" : []';
			} else {
				result += '"kursJahrgangIDs" : [ ';
				for (let i = 0; i < obj.kursJahrgangIDs.size(); i++) {
					const elem = obj.kursJahrgangIDs.get(i);
					result += elem;
					if (i < obj.kursJahrgangIDs.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
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
		if (typeof obj.lehrer !== "undefined") {
			if (!obj.lehrer) {
				result += '"lehrer" : []';
			} else {
				result += '"lehrer" : [ ';
				for (let i = 0; i < obj.lehrer.size(); i++) {
					const elem = obj.lehrer.get(i);
					result += elem;
					if (i < obj.lehrer.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.klassen !== "undefined") {
			if (!obj.klassen) {
				result += '"klassen" : []';
			} else {
				result += '"klassen" : [ ';
				for (let i = 0; i < obj.klassen.size(); i++) {
					const elem = obj.klassen.get(i);
					result += elem;
					if (i < obj.klassen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.raeume !== "undefined") {
			if (!obj.raeume) {
				result += '"raeume" : []';
			} else {
				result += '"raeume" : [ ';
				for (let i = 0; i < obj.raeume.size(); i++) {
					const elem = obj.raeume.get(i);
					result += elem;
					if (i < obj.raeume.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanUnterricht(obj : unknown) : StundenplanUnterricht {
	return obj as StundenplanUnterricht;
}
