import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class ENMKlasse extends JavaObject {

	/**
	 * Die ID der Klasse aus der SVWS-DB (z.B. 16)
	 */
	public id : number = 0;

	/**
	 * Das Kürzel der Klasse, wie es im Rahmen der amtlichen Schulstatistik verwendet wird. (z.B. EF)
	 */
	public kuerzel : string | null = null;

	/**
	 * Das Kürzel ser Klasse, wie er im Rahmen der Schule benannt wird und angezeigt werden soll. (z.B. EF)
	 */
	public kuerzelAnzeige : string | null = null;

	/**
	 * Die ID des Jahrgangs aus der SVWS-DB zu der die Klasse gehört (z.B. 11) oder null, falls es sich um eine jahrgangsübergreifende Klasse handelt
	 */
	public idJahrgang : number | null = null;

	/**
	 * Die Reihenfolge der Klasse bei der Sortierung der Klasse. (z.B. 8)
	 */
	public sortierung : number = 0;

	/**
	 * Die IDs der zugeordneten Klassenlehrer.
	 */
	public klassenlehrer : List<number> = new ArrayList<number>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMKlasse';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMKlasse'].includes(name);
	}

	public static class = new Class<ENMKlasse>('de.svws_nrw.core.data.enm.ENMKlasse');

	public static transpilerFromJSON(json : string): ENMKlasse {
		const obj = JSON.parse(json) as Partial<ENMKlasse>;
		const result = new ENMKlasse();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = (obj.kuerzel === undefined) ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.kuerzelAnzeige = (obj.kuerzelAnzeige === undefined) ? null : obj.kuerzelAnzeige === null ? null : obj.kuerzelAnzeige;
		result.idJahrgang = (obj.idJahrgang === undefined) ? null : obj.idJahrgang === null ? null : obj.idJahrgang;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (obj.klassenlehrer !== undefined) {
			for (const elem of obj.klassenlehrer) {
				result.klassenlehrer.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : ENMKlasse) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"kuerzelAnzeige" : ' + ((obj.kuerzelAnzeige === null) ? 'null' : JSON.stringify(obj.kuerzelAnzeige)) + ',';
		result += '"idJahrgang" : ' + ((obj.idJahrgang === null) ? 'null' : obj.idJahrgang.toString()) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"klassenlehrer" : [ ';
		for (let i = 0; i < obj.klassenlehrer.size(); i++) {
			const elem = obj.klassenlehrer.get(i);
			result += elem.toString();
			if (i < obj.klassenlehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMKlasse>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		}
		if (obj.kuerzelAnzeige !== undefined) {
			result += '"kuerzelAnzeige" : ' + ((obj.kuerzelAnzeige === null) ? 'null' : JSON.stringify(obj.kuerzelAnzeige)) + ',';
		}
		if (obj.idJahrgang !== undefined) {
			result += '"idJahrgang" : ' + ((obj.idJahrgang === null) ? 'null' : obj.idJahrgang.toString()) + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.klassenlehrer !== undefined) {
			result += '"klassenlehrer" : [ ';
			for (let i = 0; i < obj.klassenlehrer.size(); i++) {
				const elem = obj.klassenlehrer.get(i);
				result += elem.toString();
				if (i < obj.klassenlehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMKlasse(obj : unknown) : ENMKlasse {
	return obj as ENMKlasse;
}
