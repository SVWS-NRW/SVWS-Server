import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { List, cast_java_util_List } from '../../../java/util/List';
import { BerufskollegFachklassenKatalogIndex, cast_de_nrw_schule_svws_core_data_schule_BerufskollegFachklassenKatalogIndex } from '../../../core/data/schule/BerufskollegFachklassenKatalogIndex';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class BerufskollegFachklassenKatalog extends JavaObject {

	/**
	 * Die Version des Katalogs. Diese wird bei Änderungen am Katalog erhöht.
	 */
	public version : number = 0;

	/**
	 * Die Teilkataloge in Abhängigkeit vom Index der Fachklassen.
	 */
	public indizes : List<BerufskollegFachklassenKatalogIndex> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schule.BerufskollegFachklassenKatalog'].includes(name);
	}

	public static transpilerFromJSON(json : string): BerufskollegFachklassenKatalog {
		const obj = JSON.parse(json);
		const result = new BerufskollegFachklassenKatalog();
		if (typeof obj.version === "undefined")
			 throw new Error('invalid json format, missing attribute version');
		result.version = obj.version;
		if ((obj.indizes !== undefined) && (obj.indizes !== null)) {
			for (const elem of obj.indizes) {
				result.indizes?.add(BerufskollegFachklassenKatalogIndex.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : BerufskollegFachklassenKatalog) : string {
		let result = '{';
		result += '"version" : ' + obj.version + ',';
		if (!obj.indizes) {
			result += '"indizes" : []';
		} else {
			result += '"indizes" : [ ';
			for (let i = 0; i < obj.indizes.size(); i++) {
				const elem = obj.indizes.get(i);
				result += BerufskollegFachklassenKatalogIndex.transpilerToJSON(elem);
				if (i < obj.indizes.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BerufskollegFachklassenKatalog>) : string {
		let result = '{';
		if (typeof obj.version !== "undefined") {
			result += '"version" : ' + obj.version + ',';
		}
		if (typeof obj.indizes !== "undefined") {
			if (!obj.indizes) {
				result += '"indizes" : []';
			} else {
				result += '"indizes" : [ ';
				for (let i = 0; i < obj.indizes.size(); i++) {
					const elem = obj.indizes.get(i);
					result += BerufskollegFachklassenKatalogIndex.transpilerToJSON(elem);
					if (i < obj.indizes.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_schule_BerufskollegFachklassenKatalog(obj : unknown) : BerufskollegFachklassenKatalog {
	return obj as BerufskollegFachklassenKatalog;
}
