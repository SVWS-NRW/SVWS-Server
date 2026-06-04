import { JavaObject } from '../../../../../java/lang/JavaObject';
import { GostLaufbahnplanungExportV1Sprachbelegung } from '../../../../../core/data/gost/laufbahnplanung/v1/GostLaufbahnplanungExportV1Sprachbelegung';
import { ArrayList } from '../../../../../java/util/ArrayList';
import { GostLaufbahnplanungExportV1Sprachpruefung } from '../../../../../core/data/gost/laufbahnplanung/v1/GostLaufbahnplanungExportV1Sprachpruefung';
import type { List } from '../../../../../java/util/List';
import { Class } from '../../../../../java/lang/Class';

export class GostLaufbahnplanungExportV1Sprachen extends JavaObject {

	/**
	 * Die ID des Schülers.
	 */
	public schuelerID: number = 0;

	/**
	 * Die Liste der Sprachbelegungen.
	 */
	public belegungen: List<GostLaufbahnplanungExportV1Sprachbelegung> = new ArrayList<GostLaufbahnplanungExportV1Sprachbelegung>();

	/**
	 * Die Liste der Sprachprüfungen.
	 */
	public pruefungen: List<GostLaufbahnplanungExportV1Sprachpruefung> = new ArrayList<GostLaufbahnplanungExportV1Sprachpruefung>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1Sprachen';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1Sprachen'].includes(name);
	}

	public static readonly class = new Class<GostLaufbahnplanungExportV1Sprachen>('de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1Sprachen');

	public static transpilerFromJSON(json: string): GostLaufbahnplanungExportV1Sprachen {
		const obj = JSON.parse(json) as Partial<GostLaufbahnplanungExportV1Sprachen>;
		const result = new GostLaufbahnplanungExportV1Sprachen();
		if (obj.schuelerID === undefined)
			throw new Error('invalid json format, missing attribute schuelerID');
		result.schuelerID = obj.schuelerID;
		if (obj.belegungen !== undefined) {
			for (const elem of obj.belegungen) {
				result.belegungen.add(GostLaufbahnplanungExportV1Sprachbelegung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.pruefungen !== undefined) {
			for (const elem of obj.pruefungen) {
				result.pruefungen.add(GostLaufbahnplanungExportV1Sprachpruefung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: GostLaufbahnplanungExportV1Sprachen): string {
		let result = '{';
		result += '"schuelerID" : ' + obj.schuelerID.toString() + ',';
		result += '"belegungen" : [ ';
		for (let i = 0; i < obj.belegungen.size(); i++) {
			const elem = obj.belegungen.get(i);
			result += GostLaufbahnplanungExportV1Sprachbelegung.transpilerToJSON(elem);
			if (i < obj.belegungen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"pruefungen" : [ ';
		for (let i = 0; i < obj.pruefungen.size(); i++) {
			const elem = obj.pruefungen.get(i);
			result += GostLaufbahnplanungExportV1Sprachpruefung.transpilerToJSON(elem);
			if (i < obj.pruefungen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<GostLaufbahnplanungExportV1Sprachen>): string {
		let result = '{';
		if (obj.schuelerID !== undefined) {
			result += '"schuelerID" : ' + obj.schuelerID.toString() + ',';
		}
		if (obj.belegungen !== undefined) {
			result += '"belegungen" : [ ';
			for (let i = 0; i < obj.belegungen.size(); i++) {
				const elem = obj.belegungen.get(i);
				result += GostLaufbahnplanungExportV1Sprachbelegung.transpilerToJSON(elem);
				if (i < obj.belegungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.pruefungen !== undefined) {
			result += '"pruefungen" : [ ';
			for (let i = 0; i < obj.pruefungen.size(); i++) {
				const elem = obj.pruefungen.get(i);
				result += GostLaufbahnplanungExportV1Sprachpruefung.transpilerToJSON(elem);
				if (i < obj.pruefungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_laufbahnplanung_v1_GostLaufbahnplanungExportV1Sprachen(obj: unknown): GostLaufbahnplanungExportV1Sprachen {
	return obj as GostLaufbahnplanungExportV1Sprachen;
}
