import { JavaLong } from '../../../java/lang/JavaLong';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvUnterricht extends JavaObject {

	/**
	 * Die ID des Unterrichts.
	 */
	public id: number = -1;

	/**
	 * Die ID des Planungsabschnitts.
	 */
	public idPlanungsabschnitt: number = -1;

	/**
	 * Die ID des Zeitraster-Eintrags (kann null sein).
	 */
	public idZeitrasterEintrag: number | null = null;

	/**
	 * Die ID der Lerngruppe.
	 */
	public idLerngruppe: number = -1;

	/**
	 * Ein Array mit den IDs der Räume des Unterrichts.
	 */
	public idsRaeume: List<number> = new ArrayList<number>();


	/**
	 *Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	/**
	 * Vergleicht, ob das aktuelle Objekt dasselbe ist wie ein anderes übergebenes Objekt.
	 *
	 * @param obj das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	public equals(obj: unknown | null): boolean {
		return (((obj instanceof JavaObject) && (obj.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvUnterricht')))) && (this.id === (obj as unknown as UvUnterricht).id);
	}

	/**
	 * Erzeugt den Hashcode zum Objekt auf Basis der ID.
	 *
	 * @return den HashCode
	 */
	public hashCode(): number {
		return JavaLong.hashCode((this.id));
	}

	/**
	 * Gibt eine String-Repräsentation des Objekts zurück.
	 *
	 * @return die String-Darstellung
	 */
	public toString(): string | null {
		return this.id + "-" + this.idPlanungsabschnitt + "-" + this.idLerngruppe + "-" + (this.idZeitrasterEintrag !== null ? this.idZeitrasterEintrag : "");
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvUnterricht';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvUnterricht'].includes(name);
	}

	public static readonly class = new Class<UvUnterricht>('de.svws_nrw.core.data.uv.UvUnterricht');

	public static transpilerFromJSON(json: string): UvUnterricht {
		const obj = JSON.parse(json) as Partial<UvUnterricht>;
		const result = new UvUnterricht();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idPlanungsabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idPlanungsabschnitt');
		result.idPlanungsabschnitt = obj.idPlanungsabschnitt;
		result.idZeitrasterEintrag = (obj.idZeitrasterEintrag === undefined) ? null : obj.idZeitrasterEintrag === null ? null : obj.idZeitrasterEintrag;
		if (obj.idLerngruppe === undefined)
			throw new Error('invalid json format, missing attribute idLerngruppe');
		result.idLerngruppe = obj.idLerngruppe;
		if (obj.idsRaeume !== undefined) {
			for (const elem of obj.idsRaeume) {
				result.idsRaeume.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: UvUnterricht): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		result += '"idZeitrasterEintrag" : ' + ((obj.idZeitrasterEintrag === null) ? 'null' : obj.idZeitrasterEintrag.toString()) + ',';
		result += '"idLerngruppe" : ' + obj.idLerngruppe.toString() + ',';
		result += '"idsRaeume" : [ ';
		for (let i = 0; i < obj.idsRaeume.size(); i++) {
			const elem = obj.idsRaeume.get(i);
			result += elem.toString();
			if (i < obj.idsRaeume.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvUnterricht>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idPlanungsabschnitt !== undefined) {
			result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		}
		if (obj.idZeitrasterEintrag !== undefined) {
			result += '"idZeitrasterEintrag" : ' + ((obj.idZeitrasterEintrag === null) ? 'null' : obj.idZeitrasterEintrag.toString()) + ',';
		}
		if (obj.idLerngruppe !== undefined) {
			result += '"idLerngruppe" : ' + obj.idLerngruppe.toString() + ',';
		}
		if (obj.idsRaeume !== undefined) {
			result += '"idsRaeume" : [ ';
			for (let i = 0; i < obj.idsRaeume.size(); i++) {
				const elem = obj.idsRaeume.get(i);
				result += elem.toString();
				if (i < obj.idsRaeume.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvUnterricht(obj: unknown): UvUnterricht {
	return obj as UvUnterricht;
}
