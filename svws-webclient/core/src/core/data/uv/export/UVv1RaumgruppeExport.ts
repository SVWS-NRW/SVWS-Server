import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class UVv1RaumgruppeExport extends JavaObject {

	/**
	 * Die eindeutige UV-ID der Raumgruppe.
	 */
	public uvId: number = -1;

	/**
	 * Die Bezeichnung der Raumgruppe.
	 */
	public bezeichnung: string = "";

	/**
	 * Das Datum, ab dem die Raumgruppe gültig ist.
	 */
	public gueltigVon: string = "";

	/**
	 * Das Datum, bis wann die Raumgruppe gültig ist. Ist kein Datum gesetzt, gilt die Raumgruppe unbegrenzt weiter.
	 */
	public gueltigBis: string | null = null;

	/**
	 * Die Beschreibung der Raumgruppe.
	 */
	public beschreibung: string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.export.UVv1RaumgruppeExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.export.UVv1RaumgruppeExport'].includes(name);
	}

	public static readonly class = new Class<UVv1RaumgruppeExport>('de.svws_nrw.core.data.uv.export.UVv1RaumgruppeExport');

	public static transpilerFromJSON(json: string): UVv1RaumgruppeExport {
		const obj = JSON.parse(json) as Partial<UVv1RaumgruppeExport>;
		const result = new UVv1RaumgruppeExport();
		if (obj.uvId === undefined)
			throw new Error('invalid json format, missing attribute uvId');
		result.uvId = obj.uvId;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.gueltigVon === undefined)
			throw new Error('invalid json format, missing attribute gueltigVon');
		result.gueltigVon = obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		result.beschreibung = (obj.beschreibung === undefined) ? null : obj.beschreibung === null ? null : obj.beschreibung;
		return result;
	}

	public static transpilerToJSON(obj: UVv1RaumgruppeExport): string {
		let result = '{';
		result += '"uvId" : ' + obj.uvId.toString() + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : JSON.stringify(obj.gueltigBis)) + ',';
		result += '"beschreibung" : ' + ((obj.beschreibung === null) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UVv1RaumgruppeExport>): string {
		let result = '{';
		if (obj.uvId !== undefined) {
			result += '"uvId" : ' + obj.uvId.toString() + ',';
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
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_export_UVv1RaumgruppeExport(obj: unknown): UVv1RaumgruppeExport {
	return obj as UVv1RaumgruppeExport;
}
