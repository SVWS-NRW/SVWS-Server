import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class KlassenAltersstrukturStatistikExport extends JavaObject {

	/**
	 * Satzschlüssel: Das Geburtsjahr der Schüler.
	 */
	public geburtsjahr: string = "";

	/**
	 * Satzschlüssel: Die Nationalität der Schüler.
	 */
	public nationalitaet: string = "";

	/**
	 * Die Schüler des Altersstruktursatzes insgesamt.
	 */
	public schuelerInsgesamt: number = 0;

	/**
	 * Die Schüler des Altersstruktursatzes weiblich.
	 */
	public schuelerWeiblich: number = 0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.KlassenAltersstrukturStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.KlassenAltersstrukturStatistikExport'].includes(name);
	}

	public static readonly class = new Class<KlassenAltersstrukturStatistikExport>('de.svws_nrw.asd.export.data.KlassenAltersstrukturStatistikExport');

	public static transpilerFromJSON(json: string): KlassenAltersstrukturStatistikExport {
		const obj = JSON.parse(json) as Partial<KlassenAltersstrukturStatistikExport>;
		const result = new KlassenAltersstrukturStatistikExport();
		if (obj.geburtsjahr === undefined)
			throw new Error('invalid json format, missing attribute geburtsjahr');
		result.geburtsjahr = obj.geburtsjahr;
		if (obj.nationalitaet === undefined)
			throw new Error('invalid json format, missing attribute nationalitaet');
		result.nationalitaet = obj.nationalitaet;
		if (obj.schuelerInsgesamt === undefined)
			throw new Error('invalid json format, missing attribute schuelerInsgesamt');
		result.schuelerInsgesamt = obj.schuelerInsgesamt;
		if (obj.schuelerWeiblich === undefined)
			throw new Error('invalid json format, missing attribute schuelerWeiblich');
		result.schuelerWeiblich = obj.schuelerWeiblich;
		return result;
	}

	public static transpilerToJSON(obj: KlassenAltersstrukturStatistikExport): string {
		let result = '{';
		result += '"geburtsjahr" : ' + JSON.stringify(obj.geburtsjahr) + ',';
		result += '"nationalitaet" : ' + JSON.stringify(obj.nationalitaet) + ',';
		result += '"schuelerInsgesamt" : ' + obj.schuelerInsgesamt.toString() + ',';
		result += '"schuelerWeiblich" : ' + obj.schuelerWeiblich.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<KlassenAltersstrukturStatistikExport>): string {
		let result = '{';
		if (obj.geburtsjahr !== undefined) {
			result += '"geburtsjahr" : ' + JSON.stringify(obj.geburtsjahr) + ',';
		}
		if (obj.nationalitaet !== undefined) {
			result += '"nationalitaet" : ' + JSON.stringify(obj.nationalitaet) + ',';
		}
		if (obj.schuelerInsgesamt !== undefined) {
			result += '"schuelerInsgesamt" : ' + obj.schuelerInsgesamt.toString() + ',';
		}
		if (obj.schuelerWeiblich !== undefined) {
			result += '"schuelerWeiblich" : ' + obj.schuelerWeiblich.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_KlassenAltersstrukturStatistikExport(obj: unknown): KlassenAltersstrukturStatistikExport {
	return obj as KlassenAltersstrukturStatistikExport;
}
