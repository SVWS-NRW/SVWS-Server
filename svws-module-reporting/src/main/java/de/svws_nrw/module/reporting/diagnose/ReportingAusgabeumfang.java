package de.svws_nrw.module.reporting.diagnose;

import java.util.Objects;

/**
 * Der Ausgabeumfang eines Reports: wie viele Einheiten die Anfrage bestellt hat und wie viele davon in der Ausgabe landen. Die Einheit ist je Datenaufbau
 * festgelegt - etwa die angeforderten IDs einer Liste oder die Schüler eines Klausurplans. Aus den beiden Zahlen entstehen die Felder {@code angefordert}
 * und {@code ausgegeben} des Hinweis-Headers.
 * <p>Das Kennzeichen der zulässig leeren Ausgabe ist eine Absichtserklärung der Meldestelle und keine Ableitung aus den Zählwerten: Nur mit ihm
 * unterscheidet die Ausgabefactory eine gewollt leere Ausgabe von einem Rendering, das still nichts erzeugt hat.</p>
 *
 * @param angefordert          Die Anzahl der bestellten Einheiten - nur bekannte Einheiten auflösbarer Hauptobjekte zählen.
 * @param ausgegeben           Die Anzahl der Einheiten, die in der Ausgabe landen.
 * @param leereAusgabeZulaessig true, wenn eine Ausgabe ohne Dokument zulässig ist - weil kein bestellter Datensatz übrig blieb oder die Anfrage keine
 *                             zählbaren Einheiten enthält.
 */
public record ReportingAusgabeumfang(int angefordert, int ausgegeben, boolean leereAusgabeZulaessig) {

	/**
	 * Bildet den Ausgabeumfang aus dem Ergebnis einer ID-Auswahl: angefordert sind die bereinigten IDs der Anfrage, ausgegeben die ausgewählten Objekte.
	 * Die zulässig leere Ausgabe folgt der Auswahl - Datensätze waren angefordert und keiner blieb übrig.
	 *
	 * @param auswahl Das Ergebnis der Auswahl; {@code null} ist unzulässig.
	 *
	 * @return Der Ausgabeumfang der Auswahl.
	 */
	public static ReportingAusgabeumfang ausAuswahl(final ReportingAuswahlergebnis<?> auswahl) {
		Objects.requireNonNull(auswahl, "Ohne Auswahlergebnis gibt es keinen Ausgabeumfang zu melden.");
		return new ReportingAusgabeumfang(auswahl.idsAngefordert().size(), auswahl.objekte().size(), auswahl.bewusstLeer());
	}

}
