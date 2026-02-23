import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class KlassenNationalitaetenStatistikExport extends JavaObject {

	/**
	 * Satzschlüssel: Die Nationalitaet.
	 */
	public nationalitaet: string = "";

	/**
	 * Die Schüler der Nationalität insgesamt.
	 */
	public insgesamtZusammen: number = 0;

	/**
	 * Die Schüler der Nationalität insgesamt weiblich.
	 */
	public insgesamtWeiblich: number = 0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.KlassenNationalitaetenStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.KlassenNationalitaetenStatistikExport'].includes(name);
	}

	public static readonly class = new Class<KlassenNationalitaetenStatistikExport>('de.svws_nrw.asd.export.data.KlassenNationalitaetenStatistikExport');

	public static transpilerFromJSON(json: string): KlassenNationalitaetenStatistikExport {
		const obj = JSON.parse(json) as Partial<KlassenNationalitaetenStatistikExport>;
		const result = new KlassenNationalitaetenStatistikExport();
		if (obj.nationalitaet === undefined)
			throw new Error('invalid json format, missing attribute nationalitaet');
		result.nationalitaet = obj.nationalitaet;
		if (obj.insgesamtZusammen === undefined)
			throw new Error('invalid json format, missing attribute insgesamtZusammen');
		result.insgesamtZusammen = obj.insgesamtZusammen;
		if (obj.insgesamtWeiblich === undefined)
			throw new Error('invalid json format, missing attribute insgesamtWeiblich');
		result.insgesamtWeiblich = obj.insgesamtWeiblich;
		return result;
	}

	public static transpilerToJSON(obj: KlassenNationalitaetenStatistikExport): string {
		let result = '{';
		result += '"nationalitaet" : ' + JSON.stringify(obj.nationalitaet) + ',';
		result += '"insgesamtZusammen" : ' + obj.insgesamtZusammen.toString() + ',';
		result += '"insgesamtWeiblich" : ' + obj.insgesamtWeiblich.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<KlassenNationalitaetenStatistikExport>): string {
		let result = '{';
		if (obj.nationalitaet !== undefined) {
			result += '"nationalitaet" : ' + JSON.stringify(obj.nationalitaet) + ',';
		}
		if (obj.insgesamtZusammen !== undefined) {
			result += '"insgesamtZusammen" : ' + obj.insgesamtZusammen.toString() + ',';
		}
		if (obj.insgesamtWeiblich !== undefined) {
			result += '"insgesamtWeiblich" : ' + obj.insgesamtWeiblich.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_KlassenNationalitaetenStatistikExport(obj: unknown): KlassenNationalitaetenStatistikExport {
	return obj as KlassenNationalitaetenStatistikExport;
}
