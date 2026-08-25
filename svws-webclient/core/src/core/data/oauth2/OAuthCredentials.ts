import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class OAuthCredentials extends JavaObject {

	/**
	 * Die ID des Datensatzes
	 */
	public id: number = 0;

	/**
	 * Die Client ID
	 */
	public clientId: string | null = null;

	/**
	 * Das Client Secret
	 */
	public clientSecret: string | null = null;

	/**
	 * Die URL des Auth Servers
	 */
	public tokenUrl: string | null = null;

	/**
	 * Das Scope das Requested wird
	 */
	public requestedScope: string | null = null;

	/**
	 *  Die Domäne der Credentials
	 *  @see OAuthServiceDomain
	 */
	public domain: string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.oauth2.OAuthCredentials';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.oauth2.OAuthCredentials'].includes(name);
	}

	public static readonly class = new Class<OAuthCredentials>('de.svws_nrw.core.data.oauth2.OAuthCredentials');

	public static transpilerFromJSON(json: string): OAuthCredentials {
		const obj = JSON.parse(json) as Partial<OAuthCredentials>;
		const result = new OAuthCredentials();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.clientId = (obj.clientId === undefined) ? null : obj.clientId === null ? null : obj.clientId;
		result.clientSecret = (obj.clientSecret === undefined) ? null : obj.clientSecret === null ? null : obj.clientSecret;
		result.tokenUrl = (obj.tokenUrl === undefined) ? null : obj.tokenUrl === null ? null : obj.tokenUrl;
		result.requestedScope = (obj.requestedScope === undefined) ? null : obj.requestedScope === null ? null : obj.requestedScope;
		result.domain = (obj.domain === undefined) ? null : obj.domain === null ? null : obj.domain;
		return result;
	}

	public static transpilerToJSON(obj: OAuthCredentials): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"clientId" : ' + ((obj.clientId === null) ? 'null' : JSON.stringify(obj.clientId)) + ',';
		result += '"clientSecret" : ' + ((obj.clientSecret === null) ? 'null' : JSON.stringify(obj.clientSecret)) + ',';
		result += '"tokenUrl" : ' + ((obj.tokenUrl === null) ? 'null' : JSON.stringify(obj.tokenUrl)) + ',';
		result += '"requestedScope" : ' + ((obj.requestedScope === null) ? 'null' : JSON.stringify(obj.requestedScope)) + ',';
		result += '"domain" : ' + ((obj.domain === null) ? 'null' : JSON.stringify(obj.domain)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<OAuthCredentials>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.clientId !== undefined) {
			result += '"clientId" : ' + ((obj.clientId === null) ? 'null' : JSON.stringify(obj.clientId)) + ',';
		}
		if (obj.clientSecret !== undefined) {
			result += '"clientSecret" : ' + ((obj.clientSecret === null) ? 'null' : JSON.stringify(obj.clientSecret)) + ',';
		}
		if (obj.tokenUrl !== undefined) {
			result += '"tokenUrl" : ' + ((obj.tokenUrl === null) ? 'null' : JSON.stringify(obj.tokenUrl)) + ',';
		}
		if (obj.requestedScope !== undefined) {
			result += '"requestedScope" : ' + ((obj.requestedScope === null) ? 'null' : JSON.stringify(obj.requestedScope)) + ',';
		}
		if (obj.domain !== undefined) {
			result += '"domain" : ' + ((obj.domain === null) ? 'null' : JSON.stringify(obj.domain)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_oauth2_OAuthCredentials(obj: unknown): OAuthCredentials {
	return obj as OAuthCredentials;
}
