package de.svws_nrw.davapi.util.icalendar.recurrence;

import java.time.Instant;

import de.svws_nrw.davapi.util.icalendar.DateTimeUtil;

/**
 * Diese Klasse repräsentiert ein Limit, wie häufig oder bis wann ein
 * wiederkehrendes Ereignis auftritt.<br>
 * Vgl.
 * <a href="https://datatracker.ietf.org/doc/html/rfc5545#section-3.3.10">RFC
 * 5545</a> <br>
 * <code> 
      The UNTIL rule part defines a DATE or DATE-TIME value that bounds<br>
      the recurrence rule in an inclusive manner.  If the value<br>
      specified by UNTIL is synchronized with the specified recurrence,<br>
      this DATE or DATE-TIME becomes the last instance of the<br>
      recurrence.  The value of the UNTIL rule part MUST have the same<br>
      value type as the "DTSTART" property.  Furthermore, if the<br>
      "DTSTART" property is specified as a date with local time, then<br>
      the UNTIL rule part MUST also be specified as a date with local<br>
      time.  If the "DTSTART" property is specified as a date with UTC<br>
      time or a date with local time and time zone reference, then the<br>
      UNTIL rule part MUST be specified as a date with UTC time.  In the<br>
      case of the "STANDARD" and "DAYLIGHT" sub-components the UNTIL<br>
      rule part MUST always be specified as a date with UTC time.  If<br>
      specified as a DATE-TIME value, then it MUST be specified in a UTC<br>
      time format.  If not present, and the COUNT rule part is also not<br>
      present, the "RRULE" is considered to repeat forever.<br>
<br>
      The COUNT rule part defines the number of occurrences at which to<br>
      range-bound the recurrence.  The "DTSTART" property value always<br>
      counts as the first occurrence.<br>

</code>
 */
public class RecurrencyLimit {
	/** Die Anzahl, wie häufig ein wiederkehrendes Ereignis auftrittt */
	private int count;
	/** Der Zeitpunkt, bis zu dem das wiederkehrende Ereignis wiederholt wird */
	private Instant until;

	/**
	 * Konstruktor für das Recurrnylimit mit einem Zeitpunkt für eine UNTIL-Regel
	 * 
	 * @param until der Zeitpunkt, bis zu dem die Regel zulässig sein soll
	 */
	public RecurrencyLimit(Instant until) {
		this.until = until;
		this.count = 0;
	}

	/**
	 * Konstruktor für ein RecurrencyLimit anhand einer Anzahl für eine COUNT-Regel
	 * 
	 * @param count die Anzahl der Wiederholungen
	 */
	public RecurrencyLimit(int count) {
		this.count = count;
		this.until = null;
	}

	/**
	 * getter für die Anzahl der Wiederholungen, 0 wenn es eine UNTIL-Regel ist
	 * 
	 * @return die Anzahl der Wiederholungen dieser Limitierung
	 */
	public int getCount() {
		return count;
	}

	/**
	 * getter für das Enddatum einer Regel, null wenn es eine COUNT-Regel ist
	 * 
	 * @return das Enddatum dieser Limitierung
	 */
	public Instant getUntil() {
		return until;
	}

	/**
	 * Serialisiert diese Limitierung und fügt sie dem gegebenen StringBuilder hinzu
	 * 
	 * @param sb der Stringbuilder, dem dieses Limit als COUNT oder UNTIL Regel
	 *           zugefügt werden soll.
	 */
	public void append(StringBuilder sb) {
		if (this.until != null) {
			sb.append("UNTIL=");
			sb.append(DateTimeUtil.toCalDavString(this.until));
		} else {
			sb.append("COUNT=");
			sb.append(this.count);
		}
	}
}
