import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { Schueler, cast_de_nrw_schule_svws_core_data_schueler_Schueler } from '../../../core/data/schueler/Schueler';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class KursDaten extends JavaObject {

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
	public idJahrgaenge : Vector<number> = new Vector();

	/**
	 * Die ID des Faches, dem der Kurs zugeordnet ist
	 */
	public idFach : number = 0;

	/**
	 * Die ID des Kurslehrers.
	 */
	public lehrer : number | null = null;

	/**
	 * Die Sortierreihenfolge des Jahrgangslisten-Eintrags.
	 */
	public sortierung : number = 0;

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 */
	public istSichtbar : boolean = false;

	/**
	 * Die Schüler des Kurses.
	 */
	public schueler : List<Schueler> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kurse.KursDaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): KursDaten {
		const obj = JSON.parse(json);
		const result = new KursDaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.idSchuljahresabschnitt === "undefined")
			 throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (!((obj.idJahrgaenge === undefined) || (obj.idJahrgaenge === null))) {
			for (const elem of obj.idJahrgaenge) {
				result.idJahrgaenge?.add(elem);
			}
		}
		if (typeof obj.idFach === "undefined")
			 throw new Error('invalid json format, missing attribute idFach');
		result.idFach = obj.idFach;
		result.lehrer = typeof obj.lehrer === "undefined" ? null : obj.lehrer === null ? null : obj.lehrer;
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (typeof obj.istSichtbar === "undefined")
			 throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		if (!((obj.schueler === undefined) || (obj.schueler === null))) {
			for (const elem of obj.schueler) {
				result.schueler?.add(Schueler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : KursDaten) : string {
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
		result += '"sortierung" : ' + obj.sortierung + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar + ',';
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
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KursDaten>) : string {
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
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + obj.sortierung + ',';
		}
		if (typeof obj.istSichtbar !== "undefined") {
			result += '"istSichtbar" : ' + obj.istSichtbar + ',';
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
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_kurse_KursDaten(obj : unknown) : KursDaten {
	return obj as KursDaten;
}
