import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';

export class StundenplanKurs extends JavaObject {

	/**
	 * Die ID des Kurses.
	 */
	public id : number = -1;

	/**
	 * Die ID des Faches
	 */
	public idFach : number = -1;

	/**
	 * Die Bezeichnung des Kurses.
	 */
	public bezeichnung : string = "";

	/**
	 * Die Wochenstunden, welche dem Kurs zugeordnet sind
	 */
	public wochenstunden : number = 0;

	/**
	 * Eine Nummer, welche die Sortierreihenfolge bei den Kursen angibt.
	 */
	public sortierung : number = 32000;

	/**
	 * Die Liste der IDs der {@link StundenplanSchiene}-Objekte, denen der Kurs zugeordnet ist.
	 */
	public schienen : List<number> = new ArrayList();

	/**
	 * Die Liste der IDs der {@link StundenplanJahrgang}-Objekte, denen der Kurs zugeordnet ist.
	 */
	public jahrgaenge : List<number> = new ArrayList();

	/**
	 * Die Liste der IDs der {@link StundenplanSchueler}-Objekte, die dem Kurs zugeordnet sind.
	 */
	public schueler : List<number> = new ArrayList();

	/**
	 * Die Liste der IDs der {@link StundenplanLehrer}-Objekte, die dem Kurs zugeordnet sind.
	 */
	public lehrer : List<number> = new ArrayList();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanKurs'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanKurs {
		const obj = JSON.parse(json);
		const result = new StundenplanKurs();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.idFach === "undefined")
			 throw new Error('invalid json format, missing attribute idFach');
		result.idFach = obj.idFach;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (typeof obj.wochenstunden === "undefined")
			 throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if ((obj.schienen !== undefined) && (obj.schienen !== null)) {
			for (const elem of obj.schienen) {
				result.schienen?.add(elem);
			}
		}
		if ((obj.jahrgaenge !== undefined) && (obj.jahrgaenge !== null)) {
			for (const elem of obj.jahrgaenge) {
				result.jahrgaenge?.add(elem);
			}
		}
		if ((obj.schueler !== undefined) && (obj.schueler !== null)) {
			for (const elem of obj.schueler) {
				result.schueler?.add(elem);
			}
		}
		if ((obj.lehrer !== undefined) && (obj.lehrer !== null)) {
			for (const elem of obj.lehrer) {
				result.lehrer?.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : StundenplanKurs) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idFach" : ' + obj.idFach + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		if (!obj.schienen) {
			result += '"schienen" : []';
		} else {
			result += '"schienen" : [ ';
			for (let i = 0; i < obj.schienen.size(); i++) {
				const elem = obj.schienen.get(i);
				result += elem;
				if (i < obj.schienen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.jahrgaenge) {
			result += '"jahrgaenge" : []';
		} else {
			result += '"jahrgaenge" : [ ';
			for (let i = 0; i < obj.jahrgaenge.size(); i++) {
				const elem = obj.jahrgaenge.get(i);
				result += elem;
				if (i < obj.jahrgaenge.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.schueler) {
			result += '"schueler" : []';
		} else {
			result += '"schueler" : [ ';
			for (let i = 0; i < obj.schueler.size(); i++) {
				const elem = obj.schueler.get(i);
				result += elem;
				if (i < obj.schueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.lehrer) {
			result += '"lehrer" : []';
		} else {
			result += '"lehrer" : [ ';
			for (let i = 0; i < obj.lehrer.size(); i++) {
				const elem = obj.lehrer.get(i);
				result += elem;
				if (i < obj.lehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanKurs>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.idFach !== "undefined") {
			result += '"idFach" : ' + obj.idFach + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		}
		if (typeof obj.wochenstunden !== "undefined") {
			result += '"wochenstunden" : ' + obj.wochenstunden + ',';
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + obj.sortierung + ',';
		}
		if (typeof obj.schienen !== "undefined") {
			if (!obj.schienen) {
				result += '"schienen" : []';
			} else {
				result += '"schienen" : [ ';
				for (let i = 0; i < obj.schienen.size(); i++) {
					const elem = obj.schienen.get(i);
					result += elem;
					if (i < obj.schienen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.jahrgaenge !== "undefined") {
			if (!obj.jahrgaenge) {
				result += '"jahrgaenge" : []';
			} else {
				result += '"jahrgaenge" : [ ';
				for (let i = 0; i < obj.jahrgaenge.size(); i++) {
					const elem = obj.jahrgaenge.get(i);
					result += elem;
					if (i < obj.jahrgaenge.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.schueler !== "undefined") {
			if (!obj.schueler) {
				result += '"schueler" : []';
			} else {
				result += '"schueler" : [ ';
				for (let i = 0; i < obj.schueler.size(); i++) {
					const elem = obj.schueler.get(i);
					result += elem;
					if (i < obj.schueler.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.lehrer !== "undefined") {
			if (!obj.lehrer) {
				result += '"lehrer" : []';
			} else {
				result += '"lehrer" : [ ';
				for (let i = 0; i < obj.lehrer.size(); i++) {
					const elem = obj.lehrer.get(i);
					result += elem;
					if (i < obj.lehrer.size() - 1)
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

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanKurs(obj : unknown) : StundenplanKurs {
	return obj as StundenplanKurs;
}
