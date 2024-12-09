import { JavaObject } from '../../java/lang/JavaObject';
import { ArrayList } from '../../java/util/ArrayList';
import type { List } from '../../java/util/List';
import { Class } from '../../java/lang/Class';

export class LongAndStringLists extends JavaObject {

	/**
	 * Die Lehrer, die diesem Kurs bereits fest zugeordnet sind.
	 */
	public numbers : List<number> = new ArrayList<number>();

	/**
	 * Die Lehrer, die diesem Kurs bereits fest zugeordnet sind.
	 */
	public strings : List<string> = new ArrayList<string>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.LongAndStringLists';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.LongAndStringLists'].includes(name);
	}

	public static class = new Class<LongAndStringLists>('de.svws_nrw.core.data.LongAndStringLists');

	public static transpilerFromJSON(json : string): LongAndStringLists {
		const obj = JSON.parse(json) as Partial<LongAndStringLists>;
		const result = new LongAndStringLists();
		if (obj.numbers !== undefined) {
			for (const elem of obj.numbers) {
				result.numbers.add(elem);
			}
		}
		if (obj.strings !== undefined) {
			for (const elem of obj.strings) {
				result.strings.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : LongAndStringLists) : string {
		let result = '{';
		result += '"numbers" : [ ';
		for (let i = 0; i < obj.numbers.size(); i++) {
			const elem = obj.numbers.get(i);
			result += elem.toString();
			if (i < obj.numbers.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"strings" : [ ';
		for (let i = 0; i < obj.strings.size(); i++) {
			const elem = obj.strings.get(i);
			result += '"' + elem + '"';
			if (i < obj.strings.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LongAndStringLists>) : string {
		let result = '{';
		if (obj.numbers !== undefined) {
			result += '"numbers" : [ ';
			for (let i = 0; i < obj.numbers.size(); i++) {
				const elem = obj.numbers.get(i);
				result += elem.toString();
				if (i < obj.numbers.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.strings !== undefined) {
			result += '"strings" : [ ';
			for (let i = 0; i < obj.strings.size(); i++) {
				const elem = obj.strings.get(i);
				result += '"' + elem + '"';
				if (i < obj.strings.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_LongAndStringLists(obj : unknown) : LongAndStringLists {
	return obj as LongAndStringLists;
}
