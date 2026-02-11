import { JavaLong } from '../../../java/lang/JavaLong';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvSchuelergruppe extends JavaObject {

	/**
	 * Die ID der Schülergruppe.
	 */
	public id: number = -1;

	/**
	 * Die ID des Planungsabschnitts.
	 */
	public idPlanungsabschnitt: number = -1;

	/**
	 * Die Bezeichnung der Schülergruppe.
	 */
	public bezeichnung: string = "";

	/**
	 * Ein Array mit den IDs der Schüler der Gruppe.
	 */
	public idsSchueler: List<number> = new ArrayList<number>();

	/**
	 * Ein Array mit den IDs der erlaubten Jahrgänge.
	 */
	public idsJahrgaengeErlaubt: List<number> = new ArrayList<number>();

	/**
	 * Ein Array mit den IDs der Schüler der Gruppe.
	 */
	public idsGruppenErlaubt: List<number> = new ArrayList<number>();


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
		return (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvSchuelergruppe')))) && (this.id === (another as unknown as UvSchuelergruppe).id);
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der idVorgabe.
	 *
	 * @return den HashCode
	 */
	public hashCode(): number {
		return JavaLong.hashCode((this.id));
	}

	/**
	 * Gibt eine String-Repräsentation des Objekts zurück.
	 */
	public toString(): string | null {
		return this.id + "-" + this.idPlanungsabschnitt;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvSchuelergruppe';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvSchuelergruppe'].includes(name);
	}

	public static readonly class = new Class<UvSchuelergruppe>('de.svws_nrw.core.data.uv.UvSchuelergruppe');

	public static transpilerFromJSON(json: string): UvSchuelergruppe {
		const obj = JSON.parse(json) as Partial<UvSchuelergruppe>;
		const result = new UvSchuelergruppe();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idPlanungsabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idPlanungsabschnitt');
		result.idPlanungsabschnitt = obj.idPlanungsabschnitt;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.idsSchueler !== undefined) {
			for (const elem of obj.idsSchueler) {
				result.idsSchueler.add(elem);
			}
		}
		if (obj.idsJahrgaengeErlaubt !== undefined) {
			for (const elem of obj.idsJahrgaengeErlaubt) {
				result.idsJahrgaengeErlaubt.add(elem);
			}
		}
		if (obj.idsGruppenErlaubt !== undefined) {
			for (const elem of obj.idsGruppenErlaubt) {
				result.idsGruppenErlaubt.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: UvSchuelergruppe): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"idsSchueler" : [ ';
		for (let i = 0; i < obj.idsSchueler.size(); i++) {
			const elem = obj.idsSchueler.get(i);
			result += elem.toString();
			if (i < obj.idsSchueler.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"idsJahrgaengeErlaubt" : [ ';
		for (let i = 0; i < obj.idsJahrgaengeErlaubt.size(); i++) {
			const elem = obj.idsJahrgaengeErlaubt.get(i);
			result += elem.toString();
			if (i < obj.idsJahrgaengeErlaubt.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"idsGruppenErlaubt" : [ ';
		for (let i = 0; i < obj.idsGruppenErlaubt.size(); i++) {
			const elem = obj.idsGruppenErlaubt.get(i);
			result += elem.toString();
			if (i < obj.idsGruppenErlaubt.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvSchuelergruppe>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idPlanungsabschnitt !== undefined) {
			result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
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
		if (obj.idsGruppenErlaubt !== undefined) {
			result += '"idsGruppenErlaubt" : [ ';
			for (let i = 0; i < obj.idsGruppenErlaubt.size(); i++) {
				const elem = obj.idsGruppenErlaubt.get(i);
				result += elem.toString();
				if (i < obj.idsGruppenErlaubt.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvSchuelergruppe(obj: unknown): UvSchuelergruppe {
	return obj as UvSchuelergruppe;
}
