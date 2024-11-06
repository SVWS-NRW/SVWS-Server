import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuelerSchulbesuchSchule } from '../../../core/data/schueler/SchuelerSchulbesuchSchule';
import { SchuelerSchulbesuchMerkmal } from '../../../core/data/schueler/SchuelerSchulbesuchMerkmal';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

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
	public merkmale : List<SchuelerSchulbesuchMerkmal> = new ArrayList<SchuelerSchulbesuchMerkmal>();

	/**
	 * Die Informationen zu allen bisher besuchten Schulen.
	 */
	public alleSchulen : List<SchuelerSchulbesuchSchule> = new ArrayList<SchuelerSchulbesuchSchule>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schueler.SchuelerSchulbesuchsdaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.SchuelerSchulbesuchsdaten'].includes(name);
	}

	public static class = new Class<SchuelerSchulbesuchsdaten>('de.svws_nrw.core.data.schueler.SchuelerSchulbesuchsdaten');

	public static transpilerFromJSON(json : string): SchuelerSchulbesuchsdaten {
		const obj = JSON.parse(json) as Partial<SchuelerSchulbesuchsdaten>;
		const result = new SchuelerSchulbesuchsdaten();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.vorigeSchulnummer = (obj.vorigeSchulnummer === undefined) ? null : obj.vorigeSchulnummer === null ? null : obj.vorigeSchulnummer;
		result.vorigeAllgHerkunft = (obj.vorigeAllgHerkunft === undefined) ? null : obj.vorigeAllgHerkunft === null ? null : obj.vorigeAllgHerkunft;
		result.vorigeEntlassdatum = (obj.vorigeEntlassdatum === undefined) ? null : obj.vorigeEntlassdatum === null ? null : obj.vorigeEntlassdatum;
		result.vorigeEntlassjahrgang = (obj.vorigeEntlassjahrgang === undefined) ? null : obj.vorigeEntlassjahrgang === null ? null : obj.vorigeEntlassjahrgang;
		result.vorigeArtLetzteVersetzung = (obj.vorigeArtLetzteVersetzung === undefined) ? null : obj.vorigeArtLetzteVersetzung === null ? null : obj.vorigeArtLetzteVersetzung;
		result.vorigeBemerkung = (obj.vorigeBemerkung === undefined) ? null : obj.vorigeBemerkung === null ? null : obj.vorigeBemerkung;
		result.vorigeEntlassgrundID = (obj.vorigeEntlassgrundID === undefined) ? null : obj.vorigeEntlassgrundID === null ? null : obj.vorigeEntlassgrundID;
		result.vorigeAbschlussartID = (obj.vorigeAbschlussartID === undefined) ? null : obj.vorigeAbschlussartID === null ? null : obj.vorigeAbschlussartID;
		result.entlassungDatum = (obj.entlassungDatum === undefined) ? null : obj.entlassungDatum === null ? null : obj.entlassungDatum;
		result.entlassungJahrgang = (obj.entlassungJahrgang === undefined) ? null : obj.entlassungJahrgang === null ? null : obj.entlassungJahrgang;
		result.entlassungGrundID = (obj.entlassungGrundID === undefined) ? null : obj.entlassungGrundID === null ? null : obj.entlassungGrundID;
		result.entlassungAbschlussartID = (obj.entlassungAbschlussartID === undefined) ? null : obj.entlassungAbschlussartID === null ? null : obj.entlassungAbschlussartID;
		result.aufnehmdendSchulnummer = (obj.aufnehmdendSchulnummer === undefined) ? null : obj.aufnehmdendSchulnummer === null ? null : obj.aufnehmdendSchulnummer;
		result.aufnehmdendWechseldatum = (obj.aufnehmdendWechseldatum === undefined) ? null : obj.aufnehmdendWechseldatum === null ? null : obj.aufnehmdendWechseldatum;
		result.aufnehmdendBestaetigt = (obj.aufnehmdendBestaetigt === undefined) ? null : obj.aufnehmdendBestaetigt === null ? null : obj.aufnehmdendBestaetigt;
		result.grundschuleEinschulungsjahr = (obj.grundschuleEinschulungsjahr === undefined) ? null : obj.grundschuleEinschulungsjahr === null ? null : obj.grundschuleEinschulungsjahr;
		result.grundschuleEinschulungsartID = (obj.grundschuleEinschulungsartID === undefined) ? null : obj.grundschuleEinschulungsartID === null ? null : obj.grundschuleEinschulungsartID;
		result.grundschuleJahreEingangsphase = (obj.grundschuleJahreEingangsphase === undefined) ? null : obj.grundschuleJahreEingangsphase === null ? null : obj.grundschuleJahreEingangsphase;
		result.grundschuleUebergangsempfehlungID = (obj.grundschuleUebergangsempfehlungID === undefined) ? null : obj.grundschuleUebergangsempfehlungID === null ? null : obj.grundschuleUebergangsempfehlungID;
		result.sekIWechsel = (obj.sekIWechsel === undefined) ? null : obj.sekIWechsel === null ? null : obj.sekIWechsel;
		result.sekIErsteSchulform = (obj.sekIErsteSchulform === undefined) ? null : obj.sekIErsteSchulform === null ? null : obj.sekIErsteSchulform;
		result.sekIIWechsel = (obj.sekIIWechsel === undefined) ? null : obj.sekIIWechsel === null ? null : obj.sekIIWechsel;
		if (obj.merkmale !== undefined) {
			for (const elem of obj.merkmale) {
				result.merkmale.add(SchuelerSchulbesuchMerkmal.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.alleSchulen !== undefined) {
			for (const elem of obj.alleSchulen) {
				result.alleSchulen.add(SchuelerSchulbesuchSchule.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchuelerSchulbesuchsdaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"vorigeSchulnummer" : ' + ((obj.vorigeSchulnummer === null) ? 'null' : JSON.stringify(obj.vorigeSchulnummer)) + ',';
		result += '"vorigeAllgHerkunft" : ' + ((obj.vorigeAllgHerkunft === null) ? 'null' : JSON.stringify(obj.vorigeAllgHerkunft)) + ',';
		result += '"vorigeEntlassdatum" : ' + ((obj.vorigeEntlassdatum === null) ? 'null' : JSON.stringify(obj.vorigeEntlassdatum)) + ',';
		result += '"vorigeEntlassjahrgang" : ' + ((obj.vorigeEntlassjahrgang === null) ? 'null' : JSON.stringify(obj.vorigeEntlassjahrgang)) + ',';
		result += '"vorigeArtLetzteVersetzung" : ' + ((obj.vorigeArtLetzteVersetzung === null) ? 'null' : JSON.stringify(obj.vorigeArtLetzteVersetzung)) + ',';
		result += '"vorigeBemerkung" : ' + ((obj.vorigeBemerkung === null) ? 'null' : JSON.stringify(obj.vorigeBemerkung)) + ',';
		result += '"vorigeEntlassgrundID" : ' + ((obj.vorigeEntlassgrundID === null) ? 'null' : obj.vorigeEntlassgrundID.toString()) + ',';
		result += '"vorigeAbschlussartID" : ' + ((obj.vorigeAbschlussartID === null) ? 'null' : JSON.stringify(obj.vorigeAbschlussartID)) + ',';
		result += '"entlassungDatum" : ' + ((obj.entlassungDatum === null) ? 'null' : JSON.stringify(obj.entlassungDatum)) + ',';
		result += '"entlassungJahrgang" : ' + ((obj.entlassungJahrgang === null) ? 'null' : JSON.stringify(obj.entlassungJahrgang)) + ',';
		result += '"entlassungGrundID" : ' + ((obj.entlassungGrundID === null) ? 'null' : obj.entlassungGrundID.toString()) + ',';
		result += '"entlassungAbschlussartID" : ' + ((obj.entlassungAbschlussartID === null) ? 'null' : JSON.stringify(obj.entlassungAbschlussartID)) + ',';
		result += '"aufnehmdendSchulnummer" : ' + ((obj.aufnehmdendSchulnummer === null) ? 'null' : JSON.stringify(obj.aufnehmdendSchulnummer)) + ',';
		result += '"aufnehmdendWechseldatum" : ' + ((obj.aufnehmdendWechseldatum === null) ? 'null' : JSON.stringify(obj.aufnehmdendWechseldatum)) + ',';
		result += '"aufnehmdendBestaetigt" : ' + ((obj.aufnehmdendBestaetigt === null) ? 'null' : obj.aufnehmdendBestaetigt.toString()) + ',';
		result += '"grundschuleEinschulungsjahr" : ' + ((obj.grundschuleEinschulungsjahr === null) ? 'null' : obj.grundschuleEinschulungsjahr.toString()) + ',';
		result += '"grundschuleEinschulungsartID" : ' + ((obj.grundschuleEinschulungsartID === null) ? 'null' : obj.grundschuleEinschulungsartID.toString()) + ',';
		result += '"grundschuleJahreEingangsphase" : ' + ((obj.grundschuleJahreEingangsphase === null) ? 'null' : obj.grundschuleJahreEingangsphase.toString()) + ',';
		result += '"grundschuleUebergangsempfehlungID" : ' + ((obj.grundschuleUebergangsempfehlungID === null) ? 'null' : obj.grundschuleUebergangsempfehlungID.toString()) + ',';
		result += '"sekIWechsel" : ' + ((obj.sekIWechsel === null) ? 'null' : obj.sekIWechsel.toString()) + ',';
		result += '"sekIErsteSchulform" : ' + ((obj.sekIErsteSchulform === null) ? 'null' : JSON.stringify(obj.sekIErsteSchulform)) + ',';
		result += '"sekIIWechsel" : ' + ((obj.sekIIWechsel === null) ? 'null' : obj.sekIIWechsel.toString()) + ',';
		result += '"merkmale" : [ ';
		for (let i = 0; i < obj.merkmale.size(); i++) {
			const elem = obj.merkmale.get(i);
			result += SchuelerSchulbesuchMerkmal.transpilerToJSON(elem);
			if (i < obj.merkmale.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"alleSchulen" : [ ';
		for (let i = 0; i < obj.alleSchulen.size(); i++) {
			const elem = obj.alleSchulen.get(i);
			result += SchuelerSchulbesuchSchule.transpilerToJSON(elem);
			if (i < obj.alleSchulen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerSchulbesuchsdaten>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.vorigeSchulnummer !== undefined) {
			result += '"vorigeSchulnummer" : ' + ((obj.vorigeSchulnummer === null) ? 'null' : JSON.stringify(obj.vorigeSchulnummer)) + ',';
		}
		if (obj.vorigeAllgHerkunft !== undefined) {
			result += '"vorigeAllgHerkunft" : ' + ((obj.vorigeAllgHerkunft === null) ? 'null' : JSON.stringify(obj.vorigeAllgHerkunft)) + ',';
		}
		if (obj.vorigeEntlassdatum !== undefined) {
			result += '"vorigeEntlassdatum" : ' + ((obj.vorigeEntlassdatum === null) ? 'null' : JSON.stringify(obj.vorigeEntlassdatum)) + ',';
		}
		if (obj.vorigeEntlassjahrgang !== undefined) {
			result += '"vorigeEntlassjahrgang" : ' + ((obj.vorigeEntlassjahrgang === null) ? 'null' : JSON.stringify(obj.vorigeEntlassjahrgang)) + ',';
		}
		if (obj.vorigeArtLetzteVersetzung !== undefined) {
			result += '"vorigeArtLetzteVersetzung" : ' + ((obj.vorigeArtLetzteVersetzung === null) ? 'null' : JSON.stringify(obj.vorigeArtLetzteVersetzung)) + ',';
		}
		if (obj.vorigeBemerkung !== undefined) {
			result += '"vorigeBemerkung" : ' + ((obj.vorigeBemerkung === null) ? 'null' : JSON.stringify(obj.vorigeBemerkung)) + ',';
		}
		if (obj.vorigeEntlassgrundID !== undefined) {
			result += '"vorigeEntlassgrundID" : ' + ((obj.vorigeEntlassgrundID === null) ? 'null' : obj.vorigeEntlassgrundID.toString()) + ',';
		}
		if (obj.vorigeAbschlussartID !== undefined) {
			result += '"vorigeAbschlussartID" : ' + ((obj.vorigeAbschlussartID === null) ? 'null' : JSON.stringify(obj.vorigeAbschlussartID)) + ',';
		}
		if (obj.entlassungDatum !== undefined) {
			result += '"entlassungDatum" : ' + ((obj.entlassungDatum === null) ? 'null' : JSON.stringify(obj.entlassungDatum)) + ',';
		}
		if (obj.entlassungJahrgang !== undefined) {
			result += '"entlassungJahrgang" : ' + ((obj.entlassungJahrgang === null) ? 'null' : JSON.stringify(obj.entlassungJahrgang)) + ',';
		}
		if (obj.entlassungGrundID !== undefined) {
			result += '"entlassungGrundID" : ' + ((obj.entlassungGrundID === null) ? 'null' : obj.entlassungGrundID.toString()) + ',';
		}
		if (obj.entlassungAbschlussartID !== undefined) {
			result += '"entlassungAbschlussartID" : ' + ((obj.entlassungAbschlussartID === null) ? 'null' : JSON.stringify(obj.entlassungAbschlussartID)) + ',';
		}
		if (obj.aufnehmdendSchulnummer !== undefined) {
			result += '"aufnehmdendSchulnummer" : ' + ((obj.aufnehmdendSchulnummer === null) ? 'null' : JSON.stringify(obj.aufnehmdendSchulnummer)) + ',';
		}
		if (obj.aufnehmdendWechseldatum !== undefined) {
			result += '"aufnehmdendWechseldatum" : ' + ((obj.aufnehmdendWechseldatum === null) ? 'null' : JSON.stringify(obj.aufnehmdendWechseldatum)) + ',';
		}
		if (obj.aufnehmdendBestaetigt !== undefined) {
			result += '"aufnehmdendBestaetigt" : ' + ((obj.aufnehmdendBestaetigt === null) ? 'null' : obj.aufnehmdendBestaetigt.toString()) + ',';
		}
		if (obj.grundschuleEinschulungsjahr !== undefined) {
			result += '"grundschuleEinschulungsjahr" : ' + ((obj.grundschuleEinschulungsjahr === null) ? 'null' : obj.grundschuleEinschulungsjahr.toString()) + ',';
		}
		if (obj.grundschuleEinschulungsartID !== undefined) {
			result += '"grundschuleEinschulungsartID" : ' + ((obj.grundschuleEinschulungsartID === null) ? 'null' : obj.grundschuleEinschulungsartID.toString()) + ',';
		}
		if (obj.grundschuleJahreEingangsphase !== undefined) {
			result += '"grundschuleJahreEingangsphase" : ' + ((obj.grundschuleJahreEingangsphase === null) ? 'null' : obj.grundschuleJahreEingangsphase.toString()) + ',';
		}
		if (obj.grundschuleUebergangsempfehlungID !== undefined) {
			result += '"grundschuleUebergangsempfehlungID" : ' + ((obj.grundschuleUebergangsempfehlungID === null) ? 'null' : obj.grundschuleUebergangsempfehlungID.toString()) + ',';
		}
		if (obj.sekIWechsel !== undefined) {
			result += '"sekIWechsel" : ' + ((obj.sekIWechsel === null) ? 'null' : obj.sekIWechsel.toString()) + ',';
		}
		if (obj.sekIErsteSchulform !== undefined) {
			result += '"sekIErsteSchulform" : ' + ((obj.sekIErsteSchulform === null) ? 'null' : JSON.stringify(obj.sekIErsteSchulform)) + ',';
		}
		if (obj.sekIIWechsel !== undefined) {
			result += '"sekIIWechsel" : ' + ((obj.sekIIWechsel === null) ? 'null' : obj.sekIIWechsel.toString()) + ',';
		}
		if (obj.merkmale !== undefined) {
			result += '"merkmale" : [ ';
			for (let i = 0; i < obj.merkmale.size(); i++) {
				const elem = obj.merkmale.get(i);
				result += SchuelerSchulbesuchMerkmal.transpilerToJSON(elem);
				if (i < obj.merkmale.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.alleSchulen !== undefined) {
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

}

export function cast_de_svws_nrw_core_data_schueler_SchuelerSchulbesuchsdaten(obj : unknown) : SchuelerSchulbesuchsdaten {
	return obj as SchuelerSchulbesuchsdaten;
}
