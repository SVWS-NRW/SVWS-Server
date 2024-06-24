import { JavaObject } from '../../../java/lang/JavaObject';
import type { List } from '../../../java/util/List';

export class AbschlussErgebnisBerufsbildend extends JavaObject {

	/**
	 * Gibt an, ob der Berufschulabschluss erfolgreich erworben wurde.
	 */
	public hatBSA : boolean = false;

	/**
	 * Gibt die Note des Berufsschulabschlusses an, selbst wenn dieser nicht erreicht wurde.
	 */
	public note : number = 0;

	/**
	 * Gibt an, ob der Berufabschluss insgesamt erfolgreich erworben wurde, falls genügend Informationen vorliegen.
	 */
	public hatBA : boolean | null = null;

	/**
	 * Gibt an, welcher allgemeinbildende Abschluss ggf. zusätzlich erreicht wurde, falls er nicht bereits vorher erreicht wurde.
	 */
	public abschlussAllgemeinbildend : string | null = null;

	/**
	 * Der Log der Abschlussberechnung.
	 */
	public log : List<string> | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.abschluss.AbschlussErgebnisBerufsbildend';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.abschluss.AbschlussErgebnisBerufsbildend'].includes(name);
	}

	public static transpilerFromJSON(json : string): AbschlussErgebnisBerufsbildend {
		const obj = JSON.parse(json);
		const result = new AbschlussErgebnisBerufsbildend();
		if (obj.hatBSA === undefined)
			 throw new Error('invalid json format, missing attribute hatBSA');
		result.hatBSA = obj.hatBSA;
		if (obj.note === undefined)
			 throw new Error('invalid json format, missing attribute note');
		result.note = obj.note;
		result.hatBA = (obj.hatBA === undefined) ? null : obj.hatBA === null ? null : obj.hatBA;
		result.abschlussAllgemeinbildend = (obj.abschlussAllgemeinbildend === undefined) ? null : obj.abschlussAllgemeinbildend === null ? null : obj.abschlussAllgemeinbildend;
		if ((obj.log !== undefined) && (obj.log !== null)) {
			for (const elem of obj.log) {
				result.log?.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : AbschlussErgebnisBerufsbildend) : string {
		let result = '{';
		result += '"hatBSA" : ' + obj.hatBSA + ',';
		result += '"note" : ' + obj.note + ',';
		result += '"hatBA" : ' + ((!obj.hatBA) ? 'null' : obj.hatBA) + ',';
		result += '"abschlussAllgemeinbildend" : ' + ((!obj.abschlussAllgemeinbildend) ? 'null' : JSON.stringify(obj.abschlussAllgemeinbildend)) + ',';
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

	public static transpilerToJSONPatch(obj : Partial<AbschlussErgebnisBerufsbildend>) : string {
		let result = '{';
		if (obj.hatBSA !== undefined) {
			result += '"hatBSA" : ' + obj.hatBSA + ',';
		}
		if (obj.note !== undefined) {
			result += '"note" : ' + obj.note + ',';
		}
		if (obj.hatBA !== undefined) {
			result += '"hatBA" : ' + ((!obj.hatBA) ? 'null' : obj.hatBA) + ',';
		}
		if (obj.abschlussAllgemeinbildend !== undefined) {
			result += '"abschlussAllgemeinbildend" : ' + ((!obj.abschlussAllgemeinbildend) ? 'null' : JSON.stringify(obj.abschlussAllgemeinbildend)) + ',';
		}
		if (obj.log !== undefined) {
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

export function cast_de_svws_nrw_core_data_abschluss_AbschlussErgebnisBerufsbildend(obj : unknown) : AbschlussErgebnisBerufsbildend {
	return obj as AbschlussErgebnisBerufsbildend;
}
