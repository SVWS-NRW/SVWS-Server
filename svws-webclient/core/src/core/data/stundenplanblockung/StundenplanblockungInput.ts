import { JavaObject } from '../../../java/lang/JavaObject';
import { StundenplanblockungLehrkraft } from '../../../core/data/stundenplanblockung/StundenplanblockungLehrkraft';
import { StundenplanblockungKopplung } from '../../../core/data/stundenplanblockung/StundenplanblockungKopplung';
import { StundenplanblockungKlasse } from '../../../core/data/stundenplanblockung/StundenplanblockungKlasse';
import { ArrayList } from '../../../java/util/ArrayList';
import { StundenplanblockungFach } from '../../../core/data/stundenplanblockung/StundenplanblockungFach';
import { StundenplanblockungLerngruppe } from '../../../core/data/stundenplanblockung/StundenplanblockungLerngruppe';
import type { List } from '../../../java/util/List';
import { StundenplanblockungRaum } from '../../../core/data/stundenplanblockung/StundenplanblockungRaum';

export class StundenplanblockungInput extends JavaObject {

	/**
	 * Alle Lehrkräfte, die an der Stundenplanberechnung beteiligt sind.
	 */
	public lehrkraefte : List<StundenplanblockungLehrkraft> = new ArrayList<StundenplanblockungLehrkraft>();

	/**
	 * Alle Klassen, die an der Stundenplanberechnung beteiligt sind.
	 */
	public klassen : List<StundenplanblockungKlasse> = new ArrayList<StundenplanblockungKlasse>();

	/**
	 * Alle Fächer, die an der Stundenplanberechnung beteiligt sind.
	 */
	public faecher : List<StundenplanblockungFach> = new ArrayList<StundenplanblockungFach>();

	/**
	 * Alle Räume, die an der Stundenplanberechnung beteiligt sind.
	 */
	public raeume : List<StundenplanblockungRaum> = new ArrayList<StundenplanblockungRaum>();

	/**
	 * Alle Kopplungen, die an der Stundenplanberechnung beteiligt sind.
	 */
	public kopplungen : List<StundenplanblockungKopplung> = new ArrayList<StundenplanblockungKopplung>();

	/**
	 * Alle Lerngruppen, die an der Stundenplanberechnung beteiligt sind.
	 */
	public lerngruppen : List<StundenplanblockungLerngruppe> = new ArrayList<StundenplanblockungLerngruppe>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplanblockung.StundenplanblockungInput';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplanblockung.StundenplanblockungInput'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanblockungInput {
		const obj = JSON.parse(json);
		const result = new StundenplanblockungInput();
		if ((obj.lehrkraefte !== undefined) && (obj.lehrkraefte !== null)) {
			for (const elem of obj.lehrkraefte) {
				result.lehrkraefte?.add(StundenplanblockungLehrkraft.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.klassen !== undefined) && (obj.klassen !== null)) {
			for (const elem of obj.klassen) {
				result.klassen?.add(StundenplanblockungKlasse.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.faecher !== undefined) && (obj.faecher !== null)) {
			for (const elem of obj.faecher) {
				result.faecher?.add(StundenplanblockungFach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.raeume !== undefined) && (obj.raeume !== null)) {
			for (const elem of obj.raeume) {
				result.raeume?.add(StundenplanblockungRaum.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.kopplungen !== undefined) && (obj.kopplungen !== null)) {
			for (const elem of obj.kopplungen) {
				result.kopplungen?.add(StundenplanblockungKopplung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.lerngruppen !== undefined) && (obj.lerngruppen !== null)) {
			for (const elem of obj.lerngruppen) {
				result.lerngruppen?.add(StundenplanblockungLerngruppe.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : StundenplanblockungInput) : string {
		let result = '{';
		if (!obj.lehrkraefte) {
			result += '"lehrkraefte" : []';
		} else {
			result += '"lehrkraefte" : [ ';
			for (let i = 0; i < obj.lehrkraefte.size(); i++) {
				const elem = obj.lehrkraefte.get(i);
				result += StundenplanblockungLehrkraft.transpilerToJSON(elem);
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
				result += StundenplanblockungKlasse.transpilerToJSON(elem);
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
				result += StundenplanblockungFach.transpilerToJSON(elem);
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
				result += StundenplanblockungRaum.transpilerToJSON(elem);
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
				result += StundenplanblockungKopplung.transpilerToJSON(elem);
				if (i < obj.kopplungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.lerngruppen) {
			result += '"lerngruppen" : []';
		} else {
			result += '"lerngruppen" : [ ';
			for (let i = 0; i < obj.lerngruppen.size(); i++) {
				const elem = obj.lerngruppen.get(i);
				result += StundenplanblockungLerngruppe.transpilerToJSON(elem);
				if (i < obj.lerngruppen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanblockungInput>) : string {
		let result = '{';
		if (typeof obj.lehrkraefte !== "undefined") {
			if (!obj.lehrkraefte) {
				result += '"lehrkraefte" : []';
			} else {
				result += '"lehrkraefte" : [ ';
				for (let i = 0; i < obj.lehrkraefte.size(); i++) {
					const elem = obj.lehrkraefte.get(i);
					result += StundenplanblockungLehrkraft.transpilerToJSON(elem);
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
					result += StundenplanblockungKlasse.transpilerToJSON(elem);
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
					result += StundenplanblockungFach.transpilerToJSON(elem);
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
					result += StundenplanblockungRaum.transpilerToJSON(elem);
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
					result += StundenplanblockungKopplung.transpilerToJSON(elem);
					if (i < obj.kopplungen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.lerngruppen !== "undefined") {
			if (!obj.lerngruppen) {
				result += '"lerngruppen" : []';
			} else {
				result += '"lerngruppen" : [ ';
				for (let i = 0; i < obj.lerngruppen.size(); i++) {
					const elem = obj.lerngruppen.get(i);
					result += StundenplanblockungLerngruppe.transpilerToJSON(elem);
					if (i < obj.lerngruppen.size() - 1)
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

export function cast_de_svws_nrw_core_data_stundenplanblockung_StundenplanblockungInput(obj : unknown) : StundenplanblockungInput {
	return obj as StundenplanblockungInput;
}
