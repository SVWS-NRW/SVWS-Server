import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class StundenplanJahrgang extends JavaObject {

	/**
	 * Die ID des Jahrgangs.
	 */
	public id : number = -1;

	/**
	 * Das Kürzel des Jahrgangs.
	 */
	public kuerzel : string = "";

	/**
	 * Die Bezeichnung des Jahrgangs.
	 */
	public bezeichnung : string = "";


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplan.StundenplanJahrgang';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanJahrgang'].includes(name);
	}

	public static class = new Class<StundenplanJahrgang>('de.svws_nrw.core.data.stundenplan.StundenplanJahrgang');

	public static transpilerFromJSON(json : string): StundenplanJahrgang {
		const obj = JSON.parse(json) as Partial<StundenplanJahrgang>;
		const result = new StundenplanJahrgang();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanJahrgang) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanJahrgang>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanJahrgang(obj : unknown) : StundenplanJahrgang {
	return obj as StundenplanJahrgang;
}
