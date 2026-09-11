import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class UVv1ZeitrasterEintragExport extends JavaObject {

	/**
	 * Die UV-ID des Zeitraster-Eintrags.
	 */
	public uvId: number = -1;

	/**
	 * Der {@link Wochentag} an dem der Unterricht stattfindet (1=Montag, 2=Dienstag, ..., 7=Sonntag)
	 */
	public wochentag: number = -1;

	/**
	 * Die Stunde (z. B. 1 = erste Stunde).
	 */
	public stunde: number = -1;

	/**
	 * Beginn der Stunde (als Minuten seit Mitternacht).
	 */
	public beginn: number = -1;

	/**
	 * Ende der Stunde (als Minuten seit Mitternacht).
	 */
	public ende: number = -1;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.export.UVv1ZeitrasterEintragExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.export.UVv1ZeitrasterEintragExport'].includes(name);
	}

	public static readonly class = new Class<UVv1ZeitrasterEintragExport>('de.svws_nrw.core.data.uv.export.UVv1ZeitrasterEintragExport');

	public static transpilerFromJSON(json: string): UVv1ZeitrasterEintragExport {
		const obj = JSON.parse(json) as Partial<UVv1ZeitrasterEintragExport>;
		const result = new UVv1ZeitrasterEintragExport();
		if (obj.uvId === undefined)
			throw new Error('invalid json format, missing attribute uvId');
		result.uvId = obj.uvId;
		if (obj.wochentag === undefined)
			throw new Error('invalid json format, missing attribute wochentag');
		result.wochentag = obj.wochentag;
		if (obj.stunde === undefined)
			throw new Error('invalid json format, missing attribute stunde');
		result.stunde = obj.stunde;
		if (obj.beginn === undefined)
			throw new Error('invalid json format, missing attribute beginn');
		result.beginn = obj.beginn;
		if (obj.ende === undefined)
			throw new Error('invalid json format, missing attribute ende');
		result.ende = obj.ende;
		return result;
	}

	public static transpilerToJSON(obj: UVv1ZeitrasterEintragExport): string {
		let result = '{';
		result += '"uvId" : ' + obj.uvId.toString() + ',';
		result += '"wochentag" : ' + obj.wochentag.toString() + ',';
		result += '"stunde" : ' + obj.stunde.toString() + ',';
		result += '"beginn" : ' + obj.beginn.toString() + ',';
		result += '"ende" : ' + obj.ende.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UVv1ZeitrasterEintragExport>): string {
		let result = '{';
		if (obj.uvId !== undefined) {
			result += '"uvId" : ' + obj.uvId.toString() + ',';
		}
		if (obj.wochentag !== undefined) {
			result += '"wochentag" : ' + obj.wochentag.toString() + ',';
		}
		if (obj.stunde !== undefined) {
			result += '"stunde" : ' + obj.stunde.toString() + ',';
		}
		if (obj.beginn !== undefined) {
			result += '"beginn" : ' + obj.beginn.toString() + ',';
		}
		if (obj.ende !== undefined) {
			result += '"ende" : ' + obj.ende.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_export_UVv1ZeitrasterEintragExport(obj: unknown): UVv1ZeitrasterEintragExport {
	return obj as UVv1ZeitrasterEintragExport;
}
