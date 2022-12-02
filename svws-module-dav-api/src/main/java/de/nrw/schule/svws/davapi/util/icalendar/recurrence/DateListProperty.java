package de.nrw.schule.svws.davapi.util.icalendar.recurrence;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import de.nrw.schule.svws.davapi.util.icalendar.DateTimeUtil;
import de.nrw.schule.svws.davapi.util.icalendar.IProperty;
import de.nrw.schule.svws.davapi.util.icalendar.PropertyKeys;
import de.nrw.schule.svws.davapi.util.icalendar.VCalendar;

/**
 * Diese Klasse repräsentiert ein ICalendar Property für eine Datumsliste (bspw. EXDATE oder RDATE)
 *
 */
public class DateListProperty implements IProperty {

	/** Der Key für dieses Property */
	private PropertyKeys key;

	/** Liste der Daten */
	private List<LocalDate> dateList = new Vector<>();

	/**
	 * Property für eine Liste von Daten, genutzt mit den Keys
	 * {@link PropertyKeys#EXDATE} und {@link PropertyKeys#RDATE}
	 * 
	 * @param key der Key für dieses Property, {@link PropertyKeys#EXDATE} oder
	 *            {@link PropertyKeys#RDATE}
	 */
	public DateListProperty(PropertyKeys key) {
		this.key = key;
	}

	@Override
	public String getKey() {
		return this.key.toStringWithArguments("TIZD=" + DateTimeUtil.TIMEZONE_DEFAULT, "VALUE=DATE");
	}

	@Override
	public String getValue() {
		return dateList.stream().map(DateTimeUtil::toCalDavDateString).collect(Collectors.joining(","));
	}

	@Override
	public void serialize(StringBuffer sb) {
		sb.append(this.getKey());
		sb.append(COLON_CHAR);
		sb.append(this.getValue());
		sb.append(VCalendar.LINEBREAK);
	}

	/**
	 * getter für die Datumsliste
	 * 
	 * @return die Datumsliste
	 */
	public List<LocalDate> getDateList() {
		return this.dateList;
	}

	/**
	 * fügt ein Datum zu dieser Liste der Daten hinzu
	 * 
	 * @param d das Datum
	 */
	public void addDate(LocalDate d) {
		this.dateList.add(d);
	}

	/**
	 * Fügt eine Sammlung von Datumswerten zu dieser Datumsliste hinzu
	 * 
	 * @param dates die Datumswerte
	 */
	public void addAll(Collection<LocalDate> dates) {
		this.dateList.addAll(dates);
	}

	/**
	 * Fügt eine alle Datumswerte von einem Start- bis zu einem Enddatum zu dieser
	 * Liste hinzu. Startdatum ist inklusiv, Enddatum exklusiv
	 * 
	 * @param from Startdatum
	 * @param to   Enddatum
	 */
	public void addAllBetween(final LocalDate from, final LocalDate to) {
		long numOfDaysBetween = ChronoUnit.DAYS.between(from, to);
		List<LocalDate> list = IntStream.iterate(0, i -> i + 1).limit(numOfDaysBetween).mapToObj(from::plusDays).toList();
		this.addAll(list);
	}
}
