import { JavaObject } from '../../../java/lang/JavaObject';

export class BenutzerEMailDaten extends JavaObject {

	/**
	 * Die ID des Benutzers.
	 */
	public id : number = -1;

	/**
	 * Der Name des Benutzers für das Versenden von E-Mails.
	 */
	public name : string = "";

	/**
	 * Die Mail-Adresse des Benutzers.
	 */
	public address : string = "";

	/**
	 * Der Anmeldename für den SMTP-Server.
	 */
	public usernameSMTP : string = "";

	/**
	 * Das AES-verschlüsselte SMTP-Kennwort des Benutzers.
	 */
	public passwordSMTP : string = "";

	/**
	 * Die zu verwendende Signatur beim Versenden von E-Mails.
	 */
	public signatur : string = "";


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.benutzer.BenutzerEMailDaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.benutzer.BenutzerEMailDaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): BenutzerEMailDaten {
		const obj = JSON.parse(json);
		const result = new BenutzerEMailDaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.name === "undefined")
			 throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (typeof obj.address === "undefined")
			 throw new Error('invalid json format, missing attribute address');
		result.address = obj.address;
		if (typeof obj.usernameSMTP === "undefined")
			 throw new Error('invalid json format, missing attribute usernameSMTP');
		result.usernameSMTP = obj.usernameSMTP;
		if (typeof obj.passwordSMTP === "undefined")
			 throw new Error('invalid json format, missing attribute passwordSMTP');
		result.passwordSMTP = obj.passwordSMTP;
		if (typeof obj.signatur === "undefined")
			 throw new Error('invalid json format, missing attribute signatur');
		result.signatur = obj.signatur;
		return result;
	}

	public static transpilerToJSON(obj : BenutzerEMailDaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"name" : ' + JSON.stringify(obj.name!) + ',';
		result += '"address" : ' + JSON.stringify(obj.address!) + ',';
		result += '"usernameSMTP" : ' + JSON.stringify(obj.usernameSMTP!) + ',';
		result += '"passwordSMTP" : ' + JSON.stringify(obj.passwordSMTP!) + ',';
		result += '"signatur" : ' + JSON.stringify(obj.signatur!) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BenutzerEMailDaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.name !== "undefined") {
			result += '"name" : ' + JSON.stringify(obj.name!) + ',';
		}
		if (typeof obj.address !== "undefined") {
			result += '"address" : ' + JSON.stringify(obj.address!) + ',';
		}
		if (typeof obj.usernameSMTP !== "undefined") {
			result += '"usernameSMTP" : ' + JSON.stringify(obj.usernameSMTP!) + ',';
		}
		if (typeof obj.passwordSMTP !== "undefined") {
			result += '"passwordSMTP" : ' + JSON.stringify(obj.passwordSMTP!) + ',';
		}
		if (typeof obj.signatur !== "undefined") {
			result += '"signatur" : ' + JSON.stringify(obj.signatur!) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_benutzer_BenutzerEMailDaten(obj : unknown) : BenutzerEMailDaten {
	return obj as BenutzerEMailDaten;
}
