import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class BenutzerLehrerCredentials extends JavaObject {

	/**
	 * Benutzername des Account-Credentials
	 */
	public idLehrer : number = -1;

	/**
	 * Benutzername des Account-Credentials
	 */
	public benutzername : string = "";

	/**
	 * Passwort des Account-Credentials
	 */
	public password : string = "";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.benutzer.BenutzerLehrerCredentials';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.benutzer.BenutzerLehrerCredentials'].includes(name);
	}

	public static class = new Class<BenutzerLehrerCredentials>('de.svws_nrw.core.data.benutzer.BenutzerLehrerCredentials');

	public static transpilerFromJSON(json : string): BenutzerLehrerCredentials {
		const obj = JSON.parse(json) as Partial<BenutzerLehrerCredentials>;
		const result = new BenutzerLehrerCredentials();
		if (obj.idLehrer === undefined)
			throw new Error('invalid json format, missing attribute idLehrer');
		result.idLehrer = obj.idLehrer;
		if (obj.benutzername === undefined)
			throw new Error('invalid json format, missing attribute benutzername');
		result.benutzername = obj.benutzername;
		if (obj.password === undefined)
			throw new Error('invalid json format, missing attribute password');
		result.password = obj.password;
		return result;
	}

	public static transpilerToJSON(obj : BenutzerLehrerCredentials) : string {
		let result = '{';
		result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		result += '"benutzername" : ' + JSON.stringify(obj.benutzername) + ',';
		result += '"password" : ' + JSON.stringify(obj.password) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BenutzerLehrerCredentials>) : string {
		let result = '{';
		if (obj.idLehrer !== undefined) {
			result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		}
		if (obj.benutzername !== undefined) {
			result += '"benutzername" : ' + JSON.stringify(obj.benutzername) + ',';
		}
		if (obj.password !== undefined) {
			result += '"password" : ' + JSON.stringify(obj.password) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_benutzer_BenutzerLehrerCredentials(obj : unknown) : BenutzerLehrerCredentials {
	return obj as BenutzerLehrerCredentials;
}
