package de.svws_nrw.module.reporting.html.contexts.initializer;

/**
 * Die Beschriftungen eines Datenaufbaus für dessen Log-Ausgaben und Fehlermeldungen.
 * <p>Zwei Formen der Bezeichnung sind nötig, weil die Log-Zeilen sie in unterschiedlichen Fällen verwenden — „IDs von Kursen" gegenüber „Datenkontext
 * Kurse". Der ID-Typ ist eigens angegeben, weil er nicht aus der Bezeichnung ableitbar ist („Kurs-IDs" zu „Kurse").</p>
 *
 * @param nominativ Die Bezeichnung im Nominativ Plural, z. B. "Kurse" in "Validiere die Daten für Kurse …" und "Erzeuge Datenkontext Kurse …".
 * @param dativ     Die Bezeichnung im Dativ Plural, z. B. "Kursen" in "… IDs von Kursen wurden übergeben …".
 * @param idTyp     Die Bezeichnung des ID-Typs, z. B. "Kurs-IDs" in "FEHLER: Es wurden ungültige Kurs-IDs übergeben.".
 */
record HtmlContextDatenbezeichnungen(String nominativ, String dativ, String idTyp) {
}
