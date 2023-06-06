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
	public bezAbschnitte : List<string> = new ArrayList();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.SchuleAbschnitte'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuleAbschnitte {
		const obj = JSON.parse(json);
		const result = new SchuleAbschnitte();
		if (typeof obj.anzahlAbschnitte === "undefined")
			 throw new Error('invalid json format, missing attribute anzahlAbschnitte');
		result.anzahlAbschnitte = obj.anzahlAbschnitte;
		if (typeof obj.abschnittBez === "undefined")
			 throw new Error('invalid json format, missing attribute abschnittBez');
		result.abschnittBez = obj.abschnittBez;
		if ((obj.bezAbschnitte !== undefined) && (obj.bezAbschnitte !== null)) {
			for (const elem of obj.bezAbschnitte) {
				result.bezAbschnitte?.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchuleAbschnitte) : string {
		let result = '{';
		result += '"anzahlAbschnitte" : ' + obj.anzahlAbschnitte + ',';
		result += '"abschnittBez" : ' + '"' + obj.abschnittBez! + '"' + ',';
		if (!obj.bezAbschnitte) {
			result += '"bezAbschnitte" : []';
		} else {
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

	public static transpilerToJSONPatch(obj : Partial<SchuleAbschnitte>) : string {
		let result = '{';
		if (typeof obj.anzahlAbschnitte !== "undefined") {
			result += '"anzahlAbschnitte" : ' + obj.anzahlAbschnitte + ',';
		}
		if (typeof obj.abschnittBez !== "undefined") {
			result += '"abschnittBez" : ' + '"' + obj.abschnittBez + '"' + ',';
		}
		if (typeof obj.bezAbschnitte !== "undefined") {
			if (!obj.bezAbschnitte) {
				result += '"bezAbschnitte" : []';
			} else {
				result += '"bezAbschnitte" : [ ';
				for (let i = 0; i < obj.bezAbschnitte.size(); i++) {
					const elem = obj.bezAbschnitte.get(i);
					result += '"' + elem + '"';
					if (i < obj.bezAbschnitte.size() - 1)
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

export function cast_de_svws_nrw_core_data_schule_SchuleAbschnitte(obj : unknown) : SchuleAbschnitte {
	return obj as SchuleAbschnitte;
}
