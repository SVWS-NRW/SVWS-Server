import { JavaObject } from '../../../java/lang/JavaObject';
import { StundenplanInputSimpleLehrkraft } from '../../../core/data/stundenplanblockung/StundenplanInputSimpleLehrkraft';
import { ArrayList } from '../../../java/util/ArrayList';
import { StundenplanInputSimpleFach } from '../../../core/data/stundenplanblockung/StundenplanInputSimpleFach';
import { StundenplanInputSimpleRaum } from '../../../core/data/stundenplanblockung/StundenplanInputSimpleRaum';
import { StundenplanInputSimpleKopplung } from '../../../core/data/stundenplanblockung/StundenplanInputSimpleKopplung';
import type { List } from '../../../java/util/List';
import { StundenplanInputSimpleKlasse } from '../../../core/data/stundenplanblockung/StundenplanInputSimpleKlasse';

export class StundenplanInputSimpleKurs extends JavaObject {

	/**
	 * 0 bis n Lehrkräfte werden dem Kurs zugeordnet.
	 */
	public lehrkraefte : List<StundenplanInputSimpleLehrkraft> = new ArrayList<StundenplanInputSimpleLehrkraft>();

	/**
	 * 0 bis n Klassen werden dem Kurs zugeordnet.
	 */
	public klassen : List<StundenplanInputSimpleKlasse> = new ArrayList<StundenplanInputSimpleKlasse>();

	/**
	 * 0 oder 1 Fach wird dem Kurs zugeordnet.
	 */
	public faecher : List<StundenplanInputSimpleFach> = new ArrayList<StundenplanInputSimpleFach>();

	/**
	 * 0 bis n potentielle Räume, von denen 0 oder 1 Raum dem Kurs zugeordnet wird.
	 */
	public raeume : List<StundenplanInputSimpleRaum> = new ArrayList<StundenplanInputSimpleRaum>();

	/**
	 * 0 oder 1 Kopplung wird dem Kurs zugeordnet.
	 */
	public kopplungen : List<StundenplanInputSimpleKopplung> = new ArrayList<StundenplanInputSimpleKopplung>();

	/**
	 * Die Wochenstunden des Kurses. Das Stundenplanprogramm bestimmt, wie diese verteilt werden.
	 */
	public wochenstunden : number = -1;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplanblockung.StundenplanInputSimpleKurs';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplanblockung.StundenplanInputSimpleKurs'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanInputSimpleKurs {
		const obj = JSON.parse(json) as Partial<StundenplanInputSimpleKurs>;
		const result = new StundenplanInputSimpleKurs();
		if (obj.lehrkraefte !== undefined) {
			for (const elem of obj.lehrkraefte) {
				result.lehrkraefte.add(StundenplanInputSimpleLehrkraft.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.klassen !== undefined) {
			for (const elem of obj.klassen) {
				result.klassen.add(StundenplanInputSimpleKlasse.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.faecher !== undefined) {
			for (const elem of obj.faecher) {
				result.faecher.add(StundenplanInputSimpleFach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.raeume !== undefined) {
			for (const elem of obj.raeume) {
				result.raeume.add(StundenplanInputSimpleRaum.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.kopplungen !== undefined) {
			for (const elem of obj.kopplungen) {
				result.kopplungen.add(StundenplanInputSimpleKopplung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.wochenstunden === undefined)
			throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanInputSimpleKurs) : string {
		let result = '{';
		result += '"lehrkraefte" : [ ';
		for (let i = 0; i < obj.lehrkraefte.size(); i++) {
			const elem = obj.lehrkraefte.get(i);
			result += StundenplanInputSimpleLehrkraft.transpilerToJSON(elem);
			if (i < obj.lehrkraefte.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"klassen" : [ ';
		for (let i = 0; i < obj.klassen.size(); i++) {
			const elem = obj.klassen.get(i);
			result += StundenplanInputSimpleKlasse.transpilerToJSON(elem);
			if (i < obj.klassen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"faecher" : [ ';
		for (let i = 0; i < obj.faecher.size(); i++) {
			const elem = obj.faecher.get(i);
			result += StundenplanInputSimpleFach.transpilerToJSON(elem);
			if (i < obj.faecher.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"raeume" : [ ';
		for (let i = 0; i < obj.raeume.size(); i++) {
			const elem = obj.raeume.get(i);
			result += StundenplanInputSimpleRaum.transpilerToJSON(elem);
			if (i < obj.raeume.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"kopplungen" : [ ';
		for (let i = 0; i < obj.kopplungen.size(); i++) {
			const elem = obj.kopplungen.get(i);
			result += StundenplanInputSimpleKopplung.transpilerToJSON(elem);
			if (i < obj.kopplungen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanInputSimpleKurs>) : string {
		let result = '{';
		if (obj.lehrkraefte !== undefined) {
			result += '"lehrkraefte" : [ ';
			for (let i = 0; i < obj.lehrkraefte.size(); i++) {
				const elem = obj.lehrkraefte.get(i);
				result += StundenplanInputSimpleLehrkraft.transpilerToJSON(elem);
				if (i < obj.lehrkraefte.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.klassen !== undefined) {
			result += '"klassen" : [ ';
			for (let i = 0; i < obj.klassen.size(); i++) {
				const elem = obj.klassen.get(i);
				result += StundenplanInputSimpleKlasse.transpilerToJSON(elem);
				if (i < obj.klassen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.faecher !== undefined) {
			result += '"faecher" : [ ';
			for (let i = 0; i < obj.faecher.size(); i++) {
				const elem = obj.faecher.get(i);
				result += StundenplanInputSimpleFach.transpilerToJSON(elem);
				if (i < obj.faecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.raeume !== undefined) {
			result += '"raeume" : [ ';
			for (let i = 0; i < obj.raeume.size(); i++) {
				const elem = obj.raeume.get(i);
				result += StundenplanInputSimpleRaum.transpilerToJSON(elem);
				if (i < obj.raeume.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.kopplungen !== undefined) {
			result += '"kopplungen" : [ ';
			for (let i = 0; i < obj.kopplungen.size(); i++) {
				const elem = obj.kopplungen.get(i);
				result += StundenplanInputSimpleKopplung.transpilerToJSON(elem);
				if (i < obj.kopplungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.wochenstunden !== undefined) {
			result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplanblockung_StundenplanInputSimpleKurs(obj : unknown) : StundenplanInputSimpleKurs {
	return obj as StundenplanInputSimpleKurs;
}
