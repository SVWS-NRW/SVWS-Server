import { JavaObject } from '../../../java/lang/JavaObject';
import { Schueler } from '../../../core/data/schueler/Schueler';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';

export class KlassenDaten extends JavaObject {

	/**
	 * Die ID der Klasse.
	 */
	public id : number = 0;

	/**
	 * Das Kürzel der Klasse.
	 */
	public kuerzel : string | null = null;

	/**
	 * Die ID des zugeordneten Jahrgangs, dem die Klasse zugeordnet ist
	 */
	public idJahrgang : number | null = null;

	/**
	 * Das Kürzel für die Parallelität der Klasse innerhalb des Jahrgangs (A-Z).
	 */
	public parallelitaet : string | null = null;

	/**
	 * Die Sortierreihenfolge des Jahrgangslisten-Eintrags.
	 */
	public sortierung : number = 0;

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 */
	public istSichtbar : boolean = false;

	/**
	 * Die Liste der IDs der Klassenleitungen der Klasse.
	 */
	public klassenLeitungen : List<number> = new ArrayList();

	/**
	 * Die Schüler der Klasse.
	 */
	public schueler : List<Schueler> = new ArrayList();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.klassen.KlassenDaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.klassen.KlassenDaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): KlassenDaten {
		const obj = JSON.parse(json);
		const result = new KlassenDaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = typeof obj.kuerzel === "undefined" ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.idJahrgang = typeof obj.idJahrgang === "undefined" ? null : obj.idJahrgang === null ? null : obj.idJahrgang;
		result.parallelitaet = typeof obj.parallelitaet === "undefined" ? null : obj.parallelitaet === null ? null : obj.parallelitaet;
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (typeof obj.istSichtbar === "undefined")
			 throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		if ((obj.klassenLeitungen !== undefined) && (obj.klassenLeitungen !== null)) {
			for (const elem of obj.klassenLeitungen) {
				result.klassenLeitungen?.add(elem);
			}
		}
		if ((obj.schueler !== undefined) && (obj.schueler !== null)) {
			for (const elem of obj.schueler) {
				result.schueler?.add(Schueler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : KlassenDaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"idJahrgang" : ' + ((!obj.idJahrgang) ? 'null' : obj.idJahrgang) + ',';
		result += '"parallelitaet" : ' + ((!obj.parallelitaet) ? 'null' : JSON.stringify(obj.parallelitaet)) + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		if (!obj.klassenLeitungen) {
			result += '"klassenLeitungen" : []';
		} else {
			result += '"klassenLeitungen" : [ ';
			for (let i = 0; i < obj.klassenLeitungen.size(); i++) {
				const elem = obj.klassenLeitungen.get(i);
				result += elem;
				if (i < obj.klassenLeitungen.size() - 1)
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

	public static transpilerToJSONPatch(obj : Partial<KlassenDaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		}
		if (typeof obj.idJahrgang !== "undefined") {
			result += '"idJahrgang" : ' + ((!obj.idJahrgang) ? 'null' : obj.idJahrgang) + ',';
		}
		if (typeof obj.parallelitaet !== "undefined") {
			result += '"parallelitaet" : ' + ((!obj.parallelitaet) ? 'null' : JSON.stringify(obj.parallelitaet)) + ',';
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + obj.sortierung + ',';
		}
		if (typeof obj.istSichtbar !== "undefined") {
			result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		}
		if (typeof obj.klassenLeitungen !== "undefined") {
			if (!obj.klassenLeitungen) {
				result += '"klassenLeitungen" : []';
			} else {
				result += '"klassenLeitungen" : [ ';
				for (let i = 0; i < obj.klassenLeitungen.size(); i++) {
					const elem = obj.klassenLeitungen.get(i);
					result += elem;
					if (i < obj.klassenLeitungen.size() - 1)
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

export function cast_de_svws_nrw_core_data_klassen_KlassenDaten(obj : unknown) : KlassenDaten {
	return obj as KlassenDaten;
}
