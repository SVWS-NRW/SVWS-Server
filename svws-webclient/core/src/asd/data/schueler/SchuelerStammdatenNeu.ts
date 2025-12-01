import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuelerStammdatenNeu extends JavaObject {

	/**
	 * Die ID des Schülerdatensatzes.
	 */
	public id: number = 0;

	/**
	 * Der Nachname des Schülerdatensatzes.
	 */
	public nachname: string = "";

	/**
	 * Der Vorname des Schülerdatensatzes.
	 */
	public vorname: string = "";

	/**
	 * Alle Vornamen, sofern es mehrere gibt, des Schülerdatensatzes.
	 */
	public alleVornamen: string = "";

	/**
	 * Die ID des Geschlechtes
	 */
	public geschlecht: number = 0;

	/**
	 * Das Geburtsdatum des Schülerdatensatzes.
	 */
	public geburtsdatum: string | null = null;

	/**
	 * Die ID des Status des Schülerdatensatzes.
	 */
	public status: number = 0;

	/**
	 * Das Anmeldedatum des Schülerdatensatzes.
	 */
	public anmeldedatum: string | null = null;

	/**
	 * Das Aufnahmedatum des Schülerdatensatzes.
	 */
	public aufnahmedatum: string | null = null;

	/**
	 * Der Beginn des Bildungsgangs eines Schülers.
	 */
	public beginnBildungsgang: string | null = null;

	/**
	 * Dauer des Bildungsgangs am BK eines Schülers.
	 */
	public dauerBildungsgang: number | null = null;

	/**
	 * Die ID des Schuljahresabschnitts, zu welchem diese Lernabschnittdaten gehören.
	 */
	public schuljahresabschnitt: number = 0;

	/**
	 * Die ID des Jahrgangs des Schülers oder null, falls kein Jahrgang zugeordnet ist
	 */
	public jahrgangID: number | null = null;

	/**
	 * Die ID der Klasse des Schülers oder null, falls keine Klasse zugeordnet ist.
	 */
	public klassenID: number | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schueler.SchuelerStammdatenNeu';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.schueler.SchuelerStammdatenNeu'].includes(name);
	}

	public static readonly class = new Class<SchuelerStammdatenNeu>('de.svws_nrw.asd.data.schueler.SchuelerStammdatenNeu');

	public static transpilerFromJSON(json: string): SchuelerStammdatenNeu {
		const obj = JSON.parse(json) as Partial<SchuelerStammdatenNeu>;
		const result = new SchuelerStammdatenNeu();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.nachname === undefined)
			throw new Error('invalid json format, missing attribute nachname');
		result.nachname = obj.nachname;
		if (obj.vorname === undefined)
			throw new Error('invalid json format, missing attribute vorname');
		result.vorname = obj.vorname;
		if (obj.alleVornamen === undefined)
			throw new Error('invalid json format, missing attribute alleVornamen');
		result.alleVornamen = obj.alleVornamen;
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
		if (obj.schuljahresabschnitt === undefined)
			throw new Error('invalid json format, missing attribute schuljahresabschnitt');
		result.schuljahresabschnitt = obj.schuljahresabschnitt;
		result.jahrgangID = (obj.jahrgangID === undefined) ? null : obj.jahrgangID === null ? null : obj.jahrgangID;
		result.klassenID = (obj.klassenID === undefined) ? null : obj.klassenID === null ? null : obj.klassenID;
		return result;
	}

	public static transpilerToJSON(obj: SchuelerStammdatenNeu): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"nachname" : ' + JSON.stringify(obj.nachname) + ',';
		result += '"vorname" : ' + JSON.stringify(obj.vorname) + ',';
		result += '"alleVornamen" : ' + JSON.stringify(obj.alleVornamen) + ',';
		result += '"geschlecht" : ' + obj.geschlecht.toString() + ',';
		result += '"geburtsdatum" : ' + ((obj.geburtsdatum === null) ? 'null' : JSON.stringify(obj.geburtsdatum)) + ',';
		result += '"status" : ' + obj.status.toString() + ',';
		result += '"anmeldedatum" : ' + ((obj.anmeldedatum === null) ? 'null' : JSON.stringify(obj.anmeldedatum)) + ',';
		result += '"aufnahmedatum" : ' + ((obj.aufnahmedatum === null) ? 'null' : JSON.stringify(obj.aufnahmedatum)) + ',';
		result += '"beginnBildungsgang" : ' + ((obj.beginnBildungsgang === null) ? 'null' : JSON.stringify(obj.beginnBildungsgang)) + ',';
		result += '"dauerBildungsgang" : ' + ((obj.dauerBildungsgang === null) ? 'null' : obj.dauerBildungsgang.toString()) + ',';
		result += '"schuljahresabschnitt" : ' + obj.schuljahresabschnitt.toString() + ',';
		result += '"jahrgangID" : ' + ((obj.jahrgangID === null) ? 'null' : obj.jahrgangID.toString()) + ',';
		result += '"klassenID" : ' + ((obj.klassenID === null) ? 'null' : obj.klassenID.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchuelerStammdatenNeu>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.nachname !== undefined) {
			result += '"nachname" : ' + JSON.stringify(obj.nachname) + ',';
		}
		if (obj.vorname !== undefined) {
			result += '"vorname" : ' + JSON.stringify(obj.vorname) + ',';
		}
		if (obj.alleVornamen !== undefined) {
			result += '"alleVornamen" : ' + JSON.stringify(obj.alleVornamen) + ',';
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
		if (obj.schuljahresabschnitt !== undefined) {
			result += '"schuljahresabschnitt" : ' + obj.schuljahresabschnitt.toString() + ',';
		}
		if (obj.jahrgangID !== undefined) {
			result += '"jahrgangID" : ' + ((obj.jahrgangID === null) ? 'null' : obj.jahrgangID.toString()) + ',';
		}
		if (obj.klassenID !== undefined) {
			result += '"klassenID" : ' + ((obj.klassenID === null) ? 'null' : obj.klassenID.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schueler_SchuelerStammdatenNeu(obj: unknown): SchuelerStammdatenNeu {
	return obj as SchuelerStammdatenNeu;
}
