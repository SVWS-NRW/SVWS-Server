import { JavaObject } from '../../../java/lang/JavaObject';

export class ErzieherStammdaten extends JavaObject {

	/**
	 * Die ID des Erziehers.
	 */
	public id : number = 0;

	/**
	 * Die ID des Schülers, welchem der Erzieher zugeordnet ist.
	 */
	public idSchueler : number = 0;

	/**
	 * Die ID der Art des Erziehereintrages
	 */
	public idErzieherArt : number | null = null;

	/**
	 * Die Titel des Erziehers.
	 */
	public titel : string | null = null;

	/**
	 * Die Anrede des Erziehers.
	 */
	public anrede : string | null = null;

	/**
	 * Der Name des Erziehers.
	 */
	public nachname : string | null = null;

	/**
	 * Ggf. Zusatz zum Nachnamen des Erziehers.
	 */
	public zusatzNachname : string | null = null;

	/**
	 * Der Vorname des Erziehers.
	 */
	public vorname : string | null = null;

	/**
	 * Ggf. der Straßenname im Wohnort des Erziehers.
	 */
	public strassenname : string | null = null;

	/**
	 * Ggf. die Hausnummer zur Straße im Wohnort des Erziehers.
	 */
	public hausnummer : string | null = null;

	/**
	 * Ggf. der Hausnummerzusatz zur Straße im Wohnort des Erziehers.
	 */
	public hausnummerZusatz : string | null = null;

	/**
	 * Die ID des Wohnortes des Erziehers.
	 */
	public wohnortID : number | null = null;

	/**
	 * Die ID des Ortsteils des Erziehers.
	 */
	public ortsteilID : number | null = null;

	/**
	 * Gibt an, ob der Erzieher Anschreiben erhält oder nicht.
	 */
	public erhaeltAnschreiben : boolean | null = null;

	/**
	 * Die E-Mailadresse des Erziehers.
	 */
	public eMail : string | null = null;

	/**
	 * Die ID der Staatsangehörigkeit des Erziehers.
	 */
	public staatsangehoerigkeitID : string | null = null;

	/**
	 * Anmerkungen zum Erziehers.
	 */
	public bemerkungen : string | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.erzieher.ErzieherStammdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): ErzieherStammdaten {
		const obj = JSON.parse(json);
		const result = new ErzieherStammdaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.idSchueler === "undefined")
			 throw new Error('invalid json format, missing attribute idSchueler');
		result.idSchueler = obj.idSchueler;
		result.idErzieherArt = typeof obj.idErzieherArt === "undefined" ? null : obj.idErzieherArt === null ? null : obj.idErzieherArt;
		result.titel = typeof obj.titel === "undefined" ? null : obj.titel === null ? null : obj.titel;
		result.anrede = typeof obj.anrede === "undefined" ? null : obj.anrede === null ? null : obj.anrede;
		result.nachname = typeof obj.nachname === "undefined" ? null : obj.nachname === null ? null : obj.nachname;
		result.zusatzNachname = typeof obj.zusatzNachname === "undefined" ? null : obj.zusatzNachname === null ? null : obj.zusatzNachname;
		result.vorname = typeof obj.vorname === "undefined" ? null : obj.vorname === null ? null : obj.vorname;
		result.strassenname = typeof obj.strassenname === "undefined" ? null : obj.strassenname === null ? null : obj.strassenname;
		result.hausnummer = typeof obj.hausnummer === "undefined" ? null : obj.hausnummer === null ? null : obj.hausnummer;
		result.hausnummerZusatz = typeof obj.hausnummerZusatz === "undefined" ? null : obj.hausnummerZusatz === null ? null : obj.hausnummerZusatz;
		result.wohnortID = typeof obj.wohnortID === "undefined" ? null : obj.wohnortID === null ? null : obj.wohnortID;
		result.ortsteilID = typeof obj.ortsteilID === "undefined" ? null : obj.ortsteilID === null ? null : obj.ortsteilID;
		result.erhaeltAnschreiben = typeof obj.erhaeltAnschreiben === "undefined" ? null : obj.erhaeltAnschreiben === null ? null : obj.erhaeltAnschreiben;
		result.eMail = typeof obj.eMail === "undefined" ? null : obj.eMail === null ? null : obj.eMail;
		result.staatsangehoerigkeitID = typeof obj.staatsangehoerigkeitID === "undefined" ? null : obj.staatsangehoerigkeitID === null ? null : obj.staatsangehoerigkeitID;
		result.bemerkungen = typeof obj.bemerkungen === "undefined" ? null : obj.bemerkungen === null ? null : obj.bemerkungen;
		return result;
	}

	public static transpilerToJSON(obj : ErzieherStammdaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idSchueler" : ' + obj.idSchueler + ',';
		result += '"idErzieherArt" : ' + ((!obj.idErzieherArt) ? 'null' : obj.idErzieherArt) + ',';
		result += '"titel" : ' + ((!obj.titel) ? 'null' : JSON.stringify(obj.titel)) + ',';
		result += '"anrede" : ' + ((!obj.anrede) ? 'null' : JSON.stringify(obj.anrede)) + ',';
		result += '"nachname" : ' + ((!obj.nachname) ? 'null' : JSON.stringify(obj.nachname)) + ',';
		result += '"zusatzNachname" : ' + ((!obj.zusatzNachname) ? 'null' : JSON.stringify(obj.zusatzNachname)) + ',';
		result += '"vorname" : ' + ((!obj.vorname) ? 'null' : JSON.stringify(obj.vorname)) + ',';
		result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		result += '"hausnummer" : ' + ((!obj.hausnummer) ? 'null' : JSON.stringify(obj.hausnummer)) + ',';
		result += '"hausnummerZusatz" : ' + ((!obj.hausnummerZusatz) ? 'null' : JSON.stringify(obj.hausnummerZusatz)) + ',';
		result += '"wohnortID" : ' + ((!obj.wohnortID) ? 'null' : obj.wohnortID) + ',';
		result += '"ortsteilID" : ' + ((!obj.ortsteilID) ? 'null' : obj.ortsteilID) + ',';
		result += '"erhaeltAnschreiben" : ' + ((!obj.erhaeltAnschreiben) ? 'null' : obj.erhaeltAnschreiben) + ',';
		result += '"eMail" : ' + ((!obj.eMail) ? 'null' : JSON.stringify(obj.eMail)) + ',';
		result += '"staatsangehoerigkeitID" : ' + ((!obj.staatsangehoerigkeitID) ? 'null' : JSON.stringify(obj.staatsangehoerigkeitID)) + ',';
		result += '"bemerkungen" : ' + ((!obj.bemerkungen) ? 'null' : JSON.stringify(obj.bemerkungen)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ErzieherStammdaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.idSchueler !== "undefined") {
			result += '"idSchueler" : ' + obj.idSchueler + ',';
		}
		if (typeof obj.idErzieherArt !== "undefined") {
			result += '"idErzieherArt" : ' + ((!obj.idErzieherArt) ? 'null' : obj.idErzieherArt) + ',';
		}
		if (typeof obj.titel !== "undefined") {
			result += '"titel" : ' + ((!obj.titel) ? 'null' : JSON.stringify(obj.titel)) + ',';
		}
		if (typeof obj.anrede !== "undefined") {
			result += '"anrede" : ' + ((!obj.anrede) ? 'null' : JSON.stringify(obj.anrede)) + ',';
		}
		if (typeof obj.nachname !== "undefined") {
			result += '"nachname" : ' + ((!obj.nachname) ? 'null' : JSON.stringify(obj.nachname)) + ',';
		}
		if (typeof obj.zusatzNachname !== "undefined") {
			result += '"zusatzNachname" : ' + ((!obj.zusatzNachname) ? 'null' : JSON.stringify(obj.zusatzNachname)) + ',';
		}
		if (typeof obj.vorname !== "undefined") {
			result += '"vorname" : ' + ((!obj.vorname) ? 'null' : JSON.stringify(obj.vorname)) + ',';
		}
		if (typeof obj.strassenname !== "undefined") {
			result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		}
		if (typeof obj.hausnummer !== "undefined") {
			result += '"hausnummer" : ' + ((!obj.hausnummer) ? 'null' : JSON.stringify(obj.hausnummer)) + ',';
		}
		if (typeof obj.hausnummerZusatz !== "undefined") {
			result += '"hausnummerZusatz" : ' + ((!obj.hausnummerZusatz) ? 'null' : JSON.stringify(obj.hausnummerZusatz)) + ',';
		}
		if (typeof obj.wohnortID !== "undefined") {
			result += '"wohnortID" : ' + ((!obj.wohnortID) ? 'null' : obj.wohnortID) + ',';
		}
		if (typeof obj.ortsteilID !== "undefined") {
			result += '"ortsteilID" : ' + ((!obj.ortsteilID) ? 'null' : obj.ortsteilID) + ',';
		}
		if (typeof obj.erhaeltAnschreiben !== "undefined") {
			result += '"erhaeltAnschreiben" : ' + ((!obj.erhaeltAnschreiben) ? 'null' : obj.erhaeltAnschreiben) + ',';
		}
		if (typeof obj.eMail !== "undefined") {
			result += '"eMail" : ' + ((!obj.eMail) ? 'null' : JSON.stringify(obj.eMail)) + ',';
		}
		if (typeof obj.staatsangehoerigkeitID !== "undefined") {
			result += '"staatsangehoerigkeitID" : ' + ((!obj.staatsangehoerigkeitID) ? 'null' : JSON.stringify(obj.staatsangehoerigkeitID)) + ',';
		}
		if (typeof obj.bemerkungen !== "undefined") {
			result += '"bemerkungen" : ' + ((!obj.bemerkungen) ? 'null' : JSON.stringify(obj.bemerkungen)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_erzieher_ErzieherStammdaten(obj : unknown) : ErzieherStammdaten {
	return obj as ErzieherStammdaten;
}
