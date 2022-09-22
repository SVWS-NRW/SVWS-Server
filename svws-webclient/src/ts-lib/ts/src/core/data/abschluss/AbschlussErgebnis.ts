import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class AbschlussErgebnis extends JavaObject {

	public erworben : boolean = false;

	public abschluss : String | null = null;

	public npFaecher : List<String> | null = null;

	public log : List<String> | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.abschluss.AbschlussErgebnis'].includes(name);
	}

	public static transpilerFromJSON(json : string): AbschlussErgebnis {
		const obj = JSON.parse(json);
		const result = new AbschlussErgebnis();
		if (typeof obj.erworben === "undefined")
			 throw new Error('invalid json format, missing attribute erworben');
		result.erworben = obj.erworben;
		result.abschluss = typeof obj.abschluss === "undefined" ? null : obj.abschluss;
		if (!!obj.npFaecher) {
			for (let elem of obj.npFaecher) {
				result.npFaecher?.add(elem);
			}
		}
		if (!!obj.log) {
			for (let elem of obj.log) {
				result.log?.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : AbschlussErgebnis) : string {
		let result = '{';
		result += '"erworben" : ' + obj.erworben + ',';
		result += '"abschluss" : ' + ((!obj.abschluss) ? 'null' : '"' + obj.abschluss.valueOf() + '"') + ',';
		if (!obj.npFaecher) {
			result += '"npFaecher" : []';
		} else {
			result += '"npFaecher" : [ ';
			for (let i : number = 0; i < obj.npFaecher.size(); i++) {
				let elem = obj.npFaecher.get(i);
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

	public static transpilerToJSONPatch(obj : Partial<AbschlussErgebnis>) : string {
		let result = '{';
		if (typeof obj.erworben !== "undefined") {
			result += '"erworben" : ' + obj.erworben + ',';
		}
		if (typeof obj.abschluss !== "undefined") {
			result += '"abschluss" : ' + ((!obj.abschluss) ? 'null' : '"' + obj.abschluss.valueOf() + '"') + ',';
		}
		if (typeof obj.npFaecher !== "undefined") {
			if (!obj.npFaecher) {
				result += '"npFaecher" : []';
			} else {
				result += '"npFaecher" : [ ';
				for (let i : number = 0; i < obj.npFaecher.size(); i++) {
					let elem = obj.npFaecher.get(i);
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

export function cast_de_nrw_schule_svws_core_data_abschluss_AbschlussErgebnis(obj : unknown) : AbschlussErgebnis {
	return obj as AbschlussErgebnis;
}
