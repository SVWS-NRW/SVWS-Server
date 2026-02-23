import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class LehrerAnrechungenStatistikExport extends JavaObject {

	/**
	 * Die Anrechungsstunden zum Anrechnungsgrund.
	 */
	public anrechungsstunden: number = 0.0;

	/**
	 * Satzschlüssel: Die nicht unterrichtliche Tätigkeit bzw. der Anrechnungsgrund.
	 */
	public grund: string = "";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.LehrerAnrechungenStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.LehrerAnrechungenStatistikExport'].includes(name);
	}

	public static readonly class = new Class<LehrerAnrechungenStatistikExport>('de.svws_nrw.asd.export.data.LehrerAnrechungenStatistikExport');

	public static transpilerFromJSON(json: string): LehrerAnrechungenStatistikExport {
		const obj = JSON.parse(json) as Partial<LehrerAnrechungenStatistikExport>;
		const result = new LehrerAnrechungenStatistikExport();
		if (obj.anrechungsstunden === undefined)
			throw new Error('invalid json format, missing attribute anrechungsstunden');
		result.anrechungsstunden = obj.anrechungsstunden;
		if (obj.grund === undefined)
			throw new Error('invalid json format, missing attribute grund');
		result.grund = obj.grund;
		return result;
	}

	public static transpilerToJSON(obj: LehrerAnrechungenStatistikExport): string {
		let result = '{';
		result += '"anrechungsstunden" : ' + obj.anrechungsstunden.toString() + ',';
		result += '"grund" : ' + JSON.stringify(obj.grund) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<LehrerAnrechungenStatistikExport>): string {
		let result = '{';
		if (obj.anrechungsstunden !== undefined) {
			result += '"anrechungsstunden" : ' + obj.anrechungsstunden.toString() + ',';
		}
		if (obj.grund !== undefined) {
			result += '"grund" : ' + JSON.stringify(obj.grund) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_LehrerAnrechungenStatistikExport(obj: unknown): LehrerAnrechungenStatistikExport {
	return obj as LehrerAnrechungenStatistikExport;
}
