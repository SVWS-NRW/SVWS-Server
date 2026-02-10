import { JavaLong } from '../../../java/lang/JavaLong';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvPlanungsabschnitt extends JavaObject {

	/**
	 * Die ID des Planungsabschnitts.
	 */
	public id: number = -1;

	/**
	 * Das Schuljahr, zu dem der Planungsabschnitt gehört.
	 */
	public schuljahr: number = -1;

	/**
	 * Gibt an, ob der Planungsabschnitt aktiv ist. An einem Stichtag darf nur ein Planungsabschnitt aktiv sein.
	 */
	public aktiv: boolean = false;

	/**
	 * Das Datum des Gültigkeitsbeginns des Planungsabschnitts.
	 */
	public gueltigVon: string = "";

	/**
	 * Das Datum des Gültigkeitsendes des Planungsabschnitts.
	 */
	public gueltigBis: string | null = "";

	/**
	 * Die optionale Beschreibung oder Kommentar zum Planungsabschnitt.
	 */
	public beschreibung: string | null = null;

	/**
	 * Ein Array mit den IDs der Lehrer des Planungsabschnitts.
	 */
	public idsLehrer: List<number> = new ArrayList<number>();


	/**
	 * Default-Konstruktor
	 */
	public constructor() {
		super();
	}

	/**
	 * Vergleicht, ob das akutelle dasselbe Objekt, wie ein anderes übergebenes Objekt ist.
	 *
	 * @param another     das zu vergleichende Objekt
	 * @return true, falls die Objekte indentisch sind, sonst false
	 */
	public equals(another: unknown | null): boolean {
		return (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvPlanungsabschnitt')))) && (this.id === (another as unknown as UvPlanungsabschnitt).id);
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
	 * Returns a string representation of the object.
	 */
	public toString(): string | null {
		return this.id + "-" + this.schuljahr + "-" + (this.aktiv ? 1 : 0) + "-" + this.gueltigVon;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvPlanungsabschnitt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvPlanungsabschnitt'].includes(name);
	}

	public static readonly class = new Class<UvPlanungsabschnitt>('de.svws_nrw.core.data.uv.UvPlanungsabschnitt');

	public static transpilerFromJSON(json: string): UvPlanungsabschnitt {
		const obj = JSON.parse(json) as Partial<UvPlanungsabschnitt>;
		const result = new UvPlanungsabschnitt();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.schuljahr === undefined)
			throw new Error('invalid json format, missing attribute schuljahr');
		result.schuljahr = obj.schuljahr;
		if (obj.aktiv === undefined)
			throw new Error('invalid json format, missing attribute aktiv');
		result.aktiv = obj.aktiv;
		if (obj.gueltigVon === undefined)
			throw new Error('invalid json format, missing attribute gueltigVon');
		result.gueltigVon = obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		result.beschreibung = (obj.beschreibung === undefined) ? null : obj.beschreibung === null ? null : obj.beschreibung;
		if (obj.idsLehrer !== undefined) {
			for (const elem of obj.idsLehrer) {
				result.idsLehrer.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: UvPlanungsabschnitt): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		result += '"aktiv" : ' + obj.aktiv.toString() + ',';
		result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : JSON.stringify(obj.gueltigBis)) + ',';
		result += '"beschreibung" : ' + ((obj.beschreibung === null) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		result += '"idsLehrer" : [ ';
		for (let i = 0; i < obj.idsLehrer.size(); i++) {
			const elem = obj.idsLehrer.get(i);
			result += elem.toString();
			if (i < obj.idsLehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvPlanungsabschnitt>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.schuljahr !== undefined) {
			result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		}
		if (obj.aktiv !== undefined) {
			result += '"aktiv" : ' + obj.aktiv.toString() + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : JSON.stringify(obj.gueltigBis)) + ',';
		}
		if (obj.beschreibung !== undefined) {
			result += '"beschreibung" : ' + ((obj.beschreibung === null) ? 'null' : JSON.stringify(obj.beschreibung)) + ',';
		}
		if (obj.idsLehrer !== undefined) {
			result += '"idsLehrer" : [ ';
			for (let i = 0; i < obj.idsLehrer.size(); i++) {
				const elem = obj.idsLehrer.get(i);
				result += elem.toString();
				if (i < obj.idsLehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvPlanungsabschnitt(obj: unknown): UvPlanungsabschnitt {
	return obj as UvPlanungsabschnitt;
}
