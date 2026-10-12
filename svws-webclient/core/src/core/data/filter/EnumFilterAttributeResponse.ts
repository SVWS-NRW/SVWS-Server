import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class EnumFilterAttributeResponse extends JavaObject {

	/**
	 * Der Schlüssel des Attributes
	 */
	public key: string | null = null;

	/**
	 * Der Anzeigename des Attributes
	 */
	public label: string | null = null;

	/**
	 * Der Name des Enums
	 */
	public enumName: string | null = null;

	/**
	 * Der Schlüssel der Kategorie
	 */
	public keyCategory: string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.filter.EnumFilterAttributeResponse';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.filter.EnumFilterAttributeResponse'].includes(name);
	}

	public static readonly class = new Class<EnumFilterAttributeResponse>('de.svws_nrw.core.data.filter.EnumFilterAttributeResponse');

	public static transpilerFromJSON(json: string): EnumFilterAttributeResponse {
		const obj = JSON.parse(json) as Partial<EnumFilterAttributeResponse>;
		const result = new EnumFilterAttributeResponse();
		result.key = (obj.key === undefined) ? null : obj.key === null ? null : obj.key;
		result.label = (obj.label === undefined) ? null : obj.label === null ? null : obj.label;
		result.enumName = (obj.enumName === undefined) ? null : obj.enumName === null ? null : obj.enumName;
		result.keyCategory = (obj.keyCategory === undefined) ? null : obj.keyCategory === null ? null : obj.keyCategory;
		return result;
	}

	public static transpilerToJSON(obj: EnumFilterAttributeResponse): string {
		let result = '{';
		result += '"key" : ' + ((obj.key === null) ? 'null' : JSON.stringify(obj.key)) + ',';
		result += '"label" : ' + ((obj.label === null) ? 'null' : JSON.stringify(obj.label)) + ',';
		result += '"enumName" : ' + ((obj.enumName === null) ? 'null' : JSON.stringify(obj.enumName)) + ',';
		result += '"keyCategory" : ' + ((obj.keyCategory === null) ? 'null' : JSON.stringify(obj.keyCategory)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<EnumFilterAttributeResponse>): string {
		let result = '{';
		if (obj.key !== undefined) {
			result += '"key" : ' + ((obj.key === null) ? 'null' : JSON.stringify(obj.key)) + ',';
		}
		if (obj.label !== undefined) {
			result += '"label" : ' + ((obj.label === null) ? 'null' : JSON.stringify(obj.label)) + ',';
		}
		if (obj.enumName !== undefined) {
			result += '"enumName" : ' + ((obj.enumName === null) ? 'null' : JSON.stringify(obj.enumName)) + ',';
		}
		if (obj.keyCategory !== undefined) {
			result += '"keyCategory" : ' + ((obj.keyCategory === null) ? 'null' : JSON.stringify(obj.keyCategory)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_filter_EnumFilterAttributeResponse(obj: unknown): EnumFilterAttributeResponse {
	return obj as EnumFilterAttributeResponse;
}
