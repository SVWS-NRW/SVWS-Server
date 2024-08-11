import { JavaObject } from '../../../java/lang/JavaObject';
import { StundenplanblockungLehrkraft } from '../../../core/data/stundenplanblockung/StundenplanblockungLehrkraft';
import { StundenplanblockungKopplung } from '../../../core/data/stundenplanblockung/StundenplanblockungKopplung';
import { StundenplanblockungKlasse } from '../../../core/data/stundenplanblockung/StundenplanblockungKlasse';
import { ArrayList } from '../../../java/util/ArrayList';
import { StundenplanblockungFach } from '../../../core/data/stundenplanblockung/StundenplanblockungFach';
import type { List } from '../../../java/util/List';
import { StundenplanblockungStundenelement } from '../../../core/data/stundenplanblockung/StundenplanblockungStundenelement';
import { StundenplanblockungRaum } from '../../../core/data/stundenplanblockung/StundenplanblockungRaum';

export class StundenplanblockungLerngruppe extends JavaObject {

	/**
	 * Die Datenbank-ID der Lerngruppe.
	 */
	public id : number = 0;

	/**
	 * Alle Lehrkräfte, die dieser Lerngruppe zugeordnet sind.
	 */
	public lehrkraefte1 : List<StundenplanblockungLehrkraft> = new ArrayList<StundenplanblockungLehrkraft>();

	/**
	 * Alle Lehrkräfte, die dieser Lerngruppe hospitierend zugeordnet sind.
	 */
	public lehrkraefte2 : List<StundenplanblockungLehrkraft> = new ArrayList<StundenplanblockungLehrkraft>();

	/**
	 * Alle Klassen, die dieser Lerngruppe zugeordnet sind.
	 */
	public klassen : List<StundenplanblockungKlasse> = new ArrayList<StundenplanblockungKlasse>();

	/**
	 * Alle Fächer, die dieser Lerngruppe zugeordnet sind. In der Regel genau ein Fach.
	 */
	public faecher : List<StundenplanblockungFach> = new ArrayList<StundenplanblockungFach>();

	/**
	 * Alle Räume, die für diese Lerngruppe primär in Frage kommen.
	 */
	public raeume1 : List<StundenplanblockungRaum> = new ArrayList<StundenplanblockungRaum>();

	/**
	 * Alle Räume, die für diese Lerngruppe sekundär (alternativ) in Frage kommen.
	 */
	public raeume2 : List<StundenplanblockungRaum> = new ArrayList<StundenplanblockungRaum>();

	/**
	 * Alle Kopplungen, die dieser Lerngruppe zugeordnet sind.
	 */
	public kopplungen : List<StundenplanblockungKopplung> = new ArrayList<StundenplanblockungKopplung>();

	/**
	 * Alle Stundenelemente, die dieser Lerngruppe zugeordnet sind.
	 */
	public stundenelemente : List<StundenplanblockungStundenelement> = new ArrayList<StundenplanblockungStundenelement>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplanblockung.StundenplanblockungLerngruppe';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplanblockung.StundenplanblockungLerngruppe'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanblockungLerngruppe {
		const obj = JSON.parse(json) as Partial<StundenplanblockungLerngruppe>;
		const result = new StundenplanblockungLerngruppe();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.lehrkraefte1 !== undefined) {
			for (const elem of obj.lehrkraefte1) {
				result.lehrkraefte1.add(StundenplanblockungLehrkraft.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lehrkraefte2 !== undefined) {
			for (const elem of obj.lehrkraefte2) {
				result.lehrkraefte2.add(StundenplanblockungLehrkraft.transpilerFromJSON(JSON.stringify(elem)));
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
		if (obj.raeume1 !== undefined) {
			for (const elem of obj.raeume1) {
				result.raeume1.add(StundenplanblockungRaum.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.raeume2 !== undefined) {
			for (const elem of obj.raeume2) {
				result.raeume2.add(StundenplanblockungRaum.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.kopplungen !== undefined) {
			for (const elem of obj.kopplungen) {
				result.kopplungen.add(StundenplanblockungKopplung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.stundenelemente !== undefined) {
			for (const elem of obj.stundenelemente) {
				result.stundenelemente.add(StundenplanblockungStundenelement.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : StundenplanblockungLerngruppe) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"lehrkraefte1" : [ ';
		for (let i = 0; i < obj.lehrkraefte1.size(); i++) {
			const elem = obj.lehrkraefte1.get(i);
			result += StundenplanblockungLehrkraft.transpilerToJSON(elem);
			if (i < obj.lehrkraefte1.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lehrkraefte2" : [ ';
		for (let i = 0; i < obj.lehrkraefte2.size(); i++) {
			const elem = obj.lehrkraefte2.get(i);
			result += StundenplanblockungLehrkraft.transpilerToJSON(elem);
			if (i < obj.lehrkraefte2.size() - 1)
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
		result += '"raeume1" : [ ';
		for (let i = 0; i < obj.raeume1.size(); i++) {
			const elem = obj.raeume1.get(i);
			result += StundenplanblockungRaum.transpilerToJSON(elem);
			if (i < obj.raeume1.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"raeume2" : [ ';
		for (let i = 0; i < obj.raeume2.size(); i++) {
			const elem = obj.raeume2.get(i);
			result += StundenplanblockungRaum.transpilerToJSON(elem);
			if (i < obj.raeume2.size() - 1)
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
		result += '"stundenelemente" : [ ';
		for (let i = 0; i < obj.stundenelemente.size(); i++) {
			const elem = obj.stundenelemente.get(i);
			result += StundenplanblockungStundenelement.transpilerToJSON(elem);
			if (i < obj.stundenelemente.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanblockungLerngruppe>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.lehrkraefte1 !== undefined) {
			result += '"lehrkraefte1" : [ ';
			for (let i = 0; i < obj.lehrkraefte1.size(); i++) {
				const elem = obj.lehrkraefte1.get(i);
				result += StundenplanblockungLehrkraft.transpilerToJSON(elem);
				if (i < obj.lehrkraefte1.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lehrkraefte2 !== undefined) {
			result += '"lehrkraefte2" : [ ';
			for (let i = 0; i < obj.lehrkraefte2.size(); i++) {
				const elem = obj.lehrkraefte2.get(i);
				result += StundenplanblockungLehrkraft.transpilerToJSON(elem);
				if (i < obj.lehrkraefte2.size() - 1)
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
		if (obj.raeume1 !== undefined) {
			result += '"raeume1" : [ ';
			for (let i = 0; i < obj.raeume1.size(); i++) {
				const elem = obj.raeume1.get(i);
				result += StundenplanblockungRaum.transpilerToJSON(elem);
				if (i < obj.raeume1.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.raeume2 !== undefined) {
			result += '"raeume2" : [ ';
			for (let i = 0; i < obj.raeume2.size(); i++) {
				const elem = obj.raeume2.get(i);
				result += StundenplanblockungRaum.transpilerToJSON(elem);
				if (i < obj.raeume2.size() - 1)
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
		if (obj.stundenelemente !== undefined) {
			result += '"stundenelemente" : [ ';
			for (let i = 0; i < obj.stundenelemente.size(); i++) {
				const elem = obj.stundenelemente.get(i);
				result += StundenplanblockungStundenelement.transpilerToJSON(elem);
				if (i < obj.stundenelemente.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplanblockung_StundenplanblockungLerngruppe(obj : unknown) : StundenplanblockungLerngruppe {
	return obj as StundenplanblockungLerngruppe;
}
