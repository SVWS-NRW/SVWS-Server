import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class UVv1LehrerUnterrichtsfachExport extends JavaObject {

	/**
	 * Die ID des Fachs in den Fachdaten.
	 */
	public idFach: number = -1;

	/**
	 * Gibt an, ob das Fach in der Sekundarstufe I unterrichtet werden darf.
	 */
	public istSek1: boolean = false;

	/**
	 * Gibt an, ob das Fach in der Sekundarstufe II unterrichtet werden darf.
	 */
	public istSek2: boolean = false;

	/**
	 * Die Bemerkung zum Unterrichtsfach.
	 */
	public bemerkung: string | null = null;

	/**
	 * Das Datum, ab dem die Lehrkraft das Fach unterrichtet.
	 */
	public gueltigVon: string | null = null;

	/**
	 * Das Datum, bis zu dem die Lehrkraft das Fach unterrichtet.
	 */
	public gueltigBis: string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.export.UVv1LehrerUnterrichtsfachExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.export.UVv1LehrerUnterrichtsfachExport'].includes(name);
	}

	public static readonly class = new Class<UVv1LehrerUnterrichtsfachExport>('de.svws_nrw.core.data.uv.export.UVv1LehrerUnterrichtsfachExport');

	public static transpilerFromJSON(json: string): UVv1LehrerUnterrichtsfachExport {
		const obj = JSON.parse(json) as Partial<UVv1LehrerUnterrichtsfachExport>;
		const result = new UVv1LehrerUnterrichtsfachExport();
		if (obj.idFach === undefined)
			throw new Error('invalid json format, missing attribute idFach');
		result.idFach = obj.idFach;
		if (obj.istSek1 === undefined)
			throw new Error('invalid json format, missing attribute istSek1');
		result.istSek1 = obj.istSek1;
		if (obj.istSek2 === undefined)
			throw new Error('invalid json format, missing attribute istSek2');
		result.istSek2 = obj.istSek2;
		result.bemerkung = (obj.bemerkung === undefined) ? null : obj.bemerkung === null ? null : obj.bemerkung;
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj: UVv1LehrerUnterrichtsfachExport): string {
		let result = '{';
		result += '"idFach" : ' + obj.idFach.toString() + ',';
		result += '"istSek1" : ' + obj.istSek1.toString() + ',';
		result += '"istSek2" : ' + obj.istSek2.toString() + ',';
		result += '"bemerkung" : ' + ((obj.bemerkung === null) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : JSON.stringify(obj.gueltigVon)) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : JSON.stringify(obj.gueltigBis)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UVv1LehrerUnterrichtsfachExport>): string {
		let result = '{';
		if (obj.idFach !== undefined) {
			result += '"idFach" : ' + obj.idFach.toString() + ',';
		}
		if (obj.istSek1 !== undefined) {
			result += '"istSek1" : ' + obj.istSek1.toString() + ',';
		}
		if (obj.istSek2 !== undefined) {
			result += '"istSek2" : ' + obj.istSek2.toString() + ',';
		}
		if (obj.bemerkung !== undefined) {
			result += '"bemerkung" : ' + ((obj.bemerkung === null) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : JSON.stringify(obj.gueltigVon)) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : JSON.stringify(obj.gueltigBis)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_export_UVv1LehrerUnterrichtsfachExport(obj: unknown): UVv1LehrerUnterrichtsfachExport {
	return obj as UVv1LehrerUnterrichtsfachExport;
}
