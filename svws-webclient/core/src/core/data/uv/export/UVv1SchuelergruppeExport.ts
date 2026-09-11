import { JavaObject } from '../../../../java/lang/JavaObject';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

export class UVv1SchuelergruppeExport extends JavaObject {

	/**
	 * Die UV-ID der Schülergruppe.
	 */
	public uvId: number = -1;

	/**
	 * Die Bezeichnung der Schülergruppe.
	 */
	public bezeichnung: string = "";

	/**
	 * Ein Array mit den IDs der Schüler der Gruppe.
	 */
	public schuelerIds: List<number> = new ArrayList<number>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.export.UVv1SchuelergruppeExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.export.UVv1SchuelergruppeExport'].includes(name);
	}

	public static readonly class = new Class<UVv1SchuelergruppeExport>('de.svws_nrw.core.data.uv.export.UVv1SchuelergruppeExport');

	public static transpilerFromJSON(json: string): UVv1SchuelergruppeExport {
		const obj = JSON.parse(json) as Partial<UVv1SchuelergruppeExport>;
		const result = new UVv1SchuelergruppeExport();
		if (obj.uvId === undefined)
			throw new Error('invalid json format, missing attribute uvId');
		result.uvId = obj.uvId;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.schuelerIds !== undefined) {
			for (const elem of obj.schuelerIds) {
				result.schuelerIds.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: UVv1SchuelergruppeExport): string {
		let result = '{';
		result += '"uvId" : ' + obj.uvId.toString() + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"schuelerIds" : [ ';
		for (let i = 0; i < obj.schuelerIds.size(); i++) {
			const elem = obj.schuelerIds.get(i);
			result += elem.toString();
			if (i < obj.schuelerIds.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UVv1SchuelergruppeExport>): string {
		let result = '{';
		if (obj.uvId !== undefined) {
			result += '"uvId" : ' + obj.uvId.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.schuelerIds !== undefined) {
			result += '"schuelerIds" : [ ';
			for (let i = 0; i < obj.schuelerIds.size(); i++) {
				const elem = obj.schuelerIds.get(i);
				result += elem.toString();
				if (i < obj.schuelerIds.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_export_UVv1SchuelergruppeExport(obj: unknown): UVv1SchuelergruppeExport {
	return obj as UVv1SchuelergruppeExport;
}
