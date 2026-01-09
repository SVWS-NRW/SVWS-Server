import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class KlassenStatistikGesamt extends JavaObject {

	/**
	 * Die ID der Klasse.
	 */
	public id: number = 0;

	/**
	 * Die ID des Schuljahresabschnittes des Kurses.
	 */
	public idSchuljahresabschnitt: number = 0;

	/**
	 * Das Kürzel der Klasse.
	 */
	public kuerzel: string | null = null;

	/**
	 * Die ID des zugeordneten Jahrgangs, dem die Klasse zugeordnet ist, null falls es eine Jahrgangsübergreifende Klasse ist
	 */
	public idJahrgang: number | null = null;

	/**
	 * Das Kürzel für die Parallelität der Klasse innerhalb des Jahrgangs (A-Z).
	 */
	public parallelitaet: string | null = null;

	/**
	 * Die Sortierreihenfolge des Klassenlisten-Eintrags.
	 */
	public sortierung: number = 0;

	/**
	 * Die Liste der IDs der Klassenleitungen der Klasse.
	 */
	public klassenLeitungen: List<number> = new ArrayList<number>();

	/**
	 * Die Schüler der Klasse.
	 */
	public idsSchueler: List<number> = new ArrayList<number>();

	/**
	 * Adressmerkmal des Teilstandorts für die Klasse
	 */
	public teilstandort: string = "";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.statistik.KlassenStatistikGesamt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.statistik.KlassenStatistikGesamt'].includes(name);
	}

	public static readonly class = new Class<KlassenStatistikGesamt>('de.svws_nrw.asd.data.statistik.KlassenStatistikGesamt');

	public static transpilerFromJSON(json: string): KlassenStatistikGesamt {
		const obj = JSON.parse(json) as Partial<KlassenStatistikGesamt>;
		const result = new KlassenStatistikGesamt();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idSchuljahresabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		result.kuerzel = (obj.kuerzel === undefined) ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.idJahrgang = (obj.idJahrgang === undefined) ? null : obj.idJahrgang === null ? null : obj.idJahrgang;
		result.parallelitaet = (obj.parallelitaet === undefined) ? null : obj.parallelitaet === null ? null : obj.parallelitaet;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (obj.klassenLeitungen !== undefined) {
			for (const elem of obj.klassenLeitungen) {
				result.klassenLeitungen.add(elem);
			}
		}
		if (obj.idsSchueler !== undefined) {
			for (const elem of obj.idsSchueler) {
				result.idsSchueler.add(elem);
			}
		}
		if (obj.teilstandort === undefined)
			throw new Error('invalid json format, missing attribute teilstandort');
		result.teilstandort = obj.teilstandort;
		return result;
	}

	public static transpilerToJSON(obj: KlassenStatistikGesamt): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"idJahrgang" : ' + ((obj.idJahrgang === null) ? 'null' : obj.idJahrgang.toString()) + ',';
		result += '"parallelitaet" : ' + ((obj.parallelitaet === null) ? 'null' : JSON.stringify(obj.parallelitaet)) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"klassenLeitungen" : [ ';
		for (let i = 0; i < obj.klassenLeitungen.size(); i++) {
			const elem = obj.klassenLeitungen.get(i);
			result += elem.toString();
			if (i < obj.klassenLeitungen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"idsSchueler" : [ ';
		for (let i = 0; i < obj.idsSchueler.size(); i++) {
			const elem = obj.idsSchueler.get(i);
			result += elem.toString();
			if (i < obj.idsSchueler.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"teilstandort" : ' + JSON.stringify(obj.teilstandort) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<KlassenStatistikGesamt>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idSchuljahresabschnitt !== undefined) {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		}
		if (obj.idJahrgang !== undefined) {
			result += '"idJahrgang" : ' + ((obj.idJahrgang === null) ? 'null' : obj.idJahrgang.toString()) + ',';
		}
		if (obj.parallelitaet !== undefined) {
			result += '"parallelitaet" : ' + ((obj.parallelitaet === null) ? 'null' : JSON.stringify(obj.parallelitaet)) + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.klassenLeitungen !== undefined) {
			result += '"klassenLeitungen" : [ ';
			for (let i = 0; i < obj.klassenLeitungen.size(); i++) {
				const elem = obj.klassenLeitungen.get(i);
				result += elem.toString();
				if (i < obj.klassenLeitungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.idsSchueler !== undefined) {
			result += '"idsSchueler" : [ ';
			for (let i = 0; i < obj.idsSchueler.size(); i++) {
				const elem = obj.idsSchueler.get(i);
				result += elem.toString();
				if (i < obj.idsSchueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.teilstandort !== undefined) {
			result += '"teilstandort" : ' + JSON.stringify(obj.teilstandort) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_statistik_KlassenStatistikGesamt(obj: unknown): KlassenStatistikGesamt {
	return obj as KlassenStatistikGesamt;
}
