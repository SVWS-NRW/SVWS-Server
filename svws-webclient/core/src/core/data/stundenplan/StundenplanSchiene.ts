import { JavaObject } from '../../../java/lang/JavaObject';

export class StundenplanSchiene extends JavaObject {

	/**
	 * Die ID der Schiene.
	 */
	public id : number = -1;

	/**
	 * Die ID des Jahrgangs, dem die Schiene zugeordnet ist.
	 */
	public idJahrgang : number = -1;

	/**
	 * Die Nummer der Schiene.
	 */
	public nummer : number = -1;

	/**
	 * Die Bezeichnung der Schiene.
	 */
	public bezeichnung : string = "";


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplan.StundenplanSchiene';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanSchiene'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanSchiene {
		const obj = JSON.parse(json) as Partial<StundenplanSchiene>;
		const result = new StundenplanSchiene();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idJahrgang === undefined)
			throw new Error('invalid json format, missing attribute idJahrgang');
		result.idJahrgang = obj.idJahrgang;
		if (obj.nummer === undefined)
			throw new Error('invalid json format, missing attribute nummer');
		result.nummer = obj.nummer;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanSchiene) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idJahrgang" : ' + obj.idJahrgang.toString() + ',';
		result += '"nummer" : ' + obj.nummer.toString() + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanSchiene>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idJahrgang !== undefined) {
			result += '"idJahrgang" : ' + obj.idJahrgang.toString() + ',';
		}
		if (obj.nummer !== undefined) {
			result += '"nummer" : ' + obj.nummer.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanSchiene(obj : unknown) : StundenplanSchiene {
	return obj as StundenplanSchiene;
}
