import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class LehrerMehrleistungenStatistikExport extends JavaObject {

	/**
	 * Die Mehrleistungsstunden zur Mehrleistung.
	 */
	public mehrleistungsstunden: number = 0.0;

	/**
	 * Satzschlüssel: Die Mehrleistung.
	 */
	public grund: string = "";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.LehrerMehrleistungenStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.LehrerMehrleistungenStatistikExport'].includes(name);
	}

	public static readonly class = new Class<LehrerMehrleistungenStatistikExport>('de.svws_nrw.asd.export.data.LehrerMehrleistungenStatistikExport');

	public static transpilerFromJSON(json: string): LehrerMehrleistungenStatistikExport {
		const obj = JSON.parse(json) as Partial<LehrerMehrleistungenStatistikExport>;
		const result = new LehrerMehrleistungenStatistikExport();
		if (obj.mehrleistungsstunden === undefined)
			throw new Error('invalid json format, missing attribute mehrleistungsstunden');
		result.mehrleistungsstunden = obj.mehrleistungsstunden;
		if (obj.grund === undefined)
			throw new Error('invalid json format, missing attribute grund');
		result.grund = obj.grund;
		return result;
	}

	public static transpilerToJSON(obj: LehrerMehrleistungenStatistikExport): string {
		let result = '{';
		result += '"mehrleistungsstunden" : ' + obj.mehrleistungsstunden.toString() + ',';
		result += '"grund" : ' + JSON.stringify(obj.grund) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<LehrerMehrleistungenStatistikExport>): string {
		let result = '{';
		if (obj.mehrleistungsstunden !== undefined) {
			result += '"mehrleistungsstunden" : ' + obj.mehrleistungsstunden.toString() + ',';
		}
		if (obj.grund !== undefined) {
			result += '"grund" : ' + JSON.stringify(obj.grund) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_LehrerMehrleistungenStatistikExport(obj: unknown): LehrerMehrleistungenStatistikExport {
	return obj as LehrerMehrleistungenStatistikExport;
}
