package de.svws_nrw.module.reporting.utils;

import de.svws_nrw.db.utils.ApiOperationException;
import de.vwsoft.barcodelib4j.image.BarExporter;
import de.vwsoft.barcodelib4j.image.CompoundColor;
import de.vwsoft.barcodelib4j.oned.Barcode;
import de.vwsoft.barcodelib4j.oned.BarcodeException;
import de.vwsoft.barcodelib4j.oned.BarcodeType;
import de.vwsoft.barcodelib4j.twod.TwoDCode;
import de.vwsoft.barcodelib4j.twod.TwoDType;
import de.vwsoft.barcodelib4j.twod.QRCodeVersion;
import de.vwsoft.barcodelib4j.twod.QRCodeErrorCorrection;
import de.vwsoft.barcodelib4j.twod.TwoDSymbol;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.function.Consumer;


/**
 * Statische Klasse mit Hilfsmethoden zum Erstellen von Barcodes und QR-Codes.
 */
public final class ReportingBarcodeUtils {

	/**
	 * Präfix für SVG-Data-URIs im Base64-Format zur direkten Einbettung in HTML.
	 */
	private static final String SVG_DATA_URI_PREFIX = "data:image/svg+xml;base64,";

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

			return exportToSVGBase64(exporter, g2d -> barcode.draw(g2d, 0.0, 0.0, breiteMM, hoeheMM));
		} catch (final BarcodeException | IOException ex) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, ex,
					"Fehler: Beim Erzeugen des Barcodes aus dem String '" + barcodeInhaltNormalisiert + "' ist folgender Fehler aufgetreten: "
							+ ex.getMessage());
		}
	}

	/**
	 * Erstellt aus einem übergebenen String einen QR-Code im SVG-Format.
	 *
	 * @param qrInhalt   Der Inhalt, der als QR-Code dargestellt werden soll. Maximal 1000 Zeichen werden unterstützt.
	 * @param breiteInMM Die Breite des QR-Codes in Millimetern. Wenn negativ oder 0, wird die Standardbreite von 50mm verwendet.
	 * @param hoeheInMM  Die Höhe des QR-Codes in Millimetern. Wenn negativ oder 0, wird die Standardhöhe von 50mm verwendet.
	 *
	 * @return Der QR-Code als Base64-codierter SVG-String zur direkten Einbettung in HTML (Data-URI).
	 *         Eine Einbettung könnte dabei mittels Thymeleaf erfolgen: {@code "<img th:src="${StringAusDieserMethode}" alt="QR-Code" />}
	 */
	public static @NotNull String erzeuge2DCodeQRCode(final String qrInhalt, final double breiteInMM, final double hoeheInMM) {
		// Dimensionen des QR-Codes in mm festlegen.
		final double breiteMM = (breiteInMM <= 0) ? 50.0 : breiteInMM;
		final double hoeheMM = (hoeheInMM <= 0) ? 50.0 : hoeheInMM;

		// Inhalt des QR-Codes normalisieren. Dabei wird die Länge des normalisierten Inhalts auf max. 1000 Zeichen begrenzt.
		String qrInhaltNormalisiert = ((qrInhalt == null) || qrInhalt.trim().isBlank()) ? "" : qrInhalt.trim();
		if (qrInhaltNormalisiert.length() > 1000) {
			qrInhaltNormalisiert = qrInhaltNormalisiert.substring(0, 1000);
		}

		// Wenn Inhalt des QR-Codes leer ist, dann leeres SVG zurückgeben.
		if (qrInhaltNormalisiert.isBlank()) {
			return leeresTransparentesSVG(breiteMM, hoeheMM);
		}

		// Erzeuge nun den QR-Code.
		try {
			// Instance des TwoDCode erzeugen.
			final TwoDCode tdc = new TwoDCode(TwoDType.QRCODE);
			tdc.setContent(qrInhaltNormalisiert);
			tdc.setCharset(null); // ISO-8859-1
			tdc.setQRCodeVersion(QRCodeVersion.AUTO);
			tdc.setQRCodeErrCorr(QRCodeErrorCorrection.M);

			// Prüfe, ob der Inhalt enkodiert werden kann, und baue das Symbol.
			final TwoDSymbol symbol;
			if (tdc.canEncode()) {
				symbol = tdc.buildSymbol();
			} else {
				throw new ApiOperationException(Response.Status.BAD_REQUEST,
						"Der Inhalt kann nicht als QR-Code enkodiert werden: '" + qrInhaltNormalisiert + "'");
			}

			// Ein Exporter für eine SVG-Grafik in Schwarz und Weiß erzeugen.
			final BarExporter exporter = new BarExporter(breiteMM, hoeheMM);
			exporter.setTitle("QR Code");

			return exportToSVGBase64(exporter, g2d -> symbol.draw(g2d, 0.0, 0.0, breiteMM, hoeheMM));
		} catch (final Exception ex) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, ex,
					"Fehler: Beim Erzeugen des QR-Codes aus dem String '" + qrInhaltNormalisiert + "' ist folgender Fehler aufgetreten: " + ex.getMessage());
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
		return SVG_DATA_URI_PREFIX + Base64.getEncoder().encodeToString(leeresSvg.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Exportiert einen BarExporter zu einem Base64-kodierten SVG-String.
	 *
	 * @param exporter   Der konfigurierte BarExporter.
	 * @param drawAction Die Aktion zum Zeichnen auf dem Graphics2D-Kontext.
	 *
	 * @return Der Base64-kodierte SVG-String.
	 *
	 * @throws IOException Falls ein Fehler beim Schreiben auftritt.
	 */
	private static @NotNull String exportToSVGBase64(final BarExporter exporter, final Consumer<Graphics2D> drawAction) throws IOException {
		exporter.setForeground(new CompoundColor(Color.BLACK));
		exporter.setBackground(new CompoundColor(Color.WHITE));

		final Graphics2D g2d = exporter.getGraphics2D();
		drawAction.accept(g2d);
		g2d.dispose();

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			exporter.writeSVG(baos);
			final String svgString = baos.toString(StandardCharsets.UTF_8);
			final String base64Svg = Base64.getEncoder().encodeToString(svgString.getBytes(StandardCharsets.UTF_8));
			return SVG_DATA_URI_PREFIX + base64Svg;
		}
	}
}
