import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuelerNeu extends JavaObject {

	/**
	 * Der Nachname des Schülers.
	 */
	public nachname: string | null = null;

	/**
	 * Der Vorname des Schülers.
	 */
	public vorname: string | null = null;

	/**
	 * Alle Vornamen des Schülers.
	 */
	public alleVornamen: string | null = null;

	/**
	 * Das Geschlecht des Schülers.
	 */
	public geschlecht: number = 0;

	/**
	 * Das Geburtsdatum des Schülers.
	 */
	public geburtsdatum: string | null = null;

	/**
	 * Der Status des Schülers.
	 */
	public status: number = 0;

	/**
	 * Das Anmeldedatum des Schülers.
	 */
	public anmeldedatum: string | null = null;

	/**
	 * Das Aufnahmedatum des Schülers.
	 */
	public aufnahmedatum: string | null = null;

	/**
	 * Der Beginn des Bildungsgangs des Schülers.
	 */
	public beginnBildungsgang: string | null = null;

	/**
	 * Dauer des Bildungsgangs des Schülers. (nur bei BK/SB)
	 */
	public dauerBildungsgang: number | null = null;

	/**
	 * Die ID der Religion des Schülers.
	 */
	public idReligion: number | null = null;

	/**
	 * Die ID des Schuljahresabschnitts, zu dem die Lernabschnittdaten angelegt werden.
	 */
	public idSchuljahresabschnitt: number = 0;

	/**
	 * Die ID des Jahrgangs des Schülers.
	 */
	public idJahrgang: number | null = null;

	/**
	 * Die ID der Klasse des Schülers.
	 */
	public idKlasse: number | null = null;

	/**
	 * Die ID der Einschulungsart (nur bei Grundschule).
	 */
	public idGrundschuleEinschulungsart: number | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schueler.SchuelerNeu';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.schueler.SchuelerNeu'].includes(name);
	}

	public static readonly class = new Class<SchuelerNeu>('de.svws_nrw.asd.data.schueler.SchuelerNeu');

	public static transpilerFromJSON(json: string): SchuelerNeu {
		const obj = JSON.parse(json) as Partial<SchuelerNeu>;
		const result = new SchuelerNeu();
		result.nachname = (obj.nachname === undefined) ? null : obj.nachname === null ? null : obj.nachname;
		result.vorname = (obj.vorname === undefined) ? null : obj.vorname === null ? null : obj.vorname;
		result.alleVornamen = (obj.alleVornamen === undefined) ? null : obj.alleVornamen === null ? null : obj.alleVornamen;
		if (obj.geschlecht === undefined)
			throw new Error('invalid json format, missing attribute geschlecht');
		result.geschlecht = obj.geschlecht;
		result.geburtsdatum = (obj.geburtsdatum === undefined) ? null : obj.geburtsdatum === null ? null : obj.geburtsdatum;
		if (obj.status === undefined)
			throw new Error('invalid json format, missing attribute status');
		result.status = obj.status;
		result.anmeldedatum = (obj.anmeldedatum === undefined) ? null : obj.anmeldedatum === null ? null : obj.anmeldedatum;
		result.aufnahmedatum = (obj.aufnahmedatum === undefined) ? null : obj.aufnahmedatum === null ? null : obj.aufnahmedatum;
		result.beginnBildungsgang = (obj.beginnBildungsgang === undefined) ? null : obj.beginnBildungsgang === null ? null : obj.beginnBildungsgang;
		result.dauerBildungsgang = (obj.dauerBildungsgang === undefined) ? null : obj.dauerBildungsgang === null ? null : obj.dauerBildungsgang;
		result.idReligion = (obj.idReligion === undefined) ? null : obj.idReligion === null ? null : obj.idReligion;
		if (obj.idSchuljahresabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		result.idJahrgang = (obj.idJahrgang === undefined) ? null : obj.idJahrgang === null ? null : obj.idJahrgang;
		result.idKlasse = (obj.idKlasse === undefined) ? null : obj.idKlasse === null ? null : obj.idKlasse;
		result.idGrundschuleEinschulungsart = (obj.idGrundschuleEinschulungsart === undefined) ? null : obj.idGrundschuleEinschulungsart === null ? null : obj.idGrundschuleEinschulungsart;
		return result;
	}

	public static transpilerToJSON(obj: SchuelerNeu): string {
		let result = '{';
		result += '"nachname" : ' + ((obj.nachname === null) ? 'null' : JSON.stringify(obj.nachname)) + ',';
		result += '"vorname" : ' + ((obj.vorname === null) ? 'null' : JSON.stringify(obj.vorname)) + ',';
		result += '"alleVornamen" : ' + ((obj.alleVornamen === null) ? 'null' : JSON.stringify(obj.alleVornamen)) + ',';
		result += '"geschlecht" : ' + obj.geschlecht.toString() + ',';
		result += '"geburtsdatum" : ' + ((obj.geburtsdatum === null) ? 'null' : JSON.stringify(obj.geburtsdatum)) + ',';
		result += '"status" : ' + obj.status.toString() + ',';
		result += '"anmeldedatum" : ' + ((obj.anmeldedatum === null) ? 'null' : JSON.stringify(obj.anmeldedatum)) + ',';
		result += '"aufnahmedatum" : ' + ((obj.aufnahmedatum === null) ? 'null' : JSON.stringify(obj.aufnahmedatum)) + ',';
		result += '"beginnBildungsgang" : ' + ((obj.beginnBildungsgang === null) ? 'null' : JSON.stringify(obj.beginnBildungsgang)) + ',';
		result += '"dauerBildungsgang" : ' + ((obj.dauerBildungsgang === null) ? 'null' : obj.dauerBildungsgang.toString()) + ',';
		result += '"idReligion" : ' + ((obj.idReligion === null) ? 'null' : obj.idReligion.toString()) + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		result += '"idJahrgang" : ' + ((obj.idJahrgang === null) ? 'null' : obj.idJahrgang.toString()) + ',';
		result += '"idKlasse" : ' + ((obj.idKlasse === null) ? 'null' : obj.idKlasse.toString()) + ',';
		result += '"idGrundschuleEinschulungsart" : ' + ((obj.idGrundschuleEinschulungsart === null) ? 'null' : obj.idGrundschuleEinschulungsart.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchuelerNeu>): string {
		let result = '{';
		if (obj.nachname !== undefined) {
			result += '"nachname" : ' + ((obj.nachname === null) ? 'null' : JSON.stringify(obj.nachname)) + ',';
		}
		if (obj.vorname !== undefined) {
			result += '"vorname" : ' + ((obj.vorname === null) ? 'null' : JSON.stringify(obj.vorname)) + ',';
		}
		if (obj.alleVornamen !== undefined) {
			result += '"alleVornamen" : ' + ((obj.alleVornamen === null) ? 'null' : JSON.stringify(obj.alleVornamen)) + ',';
		}
		if (obj.geschlecht !== undefined) {
			result += '"geschlecht" : ' + obj.geschlecht.toString() + ',';
		}
		if (obj.geburtsdatum !== undefined) {
			result += '"geburtsdatum" : ' + ((obj.geburtsdatum === null) ? 'null' : JSON.stringify(obj.geburtsdatum)) + ',';
		}
		if (obj.status !== undefined) {
			result += '"status" : ' + obj.status.toString() + ',';
		}
		if (obj.anmeldedatum !== undefined) {
			result += '"anmeldedatum" : ' + ((obj.anmeldedatum === null) ? 'null' : JSON.stringify(obj.anmeldedatum)) + ',';
		}
		if (obj.aufnahmedatum !== undefined) {
			result += '"aufnahmedatum" : ' + ((obj.aufnahmedatum === null) ? 'null' : JSON.stringify(obj.aufnahmedatum)) + ',';
		}
		if (obj.beginnBildungsgang !== undefined) {
			result += '"beginnBildungsgang" : ' + ((obj.beginnBildungsgang === null) ? 'null' : JSON.stringify(obj.beginnBildungsgang)) + ',';
		}
		if (obj.dauerBildungsgang !== undefined) {
			result += '"dauerBildungsgang" : ' + ((obj.dauerBildungsgang === null) ? 'null' : obj.dauerBildungsgang.toString()) + ',';
		}
		if (obj.idReligion !== undefined) {
			result += '"idReligion" : ' + ((obj.idReligion === null) ? 'null' : obj.idReligion.toString()) + ',';
		}
		if (obj.idSchuljahresabschnitt !== undefined) {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		}
		if (obj.idJahrgang !== undefined) {
			result += '"idJahrgang" : ' + ((obj.idJahrgang === null) ? 'null' : obj.idJahrgang.toString()) + ',';
		}
		if (obj.idKlasse !== undefined) {
			result += '"idKlasse" : ' + ((obj.idKlasse === null) ? 'null' : obj.idKlasse.toString()) + ',';
		}
		if (obj.idGrundschuleEinschulungsart !== undefined) {
			result += '"idGrundschuleEinschulungsart" : ' + ((obj.idGrundschuleEinschulungsart === null) ? 'null' : obj.idGrundschuleEinschulungsart.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schueler_SchuelerNeu(obj: unknown): SchuelerNeu {
	return obj as SchuelerNeu;
}
