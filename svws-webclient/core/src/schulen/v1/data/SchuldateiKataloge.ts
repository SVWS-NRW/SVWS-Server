import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuldateiKatalogeintrag } from '../../../schulen/v1/data/SchuldateiKatalogeintrag';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

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
		return 'de.svws_nrw.schulen.v1.data.SchuldateiKataloge';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schulen.v1.data.SchuldateiKataloge'].includes(name);
	}

	public static class = new Class<SchuldateiKataloge>('de.svws_nrw.schulen.v1.data.SchuldateiKataloge');

	public static transpilerFromJSON(json : string): SchuldateiKataloge {
		const obj = JSON.parse(json) as Partial<SchuldateiKataloge>;
		const result = new SchuldateiKataloge();
		if (obj.katalog !== undefined) {
			for (const elem of obj.katalog) {
				result.katalog.add(SchuldateiKatalogeintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchuldateiKataloge) : string {
		let result = '{';
		result += '"katalog" : [ ';
		for (let i = 0; i < obj.katalog.size(); i++) {
			const elem = obj.katalog.get(i);
			result += SchuldateiKatalogeintrag.transpilerToJSON(elem);
			if (i < obj.katalog.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuldateiKataloge>) : string {
		let result = '{';
		if (obj.katalog !== undefined) {
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

}

export function cast_de_svws_nrw_schulen_v1_data_SchuldateiKataloge(obj : unknown) : SchuldateiKataloge {
	return obj as SchuldateiKataloge;
}
