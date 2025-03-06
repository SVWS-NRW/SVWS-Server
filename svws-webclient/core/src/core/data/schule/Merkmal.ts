import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class Merkmal extends JavaObject {

	/**
	 * Die ID des Merkmals
	 */
	public id : number = 0;

	/**
	 * Gibt an, ob das Merkmal einer Schule zugewiesen werden kann
	 */
	public istSchulmerkmal : boolean = false;

	/**
	 * Gibt an, ob das Merkmal einem Schueler zugewiesen werden kann
	 */
	public istSchuelermerkmal : boolean = false;

	/**
	 * Das Kuerzel des Merkmals
	 */
	public kuerzel : string | null = null;

	/**
	 * Die Bezeichnung des Merkmals
	 */
	public bezeichnung : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.Merkmal';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.Merkmal'].includes(name);
	}

	public static class = new Class<Merkmal>('de.svws_nrw.core.data.schule.Merkmal');

	public static transpilerFromJSON(json : string): Merkmal {
		const obj = JSON.parse(json) as Partial<Merkmal>;
		const result = new Merkmal();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.istSchulmerkmal === undefined)
			throw new Error('invalid json format, missing attribute istSchulmerkmal');
		result.istSchulmerkmal = obj.istSchulmerkmal;
		if (obj.istSchuelermerkmal === undefined)
			throw new Error('invalid json format, missing attribute istSchuelermerkmal');
		result.istSchuelermerkmal = obj.istSchuelermerkmal;
		result.kuerzel = (obj.kuerzel === undefined) ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.bezeichnung = (obj.bezeichnung === undefined) ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		return result;
	}

	public static transpilerToJSON(obj : Merkmal) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"istSchulmerkmal" : ' + obj.istSchulmerkmal.toString() + ',';
		result += '"istSchuelermerkmal" : ' + obj.istSchuelermerkmal.toString() + ',';
		result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Merkmal>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.istSchulmerkmal !== undefined) {
			result += '"istSchulmerkmal" : ' + obj.istSchulmerkmal.toString() + ',';
		}
		if (obj.istSchuelermerkmal !== undefined) {
			result += '"istSchuelermerkmal" : ' + obj.istSchuelermerkmal.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_Merkmal(obj : unknown) : Merkmal {
	return obj as Merkmal;
}
