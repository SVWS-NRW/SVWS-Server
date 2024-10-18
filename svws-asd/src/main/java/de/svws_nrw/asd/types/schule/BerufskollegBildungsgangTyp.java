package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.data.schule.BildungsgangTypKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für den (Schul-)Typ des Bildungsgangs am Berufskolleg.
 */
public enum BerufskollegBildungsgangTyp implements CoreType<BildungsgangTypKatalogEintrag, BerufskollegBildungsgangTyp> {

	/** Berufsfachschulen */
	BERUFSFACHSCHULE,

	/** Berufsfachschulen */
	BERUFSSCHULE,

	/** Berufliches Gymnasium */
	BERUFLICHES_GYMNASIUM,

	/** Fachoberschule */
	FACHOBERSCHULE,

	/** Fachschule */
	FACHSCHULE;


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<BildungsgangTypKatalogEintrag, BerufskollegBildungsgangTyp> manager) {
		CoreTypeDataManager.putManager(BerufskollegBildungsgangTyp.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<BildungsgangTypKatalogEintrag, BerufskollegBildungsgangTyp> data() {
		return CoreTypeDataManager.getManager(BerufskollegBildungsgangTyp.class);
	}

}
