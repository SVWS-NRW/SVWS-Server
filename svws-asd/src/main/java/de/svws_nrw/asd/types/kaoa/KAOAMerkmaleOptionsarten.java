package de.svws_nrw.asd.types.kaoa;

import de.svws_nrw.asd.data.kaoa.KAOAMerkmaleOptionsartenKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;
/**
 * Der Core-Type für die KAOAMerkmaleOptionsarten.
 */
public enum KAOAMerkmaleOptionsarten implements CoreType<KAOAMerkmaleOptionsartenKatalogEintrag, KAOAMerkmaleOptionsarten> {

	/** Keine Option für das KAoA-Merkmal */
	KEINE;

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<KAOAMerkmaleOptionsartenKatalogEintrag, KAOAMerkmaleOptionsarten> manager) {
		CoreTypeDataManager.putManager(KAOAMerkmaleOptionsarten.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<KAOAMerkmaleOptionsartenKatalogEintrag, KAOAMerkmaleOptionsarten> data() {
		return CoreTypeDataManager.getManager(KAOAMerkmaleOptionsarten.class);
	}

}
