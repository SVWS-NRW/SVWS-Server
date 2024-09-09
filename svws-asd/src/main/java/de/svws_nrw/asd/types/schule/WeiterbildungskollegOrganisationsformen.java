package de.svws_nrw.asd.types.schule;

import java.util.HashMap;
import java.util.Set;

import de.svws_nrw.asd.data.CoreTypeException;
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


	/** Die Menge der Schulformen. Diese ist nach der Initialisierung nicht leer. */
	private static final @NotNull HashMap<Long, Set<Schulform>> _mapSchulformenByID = new HashMap<>();


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<OrganisationsformKatalogEintrag, WeiterbildungskollegOrganisationsformen> manager) {
		CoreTypeDataManager.putManager(WeiterbildungskollegOrganisationsformen.class, manager);
		for (final var ct : data().getWerte())
			for (final var e : ct.historie())
				_mapSchulformenByID.put(e.id, Schulform.data().getWerteByBezeichnerAsNonEmptySet(e.schulformen));
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
		final OrganisationsformKatalogEintrag ke = this.daten(schuljahr);
		if (ke != null) {
			final Set<Schulform> result = _mapSchulformenByID.get(ke.id);
			if (result == null)
				throw new CoreTypeException("Fehler beim prüfen der Schulform. Der Core-Type %s ist nicht korrekt initialisiert.".formatted(this.getClass().getSimpleName()));
			return result.contains(sf);
		}
		return false;
	}

}
