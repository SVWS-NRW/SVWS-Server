import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class UVv1LerngruppenLehrerExport extends JavaObject {

	/**
	 * Die UV-ID des Lehrers, der der Lerngruppe zugeordnet ist.
	 */
	public lehrerUvId: number = -1;

	/**
	 * Die Reihenfolge der Zuordnung.
	 */
	public reihenfolge: number = 1;

	/**
	 * Die Anzahl der Wochenstunden in dieser Lerngruppe.
	 */
	public wochenstunden: number = 0.0;

	/**
	 * Die Anzahl der Wochenstunden, die auf das Deputat angerechnet werden.
	 */
	public wochenstundenAngerechnet: number = 0.0;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.export.UVv1LerngruppenLehrerExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.export.UVv1LerngruppenLehrerExport'].includes(name);
	}

	public static readonly class = new Class<UVv1LerngruppenLehrerExport>('de.svws_nrw.core.data.uv.export.UVv1LerngruppenLehrerExport');

	public static transpilerFromJSON(json: string): UVv1LerngruppenLehrerExport {
		const obj = JSON.parse(json) as Partial<UVv1LerngruppenLehrerExport>;
		const result = new UVv1LerngruppenLehrerExport();
		if (obj.lehrerUvId === undefined)
			throw new Error('invalid json format, missing attribute lehrerUvId');
		result.lehrerUvId = obj.lehrerUvId;
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

	public static transpilerToJSON(obj: UVv1LerngruppenLehrerExport): string {
		let result = '{';
		result += '"lehrerUvId" : ' + obj.lehrerUvId.toString() + ',';
		result += '"reihenfolge" : ' + obj.reihenfolge.toString() + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		result += '"wochenstundenAngerechnet" : ' + obj.wochenstundenAngerechnet.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UVv1LerngruppenLehrerExport>): string {
		let result = '{';
		if (obj.lehrerUvId !== undefined) {
			result += '"lehrerUvId" : ' + obj.lehrerUvId.toString() + ',';
		}
		if (obj.reihenfolge !== undefined) {
			result += '"reihenfolge" : ' + obj.reihenfolge.toString() + ',';
		}
		if (obj.wochenstunden !== undefined) {
			result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		}
		if (obj.wochenstundenAngerechnet !== undefined) {
			result += '"wochenstundenAngerechnet" : ' + obj.wochenstundenAngerechnet.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_export_UVv1LerngruppenLehrerExport(obj: unknown): UVv1LerngruppenLehrerExport {
	return obj as UVv1LerngruppenLehrerExport;
}
