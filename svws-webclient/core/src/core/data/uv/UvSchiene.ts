import { JavaLong } from '../../../java/lang/JavaLong';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvSchiene extends JavaObject {

	/**
	 * Die ID der Schiene (eindeutig, planungsspezifisch).
	 */
	public id: number = -1;

	/**
	 * Die ID des Planungsabschnitts, zu dem die Schiene gehört.
	 */
	public idPlanungsabschnitt: number = -1;

	/**
	 * Die laufende Nummer der Schiene innerhalb des Planungsabschnitts.
	 */
	public nummer: number = 0;

	/**
	 * Die Bezeichnung der Schiene.
	 */
	public bezeichnung: string | null = null;

	/**
	 * Ein Array mit den IDs der erlaubten Jahrgänge.
	 */
	public idsJahrgaengeErlaubt: List<number> = new ArrayList<number>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der ID.
	 *
	 * @return der HashCode
	 */
	public hashCode(): number {
		return JavaLong.hashCode((this.id));
	}

	/**
	 * Vergleicht, ob das aktuelle Objekt dasselbe ist wie ein anderes übergebenes Objekt.
	 *
	 * @param obj das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	public equals(obj: unknown | null): boolean {
		return (((obj instanceof JavaObject) && (obj.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvSchiene')))) && (this.id === (obj as unknown as UvSchiene).id);
	}

	/**
	 * Gibt eine String-Repräsentation des Objekts zurück.
	 *
	 * @return die String-Darstellung der Schiene
	 */
	public toString(): string | null {
		return this.id + "-" + this.idPlanungsabschnitt + "-" + this.nummer;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvSchiene';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvSchiene'].includes(name);
	}

	public static readonly class = new Class<UvSchiene>('de.svws_nrw.core.data.uv.UvSchiene');

	public static transpilerFromJSON(json: string): UvSchiene {
		const obj = JSON.parse(json) as Partial<UvSchiene>;
		const result = new UvSchiene();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idPlanungsabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idPlanungsabschnitt');
		result.idPlanungsabschnitt = obj.idPlanungsabschnitt;
		if (obj.nummer === undefined)
			throw new Error('invalid json format, missing attribute nummer');
		result.nummer = obj.nummer;
		result.bezeichnung = (obj.bezeichnung === undefined) ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		if (obj.idsJahrgaengeErlaubt !== undefined) {
			for (const elem of obj.idsJahrgaengeErlaubt) {
				result.idsJahrgaengeErlaubt.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: UvSchiene): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		result += '"nummer" : ' + obj.nummer.toString() + ',';
		result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result += '"idsJahrgaengeErlaubt" : [ ';
		for (let i = 0; i < obj.idsJahrgaengeErlaubt.size(); i++) {
			const elem = obj.idsJahrgaengeErlaubt.get(i);
			result += elem.toString();
			if (i < obj.idsJahrgaengeErlaubt.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvSchiene>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idPlanungsabschnitt !== undefined) {
			result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		}
		if (obj.nummer !== undefined) {
			result += '"nummer" : ' + obj.nummer.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		if (obj.idsJahrgaengeErlaubt !== undefined) {
			result += '"idsJahrgaengeErlaubt" : [ ';
			for (let i = 0; i < obj.idsJahrgaengeErlaubt.size(); i++) {
				const elem = obj.idsJahrgaengeErlaubt.get(i);
				result += elem.toString();
				if (i < obj.idsJahrgaengeErlaubt.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvSchiene(obj: unknown): UvSchiene {
	return obj as UvSchiene;
}
