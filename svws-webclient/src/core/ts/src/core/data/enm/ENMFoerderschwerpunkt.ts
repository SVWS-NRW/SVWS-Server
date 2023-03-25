import { JavaObject } from '../../../java/lang/JavaObject';

export class ENMFoerderschwerpunkt extends JavaObject {

	/**
	 * Die ID des Förderschwerpunktes.
	 */
	public id : number = 0;

	/**
	 * Das Kürzel, welche im Rahmen der amtlichen Schulstatistik verwendet wird
	 */
	public kuerzel : string | null = null;

	/**
	 * Die textuelle Bezeichnung des Förderschwerpunktes.
	 */
	public beschreibung : string | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMFoerderschwerpunkt'].includes(name);
	}

	public static transpilerFromJSON(json : string): ENMFoerderschwerpunkt {
		const obj = JSON.parse(json);
		const result = new ENMFoerderschwerpunkt();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = typeof obj.kuerzel === "undefined" ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.beschreibung = typeof obj.beschreibung === "undefined" ? null : obj.beschreibung === null ? null : obj.beschreibung;
		return result;
	}

	public static transpilerToJSON(obj : ENMFoerderschwerpunkt) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel + '"') + ',';
		result += '"beschreibung" : ' + ((!obj.beschreibung) ? 'null' : '"' + obj.beschreibung + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMFoerderschwerpunkt>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel + '"') + ',';
		}
		if (typeof obj.beschreibung !== "undefined") {
			result += '"beschreibung" : ' + ((!obj.beschreibung) ? 'null' : '"' + obj.beschreibung + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMFoerderschwerpunkt(obj : unknown) : ENMFoerderschwerpunkt {
	return obj as ENMFoerderschwerpunkt;
}
