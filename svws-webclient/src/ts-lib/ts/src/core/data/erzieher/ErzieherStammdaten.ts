import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { JavaBoolean, cast_java_lang_Boolean } from '../../../java/lang/JavaBoolean';

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
	public idErzieherArt : Number | null = null;

	/**
	 * Die Titel des Erziehers. 
	 */
	public titel : String | null = null;

	/**
	 * Die Anrede des Erziehers. 
	 */
	public anrede : String | null = null;

	/**
	 * Der Name des Erziehers. 
	 */
	public nachname : String | null = null;

	/**
	 * Ggf. Zusatz zum Nachnamen des Erziehers. 
	 */
	public zusatzNachname : String | null = null;

	/**
	 * Der Vorname des Erziehers. 
	 */
	public vorname : String | null = null;

	/**
	 * Ggf. der Straßenname im Wohnort des Erziehers. 
	 */
	public strassenname : String | null = null;

	/**
	 * Ggf. die Hausnummer zur Straße im Wohnort des Erziehers. 
	 */
	public hausnummer : String | null = null;

	/**
	 * Ggf. der Hausnummerzusatz zur Straße im Wohnort des Erziehers. 
	 */
	public hausnummerZusatz : String | null = null;

	/**
	 * Die ID des Wohnortes des Erziehers. 
	 */
	public wohnortID : Number | null = null;

	/**
	 * Die ID des Ortsteils des Erziehers. 
	 */
	public ortsteilID : Number | null = null;

	/**
	 * Gibt an, ob der Erzieher Anschreiben erhält oder nicht. 
	 */
	public erhaeltAnschreiben : Boolean | null = null;

	/**
	 * Die E-Mailadresse des Erziehers. 
	 */
	public eMail : String | null = null;

	/**
	 * Die ID der Staatsangehörigkeit des Erziehers. 
	 */
	public staatsangehoerigkeitID : String | null = null;

	/**
	 * Anmerkungen zum Erziehers. 
	 */
	public bemerkungen : String | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.erzieher.ErzieherStammdaten'].includes(name);
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
		result.idErzieherArt = typeof obj.idErzieherArt === "undefined" ? null : obj.idErzieherArt === null ? null : Number(obj.idErzieherArt);
		result.titel = typeof obj.titel === "undefined" ? null : obj.titel === null ? null : String(obj.titel);
		result.anrede = typeof obj.anrede === "undefined" ? null : obj.anrede === null ? null : String(obj.anrede);
		result.nachname = typeof obj.nachname === "undefined" ? null : obj.nachname === null ? null : String(obj.nachname);
		result.zusatzNachname = typeof obj.zusatzNachname === "undefined" ? null : obj.zusatzNachname === null ? null : String(obj.zusatzNachname);
		result.vorname = typeof obj.vorname === "undefined" ? null : obj.vorname === null ? null : String(obj.vorname);
		result.strassenname = typeof obj.strassenname === "undefined" ? null : obj.strassenname === null ? null : String(obj.strassenname);
		result.hausnummer = typeof obj.hausnummer === "undefined" ? null : obj.hausnummer === null ? null : String(obj.hausnummer);
		result.hausnummerZusatz = typeof obj.hausnummerZusatz === "undefined" ? null : obj.hausnummerZusatz === null ? null : String(obj.hausnummerZusatz);
		result.wohnortID = typeof obj.wohnortID === "undefined" ? null : obj.wohnortID === null ? null : Number(obj.wohnortID);
		result.ortsteilID = typeof obj.ortsteilID === "undefined" ? null : obj.ortsteilID === null ? null : Number(obj.ortsteilID);
		result.erhaeltAnschreiben = typeof obj.erhaeltAnschreiben === "undefined" ? null : obj.erhaeltAnschreiben === null ? null : Boolean(obj.erhaeltAnschreiben);
		result.eMail = typeof obj.eMail === "undefined" ? null : obj.eMail === null ? null : String(obj.eMail);
		result.staatsangehoerigkeitID = typeof obj.staatsangehoerigkeitID === "undefined" ? null : obj.staatsangehoerigkeitID === null ? null : String(obj.staatsangehoerigkeitID);
		result.bemerkungen = typeof obj.bemerkungen === "undefined" ? null : obj.bemerkungen === null ? null : String(obj.bemerkungen);
		return result;
	}

	public static transpilerToJSON(obj : ErzieherStammdaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idSchueler" : ' + obj.idSchueler + ',';
		result += '"idErzieherArt" : ' + ((!obj.idErzieherArt) ? 'null' : obj.idErzieherArt.valueOf()) + ',';
		result += '"titel" : ' + ((!obj.titel) ? 'null' : '"' + obj.titel.valueOf() + '"') + ',';
		result += '"anrede" : ' + ((!obj.anrede) ? 'null' : '"' + obj.anrede.valueOf() + '"') + ',';
		result += '"nachname" : ' + ((!obj.nachname) ? 'null' : '"' + obj.nachname.valueOf() + '"') + ',';
		result += '"zusatzNachname" : ' + ((!obj.zusatzNachname) ? 'null' : '"' + obj.zusatzNachname.valueOf() + '"') + ',';
		result += '"vorname" : ' + ((!obj.vorname) ? 'null' : '"' + obj.vorname.valueOf() + '"') + ',';
		result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : '"' + obj.strassenname.valueOf() + '"') + ',';
		result += '"hausnummer" : ' + ((!obj.hausnummer) ? 'null' : '"' + obj.hausnummer.valueOf() + '"') + ',';
		result += '"hausnummerZusatz" : ' + ((!obj.hausnummerZusatz) ? 'null' : '"' + obj.hausnummerZusatz.valueOf() + '"') + ',';
		result += '"wohnortID" : ' + ((!obj.wohnortID) ? 'null' : obj.wohnortID.valueOf()) + ',';
		result += '"ortsteilID" : ' + ((!obj.ortsteilID) ? 'null' : obj.ortsteilID.valueOf()) + ',';
		result += '"erhaeltAnschreiben" : ' + ((!obj.erhaeltAnschreiben) ? 'null' : obj.erhaeltAnschreiben.valueOf()) + ',';
		result += '"eMail" : ' + ((!obj.eMail) ? 'null' : '"' + obj.eMail.valueOf() + '"') + ',';
		result += '"staatsangehoerigkeitID" : ' + ((!obj.staatsangehoerigkeitID) ? 'null' : '"' + obj.staatsangehoerigkeitID.valueOf() + '"') + ',';
		result += '"bemerkungen" : ' + ((!obj.bemerkungen) ? 'null' : '"' + obj.bemerkungen.valueOf() + '"') + ',';
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
			result += '"idErzieherArt" : ' + ((!obj.idErzieherArt) ? 'null' : obj.idErzieherArt.valueOf()) + ',';
		}
		if (typeof obj.titel !== "undefined") {
			result += '"titel" : ' + ((!obj.titel) ? 'null' : '"' + obj.titel.valueOf() + '"') + ',';
		}
		if (typeof obj.anrede !== "undefined") {
			result += '"anrede" : ' + ((!obj.anrede) ? 'null' : '"' + obj.anrede.valueOf() + '"') + ',';
		}
		if (typeof obj.nachname !== "undefined") {
			result += '"nachname" : ' + ((!obj.nachname) ? 'null' : '"' + obj.nachname.valueOf() + '"') + ',';
		}
		if (typeof obj.zusatzNachname !== "undefined") {
			result += '"zusatzNachname" : ' + ((!obj.zusatzNachname) ? 'null' : '"' + obj.zusatzNachname.valueOf() + '"') + ',';
		}
		if (typeof obj.vorname !== "undefined") {
			result += '"vorname" : ' + ((!obj.vorname) ? 'null' : '"' + obj.vorname.valueOf() + '"') + ',';
		}
		if (typeof obj.strassenname !== "undefined") {
			result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : '"' + obj.strassenname.valueOf() + '"') + ',';
		}
		if (typeof obj.hausnummer !== "undefined") {
			result += '"hausnummer" : ' + ((!obj.hausnummer) ? 'null' : '"' + obj.hausnummer.valueOf() + '"') + ',';
		}
		if (typeof obj.hausnummerZusatz !== "undefined") {
			result += '"hausnummerZusatz" : ' + ((!obj.hausnummerZusatz) ? 'null' : '"' + obj.hausnummerZusatz.valueOf() + '"') + ',';
		}
		if (typeof obj.wohnortID !== "undefined") {
			result += '"wohnortID" : ' + ((!obj.wohnortID) ? 'null' : obj.wohnortID.valueOf()) + ',';
		}
		if (typeof obj.ortsteilID !== "undefined") {
			result += '"ortsteilID" : ' + ((!obj.ortsteilID) ? 'null' : obj.ortsteilID.valueOf()) + ',';
		}
		if (typeof obj.erhaeltAnschreiben !== "undefined") {
			result += '"erhaeltAnschreiben" : ' + ((!obj.erhaeltAnschreiben) ? 'null' : obj.erhaeltAnschreiben.valueOf()) + ',';
		}
		if (typeof obj.eMail !== "undefined") {
			result += '"eMail" : ' + ((!obj.eMail) ? 'null' : '"' + obj.eMail.valueOf() + '"') + ',';
		}
		if (typeof obj.staatsangehoerigkeitID !== "undefined") {
			result += '"staatsangehoerigkeitID" : ' + ((!obj.staatsangehoerigkeitID) ? 'null' : '"' + obj.staatsangehoerigkeitID.valueOf() + '"') + ',';
		}
		if (typeof obj.bemerkungen !== "undefined") {
			result += '"bemerkungen" : ' + ((!obj.bemerkungen) ? 'null' : '"' + obj.bemerkungen.valueOf() + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_erzieher_ErzieherStammdaten(obj : unknown) : ErzieherStammdaten {
	return obj as ErzieherStammdaten;
}
