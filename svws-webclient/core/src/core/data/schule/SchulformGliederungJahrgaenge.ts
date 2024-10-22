import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class SchulformGliederungJahrgaenge extends JavaObject {

	/**
	 * Das Kürzel der Schulform
	 */
	public schulform : string = "";

	/**
	 * Das Kürzel der Schulgliederung bzw. des Bildungsganges. Null, falls alle Gliederungen der Schulform gemeint sind.
	 */
	public gliederung : string | null = null;

	/**
	 * Die Liste der Jahrgänge.
	 */
	public jahrgaenge : List<string> = new ArrayList<string>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.SchulformGliederungJahrgaenge';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.SchulformGliederungJahrgaenge'].includes(name);
	}

	public static class = new Class<SchulformGliederungJahrgaenge>('de.svws_nrw.core.data.schule.SchulformGliederungJahrgaenge');

	public static transpilerFromJSON(json : string): SchulformGliederungJahrgaenge {
		const obj = JSON.parse(json) as Partial<SchulformGliederungJahrgaenge>;
		const result = new SchulformGliederungJahrgaenge();
		if (obj.schulform === undefined)
			throw new Error('invalid json format, missing attribute schulform');
		result.schulform = obj.schulform;
		result.gliederung = (obj.gliederung === undefined) ? null : obj.gliederung === null ? null : obj.gliederung;
		if (obj.jahrgaenge !== undefined) {
			for (const elem of obj.jahrgaenge) {
				result.jahrgaenge.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchulformGliederungJahrgaenge) : string {
		let result = '{';
		result += '"schulform" : ' + JSON.stringify(obj.schulform) + ',';
		result += '"gliederung" : ' + ((obj.gliederung === null) ? 'null' : JSON.stringify(obj.gliederung)) + ',';
		result += '"jahrgaenge" : [ ';
		for (let i = 0; i < obj.jahrgaenge.size(); i++) {
			const elem = obj.jahrgaenge.get(i);
			result += '"' + elem + '"';
			if (i < obj.jahrgaenge.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchulformGliederungJahrgaenge>) : string {
		let result = '{';
		if (obj.schulform !== undefined) {
			result += '"schulform" : ' + JSON.stringify(obj.schulform) + ',';
		}
		if (obj.gliederung !== undefined) {
			result += '"gliederung" : ' + ((obj.gliederung === null) ? 'null' : JSON.stringify(obj.gliederung)) + ',';
		}
		if (obj.jahrgaenge !== undefined) {
			result += '"jahrgaenge" : [ ';
			for (let i = 0; i < obj.jahrgaenge.size(); i++) {
				const elem = obj.jahrgaenge.get(i);
				result += '"' + elem + '"';
				if (i < obj.jahrgaenge.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_SchulformGliederungJahrgaenge(obj : unknown) : SchulformGliederungJahrgaenge {
	return obj as SchulformGliederungJahrgaenge;
}
