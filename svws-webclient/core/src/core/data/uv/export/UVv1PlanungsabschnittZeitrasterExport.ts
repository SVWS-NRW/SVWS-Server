import { JavaObject } from '../../../../java/lang/JavaObject';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

export class UVv1PlanungsabschnittZeitrasterExport extends JavaObject {

	/**
	 * Die UV-ID des Zeitrasters.
	 */
	public zeitrasterUvId: number = -1;

	/**
	 * Ein Array mit den IDs der zugeordneten Jahrgänge.
	 */
	public idsJahrgaenge: List<number> = new ArrayList<number>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.export.UVv1PlanungsabschnittZeitrasterExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.export.UVv1PlanungsabschnittZeitrasterExport'].includes(name);
	}

	public static readonly class = new Class<UVv1PlanungsabschnittZeitrasterExport>('de.svws_nrw.core.data.uv.export.UVv1PlanungsabschnittZeitrasterExport');

	public static transpilerFromJSON(json: string): UVv1PlanungsabschnittZeitrasterExport {
		const obj = JSON.parse(json) as Partial<UVv1PlanungsabschnittZeitrasterExport>;
		const result = new UVv1PlanungsabschnittZeitrasterExport();
		if (obj.zeitrasterUvId === undefined)
			throw new Error('invalid json format, missing attribute zeitrasterUvId');
		result.zeitrasterUvId = obj.zeitrasterUvId;
		if (obj.idsJahrgaenge !== undefined) {
			for (const elem of obj.idsJahrgaenge) {
				result.idsJahrgaenge.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: UVv1PlanungsabschnittZeitrasterExport): string {
		let result = '{';
		result += '"zeitrasterUvId" : ' + obj.zeitrasterUvId.toString() + ',';
		result += '"idsJahrgaenge" : [ ';
		for (let i = 0; i < obj.idsJahrgaenge.size(); i++) {
			const elem = obj.idsJahrgaenge.get(i);
			result += elem.toString();
			if (i < obj.idsJahrgaenge.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UVv1PlanungsabschnittZeitrasterExport>): string {
		let result = '{';
		if (obj.zeitrasterUvId !== undefined) {
			result += '"zeitrasterUvId" : ' + obj.zeitrasterUvId.toString() + ',';
		}
		if (obj.idsJahrgaenge !== undefined) {
			result += '"idsJahrgaenge" : [ ';
			for (let i = 0; i < obj.idsJahrgaenge.size(); i++) {
				const elem = obj.idsJahrgaenge.get(i);
				result += elem.toString();
				if (i < obj.idsJahrgaenge.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_export_UVv1PlanungsabschnittZeitrasterExport(obj: unknown): UVv1PlanungsabschnittZeitrasterExport {
	return obj as UVv1PlanungsabschnittZeitrasterExport;
}
