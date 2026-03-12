import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { AnkreuzkompetenzJahrgangszuordnung } from '../../../core/data/schule/AnkreuzkompetenzJahrgangszuordnung';

export class Ankreuzkompetenz extends JavaObject {

	/**
	 * Die ID des Eintrags für die Ankreuzkompetenz
	 */
	public id: number = -1;

	/**
	 * Die ID des Faches, die zur Ankreuzkompetenz gehört, sofern IstASV den Wert false hat. Ansonsten wird diese ID des Faches ein null übergeben
	 */
	public idFach: number | null = null;

	/**
	 * Gibt an, falls die Fach_ID null ist, ob es sich bei der Ankreuzkompetenz um eine Floskel zum Arbeits- und Sozialverhalten handelt (true) oder nicht (false).
	 */
	public istASV: boolean = false;

	/**
	 * Schulgliederung zu der die Ankreuzkompetenz gehört (nur wichtig bei BK)
	 */
	public schulgliederung: string | null = null;

	/**
	 * Der Text der Ankreuzkompetenz
	 */
	public floskelText: string = "";

	/**
	 * Gibt an in welchem Abschnitten (1. HJ, 2. HJ oder beide) die Ankreuzkompetenz benutzt wird.
	 */
	public abschnitt: number = 0;

	/**
	 * Gibt an, ob die Ankreuzkompetenz aktiv ist.
	 */
	public istAktiv: boolean = false;

	/**
	 * Gibt an, ob die Ankreuzkompetenz in der Anwendung sichtbar sein soll oder nicht
	 */
	public istSichtbar: boolean = false;

	/**
	 * Gibt einen Wert für die Sortierung des Faches der Ankreuzkompetenz an.
	 */
	public fachSortierung: number = 0;

	/**
	 * Gibt einen Wert für die Sortierung der Ankreuzkompetenz an.
	 */
	public sortierung: number = 0;

	/**
	 * Gibt an, ob der Eintrag in anderen Datenbanktabellen referenziert ist oder nicht.
	 */
	public referenziertInAnderenTabellen: boolean = false;

	/**
	 * Die Zuordnung der Jahrgänge zu der Ankreuzkompetenzen.
	 */
	public readonly jahrgaengezuordnung: List<AnkreuzkompetenzJahrgangszuordnung> = new ArrayList<AnkreuzkompetenzJahrgangszuordnung>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.Ankreuzkompetenz';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schule.Ankreuzkompetenz'].includes(name);
	}

	public static readonly class = new Class<Ankreuzkompetenz>('de.svws_nrw.core.data.schule.Ankreuzkompetenz');

	public static transpilerFromJSON(json: string): Ankreuzkompetenz {
		const obj = JSON.parse(json) as Partial<Ankreuzkompetenz>;
		const result = new Ankreuzkompetenz();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.idFach = (obj.idFach === undefined) ? null : obj.idFach === null ? null : obj.idFach;
		if (obj.istASV === undefined)
			throw new Error('invalid json format, missing attribute istASV');
		result.istASV = obj.istASV;
		result.schulgliederung = (obj.schulgliederung === undefined) ? null : obj.schulgliederung === null ? null : obj.schulgliederung;
		if (obj.floskelText === undefined)
			throw new Error('invalid json format, missing attribute floskelText');
		result.floskelText = obj.floskelText;
		if (obj.abschnitt === undefined)
			throw new Error('invalid json format, missing attribute abschnitt');
		result.abschnitt = obj.abschnitt;
		if (obj.istAktiv === undefined)
			throw new Error('invalid json format, missing attribute istAktiv');
		result.istAktiv = obj.istAktiv;
		if (obj.istSichtbar === undefined)
			throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		if (obj.fachSortierung === undefined)
			throw new Error('invalid json format, missing attribute fachSortierung');
		result.fachSortierung = obj.fachSortierung;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (obj.referenziertInAnderenTabellen === undefined)
			throw new Error('invalid json format, missing attribute referenziertInAnderenTabellen');
		result.referenziertInAnderenTabellen = obj.referenziertInAnderenTabellen;
		if (obj.jahrgaengezuordnung !== undefined) {
			for (const elem of obj.jahrgaengezuordnung) {
				result.jahrgaengezuordnung.add(AnkreuzkompetenzJahrgangszuordnung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: Ankreuzkompetenz): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idFach" : ' + ((obj.idFach === null) ? 'null' : obj.idFach.toString()) + ',';
		result += '"istASV" : ' + obj.istASV.toString() + ',';
		result += '"schulgliederung" : ' + ((obj.schulgliederung === null) ? 'null' : JSON.stringify(obj.schulgliederung)) + ',';
		result += '"floskelText" : ' + JSON.stringify(obj.floskelText) + ',';
		result += '"abschnitt" : ' + obj.abschnitt.toString() + ',';
		result += '"istAktiv" : ' + obj.istAktiv.toString() + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		result += '"fachSortierung" : ' + obj.fachSortierung.toString() + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"referenziertInAnderenTabellen" : ' + obj.referenziertInAnderenTabellen.toString() + ',';
		result += '"jahrgaengezuordnung" : [ ';
		for (let i = 0; i < obj.jahrgaengezuordnung.size(); i++) {
			const elem = obj.jahrgaengezuordnung.get(i);
			result += AnkreuzkompetenzJahrgangszuordnung.transpilerToJSON(elem);
			if (i < obj.jahrgaengezuordnung.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<Ankreuzkompetenz>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idFach !== undefined) {
			result += '"idFach" : ' + ((obj.idFach === null) ? 'null' : obj.idFach.toString()) + ',';
		}
		if (obj.istASV !== undefined) {
			result += '"istASV" : ' + obj.istASV.toString() + ',';
		}
		if (obj.schulgliederung !== undefined) {
			result += '"schulgliederung" : ' + ((obj.schulgliederung === null) ? 'null' : JSON.stringify(obj.schulgliederung)) + ',';
		}
		if (obj.floskelText !== undefined) {
			result += '"floskelText" : ' + JSON.stringify(obj.floskelText) + ',';
		}
		if (obj.abschnitt !== undefined) {
			result += '"abschnitt" : ' + obj.abschnitt.toString() + ',';
		}
		if (obj.istAktiv !== undefined) {
			result += '"istAktiv" : ' + obj.istAktiv.toString() + ',';
		}
		if (obj.istSichtbar !== undefined) {
			result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		}
		if (obj.fachSortierung !== undefined) {
			result += '"fachSortierung" : ' + obj.fachSortierung.toString() + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.referenziertInAnderenTabellen !== undefined) {
			result += '"referenziertInAnderenTabellen" : ' + obj.referenziertInAnderenTabellen.toString() + ',';
		}
		if (obj.jahrgaengezuordnung !== undefined) {
			result += '"jahrgaengezuordnung" : [ ';
			for (let i = 0; i < obj.jahrgaengezuordnung.size(); i++) {
				const elem = obj.jahrgaengezuordnung.get(i);
				result += AnkreuzkompetenzJahrgangszuordnung.transpilerToJSON(elem);
				if (i < obj.jahrgaengezuordnung.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_Ankreuzkompetenz(obj: unknown): Ankreuzkompetenz {
	return obj as Ankreuzkompetenz;
}
