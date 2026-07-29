package de.svws_nrw.module.reporting.html.dialects;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

import de.svws_nrw.module.reporting.utils.ReportingServerUtils;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

import de.svws_nrw.core.types.ServerMode;

/**
 * Ein Dialect des SVWS-Servers für Thymeleaf, der den Helper {@code #aktuell} bereitstellt.
 * Über diesen Helper können Templates aktuelles Datum/Uhrzeit beziehen, ohne direkt auf
 * {@code java.time}-Klassen oder {@code #dates.createNow()} zuzugreifen.
 *
 * <p>In Produktion liefert der Dialect die System-Uhr. Ausschließlich im Server-Modus
 * {@link ServerMode#DEV} kann über die Umgebungsvariable {@value #ENV_USE_FIXED_DATE} (Boolean)
 * das fest hinterlegte Datum {@link #SNAPSHOT_FIXED_DATE} aktiviert werden, sodass Snapshot-Tests
 * deterministische Zeitstempel erhalten. Außerhalb von DEV wird die Variable ignoriert.</p>
 */
public class AktuellExpressionDialect extends AbstractDialect implements IExpressionObjectDialect {

	/** Name der Umgebungsvariable (Boolean), mit der im DEV-Modus das feste Snapshot-Datum aktiviert wird. */
	public static final String ENV_USE_FIXED_DATE = "SVWS_REPORTING_FIXED_DATE";

	/**
	 * Das für Snapshot-Tests fest hinterlegte Datum: der 29.02.1948. Bewusst ein Datum aus
	 * der Vor-Computer-Zeit (Schaltjahr, Sonntag), das in einem echten Dokument sofort als unplausibel
	 * auffiele, falls die feste Uhr versehentlich außerhalb der Tests griffe.
	 */
	private static final LocalDate SNAPSHOT_FIXED_DATE = LocalDate.of(1948, 2, 29);

	private final Clock clock;

	/**
	 * Erzeugt den Dialect mit der Standard-Uhr. Diese ist in Produktion die System-Uhr; nur im
	 * Server-Modus {@link ServerMode#DEV} wird bei gesetzter Umgebungsvariable {@value #ENV_USE_FIXED_DATE}
	 * das feste Datum {@link #SNAPSHOT_FIXED_DATE} berücksichtigt.
	 */
	public AktuellExpressionDialect() {
		this(standardClock());
	}

	/**
	 * Erzeugt den Dialect mit einer explizit übergebenen {@link Clock}. Vorgesehen für Tests, die
	 * eine feste Uhr direkt einspielen möchten.
	 *
	 * @param clock die Uhr, aus der „heute"/„jetzt" bezogen werden. Darf nicht {@code null} sein.
	 */
	public AktuellExpressionDialect(final Clock clock) {
		super("aktuell");
		this.clock = clock;
	}

	/**
	 * Ermittelt die in Produktion zu verwendende Uhr. Nur im Server-Modus {@link ServerMode#DEV} und
	 * bei auf {@code true} gesetzter Umgebungsvariable {@value #ENV_USE_FIXED_DATE} wird eine feste Uhr auf
	 * {@link #SNAPSHOT_FIXED_DATE} geliefert, ansonsten die System-Uhr.
	 *
	 * @return die zu verwendende {@link Clock}, nie {@code null}.
	 */
	private static Clock standardClock() {
		if (Boolean.parseBoolean(System.getenv(ENV_USE_FIXED_DATE)) && (ReportingServerUtils.servermode() == ServerMode.DEV)) {
			final ZoneId zone = ZoneId.systemDefault();
			return Clock.fixed(SNAPSHOT_FIXED_DATE.atStartOfDay(zone).toInstant(), zone);
		}
		return Clock.systemDefaultZone();
	}

	/**
	 * Die verarbeitende ExpressionObjectFactory des Dialects wird festgelegt und zurückgegeben.
	 *
	 * @return Die ExpressionObjectFactory des Dialect
	 */
	@Override
	public IExpressionObjectFactory getExpressionObjectFactory() {
		return new AktuellExpressionFactory(this.clock);
	}

}
