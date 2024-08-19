import { JavaObject } from '../../../java/lang/JavaObject';
import { BenutzergruppeDaten } from '../../../core/data/benutzer/BenutzergruppeDaten';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';

export class BenutzerDaten extends JavaObject {

	/**
	 * Die ID des Benutzers.
	 */
	public id : number = -1;

	/**
	 * Der Typ des Benutzers.
	 */
	public typ : number = 0;

	/**
	 * die ID des Benutzers in der Typ-spezifischen-Tabelle (z.B. Lehrer-ID)
	 */
	public typID : number = -1;

	/**
	 * Der Anzeigename des Benutzers.
	 */
	public anzeigename : string = "";

	/**
	 * Der Anmeldename des Benutzers
	 */
	public name : string = "";

	/**
	 * Gibt an, ob es sich um einen Administrativen Benutzer handelt oder nicht.
	 */
	public istAdmin : boolean = false;

	/**
	 * Die ID der Credentials des Benutzers.
	 */
	public idCredentials : number = -1;

	/**
	 * Die Daten der Benutzergruppen, denen dieser Benutzer zugeordnet ist.
	 */
	public gruppen : List<BenutzergruppeDaten> = new ArrayList<BenutzergruppeDaten>();

	/**
	 * Die Kompetenzen, die speziell diesem Benutzer zugeordnet sind.
	 */
	public kompetenzen : List<number> = new ArrayList<number>();

	/**
	 * Die IDs der Klassen bei denen der Benutzer funktionsbezogene Kompetenzen hat - entweder über Klassenleitungen oder über Abteilungsleitungen.
	 */
	public kompetenzenKlassen : List<number> = new ArrayList<number>();

	/**
	 * Die IDs der aktuellen Lehrer-Leitungsfunktionen, welche diesem Benutzer zugeordnet sind. Dies kann auch für funktionsbezogene Kompetenzen genutzt werden.
	 */
	public leitungsfunktionen : List<number> = new ArrayList<number>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.benutzer.BenutzerDaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.benutzer.BenutzerDaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): BenutzerDaten {
		const obj = JSON.parse(json) as Partial<BenutzerDaten>;
		const result = new BenutzerDaten();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.typ === undefined)
			throw new Error('invalid json format, missing attribute typ');
		result.typ = obj.typ;
		if (obj.typID === undefined)
			throw new Error('invalid json format, missing attribute typID');
		result.typID = obj.typID;
		if (obj.anzeigename === undefined)
			throw new Error('invalid json format, missing attribute anzeigename');
		result.anzeigename = obj.anzeigename;
		if (obj.name === undefined)
			throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (obj.istAdmin === undefined)
			throw new Error('invalid json format, missing attribute istAdmin');
		result.istAdmin = obj.istAdmin;
		if (obj.idCredentials === undefined)
			throw new Error('invalid json format, missing attribute idCredentials');
		result.idCredentials = obj.idCredentials;
		if (obj.gruppen !== undefined) {
			for (const elem of obj.gruppen) {
				result.gruppen.add(BenutzergruppeDaten.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.kompetenzen !== undefined) {
			for (const elem of obj.kompetenzen) {
				result.kompetenzen.add(elem);
			}
		}
		if (obj.kompetenzenKlassen !== undefined) {
			for (const elem of obj.kompetenzenKlassen) {
				result.kompetenzenKlassen.add(elem);
			}
		}
		if (obj.leitungsfunktionen !== undefined) {
			for (const elem of obj.leitungsfunktionen) {
				result.leitungsfunktionen.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : BenutzerDaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"typ" : ' + obj.typ.toString() + ',';
		result += '"typID" : ' + obj.typID.toString() + ',';
		result += '"anzeigename" : ' + JSON.stringify(obj.anzeigename) + ',';
		result += '"name" : ' + JSON.stringify(obj.name) + ',';
		result += '"istAdmin" : ' + obj.istAdmin.toString() + ',';
		result += '"idCredentials" : ' + obj.idCredentials.toString() + ',';
		result += '"gruppen" : [ ';
		for (let i = 0; i < obj.gruppen.size(); i++) {
			const elem = obj.gruppen.get(i);
			result += BenutzergruppeDaten.transpilerToJSON(elem);
			if (i < obj.gruppen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"kompetenzen" : [ ';
		for (let i = 0; i < obj.kompetenzen.size(); i++) {
			const elem = obj.kompetenzen.get(i);
			result += elem.toString();
			if (i < obj.kompetenzen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"kompetenzenKlassen" : [ ';
		for (let i = 0; i < obj.kompetenzenKlassen.size(); i++) {
			const elem = obj.kompetenzenKlassen.get(i);
			result += elem.toString();
			if (i < obj.kompetenzenKlassen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"leitungsfunktionen" : [ ';
		for (let i = 0; i < obj.leitungsfunktionen.size(); i++) {
			const elem = obj.leitungsfunktionen.get(i);
			result += elem.toString();
			if (i < obj.leitungsfunktionen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BenutzerDaten>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.typ !== undefined) {
			result += '"typ" : ' + obj.typ.toString() + ',';
		}
		if (obj.typID !== undefined) {
			result += '"typID" : ' + obj.typID.toString() + ',';
		}
		if (obj.anzeigename !== undefined) {
			result += '"anzeigename" : ' + JSON.stringify(obj.anzeigename) + ',';
		}
		if (obj.name !== undefined) {
			result += '"name" : ' + JSON.stringify(obj.name) + ',';
		}
		if (obj.istAdmin !== undefined) {
			result += '"istAdmin" : ' + obj.istAdmin.toString() + ',';
		}
		if (obj.idCredentials !== undefined) {
			result += '"idCredentials" : ' + obj.idCredentials.toString() + ',';
		}
		if (obj.gruppen !== undefined) {
			result += '"gruppen" : [ ';
			for (let i = 0; i < obj.gruppen.size(); i++) {
				const elem = obj.gruppen.get(i);
				result += BenutzergruppeDaten.transpilerToJSON(elem);
				if (i < obj.gruppen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.kompetenzen !== undefined) {
			result += '"kompetenzen" : [ ';
			for (let i = 0; i < obj.kompetenzen.size(); i++) {
				const elem = obj.kompetenzen.get(i);
				result += elem.toString();
				if (i < obj.kompetenzen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.kompetenzenKlassen !== undefined) {
			result += '"kompetenzenKlassen" : [ ';
			for (let i = 0; i < obj.kompetenzenKlassen.size(); i++) {
				const elem = obj.kompetenzenKlassen.get(i);
				result += elem.toString();
				if (i < obj.kompetenzenKlassen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.leitungsfunktionen !== undefined) {
			result += '"leitungsfunktionen" : [ ';
			for (let i = 0; i < obj.leitungsfunktionen.size(); i++) {
				const elem = obj.leitungsfunktionen.get(i);
				result += elem.toString();
				if (i < obj.leitungsfunktionen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_benutzer_BenutzerDaten(obj : unknown) : BenutzerDaten {
	return obj as BenutzerDaten;
}
