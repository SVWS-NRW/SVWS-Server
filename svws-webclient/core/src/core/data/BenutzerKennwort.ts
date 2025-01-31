import { JavaObject } from '../../java/lang/JavaObject';
import { Class } from '../../java/lang/Class';

export class BenutzerKennwort extends JavaObject {

	/**
	 * Der Benutzername.
	 */
	public user : string | null = null;

	/**
	 * Das Kennwort des Benutzers.
	 */
	public password : string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.BenutzerKennwort';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.BenutzerKennwort'].includes(name);
	}

	public static class = new Class<BenutzerKennwort>('de.svws_nrw.core.data.BenutzerKennwort');

	public static transpilerFromJSON(json : string): BenutzerKennwort {
		const obj = JSON.parse(json) as Partial<BenutzerKennwort>;
		const result = new BenutzerKennwort();
		result.user = (obj.user === undefined) ? null : obj.user === null ? null : obj.user;
		result.password = (obj.password === undefined) ? null : obj.password === null ? null : obj.password;
		return result;
	}

	public static transpilerToJSON(obj : BenutzerKennwort) : string {
		let result = '{';
		result += '"user" : ' + ((obj.user === null) ? 'null' : JSON.stringify(obj.user)) + ',';
		result += '"password" : ' + ((obj.password === null) ? 'null' : JSON.stringify(obj.password)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BenutzerKennwort>) : string {
		let result = '{';
		if (obj.user !== undefined) {
			result += '"user" : ' + ((obj.user === null) ? 'null' : JSON.stringify(obj.user)) + ',';
		}
		if (obj.password !== undefined) {
			result += '"password" : ' + ((obj.password === null) ? 'null' : JSON.stringify(obj.password)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_BenutzerKennwort(obj : unknown) : BenutzerKennwort {
	return obj as BenutzerKennwort;
}
