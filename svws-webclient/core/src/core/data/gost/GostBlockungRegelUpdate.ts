import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { GostBlockungRegel, cast_de_svws_nrw_core_data_gost_GostBlockungRegel } from '../../../core/data/gost/GostBlockungRegel';

export class GostBlockungRegelUpdate extends JavaObject {

	/**
	 * Die zu entfernenden Regeln
	 */
	public listEntfernen : List<GostBlockungRegel> = new ArrayList();

	/**
	 * Die hinzuzufügenden Regeln
	 */
	public listHinzuzufuegen : List<GostBlockungRegel> = new ArrayList();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostBlockungRegelUpdate';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostBlockungRegelUpdate'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBlockungRegelUpdate {
		const obj = JSON.parse(json);
		const result = new GostBlockungRegelUpdate();
		if ((obj.listEntfernen !== undefined) && (obj.listEntfernen !== null)) {
			for (const elem of obj.listEntfernen) {
				result.listEntfernen?.add(GostBlockungRegel.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.listHinzuzufuegen !== undefined) && (obj.listHinzuzufuegen !== null)) {
			for (const elem of obj.listHinzuzufuegen) {
				result.listHinzuzufuegen?.add(GostBlockungRegel.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungRegelUpdate) : string {
		let result = '{';
		if (!obj.listEntfernen) {
			result += '"listEntfernen" : []';
		} else {
			result += '"listEntfernen" : [ ';
			for (let i = 0; i < obj.listEntfernen.size(); i++) {
				const elem = obj.listEntfernen.get(i);
				result += GostBlockungRegel.transpilerToJSON(elem);
				if (i < obj.listEntfernen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.listHinzuzufuegen) {
			result += '"listHinzuzufuegen" : []';
		} else {
			result += '"listHinzuzufuegen" : [ ';
			for (let i = 0; i < obj.listHinzuzufuegen.size(); i++) {
				const elem = obj.listHinzuzufuegen.get(i);
				result += GostBlockungRegel.transpilerToJSON(elem);
				if (i < obj.listHinzuzufuegen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungRegelUpdate>) : string {
		let result = '{';
		if (typeof obj.listEntfernen !== "undefined") {
			if (!obj.listEntfernen) {
				result += '"listEntfernen" : []';
			} else {
				result += '"listEntfernen" : [ ';
				for (let i = 0; i < obj.listEntfernen.size(); i++) {
					const elem = obj.listEntfernen.get(i);
					result += GostBlockungRegel.transpilerToJSON(elem);
					if (i < obj.listEntfernen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.listHinzuzufuegen !== "undefined") {
			if (!obj.listHinzuzufuegen) {
				result += '"listHinzuzufuegen" : []';
			} else {
				result += '"listHinzuzufuegen" : [ ';
				for (let i = 0; i < obj.listHinzuzufuegen.size(); i++) {
					const elem = obj.listHinzuzufuegen.get(i);
					result += GostBlockungRegel.transpilerToJSON(elem);
					if (i < obj.listHinzuzufuegen.size() - 1)
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

export function cast_de_svws_nrw_core_data_gost_GostBlockungRegelUpdate(obj : unknown) : GostBlockungRegelUpdate {
	return obj as GostBlockungRegelUpdate;
}
