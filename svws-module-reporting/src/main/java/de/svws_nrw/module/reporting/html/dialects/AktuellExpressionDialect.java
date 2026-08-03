package de.svws_nrw.module.reporting.html.dialects;

import java.time.Clock;

import de.svws_nrw.module.reporting.utils.ReportingUhr;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

/**
 * Ein Dialect des SVWS-Servers für Thymeleaf, der den Helper {@code #aktuell} bereitstellt.
 * Über diesen Helper können Templates aktuelles Datum/Uhrzeit beziehen, ohne direkt auf
 * {@code java.time}-Klassen oder {@code #dates.createNow()} zuzugreifen.
 *
 * <p>Die Uhr stammt aus {@link ReportingUhr} — derselben Quelle, aus der auch das signierte
 * Ausstellungsdatum der Schulbescheinigung bezogen wird. Dort ist auch das feste Datum für
 * Snapshot-Tests hinterlegt.</p>
 */
public class AktuellExpressionDialect extends AbstractDialect implements IExpressionObjectDialect {

	private final Clock clock;

	/**
	 * Erzeugt den Dialect mit der Standard-Uhr des Reportings (siehe {@link ReportingUhr#standard()}).
	 */
	public AktuellExpressionDialect() {
		this(ReportingUhr.standard());
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
	 * Die verarbeitende ExpressionObjectFactory des Dialects wird festgelegt und zurückgegeben.
	 *
	 * @return Die ExpressionObjectFactory des Dialect
	 */
	@Override
	public IExpressionObjectFactory getExpressionObjectFactory() {
		return new AktuellExpressionFactory(this.clock);
	}

}
