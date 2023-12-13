import { JavaObject } from '../../../java/lang/JavaObject';
import { StundenplanInputSimpleKurs } from '../../../core/data/stundenplanblockung/StundenplanInputSimpleKurs';
import { StundenplanInputSimpleLehrkraft } from '../../../core/data/stundenplanblockung/StundenplanInputSimpleLehrkraft';
import { ArrayList } from '../../../java/util/ArrayList';
import { StundenplanInputSimpleFach } from '../../../core/data/stundenplanblockung/StundenplanInputSimpleFach';
import { StundenplanInputSimpleRaum } from '../../../core/data/stundenplanblockung/StundenplanInputSimpleRaum';
import { StundenplanInputSimpleKopplung } from '../../../core/data/stundenplanblockung/StundenplanInputSimpleKopplung';
import type { List } from '../../../java/util/List';
import { StundenplanInputSimpleKlasse } from '../../../core/data/stundenplanblockung/StundenplanInputSimpleKlasse';

export class StundenplanInputSimple extends JavaObject {

	/**
	 * Alle Lehrkräfte.
	 */
	public lehrkraefte : List<StundenplanInputSimpleLehrkraft> = new ArrayList();

	/**
	 * Alle Klassen.
	 */
	public klassen : List<StundenplanInputSimpleKlasse> = new ArrayList();

	/**
	 * Alle Fächer.
	 */
	public faecher : List<StundenplanInputSimpleFach> = new ArrayList();

	/**
	 * Alle Räume.
	 */
	public raeume : List<StundenplanInputSimpleRaum> = new ArrayList();

	/**
	 * Alle Kopplungen.
	 */
	public kopplungen : List<StundenplanInputSimpleKopplung> = new ArrayList();

	/**
	 * Alle Kurse.
	 */
	public kurse : List<StundenplanInputSimpleKurs> = new ArrayList();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplanblockung.StundenplanInputSimple';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplanblockung.StundenplanInputSimple'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanInputSimple {
		const obj = JSON.parse(json);
		const result = new StundenplanInputSimple();
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
		if ((obj.kurse !== undefined) && (obj.kurse !== null)) {
			for (const elem of obj.kurse) {
				result.kurse?.add(StundenplanInputSimpleKurs.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : StundenplanInputSimple) : string {
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
		if (!obj.kurse) {
			result += '"kurse" : []';
		} else {
			result += '"kurse" : [ ';
			for (let i = 0; i < obj.kurse.size(); i++) {
				const elem = obj.kurse.get(i);
				result += StundenplanInputSimpleKurs.transpilerToJSON(elem);
				if (i < obj.kurse.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanInputSimple>) : string {
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
		if (typeof obj.kurse !== "undefined") {
			if (!obj.kurse) {
				result += '"kurse" : []';
			} else {
				result += '"kurse" : [ ';
				for (let i = 0; i < obj.kurse.size(); i++) {
					const elem = obj.kurse.get(i);
					result += StundenplanInputSimpleKurs.transpilerToJSON(elem);
					if (i < obj.kurse.size() - 1)
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

export function cast_de_svws_nrw_core_data_stundenplanblockung_StundenplanInputSimple(obj : unknown) : StundenplanInputSimple {
	return obj as StundenplanInputSimple;
}
