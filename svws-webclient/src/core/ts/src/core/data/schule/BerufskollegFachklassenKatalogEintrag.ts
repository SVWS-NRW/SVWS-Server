import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { BerufskollegFachklassenKatalogDaten, cast_de_nrw_schule_svws_core_data_schule_BerufskollegFachklassenKatalogDaten } from '../../../core/data/schule/BerufskollegFachklassenKatalogDaten';

export class BerufskollegFachklassenKatalogEintrag extends JavaObject {

	/**
	 * Der Fachklassenschlüssel.
	 */
	public schluessel : string = "";

	/**
	 * Der Fachklassenschlüssel - Teil 2.
	 */
	public schluessel2 : string = "";

	/**
	 * Die Historie des Katalog-Eintrags.
	 */
	public historie : List<BerufskollegFachklassenKatalogDaten> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schule.BerufskollegFachklassenKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): BerufskollegFachklassenKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new BerufskollegFachklassenKatalogEintrag();
		if (typeof obj.schluessel === "undefined")
			 throw new Error('invalid json format, missing attribute schluessel');
		result.schluessel = obj.schluessel;
		if (typeof obj.schluessel2 === "undefined")
			 throw new Error('invalid json format, missing attribute schluessel2');
		result.schluessel2 = obj.schluessel2;
		if ((obj.historie !== undefined) && (obj.historie !== null)) {
			for (const elem of obj.historie) {
				result.historie?.add(BerufskollegFachklassenKatalogDaten.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : BerufskollegFachklassenKatalogEintrag) : string {
		let result = '{';
		result += '"schluessel" : ' + '"' + obj.schluessel! + '"' + ',';
		result += '"schluessel2" : ' + '"' + obj.schluessel2! + '"' + ',';
		if (!obj.historie) {
			result += '"historie" : []';
		} else {
			result += '"historie" : [ ';
			for (let i = 0; i < obj.historie.size(); i++) {
				const elem = obj.historie.get(i);
				result += BerufskollegFachklassenKatalogDaten.transpilerToJSON(elem);
				if (i < obj.historie.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BerufskollegFachklassenKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.schluessel !== "undefined") {
			result += '"schluessel" : ' + '"' + obj.schluessel + '"' + ',';
		}
		if (typeof obj.schluessel2 !== "undefined") {
			result += '"schluessel2" : ' + '"' + obj.schluessel2 + '"' + ',';
		}
		if (typeof obj.historie !== "undefined") {
			if (!obj.historie) {
				result += '"historie" : []';
			} else {
				result += '"historie" : [ ';
				for (let i = 0; i < obj.historie.size(); i++) {
					const elem = obj.historie.get(i);
					result += BerufskollegFachklassenKatalogDaten.transpilerToJSON(elem);
					if (i < obj.historie.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_schule_BerufskollegFachklassenKatalogEintrag(obj : unknown) : BerufskollegFachklassenKatalogEintrag {
	return obj as BerufskollegFachklassenKatalogEintrag;
}
