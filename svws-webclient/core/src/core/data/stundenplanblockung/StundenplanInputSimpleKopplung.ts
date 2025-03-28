import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class StundenplanInputSimpleKopplung extends JavaObject {

	/**
	 * Die ID der Kopplung.
	 */
	public id : number = 0;

	/**
	 * Das Kürzel des Kopplung. Beispielsweise '5RE' oder 'Q1LK1'.
	 */
	public kuerzel : string = "";

	/**
	 * Die Anzahl der Stunden der Kopplung. Muss mindestens so groß sein, wie der Kurs mit den meisten Stunden in
	 *  dieser Kopplung.
	 */
	public stunden : number = -1;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplanblockung.StundenplanInputSimpleKopplung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplanblockung.StundenplanInputSimpleKopplung'].includes(name);
	}

	public static class = new Class<StundenplanInputSimpleKopplung>('de.svws_nrw.core.data.stundenplanblockung.StundenplanInputSimpleKopplung');

	public static transpilerFromJSON(json : string): StundenplanInputSimpleKopplung {
		const obj = JSON.parse(json) as Partial<StundenplanInputSimpleKopplung>;
		const result = new StundenplanInputSimpleKopplung();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.stunden === undefined)
			throw new Error('invalid json format, missing attribute stunden');
		result.stunden = obj.stunden;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanInputSimpleKopplung) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"stunden" : ' + obj.stunden.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanInputSimpleKopplung>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.stunden !== undefined) {
			result += '"stunden" : ' + obj.stunden.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplanblockung_StundenplanInputSimpleKopplung(obj : unknown) : StundenplanInputSimpleKopplung {
	return obj as StundenplanInputSimpleKopplung;
}
