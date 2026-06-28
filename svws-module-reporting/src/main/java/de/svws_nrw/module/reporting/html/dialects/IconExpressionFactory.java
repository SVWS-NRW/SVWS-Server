package de.svws_nrw.module.reporting.html.dialects;

import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Set;

/**
 * Klasse für eine Expression in Thymeleaf zur Ergänzung von Icon-Funktionen ({@code #icon}).
 */
public class IconExpressionFactory implements IExpressionObjectFactory {

	/**
	 * Name der Expression
	 */
	private static final String EXPRESSION_NAME = "icon";

	/**
	 * Liste, die alle Expression-Namen dieser Klasse enthält
	 */
	private static final Set<String> ALL_EXPRESSION_NAMES = Set.of(EXPRESSION_NAME);

	/**
	 * Erstellt eine neue IconExpressionFactory
	 */
	public IconExpressionFactory() {
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
			return new IconExpressionHelper();
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
