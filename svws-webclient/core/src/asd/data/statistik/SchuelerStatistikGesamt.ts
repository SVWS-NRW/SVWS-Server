import { JavaObject } from '../../../java/lang/JavaObject';
import { AbiturStatistikGesamt } from '../../../asd/data/statistik/AbiturStatistikGesamt';
import { ArrayList } from '../../../java/util/ArrayList';
import { SchuelerLernabschnittStatistikGesamt } from '../../../asd/data/statistik/SchuelerLernabschnittStatistikGesamt';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class SchuelerStatistikGesamt extends JavaObject {

	/**
	 * Die ID des Schülerdatensatzes.
	 */
	public id: number = 0;

	/**
	 * Die ID des Geschlechtes
	 */
	public geschlecht: number = 0;

	/**
	 * Das Geburtsdatum des Schülerdatensatzes.
	 */
	public geburtsdatum: string | null = null;

	/**
	 * Die ID des Wohnortes des Schülerdatensatzes.
	 */
	public wohnortID: number | null = null;

	/**
	 * Die ID der Staatsangehörigkeit des Schülerdatensatzes.
	 */
	public staatsangehoerigkeitID: string | null = null;

	/**
	 * Die ID der Religion des Schülerdatensatzes.
	 */
	public religionID: number | null = null;

	/**
	 * Die ID des Status des Schülerdatensatzes.
	 */
	public status: number = 0;

	/**
	 * Das Datum der Religionsabmeldung des Schülerdatensatzes.
	 */
	public religionabmeldung: string | null = null;

	/**
	 * Das Datum der Religionsanmeldung des Schülerdatensatzes.
	 */
	public religionanmeldung: string | null = null;

	/**
	 * Die Anrechungszeit in Monaten für den Beginn des Bildungsganges des Berufskolleg (z.B. 0,6,12,18).
	 */
	public bkAvzo: number | null = null;

	/**
	 * Gibt an, ob ein Migrationshintergrund bei dems Schülerdatensatz vorhanden ist.
	 */
	public hatMigrationshintergrund: boolean = false;

	/**
	 * Das Zuzugsjahr des Schülerdatensatzes.
	 */
	public zuzugsjahr: number | null = null;

	/**
	 * Das Geburtsland des Schülerdatensatzes.
	 */
	public geburtsland: string | null = null;

	/**
	 * Die Verkehrssprache der Familie des Schülerdatensatzes.
	 */
	public verkehrspracheFamilie: string | null = null;

	/**
	 * Das Geburtsland des Vaters des Schülerdatensatzes.
	 */
	public geburtslandVater: string | null = null;

	/**
	 * Das Geburtsland der Mutter des Schülerdatensatzes.
	 */
	public geburtslandMutter: string | null = null;

	/**
	 * Die allgemeinen Angaben zu den Lernabschnitten der Schüler.
	 */
	public lernabschnitte: List<SchuelerLernabschnittStatistikGesamt> = new ArrayList<SchuelerLernabschnittStatistikGesamt>();

	/**
	 * Die Schulnr der vorher besuchten Schule.
	 */
	public vorherigeSchuleNr: string | null = null;

	/**
	 * Die allgemeine Herkunftsart des Schüler in Bezug auf die schulform der zuvor besuchten Schule.
	 */
	public vorigeAllgHerkunft: string | null = null;

	/**
	 * Die ID der Art der letzten Versetzung an der zuvor besuchten Schule.
	 */
	public vorigeArtLetzteVersetzung: string | null = null;

	/**
	 * Die ID des Abschlusses, welcher an der zuvor besuchten Schule erworben wurde.
	 */
	public idVorigeAbschlussart: string | null = null;

	/**
	 * Das Entlassdatum an der zuvor besuchten Schule.
	 */
	public vorigeEntlassdatum: string | null = null;

	/**
	 * Der Entlassjahrgang an der zuvor besuchten Schule.
	 */
	public vorigeEntlassjahrgang: string | null = null;

	/**
	 * Das Entlassdatum von dieser Schule.
	 */
	public entlassungDatum: string | null = null;

	/**
	 * Die ID des Abschlusses, welcher an dieser Schule erworben wurde.
	 */
	public idEntlassungAbschlussart: string | null = null;

	/**
	 * Ist Schüler einer Justizvollzugsanstalt.
	 */
	public istJvaSchueler: boolean = false;

	/**
	 * Die ID der Einschulungsart in die Grundschule.
	 */
	public idGrundschuleEinschulungsart: number | null = null;

	/**
	 * Das Kürzel für die Übergangsempfehlung der Grundschule in die Sekundarstufe I
	 */
	public kuerzelGrundschuleUebergangsempfehlung: string | null = null;

	/**
	 * Die Daten zum Abitur (sofern vorhanden).
	 */
	public abitur: AbiturStatistikGesamt | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.statistik.SchuelerStatistikGesamt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.statistik.SchuelerStatistikGesamt'].includes(name);
	}

	public static readonly class = new Class<SchuelerStatistikGesamt>('de.svws_nrw.asd.data.statistik.SchuelerStatistikGesamt');

	public static transpilerFromJSON(json: string): SchuelerStatistikGesamt {
		const obj = JSON.parse(json) as Partial<SchuelerStatistikGesamt>;
		const result = new SchuelerStatistikGesamt();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.geschlecht === undefined)
			throw new Error('invalid json format, missing attribute geschlecht');
		result.geschlecht = obj.geschlecht;
		result.geburtsdatum = (obj.geburtsdatum === undefined) ? null : obj.geburtsdatum === null ? null : obj.geburtsdatum;
		result.wohnortID = (obj.wohnortID === undefined) ? null : obj.wohnortID === null ? null : obj.wohnortID;
		result.staatsangehoerigkeitID = (obj.staatsangehoerigkeitID === undefined) ? null : obj.staatsangehoerigkeitID === null ? null : obj.staatsangehoerigkeitID;
		result.religionID = (obj.religionID === undefined) ? null : obj.religionID === null ? null : obj.religionID;
		if (obj.status === undefined)
			throw new Error('invalid json format, missing attribute status');
		result.status = obj.status;
		result.religionabmeldung = (obj.religionabmeldung === undefined) ? null : obj.religionabmeldung === null ? null : obj.religionabmeldung;
		result.religionanmeldung = (obj.religionanmeldung === undefined) ? null : obj.religionanmeldung === null ? null : obj.religionanmeldung;
		result.bkAvzo = (obj.bkAvzo === undefined) ? null : obj.bkAvzo === null ? null : obj.bkAvzo;
		if (obj.hatMigrationshintergrund === undefined)
			throw new Error('invalid json format, missing attribute hatMigrationshintergrund');
		result.hatMigrationshintergrund = obj.hatMigrationshintergrund;
		result.zuzugsjahr = (obj.zuzugsjahr === undefined) ? null : obj.zuzugsjahr === null ? null : obj.zuzugsjahr;
		result.geburtsland = (obj.geburtsland === undefined) ? null : obj.geburtsland === null ? null : obj.geburtsland;
		result.verkehrspracheFamilie = (obj.verkehrspracheFamilie === undefined) ? null : obj.verkehrspracheFamilie === null ? null : obj.verkehrspracheFamilie;
		result.geburtslandVater = (obj.geburtslandVater === undefined) ? null : obj.geburtslandVater === null ? null : obj.geburtslandVater;
		result.geburtslandMutter = (obj.geburtslandMutter === undefined) ? null : obj.geburtslandMutter === null ? null : obj.geburtslandMutter;
		if (obj.lernabschnitte !== undefined) {
			for (const elem of obj.lernabschnitte) {
				result.lernabschnitte.add(SchuelerLernabschnittStatistikGesamt.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		result.vorherigeSchuleNr = (obj.vorherigeSchuleNr === undefined) ? null : obj.vorherigeSchuleNr === null ? null : obj.vorherigeSchuleNr;
		result.vorigeAllgHerkunft = (obj.vorigeAllgHerkunft === undefined) ? null : obj.vorigeAllgHerkunft === null ? null : obj.vorigeAllgHerkunft;
		result.vorigeArtLetzteVersetzung = (obj.vorigeArtLetzteVersetzung === undefined) ? null : obj.vorigeArtLetzteVersetzung === null ? null : obj.vorigeArtLetzteVersetzung;
		result.idVorigeAbschlussart = (obj.idVorigeAbschlussart === undefined) ? null : obj.idVorigeAbschlussart === null ? null : obj.idVorigeAbschlussart;
		result.vorigeEntlassdatum = (obj.vorigeEntlassdatum === undefined) ? null : obj.vorigeEntlassdatum === null ? null : obj.vorigeEntlassdatum;
		result.vorigeEntlassjahrgang = (obj.vorigeEntlassjahrgang === undefined) ? null : obj.vorigeEntlassjahrgang === null ? null : obj.vorigeEntlassjahrgang;
		result.entlassungDatum = (obj.entlassungDatum === undefined) ? null : obj.entlassungDatum === null ? null : obj.entlassungDatum;
		result.idEntlassungAbschlussart = (obj.idEntlassungAbschlussart === undefined) ? null : obj.idEntlassungAbschlussart === null ? null : obj.idEntlassungAbschlussart;
		if (obj.istJvaSchueler === undefined)
			throw new Error('invalid json format, missing attribute istJvaSchueler');
		result.istJvaSchueler = obj.istJvaSchueler;
		result.idGrundschuleEinschulungsart = (obj.idGrundschuleEinschulungsart === undefined) ? null : obj.idGrundschuleEinschulungsart === null ? null : obj.idGrundschuleEinschulungsart;
		result.kuerzelGrundschuleUebergangsempfehlung = (obj.kuerzelGrundschuleUebergangsempfehlung === undefined) ? null : obj.kuerzelGrundschuleUebergangsempfehlung === null ? null : obj.kuerzelGrundschuleUebergangsempfehlung;
		result.abitur = ((obj.abitur === undefined) || (obj.abitur === null)) ? null : AbiturStatistikGesamt.transpilerFromJSON(JSON.stringify(obj.abitur));
		return result;
	}

	public static transpilerToJSON(obj: SchuelerStatistikGesamt): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"geschlecht" : ' + obj.geschlecht.toString() + ',';
		result += '"geburtsdatum" : ' + ((obj.geburtsdatum === null) ? 'null' : JSON.stringify(obj.geburtsdatum)) + ',';
		result += '"wohnortID" : ' + ((obj.wohnortID === null) ? 'null' : obj.wohnortID.toString()) + ',';
		result += '"staatsangehoerigkeitID" : ' + ((obj.staatsangehoerigkeitID === null) ? 'null' : JSON.stringify(obj.staatsangehoerigkeitID)) + ',';
		result += '"religionID" : ' + ((obj.religionID === null) ? 'null' : obj.religionID.toString()) + ',';
		result += '"status" : ' + obj.status.toString() + ',';
		result += '"religionabmeldung" : ' + ((obj.religionabmeldung === null) ? 'null' : JSON.stringify(obj.religionabmeldung)) + ',';
		result += '"religionanmeldung" : ' + ((obj.religionanmeldung === null) ? 'null' : JSON.stringify(obj.religionanmeldung)) + ',';
		result += '"bkAvzo" : ' + ((obj.bkAvzo === null) ? 'null' : obj.bkAvzo.toString()) + ',';
		result += '"hatMigrationshintergrund" : ' + obj.hatMigrationshintergrund.toString() + ',';
		result += '"zuzugsjahr" : ' + ((obj.zuzugsjahr === null) ? 'null' : obj.zuzugsjahr.toString()) + ',';
		result += '"geburtsland" : ' + ((obj.geburtsland === null) ? 'null' : JSON.stringify(obj.geburtsland)) + ',';
		result += '"verkehrspracheFamilie" : ' + ((obj.verkehrspracheFamilie === null) ? 'null' : JSON.stringify(obj.verkehrspracheFamilie)) + ',';
		result += '"geburtslandVater" : ' + ((obj.geburtslandVater === null) ? 'null' : JSON.stringify(obj.geburtslandVater)) + ',';
		result += '"geburtslandMutter" : ' + ((obj.geburtslandMutter === null) ? 'null' : JSON.stringify(obj.geburtslandMutter)) + ',';
		result += '"lernabschnitte" : [ ';
		for (let i = 0; i < obj.lernabschnitte.size(); i++) {
			const elem = obj.lernabschnitte.get(i);
			result += SchuelerLernabschnittStatistikGesamt.transpilerToJSON(elem);
			if (i < obj.lernabschnitte.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"vorherigeSchuleNr" : ' + ((obj.vorherigeSchuleNr === null) ? 'null' : JSON.stringify(obj.vorherigeSchuleNr)) + ',';
		result += '"vorigeAllgHerkunft" : ' + ((obj.vorigeAllgHerkunft === null) ? 'null' : JSON.stringify(obj.vorigeAllgHerkunft)) + ',';
		result += '"vorigeArtLetzteVersetzung" : ' + ((obj.vorigeArtLetzteVersetzung === null) ? 'null' : JSON.stringify(obj.vorigeArtLetzteVersetzung)) + ',';
		result += '"idVorigeAbschlussart" : ' + ((obj.idVorigeAbschlussart === null) ? 'null' : JSON.stringify(obj.idVorigeAbschlussart)) + ',';
		result += '"vorigeEntlassdatum" : ' + ((obj.vorigeEntlassdatum === null) ? 'null' : JSON.stringify(obj.vorigeEntlassdatum)) + ',';
		result += '"vorigeEntlassjahrgang" : ' + ((obj.vorigeEntlassjahrgang === null) ? 'null' : JSON.stringify(obj.vorigeEntlassjahrgang)) + ',';
		result += '"entlassungDatum" : ' + ((obj.entlassungDatum === null) ? 'null' : JSON.stringify(obj.entlassungDatum)) + ',';
		result += '"idEntlassungAbschlussart" : ' + ((obj.idEntlassungAbschlussart === null) ? 'null' : JSON.stringify(obj.idEntlassungAbschlussart)) + ',';
		result += '"istJvaSchueler" : ' + obj.istJvaSchueler.toString() + ',';
		result += '"idGrundschuleEinschulungsart" : ' + ((obj.idGrundschuleEinschulungsart === null) ? 'null' : obj.idGrundschuleEinschulungsart.toString()) + ',';
		result += '"kuerzelGrundschuleUebergangsempfehlung" : ' + ((obj.kuerzelGrundschuleUebergangsempfehlung === null) ? 'null' : JSON.stringify(obj.kuerzelGrundschuleUebergangsempfehlung)) + ',';
		result += '"abitur" : ' + ((obj.abitur === null) ? 'null' : AbiturStatistikGesamt.transpilerToJSON(obj.abitur)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchuelerStatistikGesamt>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.geschlecht !== undefined) {
			result += '"geschlecht" : ' + obj.geschlecht.toString() + ',';
		}
		if (obj.geburtsdatum !== undefined) {
			result += '"geburtsdatum" : ' + ((obj.geburtsdatum === null) ? 'null' : JSON.stringify(obj.geburtsdatum)) + ',';
		}
		if (obj.wohnortID !== undefined) {
			result += '"wohnortID" : ' + ((obj.wohnortID === null) ? 'null' : obj.wohnortID.toString()) + ',';
		}
		if (obj.staatsangehoerigkeitID !== undefined) {
			result += '"staatsangehoerigkeitID" : ' + ((obj.staatsangehoerigkeitID === null) ? 'null' : JSON.stringify(obj.staatsangehoerigkeitID)) + ',';
		}
		if (obj.religionID !== undefined) {
			result += '"religionID" : ' + ((obj.religionID === null) ? 'null' : obj.religionID.toString()) + ',';
		}
		if (obj.status !== undefined) {
			result += '"status" : ' + obj.status.toString() + ',';
		}
		if (obj.religionabmeldung !== undefined) {
			result += '"religionabmeldung" : ' + ((obj.religionabmeldung === null) ? 'null' : JSON.stringify(obj.religionabmeldung)) + ',';
		}
		if (obj.religionanmeldung !== undefined) {
			result += '"religionanmeldung" : ' + ((obj.religionanmeldung === null) ? 'null' : JSON.stringify(obj.religionanmeldung)) + ',';
		}
		if (obj.bkAvzo !== undefined) {
			result += '"bkAvzo" : ' + ((obj.bkAvzo === null) ? 'null' : obj.bkAvzo.toString()) + ',';
		}
		if (obj.hatMigrationshintergrund !== undefined) {
			result += '"hatMigrationshintergrund" : ' + obj.hatMigrationshintergrund.toString() + ',';
		}
		if (obj.zuzugsjahr !== undefined) {
			result += '"zuzugsjahr" : ' + ((obj.zuzugsjahr === null) ? 'null' : obj.zuzugsjahr.toString()) + ',';
		}
		if (obj.geburtsland !== undefined) {
			result += '"geburtsland" : ' + ((obj.geburtsland === null) ? 'null' : JSON.stringify(obj.geburtsland)) + ',';
		}
		if (obj.verkehrspracheFamilie !== undefined) {
			result += '"verkehrspracheFamilie" : ' + ((obj.verkehrspracheFamilie === null) ? 'null' : JSON.stringify(obj.verkehrspracheFamilie)) + ',';
		}
		if (obj.geburtslandVater !== undefined) {
			result += '"geburtslandVater" : ' + ((obj.geburtslandVater === null) ? 'null' : JSON.stringify(obj.geburtslandVater)) + ',';
		}
		if (obj.geburtslandMutter !== undefined) {
			result += '"geburtslandMutter" : ' + ((obj.geburtslandMutter === null) ? 'null' : JSON.stringify(obj.geburtslandMutter)) + ',';
		}
		if (obj.lernabschnitte !== undefined) {
			result += '"lernabschnitte" : [ ';
			for (let i = 0; i < obj.lernabschnitte.size(); i++) {
				const elem = obj.lernabschnitte.get(i);
				result += SchuelerLernabschnittStatistikGesamt.transpilerToJSON(elem);
				if (i < obj.lernabschnitte.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.vorherigeSchuleNr !== undefined) {
			result += '"vorherigeSchuleNr" : ' + ((obj.vorherigeSchuleNr === null) ? 'null' : JSON.stringify(obj.vorherigeSchuleNr)) + ',';
		}
		if (obj.vorigeAllgHerkunft !== undefined) {
			result += '"vorigeAllgHerkunft" : ' + ((obj.vorigeAllgHerkunft === null) ? 'null' : JSON.stringify(obj.vorigeAllgHerkunft)) + ',';
		}
		if (obj.vorigeArtLetzteVersetzung !== undefined) {
			result += '"vorigeArtLetzteVersetzung" : ' + ((obj.vorigeArtLetzteVersetzung === null) ? 'null' : JSON.stringify(obj.vorigeArtLetzteVersetzung)) + ',';
		}
		if (obj.idVorigeAbschlussart !== undefined) {
			result += '"idVorigeAbschlussart" : ' + ((obj.idVorigeAbschlussart === null) ? 'null' : JSON.stringify(obj.idVorigeAbschlussart)) + ',';
		}
		if (obj.vorigeEntlassdatum !== undefined) {
			result += '"vorigeEntlassdatum" : ' + ((obj.vorigeEntlassdatum === null) ? 'null' : JSON.stringify(obj.vorigeEntlassdatum)) + ',';
		}
		if (obj.vorigeEntlassjahrgang !== undefined) {
			result += '"vorigeEntlassjahrgang" : ' + ((obj.vorigeEntlassjahrgang === null) ? 'null' : JSON.stringify(obj.vorigeEntlassjahrgang)) + ',';
		}
		if (obj.entlassungDatum !== undefined) {
			result += '"entlassungDatum" : ' + ((obj.entlassungDatum === null) ? 'null' : JSON.stringify(obj.entlassungDatum)) + ',';
		}
		if (obj.idEntlassungAbschlussart !== undefined) {
			result += '"idEntlassungAbschlussart" : ' + ((obj.idEntlassungAbschlussart === null) ? 'null' : JSON.stringify(obj.idEntlassungAbschlussart)) + ',';
		}
		if (obj.istJvaSchueler !== undefined) {
			result += '"istJvaSchueler" : ' + obj.istJvaSchueler.toString() + ',';
		}
		if (obj.idGrundschuleEinschulungsart !== undefined) {
			result += '"idGrundschuleEinschulungsart" : ' + ((obj.idGrundschuleEinschulungsart === null) ? 'null' : obj.idGrundschuleEinschulungsart.toString()) + ',';
		}
		if (obj.kuerzelGrundschuleUebergangsempfehlung !== undefined) {
			result += '"kuerzelGrundschuleUebergangsempfehlung" : ' + ((obj.kuerzelGrundschuleUebergangsempfehlung === null) ? 'null' : JSON.stringify(obj.kuerzelGrundschuleUebergangsempfehlung)) + ',';
		}
		if (obj.abitur !== undefined) {
			result += '"abitur" : ' + ((obj.abitur === null) ? 'null' : AbiturStatistikGesamt.transpilerToJSON(obj.abitur)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_statistik_SchuelerStatistikGesamt(obj: unknown): SchuelerStatistikGesamt {
	return obj as SchuelerStatistikGesamt;
}
