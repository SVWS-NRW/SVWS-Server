import { JavaLong } from '../../../java/lang/JavaLong';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvPlanungsabschnittZeitraster extends JavaObject {

	/**
	 * Die ID des Planungsabschnitts.
	 */
	public idPlanungsabschnitt: number = -1;

	/**
	 * Die ID des Zeitrasters.
	 */
	public idZeitraster: number = -1;

	/**
	 * Ein Array mit den IDs der zugeordneten Jahrgänge.
	 */
	public idsJahrgaenge: List<number> = new ArrayList<number>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	/**
	 * Gibt eine String-Repräsentation des UvPlanungsabschnittZeitraster-Objekts zurück.
	 *
	 * @return die String-Darstellung der Zuordnung
	 */
	public toString(): string | null {
		return "UvPlanungsabschnittZeitraster{idPlanungsabschnitt=" + this.idPlanungsabschnitt + ", idZeitraster=" + this.idZeitraster + "}";
	}

	/**
	 * Vergleicht, ob das aktuelle Objekt dasselbe ist wie ein anderes übergebenes Objekt.
	 *
	 * @param another das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	public equals(another: unknown | null): boolean {
		return (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvPlanungsabschnittZeitraster')))) && (this.idPlanungsabschnitt === (another as unknown as UvPlanungsabschnittZeitraster).idPlanungsabschnitt) && (this.idZeitraster === (another as unknown as UvPlanungsabschnittZeitraster).idZeitraster);
	}

	/**
	 * Erzeugt den Hashcode zum Objekt.
	 *
	 * @return den HashCode
	 */
	public hashCode(): number {
		const prime: number = 31;
		let result: number = 1;
		result = prime * result + JavaLong.hashCode((this.idPlanungsabschnitt));
		result = prime * result + JavaLong.hashCode((this.idZeitraster));
		return result;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvPlanungsabschnittZeitraster';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvPlanungsabschnittZeitraster'].includes(name);
	}

	public static readonly class = new Class<UvPlanungsabschnittZeitraster>('de.svws_nrw.core.data.uv.UvPlanungsabschnittZeitraster');

	public static transpilerFromJSON(json: string): UvPlanungsabschnittZeitraster {
		const obj = JSON.parse(json) as Partial<UvPlanungsabschnittZeitraster>;
		const result = new UvPlanungsabschnittZeitraster();
		if (obj.idPlanungsabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idPlanungsabschnitt');
		result.idPlanungsabschnitt = obj.idPlanungsabschnitt;
		if (obj.idZeitraster === undefined)
			throw new Error('invalid json format, missing attribute idZeitraster');
		result.idZeitraster = obj.idZeitraster;
		if (obj.idsJahrgaenge !== undefined) {
			for (const elem of obj.idsJahrgaenge) {
				result.idsJahrgaenge.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: UvPlanungsabschnittZeitraster): string {
		let result = '{';
		result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		result += '"idZeitraster" : ' + obj.idZeitraster.toString() + ',';
		result += '"idsJahrgaenge" : [ ';
		for (let i = 0; i < obj.idsJahrgaenge.size(); i++) {
			const elem = obj.idsJahrgaenge.get(i);
			result += elem.toString();
			if (i < obj.idsJahrgaenge.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvPlanungsabschnittZeitraster>): string {
		let result = '{';
		if (obj.idPlanungsabschnitt !== undefined) {
			result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		}
		if (obj.idZeitraster !== undefined) {
			result += '"idZeitraster" : ' + obj.idZeitraster.toString() + ',';
		}
		if (obj.idsJahrgaenge !== undefined) {
			result += '"idsJahrgaenge" : [ ';
			for (let i = 0; i < obj.idsJahrgaenge.size(); i++) {
				const elem = obj.idsJahrgaenge.get(i);
				result += elem.toString();
				if (i < obj.idsJahrgaenge.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvPlanungsabschnittZeitraster(obj: unknown): UvPlanungsabschnittZeitraster {
	return obj as UvPlanungsabschnittZeitraster;
}
