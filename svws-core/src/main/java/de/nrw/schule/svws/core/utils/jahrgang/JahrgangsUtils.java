package de.nrw.schule.svws.core.utils.jahrgang;

import de.nrw.schule.svws.core.types.statkue.Schulform;
import de.nrw.schule.svws.core.types.statkue.Schulgliederung;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt Hilfsmethoden in Bezug auf Jahrgänge zur Verfügung. 
 */
public class JahrgangsUtils {

	/**
	 * Bestimmt für die angegebene Schulform, die übergebene Schulgliederung (auch beim Schüler eingetragenen Schulgliederung)
	 * und den angegebenen Jahrgang die restlichen Jahre an der Schule.
	 * Ist keine Berechnung für diese Kombination implementiert,
	 * so wird null zurückgegeben.
	 * 
	 * @param schulform    die Schulform 
	 * @param gliederung   die Schulgliederung
	 * @param jahrgang     der Jahrgang, für den die restlichen Jahre bestimmt werden sollen
	 * 
	 * @return die restlichen Jahre oder null 
	 */
	public static Integer getRestlicheJahre(@NotNull Schulform schulform, @NotNull Schulgliederung gliederung, @NotNull String jahrgang) {
		// TODO Benutze einen noch zu definierenden Core-Type für Jahrgänge und verschiebe diese Methode in diesen Core-Type
		if (gliederung == null)
			return null;
		if ((schulform == Schulform.FW) || (schulform == Schulform.WB) || (schulform == Schulform.BK) || (schulform == Schulform.SB))
			return null;
		if (schulform == Schulform.GY){
			// Gymnasium zählt Restjahre immer bis zum Abitur
			switch (jahrgang) {
				// DEFAULT (***) wird hier als G8 interpretiert, Ausnahme Jahrgang 10
				case "05": return gliederung.istG8() || (gliederung == Schulgliederung.DEFAULT) ? 8 : 9;
				case "06": return gliederung.istG8() || (gliederung == Schulgliederung.DEFAULT) ? 7 : 8;
				case "07": return gliederung.istG8() || (gliederung == Schulgliederung.DEFAULT) ? 6 : 7;
				case "08": return gliederung.istG8() || (gliederung == Schulgliederung.DEFAULT) ? 5 : 6;
				case "09": return gliederung.istG8() || (gliederung == Schulgliederung.DEFAULT) ? 4 : 5;
				// Jahrgangsstufe 10 gibt es am GY nur im G9, d. h. DEFAULT (***) kann dort immer als G9 interpretiert werden
				case "10": return gliederung.istG8() ? null : 4;
				case "EF": return 3;
				case "Q1": return 2;
				case "Q2": return 1;
				// Alte Oberstufenjahrgänge, keine Verwendung bei aktiven SuS an GY und GE seit 2010
				case "11": return 3;
				case "12": return 2;
				case "13": return 1;
			}
		}
		else{
			// Angaben für die allgemein bildenden Schulen ohne Gymnasium
			// TODO nicht darunter fallende Schulformen vorher verarbeiten
			switch (jahrgang) {
				// Primarstufe
				case "E1": return 4;
				case "E2": return 3;
				case "E3": return 3;
				case "03": return 2;
				case "04": return 1;
				// Sekundarstufe I inklusive Gesamtschulen
				case "05": return 6;
				case "06": return 5;
				case "07": return 4;
				case "08": return 3;
				case "09": return 2;
				case "10": return 1;
				// Sekundarstufe II
				case "EF": return 3;
				case "Q1": return 2;
				case "Q2": return 1;
				// Alte Oberstufenjahrgänge, keine Verwendung bei aktiven SuS an GY und GE seit 2010
				case "11": return 3;
				case "12": return 2;
				case "13": return 1;
			}
		}
		return null;
	}	
	
}
