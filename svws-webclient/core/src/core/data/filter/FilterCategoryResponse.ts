import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class FilterCategoryResponse extends JavaObject {

	/**
	 * Der Schlüssel der Kategorie
	 */
	public key: string | null = null;

	/**
	 * Der Anzeigename der Kategorie
	 */
	public label: string | null = null;

	/**
	 * Die Sortierreihenfolge der Kategorie
	 */
	public order: number | null = null;

	/**
	 * Liste mit den Schlüsseln der zugeordneten Attribute
	 */
	public attributes: List<string> | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.filter.FilterCategoryResponse';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.filter.FilterCategoryResponse'].includes(name);
	}

	public static readonly class = new Class<FilterCategoryResponse>('de.svws_nrw.core.data.filter.FilterCategoryResponse');

	public static transpilerFromJSON(json: string): FilterCategoryResponse {
		const obj = JSON.parse(json) as Partial<FilterCategoryResponse>;
		const result = new FilterCategoryResponse();
		result.key = (obj.key === undefined) ? null : obj.key === null ? null : obj.key;
		result.label = (obj.label === undefined) ? null : obj.label === null ? null : obj.label;
		result.order = (obj.order === undefined) ? null : obj.order === null ? null : obj.order;
		if ((obj.attributes !== undefined) && (obj.attributes !== null)) {
			result.attributes = new ArrayList();
			for (const elem of obj.attributes) {
				result.attributes.add(elem);
			}
		} else {
			result.attributes = null;
		}
		return result;
	}

	public static transpilerToJSON(obj: FilterCategoryResponse): string {
		let result = '{';
		result += '"key" : ' + ((obj.key === null) ? 'null' : JSON.stringify(obj.key)) + ',';
		result += '"label" : ' + ((obj.label === null) ? 'null' : JSON.stringify(obj.label)) + ',';
		result += '"order" : ' + ((obj.order === null) ? 'null' : obj.order.toString()) + ',';
		if (!obj.attributes) {
			result += '"attributes" : null' + ',';
		} else {
			result += '"attributes" : [ ';
			for (let i = 0; i < obj.attributes.size(); i++) {
				const elem = obj.attributes.get(i);
				result += '"' + elem + '"';
				if (i < obj.attributes.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<FilterCategoryResponse>): string {
		let result = '{';
		if (obj.key !== undefined) {
			result += '"key" : ' + ((obj.key === null) ? 'null' : JSON.stringify(obj.key)) + ',';
		}
		if (obj.label !== undefined) {
			result += '"label" : ' + ((obj.label === null) ? 'null' : JSON.stringify(obj.label)) + ',';
		}
		if (obj.order !== undefined) {
			result += '"order" : ' + ((obj.order === null) ? 'null' : obj.order.toString()) + ',';
		}
		if (obj.attributes !== undefined) {
			if (!obj.attributes) {
				result += '"attributes" : null' + ',';
			} else {
				result += '"attributes" : [ ';
				for (let i = 0; i < obj.attributes.size(); i++) {
					const elem = obj.attributes.get(i);
					result += '"' + elem + '"';
					if (i < obj.attributes.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_filter_FilterCategoryResponse(obj: unknown): FilterCategoryResponse {
	return obj as FilterCategoryResponse;
}
