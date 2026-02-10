import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class FloskelJahrgangZuordnung extends JavaObject {

	/**
	 * Die ID der Floskel-Jahrgangs-Zuordnung
	 */
	public id: number = 0;

	/**
	 * Die ID der Floskel
	 */
	public idFloskel: number = 0;

	/**
	 * Die ID des Jahrgangs
	 */
	public idJahrgang: number = 0;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.FloskelJahrgangZuordnung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schule.FloskelJahrgangZuordnung'].includes(name);
	}

	public static readonly class = new Class<FloskelJahrgangZuordnung>('de.svws_nrw.core.data.schule.FloskelJahrgangZuordnung');

	public static transpilerFromJSON(json: string): FloskelJahrgangZuordnung {
		const obj = JSON.parse(json) as Partial<FloskelJahrgangZuordnung>;
		const result = new FloskelJahrgangZuordnung();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idFloskel === undefined)
			throw new Error('invalid json format, missing attribute idFloskel');
		result.idFloskel = obj.idFloskel;
		if (obj.idJahrgang === undefined)
			throw new Error('invalid json format, missing attribute idJahrgang');
		result.idJahrgang = obj.idJahrgang;
		return result;
	}

	public static transpilerToJSON(obj: FloskelJahrgangZuordnung): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idFloskel" : ' + obj.idFloskel.toString() + ',';
		result += '"idJahrgang" : ' + obj.idJahrgang.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<FloskelJahrgangZuordnung>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idFloskel !== undefined) {
			result += '"idFloskel" : ' + obj.idFloskel.toString() + ',';
		}
		if (obj.idJahrgang !== undefined) {
			result += '"idJahrgang" : ' + obj.idJahrgang.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_FloskelJahrgangZuordnung(obj: unknown): FloskelJahrgangZuordnung {
	return obj as FloskelJahrgangZuordnung;
}
