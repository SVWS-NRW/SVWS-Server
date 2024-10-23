import { JavaInteger } from '../../java/lang/JavaInteger';
import type { Comparable } from '../../java/lang/Comparable';
import { InvalidDateException } from '../../asd/validate/InvalidDateException';
import { JavaObject } from '../../java/lang/JavaObject';
import { Class } from '../../java/lang/Class';

export class DateManager extends JavaObject implements Comparable<DateManager> {

	/**
	 * Der Tag im Monat (1-31) - je nach Monat
	 */
	private readonly tag : number;

	/**
	 * Der Monat im Jahr (1-12)
	 */
	private readonly monat : number;

	/**
	 * Das Jahr
	 */
	private readonly jahr : number;

	/**
	 * Gibt an, on das Datum in einem Schaltjahr liegt oder nicht.
	 */
	private readonly istSchaltjahr : boolean;

	/**
	 * Der Tag im Jahr (1-365/366) - je nach Schaltjahr
	 */
	private readonly tagImJahr : number;

	/**
	 * Die maximale Anzahl an Tagen in dem Monat (ein Schlatjahr ist hier ggf. berücksichtigt)
	 */
	private readonly maxTageImMonat : number;

	/**
	 * Der Wochentag: 1 für Montag, ..., 7 für Sonntag
	 */
	private readonly wochentag : number;

	/**
	 * Die Kalenderwoche im Kalenderwochenjahr
	 */
	private readonly kalenderwoche : number;

	/**
	 * Das Jahr für die Kalenderwoche - dieses kann zu Beginn oder zum Ende eines Jahres vom Jahr abweichen
	 */
	private readonly kalenderwochenjahr : number;


	/**
	 * Erstellt einen neuen Datums-Manager mit dem übergebenen Datum.
	 *
	 * @param tag     der Tag im Monat (1-31) - je nach Monat
	 * @param monat   der Monat im Jahr (1-12)
	 * @param jahr    das Jahr
	 *
	 * @throws InvalidDateException   falls das Datum ungültig ist
	 */
	private constructor(tag : number, monat : number, jahr : number) {
		super();
		this.tag = tag;
		this.monat = monat;
		this.jahr = jahr;
		if (jahr < 0)
			throw new InvalidDateException("Die Jahresangabe muss positiv sein.")
		if (jahr > 9999)
			throw new InvalidDateException("Die Jahresangabe ist größer als 9999.")
		const schalttageBisVorjahr : number = DateManager.getSchalttageBisJahr(jahr - 1);
		const schalttageBisJahr : number = DateManager.getSchalttageBisJahr(jahr);
		const schalttag : number = (schalttageBisJahr - schalttageBisVorjahr);
		this.istSchaltjahr = (schalttag === 1);
		if ((monat < 1) || (monat > 12))
			throw new InvalidDateException("Der Monat muss zwischen 1 und 12 liegen.")
		if (tag < 1)
			throw new InvalidDateException("Der Tag im Monat muss größer als 0 sein.")
		const _seexpr_1558458123 = (monat);
		if (_seexpr_1558458123 === 1) {
			this.maxTageImMonat = 31;
		} else if (_seexpr_1558458123 === 3) {
			this.maxTageImMonat = 31;
		} else if (_seexpr_1558458123 === 5) {
			this.maxTageImMonat = 31;
		} else if (_seexpr_1558458123 === 7) {
			this.maxTageImMonat = 31;
		} else if (_seexpr_1558458123 === 8) {
			this.maxTageImMonat = 31;
		} else if (_seexpr_1558458123 === 10) {
			this.maxTageImMonat = 31;
		} else if (_seexpr_1558458123 === 12) {
			this.maxTageImMonat = 31;
		} else if (_seexpr_1558458123 === 4) {
			this.maxTageImMonat = 30;
		} else if (_seexpr_1558458123 === 6) {
			this.maxTageImMonat = 30;
		} else if (_seexpr_1558458123 === 9) {
			this.maxTageImMonat = 30;
		} else if (_seexpr_1558458123 === 11) {
			this.maxTageImMonat = 30;
		} else if (_seexpr_1558458123 === 2) {
			this.maxTageImMonat = 28 + (this.istSchaltjahr ? 1 : 0);
		} else {
			this.maxTageImMonat = 0;
		}
		;
		if (tag > this.maxTageImMonat)
			throw new InvalidDateException("Im Monat " + monat + " muss der Tag im Bereicht von 1 bis " + this.maxTageImMonat + " liegen.")
		const _seexpr_670811900 = (monat);
		if (_seexpr_670811900 === 1) {
			this.tagImJahr = tag;
		} else if (_seexpr_670811900 === 2) {
			this.tagImJahr = tag + 31;
		} else if (_seexpr_670811900 === 3) {
			this.tagImJahr = tag + 59 + schalttag;
		} else if (_seexpr_670811900 === 4) {
			this.tagImJahr = tag + 90 + schalttag;
		} else if (_seexpr_670811900 === 5) {
			this.tagImJahr = tag + 120 + schalttag;
		} else if (_seexpr_670811900 === 6) {
			this.tagImJahr = tag + 151 + schalttag;
		} else if (_seexpr_670811900 === 7) {
			this.tagImJahr = tag + 181 + schalttag;
		} else if (_seexpr_670811900 === 8) {
			this.tagImJahr = tag + 212 + schalttag;
		} else if (_seexpr_670811900 === 9) {
			this.tagImJahr = tag + 243 + schalttag;
		} else if (_seexpr_670811900 === 10) {
			this.tagImJahr = tag + 273 + schalttag;
		} else if (_seexpr_670811900 === 11) {
			this.tagImJahr = tag + 304 + schalttag;
		} else if (_seexpr_670811900 === 12) {
			this.tagImJahr = tag + 334 + schalttag;
		} else {
			throw new InvalidDateException("Der Monat muss zwischen 1 und 12 liegen.");
		}
		;
		this.wochentag = DateManager.getWochentagOfTagImJahr(jahr, this.tagImJahr);
		const wt1 : number = DateManager.getWochentagOfTagImJahr(jahr, 1);
		const kwAnzahl : number = ((wt1 === 4) || (this.istSchaltjahr && (wt1 === 3))) ? 53 : 52;
		const tagImJahrMontagKW1 : number = (4 - DateManager.getWochentagOfTagImJahr(jahr, 4)) + 1;
		if (this.tagImJahr < tagImJahrMontagKW1) {
			this.kalenderwochenjahr = jahr - 1;
			const istVjSchaltjahr : boolean = (schalttageBisVorjahr - DateManager.getSchalttageBisJahr(jahr - 2)) === 1;
			const wt1vj : number = DateManager.getWochentagOfTagImJahr(jahr, 1);
			const kwVjAnzahl : number = ((wt1vj === 4) || (istVjSchaltjahr && (wt1vj === 3))) ? 53 : 52;
			this.kalenderwoche = kwVjAnzahl;
		} else {
			const tmpKW : number = 1 + (Math.trunc((this.tagImJahr - tagImJahrMontagKW1) / 7));
			if (tmpKW > kwAnzahl) {
				this.kalenderwochenjahr = jahr + 1;
				this.kalenderwoche = 1;
			} else {
				this.kalenderwochenjahr = jahr;
				this.kalenderwoche = tmpKW;
			}
		}
	}

	/**
	 * Bestimmt den Wochentag für den angegebenen Tag im Jahr.
	 * Bei der Rechnung wird verwendet, dass der Wochentag bei einem Datum sich mit jedem
	 * Jahr um 1 verschiebt und in Schaltjahren um 2.
	 * Die 5 ist dabei ein Offset, um beim richtigen Wochentag die Zählung zu beginnen, wobei
	 * zu berücksichtigen ist, dass an dieser Stelle in Berechnung noch Werte von
	 * 0 bis 6 (Mo-So) verwendet werden und erst im Anschluss um 1 verschoben werden.
	 *
	 * @param jahr       das Jahr
	 * @param tagImJahr  der Tag im Jahr
	 *
	 * @return der Wochentag (1 - Montag, ..., 7 - Sonntag)
	 */
	private static getWochentagOfTagImJahr(jahr : number, tagImJahr : number) : number {
		return ((jahr + DateManager.getSchalttageBisJahr(jahr - 1) + tagImJahr + 5) % 7) + 1;
	}

	/**
	 * Bestimmt die Anzahl der Schalttage, welche seit dem Jahr 1 in den einzelnen
	 * Jahren vorgekommen sind. Hierbei wird berücksichtigt, dass jedes 4-te Jahr
	 * ein Schaltjahr ist mit der Ausnahme, dass alle hundert Jahre doch kein Schaltjahr ist,
	 * wobei es hierbei wiederum die Ausnahme gibt, dass alle 400 Jahre doch wieder
	 * ein Schaltjahr ist.
	 *
	 * @param jahr   das Jahr
	 *
	 * @return die Anzahl der Schalttage seit dem Jahr 1
	 */
	private static getSchalttageBisJahr(jahr : number) : number {
		return (Math.trunc(jahr / 4)) - ((Math.trunc(jahr / 100)) - (Math.trunc(jahr / 400)));
	}

	/**
	 * Erstellt einen neuen Date-Manager für das angegebene Datum mit den angegebenen Werten.
	 *
	 * @param jahr    das Jahr (z.B. 2024)
	 * @param monat   der Monat (z.B. 8 für August)
	 * @param tag     der Tag im Monat (z.B. 31)
	 *
	 * @return der Manager
	 *
	 * @throws InvalidDateException falls das Datum fehlerhaft ist
	 */
	public static fromValues(jahr : number, monat : number, tag : number) : DateManager {
		return new DateManager(tag, monat, jahr);
	}

	/**
	 * Erstellt einen neuen Date-Manager für das angegebene Datum im ISO-Format 8601
	 *
	 * @param isoDate   Das Datum im ISO-Format
	 *
	 * @return der Manager
	 *
	 * @throws InvalidDateException falls das Datumsformat oder das Datum fehlerhaft ist
	 */
	public static from(isoDate : string | null) : DateManager {
		if (isoDate === null)
			throw new InvalidDateException("Es muss ein Datum angegeben werden. null ist nicht zulässig.")
		const d : Array<string | null> = isoDate.split("-");
		const strError : string = "Das Datumsformat '" + isoDate + "' ist nicht konform zu ISO8601";
		if (d.length !== 3)
			throw new InvalidDateException(strError + ": Es ist nicht durch zwei Bindestriche unterteilt.")
		let jahr : number;
		try {
			jahr = JavaInteger.parseInt(d[0]);
		} catch(e : any) {
			throw new InvalidDateException(strError + ": Der Teil vor dem ersten Bindestrich muss eine Zahl sein und sollte das Jahr angeben", e)
		}
		let monat : number;
		try {
			monat = JavaInteger.parseInt(d[1]);
		} catch(e : any) {
			throw new InvalidDateException(strError + ": Der mittlere Teil zwischen den Bindestrichen muss eine Zahl sein und sollte den Monat angeben", e)
		}
		let tag : number;
		try {
			tag = JavaInteger.parseInt(d[2]);
		} catch(e : any) {
			throw new InvalidDateException(strError + ": Der letzte Teil hinter dem zweiten Bindestrich muss eine Zahl sein und sollte den Tag angeben", e)
		}
		return new DateManager(tag, monat, jahr);
	}

	public compareTo(other : DateManager | null) : number {
		if (other === null)
			return 1;
		let tmp : number = JavaInteger.compare(this.jahr, other.jahr);
		if (tmp !== 0)
			return tmp;
		tmp = JavaInteger.compare(this.monat, other.monat);
		if (tmp !== 0)
			return tmp;
		return JavaInteger.compare(this.tag, other.tag);
	}

	public hashCode() : number {
		return (this.jahr * 10000) + (this.monat * 100) + this.tag;
	}

	public equals(obj : unknown | null) : boolean {
		if (this as unknown === obj as unknown)
			return true;
		if ((obj !== null) && (((obj instanceof JavaObject) && (obj.isTranspiledInstanceOf('de.svws_nrw.asd.validate.DateManager')))))
			return (this.compareTo(cast_de_svws_nrw_asd_validate_DateManager(obj)) === 0);
		return false;
	}

	/**
	 * Gibt den Tag im Monat des Datums zurück.
	 *
	 * @return der Tag im Monat
	 */
	public getTag() : number {
		return this.tag;
	}

	/**
	 * Gibt den Monat des Datums zurück.
	 *
	 * @return der Monat
	 */
	public getMonat() : number {
		return this.monat;
	}

	/**
	 * Gibt das Jahr des Datums zurück.
	 *
	 * @return das Jahr
	 */
	public getJahr() : number {
		return this.jahr;
	}

	/**
	 * Gibt zurück, ob es sich bei dem Jahr des Datums um ein Schaltjahr handelt oder nicht.
	 *
	 * @return true, falls ein Schaltjahr vorliegt, und ansonsten false
	 */
	public isSchaltjahr() : boolean {
		return this.istSchaltjahr;
	}

	/**
	 * Gibt den Tag im Jahr zurück. (1 - 365 bzw. 366 im Schaltjahr)
	 *
	 * @return der Tag im Jahr
	 */
	public getTagImJahr() : number {
		return this.tagImJahr;
	}

	/**
	 * Gibt die maximale Anzahl der Tage im Monat zurück. Bei dem Februar wird
	 * berücksichtigt, ob es sich um ein Schaltjahr handelt oder nicht.
	 *
	 * @return 28, 29, 30 oder 31
	 */
	public getMaxTageImMonat() : number {
		return this.maxTageImMonat;
	}

	/**
	 * Gibt den Wochentag zurück (1 - Montag, 2 - Dienstag, ..., 7 - Sonntag)
	 *
	 * @return der Wochentag
	 */
	public getWochentag() : number {
		return this.wochentag;
	}

	/**
	 * Gibt die Kalenderwoche zurück.
	 * Hierbei kann am Anfang oder Ende des Jahres das Kalenderwochenjahr ggf. vom
	 * Jahr des Datums abweichen.
	 *
	 * @return die Kalenderwoche
	 */
	public getKalenderwoche() : number {
		return this.kalenderwoche;
	}

	/**
	 * Das Kalenderwochenjahr, welches ggf. am Anfang oder Ende des Jahres
	 * vom Jahr des Datums abweichen kann.
	 *
	 * @return das Kalenderwochenjahr
	 */
	public getKalenderwochenjahr() : number {
		return this.kalenderwochenjahr;
	}

	/**
	 * Bestimmt das Alter einer Person, die am Datum dieses Managers geboren ist
	 * anhand des Datums im übergebenen Manager.
	 *
	 * @param other   der andere Manager
	 *
	 * @return das Alter einer Person, die am Datum dieses Managers geboren an dem
	 *     gegebenen Datum
	 *
	 * @throws InvalidDateException falls das übergebene Datum früher liegt als
	 *     das Geburtsdatum des Managers
	 */
	public getAlter(other : DateManager) : number {
		const cmp : number = this.compareTo(other);
		if (cmp < 0)
			throw new InvalidDateException("Das angegebene Datum ist vor dem Geburtsdatum. Eine Altersbestimmung ist so nicht möglich.")
		const tmp : number = other.jahr - this.jahr;
		if ((other.monat < this.monat) || ((other.monat === this.monat) && (other.tag < this.tag)))
			return tmp - 1;
		return tmp;
	}

	/**
	 * Prüft, ob das Datum in dem Interval [von; bis] liegt.
	 *
	 * @param von   das erste Jahr, welches akzeptiert wird
	 * @param bis   das letzte Jahr, welches akzeptiert wird
	 *
	 * @return true, falls das Datum in dem Bereich liegt, und ansonsten false
	 */
	public istInJahren(von : number, bis : number) : boolean {
		return (von <= this.jahr) && (this.jahr <= bis);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.DateManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.DateManager', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<DateManager>('de.svws_nrw.asd.validate.DateManager');

}

export function cast_de_svws_nrw_asd_validate_DateManager(obj : unknown) : DateManager {
	return obj as DateManager;
}
