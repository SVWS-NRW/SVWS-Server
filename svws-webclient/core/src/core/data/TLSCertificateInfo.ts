import { JavaObject } from '../../java/lang/JavaObject';
import { ArrayList } from '../../java/util/ArrayList';
import type { List } from '../../java/util/List';
import { Class } from '../../java/lang/Class';

export class TLSCertificateInfo extends JavaObject {

	/**
	 * Der Distinguished Name (DN)
	 */
	public dn: string = "";

	/**
	 * Die Subject Alternative Name (SAN)-Einträge (DNS oder IP)
	 */
	public sans: List<string> = new ArrayList<string>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.TLSCertificateInfo';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.TLSCertificateInfo'].includes(name);
	}

	public static readonly class = new Class<TLSCertificateInfo>('de.svws_nrw.core.data.TLSCertificateInfo');

	public static transpilerFromJSON(json: string): TLSCertificateInfo {
		const obj = JSON.parse(json) as Partial<TLSCertificateInfo>;
		const result = new TLSCertificateInfo();
		if (obj.dn === undefined)
			throw new Error('invalid json format, missing attribute dn');
		result.dn = obj.dn;
		if (obj.sans !== undefined) {
			for (const elem of obj.sans) {
				result.sans.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: TLSCertificateInfo): string {
		let result = '{';
		result += '"dn" : ' + JSON.stringify(obj.dn) + ',';
		result += '"sans" : [ ';
		for (let i = 0; i < obj.sans.size(); i++) {
			const elem = obj.sans.get(i);
			result += '"' + elem + '"';
			if (i < obj.sans.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<TLSCertificateInfo>): string {
		let result = '{';
		if (obj.dn !== undefined) {
			result += '"dn" : ' + JSON.stringify(obj.dn) + ',';
		}
		if (obj.sans !== undefined) {
			result += '"sans" : [ ';
			for (let i = 0; i < obj.sans.size(); i++) {
				const elem = obj.sans.get(i);
				result += '"' + elem + '"';
				if (i < obj.sans.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_TLSCertificateInfo(obj: unknown): TLSCertificateInfo {
	return obj as TLSCertificateInfo;
}
