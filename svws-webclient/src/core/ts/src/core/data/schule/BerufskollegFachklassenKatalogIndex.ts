import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { BerufskollegFachklassenKatalogEintrag, cast_de_nrw_schule_svws_core_data_schule_BerufskollegFachklassenKatalogEintrag } from '../../../core/data/schule/BerufskollegFachklassenKatalogEintrag';
import { List, cast_java_util_List } from '../../../java/util/List';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class BerufskollegFachklassenKatalogIndex extends JavaObject {

	/**
	 * Der Index für die Verknüpfung von einem Bildungsgang des Berufskollegs mit Fachklassen 
	 */
	public index : number = 0;

	/**
	 * Die Version des Teilkatalogs. Diese wird bei Änderungen am Katalog erhöht. 
	 */
	public version : number = -1;

	/**
	 * Die Einträge des Katalogs. 
	 */
	public fachklassen : List<BerufskollegFachklassenKatalogEintrag> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schule.BerufskollegFachklassenKatalogIndex'].includes(name);
	}

	public static transpilerFromJSON(json : string): BerufskollegFachklassenKatalogIndex {
		const obj = JSON.parse(json);
		const result = new BerufskollegFachklassenKatalogIndex();
		if (typeof obj.index === "undefined")
			 throw new Error('invalid json format, missing attribute index');
		result.index = obj.index;
		if (typeof obj.version === "undefined")
			 throw new Error('invalid json format, missing attribute version');
		result.version = obj.version;
		if (!!obj.fachklassen) {
			for (let elem of obj.fachklassen) {
				result.fachklassen?.add(BerufskollegFachklassenKatalogEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : BerufskollegFachklassenKatalogIndex) : string {
		let result = '{';
		result += '"index" : ' + obj.index + ',';
		result += '"version" : ' + obj.version + ',';
		if (!obj.fachklassen) {
			result += '"fachklassen" : []';
		} else {
			result += '"fachklassen" : [ ';
			for (let i : number = 0; i < obj.fachklassen.size(); i++) {
				let elem = obj.fachklassen.get(i);
				result += BerufskollegFachklassenKatalogEintrag.transpilerToJSON(elem);
				if (i < obj.fachklassen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BerufskollegFachklassenKatalogIndex>) : string {
		let result = '{';
		if (typeof obj.index !== "undefined") {
			result += '"index" : ' + obj.index + ',';
		}
		if (typeof obj.version !== "undefined") {
			result += '"version" : ' + obj.version + ',';
		}
		if (typeof obj.fachklassen !== "undefined") {
			if (!obj.fachklassen) {
				result += '"fachklassen" : []';
			} else {
				result += '"fachklassen" : [ ';
				for (let i : number = 0; i < obj.fachklassen.size(); i++) {
					let elem = obj.fachklassen.get(i);
					result += BerufskollegFachklassenKatalogEintrag.transpilerToJSON(elem);
					if (i < obj.fachklassen.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_schule_BerufskollegFachklassenKatalogIndex(obj : unknown) : BerufskollegFachklassenKatalogIndex {
	return obj as BerufskollegFachklassenKatalogIndex;
}
