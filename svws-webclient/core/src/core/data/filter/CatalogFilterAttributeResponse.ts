import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class CatalogFilterAttributeResponse extends JavaObject {

	/**
	 * Der Schlüssel des Attributes
	 */
	public key: string | null = null;

	/**
	 * Der Anzeigename des Attributes
	 */
	public label: string | null = null;

	/**
	 * Der Name des Katalogs
	 */
	public catalogName: string | null = null;

	/**
	 * Der Schlüssel der Kategorie
	 */
	public keyCategory: string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.filter.CatalogFilterAttributeResponse';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.filter.CatalogFilterAttributeResponse'].includes(name);
	}

	public static readonly class = new Class<CatalogFilterAttributeResponse>('de.svws_nrw.core.data.filter.CatalogFilterAttributeResponse');

	public static transpilerFromJSON(json: string): CatalogFilterAttributeResponse {
		const obj = JSON.parse(json) as Partial<CatalogFilterAttributeResponse>;
		const result = new CatalogFilterAttributeResponse();
		result.key = (obj.key === undefined) ? null : obj.key === null ? null : obj.key;
		result.label = (obj.label === undefined) ? null : obj.label === null ? null : obj.label;
		result.catalogName = (obj.catalogName === undefined) ? null : obj.catalogName === null ? null : obj.catalogName;
		result.keyCategory = (obj.keyCategory === undefined) ? null : obj.keyCategory === null ? null : obj.keyCategory;
		return result;
	}

	public static transpilerToJSON(obj: CatalogFilterAttributeResponse): string {
		let result = '{';
		result += '"key" : ' + ((obj.key === null) ? 'null' : JSON.stringify(obj.key)) + ',';
		result += '"label" : ' + ((obj.label === null) ? 'null' : JSON.stringify(obj.label)) + ',';
		result += '"catalogName" : ' + ((obj.catalogName === null) ? 'null' : JSON.stringify(obj.catalogName)) + ',';
		result += '"keyCategory" : ' + ((obj.keyCategory === null) ? 'null' : JSON.stringify(obj.keyCategory)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<CatalogFilterAttributeResponse>): string {
		let result = '{';
		if (obj.key !== undefined) {
			result += '"key" : ' + ((obj.key === null) ? 'null' : JSON.stringify(obj.key)) + ',';
		}
		if (obj.label !== undefined) {
			result += '"label" : ' + ((obj.label === null) ? 'null' : JSON.stringify(obj.label)) + ',';
		}
		if (obj.catalogName !== undefined) {
			result += '"catalogName" : ' + ((obj.catalogName === null) ? 'null' : JSON.stringify(obj.catalogName)) + ',';
		}
		if (obj.keyCategory !== undefined) {
			result += '"keyCategory" : ' + ((obj.keyCategory === null) ? 'null' : JSON.stringify(obj.keyCategory)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_filter_CatalogFilterAttributeResponse(obj: unknown): CatalogFilterAttributeResponse {
	return obj as CatalogFilterAttributeResponse;
}
