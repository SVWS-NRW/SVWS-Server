package de.svws_nrw.asd.types.schule;

import java.util.HashMap;
import java.util.Set;

import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.data.schule.OrganisationsformKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die für die amtliche Schulstatistik möglichen
 * Organisationsformen bei allgemeinbildenden Schulformen.
 */
public enum AllgemeinbildendOrganisationsformen implements CoreType<OrganisationsformKatalogEintrag, AllgemeinbildendOrganisationsformen> {

	/** Organisationsform: Nicht zuordenbar (Früherziehung für Hör- und Sehgeschädigte, Ambulante Maßnahmen) */
	NICHT_ZUGEORDNET,

	/** Organisationsform: Halbtagsunterricht */
	HALBTAG,

	/** Organisationsform: Teilnahme am gebundenen Ganztag */
	GANZTAG,

	/** Organisationsform: Teilnahme am erweiterten Ganztag */
	GANZTAG_ERWEITERT,

	/** Organisationsform: Teilnahme am offenen Ganztag */
	GANZTAG_OFFEN;


	/** Die Menge der Schulformen. Diese ist nach der Initialisierung nicht leer. */
	private static final @NotNull HashMap<Long, Set<Schulform>> _mapSchulformenByID = new HashMap<>();


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<OrganisationsformKatalogEintrag, AllgemeinbildendOrganisationsformen> manager) {
		CoreTypeDataManager.putManager(AllgemeinbildendOrganisationsformen.class, manager);
		for (final var ct : data().getWerte())
			for (final var e : ct.historie())
				_mapSchulformenByID.put(e.id, Schulform.data().getWerteByBezeichnerAsNonEmptySet(e.schulformen));
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<OrganisationsformKatalogEintrag, AllgemeinbildendOrganisationsformen> data() {
		return CoreTypeDataManager.getManager(AllgemeinbildendOrganisationsformen.class);
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
				throw new CoreTypeException(
						"Fehler beim prüfen der Schulform. Der Core-Type %s ist nicht korrekt initialisiert.".formatted(this.getClass().getSimpleName()));
			return result.contains(sf);
		}
		return false;
	}

}
