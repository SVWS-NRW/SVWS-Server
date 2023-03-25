package de.svws_nrw.davapi.util.icalendar.recurrence;

import java.util.Objects;

/**
 * Diese Klasse repräsentiert die ByDay-Regel eines wiederholten Ereignisses.
 * <br>
 * Vgl.
 * <a href="https://datatracker.ietf.org/doc/html/rfc5545#section-3.3.10">RFC
 * 5545</a> <br>
 * <code>
 The BYDAY rule part specifies a COMMA-separated list of days of
      the week; SU indicates Sunday; MO indicates Monday; TU indicates
      Tuesday; WE indicates Wednesday; TH indicates Thursday; FR
      indicates Friday; and SA indicates Saturday.

      Each BYDAY value can also be preceded by a positive (+n) or
      negative (-n) integer.  If present, this indicates the nth
      occurrence of a specific day within the MONTHLY or YEARLY "RRULE".
 * </code>
 */
public class ByDay implements Comparable<ByDay> {

	/**
	 * das führende Integer. Wenn ungleich 0 gibt es den nten Tag innerhalb einer
	 * RRULE mit Frequenz {@link Frequency#MONTHLY} oder {@link Frequency#YEARLY}
	 * an. Bei Negativem Wert wird der nt-letzte Tag, also von hinten gezählt
	 * angegeben. So gibt -1MO den letzten Montag in einer Regel wieder, '+3TU' den
	 * 3. Dienstag, etc.
	 */
	private int ordinal;

	/** Der Wochentag in dem es in dieser Regel geht. */
	private WeekDay weekDay;

	/**
	 * Konstruktor für das ByDay Property mit der Ordnung (vgl.
	 * {@link #getOrdinal()} und dem Wochentag.
	 * 
	 * @param ordinal der Filter für den Wochentag
	 * @param weekDay der Wochentag
	 */
	public ByDay(int ordinal, WeekDay weekDay) {
		this.ordinal = ordinal;
		this.weekDay = weekDay;
	}

	/**
	 * Konstruktor für den Wochentag ohne Filter
	 * 
	 * @param weekDay der Wochentag
	 */
	public ByDay(WeekDay weekDay) {
		this.weekDay = weekDay;
	}

	/**
	 * Getter für das Ordinal (Ordnung/Reihenfolge). Wenn ungleich 0 gibt es den
	 * nten Tag innerhalb einer RRULE mit Frequenz {@link Frequency#MONTHLY} oder
	 * {@link Frequency#YEARLY} an. Bei Negativem Wert wird der nt-letzte Tag, also
	 * von hinten gezählt angegeben. So gibt -1MO den letzten Montag in einer Regel
	 * wieder, '+3TU' den 3. Dienstag, etc.
	 * 
	 * @return the ordinal
	 */
	public int getOrdinal() {
		return ordinal;
	}

	/**
	 * das Ordinal (Filter). Wenn ungleich 0 gibt es den nten Tag innerhalb einer
	 * RRULE mit Frequenz {@link Frequency#MONTHLY} oder {@link Frequency#YEARLY}
	 * an. Bei Negativem Wert wird der nt-letzte Tag, also von hinten gezählt
	 * angegeben. So gibt -1MO den letzten Montag in einer Regel wieder, '+3TU' den
	 * 3. Dienstag, etc.
	 * 
	 * @param ordinal the ordinal to set
	 */
	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}

	/**
	 * @return the weekDay
	 */
	public WeekDay getWeekDay() {
		return weekDay;
	}

	/**
	 * @param weekDay the weekDay to set
	 */
	public void setWeekDay(WeekDay weekDay) {
		this.weekDay = weekDay;
	}

	@Override
	public String toString() {
		if (this.ordinal != 0) {
			return String.format("%d%s", this.ordinal, this.weekDay.getStringRep());
		}
		return this.weekDay.getStringRep();
	}

	@Override
	public int compareTo(ByDay o) {
		int compare = this.weekDay.compareTo(o.weekDay);
		if (compare == 0) {
			compare = Integer.compare(this.ordinal, o.ordinal);
		}
		return compare;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ordinal, weekDay);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ByDay other = (ByDay) obj;
		return ordinal == other.ordinal && weekDay == other.weekDay;
	}

	/**
	 * Konvertiert einen String in ein ByDay Objekt
	 * 
	 * @param s der String
	 * @return das durch den String repräsentierte ByDay-Objekt
	 */
	public static ByDay fromString(String s) {
		ByDay result = new ByDay(WeekDay.fromStringRep(s.substring(s.length() - 2)));
		if (s.length() > 2) {
			int ordinal = Integer.parseInt(s.substring(0, s.length() - 2));
			result.setOrdinal(ordinal);
		}
		return result;
	}
}
