package de.svws_nrw.module.pdf.html.dialects;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

/**
 * Ein Dialect des SVWS-Servers für Thymeleaf um Datumsmethoden zur Verfügung zu stellen.
 */
public class SVWSDateExpressionDialect extends AbstractDialect implements IExpressionObjectDialect {

	/**
	 * Ein Dialect für Thymeleaf, um SVWS spezifische Funktionen zum Datum zu implementieren.
	 */
	public SVWSDateExpressionDialect() {
		super("svwsDate");
	}

	/**
	 * Die verarbeitende ExpressionObjectFactory des Dialects wird festgelegt und zurückgegeben.
	 * @return Die ExpressionObjectFactory des Dialect
	 */
	@Override
	public IExpressionObjectFactory getExpressionObjectFactory() {
		return new SVWSDateExpressionFactory();
	}

}
