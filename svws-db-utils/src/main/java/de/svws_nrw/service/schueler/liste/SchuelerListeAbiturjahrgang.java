package de.svws_nrw.service.schueler.liste;

import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.utils.gost.GostAbiturjahrUtils;

/**
 * Bestimmt bei {@link SchuelerListeEintrag}-Objekten den Abiturjahrgang. Dies ist nur bei Schulformen
 * mit gymnasialer Oberstufe möglich.
 */
final class SchuelerListeAbiturjahrgang {

	/**
	 * Privater Konstruktor, da die Klasse nur statische Methoden bereitstellt.
	 */
	private SchuelerListeAbiturjahrgang() {
		// leer
	}

	/**
	 * Ergänzt bei den Einträgen den Abiturjahrgang, sofern die Schulform im übergebenen Schuljahr eine
	 * gymnasiale Oberstufe haben kann. Andernfalls bleiben die Einträge unverändert. Einträge, deren
	 * Schuljahresabschnitt nicht aufgelöst werden kann, bleiben ebenfalls unverändert.
	 *
	 * @param liste           die Einträge, welche ergänzt werden
	 * @param schulform       die Schulform der Schule
	 * @param schuljahr       das Schuljahr des gewählten Schuljahresabschnitts, für das die gymnasiale Oberstufe geprüft wird
	 * @param mapAbschnitte   die Schuljahresabschnitte zugeordnet zu ihrer ID
	 */
	static void addAbiturjahrgang(final List<SchuelerListeEintrag> liste, final Schulform schulform, final int schuljahr,
			final Map<Long, Schuljahresabschnitt> mapAbschnitte) {
		if (!hasGymnasialeOberstufe(schulform, schuljahr)) {
			return;
		}
		liste.stream()
				.filter(eintrag -> mapAbschnitte.containsKey(eintrag.idSchuljahresabschnitt))
				.forEach(eintrag -> eintrag.abiturjahrgang = determineAbiturjahrgang(eintrag, schulform, mapAbschnitte.get(eintrag.idSchuljahresabschnitt).schuljahr));
	}

	/**
	 * Bestimmt, ob die Schulform in dem übergebenen Schuljahr eine gymnasiale Oberstufe haben kann.
	 * Existiert für die Schulform in dem Schuljahr kein Katalog-Eintrag, so wird {@code false} zurückgegeben.
	 *
	 * @param schulform   die Schulform der Schule
	 * @param schuljahr   das Schuljahr
	 *
	 * @return {@code true}, falls die Schulform eine gymnasiale Oberstufe haben kann
	 */
	static boolean hasGymnasialeOberstufe(final Schulform schulform, final int schuljahr) {
		final var katalogEintrag = schulform.daten(schuljahr);
		return (katalogEintrag != null) && katalogEintrag.hatGymOb;
	}

	/**
	 * Bestimmt den Abiturjahrgang eines Eintrags.
	 *
	 * @param eintrag     der Eintrag
	 * @param schulform   die Schulform der Schule
	 * @param schuljahr   das Schuljahr des Schuljahresabschnitts des Eintrags
	 *
	 * @return der Abiturjahrgang oder null, falls dieser nicht bestimmt werden kann
	 */
	private static Integer determineAbiturjahrgang(final SchuelerListeEintrag eintrag, final Schulform schulform, final int schuljahr) {
		final var gliederung = Schulgliederung.data().getWertByIDOrNull(eintrag.idSchulgliederung);
		return GostAbiturjahrUtils.getGostAbiturjahr(schulform, gliederung, schuljahr, eintrag.jahrgang);
	}

}
