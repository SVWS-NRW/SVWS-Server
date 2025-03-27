import { JavaObject } from '../../../java/lang/JavaObject';
import { TLSCertificate } from '../../../core/data/TLSCertificate';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class OAuth2ClientConnection extends JavaObject {

	/**
	 * Die ID des OAuth2 Client Secrets.
	 */
	public id : number = -1;

	/**
	 * Die URL des Auth-Servers.
	 */
	public authServer : string = "";

	/**
	 * Die Client-ID für diesen Auth-Server.
	 */
	public clientID : string = "";

	/**
	 * Das Client-Secret für die Client ID für diesen Auth-Server.
	 */
	public clientSecret : string = "";

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
	 * Die Liste mit den TLS-Zertifikaten der Zertifikatskette des TLS-Servers.
	 */
	public tlsCertChain : List<TLSCertificate> = new ArrayList<TLSCertificate>();


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
		if (obj.authServer === undefined)
			throw new Error('invalid json format, missing attribute authServer');
		result.authServer = obj.authServer;
		if (obj.clientID === undefined)
			throw new Error('invalid json format, missing attribute clientID');
		result.clientID = obj.clientID;
		if (obj.clientSecret === undefined)
			throw new Error('invalid json format, missing attribute clientSecret');
		result.clientSecret = obj.clientSecret;
		result.tlsCert = (obj.tlsCert === undefined) ? null : obj.tlsCert === null ? null : obj.tlsCert;
		if (obj.tlsCertIsKnown === undefined)
			throw new Error('invalid json format, missing attribute tlsCertIsKnown');
		result.tlsCertIsKnown = obj.tlsCertIsKnown;
		if (obj.tlsCertIsTrusted === undefined)
			throw new Error('invalid json format, missing attribute tlsCertIsTrusted');
		result.tlsCertIsTrusted = obj.tlsCertIsTrusted;
		if (obj.tlsCertChain !== undefined) {
			for (const elem of obj.tlsCertChain) {
				result.tlsCertChain.add(TLSCertificate.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : OAuth2ClientConnection) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"authServer" : ' + JSON.stringify(obj.authServer) + ',';
		result += '"clientID" : ' + JSON.stringify(obj.clientID) + ',';
		result += '"clientSecret" : ' + JSON.stringify(obj.clientSecret) + ',';
		result += '"tlsCert" : ' + ((obj.tlsCert === null) ? 'null' : JSON.stringify(obj.tlsCert)) + ',';
		result += '"tlsCertIsKnown" : ' + obj.tlsCertIsKnown.toString() + ',';
		result += '"tlsCertIsTrusted" : ' + obj.tlsCertIsTrusted.toString() + ',';
		result += '"tlsCertChain" : [ ';
		for (let i = 0; i < obj.tlsCertChain.size(); i++) {
			const elem = obj.tlsCertChain.get(i);
			result += TLSCertificate.transpilerToJSON(elem);
			if (i < obj.tlsCertChain.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
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
			result += '"authServer" : ' + JSON.stringify(obj.authServer) + ',';
		}
		if (obj.clientID !== undefined) {
			result += '"clientID" : ' + JSON.stringify(obj.clientID) + ',';
		}
		if (obj.clientSecret !== undefined) {
			result += '"clientSecret" : ' + JSON.stringify(obj.clientSecret) + ',';
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
		if (obj.tlsCertChain !== undefined) {
			result += '"tlsCertChain" : [ ';
			for (let i = 0; i < obj.tlsCertChain.size(); i++) {
				const elem = obj.tlsCertChain.get(i);
				result += TLSCertificate.transpilerToJSON(elem);
				if (i < obj.tlsCertChain.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_oauth2_OAuth2ClientConnection(obj : unknown) : OAuth2ClientConnection {
	return obj as OAuth2ClientConnection;
}
