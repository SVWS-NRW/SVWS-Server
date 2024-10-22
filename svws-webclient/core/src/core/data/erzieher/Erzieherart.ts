import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class Erzieherart extends JavaObject {

	/**
	 * ID der Erzieherart
	 */
	public id : number = 0;

	/**
	 * Bezeichnung der Erzieherart
	 */
	public bezeichnung : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.erzieher.Erzieherart';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.erzieher.Erzieherart'].includes(name);
	}

	public static class = new Class<Erzieherart>('de.svws_nrw.core.data.erzieher.Erzieherart');

	public static transpilerFromJSON(json : string): Erzieherart {
		const obj = JSON.parse(json) as Partial<Erzieherart>;
		const result = new Erzieherart();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.bezeichnung = (obj.bezeichnung === undefined) ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		return result;
	}

	public static transpilerToJSON(obj : Erzieherart) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Erzieherart>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_erzieher_Erzieherart(obj : unknown) : Erzieherart {
	return obj as Erzieherart;
}
