import { JavaLong } from '../../../java/lang/JavaLong';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvLerngruppenLehrer extends JavaObject {

	/**
	 * Die ID der Zuordnung (planungsspezifisch).
	 */
	public id: number = -1;

	/**
	 * Die ID des Planungsabschnitts, in dem die Zuordnung gilt.
	 */
	public idPlanungsabschnitt: number = -1;

	/**
	 * Die ID der Lerngruppe, der der Lehrer zugeordnet ist.
	 */
	public idLerngruppe: number = -1;

	/**
	 * Die ID des Lehrers, der der Lerngruppe zugeordnet ist.
	 */
	public idLehrer: number = -1;

	/**
	 * Die Reihenfolge der Zuordnung (z. B. 1 = Hauptlehrkraft, 2 = Zweitkraft).
	 */
	public reihenfolge: number = 1;

	/**
	 * Die Anzahl der Wochenstunden, in denen der Lehrer in dieser Lerngruppe eingesetzt ist.
	 */
	public wochenstunden: number = 0.0;

	/**
	 * Die Anzahl der Wochenstunden, die auf das Deputat angerechnet werden.
	 */
	public wochenstundenAngerechnet: number = 0.0;


	/**
	 * Leerer Standardkonstruktor.
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
		return (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvLerngruppenLehrer')))) && (this.id === (another as unknown as UvLerngruppenLehrer).id);
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der ID.
	 *
	 * @return den HashCode
	 */
	public hashCode(): number {
		return JavaLong.hashCode((this.id));
	}

	/**
	 * Gibt eine String-Repräsentation des Objekts zurück.
	 *
	 * @return die String-Darstellung der Lehrer-Lerngruppen-Zuordnung
	 */
	public toString(): string | null {
		return this.idLerngruppe + "-" + this.idLehrer;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvLerngruppenLehrer';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvLerngruppenLehrer'].includes(name);
	}

	public static readonly class = new Class<UvLerngruppenLehrer>('de.svws_nrw.core.data.uv.UvLerngruppenLehrer');

	public static transpilerFromJSON(json: string): UvLerngruppenLehrer {
		const obj = JSON.parse(json) as Partial<UvLerngruppenLehrer>;
		const result = new UvLerngruppenLehrer();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idPlanungsabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idPlanungsabschnitt');
		result.idPlanungsabschnitt = obj.idPlanungsabschnitt;
		if (obj.idLerngruppe === undefined)
			throw new Error('invalid json format, missing attribute idLerngruppe');
		result.idLerngruppe = obj.idLerngruppe;
		if (obj.idLehrer === undefined)
			throw new Error('invalid json format, missing attribute idLehrer');
		result.idLehrer = obj.idLehrer;
		if (obj.reihenfolge === undefined)
			throw new Error('invalid json format, missing attribute reihenfolge');
		result.reihenfolge = obj.reihenfolge;
		if (obj.wochenstunden === undefined)
			throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		if (obj.wochenstundenAngerechnet === undefined)
			throw new Error('invalid json format, missing attribute wochenstundenAngerechnet');
		result.wochenstundenAngerechnet = obj.wochenstundenAngerechnet;
		return result;
	}

	public static transpilerToJSON(obj: UvLerngruppenLehrer): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		result += '"idLerngruppe" : ' + obj.idLerngruppe.toString() + ',';
		result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		result += '"reihenfolge" : ' + obj.reihenfolge.toString() + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		result += '"wochenstundenAngerechnet" : ' + obj.wochenstundenAngerechnet.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvLerngruppenLehrer>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idPlanungsabschnitt !== undefined) {
			result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		}
		if (obj.idLerngruppe !== undefined) {
			result += '"idLerngruppe" : ' + obj.idLerngruppe.toString() + ',';
		}
		if (obj.idLehrer !== undefined) {
			result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		}
		if (obj.reihenfolge !== undefined) {
			result += '"reihenfolge" : ' + obj.reihenfolge.toString() + ',';
		}
		if (obj.wochenstunden !== undefined) {
			result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		}
		if (obj.wochenstundenAngerechnet !== undefined) {
			result += '"wochenstundenAngerechnet" : ' + obj.wochenstundenAngerechnet.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvLerngruppenLehrer(obj: unknown): UvLerngruppenLehrer {
	return obj as UvLerngruppenLehrer;
}
