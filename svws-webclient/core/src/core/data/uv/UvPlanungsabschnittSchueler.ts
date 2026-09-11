import { Schueler } from '../../../asd/data/schueler/Schueler';
import { JavaLong } from '../../../java/lang/JavaLong';
import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvPlanungsabschnittSchueler extends JavaObject {

	/**
	 * Die ID des Planungsabschnitts.
	 */
	public idPlanungsabschnitt: number = -1;

	/**
	 * Die ID des Schülers.
	 */
	public idSchueler: number = -1;

	/**
	 * Die ID des Jahrgangs, dem der Schüler zugeordnet ist.
	 */
	public idJahrgang: number = -1;

	/**
	 * Die ID der Klasse, der der Schüler zugeordnet ist.
	 */
	public idKlasse: number | null = null;

	/**
	 * Der Schülerdatensatz.
	 */
	public daten: Schueler = new Schueler();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	/**
	 * Gibt eine String-Repräsentation des UvPlanungsabschnittSchueler-Objekts zurück.
	 *
	 * @return die String-Darstellung der Zuordnung
	 */
	public toString(): string | null {
		return "UvPlanungsabschnittSchueler{idPlanungsabschnitt=" + this.idPlanungsabschnitt + ", idSchueler=" + this.idSchueler + ", idJahrgang=" + this.idJahrgang + ", idKlasse=" + this.idKlasse + "}";
	}

	/**
	 * Vergleicht, ob das aktuelle Objekt dasselbe ist wie ein anderes übergebenes Objekt.
	 *
	 * @param another das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	public equals(another: unknown | null): boolean {
		return (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvPlanungsabschnittSchueler')))) && (this.idPlanungsabschnitt === (another as unknown as UvPlanungsabschnittSchueler).idPlanungsabschnitt) && (this.idSchueler === (another as unknown as UvPlanungsabschnittSchueler).idSchueler);
	}

	/**
	 * Erzeugt den Hashcode zum Objekt.
	 *
	 * @return den HashCode
	 */
	public hashCode(): number {
		const prime: number = 31;
		let result: number = 1;
		result = (prime * result) + JavaLong.hashCode((this.idPlanungsabschnitt));
		result = (prime * result) + JavaLong.hashCode((this.idSchueler));
		return result;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvPlanungsabschnittSchueler';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvPlanungsabschnittSchueler'].includes(name);
	}

	public static readonly class = new Class<UvPlanungsabschnittSchueler>('de.svws_nrw.core.data.uv.UvPlanungsabschnittSchueler');

	public static transpilerFromJSON(json: string): UvPlanungsabschnittSchueler {
		const obj = JSON.parse(json) as Partial<UvPlanungsabschnittSchueler>;
		const result = new UvPlanungsabschnittSchueler();
		if (obj.idPlanungsabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idPlanungsabschnitt');
		result.idPlanungsabschnitt = obj.idPlanungsabschnitt;
		if (obj.idSchueler === undefined)
			throw new Error('invalid json format, missing attribute idSchueler');
		result.idSchueler = obj.idSchueler;
		if (obj.idJahrgang === undefined)
			throw new Error('invalid json format, missing attribute idJahrgang');
		result.idJahrgang = obj.idJahrgang;
		result.idKlasse = (obj.idKlasse === undefined) ? null : obj.idKlasse === null ? null : obj.idKlasse;
		if (obj.daten === undefined)
			throw new Error('invalid json format, missing attribute daten');
		result.daten = Schueler.transpilerFromJSON(JSON.stringify(obj.daten));
		return result;
	}

	public static transpilerToJSON(obj: UvPlanungsabschnittSchueler): string {
		let result = '{';
		result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		result += '"idJahrgang" : ' + obj.idJahrgang.toString() + ',';
		result += '"idKlasse" : ' + ((obj.idKlasse === null) ? 'null' : obj.idKlasse.toString()) + ',';
		result += '"daten" : ' + Schueler.transpilerToJSON(obj.daten) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvPlanungsabschnittSchueler>): string {
		let result = '{';
		if (obj.idPlanungsabschnitt !== undefined) {
			result += '"idPlanungsabschnitt" : ' + obj.idPlanungsabschnitt.toString() + ',';
		}
		if (obj.idSchueler !== undefined) {
			result += '"idSchueler" : ' + obj.idSchueler.toString() + ',';
		}
		if (obj.idJahrgang !== undefined) {
			result += '"idJahrgang" : ' + obj.idJahrgang.toString() + ',';
		}
		if (obj.idKlasse !== undefined) {
			result += '"idKlasse" : ' + ((obj.idKlasse === null) ? 'null' : obj.idKlasse.toString()) + ',';
		}
		if (obj.daten !== undefined) {
			result += '"daten" : ' + Schueler.transpilerToJSON(obj.daten) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvPlanungsabschnittSchueler(obj: unknown): UvPlanungsabschnittSchueler {
	return obj as UvPlanungsabschnittSchueler;
}
