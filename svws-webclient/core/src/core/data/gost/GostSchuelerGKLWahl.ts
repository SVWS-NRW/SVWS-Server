import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class GostSchuelerGKLWahl extends JavaObject {

	/**
	 * Die ID des Schülers
	 */
	public idSchueler: number = -1;

	/**
	 * Die ID der Klausurvorgabe, welche für den Gleichwertigen Komplexen Leistungsnachweis in der EF im Sprachlich-künstlerisch-literarischen Fachbereich gewählt wurde
	 */
	public idKlausurvorgabeEF_Sprachen: number | null = null;

	/**
	 * Die ID der Klausurvorgabe, welche für den Gleichwertigen Komplexen Leistungsnachweis in der EF im Gesellschaftswissenschaftlichen Bereich gewählt wurde
	 */
	public idKlausurvorgabeEF_GW: number | null = null;

	/**
	 * Die ID der Klausurvorgabe, welche für den Gleichwertigen Komplexen Leistungsnachweis in der EF im Mathematisch-Naturwissenschaftlichen Bereich gewählt wurde
	 */
	public idKlausurvorgabeEF_NW: number | null = null;

	/**
	 * Die ID der Klausurvorgabe, welche für den Gleichwertigen Komplexen Leistungsnachweis in der Q-Phase im Sprachlich-künstlerisch-literarischen Fachbereich gewählt wurde
	 */
	public idKlausurvorgabeQ_Sprachen: number | null = null;

	/**
	 * Die ID der Klausurvorgabe, welche für den Gleichwertigen Komplexen Leistungsnachweis in der Q-Phase im Gesellschaftswissenschaftlichen Bereich gewählt wurde
	 */
	public idKlausurvorgabeQ_GW: number | null = null;

	/**
	 * Die ID der Klausurvorgabe, welche für den Gleichwertigen Komplexen Leistungsnachweis in der Q-Phase im Mathematisch-Naturwissenschaftlichen Bereich gewählt wurde
	 */
	public idKlausurvorgabeQ_NW: number | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostSchuelerGKLWahl';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.gost.GostSchuelerGKLWahl'].includes(name);
	}

	public static readonly class = new Class<GostSchuelerGKLWahl>('de.svws_nrw.core.data.gost.GostSchuelerGKLWahl');

	public static transpilerFromJSON(json: string): GostSchuelerGKLWahl {
		const obj = JSON.parse(json) as Partial<GostSchuelerGKLWahl>;
		const result = new GostSchuelerGKLWahl();
		if (obj.idSchueler === undefined)
			throw new Error('invalid json format, missing attribute idSchueler');
		result.idSchueler = obj.idSchueler;
		result.idKlausurvorgabeEF_Sprachen = (obj.idKlausurvorgabeEF_Sprachen === undefined) ? null : obj.idKlausurvorgabeEF_Sprachen === null ? null : obj.idKlausurvorgabeEF_Sprachen;
		result.idKlausurvorgabeEF_GW = (obj.idKlausurvorgabeEF_GW === undefined) ? null : obj.idKlausurvorgabeEF_GW === null ? null : obj.idKlausurvorgabeEF_GW;
		result.idKlausurvorgabeEF_NW = (obj.idKlausurvorgabeEF_NW === undefined) ? null : obj.idKlausurvorgabeEF_NW === null ? null : obj.idKlausurvorgabeEF_NW;
		result.idKlausurvorgabeQ_Sprachen = (obj.idKlausurvorgabeQ_Sprachen === undefined) ? null : obj.idKlausurvorgabeQ_Sprachen === null ? null : obj.idKlausurvorgabeQ_Sprachen;
		result.idKlausurvorgabeQ_GW = (obj.idKlausurvorgabeQ_GW === undefined) ? null : obj.idKlausurvorgabeQ_GW === null ? null : obj.idKlausurvorgabeQ_GW;
		result.idKlausurvorgabeQ_NW = (obj.idKlausurvorgabeQ_NW === undefined) ? null : obj.idKlausurvorgabeQ_NW === null ? null : obj.idKlausurvorgabeQ_NW;
		return result;
	}

	public static transpilerToJSON(obj: GostSchuelerGKLWahl): string {
		let result = '{';
		result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		result += '"idKlausurvorgabeEF_Sprachen" : ' + ((obj.idKlausurvorgabeEF_Sprachen === null) ? 'null' : obj.idKlausurvorgabeEF_Sprachen.toString()) + ',';
		result += '"idKlausurvorgabeEF_GW" : ' + ((obj.idKlausurvorgabeEF_GW === null) ? 'null' : obj.idKlausurvorgabeEF_GW.toString()) + ',';
		result += '"idKlausurvorgabeEF_NW" : ' + ((obj.idKlausurvorgabeEF_NW === null) ? 'null' : obj.idKlausurvorgabeEF_NW.toString()) + ',';
		result += '"idKlausurvorgabeQ_Sprachen" : ' + ((obj.idKlausurvorgabeQ_Sprachen === null) ? 'null' : obj.idKlausurvorgabeQ_Sprachen.toString()) + ',';
		result += '"idKlausurvorgabeQ_GW" : ' + ((obj.idKlausurvorgabeQ_GW === null) ? 'null' : obj.idKlausurvorgabeQ_GW.toString()) + ',';
		result += '"idKlausurvorgabeQ_NW" : ' + ((obj.idKlausurvorgabeQ_NW === null) ? 'null' : obj.idKlausurvorgabeQ_NW.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<GostSchuelerGKLWahl>): string {
		let result = '{';
		if (obj.idSchueler !== undefined) {
			result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		}
		if (obj.idKlausurvorgabeEF_Sprachen !== undefined) {
			result += '"idKlausurvorgabeEF_Sprachen" : ' + ((obj.idKlausurvorgabeEF_Sprachen === null) ? 'null' : obj.idKlausurvorgabeEF_Sprachen.toString()) + ',';
		}
		if (obj.idKlausurvorgabeEF_GW !== undefined) {
			result += '"idKlausurvorgabeEF_GW" : ' + ((obj.idKlausurvorgabeEF_GW === null) ? 'null' : obj.idKlausurvorgabeEF_GW.toString()) + ',';
		}
		if (obj.idKlausurvorgabeEF_NW !== undefined) {
			result += '"idKlausurvorgabeEF_NW" : ' + ((obj.idKlausurvorgabeEF_NW === null) ? 'null' : obj.idKlausurvorgabeEF_NW.toString()) + ',';
		}
		if (obj.idKlausurvorgabeQ_Sprachen !== undefined) {
			result += '"idKlausurvorgabeQ_Sprachen" : ' + ((obj.idKlausurvorgabeQ_Sprachen === null) ? 'null' : obj.idKlausurvorgabeQ_Sprachen.toString()) + ',';
		}
		if (obj.idKlausurvorgabeQ_GW !== undefined) {
			result += '"idKlausurvorgabeQ_GW" : ' + ((obj.idKlausurvorgabeQ_GW === null) ? 'null' : obj.idKlausurvorgabeQ_GW.toString()) + ',';
		}
		if (obj.idKlausurvorgabeQ_NW !== undefined) {
			result += '"idKlausurvorgabeQ_NW" : ' + ((obj.idKlausurvorgabeQ_NW === null) ? 'null' : obj.idKlausurvorgabeQ_NW.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostSchuelerGKLWahl(obj: unknown): GostSchuelerGKLWahl {
	return obj as GostSchuelerGKLWahl;
}
