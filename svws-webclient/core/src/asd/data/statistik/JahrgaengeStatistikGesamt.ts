import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class JahrgaengeStatistikGesamt extends JavaObject {

	/**
	 * Die ID des Jahrgangs.
	 */
	public id: number = 0;

	/**
	 * Das schulinterne Kürzel des Jahrgangs.
	 */
	public kuerzel: string | null = null;

	/**
	 * Das dem Jahrgang zugeordnete Statistik-Kürzel.
	 */
	public kuerzelStatistik: string | null = null;

	/**
	 * Die Sortierreihenfolge des Jahrgangslisten-Eintrags.
	 */
	public sortierung: number = 0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.statistik.JahrgaengeStatistikGesamt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.statistik.JahrgaengeStatistikGesamt'].includes(name);
	}

	public static readonly class = new Class<JahrgaengeStatistikGesamt>('de.svws_nrw.asd.data.statistik.JahrgaengeStatistikGesamt');

	public static transpilerFromJSON(json: string): JahrgaengeStatistikGesamt {
		const obj = JSON.parse(json) as Partial<JahrgaengeStatistikGesamt>;
		const result = new JahrgaengeStatistikGesamt();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = (obj.kuerzel === undefined) ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.kuerzelStatistik = (obj.kuerzelStatistik === undefined) ? null : obj.kuerzelStatistik === null ? null : obj.kuerzelStatistik;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		return result;
	}

	public static transpilerToJSON(obj: JahrgaengeStatistikGesamt): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"kuerzelStatistik" : ' + ((obj.kuerzelStatistik === null) ? 'null' : JSON.stringify(obj.kuerzelStatistik)) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<JahrgaengeStatistikGesamt>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		}
		if (obj.kuerzelStatistik !== undefined) {
			result += '"kuerzelStatistik" : ' + ((obj.kuerzelStatistik === null) ? 'null' : JSON.stringify(obj.kuerzelStatistik)) + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_statistik_JahrgaengeStatistikGesamt(obj: unknown): JahrgaengeStatistikGesamt {
	return obj as JahrgaengeStatistikGesamt;
}
