package de.svws_nrw.module.reporting.utils;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;

import de.svws_nrw.core.types.ServerMode;

/**
 * Die gemeinsame Zeitquelle des Reportings. Alles, was in einem Report „heute" oder „jetzt" bedeutet, wird aus dieser Uhr bezogen — das sichtbare
 * Dokumentdatum ebenso wie das signierte Ausstellungsdatum einer Schulbescheinigung. Damit können beide nicht auseinanderlaufen.
 *
 * <p>Die Uhr läuft in der Zeitzone {@code Europe/Berlin} und nicht in der Zeitzone des Servers: Ein Report für eine Schule in NRW soll das deutsche Datum
 * tragen, unabhängig davon, wie der Server konfiguriert ist. Ohne diese Festlegung könnte ein Server in einer anderen Zeitzone um Mitternacht herum ein
 * abweichendes Datum ausweisen.</p>
 *
 * <p>Ausschließlich im Server-Modus {@link ServerMode#DEV} kann über die Umgebungsvariable {@value #ENV_USE_FIXED_DATE} (Boolean) das fest hinterlegte
 * Datum {@link #SNAPSHOT_FIXED_DATE} aktiviert werden, sodass Snapshot-Tests deterministische Zeitstempel erhalten. Außerhalb von DEV wird die Variable
 * ignoriert.</p>
 */
public final class ReportingUhr {

	/** Name der Umgebungsvariable (Boolean), mit der im DEV-Modus das feste Snapshot-Datum aktiviert wird. */
	public static final String ENV_USE_FIXED_DATE = "SVWS_REPORTING_FIXED_DATE";

	/**
	 * Das für Snapshot-Tests fest hinterlegte Datum: der 29.02.1948. Bewusst ein Datum aus der Vor-Computer-Zeit (Schaltjahr, Sonntag), das in einem
	 * echten Dokument sofort als unplausibel auffiele, falls die feste Uhr versehentlich außerhalb der Tests griffe.
	 */
	private static final LocalDate SNAPSHOT_FIXED_DATE = LocalDate.of(1948, Month.FEBRUARY, 29);

	/** Die Zeitzone, in der die Reports ihr Datum und ihre Uhrzeit ausweisen. */
	private static final ZoneId ZONE = ZoneId.of("Europe/Berlin");

	private ReportingUhr() {
		throw new IllegalStateException("Hilfsklasse - Initialisierung nicht möglich.");
	}

	/**
	 * Ermittelt die zu verwendende Uhr. Nur im Server-Modus {@link ServerMode#DEV} und bei auf {@code true} gesetzter Umgebungsvariable
	 * {@value #ENV_USE_FIXED_DATE} wird eine feste Uhr auf {@link #SNAPSHOT_FIXED_DATE} geliefert, ansonsten die Systemzeit in der Zeitzone
	 * {@code Europe/Berlin}.
	 *
	 * @return die zu verwendende {@link Clock}, nie {@code null}.
	 */
	public static Clock standard() {
		if (Boolean.parseBoolean(System.getenv(ENV_USE_FIXED_DATE)) && (ReportingServerUtils.servermode() == ServerMode.DEV)) {
			return Clock.fixed(SNAPSHOT_FIXED_DATE.atStartOfDay(ZONE).toInstant(), ZONE);
		}
		return Clock.system(ZONE);
	}

}
