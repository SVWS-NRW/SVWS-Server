import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class FilterAttributeResponse extends JavaObject {

	/**
	 * Der Schlüssel des Attributes
	 */
	public key: string | null = null;

	/**
	 * Der Anzeigename des Attributes
	 */
	public label: string | null = null;

	/**
	 * Der Schlüssel der Kategorie
	 */
	public keyCategory: string | null = null;

	/**
	 * Der Typ des Attributes
	 */
	public type: string | null = null;

	/**
	 * Der Name des Enums, CoreTypes oder Katalogs, aus dem die Werte stammen
	 */
	public source: string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.filter.FilterAttributeResponse';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.filter.FilterAttributeResponse'].includes(name);
	}

	public static readonly class = new Class<FilterAttributeResponse>('de.svws_nrw.core.data.filter.FilterAttributeResponse');

	public static transpilerFromJSON(json: string): FilterAttributeResponse {
		const obj = JSON.parse(json) as Partial<FilterAttributeResponse>;
		const result = new FilterAttributeResponse();
		result.key = (obj.key === undefined) ? null : obj.key === null ? null : obj.key;
		result.label = (obj.label === undefined) ? null : obj.label === null ? null : obj.label;
		result.keyCategory = (obj.keyCategory === undefined) ? null : obj.keyCategory === null ? null : obj.keyCategory;
		result.type = (obj.type === undefined) ? null : obj.type === null ? null : obj.type;
		result.source = (obj.source === undefined) ? null : obj.source === null ? null : obj.source;
		return result;
	}

	public static transpilerToJSON(obj: FilterAttributeResponse): string {
		let result = '{';
		result += '"key" : ' + ((obj.key === null) ? 'null' : JSON.stringify(obj.key)) + ',';
		result += '"label" : ' + ((obj.label === null) ? 'null' : JSON.stringify(obj.label)) + ',';
		result += '"keyCategory" : ' + ((obj.keyCategory === null) ? 'null' : JSON.stringify(obj.keyCategory)) + ',';
		result += '"type" : ' + ((obj.type === null) ? 'null' : JSON.stringify(obj.type)) + ',';
		result += '"source" : ' + ((obj.source === null) ? 'null' : JSON.stringify(obj.source)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<FilterAttributeResponse>): string {
		let result = '{';
		if (obj.key !== undefined) {
			result += '"key" : ' + ((obj.key === null) ? 'null' : JSON.stringify(obj.key)) + ',';
		}
		if (obj.label !== undefined) {
			result += '"label" : ' + ((obj.label === null) ? 'null' : JSON.stringify(obj.label)) + ',';
		}
		if (obj.keyCategory !== undefined) {
			result += '"keyCategory" : ' + ((obj.keyCategory === null) ? 'null' : JSON.stringify(obj.keyCategory)) + ',';
		}
		if (obj.type !== undefined) {
			result += '"type" : ' + ((obj.type === null) ? 'null' : JSON.stringify(obj.type)) + ',';
		}
		if (obj.source !== undefined) {
			result += '"source" : ' + ((obj.source === null) ? 'null' : JSON.stringify(obj.source)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_filter_FilterAttributeResponse(obj: unknown): FilterAttributeResponse {
	return obj as FilterAttributeResponse;
}
