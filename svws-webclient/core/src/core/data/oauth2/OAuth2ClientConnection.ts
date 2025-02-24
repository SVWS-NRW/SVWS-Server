import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class OAuth2ClientConnection extends JavaObject {

	/**
	 * Die ID des OAuth2 Client Secrets.
	 */
	public id : number = 0;

	/**
	 * Die URL des Auth-Servers.
	 */
	public authServer : string | null = null;

	/**
	 * Die Client-ID für diesen Auth-Server.
	 */
	public clientID : string | null = null;

	/**
	 * Das Client-Secret für die Client ID für diesen Auth-Server.
	 */
	public clientSecret : string | null = null;

	/**
	 * Das TLS-Zertifikat, welches von dem Auth-Server verwendet wird.
	 */
	public tlsCert : string | null = null;

	/**
	 * Gibt an, ob das TLS-Zertifikat von dem SVWS-Server über die Chain automatisch validiert werden kann.
	 */
	public tlsCertIsKnown : boolean = false;

	/**
	 * Gibt an, ob dem TLS-Zertifikat von dem SVWS-Server vertraut wird oder nicht.
	 */
	public tlsCertIsTrusted : boolean = false;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.oauth2.OAuth2ClientConnection';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.oauth2.OAuth2ClientConnection'].includes(name);
	}

	public static class = new Class<OAuth2ClientConnection>('de.svws_nrw.core.data.oauth2.OAuth2ClientConnection');

	public static transpilerFromJSON(json : string): OAuth2ClientConnection {
		const obj = JSON.parse(json) as Partial<OAuth2ClientConnection>;
		const result = new OAuth2ClientConnection();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.authServer = (obj.authServer === undefined) ? null : obj.authServer === null ? null : obj.authServer;
		result.clientID = (obj.clientID === undefined) ? null : obj.clientID === null ? null : obj.clientID;
		result.clientSecret = (obj.clientSecret === undefined) ? null : obj.clientSecret === null ? null : obj.clientSecret;
		result.tlsCert = (obj.tlsCert === undefined) ? null : obj.tlsCert === null ? null : obj.tlsCert;
		if (obj.tlsCertIsKnown === undefined)
			throw new Error('invalid json format, missing attribute tlsCertIsKnown');
		result.tlsCertIsKnown = obj.tlsCertIsKnown;
		if (obj.tlsCertIsTrusted === undefined)
			throw new Error('invalid json format, missing attribute tlsCertIsTrusted');
		result.tlsCertIsTrusted = obj.tlsCertIsTrusted;
		return result;
	}

	public static transpilerToJSON(obj : OAuth2ClientConnection) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"authServer" : ' + ((obj.authServer === null) ? 'null' : JSON.stringify(obj.authServer)) + ',';
		result += '"clientID" : ' + ((obj.clientID === null) ? 'null' : JSON.stringify(obj.clientID)) + ',';
		result += '"clientSecret" : ' + ((obj.clientSecret === null) ? 'null' : JSON.stringify(obj.clientSecret)) + ',';
		result += '"tlsCert" : ' + ((obj.tlsCert === null) ? 'null' : JSON.stringify(obj.tlsCert)) + ',';
		result += '"tlsCertIsKnown" : ' + obj.tlsCertIsKnown.toString() + ',';
		result += '"tlsCertIsTrusted" : ' + obj.tlsCertIsTrusted.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<OAuth2ClientConnection>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.authServer !== undefined) {
			result += '"authServer" : ' + ((obj.authServer === null) ? 'null' : JSON.stringify(obj.authServer)) + ',';
		}
		if (obj.clientID !== undefined) {
			result += '"clientID" : ' + ((obj.clientID === null) ? 'null' : JSON.stringify(obj.clientID)) + ',';
		}
		if (obj.clientSecret !== undefined) {
			result += '"clientSecret" : ' + ((obj.clientSecret === null) ? 'null' : JSON.stringify(obj.clientSecret)) + ',';
		}
		if (obj.tlsCert !== undefined) {
			result += '"tlsCert" : ' + ((obj.tlsCert === null) ? 'null' : JSON.stringify(obj.tlsCert)) + ',';
		}
		if (obj.tlsCertIsKnown !== undefined) {
			result += '"tlsCertIsKnown" : ' + obj.tlsCertIsKnown.toString() + ',';
		}
		if (obj.tlsCertIsTrusted !== undefined) {
			result += '"tlsCertIsTrusted" : ' + obj.tlsCertIsTrusted.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_oauth2_OAuth2ClientConnection(obj : unknown) : OAuth2ClientConnection {
	return obj as OAuth2ClientConnection;
}
