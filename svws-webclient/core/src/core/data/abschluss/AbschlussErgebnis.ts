import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class AbschlussErgebnis extends JavaObject {

	/**
	 * Gibt an, ob der Abschluss erfolgreich erworben wurde, bzw. bei einer Prognose, ob ein ein Abschluss erworben wurde.
	 */
	public erworben : boolean = false;

	/**
	 * Gibt an, welcher Abschluss geprüft wurde.
	 */
	public abschluss : string | null = null;

	/**
	 * Eine Liste der Kuerzel für mögliche Nachprüfungsfächer.
	 */
	public npFaecher : List<string> | null = null;

	/**
	 * Der Log der Abschlussberechnung.
	 */
	public log : List<string> | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.abschluss.AbschlussErgebnis';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.abschluss.AbschlussErgebnis'].includes(name);
	}

	public static class = new Class<AbschlussErgebnis>('de.svws_nrw.core.data.abschluss.AbschlussErgebnis');

	public static transpilerFromJSON(json : string): AbschlussErgebnis {
		const obj = JSON.parse(json) as Partial<AbschlussErgebnis>;
		const result = new AbschlussErgebnis();
		if (obj.erworben === undefined)
			throw new Error('invalid json format, missing attribute erworben');
		result.erworben = obj.erworben;
		result.abschluss = (obj.abschluss === undefined) ? null : obj.abschluss === null ? null : obj.abschluss;
		if ((obj.npFaecher !== undefined) && (obj.npFaecher !== null)) {
			result.npFaecher = new ArrayList();
			for (const elem of obj.npFaecher) {
				result.npFaecher.add(elem);
			}
		} else {
			result.npFaecher = null;
		}
		if ((obj.log !== undefined) && (obj.log !== null)) {
			result.log = new ArrayList();
			for (const elem of obj.log) {
				result.log.add(elem);
			}
		} else {
			result.log = null;
		}
		return result;
	}

	public static transpilerToJSON(obj : AbschlussErgebnis) : string {
		let result = '{';
		result += '"erworben" : ' + obj.erworben.toString() + ',';
		result += '"abschluss" : ' + ((obj.abschluss === null) ? 'null' : JSON.stringify(obj.abschluss)) + ',';
		if (!obj.npFaecher) {
			result += '"npFaecher" : null' + ',';
		} else {
			result += '"npFaecher" : [ ';
			for (let i = 0; i < obj.npFaecher.size(); i++) {
				const elem = obj.npFaecher.get(i);
				result += '"' + elem + '"';
				if (i < obj.npFaecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.log) {
			result += '"log" : null' + ',';
		} else {
			result += '"log" : [ ';
			for (let i = 0; i < obj.log.size(); i++) {
				const elem = obj.log.get(i);
				result += '"' + elem + '"';
				if (i < obj.log.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<AbschlussErgebnis>) : string {
		let result = '{';
		if (obj.erworben !== undefined) {
			result += '"erworben" : ' + obj.erworben.toString() + ',';
		}
		if (obj.abschluss !== undefined) {
			result += '"abschluss" : ' + ((obj.abschluss === null) ? 'null' : JSON.stringify(obj.abschluss)) + ',';
		}
		if (obj.npFaecher !== undefined) {
			if (!obj.npFaecher) {
				result += '"npFaecher" : null' + ',';
			} else {
				result += '"npFaecher" : [ ';
				for (let i = 0; i < obj.npFaecher.size(); i++) {
					const elem = obj.npFaecher.get(i);
					result += '"' + elem + '"';
					if (i < obj.npFaecher.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (obj.log !== undefined) {
			if (!obj.log) {
				result += '"log" : null' + ',';
			} else {
				result += '"log" : [ ';
				for (let i = 0; i < obj.log.size(); i++) {
					const elem = obj.log.get(i);
					result += '"' + elem + '"';
					if (i < obj.log.size() - 1)
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

export function cast_de_svws_nrw_core_data_abschluss_AbschlussErgebnis(obj : unknown) : AbschlussErgebnis {
	return obj as AbschlussErgebnis;
}
