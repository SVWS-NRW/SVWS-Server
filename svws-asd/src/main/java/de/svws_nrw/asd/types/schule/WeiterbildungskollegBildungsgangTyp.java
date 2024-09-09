package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.data.schule.BildungsgangTypKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die Bildungsgangtypen an Weiterbildungskollegs
 */
public enum WeiterbildungskollegBildungsgangTyp implements @NotNull CoreType<BildungsgangTypKatalogEintrag, WeiterbildungskollegBildungsgangTyp> {

	/** Abendgymnasium */
	ABENDGYMNASIUM,

	/** Abendrealschule */
	ABENDREALSCHULE,

	/** Kolleg */
	KOLLEG;

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<BildungsgangTypKatalogEintrag, WeiterbildungskollegBildungsgangTyp> manager) {
		CoreTypeDataManager.putManager(WeiterbildungskollegBildungsgangTyp.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<BildungsgangTypKatalogEintrag, WeiterbildungskollegBildungsgangTyp> data() {
		return CoreTypeDataManager.getManager(WeiterbildungskollegBildungsgangTyp.class);
	}

}
