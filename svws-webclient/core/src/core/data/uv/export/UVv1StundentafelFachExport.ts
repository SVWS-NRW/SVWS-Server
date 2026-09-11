import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class UVv1StundentafelFachExport extends JavaObject {

	/**
	 * Der Abschnitt des Schuljahres (z. B. 1 oder 2).
	 */
	public abschnitt: number = 1;

	/**
	 * Die UV-ID des Faches.
	 */
	public fachUvId: number = -1;

	/**
	 * Die Anzahl der Wochenstunden für das Fach.
	 */
	public wochenstunden: number = 0.0;

	/**
	 * Die Anzahl der Ergänzungsstunden für das Fach (in den Wochenstunden enthalten).
	 */
	public davonErgaenzungsstunden: number = 0.0;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.export.UVv1StundentafelFachExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.export.UVv1StundentafelFachExport'].includes(name);
	}

	public static readonly class = new Class<UVv1StundentafelFachExport>('de.svws_nrw.core.data.uv.export.UVv1StundentafelFachExport');

	public static transpilerFromJSON(json: string): UVv1StundentafelFachExport {
		const obj = JSON.parse(json) as Partial<UVv1StundentafelFachExport>;
		const result = new UVv1StundentafelFachExport();
		if (obj.abschnitt === undefined)
			throw new Error('invalid json format, missing attribute abschnitt');
		result.abschnitt = obj.abschnitt;
		if (obj.fachUvId === undefined)
			throw new Error('invalid json format, missing attribute fachUvId');
		result.fachUvId = obj.fachUvId;
		if (obj.wochenstunden === undefined)
			throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		if (obj.davonErgaenzungsstunden === undefined)
			throw new Error('invalid json format, missing attribute davonErgaenzungsstunden');
		result.davonErgaenzungsstunden = obj.davonErgaenzungsstunden;
		return result;
	}

	public static transpilerToJSON(obj: UVv1StundentafelFachExport): string {
		let result = '{';
		result += '"abschnitt" : ' + obj.abschnitt.toString() + ',';
		result += '"fachUvId" : ' + obj.fachUvId.toString() + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		result += '"davonErgaenzungsstunden" : ' + obj.davonErgaenzungsstunden.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UVv1StundentafelFachExport>): string {
		let result = '{';
		if (obj.abschnitt !== undefined) {
			result += '"abschnitt" : ' + obj.abschnitt.toString() + ',';
		}
		if (obj.fachUvId !== undefined) {
			result += '"fachUvId" : ' + obj.fachUvId.toString() + ',';
		}
		if (obj.wochenstunden !== undefined) {
			result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		}
		if (obj.davonErgaenzungsstunden !== undefined) {
			result += '"davonErgaenzungsstunden" : ' + obj.davonErgaenzungsstunden.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_export_UVv1StundentafelFachExport(obj: unknown): UVv1StundentafelFachExport {
	return obj as UVv1StundentafelFachExport;
}
