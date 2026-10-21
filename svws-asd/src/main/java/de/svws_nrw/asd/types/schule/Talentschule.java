package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.data.schule.TalentschuleKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für den Katalog der Talentschulen
 */
public enum Talentschule implements @NotNull CoreType<TalentschuleKatalogEintrag, Talentschule> {

	/** keine Talentschule */
	KEINE_TALENTSCHULE,

	/** Talentschule */
	TALENTSCHULE,

	/** Talentschule auslaufend */
	TALENTSCHULE_AUSLAUFEND;

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<TalentschuleKatalogEintrag, Talentschule> manager) {
		CoreTypeDataManager.putManager(Talentschule.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<TalentschuleKatalogEintrag, Talentschule> data() {
		return CoreTypeDataManager.getManager(Talentschule.class);
	}

}
