package de.svws_nrw.module.reporting.html.dialects;

import de.svws_nrw.base.compression.GZip;
import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.core.utils.encoding.Base32;
import de.svws_nrw.core.utils.encoding.Base45;
import de.svws_nrw.module.reporting.utils.ReportingBarcodeUtils;
import jakarta.validation.constraints.NotNull;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;




/**
 * Die Klasse stellt Hilfsmethoden zur Datumsverarbeitung zur Verfügung, die über einen Thymeleaf-Dialect
 * und dessen ExpressionFactory in HTML-Templates verwendet werden können.
 */
// toDateObject(...) ist ein bewusster Adapter an die Thymeleaf-Template-API, die für ihre Datums-Hilfsmethoden ein Date erwartet.
public class ConvertExpressionHelper {

	/**
	 * Erstellt einen neuen ConvertExpressionHelper
	 */
	public ConvertExpressionHelper() {
		// Standardkonstruktor
	}

	/**
	 * Gibt ein als String im ISO-Format vorliegendes Datum als Objekt vom Typ Date zurück.
	 *
	 * @param dateISO8601		Der String, der das im ISO-Format yyyy-MM-dd vorliegende Datum enthält.
	 *
	 * @return					Das übergebene Datum als Date-Objekt oder {@code null}, falls kein Datum übergeben wurde.
	 */
	public Date toDateObject(final String dateISO8601) {
		if ((dateISO8601 == null) || dateISO8601.isBlank()) {
			return null;
		}
		final @NotNull int[] info = DateUtils.extractFromDateISO8601(dateISO8601);
		return Date.from(LocalDate.of(info[0], info[1], info[2]).atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * Gibt ein als String im ISO-Format vorliegendes Datum als String im deutschen Datumsformat zurück.
	 * @param dateISO8601		Der String, der das im ISO-Format yyyy-MM-dd vorliegende Datum enthält.
	 *
	 * @return					Das übergebene Datum im deutschen Format.
	 */
	public String toDateDE(final String dateISO8601) {
		if ((dateISO8601 == null) || dateISO8601.isEmpty()) {
			return "";
		}
		try {
			return DateUtils.gibDatumGermanFormat(dateISO8601);
		} catch (@SuppressWarnings("unused") final Exception ignore) {
			return "";
		}
	}

	/**
	 * Gibt ein als String im ISO-Format vorliegendes Datum als String im deutschen Datumsformat mit ausgeschriebenem Monat zurück.
	 *
	 * @param dateISO8601		Der String, der das im ISO-Format yyyy-MM-dd vorliegende Datum enthält.
	 *
	 * @return					Das übergebene Datum im deutschen Format mit ausgeschriebenem Monat.
	 */
	public String toDateDELong(final String dateISO8601) {
		if ((dateISO8601 == null) || dateISO8601.isEmpty()) {
			return "";
		}
		try {
			return DateUtils.gibDatumGermanFormatAusgeschrieben(dateISO8601);
		} catch (@SuppressWarnings("unused") final Exception ignore) {
			return "";
		}
	}

	/**
	 * Gibt zu einem als String im ISO-Format vorliegenden Datum den deutschen Wochentag zurück.
	 *
	 * @param dateISO8601		Der String, der das im ISO-Format yyyy-MM-dd vorliegende Datum enthält.
	 *
	 * @return					Der deutsche Wochentag des Datums.
	 */
	public String toWochentagDE(final String dateISO8601) {
		if ((dateISO8601 == null) || dateISO8601.isEmpty()) {
			return "";
		}
		try {
			final String[] wochentage = new String[] { "", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag" };
			return wochentage[DateUtils.gibWochentagDesDatumsISO8601(dateISO8601)];
		} catch (@SuppressWarnings("unused") final Exception ignore) {
			return "";
		}
	}

	/**
	 * Gibt zu einem als String im ISO-Format vorliegenden Datum den deutschen Wochentag als Abkürzung zurück.
	 *
	 * @param dateISO8601		Der String, der das im ISO-Format yyyy-MM-dd vorliegende Datum enthält.
	 *
	 * @return					Der abgekürzte deutsche Wochentag des Datums.
	 */
	public String toWochentagKurzDE(final String dateISO8601) {
		if ((dateISO8601 == null) || dateISO8601.isEmpty()) {
			return "";
		}
		try {
			final String[] wochentage = new String[] { "", "Mo.", "Di.", "Mi.", "Do.", "Fr.", "Sa.", "So." };
			return wochentage[DateUtils.gibWochentagDesDatumsISO8601(dateISO8601)];
		} catch (@SuppressWarnings("unused") final Exception ignore) {
			return "";
		}
	}

	/**
	 * Gibt zu einem als String im ISO-Format vorliegenden Datum die Kalenderwoche in Deutschland zurück.
	 *
	 * @param dateISO8601		Der String, der das im ISO-Format yyyy-MM-dd vorliegende Datum enthält.
	 *
	 * @return					Die Kalenderwoche des Datums in Deutschland.
	 */
	public String toKalenderwocheDE(final String dateISO8601) {
		if ((dateISO8601 == null) || dateISO8601.isEmpty()) {
			return "";
		}
		try {
			return String.valueOf(DateUtils.extractFromDateISO8601(dateISO8601)[5]);
		} catch (@SuppressWarnings("unused") final Exception ignore) {
			return "";
		}
	}

	/**
	 * Gibt ein SVG für eine Checkbox zurück. Ist der Parameter true, wird eine angekreuzte Box zurückgegeben, andernfalls (false oder null) eine leere Box.
	 * Die Checkbox wird in der Standardgröße 14x14 Pixel gerendert.
	 *
	 * @param checked Gibt an, ob die Checkbox angekreuzt sein soll.
	 *
	 * @return Das SVG als HTML-String.
	 */
	public String toCheckboxSVG(final Boolean checked) {
		return toCheckboxSVG(checked, 14);
	}

	/**
	 * Gibt ein SVG für eine Checkbox zurück. Ist der Parameter true, wird eine angekreuzte Box zurückgegeben, andernfalls (false oder null) eine leere Box.
	 *
	 * @param checked Gibt an, ob die Checkbox angekreuzt sein soll.
	 * @param size    Die Kantenlänge der Checkbox in Pixeln (Breite und Höhe).
	 *
	 * @return Das SVG als HTML-String.
	 */
	public String toCheckboxSVG(final Boolean checked, final int size) {
		final String iconName = Boolean.TRUE.equals(checked) ? "checkbox_checked" : "checkbox_blank";
		return ReportingIcon.get(iconName, size, "black");
	}

	/**
	 * Gibt zu einem als String vorliegenden Barcode-Inhalt einen Code128-Barcode im SVG-Format zurück,
	 * der als Base64-codierter String zur direkten Einbettung in HTML (Data-URI) verwendet werden kann.
	 *
	 * @param barcodeInhalt		Der Barcode-Inhalt, der kodiert werden soll.
	 * @param breiteInMM		Die gewünschte Breite des Barcodes in Millimetern.
	 * @param hoeheInMM			Die gewünschte Höhe des Barcodes in Millimetern.
	 *
	 * @return					Der Barcode als Base64-codierter SVG-String zur direkten Einbettung in HTML (Data-URI). Bei leerem oder null-Inhalt
	 *                          wird ein leeres, transparentes SVG zurückgegeben (nie null).
	 *                          Eine Einbettung könnte dabei mittels Thymeleaf erfolgen:
	 *                          {@code <img th:src="${#convert.toBarcodeCode128AsSvgHtmlImageSource(schueler.id(), 50, 30)}"
	 *                                      th:alt="${'Barcode zur ID ' + schueler.id()}" />}
	 */
	public String toBarcodeCode128AsSvgHtmlImageSource(final String barcodeInhalt, final double breiteInMM, final double hoeheInMM) {
		return ReportingBarcodeUtils.erzeugeBarcodeCode128(barcodeInhalt, breiteInMM, hoeheInMM);
	}

	/**
	 * Gibt zu einem als String vorliegenden QR-Code-Inhalt einen QR-Code im SVG-Format zurück,
	 * der als Base64-codierter String zur direkten Einbettung in HTML (Data-URI) verwendet werden kann.
	 *
	 * @param qrInhalt		Der QR-Code-Inhalt, der kodiert werden soll.
	 * @param breiteInMM	Die gewünschte Breite des QR-Codes in Millimetern.
	 * @param hoeheInMM		Die gewünschte Höhe des QR-Codes in Millimetern.
	 *
	 * @return				Der QR-Code als Base64-codierter SVG-String zur direkten Einbettung in HTML (Data-URI). Bei leerem oder null-Inhalt
	 *                      wird ein leeres, transparentes SVG zurückgegeben (nie null).
	 *                      Eine Einbettung könnte dabei mittels Thymeleaf erfolgen:
	 *                      {@code <img th:src="${#convert.to2DCodeQRCodeAsSvgHtmlImageSource('Hello World', 50, 50)}"
	 *                                  th:alt="'QR-Code'" />}
	 */
	public String to2DCodeQRCodeAsSvgHtmlImageSource(final String qrInhalt, final double breiteInMM, final double hoeheInMM) {
		return ReportingBarcodeUtils.erzeuge2DCodeQRCode(qrInhalt, breiteInMM, hoeheInMM);
	}

	/**
	 * Komprimiert den übergebenen String mit GZip und gibt ihn als Base64-kodierten String zurück.
	 *
	 * @param input   der zu komprimierende String
	 *
	 * @return        der komprimierte String als Base64 oder leerer String bei Fehler oder null/empty input
	 */
	public String compressGZipString(final String input) {
		if ((input == null) || input.isEmpty()) {
			return "";
		}
		try {
			return GZip.encodeBase64(input.getBytes(StandardCharsets.UTF_8));
		} catch (@SuppressWarnings("unused") final Exception ignore) {
			return "";
		}
	}

	/**
	 * Dekomprimiert den übergebenen Base64-kodierten, GZip-komprimierten String.
	 *
	 * @param input   der zu dekomprimierende Base64-String
	 *
	 * @return        der dekomprimierte String oder leerer String bei Fehler oder null/empty input
	 */
	public String decompressGZipString(final String input) {
		if ((input == null) || input.isEmpty()) {
			return "";
		}
		try {
			final byte[] decompressed = GZip.decodeBase64(input);
			return new String(decompressed, StandardCharsets.UTF_8);
		} catch (@SuppressWarnings("unused") final Exception ignore) {
			return "";
		}
	}

	/**
	 * Kodiert den übergebenen String mit Base45 und gibt ihn als String zurück.
	 *
	 * @param input   der zu kodierende String
	 *
	 * @return        der kodierte String oder leerer String bei Fehler oder null/empty input
	 */
	public String encodeBase45(final String input) {
		if ((input == null) || input.isEmpty()) {
			return "";
		}
		try {
			return Base45.encode(input.getBytes(StandardCharsets.UTF_8));
		} catch (@SuppressWarnings("unused") final Exception ignore) {
			return "";
		}
	}

	/**
	 * Dekodiert den übergebenen Base45-kodierten String und gibt ihn als String zurück.
	 *
	 * @param input   der zu dekodierende Base45-String
	 *
	 * @return        der dekodierte String oder leerer String bei Fehler oder null/empty input
	 */
	public String decodeBase45(final String input) {
		if ((input == null) || input.isEmpty()) {
			return "";
		}
		try {
			final byte[] decoded = Base45.decode(input);
			return new String(decoded, StandardCharsets.UTF_8);
		} catch (@SuppressWarnings("unused") final Exception ignore) {
			return "";
		}
	}

	/**
	 * Kodiert den übergebenen String mit Base64 und gibt ihn als String zurück.
	 *
	 * @param input   der zu kodierende String
	 *
	 * @return        der kodierte String oder leerer String bei Fehler oder null/empty input
	 */
	public String encodeBase64(final String input) {
		if ((input == null) || input.isEmpty()) {
			return "";
		}
		try {
			return Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
		} catch (@SuppressWarnings("unused") final Exception ignore) {
			return "";
		}
	}

	/**
	 * Dekodiert den übergebenen Base64-kodierten String und gibt ihn als String zurück.
	 *
	 * @param input   der zu dekodierende Base64-String
	 *
	 * @return        der dekodierte String oder leerer String bei Fehler oder null/empty input
	 */
	public String decodeBase64(final String input) {
		if ((input == null) || input.isEmpty()) {
			return "";
		}
		try {
			final byte[] decoded = Base64.getDecoder().decode(input);
			return new String(decoded, StandardCharsets.UTF_8);
		} catch (@SuppressWarnings("unused") final Exception ignore) {
			return "";
		}
	}

	/**
	 * Kodiert den übergebenen String mit Base32 und gibt ihn als String zurück.
	 *
	 * @param input   der zu kodierende String
	 *
	 * @return        der kodierte String oder leerer String bei Fehler oder null/empty input
	 */
	public String encodeBase32(final String input) {
		if ((input == null) || input.isEmpty()) {
			return "";
		}
		try {
			return Base32.encode(input.getBytes(StandardCharsets.UTF_8));
		} catch (@SuppressWarnings("unused") final Exception ignore) {
			return "";
		}
	}

	/**
	 * Dekodiert den übergebenen Base32-kodierten String und gibt ihn als String zurück.
	 *
	 * @param input   der zu dekodierende Base32-String
	 *
	 * @return        der dekodierte String oder leerer String bei Fehler oder null/empty input
	 */
	public String decodeBase32(final String input) {
		if ((input == null) || input.isEmpty()) {
			return "";
		}
		try {
			final byte[] decoded = Base32.decode(input);
			return new String(decoded, StandardCharsets.UTF_8);
		} catch (@SuppressWarnings("unused") final Exception ignore) {
			return "";
		}
	}
}
