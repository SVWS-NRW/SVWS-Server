import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class StundenplanPausenzeit extends JavaObject {

	/**
	 * Die ID der Pausenzeit.
	 */
	public id : number = -1;

	/**
	 * Der {@link Wochentag} für die Pausenzeit (1=Montag, 2=Dienstag, ..., 7=Sonntag)
	 */
	public wochentag : number = 1;

	/**
	 * Die Uhrzeit in Minuten seit 0 Uhr, wann die Pause beginnt. NULL bedeutet "noch nicht definiert".
	 */
	public beginn : number | null = null;

	/**
	 * Die Uhrzeit in Minuten seit 0 Uhr, wann die Pause endet. NULL bedeutet "noch nicht definiert".
	 */
	public ende : number | null = null;

	/**
	 * Die Bezeichnung der Pausenzeit, welche die Art der Pausenzeit genauer beschreibt (z.B. Mittagspause).
	 */
	public bezeichnung : string = "Pause";

	/**
	 * Die IDs der Klassen, denen diese Pausenzeit zugeordnet sind. Ist die Liste leer, so gilt die Pausenzeit für alle Klassen!
	 */
	public klassen : List<number> = new ArrayList<number>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplan.StundenplanPausenzeit';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanPausenzeit'].includes(name);
	}

	public static class = new Class<StundenplanPausenzeit>('de.svws_nrw.core.data.stundenplan.StundenplanPausenzeit');

	public static transpilerFromJSON(json : string): StundenplanPausenzeit {
		const obj = JSON.parse(json) as Partial<StundenplanPausenzeit>;
		const result = new StundenplanPausenzeit();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.wochentag === undefined)
			throw new Error('invalid json format, missing attribute wochentag');
		result.wochentag = obj.wochentag;
		result.beginn = (obj.beginn === undefined) ? null : obj.beginn === null ? null : obj.beginn;
		result.ende = (obj.ende === undefined) ? null : obj.ende === null ? null : obj.ende;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.klassen !== undefined) {
			for (const elem of obj.klassen) {
				result.klassen.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : StundenplanPausenzeit) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"wochentag" : ' + obj.wochentag.toString() + ',';
		result += '"beginn" : ' + ((obj.beginn === null) ? 'null' : obj.beginn.toString()) + ',';
		result += '"ende" : ' + ((obj.ende === null) ? 'null' : obj.ende.toString()) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"klassen" : [ ';
		for (let i = 0; i < obj.klassen.size(); i++) {
			const elem = obj.klassen.get(i);
			result += elem.toString();
			if (i < obj.klassen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanPausenzeit>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.wochentag !== undefined) {
			result += '"wochentag" : ' + obj.wochentag.toString() + ',';
		}
		if (obj.beginn !== undefined) {
			result += '"beginn" : ' + ((obj.beginn === null) ? 'null' : obj.beginn.toString()) + ',';
		}
		if (obj.ende !== undefined) {
			result += '"ende" : ' + ((obj.ende === null) ? 'null' : obj.ende.toString()) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.klassen !== undefined) {
			result += '"klassen" : [ ';
			for (let i = 0; i < obj.klassen.size(); i++) {
				const elem = obj.klassen.get(i);
				result += elem.toString();
				if (i < obj.klassen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanPausenzeit(obj : unknown) : StundenplanPausenzeit {
	return obj as StundenplanPausenzeit;
}
