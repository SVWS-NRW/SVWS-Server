package de.svws_nrw.module.reporting.html.dialects;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

/**
 * Ein Dialect des SVWS-Servers für Thymeleaf, um HTML- und CSS-Elemente inline in eine HTML-Datei zu überführen.
 */
public class InlineExpressionDialect extends AbstractDialect implements IExpressionObjectDialect {

	/**
	 * Ein Dialect für Thymeleaf, um Konvertierungsoptionen zu implementieren.
	 */
	public InlineExpressionDialect() {
		super("inline");
	}

	/**
	 * Die verarbeitende ExpressionObjectFactory des Dialects wird festgelegt und zurückgegeben.
	 *
	 * @return Die ExpressionObjectFactory des Dialect
	 */
	@Override
	public IExpressionObjectFactory getExpressionObjectFactory() {
		return new InlineExpressionFactory();
	}

}
