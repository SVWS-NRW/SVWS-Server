import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class BKGymFach extends JavaObject {

	/**
	 * Die ID des Faches
	 */
	public id : number = -1;

	/**
	 * Das Statistik-Kürzel des Faches
	 */
	public kuerzel : string = "";

	/**
	 * Das Fach-Kürzel, welches zur Anzeige verwendet wird.
	 */
	public kuerzelAnzeige : string | null = null;

	/**
	 * Die Bezeichnung des Faches
	 */
	public bezeichnung : string | null = null;

	/**
	 * Die Nummer, welche die Sortierung der Fächer angibt.
	 */
	public sortierung : number = 32000;

	/**
	 * Gibt an, ob es sich bei dem Fach um eine Fremdsprache handelt oder nicht
	 */
	public istFremdsprache : boolean = false;

	/**
	 * Gibt an, ob das Fache eine neu einsetzende Fremdsprache ist.
	 */
	public istFremdSpracheNeuEinsetzend : boolean = false;

	/**
	 * Gibt im Falle eines bilingualen Sachfaches das einstellige Fremdsprachenkürzel an.
	 */
	public biliSprache : string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.bk.abi.BKGymFach';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.bk.abi.BKGymFach'].includes(name);
	}

	public static class = new Class<BKGymFach>('de.svws_nrw.core.data.bk.abi.BKGymFach');

	public static transpilerFromJSON(json : string): BKGymFach {
		const obj = JSON.parse(json) as Partial<BKGymFach>;
		const result = new BKGymFach();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		result.kuerzelAnzeige = (obj.kuerzelAnzeige === undefined) ? null : obj.kuerzelAnzeige === null ? null : obj.kuerzelAnzeige;
		result.bezeichnung = (obj.bezeichnung === undefined) ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (obj.istFremdsprache === undefined)
			throw new Error('invalid json format, missing attribute istFremdsprache');
		result.istFremdsprache = obj.istFremdsprache;
		if (obj.istFremdSpracheNeuEinsetzend === undefined)
			throw new Error('invalid json format, missing attribute istFremdSpracheNeuEinsetzend');
		result.istFremdSpracheNeuEinsetzend = obj.istFremdSpracheNeuEinsetzend;
		result.biliSprache = (obj.biliSprache === undefined) ? null : obj.biliSprache === null ? null : obj.biliSprache;
		return result;
	}

	public static transpilerToJSON(obj : BKGymFach) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"kuerzelAnzeige" : ' + ((obj.kuerzelAnzeige === null) ? 'null' : JSON.stringify(obj.kuerzelAnzeige)) + ',';
		result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"istFremdsprache" : ' + obj.istFremdsprache.toString() + ',';
		result += '"istFremdSpracheNeuEinsetzend" : ' + obj.istFremdSpracheNeuEinsetzend.toString() + ',';
		result += '"biliSprache" : ' + ((obj.biliSprache === null) ? 'null' : JSON.stringify(obj.biliSprache)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BKGymFach>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.kuerzelAnzeige !== undefined) {
			result += '"kuerzelAnzeige" : ' + ((obj.kuerzelAnzeige === null) ? 'null' : JSON.stringify(obj.kuerzelAnzeige)) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.istFremdsprache !== undefined) {
			result += '"istFremdsprache" : ' + obj.istFremdsprache.toString() + ',';
		}
		if (obj.istFremdSpracheNeuEinsetzend !== undefined) {
			result += '"istFremdSpracheNeuEinsetzend" : ' + obj.istFremdSpracheNeuEinsetzend.toString() + ',';
		}
		if (obj.biliSprache !== undefined) {
			result += '"biliSprache" : ' + ((obj.biliSprache === null) ? 'null' : JSON.stringify(obj.biliSprache)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_bk_abi_BKGymFach(obj : unknown) : BKGymFach {
	return obj as BKGymFach;
}
