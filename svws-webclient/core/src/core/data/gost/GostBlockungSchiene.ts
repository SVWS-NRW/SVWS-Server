import { JavaObject } from '../../../java/lang/JavaObject';

export class GostBlockungSchiene extends JavaObject {

	/**
	 * Die ID der Schiene
	 */
	public id : number = -1;

	/**
	 * Die Nummer der Schiene bei der Blockung (zur Sortierung)
	 */
	public nummer : number = 1;

	/**
	 * Bezeichnung der Schiene (z.B. LK Schiene 1)
	 */
	public bezeichnung : string = "Neue Schiene";

	/**
	 * Die Anzahl der Wochenstunden, welche der Schiene zugeordnet sind
	 */
	public wochenstunden : number = 3;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostBlockungSchiene';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostBlockungSchiene'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBlockungSchiene {
		const obj = JSON.parse(json) as Partial<GostBlockungSchiene>;
		const result = new GostBlockungSchiene();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.nummer === undefined)
			throw new Error('invalid json format, missing attribute nummer');
		result.nummer = obj.nummer;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.wochenstunden === undefined)
			throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungSchiene) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"nummer" : ' + obj.nummer.toString() + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungSchiene>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.nummer !== undefined) {
			result += '"nummer" : ' + obj.nummer.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.wochenstunden !== undefined) {
			result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostBlockungSchiene(obj : unknown) : GostBlockungSchiene {
	return obj as GostBlockungSchiene;
}
