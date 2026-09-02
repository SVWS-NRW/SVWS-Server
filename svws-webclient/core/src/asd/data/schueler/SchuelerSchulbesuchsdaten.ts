import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuelerSchulbesuchSchule } from '../../../asd/data/schueler/SchuelerSchulbesuchSchule';
import { SchuelerSchulbesuchMerkmal } from '../../../asd/data/schueler/SchuelerSchulbesuchMerkmal';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class SchuelerSchulbesuchsdaten extends JavaObject {

	/**
	 * Die ID des Schulbesuchdatensatzes.
	 */
	public id: number = 0;

	/**
	 * Der Schlüssel des höchsten Schulabschlusses.
	 */
	public schluesselHoechsterSchulabschluss: string | null = null;

	/**
	 * Gibt an, ob ein Berufsabschluss vorhanden ist.
	 */
	public berufsabschlussVorhanden: boolean = false;

	/**
	 * Die ID der zuvor besuchten Schule.
	 */
	public idVorherigeSchule: number | null = null;

	/**
	 * Die ID der HerkunftSonstige (falls zuvor besuchte Schule = kein Schulbesuch).
	 */
	public idHerkunftSonstigeVorherigeSchule: number | null = null;

	/**
	 * Das Entlassdatum an der zuvor besuchten Schule.
	 */
	public entlassdatumVorherigeSchule: string | null = null;

	/**
	 * Das Kürzel des Entlassjahrgangs an der zuvor besuchten Schule.
	 */
	public kuerzelEntlassjahrgangVorherigeSchule: string | null = null;

	/**
	 * Die ID der Herkunftsart der Versetzung an der zuvor besuchten Schule.
	 */
	public idHerkunftsartVersetzungVorherigeSchule: string | null = null;

	/**
	 * Bemerkungen zur zuvor besuchten Schule.
	 */
	public bemerkungVorherigeSchule: string | null = null;

	/**
	 * Die ID des Entlassgrundes der zuvor besuchten Schule.
	 */
	public idEntlassgrundVorherigeSchule: number | null = null;

	/**
	 * Der Schlüssel des Schulabschlusses (Allgemeinbildend) der zuvor besuchten Schule.
	 */
	public schluesselAbschlussartAllgemeinbildendVorherigeSchule: string | null = null;

	/**
	 * Der Schlüssel des Schulabschlusses (Berufsbildend) der zuvor besuchten Schule.
	 */
	public schluesselAbschlussartBerufsbildendVorherigeSchule: string | null = null;

	/**
	 * Die ID der Schulgliederung aus Herkunftbildungsgang.json (BK/SB) der zuvor besuchten Schule.
	 */
	public idSchulgliederungVorherigeSchule: number | null = null;

	/**
	 * Der Schlüssel des CoreTypes der Fachklasse der zuvor besuchten Schule (BK/SB).
	 */
	public schluesselCoreTypeFachklasseVorherigeSchule: string | null = null;

	/**
	 * Die ID des Hochschulabschlusses aus Hochschulabschluss.json (BK/SB/WB).
	 */
	public idHochschulabschluss: number | null = null;

	/**
	 * Das Entlassdatum von dieser Schule.
	 */
	public entlassdatumDieseSchule: string | null = null;

	/**
	 * Die Id des Jahrgangs bei der Entlassung von dieser Schule.
	 */
	public idEntlassjahrgangDieseSchule: number | null = null;

	/**
	 * Die ID des Entlassgrundes von dieser Schule.
	 */
	public idEntlassgrundDieseSchule: number | null = null;

	/**
	 * Die ID der Abschlussart, welcher an dieser Schule erworben wurde.
	 */
	public idAbschlussartDieseSchule: string | null = null;

	/**
	 * Die ID der aufnehmenden Schule.
	 */
	public idAufnehmendeSchule: number | null = null;

	/**
	 * Das Datum beim Wechsel zu einer aufnehmenden Schule.
	 */
	public wechseldatumAufnehmendeSchule: string | null = null;

	/**
	 * Gibt an, ob die aufnehmende Schule den Wechsel bestätigt hat.
	 */
	public wechselBestaetigtAufnehmendeSchule: boolean = false;

	/**
	 * Das Jahr der Einschulung in die Grundschule.
	 */
	public einschulungsjahrGrundschule: number | null = null;

	/**
	 * Die ID der Einschulungsart in die Grundschule.
	 */
	public idEinschulungsartGrundschule: number | null = null;

	/**
	 * Die ID der Schuleingangsphase der Grundschule.
	 */
	public idEingangsphaseGrundschule: number | null = null;

	/**
	 * Die ID der Übergangsempfehlung der Grundschule in die Sekundarstufe I.
	 */
	public idUebergangsempfehlungGrundschule: number | null = null;

	/**
	 * Das Jahr des Wechsels in die Sekundarstufe I.
	 */
	public wechseljahrSekI: number | null = null;

	/**
	 * Das Kürzel der ersten Schulform in der Sekundarstufe I
	 */
	public kuerzelErsteSchulformSek1: string | null = null;

	/**
	 * Das Jahr des Wechsels in die Sekundarstufe II.
	 */
	public wechseljahrSekII: number | null = null;

	/**
	 * Die ID der Dauer des Kindergartenbesuchs eines Schülers.
	 */
	public idDauerKindergartenbesuch: number | null = null;

	/**
	 * Die ID des Kindergartens.
	 */
	public idKindergarten: number | null = null;

	/**
	 * Schüler wurde zu einem Sprachförderkurs verpflichtet (Ja/Nein).
	 */
	public verpflichtungSprachfoerderkurs: boolean = false;

	/**
	 * Teilnahme des Schülers an einem Sprachförderkurs (Ja/Nein).
	 */
	public teilnahmeSprachfoerderkurs: boolean = false;

	/**
	 * Die Informationen zu den besonderen Merkmalen für die Statistik.
	 */
	public merkmale: List<SchuelerSchulbesuchMerkmal> = new ArrayList<SchuelerSchulbesuchMerkmal>();

	/**
	 * Die Informationen zu allen bisher besuchten Schulen.
	 */
	public bisherBesuchteSchulen: List<SchuelerSchulbesuchSchule> = new ArrayList<SchuelerSchulbesuchSchule>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten'].includes(name);
	}

	public static readonly class = new Class<SchuelerSchulbesuchsdaten>('de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten');

	public static transpilerFromJSON(json: string): SchuelerSchulbesuchsdaten {
		const obj = JSON.parse(json) as Partial<SchuelerSchulbesuchsdaten>;
		const result = new SchuelerSchulbesuchsdaten();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.schluesselHoechsterSchulabschluss = (obj.schluesselHoechsterSchulabschluss === undefined) ? null : obj.schluesselHoechsterSchulabschluss === null ? null : obj.schluesselHoechsterSchulabschluss;
		if (obj.berufsabschlussVorhanden === undefined)
			throw new Error('invalid json format, missing attribute berufsabschlussVorhanden');
		result.berufsabschlussVorhanden = obj.berufsabschlussVorhanden;
		result.idVorherigeSchule = (obj.idVorherigeSchule === undefined) ? null : obj.idVorherigeSchule === null ? null : obj.idVorherigeSchule;
		result.idHerkunftSonstigeVorherigeSchule = (obj.idHerkunftSonstigeVorherigeSchule === undefined) ? null : obj.idHerkunftSonstigeVorherigeSchule === null ? null : obj.idHerkunftSonstigeVorherigeSchule;
		result.entlassdatumVorherigeSchule = (obj.entlassdatumVorherigeSchule === undefined) ? null : obj.entlassdatumVorherigeSchule === null ? null : obj.entlassdatumVorherigeSchule;
		result.kuerzelEntlassjahrgangVorherigeSchule = (obj.kuerzelEntlassjahrgangVorherigeSchule === undefined) ? null : obj.kuerzelEntlassjahrgangVorherigeSchule === null ? null : obj.kuerzelEntlassjahrgangVorherigeSchule;
		result.idHerkunftsartVersetzungVorherigeSchule = (obj.idHerkunftsartVersetzungVorherigeSchule === undefined) ? null : obj.idHerkunftsartVersetzungVorherigeSchule === null ? null : obj.idHerkunftsartVersetzungVorherigeSchule;
		result.bemerkungVorherigeSchule = (obj.bemerkungVorherigeSchule === undefined) ? null : obj.bemerkungVorherigeSchule === null ? null : obj.bemerkungVorherigeSchule;
		result.idEntlassgrundVorherigeSchule = (obj.idEntlassgrundVorherigeSchule === undefined) ? null : obj.idEntlassgrundVorherigeSchule === null ? null : obj.idEntlassgrundVorherigeSchule;
		result.schluesselAbschlussartAllgemeinbildendVorherigeSchule = (obj.schluesselAbschlussartAllgemeinbildendVorherigeSchule === undefined) ? null : obj.schluesselAbschlussartAllgemeinbildendVorherigeSchule === null ? null : obj.schluesselAbschlussartAllgemeinbildendVorherigeSchule;
		result.schluesselAbschlussartBerufsbildendVorherigeSchule = (obj.schluesselAbschlussartBerufsbildendVorherigeSchule === undefined) ? null : obj.schluesselAbschlussartBerufsbildendVorherigeSchule === null ? null : obj.schluesselAbschlussartBerufsbildendVorherigeSchule;
		result.idSchulgliederungVorherigeSchule = (obj.idSchulgliederungVorherigeSchule === undefined) ? null : obj.idSchulgliederungVorherigeSchule === null ? null : obj.idSchulgliederungVorherigeSchule;
		result.schluesselCoreTypeFachklasseVorherigeSchule = (obj.schluesselCoreTypeFachklasseVorherigeSchule === undefined) ? null : obj.schluesselCoreTypeFachklasseVorherigeSchule === null ? null : obj.schluesselCoreTypeFachklasseVorherigeSchule;
		result.idHochschulabschluss = (obj.idHochschulabschluss === undefined) ? null : obj.idHochschulabschluss === null ? null : obj.idHochschulabschluss;
		result.entlassdatumDieseSchule = (obj.entlassdatumDieseSchule === undefined) ? null : obj.entlassdatumDieseSchule === null ? null : obj.entlassdatumDieseSchule;
		result.idEntlassjahrgangDieseSchule = (obj.idEntlassjahrgangDieseSchule === undefined) ? null : obj.idEntlassjahrgangDieseSchule === null ? null : obj.idEntlassjahrgangDieseSchule;
		result.idEntlassgrundDieseSchule = (obj.idEntlassgrundDieseSchule === undefined) ? null : obj.idEntlassgrundDieseSchule === null ? null : obj.idEntlassgrundDieseSchule;
		result.idAbschlussartDieseSchule = (obj.idAbschlussartDieseSchule === undefined) ? null : obj.idAbschlussartDieseSchule === null ? null : obj.idAbschlussartDieseSchule;
		result.idAufnehmendeSchule = (obj.idAufnehmendeSchule === undefined) ? null : obj.idAufnehmendeSchule === null ? null : obj.idAufnehmendeSchule;
		result.wechseldatumAufnehmendeSchule = (obj.wechseldatumAufnehmendeSchule === undefined) ? null : obj.wechseldatumAufnehmendeSchule === null ? null : obj.wechseldatumAufnehmendeSchule;
		if (obj.wechselBestaetigtAufnehmendeSchule === undefined)
			throw new Error('invalid json format, missing attribute wechselBestaetigtAufnehmendeSchule');
		result.wechselBestaetigtAufnehmendeSchule = obj.wechselBestaetigtAufnehmendeSchule;
		result.einschulungsjahrGrundschule = (obj.einschulungsjahrGrundschule === undefined) ? null : obj.einschulungsjahrGrundschule === null ? null : obj.einschulungsjahrGrundschule;
		result.idEinschulungsartGrundschule = (obj.idEinschulungsartGrundschule === undefined) ? null : obj.idEinschulungsartGrundschule === null ? null : obj.idEinschulungsartGrundschule;
		result.idEingangsphaseGrundschule = (obj.idEingangsphaseGrundschule === undefined) ? null : obj.idEingangsphaseGrundschule === null ? null : obj.idEingangsphaseGrundschule;
		result.idUebergangsempfehlungGrundschule = (obj.idUebergangsempfehlungGrundschule === undefined) ? null : obj.idUebergangsempfehlungGrundschule === null ? null : obj.idUebergangsempfehlungGrundschule;
		result.wechseljahrSekI = (obj.wechseljahrSekI === undefined) ? null : obj.wechseljahrSekI === null ? null : obj.wechseljahrSekI;
		result.kuerzelErsteSchulformSek1 = (obj.kuerzelErsteSchulformSek1 === undefined) ? null : obj.kuerzelErsteSchulformSek1 === null ? null : obj.kuerzelErsteSchulformSek1;
		result.wechseljahrSekII = (obj.wechseljahrSekII === undefined) ? null : obj.wechseljahrSekII === null ? null : obj.wechseljahrSekII;
		result.idDauerKindergartenbesuch = (obj.idDauerKindergartenbesuch === undefined) ? null : obj.idDauerKindergartenbesuch === null ? null : obj.idDauerKindergartenbesuch;
		result.idKindergarten = (obj.idKindergarten === undefined) ? null : obj.idKindergarten === null ? null : obj.idKindergarten;
		if (obj.verpflichtungSprachfoerderkurs === undefined)
			throw new Error('invalid json format, missing attribute verpflichtungSprachfoerderkurs');
		result.verpflichtungSprachfoerderkurs = obj.verpflichtungSprachfoerderkurs;
		if (obj.teilnahmeSprachfoerderkurs === undefined)
			throw new Error('invalid json format, missing attribute teilnahmeSprachfoerderkurs');
		result.teilnahmeSprachfoerderkurs = obj.teilnahmeSprachfoerderkurs;
		if (obj.merkmale !== undefined) {
			for (const elem of obj.merkmale) {
				result.merkmale.add(SchuelerSchulbesuchMerkmal.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.bisherBesuchteSchulen !== undefined) {
			for (const elem of obj.bisherBesuchteSchulen) {
				result.bisherBesuchteSchulen.add(SchuelerSchulbesuchSchule.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: SchuelerSchulbesuchsdaten): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schluesselHoechsterSchulabschluss" : ' + ((obj.schluesselHoechsterSchulabschluss === null) ? 'null' : JSON.stringify(obj.schluesselHoechsterSchulabschluss)) + ',';
		result += '"berufsabschlussVorhanden" : ' + obj.berufsabschlussVorhanden.toString() + ',';
		result += '"idVorherigeSchule" : ' + ((obj.idVorherigeSchule === null) ? 'null' : obj.idVorherigeSchule.toString()) + ',';
		result += '"idHerkunftSonstigeVorherigeSchule" : ' + ((obj.idHerkunftSonstigeVorherigeSchule === null) ? 'null' : obj.idHerkunftSonstigeVorherigeSchule.toString()) + ',';
		result += '"entlassdatumVorherigeSchule" : ' + ((obj.entlassdatumVorherigeSchule === null) ? 'null' : JSON.stringify(obj.entlassdatumVorherigeSchule)) + ',';
		result += '"kuerzelEntlassjahrgangVorherigeSchule" : ' + ((obj.kuerzelEntlassjahrgangVorherigeSchule === null) ? 'null' : JSON.stringify(obj.kuerzelEntlassjahrgangVorherigeSchule)) + ',';
		result += '"idHerkunftsartVersetzungVorherigeSchule" : ' + ((obj.idHerkunftsartVersetzungVorherigeSchule === null) ? 'null' : JSON.stringify(obj.idHerkunftsartVersetzungVorherigeSchule)) + ',';
		result += '"bemerkungVorherigeSchule" : ' + ((obj.bemerkungVorherigeSchule === null) ? 'null' : JSON.stringify(obj.bemerkungVorherigeSchule)) + ',';
		result += '"idEntlassgrundVorherigeSchule" : ' + ((obj.idEntlassgrundVorherigeSchule === null) ? 'null' : obj.idEntlassgrundVorherigeSchule.toString()) + ',';
		result += '"schluesselAbschlussartAllgemeinbildendVorherigeSchule" : ' + ((obj.schluesselAbschlussartAllgemeinbildendVorherigeSchule === null) ? 'null' : JSON.stringify(obj.schluesselAbschlussartAllgemeinbildendVorherigeSchule)) + ',';
		result += '"schluesselAbschlussartBerufsbildendVorherigeSchule" : ' + ((obj.schluesselAbschlussartBerufsbildendVorherigeSchule === null) ? 'null' : JSON.stringify(obj.schluesselAbschlussartBerufsbildendVorherigeSchule)) + ',';
		result += '"idSchulgliederungVorherigeSchule" : ' + ((obj.idSchulgliederungVorherigeSchule === null) ? 'null' : obj.idSchulgliederungVorherigeSchule.toString()) + ',';
		result += '"schluesselCoreTypeFachklasseVorherigeSchule" : ' + ((obj.schluesselCoreTypeFachklasseVorherigeSchule === null) ? 'null' : JSON.stringify(obj.schluesselCoreTypeFachklasseVorherigeSchule)) + ',';
		result += '"idHochschulabschluss" : ' + ((obj.idHochschulabschluss === null) ? 'null' : obj.idHochschulabschluss.toString()) + ',';
		result += '"entlassdatumDieseSchule" : ' + ((obj.entlassdatumDieseSchule === null) ? 'null' : JSON.stringify(obj.entlassdatumDieseSchule)) + ',';
		result += '"idEntlassjahrgangDieseSchule" : ' + ((obj.idEntlassjahrgangDieseSchule === null) ? 'null' : obj.idEntlassjahrgangDieseSchule.toString()) + ',';
		result += '"idEntlassgrundDieseSchule" : ' + ((obj.idEntlassgrundDieseSchule === null) ? 'null' : obj.idEntlassgrundDieseSchule.toString()) + ',';
		result += '"idAbschlussartDieseSchule" : ' + ((obj.idAbschlussartDieseSchule === null) ? 'null' : JSON.stringify(obj.idAbschlussartDieseSchule)) + ',';
		result += '"idAufnehmendeSchule" : ' + ((obj.idAufnehmendeSchule === null) ? 'null' : obj.idAufnehmendeSchule.toString()) + ',';
		result += '"wechseldatumAufnehmendeSchule" : ' + ((obj.wechseldatumAufnehmendeSchule === null) ? 'null' : JSON.stringify(obj.wechseldatumAufnehmendeSchule)) + ',';
		result += '"wechselBestaetigtAufnehmendeSchule" : ' + obj.wechselBestaetigtAufnehmendeSchule.toString() + ',';
		result += '"einschulungsjahrGrundschule" : ' + ((obj.einschulungsjahrGrundschule === null) ? 'null' : obj.einschulungsjahrGrundschule.toString()) + ',';
		result += '"idEinschulungsartGrundschule" : ' + ((obj.idEinschulungsartGrundschule === null) ? 'null' : obj.idEinschulungsartGrundschule.toString()) + ',';
		result += '"idEingangsphaseGrundschule" : ' + ((obj.idEingangsphaseGrundschule === null) ? 'null' : obj.idEingangsphaseGrundschule.toString()) + ',';
		result += '"idUebergangsempfehlungGrundschule" : ' + ((obj.idUebergangsempfehlungGrundschule === null) ? 'null' : obj.idUebergangsempfehlungGrundschule.toString()) + ',';
		result += '"wechseljahrSekI" : ' + ((obj.wechseljahrSekI === null) ? 'null' : obj.wechseljahrSekI.toString()) + ',';
		result += '"kuerzelErsteSchulformSek1" : ' + ((obj.kuerzelErsteSchulformSek1 === null) ? 'null' : JSON.stringify(obj.kuerzelErsteSchulformSek1)) + ',';
		result += '"wechseljahrSekII" : ' + ((obj.wechseljahrSekII === null) ? 'null' : obj.wechseljahrSekII.toString()) + ',';
		result += '"idDauerKindergartenbesuch" : ' + ((obj.idDauerKindergartenbesuch === null) ? 'null' : obj.idDauerKindergartenbesuch.toString()) + ',';
		result += '"idKindergarten" : ' + ((obj.idKindergarten === null) ? 'null' : obj.idKindergarten.toString()) + ',';
		result += '"verpflichtungSprachfoerderkurs" : ' + obj.verpflichtungSprachfoerderkurs.toString() + ',';
		result += '"teilnahmeSprachfoerderkurs" : ' + obj.teilnahmeSprachfoerderkurs.toString() + ',';
		result += '"merkmale" : [ ';
		for (let i = 0; i < obj.merkmale.size(); i++) {
			const elem = obj.merkmale.get(i);
			result += SchuelerSchulbesuchMerkmal.transpilerToJSON(elem);
			if (i < obj.merkmale.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"bisherBesuchteSchulen" : [ ';
		for (let i = 0; i < obj.bisherBesuchteSchulen.size(); i++) {
			const elem = obj.bisherBesuchteSchulen.get(i);
			result += SchuelerSchulbesuchSchule.transpilerToJSON(elem);
			if (i < obj.bisherBesuchteSchulen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchuelerSchulbesuchsdaten>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.schluesselHoechsterSchulabschluss !== undefined) {
			result += '"schluesselHoechsterSchulabschluss" : ' + ((obj.schluesselHoechsterSchulabschluss === null) ? 'null' : JSON.stringify(obj.schluesselHoechsterSchulabschluss)) + ',';
		}
		if (obj.berufsabschlussVorhanden !== undefined) {
			result += '"berufsabschlussVorhanden" : ' + obj.berufsabschlussVorhanden.toString() + ',';
		}
		if (obj.idVorherigeSchule !== undefined) {
			result += '"idVorherigeSchule" : ' + ((obj.idVorherigeSchule === null) ? 'null' : obj.idVorherigeSchule.toString()) + ',';
		}
		if (obj.idHerkunftSonstigeVorherigeSchule !== undefined) {
			result += '"idHerkunftSonstigeVorherigeSchule" : ' + ((obj.idHerkunftSonstigeVorherigeSchule === null) ? 'null' : obj.idHerkunftSonstigeVorherigeSchule.toString()) + ',';
		}
		if (obj.entlassdatumVorherigeSchule !== undefined) {
			result += '"entlassdatumVorherigeSchule" : ' + ((obj.entlassdatumVorherigeSchule === null) ? 'null' : JSON.stringify(obj.entlassdatumVorherigeSchule)) + ',';
		}
		if (obj.kuerzelEntlassjahrgangVorherigeSchule !== undefined) {
			result += '"kuerzelEntlassjahrgangVorherigeSchule" : ' + ((obj.kuerzelEntlassjahrgangVorherigeSchule === null) ? 'null' : JSON.stringify(obj.kuerzelEntlassjahrgangVorherigeSchule)) + ',';
		}
		if (obj.idHerkunftsartVersetzungVorherigeSchule !== undefined) {
			result += '"idHerkunftsartVersetzungVorherigeSchule" : ' + ((obj.idHerkunftsartVersetzungVorherigeSchule === null) ? 'null' : JSON.stringify(obj.idHerkunftsartVersetzungVorherigeSchule)) + ',';
		}
		if (obj.bemerkungVorherigeSchule !== undefined) {
			result += '"bemerkungVorherigeSchule" : ' + ((obj.bemerkungVorherigeSchule === null) ? 'null' : JSON.stringify(obj.bemerkungVorherigeSchule)) + ',';
		}
		if (obj.idEntlassgrundVorherigeSchule !== undefined) {
			result += '"idEntlassgrundVorherigeSchule" : ' + ((obj.idEntlassgrundVorherigeSchule === null) ? 'null' : obj.idEntlassgrundVorherigeSchule.toString()) + ',';
		}
		if (obj.schluesselAbschlussartAllgemeinbildendVorherigeSchule !== undefined) {
			result += '"schluesselAbschlussartAllgemeinbildendVorherigeSchule" : ' + ((obj.schluesselAbschlussartAllgemeinbildendVorherigeSchule === null) ? 'null' : JSON.stringify(obj.schluesselAbschlussartAllgemeinbildendVorherigeSchule)) + ',';
		}
		if (obj.schluesselAbschlussartBerufsbildendVorherigeSchule !== undefined) {
			result += '"schluesselAbschlussartBerufsbildendVorherigeSchule" : ' + ((obj.schluesselAbschlussartBerufsbildendVorherigeSchule === null) ? 'null' : JSON.stringify(obj.schluesselAbschlussartBerufsbildendVorherigeSchule)) + ',';
		}
		if (obj.idSchulgliederungVorherigeSchule !== undefined) {
			result += '"idSchulgliederungVorherigeSchule" : ' + ((obj.idSchulgliederungVorherigeSchule === null) ? 'null' : obj.idSchulgliederungVorherigeSchule.toString()) + ',';
		}
		if (obj.schluesselCoreTypeFachklasseVorherigeSchule !== undefined) {
			result += '"schluesselCoreTypeFachklasseVorherigeSchule" : ' + ((obj.schluesselCoreTypeFachklasseVorherigeSchule === null) ? 'null' : JSON.stringify(obj.schluesselCoreTypeFachklasseVorherigeSchule)) + ',';
		}
		if (obj.idHochschulabschluss !== undefined) {
			result += '"idHochschulabschluss" : ' + ((obj.idHochschulabschluss === null) ? 'null' : obj.idHochschulabschluss.toString()) + ',';
		}
		if (obj.entlassdatumDieseSchule !== undefined) {
			result += '"entlassdatumDieseSchule" : ' + ((obj.entlassdatumDieseSchule === null) ? 'null' : JSON.stringify(obj.entlassdatumDieseSchule)) + ',';
		}
		if (obj.idEntlassjahrgangDieseSchule !== undefined) {
			result += '"idEntlassjahrgangDieseSchule" : ' + ((obj.idEntlassjahrgangDieseSchule === null) ? 'null' : obj.idEntlassjahrgangDieseSchule.toString()) + ',';
		}
		if (obj.idEntlassgrundDieseSchule !== undefined) {
			result += '"idEntlassgrundDieseSchule" : ' + ((obj.idEntlassgrundDieseSchule === null) ? 'null' : obj.idEntlassgrundDieseSchule.toString()) + ',';
		}
		if (obj.idAbschlussartDieseSchule !== undefined) {
			result += '"idAbschlussartDieseSchule" : ' + ((obj.idAbschlussartDieseSchule === null) ? 'null' : JSON.stringify(obj.idAbschlussartDieseSchule)) + ',';
		}
		if (obj.idAufnehmendeSchule !== undefined) {
			result += '"idAufnehmendeSchule" : ' + ((obj.idAufnehmendeSchule === null) ? 'null' : obj.idAufnehmendeSchule.toString()) + ',';
		}
		if (obj.wechseldatumAufnehmendeSchule !== undefined) {
			result += '"wechseldatumAufnehmendeSchule" : ' + ((obj.wechseldatumAufnehmendeSchule === null) ? 'null' : JSON.stringify(obj.wechseldatumAufnehmendeSchule)) + ',';
		}
		if (obj.wechselBestaetigtAufnehmendeSchule !== undefined) {
			result += '"wechselBestaetigtAufnehmendeSchule" : ' + obj.wechselBestaetigtAufnehmendeSchule.toString() + ',';
		}
		if (obj.einschulungsjahrGrundschule !== undefined) {
			result += '"einschulungsjahrGrundschule" : ' + ((obj.einschulungsjahrGrundschule === null) ? 'null' : obj.einschulungsjahrGrundschule.toString()) + ',';
		}
		if (obj.idEinschulungsartGrundschule !== undefined) {
			result += '"idEinschulungsartGrundschule" : ' + ((obj.idEinschulungsartGrundschule === null) ? 'null' : obj.idEinschulungsartGrundschule.toString()) + ',';
		}
		if (obj.idEingangsphaseGrundschule !== undefined) {
			result += '"idEingangsphaseGrundschule" : ' + ((obj.idEingangsphaseGrundschule === null) ? 'null' : obj.idEingangsphaseGrundschule.toString()) + ',';
		}
		if (obj.idUebergangsempfehlungGrundschule !== undefined) {
			result += '"idUebergangsempfehlungGrundschule" : ' + ((obj.idUebergangsempfehlungGrundschule === null) ? 'null' : obj.idUebergangsempfehlungGrundschule.toString()) + ',';
		}
		if (obj.wechseljahrSekI !== undefined) {
			result += '"wechseljahrSekI" : ' + ((obj.wechseljahrSekI === null) ? 'null' : obj.wechseljahrSekI.toString()) + ',';
		}
		if (obj.kuerzelErsteSchulformSek1 !== undefined) {
			result += '"kuerzelErsteSchulformSek1" : ' + ((obj.kuerzelErsteSchulformSek1 === null) ? 'null' : JSON.stringify(obj.kuerzelErsteSchulformSek1)) + ',';
		}
		if (obj.wechseljahrSekII !== undefined) {
			result += '"wechseljahrSekII" : ' + ((obj.wechseljahrSekII === null) ? 'null' : obj.wechseljahrSekII.toString()) + ',';
		}
		if (obj.idDauerKindergartenbesuch !== undefined) {
			result += '"idDauerKindergartenbesuch" : ' + ((obj.idDauerKindergartenbesuch === null) ? 'null' : obj.idDauerKindergartenbesuch.toString()) + ',';
		}
		if (obj.idKindergarten !== undefined) {
			result += '"idKindergarten" : ' + ((obj.idKindergarten === null) ? 'null' : obj.idKindergarten.toString()) + ',';
		}
		if (obj.verpflichtungSprachfoerderkurs !== undefined) {
			result += '"verpflichtungSprachfoerderkurs" : ' + obj.verpflichtungSprachfoerderkurs.toString() + ',';
		}
		if (obj.teilnahmeSprachfoerderkurs !== undefined) {
			result += '"teilnahmeSprachfoerderkurs" : ' + obj.teilnahmeSprachfoerderkurs.toString() + ',';
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
		if (obj.bisherBesuchteSchulen !== undefined) {
			result += '"bisherBesuchteSchulen" : [ ';
			for (let i = 0; i < obj.bisherBesuchteSchulen.size(); i++) {
				const elem = obj.bisherBesuchteSchulen.get(i);
				result += SchuelerSchulbesuchSchule.transpilerToJSON(elem);
				if (i < obj.bisherBesuchteSchulen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schueler_SchuelerSchulbesuchsdaten(obj: unknown): SchuelerSchulbesuchsdaten {
	return obj as SchuelerSchulbesuchsdaten;
}
