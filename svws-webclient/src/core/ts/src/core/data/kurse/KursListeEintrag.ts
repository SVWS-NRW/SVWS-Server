import { JavaObject } from '../../../java/lang/JavaObject';
import { Schueler } from '../../../core/data/schueler/Schueler';
import { ArrayList } from '../../../java/util/ArrayList';
import { List } from '../../../java/util/List';

export class KursListeEintrag extends JavaObject {

	/**
	 * Die ID des Kurses.
	 */
	public id : number = 0;

	/**
	 * Die ID des Schuljahresabschnittes des Kurses.
	 */
	public idSchuljahresabschnitt : number = 0;

	/**
	 * Das Kürzel des Kurses.
	 */
	public kuerzel : string = "";

	/**
	 * Die IDs der Jahrgänge, denen der Kurs zugeordnet ist
	 */
	public idJahrgaenge : List<number> = new ArrayList();

	/**
	 * Die ID des Faches, dem der Kurs zugeordnet ist
	 */
	public idFach : number = 0;

	/**
	 * Die ID des Kurslehrers.
	 */
	public lehrer : number | null = null;

	/**
	 * Die Schüler des Kurses.
	 */
	public schueler : List<Schueler> = new ArrayList();

	/**
	 * Die Sortierreihenfolge des Jahrgangslisten-Eintrags.
	 */
	public sortierung : number = 0;

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 */
	public istSichtbar : boolean = false;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.kurse.KursListeEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): KursListeEintrag {
		const obj = JSON.parse(json);
		const result = new KursListeEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.idSchuljahresabschnitt === "undefined")
			 throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if ((obj.idJahrgaenge !== undefined) && (obj.idJahrgaenge !== null)) {
			for (const elem of obj.idJahrgaenge) {
				result.idJahrgaenge?.add(elem);
			}
		}
		if (typeof obj.idFach === "undefined")
			 throw new Error('invalid json format, missing attribute idFach');
		result.idFach = obj.idFach;
		result.lehrer = typeof obj.lehrer === "undefined" ? null : obj.lehrer === null ? null : obj.lehrer;
		if ((obj.schueler !== undefined) && (obj.schueler !== null)) {
			for (const elem of obj.schueler) {
				result.schueler?.add(Schueler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (typeof obj.istSichtbar === "undefined")
			 throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		return result;
	}

	public static transpilerToJSON(obj : KursListeEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel! + '"' + ',';
		if (!obj.idJahrgaenge) {
			result += '"idJahrgaenge" : []';
		} else {
			result += '"idJahrgaenge" : [ ';
			for (let i = 0; i < obj.idJahrgaenge.size(); i++) {
				const elem = obj.idJahrgaenge.get(i);
				result += elem;
				if (i < obj.idJahrgaenge.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"idFach" : ' + obj.idFach + ',';
		result += '"lehrer" : ' + ((!obj.lehrer) ? 'null' : obj.lehrer) + ',';
		if (!obj.schueler) {
			result += '"schueler" : []';
		} else {
			result += '"schueler" : [ ';
			for (let i = 0; i < obj.schueler.size(); i++) {
				const elem = obj.schueler.get(i);
				result += Schueler.transpilerToJSON(elem);
				if (i < obj.schueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"sortierung" : ' + obj.sortierung + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KursListeEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.idSchuljahresabschnitt !== "undefined") {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel + '"' + ',';
		}
		if (typeof obj.idJahrgaenge !== "undefined") {
			if (!obj.idJahrgaenge) {
				result += '"idJahrgaenge" : []';
			} else {
				result += '"idJahrgaenge" : [ ';
				for (let i = 0; i < obj.idJahrgaenge.size(); i++) {
					const elem = obj.idJahrgaenge.get(i);
					result += elem;
					if (i < obj.idJahrgaenge.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.idFach !== "undefined") {
			result += '"idFach" : ' + obj.idFach + ',';
		}
		if (typeof obj.lehrer !== "undefined") {
			result += '"lehrer" : ' + ((!obj.lehrer) ? 'null' : obj.lehrer) + ',';
		}
		if (typeof obj.schueler !== "undefined") {
			if (!obj.schueler) {
				result += '"schueler" : []';
			} else {
				result += '"schueler" : [ ';
				for (let i = 0; i < obj.schueler.size(); i++) {
					const elem = obj.schueler.get(i);
					result += Schueler.transpilerToJSON(elem);
					if (i < obj.schueler.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + obj.sortierung + ',';
		}
		if (typeof obj.istSichtbar !== "undefined") {
			result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_kurse_KursListeEintrag(obj : unknown) : KursListeEintrag {
	return obj as KursListeEintrag;
}
