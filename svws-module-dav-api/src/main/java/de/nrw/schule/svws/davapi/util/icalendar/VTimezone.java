package de.nrw.schule.svws.davapi.util.icalendar;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import jakarta.validation.constraints.NotNull;

/**
 * 
 * Diese Klasse repräsentiert ein VTIMEZONE-Eintrag innerhalb eines VCALENDAR
 */
public class VTimezone {

	/** konstante für das BEGIN: und END: property der der VTimezone */
	public static final @NotNull String TIMEZONE_BEGIN_VALUE = "VTIMEZONE";
	/** Liste der Properties für die TZID:EUROPE/BERLIN */
	private static final List<String> DEFAULT_TZ_BERLIN_EUROPE_PROPERTIES = Arrays.asList("TZID:Europe/Berlin",
			"BEGIN:DAYLIGHT", "TZOFFSETFROM:+0100", "TZOFFSETTO:+0200", "TZNAME:CEST", "DTSTART:19700329T020000",
			"RRULE:FREQ=YEARLY;BYDAY=-1SU;BYMONTH=3", "END:DAYLIGHT", "BEGIN:STANDARD", "TZOFFSETFROM:+0200",
			"TZOFFSETTO:+0100", "TZNAME:CET", "DTSTART:19701025T030000", "RRULE:FREQ=YEARLY;BYDAY=-1SU;BYMONTH=10",
			"END:STANDARD", "END:VTIMEZONE");
	/** VTimezone für EUROPE/BERLIN */
	public static final VTimezone DEFAULT_TZ_BERLIN_EUROPE = getBerlinEuropeTimezone();
	/** TZID für EUROPE/BERLIN */
	public static final String DEFAULT_TZ_BERLIN_EUROPE_STR = "Europe/Berlin";
	/** Liste der Properties dieses VTIMEZONE-Eintrags */
	private List<IProperty> properties = new Vector<>();

	/**
	 * public default constructor
	 */
	public VTimezone() {
		// empty default constructor
	}

	/**
	 * erstellt ein VTimezone-Objekt für die Zeitzone Europe/Berlin zur Verwendung
	 * beim serialisieren von Kalendern.
	 * 
	 * @return ein vordefiniertes VTimezone Objekt für die Zeitzone Europe/Berlin
	 */
	private static VTimezone getBerlinEuropeTimezone() {
		return parse(DEFAULT_TZ_BERLIN_EUROPE_PROPERTIES.iterator());
	}

	/**
	 * fügt der Liste der Properties ein neues Property hinzu
	 * 
	 * @param property das zuzufügende Property
	 */
	public void addProperty(IProperty property) {
		this.properties.add(property);
	}

	/**
	 * parst die Properties aus dem gegebenen linesIterator zu einem
	 * VTimezone-Objekt
	 * 
	 * @param linesIterator der iterator über die liste der Property-Strings
	 * @return das geparste VTimezone-Objekt
	 */
	static VTimezone parse(Iterator<String> linesIterator) {
		VTimezone tz = new VTimezone();
		IProperty property = IProperty.fromString(linesIterator.next());
		while (!IProperty.isProperty(property, VCalendar.END_PROPERTY_KEY, VTimezone.TIMEZONE_BEGIN_VALUE)) {
			tz.addProperty(property);
			property = IProperty.fromString(linesIterator.next());
		}
		return tz;
	}

	/**
	 * serialisiert diese VTimezone am gegebenen Stringbuffer
	 * 
	 * @param sb der Stringbuffer
	 */
	public void serialize(StringBuffer sb) {
		new Property(VCalendar.BEGIN_PROPERTY_KEY, TIMEZONE_BEGIN_VALUE).serialize(sb);
		for (IProperty p : this.properties) {
			p.serialize(sb);
		}
		new Property(VCalendar.END_PROPERTY_KEY, TIMEZONE_BEGIN_VALUE).serialize(sb);
	}

}
