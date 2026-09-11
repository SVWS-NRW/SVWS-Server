import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class UVv1RaumExport extends JavaObject {

	/**
	 * Die eindeutige UV-ID des Raums.
	 */
	public uvId: number = -1;

	/**
	 * Das Kürzel des Raums.
	 */
	public kuerzel: string = "";

	/**
	 * Die Beschreibung des Raums.
	 */
	public beschreibung: string | null = "";

	/**
	 * Die Größe des Raumes an Arbeitsplätzen für Schüler.
	 */
	public groesse: number = -1;

	/**
	 * Die UV-ID der Raumgruppe, falls der Raum zu einer solchen gehört.
	 */
	public raumgruppeUvId: number | null = null;

	/**
	 * Das Datum, ab dem der Raum gültig ist.
	 */
	public gueltigVon: string = "";

	/**
	 * Das Datum, bis wann der Raum gültig ist. Ist kein Datum gesetzt, gilt der Raum unbegrenzt weiter.
	 */
	public gueltigBis: string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.export.UVv1RaumExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.export.UVv1RaumExport'].includes(name);
	}

	public static readonly class = new Class<UVv1RaumExport>('de.svws_nrw.core.data.uv.export.UVv1RaumExport');

	public static transpilerFromJSON(json: string): UVv1RaumExport {
		const obj = JSON.parse(json) as Partial<UVv1RaumExport>;
		const result = new UVv1RaumExport();
		if (obj.uvId === undefined)
			throw new Error('invalid json format, missing attribute uvId');
		result.uvId = obj.uvId;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		result.beschreibung = (obj.beschreibung === undefined) ? null : obj.beschreibung === null ? null : obj.beschreibung;
		if (obj.groesse === undefined)
			throw new Error('invalid json format, missing attribute groesse');
		result.groesse = obj.groesse;
		result.raumgruppeUvId = (obj.raumgruppeUvId === undefined) ? null : obj.raumgruppeUvId === null ? null : obj.raumgruppeUvId;
		if (obj.gueltigVon === undefined)
			throw new Error('invalid json format, missing attribute gueltigVon');
		result.gueltigVon = obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj: UVv1RaumExport): string {
		let result = '{';
		result += '"uvId" : ' + obj.uvId.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"beschreibung" : ' + ((obj.beschreibung === null) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		result += '"groesse" : ' + obj.groesse.toString() + ',';
		result += '"raumgruppeUvId" : ' + ((obj.raumgruppeUvId === null) ? 'null' : obj.raumgruppeUvId.toString()) + ',';
		result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : JSON.stringify(obj.gueltigBis)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UVv1RaumExport>): string {
		let result = '{';
		if (obj.uvId !== undefined) {
			result += '"uvId" : ' + obj.uvId.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.beschreibung !== undefined) {
			result += '"beschreibung" : ' + ((obj.beschreibung === null) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		}
		if (obj.groesse !== undefined) {
			result += '"groesse" : ' + obj.groesse.toString() + ',';
		}
		if (obj.raumgruppeUvId !== undefined) {
			result += '"raumgruppeUvId" : ' + ((obj.raumgruppeUvId === null) ? 'null' : obj.raumgruppeUvId.toString()) + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : JSON.stringify(obj.gueltigBis)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_export_UVv1RaumExport(obj: unknown): UVv1RaumExport {
	return obj as UVv1RaumExport;
}
