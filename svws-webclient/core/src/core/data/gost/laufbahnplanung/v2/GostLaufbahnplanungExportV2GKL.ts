import { JavaObject } from '../../../../../java/lang/JavaObject';
import { Class } from '../../../../../java/lang/Class';

export class GostLaufbahnplanungExportV2GKL extends JavaObject {

	/**
	 * Die ID der Definition der Gleichwertigen komplexen Lernleistung (GKL).
	 */
	public id: number = -1;

	/**
	 * Gibt an, in welchem Fach die GKL gewählte wurde.
	 */
	public idFach: number = -1;

	/**
	 * Gibt das Halbjahr der gymnasialen Oberstufe an, wo die GKL stattfinden (0-4) - Q2.2 nicht möglich
	 */
	public idHalbjahr: number = -1;

	/**
	 * Gibt das Quartal an, in welchem die GKL in dem Halbjahr stattfinden soll.
	 */
	public quartal: number = -1;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2GKL';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2GKL'].includes(name);
	}

	public static readonly class = new Class<GostLaufbahnplanungExportV2GKL>('de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2GKL');

	public static transpilerFromJSON(json: string): GostLaufbahnplanungExportV2GKL {
		const obj = JSON.parse(json) as Partial<GostLaufbahnplanungExportV2GKL>;
		const result = new GostLaufbahnplanungExportV2GKL();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idFach === undefined)
			throw new Error('invalid json format, missing attribute idFach');
		result.idFach = obj.idFach;
		if (obj.idHalbjahr === undefined)
			throw new Error('invalid json format, missing attribute idHalbjahr');
		result.idHalbjahr = obj.idHalbjahr;
		if (obj.quartal === undefined)
			throw new Error('invalid json format, missing attribute quartal');
		result.quartal = obj.quartal;
		return result;
	}

	public static transpilerToJSON(obj: GostLaufbahnplanungExportV2GKL): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idFach" : ' + obj.idFach.toString() + ',';
		result += '"idHalbjahr" : ' + obj.idHalbjahr.toString() + ',';
		result += '"quartal" : ' + obj.quartal.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<GostLaufbahnplanungExportV2GKL>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idFach !== undefined) {
			result += '"idFach" : ' + obj.idFach.toString() + ',';
		}
		if (obj.idHalbjahr !== undefined) {
			result += '"idHalbjahr" : ' + obj.idHalbjahr.toString() + ',';
		}
		if (obj.quartal !== undefined) {
			result += '"quartal" : ' + obj.quartal.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_laufbahnplanung_v2_GostLaufbahnplanungExportV2GKL(obj: unknown): GostLaufbahnplanungExportV2GKL {
	return obj as GostLaufbahnplanungExportV2GKL;
}
