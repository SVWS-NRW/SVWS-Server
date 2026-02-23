import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class LehrerMinderleistungenStatistikExport extends JavaObject {

	/**
	 * Die Minderleistungsstunden zur Minderleistung.
	 */
	public minderleistungsstunden: number = 0.0;

	/**
	 * Satzschlüssel: Die Minderleistung.
	 */
	public grund: string = "";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.LehrerMinderleistungenStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.LehrerMinderleistungenStatistikExport'].includes(name);
	}

	public static readonly class = new Class<LehrerMinderleistungenStatistikExport>('de.svws_nrw.asd.export.data.LehrerMinderleistungenStatistikExport');

	public static transpilerFromJSON(json: string): LehrerMinderleistungenStatistikExport {
		const obj = JSON.parse(json) as Partial<LehrerMinderleistungenStatistikExport>;
		const result = new LehrerMinderleistungenStatistikExport();
		if (obj.minderleistungsstunden === undefined)
			throw new Error('invalid json format, missing attribute minderleistungsstunden');
		result.minderleistungsstunden = obj.minderleistungsstunden;
		if (obj.grund === undefined)
			throw new Error('invalid json format, missing attribute grund');
		result.grund = obj.grund;
		return result;
	}

	public static transpilerToJSON(obj: LehrerMinderleistungenStatistikExport): string {
		let result = '{';
		result += '"minderleistungsstunden" : ' + obj.minderleistungsstunden.toString() + ',';
		result += '"grund" : ' + JSON.stringify(obj.grund) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<LehrerMinderleistungenStatistikExport>): string {
		let result = '{';
		if (obj.minderleistungsstunden !== undefined) {
			result += '"minderleistungsstunden" : ' + obj.minderleistungsstunden.toString() + ',';
		}
		if (obj.grund !== undefined) {
			result += '"grund" : ' + JSON.stringify(obj.grund) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_LehrerMinderleistungenStatistikExport(obj: unknown): LehrerMinderleistungenStatistikExport {
	return obj as LehrerMinderleistungenStatistikExport;
}
