import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvLerngruppenSchieneCreateRequest extends JavaObject {

	/**
	 * Die ID des Planungsabschnitts.
	 */
	public idPlanungsabschnitt: number = 0;

	/**
	 * Die ID der Lerngruppe.
	 */
	public idLerngruppe: number = 0;

	/**
	 * Die ID der Schiene.
	 */
	public idSchiene: number = 0;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvLerngruppenSchieneCreateRequest';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvLerngruppenSchieneCreateRequest'].includes(name);
	}

	public static readonly class = new Class<UvLerngruppenSchieneCreateRequest>('de.svws_nrw.core.data.uv.UvLerngruppenSchieneCreateRequest');

	public static transpilerFromJSON(json: string): UvLerngruppenSchieneCreateRequest {
		const obj = JSON.parse(json) as Partial<UvLerngruppenSchieneCreateRequest>;
		const result = new UvLerngruppenSchieneCreateRequest();
		if (obj.idPlanungsabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idPlanungsabschnitt');
		result.idPlanungsabschnitt = obj.idPlanungsabschnitt;
		if (obj.idLerngruppe === undefined)
			throw new Error('invalid json format, missing attribute idLerngruppe');
		result.idLerngruppe = obj.idLerngruppe;
		if (obj.idSchiene === undefined)
			throw new Error('invalid json format, missing attribute idSchiene');
		result.idSchiene = obj.idSchiene;
		return result;
	}

	public static transpilerToJSON(obj: UvLerngruppenSchieneCreateRequest): string {
		let result = '{';
		result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt + ',';
		result += '"idLerngruppe" : ' + obj.idLerngruppe + ',';
		result += '"idSchiene" : ' + obj.idSchiene + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvLerngruppenSchieneCreateRequest>): string {
		let result = '{';
		if (obj.idPlanungsabschnitt !== undefined) {
			result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt + ',';
		}
		if (obj.idLerngruppe !== undefined) {
			result += '"idLerngruppe" : ' + obj.idLerngruppe + ',';
		}
		if (obj.idSchiene !== undefined) {
			result += '"idSchiene" : ' + obj.idSchiene + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvLerngruppenSchieneCreateRequest(obj: unknown): UvLerngruppenSchieneCreateRequest {
	return obj as UvLerngruppenSchieneCreateRequest;
}
