import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class ReligionStatistikGesamt extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id: number = -1;

	/**
	 * Die ID des Eintrages für die Statistik.
	 */
	public idKatalog: number = -1;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.statistik.ReligionStatistikGesamt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.statistik.ReligionStatistikGesamt'].includes(name);
	}

	public static readonly class = new Class<ReligionStatistikGesamt>('de.svws_nrw.asd.data.statistik.ReligionStatistikGesamt');

	public static transpilerFromJSON(json: string): ReligionStatistikGesamt {
		const obj = JSON.parse(json) as Partial<ReligionStatistikGesamt>;
		const result = new ReligionStatistikGesamt();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idKatalog === undefined)
			throw new Error('invalid json format, missing attribute idKatalog');
		result.idKatalog = obj.idKatalog;
		return result;
	}

	public static transpilerToJSON(obj: ReligionStatistikGesamt): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idKatalog" : ' + obj.idKatalog.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ReligionStatistikGesamt>): string {
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

export function cast_de_svws_nrw_asd_data_statistik_ReligionStatistikGesamt(obj: unknown): ReligionStatistikGesamt {
	return obj as ReligionStatistikGesamt;
}
