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

	/** Die Breite in Millimetern, die für Barcodes und QR-Codes ohne eigene Angabe verwendet wird. */
	public static final double STANDARD_BREITE_MM = 50.0;

	/** Die Höhe in Millimetern, die für einen Code128-Barcode ohne eigene Angabe verwendet wird. */
	public static final double STANDARD_HOEHE_BARCODE_MM = 30.0;

	/** Die Höhe in Millimetern, die für einen QR-Code ohne eigene Angabe verwendet wird - er ist quadratisch. */
	public static final double STANDARD_HOEHE_QRCODE_MM = STANDARD_BREITE_MM;

	private ReportingBarcodeUtils() {
		throw new IllegalStateException("Statische Klasse mit Hilfsmethoden zum Fehler-Logging. Initialisierung nicht möglich.");
	}

	/**
	 * Erstellt aus einem übergebenen String einen Code128-Barcode im SVG-Format.
	 *
	 * @param barcodeInhalt Der Inhalt, der als Barcode dargestellt werden soll. Maximal 64 Zeichen werden unterstützt; ein längerer Inhalt wirft.
	 * @param breiteInMM    Die Breite des Barcodes in Millimetern. Wenn negativ oder 0, wird die Standardbreite von 50mm verwendet.
	 * @param hoeheInMM     Die Höhe des Barcodes in Millimetern. Wenn negativ oder 0, wird die Standardhöhe von 30mm verwendet.
	 *
	 * @return Der Barcode als Base64-codierter SVG-String zur direkten Einbettung in HTML (Data-URI); bei leerem Inhalt ein leeres, transparentes SVG.
	 *         Eine Einbettung könnte dabei mittels Thymeleaf erfolgen: {@code "<img th:src="${StringAusDieserMethode}" alt="Barcode" />}
	 *
	 * @throws ApiOperationException Mit Status 500, wenn der Inhalt länger als 64 Zeichen ist oder sich nicht als Barcode kodieren lässt.
	 */
	public static @NotNull String erzeugeBarcodeCode128(final String barcodeInhalt, final double breiteInMM, final double hoeheInMM) {
		// Dimensionen des Barcodes in mm festlegen.
		final double breiteMM = (breiteInMM <= 0) ? STANDARD_BREITE_MM : breiteInMM;
		final double hoeheMM = (hoeheInMM <= 0) ? STANDARD_HOEHE_BARCODE_MM : hoeheInMM;

		// Inhalt des Barcodes normalisieren. Bei mehr als 64 Zeichen wäre der Barcode nicht mehr zuverlässig lesbar - ein stilles Abschneiden erzeugte
		// dann einen Code mit anderem Inhalt als den Daten. Deshalb wird geworfen; der Aufrufer im Dialekt meldet daraus eine Lücke.
		final String barcodeInhaltNormalisiert = ((barcodeInhalt == null) || barcodeInhalt.trim().isBlank()) ? "" : barcodeInhalt.trim();
		if (barcodeInhaltNormalisiert.length() > 64) {
			// Der Inhalt stammt aus den Reportdaten und nicht aus dem Request: Ein nicht darstellbarer Wert ist ein serverseitiges Problem.
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR,
					"### FEHLER: Der Inhalt ist mit %d Zeichen zu lang für einen lesbaren Barcode; erlaubt sind höchstens 64 Zeichen."
							.formatted(barcodeInhaltNormalisiert.length()));
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
					"### FEHLER: Der Barcode konnte nicht erzeugt werden.");
		}
	}

	/**
	 * Erstellt aus einem übergebenen String einen QR-Code (Fehlerkorrektur-Level M) im SVG-Format.
	 *
	 * @param qrInhalt   Der Inhalt, der als QR-Code dargestellt werden soll.
	 * @param breiteInMM Die Breite des QR-Codes in Millimetern. Wenn negativ oder 0, wird die Standardbreite von 50mm verwendet.
	 * @param hoeheInMM  Die Höhe des QR-Codes in Millimetern. Wenn negativ oder 0, wird die Standardhöhe von 50mm verwendet.
	 *
	 * @return Der QR-Code als Base64-codierter SVG-String zur direkten Einbettung in HTML (Data-URI).
	 *         Eine Einbettung könnte dabei mittels Thymeleaf erfolgen: {@code "<img th:src="${StringAusDieserMethode}" alt="QR-Code" />}
	 */
	public static @NotNull String erzeuge2DCodeQRCode(final String qrInhalt, final double breiteInMM, final double hoeheInMM) {
		return erzeuge2DCodeQRCode(qrInhalt, breiteInMM, hoeheInMM, QRCodeErrorCorrection.M);
	}

	/**
	 * Erstellt aus einem übergebenen String einen QR-Code im SVG-Format mit wählbarem Fehlerkorrektur-Level.
	 *
	 * <p>Die Version wird automatisch bis maximal Version 40 skaliert. Bei rein alphanumerischem Inhalt (z. B.
	 * Base45-kodierte Daten) wählt die Bibliothek selbsttätig den kapazitätsstärksten Alphanumerik-Modus; ein
	 * fester Zeichensatz wird daher bewusst nicht gesetzt. Passt der Inhalt nicht in Version 40, wird eine
	 * {@link ApiOperationException} geworfen (kein stilles Abschneiden).</p>
	 *
	 * @param qrInhalt   Der Inhalt, der als QR-Code dargestellt werden soll.
	 * @param breiteInMM Die Breite des QR-Codes in Millimetern. Wenn negativ oder 0, wird die Standardbreite von 50mm verwendet.
	 * @param hoeheInMM  Die Höhe des QR-Codes in Millimetern. Wenn negativ oder 0, wird die Standardhöhe von 50mm verwendet.
	 * @param ecLevel    Das Fehlerkorrektur-Level (L = höchste Kapazität, H = höchste Redundanz).
	 *
	 * @return Der QR-Code als Base64-codierter SVG-String zur direkten Einbettung in HTML (Data-URI).
	 */
	public static @NotNull String erzeuge2DCodeQRCode(final String qrInhalt, final double breiteInMM, final double hoeheInMM,
			final QRCodeErrorCorrection ecLevel) {
		// Dimensionen des QR-Codes in mm festlegen.
		final double breiteMM = (breiteInMM <= 0) ? STANDARD_BREITE_MM : breiteInMM;
		final double hoeheMM = (hoeheInMM <= 0) ? STANDARD_HOEHE_QRCODE_MM : hoeheInMM;

		// Inhalt des QR-Codes normalisieren (kein Längen-Cap: ein Abschneiden würde z. B. eine Signatur zerstören).
		final String qrInhaltNormalisiert = ((qrInhalt == null) || qrInhalt.trim().isBlank()) ? "" : qrInhalt.trim();

		// Wenn Inhalt des QR-Codes leer ist, dann leeres SVG zurückgeben.
		if (qrInhaltNormalisiert.isBlank()) {
			return leeresTransparentesSVG(breiteMM, hoeheMM);
		}

		// Erzeuge nun den QR-Code.
		try {
			// Instance des TwoDCode erzeugen.
			final TwoDCode tdc = new TwoDCode(TwoDType.QRCODE);
			tdc.setContent(qrInhaltNormalisiert);
			tdc.setCharset(null); // kein CHARACTER_SET-Hint -> Bibliothek wählt automatisch den optimalen (alphanumerischen) Modus
			tdc.setQRCodeVersion(QRCodeVersion.AUTO);
			tdc.setQRCodeErrCorr(ecLevel);

			// canEncode() prüft nur die Zeichen-Gültigkeit, nicht die QR-Kapazität; die Kapazitätsgrenze (V40) wirft buildSymbol().
			final TwoDSymbol symbol;
			if (tdc.canEncode()) {
				symbol = tdc.buildSymbol();
			} else {
				// Der Inhalt stammt aus den Reportdaten und nicht aus dem Request: Ein nicht darstellbarer Wert ist ein serverseitiges Problem.
				throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR,
						"### FEHLER: Der Inhalt lässt sich nicht als QR-Code darstellen.");
			}

			// Ein Exporter für eine SVG-Grafik in Schwarz und Weiß erzeugen.
			final BarExporter exporter = new BarExporter(breiteMM, hoeheMM);
			exporter.setTitle("QR Code");

			return exportToSVGBase64(exporter, g2d -> symbol.draw(g2d, 0.0, 0.0, breiteMM, hoeheMM));
		} catch (final ApiOperationException ex) {
			// Die eigene Meldung oben ist bereits klassifiziert; der allgemeine Catch verpackte sie sonst ein zweites Mal.
			throw ex;
		} catch (final Exception ex) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, ex,
					"### FEHLER: Der QR-Code konnte nicht erzeugt werden.");
		}
	}

	/**
	 * Erzeugt ein leeres, transparentes SVG mit den angegebenen Maßen und gibt diesen als Base64-String zurück.
	 * <p>Das leere SVG ist die Darstellung einer Lücke: Es hält den Platz im Layout frei, ohne einen Inhalt vorzutäuschen. Die Erzeugungsmethoden
	 * verwenden es bei leerem Inhalt; Aufrufer, die einen Fehler nicht bis in die Ausgabe durchschlagen lassen dürfen, verwenden es als Ersatz.</p>
	 *
	 * @param breiteMM  Die Breite des SVG in Millimetern.
	 * @param hoeheMM   Die Höhe des SVG in Millimetern.
	 *
	 * @return Der Base64-String des leeren, transparenten SVGs.
	 */
	public static @NotNull String leeresTransparentesSVG(final double breiteMM, final double hoeheMM) {
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
