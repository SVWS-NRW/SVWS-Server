import { JavaObject } from '../../java/lang/JavaObject';
import { Class } from '../../java/lang/Class';

export class TLSCertificate extends JavaObject {

	/**
	 * Die Version der Zertifikats (0 -> V1, 1 -> V2, 2 -> V3).
	 */
	public version : number = 0;

	/**
	 * Der Typ des Zertifikats
	 */
	public type : string = "";

	/**
	 * Die Informationen zum Inhaber des Zertifikats
	 */
	public subject : string | null = null;

	/**
	 * Die Informationen zum Aussteller des Zertifikats
	 */
	public issuer : string | null = null;

	/**
	 * Die Seriennummer des Zertifikats
	 */
	public serialNumber : string | null = null;

	/**
	 * Der Signaturalgorithmus
	 */
	public signatureAlgorithm : string | null = null;

	/**
	 * Die OID des Signaturalgorithmus
	 */
	public signatureAlgorithmOID : string | null = null;

	/**
	 * Ggf. weitere Parameter für den Signaturalgorithmus
	 */
	public params : string | null = null;

	/**
	 * Die Signatur (Base64-kodiert)
	 */
	public signature : string = "";

	/**
	 * Der Algorithmus, der für den öffentlichen Schlüssel verwendet wurde
	 */
	public keyAlgorithm : string | null = null;

	/**
	 * Das Format, in welchem der öffentliche Schlüssel vorliegt
	 */
	public keyFormat : string | null = null;

	/**
	 * Der öffentliche Schlüssel (Base64-kodiert)
	 */
	public key : string = "";

	/**
	 * Das Datum, ab dem das Zertifkat gültig ist (ISO 8601 format).
	 */
	public validSince : string | null = null;

	/**
	 * Das Datum, bis zu welchem Tag das Zertifkat gültig ist (ISO 8601 format).
	 */
	public validUntil : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.TLSCertificate';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.TLSCertificate'].includes(name);
	}

	public static class = new Class<TLSCertificate>('de.svws_nrw.core.data.TLSCertificate');

	public static transpilerFromJSON(json : string): TLSCertificate {
		const obj = JSON.parse(json) as Partial<TLSCertificate>;
		const result = new TLSCertificate();
		if (obj.version === undefined)
			throw new Error('invalid json format, missing attribute version');
		result.version = obj.version;
		if (obj.type === undefined)
			throw new Error('invalid json format, missing attribute type');
		result.type = obj.type;
		result.subject = (obj.subject === undefined) ? null : obj.subject === null ? null : obj.subject;
		result.issuer = (obj.issuer === undefined) ? null : obj.issuer === null ? null : obj.issuer;
		result.serialNumber = (obj.serialNumber === undefined) ? null : obj.serialNumber === null ? null : obj.serialNumber;
		result.signatureAlgorithm = (obj.signatureAlgorithm === undefined) ? null : obj.signatureAlgorithm === null ? null : obj.signatureAlgorithm;
		result.signatureAlgorithmOID = (obj.signatureAlgorithmOID === undefined) ? null : obj.signatureAlgorithmOID === null ? null : obj.signatureAlgorithmOID;
		result.params = (obj.params === undefined) ? null : obj.params === null ? null : obj.params;
		if (obj.signature === undefined)
			throw new Error('invalid json format, missing attribute signature');
		result.signature = obj.signature;
		result.keyAlgorithm = (obj.keyAlgorithm === undefined) ? null : obj.keyAlgorithm === null ? null : obj.keyAlgorithm;
		result.keyFormat = (obj.keyFormat === undefined) ? null : obj.keyFormat === null ? null : obj.keyFormat;
		if (obj.key === undefined)
			throw new Error('invalid json format, missing attribute key');
		result.key = obj.key;
		result.validSince = (obj.validSince === undefined) ? null : obj.validSince === null ? null : obj.validSince;
		result.validUntil = (obj.validUntil === undefined) ? null : obj.validUntil === null ? null : obj.validUntil;
		return result;
	}

	public static transpilerToJSON(obj : TLSCertificate) : string {
		let result = '{';
		result += '"version" : ' + obj.version.toString() + ',';
		result += '"type" : ' + JSON.stringify(obj.type) + ',';
		result += '"subject" : ' + ((obj.subject === null) ? 'null' : JSON.stringify(obj.subject)) + ',';
		result += '"issuer" : ' + ((obj.issuer === null) ? 'null' : JSON.stringify(obj.issuer)) + ',';
		result += '"serialNumber" : ' + ((obj.serialNumber === null) ? 'null' : JSON.stringify(obj.serialNumber)) + ',';
		result += '"signatureAlgorithm" : ' + ((obj.signatureAlgorithm === null) ? 'null' : JSON.stringify(obj.signatureAlgorithm)) + ',';
		result += '"signatureAlgorithmOID" : ' + ((obj.signatureAlgorithmOID === null) ? 'null' : JSON.stringify(obj.signatureAlgorithmOID)) + ',';
		result += '"params" : ' + ((obj.params === null) ? 'null' : JSON.stringify(obj.params)) + ',';
		result += '"signature" : ' + JSON.stringify(obj.signature) + ',';
		result += '"keyAlgorithm" : ' + ((obj.keyAlgorithm === null) ? 'null' : JSON.stringify(obj.keyAlgorithm)) + ',';
		result += '"keyFormat" : ' + ((obj.keyFormat === null) ? 'null' : JSON.stringify(obj.keyFormat)) + ',';
		result += '"key" : ' + JSON.stringify(obj.key) + ',';
		result += '"validSince" : ' + ((obj.validSince === null) ? 'null' : JSON.stringify(obj.validSince)) + ',';
		result += '"validUntil" : ' + ((obj.validUntil === null) ? 'null' : JSON.stringify(obj.validUntil)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<TLSCertificate>) : string {
		let result = '{';
		if (obj.version !== undefined) {
			result += '"version" : ' + obj.version.toString() + ',';
		}
		if (obj.type !== undefined) {
			result += '"type" : ' + JSON.stringify(obj.type) + ',';
		}
		if (obj.subject !== undefined) {
			result += '"subject" : ' + ((obj.subject === null) ? 'null' : JSON.stringify(obj.subject)) + ',';
		}
		if (obj.issuer !== undefined) {
			result += '"issuer" : ' + ((obj.issuer === null) ? 'null' : JSON.stringify(obj.issuer)) + ',';
		}
		if (obj.serialNumber !== undefined) {
			result += '"serialNumber" : ' + ((obj.serialNumber === null) ? 'null' : JSON.stringify(obj.serialNumber)) + ',';
		}
		if (obj.signatureAlgorithm !== undefined) {
			result += '"signatureAlgorithm" : ' + ((obj.signatureAlgorithm === null) ? 'null' : JSON.stringify(obj.signatureAlgorithm)) + ',';
		}
		if (obj.signatureAlgorithmOID !== undefined) {
			result += '"signatureAlgorithmOID" : ' + ((obj.signatureAlgorithmOID === null) ? 'null' : JSON.stringify(obj.signatureAlgorithmOID)) + ',';
		}
		if (obj.params !== undefined) {
			result += '"params" : ' + ((obj.params === null) ? 'null' : JSON.stringify(obj.params)) + ',';
		}
		if (obj.signature !== undefined) {
			result += '"signature" : ' + JSON.stringify(obj.signature) + ',';
		}
		if (obj.keyAlgorithm !== undefined) {
			result += '"keyAlgorithm" : ' + ((obj.keyAlgorithm === null) ? 'null' : JSON.stringify(obj.keyAlgorithm)) + ',';
		}
		if (obj.keyFormat !== undefined) {
			result += '"keyFormat" : ' + ((obj.keyFormat === null) ? 'null' : JSON.stringify(obj.keyFormat)) + ',';
		}
		if (obj.key !== undefined) {
			result += '"key" : ' + JSON.stringify(obj.key) + ',';
		}
		if (obj.validSince !== undefined) {
			result += '"validSince" : ' + ((obj.validSince === null) ? 'null' : JSON.stringify(obj.validSince)) + ',';
		}
		if (obj.validUntil !== undefined) {
			result += '"validUntil" : ' + ((obj.validUntil === null) ? 'null' : JSON.stringify(obj.validUntil)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_TLSCertificate(obj : unknown) : TLSCertificate {
	return obj as TLSCertificate;
}
