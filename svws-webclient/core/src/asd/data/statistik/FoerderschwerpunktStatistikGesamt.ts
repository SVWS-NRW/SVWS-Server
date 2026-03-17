import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class FoerderschwerpunktStatistikGesamt extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id: number = 0;

	/**
	 * Die ID des Eintrags im Rahmen der amtlichen Schulstatisik.
	 */
	public idKatalog: number = -1;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.statistik.FoerderschwerpunktStatistikGesamt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.statistik.FoerderschwerpunktStatistikGesamt'].includes(name);
	}

	public static readonly class = new Class<FoerderschwerpunktStatistikGesamt>('de.svws_nrw.asd.data.statistik.FoerderschwerpunktStatistikGesamt');

	public static transpilerFromJSON(json: string): FoerderschwerpunktStatistikGesamt {
		const obj = JSON.parse(json) as Partial<FoerderschwerpunktStatistikGesamt>;
		const result = new FoerderschwerpunktStatistikGesamt();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idKatalog === undefined)
			throw new Error('invalid json format, missing attribute idKatalog');
		result.idKatalog = obj.idKatalog;
		return result;
	}

	public static transpilerToJSON(obj: FoerderschwerpunktStatistikGesamt): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idKatalog" : ' + obj.idKatalog.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<FoerderschwerpunktStatistikGesamt>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idKatalog !== undefined) {
			result += '"idKatalog" : ' + obj.idKatalog.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_statistik_FoerderschwerpunktStatistikGesamt(obj: unknown): FoerderschwerpunktStatistikGesamt {
	return obj as FoerderschwerpunktStatistikGesamt;
}
