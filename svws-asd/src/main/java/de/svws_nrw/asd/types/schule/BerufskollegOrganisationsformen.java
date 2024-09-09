package de.svws_nrw.asd.types.schule;

import java.util.HashMap;
import java.util.Set;

import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.data.schule.OrganisationsformKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die Arten von Organisationsformen am Berufskolleg.
 */
public enum BerufskollegOrganisationsformen implements CoreType<OrganisationsformKatalogEintrag, BerufskollegOrganisationsformen> {

	/** Organisationsform: Teilzeitunterricht (außerhalb der TZ-Berufsschule) */
	TEILZEIT,

	/** Organisationsform: Teilzeitunterricht ohne Blockunterricht (Normalklasse) TZ-Berufsschule */
	TEILZEIT_NORMALKLASSE,

	/** Organisationsform: Teilzeitunterricht ohne Blockunterricht (Bezirksfachklasse) TZ-Berufsschule */
	TEILZEIT_BEZIRKSFACHKLASSE,

	/** Organisationsform: Teilzeitunterricht ohne Blockunterricht (Landesfachklasse) TZ-Berufsschule */
	TEILZEIT_LANDESFACHKLASSE,

	/** Organisationsform: Vollzeitunterricht */
	VOLLZEIT,

	/** Organisationsform: Blockunterricht z.Zt. im Unterricht (Normalklasse) im dualen System */
	DUAL_BLOCK_IM_UNTERRICHT,

	/** Organisationsform: Blockunterricht z.Zt. nicht im Unterricht (Normalklasse) im dualen System */
	DUAL_BLOCK_NICHT_IM_UNTERRICHT,

	/** Organisationsform: Block- und Teilzeitunterr. z.Zt. im Block- oder TZ-Unterricht (Normalklasse) im dualen System */
	DUAL_BLOCK_TEILZEIT_IM_UNTERRICHT,

	/** Organisationsform: Ganztagsunterricht (Normalklasse) im dualen System */
	DUAL_GANZTAG,

	/** Organisationsform: Blockunterricht z.Zt. im Unterricht (Bezirksfachklasse) im dualen System */
	DUAL_BLOCK_IM_UNTERRICHT_BEZIRKSFACHKLASSE,

	/** Organisationsform: Blockunterricht z.Zt. nicht im Unterricht (Bezirksfachklasse) im dualen System */
	DUAL_BLOCK_NICHT_IM_UNTERRICHT_BEZIRKSFACHKLASSE,

	/** Organisationsform: Block- und Teilzeitunterr. z.Zt. im Block- oder TZ-Unterricht (Bezirksfachklasse) im dualen System */
	DUAL_BLOCK_TEILZEIT_IM_UNTERRICHT_BEZIRKSFACHKLASSE,

	/** Organisationsform: Ganztagsunterricht (Bezirksfachklasse) im dualen System */
	DUAL_GANZTAG_BEZIRKSFACHKLASSE,

	/** Organisationsform: Blockunterricht z.Zt. im Unterricht (Landesfachklasse) im dualen System */
	DUAL_BLOCK_IM_UNTERRICHT_LANDESFACHKLASSE,

	/** Organisationsform: Blockunterricht z.Zt. nicht im Unterricht (Landesfachklasse) im dualen System */
	DUAL_BLOCK_NICHT_IM_UNTERRICHT_LANDESFACHKLASSE,

	/** Organisationsform: Block- und Teilzeitunterr. z.Zt. im Block- oder TZ-Unterricht (Landesfachklasse) im dualen System */
	DUAL_BLOCK_TEILZEIT_IM_UNTERRICHT_LANDESFACHKLASSE,

	/** Organisationsform: Ganztagsunterricht (Landesfachklasse) im dualen System */
	DUAL_GANZTAG_LANDESFACHKLASSE;


	/** Die Menge der Schulformen. Diese ist nach der Initialisierung nicht leer. */
	private static final @NotNull HashMap<Long, Set<Schulform>> _mapSchulformenByID = new HashMap<>();


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<OrganisationsformKatalogEintrag, BerufskollegOrganisationsformen> manager) {
		CoreTypeDataManager.putManager(BerufskollegOrganisationsformen.class, manager);
		for (final var ct : data().getWerte())
			for (final var e : ct.historie())
				_mapSchulformenByID.put(e.id, Schulform.data().getWerteByBezeichnerAsNonEmptySet(e.schulformen));
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<OrganisationsformKatalogEintrag, BerufskollegOrganisationsformen> data() {
		return CoreTypeDataManager.getManager(BerufskollegOrganisationsformen.class);
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
