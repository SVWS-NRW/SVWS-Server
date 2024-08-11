import { JavaObject } from '../../../java/lang/JavaObject';

export class OAuth2ClientSecret extends JavaObject {

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


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.oauth2.OAuth2ClientSecret';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.oauth2.OAuth2ClientSecret'].includes(name);
	}

	public static transpilerFromJSON(json : string): OAuth2ClientSecret {
		const obj = JSON.parse(json) as Partial<OAuth2ClientSecret>;
		const result = new OAuth2ClientSecret();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.authServer = (obj.authServer === undefined) ? null : obj.authServer === null ? null : obj.authServer;
		result.clientID = (obj.clientID === undefined) ? null : obj.clientID === null ? null : obj.clientID;
		result.clientSecret = (obj.clientSecret === undefined) ? null : obj.clientSecret === null ? null : obj.clientSecret;
		return result;
	}

	public static transpilerToJSON(obj : OAuth2ClientSecret) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"authServer" : ' + ((!obj.authServer) ? 'null' : JSON.stringify(obj.authServer)) + ',';
		result += '"clientID" : ' + ((!obj.clientID) ? 'null' : JSON.stringify(obj.clientID)) + ',';
		result += '"clientSecret" : ' + ((!obj.clientSecret) ? 'null' : JSON.stringify(obj.clientSecret)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<OAuth2ClientSecret>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.authServer !== undefined) {
			result += '"authServer" : ' + ((!obj.authServer) ? 'null' : JSON.stringify(obj.authServer)) + ',';
		}
		if (obj.clientID !== undefined) {
			result += '"clientID" : ' + ((!obj.clientID) ? 'null' : JSON.stringify(obj.clientID)) + ',';
		}
		if (obj.clientSecret !== undefined) {
			result += '"clientSecret" : ' + ((!obj.clientSecret) ? 'null' : JSON.stringify(obj.clientSecret)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_oauth2_OAuth2ClientSecret(obj : unknown) : OAuth2ClientSecret {
	return obj as OAuth2ClientSecret;
}
