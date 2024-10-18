package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.data.schule.OrganisationsformKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die Arten von Organisationsformen am Weiterbildungskolleg.
 */
public enum WeiterbildungskollegOrganisationsformen implements CoreType<OrganisationsformKatalogEintrag, WeiterbildungskollegOrganisationsformen> {

	/** Organisationsform: Teilbeleger */
	TEILZEIT,

	/** Organisationsform: Vollbeleger */
	VOLLZEIT;


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<OrganisationsformKatalogEintrag, WeiterbildungskollegOrganisationsformen> manager) {
		CoreTypeDataManager.putManager(WeiterbildungskollegOrganisationsformen.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<OrganisationsformKatalogEintrag, WeiterbildungskollegOrganisationsformen> data() {
		return CoreTypeDataManager.getManager(WeiterbildungskollegOrganisationsformen.class);
	}


	/**
	 * Prüft, ob die Schulform bei diesem Core-Type-Wert in dem angegeben Schuljahr zulässig ist oder nicht.
	 *
	 * @param schuljahr   das zu prüfende Schuljahr
	 * @param sf          die Schulform, auf die geprüft wird
	 *
	 * @return true, falls die Schulform zulässig ist, und ansonsten false
	 */
	public boolean hatSchulform(final int schuljahr, final @NotNull Schulform sf) {
		return data().hatSchulform(schuljahr, sf, this);
	}

}
