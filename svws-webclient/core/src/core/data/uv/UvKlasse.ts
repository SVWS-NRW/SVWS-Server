import { JavaLong } from '../../../java/lang/JavaLong';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvKlasse extends JavaObject {

	/**
	 *  Die ID der Klasse (generiert, planungsspezifisch).
	 */
	public id: number = 0;

	/**
	 *  Die ID des Planungsabschnitts.
	 */
	public idPlanungsabschnitt: number = 0;

	/**
	 *  Die ID des Schuljahresabschnitts.
	 */
	public idSchuljahresabschnitt: number = 0;

	/**
	 *  Bezeichnender Text für die Klasse.
	 */
	public bezeichnung: string | null = null;

	/**
	 *  Das Kürzel der Klasse.
	 */
	public kuerzel: string = "";

	/**
	 *  Die Parallelität (z. B. a/b/c).
	 */
	public parallelitaet: string = "";

	/**
	 *  Die ID der Stundentafel.
	 */
	public idStundentafel: number | null = null;

	/**
	 *  Die ID der zugehörigen Schülergruppe.
	 */
	public idSchuelergruppe: number = 0;

	/**
	 *  Das Kürzel der Organisationsform.
	 */
	public orgFormKrz: string | null = null;

	/**
	 *  Die ID der Fachklasse (nur BK SBK).
	 */
	public idFachklasse: number | null = null;

	/**
	 *  Die Schulgliederungsnummer (ASD-Schulform-Nr).
	 */
	public asdSchulformNr: string | null = null;


	/**
	 * Default-Konstruktor.
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
		return (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvKlasse')))) && (this.id === (another as unknown as UvKlasse).id);
	}

	/**
	 * Erzeugt den Hashcode zum Objekt auf Basis der ID.
	 *
	 * @return den Hashcode
	 */
	public hashCode(): number {
		return JavaLong.hashCode((this.id));
	}

	/**
	 * Gibt eine String-Repräsentation des Objekts zurück.
	 *
	 * @return eine lesbare Beschreibung des Objekts
	 */
	public toString(): string | null {
		return this.id + "-" + this.idPlanungsabschnitt + "-" + this.kuerzel;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvKlasse';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvKlasse'].includes(name);
	}

	public static readonly class = new Class<UvKlasse>('de.svws_nrw.core.data.uv.UvKlasse');

	public static transpilerFromJSON(json: string): UvKlasse {
		const obj = JSON.parse(json) as Partial<UvKlasse>;
		const result = new UvKlasse();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idPlanungsabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idPlanungsabschnitt');
		result.idPlanungsabschnitt = obj.idPlanungsabschnitt;
		if (obj.idSchuljahresabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		result.bezeichnung = (obj.bezeichnung === undefined) ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.parallelitaet === undefined)
			throw new Error('invalid json format, missing attribute parallelitaet');
		result.parallelitaet = obj.parallelitaet;
		result.idStundentafel = (obj.idStundentafel === undefined) ? null : obj.idStundentafel === null ? null : obj.idStundentafel;
		if (obj.idSchuelergruppe === undefined)
			throw new Error('invalid json format, missing attribute idSchuelergruppe');
		result.idSchuelergruppe = obj.idSchuelergruppe;
		result.orgFormKrz = (obj.orgFormKrz === undefined) ? null : obj.orgFormKrz === null ? null : obj.orgFormKrz;
		result.idFachklasse = (obj.idFachklasse === undefined) ? null : obj.idFachklasse === null ? null : obj.idFachklasse;
		result.asdSchulformNr = (obj.asdSchulformNr === undefined) ? null : obj.asdSchulformNr === null ? null : obj.asdSchulformNr;
		return result;
	}

	public static transpilerToJSON(obj: UvKlasse): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"parallelitaet" : ' + JSON.stringify(obj.parallelitaet) + ',';
		result += '"idStundentafel" : ' + ((obj.idStundentafel === null) ? 'null' : obj.idStundentafel.toString()) + ',';
		result += '"idSchuelergruppe" : ' + obj.idSchuelergruppe.toString() + ',';
		result += '"orgFormKrz" : ' + ((obj.orgFormKrz === null) ? 'null' : JSON.stringify(obj.orgFormKrz)) + ',';
		result += '"idFachklasse" : ' + ((obj.idFachklasse === null) ? 'null' : obj.idFachklasse.toString()) + ',';
		result += '"asdSchulformNr" : ' + ((obj.asdSchulformNr === null) ? 'null' : JSON.stringify(obj.asdSchulformNr)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvKlasse>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idPlanungsabschnitt !== undefined) {
			result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		}
		if (obj.idSchuljahresabschnitt !== undefined) {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.parallelitaet !== undefined) {
			result += '"parallelitaet" : ' + JSON.stringify(obj.parallelitaet) + ',';
		}
		if (obj.idStundentafel !== undefined) {
			result += '"idStundentafel" : ' + ((obj.idStundentafel === null) ? 'null' : obj.idStundentafel.toString()) + ',';
		}
		if (obj.idSchuelergruppe !== undefined) {
			result += '"idSchuelergruppe" : ' + obj.idSchuelergruppe.toString() + ',';
		}
		if (obj.orgFormKrz !== undefined) {
			result += '"orgFormKrz" : ' + ((obj.orgFormKrz === null) ? 'null' : JSON.stringify(obj.orgFormKrz)) + ',';
		}
		if (obj.idFachklasse !== undefined) {
			result += '"idFachklasse" : ' + ((obj.idFachklasse === null) ? 'null' : obj.idFachklasse.toString()) + ',';
		}
		if (obj.asdSchulformNr !== undefined) {
			result += '"asdSchulformNr" : ' + ((obj.asdSchulformNr === null) ? 'null' : JSON.stringify(obj.asdSchulformNr)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvKlasse(obj: unknown): UvKlasse {
	return obj as UvKlasse;
}
