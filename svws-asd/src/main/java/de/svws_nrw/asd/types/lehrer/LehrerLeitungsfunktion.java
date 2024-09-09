package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerLeitungsfunktionKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Core-Type enthält die unterschiedlichen Leitungsfunktionen von Lehrern.
 */
public enum LehrerLeitungsfunktion implements @NotNull CoreType<@NotNull LehrerLeitungsfunktionKatalogEintrag, @NotNull LehrerLeitungsfunktion> {

	/** Schulleitung */
	SL,

	/** Stellvertretende Schulleitung */
	SL_STV;

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<LehrerLeitungsfunktionKatalogEintrag, LehrerLeitungsfunktion> manager) {
		CoreTypeDataManager.putManager(LehrerLeitungsfunktion.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<LehrerLeitungsfunktionKatalogEintrag, LehrerLeitungsfunktion> data() {
		return CoreTypeDataManager.getManager(LehrerLeitungsfunktion.class);
	}

}
