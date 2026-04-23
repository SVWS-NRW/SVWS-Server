import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuelerLeistungsdatenStatistikGesamt extends JavaObject {

	/**
	 * Die ID der Leistungsdaten in der Datenbank.
	 */
	public id: number = -1;

	/**
	 * Die ID des Faches, auf welches sich die Leistungsdaten beziehen.
	 */
	public fachID: number = -1;

	/**
	 * Die ID des Kurses, auf welches sich die Leistungsdaten beziehen - bei Klassenunterricht NULL.
	 */
	public kursID: number | null = null;

	/**
	 * Die spezielle Kursart des Schülers, sofern Kursunterricht vorliegt.
	 */
	public kursart: string | null = null;

	/**
	 * Gibt an, ob es sich bei der Fachbelegung um ein Abiturfach des Schülers handelt und wenn ja, um welches (NULL, 1, 2, 3, 4)
	 */
	public abifach: number | null = null;

	/**
	 * Die ID des zugehörigen Fach-Lehrers.
	 */
	public lehrerID: number | null = null;

	/**
	 * Die Anzahl der Wochenstunden, welche das Fach unterrichtet wird.
	 */
	public wochenstunden: number = 0;

	/**
	 * Die ID der Zusatzkraft.
	 */
	public zusatzkraftID: number | null = null;

	/**
	 * Die Anzahl der Wochenstunden der Zusatzkraft.
	 */
	public zusatzkraftWochenstunden: number = 0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.statistik.SchuelerLeistungsdatenStatistikGesamt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.statistik.SchuelerLeistungsdatenStatistikGesamt'].includes(name);
	}

	public static readonly class = new Class<SchuelerLeistungsdatenStatistikGesamt>('de.svws_nrw.asd.data.statistik.SchuelerLeistungsdatenStatistikGesamt');

	public static transpilerFromJSON(json: string): SchuelerLeistungsdatenStatistikGesamt {
		const obj = JSON.parse(json) as Partial<SchuelerLeistungsdatenStatistikGesamt>;
		const result = new SchuelerLeistungsdatenStatistikGesamt();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.fachID === undefined)
			throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		result.kursID = (obj.kursID === undefined) ? null : obj.kursID === null ? null : obj.kursID;
		result.kursart = (obj.kursart === undefined) ? null : obj.kursart === null ? null : obj.kursart;
		result.abifach = (obj.abifach === undefined) ? null : obj.abifach === null ? null : obj.abifach;
		result.lehrerID = (obj.lehrerID === undefined) ? null : obj.lehrerID === null ? null : obj.lehrerID;
		if (obj.wochenstunden === undefined)
			throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		result.zusatzkraftID = (obj.zusatzkraftID === undefined) ? null : obj.zusatzkraftID === null ? null : obj.zusatzkraftID;
		if (obj.zusatzkraftWochenstunden === undefined)
			throw new Error('invalid json format, missing attribute zusatzkraftWochenstunden');
		result.zusatzkraftWochenstunden = obj.zusatzkraftWochenstunden;
		return result;
	}

	public static transpilerToJSON(obj: SchuelerLeistungsdatenStatistikGesamt): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"fachID" : ' + obj.fachID.toString() + ',';
		result += '"kursID" : ' + ((obj.kursID === null) ? 'null' : obj.kursID.toString()) + ',';
		result += '"kursart" : ' + ((obj.kursart === null) ? 'null' : JSON.stringify(obj.kursart)) + ',';
		result += '"abifach" : ' + ((obj.abifach === null) ? 'null' : obj.abifach.toString()) + ',';
		result += '"lehrerID" : ' + ((obj.lehrerID === null) ? 'null' : obj.lehrerID.toString()) + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		result += '"zusatzkraftID" : ' + ((obj.zusatzkraftID === null) ? 'null' : obj.zusatzkraftID.toString()) + ',';
		result += '"zusatzkraftWochenstunden" : ' + obj.zusatzkraftWochenstunden.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchuelerLeistungsdatenStatistikGesamt>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.fachID !== undefined) {
			result += '"fachID" : ' + obj.fachID.toString() + ',';
		}
		if (obj.kursID !== undefined) {
			result += '"kursID" : ' + ((obj.kursID === null) ? 'null' : obj.kursID.toString()) + ',';
		}
		if (obj.kursart !== undefined) {
			result += '"kursart" : ' + ((obj.kursart === null) ? 'null' : JSON.stringify(obj.kursart)) + ',';
		}
		if (obj.abifach !== undefined) {
			result += '"abifach" : ' + ((obj.abifach === null) ? 'null' : obj.abifach.toString()) + ',';
		}
		if (obj.lehrerID !== undefined) {
			result += '"lehrerID" : ' + ((obj.lehrerID === null) ? 'null' : obj.lehrerID.toString()) + ',';
		}
		if (obj.wochenstunden !== undefined) {
			result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		}
		if (obj.zusatzkraftID !== undefined) {
			result += '"zusatzkraftID" : ' + ((obj.zusatzkraftID === null) ? 'null' : obj.zusatzkraftID.toString()) + ',';
		}
		if (obj.zusatzkraftWochenstunden !== undefined) {
			result += '"zusatzkraftWochenstunden" : ' + obj.zusatzkraftWochenstunden.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_statistik_SchuelerLeistungsdatenStatistikGesamt(obj: unknown): SchuelerLeistungsdatenStatistikGesamt {
	return obj as SchuelerLeistungsdatenStatistikGesamt;
}
