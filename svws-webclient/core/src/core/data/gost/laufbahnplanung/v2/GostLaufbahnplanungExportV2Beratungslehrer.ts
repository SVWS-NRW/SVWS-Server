import { JavaObject } from '../../../../../java/lang/JavaObject';
import { Class } from '../../../../../java/lang/Class';

export class GostLaufbahnplanungExportV2Beratungslehrer extends JavaObject {

	/**
	 * Die ID des Lehrers.
	 */
	public id: number = 0;

	/**
	 * Das Kürzel des Lehrers.
	 */
	public kuerzel: string | null = null;

	/**
	 * Der Nachname des Lehrers.
	 */
	public nachname: string | null = null;

	/**
	 * Der Vorname des Lehrers.
	 */
	public vorname: string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2Beratungslehrer';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2Beratungslehrer'].includes(name);
	}

	public static readonly class = new Class<GostLaufbahnplanungExportV2Beratungslehrer>('de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2Beratungslehrer');

	public static transpilerFromJSON(json: string): GostLaufbahnplanungExportV2Beratungslehrer {
		const obj = JSON.parse(json) as Partial<GostLaufbahnplanungExportV2Beratungslehrer>;
		const result = new GostLaufbahnplanungExportV2Beratungslehrer();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = (obj.kuerzel === undefined) ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.nachname = (obj.nachname === undefined) ? null : obj.nachname === null ? null : obj.nachname;
		result.vorname = (obj.vorname === undefined) ? null : obj.vorname === null ? null : obj.vorname;
		return result;
	}

	public static transpilerToJSON(obj: GostLaufbahnplanungExportV2Beratungslehrer): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"nachname" : ' + ((obj.nachname === null) ? 'null' : JSON.stringify(obj.nachname)) + ',';
		result += '"vorname" : ' + ((obj.vorname === null) ? 'null' : JSON.stringify(obj.vorname)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<GostLaufbahnplanungExportV2Beratungslehrer>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		}
		if (obj.nachname !== undefined) {
			result += '"nachname" : ' + ((obj.nachname === null) ? 'null' : JSON.stringify(obj.nachname)) + ',';
		}
		if (obj.vorname !== undefined) {
			result += '"vorname" : ' + ((obj.vorname === null) ? 'null' : JSON.stringify(obj.vorname)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_laufbahnplanung_v2_GostLaufbahnplanungExportV2Beratungslehrer(obj: unknown): GostLaufbahnplanungExportV2Beratungslehrer {
	return obj as GostLaufbahnplanungExportV2Beratungslehrer;
}
