import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';

export class SchuleAbschnitte extends JavaObject {

	/**
	 * Die Anzahl der Abschnitte pro Jahr
	 */
	public anzahlAbschnitte : number = 0;

	/**
	 * Die allgemeine Bezeichnung der Abschnitte (z.B. Quartal oder Halbjahr)
	 */
	public abschnittBez : string = "Halbjahr";

	/**
	 * Eine Liste der einzelnen speziellen Bezeichnungnen für dei Abschnitte (z.B. 1. Quartal, 2. Quartal, ...)
	 */
	public bezAbschnitte : List<string> = new ArrayList<string>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.SchuleAbschnitte';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.SchuleAbschnitte'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuleAbschnitte {
		const obj = JSON.parse(json) as Partial<SchuleAbschnitte>;
		const result = new SchuleAbschnitte();
		if (obj.anzahlAbschnitte === undefined)
			throw new Error('invalid json format, missing attribute anzahlAbschnitte');
		result.anzahlAbschnitte = obj.anzahlAbschnitte;
		if (obj.abschnittBez === undefined)
			throw new Error('invalid json format, missing attribute abschnittBez');
		result.abschnittBez = obj.abschnittBez;
		if (obj.bezAbschnitte !== undefined) {
			for (const elem of obj.bezAbschnitte) {
				result.bezAbschnitte.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchuleAbschnitte) : string {
		let result = '{';
		result += '"anzahlAbschnitte" : ' + obj.anzahlAbschnitte.toString() + ',';
		result += '"abschnittBez" : ' + JSON.stringify(obj.abschnittBez) + ',';
		result += '"bezAbschnitte" : [ ';
		for (let i = 0; i < obj.bezAbschnitte.size(); i++) {
			const elem = obj.bezAbschnitte.get(i);
			result += '"' + elem + '"';
			if (i < obj.bezAbschnitte.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuleAbschnitte>) : string {
		let result = '{';
		if (obj.anzahlAbschnitte !== undefined) {
			result += '"anzahlAbschnitte" : ' + obj.anzahlAbschnitte.toString() + ',';
		}
		if (obj.abschnittBez !== undefined) {
			result += '"abschnittBez" : ' + JSON.stringify(obj.abschnittBez) + ',';
		}
		if (obj.bezAbschnitte !== undefined) {
			result += '"bezAbschnitte" : [ ';
			for (let i = 0; i < obj.bezAbschnitte.size(); i++) {
				const elem = obj.bezAbschnitte.get(i);
				result += '"' + elem + '"';
				if (i < obj.bezAbschnitte.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_SchuleAbschnitte(obj : unknown) : SchuleAbschnitte {
	return obj as SchuleAbschnitte;
}
