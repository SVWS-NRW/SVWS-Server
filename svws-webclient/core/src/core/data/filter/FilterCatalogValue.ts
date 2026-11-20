import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class FilterCatalogValue extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags
	 */
	public id: number = 0;

	/**
	 * Die Bezeichnung des Katalog-Eintrags für die Anzeige
	 */
	public bezeichnung: string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.filter.FilterCatalogValue';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.filter.FilterCatalogValue'].includes(name);
	}

	public static readonly class = new Class<FilterCatalogValue>('de.svws_nrw.core.data.filter.FilterCatalogValue');

	public static transpilerFromJSON(json: string): FilterCatalogValue {
		const obj = JSON.parse(json) as Partial<FilterCatalogValue>;
		const result = new FilterCatalogValue();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.bezeichnung = (obj.bezeichnung === undefined) ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		return result;
	}

	public static transpilerToJSON(obj: FilterCatalogValue): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<FilterCatalogValue>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_filter_FilterCatalogValue(obj: unknown): FilterCatalogValue {
	return obj as FilterCatalogValue;
}
