import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class KlassenDatenMinimal extends JavaObject {

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
	public beschreibung: string | null = null;

	/**
	 * Das Kürzel für die Parallelität der Klasse innerhalb des Jahrgangs (A-Z).
	 */
	public parallelitaet: string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.klassen.KlassenDatenMinimal';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.klassen.KlassenDatenMinimal'].includes(name);
	}

	public static readonly class = new Class<KlassenDatenMinimal>('de.svws_nrw.asd.data.klassen.KlassenDatenMinimal');

	public static transpilerFromJSON(json: string): KlassenDatenMinimal {
		const obj = JSON.parse(json) as Partial<KlassenDatenMinimal>;
		const result = new KlassenDatenMinimal();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idSchuljahresabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		result.idJahrgang = (obj.idJahrgang === undefined) ? null : obj.idJahrgang === null ? null : obj.idJahrgang;
		result.kuerzel = (obj.kuerzel === undefined) ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.beschreibung = (obj.beschreibung === undefined) ? null : obj.beschreibung === null ? null : obj.beschreibung;
		result.parallelitaet = (obj.parallelitaet === undefined) ? null : obj.parallelitaet === null ? null : obj.parallelitaet;
		return result;
	}

	public static transpilerToJSON(obj: KlassenDatenMinimal): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		result += '"idJahrgang" : ' + ((obj.idJahrgang === null) ? 'null' : obj.idJahrgang.toString()) + ',';
		result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"beschreibung" : ' + ((obj.beschreibung === null) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		result += '"parallelitaet" : ' + ((obj.parallelitaet === null) ? 'null' : JSON.stringify(obj.parallelitaet)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<KlassenDatenMinimal>): string {
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
			result += '"beschreibung" : ' + ((obj.beschreibung === null) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		}
		if (obj.parallelitaet !== undefined) {
			result += '"parallelitaet" : ' + ((obj.parallelitaet === null) ? 'null' : JSON.stringify(obj.parallelitaet)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_klassen_KlassenDatenMinimal(obj: unknown): KlassenDatenMinimal {
	return obj as KlassenDatenMinimal;
}
