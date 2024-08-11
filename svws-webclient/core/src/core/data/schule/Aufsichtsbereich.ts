import { JavaObject } from '../../../java/lang/JavaObject';

export class Aufsichtsbereich extends JavaObject {

	/**
	 * Die ID des Aufsichtsbereichs.
	 */
	public id : number = -1;

	/**
	 * Das Kürzel des Aufsichtsbereichs.
	 */
	public kuerzel : string = "";

	/**
	 * Die Beschreibung des Aufsichtsbereichs.
	 */
	public beschreibung : string = "";


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.Aufsichtsbereich';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.Aufsichtsbereich'].includes(name);
	}

	public static transpilerFromJSON(json : string): Aufsichtsbereich {
		const obj = JSON.parse(json) as Partial<Aufsichtsbereich>;
		const result = new Aufsichtsbereich();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.beschreibung === undefined)
			throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = obj.beschreibung;
		return result;
	}

	public static transpilerToJSON(obj : Aufsichtsbereich) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Aufsichtsbereich>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.beschreibung !== undefined) {
			result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_Aufsichtsbereich(obj : unknown) : Aufsichtsbereich {
	return obj as Aufsichtsbereich;
}
