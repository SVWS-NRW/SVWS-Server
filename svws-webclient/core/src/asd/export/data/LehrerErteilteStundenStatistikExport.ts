import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class LehrerErteilteStundenStatistikExport extends JavaObject {

	/**
	 * Satzschlüssel: Bereich in dem die Stunden erteilt werden.
	 */
	public bereich: string = "";

	/**
	 * Erteilte Stunden zum Bereich
	 */
	public erteilteStunden: number = 0.0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.LehrerErteilteStundenStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.LehrerErteilteStundenStatistikExport'].includes(name);
	}

	public static readonly class = new Class<LehrerErteilteStundenStatistikExport>('de.svws_nrw.asd.export.data.LehrerErteilteStundenStatistikExport');

	public static transpilerFromJSON(json: string): LehrerErteilteStundenStatistikExport {
		const obj = JSON.parse(json) as Partial<LehrerErteilteStundenStatistikExport>;
		const result = new LehrerErteilteStundenStatistikExport();
		if (obj.bereich === undefined)
			throw new Error('invalid json format, missing attribute bereich');
		result.bereich = obj.bereich;
		if (obj.erteilteStunden === undefined)
			throw new Error('invalid json format, missing attribute erteilteStunden');
		result.erteilteStunden = obj.erteilteStunden;
		return result;
	}

	public static transpilerToJSON(obj: LehrerErteilteStundenStatistikExport): string {
		let result = '{';
		result += '"bereich" : ' + JSON.stringify(obj.bereich) + ',';
		result += '"erteilteStunden" : ' + obj.erteilteStunden.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<LehrerErteilteStundenStatistikExport>): string {
		let result = '{';
		if (obj.bereich !== undefined) {
			result += '"bereich" : ' + JSON.stringify(obj.bereich) + ',';
		}
		if (obj.erteilteStunden !== undefined) {
			result += '"erteilteStunden" : ' + obj.erteilteStunden.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_LehrerErteilteStundenStatistikExport(obj: unknown): LehrerErteilteStundenStatistikExport {
	return obj as LehrerErteilteStundenStatistikExport;
}
