import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { BetriebAnsprechpartner } from '../../../core/data/betrieb/BetriebAnsprechpartner';

export class BetriebStammdaten extends JavaObject {

	/**
	 * ID der weiteren Adresse (Betriebe)
	 */
	public id : number = -1;

	/**
	 * Adressart des Betriebs, Fremdschlüssel auf die ID in K_Adressart
	 */
	public adressArt : number | null = null;

	/**
	 * Name1 des Betriebs
	 */
	public name1 : string = "";

	/**
	 * Name2 des Betriebs
	 */
	public name2 : string = "";

	/**
	 * Straßenname des Betriebsdatensatz
	 */
	public strassenname : string = "";

	/**
	 * Hausnummer wenn getrennt gespeichert
	 */
	public hausnr : string = "";

	/**
	 * Zusatz zur Hausnummer wenn Hausnummern getrennt gespeichert werden
	 */
	public hausnrzusatz : string = "";

	/**
	 * OrtID des Betriebs
	 */
	public ort_id : number | null = null;

	/**
	 * Erste Telefonnummer des Betriebs
	 */
	public telefon1 : string = "";

	/**
	 * Zweite Telefonnummer des Betriebs
	 */
	public telefon2 : string = "";

	/**
	 * Faxnummer des Betriebs
	 */
	public fax : string = "";

	/**
	 * E-MailAdresse des Betriebes
	 */
	public email : string = "";

	/**
	 * Bemerkung zum Betrieb
	 */
	public bemerkungen : string = "";

	/**
	 * Sortierung des Betriebsdatensatz
	 */
	public sortierung : number = 32000;

	/**
	 * Gibt an, ob der Betrieb ausbildet
	 */
	public ausbildungsbetrieb : boolean = false;

	/**
	 * Gibt an, ob der Betrieb Praktikumsplätze bietet
	 */
	public bietetPraktika : boolean = false;

	/**
	 * Branche des Betriebs
	 */
	public branche : string = "";

	/**
	 * Adresszusatz zum Betrieb
	 */
	public zusatz1 : string = "";

	/**
	 * Adresszusatz2 zum Betrieb
	 */
	public zusatz2 : string = "";

	/**
	 * Sichtbarkeit des Datensatzes
	 */
	public Sichtbar : boolean = true;

	/**
	 * Datensatz ist änderbar Ja Nein
	 */
	public Aenderbar : boolean = true;

	/**
	 * Bezeichnung des Maßnahmenträgers
	 */
	public Massnahmentraeger : boolean = false;

	/**
	 * Belehrung nach Infektionsschutzgesetz notwendig Ja Nein
	 */
	public BelehrungISG : boolean = false;

	/**
	 * GU_ID des Betriebsdatensatzes (für Import zur Erkennung)
	 */
	public GU_ID : string = "";

	/**
	 * Wird für diesen Betrieb ein Erweitertes Führungszeugnis benötigt?
	 */
	public ErwFuehrungszeugnis : boolean = false;

	/**
	 * Externe ID des Betriebsdatensatzes
	 */
	public ExtID : string | null = null;

	/**
	 * Ein Array mit den Ansprechpartnern im Betrieb.
	 */
	public ansprechpartner : List<BetriebAnsprechpartner> = new ArrayList();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.betrieb.BetriebStammdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): BetriebStammdaten {
		const obj = JSON.parse(json);
		const result = new BetriebStammdaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.adressArt = typeof obj.adressArt === "undefined" ? null : obj.adressArt === null ? null : obj.adressArt;
		if (typeof obj.name1 === "undefined")
			 throw new Error('invalid json format, missing attribute name1');
		result.name1 = obj.name1;
		if (typeof obj.name2 === "undefined")
			 throw new Error('invalid json format, missing attribute name2');
		result.name2 = obj.name2;
		if (typeof obj.strassenname === "undefined")
			 throw new Error('invalid json format, missing attribute strassenname');
		result.strassenname = obj.strassenname;
		if (typeof obj.hausnr === "undefined")
			 throw new Error('invalid json format, missing attribute hausnr');
		result.hausnr = obj.hausnr;
		if (typeof obj.hausnrzusatz === "undefined")
			 throw new Error('invalid json format, missing attribute hausnrzusatz');
		result.hausnrzusatz = obj.hausnrzusatz;
		result.ort_id = typeof obj.ort_id === "undefined" ? null : obj.ort_id === null ? null : obj.ort_id;
		if (typeof obj.telefon1 === "undefined")
			 throw new Error('invalid json format, missing attribute telefon1');
		result.telefon1 = obj.telefon1;
		if (typeof obj.telefon2 === "undefined")
			 throw new Error('invalid json format, missing attribute telefon2');
		result.telefon2 = obj.telefon2;
		if (typeof obj.fax === "undefined")
			 throw new Error('invalid json format, missing attribute fax');
		result.fax = obj.fax;
		if (typeof obj.email === "undefined")
			 throw new Error('invalid json format, missing attribute email');
		result.email = obj.email;
		if (typeof obj.bemerkungen === "undefined")
			 throw new Error('invalid json format, missing attribute bemerkungen');
		result.bemerkungen = obj.bemerkungen;
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (typeof obj.ausbildungsbetrieb === "undefined")
			 throw new Error('invalid json format, missing attribute ausbildungsbetrieb');
		result.ausbildungsbetrieb = obj.ausbildungsbetrieb;
		if (typeof obj.bietetPraktika === "undefined")
			 throw new Error('invalid json format, missing attribute bietetPraktika');
		result.bietetPraktika = obj.bietetPraktika;
		if (typeof obj.branche === "undefined")
			 throw new Error('invalid json format, missing attribute branche');
		result.branche = obj.branche;
		if (typeof obj.zusatz1 === "undefined")
			 throw new Error('invalid json format, missing attribute zusatz1');
		result.zusatz1 = obj.zusatz1;
		if (typeof obj.zusatz2 === "undefined")
			 throw new Error('invalid json format, missing attribute zusatz2');
		result.zusatz2 = obj.zusatz2;
		if (typeof obj.Sichtbar === "undefined")
			 throw new Error('invalid json format, missing attribute Sichtbar');
		result.Sichtbar = obj.Sichtbar;
		if (typeof obj.Aenderbar === "undefined")
			 throw new Error('invalid json format, missing attribute Aenderbar');
		result.Aenderbar = obj.Aenderbar;
		if (typeof obj.Massnahmentraeger === "undefined")
			 throw new Error('invalid json format, missing attribute Massnahmentraeger');
		result.Massnahmentraeger = obj.Massnahmentraeger;
		if (typeof obj.BelehrungISG === "undefined")
			 throw new Error('invalid json format, missing attribute BelehrungISG');
		result.BelehrungISG = obj.BelehrungISG;
		if (typeof obj.GU_ID === "undefined")
			 throw new Error('invalid json format, missing attribute GU_ID');
		result.GU_ID = obj.GU_ID;
		if (typeof obj.ErwFuehrungszeugnis === "undefined")
			 throw new Error('invalid json format, missing attribute ErwFuehrungszeugnis');
		result.ErwFuehrungszeugnis = obj.ErwFuehrungszeugnis;
		result.ExtID = typeof obj.ExtID === "undefined" ? null : obj.ExtID === null ? null : obj.ExtID;
		if ((obj.ansprechpartner !== undefined) && (obj.ansprechpartner !== null)) {
			for (const elem of obj.ansprechpartner) {
				result.ansprechpartner?.add(BetriebAnsprechpartner.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : BetriebStammdaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"adressArt" : ' + ((!obj.adressArt) ? 'null' : obj.adressArt) + ',';
		result += '"name1" : ' + JSON.stringify(obj.name1!) + ',';
		result += '"name2" : ' + JSON.stringify(obj.name2!) + ',';
		result += '"strassenname" : ' + JSON.stringify(obj.strassenname!) + ',';
		result += '"hausnr" : ' + JSON.stringify(obj.hausnr!) + ',';
		result += '"hausnrzusatz" : ' + JSON.stringify(obj.hausnrzusatz!) + ',';
		result += '"ort_id" : ' + ((!obj.ort_id) ? 'null' : obj.ort_id) + ',';
		result += '"telefon1" : ' + JSON.stringify(obj.telefon1!) + ',';
		result += '"telefon2" : ' + JSON.stringify(obj.telefon2!) + ',';
		result += '"fax" : ' + JSON.stringify(obj.fax!) + ',';
		result += '"email" : ' + JSON.stringify(obj.email!) + ',';
		result += '"bemerkungen" : ' + JSON.stringify(obj.bemerkungen!) + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		result += '"ausbildungsbetrieb" : ' + obj.ausbildungsbetrieb + ',';
		result += '"bietetPraktika" : ' + obj.bietetPraktika + ',';
		result += '"branche" : ' + JSON.stringify(obj.branche!) + ',';
		result += '"zusatz1" : ' + JSON.stringify(obj.zusatz1!) + ',';
		result += '"zusatz2" : ' + JSON.stringify(obj.zusatz2!) + ',';
		result += '"Sichtbar" : ' + obj.Sichtbar + ',';
		result += '"Aenderbar" : ' + obj.Aenderbar + ',';
		result += '"Massnahmentraeger" : ' + obj.Massnahmentraeger + ',';
		result += '"BelehrungISG" : ' + obj.BelehrungISG + ',';
		result += '"GU_ID" : ' + JSON.stringify(obj.GU_ID!) + ',';
		result += '"ErwFuehrungszeugnis" : ' + obj.ErwFuehrungszeugnis + ',';
		result += '"ExtID" : ' + ((!obj.ExtID) ? 'null' : JSON.stringify(obj.ExtID)) + ',';
		if (!obj.ansprechpartner) {
			result += '"ansprechpartner" : []';
		} else {
			result += '"ansprechpartner" : [ ';
			for (let i = 0; i < obj.ansprechpartner.size(); i++) {
				const elem = obj.ansprechpartner.get(i);
				result += BetriebAnsprechpartner.transpilerToJSON(elem);
				if (i < obj.ansprechpartner.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BetriebStammdaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.adressArt !== "undefined") {
			result += '"adressArt" : ' + ((!obj.adressArt) ? 'null' : obj.adressArt) + ',';
		}
		if (typeof obj.name1 !== "undefined") {
			result += '"name1" : ' + JSON.stringify(obj.name1!) + ',';
		}
		if (typeof obj.name2 !== "undefined") {
			result += '"name2" : ' + JSON.stringify(obj.name2!) + ',';
		}
		if (typeof obj.strassenname !== "undefined") {
			result += '"strassenname" : ' + JSON.stringify(obj.strassenname!) + ',';
		}
		if (typeof obj.hausnr !== "undefined") {
			result += '"hausnr" : ' + JSON.stringify(obj.hausnr!) + ',';
		}
		if (typeof obj.hausnrzusatz !== "undefined") {
			result += '"hausnrzusatz" : ' + JSON.stringify(obj.hausnrzusatz!) + ',';
		}
		if (typeof obj.ort_id !== "undefined") {
			result += '"ort_id" : ' + ((!obj.ort_id) ? 'null' : obj.ort_id) + ',';
		}
		if (typeof obj.telefon1 !== "undefined") {
			result += '"telefon1" : ' + JSON.stringify(obj.telefon1!) + ',';
		}
		if (typeof obj.telefon2 !== "undefined") {
			result += '"telefon2" : ' + JSON.stringify(obj.telefon2!) + ',';
		}
		if (typeof obj.fax !== "undefined") {
			result += '"fax" : ' + JSON.stringify(obj.fax!) + ',';
		}
		if (typeof obj.email !== "undefined") {
			result += '"email" : ' + JSON.stringify(obj.email!) + ',';
		}
		if (typeof obj.bemerkungen !== "undefined") {
			result += '"bemerkungen" : ' + JSON.stringify(obj.bemerkungen!) + ',';
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + obj.sortierung + ',';
		}
		if (typeof obj.ausbildungsbetrieb !== "undefined") {
			result += '"ausbildungsbetrieb" : ' + obj.ausbildungsbetrieb + ',';
		}
		if (typeof obj.bietetPraktika !== "undefined") {
			result += '"bietetPraktika" : ' + obj.bietetPraktika + ',';
		}
		if (typeof obj.branche !== "undefined") {
			result += '"branche" : ' + JSON.stringify(obj.branche!) + ',';
		}
		if (typeof obj.zusatz1 !== "undefined") {
			result += '"zusatz1" : ' + JSON.stringify(obj.zusatz1!) + ',';
		}
		if (typeof obj.zusatz2 !== "undefined") {
			result += '"zusatz2" : ' + JSON.stringify(obj.zusatz2!) + ',';
		}
		if (typeof obj.Sichtbar !== "undefined") {
			result += '"Sichtbar" : ' + obj.Sichtbar + ',';
		}
		if (typeof obj.Aenderbar !== "undefined") {
			result += '"Aenderbar" : ' + obj.Aenderbar + ',';
		}
		if (typeof obj.Massnahmentraeger !== "undefined") {
			result += '"Massnahmentraeger" : ' + obj.Massnahmentraeger + ',';
		}
		if (typeof obj.BelehrungISG !== "undefined") {
			result += '"BelehrungISG" : ' + obj.BelehrungISG + ',';
		}
		if (typeof obj.GU_ID !== "undefined") {
			result += '"GU_ID" : ' + JSON.stringify(obj.GU_ID!) + ',';
		}
		if (typeof obj.ErwFuehrungszeugnis !== "undefined") {
			result += '"ErwFuehrungszeugnis" : ' + obj.ErwFuehrungszeugnis + ',';
		}
		if (typeof obj.ExtID !== "undefined") {
			result += '"ExtID" : ' + ((!obj.ExtID) ? 'null' : JSON.stringify(obj.ExtID)) + ',';
		}
		if (typeof obj.ansprechpartner !== "undefined") {
			if (!obj.ansprechpartner) {
				result += '"ansprechpartner" : []';
			} else {
				result += '"ansprechpartner" : [ ';
				for (let i = 0; i < obj.ansprechpartner.size(); i++) {
					const elem = obj.ansprechpartner.get(i);
					result += BetriebAnsprechpartner.transpilerToJSON(elem);
					if (i < obj.ansprechpartner.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_betrieb_BetriebStammdaten(obj : unknown) : BetriebStammdaten {
	return obj as BetriebStammdaten;
}
