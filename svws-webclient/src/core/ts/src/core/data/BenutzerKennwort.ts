import { JavaObject } from '../../java/lang/JavaObject';

export class BenutzerKennwort extends JavaObject {

	/**
	 * Der Benutzername.
	 */
	public user : string | null = null;

	/**
	 * Das Kennwort des Benutzers.
	 */
	public password : string | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.BenutzerKennwort'].includes(name);
	}

	public static transpilerFromJSON(json : string): BenutzerKennwort {
		const obj = JSON.parse(json);
		const result = new BenutzerKennwort();
		result.user = typeof obj.user === "undefined" ? null : obj.user === null ? null : obj.user;
		result.password = typeof obj.password === "undefined" ? null : obj.password === null ? null : obj.password;
		return result;
	}

	public static transpilerToJSON(obj : BenutzerKennwort) : string {
		let result = '{';
		result += '"user" : ' + ((!obj.user) ? 'null' : '"' + obj.user + '"') + ',';
		result += '"password" : ' + ((!obj.password) ? 'null' : '"' + obj.password + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BenutzerKennwort>) : string {
		let result = '{';
		if (typeof obj.user !== "undefined") {
			result += '"user" : ' + ((!obj.user) ? 'null' : '"' + obj.user + '"') + ',';
		}
		if (typeof obj.password !== "undefined") {
			result += '"password" : ' + ((!obj.password) ? 'null' : '"' + obj.password + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_BenutzerKennwort(obj : unknown) : BenutzerKennwort {
	return obj as BenutzerKennwort;
}
