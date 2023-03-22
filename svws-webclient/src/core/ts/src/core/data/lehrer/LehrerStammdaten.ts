import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class LehrerStammdaten extends JavaObject {

	/**
	 * Die ID des Lehrers.
	 */
	public id : number = 0;

	/**
	 * Das Kürzel des Lehrers.
	 */
	public kuerzel : string = "";

	/**
	 * Die Bezeichnung des Personals-Typs des Lehrers.
	 */
	public personalTyp : string = "";

	/**
	 * Ggf. die Anrede des Lehrers.
	 */
	public anrede : string | null = null;

	/**
	 * Ggf. ein akademischer Grad des Lehrers.
	 */
	public titel : string | null = null;

	/**
	 * Ggf. die Amtsbezeichnung des Lehrers.
	 */
	public amtsbezeichnung : string | null = null;

	/**
	 * Der Nachname des Lehrers.
	 */
	public nachname : string = "";

	/**
	 * Der Vorname des Lehrers.
	 */
	public vorname : string = "";

	/**
	 * Die ID des Geschlechtes
	 */
	public geschlecht : number = 0;

	/**
	 * Das Geburtsdatum des Lehrers.
	 */
	public geburtsdatum : string | null = null;

	/**
	 * Ggf. die ID für die Staatsangehörigkeit des Lehrers.
	 */
	public staatsangehoerigkeitID : string | null = null;

	/**
	 * Ggf. der Straßenname im Wohnort des Lehrers.
	 */
	public strassenname : string | null = null;

	/**
	 * Ggf. die Hausnummer zur Straße im Wohnort des Lehrers.
	 */
	public hausnummer : string | null = null;

	/**
	 * Ggf. der Hausnummerzusatz zur Straße im Wohnort des Lehrers.
	 */
	public hausnummerZusatz : string | null = null;

	/**
	 * Ggf. die ID des Wohnortes des Lehrers.
	 */
	public wohnortID : number | null = null;

	/**
	 * Ggf. die ID des Ortsteils im Wohnort des Lehrers.
	 */
	public ortsteilID : number | null = null;

	/**
	 * Ggf. die Telefonnummer des Lehrers.
	 */
	public telefon : string | null = null;

	/**
	 * Ggf. die Mobilnummer des Lehrers.
	 */
	public telefonMobil : string | null = null;

	/**
	 * Ggf. die private Email-Adresse des Lehrers.
	 */
	public emailPrivat : string | null = null;

	/**
	 * Ggf. die dienstliche Email-Adresse des Lehrers.
	 */
	public emailDienstlich : string | null = null;

	/**
	 * Ggf. das Foto des Lehrers (jpg, Base64-kodiert des Lehrers.)
	 */
	public foto : string | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.lehrer.LehrerStammdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): LehrerStammdaten {
		const obj = JSON.parse(json);
		const result = new LehrerStammdaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (typeof obj.personalTyp === "undefined")
			 throw new Error('invalid json format, missing attribute personalTyp');
		result.personalTyp = obj.personalTyp;
		result.anrede = typeof obj.anrede === "undefined" ? null : obj.anrede === null ? null : obj.anrede;
		result.titel = typeof obj.titel === "undefined" ? null : obj.titel === null ? null : obj.titel;
		result.amtsbezeichnung = typeof obj.amtsbezeichnung === "undefined" ? null : obj.amtsbezeichnung === null ? null : obj.amtsbezeichnung;
		if (typeof obj.nachname === "undefined")
			 throw new Error('invalid json format, missing attribute nachname');
		result.nachname = obj.nachname;
		if (typeof obj.vorname === "undefined")
			 throw new Error('invalid json format, missing attribute vorname');
		result.vorname = obj.vorname;
		if (typeof obj.geschlecht === "undefined")
			 throw new Error('invalid json format, missing attribute geschlecht');
		result.geschlecht = obj.geschlecht;
		result.geburtsdatum = typeof obj.geburtsdatum === "undefined" ? null : obj.geburtsdatum === null ? null : obj.geburtsdatum;
		result.staatsangehoerigkeitID = typeof obj.staatsangehoerigkeitID === "undefined" ? null : obj.staatsangehoerigkeitID === null ? null : obj.staatsangehoerigkeitID;
		result.strassenname = typeof obj.strassenname === "undefined" ? null : obj.strassenname === null ? null : obj.strassenname;
		result.hausnummer = typeof obj.hausnummer === "undefined" ? null : obj.hausnummer === null ? null : obj.hausnummer;
		result.hausnummerZusatz = typeof obj.hausnummerZusatz === "undefined" ? null : obj.hausnummerZusatz === null ? null : obj.hausnummerZusatz;
		result.wohnortID = typeof obj.wohnortID === "undefined" ? null : obj.wohnortID === null ? null : obj.wohnortID;
		result.ortsteilID = typeof obj.ortsteilID === "undefined" ? null : obj.ortsteilID === null ? null : obj.ortsteilID;
		result.telefon = typeof obj.telefon === "undefined" ? null : obj.telefon === null ? null : obj.telefon;
		result.telefonMobil = typeof obj.telefonMobil === "undefined" ? null : obj.telefonMobil === null ? null : obj.telefonMobil;
		result.emailPrivat = typeof obj.emailPrivat === "undefined" ? null : obj.emailPrivat === null ? null : obj.emailPrivat;
		result.emailDienstlich = typeof obj.emailDienstlich === "undefined" ? null : obj.emailDienstlich === null ? null : obj.emailDienstlich;
		result.foto = typeof obj.foto === "undefined" ? null : obj.foto === null ? null : obj.foto;
		return result;
	}

	public static transpilerToJSON(obj : LehrerStammdaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel! + '"' + ',';
		result += '"personalTyp" : ' + '"' + obj.personalTyp! + '"' + ',';
		result += '"anrede" : ' + ((!obj.anrede) ? 'null' : '"' + obj.anrede + '"') + ',';
		result += '"titel" : ' + ((!obj.titel) ? 'null' : '"' + obj.titel + '"') + ',';
		result += '"amtsbezeichnung" : ' + ((!obj.amtsbezeichnung) ? 'null' : '"' + obj.amtsbezeichnung + '"') + ',';
		result += '"nachname" : ' + '"' + obj.nachname! + '"' + ',';
		result += '"vorname" : ' + '"' + obj.vorname! + '"' + ',';
		result += '"geschlecht" : ' + obj.geschlecht + ',';
		result += '"geburtsdatum" : ' + ((!obj.geburtsdatum) ? 'null' : '"' + obj.geburtsdatum + '"') + ',';
		result += '"staatsangehoerigkeitID" : ' + ((!obj.staatsangehoerigkeitID) ? 'null' : '"' + obj.staatsangehoerigkeitID + '"') + ',';
		result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : '"' + obj.strassenname + '"') + ',';
		result += '"hausnummer" : ' + ((!obj.hausnummer) ? 'null' : '"' + obj.hausnummer + '"') + ',';
		result += '"hausnummerZusatz" : ' + ((!obj.hausnummerZusatz) ? 'null' : '"' + obj.hausnummerZusatz + '"') + ',';
		result += '"wohnortID" : ' + ((!obj.wohnortID) ? 'null' : obj.wohnortID) + ',';
		result += '"ortsteilID" : ' + ((!obj.ortsteilID) ? 'null' : obj.ortsteilID) + ',';
		result += '"telefon" : ' + ((!obj.telefon) ? 'null' : '"' + obj.telefon + '"') + ',';
		result += '"telefonMobil" : ' + ((!obj.telefonMobil) ? 'null' : '"' + obj.telefonMobil + '"') + ',';
		result += '"emailPrivat" : ' + ((!obj.emailPrivat) ? 'null' : '"' + obj.emailPrivat + '"') + ',';
		result += '"emailDienstlich" : ' + ((!obj.emailDienstlich) ? 'null' : '"' + obj.emailDienstlich + '"') + ',';
		result += '"foto" : ' + ((!obj.foto) ? 'null' : '"' + obj.foto + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LehrerStammdaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel + '"' + ',';
		}
		if (typeof obj.personalTyp !== "undefined") {
			result += '"personalTyp" : ' + '"' + obj.personalTyp + '"' + ',';
		}
		if (typeof obj.anrede !== "undefined") {
			result += '"anrede" : ' + ((!obj.anrede) ? 'null' : '"' + obj.anrede + '"') + ',';
		}
		if (typeof obj.titel !== "undefined") {
			result += '"titel" : ' + ((!obj.titel) ? 'null' : '"' + obj.titel + '"') + ',';
		}
		if (typeof obj.amtsbezeichnung !== "undefined") {
			result += '"amtsbezeichnung" : ' + ((!obj.amtsbezeichnung) ? 'null' : '"' + obj.amtsbezeichnung + '"') + ',';
		}
		if (typeof obj.nachname !== "undefined") {
			result += '"nachname" : ' + '"' + obj.nachname + '"' + ',';
		}
		if (typeof obj.vorname !== "undefined") {
			result += '"vorname" : ' + '"' + obj.vorname + '"' + ',';
		}
		if (typeof obj.geschlecht !== "undefined") {
			result += '"geschlecht" : ' + obj.geschlecht + ',';
		}
		if (typeof obj.geburtsdatum !== "undefined") {
			result += '"geburtsdatum" : ' + ((!obj.geburtsdatum) ? 'null' : '"' + obj.geburtsdatum + '"') + ',';
		}
		if (typeof obj.staatsangehoerigkeitID !== "undefined") {
			result += '"staatsangehoerigkeitID" : ' + ((!obj.staatsangehoerigkeitID) ? 'null' : '"' + obj.staatsangehoerigkeitID + '"') + ',';
		}
		if (typeof obj.strassenname !== "undefined") {
			result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : '"' + obj.strassenname + '"') + ',';
		}
		if (typeof obj.hausnummer !== "undefined") {
			result += '"hausnummer" : ' + ((!obj.hausnummer) ? 'null' : '"' + obj.hausnummer + '"') + ',';
		}
		if (typeof obj.hausnummerZusatz !== "undefined") {
			result += '"hausnummerZusatz" : ' + ((!obj.hausnummerZusatz) ? 'null' : '"' + obj.hausnummerZusatz + '"') + ',';
		}
		if (typeof obj.wohnortID !== "undefined") {
			result += '"wohnortID" : ' + ((!obj.wohnortID) ? 'null' : obj.wohnortID) + ',';
		}
		if (typeof obj.ortsteilID !== "undefined") {
			result += '"ortsteilID" : ' + ((!obj.ortsteilID) ? 'null' : obj.ortsteilID) + ',';
		}
		if (typeof obj.telefon !== "undefined") {
			result += '"telefon" : ' + ((!obj.telefon) ? 'null' : '"' + obj.telefon + '"') + ',';
		}
		if (typeof obj.telefonMobil !== "undefined") {
			result += '"telefonMobil" : ' + ((!obj.telefonMobil) ? 'null' : '"' + obj.telefonMobil + '"') + ',';
		}
		if (typeof obj.emailPrivat !== "undefined") {
			result += '"emailPrivat" : ' + ((!obj.emailPrivat) ? 'null' : '"' + obj.emailPrivat + '"') + ',';
		}
		if (typeof obj.emailDienstlich !== "undefined") {
			result += '"emailDienstlich" : ' + ((!obj.emailDienstlich) ? 'null' : '"' + obj.emailDienstlich + '"') + ',';
		}
		if (typeof obj.foto !== "undefined") {
			result += '"foto" : ' + ((!obj.foto) ? 'null' : '"' + obj.foto + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_lehrer_LehrerStammdaten(obj : unknown) : LehrerStammdaten {
	return obj as LehrerStammdaten;
}
