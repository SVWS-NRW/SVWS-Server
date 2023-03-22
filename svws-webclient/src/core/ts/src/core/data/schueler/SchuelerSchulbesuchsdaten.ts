import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { SchuelerSchulbesuchSchule, cast_de_nrw_schule_svws_core_data_schueler_SchuelerSchulbesuchSchule } from '../../../core/data/schueler/SchuelerSchulbesuchSchule';
import { SchuelerSchulbesuchMerkmal, cast_de_nrw_schule_svws_core_data_schueler_SchuelerSchulbesuchMerkmal } from '../../../core/data/schueler/SchuelerSchulbesuchMerkmal';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { JavaBoolean, cast_java_lang_Boolean } from '../../../java/lang/JavaBoolean';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class SchuelerSchulbesuchsdaten extends JavaObject {

	/**
	 * Die ID des Schülerdatensatzes.
	 */
	public id : number = 0;

	/**
	 * Die Schulnummer der vorher besuchten Schule.
	 */
	public vorigeSchulnummer : string | null = null;

	/**
	 * Die allgemeine Herkunftsart des Schüler in Bezug auf die schulform der zuvor besuchten Schule.
	 */
	public vorigeAllgHerkunft : string | null = null;

	/**
	 * Das Entlassdatum an der zuvor besuchten Schule.
	 */
	public vorigeEntlassdatum : string | null = null;

	/**
	 * Der Entlassjahrgang an der zuvor besuchten Schule.
	 */
	public vorigeEntlassjahrgang : string | null = null;

	/**
	 * Die ID der Art der letzten Versetzung an der zuvor besuchten Schule.
	 */
	public vorigeArtLetzteVersetzung : string | null = null;

	/**
	 * Bemerkungen zu der zuvor besuchten Schule.
	 */
	public vorigeBemerkung : string | null = null;

	/**
	 * Die ID des Grundes für die Entlassung von der zuvor besuchten Schule.
	 */
	public vorigeEntlassgrundID : number | null = null;

	/**
	 * Die ID des Abschlusses, welcher an der zuvor besuchten Schule erworben wurde.
	 */
	public vorigeAbschlussartID : string | null = null;

	/**
	 * Das Entlassdatum von dieser Schule.
	 */
	public entlassungDatum : string | null = null;

	/**
	 * Der Jahrgang bei der Entlassung von dieser Schule.
	 */
	public entlassungJahrgang : string | null = null;

	/**
	 * Die ID des Grundes für die Entlassung von dieser Schule.
	 */
	public entlassungGrundID : number | null = null;

	/**
	 * Die ID des Abschlusses, welcher an dieser Schule erworben wurde.
	 */
	public entlassungAbschlussartID : string | null = null;

	/**
	 * Die Schulnummer der aufnehmenden Schule nach einer Entlassung.
	 */
	public aufnehmdendSchulnummer : string | null = null;

	/**
	 * Das Datum beim Wechsel zu einer aufnehmenden Schule.
	 */
	public aufnehmdendWechseldatum : string | null = null;

	/**
	 * Gibt an, ob die aufnehmende Schule den Wechsel bestätigt hat.
	 */
	public aufnehmdendBestaetigt : boolean | null = null;

	/**
	 * Das Jahr der Einschulung in die Grundschule.
	 */
	public grundschuleEinschulungsjahr : number | null = null;

	/**
	 * Die ID der Einschulungsart in die Grundschule.
	 */
	public grundschuleEinschulungsartID : number | null = null;

	/**
	 * Die Anzahl der Jahre in der Schuleingangsphase der Grundschule.
	 */
	public grundschuleJahreEingangsphase : number | null = null;

	/**
	 * Die ID für die Übergangsempfehlung der Grundschule in die Sekundarstufe I
	 */
	public grundschuleUebergangsempfehlungID : number | null = null;

	/**
	 * Das Jahr des Wechsels in die Sekundarstufe I.
	 */
	public sekIWechsel : number | null = null;

	/**
	 * Das Kürzel der ersten Schulform in der Sekundarstufe I
	 */
	public sekIErsteSchulform : string | null = null;

	/**
	 * Das Jahr des Wechsels in die Sekundarstufe II.
	 */
	public sekIIWechsel : number | null = null;

	/**
	 * Die Informationen zu den besonderen Merkmalen für die Statistik.
	 */
	public merkmale : Vector<SchuelerSchulbesuchMerkmal> = new Vector();

	/**
	 * Die Informationen zu allen bisher besuchten Schulen.
	 */
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
		result.vorigeSchulnummer = typeof obj.vorigeSchulnummer === "undefined" ? null : obj.vorigeSchulnummer === null ? null : obj.vorigeSchulnummer;
		result.vorigeAllgHerkunft = typeof obj.vorigeAllgHerkunft === "undefined" ? null : obj.vorigeAllgHerkunft === null ? null : obj.vorigeAllgHerkunft;
		result.vorigeEntlassdatum = typeof obj.vorigeEntlassdatum === "undefined" ? null : obj.vorigeEntlassdatum === null ? null : obj.vorigeEntlassdatum;
		result.vorigeEntlassjahrgang = typeof obj.vorigeEntlassjahrgang === "undefined" ? null : obj.vorigeEntlassjahrgang === null ? null : obj.vorigeEntlassjahrgang;
		result.vorigeArtLetzteVersetzung = typeof obj.vorigeArtLetzteVersetzung === "undefined" ? null : obj.vorigeArtLetzteVersetzung === null ? null : obj.vorigeArtLetzteVersetzung;
		result.vorigeBemerkung = typeof obj.vorigeBemerkung === "undefined" ? null : obj.vorigeBemerkung === null ? null : obj.vorigeBemerkung;
		result.vorigeEntlassgrundID = typeof obj.vorigeEntlassgrundID === "undefined" ? null : obj.vorigeEntlassgrundID === null ? null : obj.vorigeEntlassgrundID;
		result.vorigeAbschlussartID = typeof obj.vorigeAbschlussartID === "undefined" ? null : obj.vorigeAbschlussartID === null ? null : obj.vorigeAbschlussartID;
		result.entlassungDatum = typeof obj.entlassungDatum === "undefined" ? null : obj.entlassungDatum === null ? null : obj.entlassungDatum;
		result.entlassungJahrgang = typeof obj.entlassungJahrgang === "undefined" ? null : obj.entlassungJahrgang === null ? null : obj.entlassungJahrgang;
		result.entlassungGrundID = typeof obj.entlassungGrundID === "undefined" ? null : obj.entlassungGrundID === null ? null : obj.entlassungGrundID;
		result.entlassungAbschlussartID = typeof obj.entlassungAbschlussartID === "undefined" ? null : obj.entlassungAbschlussartID === null ? null : obj.entlassungAbschlussartID;
		result.aufnehmdendSchulnummer = typeof obj.aufnehmdendSchulnummer === "undefined" ? null : obj.aufnehmdendSchulnummer === null ? null : obj.aufnehmdendSchulnummer;
		result.aufnehmdendWechseldatum = typeof obj.aufnehmdendWechseldatum === "undefined" ? null : obj.aufnehmdendWechseldatum === null ? null : obj.aufnehmdendWechseldatum;
		result.aufnehmdendBestaetigt = typeof obj.aufnehmdendBestaetigt === "undefined" ? null : obj.aufnehmdendBestaetigt === null ? null : obj.aufnehmdendBestaetigt;
		result.grundschuleEinschulungsjahr = typeof obj.grundschuleEinschulungsjahr === "undefined" ? null : obj.grundschuleEinschulungsjahr === null ? null : obj.grundschuleEinschulungsjahr;
		result.grundschuleEinschulungsartID = typeof obj.grundschuleEinschulungsartID === "undefined" ? null : obj.grundschuleEinschulungsartID === null ? null : obj.grundschuleEinschulungsartID;
		result.grundschuleJahreEingangsphase = typeof obj.grundschuleJahreEingangsphase === "undefined" ? null : obj.grundschuleJahreEingangsphase === null ? null : obj.grundschuleJahreEingangsphase;
		result.grundschuleUebergangsempfehlungID = typeof obj.grundschuleUebergangsempfehlungID === "undefined" ? null : obj.grundschuleUebergangsempfehlungID === null ? null : obj.grundschuleUebergangsempfehlungID;
		result.sekIWechsel = typeof obj.sekIWechsel === "undefined" ? null : obj.sekIWechsel === null ? null : obj.sekIWechsel;
		result.sekIErsteSchulform = typeof obj.sekIErsteSchulform === "undefined" ? null : obj.sekIErsteSchulform === null ? null : obj.sekIErsteSchulform;
		result.sekIIWechsel = typeof obj.sekIIWechsel === "undefined" ? null : obj.sekIIWechsel === null ? null : obj.sekIIWechsel;
		if (!((obj.merkmale === undefined) || (obj.merkmale === null))) {
			for (const elem of obj.merkmale) {
				result.merkmale?.add(SchuelerSchulbesuchMerkmal.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!((obj.alleSchulen === undefined) || (obj.alleSchulen === null))) {
			for (const elem of obj.alleSchulen) {
				result.alleSchulen?.add(SchuelerSchulbesuchSchule.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchuelerSchulbesuchsdaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"vorigeSchulnummer" : ' + ((!obj.vorigeSchulnummer) ? 'null' : '"' + obj.vorigeSchulnummer + '"') + ',';
		result += '"vorigeAllgHerkunft" : ' + ((!obj.vorigeAllgHerkunft) ? 'null' : '"' + obj.vorigeAllgHerkunft + '"') + ',';
		result += '"vorigeEntlassdatum" : ' + ((!obj.vorigeEntlassdatum) ? 'null' : '"' + obj.vorigeEntlassdatum + '"') + ',';
		result += '"vorigeEntlassjahrgang" : ' + ((!obj.vorigeEntlassjahrgang) ? 'null' : '"' + obj.vorigeEntlassjahrgang + '"') + ',';
		result += '"vorigeArtLetzteVersetzung" : ' + ((!obj.vorigeArtLetzteVersetzung) ? 'null' : '"' + obj.vorigeArtLetzteVersetzung + '"') + ',';
		result += '"vorigeBemerkung" : ' + ((!obj.vorigeBemerkung) ? 'null' : '"' + obj.vorigeBemerkung + '"') + ',';
		result += '"vorigeEntlassgrundID" : ' + ((!obj.vorigeEntlassgrundID) ? 'null' : obj.vorigeEntlassgrundID) + ',';
		result += '"vorigeAbschlussartID" : ' + ((!obj.vorigeAbschlussartID) ? 'null' : '"' + obj.vorigeAbschlussartID + '"') + ',';
		result += '"entlassungDatum" : ' + ((!obj.entlassungDatum) ? 'null' : '"' + obj.entlassungDatum + '"') + ',';
		result += '"entlassungJahrgang" : ' + ((!obj.entlassungJahrgang) ? 'null' : '"' + obj.entlassungJahrgang + '"') + ',';
		result += '"entlassungGrundID" : ' + ((!obj.entlassungGrundID) ? 'null' : obj.entlassungGrundID) + ',';
		result += '"entlassungAbschlussartID" : ' + ((!obj.entlassungAbschlussartID) ? 'null' : '"' + obj.entlassungAbschlussartID + '"') + ',';
		result += '"aufnehmdendSchulnummer" : ' + ((!obj.aufnehmdendSchulnummer) ? 'null' : '"' + obj.aufnehmdendSchulnummer + '"') + ',';
		result += '"aufnehmdendWechseldatum" : ' + ((!obj.aufnehmdendWechseldatum) ? 'null' : '"' + obj.aufnehmdendWechseldatum + '"') + ',';
		result += '"aufnehmdendBestaetigt" : ' + ((!obj.aufnehmdendBestaetigt) ? 'null' : obj.aufnehmdendBestaetigt) + ',';
		result += '"grundschuleEinschulungsjahr" : ' + ((!obj.grundschuleEinschulungsjahr) ? 'null' : obj.grundschuleEinschulungsjahr) + ',';
		result += '"grundschuleEinschulungsartID" : ' + ((!obj.grundschuleEinschulungsartID) ? 'null' : obj.grundschuleEinschulungsartID) + ',';
		result += '"grundschuleJahreEingangsphase" : ' + ((!obj.grundschuleJahreEingangsphase) ? 'null' : obj.grundschuleJahreEingangsphase) + ',';
		result += '"grundschuleUebergangsempfehlungID" : ' + ((!obj.grundschuleUebergangsempfehlungID) ? 'null' : obj.grundschuleUebergangsempfehlungID) + ',';
		result += '"sekIWechsel" : ' + ((!obj.sekIWechsel) ? 'null' : obj.sekIWechsel) + ',';
		result += '"sekIErsteSchulform" : ' + ((!obj.sekIErsteSchulform) ? 'null' : '"' + obj.sekIErsteSchulform + '"') + ',';
		result += '"sekIIWechsel" : ' + ((!obj.sekIIWechsel) ? 'null' : obj.sekIIWechsel) + ',';
		if (!obj.merkmale) {
			result += '"merkmale" : []';
		} else {
			result += '"merkmale" : [ ';
			for (let i = 0; i < obj.merkmale.size(); i++) {
				const elem = obj.merkmale.get(i);
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
			for (let i = 0; i < obj.alleSchulen.size(); i++) {
				const elem = obj.alleSchulen.get(i);
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
			result += '"vorigeSchulnummer" : ' + ((!obj.vorigeSchulnummer) ? 'null' : '"' + obj.vorigeSchulnummer + '"') + ',';
		}
		if (typeof obj.vorigeAllgHerkunft !== "undefined") {
			result += '"vorigeAllgHerkunft" : ' + ((!obj.vorigeAllgHerkunft) ? 'null' : '"' + obj.vorigeAllgHerkunft + '"') + ',';
		}
		if (typeof obj.vorigeEntlassdatum !== "undefined") {
			result += '"vorigeEntlassdatum" : ' + ((!obj.vorigeEntlassdatum) ? 'null' : '"' + obj.vorigeEntlassdatum + '"') + ',';
		}
		if (typeof obj.vorigeEntlassjahrgang !== "undefined") {
			result += '"vorigeEntlassjahrgang" : ' + ((!obj.vorigeEntlassjahrgang) ? 'null' : '"' + obj.vorigeEntlassjahrgang + '"') + ',';
		}
		if (typeof obj.vorigeArtLetzteVersetzung !== "undefined") {
			result += '"vorigeArtLetzteVersetzung" : ' + ((!obj.vorigeArtLetzteVersetzung) ? 'null' : '"' + obj.vorigeArtLetzteVersetzung + '"') + ',';
		}
		if (typeof obj.vorigeBemerkung !== "undefined") {
			result += '"vorigeBemerkung" : ' + ((!obj.vorigeBemerkung) ? 'null' : '"' + obj.vorigeBemerkung + '"') + ',';
		}
		if (typeof obj.vorigeEntlassgrundID !== "undefined") {
			result += '"vorigeEntlassgrundID" : ' + ((!obj.vorigeEntlassgrundID) ? 'null' : obj.vorigeEntlassgrundID) + ',';
		}
		if (typeof obj.vorigeAbschlussartID !== "undefined") {
			result += '"vorigeAbschlussartID" : ' + ((!obj.vorigeAbschlussartID) ? 'null' : '"' + obj.vorigeAbschlussartID + '"') + ',';
		}
		if (typeof obj.entlassungDatum !== "undefined") {
			result += '"entlassungDatum" : ' + ((!obj.entlassungDatum) ? 'null' : '"' + obj.entlassungDatum + '"') + ',';
		}
		if (typeof obj.entlassungJahrgang !== "undefined") {
			result += '"entlassungJahrgang" : ' + ((!obj.entlassungJahrgang) ? 'null' : '"' + obj.entlassungJahrgang + '"') + ',';
		}
		if (typeof obj.entlassungGrundID !== "undefined") {
			result += '"entlassungGrundID" : ' + ((!obj.entlassungGrundID) ? 'null' : obj.entlassungGrundID) + ',';
		}
		if (typeof obj.entlassungAbschlussartID !== "undefined") {
			result += '"entlassungAbschlussartID" : ' + ((!obj.entlassungAbschlussartID) ? 'null' : '"' + obj.entlassungAbschlussartID + '"') + ',';
		}
		if (typeof obj.aufnehmdendSchulnummer !== "undefined") {
			result += '"aufnehmdendSchulnummer" : ' + ((!obj.aufnehmdendSchulnummer) ? 'null' : '"' + obj.aufnehmdendSchulnummer + '"') + ',';
		}
		if (typeof obj.aufnehmdendWechseldatum !== "undefined") {
			result += '"aufnehmdendWechseldatum" : ' + ((!obj.aufnehmdendWechseldatum) ? 'null' : '"' + obj.aufnehmdendWechseldatum + '"') + ',';
		}
		if (typeof obj.aufnehmdendBestaetigt !== "undefined") {
			result += '"aufnehmdendBestaetigt" : ' + ((!obj.aufnehmdendBestaetigt) ? 'null' : obj.aufnehmdendBestaetigt) + ',';
		}
		if (typeof obj.grundschuleEinschulungsjahr !== "undefined") {
			result += '"grundschuleEinschulungsjahr" : ' + ((!obj.grundschuleEinschulungsjahr) ? 'null' : obj.grundschuleEinschulungsjahr) + ',';
		}
		if (typeof obj.grundschuleEinschulungsartID !== "undefined") {
			result += '"grundschuleEinschulungsartID" : ' + ((!obj.grundschuleEinschulungsartID) ? 'null' : obj.grundschuleEinschulungsartID) + ',';
		}
		if (typeof obj.grundschuleJahreEingangsphase !== "undefined") {
			result += '"grundschuleJahreEingangsphase" : ' + ((!obj.grundschuleJahreEingangsphase) ? 'null' : obj.grundschuleJahreEingangsphase) + ',';
		}
		if (typeof obj.grundschuleUebergangsempfehlungID !== "undefined") {
			result += '"grundschuleUebergangsempfehlungID" : ' + ((!obj.grundschuleUebergangsempfehlungID) ? 'null' : obj.grundschuleUebergangsempfehlungID) + ',';
		}
		if (typeof obj.sekIWechsel !== "undefined") {
			result += '"sekIWechsel" : ' + ((!obj.sekIWechsel) ? 'null' : obj.sekIWechsel) + ',';
		}
		if (typeof obj.sekIErsteSchulform !== "undefined") {
			result += '"sekIErsteSchulform" : ' + ((!obj.sekIErsteSchulform) ? 'null' : '"' + obj.sekIErsteSchulform + '"') + ',';
		}
		if (typeof obj.sekIIWechsel !== "undefined") {
			result += '"sekIIWechsel" : ' + ((!obj.sekIIWechsel) ? 'null' : obj.sekIIWechsel) + ',';
		}
		if (typeof obj.merkmale !== "undefined") {
			if (!obj.merkmale) {
				result += '"merkmale" : []';
			} else {
				result += '"merkmale" : [ ';
				for (let i = 0; i < obj.merkmale.size(); i++) {
					const elem = obj.merkmale.get(i);
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
				for (let i = 0; i < obj.alleSchulen.size(); i++) {
					const elem = obj.alleSchulen.get(i);
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
