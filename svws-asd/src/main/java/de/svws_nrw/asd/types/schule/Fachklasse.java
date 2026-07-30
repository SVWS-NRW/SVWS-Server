package de.svws_nrw.asd.types.schule;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.schule.FachklasseKatalogEintrag;
import de.svws_nrw.asd.types.CoreTypeSimple;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die möglichen Fachklassen an Berufskollegen
 */
public class Fachklasse extends CoreTypeSimple<FachklasseKatalogEintrag, Fachklasse> {

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<FachklasseKatalogEintrag, Fachklasse> manager) {
		CoreTypeDataManager.putManager(Fachklasse.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<FachklasseKatalogEintrag, Fachklasse> data() {
		return CoreTypeDataManager.getManager(Fachklasse.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static @NotNull Fachklasse @NotNull [] values() {
		return CoreTypeSimple.valuesByClass(Fachklasse.class);
	}

	/**
	 * Erzeugt eine Instanz dieser Klasse.
	 */
	@Override
	public Fachklasse getInstance() {
		return new Fachklasse();
	}

	/**
	 * Liefert die zulässigen Fachklassen für den angegebenen bkIndex in dem angegebenen Schuljahr
	 * Wenn der angegebene bkIndex null ist, dann werden alle Fachklassen für das angegebene Schuljahr zurückgegeben
	 *
	 * @param schuljahr das Schuljahr
	 * @param bkIndex der BKIndex
	 *
	 * @return Liste von {@link FachklasseKatalogEintrag}
	 */
	public static @NotNull List<FachklasseKatalogEintrag> getBySchuljahrAndBKIndex(final int schuljahr, final Integer bkIndex) {
		final var result = new ArrayList<FachklasseKatalogEintrag>();
		final var fachklassen = data().getEintraegeBySchuljahr(schuljahr);
		if (bkIndex == null) {
			return fachklassen;
		}
		for (final FachklasseKatalogEintrag fachklasse : fachklassen) {
			if (bkIndex.equals(fachklasse.bkIndex)) {
				result.add(fachklasse);
			}
		}
		return result;
	}

}
