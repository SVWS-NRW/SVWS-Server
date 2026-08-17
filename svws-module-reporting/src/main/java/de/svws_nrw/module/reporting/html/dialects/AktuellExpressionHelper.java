package de.svws_nrw.module.reporting.html.dialects;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Die Klasse stellt Hilfsmethoden für aktuelles Datum und aktuelle Uhrzeit zur Verfügung, die über
 * einen Thymeleaf-Dialect und dessen ExpressionFactory in HTML-Templates verwendet werden können.
 *
 * <p>Die zugrunde liegende {@link Clock} wird beim Erzeugen des Helpers gesetzt. Standardmäßig stammt sie aus
 * {@code ReportingUhr.standard()} und läuft in der Zeitzone des Servers; in Test-Szenarien kann eine feste Uhr eingespielt werden,
 * sodass Snapshot-Vergleiche deterministisch sind.</p>
 *
 * <p>Alle Methoden lesen über {@code java.time} aus der Uhr und übernehmen dabei deren Zeitzone. Vorlagen formatieren einen Zeitpunkt deshalb
 * über {@link #formatiert(String)} und nicht über {@code #dates.format(...)}: Letzteres arbeitet auf {@link java.util.Date}, das keine Zeitzone
 * mitführt, und bände die Ausgabe damit an die Default-Zone der JVM statt an die der Uhr. Bei einer festen Uhr fiele das Ergebnis dadurch je nach
 * Zeitzone des ausführenden Systems unterschiedlich aus.</p>
 */
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
	 * Formatiert den aktuellen Zeitpunkt nach dem angegebenen Muster, z. B. {@code dd.MM.yyyy HH:mm} – ersetzt {@code #dates.format(...)}.
	 *
	 * @param muster das Formatmuster gemäß {@link DateTimeFormatter#ofPattern(String)}.
	 *
	 * @return Der formatierte Zeitpunkt gemäß der eingestellten {@link Clock}.
	 */
	public String formatiert(final String muster) {
		return LocalDateTime.now(clock).format(DateTimeFormatter.ofPattern(muster));
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
