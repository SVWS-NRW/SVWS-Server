import { JavaObject } from '../../java/lang/JavaObject';
import { DeveloperNotificationException } from '../../core/exceptions/DeveloperNotificationException';

export class DateUtils extends JavaObject {

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
	 * Auszug aus Wikipedia:: Da ISO 8601 den Montag als ersten Tag der Woche definiert, ist dies somit die erste Woche,
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
		DeveloperNotificationException.ifTrue("Das Jahr von " + datumISO8601! + " ist ungültig!", (jahr < 1900) || (jahr > 2900));
		const monat : number = DeveloperNotificationException.ifNotInt(split[1]);
		DeveloperNotificationException.ifTrue("Der Monat von " + datumISO8601! + " ist ungültig!", (monat < 1) || (monat > 12));
		const tagImMonat : number = DeveloperNotificationException.ifNotInt(split[2]);
		DeveloperNotificationException.ifTrue("Der Tag von " + datumISO8601! + " ist ungültig!", (tagImMonat < 1) || (tagImMonat > 31));
		const schalttage1 : number = (Math.trunc((jahr - 1) / 4)) - (Math.trunc((jahr - 1) / 100)) + (Math.trunc((jahr - 1) / 400));
		const schalttage2 : number = (Math.trunc(jahr / 4)) - (Math.trunc(jahr / 100)) + (Math.trunc(jahr / 400));
		const schaltjahr : number = schalttage2 - schalttage1;
		const tagImJahr : number = DateUtils.monat_zu_vergangene_tage[schaltjahr][monat] + tagImMonat;
		const tagInWoche : number = (jahr + schalttage1 + tagImJahr + 5) % 7 + 1;
		const tagImJahrAmJanuar4 : number = DateUtils.monat_zu_vergangene_tage[schaltjahr][1] + 4;
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

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.DateUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_DateUtils(obj : unknown) : DateUtils {
	return obj as DateUtils;
}
