import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class KlassenBetreuungStatistikExport extends JavaObject {

	/**
	 * Die Art der Betreuung
	 */
	public betreuungsart: string | null = "";

	/**
	 * Die Schüler Insgesamt.
	 */
	public insgesamtZusammen: number = 0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.KlassenBetreuungStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.KlassenBetreuungStatistikExport'].includes(name);
	}

	public static readonly class = new Class<KlassenBetreuungStatistikExport>('de.svws_nrw.asd.export.data.KlassenBetreuungStatistikExport');

	public static transpilerFromJSON(json: string): KlassenBetreuungStatistikExport {
		const obj = JSON.parse(json) as Partial<KlassenBetreuungStatistikExport>;
		const result = new KlassenBetreuungStatistikExport();
		result.betreuungsart = (obj.betreuungsart === undefined) ? null : obj.betreuungsart === null ? null : obj.betreuungsart;
		if (obj.insgesamtZusammen === undefined)
			throw new Error('invalid json format, missing attribute insgesamtZusammen');
		result.insgesamtZusammen = obj.insgesamtZusammen;
		return result;
	}

	public static transpilerToJSON(obj: KlassenBetreuungStatistikExport): string {
		let result = '{';
		result += '"betreuungsart" : ' + ((obj.betreuungsart === null) ? 'null' : JSON.stringify(obj.betreuungsart)) + ',';
		result += '"insgesamtZusammen" : ' + obj.insgesamtZusammen.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<KlassenBetreuungStatistikExport>): string {
		let result = '{';
		if (obj.betreuungsart !== undefined) {
			result += '"betreuungsart" : ' + ((obj.betreuungsart === null) ? 'null' : JSON.stringify(obj.betreuungsart)) + ',';
		}
		if (obj.insgesamtZusammen !== undefined) {
			result += '"insgesamtZusammen" : ' + obj.insgesamtZusammen.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_KlassenBetreuungStatistikExport(obj: unknown): KlassenBetreuungStatistikExport {
	return obj as KlassenBetreuungStatistikExport;
}
