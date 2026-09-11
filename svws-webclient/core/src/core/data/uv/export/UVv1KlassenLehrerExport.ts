import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class UVv1KlassenLehrerExport extends JavaObject {

	/**
	 * Die ID des Lehrers, der der Klasse als Klassenlehrer zugeordnet ist.
	 */
	public idLehrer: number = -1;

	/**
	 * Die Reihenfolge der Zuordnung (z. B. 1 = Klassenleitung, 2 = stellvertretende Klassenleitung).
	 */
	public reihenfolge: number = 1;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.export.UVv1KlassenLehrerExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.export.UVv1KlassenLehrerExport'].includes(name);
	}

	public static readonly class = new Class<UVv1KlassenLehrerExport>('de.svws_nrw.core.data.uv.export.UVv1KlassenLehrerExport');

	public static transpilerFromJSON(json: string): UVv1KlassenLehrerExport {
		const obj = JSON.parse(json) as Partial<UVv1KlassenLehrerExport>;
		const result = new UVv1KlassenLehrerExport();
		if (obj.idLehrer === undefined)
			throw new Error('invalid json format, missing attribute idLehrer');
		result.idLehrer = obj.idLehrer;
		if (obj.reihenfolge === undefined)
			throw new Error('invalid json format, missing attribute reihenfolge');
		result.reihenfolge = obj.reihenfolge;
		return result;
	}

	public static transpilerToJSON(obj: UVv1KlassenLehrerExport): string {
		let result = '{';
		result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		result += '"reihenfolge" : ' + obj.reihenfolge.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UVv1KlassenLehrerExport>): string {
		let result = '{';
		if (obj.idLehrer !== undefined) {
			result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		}
		if (obj.reihenfolge !== undefined) {
			result += '"reihenfolge" : ' + obj.reihenfolge.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_export_UVv1KlassenLehrerExport(obj: unknown): UVv1KlassenLehrerExport {
	return obj as UVv1KlassenLehrerExport;
}
