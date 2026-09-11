import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class UVv1SchieneExport extends JavaObject {

	/**
	 * Die UV-ID der Schiene.
	 */
	public uvId: number = -1;

	/**
	 * Die laufende Nummer der Schiene innerhalb des Planungsabschnitts.
	 */
	public nummer: number = 0;

	/**
	 * Die Bezeichnung der Schiene.
	 */
	public bezeichnung: string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.export.UVv1SchieneExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.export.UVv1SchieneExport'].includes(name);
	}

	public static readonly class = new Class<UVv1SchieneExport>('de.svws_nrw.core.data.uv.export.UVv1SchieneExport');

	public static transpilerFromJSON(json: string): UVv1SchieneExport {
		const obj = JSON.parse(json) as Partial<UVv1SchieneExport>;
		const result = new UVv1SchieneExport();
		if (obj.uvId === undefined)
			throw new Error('invalid json format, missing attribute uvId');
		result.uvId = obj.uvId;
		if (obj.nummer === undefined)
			throw new Error('invalid json format, missing attribute nummer');
		result.nummer = obj.nummer;
		result.bezeichnung = (obj.bezeichnung === undefined) ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		return result;
	}

	public static transpilerToJSON(obj: UVv1SchieneExport): string {
		let result = '{';
		result += '"uvId" : ' + obj.uvId.toString() + ',';
		result += '"nummer" : ' + obj.nummer.toString() + ',';
		result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UVv1SchieneExport>): string {
		let result = '{';
		if (obj.uvId !== undefined) {
			result += '"uvId" : ' + obj.uvId.toString() + ',';
		}
		if (obj.nummer !== undefined) {
			result += '"nummer" : ' + obj.nummer.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_export_UVv1SchieneExport(obj: unknown): UVv1SchieneExport {
	return obj as UVv1SchieneExport;
}
