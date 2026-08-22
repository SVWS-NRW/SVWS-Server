package de.svws_nrw.module.reporting.signing;

/**
 * Die fertig gerenderten QR-Codes einer Schulbescheinigung für die Übergabe ins Template. Die SVGs werden im Batch des Repositories erzeugt; das Template
 * bettet sie nur noch ein und wählt anhand des Zustands zwischen den QR-Codes und den zwei identischen Fehlerbildern samt festem Text.
 * <p>Invarianten: Im Zustand {@link SchulbescheinigungSignaturzustand#SIGNIERT} sind beide SVGs gesetzt, in jedem anderen Zustand sind beide {@code null}.
 * Eine technische Fehlermeldung führt dieser Typ bewusst nicht - so kann kein Diensttext auf der Bescheinigung erscheinen.</p>
 *
 * @param qr1Svg  Fertige SVG-Data-URI des Inhalt-QR-Codes (XSchule-XML) oder {@code null}.
 * @param qr2Svg  Fertige SVG-Data-URI des Signatur-QR-Codes oder {@code null}.
 * @param zustand Der Zustand der QR-Codes; nie {@code null}.
 */
public record SchulbescheinigungQrDaten(String qr1Svg, String qr2Svg, SchulbescheinigungSignaturzustand zustand) {

	/**
	 * Gibt an, ob die Bescheinigung digital signiert ist und beide QR-Codes trägt.
	 *
	 * @return true im Zustand SIGNIERT, sonst false.
	 */
	public boolean istSigniert() {
		return zustand == SchulbescheinigungSignaturzustand.SIGNIERT;
	}

	/**
	 * Gibt an, ob die QR-Codes wegen fehlender oder fehlerhafter Daten des Schülers fehlen.
	 *
	 * @return true im Zustand DATENFEHLER, sonst false.
	 */
	public boolean hatDatenfehler() {
		return zustand == SchulbescheinigungSignaturzustand.DATENFEHLER;
	}

}
