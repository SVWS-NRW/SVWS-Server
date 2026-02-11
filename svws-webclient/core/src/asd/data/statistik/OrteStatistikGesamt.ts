import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class OrteStatistikGesamt extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id: number = 0;

	/**
	 * Die Postleitzahl.
	 */
	public plz: string | null = null;

	/**
	 * Der Name des Ortes.
	 */
	public ortsname: string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.statistik.OrteStatistikGesamt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.statistik.OrteStatistikGesamt'].includes(name);
	}

	public static readonly class = new Class<OrteStatistikGesamt>('de.svws_nrw.asd.data.statistik.OrteStatistikGesamt');

	public static transpilerFromJSON(json: string): OrteStatistikGesamt {
		const obj = JSON.parse(json) as Partial<OrteStatistikGesamt>;
		const result = new OrteStatistikGesamt();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.plz = (obj.plz === undefined) ? null : obj.plz === null ? null : obj.plz;
		result.ortsname = (obj.ortsname === undefined) ? null : obj.ortsname === null ? null : obj.ortsname;
		return result;
	}

	public static transpilerToJSON(obj: OrteStatistikGesamt): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"plz" : ' + ((obj.plz === null) ? 'null' : JSON.stringify(obj.plz)) + ',';
		result += '"ortsname" : ' + ((obj.ortsname === null) ? 'null' : JSON.stringify(obj.ortsname)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<OrteStatistikGesamt>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.plz !== undefined) {
			result += '"plz" : ' + ((obj.plz === null) ? 'null' : JSON.stringify(obj.plz)) + ',';
		}
		if (obj.ortsname !== undefined) {
			result += '"ortsname" : ' + ((obj.ortsname === null) ? 'null' : JSON.stringify(obj.ortsname)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_statistik_OrteStatistikGesamt(obj: unknown): OrteStatistikGesamt {
	return obj as OrteStatistikGesamt;
}
