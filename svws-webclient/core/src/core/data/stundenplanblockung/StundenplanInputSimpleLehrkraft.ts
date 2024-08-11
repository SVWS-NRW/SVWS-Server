import { JavaObject } from '../../../java/lang/JavaObject';

export class StundenplanInputSimpleLehrkraft extends JavaObject {

	/**
	 * Die ID der Lehrkraft.
	 */
	public id : number = 0;

	/**
	 * Das Kürzel der Lehrkraft. Beispielsweise 'BAR'.
	 */
	public kuerzel : string = "";


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplanblockung.StundenplanInputSimpleLehrkraft';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplanblockung.StundenplanInputSimpleLehrkraft'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanInputSimpleLehrkraft {
		const obj = JSON.parse(json) as Partial<StundenplanInputSimpleLehrkraft>;
		const result = new StundenplanInputSimpleLehrkraft();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanInputSimpleLehrkraft) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanInputSimpleLehrkraft>) : string {
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

export function cast_de_svws_nrw_core_data_stundenplanblockung_StundenplanInputSimpleLehrkraft(obj : unknown) : StundenplanInputSimpleLehrkraft {
	return obj as StundenplanInputSimpleLehrkraft;
}
