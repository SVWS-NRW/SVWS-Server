import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class CoreTypeFilterAttributeResponse extends JavaObject {

	/**
	 * Der Schlüssel des Attributes
	 */
	public key: string | null = null;

	/**
	 * Der Anzeigename des Attributes
	 */
	public label: string | null = null;

	/**
	 * Der Name des CoreTypes
	 */
	public coreTypeName: string | null = null;

	/**
	 * Der Schlüssel der Kategorie
	 */
	public keyCategory: string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.filter.CoreTypeFilterAttributeResponse';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.filter.CoreTypeFilterAttributeResponse'].includes(name);
	}

	public static readonly class = new Class<CoreTypeFilterAttributeResponse>('de.svws_nrw.core.data.filter.CoreTypeFilterAttributeResponse');

	public static transpilerFromJSON(json: string): CoreTypeFilterAttributeResponse {
		const obj = JSON.parse(json) as Partial<CoreTypeFilterAttributeResponse>;
		const result = new CoreTypeFilterAttributeResponse();
		result.key = (obj.key === undefined) ? null : obj.key === null ? null : obj.key;
		result.label = (obj.label === undefined) ? null : obj.label === null ? null : obj.label;
		result.coreTypeName = (obj.coreTypeName === undefined) ? null : obj.coreTypeName === null ? null : obj.coreTypeName;
		result.keyCategory = (obj.keyCategory === undefined) ? null : obj.keyCategory === null ? null : obj.keyCategory;
		return result;
	}

	public static transpilerToJSON(obj: CoreTypeFilterAttributeResponse): string {
		let result = '{';
		result += '"key" : ' + ((obj.key === null) ? 'null' : JSON.stringify(obj.key)) + ',';
		result += '"label" : ' + ((obj.label === null) ? 'null' : JSON.stringify(obj.label)) + ',';
		result += '"coreTypeName" : ' + ((obj.coreTypeName === null) ? 'null' : JSON.stringify(obj.coreTypeName)) + ',';
		result += '"keyCategory" : ' + ((obj.keyCategory === null) ? 'null' : JSON.stringify(obj.keyCategory)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<CoreTypeFilterAttributeResponse>): string {
		let result = '{';
		if (obj.key !== undefined) {
			result += '"key" : ' + ((obj.key === null) ? 'null' : JSON.stringify(obj.key)) + ',';
		}
		if (obj.label !== undefined) {
			result += '"label" : ' + ((obj.label === null) ? 'null' : JSON.stringify(obj.label)) + ',';
		}
		if (obj.coreTypeName !== undefined) {
			result += '"coreTypeName" : ' + ((obj.coreTypeName === null) ? 'null' : JSON.stringify(obj.coreTypeName)) + ',';
		}
		if (obj.keyCategory !== undefined) {
			result += '"keyCategory" : ' + ((obj.keyCategory === null) ? 'null' : JSON.stringify(obj.keyCategory)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_filter_CoreTypeFilterAttributeResponse(obj: unknown): CoreTypeFilterAttributeResponse {
	return obj as CoreTypeFilterAttributeResponse;
}
