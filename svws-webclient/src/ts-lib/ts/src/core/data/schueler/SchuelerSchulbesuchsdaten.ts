import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { SchuelerSchulbesuchSchule, cast_de_nrw_schule_svws_core_data_schueler_SchuelerSchulbesuchSchule } from '../../../core/data/schueler/SchuelerSchulbesuchSchule';
import { SchuelerSchulbesuchMerkmal, cast_de_nrw_schule_svws_core_data_schueler_SchuelerSchulbesuchMerkmal } from '../../../core/data/schueler/SchuelerSchulbesuchMerkmal';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { JavaBoolean, cast_java_lang_Boolean } from '../../../java/lang/JavaBoolean';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class SchuelerSchulbesuchsdaten extends JavaObject {

	public id : number = 0;

	public vorigeSchulnummer : String | null = null;

	public vorigeAllgHerkunft : String | null = null;

	public vorigeEntlassdatum : String | null = null;

	public vorigeEntlassjahrgang : String | null = null;

	public vorigeArtLetzteVersetzung : String | null = null;

	public vorigeBemerkung : String | null = null;

	public vorigeEntlassgrundID : Number | null = null;

	public vorigeAbschlussartID : String | null = null;

	public entlassungDatum : String | null = null;

	public entlassungJahrgang : String | null = null;

	public entlassungGrundID : Number | null = null;

	public entlassungAbschlussartID : String | null = null;

	public aufnehmdendSchulnummer : String | null = null;

	public aufnehmdendWechseldatum : String | null = null;

	public aufnehmdendBestaetigt : Boolean | null = null;

	public grundschuleEinschulungsjahr : Number | null = null;

	public grundschuleEinschulungsartID : Number | null = null;

	public grundschuleJahreEingangsphase : Number | null = null;

	public grundschuleUebergangsempfehlungID : Number | null = null;

	public sekIWechsel : Number | null = null;

	public sekIErsteSchulform : String | null = null;

	public sekIIWechsel : Number | null = null;

	public merkmale : Vector<SchuelerSchulbesuchMerkmal> = new Vector();

	public alleSchulen : Vector<SchuelerSchulbesuchSchule> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schueler.SchuelerSchulbesuchsdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerSchulbesuchsdaten {
		const obj = JSON.parse(json);
		const result = new SchuelerSchulbesuchsdaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.vorigeSchulnummer = typeof obj.vorigeSchulnummer === "undefined" ? null : obj.vorigeSchulnummer;
		result.vorigeAllgHerkunft = typeof obj.vorigeAllgHerkunft === "undefined" ? null : obj.vorigeAllgHerkunft;
		result.vorigeEntlassdatum = typeof obj.vorigeEntlassdatum === "undefined" ? null : obj.vorigeEntlassdatum;
		result.vorigeEntlassjahrgang = typeof obj.vorigeEntlassjahrgang === "undefined" ? null : obj.vorigeEntlassjahrgang;
		result.vorigeArtLetzteVersetzung = typeof obj.vorigeArtLetzteVersetzung === "undefined" ? null : obj.vorigeArtLetzteVersetzung;
		result.vorigeBemerkung = typeof obj.vorigeBemerkung === "undefined" ? null : obj.vorigeBemerkung;
		result.vorigeEntlassgrundID = typeof obj.vorigeEntlassgrundID === "undefined" ? null : obj.vorigeEntlassgrundID;
		result.vorigeAbschlussartID = typeof obj.vorigeAbschlussartID === "undefined" ? null : obj.vorigeAbschlussartID;
		result.entlassungDatum = typeof obj.entlassungDatum === "undefined" ? null : obj.entlassungDatum;
		result.entlassungJahrgang = typeof obj.entlassungJahrgang === "undefined" ? null : obj.entlassungJahrgang;
		result.entlassungGrundID = typeof obj.entlassungGrundID === "undefined" ? null : obj.entlassungGrundID;
		result.entlassungAbschlussartID = typeof obj.entlassungAbschlussartID === "undefined" ? null : obj.entlassungAbschlussartID;
		result.aufnehmdendSchulnummer = typeof obj.aufnehmdendSchulnummer === "undefined" ? null : obj.aufnehmdendSchulnummer;
		result.aufnehmdendWechseldatum = typeof obj.aufnehmdendWechseldatum === "undefined" ? null : obj.aufnehmdendWechseldatum;
		result.aufnehmdendBestaetigt = typeof obj.aufnehmdendBestaetigt === "undefined" ? null : obj.aufnehmdendBestaetigt;
		result.grundschuleEinschulungsjahr = typeof obj.grundschuleEinschulungsjahr === "undefined" ? null : obj.grundschuleEinschulungsjahr;
		result.grundschuleEinschulungsartID = typeof obj.grundschuleEinschulungsartID === "undefined" ? null : obj.grundschuleEinschulungsartID;
		result.grundschuleJahreEingangsphase = typeof obj.grundschuleJahreEingangsphase === "undefined" ? null : obj.grundschuleJahreEingangsphase;
		result.grundschuleUebergangsempfehlungID = typeof obj.grundschuleUebergangsempfehlungID === "undefined" ? null : obj.grundschuleUebergangsempfehlungID;
		result.sekIWechsel = typeof obj.sekIWechsel === "undefined" ? null : obj.sekIWechsel;
		result.sekIErsteSchulform = typeof obj.sekIErsteSchulform === "undefined" ? null : obj.sekIErsteSchulform;
		result.sekIIWechsel = typeof obj.sekIIWechsel === "undefined" ? null : obj.sekIIWechsel;
		if (!!obj.merkmale) {
			for (let elem of obj.merkmale) {
				result.merkmale?.add(SchuelerSchulbesuchMerkmal.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!!obj.alleSchulen) {
			for (let elem of obj.alleSchulen) {
				result.alleSchulen?.add(SchuelerSchulbesuchSchule.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchuelerSchulbesuchsdaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"vorigeSchulnummer" : ' + ((!obj.vorigeSchulnummer) ? 'null' : '"' + obj.vorigeSchulnummer.valueOf() + '"') + ',';
		result += '"vorigeAllgHerkunft" : ' + ((!obj.vorigeAllgHerkunft) ? 'null' : '"' + obj.vorigeAllgHerkunft.valueOf() + '"') + ',';
		result += '"vorigeEntlassdatum" : ' + ((!obj.vorigeEntlassdatum) ? 'null' : '"' + obj.vorigeEntlassdatum.valueOf() + '"') + ',';
		result += '"vorigeEntlassjahrgang" : ' + ((!obj.vorigeEntlassjahrgang) ? 'null' : '"' + obj.vorigeEntlassjahrgang.valueOf() + '"') + ',';
		result += '"vorigeArtLetzteVersetzung" : ' + ((!obj.vorigeArtLetzteVersetzung) ? 'null' : '"' + obj.vorigeArtLetzteVersetzung.valueOf() + '"') + ',';
		result += '"vorigeBemerkung" : ' + ((!obj.vorigeBemerkung) ? 'null' : '"' + obj.vorigeBemerkung.valueOf() + '"') + ',';
		result += '"vorigeEntlassgrundID" : ' + ((!obj.vorigeEntlassgrundID) ? 'null' : obj.vorigeEntlassgrundID.valueOf()) + ',';
		result += '"vorigeAbschlussartID" : ' + ((!obj.vorigeAbschlussartID) ? 'null' : '"' + obj.vorigeAbschlussartID.valueOf() + '"') + ',';
		result += '"entlassungDatum" : ' + ((!obj.entlassungDatum) ? 'null' : '"' + obj.entlassungDatum.valueOf() + '"') + ',';
		result += '"entlassungJahrgang" : ' + ((!obj.entlassungJahrgang) ? 'null' : '"' + obj.entlassungJahrgang.valueOf() + '"') + ',';
		result += '"entlassungGrundID" : ' + ((!obj.entlassungGrundID) ? 'null' : obj.entlassungGrundID.valueOf()) + ',';
		result += '"entlassungAbschlussartID" : ' + ((!obj.entlassungAbschlussartID) ? 'null' : '"' + obj.entlassungAbschlussartID.valueOf() + '"') + ',';
		result += '"aufnehmdendSchulnummer" : ' + ((!obj.aufnehmdendSchulnummer) ? 'null' : '"' + obj.aufnehmdendSchulnummer.valueOf() + '"') + ',';
		result += '"aufnehmdendWechseldatum" : ' + ((!obj.aufnehmdendWechseldatum) ? 'null' : '"' + obj.aufnehmdendWechseldatum.valueOf() + '"') + ',';
		result += '"aufnehmdendBestaetigt" : ' + ((!obj.aufnehmdendBestaetigt) ? 'null' : obj.aufnehmdendBestaetigt.valueOf()) + ',';
		result += '"grundschuleEinschulungsjahr" : ' + ((!obj.grundschuleEinschulungsjahr) ? 'null' : obj.grundschuleEinschulungsjahr.valueOf()) + ',';
		result += '"grundschuleEinschulungsartID" : ' + ((!obj.grundschuleEinschulungsartID) ? 'null' : obj.grundschuleEinschulungsartID.valueOf()) + ',';
		result += '"grundschuleJahreEingangsphase" : ' + ((!obj.grundschuleJahreEingangsphase) ? 'null' : obj.grundschuleJahreEingangsphase.valueOf()) + ',';
		result += '"grundschuleUebergangsempfehlungID" : ' + ((!obj.grundschuleUebergangsempfehlungID) ? 'null' : obj.grundschuleUebergangsempfehlungID.valueOf()) + ',';
		result += '"sekIWechsel" : ' + ((!obj.sekIWechsel) ? 'null' : obj.sekIWechsel.valueOf()) + ',';
		result += '"sekIErsteSchulform" : ' + ((!obj.sekIErsteSchulform) ? 'null' : '"' + obj.sekIErsteSchulform.valueOf() + '"') + ',';
		result += '"sekIIWechsel" : ' + ((!obj.sekIIWechsel) ? 'null' : obj.sekIIWechsel.valueOf()) + ',';
		if (!obj.merkmale) {
			result += '"merkmale" : []';
		} else {
			result += '"merkmale" : [ ';
			for (let i : number = 0; i < obj.merkmale.size(); i++) {
				let elem = obj.merkmale.get(i);
				result += SchuelerSchulbesuchMerkmal.transpilerToJSON(elem);
				if (i < obj.merkmale.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.alleSchulen) {
			result += '"alleSchulen" : []';
		} else {
			result += '"alleSchulen" : [ ';
			for (let i : number = 0; i < obj.alleSchulen.size(); i++) {
				let elem = obj.alleSchulen.get(i);
				result += SchuelerSchulbesuchSchule.transpilerToJSON(elem);
				if (i < obj.alleSchulen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerSchulbesuchsdaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.vorigeSchulnummer !== "undefined") {
			result += '"vorigeSchulnummer" : ' + ((!obj.vorigeSchulnummer) ? 'null' : '"' + obj.vorigeSchulnummer.valueOf() + '"') + ',';
		}
		if (typeof obj.vorigeAllgHerkunft !== "undefined") {
			result += '"vorigeAllgHerkunft" : ' + ((!obj.vorigeAllgHerkunft) ? 'null' : '"' + obj.vorigeAllgHerkunft.valueOf() + '"') + ',';
		}
		if (typeof obj.vorigeEntlassdatum !== "undefined") {
			result += '"vorigeEntlassdatum" : ' + ((!obj.vorigeEntlassdatum) ? 'null' : '"' + obj.vorigeEntlassdatum.valueOf() + '"') + ',';
		}
		if (typeof obj.vorigeEntlassjahrgang !== "undefined") {
			result += '"vorigeEntlassjahrgang" : ' + ((!obj.vorigeEntlassjahrgang) ? 'null' : '"' + obj.vorigeEntlassjahrgang.valueOf() + '"') + ',';
		}
		if (typeof obj.vorigeArtLetzteVersetzung !== "undefined") {
			result += '"vorigeArtLetzteVersetzung" : ' + ((!obj.vorigeArtLetzteVersetzung) ? 'null' : '"' + obj.vorigeArtLetzteVersetzung.valueOf() + '"') + ',';
		}
		if (typeof obj.vorigeBemerkung !== "undefined") {
			result += '"vorigeBemerkung" : ' + ((!obj.vorigeBemerkung) ? 'null' : '"' + obj.vorigeBemerkung.valueOf() + '"') + ',';
		}
		if (typeof obj.vorigeEntlassgrundID !== "undefined") {
			result += '"vorigeEntlassgrundID" : ' + ((!obj.vorigeEntlassgrundID) ? 'null' : obj.vorigeEntlassgrundID.valueOf()) + ',';
		}
		if (typeof obj.vorigeAbschlussartID !== "undefined") {
			result += '"vorigeAbschlussartID" : ' + ((!obj.vorigeAbschlussartID) ? 'null' : '"' + obj.vorigeAbschlussartID.valueOf() + '"') + ',';
		}
		if (typeof obj.entlassungDatum !== "undefined") {
			result += '"entlassungDatum" : ' + ((!obj.entlassungDatum) ? 'null' : '"' + obj.entlassungDatum.valueOf() + '"') + ',';
		}
		if (typeof obj.entlassungJahrgang !== "undefined") {
			result += '"entlassungJahrgang" : ' + ((!obj.entlassungJahrgang) ? 'null' : '"' + obj.entlassungJahrgang.valueOf() + '"') + ',';
		}
		if (typeof obj.entlassungGrundID !== "undefined") {
			result += '"entlassungGrundID" : ' + ((!obj.entlassungGrundID) ? 'null' : obj.entlassungGrundID.valueOf()) + ',';
		}
		if (typeof obj.entlassungAbschlussartID !== "undefined") {
			result += '"entlassungAbschlussartID" : ' + ((!obj.entlassungAbschlussartID) ? 'null' : '"' + obj.entlassungAbschlussartID.valueOf() + '"') + ',';
		}
		if (typeof obj.aufnehmdendSchulnummer !== "undefined") {
			result += '"aufnehmdendSchulnummer" : ' + ((!obj.aufnehmdendSchulnummer) ? 'null' : '"' + obj.aufnehmdendSchulnummer.valueOf() + '"') + ',';
		}
		if (typeof obj.aufnehmdendWechseldatum !== "undefined") {
			result += '"aufnehmdendWechseldatum" : ' + ((!obj.aufnehmdendWechseldatum) ? 'null' : '"' + obj.aufnehmdendWechseldatum.valueOf() + '"') + ',';
		}
		if (typeof obj.aufnehmdendBestaetigt !== "undefined") {
			result += '"aufnehmdendBestaetigt" : ' + ((!obj.aufnehmdendBestaetigt) ? 'null' : obj.aufnehmdendBestaetigt.valueOf()) + ',';
		}
		if (typeof obj.grundschuleEinschulungsjahr !== "undefined") {
			result += '"grundschuleEinschulungsjahr" : ' + ((!obj.grundschuleEinschulungsjahr) ? 'null' : obj.grundschuleEinschulungsjahr.valueOf()) + ',';
		}
		if (typeof obj.grundschuleEinschulungsartID !== "undefined") {
			result += '"grundschuleEinschulungsartID" : ' + ((!obj.grundschuleEinschulungsartID) ? 'null' : obj.grundschuleEinschulungsartID.valueOf()) + ',';
		}
		if (typeof obj.grundschuleJahreEingangsphase !== "undefined") {
			result += '"grundschuleJahreEingangsphase" : ' + ((!obj.grundschuleJahreEingangsphase) ? 'null' : obj.grundschuleJahreEingangsphase.valueOf()) + ',';
		}
		if (typeof obj.grundschuleUebergangsempfehlungID !== "undefined") {
			result += '"grundschuleUebergangsempfehlungID" : ' + ((!obj.grundschuleUebergangsempfehlungID) ? 'null' : obj.grundschuleUebergangsempfehlungID.valueOf()) + ',';
		}
		if (typeof obj.sekIWechsel !== "undefined") {
			result += '"sekIWechsel" : ' + ((!obj.sekIWechsel) ? 'null' : obj.sekIWechsel.valueOf()) + ',';
		}
		if (typeof obj.sekIErsteSchulform !== "undefined") {
			result += '"sekIErsteSchulform" : ' + ((!obj.sekIErsteSchulform) ? 'null' : '"' + obj.sekIErsteSchulform.valueOf() + '"') + ',';
		}
		if (typeof obj.sekIIWechsel !== "undefined") {
			result += '"sekIIWechsel" : ' + ((!obj.sekIIWechsel) ? 'null' : obj.sekIIWechsel.valueOf()) + ',';
		}
		if (typeof obj.merkmale !== "undefined") {
			if (!obj.merkmale) {
				result += '"merkmale" : []';
			} else {
				result += '"merkmale" : [ ';
				for (let i : number = 0; i < obj.merkmale.size(); i++) {
					let elem = obj.merkmale.get(i);
					result += SchuelerSchulbesuchMerkmal.transpilerToJSON(elem);
					if (i < obj.merkmale.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.alleSchulen !== "undefined") {
			if (!obj.alleSchulen) {
				result += '"alleSchulen" : []';
			} else {
				result += '"alleSchulen" : [ ';
				for (let i : number = 0; i < obj.alleSchulen.size(); i++) {
					let elem = obj.alleSchulen.get(i);
					result += SchuelerSchulbesuchSchule.transpilerToJSON(elem);
					if (i < obj.alleSchulen.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_schueler_SchuelerSchulbesuchsdaten(obj : unknown) : SchuelerSchulbesuchsdaten {
	return obj as SchuelerSchulbesuchsdaten;
}
