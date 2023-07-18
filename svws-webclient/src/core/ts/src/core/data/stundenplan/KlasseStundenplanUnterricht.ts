import { JavaObject } from '../../../java/lang/JavaObject';
import { StundenplanRaum } from '../../../core/data/stundenplan/StundenplanRaum';
import { StundenplanKlasse } from '../../../core/data/stundenplan/StundenplanKlasse';
import { StundenplanLehrer } from '../../../core/data/stundenplan/StundenplanLehrer';
import { StundenplanSchiene } from '../../../core/data/stundenplan/StundenplanSchiene';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';

export class KlasseStundenplanUnterricht extends JavaObject {

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
	 * Die Räume in denen der Unterricht stattfindet
	 */
	public unterrichtsraeume : List<StundenplanRaum> = new ArrayList();

	/**
	 * Die Schiene des Unterrichts bei Oberstufenunterrichten
	 */
	public schiene : List<StundenplanSchiene> = new ArrayList();

	/**
	 * Die Klassen, welche unterrichtet werden
	 */
	public klassen : List<StundenplanKlasse> = new ArrayList();

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
	public lehrer : List<StundenplanLehrer> = new ArrayList();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.KlasseStundenplanUnterricht'].includes(name);
	}

	public static transpilerFromJSON(json : string): KlasseStundenplanUnterricht {
		const obj = JSON.parse(json);
		const result = new KlasseStundenplanUnterricht();
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
		if ((obj.unterrichtsraeume !== undefined) && (obj.unterrichtsraeume !== null)) {
			for (const elem of obj.unterrichtsraeume) {
				result.unterrichtsraeume?.add(StundenplanRaum.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.schiene !== undefined) && (obj.schiene !== null)) {
			for (const elem of obj.schiene) {
				result.schiene?.add(StundenplanSchiene.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.klassen !== undefined) && (obj.klassen !== null)) {
			for (const elem of obj.klassen) {
				result.klassen?.add(StundenplanKlasse.transpilerFromJSON(JSON.stringify(elem)));
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
				result.lehrer?.add(StundenplanLehrer.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : KlasseStundenplanUnterricht) : string {
		let result = '{';
		result += '"idLeistungen" : ' + obj.idLeistungen + ',';
		result += '"idUnterricht" : ' + obj.idUnterricht + ',';
		result += '"idZeitraster" : ' + obj.idZeitraster + ',';
		result += '"kursart" : ' + '"' + obj.kursart! + '"' + ',';
		result += '"wochentyp" : ' + obj.wochentyp + ',';
		if (!obj.unterrichtsraeume) {
			result += '"unterrichtsraeume" : []';
		} else {
			result += '"unterrichtsraeume" : [ ';
			for (let i = 0; i < obj.unterrichtsraeume.size(); i++) {
				const elem = obj.unterrichtsraeume.get(i);
				result += StundenplanRaum.transpilerToJSON(elem);
				if (i < obj.unterrichtsraeume.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.schiene) {
			result += '"schiene" : []';
		} else {
			result += '"schiene" : [ ';
			for (let i = 0; i < obj.schiene.size(); i++) {
				const elem = obj.schiene.get(i);
				result += StundenplanSchiene.transpilerToJSON(elem);
				if (i < obj.schiene.size() - 1)
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
				result += StundenplanKlasse.transpilerToJSON(elem);
				if (i < obj.klassen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"idFach" : ' + obj.idFach! + ',';
		result += '"fachKuerzel" : ' + '"' + obj.fachKuerzel! + '"' + ',';
		result += '"fachBezeichnung" : ' + '"' + obj.fachBezeichnung! + '"' + ',';
		result += '"fachKuerzelStatistik" : ' + '"' + obj.fachKuerzelStatistik! + '"' + ',';
		if (!obj.lehrer) {
			result += '"lehrer" : []';
		} else {
			result += '"lehrer" : [ ';
			for (let i = 0; i < obj.lehrer.size(); i++) {
				const elem = obj.lehrer.get(i);
				result += StundenplanLehrer.transpilerToJSON(elem);
				if (i < obj.lehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KlasseStundenplanUnterricht>) : string {
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
		if (typeof obj.unterrichtsraeume !== "undefined") {
			if (!obj.unterrichtsraeume) {
				result += '"unterrichtsraeume" : []';
			} else {
				result += '"unterrichtsraeume" : [ ';
				for (let i = 0; i < obj.unterrichtsraeume.size(); i++) {
					const elem = obj.unterrichtsraeume.get(i);
					result += StundenplanRaum.transpilerToJSON(elem);
					if (i < obj.unterrichtsraeume.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.schiene !== "undefined") {
			if (!obj.schiene) {
				result += '"schiene" : []';
			} else {
				result += '"schiene" : [ ';
				for (let i = 0; i < obj.schiene.size(); i++) {
					const elem = obj.schiene.get(i);
					result += StundenplanSchiene.transpilerToJSON(elem);
					if (i < obj.schiene.size() - 1)
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
					result += StundenplanKlasse.transpilerToJSON(elem);
					if (i < obj.klassen.size() - 1)
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
					result += StundenplanLehrer.transpilerToJSON(elem);
					if (i < obj.lehrer.size() - 1)
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

export function cast_de_svws_nrw_core_data_stundenplan_KlasseStundenplanUnterricht(obj : unknown) : KlasseStundenplanUnterricht {
	return obj as KlasseStundenplanUnterricht;
}
