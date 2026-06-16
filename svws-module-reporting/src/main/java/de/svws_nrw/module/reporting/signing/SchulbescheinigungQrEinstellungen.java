package de.svws_nrw.module.reporting.signing;

import de.vwsoft.barcodelib4j.twod.QRCodeErrorCorrection;

/**
 * Zentrale Konstanten für die beiden QR-Codes der Schulbescheinigung (Format-Präfixe, Maße, Fehlerkorrektur-Level).
 * Die Fehlerkorrektur-Level sind hier an <b>genau einer Stelle</b> definiert, damit Kapazitätsprüfung und Rendern
 * niemals divergieren können. Die endgültige Wahl der Level ist eine Ein-Zeilen-Änderung an dieser Stelle.
 */
public final class SchulbescheinigungQrEinstellungen {

	/** Format-Präfix des Inhalt-QR-Codes (QR1) – vom externen Prüfer vorgegeben. */
	public static final String PRAEFIX_QR1 = "DATAV1:";

	/** Format-Präfix des Signatur-QR-Codes (QR2) – vom externen Prüfer vorgegeben. */
	public static final String PRAEFIX_QR2 = "SIGNV1:";

	/**
	 * Breite des QR-Codes in Millimetern. Dieser Wert wird ausschließlich für die SVG-Generierung (Canvas-Größe)
	 * genutzt. Die Darstellungsgröße im Druck steuert das Template über die Vorlage-Parameter
	 * {@code qrCodeBreite}/{@code qrCodeHoehe}.
	 */
	public static final double QR_BREITE_MM = 40.0;

	/**
	 * Höhe des QR-Codes in Millimetern. Dieser Wert wird ausschließlich für die SVG-Generierung (Canvas-Größe)
	 * genutzt. Die Darstellungsgröße im Druck steuert das Template über die Vorlage-Parameter
	 * {@code qrCodeBreite}/{@code qrCodeHoehe}.
	 */
	public static final double QR_HOEHE_MM = 40.0;

	/** Fehlerkorrektur-Level des Inhalt-QR-Codes (QR1). */
	public static final QRCodeErrorCorrection EC_QR1 = QRCodeErrorCorrection.M;

	/** Fehlerkorrektur-Level des Signatur-QR-Codes (QR2). */
	public static final QRCodeErrorCorrection EC_QR2 = QRCodeErrorCorrection.M;

	private SchulbescheinigungQrEinstellungen() {
		throw new IllegalStateException("Konstantenklasse. Initialisierung nicht möglich.");
	}

}
