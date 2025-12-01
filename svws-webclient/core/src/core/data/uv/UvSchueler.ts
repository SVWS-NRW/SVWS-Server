import { LongPair } from '../../../core/data/uv/LongPair';
import { JavaLong } from '../../../java/lang/JavaLong';
import { ArrayList } from '../../../java/util/ArrayList';
import { JavaObject } from '../../../java/lang/JavaObject';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class UvSchueler extends JavaObject {

	/**
	 * Die ID des Eintrags.
	 */
	public idPlanungsabschnitt: number = -1;

	/**
	 * Die ID des Schülers.
	 */
	public idSchueler: number = -1;

	/**
	 * Die ID des Jahrgangs.
	 */
	public idJahrgang: number | null = null;

	/**
	 * Die ID der Klasse.
	 */
	public idKlasse: number | null = null;


	/**
	 * Default-Konstruktor
	 */
	public constructor() {
		super();
	}

	/**
	 * Vergleicht, ob das aktuelle Objekt dasselbe ist wie ein anderes übergebenes Objekt.
	 *
	 * @param another das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	public equals(another: unknown | null): boolean {
		return (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvSchueler')))) && (this.idPlanungsabschnitt === (another as unknown as UvSchueler).idPlanungsabschnitt) && (this.idSchueler === (another as unknown as UvSchueler).idSchueler);
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der idVorgabe.
	 *
	 * @return den HashCode
	 */
	public hashCode(): number {
		return (31 * JavaLong.hashCode((this.idPlanungsabschnitt))) + JavaLong.hashCode((this.idSchueler));
	}

	/**
	 * Gibt eine String-Repräsentation des Objekts zurück.
	 */
	public toString(): string | null {
		return this.idPlanungsabschnitt + "-" + this.idSchueler;
	}

	/**
	 * Erstellt aus einer Liste von UvSchueler-Objekten eine Liste von LongPair-Objekten zur Kommunikation über die OpenAPI-Schnittstelle.
	 *
	 * @param list die Liste von UvSchueler-Objekten
	 * @return die Liste von LongPair-Objekten
	 */
	public static idsFromList(list: List<UvSchueler> | null): List<LongPair> | null {
		const result: List<LongPair> | null = new ArrayList<LongPair>();
		if (list === null)
			return result;
		for (const s of list)
			result.add(new LongPair(s.idPlanungsabschnitt, s.idSchueler));
		return result;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvSchueler';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvSchueler'].includes(name);
	}

	public static readonly class = new Class<UvSchueler>('de.svws_nrw.core.data.uv.UvSchueler');

	public static transpilerFromJSON(json: string): UvSchueler {
		const obj = JSON.parse(json) as Partial<UvSchueler>;
		const result = new UvSchueler();
		if (obj.idPlanungsabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idPlanungsabschnitt');
		result.idPlanungsabschnitt = obj.idPlanungsabschnitt;
		if (obj.idSchueler === undefined)
			throw new Error('invalid json format, missing attribute idSchueler');
		result.idSchueler = obj.idSchueler;
		result.idJahrgang = (obj.idJahrgang === undefined) ? null : obj.idJahrgang === null ? null : obj.idJahrgang;
		result.idKlasse = (obj.idKlasse === undefined) ? null : obj.idKlasse === null ? null : obj.idKlasse;
		return result;
	}

	public static transpilerToJSON(obj: UvSchueler): string {
		let result = '{';
		result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		result += '"idJahrgang" : ' + ((obj.idJahrgang === null) ? 'null' : obj.idJahrgang.toString()) + ',';
		result += '"idKlasse" : ' + ((obj.idKlasse === null) ? 'null' : obj.idKlasse.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvSchueler>): string {
		let result = '{';
		if (obj.idPlanungsabschnitt !== undefined) {
			result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		}
		if (obj.idSchueler !== undefined) {
			result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		}
		if (obj.idJahrgang !== undefined) {
			result += '"idJahrgang" : ' + ((obj.idJahrgang === null) ? 'null' : obj.idJahrgang.toString()) + ',';
		}
		if (obj.idKlasse !== undefined) {
			result += '"idKlasse" : ' + ((obj.idKlasse === null) ? 'null' : obj.idKlasse.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvSchueler(obj: unknown): UvSchueler {
	return obj as UvSchueler;
}
