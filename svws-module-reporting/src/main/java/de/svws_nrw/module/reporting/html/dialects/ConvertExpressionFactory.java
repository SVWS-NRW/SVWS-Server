package de.svws_nrw.module.reporting.html.dialects;

import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.module.reporting.builders.ReportBuilderUtils;

import java.util.Set;

/**
 * Klasse für eine Expression in Thymeleaf zur Ergänzung von Konvertierungsfunktionen.
 */
public class ConvertExpressionFactory implements IExpressionObjectFactory {

	/**
	 * Name der Expression
	 */
	private static final String EXPRESSION_NAME = "convert";

	/**
	 * Liste, die alle Expression-Namen dieser Klasse enthält
	 */
	private static final Set<String> ALL_EXPRESSION_NAMES = Set.of(EXPRESSION_NAME);

	/**
	 * Erstellt eine neue ConvertExpressionFactory
	 */
	public ConvertExpressionFactory() {
		// Standardkonstruktor
	}

	/**
	 * Überschreibt die getAllExpressionObjectNames Methode des IExpressionObjectFactory Interfaces.
	 *
	 * @return Alle Expression-Namen dieser Klasse
	 */
	@Override
	public Set<String> getAllExpressionObjectNames() {
		return ALL_EXPRESSION_NAMES;
	}

	/**
	 * Überschreibt die buildObject Methode des IExpressionObjectFactory Interfaces.
	 *
	 * @param context 				Der Context, mit dem das HTML-Template mit Daten gefüllt wird.
	 * @param expressionObjectName 	Name des Expression-Objekts, das erzeugt werden soll.
	 *
	 * @return 						Das Expression-Objekt, d. h. die Klasse mit den Java-Methoden für die Expression.
	 */
	@Override
	public Object buildObject(final IExpressionContext context, final String expressionObjectName) {
		if (EXPRESSION_NAME.equals(expressionObjectName)) {
			// Der Logger des laufenden Reports reist im Context mit. So schreiben die Hilfsmethoden in den Log genau der Ausgabe, die sie gerade
			// erzeugen - der Dialekt selbst ist an der geteilten TemplateEngine registriert und kennt keinen Report.
			return new ConvertExpressionHelper((context.getVariable(ReportBuilderUtils.VARIABLE_LOGGER) instanceof final Logger logger) ? logger : null);
		}
		return null;
	}

	/**
	 * Überschreibt das Attribut isCacheable des IExpressionObjectFactory Interfaces.
	 *
	 * @param expressionObjectName 	Name des Expression-Objekts.
	 *
	 * @return 						Gibt true zurück.
	 */
	@Override
	public boolean isCacheable(final String expressionObjectName) {
		return true;
	}
}
