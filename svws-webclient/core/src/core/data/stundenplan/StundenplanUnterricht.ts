import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';

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
	 * Die ID des Kurses, sofern es sich um {@link StundenplanKurs} handelt, andernfalls NULL.
	 */
	public idKurs : number | null = null;

	/**
	 * Die ID des Faches
	 */
	public idFach : number = -1;

	/**
	 * Die IDs der Lehrer, die dieser Unterrichtseinheit zugeordnet sind.
	 */
	public lehrer : List<number> = new ArrayList<number>();

	/**
	 * Die IDs der Klassen, die dieser Unterrichtseinheit zugeordnet sind. Diese Liste ist leer, falls idKurs definiert ist.
	 *   Dann müssen die Klassen über die Schüler des Kurses aggregiert werden!
	 */
	public klassen : List<number> = new ArrayList<number>();

	/**
	 * Die IDs der Räume, die dieser Unterrichtseinheit zugeordnet sind.
	 */
	public raeume : List<number> = new ArrayList<number>();

	/**
	 * Die IDs der Schienen, die dieser Unterrichtseinheit zugeordnet sind (im Normalfall eine, bei Kursen mit Schülern aus mehreren Jahrgangsstufen ggf. mehrere).
	 */
	public schienen : List<number> = new ArrayList<number>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplan.StundenplanUnterricht';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanUnterricht'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanUnterricht {
		const obj = JSON.parse(json) as Partial<StundenplanUnterricht>;
		const result = new StundenplanUnterricht();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idZeitraster === undefined)
			throw new Error('invalid json format, missing attribute idZeitraster');
		result.idZeitraster = obj.idZeitraster;
		if (obj.wochentyp === undefined)
			throw new Error('invalid json format, missing attribute wochentyp');
		result.wochentyp = obj.wochentyp;
		result.idKurs = (obj.idKurs === undefined) ? null : obj.idKurs === null ? null : obj.idKurs;
		if (obj.idFach === undefined)
			throw new Error('invalid json format, missing attribute idFach');
		result.idFach = obj.idFach;
		if (obj.lehrer !== undefined) {
			for (const elem of obj.lehrer) {
				result.lehrer.add(elem);
			}
		}
		if (obj.klassen !== undefined) {
			for (const elem of obj.klassen) {
				result.klassen.add(elem);
			}
		}
		if (obj.raeume !== undefined) {
			for (const elem of obj.raeume) {
				result.raeume.add(elem);
			}
		}
		if (obj.schienen !== undefined) {
			for (const elem of obj.schienen) {
				result.schienen.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : StundenplanUnterricht) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idZeitraster" : ' + obj.idZeitraster.toString() + ',';
		result += '"wochentyp" : ' + obj.wochentyp.toString() + ',';
		result += '"idKurs" : ' + ((!obj.idKurs) ? 'null' : obj.idKurs.toString()) + ',';
		result += '"idFach" : ' + obj.idFach.toString() + ',';
		result += '"lehrer" : [ ';
		for (let i = 0; i < obj.lehrer.size(); i++) {
			const elem = obj.lehrer.get(i);
			result += elem.toString();
			if (i < obj.lehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"klassen" : [ ';
		for (let i = 0; i < obj.klassen.size(); i++) {
			const elem = obj.klassen.get(i);
			result += elem.toString();
			if (i < obj.klassen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"raeume" : [ ';
		for (let i = 0; i < obj.raeume.size(); i++) {
			const elem = obj.raeume.get(i);
			result += elem.toString();
			if (i < obj.raeume.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schienen" : [ ';
		for (let i = 0; i < obj.schienen.size(); i++) {
			const elem = obj.schienen.get(i);
			result += elem.toString();
			if (i < obj.schienen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanUnterricht>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idZeitraster !== undefined) {
			result += '"idZeitraster" : ' + obj.idZeitraster.toString() + ',';
		}
		if (obj.wochentyp !== undefined) {
			result += '"wochentyp" : ' + obj.wochentyp.toString() + ',';
		}
		if (obj.idKurs !== undefined) {
			result += '"idKurs" : ' + ((!obj.idKurs) ? 'null' : obj.idKurs.toString()) + ',';
		}
		if (obj.idFach !== undefined) {
			result += '"idFach" : ' + obj.idFach.toString() + ',';
		}
		if (obj.lehrer !== undefined) {
			result += '"lehrer" : [ ';
			for (let i = 0; i < obj.lehrer.size(); i++) {
				const elem = obj.lehrer.get(i);
				result += elem.toString();
				if (i < obj.lehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.klassen !== undefined) {
			result += '"klassen" : [ ';
			for (let i = 0; i < obj.klassen.size(); i++) {
				const elem = obj.klassen.get(i);
				result += elem.toString();
				if (i < obj.klassen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.raeume !== undefined) {
			result += '"raeume" : [ ';
			for (let i = 0; i < obj.raeume.size(); i++) {
				const elem = obj.raeume.get(i);
				result += elem.toString();
				if (i < obj.raeume.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schienen !== undefined) {
			result += '"schienen" : [ ';
			for (let i = 0; i < obj.schienen.size(); i++) {
				const elem = obj.schienen.get(i);
				result += elem.toString();
				if (i < obj.schienen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanUnterricht(obj : unknown) : StundenplanUnterricht {
	return obj as StundenplanUnterricht;
}
