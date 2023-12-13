import { JavaObject } from '../../../java/lang/JavaObject';

export class StundenplanblockungLehrkraft extends JavaObject {

	/**
	 * Die Datenbank-ID der Lehrkraft.
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
		return 'de.svws_nrw.core.data.stundenplanblockung.StundenplanblockungLehrkraft';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplanblockung.StundenplanblockungLehrkraft'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanblockungLehrkraft {
		const obj = JSON.parse(json);
		const result = new StundenplanblockungLehrkraft();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanblockungLehrkraft) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel!) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanblockungLehrkraft>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel!) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplanblockung_StundenplanblockungLehrkraft(obj : unknown) : StundenplanblockungLehrkraft {
	return obj as StundenplanblockungLehrkraft;
}
