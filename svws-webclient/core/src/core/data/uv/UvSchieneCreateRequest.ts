import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class UvSchieneCreateRequest extends JavaObject {

	/**
	 * Die negative temporäre ID beim Sammelimport.
	 */
	public id: number = 0;

	/**
	 * Die ID des Planungsabschnitts, in dem die Schiene gilt.
	 */
	public idPlanungsabschnitt: number = 0;

	/**
	 * Die Nummer der Schiene.
	 */
	public nummer: number = 0;

	/**
	 * Die Bezeichnung der Schiene.
	 */
	public bezeichnung: string | null = null;

	/**
	 * Ein Array mit den IDs der erlaubten Jahrgänge.
	 */
	public idsJahrgaengeErlaubt: List<number> = new ArrayList<number>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvSchieneCreateRequest';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvSchieneCreateRequest'].includes(name);
	}

	public static readonly class = new Class<UvSchieneCreateRequest>('de.svws_nrw.core.data.uv.UvSchieneCreateRequest');

	public static transpilerFromJSON(json: string): UvSchieneCreateRequest {
		const obj = JSON.parse(json) as Partial<UvSchieneCreateRequest>;
		const result = new UvSchieneCreateRequest();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idPlanungsabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idPlanungsabschnitt');
		result.idPlanungsabschnitt = obj.idPlanungsabschnitt;
		if (obj.nummer === undefined)
			throw new Error('invalid json format, missing attribute nummer');
		result.nummer = obj.nummer;
		result.bezeichnung = (obj.bezeichnung === undefined) ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		if (obj.idsJahrgaengeErlaubt !== undefined) {
			for (const elem of obj.idsJahrgaengeErlaubt) {
				result.idsJahrgaengeErlaubt.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: UvSchieneCreateRequest): string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt + ',';
		result += '"nummer" : ' + obj.nummer + ',';
		result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result += '"idsJahrgaengeErlaubt" : [ ';
		for (let i = 0; i < obj.idsJahrgaengeErlaubt.size(); i++) {
			const elem = obj.idsJahrgaengeErlaubt.get(i);
			result += elem.toString();
			if (i < obj.idsJahrgaengeErlaubt.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvSchieneCreateRequest>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id + ',';
		}
		if (obj.idPlanungsabschnitt !== undefined) {
			result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt + ',';
		}
		if (obj.nummer !== undefined) {
			result += '"nummer" : ' + obj.nummer + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
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
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvSchieneCreateRequest(obj: unknown): UvSchieneCreateRequest {
	return obj as UvSchieneCreateRequest;
}
