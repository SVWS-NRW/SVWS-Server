package de.svws_nrw.module.reporting.signing;

/**
 * Die fertig gerenderten QR-Codes einer Schulbescheinigung für die Übergabe ins Template. Die SVGs werden im
 * Bulk-Loader des Repositories erzeugt; das Template bettet sie nur noch ein.
 *
 * @param qr1Svg        Fertige SVG-Data-URI des Inhalt-QR-Codes (XSchule-XML); immer gesetzt.
 * @param qr2Svg        Fertige SVG-Data-URI des Signatur-QR-Codes; {@code null}, wenn die Signierung fehlschlug.
 * @param fehlermeldung Beschreibung des Fehlers; {@code null} bei Erfolg.
 */
public record SchulbescheinigungQrDaten(String qr1Svg, String qr2Svg, String fehlermeldung) {
	// Reines Datenobjekt ohne weitere Logik.
}
