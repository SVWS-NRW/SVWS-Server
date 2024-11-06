import { JavaObject } from '../../../java/lang/JavaObject';
import { StundenplanSchueler } from '../../../core/data/stundenplan/StundenplanSchueler';
import { StundenplanLehrer } from '../../../core/data/stundenplan/StundenplanLehrer';
import { StundenplanKlasse } from '../../../core/data/stundenplan/StundenplanKlasse';
import { StundenplanKlassenunterricht } from '../../../core/data/stundenplan/StundenplanKlassenunterricht';
import { StundenplanFach } from '../../../core/data/stundenplan/StundenplanFach';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { StundenplanKurs } from '../../../core/data/stundenplan/StundenplanKurs';
import { Class } from '../../../java/lang/Class';

export class StundenplanUnterrichtsverteilung extends JavaObject {

	/**
	 * Die ID des Stundenplans.
	 */
	public id : number = -1;

	/**
	 * Die Liste der Lehrer, die für den Stundenplan zur Verfügung stehen.
	 */
	public lehrer : List<StundenplanLehrer> = new ArrayList<StundenplanLehrer>();

	/**
	 * Die Liste der Schüler, die für den Stundenplan zur Verfügung stehen.
	 */
	public schueler : List<StundenplanSchueler> = new ArrayList<StundenplanSchueler>();

	/**
	 * Die Liste der Fächer, die für den Stundenplan zur Verfügung stehen.
	 */
	public faecher : List<StundenplanFach> = new ArrayList<StundenplanFach>();

	/**
	 * Die Liste der Klassen, die für den Stundenplan zur Verfügung stehen.
	 */
	public klassen : List<StundenplanKlasse> = new ArrayList<StundenplanKlasse>();

	/**
	 * Die Liste der Kurse, die für den Stundenplan zur Verfügung stehen.
	 */
	public kurse : List<StundenplanKurs> = new ArrayList<StundenplanKurs>();

	/**
	 * Die Liste der Klassenunterrichte, die für den Stundenplan zur Verfügung stehen.
	 */
	public klassenunterricht : List<StundenplanKlassenunterricht> = new ArrayList<StundenplanKlassenunterricht>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplan.StundenplanUnterrichtsverteilung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanUnterrichtsverteilung'].includes(name);
	}

	public static class = new Class<StundenplanUnterrichtsverteilung>('de.svws_nrw.core.data.stundenplan.StundenplanUnterrichtsverteilung');

	public static transpilerFromJSON(json : string): StundenplanUnterrichtsverteilung {
		const obj = JSON.parse(json) as Partial<StundenplanUnterrichtsverteilung>;
		const result = new StundenplanUnterrichtsverteilung();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.lehrer !== undefined) {
			for (const elem of obj.lehrer) {
				result.lehrer.add(StundenplanLehrer.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schueler !== undefined) {
			for (const elem of obj.schueler) {
				result.schueler.add(StundenplanSchueler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.faecher !== undefined) {
			for (const elem of obj.faecher) {
				result.faecher.add(StundenplanFach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.klassen !== undefined) {
			for (const elem of obj.klassen) {
				result.klassen.add(StundenplanKlasse.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.kurse !== undefined) {
			for (const elem of obj.kurse) {
				result.kurse.add(StundenplanKurs.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.klassenunterricht !== undefined) {
			for (const elem of obj.klassenunterricht) {
				result.klassenunterricht.add(StundenplanKlassenunterricht.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : StundenplanUnterrichtsverteilung) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"lehrer" : [ ';
		for (let i = 0; i < obj.lehrer.size(); i++) {
			const elem = obj.lehrer.get(i);
			result += StundenplanLehrer.transpilerToJSON(elem);
			if (i < obj.lehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schueler" : [ ';
		for (let i = 0; i < obj.schueler.size(); i++) {
			const elem = obj.schueler.get(i);
			result += StundenplanSchueler.transpilerToJSON(elem);
			if (i < obj.schueler.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"faecher" : [ ';
		for (let i = 0; i < obj.faecher.size(); i++) {
			const elem = obj.faecher.get(i);
			result += StundenplanFach.transpilerToJSON(elem);
			if (i < obj.faecher.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"klassen" : [ ';
		for (let i = 0; i < obj.klassen.size(); i++) {
			const elem = obj.klassen.get(i);
			result += StundenplanKlasse.transpilerToJSON(elem);
			if (i < obj.klassen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"kurse" : [ ';
		for (let i = 0; i < obj.kurse.size(); i++) {
			const elem = obj.kurse.get(i);
			result += StundenplanKurs.transpilerToJSON(elem);
			if (i < obj.kurse.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"klassenunterricht" : [ ';
		for (let i = 0; i < obj.klassenunterricht.size(); i++) {
			const elem = obj.klassenunterricht.get(i);
			result += StundenplanKlassenunterricht.transpilerToJSON(elem);
			if (i < obj.klassenunterricht.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanUnterrichtsverteilung>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.lehrer !== undefined) {
			result += '"lehrer" : [ ';
			for (let i = 0; i < obj.lehrer.size(); i++) {
				const elem = obj.lehrer.get(i);
				result += StundenplanLehrer.transpilerToJSON(elem);
				if (i < obj.lehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schueler !== undefined) {
			result += '"schueler" : [ ';
			for (let i = 0; i < obj.schueler.size(); i++) {
				const elem = obj.schueler.get(i);
				result += StundenplanSchueler.transpilerToJSON(elem);
				if (i < obj.schueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.faecher !== undefined) {
			result += '"faecher" : [ ';
			for (let i = 0; i < obj.faecher.size(); i++) {
				const elem = obj.faecher.get(i);
				result += StundenplanFach.transpilerToJSON(elem);
				if (i < obj.faecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.klassen !== undefined) {
			result += '"klassen" : [ ';
			for (let i = 0; i < obj.klassen.size(); i++) {
				const elem = obj.klassen.get(i);
				result += StundenplanKlasse.transpilerToJSON(elem);
				if (i < obj.klassen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.kurse !== undefined) {
			result += '"kurse" : [ ';
			for (let i = 0; i < obj.kurse.size(); i++) {
				const elem = obj.kurse.get(i);
				result += StundenplanKurs.transpilerToJSON(elem);
				if (i < obj.kurse.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.klassenunterricht !== undefined) {
			result += '"klassenunterricht" : [ ';
			for (let i = 0; i < obj.klassenunterricht.size(); i++) {
				const elem = obj.klassenunterricht.get(i);
				result += StundenplanKlassenunterricht.transpilerToJSON(elem);
				if (i < obj.klassenunterricht.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanUnterrichtsverteilung(obj : unknown) : StundenplanUnterrichtsverteilung {
	return obj as StundenplanUnterrichtsverteilung;
}
