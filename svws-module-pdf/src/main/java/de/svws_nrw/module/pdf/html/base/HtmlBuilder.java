package de.svws_nrw.module.pdf.html.base;

import de.svws_nrw.base.ResourceUtils;
import de.svws_nrw.module.pdf.html.dialects.SVWSDateExpressionDialect;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.List;
import java.util.Map;

/**
 * Erzeugt aus einem Thymeleaf-html-Template (html-Vorlage) und den zugehörigen Daten in den Contexts das finale html inklusive der Daten.
 */
public class HtmlBuilder {

    /**
     * html-Inhalt der Vorlagendatei. Wird bei Initialisierung aus der Datei unter dem übergebenen Pfad eingelesen.
     */
    private final String htmlVorlage;


    /**
     * Liste mit Daten-Contexts, die zu einem finalen Context zusammengefügt werden, um damit das html-Template zu füllen.
     */
    private final List<HtmlContext> contexts;


    /**
     * Variablen, die dem finalen Daten-Context hinzugefügt werden sollen, bevor damit das html-Template gefüllt wird.
     */
    private final Map<String, Object> variables;


    /**
     * Erstellt einen neunen html-Builder und initialisiert die Variablen
	 *
	 * @param htmlVorlageDateipfad  Pfad zur html-Vorlagendatei, die mit Daten gefüllt werden soll
     * @param contexts 		        Liste mit Daten-Contexts, die zu einem finalen Context zusammengefügt werden, um damit das html-Template zu füllen.
     * @param variables		        Variablen, die dem finalen Daten-Context hinzugefügt werden sollen, bevor damit das html-Template gefüllt wird.
     */
    public HtmlBuilder(final String htmlVorlageDateipfad, final List<HtmlContext> contexts, final Map<String, Object> variables) {
        this.contexts = contexts;
        this.variables = variables;
        this.htmlVorlage = ResourceUtils.text(htmlVorlageDateipfad);
    }


    /**
     * Erstellt das finale html-Dokument mit den Daten, das dann z. B. für die Erzeugung der PDF-Datei genutzt werden kann.
     * Hierzu werden die Variablen in der html-Vorlage durch Daten ersetzt.
     *
     * @return 		Das finale html mit den Daten
     */
    public String getHtml() {

        StringTemplateResolver resolver = new StringTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(resolver);

		// Dialects des SVWS-Servers für weitere Funktionen hinzufügen.
		templateEngine.addDialect(new SVWSDateExpressionDialect());

        // Füge die übergebenen Contexts zu einem Context zusammen.
        Context finalContext = new Context();
        if (contexts != null && !contexts.isEmpty()) {
            for (HtmlContext htmlCtx : contexts) {
                for (final String variable : htmlCtx.getContext().getVariableNames()) {
                    finalContext.setVariable(variable, htmlCtx.getContext().getVariable(variable));
                }
            }
        }
        // Ergänze im Context weitere übergebene Variablen.
        if (variables != null && !variables.isEmpty())
            finalContext.setVariables(variables);

        if (!(finalContext.getVariableNames().isEmpty() || htmlVorlage == null))
            return templateEngine.process(htmlVorlage, finalContext);
        else
            return "";
    }
}
