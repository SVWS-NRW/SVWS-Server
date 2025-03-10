import { JavaObject } from '../../../java/lang/JavaObject';
import { AbiturFachbelegung } from '../../../core/data/gost/AbiturFachbelegung';
import { ArrayList } from '../../../java/util/ArrayList';
import { GostBesondereLernleistung } from '../../../core/types/gost/GostBesondereLernleistung';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { Sprachendaten } from '../../../core/data/schueler/Sprachendaten';

export class Abiturdaten extends JavaObject {

	/**
	 * Die eindeutige ID des Schülers
	 */
	public schuelerID : number = -1;

	/**
	 * Der Bezeichner der Schulform
	 */
	public schulform : string = "GY";

	/**
	 * Das Kalenderjahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird.
	 */
	public abiturjahr : number = 0;

	/**
	 * Das Schuljahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird.
	 */
	public schuljahrAbitur : number = 0;

	/**
	 * Gibt für die einzelnen {@link GostHalbjahr}-Werte an, ob gewertete Leistungsdaten vorhanden sind oder es sich um Werte der Laufbahnplanung handelt.
	 */
	public readonly bewertetesHalbjahr : Array<boolean> = Array(6).fill(false);

	/**
	 * Ein Array mit den Fachbelegungen in der Oberstufe.
	 */
	public readonly fachbelegungen : List<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();

	/**
	 * Die Sprachendaten des Schülers mit Informationen zu Sprachbelegungen (Sprachenfolge) und zu Sprachprüfungen.
	 */
	public sprachendaten : Sprachendaten = new Sprachendaten();

	/**
	 * Das einstellige Kürzel der Sprache des bilingualen Bildungsganges, falls der Schüler an einem solchen teilnimmt.
	 */
	public bilingualeSprache : string | null = null;

	/**
	 * Gibt an, ob das große Latinum erworben wurde.
	 */
	public latinum : boolean = false;

	/**
	 * Gibt an, ob das kleine Latinum erworben wurde.
	 */
	public kleinesLatinum : boolean = false;

	/**
	 * Gibt an, ob das Graecum erworben wurde.
	 */
	public graecum : boolean = false;

	/**
	 * Gibt an, ob das Hebraicum erworben wurde.
	 */
	public hebraicum : boolean = false;

	/**
	 * Die Anzahl der Fehlstunden in der gesamten Qualifikationsphase.
	 */
	public block1FehlstundenGesamt : number = 0;

	/**
	 * Die Anzahl der unentschuldigten Fehlstunden in der gesamten Qualifikationsphase.
	 */
	public block1FehlstundenUnentschuldigt : number = 0;

	/**
	 * Das Projektkursthema, sofern ein Projektkurs belegt wurde.
	 */
	public projektKursThema : string | null = null;

	/**
	 * Das Kürzel des ersten Leitfaches des belegten Projektkurs, sofern einer belegt wurde.
	 */
	public projektkursLeitfach1Kuerzel : string | null = null;

	/**
	 * Das Kürzel des zweiten Leitfaches des belegten Projektkurs, sofern einer belegt wurde und ein zweites Leitfach für diesen festgelegt wurde
	 */
	public projektkursLeitfach2Kuerzel : string | null = null;

	/**
	 * Gibt an, ob eine besondere Lernleistung vorliegt (K - keine, P - in einem Projektkurs, E - extern).
	 */
	public besondereLernleistung : string | null = GostBesondereLernleistung.KEINE.kuerzel;

	/**
	 * Gibt ggf. die Note einer externen besonderen Lernleistung an.
	 */
	public besondereLernleistungNotenKuerzel : string | null = null;

	/**
	 * Gibt das Thema der Besonderen Lernleistung an.
	 */
	public besondereLernleistungThema : string | null = null;

	/**
	 * Gibt die Anzahl der Kurse in der Qualifikationsphase an.
	 */
	public block1AnzahlKurse : number | null = null;

	/**
	 * Gibt die Anzahl der Gesamtdefizite in der Qualifikationsphase an.
	 */
	public block1DefiziteGesamt : number | null = null;

	/**
	 * Gibt die Anzahl der Defizite im LK-Bereich in der Qualifikationsphase an.
	 */
	public block1DefiziteLK : number | null = null;

	/**
	 * Gibt die Punktsumme aller Grundkurse in der Qualifikationsphase an.
	 */
	public block1PunktSummeGK : number | null = null;

	/**
	 * Gibt die Punktsumme aller Leistungskurse in der Qualifikationsphase an.
	 */
	public block1PunktSummeLK : number | null = null;

	/**
	 * Gibt die normierte Punktsumme aller Kurse in der Qualifikationsphase an.
	 */
	public block1PunktSummeNormiert : number | null = null;

	/**
	 * Gibt den Durchschnitt der Notenpunkte von allen Kursen der Qualifikationsphase an.
	 */
	public block1NotenpunkteDurchschnitt : number | null = null;

	/**
	 * Gibt an, ob die Zulassung erreicht wurde oder nicht - sofern diese schon geprüft wurde.
	 */
	public block1Zulassung : boolean | null = null;

	/**
	 * Gibt an, ob freiwillig von der Abiturprüfung zurückgetreten wurde.
	 */
	public freiwilligerRuecktritt : boolean = false;

	/**
	 * Gibt die Anzahl der Gesamtdefizite im Abiturbereich (Block II) an.
	 */
	public block2DefiziteGesamt : number | null = null;

	/**
	 * Gibt die Anzahl der Leistungskurs-Defizite im Abiturbereich (Block II) an.
	 */
	public block2DefiziteLK : number | null = null;

	/**
	 * Gibt die Punktsumme im Abiturbereich (Block II) an.
	 */
	public block2PunktSumme : number | null = null;

	/**
	 * Gibt die erreichte Gesamtpunktzahl in der Qualifikation und im Abiturbereich (Block I und II) an.
	 */
	public gesamtPunkte : number | null = null;

	/**
	 * Gibt die Gesamtpunktzahl an, ab der sich die Abiturnote verbessern würde
	 */
	public gesamtPunkteVerbesserung : number | null = null;

	/**
	 * Gibt die Gesamtpunktzahl an, ab der sich die Abiturnote verschlechtern würde.
	 */
	public gesamtPunkteVerschlechterung : number | null = null;

	/**
	 * Gibt an, ob die Abiturprüfung bestanden wurde oder nicht - sofern das Prüfungsverfahren schon abgeschlossen wurde.
	 */
	public pruefungBestanden : boolean | null = null;

	/**
	 * Die Abiturnote einer bestandenen Abiturprüfung - sofern das Prüfungsverfahren schon abgeschlossen wurde.
	 */
	public note : string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.Abiturdaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.Abiturdaten'].includes(name);
	}

	public static class = new Class<Abiturdaten>('de.svws_nrw.core.data.gost.Abiturdaten');

	public static transpilerFromJSON(json : string): Abiturdaten {
		const obj = JSON.parse(json) as Partial<Abiturdaten>;
		const result = new Abiturdaten();
		if (obj.schuelerID === undefined)
			throw new Error('invalid json format, missing attribute schuelerID');
		result.schuelerID = obj.schuelerID;
		if (obj.schulform === undefined)
			throw new Error('invalid json format, missing attribute schulform');
		result.schulform = obj.schulform;
		if (obj.abiturjahr === undefined)
			throw new Error('invalid json format, missing attribute abiturjahr');
		result.abiturjahr = obj.abiturjahr;
		if (obj.schuljahrAbitur === undefined)
			throw new Error('invalid json format, missing attribute schuljahrAbitur');
		result.schuljahrAbitur = obj.schuljahrAbitur;
		if (obj.bewertetesHalbjahr !== undefined) {
			for (let i = 0; i < obj.bewertetesHalbjahr.length; i++) {
				result.bewertetesHalbjahr[i] = obj.bewertetesHalbjahr[i];
			}
		}
		if (obj.fachbelegungen !== undefined) {
			for (const elem of obj.fachbelegungen) {
				result.fachbelegungen.add(AbiturFachbelegung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.sprachendaten === undefined)
			throw new Error('invalid json format, missing attribute sprachendaten');
		result.sprachendaten = Sprachendaten.transpilerFromJSON(JSON.stringify(obj.sprachendaten));
		result.bilingualeSprache = (obj.bilingualeSprache === undefined) ? null : obj.bilingualeSprache === null ? null : obj.bilingualeSprache;
		if (obj.latinum === undefined)
			throw new Error('invalid json format, missing attribute latinum');
		result.latinum = obj.latinum;
		if (obj.kleinesLatinum === undefined)
			throw new Error('invalid json format, missing attribute kleinesLatinum');
		result.kleinesLatinum = obj.kleinesLatinum;
		if (obj.graecum === undefined)
			throw new Error('invalid json format, missing attribute graecum');
		result.graecum = obj.graecum;
		if (obj.hebraicum === undefined)
			throw new Error('invalid json format, missing attribute hebraicum');
		result.hebraicum = obj.hebraicum;
		if (obj.block1FehlstundenGesamt === undefined)
			throw new Error('invalid json format, missing attribute block1FehlstundenGesamt');
		result.block1FehlstundenGesamt = obj.block1FehlstundenGesamt;
		if (obj.block1FehlstundenUnentschuldigt === undefined)
			throw new Error('invalid json format, missing attribute block1FehlstundenUnentschuldigt');
		result.block1FehlstundenUnentschuldigt = obj.block1FehlstundenUnentschuldigt;
		result.projektKursThema = (obj.projektKursThema === undefined) ? null : obj.projektKursThema === null ? null : obj.projektKursThema;
		result.projektkursLeitfach1Kuerzel = (obj.projektkursLeitfach1Kuerzel === undefined) ? null : obj.projektkursLeitfach1Kuerzel === null ? null : obj.projektkursLeitfach1Kuerzel;
		result.projektkursLeitfach2Kuerzel = (obj.projektkursLeitfach2Kuerzel === undefined) ? null : obj.projektkursLeitfach2Kuerzel === null ? null : obj.projektkursLeitfach2Kuerzel;
		result.besondereLernleistung = (obj.besondereLernleistung === undefined) ? null : obj.besondereLernleistung === null ? null : obj.besondereLernleistung;
		result.besondereLernleistungNotenKuerzel = (obj.besondereLernleistungNotenKuerzel === undefined) ? null : obj.besondereLernleistungNotenKuerzel === null ? null : obj.besondereLernleistungNotenKuerzel;
		result.besondereLernleistungThema = (obj.besondereLernleistungThema === undefined) ? null : obj.besondereLernleistungThema === null ? null : obj.besondereLernleistungThema;
		result.block1AnzahlKurse = (obj.block1AnzahlKurse === undefined) ? null : obj.block1AnzahlKurse === null ? null : obj.block1AnzahlKurse;
		result.block1DefiziteGesamt = (obj.block1DefiziteGesamt === undefined) ? null : obj.block1DefiziteGesamt === null ? null : obj.block1DefiziteGesamt;
		result.block1DefiziteLK = (obj.block1DefiziteLK === undefined) ? null : obj.block1DefiziteLK === null ? null : obj.block1DefiziteLK;
		result.block1PunktSummeGK = (obj.block1PunktSummeGK === undefined) ? null : obj.block1PunktSummeGK === null ? null : obj.block1PunktSummeGK;
		result.block1PunktSummeLK = (obj.block1PunktSummeLK === undefined) ? null : obj.block1PunktSummeLK === null ? null : obj.block1PunktSummeLK;
		result.block1PunktSummeNormiert = (obj.block1PunktSummeNormiert === undefined) ? null : obj.block1PunktSummeNormiert === null ? null : obj.block1PunktSummeNormiert;
		result.block1NotenpunkteDurchschnitt = (obj.block1NotenpunkteDurchschnitt === undefined) ? null : obj.block1NotenpunkteDurchschnitt === null ? null : obj.block1NotenpunkteDurchschnitt;
		result.block1Zulassung = (obj.block1Zulassung === undefined) ? null : obj.block1Zulassung === null ? null : obj.block1Zulassung;
		if (obj.freiwilligerRuecktritt === undefined)
			throw new Error('invalid json format, missing attribute freiwilligerRuecktritt');
		result.freiwilligerRuecktritt = obj.freiwilligerRuecktritt;
		result.block2DefiziteGesamt = (obj.block2DefiziteGesamt === undefined) ? null : obj.block2DefiziteGesamt === null ? null : obj.block2DefiziteGesamt;
		result.block2DefiziteLK = (obj.block2DefiziteLK === undefined) ? null : obj.block2DefiziteLK === null ? null : obj.block2DefiziteLK;
		result.block2PunktSumme = (obj.block2PunktSumme === undefined) ? null : obj.block2PunktSumme === null ? null : obj.block2PunktSumme;
		result.gesamtPunkte = (obj.gesamtPunkte === undefined) ? null : obj.gesamtPunkte === null ? null : obj.gesamtPunkte;
		result.gesamtPunkteVerbesserung = (obj.gesamtPunkteVerbesserung === undefined) ? null : obj.gesamtPunkteVerbesserung === null ? null : obj.gesamtPunkteVerbesserung;
		result.gesamtPunkteVerschlechterung = (obj.gesamtPunkteVerschlechterung === undefined) ? null : obj.gesamtPunkteVerschlechterung === null ? null : obj.gesamtPunkteVerschlechterung;
		result.pruefungBestanden = (obj.pruefungBestanden === undefined) ? null : obj.pruefungBestanden === null ? null : obj.pruefungBestanden;
		result.note = (obj.note === undefined) ? null : obj.note === null ? null : obj.note;
		return result;
	}

	public static transpilerToJSON(obj : Abiturdaten) : string {
		let result = '{';
		result += '"schuelerID" : ' + obj.schuelerID.toString() + ',';
		result += '"schulform" : ' + JSON.stringify(obj.schulform) + ',';
		result += '"abiturjahr" : ' + obj.abiturjahr.toString() + ',';
		result += '"schuljahrAbitur" : ' + obj.schuljahrAbitur.toString() + ',';
		result += '"bewertetesHalbjahr" : [ ';
		for (let i = 0; i < obj.bewertetesHalbjahr.length; i++) {
			const elem = obj.bewertetesHalbjahr[i];
			result += JSON.stringify(elem);
			if (i < obj.bewertetesHalbjahr.length - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"fachbelegungen" : [ ';
		for (let i = 0; i < obj.fachbelegungen.size(); i++) {
			const elem = obj.fachbelegungen.get(i);
			result += AbiturFachbelegung.transpilerToJSON(elem);
			if (i < obj.fachbelegungen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"sprachendaten" : ' + Sprachendaten.transpilerToJSON(obj.sprachendaten) + ',';
		result += '"bilingualeSprache" : ' + ((obj.bilingualeSprache === null) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
		result += '"latinum" : ' + obj.latinum.toString() + ',';
		result += '"kleinesLatinum" : ' + obj.kleinesLatinum.toString() + ',';
		result += '"graecum" : ' + obj.graecum.toString() + ',';
		result += '"hebraicum" : ' + obj.hebraicum.toString() + ',';
		result += '"block1FehlstundenGesamt" : ' + obj.block1FehlstundenGesamt.toString() + ',';
		result += '"block1FehlstundenUnentschuldigt" : ' + obj.block1FehlstundenUnentschuldigt.toString() + ',';
		result += '"projektKursThema" : ' + ((obj.projektKursThema === null) ? 'null' : JSON.stringify(obj.projektKursThema)) + ',';
		result += '"projektkursLeitfach1Kuerzel" : ' + ((obj.projektkursLeitfach1Kuerzel === null) ? 'null' : JSON.stringify(obj.projektkursLeitfach1Kuerzel)) + ',';
		result += '"projektkursLeitfach2Kuerzel" : ' + ((obj.projektkursLeitfach2Kuerzel === null) ? 'null' : JSON.stringify(obj.projektkursLeitfach2Kuerzel)) + ',';
		result += '"besondereLernleistung" : ' + ((obj.besondereLernleistung === null) ? 'null' : JSON.stringify(obj.besondereLernleistung)) + ',';
		result += '"besondereLernleistungNotenKuerzel" : ' + ((obj.besondereLernleistungNotenKuerzel === null) ? 'null' : JSON.stringify(obj.besondereLernleistungNotenKuerzel)) + ',';
		result += '"besondereLernleistungThema" : ' + ((obj.besondereLernleistungThema === null) ? 'null' : JSON.stringify(obj.besondereLernleistungThema)) + ',';
		result += '"block1AnzahlKurse" : ' + ((obj.block1AnzahlKurse === null) ? 'null' : obj.block1AnzahlKurse.toString()) + ',';
		result += '"block1DefiziteGesamt" : ' + ((obj.block1DefiziteGesamt === null) ? 'null' : obj.block1DefiziteGesamt.toString()) + ',';
		result += '"block1DefiziteLK" : ' + ((obj.block1DefiziteLK === null) ? 'null' : obj.block1DefiziteLK.toString()) + ',';
		result += '"block1PunktSummeGK" : ' + ((obj.block1PunktSummeGK === null) ? 'null' : obj.block1PunktSummeGK.toString()) + ',';
		result += '"block1PunktSummeLK" : ' + ((obj.block1PunktSummeLK === null) ? 'null' : obj.block1PunktSummeLK.toString()) + ',';
		result += '"block1PunktSummeNormiert" : ' + ((obj.block1PunktSummeNormiert === null) ? 'null' : obj.block1PunktSummeNormiert.toString()) + ',';
		result += '"block1NotenpunkteDurchschnitt" : ' + ((obj.block1NotenpunkteDurchschnitt === null) ? 'null' : obj.block1NotenpunkteDurchschnitt.toString()) + ',';
		result += '"block1Zulassung" : ' + ((obj.block1Zulassung === null) ? 'null' : obj.block1Zulassung.toString()) + ',';
		result += '"freiwilligerRuecktritt" : ' + obj.freiwilligerRuecktritt.toString() + ',';
		result += '"block2DefiziteGesamt" : ' + ((obj.block2DefiziteGesamt === null) ? 'null' : obj.block2DefiziteGesamt.toString()) + ',';
		result += '"block2DefiziteLK" : ' + ((obj.block2DefiziteLK === null) ? 'null' : obj.block2DefiziteLK.toString()) + ',';
		result += '"block2PunktSumme" : ' + ((obj.block2PunktSumme === null) ? 'null' : obj.block2PunktSumme.toString()) + ',';
		result += '"gesamtPunkte" : ' + ((obj.gesamtPunkte === null) ? 'null' : obj.gesamtPunkte.toString()) + ',';
		result += '"gesamtPunkteVerbesserung" : ' + ((obj.gesamtPunkteVerbesserung === null) ? 'null' : obj.gesamtPunkteVerbesserung.toString()) + ',';
		result += '"gesamtPunkteVerschlechterung" : ' + ((obj.gesamtPunkteVerschlechterung === null) ? 'null' : obj.gesamtPunkteVerschlechterung.toString()) + ',';
		result += '"pruefungBestanden" : ' + ((obj.pruefungBestanden === null) ? 'null' : obj.pruefungBestanden.toString()) + ',';
		result += '"note" : ' + ((obj.note === null) ? 'null' : JSON.stringify(obj.note)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Abiturdaten>) : string {
		let result = '{';
		if (obj.schuelerID !== undefined) {
			result += '"schuelerID" : ' + obj.schuelerID.toString() + ',';
		}
		if (obj.schulform !== undefined) {
			result += '"schulform" : ' + JSON.stringify(obj.schulform) + ',';
		}
		if (obj.abiturjahr !== undefined) {
			result += '"abiturjahr" : ' + obj.abiturjahr.toString() + ',';
		}
		if (obj.schuljahrAbitur !== undefined) {
			result += '"schuljahrAbitur" : ' + obj.schuljahrAbitur.toString() + ',';
		}
		if (obj.bewertetesHalbjahr !== undefined) {
			const a = obj.bewertetesHalbjahr;
			result += '"bewertetesHalbjahr" : [ ';
			for (let i = 0; i < a.length; i++) {
				const elem = a[i];
				result += JSON.stringify(elem);
				if (i < a.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.fachbelegungen !== undefined) {
			result += '"fachbelegungen" : [ ';
			for (let i = 0; i < obj.fachbelegungen.size(); i++) {
				const elem = obj.fachbelegungen.get(i);
				result += AbiturFachbelegung.transpilerToJSON(elem);
				if (i < obj.fachbelegungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.sprachendaten !== undefined) {
			result += '"sprachendaten" : ' + Sprachendaten.transpilerToJSON(obj.sprachendaten) + ',';
		}
		if (obj.bilingualeSprache !== undefined) {
			result += '"bilingualeSprache" : ' + ((obj.bilingualeSprache === null) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
		}
		if (obj.latinum !== undefined) {
			result += '"latinum" : ' + obj.latinum.toString() + ',';
		}
		if (obj.kleinesLatinum !== undefined) {
			result += '"kleinesLatinum" : ' + obj.kleinesLatinum.toString() + ',';
		}
		if (obj.graecum !== undefined) {
			result += '"graecum" : ' + obj.graecum.toString() + ',';
		}
		if (obj.hebraicum !== undefined) {
			result += '"hebraicum" : ' + obj.hebraicum.toString() + ',';
		}
		if (obj.block1FehlstundenGesamt !== undefined) {
			result += '"block1FehlstundenGesamt" : ' + obj.block1FehlstundenGesamt.toString() + ',';
		}
		if (obj.block1FehlstundenUnentschuldigt !== undefined) {
			result += '"block1FehlstundenUnentschuldigt" : ' + obj.block1FehlstundenUnentschuldigt.toString() + ',';
		}
		if (obj.projektKursThema !== undefined) {
			result += '"projektKursThema" : ' + ((obj.projektKursThema === null) ? 'null' : JSON.stringify(obj.projektKursThema)) + ',';
		}
		if (obj.projektkursLeitfach1Kuerzel !== undefined) {
			result += '"projektkursLeitfach1Kuerzel" : ' + ((obj.projektkursLeitfach1Kuerzel === null) ? 'null' : JSON.stringify(obj.projektkursLeitfach1Kuerzel)) + ',';
		}
		if (obj.projektkursLeitfach2Kuerzel !== undefined) {
			result += '"projektkursLeitfach2Kuerzel" : ' + ((obj.projektkursLeitfach2Kuerzel === null) ? 'null' : JSON.stringify(obj.projektkursLeitfach2Kuerzel)) + ',';
		}
		if (obj.besondereLernleistung !== undefined) {
			result += '"besondereLernleistung" : ' + ((obj.besondereLernleistung === null) ? 'null' : JSON.stringify(obj.besondereLernleistung)) + ',';
		}
		if (obj.besondereLernleistungNotenKuerzel !== undefined) {
			result += '"besondereLernleistungNotenKuerzel" : ' + ((obj.besondereLernleistungNotenKuerzel === null) ? 'null' : JSON.stringify(obj.besondereLernleistungNotenKuerzel)) + ',';
		}
		if (obj.besondereLernleistungThema !== undefined) {
			result += '"besondereLernleistungThema" : ' + ((obj.besondereLernleistungThema === null) ? 'null' : JSON.stringify(obj.besondereLernleistungThema)) + ',';
		}
		if (obj.block1AnzahlKurse !== undefined) {
			result += '"block1AnzahlKurse" : ' + ((obj.block1AnzahlKurse === null) ? 'null' : obj.block1AnzahlKurse.toString()) + ',';
		}
		if (obj.block1DefiziteGesamt !== undefined) {
			result += '"block1DefiziteGesamt" : ' + ((obj.block1DefiziteGesamt === null) ? 'null' : obj.block1DefiziteGesamt.toString()) + ',';
		}
		if (obj.block1DefiziteLK !== undefined) {
			result += '"block1DefiziteLK" : ' + ((obj.block1DefiziteLK === null) ? 'null' : obj.block1DefiziteLK.toString()) + ',';
		}
		if (obj.block1PunktSummeGK !== undefined) {
			result += '"block1PunktSummeGK" : ' + ((obj.block1PunktSummeGK === null) ? 'null' : obj.block1PunktSummeGK.toString()) + ',';
		}
		if (obj.block1PunktSummeLK !== undefined) {
			result += '"block1PunktSummeLK" : ' + ((obj.block1PunktSummeLK === null) ? 'null' : obj.block1PunktSummeLK.toString()) + ',';
		}
		if (obj.block1PunktSummeNormiert !== undefined) {
			result += '"block1PunktSummeNormiert" : ' + ((obj.block1PunktSummeNormiert === null) ? 'null' : obj.block1PunktSummeNormiert.toString()) + ',';
		}
		if (obj.block1NotenpunkteDurchschnitt !== undefined) {
			result += '"block1NotenpunkteDurchschnitt" : ' + ((obj.block1NotenpunkteDurchschnitt === null) ? 'null' : obj.block1NotenpunkteDurchschnitt.toString()) + ',';
		}
		if (obj.block1Zulassung !== undefined) {
			result += '"block1Zulassung" : ' + ((obj.block1Zulassung === null) ? 'null' : obj.block1Zulassung.toString()) + ',';
		}
		if (obj.freiwilligerRuecktritt !== undefined) {
			result += '"freiwilligerRuecktritt" : ' + obj.freiwilligerRuecktritt.toString() + ',';
		}
		if (obj.block2DefiziteGesamt !== undefined) {
			result += '"block2DefiziteGesamt" : ' + ((obj.block2DefiziteGesamt === null) ? 'null' : obj.block2DefiziteGesamt.toString()) + ',';
		}
		if (obj.block2DefiziteLK !== undefined) {
			result += '"block2DefiziteLK" : ' + ((obj.block2DefiziteLK === null) ? 'null' : obj.block2DefiziteLK.toString()) + ',';
		}
		if (obj.block2PunktSumme !== undefined) {
			result += '"block2PunktSumme" : ' + ((obj.block2PunktSumme === null) ? 'null' : obj.block2PunktSumme.toString()) + ',';
		}
		if (obj.gesamtPunkte !== undefined) {
			result += '"gesamtPunkte" : ' + ((obj.gesamtPunkte === null) ? 'null' : obj.gesamtPunkte.toString()) + ',';
		}
		if (obj.gesamtPunkteVerbesserung !== undefined) {
			result += '"gesamtPunkteVerbesserung" : ' + ((obj.gesamtPunkteVerbesserung === null) ? 'null' : obj.gesamtPunkteVerbesserung.toString()) + ',';
		}
		if (obj.gesamtPunkteVerschlechterung !== undefined) {
			result += '"gesamtPunkteVerschlechterung" : ' + ((obj.gesamtPunkteVerschlechterung === null) ? 'null' : obj.gesamtPunkteVerschlechterung.toString()) + ',';
		}
		if (obj.pruefungBestanden !== undefined) {
			result += '"pruefungBestanden" : ' + ((obj.pruefungBestanden === null) ? 'null' : obj.pruefungBestanden.toString()) + ',';
		}
		if (obj.note !== undefined) {
			result += '"note" : ' + ((obj.note === null) ? 'null' : JSON.stringify(obj.note)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_Abiturdaten(obj : unknown) : Abiturdaten {
	return obj as Abiturdaten;
}
