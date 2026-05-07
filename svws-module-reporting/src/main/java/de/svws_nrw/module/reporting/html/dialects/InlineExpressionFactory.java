package de.svws_nrw.module.reporting.html.dialects;

import java.util.Set;

import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;

/**
 * Klasse für eine Expression in Thymeleaf zur Ergänzung von Funktionen zur inline-Darstellung von HTML- und CSS-Elementen.
 */
public class InlineExpressionFactory implements IExpressionObjectFactory {

	/**
	 * Name der Expression
	 */
	private static final String EXPRESSION_NAME = "inline";

	/**
	 * Eine Liste, die alle Expression-Namen dieser Klasse enthält
	 */
	private static final Set<String> ALL_EXPRESSION_NAMES = Set.of(EXPRESSION_NAME);

	/**
	 * Erstellt eine neue InlineExpressionFactory
	 */
	public InlineExpressionFactory() {
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
			return new InlineExpressionHelper();
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
