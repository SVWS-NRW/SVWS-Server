package de.svws_nrw.asd.types.schueler;

import de.svws_nrw.asd.data.schueler.HochschulabschlussKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die Hochschulabschlüsse.
 */
public enum Hochschulabschluss implements CoreType<HochschulabschlussKatalogEintrag, Hochschulabschluss> {

	/** Ohne Hochschulabschluss */
	OHNE_HOCHSCHULABSCHLUSS,

	/** Bachelor */
	BACHELOR,

	/** Master */
	MASTER,

	/** Promotion */
	PROMOTION,

	/** 1. Staatsexamen (Bachelor-Niveau) */
	STAATSEXAMEN_1_BACHELOR,

	/** 2. Staatsexamen (Bachelor-Niveau) */
	STAATSEXAMEN_2_BACHELOR,

	/** 1. Staatsexamen (Master-Niveau) */
	STAATSEXAMEN_1_MASTER,

	/** 2. Staatsexamen (Master-Niveau) */
	STAATSEXAMEN_2_MASTER,

	/** Diplom (Fachhochschule) */
	DIPLOM_FACHHOCHSCHULE,

	/** Diplom (Universität) */
	DIPLOM_UNIVERSITAET,

	/** Magister */
	MAGISTER,

	/** Sonstiger Hochschulabschluss (Bachelor-Niveau) */
	SONSTIGER_HOCHSCHULABSCHLUSS_BACHELOR,

	/** Sonstiger Hochschulabschluss (Master-Niveau) */
	SONSTIGER_HOCHSCHULABSCHLUSS_MASTER;

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<HochschulabschlussKatalogEintrag, Hochschulabschluss> manager) {
		CoreTypeDataManager.putManager(Hochschulabschluss.class, manager);
	}

	/**
	 * Gibt den Daten-Manager für den Zugriff auf die CoreType-Daten zurück.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<HochschulabschlussKatalogEintrag, Hochschulabschluss> data() {
		return CoreTypeDataManager.getManager(Hochschulabschluss.class);
	}

}
