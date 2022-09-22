package de.nrw.schule.svws.core.utils.gost;

import de.nrw.schule.svws.core.types.statkue.Schulform;
import de.nrw.schule.svws.core.types.statkue.Schulgliederung;
import de.nrw.schule.svws.core.utils.jahrgang.JahrgangsUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt Hilfsmethoden in Bezug auf Abitur-Jahrgänge der gymnasialen Oberstufe zur Verfügung. 
 */
public class GostAbiturjahrUtils {

	/**
	 * Bestimmt für das übergegebene Schuljahr eines Schülers das Kalenderjahr, in welchem ein Schüler 
	 * der gymnasialen Oberstufe Abitur gemacht hat, macht oder machen wird. Hierbei wird für die 
	 * Bestimmung die Schulgliederung und Schulform bei dem Schüler sowie das aktuelle Schuljahr und der 
	 * Statistik-Jahrgang, in welchem sich der Schüler befindet, benötigt.
	 * 
	 * @param schulform            die Schulform 
	 * @param gliederung           die Schulgliederung des Schülers
	 * @param aktuellesSchuljahr   das aktuelle Schuljahr, in welchem sich der Schüler befindet
	 * @param jahrgang             der Statistik-Jahrgang des Schülers
	 * 
	 * @return das Kalenderjahr des Abiturs oder null, falls das Jahr des Abiturs nicht bestimmt werden kann.
	 */
	public static Integer getGostAbiturjahr(@NotNull Schulform schulform, @NotNull Schulgliederung gliederung, int aktuellesSchuljahr, @NotNull String jahrgang) {
		if ((schulform.daten == null) || (!schulform.daten.hatGymOb))
			return null;
		Integer restjahre = JahrgangsUtils.getRestlicheJahre(schulform, gliederung, jahrgang);
		return restjahre == null ? null : aktuellesSchuljahr + restjahre;		
	}

	
	/**
	 * Bestimmt für das angegebene Abiturjahr, das angegebene aktuelles Schuljahr, in dem sich die
	 * Schule befindet, und der angegebenen Schulform und Schulgliederung den zugrhörigen Statistik-Jahrgang 
	 * einer Schule mit gymnasialer Oberstufe.  
	 * 
	 * @param schulform     die Schulform 
	 * @param gliederung    die Schulgliederung
	 * @param schuljahr     das Schuljahr, in dem sich die Schule befindet
	 * @param abiturjahr    das Abiturjahr, für welches der Statistik-Jahrgang ermittelt werden soll. 
	 * 
	 * @return der Statistik-Jahrgang zu dem angegeben Abiturjahrgang
	 */
	public static String getGostAbiturjahrJahrgang(@NotNull Schulform schulform, @NotNull Schulgliederung gliederung, int schuljahr, int abiturjahr) {
		if ((schulform.daten == null) || (!schulform.daten.hatGymOb))
			return null;
		int restlicheJahre = abiturjahr - schuljahr;
		if (restlicheJahre <= 1)
			return "Q2";
		if (restlicheJahre == 2)
			return "Q1";
		if (restlicheJahre == 3)
			return "EF";
		int sekIJahre = gliederung.istG8() || ((schulform == Schulform.GY) && (gliederung == Schulgliederung.DEFAULT)) 
				      ? 9 : 10;
		if (restlicheJahre >= sekIJahre)
			return null;
		String strJG = "" + (sekIJahre - (restlicheJahre - 4));
		if (strJG.length() == 1)
			strJG = "0" + strJG;
		return strJG;
	}

	
}
