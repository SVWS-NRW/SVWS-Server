import { JavaObject } from '../../../../java/lang/JavaObject';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

export class ENMv2Abteilung extends JavaObject {

	/**
	 * Die ID des Eintrags für die Abteilung
	 */
	public id: number = -1;

	/**
	 * Die Lehrer-ID des Abteilungsleiters, sofern die Abteilung einen zugewiesen hat.
	 */
	public idAbteilungsleiter: number | null = null;

	/**
	 * Die Bezeichnung der Abteilung (max. 50 Zeichen)
	 */
	public bezeichnung: string = "";

	/**
	 * Gibt einen Wert für die Sortierung der Abteilungen an.
	 */
	public sortierung: number = 32000;

	/**
	 * Die Zuordnung der Klassen zu der Abteilung.
	 */
	public readonly klassenzuordnungen: List<number> = new ArrayList<number>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.v2.ENMv2Abteilung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.enm.v2.ENMv2Abteilung'].includes(name);
	}

	public static readonly class = new Class<ENMv2Abteilung>('de.svws_nrw.core.data.enm.v2.ENMv2Abteilung');

	public static transpilerFromJSON(json: string): ENMv2Abteilung {
		const obj = JSON.parse(json) as Partial<ENMv2Abteilung>;
		const result = new ENMv2Abteilung();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.idAbteilungsleiter = (obj.idAbteilungsleiter === undefined) ? null : obj.idAbteilungsleiter === null ? null : obj.idAbteilungsleiter;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (obj.klassenzuordnungen !== undefined) {
			for (const elem of obj.klassenzuordnungen) {
				result.klassenzuordnungen.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: ENMv2Abteilung): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idAbteilungsleiter" : ' + ((obj.idAbteilungsleiter === null) ? 'null' : obj.idAbteilungsleiter.toString()) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"klassenzuordnungen" : [ ';
		for (let i = 0; i < obj.klassenzuordnungen.size(); i++) {
			const elem = obj.klassenzuordnungen.get(i);
			result += elem.toString();
			if (i < obj.klassenzuordnungen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ENMv2Abteilung>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idAbteilungsleiter !== undefined) {
			result += '"idAbteilungsleiter" : ' + ((obj.idAbteilungsleiter === null) ? 'null' : obj.idAbteilungsleiter.toString()) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.klassenzuordnungen !== undefined) {
			result += '"klassenzuordnungen" : [ ';
			for (let i = 0; i < obj.klassenzuordnungen.size(); i++) {
				const elem = obj.klassenzuordnungen.get(i);
				result += elem.toString();
				if (i < obj.klassenzuordnungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_v2_ENMv2Abteilung(obj: unknown): ENMv2Abteilung {
	return obj as ENMv2Abteilung;
}
