import { JavaObject } from '../../java/lang/JavaObject';
import { JavaInteger } from '../../java/lang/JavaInteger';
import { StringBuilder } from '../../java/lang/StringBuilder';
import { StringUtils } from '../../core/utils/StringUtils';
import { Class } from '../../java/lang/Class';
import { DeveloperNotificationException } from '../../core/exceptions/DeveloperNotificationException';

export class DateUtils extends JavaObject {

	/**
	 * Die Anzahl der Tage zwischen dem Jahr 0 und dem Jahr 1.1.1970
	 */
	public static readonly DAYS_FROM_0_TO_1970 : number = 719528;

	/**
	 * Die Anzahl an Tagen in 400 Jahren
	 */
	public static readonly DAYS_PER_400_YEARS : number = 146097;

	/**
	 * Die Anzahl an Tagen in 100 Jahren, nicht der Speziallfall, wenn ein Jahrhundert mit einem Schaltjahr beginnt
	 */
	public static readonly DAYS_PER_100_YEARS : number = 36524;

	/**
	 * Die Anzahl an Tagen in 4 Jahren
	 */
	public static readonly DAYS_PER_4_YEARS : number = 1461;

	/**
	 * Die Anzahl an Tagen in einem Jahr, welches kein Schaltjahr ist
	 */
	public static readonly DAYS_PER_YEAR : number = 365;

	/**
	 * Die Anzahl an Tagen in einem Schaltjahr
	 */
	public static readonly DAYS_PER_LEAP_YEAR : number = 366;

	/**
	 * Das kleinste gültige Jahr für das alle Datumsberechnungen geprüft wurden.
	 */
	public static readonly MIN_GUELTIGES_JAHR : number = 1900;

	/**
	 * Das größte gültige Jahr für das alle Datumsberechnungen geprüft wurden.
	 */
	public static readonly MAX_GUELTIGES_JAHR : number = 2900;

	/**
	 * Ein Mapping für den Monat als Zahl zu seiner textuellen Beschreibung.
	 */
	private static readonly MONAT_ZU_TEXT : Array<string | null> = ["", "Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember"];

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
		const schalttage1 : number = ((Math.trunc((jahr - 1) / 4)) - (Math.trunc((jahr - 1) / 100))) + (Math.trunc((jahr - 1) / 400));
		const schalttage2 : number = ((Math.trunc(jahr / 4)) - (Math.trunc(jahr / 100))) + (Math.trunc(jahr / 400));
		const schaltjahr : number = schalttage2 - schalttage1;
		const tagImJahr : number = DateUtils.monat_zu_vergangene_tage[schaltjahr][monat] + tagImMonat;
		const tagInWoche : number = ((jahr + schalttage1 + tagImJahr + 5) % 7) + 1;
		const tagImJahrAmJanuar4 : number = 4;
		const wochentagAmJanuar4 : number = ((jahr + schalttage1 + tagImJahrAmJanuar4 + 5) % 7) + 1;
		const tagImJahrAmMontagDerKW1 : number = (tagImJahrAmJanuar4 - wochentagAmJanuar4) + 1;
		const kalenderwochen : number = DateUtils.gibKalenderwochenOfJahr(jahr);
		let kalenderwochenjahr : number = jahr;
		let kalenderwoche : number = 1 + (Math.trunc((tagImJahr - tagImJahrAmMontagDerKW1) / 7));
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
		const schalttage1 : number = ((Math.trunc((jahr - 1) / 4)) - (Math.trunc((jahr - 1) / 100))) + (Math.trunc((jahr - 1) / 400));
		const schalttage2 : number = ((Math.trunc(jahr / 4)) - (Math.trunc(jahr / 100))) + (Math.trunc(jahr / 400));
		const schaltjahr : number = schalttage2 - schalttage1;
		const wochentagAmJanuar1 : number = ((jahr + schalttage1 + 1 + 5) % 7) + 1;
		return ((wochentagAmJanuar1 === 4) || ((schaltjahr === 1) && (wochentagAmJanuar1 === 3))) ? 53 : 52;
	}

	/**
	 * Liefert die Anzahl an Tagen des Jahres (365 oder 366).
	 *
	 * @param jahr Das Jahr.
	 *
	 * @return die Anzahl an Tagen des Jahres (365 oder 366).
	 */
	public static gibTageOfJahr(jahr : number) : number {
		const schalttage1 : number = ((Math.trunc((jahr - 1) / 4)) - (Math.trunc((jahr - 1) / 100))) + (Math.trunc((jahr - 1) / 400));
		const schalttage2 : number = ((Math.trunc(jahr / 4)) - (Math.trunc(jahr / 100))) + (Math.trunc(jahr / 400));
		const schaltjahr : number = schalttage2 - schalttage1;
		return 365 + schaltjahr;
	}

	/**
	 * Liefert das Datum im ISO8601-Format (uuuu-MM-dd) des Wochentags (aus dem Intervall 1 bis 7) der Kalenderwoche des Kalenderwochenjahres.
	 * <br>Hinweis: Der Montag kann bei der 1. KW im Vorjahr liegen!
	 *
	 * @param kalenderwochenjahr  Das Jahr der Kalenderwoche.
	 * @param kalenderwoche       Die Kalenderwoche.
	 * @param wochentag           Der Wochentag aus dem Intervall 1 bis 7.
	 *
	 * @return das Datum im ISO8601-Format (uuuu-MM-dd) des Wochentags (aus dem Intervall 1 bis 7) der Kalenderwoche des Kalenderwochenjahres.
	 */
	public static gibDatumDesWochentagsOfJahrAndKalenderwoche(kalenderwochenjahr : number, kalenderwoche : number, wochentag : number) : string {
		DeveloperNotificationException.ifTrue("kalenderwoche < 1", kalenderwoche < 1);
		DeveloperNotificationException.ifTrue("kalenderwoche > gibKalenderwochenOfJahr(jahr)", kalenderwoche > DateUtils.gibKalenderwochenOfJahr(kalenderwochenjahr));
		DeveloperNotificationException.ifTrue("(wochentag < 1) || (wochentag > 7)", (wochentag < 1) || (wochentag > 7));
		const schalttage1 : number = ((Math.trunc((kalenderwochenjahr - 1) / 4)) - (Math.trunc((kalenderwochenjahr - 1) / 100))) + (Math.trunc((kalenderwochenjahr - 1) / 400));
		const tagImJahrAmJanuar4 : number = 4;
		const wochentagAmJanuar4 : number = ((kalenderwochenjahr + schalttage1 + tagImJahrAmJanuar4 + 5) % 7) + 1;
		const tagImJahr : number = (((7 * kalenderwoche) - wochentagAmJanuar4) + wochentag) - 3;
		return DateUtils.gibDatumDesTagesOfJahr(kalenderwochenjahr, tagImJahr);
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
		return DateUtils.gibDatumDesWochentagsOfJahrAndKalenderwoche(kalenderwochenjahr, kalenderwoche, 1);
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
		return DateUtils.gibDatumDesWochentagsOfJahrAndKalenderwoche(kalenderwochenjahr, kalenderwoche, 7);
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

	/**
	 * Liefert das vom Format "2006-08-31" ins Format "31. August 2006" konvertierte Datum.
	 *
	 * @param datumISO8601 Das Datum im ISO8601-Format uuuu-MM-dd (z.B. 2023-02-28).
	 *
	 * @return das vom Format "2006-08-31" ins Format "31. August 2006" konvertierte Datum.
	 */
	public static gibDatumGermanFormatAusgeschrieben(datumISO8601 : string) : string {
		const info : Array<number> = DateUtils.extractFromDateISO8601(datumISO8601);
		const jahr : number = info[0];
		const monat : number = info[1];
		const tagImMonat : number = info[2];
		return tagImMonat + ". " + DateUtils.MONAT_ZU_TEXT[monat] + " " + StringUtils.padZahl(jahr, 4)!;
	}

	/**
	 * Liefert die Minuten einer Zeitangabe im Format hh:mm oder hh.mm.
	 * <br>hh muss ein- oder zweistellig sein, im Bereich 0 bis 23.
	 * <br>mm muss ein- oder zweistellig sein, im Bereich 0 bis 59.
	 *
	 * @param zeit  Die Zeitangabe im Format hh:mm oder hh.mm.
	 *
	 * @return die Minuten einer Zeitangabe im Format hh:mm oder hh.mm.
	 */
	public static gibMinutenOfZeitAsString(zeit : string) : number {
		let sSplit : Array<string> = zeit.split(":");
		if (sSplit.length !== 2)
			sSplit = zeit.split(".");
		DeveloperNotificationException.ifTrue("Zeit muss im Format hh:mm oder hh.mm sein!", sSplit.length !== 2);
		const sStunden : string = sSplit[0].trim();
		const sMinuten : string = sSplit[1].trim();
		DeveloperNotificationException.ifTrue("Zeit muss im Format hh:mm oder hh.mm sein!", (sStunden.length < 1) || (sStunden.length > 2));
		DeveloperNotificationException.ifTrue("Zeit muss im Format hh:mm oder hh.mm sein!", (sMinuten.length < 1) || (sMinuten.length > 2));
		const stunden : number = JavaInteger.parseInt(sStunden);
		const minuten : number = JavaInteger.parseInt(sMinuten);
		DeveloperNotificationException.ifTrue("(stunden < 0) || (stunden > 23)", (stunden < 0) || (stunden > 23));
		DeveloperNotificationException.ifTrue("(minuten < 0) || (minuten > 59)", (minuten < 0) || (minuten > 59));
		return (stunden * 60) + minuten;
	}

	/**
	 * Liefert den ZeitString im Format hh:mm zu einer vorgegebenen Minutenanzahl.
	 * <br>Gültige Werte sind im Bereich 0 bis 24*60=1440 (exklusive).
	 *
	 * @param minuten  Die Anzahl der Minuten.
	 *
	 * @return den ZeitString im Format hh:mm zu einer vorgegebenen Minutenanzahl.
	 */
	public static gibZeitStringOfMinuten(minuten : number) : string {
		DeveloperNotificationException.ifTrue("(minuten < 0) || (minuten >= 1440)", (minuten < 0) || (minuten >= 1440));
		const std : number = Math.trunc(minuten / 60);
		const min : number = minuten - (std * 60);
		const sStd : string = (std < 10 ? "0" : "") + std;
		const sMin : string = (min < 10 ? "0" : "") + min;
		return sStd! + ":" + sMin!;
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
		return DateUtils.gibZeitStringOfMinuten(minuten);
	}

	/**
	 * Liefert die Kalenderwoche zu einem bestimmten Datum.
	 *
	 * @param datumISO8601 Das Datum im ISO8601-Format uuuu-MM-dd (z.B. 2014-03-14).
	 *
	 * @return die Kalenderwoche zu einem bestimmten Datum.
	 */
	public static gibKwDesDatumsISO8601(datumISO8601 : string) : number {
		return DateUtils.extractFromDateISO8601(datumISO8601)[5];
	}

	/**
	 * Liefert das Kalenderwochenjahr zu einem bestimmten Datum.
	 *
	 * @param datumISO8601   das Datum im ISO8601-Format uuuu-MM-dd (z.B. 2014-03-14).
	 *
	 * @return die Kalenderwochenjahr zu einem bestimmten Datum.
	 */
	public static gibKwJahrDesDatumsISO8601(datumISO8601 : string) : number {
		return DateUtils.extractFromDateISO8601(datumISO8601)[6];
	}

	/**
	 * Liefert den Wochentag (Mo=1...So=7) zu einem bestimmten Datum.
	 *
	 * @param datumISO8601   das Datum im ISO8601-Format uuuu-MM-dd (z.B. 2014-03-14).
	 *
	 * @return den Wochentag (Mo=1...So=7) zu einem bestimmten Datum.
	 */
	public static gibWochentagDesDatumsISO8601(datumISO8601 : string) : number {
		return DateUtils.extractFromDateISO8601(datumISO8601)[3];
	}

	/**
	 * Liefert das Schuljahr zu einem bestimmten Datum. Dabei wird von dem Stichtag des
	 * 1.8. für den Beginn des neuen Schuljahres ausgegangen. Da das Schuljahr immer in
	 * zwei Kalenderjahren liegt, wird immer das erste Kalenderjahr als Schuljahr angegeben.
	 *
	 * @param datumISO8601   das Datum im ISO8601-Format uuuu-MM-dd (z.B. 2014-03-14).
	 *
	 * @return das Schuljahr
	 */
	public static getSchuljahrFromDateISO8601(datumISO8601 : string) : number {
		const iso8601 : Array<number> | null = DateUtils.extractFromDateISO8601(datumISO8601);
		return (iso8601[1] > 7) ? iso8601[0] : (iso8601[0] - 1);
	}

	/**
	 * Liefert das Halbjahr zu einem bestimmten Datum. Dabei wird zum Einen von dem Stichtag des
	 * 1.8. für den Beginn des neuen Schuljahres ausgegangen und zum Anderen wird vereinfacht (!)
	 * vom 1.2. als Beginn des neuen Halbjahres ausgegangen.
	 *
	 * @param datumISO8601   das Datum im ISO8601-Format uuuu-MM-dd (z.B. 2014-03-14).
	 *
	 * @return das Halbjahr anhand des vereinfachten Kriteriums
	 */
	public static getHalbjahrFromDateISO8601(datumISO8601 : string) : number {
		const iso8601 : Array<number> | null = DateUtils.extractFromDateISO8601(datumISO8601);
		return ((iso8601[1] > 1) && (iso8601[1] < 8)) ? 2 : 1;
	}

	/**
	 * Liefert das Schuljahr und das Halbjahr zu einem bestimmten Datum.
	 * Dabei wird zum Einen von dem Stichtag des 1.8. für den Beginn des neuen Schuljahres
	 * ausgegangen und zum Anderen wird vereinfacht (!) vom 1.2. als Beginn des neuen
	 * Halbjahres ausgegangen.
	 *
	 * @param datumISO8601   das Datum im ISO8601-Format uuuu-MM-dd (z.B. 2014-03-14).
	 *
	 * @return das Schuljahr (Index 0) und das Halbjahr (Index 1) anhand des vereinfachten Kriteriums
	 */
	public static getSchuljahrUndHalbjahrFromDateISO8601(datumISO8601 : string) : Array<number> | null {
		const iso8601 : Array<number> | null = DateUtils.extractFromDateISO8601(datumISO8601);
		const result : Array<number> | null = Array(2).fill(0);
		result[0] = (iso8601[1] > 7) ? iso8601[0] : (iso8601[0] - 1);
		result[1] = ((iso8601[1] > 1) && (iso8601[1] < 8)) ? 2 : 1;
		return result;
	}

	/**
	 * Gibt den übergebenen Unix-Zeitstempel (Millisekunden seit dem 1.1.1970 um 0 Uhr) in der
	 * ISO-8601-Darstellung {@code uuuu-MM-dd'T'HH:mm:ss.SSS} als String zurück.
	 *
	 * @param time   der Zeitstempel
	 *
	 * @return der String mit der ISO-8601-Darstellung des Zeitstempels
	 */
	public static toISO8601(time : number) : string | null {
		const s : StringBuilder | null = new StringBuilder();
		const days : number = DateUtils.DAYS_FROM_0_TO_1970 + Math.trunc(time / 86400000);
		const years400 : number = Math.trunc(days / DateUtils.DAYS_PER_400_YEARS);
		const daysLeft400 : number = days - years400 * DateUtils.DAYS_PER_400_YEARS;
		const years100 : number = Math.trunc((daysLeft400 - 1) / DateUtils.DAYS_PER_100_YEARS);
		const daysLeft100 : number = daysLeft400 - years100 * DateUtils.DAYS_PER_100_YEARS;
		const years4 : number = Math.trunc(daysLeft100 / DateUtils.DAYS_PER_4_YEARS);
		const years1 : number = Math.trunc((daysLeft100 - 1) / DateUtils.DAYS_PER_YEAR) % 4;
		const year : number = years400 * 400 + years100 * 100 + years4 * 4 + years1;
		const isLeapYear : boolean = (years1 === 0) && ((years100 === 0) || (years4 !== 0));
		let day : number = (daysLeft400 - 60) - years100 * DateUtils.DAYS_PER_100_YEARS - years4 * DateUtils.DAYS_PER_4_YEARS - years1 * DateUtils.DAYS_PER_YEAR;
		if (day < 0)
			day += (isLeapYear ? DateUtils.DAYS_PER_LEAP_YEAR : DateUtils.DAYS_PER_YEAR);
		const m5 : number = Math.trunc(day / 153);
		day -= m5 * 153;
		const m2 : number = Math.trunc(day / 61);
		day -= m2 * 61;
		const m1 : number = Math.trunc(day / 31);
		day -= m1 * 31;
		const month : number = (((m5 * 5 + m2 * 2 + m1) + 2) % 12) + 1;
		if (year < 1000)
			s.append("0");
		if (year < 100)
			s.append("0");
		if (year < 10)
			s.append("0");
		s.append(year);
		s.append("-");
		if (month < 10)
			s.append("0");
		s.append(month);
		s.append("-");
		if (day < 10)
			s.append("0");
		s.append(day);
		s.append("T");
		const hour : number = Math.trunc(time / 3600000) % 24;
		if (hour < 10)
			s.append("0");
		s.append(hour);
		s.append(":");
		const min : number = Math.trunc(time / 60000) % 60;
		if (min < 10)
			s.append("0");
		s.append(min);
		s.append(":");
		const sec : number = Math.trunc(time / 1000) % 60;
		if (sec < 10)
			s.append("0");
		s.append(sec);
		s.append(".");
		const ms : number = time % 1000;
		if (ms < 100)
			s.append("0");
		if (ms < 10)
			s.append("0");
		s.append(ms);
		return s.toString();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.DateUtils';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.DateUtils'].includes(name);
	}

	public static class = new Class<DateUtils>('de.svws_nrw.core.utils.DateUtils');

}

export function cast_de_svws_nrw_core_utils_DateUtils(obj : unknown) : DateUtils {
	return obj as DateUtils;
}
