import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { JavaBoolean, cast_java_lang_Boolean } from '../../../java/lang/JavaBoolean';

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
	public hatBA : Boolean | null = null;

	/**
	 * Gibt an, welcher allgemeinbildende Abschluss ggf. zusätzlich erreicht wurde, falls er nicht bereits vorher erreicht wurde. 
	 */
	public abschlussAllgemeinbildend : String | null = null;

	/**
	 * Der Log der Abschlussberechnung. 
	 */
	public log : List<String> | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.abschluss.AbschlussErgebnisBerufsbildend'].includes(name);
	}

	public static transpilerFromJSON(json : string): AbschlussErgebnisBerufsbildend {
		const obj = JSON.parse(json);
		const result = new AbschlussErgebnisBerufsbildend();
		if (typeof obj.hatBSA === "undefined")
			 throw new Error('invalid json format, missing attribute hatBSA');
		result.hatBSA = obj.hatBSA;
		if (typeof obj.note === "undefined")
			 throw new Error('invalid json format, missing attribute note');
		result.note = obj.note;
		result.hatBA = typeof obj.hatBA === "undefined" ? null : obj.hatBA === null ? null : Boolean(obj.hatBA);
		result.abschlussAllgemeinbildend = typeof obj.abschlussAllgemeinbildend === "undefined" ? null : obj.abschlussAllgemeinbildend === null ? null : String(obj.abschlussAllgemeinbildend);
		if (!!obj.log) {
			for (let elem of obj.log) {
				result.log?.add(String(elem));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : AbschlussErgebnisBerufsbildend) : string {
		let result = '{';
		result += '"hatBSA" : ' + obj.hatBSA + ',';
		result += '"note" : ' + obj.note + ',';
		result += '"hatBA" : ' + ((!obj.hatBA) ? 'null' : obj.hatBA.valueOf()) + ',';
		result += '"abschlussAllgemeinbildend" : ' + ((!obj.abschlussAllgemeinbildend) ? 'null' : '"' + obj.abschlussAllgemeinbildend.valueOf() + '"') + ',';
		if (!obj.log) {
			result += '"log" : []';
		} else {
			result += '"log" : [ ';
			for (let i : number = 0; i < obj.log.size(); i++) {
				let elem = obj.log.get(i);
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
		if (typeof obj.hatBSA !== "undefined") {
			result += '"hatBSA" : ' + obj.hatBSA + ',';
		}
		if (typeof obj.note !== "undefined") {
			result += '"note" : ' + obj.note + ',';
		}
		if (typeof obj.hatBA !== "undefined") {
			result += '"hatBA" : ' + ((!obj.hatBA) ? 'null' : obj.hatBA.valueOf()) + ',';
		}
		if (typeof obj.abschlussAllgemeinbildend !== "undefined") {
			result += '"abschlussAllgemeinbildend" : ' + ((!obj.abschlussAllgemeinbildend) ? 'null' : '"' + obj.abschlussAllgemeinbildend.valueOf() + '"') + ',';
		}
		if (typeof obj.log !== "undefined") {
			if (!obj.log) {
				result += '"log" : []';
			} else {
				result += '"log" : [ ';
				for (let i : number = 0; i < obj.log.size(); i++) {
					let elem = obj.log.get(i);
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

export function cast_de_nrw_schule_svws_core_data_abschluss_AbschlussErgebnisBerufsbildend(obj : unknown) : AbschlussErgebnisBerufsbildend {
	return obj as AbschlussErgebnisBerufsbildend;
}
