import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvSchuelergruppeSchuelerCreateRequest extends JavaObject {

	/**
	 * Die ID des Planungsabschnitts.
	 */
	public idPlanungsabschnitt: number = 0;

	/**
	 * Die ID der Schülergruppe.
	 */
	public idSchuelergruppe: number = 0;

	/**
	 * Die ID des Schülers.
	 */
	public idSchueler: number = 0;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvSchuelergruppeSchuelerCreateRequest';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvSchuelergruppeSchuelerCreateRequest'].includes(name);
	}

	public static readonly class = new Class<UvSchuelergruppeSchuelerCreateRequest>('de.svws_nrw.core.data.uv.UvSchuelergruppeSchuelerCreateRequest');

	public static transpilerFromJSON(json: string): UvSchuelergruppeSchuelerCreateRequest {
		const obj = JSON.parse(json) as Partial<UvSchuelergruppeSchuelerCreateRequest>;
		const result = new UvSchuelergruppeSchuelerCreateRequest();
		if (obj.idPlanungsabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idPlanungsabschnitt');
		result.idPlanungsabschnitt = obj.idPlanungsabschnitt;
		if (obj.idSchuelergruppe === undefined)
			throw new Error('invalid json format, missing attribute idSchuelergruppe');
		result.idSchuelergruppe = obj.idSchuelergruppe;
		if (obj.idSchueler === undefined)
			throw new Error('invalid json format, missing attribute idSchueler');
		result.idSchueler = obj.idSchueler;
		return result;
	}

	public static transpilerToJSON(obj: UvSchuelergruppeSchuelerCreateRequest): string {
		let result = '{';
		result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt + ',';
		result += '"idSchuelergruppe" : ' + obj.idSchuelergruppe + ',';
		result += '"idSchueler" : ' + obj.idSchueler + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvSchuelergruppeSchuelerCreateRequest>): string {
		let result = '{';
		if (obj.idPlanungsabschnitt !== undefined) {
			result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt + ',';
		}
		if (obj.idSchuelergruppe !== undefined) {
			result += '"idSchuelergruppe" : ' + obj.idSchuelergruppe + ',';
		}
		if (obj.idSchueler !== undefined) {
			result += '"idSchueler" : ' + obj.idSchueler + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvSchuelergruppeSchuelerCreateRequest(obj: unknown): UvSchuelergruppeSchuelerCreateRequest {
	return obj as UvSchuelergruppeSchuelerCreateRequest;
}
