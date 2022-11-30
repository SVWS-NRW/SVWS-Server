package de.nrw.schule.svws.davapi.util.icalendar;

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
	private Instant dtStartProperty;
	/** der Endzeitpunkt des Events */
	private Instant dtEndProperty;
	/** die Properties dieses Events */
	private List<IProperty> properties = new Vector<>();

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
		return this.dtStartProperty;
	}

	/**
	 * getter für den Endzeitpunkt des Events
	 * 
	 * @return den Endzeitpunkt
	 */
	public Instant getDTEnd() {
		return this.dtEndProperty;
	}

	/**
	 * Fügt ein Property zu diesem Event hinzu und parst dieses gegebenenfalls in
	 * Start- und Endzeitpunkt.
	 * 
	 * @param property das zuzufügende Property
	 */
	public void addProperty(IProperty property) {
		if (property.getKey().startsWith(DTSTART_KEY)) {
			this.dtStartProperty = DateTimeUtil.parseCalDav(property);
		} else if (property.getKey().startsWith(DTEND_KEY)) {
			this.dtEndProperty = DateTimeUtil.parseCalDav(property);
		} else {
			this.properties.add(property);
		}
	}

	/**
	 * parst ein VEvent aus dem gegebenen Iterator über die Zeilen eines
	 * serialisierten VCalendars
	 * 
	 * @param linesIterator der iterator über die Zeilen des serialisierten
	 *                      VCalendars
	 * @return das VEvent mit den geparsten Properties
	 */
	static VEvent parse(Iterator<String> linesIterator) {
		IProperty property = IProperty.fromString(linesIterator.next());
		VEvent event = new VEvent();
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
	public void serialize(StringBuffer sb) {
		new Property(VCalendar.BEGIN_PROPERTY_KEY, VEVENT_VALUE).serialize(sb);
		for (IProperty p : properties) {
			p.serialize(sb);
		}
		// TODO serialisieren von DTStart und DTEnd fehlt
		new Property(VCalendar.END_PROPERTY_KEY, VEVENT_VALUE).serialize(sb);
	}

}
