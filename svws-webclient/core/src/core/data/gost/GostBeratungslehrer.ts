import { JavaObject } from '../../../java/lang/JavaObject';

export class GostBeratungslehrer extends JavaObject {

	/**
	 * Die ID des Lehrers.
	 */
	public id : number = 0;

	/**
	 * Das Kürzel des Lehrers.
	 */
	public kuerzel : string | null = null;

	/**
	 * Der Nachname des Lehrers.
	 */
	public nachname : string | null = null;

	/**
	 * Der Vorname des Lehrers.
	 */
	public vorname : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostBeratungslehrer';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostBeratungslehrer'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBeratungslehrer {
		const obj = JSON.parse(json);
		const result = new GostBeratungslehrer();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = typeof obj.kuerzel === "undefined" ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.nachname = typeof obj.nachname === "undefined" ? null : obj.nachname === null ? null : obj.nachname;
		result.vorname = typeof obj.vorname === "undefined" ? null : obj.vorname === null ? null : obj.vorname;
		return result;
	}

	public static transpilerToJSON(obj : GostBeratungslehrer) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"nachname" : ' + ((!obj.nachname) ? 'null' : JSON.stringify(obj.nachname)) + ',';
		result += '"vorname" : ' + ((!obj.vorname) ? 'null' : JSON.stringify(obj.vorname)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBeratungslehrer>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		}
		if (typeof obj.nachname !== "undefined") {
			result += '"nachname" : ' + ((!obj.nachname) ? 'null' : JSON.stringify(obj.nachname)) + ',';
		}
		if (typeof obj.vorname !== "undefined") {
			result += '"vorname" : ' + ((!obj.vorname) ? 'null' : JSON.stringify(obj.vorname)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostBeratungslehrer(obj : unknown) : GostBeratungslehrer {
	return obj as GostBeratungslehrer;
}
