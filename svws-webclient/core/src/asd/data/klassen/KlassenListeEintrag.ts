import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class KlassenListeEintrag extends JavaObject {

	/**
	 * Die ID der Klasse.
	 */
	public id: number = 0;

	/**
	 * Die ID des Schuljahresabschnittes.
	 */
	public idSchuljahresabschnitt: number = 0;

	/**
	 * Die ID des Jahrgangs.
	 */
	public idJahrgang: number | null = null;

	/**
	 * Das Kürzel der Klasse.
	 */
	public kuerzel: string | null = null;

	/**
	 * Eine zusätzliche Beschreibung zu der Klasse
	 */
	public beschreibung: string = "";

	/**
	 * Das Kürzel für die Parallelität der Klasse innerhalb des Jahrgangs (A-Z).
	 */
	public parallelitaet: string | null = null;

	/**
	 * Gibt die Anzahl der Schüler, die dieser Klasse zugeordnet sind zurück.
	 */
	public anzahlZugeordneteSchueler: number = 0;

	/**
	 * Die Liste der IDs der Klassenleitungen der Klasse.
	 */
	public idsKlassenleitungen: List<number> = new ArrayList<number>();

	/**
	 * Die Sortierreihenfolge des Klassenlisten-Eintrags.
	 */
	public sortierung: number = 0;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.klassen.KlassenListeEintrag';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.klassen.KlassenListeEintrag'].includes(name);
	}

	public static readonly class = new Class<KlassenListeEintrag>('de.svws_nrw.asd.data.klassen.KlassenListeEintrag');

	public static transpilerFromJSON(json: string): KlassenListeEintrag {
		const obj = JSON.parse(json) as Partial<KlassenListeEintrag>;
		const result = new KlassenListeEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idSchuljahresabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		result.idJahrgang = (obj.idJahrgang === undefined) ? null : obj.idJahrgang === null ? null : obj.idJahrgang;
		result.kuerzel = (obj.kuerzel === undefined) ? null : obj.kuerzel === null ? null : obj.kuerzel;
		if (obj.beschreibung === undefined)
			throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = obj.beschreibung;
		result.parallelitaet = (obj.parallelitaet === undefined) ? null : obj.parallelitaet === null ? null : obj.parallelitaet;
		if (obj.anzahlZugeordneteSchueler === undefined)
			throw new Error('invalid json format, missing attribute anzahlZugeordneteSchueler');
		result.anzahlZugeordneteSchueler = obj.anzahlZugeordneteSchueler;
		if (obj.idsKlassenleitungen !== undefined) {
			for (const elem of obj.idsKlassenleitungen) {
				result.idsKlassenleitungen.add(elem);
			}
		}
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		return result;
	}

	public static transpilerToJSON(obj: KlassenListeEintrag): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		result += '"idJahrgang" : ' + ((obj.idJahrgang === null) ? 'null' : obj.idJahrgang.toString()) + ',';
		result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		result += '"parallelitaet" : ' + ((obj.parallelitaet === null) ? 'null' : JSON.stringify(obj.parallelitaet)) + ',';
		result += '"anzahlZugeordneteSchueler" : ' + obj.anzahlZugeordneteSchueler.toString() + ',';
		result += '"idsKlassenleitungen" : [ ';
		for (let i = 0; i < obj.idsKlassenleitungen.size(); i++) {
			const elem = obj.idsKlassenleitungen.get(i);
			result += elem.toString();
			if (i < obj.idsKlassenleitungen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<KlassenListeEintrag>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idSchuljahresabschnitt !== undefined) {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		}
		if (obj.idJahrgang !== undefined) {
			result += '"idJahrgang" : ' + ((obj.idJahrgang === null) ? 'null' : obj.idJahrgang.toString()) + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		}
		if (obj.beschreibung !== undefined) {
			result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		}
		if (obj.parallelitaet !== undefined) {
			result += '"parallelitaet" : ' + ((obj.parallelitaet === null) ? 'null' : JSON.stringify(obj.parallelitaet)) + ',';
		}
		if (obj.anzahlZugeordneteSchueler !== undefined) {
			result += '"anzahlZugeordneteSchueler" : ' + obj.anzahlZugeordneteSchueler.toString() + ',';
		}
		if (obj.idsKlassenleitungen !== undefined) {
			result += '"idsKlassenleitungen" : [ ';
			for (let i = 0; i < obj.idsKlassenleitungen.size(); i++) {
				const elem = obj.idsKlassenleitungen.get(i);
				result += elem.toString();
				if (i < obj.idsKlassenleitungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_klassen_KlassenListeEintrag(obj: unknown): KlassenListeEintrag {
	return obj as KlassenListeEintrag;
}
