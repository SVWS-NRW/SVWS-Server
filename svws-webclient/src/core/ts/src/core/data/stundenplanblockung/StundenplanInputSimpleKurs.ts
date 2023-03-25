import { JavaObject } from '../../../java/lang/JavaObject';
import { StundenplanInputSimpleLehrkraft } from '../../../core/data/stundenplanblockung/StundenplanInputSimpleLehrkraft';
import { StundenplanInputSimpleFach } from '../../../core/data/stundenplanblockung/StundenplanInputSimpleFach';
import { StundenplanInputSimpleRaum } from '../../../core/data/stundenplanblockung/StundenplanInputSimpleRaum';
import { StundenplanInputSimpleKopplung } from '../../../core/data/stundenplanblockung/StundenplanInputSimpleKopplung';
import { Vector } from '../../../java/util/Vector';
import { StundenplanInputSimpleKlasse } from '../../../core/data/stundenplanblockung/StundenplanInputSimpleKlasse';

export class StundenplanInputSimpleKurs extends JavaObject {

	/**
	 * 0 bis n Lehrkräfte werden dem Kurs zugeordnet.
	 */
	public lehrkraefte : Vector<StundenplanInputSimpleLehrkraft> = new Vector();

	/**
	 * 0 bis n Klassen werden dem Kurs zugeordnet.
	 */
	public klassen : Vector<StundenplanInputSimpleKlasse> = new Vector();

	/**
	 * 0 oder 1 Fach wird dem Kurs zugeordnet.
	 */
	public faecher : Vector<StundenplanInputSimpleFach> = new Vector();

	/**
	 * 0 bis n potentielle Räume, von denen 0 oder 1 Raum dem Kurs zugeordnet wird.
	 */
	public raeume : Vector<StundenplanInputSimpleRaum> = new Vector();

	/**
	 * 0 oder 1 Kopplung wird dem Kurs zugeordnet.
	 */
	public kopplungen : Vector<StundenplanInputSimpleKopplung> = new Vector();

	/**
	 * Die Wochenstunden des Kurses. Das Stundenplanprogramm bestimmt, wie diese verteilt werden.
	 */
	public wochenstunden : number = -1;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplanblockung.StundenplanInputSimpleKurs'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanInputSimpleKurs {
		const obj = JSON.parse(json);
		const result = new StundenplanInputSimpleKurs();
		if ((obj.lehrkraefte !== undefined) && (obj.lehrkraefte !== null)) {
			for (const elem of obj.lehrkraefte) {
				result.lehrkraefte?.add(StundenplanInputSimpleLehrkraft.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.klassen !== undefined) && (obj.klassen !== null)) {
			for (const elem of obj.klassen) {
				result.klassen?.add(StundenplanInputSimpleKlasse.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.faecher !== undefined) && (obj.faecher !== null)) {
			for (const elem of obj.faecher) {
				result.faecher?.add(StundenplanInputSimpleFach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.raeume !== undefined) && (obj.raeume !== null)) {
			for (const elem of obj.raeume) {
				result.raeume?.add(StundenplanInputSimpleRaum.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.kopplungen !== undefined) && (obj.kopplungen !== null)) {
			for (const elem of obj.kopplungen) {
				result.kopplungen?.add(StundenplanInputSimpleKopplung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (typeof obj.wochenstunden === "undefined")
			 throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanInputSimpleKurs) : string {
		let result = '{';
		if (!obj.lehrkraefte) {
			result += '"lehrkraefte" : []';
		} else {
			result += '"lehrkraefte" : [ ';
			for (let i = 0; i < obj.lehrkraefte.size(); i++) {
				const elem = obj.lehrkraefte.get(i);
				result += StundenplanInputSimpleLehrkraft.transpilerToJSON(elem);
				if (i < obj.lehrkraefte.size() - 1)
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
				result += StundenplanInputSimpleKlasse.transpilerToJSON(elem);
				if (i < obj.klassen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.faecher) {
			result += '"faecher" : []';
		} else {
			result += '"faecher" : [ ';
			for (let i = 0; i < obj.faecher.size(); i++) {
				const elem = obj.faecher.get(i);
				result += StundenplanInputSimpleFach.transpilerToJSON(elem);
				if (i < obj.faecher.size() - 1)
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
				result += StundenplanInputSimpleRaum.transpilerToJSON(elem);
				if (i < obj.raeume.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.kopplungen) {
			result += '"kopplungen" : []';
		} else {
			result += '"kopplungen" : [ ';
			for (let i = 0; i < obj.kopplungen.size(); i++) {
				const elem = obj.kopplungen.get(i);
				result += StundenplanInputSimpleKopplung.transpilerToJSON(elem);
				if (i < obj.kopplungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"wochenstunden" : ' + obj.wochenstunden + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanInputSimpleKurs>) : string {
		let result = '{';
		if (typeof obj.lehrkraefte !== "undefined") {
			if (!obj.lehrkraefte) {
				result += '"lehrkraefte" : []';
			} else {
				result += '"lehrkraefte" : [ ';
				for (let i = 0; i < obj.lehrkraefte.size(); i++) {
					const elem = obj.lehrkraefte.get(i);
					result += StundenplanInputSimpleLehrkraft.transpilerToJSON(elem);
					if (i < obj.lehrkraefte.size() - 1)
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
					result += StundenplanInputSimpleKlasse.transpilerToJSON(elem);
					if (i < obj.klassen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.faecher !== "undefined") {
			if (!obj.faecher) {
				result += '"faecher" : []';
			} else {
				result += '"faecher" : [ ';
				for (let i = 0; i < obj.faecher.size(); i++) {
					const elem = obj.faecher.get(i);
					result += StundenplanInputSimpleFach.transpilerToJSON(elem);
					if (i < obj.faecher.size() - 1)
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
					result += StundenplanInputSimpleRaum.transpilerToJSON(elem);
					if (i < obj.raeume.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.kopplungen !== "undefined") {
			if (!obj.kopplungen) {
				result += '"kopplungen" : []';
			} else {
				result += '"kopplungen" : [ ';
				for (let i = 0; i < obj.kopplungen.size(); i++) {
					const elem = obj.kopplungen.get(i);
					result += StundenplanInputSimpleKopplung.transpilerToJSON(elem);
					if (i < obj.kopplungen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.wochenstunden !== "undefined") {
			result += '"wochenstunden" : ' + obj.wochenstunden + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplanblockung_StundenplanInputSimpleKurs(obj : unknown) : StundenplanInputSimpleKurs {
	return obj as StundenplanInputSimpleKurs;
}
