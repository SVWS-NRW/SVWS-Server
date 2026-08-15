package de.svws_nrw.module.reporting.diagnose;

/**
 * Die Auswirkung, die eine {@link ReportingProblemursache} in der Ausgabe hat. Dieselbe Ursache wirkt je nach Stelle verschieden: Ein Ladefehler lässt einen
 * Schüler ganz entfallen oder nur seine Teildaten. Die Auswirkung beschreibt, was fehlt - nicht, wie die Vorlage die Stelle darstellt; das ist deren
 * Entscheidung und keine eigene Auswirkungsart.
 */
public enum ReportingProblemauswirkung {

	/** Eine angeforderte Ausgabeeinheit entfällt vollständig - der Datensatz erscheint in der Ausgabe nicht. */
	DATENSATZ_AUSGELASSEN,

	/** Der Datensatz erscheint, ihm fehlen aber untergeordnete Daten - auch dann, wenn die Vorlage die Stelle sichtbar als fehlend kennzeichnet. */
	TEILDATEN_FEHLEN

}
