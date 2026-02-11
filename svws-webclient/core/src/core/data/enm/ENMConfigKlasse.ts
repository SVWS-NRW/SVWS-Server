import { JavaObject } from '../../../java/lang/JavaObject';
import { ENMConfigKlasseSpalte } from '../../../core/data/enm/ENMConfigKlasseSpalte';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class ENMConfigKlasse extends JavaObject {

	/**
	 * Die ID der Klasse aus der SVWS-DB (z.B. 16)
	 */
	public id: number = 0;

	/**
	 * Der Zeitstempel, ab wann die Noteneingabe erlaubt ist, sofern eine Einschränkung vorliegt, sonst null.
	 */
	public tsEingabeAb: string | null = null;

	/**
	 * Der Zeitstempel, bis wann die Noteneingabe erlaubt ist, sofern eine Einschränkung vorliegt, sonst null.
	 */
	public tsEingabeBis: string | null = null;

	/**
	 * Gibt an, ob die Fehlstunden klassen- oder kursweise eingegeben werden.
	 */
	public istFehlstundenEingabeKlassenweise: boolean = false;

	/**
	 * die globale Konfiguration für die einzelnen Spalten für diese Klasse.
	 */
	public spalten: List<ENMConfigKlasseSpalte> = new ArrayList<ENMConfigKlasseSpalte>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMConfigKlasse';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMConfigKlasse'].includes(name);
	}

	public static readonly class = new Class<ENMConfigKlasse>('de.svws_nrw.core.data.enm.ENMConfigKlasse');

	public static transpilerFromJSON(json: string): ENMConfigKlasse {
		const obj = JSON.parse(json) as Partial<ENMConfigKlasse>;
		const result = new ENMConfigKlasse();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.tsEingabeAb = (obj.tsEingabeAb === undefined) ? null : obj.tsEingabeAb === null ? null : obj.tsEingabeAb;
		result.tsEingabeBis = (obj.tsEingabeBis === undefined) ? null : obj.tsEingabeBis === null ? null : obj.tsEingabeBis;
		if (obj.istFehlstundenEingabeKlassenweise === undefined)
			throw new Error('invalid json format, missing attribute istFehlstundenEingabeKlassenweise');
		result.istFehlstundenEingabeKlassenweise = obj.istFehlstundenEingabeKlassenweise;
		if (obj.spalten !== undefined) {
			for (const elem of obj.spalten) {
				result.spalten.add(ENMConfigKlasseSpalte.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: ENMConfigKlasse): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"tsEingabeAb" : ' + ((obj.tsEingabeAb === null) ? 'null' : JSON.stringify(obj.tsEingabeAb)) + ',';
		result += '"tsEingabeBis" : ' + ((obj.tsEingabeBis === null) ? 'null' : JSON.stringify(obj.tsEingabeBis)) + ',';
		result += '"istFehlstundenEingabeKlassenweise" : ' + obj.istFehlstundenEingabeKlassenweise.toString() + ',';
		result += '"spalten" : [ ';
		for (let i = 0; i < obj.spalten.size(); i++) {
			const elem = obj.spalten.get(i);
			result += ENMConfigKlasseSpalte.transpilerToJSON(elem);
			if (i < obj.spalten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ENMConfigKlasse>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.tsEingabeAb !== undefined) {
			result += '"tsEingabeAb" : ' + ((obj.tsEingabeAb === null) ? 'null' : JSON.stringify(obj.tsEingabeAb)) + ',';
		}
		if (obj.tsEingabeBis !== undefined) {
			result += '"tsEingabeBis" : ' + ((obj.tsEingabeBis === null) ? 'null' : JSON.stringify(obj.tsEingabeBis)) + ',';
		}
		if (obj.istFehlstundenEingabeKlassenweise !== undefined) {
			result += '"istFehlstundenEingabeKlassenweise" : ' + obj.istFehlstundenEingabeKlassenweise.toString() + ',';
		}
		if (obj.spalten !== undefined) {
			result += '"spalten" : [ ';
			for (let i = 0; i < obj.spalten.size(); i++) {
				const elem = obj.spalten.get(i);
				result += ENMConfigKlasseSpalte.transpilerToJSON(elem);
				if (i < obj.spalten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMConfigKlasse(obj: unknown): ENMConfigKlasse {
	return obj as ENMConfigKlasse;
}
