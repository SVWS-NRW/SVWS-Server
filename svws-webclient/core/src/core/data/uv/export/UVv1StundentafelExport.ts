import { JavaObject } from '../../../../java/lang/JavaObject';
import { ArrayList } from '../../../../java/util/ArrayList';
import { UVv1StundentafelFachExport } from '../../../../core/data/uv/export/UVv1StundentafelFachExport';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

export class UVv1StundentafelExport extends JavaObject {

	/**
	 * Die UV-ID der Stundentafel.
	 */
	public uvId: number = -1;

	/**
	 * Die ID des zugehörigen Jahrgangs.
	 */
	public idJahrgang: number = -1;

	/**
	 * Die optionale Bezeichnung der Stundentafel.
	 */
	public bezeichnung: string = "";

	/**
	 * Das Datum, ab dem die Stundentafel gültig ist.
	 */
	public gueltigVon: string = "";

	/**
	 * Das Datum, bis wann die Stundentafel gültig ist.
	 */
	public gueltigBis: string | null = "";

	/**
	 * Die optionale Beschreibung oder der Kommentar zur Stundentafel.
	 */
	public beschreibung: string | null = null;

	/**
	 * Ein Array mit den Fächern der Stundentafel.
	 */
	public stundentafelfaecher: List<UVv1StundentafelFachExport> = new ArrayList<UVv1StundentafelFachExport>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.export.UVv1StundentafelExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.export.UVv1StundentafelExport'].includes(name);
	}

	public static readonly class = new Class<UVv1StundentafelExport>('de.svws_nrw.core.data.uv.export.UVv1StundentafelExport');

	public static transpilerFromJSON(json: string): UVv1StundentafelExport {
		const obj = JSON.parse(json) as Partial<UVv1StundentafelExport>;
		const result = new UVv1StundentafelExport();
		if (obj.uvId === undefined)
			throw new Error('invalid json format, missing attribute uvId');
		result.uvId = obj.uvId;
		if (obj.idJahrgang === undefined)
			throw new Error('invalid json format, missing attribute idJahrgang');
		result.idJahrgang = obj.idJahrgang;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.gueltigVon === undefined)
			throw new Error('invalid json format, missing attribute gueltigVon');
		result.gueltigVon = obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		result.beschreibung = (obj.beschreibung === undefined) ? null : obj.beschreibung === null ? null : obj.beschreibung;
		if (obj.stundentafelfaecher !== undefined) {
			for (const elem of obj.stundentafelfaecher) {
				result.stundentafelfaecher.add(UVv1StundentafelFachExport.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: UVv1StundentafelExport): string {
		let result = '{';
		result += '"uvId" : ' + obj.uvId.toString() + ',';
		result += '"idJahrgang" : ' + obj.idJahrgang.toString() + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : JSON.stringify(obj.gueltigBis)) + ',';
		result += '"beschreibung" : ' + ((obj.beschreibung === null) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		result += '"stundentafelfaecher" : [ ';
		for (let i = 0; i < obj.stundentafelfaecher.size(); i++) {
			const elem = obj.stundentafelfaecher.get(i);
			result += UVv1StundentafelFachExport.transpilerToJSON(elem);
			if (i < obj.stundentafelfaecher.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UVv1StundentafelExport>): string {
		let result = '{';
		if (obj.uvId !== undefined) {
			result += '"uvId" : ' + obj.uvId.toString() + ',';
		}
		if (obj.idJahrgang !== undefined) {
			result += '"idJahrgang" : ' + obj.idJahrgang.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : JSON.stringify(obj.gueltigBis)) + ',';
		}
		if (obj.beschreibung !== undefined) {
			result += '"beschreibung" : ' + ((obj.beschreibung === null) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		}
		if (obj.stundentafelfaecher !== undefined) {
			result += '"stundentafelfaecher" : [ ';
			for (let i = 0; i < obj.stundentafelfaecher.size(); i++) {
				const elem = obj.stundentafelfaecher.get(i);
				result += UVv1StundentafelFachExport.transpilerToJSON(elem);
				if (i < obj.stundentafelfaecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_export_UVv1StundentafelExport(obj: unknown): UVv1StundentafelExport {
	return obj as UVv1StundentafelExport;
}
