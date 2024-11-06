import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class Schueler extends JavaObject {

	/**
	 * Die ID des Schülerdatensatzes.
	 */
	public id : number = 0;

	/**
	 * Der Nachname des Schülerdatensatzes.
	 */
	public nachname : string = "";

	/**
	 * Der Vorname des Schülerdatensatzes.
	 */
	public vorname : string = "";

	/**
	 * Die ID des Geschlechtes. Gültige Werte sind im Enum-Typ {@link Geschlecht} definiert.
	 */
	public geschlecht : number = 0;

	/**
	 * Der Status des Schülerdatensatzes. Gültige Werte sind im Enum-Typ {@link SchuelerStatus} definiert.
	 */
	public status : number = 0;

	/**
	 * Ggf. der Abschlussjahrgang, dem der Schüler aktuell zugeordnet ist
	 */
	public abschlussjahrgang : number = -1;

	/**
	 * Das Schulnummer bei einem externen Schüler oder null, wenn der Schüler kein externer Schüler ist.
	 */
	public externeSchulNr : string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schueler.Schueler';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.Schueler'].includes(name);
	}

	public static class = new Class<Schueler>('de.svws_nrw.core.data.schueler.Schueler');

	public static transpilerFromJSON(json : string): Schueler {
		const obj = JSON.parse(json) as Partial<Schueler>;
		const result = new Schueler();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.nachname === undefined)
			throw new Error('invalid json format, missing attribute nachname');
		result.nachname = obj.nachname;
		if (obj.vorname === undefined)
			throw new Error('invalid json format, missing attribute vorname');
		result.vorname = obj.vorname;
		if (obj.geschlecht === undefined)
			throw new Error('invalid json format, missing attribute geschlecht');
		result.geschlecht = obj.geschlecht;
		if (obj.status === undefined)
			throw new Error('invalid json format, missing attribute status');
		result.status = obj.status;
		if (obj.abschlussjahrgang === undefined)
			throw new Error('invalid json format, missing attribute abschlussjahrgang');
		result.abschlussjahrgang = obj.abschlussjahrgang;
		result.externeSchulNr = (obj.externeSchulNr === undefined) ? null : obj.externeSchulNr === null ? null : obj.externeSchulNr;
		return result;
	}

	public static transpilerToJSON(obj : Schueler) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"nachname" : ' + JSON.stringify(obj.nachname) + ',';
		result += '"vorname" : ' + JSON.stringify(obj.vorname) + ',';
		result += '"geschlecht" : ' + obj.geschlecht.toString() + ',';
		result += '"status" : ' + obj.status.toString() + ',';
		result += '"abschlussjahrgang" : ' + obj.abschlussjahrgang.toString() + ',';
		result += '"externeSchulNr" : ' + ((obj.externeSchulNr === null) ? 'null' : JSON.stringify(obj.externeSchulNr)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schueler>) : string {
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
		if (obj.geschlecht !== undefined) {
			result += '"geschlecht" : ' + obj.geschlecht.toString() + ',';
		}
		if (obj.status !== undefined) {
			result += '"status" : ' + obj.status.toString() + ',';
		}
		if (obj.abschlussjahrgang !== undefined) {
			result += '"abschlussjahrgang" : ' + obj.abschlussjahrgang.toString() + ',';
		}
		if (obj.externeSchulNr !== undefined) {
			result += '"externeSchulNr" : ' + ((obj.externeSchulNr === null) ? 'null' : JSON.stringify(obj.externeSchulNr)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schueler_Schueler(obj : unknown) : Schueler {
	return obj as Schueler;
}
