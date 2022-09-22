import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { JavaBoolean, cast_java_lang_Boolean } from '../../../java/lang/JavaBoolean';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { BetriebAnsprechpartner, cast_de_nrw_schule_svws_core_data_betrieb_BetriebAnsprechpartner } from '../../../core/data/betrieb/BetriebAnsprechpartner';

export class BetriebStammdaten extends JavaObject {

	public id : Number | null = null;

	public adressArt : Number | null = null;

	public name1 : String | null = null;

	public name2 : String | null = null;

	public strassenname : String | null = null;

	public hausnr : String | null = null;

	public hausnrzusatz : String | null = null;

	public ort_id : Number | null = null;

	public plz : String | null = null;

	public telefon1 : String | null = null;

	public telefon2 : String | null = null;

	public fax : String | null = null;

	public email : String | null = null;

	public bemerkungen : String | null = null;

	public sortierung : Number | null = null;

	public ausbildungsbetrieb : Boolean | null = null;

	public bietetPraktika : Boolean | null = null;

	public branche : String | null = null;

	public zusatz1 : String | null = null;

	public zusatz2 : String | null = null;

	public Sichtbar : Boolean | null = null;

	public Aenderbar : Boolean | null = null;

	public Massnahmentraeger : Boolean | null = null;

	public BelehrungISG : Boolean | null = null;

	public GU_ID : String | null = null;

	public ErwFuehrungszeugnis : Boolean | null = null;

	public ExtID : String | null = null;

	public ansprechpartner : Vector<BetriebAnsprechpartner> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.betrieb.BetriebStammdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): BetriebStammdaten {
		const obj = JSON.parse(json);
		const result = new BetriebStammdaten();
		result.id = typeof obj.id === "undefined" ? null : obj.id;
		result.adressArt = typeof obj.adressArt === "undefined" ? null : obj.adressArt;
		result.name1 = typeof obj.name1 === "undefined" ? null : obj.name1;
		result.name2 = typeof obj.name2 === "undefined" ? null : obj.name2;
		result.strassenname = typeof obj.strassenname === "undefined" ? null : obj.strassenname;
		result.hausnr = typeof obj.hausnr === "undefined" ? null : obj.hausnr;
		result.hausnrzusatz = typeof obj.hausnrzusatz === "undefined" ? null : obj.hausnrzusatz;
		result.ort_id = typeof obj.ort_id === "undefined" ? null : obj.ort_id;
		result.plz = typeof obj.plz === "undefined" ? null : obj.plz;
		result.telefon1 = typeof obj.telefon1 === "undefined" ? null : obj.telefon1;
		result.telefon2 = typeof obj.telefon2 === "undefined" ? null : obj.telefon2;
		result.fax = typeof obj.fax === "undefined" ? null : obj.fax;
		result.email = typeof obj.email === "undefined" ? null : obj.email;
		result.bemerkungen = typeof obj.bemerkungen === "undefined" ? null : obj.bemerkungen;
		result.sortierung = typeof obj.sortierung === "undefined" ? null : obj.sortierung;
		result.ausbildungsbetrieb = typeof obj.ausbildungsbetrieb === "undefined" ? null : obj.ausbildungsbetrieb;
		result.bietetPraktika = typeof obj.bietetPraktika === "undefined" ? null : obj.bietetPraktika;
		result.branche = typeof obj.branche === "undefined" ? null : obj.branche;
		result.zusatz1 = typeof obj.zusatz1 === "undefined" ? null : obj.zusatz1;
		result.zusatz2 = typeof obj.zusatz2 === "undefined" ? null : obj.zusatz2;
		result.Sichtbar = typeof obj.Sichtbar === "undefined" ? null : obj.Sichtbar;
		result.Aenderbar = typeof obj.Aenderbar === "undefined" ? null : obj.Aenderbar;
		result.Massnahmentraeger = typeof obj.Massnahmentraeger === "undefined" ? null : obj.Massnahmentraeger;
		result.BelehrungISG = typeof obj.BelehrungISG === "undefined" ? null : obj.BelehrungISG;
		result.GU_ID = typeof obj.GU_ID === "undefined" ? null : obj.GU_ID;
		result.ErwFuehrungszeugnis = typeof obj.ErwFuehrungszeugnis === "undefined" ? null : obj.ErwFuehrungszeugnis;
		result.ExtID = typeof obj.ExtID === "undefined" ? null : obj.ExtID;
		if (!!obj.ansprechpartner) {
			for (let elem of obj.ansprechpartner) {
				result.ansprechpartner?.add(BetriebAnsprechpartner.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : BetriebStammdaten) : string {
		let result = '{';
		result += '"id" : ' + ((!obj.id) ? 'null' : obj.id.valueOf()) + ',';
		result += '"adressArt" : ' + ((!obj.adressArt) ? 'null' : obj.adressArt.valueOf()) + ',';
		result += '"name1" : ' + ((!obj.name1) ? 'null' : '"' + obj.name1.valueOf() + '"') + ',';
		result += '"name2" : ' + ((!obj.name2) ? 'null' : '"' + obj.name2.valueOf() + '"') + ',';
		result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : '"' + obj.strassenname.valueOf() + '"') + ',';
		result += '"hausnr" : ' + ((!obj.hausnr) ? 'null' : '"' + obj.hausnr.valueOf() + '"') + ',';
		result += '"hausnrzusatz" : ' + ((!obj.hausnrzusatz) ? 'null' : '"' + obj.hausnrzusatz.valueOf() + '"') + ',';
		result += '"ort_id" : ' + ((!obj.ort_id) ? 'null' : obj.ort_id.valueOf()) + ',';
		result += '"plz" : ' + ((!obj.plz) ? 'null' : '"' + obj.plz.valueOf() + '"') + ',';
		result += '"telefon1" : ' + ((!obj.telefon1) ? 'null' : '"' + obj.telefon1.valueOf() + '"') + ',';
		result += '"telefon2" : ' + ((!obj.telefon2) ? 'null' : '"' + obj.telefon2.valueOf() + '"') + ',';
		result += '"fax" : ' + ((!obj.fax) ? 'null' : '"' + obj.fax.valueOf() + '"') + ',';
		result += '"email" : ' + ((!obj.email) ? 'null' : '"' + obj.email.valueOf() + '"') + ',';
		result += '"bemerkungen" : ' + ((!obj.bemerkungen) ? 'null' : '"' + obj.bemerkungen.valueOf() + '"') + ',';
		result += '"sortierung" : ' + ((!obj.sortierung) ? 'null' : obj.sortierung.valueOf()) + ',';
		result += '"ausbildungsbetrieb" : ' + ((!obj.ausbildungsbetrieb) ? 'null' : obj.ausbildungsbetrieb.valueOf()) + ',';
		result += '"bietetPraktika" : ' + ((!obj.bietetPraktika) ? 'null' : obj.bietetPraktika.valueOf()) + ',';
		result += '"branche" : ' + ((!obj.branche) ? 'null' : '"' + obj.branche.valueOf() + '"') + ',';
		result += '"zusatz1" : ' + ((!obj.zusatz1) ? 'null' : '"' + obj.zusatz1.valueOf() + '"') + ',';
		result += '"zusatz2" : ' + ((!obj.zusatz2) ? 'null' : '"' + obj.zusatz2.valueOf() + '"') + ',';
		result += '"Sichtbar" : ' + ((!obj.Sichtbar) ? 'null' : obj.Sichtbar.valueOf()) + ',';
		result += '"Aenderbar" : ' + ((!obj.Aenderbar) ? 'null' : obj.Aenderbar.valueOf()) + ',';
		result += '"Massnahmentraeger" : ' + ((!obj.Massnahmentraeger) ? 'null' : obj.Massnahmentraeger.valueOf()) + ',';
		result += '"BelehrungISG" : ' + ((!obj.BelehrungISG) ? 'null' : obj.BelehrungISG.valueOf()) + ',';
		result += '"GU_ID" : ' + ((!obj.GU_ID) ? 'null' : '"' + obj.GU_ID.valueOf() + '"') + ',';
		result += '"ErwFuehrungszeugnis" : ' + ((!obj.ErwFuehrungszeugnis) ? 'null' : obj.ErwFuehrungszeugnis.valueOf()) + ',';
		result += '"ExtID" : ' + ((!obj.ExtID) ? 'null' : '"' + obj.ExtID.valueOf() + '"') + ',';
		if (!obj.ansprechpartner) {
			result += '"ansprechpartner" : []';
		} else {
			result += '"ansprechpartner" : [ ';
			for (let i : number = 0; i < obj.ansprechpartner.size(); i++) {
				let elem = obj.ansprechpartner.get(i);
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
			result += '"id" : ' + ((!obj.id) ? 'null' : obj.id.valueOf()) + ',';
		}
		if (typeof obj.adressArt !== "undefined") {
			result += '"adressArt" : ' + ((!obj.adressArt) ? 'null' : obj.adressArt.valueOf()) + ',';
		}
		if (typeof obj.name1 !== "undefined") {
			result += '"name1" : ' + ((!obj.name1) ? 'null' : '"' + obj.name1.valueOf() + '"') + ',';
		}
		if (typeof obj.name2 !== "undefined") {
			result += '"name2" : ' + ((!obj.name2) ? 'null' : '"' + obj.name2.valueOf() + '"') + ',';
		}
		if (typeof obj.strassenname !== "undefined") {
			result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : '"' + obj.strassenname.valueOf() + '"') + ',';
		}
		if (typeof obj.hausnr !== "undefined") {
			result += '"hausnr" : ' + ((!obj.hausnr) ? 'null' : '"' + obj.hausnr.valueOf() + '"') + ',';
		}
		if (typeof obj.hausnrzusatz !== "undefined") {
			result += '"hausnrzusatz" : ' + ((!obj.hausnrzusatz) ? 'null' : '"' + obj.hausnrzusatz.valueOf() + '"') + ',';
		}
		if (typeof obj.ort_id !== "undefined") {
			result += '"ort_id" : ' + ((!obj.ort_id) ? 'null' : obj.ort_id.valueOf()) + ',';
		}
		if (typeof obj.plz !== "undefined") {
			result += '"plz" : ' + ((!obj.plz) ? 'null' : '"' + obj.plz.valueOf() + '"') + ',';
		}
		if (typeof obj.telefon1 !== "undefined") {
			result += '"telefon1" : ' + ((!obj.telefon1) ? 'null' : '"' + obj.telefon1.valueOf() + '"') + ',';
		}
		if (typeof obj.telefon2 !== "undefined") {
			result += '"telefon2" : ' + ((!obj.telefon2) ? 'null' : '"' + obj.telefon2.valueOf() + '"') + ',';
		}
		if (typeof obj.fax !== "undefined") {
			result += '"fax" : ' + ((!obj.fax) ? 'null' : '"' + obj.fax.valueOf() + '"') + ',';
		}
		if (typeof obj.email !== "undefined") {
			result += '"email" : ' + ((!obj.email) ? 'null' : '"' + obj.email.valueOf() + '"') + ',';
		}
		if (typeof obj.bemerkungen !== "undefined") {
			result += '"bemerkungen" : ' + ((!obj.bemerkungen) ? 'null' : '"' + obj.bemerkungen.valueOf() + '"') + ',';
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + ((!obj.sortierung) ? 'null' : obj.sortierung.valueOf()) + ',';
		}
		if (typeof obj.ausbildungsbetrieb !== "undefined") {
			result += '"ausbildungsbetrieb" : ' + ((!obj.ausbildungsbetrieb) ? 'null' : obj.ausbildungsbetrieb.valueOf()) + ',';
		}
		if (typeof obj.bietetPraktika !== "undefined") {
			result += '"bietetPraktika" : ' + ((!obj.bietetPraktika) ? 'null' : obj.bietetPraktika.valueOf()) + ',';
		}
		if (typeof obj.branche !== "undefined") {
			result += '"branche" : ' + ((!obj.branche) ? 'null' : '"' + obj.branche.valueOf() + '"') + ',';
		}
		if (typeof obj.zusatz1 !== "undefined") {
			result += '"zusatz1" : ' + ((!obj.zusatz1) ? 'null' : '"' + obj.zusatz1.valueOf() + '"') + ',';
		}
		if (typeof obj.zusatz2 !== "undefined") {
			result += '"zusatz2" : ' + ((!obj.zusatz2) ? 'null' : '"' + obj.zusatz2.valueOf() + '"') + ',';
		}
		if (typeof obj.Sichtbar !== "undefined") {
			result += '"Sichtbar" : ' + ((!obj.Sichtbar) ? 'null' : obj.Sichtbar.valueOf()) + ',';
		}
		if (typeof obj.Aenderbar !== "undefined") {
			result += '"Aenderbar" : ' + ((!obj.Aenderbar) ? 'null' : obj.Aenderbar.valueOf()) + ',';
		}
		if (typeof obj.Massnahmentraeger !== "undefined") {
			result += '"Massnahmentraeger" : ' + ((!obj.Massnahmentraeger) ? 'null' : obj.Massnahmentraeger.valueOf()) + ',';
		}
		if (typeof obj.BelehrungISG !== "undefined") {
			result += '"BelehrungISG" : ' + ((!obj.BelehrungISG) ? 'null' : obj.BelehrungISG.valueOf()) + ',';
		}
		if (typeof obj.GU_ID !== "undefined") {
			result += '"GU_ID" : ' + ((!obj.GU_ID) ? 'null' : '"' + obj.GU_ID.valueOf() + '"') + ',';
		}
		if (typeof obj.ErwFuehrungszeugnis !== "undefined") {
			result += '"ErwFuehrungszeugnis" : ' + ((!obj.ErwFuehrungszeugnis) ? 'null' : obj.ErwFuehrungszeugnis.valueOf()) + ',';
		}
		if (typeof obj.ExtID !== "undefined") {
			result += '"ExtID" : ' + ((!obj.ExtID) ? 'null' : '"' + obj.ExtID.valueOf() + '"') + ',';
		}
		if (typeof obj.ansprechpartner !== "undefined") {
			if (!obj.ansprechpartner) {
				result += '"ansprechpartner" : []';
			} else {
				result += '"ansprechpartner" : [ ';
				for (let i : number = 0; i < obj.ansprechpartner.size(); i++) {
					let elem = obj.ansprechpartner.get(i);
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

export function cast_de_nrw_schule_svws_core_data_betrieb_BetriebStammdaten(obj : unknown) : BetriebStammdaten {
	return obj as BetriebStammdaten;
}
