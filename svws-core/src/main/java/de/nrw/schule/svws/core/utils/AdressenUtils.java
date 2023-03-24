package de.nrw.schule.svws.core.utils;

import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse stellt Hilfsmethoden rund um die Handhabung von 
 * Addressen zur Verfügung
 */
public class AdressenUtils {

	/**
	 * Teilt eine Strassenangabe bestehend aus dem 
	 * Strassennamen, der Hausnummer und dem Hausnummerzusatz
	 * in die Bestandteile auf.
	 * 
	 * @param strasse   die Strassenangabe
	 * 
	 * @return ein Array mit den 3 Elementen (0 - Strassennamen, 1 - Hausnummer und 2 - Hausnummerzusatz)
	 */
	public static @NotNull String@NotNull[] splitStrasse(final String strasse) {
		final @NotNull String @NotNull[] result = new String[3];
		if (strasse == null) {
			result[0] = "";
			result[1] = "";
			result[2] = "";
			return result;
		}
		final @NotNull String tmp = strasse.trim().replace("  ", " ").replace("  ", " ").replace(" -", "-").replace("- ", "-");
		result[0] = tmp.replaceFirst(" *([0-9]+ *[-\\+]+)* *[0-9]+\\D*$", "");
		final @NotNull String rest = tmp.substring(result[0].length()).trim();
		result[1] = rest.replaceFirst("\\D*$", "").trim();
		result[2] = rest.substring(result[1].length()).trim();
		if (result[0].length() > 55)
			result[0] = result[0].substring(0, 55);
		if (result[1].length() > 10)
			result[1] = result[1].substring(0, 10);
		if (result[2].length() > 30)
			result[2] = result[2].substring(0, 30);
		return result;
	}


	/**
	 * Kombiniert die übergebenen Werte für den Strassennamen, die Hausnummer und
	 * den Zusatz zu einer Strassenangabe in einem String.
	 * 
	 * @param name         der Strassenname
	 * @param hausNummer   die Hausnummer
	 * @param zusatz       der Hausnummerzusatz
	 * 
	 * @return die kombinierte Strassenangabe
	 */
	public static String combineStrasse(final String name, final String hausNummer, final String zusatz) {
		if ((name == null) || (hausNummer == null) || (zusatz == null))
			return null;
		if ("".equals(hausNummer.trim()) && ("".equals(zusatz.trim())))
			return name;
		return name + " " + hausNummer.trim() + zusatz.trim();
	}
	
}
