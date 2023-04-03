package de.svws_nrw.core.utils.gost;

import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.types.schule.Schulgliederung;
import de.svws_nrw.core.utils.jahrgang.JahrgangsUtils;
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
	public static Integer getGostAbiturjahr(final @NotNull Schulform schulform, final @NotNull Schulgliederung gliederung, final int aktuellesSchuljahr, final @NotNull String jahrgang) {
		if ((schulform.daten == null) || (!schulform.daten.hatGymOb))
			return null;
		final Integer restjahre = JahrgangsUtils.getRestlicheJahre(schulform, gliederung, jahrgang);
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
	public static String getGostAbiturjahrJahrgang(final @NotNull Schulform schulform, final @NotNull Schulgliederung gliederung, final int schuljahr, final int abiturjahr) {
		if ((schulform.daten == null) || (!schulform.daten.hatGymOb))
			return null;
		final int restlicheJahre = abiturjahr - schuljahr;
		if (restlicheJahre <= 1)
			return "Q2";
		if (restlicheJahre == 2)
			return "Q1";
		if (restlicheJahre == 3)
			return "EF";
		final int sekIJahre = gliederung.istG8() || ((schulform == Schulform.GY) && (gliederung == Schulgliederung.DEFAULT))
				      ? 9 : 10;
		if (restlicheJahre >= sekIJahre)
			return null;
		String strJG = "" + (sekIJahre - (restlicheJahre - 4));
		if (strJG.length() == 1)
			strJG = "0" + strJG;
		return strJG;
	}


}
