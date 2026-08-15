/**
 * Typen der Reporting-Diagnose: Sie beschreiben, warum Daten in einer Ausgabe fehlen, was daraus folgt und was davon den Anwender erreicht.
 *
 * <p>Für die Orientierung genügt die Einteilung in drei Gruppen:</p>
 *
 * <ul>
 * <li><b>Beim Melden eines Befunds:</b> {@link de.svws_nrw.module.reporting.diagnose.ReportingProblemursache},
 * {@link de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung} und {@link de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel}
 * sind die Parameter der Fassade {@code ReportingContext.meldeAusgabeproblem(...)}. Mehr braucht eine Meldestelle nicht.</li>
 * <li><b>Beim Bau eines Datenaufbaus:</b> {@link de.svws_nrw.module.reporting.diagnose.ReportingLadezustand} und
 * {@link de.svws_nrw.module.reporting.diagnose.ReportingAuswahlergebnis} tragen das Ergebnis der Datenzugriffe bis zur Ausgabe.</li>
 * <li><b>Innenleben:</b> {@link de.svws_nrw.module.reporting.diagnose.ReportingProblem} und
 * {@link de.svws_nrw.module.reporting.diagnose.ReportingProblemSammler} arbeiten hinter der Fassade;
 * {@link de.svws_nrw.module.reporting.diagnose.ReportingHinweisKategorie} und {@link de.svws_nrw.module.reporting.diagnose.ReportingHinweisSerializer}
 * bilden daraus den öffentlichen Hinweisvertrag (Header und Hinweisdatei).</li>
 * </ul>
 *
 * <p>Zwei Regeln halten die Grenzen: Ein vom Benutzerfilter ausgeschlossener Datensatz ist eine Auswahlentscheidung und wird nicht gemeldet. Und eine
 * Ursache, die die Ausgabe abbricht, wird geworfen statt gesammelt.</p>
 *
 * <p>Die Einordnung in die Architektur beschreibt {@code reporting-architektur.md}, Abschnitt 9.2.</p>
 */
package de.svws_nrw.module.reporting.diagnose;
