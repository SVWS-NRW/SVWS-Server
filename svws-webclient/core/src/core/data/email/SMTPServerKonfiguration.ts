import { JavaObject } from '../../../java/lang/JavaObject';

export class SMTPServerKonfiguration extends JavaObject {

	/**
	 * Die ID der Konfiguration.
	 */
	public id : number = -1;

	/**
	 * Der Hostname des SMTP-Servers.
	 */
	public host : string = "";

	/**
	 * Die Port-Adresse des SMTP-Servers.
	 */
	public port : number = 25;

	/**
	 * Gibt an, ob StartTLS für die SMTP-Verbindung genutzt wird oder nicht.
	 */
	public useStartTLS : boolean = true;

	/**
	 * Gibt an, ob TLS für die SMTP-Verbindung genutzt wird oder nicht. Wird dies genutzt, so wird entweder ein Zertifikat im Key-Store des Servers benötigt oder es muss einem Host vertraut werden (siehe trustTLSHost).
	 */
	public useTLS : boolean = false;

	/**
	 * Gibt an, falls nicht null, welchem Host - unabhängig von vorhandenen Zertifikaten - vertraut werden kann, '*' für jeden beliebigen Host.
	 */
	public trustTLSHost : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.email.SMTPServerKonfiguration';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.email.SMTPServerKonfiguration'].includes(name);
	}

	public static transpilerFromJSON(json : string): SMTPServerKonfiguration {
		const obj = JSON.parse(json);
		const result = new SMTPServerKonfiguration();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.host === "undefined")
			 throw new Error('invalid json format, missing attribute host');
		result.host = obj.host;
		if (typeof obj.port === "undefined")
			 throw new Error('invalid json format, missing attribute port');
		result.port = obj.port;
		if (typeof obj.useStartTLS === "undefined")
			 throw new Error('invalid json format, missing attribute useStartTLS');
		result.useStartTLS = obj.useStartTLS;
		if (typeof obj.useTLS === "undefined")
			 throw new Error('invalid json format, missing attribute useTLS');
		result.useTLS = obj.useTLS;
		result.trustTLSHost = typeof obj.trustTLSHost === "undefined" ? null : obj.trustTLSHost === null ? null : obj.trustTLSHost;
		return result;
	}

	public static transpilerToJSON(obj : SMTPServerKonfiguration) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"host" : ' + JSON.stringify(obj.host!) + ',';
		result += '"port" : ' + obj.port + ',';
		result += '"useStartTLS" : ' + obj.useStartTLS + ',';
		result += '"useTLS" : ' + obj.useTLS + ',';
		result += '"trustTLSHost" : ' + ((!obj.trustTLSHost) ? 'null' : JSON.stringify(obj.trustTLSHost)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SMTPServerKonfiguration>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.host !== "undefined") {
			result += '"host" : ' + JSON.stringify(obj.host!) + ',';
		}
		if (typeof obj.port !== "undefined") {
			result += '"port" : ' + obj.port + ',';
		}
		if (typeof obj.useStartTLS !== "undefined") {
			result += '"useStartTLS" : ' + obj.useStartTLS + ',';
		}
		if (typeof obj.useTLS !== "undefined") {
			result += '"useTLS" : ' + obj.useTLS + ',';
		}
		if (typeof obj.trustTLSHost !== "undefined") {
			result += '"trustTLSHost" : ' + ((!obj.trustTLSHost) ? 'null' : JSON.stringify(obj.trustTLSHost)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_email_SMTPServerKonfiguration(obj : unknown) : SMTPServerKonfiguration {
	return obj as SMTPServerKonfiguration;
}
