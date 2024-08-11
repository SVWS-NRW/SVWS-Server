import { JavaObject } from '../../../java/lang/JavaObject';

export class ReligionEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = -1;

	/**
	 * Die Bezeichnung bzw. der Name der Religion.
	 */
	public text : string | null = "";

	/**
	 * Die Bezeichnung bzw. der Name der Religion, wie sie auf einem Zeugnis erscheint.
	 */
	public textZeugnis : string | null = "";

	/**
	 * Das Kürzel des Eintrages für die Statistik.
	 */
	public kuerzel : string | null = "";

	/**
	 * Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an.
	 */
	public sortierung : number = 1;

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 */
	public istSichtbar : boolean = true;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.ReligionEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.ReligionEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): ReligionEintrag {
		const obj = JSON.parse(json) as Partial<ReligionEintrag>;
		const result = new ReligionEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.text = (obj.text === undefined) ? null : obj.text === null ? null : obj.text;
		result.textZeugnis = (obj.textZeugnis === undefined) ? null : obj.textZeugnis === null ? null : obj.textZeugnis;
		result.kuerzel = (obj.kuerzel === undefined) ? null : obj.kuerzel === null ? null : obj.kuerzel;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (obj.istSichtbar === undefined)
			throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		return result;
	}

	public static transpilerToJSON(obj : ReligionEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"text" : ' + ((!obj.text) ? 'null' : JSON.stringify(obj.text)) + ',';
		result += '"textZeugnis" : ' + ((!obj.textZeugnis) ? 'null' : JSON.stringify(obj.textZeugnis)) + ',';
		result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ReligionEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.text !== undefined) {
			result += '"text" : ' + ((!obj.text) ? 'null' : JSON.stringify(obj.text)) + ',';
		}
		if (obj.textZeugnis !== undefined) {
			result += '"textZeugnis" : ' + ((!obj.textZeugnis) ? 'null' : JSON.stringify(obj.textZeugnis)) + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.istSichtbar !== undefined) {
			result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_ReligionEintrag(obj : unknown) : ReligionEintrag {
	return obj as ReligionEintrag;
}
