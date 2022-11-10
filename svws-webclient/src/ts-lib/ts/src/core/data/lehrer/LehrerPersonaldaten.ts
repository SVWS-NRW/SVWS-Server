import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { JavaBoolean, cast_java_lang_Boolean } from '../../../java/lang/JavaBoolean';
import { JavaDouble, cast_java_lang_Double } from '../../../java/lang/JavaDouble';

export class LehrerPersonaldaten extends JavaObject {

	public id : number = 0;

	public identNrTeil1 : String | null = null;

	public identNrTeil2SerNr : String | null = null;

	public personalaktennummer : String | null = null;

	public lbvPersonalnummer : String | null = null;

	public lbvVerguetungsschluessel : String | null = null;

	public zugangsdatum : String | null = null;

	public zugangsgrund : String | null = null;

	public abgangsdatum : String | null = null;

	public abgangsgrund : String | null = null;

	public pflichtstundensoll : Number | null = null;

	public rechtsverhaeltnis : String | null = null;

	public beschaeftigungsart : String | null = null;

	public einsatzstatus : String | null = null;

	public stammschulnummer : String | null = null;

	public masernImpfnachweis : Boolean | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.lehrer.LehrerPersonaldaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): LehrerPersonaldaten {
		const obj = JSON.parse(json);
		const result = new LehrerPersonaldaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.identNrTeil1 = typeof obj.identNrTeil1 === "undefined" ? null : obj.identNrTeil1 === null ? null : String(obj.identNrTeil1);
		result.identNrTeil2SerNr = typeof obj.identNrTeil2SerNr === "undefined" ? null : obj.identNrTeil2SerNr === null ? null : String(obj.identNrTeil2SerNr);
		result.personalaktennummer = typeof obj.personalaktennummer === "undefined" ? null : obj.personalaktennummer === null ? null : String(obj.personalaktennummer);
		result.lbvPersonalnummer = typeof obj.lbvPersonalnummer === "undefined" ? null : obj.lbvPersonalnummer === null ? null : String(obj.lbvPersonalnummer);
		result.lbvVerguetungsschluessel = typeof obj.lbvVerguetungsschluessel === "undefined" ? null : obj.lbvVerguetungsschluessel === null ? null : String(obj.lbvVerguetungsschluessel);
		result.zugangsdatum = typeof obj.zugangsdatum === "undefined" ? null : obj.zugangsdatum === null ? null : String(obj.zugangsdatum);
		result.zugangsgrund = typeof obj.zugangsgrund === "undefined" ? null : obj.zugangsgrund === null ? null : String(obj.zugangsgrund);
		result.abgangsdatum = typeof obj.abgangsdatum === "undefined" ? null : obj.abgangsdatum === null ? null : String(obj.abgangsdatum);
		result.abgangsgrund = typeof obj.abgangsgrund === "undefined" ? null : obj.abgangsgrund === null ? null : String(obj.abgangsgrund);
		result.pflichtstundensoll = typeof obj.pflichtstundensoll === "undefined" ? null : obj.pflichtstundensoll === null ? null : Number(obj.pflichtstundensoll);
		result.rechtsverhaeltnis = typeof obj.rechtsverhaeltnis === "undefined" ? null : obj.rechtsverhaeltnis === null ? null : String(obj.rechtsverhaeltnis);
		result.beschaeftigungsart = typeof obj.beschaeftigungsart === "undefined" ? null : obj.beschaeftigungsart === null ? null : String(obj.beschaeftigungsart);
		result.einsatzstatus = typeof obj.einsatzstatus === "undefined" ? null : obj.einsatzstatus === null ? null : String(obj.einsatzstatus);
		result.stammschulnummer = typeof obj.stammschulnummer === "undefined" ? null : obj.stammschulnummer === null ? null : String(obj.stammschulnummer);
		result.masernImpfnachweis = typeof obj.masernImpfnachweis === "undefined" ? null : obj.masernImpfnachweis === null ? null : Boolean(obj.masernImpfnachweis);
		return result;
	}

	public static transpilerToJSON(obj : LehrerPersonaldaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"identNrTeil1" : ' + ((!obj.identNrTeil1) ? 'null' : '"' + obj.identNrTeil1.valueOf() + '"') + ',';
		result += '"identNrTeil2SerNr" : ' + ((!obj.identNrTeil2SerNr) ? 'null' : '"' + obj.identNrTeil2SerNr.valueOf() + '"') + ',';
		result += '"personalaktennummer" : ' + ((!obj.personalaktennummer) ? 'null' : '"' + obj.personalaktennummer.valueOf() + '"') + ',';
		result += '"lbvPersonalnummer" : ' + ((!obj.lbvPersonalnummer) ? 'null' : '"' + obj.lbvPersonalnummer.valueOf() + '"') + ',';
		result += '"lbvVerguetungsschluessel" : ' + ((!obj.lbvVerguetungsschluessel) ? 'null' : '"' + obj.lbvVerguetungsschluessel.valueOf() + '"') + ',';
		result += '"zugangsdatum" : ' + ((!obj.zugangsdatum) ? 'null' : '"' + obj.zugangsdatum.valueOf() + '"') + ',';
		result += '"zugangsgrund" : ' + ((!obj.zugangsgrund) ? 'null' : '"' + obj.zugangsgrund.valueOf() + '"') + ',';
		result += '"abgangsdatum" : ' + ((!obj.abgangsdatum) ? 'null' : '"' + obj.abgangsdatum.valueOf() + '"') + ',';
		result += '"abgangsgrund" : ' + ((!obj.abgangsgrund) ? 'null' : '"' + obj.abgangsgrund.valueOf() + '"') + ',';
		result += '"pflichtstundensoll" : ' + ((!obj.pflichtstundensoll) ? 'null' : obj.pflichtstundensoll.valueOf()) + ',';
		result += '"rechtsverhaeltnis" : ' + ((!obj.rechtsverhaeltnis) ? 'null' : '"' + obj.rechtsverhaeltnis.valueOf() + '"') + ',';
		result += '"beschaeftigungsart" : ' + ((!obj.beschaeftigungsart) ? 'null' : '"' + obj.beschaeftigungsart.valueOf() + '"') + ',';
		result += '"einsatzstatus" : ' + ((!obj.einsatzstatus) ? 'null' : '"' + obj.einsatzstatus.valueOf() + '"') + ',';
		result += '"stammschulnummer" : ' + ((!obj.stammschulnummer) ? 'null' : '"' + obj.stammschulnummer.valueOf() + '"') + ',';
		result += '"masernImpfnachweis" : ' + ((!obj.masernImpfnachweis) ? 'null' : obj.masernImpfnachweis.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LehrerPersonaldaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.identNrTeil1 !== "undefined") {
			result += '"identNrTeil1" : ' + ((!obj.identNrTeil1) ? 'null' : '"' + obj.identNrTeil1.valueOf() + '"') + ',';
		}
		if (typeof obj.identNrTeil2SerNr !== "undefined") {
			result += '"identNrTeil2SerNr" : ' + ((!obj.identNrTeil2SerNr) ? 'null' : '"' + obj.identNrTeil2SerNr.valueOf() + '"') + ',';
		}
		if (typeof obj.personalaktennummer !== "undefined") {
			result += '"personalaktennummer" : ' + ((!obj.personalaktennummer) ? 'null' : '"' + obj.personalaktennummer.valueOf() + '"') + ',';
		}
		if (typeof obj.lbvPersonalnummer !== "undefined") {
			result += '"lbvPersonalnummer" : ' + ((!obj.lbvPersonalnummer) ? 'null' : '"' + obj.lbvPersonalnummer.valueOf() + '"') + ',';
		}
		if (typeof obj.lbvVerguetungsschluessel !== "undefined") {
			result += '"lbvVerguetungsschluessel" : ' + ((!obj.lbvVerguetungsschluessel) ? 'null' : '"' + obj.lbvVerguetungsschluessel.valueOf() + '"') + ',';
		}
		if (typeof obj.zugangsdatum !== "undefined") {
			result += '"zugangsdatum" : ' + ((!obj.zugangsdatum) ? 'null' : '"' + obj.zugangsdatum.valueOf() + '"') + ',';
		}
		if (typeof obj.zugangsgrund !== "undefined") {
			result += '"zugangsgrund" : ' + ((!obj.zugangsgrund) ? 'null' : '"' + obj.zugangsgrund.valueOf() + '"') + ',';
		}
		if (typeof obj.abgangsdatum !== "undefined") {
			result += '"abgangsdatum" : ' + ((!obj.abgangsdatum) ? 'null' : '"' + obj.abgangsdatum.valueOf() + '"') + ',';
		}
		if (typeof obj.abgangsgrund !== "undefined") {
			result += '"abgangsgrund" : ' + ((!obj.abgangsgrund) ? 'null' : '"' + obj.abgangsgrund.valueOf() + '"') + ',';
		}
		if (typeof obj.pflichtstundensoll !== "undefined") {
			result += '"pflichtstundensoll" : ' + ((!obj.pflichtstundensoll) ? 'null' : obj.pflichtstundensoll.valueOf()) + ',';
		}
		if (typeof obj.rechtsverhaeltnis !== "undefined") {
			result += '"rechtsverhaeltnis" : ' + ((!obj.rechtsverhaeltnis) ? 'null' : '"' + obj.rechtsverhaeltnis.valueOf() + '"') + ',';
		}
		if (typeof obj.beschaeftigungsart !== "undefined") {
			result += '"beschaeftigungsart" : ' + ((!obj.beschaeftigungsart) ? 'null' : '"' + obj.beschaeftigungsart.valueOf() + '"') + ',';
		}
		if (typeof obj.einsatzstatus !== "undefined") {
			result += '"einsatzstatus" : ' + ((!obj.einsatzstatus) ? 'null' : '"' + obj.einsatzstatus.valueOf() + '"') + ',';
		}
		if (typeof obj.stammschulnummer !== "undefined") {
			result += '"stammschulnummer" : ' + ((!obj.stammschulnummer) ? 'null' : '"' + obj.stammschulnummer.valueOf() + '"') + ',';
		}
		if (typeof obj.masernImpfnachweis !== "undefined") {
			result += '"masernImpfnachweis" : ' + ((!obj.masernImpfnachweis) ? 'null' : obj.masernImpfnachweis.valueOf()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_lehrer_LehrerPersonaldaten(obj : unknown) : LehrerPersonaldaten {
	return obj as LehrerPersonaldaten;
}
