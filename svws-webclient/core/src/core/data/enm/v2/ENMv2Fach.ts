import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class ENMv2Fach extends JavaObject {

	/**
	 * Die ID des Faches in der SVWS-DB.
	 */
	public id: number = 0;

	/**
	 * Das Kürzel des Faches, wie es im Rahmen der amtlichen Schulstatistik verwendet wird. (z.B. D)
	 */
	public kuerzel: string = "";

	/**
	 * Das Kürzel des Faches, wie es im Rahmen der Schule benannt wird und angezeigt werden soll. (z.B. D)
	 */
	public kuerzelAnzeige: string = "";

	/**
	 * Die Bezeichnung des Faches
	 */
	public bezeichnung: string = "";

	/**
	 * Die Reihenfolge des Faches bei der Sortierung der Fächer. (z.B. 37)
	 */
	public sortierung: number = 0;

	/**
	 * Gibt an, ob es sich bei dem Fach um eine Fremdsprache handelt oder nicht.
	 */
	public istFremdsprache: boolean = false;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.v2.ENMv2Fach';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.enm.v2.ENMv2Fach'].includes(name);
	}

	public static readonly class = new Class<ENMv2Fach>('de.svws_nrw.core.data.enm.v2.ENMv2Fach');

	public static transpilerFromJSON(json: string): ENMv2Fach {
		const obj = JSON.parse(json) as Partial<ENMv2Fach>;
		const result = new ENMv2Fach();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.kuerzelAnzeige === undefined)
			throw new Error('invalid json format, missing attribute kuerzelAnzeige');
		result.kuerzelAnzeige = obj.kuerzelAnzeige;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (obj.istFremdsprache === undefined)
			throw new Error('invalid json format, missing attribute istFremdsprache');
		result.istFremdsprache = obj.istFremdsprache;
		return result;
	}

	public static transpilerToJSON(obj: ENMv2Fach): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"kuerzelAnzeige" : ' + JSON.stringify(obj.kuerzelAnzeige) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"istFremdsprache" : ' + obj.istFremdsprache.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ENMv2Fach>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.kuerzelAnzeige !== undefined) {
			result += '"kuerzelAnzeige" : ' + JSON.stringify(obj.kuerzelAnzeige) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.istFremdsprache !== undefined) {
			result += '"istFremdsprache" : ' + obj.istFremdsprache.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_v2_ENMv2Fach(obj: unknown): ENMv2Fach {
	return obj as ENMv2Fach;
}
