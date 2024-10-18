package de.svws_nrw.asd.types.schueler;

import de.svws_nrw.asd.data.schueler.UebergangsempfehlungKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die Übergangsempfehlungen von Schülern der Primarstufe
 * an eine weiterführende Schule.
 */
public enum Uebergangsempfehlung implements @NotNull CoreType<UebergangsempfehlungKatalogEintrag, Uebergangsempfehlung> {

	/**
	 * Übergangsempfehlung Hauptschule
	 */
	HAUPTSCHULE,

	/**
	 * Übergangsempfehlung Hauptschule / Realschule (eingeschränkt)
	 */
	HAUPTSCHULE_REALSCHULE,

	/**
	 * Übergangsempfehlung Realschule
	 */
	REALSCHULE,

	/**
	 * Übergangsempfehlung Realschule / Gymnasium (eingeschränkt)
	 */
	REALSCHULE_GYMNASIUM,

	/**
	 * Übergangsempfehlung Gymnasium
	 */
	GYMNASIUM,

	/**
	 * Keine Übergangsempfehlung
	 */
	KEINE;


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<UebergangsempfehlungKatalogEintrag, Uebergangsempfehlung> manager) {
		CoreTypeDataManager.putManager(Uebergangsempfehlung.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<UebergangsempfehlungKatalogEintrag, Uebergangsempfehlung> data() {
		return CoreTypeDataManager.getManager(Uebergangsempfehlung.class);
	}

}
