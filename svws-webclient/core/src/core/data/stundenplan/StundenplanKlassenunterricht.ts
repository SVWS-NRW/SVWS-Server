import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';

export class StundenplanKlassenunterricht extends JavaObject {

	/**
	 * Die ID der Klasse.
	 */
	public idKlasse : number = -1;

	/**
	 * Die ID des Faches.
	 */
	public idFach : number = -1;

	/**
	 * Die Bezeichnung des Klassenunterrichtes.
	 */
	public bezeichnung : string = "";

	/**
	 * Die Wochenstunden, welche dem Klassenunterricht zugeordnet sind
	 */
	public wochenstunden : number = 0;

	/**
	 * Die Liste der IDs der {@link StundenplanSchiene}-Objekte, denen der Klassenunterricht zugeordnet ist.
	 */
	public schienen : List<number> = new ArrayList<number>();

	/**
	 * Die Liste der IDs der {@link StundenplanSchueler}-Objekte, denen der Klassenunterricht zugeordnet ist.
	 */
	public schueler : List<number> = new ArrayList<number>();

	/**
	 * Die Liste der IDs der {@link StundenplanLehrer}-Objekte, die dem Klassenunterricht zugeordnet sind.
	 */
	public lehrer : List<number> = new ArrayList<number>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplan.StundenplanKlassenunterricht';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanKlassenunterricht'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanKlassenunterricht {
		const obj = JSON.parse(json);
		const result = new StundenplanKlassenunterricht();
		if (obj.idKlasse === undefined)
			 throw new Error('invalid json format, missing attribute idKlasse');
		result.idKlasse = obj.idKlasse;
		if (obj.idFach === undefined)
			 throw new Error('invalid json format, missing attribute idFach');
		result.idFach = obj.idFach;
		if (obj.bezeichnung === undefined)
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.wochenstunden === undefined)
			 throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		if ((obj.schienen !== undefined) && (obj.schienen !== null)) {
			for (const elem of obj.schienen) {
				result.schienen?.add(elem);
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

	public static transpilerToJSON(obj : StundenplanKlassenunterricht) : string {
		let result = '{';
		result += '"idKlasse" : ' + obj.idKlasse + ',';
		result += '"idFach" : ' + obj.idFach + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden + ',';
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

	public static transpilerToJSONPatch(obj : Partial<StundenplanKlassenunterricht>) : string {
		let result = '{';
		if (obj.idKlasse !== undefined) {
			result += '"idKlasse" : ' + obj.idKlasse + ',';
		}
		if (obj.idFach !== undefined) {
			result += '"idFach" : ' + obj.idFach + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		}
		if (obj.wochenstunden !== undefined) {
			result += '"wochenstunden" : ' + obj.wochenstunden + ',';
		}
		if (obj.schienen !== undefined) {
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
		if (obj.schueler !== undefined) {
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
		if (obj.lehrer !== undefined) {
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

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanKlassenunterricht(obj : unknown) : StundenplanKlassenunterricht {
	return obj as StundenplanKlassenunterricht;
}
