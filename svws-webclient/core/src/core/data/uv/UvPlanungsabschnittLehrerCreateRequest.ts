import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvPlanungsabschnittLehrerCreateRequest extends JavaObject {

	/**
	 * Die ID des Planungsabschnitts.
	 */
	public idPlanungsabschnitt: number = 0;

	/**
	 * Die ID des Lehrers.
	 */
	public idLehrer: number = 0;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvPlanungsabschnittLehrerCreateRequest';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvPlanungsabschnittLehrerCreateRequest'].includes(name);
	}

	public static readonly class = new Class<UvPlanungsabschnittLehrerCreateRequest>('de.svws_nrw.core.data.uv.UvPlanungsabschnittLehrerCreateRequest');

	public static transpilerFromJSON(json: string): UvPlanungsabschnittLehrerCreateRequest {
		const obj = JSON.parse(json) as Partial<UvPlanungsabschnittLehrerCreateRequest>;
		const result = new UvPlanungsabschnittLehrerCreateRequest();
		if (obj.idPlanungsabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idPlanungsabschnitt');
		result.idPlanungsabschnitt = obj.idPlanungsabschnitt;
		if (obj.idLehrer === undefined)
			throw new Error('invalid json format, missing attribute idLehrer');
		result.idLehrer = obj.idLehrer;
		return result;
	}

	public static transpilerToJSON(obj: UvPlanungsabschnittLehrerCreateRequest): string {
		let result = '{';
		result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt + ',';
		result += '"idLehrer" : ' + obj.idLehrer + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvPlanungsabschnittLehrerCreateRequest>): string {
		let result = '{';
		if (obj.idPlanungsabschnitt !== undefined) {
			result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt + ',';
		}
		if (obj.idLehrer !== undefined) {
			result += '"idLehrer" : ' + obj.idLehrer + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvPlanungsabschnittLehrerCreateRequest(obj: unknown): UvPlanungsabschnittLehrerCreateRequest {
	return obj as UvPlanungsabschnittLehrerCreateRequest;
}
