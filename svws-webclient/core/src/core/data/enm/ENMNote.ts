import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class ENMNote extends JavaObject {

	/**
	 * Die ID der Note.
	 */
	public id : number = 0;

	/**
	 * Die Kurzschreibweise der Note als Zahl ggf. mit Tendenz (+/-), ggf. auch ein Kürzel für PseudoNoten
	 */
	public kuerzel : string | null = null;

	/**
	 * Die Notenpunkte, die dieser Note ggf. zugeordnet sind
	 */
	public notenpunkte : number | null = null;

	/**
	 * Die Note in ausführlicher Textform ggf. mit Tendenz (plus/minus)
	 */
	public text : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMNote';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMNote'].includes(name);
	}

	public static class = new Class<ENMNote>('de.svws_nrw.core.data.enm.ENMNote');

	public static transpilerFromJSON(json : string): ENMNote {
		const obj = JSON.parse(json) as Partial<ENMNote>;
		const result = new ENMNote();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = (obj.kuerzel === undefined) ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.notenpunkte = (obj.notenpunkte === undefined) ? null : obj.notenpunkte === null ? null : obj.notenpunkte;
		result.text = (obj.text === undefined) ? null : obj.text === null ? null : obj.text;
		return result;
	}

	public static transpilerToJSON(obj : ENMNote) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"notenpunkte" : ' + ((obj.notenpunkte === null) ? 'null' : obj.notenpunkte.toString()) + ',';
		result += '"text" : ' + ((obj.text === null) ? 'null' : JSON.stringify(obj.text)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMNote>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		}
		if (obj.notenpunkte !== undefined) {
			result += '"notenpunkte" : ' + ((obj.notenpunkte === null) ? 'null' : obj.notenpunkte.toString()) + ',';
		}
		if (obj.text !== undefined) {
			result += '"text" : ' + ((obj.text === null) ? 'null' : JSON.stringify(obj.text)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMNote(obj : unknown) : ENMNote {
	return obj as ENMNote;
}
