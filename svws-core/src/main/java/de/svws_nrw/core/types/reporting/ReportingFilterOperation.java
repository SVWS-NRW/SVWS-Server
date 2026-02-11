package de.svws_nrw.core.types.reporting;

import jakarta.validation.constraints.NotNull;

/**
 * Die unterstützten Operationen für Filterkriterien im Reporting.
 */
public enum ReportingFilterOperation {

	/** Undefiniert, eine Filter-Operation wurde nicht angegeben. */
	UNDEFINED(0),

	/** Prüfung auf Gleichheit */
	EQUAL(1),

	/** Prüfung auf Ungleichheit */
	NOT_EQUAL(2),

	/** Prüfung, ob der Wert im Filterwert enthalten ist (bei Strings) */
	CONTAINS(3),

	/** Prüfung, ob der Wert mit dem Filterwert beginnt (bei Strings) */
	STARTS_WITH(4),

	/** Prüfung, ob der Wert mit dem Filterwert endet (bei Strings) */
	ENDS_WITH(5),

	/** Prüfung auf größer als */
	GREATER(6),

	/** Prüfung auf größer oder gleich */
	GREATER_OR_EQUAL(7),

	/** Prüfung auf kleiner als */
	LESS(8),

	/** Prüfung auf kleiner oder gleich */
	LESS_OR_EQUAL(9),

	/** Prüfung, ob der Wert in einer Liste von Werten enthalten ist */
	IN(10),

	/** Prüfung, ob der Wert zwischen zwei Werten liegt (inklusiv) */
	BETWEEN(11);


	/** Die ID der Filter-Operation */
	private final int id;

	/**
	 * Erstellt eine neue Filter-Operation
	 *
	 * @param id Die ID der Filter-Operation
	 */
	ReportingFilterOperation(final int id) {
		this.id = id;
	}

	/**
	 * Gibt die ID der Filter-Operation zurück
	 *
	 * @return Die ID der Filter-Operation
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Diese Methode ermittelt die Filter-Operation anhand der übergebenen ID.
	 *
	 * @param id   	Die ID der gesuchten Filter-Operation
	 *
	 * @return 		Die Filter-Operation
	 */
	public static @NotNull ReportingFilterOperation getByID(final int id) {
		for (final ReportingFilterOperation op : ReportingFilterOperation.values())
			if (op.id == id)
				return op;
		return UNDEFINED;
	}

}
