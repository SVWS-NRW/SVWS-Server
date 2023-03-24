package de.nrw.schule.svws.core.utils.schule;

import de.nrw.schule.svws.core.data.schule.Schuljahresabschnitt;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse dient der Formatierung von Schuljahresabschnitten
 *
 */
public class SchuljahresAbschnittsManager {

	/**
	 * Der aktuelle Schuljahresabschnitt
	 */
	@NotNull private final Schuljahresabschnitt abschnitt;

	/**
	 * Die Anzahl an Schuljahresabschnitten an dieser Schule
	 */
	private final int anzahlAbschnitte;

	/**
	 * Konstruktor für den SchuljahresManager mit Schuljahresabschnitt und der
	 * Anzahl an Abschnitten für diese Schule
	 * 
	 * @param schuljahresabschnitt der Schuljahresabschnitt für den dieser Manager
	 *                             die Stringrepräsentation bestimmen soll
	 * @param anzahlAbschnitte     die Anzahl an Schuljahresabschnitten dieser
	 *                             Schule
	 */
	public SchuljahresAbschnittsManager(final @NotNull Schuljahresabschnitt schuljahresabschnitt, final int anzahlAbschnitte) {
		this.abschnitt = schuljahresabschnitt;
		this.anzahlAbschnitte = anzahlAbschnitte;
	}

	/**
	 * Gibt den Schuljahresabschnitt dieses Managers als Stringrepräsentation
	 * wieder. Arbeitet mit den Konstruktorparametern für Schuljahresabschnitt und
	 * die Anzahl der Abschnitte
	 * 
	 * @return einen String, der den Schuljahresabschnitt wiedergibt, bspw:<br>
	 *         {@code S2 2022}<br>
	 *         {@code Q4 2022}<br>
	 *         {@code 4/6 2022}<br>
	 * 
	 */
	public @NotNull String getSchuljahresAbschnittAsString() {
		return createSchuljahresAbschnittString(abschnitt, anzahlAbschnitte);
	}

	/**
	 * Gibt den Schuljahresabschnitt dieses Managers als Stringrepräsentation
	 * wieder. Diese Methode dient dazu, abweichende Parameter als im Konstruktor
	 * angeben zu können
	 * 
	 * @param abschnitt        der Schuljahresabschnitt für den die
	 *                         Stringrepräsentation bestimmt werden soll
	 * @param anzahlAbschnitte die Anzahl an Schuljahresabschnitten dieser Schule
	 * 
	 * @return einen String, der den Schuljahresabschnitt wiedergibt, bspw:<br>
	 *         {@code S2 2022}<br>
	 *         {@code Q4 2022}<br>
	 *         {@code 4/6 2022}<br>
	 * 
	 */
	public static @NotNull String createSchuljahresAbschnittString(final @NotNull Schuljahresabschnitt abschnitt,
			final int anzahlAbschnitte) {
		if (anzahlAbschnitte <= 1) {
			return "" + abschnitt.schuljahr;
		} else if (anzahlAbschnitte > 1 && anzahlAbschnitte < 5) {
			return createRepresentationForAnzahlAbschnitte(anzahlAbschnitte) + abschnitt.abschnitt + " " + abschnitt.schuljahr;
		} else {
			return abschnitt.abschnitt + "/" + anzahlAbschnitte + " " + abschnitt.schuljahr;
		}
	}

	/**
	 * Gibt abhängig von der für diesen Manager konfigurierten Anzahl der
	 * Schuljahresabschnitte im Jahr ein Abschnittskuerzel wieder:<br>
	 * 
	 * @return S (für Semester), wenn es 2 Abschnitte gibt<br>
	 *         T (Für Trimester), wenn es 3 Abschnitte gibt<br>
	 *         Q (für Quartale), wenn es 4 Abschnitte gibt<br>
	 *         leerer String, bei 1 oder mehr als 4 Abschnitten<br>
	 */

	public @NotNull String getRepresentationForAnzahlAbschnitte() {
		return createRepresentationForAnzahlAbschnitte(anzahlAbschnitte);
	}

	/**
	 * Gibt abhängig von der Anzahl der Schuljahresabschnitte im Jahr ein
	 * Abschnittskuerzel wieder. Mit dieser Methode kann ein vom
	 * Konstruktorparameter abweichender Wert bestimmt werden:<br>
	 * 
	 * @param anzahlAbschnitte Anzahl der Abschnitte in einem Schuljahr
	 * @return S (für Semester), wenn es 2 Abschnitte gibt<br>
	 *         T (Für Trimester), wenn es 3 Abschnitte gibt<br>
	 *         Q (für Quartale), wenn es 4 Abschnitte gibt<br>
	 *         leerer String, bei 1 oder mehr als 4 Abschnitten<br>
	 */
	public static @NotNull String createRepresentationForAnzahlAbschnitte(final int anzahlAbschnitte) {
		if (anzahlAbschnitte == 2)
			return "S";
		else if (anzahlAbschnitte == 3)
			return "T";
		else if (anzahlAbschnitte == 4)
			return "Q";
		else
			return "";
	}
}
