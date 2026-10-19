package de.svws_nrw.core.types.reporting;

/**
 * Die Bereiche im Client, in denen eine Report-Vorlage angeboten wird.
 * Der Client erzeugt dort die Report-Cards aus allen Vorlagen mit der passenden Nutzung, statt jede Vorlage einzeln einzubinden.
 * Der Server wertet die Angabe nicht aus.
 */
public enum ReportingReportvorlageUiNutzung {

	/** Bereich der Schülerinnen und Schüler */
	SCHUELER,

	/** Bereich der Klassen */
	KLASSEN,

	/** Bereich der Kurse */
	KURSE,

	/** Bereich der Lehrkräfte */
	LEHRER,

	/** Bereich der Fächer */
	FAECHER;

}
