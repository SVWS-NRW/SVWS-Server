import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { AbgangsartKatalogDaten, cast_de_nrw_schule_svws_core_data_schule_AbgangsartKatalogDaten } from '../../../core/data/schule/AbgangsartKatalogDaten';

export class AbgangsartKatalogEintrag extends JavaObject {

	/**
	 * Das eindeutige Kürzel des Katalog-Eintrags.
	 */
	public kuerzel : string = "";

	/**
	 * Die Historie des Katalog-Eintrags.
	 */
	public historie : List<AbgangsartKatalogDaten> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schule.AbgangsartKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): AbgangsartKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new AbgangsartKatalogEintrag();
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if ((obj.historie !== undefined) && (obj.historie !== null)) {
			for (const elem of obj.historie) {
				result.historie?.add(AbgangsartKatalogDaten.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : AbgangsartKatalogEintrag) : string {
		let result = '{';
		result += '"kuerzel" : ' + '"' + obj.kuerzel! + '"' + ',';
		if (!obj.historie) {
			result += '"historie" : []';
		} else {
			result += '"historie" : [ ';
			for (let i = 0; i < obj.historie.size(); i++) {
				const elem = obj.historie.get(i);
				result += AbgangsartKatalogDaten.transpilerToJSON(elem);
				if (i < obj.historie.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<AbgangsartKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel + '"' + ',';
		}
		if (typeof obj.historie !== "undefined") {
			if (!obj.historie) {
				result += '"historie" : []';
			} else {
				result += '"historie" : [ ';
				for (let i = 0; i < obj.historie.size(); i++) {
					const elem = obj.historie.get(i);
					result += AbgangsartKatalogDaten.transpilerToJSON(elem);
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

export function cast_de_nrw_schule_svws_core_data_schule_AbgangsartKatalogEintrag(obj : unknown) : AbgangsartKatalogEintrag {
	return obj as AbgangsartKatalogEintrag;
}
