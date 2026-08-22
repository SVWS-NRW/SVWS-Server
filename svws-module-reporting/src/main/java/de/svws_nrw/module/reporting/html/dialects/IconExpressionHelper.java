package de.svws_nrw.module.reporting.html.dialects;

import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;

/**
 * Die Klasse stellt Icon-Methoden zur Verfügung, die über einen Thymeleaf-Dialect ({@code #icon}) und dessen
 * ExpressionFactory in HTML-Report-Templates verwendet werden können. Ein Icon wird als SVG (RemixIcon, fill-basiert)
 * erzeugt und als Data-URI in ein {@code <img>}-Element eingebettet — analog zu
 * {@link ConvertExpressionHelper#toCheckboxSVG(Boolean, int)}.
 *
 * <p>Die Generierung und der Pfaddaten-Katalog werden zentral über {@link ReportingIcon} gesteuert.</p>
 */
public class IconExpressionHelper {

	/**
	 * Erstellt einen neuen IconExpressionHelper.
	 */
	public IconExpressionHelper() {
		// Standardkonstruktor
	}

	/**
	 * Liefert das {@code <img>}-Element für das Icon zum übergebenen Namen in Standardgröße und -farbe.
	 *
	 * @param name Der Icon-Name (z. B. {@code "external"}).
	 *
	 * @return Das {@code <img>}-Element als String oder ein leerer String, falls der Name unbekannt ist.
	 */
	public String get(final String name) {
		return get(name, ReportingIcon.STANDARD_GROESSE);
	}

	/**
	 * Liefert das {@code <img>}-Element für das Icon zum übergebenen Namen in der angegebenen Größe und Standardfarbe.
	 *
	 * @param name      Der Icon-Name (z. B. {@code "external"}).
	 * @param groessePx Die Kantenlänge des Icons in Pixeln.
	 *
	 * @return Das {@code <img>}-Element als String oder ein leerer String, falls der Name unbekannt ist.
	 */
	public String get(final String name, final int groessePx) {
		final String src = ReportingIcon.get(name, groessePx);
		if (src.isEmpty()) {
			return "";
		}
		return "<img class=\"icon\" width=\"" + groessePx + "\" height=\"" + groessePx + "\" src=\"" + src + "\" alt=\"\" />";
	}

	/**
	 * Liefert das {@code <img>}-Element für das Icon zum übergebenen Namen in der angegebenen Größe und Farbe.
	 * Die Farbe wird über {@code fill} fest in das SVG geschrieben, da ein per {@code <img>} eingebettetes SVG die
	 * CSS-Farbe des umgebenden Textes nicht erbt.
	 *
	 * @param name      Der Icon-Name (z. B. {@code "external"}).
	 * @param groessePx Die Kantenlänge des Icons in Pixeln.
	 * @param farbe     Die Füllfarbe des Icons (CSS-Farbwert, z. B. {@code "black"} oder {@code "#c00"}).
	 *
	 * @return Das {@code <img>}-Element als String oder ein leerer String, falls der Name unbekannt ist.
	 */
	public String get(final String name, final int groessePx, final String farbe) {
		final String src = ReportingIcon.get(name, groessePx, farbe);
		if (src.isEmpty()) {
			return "";
		}
		return "<img class=\"icon\" width=\"" + groessePx + "\" height=\"" + groessePx + "\" src=\"" + src + "\" alt=\"\" />";
	}

	/**
	 * Liefert das Extern-Icon (Icon-Name {@code "external"}) in der angegebenen Größe und ergänzt optional das Kürzel der
	 * Stammschule als Text rechts neben dem Icon, sofern der übergebene Schüler ein externer Schüler ist.
	 *
	 * @param groessePx                 Die Kantenlänge des Icons in Pixeln.
	 * @param reportingSchueler         Der Schüler, für den das Extern-Icon erzeugt werden soll.
	 * @param mitExternesSchulKuerzel   Gibt an, ob das Kürzel der externen Schule rechts neben dem Icon erscheinen soll.
	 * @param fuehrenderText            Legt den Text fest, der vor dem Icon angegeben werden soll.
	 * @param folgenderText             Legt den Text fest, der nach dem Icon angegeben werden soll.
	 *
	 * @return Das HTML-Fragment als String oder ein leerer String, falls das Icon unbekannt ist oder der Schüler kein externer Schüler ist.
	 */
	public String getExtern(final int groessePx, final ReportingSchueler reportingSchueler, final boolean mitExternesSchulKuerzel,
			final String fuehrenderText, final String folgenderText) {
		String result = "";
		if ((reportingSchueler == null) || (reportingSchueler.status() != SchuelerStatus.EXTERN)) {
			return result;
		}
		final String img = get("external", groessePx);
		final String kuerzel = reportingSchueler.externesSchulKuerzel();
		if (!img.isBlank()) {
			result = img;
			if (mitExternesSchulKuerzel && !kuerzel.isBlank()) {
				result = "<span class=\"icon-extern\">" + img + "<span class=\"icon-extern-kuerzel\">" + escapeXml(kuerzel) + "</span></span>";
			}
		}
		return result.isBlank() ? "" : (escapeXml(fuehrenderText) + result + escapeXml(folgenderText));
	}


	/**
	 * Liefert die Data-URI des Icons in der angegebenen Farbe für ein eigenes {@code <img>}-Element der Vorlage - etwa wenn dessen Größe in Millimetern
	 * gesetzt wird und das feste Pixelmaß von {@link #get(String, int, String)} deshalb nicht passt. Das SVG skaliert verlustfrei.
	 *
	 * @param name  Der Icon-Name (z. B. {@code "error_warning"}).
	 * @param farbe Die Füllfarbe des Icons (CSS-Farbwert).
	 *
	 * @return Der Data-URI String des SVGs oder ein leerer String bei unbekanntem Icon.
	 */
	public String getSrc(final String name, final String farbe) {
		return ReportingIcon.get(name, ReportingIcon.STANDARD_GROESSE, farbe);
	}

	/**
	 * Maskiert die für XML/XHTML kritischen Zeichen, damit ein Text gefahrlos in das per {@code th:utext} eingebundene
	 * Fragment übernommen werden kann.
	 *
	 * @param text Der zu maskierende Text.
	 *
	 * @return Der maskierte Text.
	 */
	private static String escapeXml(final String text) {
		return (text == null) ? "" : text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
	}
}
