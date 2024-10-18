import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class StundenplanInputSimpleKlasse extends JavaObject {

	/**
	 * Die ID der Klasse.
	 */
	public id : number = 0;

	/**
	 * Das Kürzel der Klasse. Beispielsweise '7b' oder 'Q1'.
	 */
	public kuerzel : string = "";


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplanblockung.StundenplanInputSimpleKlasse';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplanblockung.StundenplanInputSimpleKlasse'].includes(name);
	}

	public static class = new Class<StundenplanInputSimpleKlasse>('de.svws_nrw.core.data.stundenplanblockung.StundenplanInputSimpleKlasse');

	public static transpilerFromJSON(json : string): StundenplanInputSimpleKlasse {
		const obj = JSON.parse(json) as Partial<StundenplanInputSimpleKlasse>;
		const result = new StundenplanInputSimpleKlasse();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanInputSimpleKlasse) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanInputSimpleKlasse>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplanblockung_StundenplanInputSimpleKlasse(obj : unknown) : StundenplanInputSimpleKlasse {
	return obj as StundenplanInputSimpleKlasse;
}
