import { JavaObject } from '../../../../java/lang/JavaObject';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

export class ENMv2Ankreuzkompetenz extends JavaObject {

	/**
	 * Die ID der Ankreuzkompetenz aus der SVWS-DB
	 */
	public id: number = -1;

	/**
	 * Gibt an, ob es sich um eine Fach-Ankreuzkompetenzen (true) handelt oder um eine Ankreuzkompetenz im Bereich Arbeits- und Sozialverhalten (ASV, false)
	 */
	public istFachkompetenz: boolean = true;

	/**
	 * Die ID des Faches, auf die sich die Ankreuzkompetenz bezieht, NULL bei einer Ankreuzkompetenz im Bereich Arbeits- und Sozialverhalten (ASV)
	 */
	public fachID: number | null = null;

	/**
	 * Die Jahrgänge, falls die Ankreuzkompetenz auf bestimme Jahrgänge eingeschränkt ist, ansonsten eine leere Liste bei keiner Einschränkung.
	 */
	public jahrgaenge: List<number> = new ArrayList<number>();

	/**
	 * Der Text der Ankreuzkompetenz.
	 */
	public text: string = "";

	/**
	 * Die Sortier-Reihenfolge der Ankreuzkompetenzen. Bei gleichen Werten sollte nach dem Text-Atrtribut sortiert werden.
	 */
	public sortierung: number = 1;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.v2.ENMv2Ankreuzkompetenz';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.enm.v2.ENMv2Ankreuzkompetenz'].includes(name);
	}

	public static readonly class = new Class<ENMv2Ankreuzkompetenz>('de.svws_nrw.core.data.enm.v2.ENMv2Ankreuzkompetenz');

	public static transpilerFromJSON(json: string): ENMv2Ankreuzkompetenz {
		const obj = JSON.parse(json) as Partial<ENMv2Ankreuzkompetenz>;
		const result = new ENMv2Ankreuzkompetenz();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.istFachkompetenz === undefined)
			throw new Error('invalid json format, missing attribute istFachkompetenz');
		result.istFachkompetenz = obj.istFachkompetenz;
		result.fachID = (obj.fachID === undefined) ? null : obj.fachID === null ? null : obj.fachID;
		if (obj.jahrgaenge !== undefined) {
			for (const elem of obj.jahrgaenge) {
				result.jahrgaenge.add(elem);
			}
		}
		if (obj.text === undefined)
			throw new Error('invalid json format, missing attribute text');
		result.text = obj.text;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		return result;
	}

	public static transpilerToJSON(obj: ENMv2Ankreuzkompetenz): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"istFachkompetenz" : ' + obj.istFachkompetenz.toString() + ',';
		result += '"fachID" : ' + ((obj.fachID === null) ? 'null' : obj.fachID.toString()) + ',';
		result += '"jahrgaenge" : [ ';
		for (let i = 0; i < obj.jahrgaenge.size(); i++) {
			const elem = obj.jahrgaenge.get(i);
			result += elem.toString();
			if (i < obj.jahrgaenge.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"text" : ' + JSON.stringify(obj.text) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ENMv2Ankreuzkompetenz>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.istFachkompetenz !== undefined) {
			result += '"istFachkompetenz" : ' + obj.istFachkompetenz.toString() + ',';
		}
		if (obj.fachID !== undefined) {
			result += '"fachID" : ' + ((obj.fachID === null) ? 'null' : obj.fachID.toString()) + ',';
		}
		if (obj.jahrgaenge !== undefined) {
			result += '"jahrgaenge" : [ ';
			for (let i = 0; i < obj.jahrgaenge.size(); i++) {
				const elem = obj.jahrgaenge.get(i);
				result += elem.toString();
				if (i < obj.jahrgaenge.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.text !== undefined) {
			result += '"text" : ' + JSON.stringify(obj.text) + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_v2_ENMv2Ankreuzkompetenz(obj: unknown): ENMv2Ankreuzkompetenz {
	return obj as ENMv2Ankreuzkompetenz;
}
