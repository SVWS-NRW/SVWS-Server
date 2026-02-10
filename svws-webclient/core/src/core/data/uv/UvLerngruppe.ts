import { JavaLong } from '../../../java/lang/JavaLong';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvLerngruppe extends JavaObject {

	/**
	 * Die eindeutige ID der Lerngruppe.
	 */
	public id: number = -1;

	/**
	 * Die ID der Klasse, zu der die Lerngruppe gehört (optional).
	 */
	public idKlasse: number | null = null;

	/**
	 * Die ID des Faches, das in der Lerngruppe unterrichtet wird (optional).
	 */
	public idFach: number | null = null;

	/**
	 * Die ID des Kurses, der mit dieser Lerngruppe verknüpft ist (optional).
	 */
	public idKurs: number | null = null;

	/**
	 * Die ID des Planungsabschnitts, zu dem die Lerngruppe gehört.
	 */
	public idPlanungsabschnitt: number = -1;

	/**
	 * Die Anzahl der vorgesehenen Wochenstunden für die Lerngruppe.
	 */
	public wochenstunden: number = 0.0;

	/**
	 * Die Anzahl der tatsächlich unterrichteten Wochenstunden der Lerngruppe.
	 */
	public wochenstundenUnterrichtet: number = 0.0;

	/**
	 * Die Schulnummer einer möglichen Koop-Schule, null falls keine Kooperation besteht.
	 */
	public koopSchulNr: string | null = null;

	/**
	 * Die Anzahl der externen Schüler von Koop-Schulen.
	 */
	public koopAnzahlExterne: number = 0;

	/**
	 * Ein Array mit den IDs der UV-Schienen der Lerngruppe.
	 */
	public idsSchienen: List<number> = new ArrayList<number>();


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
		return (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvLerngruppe')))) && (this.id === (another as unknown as UvLerngruppe).id);
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
	 * @return die String-Darstellung der Lerngruppe
	 */
	public toString(): string | null {
		return this.id + "-" + (this.idKlasse !== null ? this.idKlasse : "") + "-" + (this.idKurs !== null ? this.idKurs : "");
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvLerngruppe';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvLerngruppe'].includes(name);
	}

	public static readonly class = new Class<UvLerngruppe>('de.svws_nrw.core.data.uv.UvLerngruppe');

	public static transpilerFromJSON(json: string): UvLerngruppe {
		const obj = JSON.parse(json) as Partial<UvLerngruppe>;
		const result = new UvLerngruppe();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.idKlasse = (obj.idKlasse === undefined) ? null : obj.idKlasse === null ? null : obj.idKlasse;
		result.idFach = (obj.idFach === undefined) ? null : obj.idFach === null ? null : obj.idFach;
		result.idKurs = (obj.idKurs === undefined) ? null : obj.idKurs === null ? null : obj.idKurs;
		if (obj.idPlanungsabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idPlanungsabschnitt');
		result.idPlanungsabschnitt = obj.idPlanungsabschnitt;
		if (obj.wochenstunden === undefined)
			throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		if (obj.wochenstundenUnterrichtet === undefined)
			throw new Error('invalid json format, missing attribute wochenstundenUnterrichtet');
		result.wochenstundenUnterrichtet = obj.wochenstundenUnterrichtet;
		result.koopSchulNr = (obj.koopSchulNr === undefined) ? null : obj.koopSchulNr === null ? null : obj.koopSchulNr;
		if (obj.koopAnzahlExterne === undefined)
			throw new Error('invalid json format, missing attribute koopAnzahlExterne');
		result.koopAnzahlExterne = obj.koopAnzahlExterne;
		if (obj.idsSchienen !== undefined) {
			for (const elem of obj.idsSchienen) {
				result.idsSchienen.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: UvLerngruppe): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idKlasse" : ' + ((obj.idKlasse === null) ? 'null' : obj.idKlasse.toString()) + ',';
		result += '"idFach" : ' + ((obj.idFach === null) ? 'null' : obj.idFach.toString()) + ',';
		result += '"idKurs" : ' + ((obj.idKurs === null) ? 'null' : obj.idKurs.toString()) + ',';
		result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		result += '"wochenstundenUnterrichtet" : ' + obj.wochenstundenUnterrichtet.toString() + ',';
		result += '"koopSchulNr" : ' + ((obj.koopSchulNr === null) ? 'null' : JSON.stringify(obj.koopSchulNr)) + ',';
		result += '"koopAnzahlExterne" : ' + obj.koopAnzahlExterne.toString() + ',';
		result += '"idsSchienen" : [ ';
		for (let i = 0; i < obj.idsSchienen.size(); i++) {
			const elem = obj.idsSchienen.get(i);
			result += elem.toString();
			if (i < obj.idsSchienen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvLerngruppe>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idKlasse !== undefined) {
			result += '"idKlasse" : ' + ((obj.idKlasse === null) ? 'null' : obj.idKlasse.toString()) + ',';
		}
		if (obj.idFach !== undefined) {
			result += '"idFach" : ' + ((obj.idFach === null) ? 'null' : obj.idFach.toString()) + ',';
		}
		if (obj.idKurs !== undefined) {
			result += '"idKurs" : ' + ((obj.idKurs === null) ? 'null' : obj.idKurs.toString()) + ',';
		}
		if (obj.idPlanungsabschnitt !== undefined) {
			result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		}
		if (obj.wochenstunden !== undefined) {
			result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		}
		if (obj.wochenstundenUnterrichtet !== undefined) {
			result += '"wochenstundenUnterrichtet" : ' + obj.wochenstundenUnterrichtet.toString() + ',';
		}
		if (obj.koopSchulNr !== undefined) {
			result += '"koopSchulNr" : ' + ((obj.koopSchulNr === null) ? 'null' : JSON.stringify(obj.koopSchulNr)) + ',';
		}
		if (obj.koopAnzahlExterne !== undefined) {
			result += '"koopAnzahlExterne" : ' + obj.koopAnzahlExterne.toString() + ',';
		}
		if (obj.idsSchienen !== undefined) {
			result += '"idsSchienen" : [ ';
			for (let i = 0; i < obj.idsSchienen.size(); i++) {
				const elem = obj.idsSchienen.get(i);
				result += elem.toString();
				if (i < obj.idsSchienen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvLerngruppe(obj: unknown): UvLerngruppe {
	return obj as UvLerngruppe;
}
