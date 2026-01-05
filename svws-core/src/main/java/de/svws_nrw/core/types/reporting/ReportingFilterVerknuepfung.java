package de.svws_nrw.core.types.reporting;

import jakarta.validation.constraints.NotNull;

/**
 * Die unterstützten logischen Verknüpfungen für Filterkriterien im Reporting.
 */
public enum ReportingFilterVerknuepfung {

	/** Keine Verknüpfung (Einfaches Kriterium) */
	UNDEFINED(0),

	/** Logisches UND */
	AND(1),

	/** Logisches ODER */
	OR(2);


	/** Die ID der Verknüpfung */
	private final int id;

	/**
	 * Erstellt eine neue Filter-Verknüpfung.
	 *
	 * @param id Die ID der Filter-Verknüpfung
	 */
	ReportingFilterVerknuepfung(final int id) {
		this.id = id;
	}

	/**
	 * Gibt die ID der Filter-Verknüpfung zurück.
	 *
	 * @return Die ID der Filter-Verknüpfung
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Diese Methode ermittelt die Filter-Verknüpfung anhand der übergebenen ID.
	 *
	 * @param id   	Die ID der gesuchten Filter-Verknüpfung
	 *
	 * @return 		Die Filter-Verknüpfung
	 */
	public static @NotNull ReportingFilterVerknuepfung getByID(final int id) {
		for (final ReportingFilterVerknuepfung op : ReportingFilterVerknuepfung.values())
			if (op.id == id)
				return op;
		return UNDEFINED;
	}
}
