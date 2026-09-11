import { JavaObject } from '../../../../java/lang/JavaObject';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

export class UVv1UnterrichtExport extends JavaObject {

	/**
	 * Die UV-ID des Unterrichts.
	 */
	public uvId: number = -1;

	/**
	 * Die UV-ID des Zeitraster-Eintrags.
	 */
	public zeitrasterEintragUvId: number | null = null;

	/**
	 * Die UV-ID der Lerngruppe.
	 */
	public lerngruppeUvId: number = -1;

	/**
	 * Ein Array mit den UV-IDs der zugewiesenen Räume.
	 */
	public raumUvIds: List<number> = new ArrayList<number>();

	/**
	 * Ein Array mit den UV-IDs der zugewiesenen Lerngruppenlehrer.
	 */
	public lerngruppenlehrerUvIds: List<number> = new ArrayList<number>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.export.UVv1UnterrichtExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.export.UVv1UnterrichtExport'].includes(name);
	}

	public static readonly class = new Class<UVv1UnterrichtExport>('de.svws_nrw.core.data.uv.export.UVv1UnterrichtExport');

	public static transpilerFromJSON(json: string): UVv1UnterrichtExport {
		const obj = JSON.parse(json) as Partial<UVv1UnterrichtExport>;
		const result = new UVv1UnterrichtExport();
		if (obj.uvId === undefined)
			throw new Error('invalid json format, missing attribute uvId');
		result.uvId = obj.uvId;
		result.zeitrasterEintragUvId = (obj.zeitrasterEintragUvId === undefined) ? null : obj.zeitrasterEintragUvId === null ? null : obj.zeitrasterEintragUvId;
		if (obj.lerngruppeUvId === undefined)
			throw new Error('invalid json format, missing attribute lerngruppeUvId');
		result.lerngruppeUvId = obj.lerngruppeUvId;
		if (obj.raumUvIds !== undefined) {
			for (const elem of obj.raumUvIds) {
				result.raumUvIds.add(elem);
			}
		}
		if (obj.lerngruppenlehrerUvIds !== undefined) {
			for (const elem of obj.lerngruppenlehrerUvIds) {
				result.lerngruppenlehrerUvIds.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: UVv1UnterrichtExport): string {
		let result = '{';
		result += '"uvId" : ' + obj.uvId.toString() + ',';
		result += '"zeitrasterEintragUvId" : ' + ((obj.zeitrasterEintragUvId === null) ? 'null' : obj.zeitrasterEintragUvId.toString()) + ',';
		result += '"lerngruppeUvId" : ' + obj.lerngruppeUvId.toString() + ',';
		result += '"raumUvIds" : [ ';
		for (let i = 0; i < obj.raumUvIds.size(); i++) {
			const elem = obj.raumUvIds.get(i);
			result += elem.toString();
			if (i < obj.raumUvIds.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lerngruppenlehrerUvIds" : [ ';
		for (let i = 0; i < obj.lerngruppenlehrerUvIds.size(); i++) {
			const elem = obj.lerngruppenlehrerUvIds.get(i);
			result += elem.toString();
			if (i < obj.lerngruppenlehrerUvIds.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UVv1UnterrichtExport>): string {
		let result = '{';
		if (obj.uvId !== undefined) {
			result += '"uvId" : ' + obj.uvId.toString() + ',';
		}
		if (obj.zeitrasterEintragUvId !== undefined) {
			result += '"zeitrasterEintragUvId" : ' + ((obj.zeitrasterEintragUvId === null) ? 'null' : obj.zeitrasterEintragUvId.toString()) + ',';
		}
		if (obj.lerngruppeUvId !== undefined) {
			result += '"lerngruppeUvId" : ' + obj.lerngruppeUvId.toString() + ',';
		}
		if (obj.raumUvIds !== undefined) {
			result += '"raumUvIds" : [ ';
			for (let i = 0; i < obj.raumUvIds.size(); i++) {
				const elem = obj.raumUvIds.get(i);
				result += elem.toString();
				if (i < obj.raumUvIds.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lerngruppenlehrerUvIds !== undefined) {
			result += '"lerngruppenlehrerUvIds" : [ ';
			for (let i = 0; i < obj.lerngruppenlehrerUvIds.size(); i++) {
				const elem = obj.lerngruppenlehrerUvIds.get(i);
				result += elem.toString();
				if (i < obj.lerngruppenlehrerUvIds.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_export_UVv1UnterrichtExport(obj: unknown): UVv1UnterrichtExport {
	return obj as UVv1UnterrichtExport;
}
