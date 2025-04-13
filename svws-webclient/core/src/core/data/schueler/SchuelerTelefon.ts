import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuelerTelefon extends JavaObject {

	/**
	 * Die ID des Telefonnummerneintrags.
	 */
	public id : number = -1;

	/**
	 * Die ID des Schülers zum Telefonnummerneintrag.
	 */
	public idSchueler : number = -1;

	/**
	 * Die ID der TelefonArt zum Telefonnummerneintrag.
	 */
	public idTelefonArt : number = -1;

	/**
	 * Die Telefonnummer für die TelefonArt eines Schülers
	 */
	public telefonnummer : string | null = null;

	/**
	 * Die Bemerkung für die TelefonArt eines Schülers
	 */
	public bemerkung : string | null = null;

	/**
	 * Gibt die Position in der Sortierreihenfolge für die Einträge an.
	 */
	public sortierung : number = 1;

	/**
	 * Die Sperrung des Telefonnummerneintrags.
	 */
	public istGesperrt : boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schueler.SchuelerTelefon';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.SchuelerTelefon'].includes(name);
	}

	public static class = new Class<SchuelerTelefon>('de.svws_nrw.core.data.schueler.SchuelerTelefon');

	public static transpilerFromJSON(json : string): SchuelerTelefon {
		const obj = JSON.parse(json) as Partial<SchuelerTelefon>;
		const result = new SchuelerTelefon();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idSchueler === undefined)
			throw new Error('invalid json format, missing attribute idSchueler');
		result.idSchueler = obj.idSchueler;
		if (obj.idTelefonArt === undefined)
			throw new Error('invalid json format, missing attribute idTelefonArt');
		result.idTelefonArt = obj.idTelefonArt;
		result.telefonnummer = (obj.telefonnummer === undefined) ? null : obj.telefonnummer === null ? null : obj.telefonnummer;
		result.bemerkung = (obj.bemerkung === undefined) ? null : obj.bemerkung === null ? null : obj.bemerkung;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (obj.istGesperrt === undefined)
			throw new Error('invalid json format, missing attribute istGesperrt');
		result.istGesperrt = obj.istGesperrt;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerTelefon) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		result += '"idTelefonArt" : ' + obj.idTelefonArt.toString() + ',';
		result += '"telefonnummer" : ' + ((obj.telefonnummer === null) ? 'null' : JSON.stringify(obj.telefonnummer)) + ',';
		result += '"bemerkung" : ' + ((obj.bemerkung === null) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"istGesperrt" : ' + obj.istGesperrt.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerTelefon>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idSchueler !== undefined) {
			result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		}
		if (obj.idTelefonArt !== undefined) {
			result += '"idTelefonArt" : ' + obj.idTelefonArt.toString() + ',';
		}
		if (obj.telefonnummer !== undefined) {
			result += '"telefonnummer" : ' + ((obj.telefonnummer === null) ? 'null' : JSON.stringify(obj.telefonnummer)) + ',';
		}
		if (obj.bemerkung !== undefined) {
			result += '"bemerkung" : ' + ((obj.bemerkung === null) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.istGesperrt !== undefined) {
			result += '"istGesperrt" : ' + obj.istGesperrt.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schueler_SchuelerTelefon(obj : unknown) : SchuelerTelefon {
	return obj as SchuelerTelefon;
}
