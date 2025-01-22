import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class ENMServerConfigSMTP extends JavaObject {

	/**
	 * Die Serveradresse des SMTP-Servers
	 */
	public host : string | null = null;

	/**
	 * Der Port des SMTP-Servers
	 */
	public port : number = 587;

	/**
	 * Der Benutzername für den SMTP-Login
	 */
	public username : string | null = null;

	/**
	 * Das Passwort für den SMTP-Login
	 */
	public password : string | null = null;

	/**
	 * Verwendung TLS - true für TLS bzw. false für unverschlüsselt
	 */
	public useTLS : boolean = true;

	/**
	 * Der Absender der E-Mail
	 */
	public fromEmail : string | null = null;

	/**
	 * Der Name des Absenders
	 */
	public fromName : string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMServerConfigSMTP';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMServerConfigSMTP'].includes(name);
	}

	public static class = new Class<ENMServerConfigSMTP>('de.svws_nrw.core.data.enm.ENMServerConfigSMTP');

	public static transpilerFromJSON(json : string): ENMServerConfigSMTP {
		const obj = JSON.parse(json) as Partial<ENMServerConfigSMTP>;
		const result = new ENMServerConfigSMTP();
		result.host = (obj.host === undefined) ? null : obj.host === null ? null : obj.host;
		if (obj.port === undefined)
			throw new Error('invalid json format, missing attribute port');
		result.port = obj.port;
		result.username = (obj.username === undefined) ? null : obj.username === null ? null : obj.username;
		result.password = (obj.password === undefined) ? null : obj.password === null ? null : obj.password;
		if (obj.useTLS === undefined)
			throw new Error('invalid json format, missing attribute useTLS');
		result.useTLS = obj.useTLS;
		result.fromEmail = (obj.fromEmail === undefined) ? null : obj.fromEmail === null ? null : obj.fromEmail;
		result.fromName = (obj.fromName === undefined) ? null : obj.fromName === null ? null : obj.fromName;
		return result;
	}

	public static transpilerToJSON(obj : ENMServerConfigSMTP) : string {
		let result = '{';
		result += '"host" : ' + ((obj.host === null) ? 'null' : JSON.stringify(obj.host)) + ',';
		result += '"port" : ' + obj.port.toString() + ',';
		result += '"username" : ' + ((obj.username === null) ? 'null' : JSON.stringify(obj.username)) + ',';
		result += '"password" : ' + ((obj.password === null) ? 'null' : JSON.stringify(obj.password)) + ',';
		result += '"useTLS" : ' + obj.useTLS.toString() + ',';
		result += '"fromEmail" : ' + ((obj.fromEmail === null) ? 'null' : JSON.stringify(obj.fromEmail)) + ',';
		result += '"fromName" : ' + ((obj.fromName === null) ? 'null' : JSON.stringify(obj.fromName)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMServerConfigSMTP>) : string {
		let result = '{';
		if (obj.host !== undefined) {
			result += '"host" : ' + ((obj.host === null) ? 'null' : JSON.stringify(obj.host)) + ',';
		}
		if (obj.port !== undefined) {
			result += '"port" : ' + obj.port.toString() + ',';
		}
		if (obj.username !== undefined) {
			result += '"username" : ' + ((obj.username === null) ? 'null' : JSON.stringify(obj.username)) + ',';
		}
		if (obj.password !== undefined) {
			result += '"password" : ' + ((obj.password === null) ? 'null' : JSON.stringify(obj.password)) + ',';
		}
		if (obj.useTLS !== undefined) {
			result += '"useTLS" : ' + obj.useTLS.toString() + ',';
		}
		if (obj.fromEmail !== undefined) {
			result += '"fromEmail" : ' + ((obj.fromEmail === null) ? 'null' : JSON.stringify(obj.fromEmail)) + ',';
		}
		if (obj.fromName !== undefined) {
			result += '"fromName" : ' + ((obj.fromName === null) ? 'null' : JSON.stringify(obj.fromName)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMServerConfigSMTP(obj : unknown) : ENMServerConfigSMTP {
	return obj as ENMServerConfigSMTP;
}
