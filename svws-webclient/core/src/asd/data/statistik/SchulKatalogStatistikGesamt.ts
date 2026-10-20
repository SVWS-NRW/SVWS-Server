import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchulKatalogStatistikGesamt extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id: number = -1;

	/**
	 * Eine Kurzbezeichnung für die Schule.
	 */
	public kurzbezeichnung: string | null = null;

	/**
	 * Die Statistik-Schulnummer der Schule
	 */
	public schulnummerStatistik: string | null = null;

	/**
	 * Der Name des Schule.
	 */
	public name: string = "";

	/**
	 * Die ID der Schulform.
	 */
	public idSchulform: number | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.statistik.SchulKatalogStatistikGesamt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.statistik.SchulKatalogStatistikGesamt'].includes(name);
	}

	public static readonly class = new Class<SchulKatalogStatistikGesamt>('de.svws_nrw.asd.data.statistik.SchulKatalogStatistikGesamt');

	public static transpilerFromJSON(json: string): SchulKatalogStatistikGesamt {
		const obj = JSON.parse(json) as Partial<SchulKatalogStatistikGesamt>;
		const result = new SchulKatalogStatistikGesamt();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kurzbezeichnung = (obj.kurzbezeichnung === undefined) ? null : obj.kurzbezeichnung === null ? null : obj.kurzbezeichnung;
		result.schulnummerStatistik = (obj.schulnummerStatistik === undefined) ? null : obj.schulnummerStatistik === null ? null : obj.schulnummerStatistik;
		if (obj.name === undefined)
			throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		result.idSchulform = (obj.idSchulform === undefined) ? null : obj.idSchulform === null ? null : obj.idSchulform;
		return result;
	}

	public static transpilerToJSON(obj: SchulKatalogStatistikGesamt): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kurzbezeichnung" : ' + ((obj.kurzbezeichnung === null) ? 'null' : JSON.stringify(obj.kurzbezeichnung)) + ',';
		result += '"schulnummerStatistik" : ' + ((obj.schulnummerStatistik === null) ? 'null' : JSON.stringify(obj.schulnummerStatistik)) + ',';
		result += '"name" : ' + JSON.stringify(obj.name) + ',';
		result += '"idSchulform" : ' + ((obj.idSchulform === null) ? 'null' : obj.idSchulform.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchulKatalogStatistikGesamt>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kurzbezeichnung !== undefined) {
			result += '"kurzbezeichnung" : ' + ((obj.kurzbezeichnung === null) ? 'null' : JSON.stringify(obj.kurzbezeichnung)) + ',';
		}
		if (obj.schulnummerStatistik !== undefined) {
			result += '"schulnummerStatistik" : ' + ((obj.schulnummerStatistik === null) ? 'null' : JSON.stringify(obj.schulnummerStatistik)) + ',';
		}
		if (obj.name !== undefined) {
			result += '"name" : ' + JSON.stringify(obj.name) + ',';
		}
		if (obj.idSchulform !== undefined) {
			result += '"idSchulform" : ' + ((obj.idSchulform === null) ? 'null' : obj.idSchulform.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_statistik_SchulKatalogStatistikGesamt(obj: unknown): SchulKatalogStatistikGesamt {
	return obj as SchulKatalogStatistikGesamt;
}
