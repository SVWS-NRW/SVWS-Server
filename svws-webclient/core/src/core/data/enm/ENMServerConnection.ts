import { JavaObject } from '../../../java/lang/JavaObject';
import { TLSCertificate } from '../../../core/data/TLSCertificate';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class ENMServerConnection extends JavaObject {

	/**
	 * Die ID des OAuth2 Client Secrets.
	 */
	public id: number = -1;

	/**
	 * Die Bezeichnung des Servers
	 */
	public bezeichnung: string | null = "";

	/**
	 * Die URL des Auth-Servers.
	 */
	public url: string = "";

	/**
	 * Die Client-ID für diesen Auth-Server.
	 */
	public clientID: string = "";

	/**
	 * Das Client-Secret für die Client ID für diesen Auth-Server.
	 */
	public clientSecret: string = "";

	/**
	 * Das TLS-Zertifikat, welches von dem Auth-Server verwendet wird.
	 */
	public serverTLSCert: string | null = null;

	/**
	 * Gibt an, ob das TLS-Zertifikat von dem SVWS-Server über die Chain automatisch validiert werden kann.
	 */
	public serverTLSCertIsKnown: boolean = false;

	/**
	 * Gibt an, ob dem TLS-Zertifikat von dem SVWS-Server vertraut wird oder nicht.
	 */
	public serverTLSCertIsTrusted: boolean = false;

	/**
	 * Die Liste mit den TLS-Zertifikaten der Zertifikatskette des TLS-Servers.
	 */
	public serverTLSCertChain: List<TLSCertificate> = new ArrayList<TLSCertificate>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMServerConnection';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMServerConnection'].includes(name);
	}

	public static readonly class = new Class<ENMServerConnection>('de.svws_nrw.core.data.enm.ENMServerConnection');

	public static transpilerFromJSON(json: string): ENMServerConnection {
		const obj = JSON.parse(json) as Partial<ENMServerConnection>;
		const result = new ENMServerConnection();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.bezeichnung = (obj.bezeichnung === undefined) ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		if (obj.url === undefined)
			throw new Error('invalid json format, missing attribute url');
		result.url = obj.url;
		if (obj.clientID === undefined)
			throw new Error('invalid json format, missing attribute clientID');
		result.clientID = obj.clientID;
		if (obj.clientSecret === undefined)
			throw new Error('invalid json format, missing attribute clientSecret');
		result.clientSecret = obj.clientSecret;
		result.serverTLSCert = (obj.serverTLSCert === undefined) ? null : obj.serverTLSCert === null ? null : obj.serverTLSCert;
		if (obj.serverTLSCertIsKnown === undefined)
			throw new Error('invalid json format, missing attribute serverTLSCertIsKnown');
		result.serverTLSCertIsKnown = obj.serverTLSCertIsKnown;
		if (obj.serverTLSCertIsTrusted === undefined)
			throw new Error('invalid json format, missing attribute serverTLSCertIsTrusted');
		result.serverTLSCertIsTrusted = obj.serverTLSCertIsTrusted;
		if (obj.serverTLSCertChain !== undefined) {
			for (const elem of obj.serverTLSCertChain) {
				result.serverTLSCertChain.add(TLSCertificate.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: ENMServerConnection): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result += '"url" : ' + JSON.stringify(obj.url) + ',';
		result += '"clientID" : ' + JSON.stringify(obj.clientID) + ',';
		result += '"clientSecret" : ' + JSON.stringify(obj.clientSecret) + ',';
		result += '"serverTLSCert" : ' + ((obj.serverTLSCert === null) ? 'null' : JSON.stringify(obj.serverTLSCert)) + ',';
		result += '"serverTLSCertIsKnown" : ' + obj.serverTLSCertIsKnown.toString() + ',';
		result += '"serverTLSCertIsTrusted" : ' + obj.serverTLSCertIsTrusted.toString() + ',';
		result += '"serverTLSCertChain" : [ ';
		for (let i = 0; i < obj.serverTLSCertChain.size(); i++) {
			const elem = obj.serverTLSCertChain.get(i);
			result += TLSCertificate.transpilerToJSON(elem);
			if (i < obj.serverTLSCertChain.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ENMServerConnection>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		if (obj.url !== undefined) {
			result += '"url" : ' + JSON.stringify(obj.url) + ',';
		}
		if (obj.clientID !== undefined) {
			result += '"clientID" : ' + JSON.stringify(obj.clientID) + ',';
		}
		if (obj.clientSecret !== undefined) {
			result += '"clientSecret" : ' + JSON.stringify(obj.clientSecret) + ',';
		}
		if (obj.serverTLSCert !== undefined) {
			result += '"serverTLSCert" : ' + ((obj.serverTLSCert === null) ? 'null' : JSON.stringify(obj.serverTLSCert)) + ',';
		}
		if (obj.serverTLSCertIsKnown !== undefined) {
			result += '"serverTLSCertIsKnown" : ' + obj.serverTLSCertIsKnown.toString() + ',';
		}
		if (obj.serverTLSCertIsTrusted !== undefined) {
			result += '"serverTLSCertIsTrusted" : ' + obj.serverTLSCertIsTrusted.toString() + ',';
		}
		if (obj.serverTLSCertChain !== undefined) {
			result += '"serverTLSCertChain" : [ ';
			for (let i = 0; i < obj.serverTLSCertChain.size(); i++) {
				const elem = obj.serverTLSCertChain.get(i);
				result += TLSCertificate.transpilerToJSON(elem);
				if (i < obj.serverTLSCertChain.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMServerConnection(obj: unknown): ENMServerConnection {
	return obj as ENMServerConnection;
}
