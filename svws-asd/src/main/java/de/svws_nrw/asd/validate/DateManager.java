package de.svws_nrw.asd.validate;

import jakarta.validation.constraints.NotNull;

/**
 * Dieser Manager stellt Methoden zum Umgang mit Datumswerten zur Verfügung.
 * Die Daten werden im Input und Output jeweils im ISO 8601-Format erwartet.
 * Für die Lesbarkeit in Textausgaben steht auch die Möglichkeit des deutschen
 * Datumsformates zur Verfügung.
 */
public final class DateManager implements Comparable<DateManager> {

	/** Der Tag im Monat (1-31) - je nach Monat */
	private final int tag;

	/** Der Monat im Jahr (1-12) */
	private final int monat;

	/** Das Jahr */
	private final int jahr;

	/** Gibt an, on das Datum in einem Schaltjahr liegt oder nicht. */
	private final boolean istSchaltjahr;

	/** Der Tag im Jahr (1-365/366) - je nach Schaltjahr */
	private final int tagImJahr;

	/** Die maximale Anzahl an Tagen in dem Monat (ein Schlatjahr ist hier ggf. berücksichtigt) */
	private final int maxTageImMonat;

	/** Der Wochentag: 1 für Montag, ..., 7 für Sonntag */
	private final int wochentag;

	/** Die Kalenderwoche im Kalenderwochenjahr */
	private final int kalenderwoche;

	/** Das Jahr für die Kalenderwoche - dieses kann zu Beginn oder zum Ende eines Jahres vom Jahr abweichen */
	private final int kalenderwochenjahr;


	/**
	 * Erstellt einen neuen Datums-Manager mit dem übergebenen Datum.
	 *
	 * @param tag     der Tag im Monat (1-31) - je nach Monat
	 * @param monat   der Monat im Jahr (1-12)
	 * @param jahr    das Jahr
	 *
	 * @throws InvalidDateException   falls das Datum ungültig ist
	 */
	private DateManager(final int tag, final int monat, final int jahr) throws InvalidDateException {
		this.tag = tag;
		this.monat = monat;
		this.jahr = jahr;
		if (jahr < 0)
			throw new InvalidDateException("Die Jahresangabe muss positiv sein.");
		if (jahr > 9999)
			throw new InvalidDateException("Die Jahresangabe ist größer als 9999.");

		// Prüfe, ob es sich um ein Schaltjahr handelt
		final int schalttageBisVorjahr = getSchalttageBisJahr(jahr - 1);
		final int schalttageBisJahr = getSchalttageBisJahr(jahr);
		final int schalttag = (schalttageBisJahr - schalttageBisVorjahr);
		this.istSchaltjahr = (schalttag == 1);

		// Prüfe, ob der Monat im gültigen Bereich liegt
		if ((monat < 1) || (monat > 12))
			throw new InvalidDateException("Der Monat muss zwischen 1 und 12 liegen.");

		// Prüfe, ob die Angabe des Tages im Monat korrekt ist
		if (tag < 1)
			throw new InvalidDateException("Der Tag im Monat muss größer als 0 sein.");
		this.maxTageImMonat = switch (monat) {
			case 1, 3, 5, 7, 8, 10, 12 -> 31;
			case 4, 6, 9, 11 -> 30;
			case 2 -> 28 + (istSchaltjahr ? 1 : 0);
			default -> 0;
		};
		if (tag > maxTageImMonat)
			throw new InvalidDateException("Im Monat " + monat + " muss der Tag im Bereicht von 1 bis " + maxTageImMonat + " liegen.");

		// Bestimme den Tag im Jahr in Abhängigkeit davon, ob es sich um ein Schaltjahr handelt oder nicht
		this.tagImJahr = switch (monat) {
			case 1 -> tag;
			case 2 -> tag + 31;
			case 3 -> tag + 59 + schalttag;
			case 4 -> tag + 90 + schalttag;
			case 5 -> tag + 120 + schalttag;
			case 6 -> tag + 151 + schalttag;
			case 7 -> tag + 181 + schalttag;
			case 8 -> tag + 212 + schalttag;
			case 9 -> tag + 243 + schalttag;
			case 10 -> tag + 273 + schalttag;
			case 11 -> tag + 304 + schalttag;
			case 12 -> tag + 334 + schalttag;
			default -> throw new InvalidDateException("Der Monat muss zwischen 1 und 12 liegen.");
		};

		// Bestimme den Wochentag
		this.wochentag = getWochentagOfTagImJahr(jahr, this.tagImJahr);

		// Bestimme die Kalenderwoche in diesem Jahr
		final int wt1 = getWochentagOfTagImJahr(jahr, 1);
		final int kwAnzahl = ((wt1 == 4) || (this.istSchaltjahr && (wt1 == 3))) ? 53 : 52;

		final int tagImJahrMontagKW1 = (4 - getWochentagOfTagImJahr(jahr, 4)) + 1;
		if (tagImJahr < tagImJahrMontagKW1) {
			// letzte KW des Vorjahres
			this.kalenderwochenjahr = jahr - 1;
			final boolean istVjSchaltjahr = (schalttageBisVorjahr - getSchalttageBisJahr(jahr - 2)) == 1;
			final int wt1vj = getWochentagOfTagImJahr(jahr, 1);
			final int kwVjAnzahl = ((wt1vj == 4) || (istVjSchaltjahr && (wt1vj == 3))) ? 53 : 52;
			kalenderwoche = kwVjAnzahl;
		} else {
			final int tmpKW = 1 + ((tagImJahr - tagImJahrMontagKW1) / 7);
			if (tmpKW > kwAnzahl) {
				// Spezialfall: 1. KW des Folgejahres
				this.kalenderwochenjahr = jahr + 1;
				this.kalenderwoche = 1;
			} else {
				// KW in diesem Jahr
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
	private static int getWochentagOfTagImJahr(final int jahr, final int tagImJahr) {
		return ((jahr + getSchalttageBisJahr(jahr - 1) + tagImJahr + 5) % 7) + 1;
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
	private static int getSchalttageBisJahr(final int jahr) {
		return (jahr / 4) - ((jahr / 100) - (jahr / 400));
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
	public static @NotNull DateManager fromValues(final int jahr, final int monat, final int tag) throws InvalidDateException {
		// Erstelle den Manager und gib diesen zurück
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
	public static @NotNull DateManager from(final String isoDate) throws InvalidDateException {
		if (isoDate == null)
			throw new InvalidDateException("Es muss ein Datum angegeben werden. null ist nicht zulässig.");
		final @NotNull String[] d = isoDate.split("-");
		final @NotNull String strError = "Das Datumsformat '" + isoDate + "' ist nicht konform zu ISO8601";
		if (d.length != 3)
			throw new InvalidDateException(strError + ": Es ist nicht durch zwei Bindestriche unterteilt.");
		// Bestimme das Jahr
		int jahr;
		try {
			jahr = Integer.parseInt(d[0]);
		} catch (final NumberFormatException e) {
			throw new InvalidDateException(strError + ": Der Teil vor dem ersten Bindestrich muss eine Zahl sein und sollte das Jahr angeben", e);
		}
		// Bestimme den Monat
		int monat;
		try {
			monat = Integer.parseInt(d[1]);
		} catch (final NumberFormatException e) {
			throw new InvalidDateException(strError + ": Der mittlere Teil zwischen den Bindestrichen muss eine Zahl sein und sollte den Monat angeben", e);
		}
		// Bestimme den Tag
		int tag;
		try {
			tag = Integer.parseInt(d[2]);
		} catch (final NumberFormatException e) {
			throw new InvalidDateException(strError + ": Der letzte Teil hinter dem zweiten Bindestrich muss eine Zahl sein und sollte den Tag angeben", e);
		}
		// Erstelle den Manager und gib diesen zurück
		return new DateManager(tag, monat, jahr);
	}


	@Override
	public int compareTo(final DateManager other) {
		if (other == null)
			return 1;
		int tmp = Integer.compare(this.jahr, other.jahr);
		if (tmp != 0)
			return tmp;
		tmp = Integer.compare(this.monat, other.monat);
		if (tmp != 0)
			return tmp;
		return Integer.compare(this.tag, other.tag);
	}


	@Override
	public int hashCode() {
		return (jahr * 10000) + (monat * 100) + tag;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if ((obj != null) && (obj instanceof DateManager))
			return (this.compareTo((DateManager) obj) == 0);
		return false;
	}


	/**
	 * Gibt den Tag im Monat des Datums zurück.
	 *
	 * @return der Tag im Monat
	 */
	public int getTag() {
		return tag;
	}


	/**
	 * Gibt den Monat des Datums zurück.
	 *
	 * @return der Monat
	 */
	public int getMonat() {
		return monat;
	}


	/**
	 * Gibt das Jahr des Datums zurück.
	 *
	 * @return das Jahr
	 */
	public int getJahr() {
		return jahr;
	}


	/**
	 * Gibt zurück, ob es sich bei dem Jahr des Datums um ein Schaltjahr handelt oder nicht.
	 *
	 * @return true, falls ein Schaltjahr vorliegt, und ansonsten false
	 */
	public boolean isSchaltjahr() {
		return istSchaltjahr;
	}


	/**
	 * Gibt den Tag im Jahr zurück. (1 - 365 bzw. 366 im Schaltjahr)
	 *
	 * @return der Tag im Jahr
	 */
	public int getTagImJahr() {
		return tagImJahr;
	}


	/**
	 * Gibt die maximale Anzahl der Tage im Monat zurück. Bei dem Februar wird
	 * berücksichtigt, ob es sich um ein Schaltjahr handelt oder nicht.
	 *
	 * @return 28, 29, 30 oder 31
	 */
	public int getMaxTageImMonat() {
		return maxTageImMonat;
	}


	/**
	 * Gibt den Wochentag zurück (1 - Montag, 2 - Dienstag, ..., 7 - Sonntag)
	 *
	 * @return der Wochentag
	 */
	public int getWochentag() {
		return wochentag;
	}


	/**
	 * Gibt die Kalenderwoche zurück.
	 * Hierbei kann am Anfang oder Ende des Jahres das Kalenderwochenjahr ggf. vom
	 * Jahr des Datums abweichen.
	 *
	 * @return die Kalenderwoche
	 */
	public int getKalenderwoche() {
		return kalenderwoche;
	}


	/**
	 * Das Kalenderwochenjahr, welches ggf. am Anfang oder Ende des Jahres
	 * vom Jahr des Datums abweichen kann.
	 *
	 * @return das Kalenderwochenjahr
	 */
	public int getKalenderwochenjahr() {
		return kalenderwochenjahr;
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
	public int getAlter(final @NotNull DateManager other) throws InvalidDateException {
		final int cmp = compareTo(other);
		if (cmp < 0)
			throw new InvalidDateException("Das angegebene Datum ist vor dem Geburtsdatum."
					+ " Eine Altersbestimmung ist so nicht möglich.");
		final int tmp = other.jahr - this.jahr;
		if ((other.monat < this.monat) || ((other.monat == this.monat) && (other.tag < this.tag)))
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
	public boolean istInJahren(final int von, final int bis) {
		return (von <= this.jahr) && (this.jahr <= bis);
	}

}
