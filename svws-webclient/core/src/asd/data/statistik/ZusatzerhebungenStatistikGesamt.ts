import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class ZusatzerhebungenStatistikGesamt extends JavaObject {

	/**
	 * Die ID des Schuljahresabschnitts, zu welchem diese Daten gehören
	 */
	public idSchuljahresabschnitt: number = 0;

	/**
	 * Name des Statistik-Ansprechpartners für IT.NRW
	 */
	public statistikAnsprechpartnerName: string | null = null;

	/**
	 * Telefonnummer des Statistik-Ansprechpartners für IT.NRW
	 */
	public statistikAnsprechpartnerTelefon: string | null = null;

	/**
	 * Email des Statistik-Ansprechpartners für IT.NRW
	 */
	public statistikAnsprechpartnerEmail: string | null = null;

	/**
	 * Gibt an, ob die Schule internationale Kontakte pflegt
	 */
	public hatInternationaleKontakte: boolean = false;

	/**
	 * Die Anzahl der Schulanmeldungen nach § 35 SchulG
	 */
	public anzahlSchulanmeldungen: number | null = null;

	/**
	 * Die Anzahl der Zurückstellungsverfahren im Zuge der Anmeldungen
	 */
	public anzahlZurueckstellungsverfahren: number | null = null;

	/**
	 * Die Anzahl der tatsächlichen Zurückstellungen insgesamt
	 */
	public anzahlZurueckstellungenInsgesamt: number | null = null;

	/**
	 * Die Anzahl der tatsächlichen Zurückstellungen weiblich
	 */
	public anzahlZurueckstellungenWeiblich: number | null = null;

	/**
	 * Die Anzahl der tatsächlichen Zurückstellungen Ausländer zusammen
	 */
	public anzahlZurueckstellungenAuslaenderZusammen: number | null = null;

	/**
	 * Die Anzahl der tatsächlichen Zurückstellungen Ausländer weiblich
	 */
	public anzahlZurueckstellungenAuslaenderWeiblich: number | null = null;

	/**
	 * Die Anzahl der tatsächlich zurückgestellten Kinder, die im 4. Quartal des Erhebungsvorjahres 6 Jahre alt geworden sind
	 */
	public anzahlZurueckstellungenInsgesamtQuartal4: number | null = null;

	/**
	 * Die Anzahl der tatsächlich zurückgestellten Kinder, die im 1. Quartal des Erhebungsjahres 6 Jahre alt geworden sind
	 */
	public anzahlZurueckstellungenInsgesamtQuartal1: number | null = null;

	/**
	 * Die Anzahl der tatsächlich zurückgestellten Kinder, die im 2. Quartal des Erhebungsjahres 6 Jahre alt geworden sind
	 */
	public anzahlZurueckstellungenInsgesamtQuartal2: number | null = null;

	/**
	 * Die Anzahl der tatsächlich zurückgestellten Kinder, die im 3. Quartal des Erhebungsjahres 6 Jahre alt geworden sind
	 */
	public anzahlZurueckstellungenInsgesamtQuartal3: number | null = null;

	/**
	 * Die Art des Internats
	 */
	public idInternatArt: number | null = null;

	/**
	 * Die Anzahl der möglichen Internatsplätze für Jungen
	 */
	public anzahlInternatplaetzeJungenMoeglich: number | null = null;

	/**
	 * Die Anzahl der belegten Internatsplätze für Jungen
	 */
	public anzahlInternatplaetzeJungenBelegt: number | null = null;

	/**
	 * Die Anzahl der möglichen Internatsplätze für Mädchen
	 */
	public anzahlInternatplaetzeMaedchenMoeglich: number | null = null;

	/**
	 * Die Anzahl der belegten Internatsplätze für Mädchen
	 */
	public anzahlInternatplaetzeMaedchenBelegt: number | null = null;

	/**
	 * Die Anzahl der möglichen geschlechtsneutralen Internatsplätze
	 */
	public anzahlInternatplaetzeNeutralMoeglich: number | null = null;

	/**
	 * Die Anzahl der belegten geschlechtsneutralen Internatsplätze
	 */
	public anzahlInternatplaetzeNeutralBelegt: number | null = null;

	/**
	 * Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - allgemeinbildend insgesamt
	 */
	public durchschnittKlinikschuleAllgemeinbildendInsgesamt: number | null = null;

	/**
	 * Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - allgemeinbildend mit Schwerstbehinderung
	 */
	public durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte: number | null = null;

	/**
	 * Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Teilzeit insgesamt
	 */
	public durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt: number | null = null;

	/**
	 * Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Teilzeit mit Schwerstbehinderung
	 */
	public durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte: number | null = null;

	/**
	 * Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Vollzeit insgesamt
	 */
	public durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt: number | null = null;

	/**
	 * Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Vollzeit mit Schwerstbehinderung
	 */
	public durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte: number | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.statistik.ZusatzerhebungenStatistikGesamt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.statistik.ZusatzerhebungenStatistikGesamt'].includes(name);
	}

	public static readonly class = new Class<ZusatzerhebungenStatistikGesamt>('de.svws_nrw.asd.data.statistik.ZusatzerhebungenStatistikGesamt');

	public static transpilerFromJSON(json: string): ZusatzerhebungenStatistikGesamt {
		const obj = JSON.parse(json) as Partial<ZusatzerhebungenStatistikGesamt>;
		const result = new ZusatzerhebungenStatistikGesamt();
		if (obj.idSchuljahresabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		result.statistikAnsprechpartnerName = (obj.statistikAnsprechpartnerName === undefined) ? null : obj.statistikAnsprechpartnerName === null ? null : obj.statistikAnsprechpartnerName;
		result.statistikAnsprechpartnerTelefon = (obj.statistikAnsprechpartnerTelefon === undefined) ? null : obj.statistikAnsprechpartnerTelefon === null ? null : obj.statistikAnsprechpartnerTelefon;
		result.statistikAnsprechpartnerEmail = (obj.statistikAnsprechpartnerEmail === undefined) ? null : obj.statistikAnsprechpartnerEmail === null ? null : obj.statistikAnsprechpartnerEmail;
		if (obj.hatInternationaleKontakte === undefined)
			throw new Error('invalid json format, missing attribute hatInternationaleKontakte');
		result.hatInternationaleKontakte = obj.hatInternationaleKontakte;
		result.anzahlSchulanmeldungen = (obj.anzahlSchulanmeldungen === undefined) ? null : obj.anzahlSchulanmeldungen === null ? null : obj.anzahlSchulanmeldungen;
		result.anzahlZurueckstellungsverfahren = (obj.anzahlZurueckstellungsverfahren === undefined) ? null : obj.anzahlZurueckstellungsverfahren === null ? null : obj.anzahlZurueckstellungsverfahren;
		result.anzahlZurueckstellungenInsgesamt = (obj.anzahlZurueckstellungenInsgesamt === undefined) ? null : obj.anzahlZurueckstellungenInsgesamt === null ? null : obj.anzahlZurueckstellungenInsgesamt;
		result.anzahlZurueckstellungenWeiblich = (obj.anzahlZurueckstellungenWeiblich === undefined) ? null : obj.anzahlZurueckstellungenWeiblich === null ? null : obj.anzahlZurueckstellungenWeiblich;
		result.anzahlZurueckstellungenAuslaenderZusammen = (obj.anzahlZurueckstellungenAuslaenderZusammen === undefined) ? null : obj.anzahlZurueckstellungenAuslaenderZusammen === null ? null : obj.anzahlZurueckstellungenAuslaenderZusammen;
		result.anzahlZurueckstellungenAuslaenderWeiblich = (obj.anzahlZurueckstellungenAuslaenderWeiblich === undefined) ? null : obj.anzahlZurueckstellungenAuslaenderWeiblich === null ? null : obj.anzahlZurueckstellungenAuslaenderWeiblich;
		result.anzahlZurueckstellungenInsgesamtQuartal4 = (obj.anzahlZurueckstellungenInsgesamtQuartal4 === undefined) ? null : obj.anzahlZurueckstellungenInsgesamtQuartal4 === null ? null : obj.anzahlZurueckstellungenInsgesamtQuartal4;
		result.anzahlZurueckstellungenInsgesamtQuartal1 = (obj.anzahlZurueckstellungenInsgesamtQuartal1 === undefined) ? null : obj.anzahlZurueckstellungenInsgesamtQuartal1 === null ? null : obj.anzahlZurueckstellungenInsgesamtQuartal1;
		result.anzahlZurueckstellungenInsgesamtQuartal2 = (obj.anzahlZurueckstellungenInsgesamtQuartal2 === undefined) ? null : obj.anzahlZurueckstellungenInsgesamtQuartal2 === null ? null : obj.anzahlZurueckstellungenInsgesamtQuartal2;
		result.anzahlZurueckstellungenInsgesamtQuartal3 = (obj.anzahlZurueckstellungenInsgesamtQuartal3 === undefined) ? null : obj.anzahlZurueckstellungenInsgesamtQuartal3 === null ? null : obj.anzahlZurueckstellungenInsgesamtQuartal3;
		result.idInternatArt = (obj.idInternatArt === undefined) ? null : obj.idInternatArt === null ? null : obj.idInternatArt;
		result.anzahlInternatplaetzeJungenMoeglich = (obj.anzahlInternatplaetzeJungenMoeglich === undefined) ? null : obj.anzahlInternatplaetzeJungenMoeglich === null ? null : obj.anzahlInternatplaetzeJungenMoeglich;
		result.anzahlInternatplaetzeJungenBelegt = (obj.anzahlInternatplaetzeJungenBelegt === undefined) ? null : obj.anzahlInternatplaetzeJungenBelegt === null ? null : obj.anzahlInternatplaetzeJungenBelegt;
		result.anzahlInternatplaetzeMaedchenMoeglich = (obj.anzahlInternatplaetzeMaedchenMoeglich === undefined) ? null : obj.anzahlInternatplaetzeMaedchenMoeglich === null ? null : obj.anzahlInternatplaetzeMaedchenMoeglich;
		result.anzahlInternatplaetzeMaedchenBelegt = (obj.anzahlInternatplaetzeMaedchenBelegt === undefined) ? null : obj.anzahlInternatplaetzeMaedchenBelegt === null ? null : obj.anzahlInternatplaetzeMaedchenBelegt;
		result.anzahlInternatplaetzeNeutralMoeglich = (obj.anzahlInternatplaetzeNeutralMoeglich === undefined) ? null : obj.anzahlInternatplaetzeNeutralMoeglich === null ? null : obj.anzahlInternatplaetzeNeutralMoeglich;
		result.anzahlInternatplaetzeNeutralBelegt = (obj.anzahlInternatplaetzeNeutralBelegt === undefined) ? null : obj.anzahlInternatplaetzeNeutralBelegt === null ? null : obj.anzahlInternatplaetzeNeutralBelegt;
		result.durchschnittKlinikschuleAllgemeinbildendInsgesamt = (obj.durchschnittKlinikschuleAllgemeinbildendInsgesamt === undefined) ? null : obj.durchschnittKlinikschuleAllgemeinbildendInsgesamt === null ? null : obj.durchschnittKlinikschuleAllgemeinbildendInsgesamt;
		result.durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte = (obj.durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte === undefined) ? null : obj.durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte === null ? null : obj.durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte;
		result.durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt = (obj.durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt === undefined) ? null : obj.durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt === null ? null : obj.durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt;
		result.durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte = (obj.durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte === undefined) ? null : obj.durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte === null ? null : obj.durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte;
		result.durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt = (obj.durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt === undefined) ? null : obj.durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt === null ? null : obj.durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt;
		result.durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte = (obj.durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte === undefined) ? null : obj.durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte === null ? null : obj.durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte;
		return result;
	}

	public static transpilerToJSON(obj: ZusatzerhebungenStatistikGesamt): string {
		let result = '{';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		result += '"statistikAnsprechpartnerName" : ' + ((obj.statistikAnsprechpartnerName === null) ? 'null' : JSON.stringify(obj.statistikAnsprechpartnerName)) + ',';
		result += '"statistikAnsprechpartnerTelefon" : ' + ((obj.statistikAnsprechpartnerTelefon === null) ? 'null' : JSON.stringify(obj.statistikAnsprechpartnerTelefon)) + ',';
		result += '"statistikAnsprechpartnerEmail" : ' + ((obj.statistikAnsprechpartnerEmail === null) ? 'null' : JSON.stringify(obj.statistikAnsprechpartnerEmail)) + ',';
		result += '"hatInternationaleKontakte" : ' + obj.hatInternationaleKontakte.toString() + ',';
		result += '"anzahlSchulanmeldungen" : ' + ((obj.anzahlSchulanmeldungen === null) ? 'null' : obj.anzahlSchulanmeldungen.toString()) + ',';
		result += '"anzahlZurueckstellungsverfahren" : ' + ((obj.anzahlZurueckstellungsverfahren === null) ? 'null' : obj.anzahlZurueckstellungsverfahren.toString()) + ',';
		result += '"anzahlZurueckstellungenInsgesamt" : ' + ((obj.anzahlZurueckstellungenInsgesamt === null) ? 'null' : obj.anzahlZurueckstellungenInsgesamt.toString()) + ',';
		result += '"anzahlZurueckstellungenWeiblich" : ' + ((obj.anzahlZurueckstellungenWeiblich === null) ? 'null' : obj.anzahlZurueckstellungenWeiblich.toString()) + ',';
		result += '"anzahlZurueckstellungenAuslaenderZusammen" : ' + ((obj.anzahlZurueckstellungenAuslaenderZusammen === null) ? 'null' : obj.anzahlZurueckstellungenAuslaenderZusammen.toString()) + ',';
		result += '"anzahlZurueckstellungenAuslaenderWeiblich" : ' + ((obj.anzahlZurueckstellungenAuslaenderWeiblich === null) ? 'null' : obj.anzahlZurueckstellungenAuslaenderWeiblich.toString()) + ',';
		result += '"anzahlZurueckstellungenInsgesamtQuartal4" : ' + ((obj.anzahlZurueckstellungenInsgesamtQuartal4 === null) ? 'null' : obj.anzahlZurueckstellungenInsgesamtQuartal4.toString()) + ',';
		result += '"anzahlZurueckstellungenInsgesamtQuartal1" : ' + ((obj.anzahlZurueckstellungenInsgesamtQuartal1 === null) ? 'null' : obj.anzahlZurueckstellungenInsgesamtQuartal1.toString()) + ',';
		result += '"anzahlZurueckstellungenInsgesamtQuartal2" : ' + ((obj.anzahlZurueckstellungenInsgesamtQuartal2 === null) ? 'null' : obj.anzahlZurueckstellungenInsgesamtQuartal2.toString()) + ',';
		result += '"anzahlZurueckstellungenInsgesamtQuartal3" : ' + ((obj.anzahlZurueckstellungenInsgesamtQuartal3 === null) ? 'null' : obj.anzahlZurueckstellungenInsgesamtQuartal3.toString()) + ',';
		result += '"idInternatArt" : ' + ((obj.idInternatArt === null) ? 'null' : obj.idInternatArt.toString()) + ',';
		result += '"anzahlInternatplaetzeJungenMoeglich" : ' + ((obj.anzahlInternatplaetzeJungenMoeglich === null) ? 'null' : obj.anzahlInternatplaetzeJungenMoeglich.toString()) + ',';
		result += '"anzahlInternatplaetzeJungenBelegt" : ' + ((obj.anzahlInternatplaetzeJungenBelegt === null) ? 'null' : obj.anzahlInternatplaetzeJungenBelegt.toString()) + ',';
		result += '"anzahlInternatplaetzeMaedchenMoeglich" : ' + ((obj.anzahlInternatplaetzeMaedchenMoeglich === null) ? 'null' : obj.anzahlInternatplaetzeMaedchenMoeglich.toString()) + ',';
		result += '"anzahlInternatplaetzeMaedchenBelegt" : ' + ((obj.anzahlInternatplaetzeMaedchenBelegt === null) ? 'null' : obj.anzahlInternatplaetzeMaedchenBelegt.toString()) + ',';
		result += '"anzahlInternatplaetzeNeutralMoeglich" : ' + ((obj.anzahlInternatplaetzeNeutralMoeglich === null) ? 'null' : obj.anzahlInternatplaetzeNeutralMoeglich.toString()) + ',';
		result += '"anzahlInternatplaetzeNeutralBelegt" : ' + ((obj.anzahlInternatplaetzeNeutralBelegt === null) ? 'null' : obj.anzahlInternatplaetzeNeutralBelegt.toString()) + ',';
		result += '"durchschnittKlinikschuleAllgemeinbildendInsgesamt" : ' + ((obj.durchschnittKlinikschuleAllgemeinbildendInsgesamt === null) ? 'null' : obj.durchschnittKlinikschuleAllgemeinbildendInsgesamt.toString()) + ',';
		result += '"durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte" : ' + ((obj.durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte === null) ? 'null' : obj.durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte.toString()) + ',';
		result += '"durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt" : ' + ((obj.durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt === null) ? 'null' : obj.durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt.toString()) + ',';
		result += '"durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte" : ' + ((obj.durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte === null) ? 'null' : obj.durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte.toString()) + ',';
		result += '"durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt" : ' + ((obj.durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt === null) ? 'null' : obj.durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt.toString()) + ',';
		result += '"durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte" : ' + ((obj.durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte === null) ? 'null' : obj.durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ZusatzerhebungenStatistikGesamt>): string {
		let result = '{';
		if (obj.idSchuljahresabschnitt !== undefined) {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		}
		if (obj.statistikAnsprechpartnerName !== undefined) {
			result += '"statistikAnsprechpartnerName" : ' + ((obj.statistikAnsprechpartnerName === null) ? 'null' : JSON.stringify(obj.statistikAnsprechpartnerName)) + ',';
		}
		if (obj.statistikAnsprechpartnerTelefon !== undefined) {
			result += '"statistikAnsprechpartnerTelefon" : ' + ((obj.statistikAnsprechpartnerTelefon === null) ? 'null' : JSON.stringify(obj.statistikAnsprechpartnerTelefon)) + ',';
		}
		if (obj.statistikAnsprechpartnerEmail !== undefined) {
			result += '"statistikAnsprechpartnerEmail" : ' + ((obj.statistikAnsprechpartnerEmail === null) ? 'null' : JSON.stringify(obj.statistikAnsprechpartnerEmail)) + ',';
		}
		if (obj.hatInternationaleKontakte !== undefined) {
			result += '"hatInternationaleKontakte" : ' + obj.hatInternationaleKontakte.toString() + ',';
		}
		if (obj.anzahlSchulanmeldungen !== undefined) {
			result += '"anzahlSchulanmeldungen" : ' + ((obj.anzahlSchulanmeldungen === null) ? 'null' : obj.anzahlSchulanmeldungen.toString()) + ',';
		}
		if (obj.anzahlZurueckstellungsverfahren !== undefined) {
			result += '"anzahlZurueckstellungsverfahren" : ' + ((obj.anzahlZurueckstellungsverfahren === null) ? 'null' : obj.anzahlZurueckstellungsverfahren.toString()) + ',';
		}
		if (obj.anzahlZurueckstellungenInsgesamt !== undefined) {
			result += '"anzahlZurueckstellungenInsgesamt" : ' + ((obj.anzahlZurueckstellungenInsgesamt === null) ? 'null' : obj.anzahlZurueckstellungenInsgesamt.toString()) + ',';
		}
		if (obj.anzahlZurueckstellungenWeiblich !== undefined) {
			result += '"anzahlZurueckstellungenWeiblich" : ' + ((obj.anzahlZurueckstellungenWeiblich === null) ? 'null' : obj.anzahlZurueckstellungenWeiblich.toString()) + ',';
		}
		if (obj.anzahlZurueckstellungenAuslaenderZusammen !== undefined) {
			result += '"anzahlZurueckstellungenAuslaenderZusammen" : ' + ((obj.anzahlZurueckstellungenAuslaenderZusammen === null) ? 'null' : obj.anzahlZurueckstellungenAuslaenderZusammen.toString()) + ',';
		}
		if (obj.anzahlZurueckstellungenAuslaenderWeiblich !== undefined) {
			result += '"anzahlZurueckstellungenAuslaenderWeiblich" : ' + ((obj.anzahlZurueckstellungenAuslaenderWeiblich === null) ? 'null' : obj.anzahlZurueckstellungenAuslaenderWeiblich.toString()) + ',';
		}
		if (obj.anzahlZurueckstellungenInsgesamtQuartal4 !== undefined) {
			result += '"anzahlZurueckstellungenInsgesamtQuartal4" : ' + ((obj.anzahlZurueckstellungenInsgesamtQuartal4 === null) ? 'null' : obj.anzahlZurueckstellungenInsgesamtQuartal4.toString()) + ',';
		}
		if (obj.anzahlZurueckstellungenInsgesamtQuartal1 !== undefined) {
			result += '"anzahlZurueckstellungenInsgesamtQuartal1" : ' + ((obj.anzahlZurueckstellungenInsgesamtQuartal1 === null) ? 'null' : obj.anzahlZurueckstellungenInsgesamtQuartal1.toString()) + ',';
		}
		if (obj.anzahlZurueckstellungenInsgesamtQuartal2 !== undefined) {
			result += '"anzahlZurueckstellungenInsgesamtQuartal2" : ' + ((obj.anzahlZurueckstellungenInsgesamtQuartal2 === null) ? 'null' : obj.anzahlZurueckstellungenInsgesamtQuartal2.toString()) + ',';
		}
		if (obj.anzahlZurueckstellungenInsgesamtQuartal3 !== undefined) {
			result += '"anzahlZurueckstellungenInsgesamtQuartal3" : ' + ((obj.anzahlZurueckstellungenInsgesamtQuartal3 === null) ? 'null' : obj.anzahlZurueckstellungenInsgesamtQuartal3.toString()) + ',';
		}
		if (obj.idInternatArt !== undefined) {
			result += '"idInternatArt" : ' + ((obj.idInternatArt === null) ? 'null' : obj.idInternatArt.toString()) + ',';
		}
		if (obj.anzahlInternatplaetzeJungenMoeglich !== undefined) {
			result += '"anzahlInternatplaetzeJungenMoeglich" : ' + ((obj.anzahlInternatplaetzeJungenMoeglich === null) ? 'null' : obj.anzahlInternatplaetzeJungenMoeglich.toString()) + ',';
		}
		if (obj.anzahlInternatplaetzeJungenBelegt !== undefined) {
			result += '"anzahlInternatplaetzeJungenBelegt" : ' + ((obj.anzahlInternatplaetzeJungenBelegt === null) ? 'null' : obj.anzahlInternatplaetzeJungenBelegt.toString()) + ',';
		}
		if (obj.anzahlInternatplaetzeMaedchenMoeglich !== undefined) {
			result += '"anzahlInternatplaetzeMaedchenMoeglich" : ' + ((obj.anzahlInternatplaetzeMaedchenMoeglich === null) ? 'null' : obj.anzahlInternatplaetzeMaedchenMoeglich.toString()) + ',';
		}
		if (obj.anzahlInternatplaetzeMaedchenBelegt !== undefined) {
			result += '"anzahlInternatplaetzeMaedchenBelegt" : ' + ((obj.anzahlInternatplaetzeMaedchenBelegt === null) ? 'null' : obj.anzahlInternatplaetzeMaedchenBelegt.toString()) + ',';
		}
		if (obj.anzahlInternatplaetzeNeutralMoeglich !== undefined) {
			result += '"anzahlInternatplaetzeNeutralMoeglich" : ' + ((obj.anzahlInternatplaetzeNeutralMoeglich === null) ? 'null' : obj.anzahlInternatplaetzeNeutralMoeglich.toString()) + ',';
		}
		if (obj.anzahlInternatplaetzeNeutralBelegt !== undefined) {
			result += '"anzahlInternatplaetzeNeutralBelegt" : ' + ((obj.anzahlInternatplaetzeNeutralBelegt === null) ? 'null' : obj.anzahlInternatplaetzeNeutralBelegt.toString()) + ',';
		}
		if (obj.durchschnittKlinikschuleAllgemeinbildendInsgesamt !== undefined) {
			result += '"durchschnittKlinikschuleAllgemeinbildendInsgesamt" : ' + ((obj.durchschnittKlinikschuleAllgemeinbildendInsgesamt === null) ? 'null' : obj.durchschnittKlinikschuleAllgemeinbildendInsgesamt.toString()) + ',';
		}
		if (obj.durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte !== undefined) {
			result += '"durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte" : ' + ((obj.durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte === null) ? 'null' : obj.durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte.toString()) + ',';
		}
		if (obj.durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt !== undefined) {
			result += '"durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt" : ' + ((obj.durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt === null) ? 'null' : obj.durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt.toString()) + ',';
		}
		if (obj.durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte !== undefined) {
			result += '"durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte" : ' + ((obj.durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte === null) ? 'null' : obj.durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte.toString()) + ',';
		}
		if (obj.durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt !== undefined) {
			result += '"durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt" : ' + ((obj.durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt === null) ? 'null' : obj.durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt.toString()) + ',';
		}
		if (obj.durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte !== undefined) {
			result += '"durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte" : ' + ((obj.durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte === null) ? 'null' : obj.durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_statistik_ZusatzerhebungenStatistikGesamt(obj: unknown): ZusatzerhebungenStatistikGesamt {
	return obj as ZusatzerhebungenStatistikGesamt;
}
