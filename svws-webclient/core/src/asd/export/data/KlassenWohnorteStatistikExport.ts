import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class KlassenWohnorteStatistikExport extends JavaObject {

	/**
	 * Satzschlüssel: Die Postleitzahl.
	 */
	public postleitzahl: string = "";

	/**
	 * Satzschlüssel: Der amtliche Gemeindeschlüssel.
	 */
	public gemeindeschluessel: string = "";

	/**
	 * Die Schüler des Wohnortsatzes insgesamt.
	 */
	public schuelerInsgesamt: number = 0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.KlassenWohnorteStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.KlassenWohnorteStatistikExport'].includes(name);
	}

	public static readonly class = new Class<KlassenWohnorteStatistikExport>('de.svws_nrw.asd.export.data.KlassenWohnorteStatistikExport');

	public static transpilerFromJSON(json: string): KlassenWohnorteStatistikExport {
		const obj = JSON.parse(json) as Partial<KlassenWohnorteStatistikExport>;
		const result = new KlassenWohnorteStatistikExport();
		if (obj.postleitzahl === undefined)
			throw new Error('invalid json format, missing attribute postleitzahl');
		result.postleitzahl = obj.postleitzahl;
		if (obj.gemeindeschluessel === undefined)
			throw new Error('invalid json format, missing attribute gemeindeschluessel');
		result.gemeindeschluessel = obj.gemeindeschluessel;
		if (obj.schuelerInsgesamt === undefined)
			throw new Error('invalid json format, missing attribute schuelerInsgesamt');
		result.schuelerInsgesamt = obj.schuelerInsgesamt;
		return result;
	}

	public static transpilerToJSON(obj: KlassenWohnorteStatistikExport): string {
		let result = '{';
		result += '"postleitzahl" : ' + JSON.stringify(obj.postleitzahl) + ',';
		result += '"gemeindeschluessel" : ' + JSON.stringify(obj.gemeindeschluessel) + ',';
		result += '"schuelerInsgesamt" : ' + obj.schuelerInsgesamt.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<KlassenWohnorteStatistikExport>): string {
		let result = '{';
		if (obj.postleitzahl !== undefined) {
			result += '"postleitzahl" : ' + JSON.stringify(obj.postleitzahl) + ',';
		}
		if (obj.gemeindeschluessel !== undefined) {
			result += '"gemeindeschluessel" : ' + JSON.stringify(obj.gemeindeschluessel) + ',';
		}
		if (obj.schuelerInsgesamt !== undefined) {
			result += '"schuelerInsgesamt" : ' + obj.schuelerInsgesamt.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_KlassenWohnorteStatistikExport(obj: unknown): KlassenWohnorteStatistikExport {
	return obj as KlassenWohnorteStatistikExport;
}
