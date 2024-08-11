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
		const obj = JSON.parse(json) as Partial<StundenplanblockungInput>;
		const result = new StundenplanblockungInput();
		if (obj.lehrkraefte !== undefined) {
			for (const elem of obj.lehrkraefte) {
				result.lehrkraefte.add(StundenplanblockungLehrkraft.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.klassen !== undefined) {
			for (const elem of obj.klassen) {
				result.klassen.add(StundenplanblockungKlasse.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.faecher !== undefined) {
			for (const elem of obj.faecher) {
				result.faecher.add(StundenplanblockungFach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.raeume !== undefined) {
			for (const elem of obj.raeume) {
				result.raeume.add(StundenplanblockungRaum.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.kopplungen !== undefined) {
			for (const elem of obj.kopplungen) {
				result.kopplungen.add(StundenplanblockungKopplung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lerngruppen !== undefined) {
			for (const elem of obj.lerngruppen) {
				result.lerngruppen.add(StundenplanblockungLerngruppe.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : StundenplanblockungInput) : string {
		let result = '{';
		result += '"lehrkraefte" : [ ';
		for (let i = 0; i < obj.lehrkraefte.size(); i++) {
			const elem = obj.lehrkraefte.get(i);
			result += StundenplanblockungLehrkraft.transpilerToJSON(elem);
			if (i < obj.lehrkraefte.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"klassen" : [ ';
		for (let i = 0; i < obj.klassen.size(); i++) {
			const elem = obj.klassen.get(i);
			result += StundenplanblockungKlasse.transpilerToJSON(elem);
			if (i < obj.klassen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"faecher" : [ ';
		for (let i = 0; i < obj.faecher.size(); i++) {
			const elem = obj.faecher.get(i);
			result += StundenplanblockungFach.transpilerToJSON(elem);
			if (i < obj.faecher.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"raeume" : [ ';
		for (let i = 0; i < obj.raeume.size(); i++) {
			const elem = obj.raeume.get(i);
			result += StundenplanblockungRaum.transpilerToJSON(elem);
			if (i < obj.raeume.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"kopplungen" : [ ';
		for (let i = 0; i < obj.kopplungen.size(); i++) {
			const elem = obj.kopplungen.get(i);
			result += StundenplanblockungKopplung.transpilerToJSON(elem);
			if (i < obj.kopplungen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lerngruppen" : [ ';
		for (let i = 0; i < obj.lerngruppen.size(); i++) {
			const elem = obj.lerngruppen.get(i);
			result += StundenplanblockungLerngruppe.transpilerToJSON(elem);
			if (i < obj.lerngruppen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanblockungInput>) : string {
		let result = '{';
		if (obj.lehrkraefte !== undefined) {
			result += '"lehrkraefte" : [ ';
			for (let i = 0; i < obj.lehrkraefte.size(); i++) {
				const elem = obj.lehrkraefte.get(i);
				result += StundenplanblockungLehrkraft.transpilerToJSON(elem);
				if (i < obj.lehrkraefte.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.klassen !== undefined) {
			result += '"klassen" : [ ';
			for (let i = 0; i < obj.klassen.size(); i++) {
				const elem = obj.klassen.get(i);
				result += StundenplanblockungKlasse.transpilerToJSON(elem);
				if (i < obj.klassen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.faecher !== undefined) {
			result += '"faecher" : [ ';
			for (let i = 0; i < obj.faecher.size(); i++) {
				const elem = obj.faecher.get(i);
				result += StundenplanblockungFach.transpilerToJSON(elem);
				if (i < obj.faecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.raeume !== undefined) {
			result += '"raeume" : [ ';
			for (let i = 0; i < obj.raeume.size(); i++) {
				const elem = obj.raeume.get(i);
				result += StundenplanblockungRaum.transpilerToJSON(elem);
				if (i < obj.raeume.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.kopplungen !== undefined) {
			result += '"kopplungen" : [ ';
			for (let i = 0; i < obj.kopplungen.size(); i++) {
				const elem = obj.kopplungen.get(i);
				result += StundenplanblockungKopplung.transpilerToJSON(elem);
				if (i < obj.kopplungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lerngruppen !== undefined) {
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

}

export function cast_de_svws_nrw_core_data_stundenplanblockung_StundenplanblockungInput(obj : unknown) : StundenplanblockungInput {
	return obj as StundenplanblockungInput;
}
