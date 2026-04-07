package de.svws_nrw.module.reporting.utils;

import de.svws_nrw.db.utils.ApiOperationException;
import de.vwsoft.barcodelib4j.image.BarExporter;
import de.vwsoft.barcodelib4j.image.CompoundColor;
import de.vwsoft.barcodelib4j.oned.Barcode;
import de.vwsoft.barcodelib4j.oned.BarcodeException;
import de.vwsoft.barcodelib4j.oned.BarcodeType;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


/**
 * Statische Klasse mit Hilfsmethoden zum Erstellen von Barcodes und QR-Codes.
 */
public final class ReportingBarcodeUtils {

	private ReportingBarcodeUtils() {
		throw new IllegalStateException("Statische Klasse mit Hilfsmethoden zum Fehler-Logging. Initialisierung nicht möglich.");
	}

	/**
	 * Erstellt aus einem übergebenen String einen Code128-Barcode im SVG-Format.
	 *
	 * @param barcodeInhalt Der Inhalt, der als Barcode dargestellt werden soll. Maximal 64 Zeichen werden unterstützt.
	 * @param breiteInMM    Die Breite des Barcodes in Millimetern. Wenn negativ oder 0, wird die Standardbreite von 50mm verwendet.
	 * @param hoeheInMM     Die Höhe des Barcodes in Millimetern. Wenn negativ oder 0, wird die Standardhöhe von 30mm verwendet.
	 *
	 * @return Der Barcode als Base64-codierter SVG-String zur direkten Einbettung in HTML (Data-URI) oder null, falls ein Fehler auftritt.
	 *         Eine Einbettung könnte dabei mittels Thymeleaf erfolgen: {@code "<img th:src="${StringAusDieserMethode}" alt="Barcode" />}
	 */
	public static @NotNull String erzeugeBarcodeCode128(final String barcodeInhalt, final double breiteInMM, final double hoeheInMM) {
		// Dimensionen des Barcodes in mm festlegen.
		final double breiteMM = (breiteInMM <= 0) ? 50.0 : breiteInMM;
		final double hoeheMM = (hoeheInMM <= 0) ? 30.0 : hoeheInMM;

		// Inhalt des Barcodes normalisieren. Dabei wird die Länge des normalisierten Barcodes auf max. 64 Zeichen begrenzt, um noch lesbare Barcodes zu erzeugen.
		String barcodeInhaltNormalisiert = ((barcodeInhalt == null) || barcodeInhalt.trim().isBlank()) ? "" : barcodeInhalt.trim();
		if (barcodeInhaltNormalisiert.length() > 64) {
			barcodeInhaltNormalisiert = barcodeInhaltNormalisiert.substring(0, 64);
		}

		// Wenn Inhalt des Barcodes leer ist, dann leeres SVG zurückgeben.
		if (barcodeInhaltNormalisiert.isBlank()) {
			return leeresTransparentesSVG(breiteMM, hoeheMM);
		}

		// Erzeuge nun den Barcode.
		try {
			// Instance des Barcodes erzeugen.
			final Barcode barcode = Barcode.newInstance(BarcodeType.CODE128);
			barcode.setContent(barcodeInhaltNormalisiert, false, false);
			barcode.setFont(new Font("OCR-B", Font.PLAIN, 1));
			barcode.setFontSizeAdjusted(true);
			barcode.setTextOffset(-0.3);

			// Ein Barcode-Exporter für eine SVG-Grafik in Schwarz und Weiß erzeugen.
			final BarExporter exporter = new BarExporter(breiteMM, hoeheMM);
			exporter.setTitle(barcode.getText());
			exporter.setForeground(new CompoundColor(Color.BLACK));
			exporter.setBackground(new CompoundColor(Color.WHITE));

			// Erzeuge eine 'Graphics2D' des Barcodes auf Basis des Exporters.
			final Graphics2D g2d = exporter.getGraphics2D();
			barcode.draw(g2d, 0.0, 0.0, breiteMM, hoeheMM);
			g2d.dispose();

			// Schreibe die Daten des Exportes in einen ByteArrayOutputStream und konvertiere diesen in einen SVG-base64-String.
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				exporter.writeSVG(baos);
				final String svgString = baos.toString(StandardCharsets.UTF_8);

				// Base64-Encoding für die direkte Nutzung im <img>-Tag.
				final String base64Svg = Base64.getEncoder().encodeToString(svgString.getBytes(StandardCharsets.UTF_8));
				return "data:image/svg+xml;base64," + base64Svg;
			}
		} catch (final BarcodeException | IOException ex) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, ex,
					"Fehler: Beim Erzeugen des Barcodes aus dem String '" + barcodeInhaltNormalisiert + "' ist folgender Fehler aufgetreten: " + ex.getMessage());
		}
	}

	/**
	 * Erzeugt ein leeres, transparentes SVG mit den angegebenen Maßen und gibt diesen als Base64-String zurück.
	 *
	 * @param breiteMM  Die Breite des SVG in Millimetern.
	 * @param hoeheMM   Die Höhe des SVG in Millimetern.
	 *
	 * @return Der Base64-String des leeren, transparenten SVGs.
	 */
	private static @NotNull String leeresTransparentesSVG(final double breiteMM, final double hoeheMM) {
		final String leeresSvg = "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"%fmm\" height=\"%fmm\" viewBox=\"0 0 %f %f\"></svg>"
				.formatted(breiteMM, hoeheMM, breiteMM, hoeheMM);
		return "data:image/svg+xml;base64," + Base64.getEncoder().encodeToString(leeresSvg.getBytes(StandardCharsets.UTF_8));
	}
}
