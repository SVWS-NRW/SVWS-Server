package de.svws_nrw.asd.types.jahrgang;

import de.svws_nrw.asd.data.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die Besuchsjahre in der Schuleingangsphase von Schülern der Primarstufe.
 */
public enum PrimarstufeSchuleingangsphaseBesuchsjahre
		implements CoreType<PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag, PrimarstufeSchuleingangsphaseBesuchsjahre> {

	/** E1: Das erste Besuchsjahr in der Schuleingangsphase */
	E1,

	/** E2: Das zweite Besuchsjahr in der Schuleingangsphase */
	E2,

	/** E3: Das dritte Besuchsjahr in der Schuleingangsphase */
	E3;


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(
			final @NotNull CoreTypeDataManager<PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag, PrimarstufeSchuleingangsphaseBesuchsjahre> manager) {
		CoreTypeDataManager.putManager(PrimarstufeSchuleingangsphaseBesuchsjahre.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag, PrimarstufeSchuleingangsphaseBesuchsjahre> data() {
		return CoreTypeDataManager.getManager(PrimarstufeSchuleingangsphaseBesuchsjahre.class);
	}

}
