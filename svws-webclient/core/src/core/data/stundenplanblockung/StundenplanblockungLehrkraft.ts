import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class StundenplanblockungLehrkraft extends JavaObject {

	/**
	 * Die Datenbank-ID der Lehrkraft.
	 */
	public id : number = 0;

	/**
	 * Das Kürzel der Lehrkraft. Beispielsweise 'BAR'.
	 */
	public kuerzel : string = "";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplanblockung.StundenplanblockungLehrkraft';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplanblockung.StundenplanblockungLehrkraft'].includes(name);
	}

	public static class = new Class<StundenplanblockungLehrkraft>('de.svws_nrw.core.data.stundenplanblockung.StundenplanblockungLehrkraft');

	public static transpilerFromJSON(json : string): StundenplanblockungLehrkraft {
		const obj = JSON.parse(json) as Partial<StundenplanblockungLehrkraft>;
		const result = new StundenplanblockungLehrkraft();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanblockungLehrkraft) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanblockungLehrkraft>) : string {
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

export function cast_de_svws_nrw_core_data_stundenplanblockung_StundenplanblockungLehrkraft(obj : unknown) : StundenplanblockungLehrkraft {
	return obj as StundenplanblockungLehrkraft;
}
