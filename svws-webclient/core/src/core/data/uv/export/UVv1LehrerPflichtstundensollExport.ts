import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class UVv1LehrerPflichtstundensollExport extends JavaObject {

	/**
	 * Die Anzahl der Pflichtstunden, die der Lehrer in dem Gültigkeitszeitraum zu leisten hat.
	 */
	public pflichtstdSoll: number = 0;

	/**
	 * Das Datum, ab dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd).
	 */
	public gueltigVon: string = "";

	/**
	 * Das Datum, bis zu dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd).
	 */
	public gueltigBis: string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.export.UVv1LehrerPflichtstundensollExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.export.UVv1LehrerPflichtstundensollExport'].includes(name);
	}

	public static readonly class = new Class<UVv1LehrerPflichtstundensollExport>('de.svws_nrw.core.data.uv.export.UVv1LehrerPflichtstundensollExport');

	public static transpilerFromJSON(json: string): UVv1LehrerPflichtstundensollExport {
		const obj = JSON.parse(json) as Partial<UVv1LehrerPflichtstundensollExport>;
		const result = new UVv1LehrerPflichtstundensollExport();
		if (obj.pflichtstdSoll === undefined)
			throw new Error('invalid json format, missing attribute pflichtstdSoll');
		result.pflichtstdSoll = obj.pflichtstdSoll;
		if (obj.gueltigVon === undefined)
			throw new Error('invalid json format, missing attribute gueltigVon');
		result.gueltigVon = obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj: UVv1LehrerPflichtstundensollExport): string {
		let result = '{';
		result += '"pflichtstdSoll" : ' + obj.pflichtstdSoll.toString() + ',';
		result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : JSON.stringify(obj.gueltigBis)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UVv1LehrerPflichtstundensollExport>): string {
		let result = '{';
		if (obj.pflichtstdSoll !== undefined) {
			result += '"pflichtstdSoll" : ' + obj.pflichtstdSoll.toString() + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : JSON.stringify(obj.gueltigBis)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_export_UVv1LehrerPflichtstundensollExport(obj: unknown): UVv1LehrerPflichtstundensollExport {
	return obj as UVv1LehrerPflichtstundensollExport;
}
