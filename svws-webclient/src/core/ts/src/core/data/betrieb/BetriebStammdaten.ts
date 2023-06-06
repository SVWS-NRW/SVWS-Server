import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { BetriebAnsprechpartner } from '../../../core/data/betrieb/BetriebAnsprechpartner';

export class BetriebStammdaten extends JavaObject {

	/**
	 * ID der weiteren Adresse (Betriebe)
	 */
	public id : number = 0;

	/**
	 * Adressart des Betriebs, Fremdschlüssel auf die ID in K_Adressart
	 */
	public adressArt : number | null = null;

	/**
	 * Name1 des Betriebs
	 */
	public name1 : string | null = null;

	/**
	 * Name2 des Betriebs
	 */
	public name2 : string | null = null;

	/**
	 * Straßenname des Betriebsdatensatz
	 */
	public strassenname : string | null = null;

	/**
	 * Hausnummer wenn getrennt gespeichert
	 */
	public hausnr : string | null = null;

	/**
	 * Zusatz zur Hausnummer wenn Hausnummern getrennt gespeichert werden
	 */
	public hausnrzusatz : string | null = null;

	/**
	 * OrtID des Betriebs
	 */
	public ort_id : number | null = null;

	/**
	 * PLZ des Betriebs
	 */
	public plz : string | null = null;

	/**
	 * Telefonnummer1 des Betriebs
	 */
	public telefon1 : string | null = null;

	/**
	 * Telefonnummer2 des Betriebs
	 */
	public telefon2 : string | null = null;

	/**
	 * Faxnummer des Betriebs
	 */
	public fax : string | null = null;

	/**
	 * E-MailAdresse des Betriebes
	 */
	public email : string | null = null;

	/**
	 * Bemerkung zum Betrieb
	 */
	public bemerkungen : string | null = null;

	/**
	 * Sortierung des Betriebsdatensatz
	 */
	public sortierung : number | null = null;

	/**
	 * Gibt an ob der Betrieb ausbildet Ja Nein
	 */
	public ausbildungsbetrieb : boolean | null = null;

	/**
	 * Gibt an ob der Betrieb Praktikumsplätze bietet Ja Nein
	 */
	public bietetPraktika : boolean | null = null;

	/**
	 * Brache des Betriebs
	 */
	public branche : string | null = null;

	/**
	 * Adresszusatz zum Betrieb
	 */
	public zusatz1 : string | null = null;

	/**
	 * Adresszusatz2 zum Betrieb
	 */
	public zusatz2 : string | null = null;

	/**
	 * Sichtbarkeit des Datensatzes
	 */
	public Sichtbar : boolean | null = null;

	/**
	 * Datensatz ist änderbar Ja Nein
	 */
	public Aenderbar : boolean | null = null;

	/**
	 * Bezeichnung des Maßnahmenträgers
	 */
	public Massnahmentraeger : boolean | null = null;

	/**
	 * Belehrung nach Infektionsschutzgesetz notwendig Ja Nein
	 */
	public BelehrungISG : boolean | null = null;

	/**
	 * GU_ID des Betriebsdatensatzes (für Import zur Erkennung)
	 */
	public GU_ID : string | null = null;

	/**
	 * Wird für diesen Betrieb ein Erweitertes Führungszeugnis benötigt?
	 */
	public ErwFuehrungszeugnis : boolean | null = null;

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
		result.name1 = typeof obj.name1 === "undefined" ? null : obj.name1 === null ? null : obj.name1;
		result.name2 = typeof obj.name2 === "undefined" ? null : obj.name2 === null ? null : obj.name2;
		result.strassenname = typeof obj.strassenname === "undefined" ? null : obj.strassenname === null ? null : obj.strassenname;
		result.hausnr = typeof obj.hausnr === "undefined" ? null : obj.hausnr === null ? null : obj.hausnr;
		result.hausnrzusatz = typeof obj.hausnrzusatz === "undefined" ? null : obj.hausnrzusatz === null ? null : obj.hausnrzusatz;
		result.ort_id = typeof obj.ort_id === "undefined" ? null : obj.ort_id === null ? null : obj.ort_id;
		result.plz = typeof obj.plz === "undefined" ? null : obj.plz === null ? null : obj.plz;
		result.telefon1 = typeof obj.telefon1 === "undefined" ? null : obj.telefon1 === null ? null : obj.telefon1;
		result.telefon2 = typeof obj.telefon2 === "undefined" ? null : obj.telefon2 === null ? null : obj.telefon2;
		result.fax = typeof obj.fax === "undefined" ? null : obj.fax === null ? null : obj.fax;
		result.email = typeof obj.email === "undefined" ? null : obj.email === null ? null : obj.email;
		result.bemerkungen = typeof obj.bemerkungen === "undefined" ? null : obj.bemerkungen === null ? null : obj.bemerkungen;
		result.sortierung = typeof obj.sortierung === "undefined" ? null : obj.sortierung === null ? null : obj.sortierung;
		result.ausbildungsbetrieb = typeof obj.ausbildungsbetrieb === "undefined" ? null : obj.ausbildungsbetrieb === null ? null : obj.ausbildungsbetrieb;
		result.bietetPraktika = typeof obj.bietetPraktika === "undefined" ? null : obj.bietetPraktika === null ? null : obj.bietetPraktika;
		result.branche = typeof obj.branche === "undefined" ? null : obj.branche === null ? null : obj.branche;
		result.zusatz1 = typeof obj.zusatz1 === "undefined" ? null : obj.zusatz1 === null ? null : obj.zusatz1;
		result.zusatz2 = typeof obj.zusatz2 === "undefined" ? null : obj.zusatz2 === null ? null : obj.zusatz2;
		result.Sichtbar = typeof obj.Sichtbar === "undefined" ? null : obj.Sichtbar === null ? null : obj.Sichtbar;
		result.Aenderbar = typeof obj.Aenderbar === "undefined" ? null : obj.Aenderbar === null ? null : obj.Aenderbar;
		result.Massnahmentraeger = typeof obj.Massnahmentraeger === "undefined" ? null : obj.Massnahmentraeger === null ? null : obj.Massnahmentraeger;
		result.BelehrungISG = typeof obj.BelehrungISG === "undefined" ? null : obj.BelehrungISG === null ? null : obj.BelehrungISG;
		result.GU_ID = typeof obj.GU_ID === "undefined" ? null : obj.GU_ID === null ? null : obj.GU_ID;
		result.ErwFuehrungszeugnis = typeof obj.ErwFuehrungszeugnis === "undefined" ? null : obj.ErwFuehrungszeugnis === null ? null : obj.ErwFuehrungszeugnis;
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
		result += '"name1" : ' + ((!obj.name1) ? 'null' : '"' + obj.name1 + '"') + ',';
		result += '"name2" : ' + ((!obj.name2) ? 'null' : '"' + obj.name2 + '"') + ',';
		result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : '"' + obj.strassenname + '"') + ',';
		result += '"hausnr" : ' + ((!obj.hausnr) ? 'null' : '"' + obj.hausnr + '"') + ',';
		result += '"hausnrzusatz" : ' + ((!obj.hausnrzusatz) ? 'null' : '"' + obj.hausnrzusatz + '"') + ',';
		result += '"ort_id" : ' + ((!obj.ort_id) ? 'null' : obj.ort_id) + ',';
		result += '"plz" : ' + ((!obj.plz) ? 'null' : '"' + obj.plz + '"') + ',';
		result += '"telefon1" : ' + ((!obj.telefon1) ? 'null' : '"' + obj.telefon1 + '"') + ',';
		result += '"telefon2" : ' + ((!obj.telefon2) ? 'null' : '"' + obj.telefon2 + '"') + ',';
		result += '"fax" : ' + ((!obj.fax) ? 'null' : '"' + obj.fax + '"') + ',';
		result += '"email" : ' + ((!obj.email) ? 'null' : '"' + obj.email + '"') + ',';
		result += '"bemerkungen" : ' + ((!obj.bemerkungen) ? 'null' : '"' + obj.bemerkungen + '"') + ',';
		result += '"sortierung" : ' + ((!obj.sortierung) ? 'null' : obj.sortierung) + ',';
		result += '"ausbildungsbetrieb" : ' + ((!obj.ausbildungsbetrieb) ? 'null' : obj.ausbildungsbetrieb) + ',';
		result += '"bietetPraktika" : ' + ((!obj.bietetPraktika) ? 'null' : obj.bietetPraktika) + ',';
		result += '"branche" : ' + ((!obj.branche) ? 'null' : '"' + obj.branche + '"') + ',';
		result += '"zusatz1" : ' + ((!obj.zusatz1) ? 'null' : '"' + obj.zusatz1 + '"') + ',';
		result += '"zusatz2" : ' + ((!obj.zusatz2) ? 'null' : '"' + obj.zusatz2 + '"') + ',';
		result += '"Sichtbar" : ' + ((!obj.Sichtbar) ? 'null' : obj.Sichtbar) + ',';
		result += '"Aenderbar" : ' + ((!obj.Aenderbar) ? 'null' : obj.Aenderbar) + ',';
		result += '"Massnahmentraeger" : ' + ((!obj.Massnahmentraeger) ? 'null' : obj.Massnahmentraeger) + ',';
		result += '"BelehrungISG" : ' + ((!obj.BelehrungISG) ? 'null' : obj.BelehrungISG) + ',';
		result += '"GU_ID" : ' + ((!obj.GU_ID) ? 'null' : '"' + obj.GU_ID + '"') + ',';
		result += '"ErwFuehrungszeugnis" : ' + ((!obj.ErwFuehrungszeugnis) ? 'null' : obj.ErwFuehrungszeugnis) + ',';
		result += '"ExtID" : ' + ((!obj.ExtID) ? 'null' : '"' + obj.ExtID + '"') + ',';
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
			result += '"name1" : ' + ((!obj.name1) ? 'null' : '"' + obj.name1 + '"') + ',';
		}
		if (typeof obj.name2 !== "undefined") {
			result += '"name2" : ' + ((!obj.name2) ? 'null' : '"' + obj.name2 + '"') + ',';
		}
		if (typeof obj.strassenname !== "undefined") {
			result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : '"' + obj.strassenname + '"') + ',';
		}
		if (typeof obj.hausnr !== "undefined") {
			result += '"hausnr" : ' + ((!obj.hausnr) ? 'null' : '"' + obj.hausnr + '"') + ',';
		}
		if (typeof obj.hausnrzusatz !== "undefined") {
			result += '"hausnrzusatz" : ' + ((!obj.hausnrzusatz) ? 'null' : '"' + obj.hausnrzusatz + '"') + ',';
		}
		if (typeof obj.ort_id !== "undefined") {
			result += '"ort_id" : ' + ((!obj.ort_id) ? 'null' : obj.ort_id) + ',';
		}
		if (typeof obj.plz !== "undefined") {
			result += '"plz" : ' + ((!obj.plz) ? 'null' : '"' + obj.plz + '"') + ',';
		}
		if (typeof obj.telefon1 !== "undefined") {
			result += '"telefon1" : ' + ((!obj.telefon1) ? 'null' : '"' + obj.telefon1 + '"') + ',';
		}
		if (typeof obj.telefon2 !== "undefined") {
			result += '"telefon2" : ' + ((!obj.telefon2) ? 'null' : '"' + obj.telefon2 + '"') + ',';
		}
		if (typeof obj.fax !== "undefined") {
			result += '"fax" : ' + ((!obj.fax) ? 'null' : '"' + obj.fax + '"') + ',';
		}
		if (typeof obj.email !== "undefined") {
			result += '"email" : ' + ((!obj.email) ? 'null' : '"' + obj.email + '"') + ',';
		}
		if (typeof obj.bemerkungen !== "undefined") {
			result += '"bemerkungen" : ' + ((!obj.bemerkungen) ? 'null' : '"' + obj.bemerkungen + '"') + ',';
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + ((!obj.sortierung) ? 'null' : obj.sortierung) + ',';
		}
		if (typeof obj.ausbildungsbetrieb !== "undefined") {
			result += '"ausbildungsbetrieb" : ' + ((!obj.ausbildungsbetrieb) ? 'null' : obj.ausbildungsbetrieb) + ',';
		}
		if (typeof obj.bietetPraktika !== "undefined") {
			result += '"bietetPraktika" : ' + ((!obj.bietetPraktika) ? 'null' : obj.bietetPraktika) + ',';
		}
		if (typeof obj.branche !== "undefined") {
			result += '"branche" : ' + ((!obj.branche) ? 'null' : '"' + obj.branche + '"') + ',';
		}
		if (typeof obj.zusatz1 !== "undefined") {
			result += '"zusatz1" : ' + ((!obj.zusatz1) ? 'null' : '"' + obj.zusatz1 + '"') + ',';
		}
		if (typeof obj.zusatz2 !== "undefined") {
			result += '"zusatz2" : ' + ((!obj.zusatz2) ? 'null' : '"' + obj.zusatz2 + '"') + ',';
		}
		if (typeof obj.Sichtbar !== "undefined") {
			result += '"Sichtbar" : ' + ((!obj.Sichtbar) ? 'null' : obj.Sichtbar) + ',';
		}
		if (typeof obj.Aenderbar !== "undefined") {
			result += '"Aenderbar" : ' + ((!obj.Aenderbar) ? 'null' : obj.Aenderbar) + ',';
		}
		if (typeof obj.Massnahmentraeger !== "undefined") {
			result += '"Massnahmentraeger" : ' + ((!obj.Massnahmentraeger) ? 'null' : obj.Massnahmentraeger) + ',';
		}
		if (typeof obj.BelehrungISG !== "undefined") {
			result += '"BelehrungISG" : ' + ((!obj.BelehrungISG) ? 'null' : obj.BelehrungISG) + ',';
		}
		if (typeof obj.GU_ID !== "undefined") {
			result += '"GU_ID" : ' + ((!obj.GU_ID) ? 'null' : '"' + obj.GU_ID + '"') + ',';
		}
		if (typeof obj.ErwFuehrungszeugnis !== "undefined") {
			result += '"ErwFuehrungszeugnis" : ' + ((!obj.ErwFuehrungszeugnis) ? 'null' : obj.ErwFuehrungszeugnis) + ',';
		}
		if (typeof obj.ExtID !== "undefined") {
			result += '"ExtID" : ' + ((!obj.ExtID) ? 'null' : '"' + obj.ExtID + '"') + ',';
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
