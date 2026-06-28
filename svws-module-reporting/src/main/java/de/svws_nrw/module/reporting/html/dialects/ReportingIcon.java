
package de.svws_nrw.module.reporting.html.dialects;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Zentraler Katalog der im Reporting verfügbaren Icons. Die SVG-Pfaddaten werden aus der Ressource
 * {@code icons/icons.json} geladen (fill-basiert, {@code viewBox="0 0 24 24"}); Quelle sind überwiegend
 * RemixIcon-Icons sowie einzelne eigene Symbole. Je Icon-Name ist der Inhalt zwischen den {@code <svg>} Tags
 * hinterlegt (ein oder mehrere {@code <path .../>}) — das umschließende {@code <svg>} mit
 * Größe und Farbe erzeugt die zentrale Rendering-Methode {@link #get(String, int, String)}.
 *
 * <p>Neue Icons werden in {@code icons.json} mit ihren geprüften Pfaddaten ergänzt.</p>
 */
public final class ReportingIcon {

	/** Die Standardgröße eines Icons in Pixeln. */
	public static final int STANDARD_GROESSE = 14;

	/** Die Standardfarbe eines Icons. */
	private static final String STANDARD_FARBE = "black";

	/** Map: Icon-Name (z. B. {@code "external"}) auf die zugehörigen SVG-Pfaddaten. */
	private static final Map<String, String> KATALOG = new LinkedHashMap<>();

	/** Cache der bereits erzeugten Data-URIs, indiziert nach Name, Größe und Farbe. */
	private static final Map<String, String> CACHE = new ConcurrentHashMap<>();

	static {
		try (InputStream stream = ReportingIcon.class.getResourceAsStream("/de/svws_nrw/module/reporting/icons/icons.json")) {
			if (stream == null) {
				throw new IllegalStateException("Die Icons-Ressource 'icons.json' konnte nicht gefunden werden.");
			}
			final ObjectMapper mapper = new ObjectMapper();
			final Map<String, String> icons = mapper.readValue(stream, new TypeReference<Map<String, String>>() {
			});
			KATALOG.putAll(icons);
		} catch (final IOException e) {
			throw new IllegalStateException("Fehler beim Laden der Icons aus der JSON-Ressource.", e);
		}
	}

	private ReportingIcon() {
		// Katalog-Klasse, keine Instanzen
	}

	/**
	 * Liefert die SVG-Pfaddaten zum übergebenen Icon-Namen.
	 *
	 * @param name Der Icon-Name (z. B. {@code "checkbox_checked"}).
	 *
	 * @return Die SVG-Pfaddaten oder ein leerer String, falls der Name unbekannt ist.
	 */
	public static String pfadByName(final String name) {
		if (name == null) {
			return "";
		}
		return KATALOG.getOrDefault(name, "");
	}

	/**
	 * Liefert das SVG für das Icon zum übergebenen Namen in Standardgröße und -farbe
	 * direkt als Base64-codierten Data-URI String.
	 *
	 * @param name Der Icon-Name (z. B. {@code "external"}).
	 *
	 * @return Der Data-URI String des SVGs oder ein leerer String bei unbekanntem Icon.
	 */
	public static String get(final String name) {
		return get(name, STANDARD_GROESSE, STANDARD_FARBE);
	}

	/**
	 * Liefert das SVG für das Icon zum übergebenen Namen in der angegebenen Größe und Standardfarbe
	 * direkt als Base64-codierten Data-URI String.
	 *
	 * @param name      Der Icon-Name (z. B. {@code "external"}).
	 * @param groessePx Die Kantenlänge des SVGs in Pixeln.
	 *
	 * @return Der Data-URI String des SVGs oder ein leerer String bei unbekanntem Icon.
	 */
	public static String get(final String name, final int groessePx) {
		return get(name, groessePx, STANDARD_FARBE);
	}

	/**
	 * Liefert das SVG für das Icon zum übergebenen Namen in der angegebenen Größe und Farbe
	 * direkt als Base64-codierten Data-URI String.
	 *
	 * @param name      Der Icon-Name (z. B. {@code "external"}).
	 * @param groessePx Die Kantenlänge des SVGs in Pixeln.
	 * @param farbe     Die Füllfarbe des Icons (CSS-Farbwert).
	 *
	 * @return Der Data-URI String des SVGs oder ein leerer String bei unbekanntem Icon.
	 */
	public static String get(final String name, final int groessePx, final String farbe) {
		return CACHE.computeIfAbsent(name + "|" + groessePx + "|" + farbe, schluessel -> baueDataUri(name, groessePx, farbe));
	}

	/**
	 * Erzeugt den Base64-codierten Data-URI für das angegebene Icon. Wird nur bei einem Cache-Miss aufgerufen.
	 *
	 * @param name      Der Icon-Name.
	 * @param groessePx Die Kantenlänge des SVGs in Pixeln.
	 * @param farbe     Die Füllfarbe des Icons (CSS-Farbwert).
	 *
	 * @return Der Data-URI String des SVGs oder ein leerer String bei unbekanntem Icon.
	 */
	private static String baueDataUri(final String name, final int groessePx, final String farbe) {
		final String pfad = pfadByName(name);
		if (pfad.isEmpty()) {
			return "";
		}
		final String svg = "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"" + groessePx
				+ "\" height=\"" + groessePx + "\" fill=\"" + farbe + "\">" + pfad + "</svg>";
		return "data:image/svg+xml;base64," + Base64.getEncoder().encodeToString(svg.getBytes(StandardCharsets.UTF_8));
	}
}
