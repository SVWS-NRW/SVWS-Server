import { JavaLong } from '../../../java/lang/JavaLong';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvLehrerAnrechnungsstunden extends JavaObject {

	/**
	 * Die ID des Anrechnungsstunden-Eintrags.
	 */
	public id: number = -1;

	/**
	 * Die ID des Lehrers innerhalb des Unterrichtsverteilungsmoduls.
	 */
	public idLehrer: number = -1;

	/**
	 * Das Kürzel des Anrechnungsgrundes (z. B. 'AG' für Arbeitsgemeinschaft).
	 */
	public anrechnungsgrundKrz: string = "";

	/**
	 * Die Anzahl der angerechneten Stunden.
	 */
	public anzahlStunden: number = 0;

	/**
	 * Das Datum, ab dem die Anrechnungsstunde gültig ist.
	 */
	public gueltigVon: string = "";

	/**
	 * Das Datum, bis wann die Anrechnungsstunde gültig ist.
	 */
	public gueltigBis: string = "";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	/**
	 * Vergleicht dieses Objekt mit einem anderen auf Gleichheit.
	 * Der Vergleich erfolgt ausschließlich über die ID.
	 *
	 * @param obj das zu vergleichende Objekt
	 * @return {@code true}, falls beide Objekte dieselbe ID besitzen
	 */
	public equals(obj: unknown | null): boolean {
		return (((obj instanceof JavaObject) && (obj.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvLehrerAnrechnungsstunden')))) && (this.id === (obj as unknown as UvLehrerAnrechnungsstunden).id);
	}

	/**
	 * Erzeugt einen Hashcode auf Basis der ID.
	 *
	 * @return der Hashcode des Objekts
	 */
	public hashCode(): number {
		return JavaLong.hashCode((this.id));
	}

	/**
	 * Konvertiert das Objekt in eine kompakte String-Darstellung.
	 *
	 * @return die String-Repräsentation des Objekts
	 */
	public toString(): string | null {
		return this.id + "-" + this.idLehrer + "-" + this.anrechnungsgrundKrz + "-" + this.gueltigVon;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvLehrerAnrechnungsstunden';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvLehrerAnrechnungsstunden'].includes(name);
	}

	public static readonly class = new Class<UvLehrerAnrechnungsstunden>('de.svws_nrw.core.data.uv.UvLehrerAnrechnungsstunden');

	public static transpilerFromJSON(json: string): UvLehrerAnrechnungsstunden {
		const obj = JSON.parse(json) as Partial<UvLehrerAnrechnungsstunden>;
		const result = new UvLehrerAnrechnungsstunden();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idLehrer === undefined)
			throw new Error('invalid json format, missing attribute idLehrer');
		result.idLehrer = obj.idLehrer;
		if (obj.anrechnungsgrundKrz === undefined)
			throw new Error('invalid json format, missing attribute anrechnungsgrundKrz');
		result.anrechnungsgrundKrz = obj.anrechnungsgrundKrz;
		if (obj.anzahlStunden === undefined)
			throw new Error('invalid json format, missing attribute anzahlStunden');
		result.anzahlStunden = obj.anzahlStunden;
		if (obj.gueltigVon === undefined)
			throw new Error('invalid json format, missing attribute gueltigVon');
		result.gueltigVon = obj.gueltigVon;
		if (obj.gueltigBis === undefined)
			throw new Error('invalid json format, missing attribute gueltigBis');
		result.gueltigBis = obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj: UvLehrerAnrechnungsstunden): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		result += '"anrechnungsgrundKrz" : ' + JSON.stringify(obj.anrechnungsgrundKrz) + ',';
		result += '"anzahlStunden" : ' + obj.anzahlStunden.toString() + ',';
		result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + JSON.stringify(obj.gueltigBis) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvLehrerAnrechnungsstunden>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idLehrer !== undefined) {
			result += '"idLehrer" : ' + obj.idLehrer.toString() + ',';
		}
		if (obj.anrechnungsgrundKrz !== undefined) {
			result += '"anrechnungsgrundKrz" : ' + JSON.stringify(obj.anrechnungsgrundKrz) + ',';
		}
		if (obj.anzahlStunden !== undefined) {
			result += '"anzahlStunden" : ' + obj.anzahlStunden.toString() + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + JSON.stringify(obj.gueltigVon) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + JSON.stringify(obj.gueltigBis) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvLehrerAnrechnungsstunden(obj: unknown): UvLehrerAnrechnungsstunden {
	return obj as UvLehrerAnrechnungsstunden;
}
