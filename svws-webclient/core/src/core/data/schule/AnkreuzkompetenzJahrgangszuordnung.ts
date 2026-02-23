import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class AnkreuzkompetenzJahrgangszuordnung extends JavaObject {

	/**
	 * Die ID der Zuordnung
	 */
	public id: number = -1;

	/**
	 * Die ID der Ankreuzkompetenz
	 */
	public idAnkreuzkompetenz: number = -1;

	/**
	 * Die ID des Jahrgangs
	 */
	public idJahrgang: number = -1;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.AnkreuzkompetenzJahrgangszuordnung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schule.AnkreuzkompetenzJahrgangszuordnung'].includes(name);
	}

	public static readonly class = new Class<AnkreuzkompetenzJahrgangszuordnung>('de.svws_nrw.core.data.schule.AnkreuzkompetenzJahrgangszuordnung');

	public static transpilerFromJSON(json: string): AnkreuzkompetenzJahrgangszuordnung {
		const obj = JSON.parse(json) as Partial<AnkreuzkompetenzJahrgangszuordnung>;
		const result = new AnkreuzkompetenzJahrgangszuordnung();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idAnkreuzkompetenz === undefined)
			throw new Error('invalid json format, missing attribute idAnkreuzkompetenz');
		result.idAnkreuzkompetenz = obj.idAnkreuzkompetenz;
		if (obj.idJahrgang === undefined)
			throw new Error('invalid json format, missing attribute idJahrgang');
		result.idJahrgang = obj.idJahrgang;
		return result;
	}

	public static transpilerToJSON(obj: AnkreuzkompetenzJahrgangszuordnung): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idAnkreuzkompetenz" : ' + obj.idAnkreuzkompetenz.toString() + ',';
		result += '"idJahrgang" : ' + obj.idJahrgang.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<AnkreuzkompetenzJahrgangszuordnung>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idAnkreuzkompetenz !== undefined) {
			result += '"idAnkreuzkompetenz" : ' + obj.idAnkreuzkompetenz.toString() + ',';
		}
		if (obj.idJahrgang !== undefined) {
			result += '"idJahrgang" : ' + obj.idJahrgang.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_AnkreuzkompetenzJahrgangszuordnung(obj: unknown): AnkreuzkompetenzJahrgangszuordnung {
	return obj as AnkreuzkompetenzJahrgangszuordnung;
}
