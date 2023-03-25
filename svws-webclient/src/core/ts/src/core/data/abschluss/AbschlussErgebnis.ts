import { JavaObject } from '../../../java/lang/JavaObject';
import { List } from '../../../java/util/List';

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


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.abschluss.AbschlussErgebnis'].includes(name);
	}

	public static transpilerFromJSON(json : string): AbschlussErgebnis {
		const obj = JSON.parse(json);
		const result = new AbschlussErgebnis();
		if (typeof obj.erworben === "undefined")
			 throw new Error('invalid json format, missing attribute erworben');
		result.erworben = obj.erworben;
		result.abschluss = typeof obj.abschluss === "undefined" ? null : obj.abschluss === null ? null : obj.abschluss;
		if ((obj.npFaecher !== undefined) && (obj.npFaecher !== null)) {
			for (const elem of obj.npFaecher) {
				result.npFaecher?.add(elem);
			}
		}
		if ((obj.log !== undefined) && (obj.log !== null)) {
			for (const elem of obj.log) {
				result.log?.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : AbschlussErgebnis) : string {
		let result = '{';
		result += '"erworben" : ' + obj.erworben + ',';
		result += '"abschluss" : ' + ((!obj.abschluss) ? 'null' : '"' + obj.abschluss + '"') + ',';
		if (!obj.npFaecher) {
			result += '"npFaecher" : []';
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
			result += '"log" : []';
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
		if (typeof obj.erworben !== "undefined") {
			result += '"erworben" : ' + obj.erworben + ',';
		}
		if (typeof obj.abschluss !== "undefined") {
			result += '"abschluss" : ' + ((!obj.abschluss) ? 'null' : '"' + obj.abschluss + '"') + ',';
		}
		if (typeof obj.npFaecher !== "undefined") {
			if (!obj.npFaecher) {
				result += '"npFaecher" : []';
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
		if (typeof obj.log !== "undefined") {
			if (!obj.log) {
				result += '"log" : []';
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
