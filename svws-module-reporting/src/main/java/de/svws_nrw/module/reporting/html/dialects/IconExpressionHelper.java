package de.svws_nrw.module.reporting.html.dialects;

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
}
