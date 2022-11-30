package de.nrw.schule.svws.davapi.util.icalendar;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.TimeZone;

import de.nrw.schule.svws.db.converter.current.DatumUhrzeitConverter;
import de.nrw.schule.svws.db.dto.current.svws.dav.DTODavRessource;
import jakarta.validation.constraints.NotNull;

/**
 * Utility-Klasse zum Parsen und Konvertieren von Datums- und Zeitangaben im
 * Kontext von iCalendar-Einträgen.
 */
public class DateTimeUtil {

	/** Formatter für DateTime nach ISO wie es in .ics-Dateien verwendet wird */
	private static final DateTimeFormatter DAV_ISO_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
	/**
	 * Formatter für DateTime nach ISO mit Zeitzone wie es in .ics-Dateien verwendet
	 * wird
	 */
	private static final DateTimeFormatter DAV_ISO_FORMATTER_WITHZONE = DateTimeFormatter
			.ofPattern("yyyyMMdd'T'HHmmssX");
	/**
	 * Die Standardzeitzone
	 */
	public static final String TIMEZONE_DEFAULT = "Europe/Berlin";

	/**
	 * Utility Klasse, privater Konstruktor
	 */
	private DateTimeUtil() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Utility-Methode zum parsen von CalDav-Zeitangaben im Format
	 * <code>20221102T104500</code> oder <code>20221102T104500Z</code>
	 * 
	 * @param input der Input-String aus einem .ics File
	 * @param zone  die Zeitzone in die der String geparst werden soll
	 * @return den geparsten Zeitpunkt
	 */
	public static Instant parseCalDav(@NotNull String input, @NotNull String zone) {
		ZoneId zoneId = null;
		if (ZoneId.getAvailableZoneIds().contains(zone)) {
			zoneId = ZoneId.of(zone);
		} else {
			// MS Outlook nutzt andere Zeitzonen-IDs, die von JodaTime nicht erkannt werden:
			// Potenzieller Fehler: “The datetime zone id ‘W. Europe Standard Time’ is not
			// recognised
			zoneId = TimeZone.getTimeZone(zone).toZoneId();
		}
		return LocalDateTime.parse(input, DAV_ISO_FORMATTER).atZone(zoneId).toInstant();
	}

	/**
	 * Utility-Methode zum parsen von CalDav-Zeitangaben im Format
	 * <code>20221102T104500</code> oder <code>20221102T104500Z</code>. Zunächst
	 * wird versucht nach dem Format <code>20221102T104500Z</code> zu parsen, wenn
	 * dies fehlschlägt, wird auf {@link #parseCalDav(String, String)}
	 * zurückgegriffen und als Zeitzone {@value #TIMEZONE_DEFAULT} genutzt
	 * 
	 * @param input der Input-String aus einem .ics File
	 * @return den geparsten Zeitpunkt
	 */
	public static Instant parseCalDav(@NotNull String input) {
		try {
			return ZonedDateTime.parse(input, DAV_ISO_FORMATTER_WITHZONE).toInstant();
		} catch (DateTimeParseException dtpe) {
			// Ausdruck konnte nicht geparst werden, daher mit default zeitzone parsen
			return parseCalDav(input, TIMEZONE_DEFAULT);
		}
	}

	/**
	 * Utility-Methode zum parsen eines Zeitpunkts aus einem Property. Ist die
	 * Zeitzone im key gegeben, wird {@link #parseCalDav(String, String)} mit der
	 * Zeitzone genutzt, ansonsten {@link #parseCalDav(String)}
	 * 
	 * @param property das Property aus dem der Zeitpunkt genutzt werden soll
	 * @return den geparsten Zeitpunkt
	 */
	public static Instant parseCalDav(@NotNull IProperty property) {
		String[] split = property.getKey().split(";");
		String zone = null;
		boolean isDate = false;
		for (String s : split) {
			int idx = s.indexOf("TZID=");
			if (idx >= 0) {
				zone = s.substring(idx + 5);
			}
			if (s.equalsIgnoreCase("VALUE=DATE")) {
				isDate = true;
			}
		}
		String value = property.getValue() + (isDate ? "T000000" : "");
		if (zone != null) {
			return parseCalDav(value, zone);
		}
		return parseCalDav(value);
	}

	/**
	 * Konvertiert ein {@link Instant} zu einem SQL-Timestamp-String wie er am
	 * {@link DTODavRessource#KalenderStart} verwendet wird
	 * 
	 * @param instant das Instant
	 * @return der String, der den Zeitpunkt repräsentiert
	 */
	public static String toSQLTimeStamp(@NotNull Instant instant) {
		return DatumUhrzeitConverter.instance.convertToEntityAttribute(Timestamp.from(instant));
	}

	/**
	 * Konvertiert ein SQL-Timestamp String wie er an
	 * {@link DTODavRessource#KalenderStart} verwendet wird in ein {@link Instant}
	 * 
	 * @param sql der String für den SQL-Timestamp
	 * @return den Zeitpunkt aus dem SQLTimestamp
	 */
	public static Instant fromSqlTimeStamp(@NotNull String sql) {
		return DatumUhrzeitConverter.instance.convertToDatabaseColumn(sql).toInstant();
	}

	/**
	 * Gibt wieder ob zwei Zeiträume überschneidend sind. Zeiträume überschneiden
	 * sich, wenn es mindestens einen Zeitpunkt gibt, welcher in beiden Zeiträumen
	 * enthalten ist.
	 *
	 * @param rangeStart1 Startzeit des ersten Zeitraums
	 * @param rangeEnd1   Endzeit des ersten Zeitraums
	 * @param rangeStart2 Startzeit des zweiten Zeitraums
	 * @param rangeEnd2   Endzeit des zweiten Zeitraums
	 * @return true, wenn die Zeiträume überlappen
	 */
	public static boolean intersect(Instant rangeStart1, Instant rangeEnd1, Instant rangeStart2, Instant rangeEnd2) {
		if (rangeStart1.compareTo(rangeEnd1) > 0) {
			Instant buf = rangeStart1;
			rangeStart1 = rangeEnd1;
			rangeEnd1 = buf;
		}
		if (rangeStart2.compareTo(rangeEnd2) > 0) {
			Instant buf = rangeStart2;
			rangeStart2 = rangeEnd2;
			rangeEnd2 = buf;
		}
		boolean range1afterRange2 = rangeStart1.compareTo(rangeEnd2) > 0;
		boolean range1beforeRange2 = rangeEnd1.compareTo(rangeStart2) < 0;
		return !range1afterRange2 && !range1beforeRange2;
	}

	/**
	 * Gibt wieder, ob sich ein Zeitpunkt innerhalb eines Zeitraums befindet,
	 * inklusive Start und Endzeitpunkt
	 *
	 * @param rangeStart der Startzeitpunkt der Range
	 * @param rangeEnd   der Endzeitpunkt der Range
	 * @param instant    der Zeitpunkt, der gegen den Zeitraum verglichen werden
	 *                   soll
	 * @return true, wenn instant gleich oder größer als der Start und gleich oder
	 *         kleiner als das Ende ist
	 */
	public static boolean between(Instant rangeStart, Instant rangeEnd, Instant instant) {
		if (rangeStart.compareTo(rangeEnd) >= 0) {
			// Start und Endzeitpunkt vertauschen
			Instant buf = rangeStart;
			rangeStart = rangeEnd;
			rangeEnd = buf;
		}
		return instant.compareTo(rangeStart) >= 0 && instant.compareTo(rangeEnd) <= 0;
	}

	/**
	 * Gibt aus einem SQL-Timestamp-String die Millisekunden seit 1970 wieder, vgl.
	 * {@link Timestamp#getTime()}
	 * 
	 * @param time den SQL-Timestamp String wie er in
	 *             {@link DTODavRessource#KalenderStart} verwendet wird
	 * @return die Millisekunden
	 */
	public static long getTimeInMillis(String time) {
		return DatumUhrzeitConverter.instance.convertToDatabaseColumn(time).getTime();
	}
}
