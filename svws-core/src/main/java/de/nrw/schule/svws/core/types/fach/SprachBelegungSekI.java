package de.nrw.schule.svws.core.types.fach;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung beinhaltet die für die Sekundarstufe II relevanten Belegungsmöglichkeiten
 * für Sprachen in der Sekundastufe I. 
 */
public class SprachBelegungSekI implements Comparable<SprachBelegungSekI> {
	
	/** Gibt an, dass eine Sprache in der Sekundarstufe I nicht oder weniger als 2 Jahre belegt wurde */
	public static final @NotNull SprachBelegungSekI NICHT_BELEGT = new SprachBelegungSekI(0);
	
	/** Gibt an, dass eine Sprache in der Sekundarstufe I mindestens 2 Jahre - aber nicht 4 oder mehr Jahre - belegt wurde */
	public static final @NotNull SprachBelegungSekI MIND_2_JAHRE = new SprachBelegungSekI(2);
	
	/** Gibt an, dass eine Sprache in der Sekundarstufe I mindestens 4 Jahre - aber nicht ab Klasse 5 - belegt wurde */
	public static final @NotNull SprachBelegungSekI MIND_4_JAHRE = new SprachBelegungSekI(4);
	
	/** Gibt an, dass eine Sprache in der Sekundarstufe I ab Klasse 5, d.h. 5 (in G8) oder 6 Jahre belegt wurde. */
	public static final @NotNull SprachBelegungSekI AB_JAHRGANG_5 = new SprachBelegungSekI(6);



	/** Die Dauer der Sprachbelegung in der SekI - der Wert kann von der realen Belegung abweichen, da nur die relevante Dauer angeben ist und im Falle des Jahrgangs 5 abweichen kann, falls der G8-Bildungsgang vorliegt */ 
	public final int dauer;

	
	/**
	 * Erstellt einen neuen enum-Wert mit der angegebenen Dauer der Sprachbelegung.
	 * 
	 * @param dauer   die Dauer der Sprachbelegung in der Sek I
	 */
	private SprachBelegungSekI(final int dauer) {
		this.dauer = dauer;
	}
	
	
	/**
	 * Ermittelt die Spachbelegung in der Sek I anhand des übergebenen Jahrgangs. 
	 * WICHTIG: Sollte ein Schüler sich im G8-Bildungsgang bewegen, so wird die Dauer 
	 * mit 6 Jahren hier nicht korrekt zugeordnet.  
	 * 
	 * @param ASDJahrgang der Jahrgang in welchem mit der Sprache begonnen wurde
	 * 
	 * @return die Sprachbelegung in der Sek I
	 */
	public static @NotNull SprachBelegungSekI getByASDJahrgang(final String ASDJahrgang) {
		if (ASDJahrgang == null)
			return NICHT_BELEGT;
		// Die Sprache ist länger als 4 Jahre belegt - es wird daher von 6 Jahren ausgegangen. Im Rahmen des G8 liegen aber nur 5 Jahre vor
		// TODO ggf. eine G8-Prüfung einführen ??? Einfluss auf Sprachreferenzniveau gegeben?
		if (ASDJahrgang.compareTo("05") <= 0)
			return AB_JAHRGANG_5;
		// Im G8-Bildungsgang beginnt eine Sprache im Rahmen des WP I in Klasse 6, ansonsten in Klasse 7, um 4 Jahre in der Sek I zu ermöglichen 
		if (ASDJahrgang.compareTo("07") <= 0)
			return MIND_4_JAHRE;
		// Im G8-Bildungsgang beginnt eine Sprache im Rahmen des WP II in Klasse 8, ansonsten in Klasse 9, um 2 Jahre in der Sek I zu ermöglichen
		if (ASDJahrgang.compareTo("09") <= 0)
			return MIND_2_JAHRE;
		// Eine Sprache kann nicht im 10. Jahrgang einsetzen
		return NICHT_BELEGT;
	}

	
	/**
	 * Ermittelt die Spachbelegung in der Sek I anhand der übergebenen Dauer der Belegung in der Sek I.
	 * WICHTIG: Sollte ein Schüler sich im G8-Bildungsgang bewegen, so wird die Dauer 
	 * mit 6 Jahren hier nicht korrekt zugeordnet.  
	 *  
	 * @param dauer   die Dauer in vollen Jahren bei der Sprachbelegung in der Sek I
	 * 
	 * @return die Sprachbelegung in der Sek I
	 */
	public static @NotNull SprachBelegungSekI getByDauer(final int dauer) {
		if (dauer <= 0)
			return NICHT_BELEGT;
		if (dauer <= 3)
			return MIND_2_JAHRE;
		if (dauer <= 4)
			return MIND_4_JAHRE;
		// TODO Spezialfall G8 - Belegung hier auch als 6 Jahre gewertet, obwohl es nur 5 sind...
		return AB_JAHRGANG_5;
	}


	@Override
	public int compareTo(final SprachBelegungSekI other) {
		if (other == null)
			return 1;   // irgendetwas ist besser als keine Belegung
		return Integer.compare(dauer, other.dauer);
	}
	

}
