import { JavaObject } from '../../../java/lang/JavaObject';
import { GostBlockungsergebnisKursSchuelerZuordnung, cast_de_svws_nrw_core_data_gost_GostBlockungsergebnisKursSchuelerZuordnung } from '../../../core/data/gost/GostBlockungsergebnisKursSchuelerZuordnung';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';

export class GostBlockungsergebnisKursSchuelerZuordnungUpdate extends JavaObject {

	/**
	 * Die zu entfernenden Zuordnungen
	 */
	public listEntfernen : List<GostBlockungsergebnisKursSchuelerZuordnung> = new ArrayList();

	/**
	 * Die hinzuzufügenden Zuordnungen
	 */
	public listHinzuzufuegen : List<GostBlockungsergebnisKursSchuelerZuordnung> = new ArrayList();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchuelerZuordnungUpdate';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchuelerZuordnungUpdate'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBlockungsergebnisKursSchuelerZuordnungUpdate {
		const obj = JSON.parse(json);
		const result = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();
		if ((obj.listEntfernen !== undefined) && (obj.listEntfernen !== null)) {
			for (const elem of obj.listEntfernen) {
				result.listEntfernen?.add(GostBlockungsergebnisKursSchuelerZuordnung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.listHinzuzufuegen !== undefined) && (obj.listHinzuzufuegen !== null)) {
			for (const elem of obj.listHinzuzufuegen) {
				result.listHinzuzufuegen?.add(GostBlockungsergebnisKursSchuelerZuordnung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungsergebnisKursSchuelerZuordnungUpdate) : string {
		let result = '{';
		if (!obj.listEntfernen) {
			result += '"listEntfernen" : []';
		} else {
			result += '"listEntfernen" : [ ';
			for (let i = 0; i < obj.listEntfernen.size(); i++) {
				const elem = obj.listEntfernen.get(i);
				result += GostBlockungsergebnisKursSchuelerZuordnung.transpilerToJSON(elem);
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
				result += GostBlockungsergebnisKursSchuelerZuordnung.transpilerToJSON(elem);
				if (i < obj.listHinzuzufuegen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungsergebnisKursSchuelerZuordnungUpdate>) : string {
		let result = '{';
		if (typeof obj.listEntfernen !== "undefined") {
			if (!obj.listEntfernen) {
				result += '"listEntfernen" : []';
			} else {
				result += '"listEntfernen" : [ ';
				for (let i = 0; i < obj.listEntfernen.size(); i++) {
					const elem = obj.listEntfernen.get(i);
					result += GostBlockungsergebnisKursSchuelerZuordnung.transpilerToJSON(elem);
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
					result += GostBlockungsergebnisKursSchuelerZuordnung.transpilerToJSON(elem);
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

export function cast_de_svws_nrw_core_data_gost_GostBlockungsergebnisKursSchuelerZuordnungUpdate(obj : unknown) : GostBlockungsergebnisKursSchuelerZuordnungUpdate {
	return obj as GostBlockungsergebnisKursSchuelerZuordnungUpdate;
}
