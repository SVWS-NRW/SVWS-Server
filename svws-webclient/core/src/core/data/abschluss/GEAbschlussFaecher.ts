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
	public faecher : List<GEAbschlussFach> = new ArrayList<GEAbschlussFach>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.abschluss.GEAbschlussFaecher';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.abschluss.GEAbschlussFaecher'].includes(name);
	}

	public static transpilerFromJSON(json : string): GEAbschlussFaecher {
		const obj = JSON.parse(json) as Partial<GEAbschlussFaecher>;
		const result = new GEAbschlussFaecher();
		if (obj.schuljahr === undefined)
			throw new Error('invalid json format, missing attribute schuljahr');
		result.schuljahr = obj.schuljahr;
		if (obj.abschnitt === undefined)
			throw new Error('invalid json format, missing attribute abschnitt');
		result.abschnitt = obj.abschnitt;
		result.jahrgang = (obj.jahrgang === undefined) ? null : obj.jahrgang === null ? null : obj.jahrgang;
		if (obj.faecher !== undefined) {
			for (const elem of obj.faecher) {
				result.faecher.add(GEAbschlussFach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GEAbschlussFaecher) : string {
		let result = '{';
		result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		result += '"abschnitt" : ' + obj.abschnitt.toString() + ',';
		result += '"jahrgang" : ' + ((!obj.jahrgang) ? 'null' : JSON.stringify(obj.jahrgang)) + ',';
		result += '"faecher" : [ ';
		for (let i = 0; i < obj.faecher.size(); i++) {
			const elem = obj.faecher.get(i);
			result += GEAbschlussFach.transpilerToJSON(elem);
			if (i < obj.faecher.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GEAbschlussFaecher>) : string {
		let result = '{';
		if (obj.schuljahr !== undefined) {
			result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		}
		if (obj.abschnitt !== undefined) {
			result += '"abschnitt" : ' + obj.abschnitt.toString() + ',';
		}
		if (obj.jahrgang !== undefined) {
			result += '"jahrgang" : ' + ((!obj.jahrgang) ? 'null' : JSON.stringify(obj.jahrgang)) + ',';
		}
		if (obj.faecher !== undefined) {
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

}

export function cast_de_svws_nrw_core_data_abschluss_GEAbschlussFaecher(obj : unknown) : GEAbschlussFaecher {
	return obj as GEAbschlussFaecher;
}
