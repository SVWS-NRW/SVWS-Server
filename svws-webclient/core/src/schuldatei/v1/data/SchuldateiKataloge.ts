import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuldateiKatalogeintrag } from '../../../schuldatei/v1/data/SchuldateiKatalogeintrag';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';

export class SchuldateiKataloge extends JavaObject {

	/**
	 * Die Katalog-Einträge
	 */
	public katalog : List<SchuldateiKatalogeintrag> = new ArrayList<SchuldateiKatalogeintrag>();


	/**
	 * Erstellt Kataloge zu der Schuldatei
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schuldatei.v1.data.SchuldateiKataloge';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schuldatei.v1.data.SchuldateiKataloge'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuldateiKataloge {
		const obj = JSON.parse(json);
		const result = new SchuldateiKataloge();
		if ((obj.katalog !== undefined) && (obj.katalog !== null)) {
			for (const elem of obj.katalog) {
				result.katalog?.add(SchuldateiKatalogeintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchuldateiKataloge) : string {
		let result = '{';
		if (!obj.katalog) {
			result += '"katalog" : []';
		} else {
			result += '"katalog" : [ ';
			for (let i = 0; i < obj.katalog.size(); i++) {
				const elem = obj.katalog.get(i);
				result += SchuldateiKatalogeintrag.transpilerToJSON(elem);
				if (i < obj.katalog.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuldateiKataloge>) : string {
		let result = '{';
		if (typeof obj.katalog !== "undefined") {
			if (!obj.katalog) {
				result += '"katalog" : []';
			} else {
				result += '"katalog" : [ ';
				for (let i = 0; i < obj.katalog.size(); i++) {
					const elem = obj.katalog.get(i);
					result += SchuldateiKatalogeintrag.transpilerToJSON(elem);
					if (i < obj.katalog.size() - 1)
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

export function cast_de_svws_nrw_schuldatei_v1_data_SchuldateiKataloge(obj : unknown) : SchuldateiKataloge {
	return obj as SchuldateiKataloge;
}
