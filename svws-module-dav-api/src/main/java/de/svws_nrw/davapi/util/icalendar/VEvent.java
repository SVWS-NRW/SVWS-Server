package de.svws_nrw.davapi.util.icalendar;

import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Diese Klasse repräsentiert ein VEVENT innerhalb eines {@link VCalendar}
 *
 */
public class VEvent {

	private static final String DTEND_KEY = "DTEND";
	private static final String DTSTART_KEY = "DTSTART";
	/** Value des Begin- oder End-Properties, bspw <code>BEGIN:VEVENT</code> */
	public static final String VEVENT_VALUE = "VEVENT";
	/** der Startzeitpunkt des Events */
	private Instant dtStart;
	/** der Endzeitpunkt des Events */
	private Instant dtEnd;
	/** die Properties dieses Events */
	private final List<IProperty> properties = new Vector<>();

	/**
	 * empty default constructor
	 */
	public VEvent() {
		// empty default constructor
	}

	/**
	 * getter für den Startzeitpunk
	 *
	 * @return den Startzeitpunkt des Events
	 */
	public Instant getDTStart() {
		return this.dtStart;
	}

	/**
	 * getter für den Endzeitpunkt des Events
	 *
	 * @return den Endzeitpunkt
	 */
	public Instant getDTEnd() {
		return this.dtEnd;
	}

	/**
	 * Fügt ein Property zu diesem Event hinzu und parst dieses gegebenenfalls in
	 * Start- und Endzeitpunkt.
	 *
	 * @param property das zuzufügende Property
	 */
	public void addProperty(final IProperty property) {
		if (property.getKey().startsWith(DTSTART_KEY)) {
			this.dtStart = DateTimeUtil.parseCalDav(property);
		} else if (property.getKey().startsWith(DTEND_KEY)) {
			this.dtEnd = DateTimeUtil.parseCalDav(property);
		}
		this.properties.add(property);
	}

	/**
	 * parst ein VEvent aus dem gegebenen Iterator über die Zeilen eines
	 * serialisierten VCalendars
	 *
	 * @param linesIterator der iterator über die Zeilen des serialisierten
	 *                      VCalendars
	 * @return das VEvent mit den geparsten Properties
	 */
	static VEvent parse(final Iterator<String> linesIterator) {
		IProperty property = IProperty.fromString(linesIterator.next());
		final VEvent event = new VEvent();
		while (!IProperty.isProperty(property, VCalendar.END_PROPERTY_KEY, VEvent.VEVENT_VALUE)) {
			event.addProperty(property);
			property = IProperty.fromString(linesIterator.next());
		}
		return event;
	}

	/**
	 * Serialisiert dieses VEvent im gegebenen Strinbuilder
	 *
	 * @param sb der StringBuilder
	 */
	public void serialize(final StringBuffer sb) {
		new Property(VCalendar.BEGIN_PROPERTY_KEY, VEVENT_VALUE).serialize(sb);
		for (final IProperty p : properties) {
			p.serialize(sb);
		}
		new Property(VCalendar.END_PROPERTY_KEY, VEVENT_VALUE).serialize(sb);
	}

	/**
	 * Erzeugt ein VEVENT mit gegebenem Start- und Endzeitpunkt, sowie Titel und
	 * Beschreibung. Greift auf
	 * {@link #createSimpleEvent(Instant, Instant, String, String)} zurück und setzt
	 * als TimeZone die {@link DateTimeUtil#TIMEZONE_DEFAULT}
	 *
	 * @param start       startzeitpunkt
	 * @param end         endzeitpunkt
	 * @param title       Titel des Ereignis
	 * @param description Beschreibung des Ereignis
	 * @return VEvent mit gegebenen Parametern
	 */
	public static VEvent createSimpleEvent(final Instant start, final Instant end, final String title, final String description) {
		return createSimpleEvent(start, end, title, description, DateTimeUtil.TIMEZONE_DEFAULT);
	}

	/**
	 * Erzeugt ein VEVENT mit gegebenem Start- und Endzeitpunkt, sowie Titel und
	 * Beschreibung. Greift auf
	 * {@link #createSimpleEvent(Instant, Instant, String, String)} zurück und setzt
	 * als TimeZone die {@link DateTimeUtil#TIMEZONE_DEFAULT}
	 *
	 * @param start       startzeitpunkt
	 * @param end         endzeitpunkt
	 * @param title       Titel des Ereignis
	 * @param description Beschreibung des Ereignis
	 * @param tzid        die Zeitzone für den Start- und Endzeitpunk
	 * @return VEvent mit gegebenen Parametern
	 */
	public static VEvent createSimpleEvent(final Instant start, final Instant end, final String title, final String description, final String tzid) {
		final VEvent e = new VEvent();
		e.addProperty(new Property(PropertyKeys.DTSTART.toStringWithArguments("TZID=" + tzid),
				DateTimeUtil.toCalDavString(start, tzid)));
		e.addProperty(new Property(PropertyKeys.DTEND.toStringWithArguments("TZID=" + tzid),
				DateTimeUtil.toCalDavString(end, tzid)));
		e.addProperty(new Property(PropertyKeys.DESC, description));
		e.addProperty(new Property(PropertyKeys.SUMMARY, title));
		return e;
	}
}
