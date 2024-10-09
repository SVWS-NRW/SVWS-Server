import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class ENMAnkreuzkompetenz extends JavaObject {

	/**
	 * Die ID der Ankreuzkompetenz aus der SVWS-DB
	 */
	public id : number = -1;

	/**
	 * Gibt an, ob es sich um eine Fach-Ankreuzkompetenzen (true) handelt oder um eine Ankreuzkompetenz im Bereich Arbeits- und Sozialverhalten (ASV, false)
	 */
	public istFachkompetenz : boolean = true;

	/**
	 * Die ID des Faches, auf die sich die Ankreuzkompetenz bezieht, NULL bei einer Ankreuzkompetenz im Bereich Arbeits- und Sozialverhalten (ASV)
	 */
	public fachID : number | null = null;

	/**
	 * Das Statistik-Kürzel des Jahrgangs zu der die Ankreuzfloskel gehört.
	 */
	public jahrgang : string = "";

	/**
	 * Der Text der Ankreuzkompetenz.
	 */
	public text : string = "";

	/**
	 * Die Sortier-Reihenfolge der Ankreuzkompetenzen. Bei gleichen Werten sollte nach dem Text-Atrtribut sortiert werden.
	 */
	public sortierung : number = 1;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMAnkreuzkompetenz';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMAnkreuzkompetenz'].includes(name);
	}

	public static class = new Class<ENMAnkreuzkompetenz>('de.svws_nrw.core.data.enm.ENMAnkreuzkompetenz');

	public static transpilerFromJSON(json : string): ENMAnkreuzkompetenz {
		const obj = JSON.parse(json) as Partial<ENMAnkreuzkompetenz>;
		const result = new ENMAnkreuzkompetenz();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.istFachkompetenz === undefined)
			throw new Error('invalid json format, missing attribute istFachkompetenz');
		result.istFachkompetenz = obj.istFachkompetenz;
		result.fachID = (obj.fachID === undefined) ? null : obj.fachID === null ? null : obj.fachID;
		if (obj.jahrgang === undefined)
			throw new Error('invalid json format, missing attribute jahrgang');
		result.jahrgang = obj.jahrgang;
		if (obj.text === undefined)
			throw new Error('invalid json format, missing attribute text');
		result.text = obj.text;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		return result;
	}

	public static transpilerToJSON(obj : ENMAnkreuzkompetenz) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"istFachkompetenz" : ' + obj.istFachkompetenz.toString() + ',';
		result += '"fachID" : ' + ((!obj.fachID) ? 'null' : obj.fachID.toString()) + ',';
		result += '"jahrgang" : ' + JSON.stringify(obj.jahrgang) + ',';
		result += '"text" : ' + JSON.stringify(obj.text) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMAnkreuzkompetenz>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.istFachkompetenz !== undefined) {
			result += '"istFachkompetenz" : ' + obj.istFachkompetenz.toString() + ',';
		}
		if (obj.fachID !== undefined) {
			result += '"fachID" : ' + ((!obj.fachID) ? 'null' : obj.fachID.toString()) + ',';
		}
		if (obj.jahrgang !== undefined) {
			result += '"jahrgang" : ' + JSON.stringify(obj.jahrgang) + ',';
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

export function cast_de_svws_nrw_core_data_enm_ENMAnkreuzkompetenz(obj : unknown) : ENMAnkreuzkompetenz {
	return obj as ENMAnkreuzkompetenz;
}
