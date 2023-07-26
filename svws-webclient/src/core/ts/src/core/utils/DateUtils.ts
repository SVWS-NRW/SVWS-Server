import { JavaObject } from '../../java/lang/JavaObject';
import { StringUtils } from '../../core/utils/StringUtils';
import { DeveloperNotificationException } from '../../core/exceptions/DeveloperNotificationException';

export class DateUtils extends JavaObject {

	/**
	 * Das kleinste gültige Jahr für das alle Datumsberechnungen geprüft wurden.
	 */
	public static readonly MIN_GUELTIGES_JAHR : number = 1900;

	/**
	 * Das größte gültige Jahr für das alle Datumsberechnungen geprüft wurden.
	 */
	public static readonly MAX_GUELTIGES_JAHR : number = 2900;

	/**
	 *  Liefert für den jeweiligen Monat im Jahr die Summe der vergangenen Tage.<br>
	 *  [0][3] bedeutet, dass im März bereits 59 Tage vergangen sind (kein Schaltjahr).<br>
	 *  [1][3] bedeutet, dass im März bereits 60 Tage vergangen sind (Schaltjahr).
	 */
	private static readonly monat_zu_vergangene_tage : Array<Array<number>> = [[0, 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334], [0, 0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335]];


	private constructor() {
		super();
	}

	/**
	 * Liefert ein aus dem Datum extrahiertes int[] mit dem Inhalt [jahr, monat, tagImMonat, tagInWoche, tagImJahr, kalenderwoche, kalenderwochenjahr].<br>
	 * <br>
	 * Gültige Werte sind im Bereich:<br>
	 * Index 0: jahr = 1900 bis 2900<br>
	 * Index 1: monat = 1 (Januar) bis 12 (Dezember)<br>
	 * Index 2: tagImMonat = 1 bis 31<br>
	 * Index 3: tagInWoche = 1 (Montag) bis 7 (Sonntag)<br>
	 * Index 4: tagImJahr = 1 bis 365 oder 366 <br>
	 * Index 5: kalenderwoche = 1 bis 52 oder 53<br>
	 * Index 6: kalenderwochenjahr = 1900 bis 2900 (das Jahr der Kalenderwoche)<br>
	 * <br>
	 * Auszug aus Wikipedia: Da ISO 8601 den Montag als ersten Tag der Woche definiert, ist dies somit die erste Woche,
	 * von der mehr Tage (mindestens vier) auf das neue Jahr fallen als auf das alte Jahr.
	 *  Äquivalent hierzu sind die folgenden Definitionen:<br>
	 * - jene Woche, die den 4. Januar enthält<br>
	 * - jene Woche, die den 1. Januar enthält, falls dieser ein Montag, Dienstag, Mittwoch oder Donnerstag ist, sonst die darauf folgende Woche
	 *
	 * @param datumISO8601 Das Datum im ISO8601-Format uuuu-MM-dd (z.B. 2014-03-14).
	 *
	 * @return ein aus dem Datum extrahiertes int[] mit dem Inhalt [jahr, monat, tagImMonat, tagInWoche, tagImJahr, kalenderwoche, kalenderwochen, kalenderwochenjahr]..
	 * @see <a href="https://de.wikipedia.org/wiki/Woche#Kalenderwoche">https://de.wikipedia.org/wiki/Woche#Kalenderwoche</a>
	 */
	public static extractFromDateISO8601(datumISO8601 : string) : Array<number> {
		const split : Array<string | null> = datumISO8601.split("-");
		DeveloperNotificationException.ifTrue("Datumsformat von " + datumISO8601! + " ist nicht ISO8601 konform!", split.length !== 3);
		const jahr : number = DeveloperNotificationException.ifNotInt(split[0]);
		DeveloperNotificationException.ifTrue("Das Jahr von " + datumISO8601! + " ist ungültig!", DateUtils.gibIstJahrUngueltig(jahr));
		const monat : number = DeveloperNotificationException.ifNotInt(split[1]);
		DeveloperNotificationException.ifTrue("Der Monat von " + datumISO8601! + " ist ungültig!", (monat < 1) || (monat > 12));
		const tagImMonat : number = DeveloperNotificationException.ifNotInt(split[2]);
		DeveloperNotificationException.ifTrue("Der Tag von " + datumISO8601! + " ist ungültig!", (tagImMonat < 1) || (tagImMonat > 31));
		const schalttage1 : number = (Math.trunc((jahr - 1) / 4)) - (Math.trunc((jahr - 1) / 100)) + (Math.trunc((jahr - 1) / 400));
		const schalttage2 : number = (Math.trunc(jahr / 4)) - (Math.trunc(jahr / 100)) + (Math.trunc(jahr / 400));
		const schaltjahr : number = schalttage2 - schalttage1;
		const tagImJahr : number = DateUtils.monat_zu_vergangene_tage[schaltjahr][monat] + tagImMonat;
		const tagInWoche : number = (jahr + schalttage1 + tagImJahr + 5) % 7 + 1;
		const tagImJahrAmJanuar4 : number = 4;
		const wochentagAmJanuar4 : number = (jahr + schalttage1 + tagImJahrAmJanuar4 + 5) % 7 + 1;
		const tagImJahrAmMontagDerKW1 : number = tagImJahrAmJanuar4 - wochentagAmJanuar4 + 1;
		const kalenderwochen : number = DateUtils.gibKalenderwochenOfJahr(jahr);
		let kalenderwochenjahr : number = jahr;
		let kalenderwoche : number = 1 + Math.trunc((tagImJahr - tagImJahrAmMontagDerKW1) / 7);
		if (kalenderwoche > kalenderwochen) {
			kalenderwoche = 1;
			kalenderwochenjahr++;
		}
		if (tagImJahr < tagImJahrAmMontagDerKW1) {
			kalenderwoche = DateUtils.gibKalenderwochenOfJahr(jahr - 1);
			kalenderwochenjahr--;
		}
		return [jahr, monat, tagImMonat, tagInWoche, tagImJahr, kalenderwoche, kalenderwochenjahr];
	}

	/**
	 * Liefert die Anzahl an Kalenderwochen des Jahres (52 oder 53) nach ISO8601.
	 *
	 * @param jahr Das Jahr.
	 *
	 * @return die Anzahl an Kalenderwochen des Jahres (52 oder 53) nach ISO8601.
	 */
	public static gibKalenderwochenOfJahr(jahr : number) : number {
		const schalttage1 : number = (Math.trunc((jahr - 1) / 4)) - (Math.trunc((jahr - 1) / 100)) + (Math.trunc((jahr - 1) / 400));
		const schalttage2 : number = (Math.trunc(jahr / 4)) - (Math.trunc(jahr / 100)) + (Math.trunc(jahr / 400));
		const schaltjahr : number = schalttage2 - schalttage1;
		const wochentagAmJanuar1 : number = (jahr + schalttage1 + 1 + 5) % 7 + 1;
		return (wochentagAmJanuar1 === 4) || ((schaltjahr === 1) && (wochentagAmJanuar1 === 3)) ? 53 : 52;
	}

	/**
	 * Liefert die Anzahl an Tagen des Jahres (365 oder 366).
	 *
	 * @param jahr Das Jahr.
	 *
	 * @return die Anzahl an Tagen des Jahres (365 oder 366).
	 */
	public static gibTageOfJahr(jahr : number) : number {
		const schalttage1 : number = (Math.trunc((jahr - 1) / 4)) - (Math.trunc((jahr - 1) / 100)) + (Math.trunc((jahr - 1) / 400));
		const schalttage2 : number = (Math.trunc(jahr / 4)) - (Math.trunc(jahr / 100)) + (Math.trunc(jahr / 400));
		const schaltjahr : number = schalttage2 - schalttage1;
		return 365 + schaltjahr;
	}

	/**
	 * Liefert das Datum im ISO8601-Format (uuuu-MM-dd)des Montags der Kalenderwoche des Jahres.
	 * <br>Hinweis: Der Montag kann bei der 1. KW im Vorjahr liegen!
	 * <br>Beispiel 1: Der Montag der 1. KW im 2023 ist der 02.01.2023
	 * <br>Beispiel 2: Der Montag der 1. KW im 2024 ist der 01.01.2024
	 * <br>Beispiel 3: Der Montag der 1. KW im 2025 ist der 30.12.2023!
	 *
	 * @param kalenderwochenjahr  Das Jahr der Kalenderwoche.
	 * @param kalenderwoche       Die Kalenderwoche.
	 *
	 * @return das Datum im ISO8601-Format (uuuu-MM-dd) des Montags der Kalenderwoche des Jahres.
	 */
	public static gibDatumDesMontagsOfJahrAndKalenderwoche(kalenderwochenjahr : number, kalenderwoche : number) : string {
		DeveloperNotificationException.ifTrue("kalenderwoche < 1", kalenderwoche < 1);
		DeveloperNotificationException.ifTrue("kalenderwoche > gibKalenderwochenOfJahr(jahr)", kalenderwoche > DateUtils.gibKalenderwochenOfJahr(kalenderwochenjahr));
		const schalttage1 : number = (Math.trunc((kalenderwochenjahr - 1) / 4)) - (Math.trunc((kalenderwochenjahr - 1) / 100)) + (Math.trunc((kalenderwochenjahr - 1) / 400));
		const tagImJahrAmJanuar4 : number = 4;
		const wochentagAmJanuar4 : number = (kalenderwochenjahr + schalttage1 + tagImJahrAmJanuar4 + 5) % 7 + 1;
		const tagImJahr : number = 7 * kalenderwoche - 2 - wochentagAmJanuar4;
		return DateUtils.gibDatumDesTagesOfJahr(kalenderwochenjahr, tagImJahr);
	}

	/**
	 * Liefert das Datum im ISO8601-Format (uuuu-MM-dd) des Sonntags der Kalenderwoche des Jahres.
	 * <br>Hinweis: Der Sonntag kann bei der 52/53. KW im Folgejahr liegen!
	 * <br>Beispiel 1: Der Sonntag der 52. KW im 2023 ist der 31.12.2023
	 * <br>Beispiel 2: Der Sonntag der 52. KW im 2024 ist der 29.12.2024
	 * <br>Beispiel 3: Der Sonntag der 52. KW im 2025 ist der 28.12.2025
	 * <br>Beispiel 4: Der Sonntag der 53. KW im 2026 ist der 03.01.2027!
	 * <br>Beispiel 5: Der Sonntag der 52. KW im 2027 ist der 02.01.2028!
	 *
	 * @param kalenderwochenjahr  Das Jahr der Kalenderwoche.
	 * @param kalenderwoche       Die Kalenderwoche.
	 *
	 * @return das Datum im ISO8601-Format (uuuu-MM-dd) des Sonntags der Kalenderwoche des Jahres.
	 */
	public static gibDatumDesSonntagsOfJahrAndKalenderwoche(kalenderwochenjahr : number, kalenderwoche : number) : string {
		DeveloperNotificationException.ifTrue("kalenderwoche < 1", kalenderwoche < 1);
		DeveloperNotificationException.ifTrue("kalenderwoche > gibKalenderwochenOfJahr(jahr)", kalenderwoche > DateUtils.gibKalenderwochenOfJahr(kalenderwochenjahr));
		const schalttage1 : number = (Math.trunc((kalenderwochenjahr - 1) / 4)) - (Math.trunc((kalenderwochenjahr - 1) / 100)) + (Math.trunc((kalenderwochenjahr - 1) / 400));
		const tagImJahrAmJanuar4 : number = 4;
		const wochentagAmJanuar4 : number = (kalenderwochenjahr + schalttage1 + tagImJahrAmJanuar4 + 5) % 7 + 1;
		const tagImJahr : number = 7 * kalenderwoche + 4 - wochentagAmJanuar4;
		return DateUtils.gibDatumDesTagesOfJahr(kalenderwochenjahr, tagImJahr);
	}

	/**
	 * Liefert das Datum im ISO8601-Format (uuuu-MM-dd) eines Tages im Jahres.
	 * <br> Hinweis: Die Methode erlaubt Werte die zum Vorjahr oder Folgejahr führen.
	 *
	 * @param jahr       Das Jahr.
	 * @param tagImJahr  Der jeweilige Tag im Jahr. Meistens zwischen 1 und 365, aber nicht zwingend!
	 *
	 * @return das Datum im ISO8601-Format (uuuu-MM-dd) eines Tages im Jahres.
	 */
	public static gibDatumDesTagesOfJahr(jahr : number, tagImJahr : number) : string {
		let j : number = jahr;
		let t : number = tagImJahr;
		if (t <= 0) {
			j--;
			t = t + DateUtils.gibTageOfJahr(j);
		}
		DeveloperNotificationException.ifTrue("Man kann maximal ins Vorjahr springen!", t <= 0);
		if (t > DateUtils.gibTageOfJahr(j)) {
			t = t - DateUtils.gibTageOfJahr(j);
			j++;
		}
		const tageDesJahres : number = DateUtils.gibTageOfJahr(j);
		DeveloperNotificationException.ifTrue("Man kann maximal ins Folgejahr springen!", t > tageDesJahres);
		const vergangeneTage : Array<number> | null = (tageDesJahres === 365) ? DateUtils.monat_zu_vergangene_tage[0] : DateUtils.monat_zu_vergangene_tage[1];
		let monat : number = 12;
		for (let i : number = 2; i <= 12; i++)
			if (vergangeneTage[i] >= t) {
				monat = i - 1;
				break;
			}
		const tagDesMonats : number = t - vergangeneTage[monat];
		return StringUtils.padZahl(j, 4)! + "-" + StringUtils.padZahl(monat, 2)! + "-" + StringUtils.padZahl(tagDesMonats, 2)!;
	}

	/**
	 * Liefert TRUE, falls das Jahr ungültig ist.
	 *
	 * @param jahr  Das Jahr.
	 *
	 * @return TRUE, falls das Jahr ungültig ist.
	 */
	public static gibIstJahrUngueltig(jahr : number) : boolean {
		return (jahr < DateUtils.MIN_GUELTIGES_JAHR) || (jahr > DateUtils.MAX_GUELTIGES_JAHR);
	}

	/**
	 * Liefert anhand der Minuten eine String-Repräsentation der Uhrzeit im Format "hh:mm".
	 * <br>Beispiel: 1000 Minuten --> "16:40"
	 *
	 * @param minuten  Die vergangenen Minuten seit 0 Uhr.
	 *
	 * @return anhand der Minuten eine String-Repräsentation der Uhrzeit im Format "hh:mm".
	 */
	public static getStringOfUhrzeitFromMinuten(minuten : number) : string {
		DeveloperNotificationException.ifSmaller("minuten", minuten, 0);
		DeveloperNotificationException.ifGreater("minuten", minuten, 24 * 60);
		const h : number = Math.trunc(minuten / 60);
		const m : number = minuten - h * 60;
		const sStunden : string | null = (h < 10 ? "0" : "") + h;
		const sMinuten : string | null = (m < 10 ? "0" : "") + m;
		return sStunden! + ":" + sMinuten!;
	}

	/**
	 * Liefert das nach "DIN 5008 optional" konvertierte Datumsformat, z.B. 2023-02-28 zu 28.02.2023.
	 *
	 * @param datumISO8601 Das Datum im ISO8601-Format uuuu-MM-dd (z.B. 2023-02-28).
	 *
	 * @return das nach "DIN 5008 optional" konvertierte Datumsformat, z.B. 2023-02-28 zu 28.02.2023.
	 */
	public static gibDatumGermanFormat(datumISO8601 : string) : string {
		const info : Array<number> = DateUtils.extractFromDateISO8601(datumISO8601);
		const jahr : number = info[0];
		const monat : number = info[1];
		const tagImMonat : number = info[2];
		return StringUtils.padZahl(tagImMonat, 2)! + "." + StringUtils.padZahl(monat, 2)! + "." + StringUtils.padZahl(jahr, 4)!;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.DateUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_DateUtils(obj : unknown) : DateUtils {
	return obj as DateUtils;
}
