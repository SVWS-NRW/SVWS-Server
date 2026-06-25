import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class OAuthCredentials extends JavaObject {

	/**
	 * CLient ID
	 */
	public clientId: string | null = null;

	/**
	 * Client Secret
	 */
	public clientSecret: string | null = null;

	/**
	 * tokenUrl
	 */
	public tokenUrl: string | null = null;

	/**
	 * defaultScope
	 */
	public defaultScope: string | null = null;


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
		result.clientId = (obj.clientId === undefined) ? null : obj.clientId === null ? null : obj.clientId;
		result.clientSecret = (obj.clientSecret === undefined) ? null : obj.clientSecret === null ? null : obj.clientSecret;
		result.tokenUrl = (obj.tokenUrl === undefined) ? null : obj.tokenUrl === null ? null : obj.tokenUrl;
		result.defaultScope = (obj.defaultScope === undefined) ? null : obj.defaultScope === null ? null : obj.defaultScope;
		return result;
	}

	public static transpilerToJSON(obj: OAuthCredentials): string {
		let result = '{';
		result += '"clientId" : ' + ((obj.clientId === null) ? 'null' : JSON.stringify(obj.clientId)) + ',';
		result += '"clientSecret" : ' + ((obj.clientSecret === null) ? 'null' : JSON.stringify(obj.clientSecret)) + ',';
		result += '"tokenUrl" : ' + ((obj.tokenUrl === null) ? 'null' : JSON.stringify(obj.tokenUrl)) + ',';
		result += '"defaultScope" : ' + ((obj.defaultScope === null) ? 'null' : JSON.stringify(obj.defaultScope)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<OAuthCredentials>): string {
		let result = '{';
		if (obj.clientId !== undefined) {
			result += '"clientId" : ' + ((obj.clientId === null) ? 'null' : JSON.stringify(obj.clientId)) + ',';
		}
		if (obj.clientSecret !== undefined) {
			result += '"clientSecret" : ' + ((obj.clientSecret === null) ? 'null' : JSON.stringify(obj.clientSecret)) + ',';
		}
		if (obj.tokenUrl !== undefined) {
			result += '"tokenUrl" : ' + ((obj.tokenUrl === null) ? 'null' : JSON.stringify(obj.tokenUrl)) + ',';
		}
		if (obj.defaultScope !== undefined) {
			result += '"defaultScope" : ' + ((obj.defaultScope === null) ? 'null' : JSON.stringify(obj.defaultScope)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_oauth2_OAuthCredentials(obj: unknown): OAuthCredentials {
	return obj as OAuthCredentials;
}
