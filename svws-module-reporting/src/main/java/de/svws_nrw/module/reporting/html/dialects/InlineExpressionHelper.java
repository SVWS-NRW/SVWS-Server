package de.svws_nrw.module.reporting.html.dialects;

import de.svws_nrw.base.ResourceUtils;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;


/**
 * Die Klasse stellt Hilfsmethoden zur inline-Darstellung von HTML- und CSS-Elementen zur Verfügung, die über einen Thymeleaf-Dialect
 * und dessen ExpressionFactory in HTML-Templates verwendet werden können.
 */
public class InlineExpressionHelper {

	/**
	 * Erstellt einen neuen InlineExpressionHelper
	 */
	public InlineExpressionHelper() {
		// Standardkonstruktor
	}

	/**
	 * Gibt den Inhalt der angegebenen CSS-Datei als String zurück.
	 *
	 * @param relativerCssPfad Der relative Pfad zur CSS-Datei.
	 *
	 * @return CSS-Inhalt als String oder ein leeres String, falls die Datei nicht gefunden wurde.
	 */
	public String css(final String relativerCssPfad) {
		return ResourceUtils.textOrEmpty(ReportingReportvorlage.getRootPfad() + relativerCssPfad);
	}
}
