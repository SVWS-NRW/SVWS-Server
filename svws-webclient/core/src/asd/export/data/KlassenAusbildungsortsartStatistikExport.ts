import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class KlassenAusbildungsortsartStatistikExport extends JavaObject {

	/**
	 * Ausbildungsort Betrieb.
	 */
	public ausbildungsortBetrieb: number = 0;

	/**
	 * Ausbildungsort Träger.
	 */
	public ausbildungsortTraeger: number = 0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.KlassenAusbildungsortsartStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.KlassenAusbildungsortsartStatistikExport'].includes(name);
	}

	public static readonly class = new Class<KlassenAusbildungsortsartStatistikExport>('de.svws_nrw.asd.export.data.KlassenAusbildungsortsartStatistikExport');

	public static transpilerFromJSON(json: string): KlassenAusbildungsortsartStatistikExport {
		const obj = JSON.parse(json) as Partial<KlassenAusbildungsortsartStatistikExport>;
		const result = new KlassenAusbildungsortsartStatistikExport();
		if (obj.ausbildungsortBetrieb === undefined)
			throw new Error('invalid json format, missing attribute ausbildungsortBetrieb');
		result.ausbildungsortBetrieb = obj.ausbildungsortBetrieb;
		if (obj.ausbildungsortTraeger === undefined)
			throw new Error('invalid json format, missing attribute ausbildungsortTraeger');
		result.ausbildungsortTraeger = obj.ausbildungsortTraeger;
		return result;
	}

	public static transpilerToJSON(obj: KlassenAusbildungsortsartStatistikExport): string {
		let result = '{';
		result += '"ausbildungsortBetrieb" : ' + obj.ausbildungsortBetrieb.toString() + ',';
		result += '"ausbildungsortTraeger" : ' + obj.ausbildungsortTraeger.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<KlassenAusbildungsortsartStatistikExport>): string {
		let result = '{';
		if (obj.ausbildungsortBetrieb !== undefined) {
			result += '"ausbildungsortBetrieb" : ' + obj.ausbildungsortBetrieb.toString() + ',';
		}
		if (obj.ausbildungsortTraeger !== undefined) {
			result += '"ausbildungsortTraeger" : ' + obj.ausbildungsortTraeger.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_KlassenAusbildungsortsartStatistikExport(obj: unknown): KlassenAusbildungsortsartStatistikExport {
	return obj as KlassenAusbildungsortsartStatistikExport;
}
