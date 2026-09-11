import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvLerngruppenLehrerCreateRequest extends JavaObject {

	/**
	 * Die ID des Planungsabschnitts.
	 */
	public idPlanungsabschnitt: number = 0;

	/**
	 * Die ID der Lerngruppe.
	 */
	public idLerngruppe: number = 0;

	/**
	 * Die ID des Lehrers.
	 */
	public idLehrer: number = 0;

	/**
	 * Die Reihenfolge.
	 */
	public reihenfolge: number = 0;

	/**
	 * Die Wochenstunden.
	 */
	public wochenstunden: number = 0.0;

	/**
	 * Die angerechneten Wochenstunden.
	 */
	public wochenstundenAngerechnet: number = 0.0;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvLerngruppenLehrerCreateRequest';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvLerngruppenLehrerCreateRequest'].includes(name);
	}

	public static readonly class = new Class<UvLerngruppenLehrerCreateRequest>('de.svws_nrw.core.data.uv.UvLerngruppenLehrerCreateRequest');

	public static transpilerFromJSON(json: string): UvLerngruppenLehrerCreateRequest {
		const obj = JSON.parse(json) as Partial<UvLerngruppenLehrerCreateRequest>;
		const result = new UvLerngruppenLehrerCreateRequest();
		if (obj.idPlanungsabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idPlanungsabschnitt');
		result.idPlanungsabschnitt = obj.idPlanungsabschnitt;
		if (obj.idLerngruppe === undefined)
			throw new Error('invalid json format, missing attribute idLerngruppe');
		result.idLerngruppe = obj.idLerngruppe;
		if (obj.idLehrer === undefined)
			throw new Error('invalid json format, missing attribute idLehrer');
		result.idLehrer = obj.idLehrer;
		if (obj.reihenfolge === undefined)
			throw new Error('invalid json format, missing attribute reihenfolge');
		result.reihenfolge = obj.reihenfolge;
		if (obj.wochenstunden === undefined)
			throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		if (obj.wochenstundenAngerechnet === undefined)
			throw new Error('invalid json format, missing attribute wochenstundenAngerechnet');
		result.wochenstundenAngerechnet = obj.wochenstundenAngerechnet;
		return result;
	}

	public static transpilerToJSON(obj: UvLerngruppenLehrerCreateRequest): string {
		let result = '{';
		result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt + ',';
		result += '"idLerngruppe" : ' + obj.idLerngruppe + ',';
		result += '"idLehrer" : ' + obj.idLehrer + ',';
		result += '"reihenfolge" : ' + obj.reihenfolge + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden + ',';
		result += '"wochenstundenAngerechnet" : ' + obj.wochenstundenAngerechnet + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvLerngruppenLehrerCreateRequest>): string {
		let result = '{';
		if (obj.idPlanungsabschnitt !== undefined) {
			result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt + ',';
		}
		if (obj.idLerngruppe !== undefined) {
			result += '"idLerngruppe" : ' + obj.idLerngruppe + ',';
		}
		if (obj.idLehrer !== undefined) {
			result += '"idLehrer" : ' + obj.idLehrer + ',';
		}
		if (obj.reihenfolge !== undefined) {
			result += '"reihenfolge" : ' + obj.reihenfolge + ',';
		}
		if (obj.wochenstunden !== undefined) {
			result += '"wochenstunden" : ' + obj.wochenstunden + ',';
		}
		if (obj.wochenstundenAngerechnet !== undefined) {
			result += '"wochenstundenAngerechnet" : ' + obj.wochenstundenAngerechnet + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvLerngruppenLehrerCreateRequest(obj: unknown): UvLerngruppenLehrerCreateRequest {
	return obj as UvLerngruppenLehrerCreateRequest;
}
