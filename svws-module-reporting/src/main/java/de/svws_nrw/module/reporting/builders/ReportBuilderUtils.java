package de.svws_nrw.module.reporting.builders;

import java.io.File;
import java.util.List;
import java.util.Locale;

import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.html.dialects.AktuellExpressionDialect;
import de.svws_nrw.module.reporting.html.dialects.ConvertExpressionDialect;
import de.svws_nrw.module.reporting.html.dialects.IconExpressionDialect;
import de.svws_nrw.module.reporting.html.dialects.InlineExpressionDialect;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import jakarta.ws.rs.core.Response;

/**
 * Hilfsklasse für wiederverwendbare Logik rund um Report-Builder:
 * - Generierung eines Dateinamens aus einer Thymeleaf-Vorlage mit Fallback.
 * - Zusammenführen mehrerer HtmlContext-Objekte zu einem Gesamt-Context.
 */
public final class ReportBuilderUtils {

	/**
	 * Der Name der Thymeleaf-Variablen, unter der der Logger des laufenden Reports im Context bereitsteht.
	 * <p>Die Dialekte werden einmalig an der geteilten TemplateEngine registriert und können deshalb keinen Logger als Feld halten. Über diese Variable
	 * erhält jede Dialekt-Ausführung den Logger genau des Reports, den sie gerade rendert. Der doppelte Unterstrich hält den Namen von den fachlichen
	 * Context-Variablen der Vorlagen getrennt.</p>
	 */
	public static final String VARIABLE_LOGGER = "__logger";

	/**
	 * Die geteilte TemplateEngine für HTML-Report-Templates. Thymeleaf-TemplateEngines sind thread-sicher und als
	 * langlebige Singletons konzipiert; die Instanz wird lazy bei erstem Klassenzugriff erzeugt (JVM-garantiert
	 * thread-sicher) und über alle Builder-Aufrufe hinweg geteilt, damit der Template-Cache der Engine wirkt.
	 */
	private static final TemplateEngine HTML_TEMPLATE_ENGINE = createTemplateEngine(TemplateMode.HTML);

	/** Die geteilte TemplateEngine für TEXT-Mode-Templates (Dateinamensvorlagen). Siehe {@link #HTML_TEMPLATE_ENGINE}. */
	private static final TemplateEngine TEXT_TEMPLATE_ENGINE = createTemplateEngine(TemplateMode.TEXT);

	/**
	 * Die Locale, in der Thymeleaf die Werte einer Vorlage formatiert — etwa das Dezimaltrennzeichen von {@code #numbers.formatDecimal(...)}.
	 * <p>Fest auf {@link Locale#GERMANY} und nicht die Standard-Locale der JVM: Ein Report ist ein deutschsprachiges Dokument und weist deshalb
	 * ein Komma als Dezimaltrennzeichen aus, unabhängig davon, wie der Server konfiguriert ist. Ohne diese Festlegung hinge die Ausgabe an der
	 * Umgebung — derselbe Report ergäbe auf einem Rechner {@code 36,0} und in einem Container {@code 36.0}.</p>
	 */
	private static final Locale LOCALE = Locale.GERMANY;

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
		engine.addDialect(new InlineExpressionDialect());
		engine.addDialect(new IconExpressionDialect());
		engine.addDialect(new AktuellExpressionDialect());
		return engine;
	}


	/**
	 * Gibt die geteilte TemplateEngine für HTML-Report-Templates zurück.
	 * Diese Engine wird für die Verarbeitung von HTML-Report-Templates verwendet.
	 *
	 * @return TemplateEngine im HTML-Mode
	 */
	public static TemplateEngine getHtmlTemplateEngine() {
		return HTML_TEMPLATE_ENGINE;
	}

	/**
	 * Gibt die geteilte TemplateEngine für TEXT-Mode-Templates zurück.
	 * Diese Engine wird speziell für die Verarbeitung von Dateinamensvorlagen (Textual Syntax von Thymeleaf).
	 *
	 * @return TemplateEngine im TEXT-Mode
	 */
	private static TemplateEngine getTextTemplateEngine() {
		return TEXT_TEMPLATE_ENGINE;
	}

	/**
	 * Führt mehrere HtmlContext-Objekte zu einem einzigen Thymeleaf-Context zusammen.
	 *
	 * @param contexts Liste der HtmlContext-Objekte (kann null oder leer sein)
	 *
	 * @return Ein kombinierter Thymeleaf-Context oder ein neuer, leerer Context, falls keine Contexts vorhanden sind.
	 */
	public static Context mergeHtmlContexts(final List<HtmlContext<?>> contexts) {
		// Über diesen Context laufen beide Renderpfade des Moduls - der HTML-Report und die Dateinamensvorlage. Die hier gesetzte Locale legt damit
		// die Formatierung für die gesamte Ausgabe fest; die Locale der zusammengeführten Teil-Contexts spielt keine Rolle, da nur deren Variablen
		// übernommen werden.
		final Context finalContext = new Context(LOCALE);
		if ((contexts == null) || contexts.isEmpty()) {
			return finalContext;
		}

		for (final HtmlContext<?> htmlCtx : contexts) {
			if (htmlCtx != null) {
				final Context ctx = htmlCtx.getContext();
				if (ctx == null) {
					continue;
				}
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
	 * Wenn die Vorlage leer ist oder ihre Auswertung scheitert, wird ein leerer String zurückgegeben - außer bei einer Infrastrukturstörung, die die Ausgabe
	 * beendet.
	 *
	 * @param dateinamensvorlage Die Vorlage für den zu generierenden Dateinamen. Kann null oder leer sein.
	 * @param contexts           Eine Liste von HtmlContext-Objekten, die zusätzliche Variablen für die Vorlage enthalten. Darf null oder leer sein.
	 *
	 * @return Der generierte Dateiname. Ist die Vorlage leer oder liefert sie keinen Namen, wird ein leerer String zurückgegeben.
	 *
	 * @throws ApiOperationException Mit dem Status 500, wenn die Auswertung an einer Infrastrukturstörung scheitert.
	 */
	public static String generiereDateinameAusVorlage(final String dateinamensvorlage, final List<HtmlContext<?>> contexts) {

		if ((dateinamensvorlage == null) || dateinamensvorlage.isBlank()) {
			return "";
		}

		final Context context = mergeHtmlContexts(contexts);

		try {
			// Verwende immer TEXT-Mode Engine für Dateinamensvorlagen (ignoriere übergebene Engine)
			final TemplateEngine textEngine = getTextTemplateEngine();

			// Verarbeite die Vorlage im TEXT-Mode (unterstützt Textual Syntax)
			final String textOutput = textEngine.process(dateinamensvorlage, context);

			// Der Output ist bereits reiner Text, kein HTML-Parsing notwendig. Aber der Output enthält auch die Formatierungen aus der Textdatei.
			// Entferne daher Tabs, Zeilenumbrüche und weitere Whitespaces (außer Space) wie Formfeed, Vertical Tab, ggf. Unicode-Whitespaces.
			final String dateiname = textOutput.replace("\t", "").replaceAll("\\R\\s*", "").replaceAll("[^\\S ]+", "").trim();

			if (dateiname.isBlank()) {
				return "";
			}

			if (istValiderDateiname(dateiname)) {
				return dateiname;
			}

			return "";
		} catch (final Exception e) {
			// Eine Infrastrukturstörung ist kein Grund für den Rückfall auf den statischen Namen: Sie beendet die Ausgabe. Entschieden wird über die
			// Ursachenkette und nicht über den Fehlertyp, weil Thymeleaf den Fehler eines Datenzugriffs in der Vorlage verpackt.
			if (ReportingProblemursache.fuerLadefehler(e).istAbbruch()) {
				throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, e,
						"### FEHLER: Der Dateiname konnte wegen einer Störung des Servers nicht ermittelt werden.");
			}
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
		if ((dateiname == null) || dateiname.isBlank()) {
			return false;
		}
		try {
			//noinspection ResultOfMethodCallIgnored
			new File(dateiname).getCanonicalFile();
			return true;
		} catch (@SuppressWarnings("unused") final Exception ignore) {
			return false;
		}
	}
}
