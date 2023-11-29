package de.svws_nrw.module.pdf.html.dialects;

import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Set;

/**
 * Klasse für eine Expression in Thymeleaf zur Ergänzung von Datumsfunktionen aus dem SVWS-Server
 */
public class SVWSDateExpressionFactory implements IExpressionObjectFactory {

	/**
	 * Name der Expression
	 */
	private static final String expressionName = "svwsDate";

	/**
	 * Liste, die alle Expression-Namen dieser Klasse enthält
	 */
	private static final Set<String> allExpressionNames = Set.of(expressionName);

	/**
	 * Erstellt einen neue SVWSDateExpressionFactory
	 */
	public SVWSDateExpressionFactory() {
		// leerer Konstruktor, um diesen mit einem JavaDoc-Kommentar versehen zu können.
	}

	/**
	 * Überschreibt die getAllExpressionObjectNames Methode des IExpressionObjectFactory Interfaces.
	 * @return Alle Expression-Namen dieser Klasse
	 */
	@Override
	public Set<String> getAllExpressionObjectNames() {
		return allExpressionNames;
	}

	/**
	 * Überschreibt die buildObject Methode des IExpressionObjectFactory Interfaces.
	 * @param context 				Der Context, mit dem das html-Template mit Daten gefüllt wird.
	 * @param expressionObjectName 	Name des Expression-Objekts, das erzeugt werden soll.
	 * @return 						Das Expression-Objekt, d. h. die Klasse mit den Java-Methoden für die Expression.
	 */
	@Override
	public Object buildObject(final IExpressionContext context, final String expressionObjectName) {
		if (expressionName.equals(expressionObjectName)) {
			return new SVWSDateExpressionHelper();
		}
		return null;
	}

	/**
	 * Überschreibt das Attribute isCacheable des IExpressionObjectFactory Interfaces.
	 * @param expressionObjectName 	Name des Expression-Objekts.
	 * @return 						Gibt true zurück.
	 */
	@Override
	public boolean isCacheable(final String expressionObjectName) {
		return true;
	}
}
