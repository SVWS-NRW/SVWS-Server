import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class Floskel extends JavaObject {

	/**
	 * Die ID der Floskel
	 */
	public id: number = 0;

	/**
	 * Das Kürzel der Floskel
	 */
	public kuerzel: string | null = null;

	/**
	 * Der Text
	 */
	public text: string | null = null;

	/**
	 * Die ID der Floskelgruppe
	 */
	public idFloskelgruppe: number | null = null;

	/**
	 * Die ID des Fachs
	 */
	public idFach: number | null = null;

	/**
	 * Das Niveau
	 */
	public niveau: number | null = null;

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 */
	public istSichtbar: boolean = true;

	/**
	 * Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an.
	 */
	public sortierung: number = 32000;

	/**
	 * Die IDs der Jahrgänge, falls die Floskel auf bestimmte Jahrgänge eingeschränkt ist. Liegt keine Einschränkung vor so ist die Liste leer
	 */
	public idsJahrgaenge: List<number> | null = new ArrayList<number>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.Floskel';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schule.Floskel'].includes(name);
	}

	public static readonly class = new Class<Floskel>('de.svws_nrw.core.data.schule.Floskel');

	public static transpilerFromJSON(json: string): Floskel {
		const obj = JSON.parse(json) as Partial<Floskel>;
		const result = new Floskel();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = (obj.kuerzel === undefined) ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.text = (obj.text === undefined) ? null : obj.text === null ? null : obj.text;
		result.idFloskelgruppe = (obj.idFloskelgruppe === undefined) ? null : obj.idFloskelgruppe === null ? null : obj.idFloskelgruppe;
		result.idFach = (obj.idFach === undefined) ? null : obj.idFach === null ? null : obj.idFach;
		result.niveau = (obj.niveau === undefined) ? null : obj.niveau === null ? null : obj.niveau;
		if (obj.istSichtbar === undefined)
			throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if ((obj.idsJahrgaenge !== undefined) && (obj.idsJahrgaenge !== null)) {
			result.idsJahrgaenge = new ArrayList();
			for (const elem of obj.idsJahrgaenge) {
				result.idsJahrgaenge.add(elem);
			}
		} else {
			result.idsJahrgaenge = null;
		}
		return result;
	}

	public static transpilerToJSON(obj: Floskel): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"text" : ' + ((obj.text === null) ? 'null' : JSON.stringify(obj.text)) + ',';
		result += '"idFloskelgruppe" : ' + ((obj.idFloskelgruppe === null) ? 'null' : obj.idFloskelgruppe.toString()) + ',';
		result += '"idFach" : ' + ((obj.idFach === null) ? 'null' : obj.idFach.toString()) + ',';
		result += '"niveau" : ' + ((obj.niveau === null) ? 'null' : obj.niveau.toString()) + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		if (!obj.idsJahrgaenge) {
			result += '"idsJahrgaenge" : null' + ',';
		} else {
			result += '"idsJahrgaenge" : [ ';
			for (let i = 0; i < obj.idsJahrgaenge.size(); i++) {
				const elem = obj.idsJahrgaenge.get(i);
				result += elem.toString();
				if (i < obj.idsJahrgaenge.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<Floskel>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		}
		if (obj.text !== undefined) {
			result += '"text" : ' + ((obj.text === null) ? 'null' : JSON.stringify(obj.text)) + ',';
		}
		if (obj.idFloskelgruppe !== undefined) {
			result += '"idFloskelgruppe" : ' + ((obj.idFloskelgruppe === null) ? 'null' : obj.idFloskelgruppe.toString()) + ',';
		}
		if (obj.idFach !== undefined) {
			result += '"idFach" : ' + ((obj.idFach === null) ? 'null' : obj.idFach.toString()) + ',';
		}
		if (obj.niveau !== undefined) {
			result += '"niveau" : ' + ((obj.niveau === null) ? 'null' : obj.niveau.toString()) + ',';
		}
		if (obj.istSichtbar !== undefined) {
			result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.idsJahrgaenge !== undefined) {
			if (!obj.idsJahrgaenge) {
				result += '"idsJahrgaenge" : null' + ',';
			} else {
				result += '"idsJahrgaenge" : [ ';
				for (let i = 0; i < obj.idsJahrgaenge.size(); i++) {
					const elem = obj.idsJahrgaenge.get(i);
					result += elem.toString();
					if (i < obj.idsJahrgaenge.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_Floskel(obj: unknown): Floskel {
	return obj as Floskel;
}
