package de.svws_nrw.asd.types.kaoa;

import de.svws_nrw.asd.data.kaoa.KAOAZusatzmerkmaleOptionsartenKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die KAOA-ZusatzmerkmaleOptionsarten.
 */
public enum KAOAZusatzmerkmaleOptionsarten implements CoreType<KAOAZusatzmerkmaleOptionsartenKatalogEintrag, KAOAZusatzmerkmaleOptionsarten> {

	/** Keine Option für das KAoA-Zusatzmerkmal */
	KEINE,

	/** Anschlussoptionen laut SBO 10.7 */
	ANSCHLUSSOPTION,

	/** Berufsfelder */
	BERUFSFELD,

	/** Freitext */
	FREITEXT,

	/** Freitext Beruf */
	FREITEXT_BERUF,

	/** SBO der Ebene 4 (SBO x.x.x.y) */
	SBO_EBENE_4;


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<KAOAZusatzmerkmaleOptionsartenKatalogEintrag, KAOAZusatzmerkmaleOptionsarten> manager) {
		CoreTypeDataManager.putManager(KAOAZusatzmerkmaleOptionsarten.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<KAOAZusatzmerkmaleOptionsartenKatalogEintrag, KAOAZusatzmerkmaleOptionsarten> data() {
		return CoreTypeDataManager.getManager(KAOAZusatzmerkmaleOptionsarten.class);
	}

}
