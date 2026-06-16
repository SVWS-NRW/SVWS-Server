package de.svws_nrw.module.reporting.signing;

import java.util.UUID;

/**
 * Das Ergebnis einer einzelnen Signierung.
 *
 * @param correlationId Anonyme Korrelations-ID, identisch zur zugehörigen {@link SignierAnfrage}.
 * @param cms           Die rohe CMS-/P7S-Signatur als Bytes; {@code null}, wenn die Signierung fehlgeschlagen ist.
 * @param fehler        Fehlermeldung, falls die Signierung fehlschlug; {@code null} bei Erfolg.
 */
// Ergebnisse werden ausschließlich über die correlationId zugeordnet, nie per Wertgleichheit auf dem Byte-Array verglichen.
@SuppressWarnings("java:S6218")
public record SignierErgebnis(UUID correlationId, byte[] cms, String fehler) {

	/**
	 * Gibt an, ob die Signierung erfolgreich war.
	 *
	 * @return {@code true}, wenn keine Fehlermeldung vorliegt und CMS-Bytes vorhanden sind.
	 */
	public boolean istErfolgreich() {
		return (fehler == null) && (cms != null);
	}

}
