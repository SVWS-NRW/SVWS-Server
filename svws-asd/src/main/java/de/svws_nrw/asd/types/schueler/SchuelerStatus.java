package de.svws_nrw.asd.types.schueler;

import de.svws_nrw.asd.data.schueler.SchuelerStatusKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;


/**
 * Der Core-Type zu den möglichen Werten beim Schüler-Status
 */
public enum SchuelerStatus implements @NotNull CoreType<SchuelerStatusKatalogEintrag, SchuelerStatus> {

	/** Status Neuaufnahme mit dem Wert 0 */
	NEUAUFNAHME,

	/** Status Warteliste mit dem Wert 1 */
	WARTELISTE,

	/** Status Aktiv mit dem Wert 2 */
	AKTIV,

	/** Status Beurlaubt mit dem Wert 3 */
	BEURLAUBT,

	/** Status Extern mit dem Wert 6 */
	EXTERN,

	/** Status Abschluss mit dem Wert 8 */
	ABSCHLUSS,

	/** Status Abgänger mit dem Wert 9 */
	ABGANG,

	/** Status Abgänger mit dem Wert 10 */
	EHEMALIGE;

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<SchuelerStatusKatalogEintrag, SchuelerStatus> manager) {
		CoreTypeDataManager.putManager(SchuelerStatus.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<SchuelerStatusKatalogEintrag, SchuelerStatus> data() {
		return CoreTypeDataManager.getManager(SchuelerStatus.class);
	}

}
