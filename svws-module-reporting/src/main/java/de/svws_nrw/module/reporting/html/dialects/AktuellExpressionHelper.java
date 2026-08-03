package de.svws_nrw.module.reporting.html.dialects;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Die Klasse stellt Hilfsmethoden für aktuelles Datum und aktuelle Uhrzeit zur Verfügung, die über
 * einen Thymeleaf-Dialect und dessen ExpressionFactory in HTML-Templates verwendet werden können.
 *
 * <p>Die zugrunde liegende {@link Clock} wird beim Erzeugen des Helpers gesetzt. Standardmäßig stammt sie aus
 * {@code ReportingUhr.standard()} und läuft in der Zeitzone {@code Europe/Berlin}; in Test-Szenarien kann eine feste Uhr eingespielt werden,
 * sodass Snapshot-Vergleiche deterministisch sind.</p>
 */
// Intern arbeitet der Helper durchgängig mit java.time; die Date-Rückgabe ist ein bewusster Adapter an die Thymeleaf-Template-API, weil zahlreiche
// Vorlagen das Ergebnis an #dates.format(...) übergeben.
public class AktuellExpressionHelper {

	private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ISO_LOCAL_DATE;

	private static final DateTimeFormatter ZEIT_HHMM = DateTimeFormatter.ofPattern("HH:mm");

	private final Clock clock;

	/**
	 * Erstellt einen neuen AktuellExpressionHelper mit der übergebenen {@link Clock}.
	 *
	 * @param clock die Uhr, aus der „heute"/„jetzt" bezogen werden. Darf nicht {@code null} sein.
	 */
	public AktuellExpressionHelper(final Clock clock) {
		this.clock = clock;
	}

	/**
	 * Liefert den aktuellen Zeitpunkt als {@link Date} – ersetzt {@code #dates.createNow()}.
	 *
	 * @return Der aktuelle Zeitpunkt gemäß der eingestellten {@link Clock}.
	 */
	public Date jetztAlsDate() {
		return Date.from(clock.instant());
	}

	/**
	 * Liefert das aktuelle Datum als String im ISO-Format {@code yyyy-MM-dd}.
	 *
	 * @return Das aktuelle Datum gemäß der eingestellten {@link Clock} im ISO-Format.
	 */
	public String datum() {
		return LocalDate.now(clock).format(ISO_DATE);
	}

	/**
	 * Liefert die aktuelle Uhrzeit als String im Format {@code HH:mm}.
	 *
	 * @return Die aktuelle Uhrzeit gemäß der eingestellten {@link Clock} im Format HH:mm.
	 */
	public String uhrzeit() {
		return LocalTime.now(clock).format(ZEIT_HHMM);
	}

	/**
	 * Liefert den aktuellen Zeitpunkt als {@link LocalDateTime} (für weitergehende Formatierungen).
	 *
	 * @return Der aktuelle Zeitpunkt gemäß der eingestellten {@link Clock} als LocalDateTime.
	 */
	public LocalDateTime jetzt() {
		return LocalDateTime.now(clock);
	}

}
