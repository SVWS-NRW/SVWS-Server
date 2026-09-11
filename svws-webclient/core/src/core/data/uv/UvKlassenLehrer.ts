import { JavaLong } from '../../../java/lang/JavaLong';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvKlassenLehrer extends JavaObject {

	/**
	 * Die ID der Zuordnung (planungsspezifisch).
	 */
	public id: number = -1;

	/**
	 * Die ID des Planungsabschnitts, in dem die Zuordnung gilt.
	 */
	public idPlanungsabschnitt: number = -1;

	/**
	 * Die ID der Klasse, der der Klassenlehrer zugeordnet ist.
	 */
	public idKlasse: number = -1;

	/**
	 * Die ID des Lehrers, der der Klasse als Klassenlehrer zugeordnet ist.
	 */
	public idLehrer: number = -1;

	/**
	 * Die Reihenfolge der Zuordnung (z. B. 1 = Klassenleitung, 2 = stellvertretende Klassenleitung).
	 */
	public reihenfolge: number = 1;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	/**
	 * Gibt eine String-Repräsentation des UvKlassenLehrer-Objekts zurück.
	 *
	 * @return die String-Darstellung der Klassenlehrer-Zuordnung
	 */
	public toString(): string | null {
		return "UvKlassenLehrer{id=" + this.id + ", idPlanungsabschnitt=" + this.idPlanungsabschnitt + ", idKlasse=" + this.idKlasse + ", idLehrer=" + this.idLehrer + ", reihenfolge=" + this.reihenfolge + "}";
	}

	/**
	 * Vergleicht, ob das aktuelle Objekt dasselbe ist wie ein anderes übergebenes Objekt.
	 *
	 * @param another das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	public equals(another: unknown | null): boolean {
		return (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvKlassenLehrer')))) && (this.id === (another as unknown as UvKlassenLehrer).id);
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der ID.
	 *
	 * @return den HashCode
	 */
	public hashCode(): number {
		return JavaLong.hashCode((this.id));
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvKlassenLehrer';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvKlassenLehrer'].includes(name);
	}

	public static readonly class = new Class<UvKlassenLehrer>('de.svws_nrw.core.data.uv.UvKlassenLehrer');

	public static transpilerFromJSON(json: string): UvKlassenLehrer {
		const obj = JSON.parse(json) as Partial<UvKlassenLehrer>;
		const result = new UvKlassenLehrer();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idPlanungsabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idPlanungsabschnitt');
		result.idPlanungsabschnitt = obj.idPlanungsabschnitt;
		if (obj.idKlasse === undefined)
			throw new Error('invalid json format, missing attribute idKlasse');
		result.idKlasse = obj.idKlasse;
		if (obj.idLehrer === undefined)
			throw new Error('invalid json format, missing attribute idLehrer');
		result.idLehrer = obj.idLehrer;
		if (obj.reihenfolge === undefined)
			throw new Error('invalid json format, missing attribute reihenfolge');
		result.reihenfolge = obj.reihenfolge;
		return result;
	}

	public static transpilerToJSON(obj: UvKlassenLehrer): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		result += '"idKlasse" : ' + obj.idKlasse.toString() + ',';
		result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		result += '"reihenfolge" : ' + obj.reihenfolge.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvKlassenLehrer>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idPlanungsabschnitt !== undefined) {
			result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		}
		if (obj.idKlasse !== undefined) {
			result += '"idKlasse" : ' + obj.idKlasse.toString() + ',';
		}
		if (obj.idLehrer !== undefined) {
			result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		}
		if (obj.reihenfolge !== undefined) {
			result += '"reihenfolge" : ' + obj.reihenfolge.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvKlassenLehrer(obj: unknown): UvKlassenLehrer {
	return obj as UvKlassenLehrer;
}
