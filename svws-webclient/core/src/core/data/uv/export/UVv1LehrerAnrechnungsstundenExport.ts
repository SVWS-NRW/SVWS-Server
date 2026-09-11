import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class UVv1LehrerAnrechnungsstundenExport extends JavaObject {

	/**
	 * Das Kürzel des Anrechnungsgrundes (z. B. 'AG' für Arbeitsgemeinschaft).
	 */
	public anrechnungsgrundKrz: string = "";

	/**
	 * Die Anzahl der angerechneten Stunden.
	 */
	public anzahlStunden: number = 0;

	/**
	 * Das Datum, ab dem die Anrechnungsstunde gültig ist.
	 */
	public gueltigVon: string = "";

	/**
	 * Das Datum, bis wann die Anrechnungsstunde gültig ist.
	 */
	public gueltigBis: string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.export.UVv1LehrerAnrechnungsstundenExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.export.UVv1LehrerAnrechnungsstundenExport'].includes(name);
	}

	public static readonly class = new Class<UVv1LehrerAnrechnungsstundenExport>('de.svws_nrw.core.data.uv.export.UVv1LehrerAnrechnungsstundenExport');

	public static transpilerFromJSON(json: string): UVv1LehrerAnrechnungsstundenExport {
		const obj = JSON.parse(json) as Partial<UVv1LehrerAnrechnungsstundenExport>;
		const result = new UVv1LehrerAnrechnungsstundenExport();
		if (obj.anrechnungsgrundKrz === undefined)
			throw new Error('invalid json format, missing attribute anrechnungsgrundKrz');
		result.anrechnungsgrundKrz = obj.anrechnungsgrundKrz;
		if (obj.anzahlStunden === undefined)
			throw new Error('invalid json format, missing attribute anzahlStunden');
		result.anzahlStunden = obj.anzahlStunden;
		if (obj.gueltigVon === undefined)
			throw new Error('invalid json format, missing attribute gueltigVon');
		result.gueltigVon = obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj: UVv1LehrerAnrechnungsstundenExport): string {
		let result = '{';
		result += '"anrechnungsgrundKrz" : ' + JSON.stringify(obj.anrechnungsgrundKrz) + ',';
		result += '"anzahlStunden" : ' + obj.anzahlStunden.toString() + ',';
		result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : JSON.stringify(obj.gueltigBis)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UVv1LehrerAnrechnungsstundenExport>): string {
		let result = '{';
		if (obj.anrechnungsgrundKrz !== undefined) {
			result += '"anrechnungsgrundKrz" : ' + JSON.stringify(obj.anrechnungsgrundKrz) + ',';
		}
		if (obj.anzahlStunden !== undefined) {
			result += '"anzahlStunden" : ' + obj.anzahlStunden.toString() + ',';
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

export function cast_de_svws_nrw_core_data_uv_export_UVv1LehrerAnrechnungsstundenExport(obj: unknown): UVv1LehrerAnrechnungsstundenExport {
	return obj as UVv1LehrerAnrechnungsstundenExport;
}
