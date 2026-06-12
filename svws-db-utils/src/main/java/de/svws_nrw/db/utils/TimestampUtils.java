package de.svws_nrw.db.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse beinhaltet allgemeine Hilfsmethoden für den Zugriff Zeitstempel
 */
public final class TimestampUtils {

	/** Der Formatter zum Einlesen von Zeitstempeln */
	private static final DateTimeFormatter dateTimeImportFormatter = new DateTimeFormatterBuilder()
			.appendPattern("yyyy-MM-dd")
			.optionalStart().appendLiteral('T').optionalEnd()
			.optionalStart().appendLiteral(' ').optionalEnd()
			.appendPattern("HH:mm:ss")
			.appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
			.toFormatter();


	/** Der Formatter zur Schreiben von Zeitstempeln */
	private static final DateTimeFormatter dateTimeExportFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");


	private TimestampUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}


	/**
	 * Wandelt den übergebenen UTC-Zeitstempel in einen Zeitstempel der Zeitzone Europe/Berlin um.
	 *
	 * @param utcTimstamp   der UTC-Zeitstempel
	 *
	 * @return der umgerechnete Zeitstempel für die Zeitzone Europe/Berlin
	 */
	public static String convertUtcToLocal(final String utcTimstamp) {
		if (utcTimstamp == null) {
			return null;
		}
		try {
			final ZonedDateTime localTime = LocalDateTime.parse(utcTimstamp, dateTimeImportFormatter)
					.atZone(ZoneId.of("UTC"))
					.withZoneSameInstant(ZoneId.of("Europe/Berlin"));
			return localTime.format(dateTimeExportFormatter);
		} catch (final Exception e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Umrechnen des Zeitstempels '%s'.".formatted(utcTimstamp));
		}
	}


	/**
	 * Wandelt den übergebenen Zeitstempel der Zeitzone Europe/Berlin in einen UTC-Zeitstempel um.
	 *
	 * @param localTimstamp   der Zeitstempel
	 *
	 * @return der UTC-Zeitstempel
	 */
	public static String convertLocalToUtc(final String localTimstamp) {
		if (localTimstamp == null) {
			return null;
		}
		try {
			final ZonedDateTime utc = LocalDateTime.parse(localTimstamp, dateTimeImportFormatter)
					.atZone(ZoneId.of("Europe/Berlin"))
					.withZoneSameInstant(ZoneId.of("UTC"));
			return utc.format(dateTimeExportFormatter);
		} catch (final Exception e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Umrechnen des Zeitstempels '%s'.".formatted(localTimstamp));
		}
	}

}
