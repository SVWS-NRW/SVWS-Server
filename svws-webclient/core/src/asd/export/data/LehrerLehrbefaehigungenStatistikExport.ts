import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class LehrerLehrbefaehigungenStatistikExport extends JavaObject {

	/**
	 * Satzschlüssel: Eine Lehrbefähigung eines Lehrers.
	 */
	public lehrbefaehigung: string = "";

	/**
	 * Die Qualifikation zu der Lehrbefähigung.
	 */
	public qualifikation: string = "";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.LehrerLehrbefaehigungenStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.LehrerLehrbefaehigungenStatistikExport'].includes(name);
	}

	public static readonly class = new Class<LehrerLehrbefaehigungenStatistikExport>('de.svws_nrw.asd.export.data.LehrerLehrbefaehigungenStatistikExport');

	public static transpilerFromJSON(json: string): LehrerLehrbefaehigungenStatistikExport {
		const obj = JSON.parse(json) as Partial<LehrerLehrbefaehigungenStatistikExport>;
		const result = new LehrerLehrbefaehigungenStatistikExport();
		if (obj.lehrbefaehigung === undefined)
			throw new Error('invalid json format, missing attribute lehrbefaehigung');
		result.lehrbefaehigung = obj.lehrbefaehigung;
		if (obj.qualifikation === undefined)
			throw new Error('invalid json format, missing attribute qualifikation');
		result.qualifikation = obj.qualifikation;
		return result;
	}

	public static transpilerToJSON(obj: LehrerLehrbefaehigungenStatistikExport): string {
		let result = '{';
		result += '"lehrbefaehigung" : ' + JSON.stringify(obj.lehrbefaehigung) + ',';
		result += '"qualifikation" : ' + JSON.stringify(obj.qualifikation) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<LehrerLehrbefaehigungenStatistikExport>): string {
		let result = '{';
		if (obj.lehrbefaehigung !== undefined) {
			result += '"lehrbefaehigung" : ' + JSON.stringify(obj.lehrbefaehigung) + ',';
		}
		if (obj.qualifikation !== undefined) {
			result += '"qualifikation" : ' + JSON.stringify(obj.qualifikation) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_LehrerLehrbefaehigungenStatistikExport(obj: unknown): LehrerLehrbefaehigungenStatistikExport {
	return obj as LehrerLehrbefaehigungenStatistikExport;
}
