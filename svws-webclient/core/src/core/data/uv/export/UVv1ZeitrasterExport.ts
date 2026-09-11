import { JavaObject } from '../../../../java/lang/JavaObject';
import { UVv1ZeitrasterEintragExport } from '../../../../core/data/uv/export/UVv1ZeitrasterEintragExport';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

export class UVv1ZeitrasterExport extends JavaObject {

	/**
	 * Die UV-ID des Zeitrasters.
	 */
	public uvId: number = -1;

	/**
	 * Das Datum, ab dem das Zeitraster gültig ist (ISO-Datum als String, z. B. 2025-08-01).
	 */
	public gueltigVon: string = "";

	/**
	 * Das Datum, bis zu dem das Zeitraster gültig ist (oder null, falls unbegrenzt gültig).
	 */
	public gueltigBis: string | null = null;

	/**
	 * Die Bezeichnung des Zeitrasters.
	 */
	public bezeichnung: string | null = null;

	/**
	 * Ein Array mit den Einträgen des Zeitrasters.
	 */
	public eintraege: List<UVv1ZeitrasterEintragExport> = new ArrayList<UVv1ZeitrasterEintragExport>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.export.UVv1ZeitrasterExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.export.UVv1ZeitrasterExport'].includes(name);
	}

	public static readonly class = new Class<UVv1ZeitrasterExport>('de.svws_nrw.core.data.uv.export.UVv1ZeitrasterExport');

	public static transpilerFromJSON(json: string): UVv1ZeitrasterExport {
		const obj = JSON.parse(json) as Partial<UVv1ZeitrasterExport>;
		const result = new UVv1ZeitrasterExport();
		if (obj.uvId === undefined)
			throw new Error('invalid json format, missing attribute uvId');
		result.uvId = obj.uvId;
		if (obj.gueltigVon === undefined)
			throw new Error('invalid json format, missing attribute gueltigVon');
		result.gueltigVon = obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		result.bezeichnung = (obj.bezeichnung === undefined) ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		if (obj.eintraege !== undefined) {
			for (const elem of obj.eintraege) {
				result.eintraege.add(UVv1ZeitrasterEintragExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: UVv1ZeitrasterExport): string {
		let result = '{';
		result += '"uvId" : ' + obj.uvId.toString() + ',';
		result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : JSON.stringify(obj.gueltigBis)) + ',';
		result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result += '"eintraege" : [ ';
		for (let i = 0; i < obj.eintraege.size(); i++) {
			const elem = obj.eintraege.get(i);
			result += UVv1ZeitrasterEintragExport.transpilerToJSON(elem);
			if (i < obj.eintraege.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UVv1ZeitrasterExport>): string {
		let result = '{';
		if (obj.uvId !== undefined) {
			result += '"uvId" : ' + obj.uvId.toString() + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : JSON.stringify(obj.gueltigBis)) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		if (obj.eintraege !== undefined) {
			result += '"eintraege" : [ ';
			for (let i = 0; i < obj.eintraege.size(); i++) {
				const elem = obj.eintraege.get(i);
				result += UVv1ZeitrasterEintragExport.transpilerToJSON(elem);
				if (i < obj.eintraege.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_export_UVv1ZeitrasterExport(obj: unknown): UVv1ZeitrasterExport {
	return obj as UVv1ZeitrasterExport;
}
