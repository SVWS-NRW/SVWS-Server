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
	 * Liefert ein aus dem Datum extrahiertes int[] mit dem Inhalt [jahr, monat, tag, wochentag].<br>
	 * Gültige Werte sind im Bereich:<br>
	 * Index 0: jahr = 1900 bis 2900<br>
	 * Index 1: monat = 1 (Januar) bis 12 (Dezember)<br>
	 * Index 2: tag = 1 bis 31<br>
	 * Index 3: wochentag = 1 (Montag) bis 7 (Sonntag)<br>
	 *
	 * TODO BAR KW
	 *
	 * @param datumISO8601 Das Datum im ISO8601-Format uuuu-MM-dd (z.B. 2014-03-14).
	 *
	 * @return ein aus dem Datum extrahiertes int[] mit dem Inhalt [jahr, monat, tag, wochentag].
	 */
	public static convert(datumISO8601 : string) : Array<number> {
		const split : Array<string | null> = datumISO8601.split("-");
		DeveloperNotificationException.ifTrue("Datumsformat von " + datumISO8601! + " ist nicht ISO8601 konform!", split.length !== 3);
		const jahr : number = DeveloperNotificationException.ifNotInt(split[0]);
		DeveloperNotificationException.ifTrue("Das Jahr von " + datumISO8601! + " ist ungültig!", (jahr < 1900) || (jahr > 2900));
		const monat : number = DeveloperNotificationException.ifNotInt(split[1]);
		DeveloperNotificationException.ifTrue("Der Monat von " + datumISO8601! + " ist ungültig!", (monat < 1) || (monat > 12));
		const tag : number = DeveloperNotificationException.ifNotInt(split[2]);
		DeveloperNotificationException.ifTrue("Der Tag von " + datumISO8601! + " ist ungültig!", (tag < 1) || (tag > 31));
		const schalttage1 : number = (Math.trunc((jahr - 1) / 4)) - (Math.trunc((jahr - 1) / 100)) + (Math.trunc((jahr - 1) / 400));
		const schalttage2 : number = (Math.trunc(jahr / 4)) - (Math.trunc(jahr / 100)) + (Math.trunc(jahr / 400));
		const schaltjahr : number = schalttage2 - schalttage1;
		const wochentag : number = (jahr + schalttage1 + DateUtils.monat_zu_vergangene_tage[schaltjahr][monat] + tag + 5) % 7 + 1;
		return [jahr, monat, tag, wochentag];
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.DateUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_DateUtils(obj : unknown) : DateUtils {
	return obj as DateUtils;
}
