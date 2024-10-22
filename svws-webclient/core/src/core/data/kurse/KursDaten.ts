import { JavaObject } from '../../../java/lang/JavaObject';
import { Schueler } from '../../../core/data/schueler/Schueler';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

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
	public idJahrgaenge : List<number> = new ArrayList<number>();

	/**
	 * Die ID des Faches, dem der Kurs zugeordnet ist
	 */
	public idFach : number = 0;

	/**
	 * Die ID des Kurslehrers.
	 */
	public lehrer : number | null = null;

	/**
	 * Die allgemeine Kursart, welche zur Filterung der speziellen Kursarten verwendet wird.
	 */
	public kursartAllg : string = "";

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
	public schueler : List<Schueler> = new ArrayList<Schueler>();

	/**
	 * Die Nummern der Kurs-Schienen, in welchen sich der Kurs befindet - sofern eine Schiene zugeordnet wurde
	 */
	public schienen : List<number> = new ArrayList<number>();

	/**
	 * Die Wochenstunden des Kurses.
	 */
	public wochenstunden : number = -1;

	/**
	 * Die Wochenstunden des Kurslehrers in dem Kurs.
	 */
	public wochenstundenLehrer : number = -1;

	/**
	 * Die Fortschreibungsart des Kurses (Keine, nur Definition mit Jahrgang behalten oder hochschreiben oder komplett)
	 */
	public idKursFortschreibungsart : number = 0;

	/**
	 * Die Schulnummer des Kurses, falls der Kurs an einer anderes Schule stattfindet (z.B. im Rahmen einer Kooperation).
	 */
	public schulnummer : number | null = null;

	/**
	 * Gibt an, ob der Kurs epochal unterrichtet wird.
	 */
	public istEpochalunterricht : boolean = false;

	/**
	 * Ggf. die Zeugnisbezeichnung des Kurses
	 */
	public bezeichnungZeugnis : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.kurse.KursDaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.kurse.KursDaten'].includes(name);
	}

	public static class = new Class<KursDaten>('de.svws_nrw.core.data.kurse.KursDaten');

	public static transpilerFromJSON(json : string): KursDaten {
		const obj = JSON.parse(json) as Partial<KursDaten>;
		const result = new KursDaten();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idSchuljahresabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.idJahrgaenge !== undefined) {
			for (const elem of obj.idJahrgaenge) {
				result.idJahrgaenge.add(elem);
			}
		}
		if (obj.idFach === undefined)
			throw new Error('invalid json format, missing attribute idFach');
		result.idFach = obj.idFach;
		result.lehrer = (obj.lehrer === undefined) ? null : obj.lehrer === null ? null : obj.lehrer;
		if (obj.kursartAllg === undefined)
			throw new Error('invalid json format, missing attribute kursartAllg');
		result.kursartAllg = obj.kursartAllg;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (obj.istSichtbar === undefined)
			throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		if (obj.schueler !== undefined) {
			for (const elem of obj.schueler) {
				result.schueler.add(Schueler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schienen !== undefined) {
			for (const elem of obj.schienen) {
				result.schienen.add(elem);
			}
		}
		if (obj.wochenstunden === undefined)
			throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		if (obj.wochenstundenLehrer === undefined)
			throw new Error('invalid json format, missing attribute wochenstundenLehrer');
		result.wochenstundenLehrer = obj.wochenstundenLehrer;
		if (obj.idKursFortschreibungsart === undefined)
			throw new Error('invalid json format, missing attribute idKursFortschreibungsart');
		result.idKursFortschreibungsart = obj.idKursFortschreibungsart;
		result.schulnummer = (obj.schulnummer === undefined) ? null : obj.schulnummer === null ? null : obj.schulnummer;
		if (obj.istEpochalunterricht === undefined)
			throw new Error('invalid json format, missing attribute istEpochalunterricht');
		result.istEpochalunterricht = obj.istEpochalunterricht;
		result.bezeichnungZeugnis = (obj.bezeichnungZeugnis === undefined) ? null : obj.bezeichnungZeugnis === null ? null : obj.bezeichnungZeugnis;
		return result;
	}

	public static transpilerToJSON(obj : KursDaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"idJahrgaenge" : [ ';
		for (let i = 0; i < obj.idJahrgaenge.size(); i++) {
			const elem = obj.idJahrgaenge.get(i);
			result += elem.toString();
			if (i < obj.idJahrgaenge.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"idFach" : ' + obj.idFach.toString() + ',';
		result += '"lehrer" : ' + ((obj.lehrer === null) ? 'null' : obj.lehrer.toString()) + ',';
		result += '"kursartAllg" : ' + JSON.stringify(obj.kursartAllg) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		result += '"schueler" : [ ';
		for (let i = 0; i < obj.schueler.size(); i++) {
			const elem = obj.schueler.get(i);
			result += Schueler.transpilerToJSON(elem);
			if (i < obj.schueler.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schienen" : [ ';
		for (let i = 0; i < obj.schienen.size(); i++) {
			const elem = obj.schienen.get(i);
			result += elem.toString();
			if (i < obj.schienen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		result += '"wochenstundenLehrer" : ' + obj.wochenstundenLehrer.toString() + ',';
		result += '"idKursFortschreibungsart" : ' + obj.idKursFortschreibungsart.toString() + ',';
		result += '"schulnummer" : ' + ((obj.schulnummer === null) ? 'null' : obj.schulnummer.toString()) + ',';
		result += '"istEpochalunterricht" : ' + obj.istEpochalunterricht.toString() + ',';
		result += '"bezeichnungZeugnis" : ' + ((obj.bezeichnungZeugnis === null) ? 'null' : JSON.stringify(obj.bezeichnungZeugnis)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KursDaten>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idSchuljahresabschnitt !== undefined) {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.idJahrgaenge !== undefined) {
			result += '"idJahrgaenge" : [ ';
			for (let i = 0; i < obj.idJahrgaenge.size(); i++) {
				const elem = obj.idJahrgaenge.get(i);
				result += elem.toString();
				if (i < obj.idJahrgaenge.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.idFach !== undefined) {
			result += '"idFach" : ' + obj.idFach.toString() + ',';
		}
		if (obj.lehrer !== undefined) {
			result += '"lehrer" : ' + ((obj.lehrer === null) ? 'null' : obj.lehrer.toString()) + ',';
		}
		if (obj.kursartAllg !== undefined) {
			result += '"kursartAllg" : ' + JSON.stringify(obj.kursartAllg) + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.istSichtbar !== undefined) {
			result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		}
		if (obj.schueler !== undefined) {
			result += '"schueler" : [ ';
			for (let i = 0; i < obj.schueler.size(); i++) {
				const elem = obj.schueler.get(i);
				result += Schueler.transpilerToJSON(elem);
				if (i < obj.schueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schienen !== undefined) {
			result += '"schienen" : [ ';
			for (let i = 0; i < obj.schienen.size(); i++) {
				const elem = obj.schienen.get(i);
				result += elem.toString();
				if (i < obj.schienen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.wochenstunden !== undefined) {
			result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		}
		if (obj.wochenstundenLehrer !== undefined) {
			result += '"wochenstundenLehrer" : ' + obj.wochenstundenLehrer.toString() + ',';
		}
		if (obj.idKursFortschreibungsart !== undefined) {
			result += '"idKursFortschreibungsart" : ' + obj.idKursFortschreibungsart.toString() + ',';
		}
		if (obj.schulnummer !== undefined) {
			result += '"schulnummer" : ' + ((obj.schulnummer === null) ? 'null' : obj.schulnummer.toString()) + ',';
		}
		if (obj.istEpochalunterricht !== undefined) {
			result += '"istEpochalunterricht" : ' + obj.istEpochalunterricht.toString() + ',';
		}
		if (obj.bezeichnungZeugnis !== undefined) {
			result += '"bezeichnungZeugnis" : ' + ((obj.bezeichnungZeugnis === null) ? 'null' : JSON.stringify(obj.bezeichnungZeugnis)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_kurse_KursDaten(obj : unknown) : KursDaten {
	return obj as KursDaten;
}
