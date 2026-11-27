import { JavaObject } from '../../../java/lang/JavaObject';
import { UvLerngruppeCreateRequest } from '../../../core/data/uv/UvLerngruppeCreateRequest';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { UvKursCreateRequest } from '../../../core/data/uv/UvKursCreateRequest';
import { UvSchuelergruppeCreateRequest } from '../../../core/data/uv/UvSchuelergruppeCreateRequest';

export class UvKursunterrichtAnlageRequest extends JavaObject {

	/**
	 * Der anzulegende Kurs.
	 */
	public kurs: UvKursCreateRequest = new UvKursCreateRequest();

	/**
	 * Die anzulegende Kurslerngruppe.
	 */
	public lerngruppe: UvLerngruppeCreateRequest = new UvLerngruppeCreateRequest();

	/**
	 * Die neue Schülergruppe; null verwendet die Gruppen-ID des Kurses.
	 */
	public schuelergruppe: UvSchuelergruppeCreateRequest | null = null;

	/**
	 * Die zusätzlich zuzuordnenden Schüler-IDs.
	 */
	public idsSchueler: List<number> = new ArrayList<number>();


	/**
	 *Erstellt eine leere Anlageanfrage.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvKursunterrichtAnlageRequest';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvKursunterrichtAnlageRequest'].includes(name);
	}

	public static readonly class = new Class<UvKursunterrichtAnlageRequest>('de.svws_nrw.core.data.uv.UvKursunterrichtAnlageRequest');

	public static transpilerFromJSON(json: string): UvKursunterrichtAnlageRequest {
		const obj = JSON.parse(json) as Partial<UvKursunterrichtAnlageRequest>;
		const result = new UvKursunterrichtAnlageRequest();
		if (obj.kurs === undefined)
			throw new Error('invalid json format, missing attribute kurs');
		result.kurs = UvKursCreateRequest.transpilerFromJSON(JSON.stringify(obj.kurs));
		if (obj.lerngruppe === undefined)
			throw new Error('invalid json format, missing attribute lerngruppe');
		result.lerngruppe = UvLerngruppeCreateRequest.transpilerFromJSON(JSON.stringify(obj.lerngruppe));
		result.schuelergruppe = ((obj.schuelergruppe === undefined) || (obj.schuelergruppe === null)) ? null : UvSchuelergruppeCreateRequest.transpilerFromJSON(JSON.stringify(obj.schuelergruppe));
		if (obj.idsSchueler !== undefined) {
			for (const elem of obj.idsSchueler) {
				result.idsSchueler.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: UvKursunterrichtAnlageRequest): string {
		let result = '{';
		result += '"kurs" : ' + UvKursCreateRequest.transpilerToJSON(obj.kurs) + ',';
		result += '"lerngruppe" : ' + UvLerngruppeCreateRequest.transpilerToJSON(obj.lerngruppe) + ',';
		result += '"schuelergruppe" : ' + ((obj.schuelergruppe === null) ? 'null' : UvSchuelergruppeCreateRequest.transpilerToJSON(obj.schuelergruppe)) + ',';
		result += '"idsSchueler" : [ ';
		for (let i = 0; i < obj.idsSchueler.size(); i++) {
			const elem = obj.idsSchueler.get(i);
			result += elem.toString();
			if (i < obj.idsSchueler.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvKursunterrichtAnlageRequest>): string {
		let result = '{';
		if (obj.kurs !== undefined) {
			result += '"kurs" : ' + UvKursCreateRequest.transpilerToJSON(obj.kurs) + ',';
		}
		if (obj.lerngruppe !== undefined) {
			result += '"lerngruppe" : ' + UvLerngruppeCreateRequest.transpilerToJSON(obj.lerngruppe) + ',';
		}
		if (obj.schuelergruppe !== undefined) {
			result += '"schuelergruppe" : ' + ((obj.schuelergruppe === null) ? 'null' : UvSchuelergruppeCreateRequest.transpilerToJSON(obj.schuelergruppe)) + ',';
		}
		if (obj.idsSchueler !== undefined) {
			result += '"idsSchueler" : [ ';
			for (let i = 0; i < obj.idsSchueler.size(); i++) {
				const elem = obj.idsSchueler.get(i);
				result += elem.toString();
				if (i < obj.idsSchueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvKursunterrichtAnlageRequest(obj: unknown): UvKursunterrichtAnlageRequest {
	return obj as UvKursunterrichtAnlageRequest;
}
