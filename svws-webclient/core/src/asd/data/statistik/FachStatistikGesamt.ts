import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class FachStatistikGesamt extends JavaObject {

	/**
	 * Die ID des Fachs.
	 */
	public id: number = -1;

	/**
	 * Das eindeutige Kürzel des Fachs
	 */
	public kuerzel: string = "";

	/**
	 * Das Statistik-Kürzel des Fachs
	 */
	public kuerzelStatistik: string = "";

	/**
	 * Die Sprache in der das Fach unterrichtet wird, sofern es sich um ein bilinguales Sachfach handelt.
	 */
	public bilingualeSprache: string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.statistik.FachStatistikGesamt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.statistik.FachStatistikGesamt'].includes(name);
	}

	public static readonly class = new Class<FachStatistikGesamt>('de.svws_nrw.asd.data.statistik.FachStatistikGesamt');

	public static transpilerFromJSON(json: string): FachStatistikGesamt {
		const obj = JSON.parse(json) as Partial<FachStatistikGesamt>;
		const result = new FachStatistikGesamt();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.kuerzelStatistik === undefined)
			throw new Error('invalid json format, missing attribute kuerzelStatistik');
		result.kuerzelStatistik = obj.kuerzelStatistik;
		result.bilingualeSprache = (obj.bilingualeSprache === undefined) ? null : obj.bilingualeSprache === null ? null : obj.bilingualeSprache;
		return result;
	}

	public static transpilerToJSON(obj: FachStatistikGesamt): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"kuerzelStatistik" : ' + JSON.stringify(obj.kuerzelStatistik) + ',';
		result += '"bilingualeSprache" : ' + ((obj.bilingualeSprache === null) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<FachStatistikGesamt>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.kuerzelStatistik !== undefined) {
			result += '"kuerzelStatistik" : ' + JSON.stringify(obj.kuerzelStatistik) + ',';
		}
		if (obj.bilingualeSprache !== undefined) {
			result += '"bilingualeSprache" : ' + ((obj.bilingualeSprache === null) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_statistik_FachStatistikGesamt(obj: unknown): FachStatistikGesamt {
	return obj as FachStatistikGesamt;
}
