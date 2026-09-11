import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class UvSchuelergruppeCreateRequest extends JavaObject {

	/**
	 * Die negative temporäre ID beim Sammelimport.
	 */
	public id: number = 0;

	/**
	 * Die ID des Planungsabschnitts.
	 */
	public idPlanungsabschnitt: number = 0;

	/**
	 * Die Bezeichnung der Schülergruppe.
	 */
	public bezeichnung: string = "";

	/**
	 * Die IDs der erlaubten Jahrgänge.
	 */
	public idsJahrgaengeErlaubt: List<number> = new ArrayList<number>();

	/**
	 * Die IDs der erlaubten Gruppen.
	 */
	public idsGruppenErlaubt: List<number> = new ArrayList<number>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvSchuelergruppeCreateRequest';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvSchuelergruppeCreateRequest'].includes(name);
	}

	public static readonly class = new Class<UvSchuelergruppeCreateRequest>('de.svws_nrw.core.data.uv.UvSchuelergruppeCreateRequest');

	public static transpilerFromJSON(json: string): UvSchuelergruppeCreateRequest {
		const obj = JSON.parse(json) as Partial<UvSchuelergruppeCreateRequest>;
		const result = new UvSchuelergruppeCreateRequest();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idPlanungsabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idPlanungsabschnitt');
		result.idPlanungsabschnitt = obj.idPlanungsabschnitt;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.idsJahrgaengeErlaubt !== undefined) {
			for (const elem of obj.idsJahrgaengeErlaubt) {
				result.idsJahrgaengeErlaubt.add(elem);
			}
		}
		if (obj.idsGruppenErlaubt !== undefined) {
			for (const elem of obj.idsGruppenErlaubt) {
				result.idsGruppenErlaubt.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: UvSchuelergruppeCreateRequest): string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"idsJahrgaengeErlaubt" : [ ';
		for (let i = 0; i < obj.idsJahrgaengeErlaubt.size(); i++) {
			const elem = obj.idsJahrgaengeErlaubt.get(i);
			result += elem.toString();
			if (i < obj.idsJahrgaengeErlaubt.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"idsGruppenErlaubt" : [ ';
		for (let i = 0; i < obj.idsGruppenErlaubt.size(); i++) {
			const elem = obj.idsGruppenErlaubt.get(i);
			result += elem.toString();
			if (i < obj.idsGruppenErlaubt.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvSchuelergruppeCreateRequest>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id + ',';
		}
		if (obj.idPlanungsabschnitt !== undefined) {
			result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.idsJahrgaengeErlaubt !== undefined) {
			result += '"idsJahrgaengeErlaubt" : [ ';
			for (let i = 0; i < obj.idsJahrgaengeErlaubt.size(); i++) {
				const elem = obj.idsJahrgaengeErlaubt.get(i);
				result += elem.toString();
				if (i < obj.idsJahrgaengeErlaubt.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.idsGruppenErlaubt !== undefined) {
			result += '"idsGruppenErlaubt" : [ ';
			for (let i = 0; i < obj.idsGruppenErlaubt.size(); i++) {
				const elem = obj.idsGruppenErlaubt.get(i);
				result += elem.toString();
				if (i < obj.idsGruppenErlaubt.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvSchuelergruppeCreateRequest(obj: unknown): UvSchuelergruppeCreateRequest {
	return obj as UvSchuelergruppeCreateRequest;
}
