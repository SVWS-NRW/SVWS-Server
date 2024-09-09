package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerEinsatzstatusKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für den Einsatzstatus für
 * Lehrer an der Schule zur Verfügung.
 */
public enum LehrerEinsatzstatus implements @NotNull CoreType<LehrerEinsatzstatusKatalogEintrag, LehrerEinsatzstatus> {

	/** Einsatzstatus: 'Stammschule, ganz oder teilweise auch an anderen Schulen tätig' */
	A,

	/** Einsatzstatus: 'nicht Stammschule, aber auch hier tätig' */
	B,

	/** Einsatzstatus: 'Stammschule, nur hier tätig' */
	DEFAULT;

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<LehrerEinsatzstatusKatalogEintrag, LehrerEinsatzstatus> manager) {
		CoreTypeDataManager.putManager(LehrerEinsatzstatus.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<LehrerEinsatzstatusKatalogEintrag, LehrerEinsatzstatus> data() {
		return CoreTypeDataManager.getManager(LehrerEinsatzstatus.class);
	}

}