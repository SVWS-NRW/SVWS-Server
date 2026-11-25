package de.svws_nrw.service.schule.katalog.fachklasse;

import java.util.Optional;

import de.svws_nrw.asd.types.schule.Fachklasse;
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOFachklassen;

/**
 * Hilfsmethoden zur Auflösung der {@link DTOFachklassen#Kennung} einer Fachklasse des schulinternen Katalogs
 * auf den zugehörigen Eintrag des Core-Types {@link Fachklasse}.
 * <p>
 * Die Kennung (z. B. {@code "10-179-02"}) enthält den Fachklassen-Schlüssel des Core-Types mit einem zusätzlichen
 * Bindestrich vor dem letzten Teil. Durch Entfernen dieses Bindestrichs ergibt sich der Schlüssel (z. B. {@code "10-17902"}).
 */
public final class FachklasseKennungUtils {

	private static final String TRENNZEICHEN = "-";

	private FachklasseKennungUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}

	/**
	 * Rekonstruiert aus der Kennung einer Fachklasse den Fachklassen-Schlüssel des Core-Types, indem der letzte
	 * Bindestrich entfernt wird (z. B. {@code "10-179-02"} wird zu {@code "10-17902"}). Enthält die Kennung keinen
	 * Bindestrich, so wird sie unverändert zurückgegeben.
	 *
	 * @param kennung   die Kennung der Fachklasse
	 *
	 * @return der Fachklassen-Schlüssel oder {@code null}, wenn die Kennung {@code null} oder leer ist
	 */
	static String toSchluessel(final String kennung) {
		if ((kennung == null) || kennung.isBlank()) {
			return null;
		}
		final var lastDash = kennung.lastIndexOf(TRENNZEICHEN);
		if (lastDash < 0) {
			return kennung;
		}
		return kennung.substring(0, lastDash) + kennung.substring(lastDash + 1);
	}

	/**
	 * Ermittelt zu der Kennung einer Fachklasse die statistikrelevante ID des Eintrags im Core-Type {@link Fachklasse},
	 * der im angegebenen Schuljahr gültig ist.
	 *
	 * @param kennung     die Kennung der Fachklasse
	 * @param schuljahr   das Schuljahr, in dem der Eintrag des Core-Types gültig sein muss
	 *
	 * @return die ID des Core-Type-Eintrags oder {@code null}, wenn die Kennung {@code null} oder leer ist oder der
	 *         Schlüssel im Schuljahr unbekannt ist
	 */
	public static Long findIdFachklasseStatistik(final String kennung, final int schuljahr) {
		return Optional.ofNullable(toSchluessel(kennung))
				.map(schluessel -> Fachklasse.data().getEintragBySchuljahrUndSchluessel(schuljahr, schluessel))
				.map(eintrag -> eintrag.id)
				.orElse(null);
	}

}
