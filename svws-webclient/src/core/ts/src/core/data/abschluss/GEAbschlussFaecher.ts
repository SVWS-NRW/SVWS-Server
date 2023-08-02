import { JavaObject } from '../../../java/lang/JavaObject';
import { GEAbschlussFach } from '../../../core/data/abschluss/GEAbschlussFach';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';

export class GEAbschlussFaecher extends JavaObject {

	/**
	 * Gibt das Schuljahr an, in welchem die Abschlussberechnung durchgeführt werden soll.
	 */
	public schuljahr : number = 0;

	/**
	 * Gibt den Abschnitt in des Schuljahres an, in welchem die Abschlussberechnung durchgeführt werden soll.
	 */
	public abschnitt : number = 0;

	/**
	 * Gibt den Jahrgang an, für den die Abschlussberechnung durchgeführt werden soll.
	 */
	public jahrgang : string | null = null;

	/**
	 * Eine Liste der einzelnen Fächer, die für die Abschlussberechnung genutzt werden sollen.
	 */
	public faecher : List<GEAbschlussFach> = new ArrayList();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.abschluss.GEAbschlussFaecher'].includes(name);
	}

	public static transpilerFromJSON(json : string): GEAbschlussFaecher {
		const obj = JSON.parse(json);
		const result = new GEAbschlussFaecher();
		if (typeof obj.schuljahr === "undefined")
			 throw new Error('invalid json format, missing attribute schuljahr');
		result.schuljahr = obj.schuljahr;
		if (typeof obj.abschnitt === "undefined")
			 throw new Error('invalid json format, missing attribute abschnitt');
		result.abschnitt = obj.abschnitt;
		result.jahrgang = typeof obj.jahrgang === "undefined" ? null : obj.jahrgang === null ? null : obj.jahrgang;
		if ((obj.faecher !== undefined) && (obj.faecher !== null)) {
			for (const elem of obj.faecher) {
				result.faecher?.add(GEAbschlussFach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GEAbschlussFaecher) : string {
		let result = '{';
		result += '"schuljahr" : ' + obj.schuljahr + ',';
		result += '"abschnitt" : ' + obj.abschnitt + ',';
		result += '"jahrgang" : ' + ((!obj.jahrgang) ? 'null' : JSON.stringify(obj.jahrgang)) + ',';
		if (!obj.faecher) {
			result += '"faecher" : []';
		} else {
			result += '"faecher" : [ ';
			for (let i = 0; i < obj.faecher.size(); i++) {
				const elem = obj.faecher.get(i);
				result += GEAbschlussFach.transpilerToJSON(elem);
				if (i < obj.faecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GEAbschlussFaecher>) : string {
		let result = '{';
		if (typeof obj.schuljahr !== "undefined") {
			result += '"schuljahr" : ' + obj.schuljahr + ',';
		}
		if (typeof obj.abschnitt !== "undefined") {
			result += '"abschnitt" : ' + obj.abschnitt + ',';
		}
		if (typeof obj.jahrgang !== "undefined") {
			result += '"jahrgang" : ' + ((!obj.jahrgang) ? 'null' : JSON.stringify(obj.jahrgang)) + ',';
		}
		if (typeof obj.faecher !== "undefined") {
			if (!obj.faecher) {
				result += '"faecher" : []';
			} else {
				result += '"faecher" : [ ';
				for (let i = 0; i < obj.faecher.size(); i++) {
					const elem = obj.faecher.get(i);
					result += GEAbschlussFach.transpilerToJSON(elem);
					if (i < obj.faecher.size() - 1)
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

export function cast_de_svws_nrw_core_data_abschluss_GEAbschlussFaecher(obj : unknown) : GEAbschlussFaecher {
	return obj as GEAbschlussFaecher;
}
