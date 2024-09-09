package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.data.schule.KindergartenbesuchKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;


/**
 * Ein Core-Type für die für die Dauer des Kindergartenbesuchs.
 */
public enum Kindergartenbesuch implements @NotNull CoreType<KindergartenbesuchKatalogEintrag, Kindergartenbesuch> {

	/** Kein Kindergartenbesuch */
	KEINER,

	/** Kindergartenbesuch unter einem Jahr */
	MAX_1_JAHR,

	/** Kindergartenbesuch unter einem Jahr */
	MAX_2_JAHRE,

	/** Kindergartenbesuch unter einem Jahr */
	MAX_3_JAHRE,

	/** Kindergartenbesuch unter einem Jahr */
	MIN_3_JAHRE;


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<KindergartenbesuchKatalogEintrag, Kindergartenbesuch> manager) {
		CoreTypeDataManager.putManager(Kindergartenbesuch.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<KindergartenbesuchKatalogEintrag, Kindergartenbesuch> data() {
		return CoreTypeDataManager.getManager(Kindergartenbesuch.class);
	}

}
