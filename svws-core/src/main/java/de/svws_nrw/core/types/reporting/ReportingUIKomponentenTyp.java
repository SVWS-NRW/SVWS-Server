package de.svws_nrw.core.types.reporting;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung definiert die möglichen Typen von UI-Komponenten, die für die Eingabe von Report-Parametern in der GUI verwendet werden können.
 */
public enum ReportingUIKomponentenTyp {
	/** KomponentenTyp wurde vom Typ her noch nicht festgelegt. */
	UNDEFINED(0),

	/** KomponentenTyp Checkbox, der für die Eingabe von Boolean-Werten verwendet werden kann. */
	CHECKBOX(1),

	/** KomponentenTyp Input, der für die Eingabe von String-Werten verwendet werden kann. */
	INPUT(2),

	/** KomponentenTyp Select, der für die Auswahl von Werten aus einer Liste verwendet werden kann. */
	SELECT(3),

	/** KomponentenTyp Textarea, der für die Eingabe von längeren String-Werten verwendet werden kann. */
	TEXTAREA(4),

	/** KomponentenTyp NumberPicker, der für die Eingabe von numerischen Werten verwendet werden kann. */
	NUMBERPICKER(5),

	/** KomponentenTyp DatePicker, der für die Eingabe von Datumswerten verwendet werden kann. */
	DATEPICKER(6);

	/** Die ID des KomponentenTyps */
	private final int id;


	/**
	 * Erstellt einen neuen ReportingUIKomponentenTyp
	 *
	 * @param id Die ID des ReportingUIKomponentenTyp
	 */
	ReportingUIKomponentenTyp(final int id) {
		this.id = id;
	}

	/**
	 * Gibt die ID dieses ReportingUIKomponentenTyp zurück
	 *
	 * @return Die ID dieses ReportingUIKomponentenTyp
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Diese Methode ermittelt den ReportingUIKomponentenTyp anhand der übergebenen ID.
	 *
	 * @param id   	Die ID des gesuchten KomponentenTyps
	 *
	 * @return 		Der ReportingUIKomponentenTyp
	 */
	public static @NotNull ReportingUIKomponentenTyp getByID(final int id) {
		for (final ReportingUIKomponentenTyp dp : ReportingUIKomponentenTyp.values())
			if (dp.id == id)
				return dp;
		return UNDEFINED;
	}


}

