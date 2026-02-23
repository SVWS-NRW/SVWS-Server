import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class KlassenAusbildungsorteStatistikExport extends JavaObject {

	/**
	 * Satzschlüssel: Der amtliche Gemeindeschlüssel.
	 */
	public gemeindeschluessel: string = "";

	/**
	 * Die Schüler des Ausbildungsortsatzes insgesamt.
	 */
	public schuelerInsgesamt: number = 0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.KlassenAusbildungsorteStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.KlassenAusbildungsorteStatistikExport'].includes(name);
	}

	public static readonly class = new Class<KlassenAusbildungsorteStatistikExport>('de.svws_nrw.asd.export.data.KlassenAusbildungsorteStatistikExport');

	public static transpilerFromJSON(json: string): KlassenAusbildungsorteStatistikExport {
		const obj = JSON.parse(json) as Partial<KlassenAusbildungsorteStatistikExport>;
		const result = new KlassenAusbildungsorteStatistikExport();
		if (obj.gemeindeschluessel === undefined)
			throw new Error('invalid json format, missing attribute gemeindeschluessel');
		result.gemeindeschluessel = obj.gemeindeschluessel;
		if (obj.schuelerInsgesamt === undefined)
			throw new Error('invalid json format, missing attribute schuelerInsgesamt');
		result.schuelerInsgesamt = obj.schuelerInsgesamt;
		return result;
	}

	public static transpilerToJSON(obj: KlassenAusbildungsorteStatistikExport): string {
		let result = '{';
		result += '"gemeindeschluessel" : ' + JSON.stringify(obj.gemeindeschluessel) + ',';
		result += '"schuelerInsgesamt" : ' + obj.schuelerInsgesamt.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<KlassenAusbildungsorteStatistikExport>): string {
		let result = '{';
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

export function cast_de_svws_nrw_asd_export_data_KlassenAusbildungsorteStatistikExport(obj: unknown): KlassenAusbildungsorteStatistikExport {
	return obj as KlassenAusbildungsorteStatistikExport;
}
