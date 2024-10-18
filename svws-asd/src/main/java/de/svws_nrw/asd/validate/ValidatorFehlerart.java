package de.svws_nrw.asd.validate;

/**
 * Enum der Fehlerarten von Validatoren.
 */
public enum ValidatorFehlerart {

	/** MUSS-Fehler : verhindert das Absenden der Statistik */
	HART,

	/** KANN-Fehler: Fehler, der erklärt werden muss, aber das Absenden der Statistik nicht verhindert */
	MUSS,

	/** HINWEIS: auf einen möglichen Fehler */
	HINWEIS,

	/** UNGENUTZT: der Validator soll nicht ausgeführt werden, wegen Ausschluss im Umfeld oder Schulform */
	UNGENUTZT;

}
