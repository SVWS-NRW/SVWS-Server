import { JavaObject } from '../../../java/lang/JavaObject';

export class BenutzerAllgemeinCredentials extends JavaObject {

	/**
	 * Benutzername des Account-Credentials
	 */
	public anzeigename : string = "";

	/**
	 * Benutzername des Account-Credentials
	 */
	public benutzername : string = "";

	/**
	 * Passwort des Account-Credentials
	 */
	public password : string = "";


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.benutzer.BenutzerAllgemeinCredentials';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.benutzer.BenutzerAllgemeinCredentials'].includes(name);
	}

	public static transpilerFromJSON(json : string): BenutzerAllgemeinCredentials {
		const obj = JSON.parse(json);
		const result = new BenutzerAllgemeinCredentials();
		if (typeof obj.anzeigename === "undefined")
			 throw new Error('invalid json format, missing attribute anzeigename');
		result.anzeigename = obj.anzeigename;
		if (typeof obj.benutzername === "undefined")
			 throw new Error('invalid json format, missing attribute benutzername');
		result.benutzername = obj.benutzername;
		if (typeof obj.password === "undefined")
			 throw new Error('invalid json format, missing attribute password');
		result.password = obj.password;
		return result;
	}

	public static transpilerToJSON(obj : BenutzerAllgemeinCredentials) : string {
		let result = '{';
		result += '"anzeigename" : ' + JSON.stringify(obj.anzeigename!) + ',';
		result += '"benutzername" : ' + JSON.stringify(obj.benutzername!) + ',';
		result += '"password" : ' + JSON.stringify(obj.password!) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BenutzerAllgemeinCredentials>) : string {
		let result = '{';
		if (typeof obj.anzeigename !== "undefined") {
			result += '"anzeigename" : ' + JSON.stringify(obj.anzeigename!) + ',';
		}
		if (typeof obj.benutzername !== "undefined") {
			result += '"benutzername" : ' + JSON.stringify(obj.benutzername!) + ',';
		}
		if (typeof obj.password !== "undefined") {
			result += '"password" : ' + JSON.stringify(obj.password!) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_benutzer_BenutzerAllgemeinCredentials(obj : unknown) : BenutzerAllgemeinCredentials {
	return obj as BenutzerAllgemeinCredentials;
}
