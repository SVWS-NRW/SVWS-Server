import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class KlinikschuleStatistikExport extends JavaObject {

	/**
	 * Schüler allgemeinbildend insgesamt.
	 */
	public allgemeinbildendInsgesamt: number = 0.0;

	/**
	 * Schüler allgemeinbildend mit Schwerstbehinderung.
	 */
	public allgemeinbildendSchwerstbehindert: number = 0.0;

	/**
	 * Schüler berufsbildend Teilzeit insgesamt.
	 */
	public berufsbildendTeilzeitInsgesamt: number = 0.0;

	/**
	 * Schüler berufsbildend Teilzeit mit Schwerstbehinderung.
	 */
	public berufsbildendTeilzeitSchwerstbehindert: number = 0.0;

	/**
	 * Schüler berufsbildend Vollzeit insgesamt.
	 */
	public berufsbildendVollzeitInsgesamt: number = 0.0;

	/**
	 * Schüler berufsbildend Vollzeit mit Schwerstbehinderung.
	 */
	public berufsbildendVollzeitSchwerstbehindert: number = 0.0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.KlinikschuleStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.KlinikschuleStatistikExport'].includes(name);
	}

	public static readonly class = new Class<KlinikschuleStatistikExport>('de.svws_nrw.asd.export.data.KlinikschuleStatistikExport');

	public static transpilerFromJSON(json: string): KlinikschuleStatistikExport {
		const obj = JSON.parse(json) as Partial<KlinikschuleStatistikExport>;
		const result = new KlinikschuleStatistikExport();
		if (obj.allgemeinbildendInsgesamt === undefined)
			throw new Error('invalid json format, missing attribute allgemeinbildendInsgesamt');
		result.allgemeinbildendInsgesamt = obj.allgemeinbildendInsgesamt;
		if (obj.allgemeinbildendSchwerstbehindert === undefined)
			throw new Error('invalid json format, missing attribute allgemeinbildendSchwerstbehindert');
		result.allgemeinbildendSchwerstbehindert = obj.allgemeinbildendSchwerstbehindert;
		if (obj.berufsbildendTeilzeitInsgesamt === undefined)
			throw new Error('invalid json format, missing attribute berufsbildendTeilzeitInsgesamt');
		result.berufsbildendTeilzeitInsgesamt = obj.berufsbildendTeilzeitInsgesamt;
		if (obj.berufsbildendTeilzeitSchwerstbehindert === undefined)
			throw new Error('invalid json format, missing attribute berufsbildendTeilzeitSchwerstbehindert');
		result.berufsbildendTeilzeitSchwerstbehindert = obj.berufsbildendTeilzeitSchwerstbehindert;
		if (obj.berufsbildendVollzeitInsgesamt === undefined)
			throw new Error('invalid json format, missing attribute berufsbildendVollzeitInsgesamt');
		result.berufsbildendVollzeitInsgesamt = obj.berufsbildendVollzeitInsgesamt;
		if (obj.berufsbildendVollzeitSchwerstbehindert === undefined)
			throw new Error('invalid json format, missing attribute berufsbildendVollzeitSchwerstbehindert');
		result.berufsbildendVollzeitSchwerstbehindert = obj.berufsbildendVollzeitSchwerstbehindert;
		return result;
	}

	public static transpilerToJSON(obj: KlinikschuleStatistikExport): string {
		let result = '{';
		result += '"allgemeinbildendInsgesamt" : ' + obj.allgemeinbildendInsgesamt.toString() + ',';
		result += '"allgemeinbildendSchwerstbehindert" : ' + obj.allgemeinbildendSchwerstbehindert.toString() + ',';
		result += '"berufsbildendTeilzeitInsgesamt" : ' + obj.berufsbildendTeilzeitInsgesamt.toString() + ',';
		result += '"berufsbildendTeilzeitSchwerstbehindert" : ' + obj.berufsbildendTeilzeitSchwerstbehindert.toString() + ',';
		result += '"berufsbildendVollzeitInsgesamt" : ' + obj.berufsbildendVollzeitInsgesamt.toString() + ',';
		result += '"berufsbildendVollzeitSchwerstbehindert" : ' + obj.berufsbildendVollzeitSchwerstbehindert.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<KlinikschuleStatistikExport>): string {
		let result = '{';
		if (obj.allgemeinbildendInsgesamt !== undefined) {
			result += '"allgemeinbildendInsgesamt" : ' + obj.allgemeinbildendInsgesamt.toString() + ',';
		}
		if (obj.allgemeinbildendSchwerstbehindert !== undefined) {
			result += '"allgemeinbildendSchwerstbehindert" : ' + obj.allgemeinbildendSchwerstbehindert.toString() + ',';
		}
		if (obj.berufsbildendTeilzeitInsgesamt !== undefined) {
			result += '"berufsbildendTeilzeitInsgesamt" : ' + obj.berufsbildendTeilzeitInsgesamt.toString() + ',';
		}
		if (obj.berufsbildendTeilzeitSchwerstbehindert !== undefined) {
			result += '"berufsbildendTeilzeitSchwerstbehindert" : ' + obj.berufsbildendTeilzeitSchwerstbehindert.toString() + ',';
		}
		if (obj.berufsbildendVollzeitInsgesamt !== undefined) {
			result += '"berufsbildendVollzeitInsgesamt" : ' + obj.berufsbildendVollzeitInsgesamt.toString() + ',';
		}
		if (obj.berufsbildendVollzeitSchwerstbehindert !== undefined) {
			result += '"berufsbildendVollzeitSchwerstbehindert" : ' + obj.berufsbildendVollzeitSchwerstbehindert.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_KlinikschuleStatistikExport(obj: unknown): KlinikschuleStatistikExport {
	return obj as KlinikschuleStatistikExport;
}
