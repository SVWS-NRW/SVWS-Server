import { JavaObject } from '../../../../java/lang/JavaObject';
import { UVv1GrunddatenExport } from '../../../../core/data/uv/export/UVv1GrunddatenExport';
import { UVv1PlanungsabschnittExport } from '../../../../core/data/uv/export/UVv1PlanungsabschnittExport';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

export class UVv1Export extends JavaObject {

	/**
	 * Die Revision des UVv1Export-Datenformates, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten aufsteigend ab 1
	 */
	public uvExportRevision: number = 1;

	/**
	 * Die Grunddaten der UV, die für alle Planungsabschnitte gelten.
	 */
	public grunddaten: UVv1GrunddatenExport = new UVv1GrunddatenExport();

	/**
	 * Ein Array mit den Planungsabschnitten der Unterrichtsverteilung.
	 */
	public planungsabschnitte: List<UVv1PlanungsabschnittExport> = new ArrayList<UVv1PlanungsabschnittExport>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.export.UVv1Export';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.export.UVv1Export'].includes(name);
	}

	public static readonly class = new Class<UVv1Export>('de.svws_nrw.core.data.uv.export.UVv1Export');

	public static transpilerFromJSON(json: string): UVv1Export {
		const obj = JSON.parse(json) as Partial<UVv1Export>;
		const result = new UVv1Export();
		if (obj.uvExportRevision === undefined)
			throw new Error('invalid json format, missing attribute uvExportRevision');
		result.uvExportRevision = obj.uvExportRevision;
		if (obj.grunddaten === undefined)
			throw new Error('invalid json format, missing attribute grunddaten');
		result.grunddaten = UVv1GrunddatenExport.transpilerFromJSON(JSON.stringify(obj.grunddaten));
		if (obj.planungsabschnitte !== undefined) {
			for (const elem of obj.planungsabschnitte) {
				result.planungsabschnitte.add(UVv1PlanungsabschnittExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: UVv1Export): string {
		let result = '{';
		result += '"uvExportRevision" : ' + obj.uvExportRevision.toString() + ',';
		result += '"grunddaten" : ' + UVv1GrunddatenExport.transpilerToJSON(obj.grunddaten) + ',';
		result += '"planungsabschnitte" : [ ';
		for (let i = 0; i < obj.planungsabschnitte.size(); i++) {
			const elem = obj.planungsabschnitte.get(i);
			result += UVv1PlanungsabschnittExport.transpilerToJSON(elem);
			if (i < obj.planungsabschnitte.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UVv1Export>): string {
		let result = '{';
		if (obj.uvExportRevision !== undefined) {
			result += '"uvExportRevision" : ' + obj.uvExportRevision.toString() + ',';
		}
		if (obj.grunddaten !== undefined) {
			result += '"grunddaten" : ' + UVv1GrunddatenExport.transpilerToJSON(obj.grunddaten) + ',';
		}
		if (obj.planungsabschnitte !== undefined) {
			result += '"planungsabschnitte" : [ ';
			for (let i = 0; i < obj.planungsabschnitte.size(); i++) {
				const elem = obj.planungsabschnitte.get(i);
				result += UVv1PlanungsabschnittExport.transpilerToJSON(elem);
				if (i < obj.planungsabschnitte.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_export_UVv1Export(obj: unknown): UVv1Export {
	return obj as UVv1Export;
}
