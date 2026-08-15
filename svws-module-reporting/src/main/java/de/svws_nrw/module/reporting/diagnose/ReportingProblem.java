package de.svws_nrw.module.reporting.diagnose;

import java.util.Objects;

/**
 * Ein hingenommenes Ausgabeproblem: ein Befund, durch den etwas in der Ausgabe fehlt, ohne den Report abzubrechen. Die drei Bestandteile bilden seine
 * Identität - gleiche Ursache, Auswirkung und Schlüssel bezeichnen denselben Befund und zählen einmal.
 *
 * @param ursache    Woran es liegt.
 * @param auswirkung Was daraus in der Ausgabe folgt.
 * @param schluessel Welches Objekt betroffen ist.
 */
public record ReportingProblem(ReportingProblemursache ursache, ReportingProblemauswirkung auswirkung, ReportingProblemSchluessel schluessel) {

	/**
	 * Prüft, dass alle drei Bestandteile vorliegen und die Ursache hingenommen werden darf - sonst wäre die Deduplizierung unzuverlässig.
	 *
	 * @param ursache    Woran es liegt; eine abbrechende Ursache ist unzulässig.
	 * @param auswirkung Was daraus in der Ausgabe folgt.
	 * @param schluessel Welches Objekt betroffen ist.
	 *
	 * @throws IllegalArgumentException Wenn die Ursache die Ausgabe abbricht - ein Abbruch ist kein hingenommenes Problem.
	 */
	public ReportingProblem {
		Objects.requireNonNull(ursache, "Ein Ausgabeproblem braucht eine Ursache.");
		Objects.requireNonNull(auswirkung, "Ein Ausgabeproblem braucht eine Auswirkung.");
		Objects.requireNonNull(schluessel, "Ein Ausgabeproblem braucht einen Schlüssel.");
		if (ursache.istAbbruch()) {
			throw new IllegalArgumentException(
					"Die Ursache %s bricht die Ausgabe ab und ist deshalb kein hingenommenes Ausgabeproblem.".formatted(ursache.name()));
		}
	}

}
