import { JavaObject } from '../../../../java/lang/JavaObject';
import { ENMv2Floskel, cast_de_svws_nrw_core_data_enm_v2_ENMv2Floskel } from '../../../../core/data/enm/v2/ENMv2Floskel';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

export class ENMv2Floskelgruppe extends JavaObject {

	/**
	 * Das Kürzel der Floskelgruppe, z. B. AL1, AL2 oder ASV.
	 */
	public kuerzel: string = "";

	/**
	 * Die textuelle Bezeichnung der Floskelgruppe, z. B. Allgemeine Floskeln oder Floskeln zum Arbeits- und Sozialverhalten.
	 */
	public bezeichnung: string | null = null;

	/**
	 * Die Hauptgruppe für Floskeln. Diese kann bei mehreren Floskelgruppen auftreten und fasst diese ggf. nochmals zusammen (z.B. ALLG)
	 */
	public hauptgruppe: string | null = null;

	/**
	 * Die Liste der Floskeln, die dieser Floskelgruppe zugeordnet sind.
	 */
	public readonly floskeln: List<ENMv2Floskel> = new ArrayList<ENMv2Floskel>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.v2.ENMv2Floskelgruppe';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.enm.v2.ENMv2Floskelgruppe'].includes(name);
	}

	public static readonly class = new Class<ENMv2Floskelgruppe>('de.svws_nrw.core.data.enm.v2.ENMv2Floskelgruppe');

	public static transpilerFromJSON(json: string): ENMv2Floskelgruppe {
		const obj = JSON.parse(json) as Partial<ENMv2Floskelgruppe>;
		const result = new ENMv2Floskelgruppe();
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		result.bezeichnung = (obj.bezeichnung === undefined) ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		result.hauptgruppe = (obj.hauptgruppe === undefined) ? null : obj.hauptgruppe === null ? null : obj.hauptgruppe;
		if (obj.floskeln !== undefined) {
			for (const elem of obj.floskeln) {
				result.floskeln.add(ENMv2Floskel.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: ENMv2Floskelgruppe): string {
		let result = '{';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result += '"hauptgruppe" : ' + ((obj.hauptgruppe === null) ? 'null' : JSON.stringify(obj.hauptgruppe)) + ',';
		result += '"floskeln" : [ ';
		for (let i = 0; i < obj.floskeln.size(); i++) {
			const elem = obj.floskeln.get(i);
			result += ENMv2Floskel.transpilerToJSON(elem);
			if (i < obj.floskeln.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ENMv2Floskelgruppe>): string {
		let result = '{';
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		if (obj.hauptgruppe !== undefined) {
			result += '"hauptgruppe" : ' + ((obj.hauptgruppe === null) ? 'null' : JSON.stringify(obj.hauptgruppe)) + ',';
		}
		if (obj.floskeln !== undefined) {
			result += '"floskeln" : [ ';
			for (let i = 0; i < obj.floskeln.size(); i++) {
				const elem = obj.floskeln.get(i);
				result += ENMv2Floskel.transpilerToJSON(elem);
				if (i < obj.floskeln.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_v2_ENMv2Floskelgruppe(obj: unknown): ENMv2Floskelgruppe {
	return obj as ENMv2Floskelgruppe;
}
