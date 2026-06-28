package de.svws_nrw.module.reporting.html.dialects;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

/**
 * Ein Dialect des SVWS-Servers für Thymeleaf, um Icons über {@code #icon} in HTML-Report-Templates zur Verfügung zu stellen.
 */
public class IconExpressionDialect extends AbstractDialect implements IExpressionObjectDialect {

	/**
	 * Ein Dialect für Thymeleaf, um Icon-Funktionen zu implementieren.
	 */
	public IconExpressionDialect() {
		super("icon");
	}

	/**
	 * Die verarbeitende ExpressionObjectFactory des Dialects wird festgelegt und zurückgegeben.
	 *
	 * @return Die ExpressionObjectFactory des Dialect
	 */
	@Override
	public IExpressionObjectFactory getExpressionObjectFactory() {
		return new IconExpressionFactory();
	}

}
