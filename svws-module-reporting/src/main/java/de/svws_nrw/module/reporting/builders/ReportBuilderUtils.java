package de.svws_nrw.module.reporting.builders;

import java.io.File;
import java.util.List;

import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.html.dialects.ConvertExpressionDialect;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

/**
 * Hilfsklasse für wiederverwendbare Logik rund um Report-Builder:
 * - Generierung eines Dateinamens aus einer Thymeleaf-Vorlage mit Fallback.
 * - Zusammenführen mehrerer HtmlContext-Objekte zu einem Gesamt-Context.
 */
public final class ReportBuilderUtils {

	private ReportBuilderUtils() {
		// Utility
	}

	/**
	 * Erstellt eine minimal konfigurierte TemplateEngine für String-Templates.
	 *
	 * @param templateMode Der Template-Mode (HTML oder TEXT)
	 * @return TemplateEngine mit dem angegebenen Mode
	 */
	private static TemplateEngine createTemplateEngine(final TemplateMode templateMode) {
		final ClassLoaderTemplateResolver classLoaderResolver = new ClassLoaderTemplateResolver();
		classLoaderResolver.setTemplateMode(templateMode);
		classLoaderResolver.setPrefix("de/svws_nrw/module/reporting/");
		classLoaderResolver.setSuffix((templateMode == TemplateMode.HTML) ? ".html" : "");
		classLoaderResolver.setCharacterEncoding("UTF-8");
		classLoaderResolver.setOrder(1);
		classLoaderResolver.setCheckExistence(true);

		final StringTemplateResolver stringResolver = new StringTemplateResolver();
		stringResolver.setTemplateMode(templateMode);
		stringResolver.setOrder(2);

		final TemplateEngine engine = new TemplateEngine();
		engine.addTemplateResolver(classLoaderResolver);
		engine.addTemplateResolver(stringResolver);
		engine.addDialect(new ConvertExpressionDialect());
		return engine;
	}


	/**
	 * Erstellt eine TemplateEngine für HTML-Report-Templates.
	 * Diese Engine wird für die Verarbeitung von HTML-Report-Templates verwendet.
	 *
	 * @return TemplateEngine im HTML-Mode
	 */
	public static TemplateEngine createHtmlTemplateEngine() {
		return createTemplateEngine(TemplateMode.HTML);
	}

	/**
	 * Erstellt eine TemplateEngine für TEXT-Mode-Templates.
	 * Diese Engine wird speziell für die Verarbeitung von Dateinamensvorlagen (Textual Syntax von Thymeleaf).
	 *
	 * @return TemplateEngine im TEXT-Mode
	 */
	private static TemplateEngine createTextTemplateEngine() {
		return createTemplateEngine(TemplateMode.TEXT);
	}

	/**
	 * Führt mehrere HtmlContext-Objekte zu einem einzigen Thymeleaf-Context zusammen.
	 *
	 * @param contexts Liste der HtmlContext-Objekte (kann null oder leer sein)
	 *
	 * @return Ein kombinierter Thymeleaf-Context oder ein neuer, leerer Context, falls keine Contexts vorhanden sind.
	 */
	public static Context mergeHtmlContexts(final List<HtmlContext<?>> contexts) {
		final Context finalContext = new Context();
		if ((contexts == null) || contexts.isEmpty())
			return finalContext;

		for (final HtmlContext<?> htmlCtx : contexts) {
			if (htmlCtx != null) {
				final Context ctx = htmlCtx.getContext();
				if (ctx == null)
					continue;
				for (final String variable : ctx.getVariableNames()) {
					finalContext.setVariable(variable, ctx.getVariable(variable));
				}
			}
		}
		return finalContext;
	}

	/**
	 * Generiert einen Dateinamen basierend auf einer Vorlage unter Verwendung zusätzlicher Kontextinformationen.
	 * Die Vorlage wird im TEXT-Mode verarbeitet und unterstützt die Textual Syntax von Thymeleaf.
	 * Wenn die Vorlage leer ist, wird ein leerer String zurückgegeben. Tritt ein Fehler während der Dateinamensgenerierung auf,
	 * wird ebenfalls ein leerer String zurückgegeben.
	 *
	 * @param dateinamensvorlage Die Vorlage für den zu generierenden Dateinamen. Darf nicht null oder leer sein.
	 * @param contexts           Eine Liste von HtmlContext-Objekten, die zusätzliche Variablen für die Vorlage enthalten. Darf null oder leer sein.
	 *
	 * @return Der generierte Dateiname. Ist die Vorlage leer oder tritt ein Fehler auf, wird ein leerer String zurückgegeben.
	 */
	public static String generiereDateinameAusVorlage(final String dateinamensvorlage, final List<HtmlContext<?>> contexts) {

		if ((dateinamensvorlage == null) || dateinamensvorlage.isBlank())
			return "";

		final Context context = mergeHtmlContexts(contexts);

		try {
			// Verwende immer TEXT-Mode Engine für Dateinamensvorlagen (ignoriere übergebene Engine)
			final TemplateEngine textEngine = createTextTemplateEngine();

			// Verarbeite die Vorlage im TEXT-Mode (unterstützt Textual Syntax)
			final String textOutput = textEngine.process(dateinamensvorlage, context);

			// Der Output ist bereits reiner Text, kein HTML-Parsing notwendig. Aber der Output enthält auch die Formatierungen aus der Textdatei.
			// Entferne daher Tabs, Zeilenumbrüche und weitere Whitespaces (außer Space) wie Formfeed, Vertical Tab, ggf. Unicode-Whitespaces.
			final String dateiname = textOutput.replace("\t", "").replaceAll("\\R\\s*", "").replaceAll("[^\\S ]+", "").trim();

			if (dateiname.isBlank())
				return "";

			if (istValiderDateiname(dateiname))
				return dateiname;

			return "";
		} catch (final Exception e) {
			// Bei Fehler leeren String zurückgeben (Fallback auf statischen Namen)
			return "";
		}
	}

	/**
	 * Überprüft, ob ein übergebener String einen gültigen Dateinamen darstellt.
	 *
	 * @param dateiname Der zu überprüfende Dateiname als String. Darf nicht null oder leer sein.
	 *
	 * @return true, wenn der Dateiname gültig ist; false, wenn er null, leer oder ungültig ist.
	 */
	private static boolean istValiderDateiname(final String dateiname) {
		if ((dateiname == null) || dateiname.isBlank())
			return false;
		try {
			//noinspection ResultOfMethodCallIgnored
			new File(dateiname).getCanonicalFile();
			return true;
		} catch (@SuppressWarnings("unused") final Exception e) {
			return false;
		}
	}
}
