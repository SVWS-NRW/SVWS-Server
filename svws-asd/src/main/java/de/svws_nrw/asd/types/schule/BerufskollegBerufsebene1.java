package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.data.schule.BerufskollegBerufsebeneKatalogEintrag;
import de.svws_nrw.asd.types.CoreTypeSimple;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die Berufsebenen der Ebene 1 am Berufskolleg.
 */
public final class BerufskollegBerufsebene1 extends CoreTypeSimple<BerufskollegBerufsebeneKatalogEintrag, BerufskollegBerufsebene1> {


	/**
	 * Erstellt einen Eintrag für die Berufsebene 1 mit Standardwerten
	 */
	public BerufskollegBerufsebene1() {
		// nichts zu tun
	}


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<BerufskollegBerufsebeneKatalogEintrag, BerufskollegBerufsebene1> manager) {
		CoreTypeDataManager.putManager(BerufskollegBerufsebene1.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<BerufskollegBerufsebeneKatalogEintrag, BerufskollegBerufsebene1> data() {
		return CoreTypeDataManager.getManager(BerufskollegBerufsebene1.class);
	}


	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static @NotNull BerufskollegBerufsebene1 @NotNull [] values() {
		return CoreTypeSimple.valuesByClass(BerufskollegBerufsebene1.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	@Override
	public BerufskollegBerufsebene1 getInstance() {
		return new BerufskollegBerufsebene1();
	}

}
