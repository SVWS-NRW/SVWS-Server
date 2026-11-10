package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.data.schule.InternatArtKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für den Katalog der möglichen Internatsarten
 */
public enum InternatArt implements @NotNull CoreType<InternatArtKatalogEintrag, InternatArt> {

	/** kein Internat */
	KEIN_INTERNAT,

	/** Internat nur für Jungen */
	INTERNAT_JUNGEN,

	/** Internat nur für Mädchen */
	INTERNAT_MAEDCHEN,

	/** Internat für Jungen und Mädchen */
	INTERNAT_NEUTRAL;


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<InternatArtKatalogEintrag, InternatArt> manager) {
		CoreTypeDataManager.putManager(InternatArt.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<InternatArtKatalogEintrag, InternatArt> data() {
		return CoreTypeDataManager.getManager(InternatArt.class);
	}

}
