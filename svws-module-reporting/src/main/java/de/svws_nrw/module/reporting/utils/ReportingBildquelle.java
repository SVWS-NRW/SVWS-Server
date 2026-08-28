package de.svws_nrw.module.reporting.utils;

import java.util.List;

import de.svws_nrw.service.schule.logoverwaltung.DataUrl;
import de.svws_nrw.service.schule.logoverwaltung.DataUrlResolver;

/**
 * Erzeugt aus Base64-Daten die Bildquelle für ein {@code img}-Element einer Report-Vorlage.
 * Den MIME-Type bestimmt der {@link DataUrlResolver} aus den Daten - dieselbe Erkennung, mit der die Logoverwaltung ihre Bilder annimmt und ausliefert.
 * Diese Klasse enthält ausschließlich statische Methoden und kann nicht instanziiert werden.
 */
public final class ReportingBildquelle {

	/**
	 * Die Formate, die eine Ausgabe darstellen kann. Der Resolver löst auch Daten auf, die kein Bild sind, und liefert dafür etwa {@code text/plain};
	 * ohne diese Prüfung entstünde daraus eine Bildquelle, an der eine Vorlage ein defektes Bild statt ihrer Ersatzdarstellung zeigte.
	 */
	private static final List<String> BILDFORMATE = List.of("image/png", "image/jpeg", "image/gif", "image/svg+xml", "image/tiff");


	private ReportingBildquelle() {
		throw new IllegalStateException("Statische Klasse mit Hilfsmethoden zur Bildausgabe. Initialisierung nicht möglich.");
	}


	/**
	 * Erzeugt aus den übergebenen Bilddaten eine Data-URL inklusive MIME-Type.
	 *
	 * @param base64 Die Bilddaten im Base64-Format, ohne den Kopf einer Data-URL.
	 *
	 * @return Die Data-URL oder ein leerer String, wenn keine Daten vorliegen oder sie kein darstellbares Bild ergeben.
	 */
	public static String ausBase64(final String base64) {
		if ((base64 == null) || base64.isBlank()) {
			return "";
		}
		return DataUrlResolver.resolve(base64).filter(dataUrl -> dataUrl.hasAnyMimeTypeOf(BILDFORMATE)).map(DataUrl::value).orElse("");
	}

}
