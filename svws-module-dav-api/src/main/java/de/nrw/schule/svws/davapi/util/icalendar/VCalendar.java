package de.nrw.schule.svws.davapi.util.icalendar;

import java.time.Instant;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import de.nrw.schule.svws.core.data.kalender.KalenderEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse repräsentiert einen VCalendar und bietet Methoden, Eigenschaften
 * hinzuzufügen, zu lesen sowie die VCalendar zu serialisieren.
 *
 */
public class VCalendar {

	private static final String VCALENDAR_VALUE = "VCALENDAR";
	/**
	 * Key für den Anfang eines Teils des Kalendereintrags, bspw.
	 * <code>BEGIN:VTIMEZONE</code>
	 */
	static final String BEGIN_PROPERTY_KEY = "BEGIN";
	/**
	 * Key für das Ende eines Teils des Kalendereintrags, bspw.
	 * <code>END:VTIMEZONE</code>
	 */
	static final String END_PROPERTY_KEY = "END";
	/** Konstante für Zeilenumbrüche */
	public static final String LINEBREAK = "\r\n";
	/**
	 * konstante für Zeilenumbrüche innerhalb eines Properties mit führendem
	 * Leerzeichen
	 */
	private static final String FOLDING_SEPERATOR_HTAB = LINEBREAK + (char) 9;
	/**
	 * konstante für Zeilenumbrüche innerhalb eines Properties mit führendem
	 * Tabulator
	 */
	private static final String FOLDING_SEPERATOR_SPACE = LINEBREAK + (char) 32;
	/** das VTimezone-Objekt dieses VCalendars falls gegeben */
	private VTimezone timezone;
	/** serialized payload */
	private String serialized = null;
	/** der minimale Startzeitpunkt aller Events in diesem VCalendar */
	private Instant minStart;
	/** der maximale Endzeitpunkt aller Events in diesem VCalendar */
	private Instant maxEnd;
	/** die Liste der VEVENTS in diesem VCalendar */
	private List<VEvent> events = new Vector<>();
	/**
	 * der typ des VCalendars - RFC schreibt vor, dass es neben der VTIMEZONE
	 * Definition nur einen Typ je VCALENDAR geben darf
	 */
	private VCalendarTyp typ;

	/**
	 * Konstruktor auf basis eines Kalendereintrags
	 * 
	 * @param eintrag der Kalendereintrag
	 */
	public VCalendar(KalenderEintrag eintrag) {
		this(eintrag.data);
		typ = VCalendarTyp.valueOf(eintrag.kalenderTyp);
	}

	/**
	 * Statische Methode zum erstellen einer VCalendar auf Basis eines
	 * KalenderEintrag.
	 *
	 * @param eintrag der KalenderEintrag
	 * @return die VCalendar, die diesen KalenderEintrag repräsentiert.
	 */
	public static VCalendar createVCalendar(KalenderEintrag eintrag) {
		return new VCalendar(eintrag);
	}

	/** default constructor */
	public VCalendar() {
	}

	/**
	 * Konstruktor für .ics String
	 * 
	 * @param data der .ics String
	 */
	public VCalendar(String data) {
		if (data != null) {
			this.serialized = data;
		}
	}

	/**
	 * getter für die Liste der Events in diesem VCalendar
	 * 
	 * @return die Liste der Events
	 */
	public List<VEvent> getEvents() {
		return this.events;
	}

	/**
	 * getter für den VTIMEZONE-Eintrag in diesem VCalendar
	 * 
	 * @return das repräsentierende VTimezone-Objekt
	 */
	public VTimezone getTimezoneDefinition() {
		return this.timezone;
	}

	/**
	 * setter für den VTIMEZONE-Eintrag in diesem VCalendar
	 * 
	 * @param tz die Zeitzonendefinition
	 */
	public void setTimezoneDefinition(VTimezone tz) {
		this.timezone = tz;
	}

	/**
	 * getter für den Typ der Einträge dieses VCalendars
	 * 
	 * @return den Typ der Einträge dieses VCalendars
	 */
	public VCalendarTyp getTyp() {
		return this.typ;
	}

	/**
	 * Methode zum Serialisieren dieser VCard
	 *
	 * @return die VCard als Zeichenkette
	 */
	public String serialize() {
		if (this.serialized != null) {
			return serialized;
		}
		StringBuffer sb = new StringBuffer();
		new Property(BEGIN_PROPERTY_KEY, VCALENDAR_VALUE).serialize(sb);
		new Property("VERSION", "2.0").serialize(sb);
		if (this.timezone != null) {
			this.timezone.serialize(sb);
		}
		for (VEvent event : this.events) {
			event.serialize(sb);
		}
		new Property(END_PROPERTY_KEY, VCALENDAR_VALUE).serialize(sb);
		return sb.toString();
	}

	/**
	 * Utility zum Parsen eines VCalendars aus einem gegebenen .ics String
	 * 
	 * @param vCalendarString der serialisierte VCalendar als String
	 * @return ein VCalendarobjekt, welches den gegebenen String geparst
	 *         repräsentiert
	 */
	public static VCalendar parse(@NotNull String vCalendarString) {
		VCalendar result = new VCalendar(vCalendarString);
		result.parse();
		return result;
	}

	/**
	 * Utility zum Parsen eines VCalendars aus einem gegebenen .ics String
	 * 
	 */
	private void parse() {
		String vCalendarString = unfold(this.serialized);
		String[] split = vCalendarString.split(LINEBREAK);
		List<String> lines = Arrays.asList(split);
		Iterator<String> linesIterator = lines.iterator();
		IProperty property;
		while (linesIterator.hasNext()) {
			property = IProperty.fromString(linesIterator.next());
			if (IProperty.isProperty(property, BEGIN_PROPERTY_KEY, VTimezone.TIMEZONE_BEGIN_VALUE)) {
				VTimezone tz = new VTimezone();
				this.setTimeZone(tz);
				this.setTimeZone(VTimezone.parse(linesIterator));
			} else if (BEGIN_PROPERTY_KEY.equals(property.getKey()) && !VCALENDAR_VALUE.equals(property.getValue())) {
				this.typ = VCalendarTyp.valueOf(property.getValue());
			}
			if (IProperty.isProperty(property, BEGIN_PROPERTY_KEY, VEvent.VEVENT_VALUE)) {
				VEvent event = VEvent.parse(linesIterator);
				this.getEvents().add(event);
				updateMinStartAndMaxEnd(event);
			}
		}
	}

	/**
	 * aktualisiert den kleinesten Start- und größten Endzeitpunkt für diesen
	 * VCalendar anhand des gegebenen Events
	 * 
	 * @param event das Event welches ggf. einen kleineren Start- oder größeren
	 *              Endzeitpunkt enthält
	 */
	private void updateMinStartAndMaxEnd(VEvent event) {
		if (this.maxEnd == null || event.getDTEnd().compareTo(this.maxEnd) > 0) {
			this.maxEnd = event.getDTEnd();
		}
		if (this.minStart == null || event.getDTStart().compareTo(this.minStart) < 0) {
			this.minStart = event.getDTStart();
		}
	}

	/**
	 * Unfolding für den VCalendar Payload. Laut RFC KANN ein Property umgebrochen
	 * werden indem ein whitespace character nach einem Zeilenumbruch folgt. vgl.
	 * https://datatracker.ietf.org/doc/html/rfc2445#section-4.1
	 * 
	 * @param vCalendarString der VCalendar Payload
	 * @return den VCalendarpayload in dem alle Zeilenumbrüche für Folding entfernt
	 *         wurden
	 */
	private static @NotNull String unfold(@NotNull String vCalendarString) {
		return vCalendarString.replace(FOLDING_SEPERATOR_SPACE, "").replace(FOLDING_SEPERATOR_HTAB, "");
	}

	/**
	 * privater setter für das VTimezone Objekt
	 * 
	 * @param tz das VTimezone Objekt
	 */
	private void setTimeZone(VTimezone tz) {
		this.timezone = tz;
	}

	/**
	 * getter für den minimalen Startzeitpunkt
	 * 
	 * @return den minimalen Startzeitpunkt der Events dieses VCalendars
	 */
	public Instant getMinDTStart() {
		return this.minStart;
	}

	/**
	 * getter für den maximalen Endzeitpunkt der Events dieses VCalendars
	 * 
	 * @return den maximalen Endzeitpunkt der Events dieses VCalendars
	 */
	public Instant getMaxDTEnd() {
		return this.maxEnd;
	}

}
