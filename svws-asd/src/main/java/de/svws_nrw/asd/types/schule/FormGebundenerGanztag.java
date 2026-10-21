package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.data.schule.FormGebundenerGanztagKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für den Katalog der möglichen Formen des gebundenen Ganztags
 */
public enum FormGebundenerGanztag implements @NotNull CoreType<FormGebundenerGanztagKatalogEintrag, FormGebundenerGanztag> {

	/** Schule ohne Ganztagsbetrieb */
	OHNE_GANZTAG,

	/** Genehmigte Ganztagsschule */
	GANZTAG_GENEHMIGT,

	/** Schule mit Ganztagsbetrieb (ohne Genehmigung) */
	GANZTAG_NICHT_GENEHMIGT,

	/** Schule mit erweitertem Ganztagsbetrieb */
	GANZTAG_ERWEITERT;


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<FormGebundenerGanztagKatalogEintrag, FormGebundenerGanztag> manager) {
		CoreTypeDataManager.putManager(FormGebundenerGanztag.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<FormGebundenerGanztagKatalogEintrag, FormGebundenerGanztag> data() {
		return CoreTypeDataManager.getManager(FormGebundenerGanztag.class);
	}

}
