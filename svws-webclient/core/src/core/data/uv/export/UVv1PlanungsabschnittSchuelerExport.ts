import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class UVv1PlanungsabschnittSchuelerExport extends JavaObject {

	/**
	 * Die ID des Schülers.
	 */
	public idSchueler: number = -1;

	/**
	 * Die ID des Jahrgangs, dem der Schüler zugeordnet ist.
	 */
	public idJahrgang: number = -1;

	/**
	 * Die UV-ID der Klasse, der der Schüler zugeordnet ist.
	 */
	public klasseUvId: number | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.export.UVv1PlanungsabschnittSchuelerExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.export.UVv1PlanungsabschnittSchuelerExport'].includes(name);
	}

	public static readonly class = new Class<UVv1PlanungsabschnittSchuelerExport>('de.svws_nrw.core.data.uv.export.UVv1PlanungsabschnittSchuelerExport');

	public static transpilerFromJSON(json: string): UVv1PlanungsabschnittSchuelerExport {
		const obj = JSON.parse(json) as Partial<UVv1PlanungsabschnittSchuelerExport>;
		const result = new UVv1PlanungsabschnittSchuelerExport();
		if (obj.idSchueler === undefined)
			throw new Error('invalid json format, missing attribute idSchueler');
		result.idSchueler = obj.idSchueler;
		if (obj.idJahrgang === undefined)
			throw new Error('invalid json format, missing attribute idJahrgang');
		result.idJahrgang = obj.idJahrgang;
		result.klasseUvId = (obj.klasseUvId === undefined) ? null : obj.klasseUvId === null ? null : obj.klasseUvId;
		return result;
	}

	public static transpilerToJSON(obj: UVv1PlanungsabschnittSchuelerExport): string {
		let result = '{';
		result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		result += '"idJahrgang" : ' + obj.idJahrgang.toString() + ',';
		result += '"klasseUvId" : ' + ((obj.klasseUvId === null) ? 'null' : obj.klasseUvId.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UVv1PlanungsabschnittSchuelerExport>): string {
		let result = '{';
		if (obj.idSchueler !== undefined) {
			result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		}
		if (obj.idJahrgang !== undefined) {
			result += '"idJahrgang" : ' + obj.idJahrgang.toString() + ',';
		}
		if (obj.klasseUvId !== undefined) {
			result += '"klasseUvId" : ' + ((obj.klasseUvId === null) ? 'null' : obj.klasseUvId.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_export_UVv1PlanungsabschnittSchuelerExport(obj: unknown): UVv1PlanungsabschnittSchuelerExport {
	return obj as UVv1PlanungsabschnittSchuelerExport;
}
